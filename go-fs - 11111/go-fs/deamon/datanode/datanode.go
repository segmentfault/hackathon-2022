package datanode

import (
	"errors"
	"fmt"
	"go-fs/datanode"
	datanode_pb "go-fs/proto/datanode"
	"go-fs/registration"
	"log"
	"net"
	"os"
	"os/signal"
	"strconv"
	"syscall"

	"github.com/google/uuid"
	"google.golang.org/grpc"
	"google.golang.org/grpc/health"
	"google.golang.org/grpc/health/grpc_health_v1"
)

func InitializeDataNodeUtil(serverPort int, dataLocation string) {
	// 注册到consul
	register := registration.NewConsulRegister(
		registration.ConsulHost,
		registration.ConsulPort)

	id := uuid.New().String()
	err := register.RegisterCheckByGRPC(
		"datanode",
		id,
		registration.MYIP,
		serverPort,
		[]string{"datanode"})
	if err != nil {
		log.Printf("Datanode register to Consul failed, err: %s", err.Error())
		panic(err)
	}

	dataNodeInstance := new(datanode.Service)
	dataNodeInstance.DataDirectory = dataLocation
	dataNodeInstance.ServicePort = uint16(serverPort)

	log.Printf("Data storage location is %s\n", dataLocation)

	server := grpc.NewServer()
	datanode_pb.RegisterDataNodeServer(server, dataNodeInstance)

	grpc_health_v1.RegisterHealthServer(server, health.NewServer())

	// 创建监听器, 如果端口被占用, 则端口号+1, 直至找到空闲端口
	var listener net.Listener
	initErr := errors.New("init")

	for initErr != nil {
		listener, initErr = net.Listen("tcp", ":"+strconv.Itoa(serverPort))
		serverPort += 1
	}
	log.Printf("DataNode port is %d\n", serverPort-1)
	defer listener.Close()

	//// 注册prometheus
	//// Create a metrics registry.
	//reg := prometheus.NewRegistry()
	//// Create some standard client metrics.
	//grpcMetrics := grpc_prometheus.NewClientMetrics()
	//// Register client metrics to registry.
	//reg.MustRegister(grpcMetrics)
	//// Create  insecure gRPC channel to communicate with the server.

	//// Create  HTTP server for prometheus.
	//httpServer := &http.Server{
	//Handler: promhttp.HandlerFor(
	//reg,
	//promhttp.HandlerOpts{}),
	//// TODO: HARD CODING
	//Addr: ":9094",
	//}

	//// Start http server for prometheus.
	//go func() {
	//if err := httpServer.ListenAndServe(); err != nil {
	//log.Fatal("Unable to start a http server.")
	//}
	//}()

	go func() {
		if err := server.Serve(listener); err != nil {
			log.Printf(fmt.Sprintf("Server Serve failed in %s", ":"+strconv.Itoa(serverPort)), "err", err.Error())
			panic(err)
		}
	}()

	log.Printf("DataNode daemon %s started on port: %s\n", id, strconv.Itoa(serverPort-1))

	// graceful shutdown
	sig := make(chan os.Signal)
	signal.Notify(sig, syscall.SIGINT, syscall.SIGKILL)

	<-sig

	err = register.DeRegister(id)
	if err != nil {
		log.Printf("Datanode deregister from Consul failed, err: %s", err.Error())
	}

	server.GracefulStop()
}
