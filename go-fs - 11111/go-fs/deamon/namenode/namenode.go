package namenode

import (
	"context"
	"fmt"
	"github.com/google/uuid"
	grpc_prometheus "github.com/grpc-ecosystem/go-grpc-prometheus"
	"github.com/hashicorp/consul/api"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	datanode_pb "go-fs/proto/datanode"
	"go-fs/registration"
	"google.golang.org/grpc/credentials/insecure"
	"net/http"

	"go-fs/namenode"
	"go-fs/pkg/util"
	namenode_pb "go-fs/proto/namenode"
	"log"
	"net"

	"google.golang.org/grpc"
	"google.golang.org/grpc/health"
	"google.golang.org/grpc/health/grpc_health_v1"

	"os"
	"os/signal"
	"strconv"
	"syscall"
	"time"
)

func removeElementFromSlice(elements []string, index int) []string {
	return append(elements[:index], elements[index+1:]...)
}

// discoverDataNodes 发现 data node
func discoverDataNodes(nameNodeInstance *namenode.Service) error {
	nameNodeInstance.IdToDataNodes = make(map[string]util.DataNodeInstance)

	client, err := util.NewConsulClient(registration.ConsulHost, registration.ConsulPort)
	if err != nil {
		return nil
	}

	namenodeServices, err := client.Agent().ServicesWithFilter("Service==`datanode`")
	if err != nil {
		return nil
	}

	for id, namenodeInfo := range namenodeServices {
		log.Printf("Discovered DataNode %s, addr: %s:%d\n", id, namenodeInfo.Address, namenodeInfo.Port)
		dataNodeInstance := util.DataNodeInstance{
			Host:        namenodeInfo.Address,
			ServicePort: strconv.Itoa(namenodeInfo.Port),
		}
		nameNodeInstance.IdToDataNodes[id] = dataNodeInstance
	}

	return nil
}

func InitializeNameNodeUtil(serverPort int, blockSize int, replicationFactor int) {
	nameNodeInstance := namenode.NewService(uint64(blockSize), uint64(replicationFactor), uint16(serverPort))

	// 注册到consul
	register := registration.NewConsulRegister(
		registration.ConsulHost,
		registration.ConsulPort)

	id := uuid.New().String()
	err := register.RegisterCheckByGRPC(
		"namenode",
		id,
		registration.MYIP,
		serverPort,
		[]string{"namenode"})
	if err != nil {
		log.Printf("Namenode register to Consul failed, err: %s", err.Error())
		panic(err)
	}

	err = discoverDataNodes(nameNodeInstance)
	util.Check(err)

	log.Printf("BlockSize is %d\n", blockSize)
	log.Printf("Replication Factor is %d\n", replicationFactor)
	log.Printf("NameNode port is %d\n", serverPort)

	client, err := util.NewConsulClient(registration.ConsulHost, registration.ConsulPort)
	if err != nil {
		panic(err)
	}

	go SyncDataNodes(client, nameNodeInstance)
	//go heartbeatToDataNodes(nameNodeInstance)

	addr := ":" + strconv.Itoa(serverPort)

	listener, err := net.Listen("tcp", addr)
	defer listener.Close()
	util.Check(err)

	// 注册prometheus
	// Create a metrics registry.
	prometheusReg := prometheus.NewRegistry()
	// Create some standard server metrics.
	grpcMetrics := grpc_prometheus.NewServerMetrics()
	// Register standard server metrics to registry.
	prometheusReg.MustRegister(grpcMetrics)

	server := grpc.NewServer(
		grpc.StreamInterceptor(grpcMetrics.StreamServerInterceptor()),
		grpc.UnaryInterceptor(grpcMetrics.UnaryServerInterceptor()),
	)

	// Create a HTTP server for prometheus.
	httpServer := &http.Server{
		Handler: promhttp.HandlerFor(
			prometheusReg,
			promhttp.HandlerOpts{},
		),
		// TODO: HARD CODING
		Addr: ":9092",
	}

	namenode_pb.RegisterNameNodeServiceServer(server, nameNodeInstance)

	grpc_health_v1.RegisterHealthServer(server, health.NewServer())

	// Initialize all metrics.
	grpcMetrics.InitializeMetrics(server)

	// Start your http server for prometheus.
	go func() {
		if err := httpServer.ListenAndServe(); err != nil {
			log.Fatal("Unable to start a http server.")
		}
	}()

	go func() {
		if err := server.Serve(listener); err != nil {
			log.Printf(fmt.Sprintf("Server Serve failed in %s", addr), "err", err.Error())
			panic(err)
		}
	}()

	log.Println("NameNode daemon started on port: " + strconv.Itoa(serverPort))

	// graceful shutdown
	sig := make(chan os.Signal)
	signal.Notify(sig, syscall.SIGINT, syscall.SIGKILL)

	<-sig

	err = register.DeRegister(id)
	if err != nil {
		log.Printf("Namenode deregister from Consul failed, err: %s", err.Error())
	}

	server.GracefulStop()

}

