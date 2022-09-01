package openstack

import (
	"fmt"
	"strings"

	"gopkg.in/ini.v1"

	"github.com/gophercloud/gophercloud"
	"github.com/gophercloud/gophercloud/openstack"
	"github.com/gophercloud/gophercloud/openstack/blockstorage/noauth"
	"github.com/gophercloud/gophercloud/openstack/blockstorage/v3/attachments"
	"github.com/gophercloud/gophercloud/openstack/blockstorage/v3/snapshots"
	"github.com/gophercloud/gophercloud/openstack/blockstorage/v3/volumes"
	"k8s.io/klog/v2"
)

type Config struct {
	Global
	BlockStorage
}

type IOpenstack interface {
	CreateVolume(name, zone, volType, snapshotID, sourceVolID string, size int) (*volumes.Volume, error)
	DeleteVolume(volumeID string) error
	AttachVolume(volumeID, mountPoint, hostName string) error
	DetachVolume(volumeID string) error
	GetVolumeByName(volumeName string) ([]volumes.Volume, error)
	GetVolumeByID(volumeID string) (*volumes.Volume, error)
	GetAvailability() (string, error)
	ListVolume(maxLimit int32, marker string) ([]volumes.Volume, string, error)
	CreateSnapShot(name string, sourceVolumeID string) (*snapshots.Snapshot, error)
	DeleteSnapshot(snapshotID string) error
	ListSnapshot(filter map[string]string) ([]snapshots.Snapshot, string, error)
	GetSnapshotByID(snapshotID string) (*snapshots.Snapshot, error)
	ExpandVolume(volumeID string, status string, size int) error
	GetBsOpts() BlockStorage
	CheckBlockStorageAPI() error
	GetMaxVolumeLimit() int64
	CreateVolumeAttachment(volumeID, instanceID string) (*attachments.Attachment, error)
	GetVolumeAttachment(attachmentID string) (*attachments.Attachment, error)
	DeleteVolumeAttachment(volumeID string) error
	VolumeAttachmentComplete(attachmentID string) error
	GetAttachmentByVolumeID(volumeID string) (*attachments.Attachment, error)
}

type Global struct {
	AuthUrl           string                   `ini:"auth-url"`
	Username          string                   `ini:"username"`
	Password          string                   `ini:"password"`
	UserDomainName    string                   `ini:"user-domain-name"`
	ProjectDomainName string                   `ini:"project-domain-name"`
	ProjectName       string                   `ini:"project-name"`
	TenantName        string                   `ini:"tenant-name"`
	RegionName        string                   `ini:"region-name"`
	EndpointType      gophercloud.Availability `ini:"endpoint-type"`
}

type BlockStorage struct {
	AuthStrategy          string `ini:"auth-strategy"`
	CinderListenAddr      string `ini:"cinder-listen-addr"`
	NodeVolumeAttachLimit int64  `ini:"node-volume-attach-limit"`
}

type Openstack struct {
	BlockStorageClient *gophercloud.ServiceClient
	EsOpts             gophercloud.EndpointOpts
	BsOpts             BlockStorage
}

type EndpointOpts struct {
	Region       string
	Availability gophercloud.Availability
}

func loadCfg(cloudConf string) *Config {
	klog.V(3).Infof("Start load config file %s", cloudConf)
	config := &Config{}
	cfg, err := ini.Load(cloudConf)
	if err != nil {
		klog.Fatalf("Load config file failed, %v", err)
	}
	err = cfg.MapTo(&config)
	if err != nil {
		klog.Fatalf("Parse config file failed, %v", err)
	}
	return config
}

func CreateOpenstackClient(cloudConf string) (IOpenstack, error) {
	var err error
	config := loadCfg(cloudConf)
	provider, err := NewOpenstackClient(config)
	if err != nil {
		klog.Error(fmt.Sprintf("Get openstack provider client failed, %v", err))
	}
	esOpts := gophercloud.EndpointOpts{
		Region:       config.RegionName,
		Availability: config.EndpointType,
	}
	var blockStorageClient *gophercloud.ServiceClient
	if strings.ToLower(config.AuthStrategy) == "keystone" {
		blockStorageClient, err = openstack.NewBlockStorageV3(provider, esOpts)
		if err != nil {
			klog.Error(fmt.Sprintf("Get keystone openstack client failed, %v", err))
			return nil, err
		}
	} else if strings.ToLower(config.AuthStrategy) == "noauth" {
		blockStorageClient, err = noauth.NewBlockStorageNoAuthV3(provider, noauth.EndpointOpts{CinderEndpoint: config.CinderListenAddr})
		if err != nil {
			klog.Error(fmt.Sprintf("Get noauth openstack client failed, %v", err))
			return nil, err
		}
	}
	OsInstance := &Openstack{
		BlockStorageClient: blockStorageClient,
		BsOpts: BlockStorage{
			AuthStrategy:          config.AuthStrategy,
			CinderListenAddr:      config.CinderListenAddr,
			NodeVolumeAttachLimit: config.NodeVolumeAttachLimit,
		},
		EsOpts: esOpts,
	}
	return OsInstance, nil
}

func NewOpenstackClient(cfg *Config) (*gophercloud.ProviderClient, error) {
	opts := gophercloud.AuthOptions{
		IdentityEndpoint: cfg.AuthUrl,
		Username:         cfg.Username,
		Password:         cfg.Password,
		TenantName:       cfg.TenantName,
		DomainName:       cfg.ProjectDomainName,
		AllowReauth:      true,
	}
	provider, err := openstack.AuthenticatedClient(opts)
	if err != nil {
		klog.Error(fmt.Sprintf("Request openstack provider client failed, %v", err))
		return nil, err
	}
	return provider, nil
}
