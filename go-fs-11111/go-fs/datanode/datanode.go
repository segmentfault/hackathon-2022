package datanode

import (
	"bufio"
	"context"
	"go-fs/pkg/e"
	"go-fs/pkg/util"
	datanode_pb "go-fs/proto/datanode"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"io/ioutil"
	"log"
	"os"
	"path"
)

type Service struct {
	datanode_pb.UnimplementedDataNodeServer

	DataDirectory string
	ServicePort   uint16
	NameNodeHost  string
	NameNodePort  uint16
}

type DataNodePutRequest struct {
	FilePath         string
	BlockId          string
	Data             string
	ReplicationNodes []util.DataNodeInstance
}

type DataNodeGetRequest struct {
	//BlockId string
	FilePath string
}

type DataNodeWriteStatus struct {
	Status bool
}

type DataNodeData struct {
	Data string
}

type NameNodePingRequest struct {
	Host string
	Port uint16
}

type NameNodePingResponse struct {
	Ack bool
}

func (dn *Service) Ping(ctx context.Context, req *datanode_pb.PingRequest) (*datanode_pb.PingResponse, error) {
	var res datanode_pb.PingResponse

	dn.NameNodeHost = req.Host
	dn.NameNodePort = uint16(req.Port)
	log.Printf("Received ping from NameNode, recorded as {NameNodeHost: %s, NameNodePort: %d}\n", dn.NameNodeHost, dn.NameNodePort)

	res.Ack = true
	return &res, nil
}

func (dn *Service) HeartBeat(ctx context.Context, req *datanode_pb.HeartBeatRequest) (*datanode_pb.HeartBeatResponse, error) {
	var res datanode_pb.HeartBeatResponse

	if req.Request {
		log.Println("Received heartbeat from NameNode")
		res.Success = true
		return &res, nil
	}

	return nil, e.ErrHeartBeat
}

func (dn *Service) ForwardForReplication(ctx context.Context, req *datanode_pb.ForwardForReplicationRequest) (*datanode_pb.PutResponse, error) {
	var res datanode_pb.PutResponse

	if !req.PutResponse.Success {
		return nil, e.ErrFileDuplication
	}

	blockId := req.PutRequest.BlockId
	blockAddresses := req.PutRequest.ReplicationNodes

	if len(blockAddresses) == 0 {
		return &datanode_pb.PutResponse{Success: true}, nil
	}

	startingDataNode := blockAddresses[0]
	remainingDataNodes := blockAddresses[1:]

	otherDataNodeConn, err := grpc.Dial(startingDataNode.Host+":"+startingDataNode.ServicePort, grpc.WithTransportCredentials(insecure.NewCredentials()))
	defer otherDataNodeConn.Close()
	util.Check(err)

	dataNodeInstance := datanode_pb.NewDataNodeClient(otherDataNodeConn)

	payloadRequest := &datanode_pb.PutRequest{
		FilePath:         req.PutRequest.FilePath,
		BlockId:          blockId,
		Data:             req.PutRequest.Data,
		ReplicationNodes: remainingDataNodes,
	}

	putResponse, err := dataNodeInstance.Put(context.Background(), payloadRequest)
	util.Check(err)

	res.Success = putResponse.Success

	return &res, nil
}

func (dn *Service) Put(ctx context.Context, req *datanode_pb.PutRequest) (*datanode_pb.PutResponse, error) {
	var res datanode_pb.PutResponse

	filePath, fileName := path.Split(path.Join(dn.DataDirectory, req.FilePath))
	filePathExist, err := util.PathExist(filePath)
	util.Check(err)

	if !filePathExist {
		err := os.MkdirAll(filePath, 0750)
		util.Check(err)
	}

	name, ext := util.SplitFileNameAndExt(fileName)
	finalName := name + req.BlockId + ext

	fileWriteHandler, err := os.Create(path.Join(filePath, finalName))
	util.Check(err)
	defer fileWriteHandler.Close()

	fileWriter := bufio.NewWriter(fileWriteHandler)
	_, err = fileWriter.WriteString(req.Data)
	util.Check(err)
	fileWriter.Flush()
	res.Success = true

	return dn.ForwardForReplication(
		ctx,
		&datanode_pb.ForwardForReplicationRequest{
			PutRequest:  req,
			PutResponse: &res,
		})
}

func (dn *Service) Get(ctx context.Context, req *datanode_pb.GetRequest) (*datanode_pb.GetResponse, error) {
	var res datanode_pb.GetResponse

	filePath := path.Join(dn.DataDirectory, req.FilePath)

	pathWithoutExt, ext := util.SplitFileNameAndExt(filePath)

	finalPath := pathWithoutExt + req.BlockId + ext

	filePathExist, err := util.PathExist(finalPath)
	util.Check(err)

	if !filePathExist {
		return nil, e.ErrFileDoesNotExist
	}

	dataBytes, err := ioutil.ReadFile(finalPath)
	util.Check(err)

	res.Data = string(dataBytes)

	return &res, nil
}
