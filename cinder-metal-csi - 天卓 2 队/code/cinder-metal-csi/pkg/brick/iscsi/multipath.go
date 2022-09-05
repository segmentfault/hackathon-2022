package iscsi

import (
	"fmt"
	"path/filepath"
	"strings"

	"github.com/kungze/cinder-metal-csi/pkg/brick/utils"
	"k8s.io/klog/v2"
)

//Target a iscsi connection info
type Target struct {
	Portal string
	Iqn    string
	Lun    int
}

// DiscoverIscsiPortals get iscsi connection information
func DiscoverIscsiPortals(portal string, iqn string, luns int) []Target {
	var target []Target
	var portals []string
	var iqns []string
	args := []string{"-m", "discovery", "-t", "sendtargets", "-p", portal}
	out, err := utils.Execute("iscsiadm", args...)
	if err != nil {
		klog.Errorf("Exec iscsiadm discovery command failed", err)
		return nil
	}
	entries := strings.Split(out, "\n")
	for _, entry := range entries {
		data := strings.Split(entry, " ")
		if !strings.Contains(data[1], iqn) {
			continue
		}
		p := strings.Split(data[0], ",")[0]
		portals = append(portals, p)
		iqns = append(iqns, data[1])
	}

	for i, por := range portals {
		t := NewTarget(por, iqns[i], luns)
		target = append(target, t)
	}

	return target
}

//NewTarget Build a target object include portal, iqn, lun
func NewTarget(portals string, iqns string, luns int) Target {
	p := Target{
		Portal: portals,
		Iqn:    iqns,
		Lun:    luns,
	}
	return p
}

//FindSysfsMultipathDM Find the dm device name given a list of device names
func FindSysfsMultipathDM(deviceName string) (dmDeviceName string, err error) {
	globStr := fmt.Sprintf("/sys/block/%s/holders/dm-*", deviceName)
	paths, err := filepath.Glob(globStr)
	if err != nil {
		klog.Errorf("failed to glob dm device filepath", err)
		return "", err
	}
	if len(paths) == 0 {
		return "", fmt.Errorf("dm device is not found")
	}

	_, name := filepath.Split(paths[0])
	return name, nil
}

//flushMultipathDevice Flush dm device
func flushMultipathDevice(targetMultipathPath string) error {
	args := []string{"-f", targetMultipathPath}
	_, err := utils.Execute("multipath", args...)
	if err != nil {
		klog.Errorf("failed to execute multipath device flush command", err)
		return err
	}
	return nil
}
