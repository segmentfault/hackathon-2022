package driver

import (
	"fmt"
	"path/filepath"
	"strconv"

	"github.com/container-storage-interface/spec/lib/go/csi"
	"github.com/gophercloud/gophercloud/openstack/blockstorage/v3/snapshots"
	"github.com/kungze/cinder-metal-csi/pkg/openstack"
	"golang.org/x/net/context"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"google.golang.org/protobuf/types/known/timestamppb"
	"k8s.io/klog/v2"

	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/client-go/kubernetes"
	"k8s.io/client-go/rest"

	"k8s.io/client-go/tools/clientcmd"
	"k8s.io/client-go/util/homedir"
)

const cinderVolumeType = "cinderVolumeType"

type ControllerServer struct {
	driver *Driver
	cloud  openstack.IOpenstack
	client kubernetes.Clientset
}

func (c *ControllerServer) CreateVolume(ctx context.Context, req *csi.CreateVolumeRequest) (*csi.CreateVolumeResponse, error) {
	name := req.GetName()
	if name == "" {
		return nil, status.Error(codes.NotFound, "CreateVolume: The volume name not exists")
	}
	size := RoundOffBytes(req.GetCapacityRange().GetRequiredBytes())
	if size < 1 {
		return nil, status.Error(codes.InvalidArgument, "CreateVolume: The cinder volume size not less than 1")
	}
	volumeType := req.GetParameters()[cinderVolumeType]
	if volumeType == "" {
		return nil, status.Error(codes.InvalidArgument, "CreateVolume: The volume type must")
	}
	// Verify that a volume with the same name exists
	vol, err := c.cloud.GetVolumeByName(name)
	if err != nil {
		return nil, status.Error(codes.Internal, fmt.Sprintf("CreateVolume: Failed to get volumes: %v", err))
	}
	if len(vol) == 1 {
		if size != vol[0].Size {
			return nil, status.Errorf(codes.AlreadyExists, "CreateVolume: Volume already exists with same name and different capacity")
		}
		return &csi.CreateVolumeResponse{
			Volume: &csi.Volume{
				VolumeId:      vol[0].ID,
				CapacityBytes: int64(vol[0].Size * 1024 * 1024 * 1024),
			},
		}, nil
	} else if len(vol) > 1 {
		return nil, status.Error(codes.Internal, "CreateVolume: Multiple volumes reported by cinder with same name")
	}
	var snapshotID string
	var sourceVolID string

	snapshotID = req.GetVolumeContentSource().GetSnapshot().GetSnapshotId()
	if snapshotID != "" {
		_, err := c.cloud.GetSnapshotByID(snapshotID)
		if err != nil {
			return nil, status.Errorf(codes.Internal, "Failed to retrieve the snapshot %s: %v", snapshotID, err)
		}
	}

	sourceVolID = req.GetVolumeContentSource().GetVolume().GetVolumeId()
	if sourceVolID != "" {
		_, err := c.cloud.GetVolumeByID(sourceVolID)
		if err != nil {
			return nil, status.Errorf(codes.Internal, "Failed to retrieve the source volume %s: %v", sourceVolID, err)
		}
	}
	zone, err := c.cloud.GetAvailability()
	if err != nil {
		return nil, status.Error(codes.Internal, fmt.Sprintf("CreateVolume: Get volume Availability zone failed, %v", err))
	}
	volume, err := c.cloud.CreateVolume(name, zone, volumeType, snapshotID, sourceVolID, size)
	if err != nil {
		return nil, status.Error(codes.Internal, fmt.Sprintf("CreateVolume: Request openstack create volume failed, %v", err))
	}
	klog.Infof("CreateVolume: Create the volume %s success!", volume.ID)
	return &csi.CreateVolumeResponse{
		Volume: &csi.Volume{
			VolumeId:      volume.ID,
			CapacityBytes: int64(volume.Size) * 1024 * 1024 * 1024,
		},
	}, nil
}

func (c *ControllerServer) DeleteVolume(ctx context.Context, req *csi.DeleteVolumeRequest) (*csi.DeleteVolumeResponse, error) {
	var err error
	volumeID := req.GetVolumeId()
	if volumeID == "" {
		return nil, status.Error(codes.NotFound, "DeleteVolume: The volume ID not exists!!")
	}
	err = c.cloud.DetachVolume(volumeID)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("DeleteVolume: Detach the volume %s failed, %v", volumeID, err))
	}
	err = c.cloud.DeleteVolume(volumeID)
	if err != nil {
		return nil, status.Error(codes.Internal, fmt.Sprintf("DeleteVolume: Delete the volume %s failed, %v", volumeID, err))
	}
	klog.Infof("DeleteVolume: Delete the volume %s success", volumeID)
	return &csi.DeleteVolumeResponse{}, nil
}

