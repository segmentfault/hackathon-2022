package driver

import (
	"github.com/container-storage-interface/spec/lib/go/csi"
	"github.com/kungze/cinder-metal-csi/pkg/openstack"
	"golang.org/x/net/context"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type identityServer struct {
	driver *Driver
	cloud  openstack.IOpenstack
}

func (i *identityServer) GetPluginInfo(ctx context.Context, req *csi.GetPluginInfoRequest) (*csi.GetPluginInfoResponse, error) {
	name := i.driver.name
	if name == "" {
		return nil, status.Error(codes.Unavailable, "GetPluginInfo: The cinder csi driver name not exists")
	}
	version := i.driver.version
	if version == "" {
		return nil, status.Error(codes.Unavailable, "GetPluginInfo: The cinder csi driver version not exists")
	}

	return &csi.GetPluginInfoResponse{
		Name:          name,
		VendorVersion: version,
	}, nil
}

func (i *identityServer) GetPluginCapabilities(ctx context.Context, req *csi.GetPluginCapabilitiesRequest) (*csi.GetPluginCapabilitiesResponse, error) {
	return &csi.GetPluginCapabilitiesResponse{
		Capabilities: []*csi.PluginCapability{
			{
				Type: &csi.PluginCapability_Service_{
					Service: &csi.PluginCapability_Service{
						Type: csi.PluginCapability_Service_CONTROLLER_SERVICE,
					},
				},
			},
			{
				Type: &csi.PluginCapability_VolumeExpansion_{
					VolumeExpansion: &csi.PluginCapability_VolumeExpansion{
						Type: csi.PluginCapability_VolumeExpansion_ONLINE,
					},
				},
			},
			{
				Type: &csi.PluginCapability_VolumeExpansion_{
					VolumeExpansion: &csi.PluginCapability_VolumeExpansion{
						Type: csi.PluginCapability_VolumeExpansion_OFFLINE,
					},
				},
			},
		},
	}, nil
}

func (i *identityServer) Probe(ctx context.Context, req *csi.ProbeRequest) (*csi.ProbeResponse, error) {
	err := i.cloud.CheckBlockStorageAPI()
	if err != nil {
		return nil, status.Errorf(codes.Internal, "The openstack cinder api check failed, err: %v", err)
	}
	return &csi.ProbeResponse{}, nil
}

func NewIdentityServer(d *Driver, cloud openstack.IOpenstack) csi.IdentityServer {
	return &identityServer{
		driver: d,
		cloud:  cloud,
	}
}
