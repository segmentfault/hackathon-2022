package driver

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"

	"github.com/container-storage-interface/spec/lib/go/csi"
	"github.com/kungze/cinder-metal-csi/pkg/brick"
	"github.com/kungze/cinder-metal-csi/pkg/mount"
	"github.com/kungze/cinder-metal-csi/pkg/openstack"
	"golang.org/x/net/context"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"k8s.io/klog/v2"
	mountutil "k8s.io/mount-utils"
	utilpath "k8s.io/utils/path"
)

const (
	FSTypeExt4 = "ext4"
	FSTypeXfs  = "xfs"
)

var defaultExtraAuth = map[string]string{}

type nodeServer struct {
	driver *Driver
	cloud  openstack.IOpenstack
	mount  mount.IMount
}

func (n *nodeServer) NodeStageVolume(ctx context.Context, req *csi.NodeStageVolumeRequest) (*csi.NodeStageVolumeResponse, error) {
	//req.GetSecrets()
	volumeID := req.GetVolumeId()
	if volumeID == "" {
		return nil, status.Error(codes.InvalidArgument, "NodeStageVolume: The volume id must provider")
	}
	sourcePath := req.GetStagingTargetPath()
	if sourcePath == "" {
		return nil, status.Error(codes.InvalidArgument, "NodeStageVolume: The volume source path must provider")
	}
	volCap := req.GetVolumeCapability()
	if volCap == nil {
		return nil, status.Error(codes.InvalidArgument, "NodeStageVolume: The volume capability must provider")
	}
	isBlock := volCap.GetBlock()
	if isBlock != nil {
		klog.V(3).Infof("NodeStageVolume: The volume %s type is block", volumeID)
		return &csi.NodeStageVolumeResponse{}, nil
	}
	vol, err := n.cloud.GetVolumeByID(volumeID)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeStageVolume: Get volume info failed %v", err))
	}
	if vol == nil {
		return nil, status.Errorf(codes.NotFound, fmt.Sprintf("NodeStageVolume: The cinder volume %s not found.", volumeID))
	}
	attachmentID := req.GetPublishContext()["attachmentID"]
	attach, err := n.cloud.GetVolumeAttachment(attachmentID)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeStageVolume: Get volume attachment info failed. Error: %v", err))
	}
	if attach == nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeStageVolume: Volume %s attachment %s could not be found.", volumeID, attachmentID))
	}
	conn, err := brick.NewConnector(attach.ConnectionInfo, req.GetSecrets())
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeStageVolume: Can not initial connector. Error: %s", err.Error()))
	}
	result, err := conn.ConnectVolume()
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeStageVolume: Connector the volume %s failed. Error: %v", volumeID, err))
	}
	klog.V(3).Infof("NodeStageVolume: Connector the volume %s success!", volumeID)
	devicePath := result["path"]

	mountInfo := volCap.GetMount()
	fsType := mountInfo.GetFsType()
	if fsType == "" {
		fsType = FSTypeExt4
	}
	mountOptions := mountInfo.GetMountFlags()
	options := collectMountOptions(fsType, mountOptions)
	// View the staging target path whether exist
	isExists, err := mountutil.PathExists(sourcePath)
	if err != nil {
		return nil, status.Error(codes.Internal, fmt.Sprintf("NodeStageVolume: Get volume path %s failed, %v", sourcePath, err))
	}
	klog.Infof("NodeStageVolume: Get the staging path isExists result, %v", isExists)
	if !isExists {
		// Create staging target path dir
		err := n.mount.MakeDir(sourcePath)
		if err != nil {
			return nil, status.Error(codes.Internal, fmt.Sprintf("NodeStageVolume: Create volume dir %s failed, %v", sourcePath, err))
		}
	}
	klog.Infof("NodeStageVolume: Start exec format and mount volume %s, the volume staging path is %s, the volume device path is %s", volumeID, sourcePath, devicePath)
	// format and mount the volume
	err = n.mount.NodeFormatAndMount(devicePath, sourcePath, fsType, options)
	if err != nil {
		return nil, status.Error(codes.Internal, fmt.Sprintf("NodeStageVolume: Format and mount the volume %s failed, %v", volumeID, err))
	}
	klog.Infof("NodeStageVolume: Format and Mount the volume %s success", volumeID)

	// If the volume is from snapshot or clone volume, decide the volume whether resize
	if vol.SourceVolID != "" || vol.SnapshotID != "" {
		resize := n.mount.NewResizeFs()
		isResize, err := resize.NeedResize(devicePath, sourcePath)
		if err != nil {
			return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeStageVolume: Get the volume %s whether resize failed %v", volumeID, err))
		}
		if isResize {
			klog.V(3).Infof("NodeStageVolume: Resizing the volume %s created from a snapshot/volume", volumeID)
			_, err := resize.Resize(devicePath, sourcePath)
			if err != nil {
				return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeStageVolume: Resize the source or snapshot volume file system failed %v", err))
			}
		}
	}
	return &csi.NodeStageVolumeResponse{}, nil
}