func (c *ControllerServer) ControllerPublishVolume(ctx context.Context, req *csi.ControllerPublishVolumeRequest) (*csi.ControllerPublishVolumeResponse, error) {
	volumeID := req.GetVolumeId()
	if volumeID == "" {
		return nil, status.Error(codes.InvalidArgument, "ControllerPublishVolume: The volume ID is required.")
	}
	nodeObjs, err := c.client.StorageV1().CSINodes().List(context.TODO(), metav1.ListOptions{})
	if err != nil {
		return nil, status.Error(
			codes.Internal,
			fmt.Sprintf("ControllerPublishVolume: Encounter err when retrieve the informations of CSINode %s. Error: %s", req.GetNodeId(), err.Error()))
	}
	for _, nodeObj := range nodeObjs.Items {
		for _, driver := range nodeObj.Spec.Drivers {
			if driver.NodeID == req.GetNodeId() {
				attachment, err := c.cloud.CreateVolumeAttachment(volumeID, string(nodeObj.GetUID()))
				if err != nil {
					return nil, status.Error(codes.Internal, fmt.Sprintf("ControllerPublishVolume: Failed to create attachment: %v", err))
				}
				return &csi.ControllerPublishVolumeResponse{
					PublishContext: map[string]string{"attachmentID": attachment.ID},
				}, nil
			}
		}
	}
	return nil, status.Error(codes.NotFound, fmt.Sprintf("ControllerPublishVolume: The CSINode %s not fount.", req.GetNodeId()))
}

func (c *ControllerServer) ControllerUnpublishVolume(ctx context.Context, req *csi.ControllerUnpublishVolumeRequest) (*csi.ControllerUnpublishVolumeResponse, error) {
	volumeID := req.GetVolumeId()
	if volumeID == "" {
		return nil, status.Error(codes.NotFound, "ControllerPublishVolume: The volume ID not exists!!")
	}
	err := c.cloud.DeleteVolumeAttachment(volumeID)
	if err != nil {
		return nil, err
	}
	return &csi.ControllerUnpublishVolumeResponse{}, nil
}

func (c *ControllerServer) ValidateVolumeCapabilities(ctx context.Context, request *csi.ValidateVolumeCapabilitiesRequest) (*csi.ValidateVolumeCapabilitiesResponse, error) {
	return nil, status.Error(codes.Unimplemented, "ValidateVolumeCapabilities is not yet implemented")
}

func (c *ControllerServer) ListVolumes(ctx context.Context, req *csi.ListVolumesRequest) (*csi.ListVolumesResponse, error) {
	maxEntry := req.GetMaxEntries()
	marker := req.GetStartingToken()
	if maxEntry == 0 {
		return nil, status.Error(codes.InvalidArgument, "ListVolumes: The max entry not ")
	}
	if marker == "" {
		return nil, status.Error(codes.InvalidArgument, "ListVolumes: The startingToken must provider")
	}
	volList, nextToken, err := c.cloud.ListVolume(maxEntry, marker)
	if err != nil {
		return nil, status.Errorf(codes.Internal, "ListVolumes: Get the volume list failed, %v", err)
	}
	var vEntries []*csi.ListVolumesResponse_Entry
	for _, v := range volList {
		ventry := &csi.ListVolumesResponse_Entry{
			Volume: &csi.Volume{
				VolumeId:      v.ID,
				CapacityBytes: int64(v.Size) * 1024 * 1024 * 1024,
			},
		}
		status := csi.ListVolumesResponse_VolumeStatus{}
		for _, attach := range v.Attachments {
			status.PublishedNodeIds = append(status.PublishedNodeIds, attach.AttachmentID)
		}
		vEntries = append(vEntries, ventry)
	}
	return &csi.ListVolumesResponse{
		Entries:   vEntries,
		NextToken: nextToken,
	}, nil
}

func (c *ControllerServer) GetCapacity(ctx context.Context, req *csi.GetCapacityRequest) (*csi.GetCapacityResponse, error) {
	return nil, status.Error(codes.Unimplemented, "GetCapacity is not yet implemented")
}

func (c *ControllerServer) ControllerGetCapabilities(ctx context.Context, req *csi.ControllerGetCapabilitiesRequest) (*csi.ControllerGetCapabilitiesResponse, error) {
	return &csi.ControllerGetCapabilitiesResponse{
		Capabilities: c.driver.cap,
	}, nil
}

