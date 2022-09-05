package mount

import (
	"fmt"
	"os"
	"strconv"
	"strings"

	"golang.org/x/sys/unix"
	"k8s.io/klog/v2"
	mountutil "k8s.io/mount-utils"
	utilexec "k8s.io/utils/exec"
)

type IMount interface {
	mountutil.Interface

	NodeFormatAndMount(devicePath string, targetPath string, fsType string, mountOptions []string) error
	MakeFile(path string) error
	MakeDir(path string) error
	GetFsMount(path string) ([]byte, error)
	GetBlockSizeBytes(path string) (int64, error)
	UnmountPath(path string) error
	NewResizeFs() *mountutil.ResizeFs
	GetDeviceStats(path string) (*DeviceStats, error)
}

type DeviceStats struct {
	Block          bool
	AvailableBytes int64
	TotalBytes     int64
	UsedBytes      int64

	AvailableInodes int64
	TotalInodes     int64
	UsedInodes      int64
}

type NodeMount struct {
	*mountutil.SafeFormatAndMount
}

func (n *NodeMount) NodeFormatAndMount(devicePath string, targetPath string, fsType string, mountOptions []string) error {
	return n.FormatAndMount(devicePath, targetPath, fsType, mountOptions)
}

func (n *NodeMount) MakeFile(path string) error {
	f, err := os.OpenFile(path, os.O_CREATE, os.FileMode(0644))
	if err != nil {
		if !os.IsExist(err) {
			klog.Error(fmt.Sprintf("Create file %s failed %v", path, err))
			return err
		}
	}
	if err = f.Close(); err != nil {
		klog.Error(fmt.Sprintf("Close file %s failed %v", path, err))
		return err
	}
	return nil
}

func (n *NodeMount) MakeDir(path string) error {
	err := os.Mkdir(path, os.FileMode(0755))
	if err != nil {
		klog.Errorf("Create dir %s is failed %v", path, err)
		return err
	}
	return nil
}

func (n *NodeMount) GetFsMount(path string) ([]byte, error) {
	args := []string{"-o", "source", "--first-only", "--noheadings", "--target", path}
	return n.Exec.Command("findmnt", args...).CombinedOutput()
}

func (n *NodeMount) GetBlockSizeBytes(path string) (int64, error) {
	args := []string{"--getsize64", path}
	output, err := n.Exec.Command("blockdev", args...).CombinedOutput()
	if err != nil {
		klog.Errorf("Get the block device size failed %s", path)
		return 0, err
	}
	size, err := strconv.ParseInt(strings.TrimSpace(string(output)), 10, 64)
	if err != nil {
		klog.Error(fmt.Sprintf("Parse volume size failed, %v", err))
	}
	return size, nil
}

func (n *NodeMount) UnmountPath(path string) error {
	return mountutil.CleanupMountPoint(path, n, false)
}

func (n *NodeMount) NewResizeFs() *mountutil.ResizeFs {
	return mountutil.NewResizeFs(n.Exec)
}

func (n *NodeMount) GetDeviceStats(path string) (*DeviceStats, error) {
	isBlock, err := IsBlockDevice(path)
	if err != nil {
		klog.Error(fmt.Sprintf("Get the volume path %s is block type failed, %v", path, err))
		return nil, err
	}
	if isBlock {
		size, err := GetBlockDeviceSize(path)
		if err != nil {
			return nil, err
		}
		return &DeviceStats{
			Block:      isBlock,
			TotalBytes: size,
		}, nil
	}
	var statfs unix.Statfs_t
	err = unix.Statfs(path, &statfs)
	if err != nil {
		return nil, err
	}
	return &DeviceStats{
		Block:          isBlock,
		AvailableBytes: int64(statfs.Bavail) * statfs.Bsize,
		TotalBytes:     int64(statfs.Blocks) * statfs.Bsize,
		UsedBytes:      (int64(statfs.Blocks) - int64(statfs.Bfree)) * statfs.Bsize,

		AvailableInodes: int64(statfs.Ffree),
		TotalInodes:     int64(statfs.Files),
		UsedInodes:      int64(statfs.Files) - int64(statfs.Ffree),
	}, nil
}

func NewNodeMount() IMount {
	safeMount := &mountutil.SafeFormatAndMount{
		Interface: mountutil.New(""),
		Exec:      utilexec.New(),
	}
	return &NodeMount{
		safeMount,
	}
}