// heartbeatToDataNodes 每五秒钟, 进行健康检查, 主要负责迁移数据
func heartbeatToDataNodes(nameNode *namenode.Service) {
	// TODO: 连接池复用连接
	//var dataNodeConn *grpc.ClientConn
	//var connectionErr error

	for range time.Tick(time.Second * 5) {
		for id, serviceInfo := range nameNode.IdToDataNodes {

			hostPort := serviceInfo.Host + ":" + serviceInfo.ServicePort
			dataNodeConn, connectionErr := grpc.Dial(hostPort, grpc.WithTransportCredentials(insecure.NewCredentials()))

			// 如果连接失败, 进行迁移
			if connectionErr != nil {
				log.Printf("Unable to connect to node %s, address: %s\n", id, hostPort)
				reDistributeError := nameNode.ReDistributeData(&namenode.ReDistributeDataRequest{DataNodeUri: hostPort})
				util.Check(reDistributeError)
				continue
			}

			// 如果心跳检测失败, 进行迁移
			dataNodeClient := datanode_pb.NewDataNodeClient(dataNodeConn)
			heartBeatResponse, hbErr := dataNodeClient.HeartBeat(context.Background(), &datanode_pb.HeartBeatRequest{Request: true})
			if hbErr != nil || !heartBeatResponse.Success {
				log.Printf("No heartbeat received from %s, address: %s\n", id, hostPort)
				reDistributeError := nameNode.ReDistributeData(&namenode.ReDistributeDataRequest{DataNodeUri: hostPort})
				util.Check(reDistributeError)
			}
		}
	}
}

// SyncDataNodes  每一秒在consul获取并更新datanode信息， 主要负责维护datanode list
func SyncDataNodes(client *api.Client, nameNode *namenode.Service) {
	log.Printf("datanodes: %v\n", nameNode.IdToDataNodes)

	// 每一秒连接一次consul
	for range time.Tick(time.Second) {
		namenodeServices, err := client.Agent().ServicesWithFilter("Service==`datanode`")
		if err != nil {
			continue
		}

		//log.Println("Sync name node")

		if len(namenodeServices) == len(nameNode.IdToDataNodes) {
			continue
		}

		// 存活的namenode数量与namenode记录的不相同, 则进行检查.
		// 1. 在consul中存在, namenode不存在 -> 添加到namenode
		diffs := ConsulDiffNameNode(nameNode.IdToDataNodes, namenodeServices)
		for _, d := range diffs {
			nameNode.IdToDataNodes[d] = util.DataNodeInstance{
				Host:        namenodeServices[d].Address,
				ServicePort: strconv.Itoa(namenodeServices[d].Port),
			}
			log.Println("Data Nodes changed! Added Data Node ", d)
		}

		// 2. namenode存在, 但是consul不存在 -> 删除namenode的记录 并迁移
		diffs = NameNodeDiffConsul(namenodeServices, nameNode.IdToDataNodes)
		for _, d := range diffs {
			deprecatedNameNode, ok := nameNode.IdToDataNodes[d]
			if !ok {
				continue
			}

			delete(nameNode.IdToDataNodes, d)
			log.Println("Data Nodes changed! Remove Data Node ", d)

			hostPort := deprecatedNameNode.Host + ":" + deprecatedNameNode.ServicePort

			// TODO: No any reaction if failing Re-distribute Data Now
			err := nameNode.ReDistributeData(&namenode.ReDistributeDataRequest{DataNodeUri: hostPort, DataNodeId: d})
			util.Check(err)

		}

		log.Println("datanodes: ")
		for id, info := range nameNode.IdToDataNodes {
			log.Printf("\tid: %s, addr: %s:%s", id, info.Host, info.ServicePort)
		}

	}
}

// ConsulDiffNameNode output A-B
func ConsulDiffNameNode(A map[string]util.DataNodeInstance, B map[string]*api.AgentService) []string {
	d := make([]string, 0)

	tmp := make(map[string]struct{})

	for a := range A {
		if _, ok := tmp[a]; !ok {
			tmp[a] = struct{}{}
		}
	}

	for b := range B {
		if _, ok := tmp[b]; !ok {
			d = append(d, b)
		}
	}

	return d
}

// NameNodeDiffConsul output A-B
func NameNodeDiffConsul(A map[string]*api.AgentService, B map[string]util.DataNodeInstance) []string {
	d := make([]string, 0)

	tmp := make(map[string]struct{})

	for a := range A {
		if _, ok := tmp[a]; !ok {
			tmp[a] = struct{}{}
		}
	}

	for b := range B {
		if _, ok := tmp[b]; !ok {
			d = append(d, b)
		}
	}

	return d
}
