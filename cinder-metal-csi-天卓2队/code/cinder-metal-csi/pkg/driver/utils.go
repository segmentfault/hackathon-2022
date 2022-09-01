package driver

import (
	"fmt"
	"math"
	"strings"

	"github.com/container-storage-interface/spec/lib/go/csi"
	"k8s.io/cloud-provider/volume/helpers"
)

func ParseEndpoint(endpoint string) (string, string, error) {
	if strings.HasPrefix(strings.ToLower(endpoint), "unix://") || strings.HasPrefix(strings.ToLower(endpoint), "tcp://") {
		s := strings.SplitN(endpoint, "://", 2)
		if s[1] != "" {
			return s[0], s[1], nil
		}
	}
	return "", "", fmt.Errorf("invaild endpoint: %s", endpoint)
}

func NewControllerServerCapability(cap csi.ControllerServiceCapability_RPC_Type) *csi.ControllerServiceCapability {
	return &csi.ControllerServiceCapability{
		Type: &csi.ControllerServiceCapability_Rpc{
			Rpc: &csi.ControllerServiceCapability_RPC{
				Type: cap,
			},
		},
	}
}

func NewVolumeCapabilityAccessMode(vap csi.VolumeCapability_AccessMode_Mode) *csi.VolumeCapability_AccessMode {
	return &csi.VolumeCapability_AccessMode{Mode: vap}
}

func RoundOffBytes(bytes int64) int {
	var num int64
	if floatBytes := float64(bytes); floatBytes < helpers.GiB {
		num = int64(math.Ceil(floatBytes / helpers.MiB))
		num *= helpers.MiB
	} else {
		num = int64(math.Ceil(floatBytes / helpers.GiB))
		num *= helpers.GiB
	}
	num = num / helpers.GiB
	return int(num)
}
