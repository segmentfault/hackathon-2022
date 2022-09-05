package brick

import (
	"fmt"
	"strings"

	"github.com/kungze/cinder-metal-csi/pkg/brick/iscsi"
	"github.com/kungze/cinder-metal-csi/pkg/brick/rbd"
	"github.com/kungze/cinder-metal-csi/pkg/brick/utils"
)

// Support connection protocl
const (
	RBD   = "RBD"
	ISCSI = "ISCSI"
)

// ConnProperties is base class interface
type Connnnector interface {
	ConnectVolume() (map[string]string, error)
	DisConnectVolume() error
	ExtendVolume() (int64, error)
	GetDevicePath() string
}

// NewConnector Build a Connector object based upon protocol and architecture
func NewConnector(connInfo map[string]interface{}, extraAuth map[string]string) (Connnnector, error) {
	protocol := utils.ToString(connInfo["driver_volume_type"])
	switch strings.ToUpper(protocol) {
	case RBD:
		return rbd.NewRBDConnector(connInfo, extraAuth), nil
	case ISCSI:
		return iscsi.NewISCSIConnector(connInfo), nil
	}
	return nil, fmt.Errorf("The 'driver_volume_type': %s don't support.", protocol)
}
