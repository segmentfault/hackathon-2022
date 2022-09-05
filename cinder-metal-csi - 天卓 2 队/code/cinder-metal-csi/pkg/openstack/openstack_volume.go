package openstack

import (
	"fmt"
	"net/url"
	"runtime"
	"strconv"
	"strings"

	"github.com/gophercloud/gophercloud/openstack"
	"github.com/gophercloud/gophercloud/openstack/blockstorage/extensions/volumeactions"
	"github.com/gophercloud/gophercloud/openstack/blockstorage/v3/attachments"
	"github.com/gophercloud/gophercloud/openstack/blockstorage/v3/snapshots"
	"github.com/gophercloud/gophercloud/openstack/blockstorage/v3/volumes"
	"github.com/gophercloud/gophercloud/openstack/compute/apiversions"
	"github.com/gophercloud/gophercloud/openstack/sharedfilesystems/v2/availabilityzones"
	"github.com/gophercloud/gophercloud/pagination"
	"k8s.io/klog/v2"
)

const (
	createVolumeDescription        = "openstack cinder metal csi driver"
	volumeAvailableStatus          = "available"
	volumeInUseStates              = "in-use"
	defaultMaxVolAttachLimit int64 = 256
)

func (op *Openstack) CreateVolume(name, zone, volType, snapshotID, sourceVolID string, size int) (*volumes.Volume, error) {
	opts := volumes.CreateOpts{
		Name:             name,
		Size:             size,
		SnapshotID:       snapshotID,
		SourceVolID:      sourceVolID,
		VolumeType:       volType,
		AvailabilityZone: zone,
		Description:      createVolumeDescription,
	}
	vol, err := volumes.Create(op.BlockStorageClient, opts).Extract()
	if err != nil {
		return nil, err
	}
	err = volumes.WaitForStatus(op.BlockStorageClient, vol.ID, "available", 300)
	if err != nil {
		return nil, err
	}
	return vol, nil
}

func (op *Openstack) DeleteVolume(volumeID string) error {
	err := volumes.Delete(op.BlockStorageClient, volumeID, volumes.DeleteOpts{}).ExtractErr()
	if err != nil {
		return err
	}
	return nil
}

func (op *Openstack) AttachVolume(volumeID, mountPoint, hostName string) error {
	opts := volumeactions.AttachOpts{
		MountPoint: mountPoint,
		HostName:   hostName,
	}
	err := volumeactions.Attach(op.BlockStorageClient, volumeID, opts).ExtractErr()
	if err != nil {
		return err
	}
	return nil
}

func (op *Openstack) DetachVolume(volumeID string) error {
	err := volumeactions.Detach(op.BlockStorageClient, volumeID, volumeactions.DetachOpts{}).ExtractErr()
	if err != nil {
		return err
	}
	return nil
}

func (op *Openstack) GetVolumeByName(volumeName string) ([]volumes.Volume, error) {
	blockStorageClient, err := openstack.NewBlockStorageV3(op.BlockStorageClient.ProviderClient, op.EsOpts)
	if err != nil {
		klog.Error(fmt.Sprintf("Request block storage client failed, %v", err))
		return nil, err
	}
	blockStorageClient.Microversion = "3.34"
	opts := volumes.ListOpts{Name: volumeName}
	page, err := volumes.List(blockStorageClient, opts).AllPages()
	if err != nil {
		klog.Error(fmt.Sprintf("Get volume list failed, %v", err))
		return nil, err
	}
	vol, err := volumes.ExtractVolumes(page)
	if err != nil {
		klog.Error(fmt.Sprintf("Extract the volume page failed, %v", err))
		return nil, err
	}
	return vol, nil
}

func (op *Openstack) GetVolumeByID(volumeID string) (*volumes.Volume, error) {
	vol, err := volumes.Get(op.BlockStorageClient, volumeID).Extract()
	if err != nil {
		vol404Msg := fmt.Sprintf("Volume %s could not be found.", volumeID)
		if strings.Contains(err.Error(), vol404Msg) {
			return nil, nil
		}
		klog.Error(fmt.Sprintf("Get volume info failed, %v", err))
		return nil, err
	}
	return vol, nil
}