func (n *nodeServer) NodeUnstageVolume(ctx context.Context, req *csi.NodeUnstageVolumeRequest) (*csi.NodeUnstageVolumeResponse, error) {
	volumeID := req.GetVolumeId()
	if volumeID == "" {
		return nil, status.Error(codes.InvalidArgument, "NodeUnstageVolume: The volume id must provider")
	}
	sourcePath := req.GetStagingTargetPath()
	if sourcePath == "" {
		return nil, status.Error(codes.InvalidArgument, "NodeUnstageVolume: The volume staging target path must provider")
	}
	vol, err := n.cloud.GetVolumeByID(volumeID)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeUnstageVolume: Get volume info failed %v", err))
	}
	if vol == nil {
		klog.Warningf("The volume %s not fount.", volumeID)
		return &csi.NodeUnstageVolumeResponse{}, nil
	}
	klog.Infof("NodeUnstageVolume: umount volume %s, volume path is %s", volumeID, sourcePath)
	err = n.mount.UnmountPath(sourcePath)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeUnstageVolume: Unmount the volume path %s failed, %v", sourcePath, err))
	}
	attach, err := n.cloud.GetAttachmentByVolumeID(volumeID)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeUnstageVolume: Get volume attachment info failed. Error: %v", err))
	}
	if attach == nil {
		return &csi.NodeUnstageVolumeResponse{}, nil
	}
	conn, err := brick.NewConnector(attach.ConnectionInfo, defaultExtraAuth)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeUnstageVolume: Can not initial connector. Error: %s", err.Error()))
	}
	err = conn.DisConnectVolume()
	klog.Infof("NodeUnstageVolume: Disconnector the volume %s", volumeID)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeUnstageVolume: Disonnector the volume %s failed, %v", volumeID, err))
	}
	return &csi.NodeUnstageVolumeResponse{}, nil
}

func (n *nodeServer) NodePublishVolume(ctx context.Context, req *csi.NodePublishVolumeRequest) (*csi.NodePublishVolumeResponse, error) {
	volumeID := req.GetVolumeId()
	if volumeID == "" {
		return nil, status.Error(codes.InvalidArgument, "NodePublishVolume: The volume id must provider")
	}
	sourcePath := req.GetStagingTargetPath()
	if sourcePath == "" {
		return nil, status.Error(codes.InvalidArgument, "NodePublishVolume: The volume staging target path must provider")
	}
	targetPath := req.GetTargetPath()
	if targetPath == "" {
		return nil, status.Error(codes.InvalidArgument, "NodePublishVolume: The volume target path must provider")
	}
	volCap := req.GetVolumeCapability()
	if volCap == nil {
		return nil, status.Error(codes.InvalidArgument, "NodePublishVolume: The volume capability must provider")
	}
	mountInfo := volCap.GetMount()
	fsType := mountInfo.FsType
	if fsType == "" {
		fsType = FSTypeExt4
	}
	mountOptions := []string{"bind"}
	if req.GetReadonly() {
		mountOptions = append(mountOptions, "ro")
	} else {
		mountOptions = append(mountOptions, "rw")
	}
	isBlock := volCap.GetBlock()
	if isBlock != nil {
		res, err := n.nodePublishBlockVolume(req, n, mountOptions)
		return res, err
	}
	isExists, err := mountutil.PathExists(targetPath)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodePublishVolume: Check volume path %s exists failed %v", targetPath, err))
	}
	if !isExists {
		err := n.mount.MakeDir(targetPath)
		if err != nil {
			return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodePublishVolume: Create Dir %s failed: %v", targetPath, err))
		}
		klog.Infof("NodePublishVolume: Exec mount volume %s, the volume staging path is %s, the volume target path is %s", volumeID, sourcePath, targetPath)
		err = n.mount.Mount(sourcePath, targetPath, fsType, mountOptions)
		if err != nil {
			return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodePublishVolume: Exec format and mount volume failed %v", err))
		}
	}
	err = n.cloud.VolumeAttachmentComplete(req.GetPublishContext()["attachmentID"])
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodePublishVolume: cinder volume attachment complete failed. Error: %s", err.Error()))
	}
	return &csi.NodePublishVolumeResponse{}, nil
}

