package utils

import (
	"fmt"
	"os"
	"os/exec"
	"strconv"

	"k8s.io/klog/v2"
)

// Execute a shell command
func Execute(command string, arg ...string) (string, error) {
	cmd := exec.Command(command, arg...)
	stdoutStderr, err := cmd.CombinedOutput()
	return string(stdoutStderr), err
}

//ExecIscsiadm exec a iscsiadm shell command
func ExecIscsiadm(portalIP string, iqn string, args []string) (string, error) {
	var cmd []string
	baseArgs := []string{"-m", "node"}
	cmd = append(baseArgs, []string{"-T", iqn}...)
	cmd = append(cmd, []string{"-p", portalIP}...)
	cmd = append(cmd, args...)

	out, err := Execute("iscsiadm", cmd...)
	if err != nil {
		klog.Errorf("failed to execute iscsiadm %s command. Error: %s. Error output: %s.", args, err.Error(), out)
		return "", err
	}
	klog.V(3).Infof("Exec iscsiadm -m node -T %s -p %s %s command success", iqn, portalIP, args)
	return out, nil
}

//UpdateIscsiadm update iscsiadm shell command
func UpdateIscsiadm(portalIP, targetIQN, key, value string, args []string) (string, error) {
	a := []string{"--op", "update", "-n", key, "-v", value}
	a = append(a, args...)
	return ExecIscsiadm(portalIP, targetIQN, a)
}

//EchoScsiCommand Used to echo strings to scsi subsystem
func EchoScsiCommand(path, content string) error {
	// write content to path (sysfs)
	klog.V(3).Infof("write scsi file [path: %s content: %s]", path, content)

	f, err := os.OpenFile(path, os.O_WRONLY, 0400)
	if err != nil {
		klog.Errorf("failed to open file", err)
		return err
	}
	defer f.Close()

	if _, err := f.WriteString(content); err != nil {
		klog.Errorf("failed to write file", err)
		return err
	}
	return nil
}

func ToBool(i interface{}) bool {
	switch res := i.(type) {
	case bool:
		return res
	case int, int32, int64, uint, uint32, uint64:
		return res != 0
	case string:
		result, err := strconv.ParseBool(res)
		if err != nil {
			panic(err.Error())
		}
		return result
	default:
		panic(fmt.Sprintf("Can not convert %T to bool.", res))
	}
}

func ToInt(i interface{}) int {
	switch res := i.(type) {
	case int:
		return res
	case string:
		result, err := strconv.Atoi(res)
		if err != nil {
			return -1
		}
		return result
	case float64:
		result := int(res)
		return result
	case float32:
		result := int(res)
		return result
	}
	return -1
}

func ToIntSlice(i interface{}) []int {
	resSlice := i.([]interface{})
	result := make([]int, len(resSlice))
	for i, v := range resSlice {
		result[i] = ToInt(v)
	}
	return result
}

func ToString(i interface{}) string {
	res := fmt.Sprint(i)
	return res
}

func ToStringSlice(i interface{}) []string {
	resSlice := i.([]interface{})
	result := make([]string, len(resSlice))
	for i, v := range resSlice {
		result[i] = ToString(v)
	}
	return result
}
