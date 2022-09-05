package registration

import (
	"fmt"
	"go-fs/pkg/util"
	"log"

	"github.com/hashicorp/consul/api"
)

var ConsulHost = "127.0.0.1"
var ConsulPort = 8500

// MYIP ONLY FOR DEBUG PHASE TODO: REMOVE LATER
var MYIP = "192.168.0.197"

//type IRegistrar interface {
//	Register(name, id string, port int, tags []string) error
//	DeRegister(id string) error
//}

type ConsulRegister struct {
	Host string
	Port int
}

// NewConsulRegister 构造向host:port的consul注册器
func NewConsulRegister(host string, port int) ConsulRegister {
	cr := ConsulRegister{
		Host: host,
		Port: port,
	}
	return cr
}

// RegisterCheckByGRPC 将服务信息为参数的服务注册进cr, check method is grpc.
func (cr ConsulRegister) RegisterCheckByGRPC(name string, id string, host string, port int, tags []string) error {
	client, err := util.NewConsulClient(cr.Host, cr.Port)
	if err != nil {
		log.Printf("Register New Consul client failed, err: %s\n", err.Error())
		return err
	}

	// 注册信息准备
	registration := &api.AgentServiceRegistration{
		ID:      id,
		Name:    name,
		Tags:    tags,
		Address: host, // 服务自己的host
		Port:    port, // 服务自己的port
		Check: &api.AgentServiceCheck{
			GRPC:                           fmt.Sprintf("%s:%d", host, port), // 服务所提供的api接口
			Timeout:                        "5s",
			Interval:                       "3s",
			DeregisterCriticalServiceAfter: "10s",
		},
	}

	// 客户端使用api进行注册
	return client.Agent().ServiceRegister(registration)
}

func (cr ConsulRegister) DeRegister(id string) error {
	client, err := util.NewConsulClient(cr.Host, cr.Port)
	if err != nil {
		log.Printf("%s deregister from Consul failed, err: %s\n", id, err.Error())
		return err
	}

	// 客户端使用api进行反注册
	return client.Agent().ServiceDeregister(id)
}