func (n *nodeServer) NodeUnpublishVolume(ctx context.Context, req *csi.NodeUnpublishVolumeRequest) (*csi.NodeUnpublishVolumeResponse, error) {
	volumeID := req.GetVolumeId()
	if volumeID == "" {
		return nil, status.Error(codes.InvalidArgument, "NodeUnpublishVolume: The volume id must provider")
	}
	targetPath := req.GetTargetPath()
	if targetPath == "" {
		return nil, status.Error(codes.InvalidArgument, "NodeUnpublishVolume: The volume target path must provider")
	}
	vol, err := n.cloud.GetVolumeByID(volumeID)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeUnpublishVolume: Get volume info failed %v", err))
	}
	if vol == nil {
		return nil, status.Errorf(codes.NotFound, fmt.Sprintf("NodeUnpublishVolume: The cinder volume %s not found", volumeID))
	}
	klog.Infof("NodeUnpublishVolume: Umount the volume %s, volume path is %s", volumeID, targetPath)
	err = n.mount.UnmountPath(targetPath)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeUnpublishVolume: Unmount the volume path %s failed, %v", targetPath, err))
	}
	return &csi.NodeUnpublishVolumeResponse{}, nil
}

func (n *nodeServer) NodeGetVolumeStats(ctx context.Context, req *csi.NodeGetVolumeStatsRequest) (*csi.NodeGetVolumeStatsResponse, error) {
	volumeID := req.GetVolumeId()
	if volumeID == "" {
		return nil, status.Error(codes.InvalidArgument, "NodeGetVolumeStats: The volume id must provider")
	}
	volumePath := req.GetVolumePath()
	if volumePath == "" {
		return nil, status.Error(codes.InvalidArgument, "NodeGetVolumeStats: The volume path must provider")
	}
	exists, err := mountutil.PathExists(volumePath)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeGetVolumeStats: Get the volume path %s exist result failed, %v", volumePath, err))
	}
	if !exists {
		return nil, status.Errorf(codes.NotFound, fmt.Sprintf("NodeGetVolumeStats: The volume path %s not exists, can not view the volume stats", volumePath))
	}
	stats, err := n.mount.GetDeviceStats(volumePath)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeGetVolumeStats: Get the volume path %s stats failed, %v", volumePath, err))
	}
	if stats.Block {
		return &csi.NodeGetVolumeStatsResponse{
			Usage: []*csi.VolumeUsage{
				{
					Total: stats.TotalBytes,
					Unit:  csi.VolumeUsage_BYTES,
				},
			},
		}, nil
	}
	return &csi.NodeGetVolumeStatsResponse{
		Usage: []*csi.VolumeUsage{
			{
				Available: stats.AvailableBytes,
				Total:     stats.TotalBytes,
				Used:      stats.UsedBytes,
				Unit:      csi.VolumeUsage_BYTES,
			},
			{
				Available: stats.AvailableInodes,
				Total:     stats.TotalInodes,
				Used:      stats.UsedInodes,
				Unit:      csi.VolumeUsage_INODES,
			},
		},
		VolumeCondition: &csi.VolumeCondition{},
	}, nil
}

func (n *nodeServer) NodeExpandVolume(ctx context.Context, req *csi.NodeExpandVolumeRequest) (*csi.NodeExpandVolumeResponse, error) {
	volumeID := req.GetVolumeId()
	if volumeID == "" {
		return nil, status.Error(codes.InvalidArgument, "NodeExpandVolume: The volume id must provider")
	}
	volSizeBytes := req.GetCapacityRange().GetRequiredBytes()
	if volSizeBytes < 0 {
		return nil, status.Error(codes.InvalidArgument, "NodeExpandVolume: The volume size must greater than 0")
	}
	targetPath := req.GetVolumePath()
	if targetPath == "" {
		return nil, status.Error(codes.InvalidArgument, "NodeExpandVolume: The volume target path must provider")
	}
	volCap := req.GetVolumeCapability()
	if volCap == nil {
		return nil, status.Error(codes.InvalidArgument, "NodeExpandVolume: The volume capability must provider")
	}
	isBlock := volCap.GetBlock()
	if isBlock != nil {
		klog.V(3).Infof(fmt.Sprintf("NodeExpandVolume called for %s at %s. Since it is a block device, ignoring...", volumeID, targetPath))
		return &csi.NodeExpandVolumeResponse{}, nil
	}
	vol, err := n.cloud.GetVolumeByID(volumeID)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeExpandVolume: Get volume info failed %v", err))
	}
	if vol == nil {
		return nil, status.Errorf(codes.NotFound, fmt.Sprintf("NodeExpandVolume: The cinder volume %s not found.", volumeID))
	}
	output, err := n.mount.GetFsMount(targetPath)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeExpandVolume: Get the volume file system mount info failed, %v", err))
	}
	devicePath := strings.TrimSpace(string(output))
	if devicePath == "" {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeExpandVolume: Unable to find device path for volume %s", volumeID))
	}
	resize := n.mount.NewResizeFs()
	_, err = resize.Resize(devicePath, targetPath)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeExpandVolume: Failed to resize the volume %s, err: %v", volumeID, err))
	}
	volSize, err := n.mount.GetBlockSizeBytes(devicePath)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodeExpandVolume: Get the block device %s size failed %v", devicePath, err))
	}
	return &csi.NodeExpandVolumeResponse{
		CapacityBytes: volSize,
	}, nil
}

