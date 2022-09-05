package iscsi

import (
	"fmt"
	"os"
	"path/filepath"
	"strconv"
	"strings"
	"time"

	"github.com/kungze/cinder-metal-csi/pkg/brick/utils"
	"k8s.io/klog/v2"
)

// Hctl is IDs of SCSI
type Hctl struct {
	HostID    int
	ChannelID int
	TargetID  int
	HostLUNID int
}

//GetHctl Given an iSCSI session return the host, channel, target, and lun
func GetHctl(id int, lun int) (*Hctl, error) {
	globStr := fmt.Sprintf("/sys/class/iscsi_host/host*/device/session%d/target*", id)
	paths, err := filepath.Glob(globStr)
	if err != nil {
		klog.Errorf("Failed to get session path", err)
		return nil, err
	}
	if len(paths) != 1 {
		klog.Errorf("target fail is not found", err)
		return nil, err
	}
	_, fileName := filepath.Split(paths[0])
	ids := strings.Split(fileName, ":")
	if len(ids) != 3 {
		klog.Errorf("failed to parse iSCSI session filename", err)
		return nil, err
	}
	channelID, err := strconv.Atoi(ids[1])
	if err != nil {
		klog.Errorf("failed to parse channel ID", err)
		return nil, err
	}
	targetID, err := strconv.Atoi(ids[2])
	if err != nil {
		klog.Errorf("failed to parse target ID", err)
		return nil, err
	}
	names := strings.Split(paths[0], "/")
	hostIDstr := strings.TrimPrefix(searchHost(names), "host")
	hostID, err := strconv.Atoi(hostIDstr)
	if err != nil {
		klog.Errorf("failed to parse host ID", err)
		return nil, err
	}
	hctl := &Hctl{
		HostID:    hostID,
		ChannelID: channelID,
		TargetID:  targetID,
		HostLUNID: lun,
	}
	return hctl, nil
}

//searchHost search param
func searchHost(names []string) string {
	for _, v := range names {
		if strings.HasPrefix(v, "host") {
			return v
		}
	}
	return ""
}

//ScanISCSI Send an iSCSI scan request given the host and optionally the ctl
func ScanISCSI(hctl *Hctl) error {
	path := fmt.Sprintf("/sys/class/scsi_host/host%d/scan", hctl.HostID)
	content := fmt.Sprintf("%d %d %d",
		hctl.ChannelID,
		hctl.TargetID,
		hctl.HostLUNID)

	return utils.EchoScsiCommand(path, content)
}

//GetDeviceName Add retry to get device name
func GetDeviceName(sessionID int, hctl *Hctl) (string, error) {
	var lastErr error
	for i := 0; i < 10; i++ {
		// retry 10 times
		deviceName, err := getDeviceName(sessionID, hctl)
		if err == nil {
			return deviceName, nil
		}
		klog.V(3).Infof("failed to get device name (sessionID: %d, hctl: %+v), do retry: %+v", sessionID, hctl, err)
		lastErr = err

		if err := ScanISCSI(hctl); err != nil {
			klog.Errorf("failed to scan iSCSI", err)
			return "", err
		}

		time.Sleep(1 * time.Second)
	}
	return "", lastErr
}

//getDeviceName Get device on /sys/class/scsi_host dir name
func getDeviceName(sessionID int, hctl *Hctl) (string, error) {
	p := fmt.Sprintf(
		"/sys/class/iscsi_host/host%d/device/session%d/target%d:%d:%d/%d:%d:%d:%d/block/*",
		hctl.HostID,
		sessionID,
		hctl.HostID, hctl.ChannelID, hctl.TargetID,
		hctl.HostID, hctl.ChannelID, hctl.TargetID, hctl.HostLUNID)

	paths, err := filepath.Glob(p)
	if err != nil {
		klog.Errorf("failed to parse iSCSI block device filepath", err)
		return "", err
	}
	if len(paths) == 0 {
		return "", fmt.Errorf("device filepath is not found")
	}
	_, deviceName := filepath.Split(paths[0])
	return deviceName, nil
}

//removeScsiDevice Removes a scsi device based upon /dev/sdX name
func removeScsiDevice(devicePath string) error {
	deviceName := strings.TrimPrefix(devicePath, "/dev/")
	deletePath := fmt.Sprintf("/sys/block/%s/device/delete", deviceName)
	_, err := os.Stat(deletePath)
	if err != nil {
		klog.Errorf("failed to stat device delete path", err)
		return err
	}

	err = flushDeviceIO(devicePath)
	if err != nil {
		klog.Errorf("failed to flush device I/O", err)
		return err
	}

	err = utils.EchoScsiCommand(deletePath, "1")
	if err != nil {
		klog.Errorf("failed to write to delete path", err)
		return err
	}
	return nil
}

//waitForVolumesRemoval Wait for device paths to be removed from the system
func waitForVolumesRemoval(targetDevicePaths []string) bool {
	exist := false
	for _, devicePath := range targetDevicePaths {
		_, err := os.Stat(devicePath)
		if err == nil {
			klog.V(3).Infof("found not deleted volume: %s", devicePath)
			exist = true
			break
		}
	}
	return exist
}