func (c *ControllerServer) CreateSnapshot(ctx context.Context, req *csi.CreateSnapshotRequest) (*csi.CreateSnapshotResponse, error) {
	sourceVolumeId := req.GetSourceVolumeId()
	if sourceVolumeId == "" {
		return nil, status.Error(codes.InvalidArgument, "CreateSnapshot: The source volume id is required fields")
	}
	name := req.GetName()
	if name == "" {
		return nil, status.Error(codes.InvalidArgument, "CreateSnapshot: The create snapshot name is none")
	}

	// Verify a snapshot with the provided name doesn't already exist for this tenant
	filter := map[string]string{}
	filter["name"] = name
	snapshot, _, err := c.cloud.ListSnapshot(filter)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("CreateSnapshot: Get the snapshots info failed, %v", err))
	}
	var snap *snapshots.Snapshot
	if len(snapshot) == 1 {
		snap = &snapshot[0]
		if snap.VolumeID != sourceVolumeId {
			return nil, status.Error(codes.AlreadyExists, "CreateSnapshot: snapshot with given name already exists, with different source volume ID")
		}
	} else if len(snapshot) > 1 {
		return nil, status.Error(codes.Internal, "CreateSnapshot: Multiple snapshots reported by Cinder with same name")
	} else {
		properties := map[string]string{}
		for _, mKey := range []string{"csi.storage.k8s.io/volumesnapshot/name", "csi.storage.k8s.io/volumesnapshot/namespace", "csi.storage.k8s.io/volumesnapshotcontent/name"} {
			if v, ok := req.Parameters[mKey]; ok {
				properties[mKey] = v
			}
		}
		snap, err = c.cloud.CreateSnapShot(name, sourceVolumeId)
		if err != nil {
			return nil, status.Errorf(codes.Internal, "CreateSnapshot: Create snapshot volume %s failed, %v", name, err)
		}
	}
	klog.Infof("CreateSnapshot: Create snapshot volume %s success", snap.ID)
	return &csi.CreateSnapshotResponse{
		Snapshot: &csi.Snapshot{
			SnapshotId:     snap.ID,
			SizeBytes:      int64(snap.Size) * 1024 * 1024 * 1024,
			CreationTime:   timestamppb.New(snap.CreatedAt),
			ReadyToUse:     true,
			SourceVolumeId: snap.VolumeID,
		},
	}, nil
}

func (c *ControllerServer) DeleteSnapshot(ctx context.Context, req *csi.DeleteSnapshotRequest) (*csi.DeleteSnapshotResponse, error) {
	snapshotID := req.GetSnapshotId()
	if snapshotID == "" {
		return nil, status.Error(codes.InvalidArgument, "DeleteSnapshot: The snapshot volume ID is required fields")
	}
	err := c.cloud.DeleteSnapshot(snapshotID)
	if err != nil {
		return nil, status.Errorf(codes.Internal, "DeleteSnapshot: Delete snapshot volume id %s is failed, %v", snapshotID, err)
	}
	klog.Infof("DeleteSnapshot: Delete snapshot volume %s success", snapshotID)
	return &csi.DeleteSnapshotResponse{}, nil
}

func (c *ControllerServer) ListSnapshots(ctx context.Context, req *csi.ListSnapshotsRequest) (*csi.ListSnapshotsResponse, error) {
	snapshotID := req.GetSnapshotId()
	if snapshotID != "" {
		snapshot, err := c.cloud.GetSnapshotByID(snapshotID)
		if err != nil {
			return nil, status.Errorf(codes.Internal, fmt.Sprintf("ListSnapshots: Get snapshot volume info failed, %v", err))
		}
		var vEntries []*csi.ListSnapshotsResponse_Entry
		entry := &csi.ListSnapshotsResponse_Entry{
			Snapshot: &csi.Snapshot{
				SnapshotId:     snapshot.ID,
				SizeBytes:      int64(snapshot.Size) * 1024 * 1024 * 1024,
				SourceVolumeId: snapshot.VolumeID,
				CreationTime:   timestamppb.New(snapshot.CreatedAt),
			},
		}
		vEntries = append(vEntries, entry)
		return &csi.ListSnapshotsResponse{
			Entries: vEntries,
		}, nil
	}
	filter := map[string]string{}
	volumeID := req.GetSourceVolumeId()
	if volumeID != "" {
		filter["volumeID"] = volumeID
	} else if req.GetMaxEntries() > 0 && req.GetStartingToken() != "" {
		filter["limit"] = strconv.Itoa(int(req.MaxEntries))
		filter["marker"] = req.StartingToken
	}
	filter["status"] = "available"
	snapshot, nextToken, err := c.cloud.ListSnapshot(filter)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("ListSnapshots: Get snapshot volume list failed, %v", err))
	}
	var vEntries []*csi.ListSnapshotsResponse_Entry
	for _, snap := range snapshot {
		entry := &csi.ListSnapshotsResponse_Entry{
			Snapshot: &csi.Snapshot{
				SnapshotId:     snap.ID,
				SizeBytes:      int64(snap.Size) * 1024 * 1024 * 1024,
				SourceVolumeId: snap.VolumeID,
				CreationTime:   timestamppb.New(snap.CreatedAt),
			},
		}
		vEntries = append(vEntries, entry)
	}
	return &csi.ListSnapshotsResponse{
		Entries:   vEntries,
		NextToken: nextToken,
	}, nil
}