func (n *nodeServer) NodeGetCapabilities(ctx context.Context, req *csi.NodeGetCapabilitiesRequest) (*csi.NodeGetCapabilitiesResponse, error) {
	return &csi.NodeGetCapabilitiesResponse{
		Capabilities: []*csi.NodeServiceCapability{
			{
				Type: &csi.NodeServiceCapability_Rpc{
					Rpc: &csi.NodeServiceCapability_RPC{
						Type: csi.NodeServiceCapability_RPC_EXPAND_VOLUME,
					},
				},
			},
			{
				Type: &csi.NodeServiceCapability_Rpc{
					Rpc: &csi.NodeServiceCapability_RPC{
						Type: csi.NodeServiceCapability_RPC_STAGE_UNSTAGE_VOLUME,
					},
				},
			},
			{
				Type: &csi.NodeServiceCapability_Rpc{
					Rpc: &csi.NodeServiceCapability_RPC{
						Type: csi.NodeServiceCapability_RPC_GET_VOLUME_STATS,
					},
				},
			},
		},
	}, nil
}

func (n *nodeServer) NodeGetInfo(ctx context.Context, req *csi.NodeGetInfoRequest) (*csi.NodeGetInfoResponse, error) {
	nodeID := n.driver.nodeId
	maxVolumesPerNode := n.cloud.GetMaxVolumeLimit()
	return &csi.NodeGetInfoResponse{
		NodeId:            nodeID,
		MaxVolumesPerNode: maxVolumesPerNode,
	}, nil
}

func NewNodeServer(d *Driver, cloud openstack.IOpenstack, mount mount.IMount) csi.NodeServer {
	return &nodeServer{
		driver: d,
		cloud:  cloud,
		mount:  mount,
	}
}

func collectMountOptions(fsType string, options []string) []string {
	var result []string
	result = append(result, options...)
	if fsType == FSTypeXfs {
		result = append(result, "nouuid")
	}
	return result
}

// nodePublishBlockVolume mount the block device type volume
func (n *nodeServer) nodePublishBlockVolume(req *csi.NodePublishVolumeRequest, ns *nodeServer, mountOptions []string) (*csi.NodePublishVolumeResponse, error) {
	volumeID := req.GetVolumeId()
	targetPath := req.GetTargetPath()
	podVolumePath := filepath.Dir(targetPath)
	attach, err := n.cloud.GetAttachmentByVolumeID(volumeID)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodePublishVolume: Get volume attachment info failed. Error: %v", err))
	}
	conn, err := brick.NewConnector(attach.ConnectionInfo, defaultExtraAuth)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodePublishVolume: Can not initial connector. Error: %s", err.Error()))
	}
	devicePath := conn.GetDevicePath()
	// View the target path dir whether symlink
	exists, err := utilpath.Exists(utilpath.CheckFollowSymlink, podVolumePath)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodePublishVolume: Get the volume path %s has syslink failed, %v", podVolumePath, err))
	}
	if !exists {
		// Create target path dir
		err := ns.mount.MakeDir(podVolumePath)
		if err != nil {
			return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodePublishVolume: Create the volume dir failed %v", err))
		}
	}
	// Create target path block device file
	err = ns.mount.MakeFile(targetPath)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("NodePublishVolume: Create the block device file failed %v", err))
	}
	//Exec mount command add --bind attribute, let block device mount to file
	if err = ns.mount.Mount(devicePath, targetPath, "", mountOptions); err != nil {
		if removeErr := os.Remove(targetPath); removeErr != nil {
			return nil, status.Errorf(codes.Internal, "NodePublishVolume: Could not remove mount target %q: %v", targetPath, err)
		}
		return nil, status.Errorf(codes.Internal, "NodePublishVolume: Could not mount %q at %q: %v", devicePath, targetPath, err)
	}
	if err = ns.cloud.AttachVolume(volumeID, targetPath, ns.driver.nodeId); err != nil {
		return nil, status.Error(codes.Internal, fmt.Sprintf("NodePublishVolume: Cinder attach block volume failed with error %v", err))
	}
	return &csi.NodePublishVolumeResponse{}, nil
}