// GetConnectionDevices get volumes in paths
func GetConnectionDevices(targets []Target) ([]string, error) {
	var devices []string
	sessions, err := GetSessions()
	if err != nil {
		klog.Errorf("failed to get iSCSI sessions", err)
		return nil, err
	}
	for _, target := range targets {
		for _, session := range sessions {
			if session.TargetPortal != target.Portal || session.IQN != target.Iqn {
				continue
			}
			hctl, err := GetHctl(session.SessionID, target.Lun)
			if err != nil {
				klog.Errorf("failed to get hctl info", err)
				return nil, err
			}
			deviceName, err := GetDeviceName(session.SessionID, hctl)
			if err != nil {
				klog.Errorf("failed to get device name", err)
				return nil, err
			}
			if hctl.HostLUNID == target.Lun {
				devices = append(devices, deviceName)
			}
		}
	}
	return devices, nil
}

//RemoveConnection Remove LUNs and multipath associated with devices names
func RemoveConnection(targetDeviceNames []string, isMultiPath bool) error {
	var devicePaths []string
	var err error
	for _, dn := range targetDeviceNames {
		devicePaths = append(devicePaths, "/dev/"+dn)
	}
	if isMultiPath {
		multiPathDeviceName, err := FindSysfsMultipathDM(targetDeviceNames[0])
		if err != nil {
			klog.Errorf("Find dm device failed", err)
			return err
		}
		klog.V(3).Infof("Removing devices %v", devicePaths)
		multiPathDevicePath := "/dev/" + multiPathDeviceName
		err = flushMultipathDevice(multiPathDevicePath)
		klog.V(3).Infof("Flush multipath devices %v", devicePaths)
		if err != nil {
			klog.Errorf("Flush %s failed", multiPathDevicePath)
		}
	}
	for _, devicePath := range devicePaths {
		err := removeScsiDevice(devicePath)
		if err != nil {
			return fmt.Errorf("timeout exceeded wait for volume removal")
		}
	}
	timeoutSecond := 10
	for i := 0; waitForVolumesRemoval(targetDeviceNames); i++ {
		// until exist target volume.
		klog.V(3).Info("wait removed target volume...")
		time.Sleep(1 * time.Second)

		if i == timeoutSecond {
			klog.Error("timeout exceeded wait for volume removal")
			return err
		}
	}
	err = removeScsiSymlinks(devicePaths)
	if err != nil {
		klog.Errorf("failed to remove scsi symlinks", err)
		return err
	}
	return nil
}

//removeScsiSymlinks Remove iscsi device link path
func removeScsiSymlinks(devicePaths []string) error {
	links, err := filepath.Glob("/dev/disk/by-id/scsi-*")
	if err != nil {
		klog.Errorf("failed to get scsi link", err)
		return err
	}
	var removeTarget []string
	for _, link := range links {
		realpath, err := filepath.EvalSymlinks(link)
		if err != nil {
			klog.Error(fmt.Sprintf("failed to get realpath: %v", err))
		}

		for _, devicePath := range devicePaths {
			if realpath == devicePath {
				removeTarget = append(removeTarget, link)
				break
			}
		}
	}
	for _, l := range removeTarget {
		err = os.Remove(l)
		if err != nil {
			klog.Errorf("failed to delete symlink", err)
			return err
		}
	}
	return nil
}

//DisconnectConnection Close iscsi connection
func DisconnectConnection(targets []Target) error {
	for _, p := range targets {
		err := disconnectFromIscsiPortal(p.Portal, p.Iqn)
		if err != nil {
			klog.Errorf("failed to disconnect from iSCSI portal", err)
			return err
		}
	}
	return nil
}

//disconnectFromIscsiPortal logout iscsi partal
func disconnectFromIscsiPortal(portal string, iqn string) error {
	_, err := utils.UpdateIscsiadm(portal, iqn, "node.startup", "manual", nil)
	if err != nil {
		klog.Errorf("failed to update node.startup to manual", err)
		return err
	}
	_, err = utils.ExecIscsiadm(portal, iqn, []string{"--logout"})
	if err != nil {
		klog.Errorf("Exec iscsiadm logout command failed", err)
		return err
	}
	_, err = utils.ExecIscsiadm(portal, iqn, []string{"--op", "delete"})
	if err != nil {
		klog.Errorf("failed to execute --op delete", err)
		return err
	}
	klog.V(3).Infof("iscsiadm portal %s logout success", portal)
	return nil
}

//flushDeviceIO This is used to flush any remaining IO in the buffers
func flushDeviceIO(devicePath string) error {
	_, err := os.Stat(devicePath)
	if err != nil {
		klog.Errorf("failed to stat device path", err)
		return err
	}
	args := []string{"--flushbufs", devicePath}
	if _, err := utils.Execute("blockdev", args...); err != nil {
		klog.Errorf("failed to execute blockdev command", err)
		return err
	}
	return nil
}
