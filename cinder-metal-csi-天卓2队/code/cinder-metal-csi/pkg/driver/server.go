package driver

import (
	"context"
	"fmt"
	"net"
	"os"
	"sync"

	"github.com/container-storage-interface/spec/lib/go/csi"
	"github.com/kubernetes-csi/csi-lib-utils/protosanitizer"
	"google.golang.org/grpc"
	"k8s.io/klog/v2"
)

type NonBlockingStorageServer interface {
	Start(endpoint string, ids csi.IdentityServer, cs csi.ControllerServer, ns csi.NodeServer)
	Wait()
	Stop()
	ForceStop()
}

func NewNonBlockingStorageServer(endpoint string, ids csi.IdentityServer, cs csi.ControllerServer, ns csi.NodeServer) NonBlockingStorageServer {
	return &nonBlockingStorageServer{
		endpoint: endpoint,
		ids:      ids,
		cs:       cs,
		ns:       ns,
	}
}

type nonBlockingStorageServer struct {
	wg       sync.WaitGroup
	server   grpc.Server
	endpoint string
	ids      csi.IdentityServer
	cs       csi.ControllerServer
	ns       csi.NodeServer
}

func (n *nonBlockingStorageServer) Start(endpoint string, ids csi.IdentityServer, cs csi.ControllerServer, ns csi.NodeServer) {
	n.wg.Add(1)
	go n.Serve(endpoint, ids, cs, ns)
}

func (n *nonBlockingStorageServer) Wait() {
	n.wg.Wait()
}

func (n *nonBlockingStorageServer) Stop() {
	n.server.GracefulStop()
}

func (n *nonBlockingStorageServer) ForceStop() {
	n.server.Stop()
}

func (n *nonBlockingStorageServer) Serve(endpoint string, ids csi.IdentityServer, cs csi.ControllerServer, ns csi.NodeServer) {
	protocol, addr, err := ParseEndpoint(endpoint)
	if err != nil {
		klog.Fatalf(err.Error())
	}
	if protocol == "unix" {
		if err := os.Remove("/" + addr); err != nil && !os.IsNotExist(err) {
			klog.Fatalf("Failed to remove %s, error: %s", addr, err.Error())
		}
	}
	listeners, err := net.Listen(protocol, addr)
	if err != nil {
		klog.Fatalf("Failed to listening %v", err)
	}
	opts := []grpc.ServerOption{
		grpc.UnaryInterceptor(logGRPC),
	}
	server := grpc.NewServer(opts...)
	if ids != nil {
		csi.RegisterIdentityServer(server, ids)
	}
	if cs != nil {
		csi.RegisterControllerServer(server, cs)
	}
	if ns != nil {
		csi.RegisterNodeServer(server, ns)
	}
	err = server.Serve(listeners)
	if err != nil {
		klog.Fatalf("Failed to start server error: %v", err)
	}
}

func logGRPC(ctx context.Context, req interface{}, info *grpc.UnaryServerInfo, handler grpc.UnaryHandler) (interface{}, error) {
	resp, err := handler(ctx, req)
	if err != nil {
		klog.Error(fmt.Sprintf("GRPC error: %v", err))
	} else {
		klog.V(3).Infof("GRPC response: %s", protosanitizer.StripSecrets(resp))
	}
	return resp, nil
}