func (op *Openstack) ListVolume(maxLimit int32, marker string) ([]volumes.Volume, string, error) {
	var volume []volumes.Volume
	var err error
	var nextPageToken string

	opts := volumes.ListOpts{
		Limit:  int(maxLimit),
		Marker: marker,
	}
	err = volumes.List(op.BlockStorageClient, opts).EachPage(func(page pagination.Page) (bool, error) {
		volume, err = volumes.ExtractVolumes(page)
		if err != nil {
			klog.Error(fmt.Sprintf("Extract volume list failed, %v", err))
			return false, err
		}
		pageUrl, err := page.NextPageURL()
		if err != nil {
			klog.Error(fmt.Sprintf("Get page url failed, %v", err))
			return false, err
		}
		if pageUrl != "" {
			parse, err := url.ParseQuery(pageUrl)
			if err != nil {
				return false, err
			}
			nextPageToken = parse.Get("Marker")
		}
		return false, nil
	})
	if err != nil {
		return nil, nextPageToken, err
	}

	return volume, nextPageToken, nil
}

func (op *Openstack) CreateVolumeAttachment(volumeID, instanceID string) (*attachments.Attachment, error) {
	blockStorageClient, err := openstack.NewBlockStorageV3(op.BlockStorageClient.ProviderClient, op.EsOpts)
	if err != nil {
		klog.Error(fmt.Sprintf("Request block storage client failed, %v", err))
		return nil, err
	}
	blockStorageClient.Microversion = "3.44"
	ams, err := op.GetAttachmentByVolumeID(volumeID)
	if err != nil {
		return nil, err
	}
	if ams != nil {
		return ams, nil
	}
	arch := runtime.GOARCH
	if arch == "amd64" {
		arch = "x86_64"
	}
	opts := attachments.CreateOpts{
		VolumeUUID:   volumeID,
		InstanceUUID: instanceID,
		Connector: map[string]interface{}{
			"platform":  arch,
			"os_type":   runtime.GOOS,
			"mode":      "rw",
			"initiator": nil,
		},
	}
	attach, err := attachments.Create(blockStorageClient, opts).Extract()
	if err != nil {
		return nil, err
	}
	return attach, nil
}

func (op *Openstack) GetVolumeAttachment(attachmentID string) (*attachments.Attachment, error) {
	blockStorageClient, err := openstack.NewBlockStorageV3(op.BlockStorageClient.ProviderClient, op.EsOpts)
	if err != nil {
		klog.Error(fmt.Sprintf("Request block storage client failed, %v", err))
		return nil, err
	}
	blockStorageClient.Microversion = "3.44"
	attach, err := attachments.Get(blockStorageClient, attachmentID).Extract()
	if err != nil {
		err404Msg := "Volume attachment could not be found with filter"
		if strings.Contains(err.Error(), err404Msg) {
			return nil, nil
		}
		return nil, err
	}
	return attach, err
}

func (op *Openstack) GetAttachmentByVolumeID(volumeID string) (*attachments.Attachment, error) {
	blockStorageClient, err := openstack.NewBlockStorageV3(op.BlockStorageClient.ProviderClient, op.EsOpts)
	if err != nil {
		klog.Error(fmt.Sprintf("Request block storage client failed, %v", err))
		return nil, err
	}
	blockStorageClient.Microversion = "3.44"
	opts := attachments.ListOpts{
		VolumeID: volumeID,
	}
	page, err := attachments.List(blockStorageClient, opts).AllPages()
	if err != nil {
		return nil, err
	}
	attachments, err := attachments.ExtractAttachments(page)
	if err != nil {
		return nil, err
	}
	if len(attachments) > 0 {
		return &attachments[0], nil
	}
	return nil, nil
}

func (op *Openstack) DeleteVolumeAttachment(volumeID string) error {
	blockStorageClient, err := openstack.NewBlockStorageV3(op.BlockStorageClient.ProviderClient, op.EsOpts)
	if err != nil {
		klog.Error(fmt.Sprintf("Request block storage client failed, %v", err))
		return err
	}
	blockStorageClient.Microversion = "3.44"
	vol, err := op.GetVolumeByID(volumeID)
	if err != nil {
		return err
	}
	if vol == nil {
		return nil
	}
	for _, attach := range vol.Attachments {
		res := attachments.Delete(blockStorageClient, attach.AttachmentID)
		if res.Err != nil {
			return res.Err
		}
	}
	return nil
}

func (op *Openstack) VolumeAttachmentComplete(attachmentID string) error {
	blockStorageClient, err := openstack.NewBlockStorageV3(op.BlockStorageClient.ProviderClient, op.EsOpts)
	if err != nil {
		klog.Error(fmt.Sprintf("Request block storage client failed, %v", err))
		return err
	}
	blockStorageClient.Microversion = "3.44"
	return attachments.Complete(blockStorageClient, attachmentID).ExtractErr()
}

