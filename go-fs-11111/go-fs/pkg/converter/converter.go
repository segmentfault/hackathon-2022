package converter

import (
	"go-fs/namenode"
	"go-fs/pkg/util"
	datanode_pb "go-fs/proto/datanode"
	namenode_pb "go-fs/proto/namenode"
)

func Pb2DataNodeInstance(dni *datanode_pb.DataNodeInstance) util.DataNodeInstance {
	dataNodeInstance := util.DataNodeInstance{
		Host:        dni.Host,
		ServicePort: dni.ServicePort,
	}
	return dataNodeInstance
}

func Pb2NameNodeMetaData(nnmd *namenode_pb.NameNodeMetaData) namenode.NameNodeMetaData {
	var blockAddresses []util.DataNodeInstance
	for _, dni := range nnmd.BlockAddresses {
		blockAddresses = append(blockAddresses, Pb2DataNodeInstance(dni))
	}

	nameNodeMetaData := namenode.NameNodeMetaData{
		BlockId:        nnmd.BlockId,
		BlockAddresses: blockAddresses,
	}

	return nameNodeMetaData
}