func (c *ControllerServer) ControllerExpandVolume(ctx context.Context, req *csi.ControllerExpandVolumeRequest) (*csi.ControllerExpandVolumeResponse, error) {
	volumeID := req.GetVolumeId()
	if volumeID == "" {
		return nil, status.Error(codes.InvalidArgument, "ControllerExpandVolume: The volume id must provider")
	}
	sizeBytes := req.GetCapacityRange().GetRequiredBytes()
	if sizeBytes < 0 {
		return nil, status.Error(codes.InvalidArgument, "ControllerExpandVolume: The volume size must greater than 0")
	}
	maxSize := req.GetCapacityRange().GetLimitBytes()
	if maxSize > 0 && maxSize < sizeBytes {
		return nil, status.Error(codes.OutOfRange, "ControllerExpandVolume: The volume size exceeds the limit specified")
	}
	vol, err := c.cloud.GetVolumeByID(volumeID)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("ControllerExpandVolume: Get volume %s info failed, %v", volumeID, err))
	}
	if vol == nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("ControllerExpandVolume: Volume %s could not found.", volumeID))
	}
	size := RoundOffBytes(sizeBytes)
	if size < vol.Size {
		klog.Infof(fmt.Sprintf("ControllerExpandVolume: Request extend cinder volume size %d must greater exists volume size %d", size, vol.Size))
		return &csi.ControllerExpandVolumeResponse{
			CapacityBytes:         int64(vol.Size) * 1024 * 1024,
			NodeExpansionRequired: false,
		}, nil
	}
	err = c.cloud.ExpandVolume(volumeID, vol.Status, size)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("ControllerExpandVolume: Request expand volume %s failed, %v", vol.ID, err))
	}
	klog.Infof("ControllerExpandVolume: Expand the volume %s success", volumeID)
	return &csi.ControllerExpandVolumeResponse{
		CapacityBytes:         int64(size) * 1024 * 1024 * 1024,
		NodeExpansionRequired: true,
	}, nil
}

func (c *ControllerServer) ControllerGetVolume(ctx context.Context, req *csi.ControllerGetVolumeRequest) (*csi.ControllerGetVolumeResponse, error) {
	volumeID := req.GetVolumeId()
	if volumeID == "" {
		return nil, status.Error(codes.InvalidArgument, "ControllerGetVolume: The volume id must exists")
	}
	vol, err := c.cloud.GetVolumeByID(volumeID)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("ControllerGetVolume: Get volume %s info failed, %v", volumeID, err))
	}
	if vol == nil {
		return nil, status.Errorf(codes.NotFound, fmt.Sprintf("ControllerGetVolume: Cinder volume %s not found", volumeID))
	}
	var status *csi.ControllerGetVolumeResponse_VolumeStatus
	for _, v := range vol.Attachments {
		status.PublishedNodeIds = append(status.PublishedNodeIds, v.HostName)
	}

	return &csi.ControllerGetVolumeResponse{
		Volume: &csi.Volume{
			VolumeId:      vol.ID,
			CapacityBytes: int64(vol.Size) * 1024 * 1024 * 1024,
		},
		Status: status,
	}, nil
}

func NewControllerServer(d *Driver, cloud openstack.IOpenstack) csi.ControllerServer {
	var config *rest.Config
	config, err := rest.InClusterConfig()
	if err != nil {
		config, err = clientcmd.BuildConfigFromFlags("", filepath.Join(homedir.HomeDir(), ".kube", "config"))
		if err != nil {
			panic(err.Error())
		}
	}
	clientset, err := kubernetes.NewForConfig(config)
	if err != nil {
		panic(err.Error())
	}
	return &ControllerServer{
		driver: d,
		cloud:  cloud,
		client: *clientset,
	}
}