func (op *Openstack) CreateSnapShot(name string, sourceVolumeID string) (*snapshots.Snapshot, error) {
	opts := snapshots.CreateOpts{
		Name:        name,
		VolumeID:    sourceVolumeID,
		Description: createVolumeDescription,
		Force:       true,
	}
	snapshot, err := snapshots.Create(op.BlockStorageClient, opts).Extract()
	if err != nil {
		klog.Error(fmt.Sprintf("Create snapshot volume %s failed, %v", name, err))
		return nil, err
	}
	return snapshot, nil
}

func (op *Openstack) DeleteSnapshot(snapshotID string) error {
	err := snapshots.Delete(op.BlockStorageClient, snapshotID).ExtractErr()
	if err != nil {
		klog.Error(fmt.Sprintf("Delete snapshot volume %s failed, %v", snapshotID, err))
	}
	return nil
}

func (op *Openstack) ListSnapshot(filter map[string]string) ([]snapshots.Snapshot, string, error) {
	opts := snapshots.ListOpts{}
	var snapshot []snapshots.Snapshot
	var err error
	var nextToken string
	for _, value := range filter {
		switch value {
		case "status":
			opts.Status = value
		case "volumeID":
			opts.VolumeID = value
		case "limit":
			opts.Limit, _ = strconv.Atoi(value)
		case "marker":
			opts.Marker = value
		}
	}
	err = snapshots.List(op.BlockStorageClient, opts).EachPage(func(page pagination.Page) (bool, error) {
		snapshot, err = snapshots.ExtractSnapshots(page)
		if err != nil {
			klog.Error(fmt.Sprintf("Extract snapshot info failed, %v", err))
			return false, err
		}
		pageUrl, err := page.NextPageURL()
		if err != nil {
			klog.Error(fmt.Sprintf("Get page url failed, %v", err))
			return false, err
		}
		if pageUrl != "" {
			result, err := url.ParseQuery(pageUrl)
			if err != nil {
				klog.Error(fmt.Sprintf("Parse url failed, %v", err))
				return false, err
			}
			nextToken = result.Get("Marker")
		}
		return false, nil
	})
	return snapshot, nextToken, err
}

func (op *Openstack) GetSnapshotByID(snapshotID string) (*snapshots.Snapshot, error) {
	snapshot, err := snapshots.Get(op.BlockStorageClient, snapshotID).Extract()
	if err != nil {
		klog.Error(fmt.Sprintf("Get snapshot volume %s info failed, %v", snapshotID, err))
		return nil, err
	}
	return snapshot, nil
}

func (op *Openstack) ExpandVolume(volumeID string, status string, size int) error {
	opts := volumeactions.ExtendSizeOpts{
		NewSize: size,
	}
	switch status {
	case volumeInUseStates:
		provider := op.BlockStorageClient.ProviderClient
		blockStorageClient, err := openstack.NewBlockStorageV3(provider, op.EsOpts)
		if err != nil {
			klog.Error(fmt.Sprintf("Get block storage client failed, %v", err))
			return err
		}
		blockStorageClient.Microversion = "3.42"

		err = volumeactions.ExtendSize(blockStorageClient, volumeID, opts).ExtractErr()
		if err != nil {
			return err
		}
	case volumeAvailableStatus:
		err := volumeactions.ExtendSize(op.BlockStorageClient, volumeID, opts).ExtractErr()
		if err != nil {
			return err
		}
	}
	return fmt.Errorf("volume cannot be resized, when status is %s", status)
}

func (op *Openstack) GetAvailability() (string, error) {
	var zoneName string
	zone, err := availabilityzones.List(op.BlockStorageClient).AllPages()
	if err != nil {
		klog.Error(fmt.Sprintf("Get availability zone info failed, %v", err))
		return "", err
	}
	result, err := availabilityzones.ExtractAvailabilityZones(zone)
	if err != nil {
		klog.Error(fmt.Sprintf("Extract availability zone info failed, %v", err))
		return "", err
	}
	for _, res := range result {
		zoneName = res.Name
	}
	return zoneName, nil
}

func (op *Openstack) GetBsOpts() BlockStorage {
	return op.BsOpts
}

func (op *Openstack) CheckBlockStorageAPI() error {
	_, err := apiversions.List(op.BlockStorageClient).AllPages()
	if err != nil {
		return err
	}
	return nil
}

func (op *Openstack) GetMaxVolumeLimit() int64 {
	if op.BsOpts.NodeVolumeAttachLimit > 0 || op.BsOpts.NodeVolumeAttachLimit <= 256 {
		return op.BsOpts.NodeVolumeAttachLimit
	}
	return defaultMaxVolAttachLimit
}
