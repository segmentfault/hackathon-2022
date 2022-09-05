package rbd

import (
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"os"
	"path"
	"strconv"
	"strings"

	"github.com/kungze/cinder-metal-csi/pkg/brick/utils"
	"k8s.io/klog/v2"
)

const (
	tmpKeyFileLocation   = "/tmp/csi/keys"
	tmpKeyFileNamePrefix = "keyfile-"
)

const (
	cephClientUser = "cephClientUser"
	cephClientKey = "cephClientKey"
)

var utilsExecute = utils.Execute

// ConnRbd contains rbd volume info
type ConnRbd struct {
	Name              string
	monitorAddr       string
	cephClientUser    string
	cephClientKeyring string
}

func joinHostsAndPorts(hosts, ports []string) string {
	var addr []string
	for index, host := range hosts {
		addr = append(addr, utils.ToString(host)+":"+utils.ToString(ports[index]))
	}
	return strings.Join(addr, ",")
}

// NewRBDConnector Return ConnRbd Pointer to the object
func NewRBDConnector(connInfo map[string]interface{}, extraAuth map[string]string) *ConnRbd {
	conn := &ConnRbd{}
	conn.Name = utils.ToString(connInfo["name"])
	hosts := utils.ToStringSlice(connInfo["hosts"])
	ports := utils.ToStringSlice(connInfo["ports"])
	conn.monitorAddr = joinHostsAndPorts(hosts, ports)
	if extraAuth[cephClientUser] != "" {
		conn.cephClientUser = extraAuth[cephClientUser]
	} else {
		conn.cephClientUser = utils.ToString(connInfo["auth_username"])
	}
	if extraAuth[cephClientKey] != "" {
		conn.cephClientKeyring = extraAuth[cephClientKey]
	} else {
		conn.cephClientKeyring = utils.ToString(connInfo["keyring"])
    }
	return conn
}

// ConnectVolume Connect to a volume
func (c *ConnRbd) ConnectVolume() (map[string]string, error) {
	var err error
	result, err := c.localAttachVolume()
	if err != nil {
		klog.Error(fmt.Sprintf("Do local attach volume failed, %v", err))
		return nil, err
	}
	klog.V(3).Infof("RBD Connect Success, Map Path is %s", result["path"])
	return result, nil
}

// DisConnectVolume Disconnect a volume
func (c *ConnRbd) DisConnectVolume() error {
	rootDevice := c.findRootDevice()
	if rootDevice != "" {
		cmd := []string{"unmap", rootDevice}
		res, err := utilsExecute("rbd", cmd...)
		if err != nil {
			klog.Errorf("Exec rbd unmap failed,", err)
			return err
		}
		klog.V(3).Info("Exec rbd unmap command success", res)
	}
	return nil
}

// ExtendVolume Refresh local volume view and return current size in bytes
// Nothing to do, RBD attached volumes are automatically refreshed, but
// we need to return the new size for compatibility
func (c *ConnRbd) ExtendVolume() (int64, error) {
	device := c.findRootDevice()
	if device == "" {
		klog.Errorf("device is not exist.")
		return -1, errors.New("device is not exist")
	}
	deviceName := path.Base(device)
	deviceNumber := deviceName[3:]
	size, err := ioutil.ReadFile("/sys/devices/rbd/" + deviceNumber + "/size")
	if err != nil {
		klog.Errorf("Read /sys/devices/rbd/?/size failed", err)
		return -1, err
	}
	strSize := string(size)
	vSize := strings.Replace(strSize, "'", "", -1)
	iSize, _ := strconv.ParseInt(vSize, 10, 64)
	klog.V(3).Infof("extend volume to %s is success", iSize)
	return iSize, nil
}

// findRootDevice Find the underlying /dev/rbd* device for a mapping
// Use the showmapped command to list all acive mappings and find the
// underlying /dev/rbd* device that corresponds to our pool and volume
func (c *ConnRbd) findRootDevice() string {
	volume := strings.Split(c.Name, "/")
	poolVolume := volume[1]
	cmd := []string{"showmapped", "--format=json"}
	res, err := utilsExecute("rbd", cmd...)
	klog.V(3).Info("Exec rbd showmapped command success", res)
	if err != nil {
		klog.Errorf("Exec rbd showmapped failed", err)
		return ""
	}
	var result []map[string]string
	err = json.Unmarshal([]byte(res), &result)
	if err != nil {
		klog.Errorf("conversion json failed")
		return ""
	}
	for _, mapping := range result {
		if mapping["name"] == poolVolume {
			return mapping["device"]
		}
	}
	return ""
}

func storeKey(key string) (string, error) {
	tmpfile, err := ioutil.TempFile(tmpKeyFileLocation, tmpKeyFileNamePrefix)
	if err != nil {
		return "", fmt.Errorf("error creating a temporary keyfile: %w", err)
	}
	defer func() {
		if err != nil {
			// don't complain about unhandled error
			_ = os.Remove(tmpfile.Name())
		}
	}()

	if _, err = tmpfile.WriteString(key); err != nil {
		return "", fmt.Errorf("error writing key to temporary keyfile: %w", err)
	}

	keyFile := tmpfile.Name()
	if keyFile == "" {
		err = fmt.Errorf("error reading temporary filename for key: %w", err)

		return "", err
	}

	if err = tmpfile.Close(); err != nil {
		return "", fmt.Errorf("error closing temporary filename: %w", err)
	}

	return keyFile, nil
}

// localAttachVolume Exec local attach volume process
func (c *ConnRbd) localAttachVolume() (map[string]string, error) {
	res := map[string]string{}
	_, err := utilsExecute("which", "rbd")
	if err != nil {
		klog.Error(fmt.Sprintf("Exec which rbd command failed, %v", err))
		return nil, err
	}

	keyFile, err := storeKey(c.cephClientKeyring)
	if err != nil {
		return nil, err
	}

	mapArgs := []string{
		"--id", c.cephClientUser,
		"-m", c.monitorAddr,
		"--keyfile=" + keyFile,
	}

	volume := strings.Split(c.Name, "/")
	poolName := volume[0]
	poolVolume := volume[1]
	rbdDevPath := c.GetDevicePath()
	_, err = os.Readlink(rbdDevPath)
	if err != nil {
		mapArgs := append(mapArgs, "map", poolVolume, "--pool", poolName)
		klog.Infof("Start exec map the pool %s the volume %s command", poolName, poolVolume)
		result, err := utilsExecute("rbd", mapArgs...)
		if err != nil {
			klog.Error(fmt.Sprintf("rbd map command exec failed: %v, Error msg: %s", err, result))
			return nil, err
		}
		klog.Infof("command succeeded: rbd map path is %s", result)
	} else {
		klog.V(3).Infof("Volume %s is already mapped to local device %s", poolVolume, rbdDevPath)
		return nil, err
	}
	klog.Infof("Get the block device path name %s", rbdDevPath)
	res["path"] = rbdDevPath
	res["type"] = "block"
	return res, nil
}

// GetDevicePath Return device name which will be generated by RBD kernel module
func (c *ConnRbd) GetDevicePath() string {
	volume := strings.Split(c.Name, "/")
	poolName := volume[0]
	poolVolume := volume[1]
	path := fmt.Sprintf("/dev/rbd/%s/%s", poolName, poolVolume)
	return path
}
