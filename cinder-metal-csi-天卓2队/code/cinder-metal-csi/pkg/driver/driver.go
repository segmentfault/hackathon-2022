package driver

import (
	"fmt"

	"github.com/container-storage-interface/spec/lib/go/csi"
	"github.com/kungze/cinder-metal-csi/pkg/mount"
	"github.com/kungze/cinder-metal-csi/pkg/openstack"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"k8s.io/klog/v2"
)

const (
	driverName = "cinder.metal.csi"
)

type Driver struct {
	name     string
	nodeId   string
	endpoint string
	version  string

	cs  csi.ControllerServer
	ids csi.IdentityServer
	ns  csi.NodeServer

	cap  []*csi.ControllerServiceCapability
	vcap []*csi.VolumeCapability_AccessMode
}

func NewDriver(nodeId, version, endpoint string, cloud openstack.IOpenstack, mount mount.IMount) *Driver {
	d := &Driver{}
	d.name = driverName
	d.nodeId = nodeId
	d.endpoint = endpoint
	d.version = version
	d.AddControllerCapability([]csi.ControllerServiceCapability_RPC_Type{
		csi.ControllerServiceCapability_RPC_LIST_SNAPSHOTS,
		csi.ControllerServiceCapability_RPC_LIST_VOLUMES,
		csi.ControllerServiceCapability_RPC_GET_VOLUME,
		csi.ControllerServiceCapability_RPC_CREATE_DELETE_VOLUME,
		csi.ControllerServiceCapability_RPC_CREATE_DELETE_SNAPSHOT,
		csi.ControllerServiceCapability_RPC_EXPAND_VOLUME,
		csi.ControllerServiceCapability_RPC_CLONE_VOLUME,
		csi.ControllerServiceCapability_RPC_PUBLISH_UNPUBLISH_VOLUME,
	})
	d.AddVolumeCapability([]csi.VolumeCapability_AccessMode_Mode{
		csi.VolumeCapability_AccessMode_MULTI_NODE_SINGLE_WRITER,
	})

	d.cs = NewControllerServer(d, cloud)
	d.ids = NewIdentityServer(d, cloud)
	d.ns = NewNodeServer(d, cloud, mount)

	return d
}

func (d *Driver) AddControllerCapability(cl []csi.ControllerServiceCapability_RPC_Type) {
	csc := make([]*csi.ControllerServiceCapability, 0, len(cl))
	for _, c := range cl {
		klog.Infof("Enabling controller capability %v", c.String())
		csc = append(csc, NewControllerServerCapability(c))
	}
	d.cap = csc
}

func (d *Driver) AddVolumeCapability(va []csi.VolumeCapability_AccessMode_Mode) []*csi.VolumeCapability_AccessMode {
	vca := make([]*csi.VolumeCapability_AccessMode, 0, len(va))
	for _, v := range va {
		klog.Infof("Enabling volume capability %v", v.String())
		vca = append(vca, NewVolumeCapabilityAccessMode(v))
	}
	d.vcap = vca
	return vca
}

func (d *Driver) ValidateControllerCapability(ca csi.ControllerServiceCapability_RPC_Type) error {
	if ca == csi.ControllerServiceCapability_RPC_UNKNOWN {
		return nil
	}
	for _, c := range d.cap {
		if ca == c.GetRpc().GetType() {
			return nil
		}
	}
	return status.Error(codes.InvalidArgument, fmt.Sprintf("Get RPC argument %s", string(ca)))
}

func (d *Driver) GetVolumeCapability() []*csi.VolumeCapability_AccessMode {
	return d.vcap
}

func (d *Driver) Run() {
	s := NewNonBlockingStorageServer(d.endpoint, d.ids, d.cs, d.ns)
	s.Start(d.endpoint, d.ids, d.cs, d.ns)
	s.Wait()
}
