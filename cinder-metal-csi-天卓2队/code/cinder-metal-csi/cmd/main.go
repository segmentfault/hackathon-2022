package main

import (
	"flag"
	"fmt"

	"github.com/kungze/cinder-metal-csi/pkg/driver"
	"github.com/kungze/cinder-metal-csi/pkg/mount"
	"github.com/kungze/cinder-metal-csi/pkg/openstack"
	"github.com/spf13/cobra"
	"k8s.io/klog/v2"
)

var (
	nodeID    string
	version   string
	endpoint  string
	cloudConf string
	v         string
)

func main() {
	command := &cobra.Command{
		Use:   "cinder-metal-csi",
		Short: "The openstack cinder metal csi driver",
		Run: func(cmd *cobra.Command, args []string) {
			handler(nodeID, version, endpoint, cloudConf)
		},
	}
	command.PersistentFlags().StringVar(&nodeID, "node-id", "127.0.0.1", "The csi driver node id")
	command.PersistentFlags().StringVar(&version, "version", "1.0.0", "The cinder csi driver version")
	command.PersistentFlags().StringVar(&endpoint, "endpoint", "unix:///csi/csi.sock", "The driver unix socket")
	command.PersistentFlags().StringVar(&cloudConf, "cloud-conf", "/etc/cloud/cloud.conf", "The openstack auth file")
	command.PersistentFlags().StringVar(&v, "v", "3", "The log level, default 3 ,support 1,2,3,4,5")
	klog.InitFlags(nil)
	_ = flag.Set("v", v)
	klog.Flush()
	err := command.Execute()
	if err != nil {
		klog.Fatalf(fmt.Sprintf("Exec command failed, %v", err))
	}
}

func handler(nodeID, version, endpoint, cloudConf string) {
	cloud, err := openstack.CreateOpenstackClient(cloudConf)
	if err != nil {
		klog.Fatalf("Get openstack client failed, &v", err)
	}
	mount := mount.NewNodeMount()
	driver := driver.NewDriver(nodeID, version, endpoint, cloud, mount)
	driver.Run()
}
