package client

import (
	"context"
	"go-fs/namenode"
	"go-fs/pkg/converter"
	"go-fs/pkg/util"
	datanode_pb "go-fs/proto/datanode"
	namenode_pb "go-fs/proto/namenode"
	"os"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

func Put(nameNodeConn *grpc.ClientConn, sourceFilePath string, destFilePath string) (putStatus bool) {
	defer nameNodeConn.Close()

	nameNodeInstance := namenode_pb.NewNameNodeServiceClient(nameNodeConn)

	fileSizeHandler, err := os.Stat(sourceFilePath)
	if err != nil {
		putStatus = false
		return
	}

	// 拿到size为了给文件分片(block), 每个block会被分配到不同的data node中
	fileSize := uint64(fileSizeHandler.Size())

	namenodeWriteRequest := &namenode_pb.WriteRequest{FileName: destFilePath, FileSize: fileSize}

	// namenode 的 writeData并不是真的写入, 返回的reply包含每一个文件的block应该被写入的data node 的地址
	writeResponse, err := nameNodeInstance.WriteData(context.Background(), namenodeWriteRequest)
	util.Check(err)

	var blockSize uint64

	namenodeGetBlockSizeRequest := &namenode_pb.GetBlockSizeRequest{Request: true}

	blockSizeResponse, err := nameNodeInstance.GetBlockSize(context.Background(), namenodeGetBlockSizeRequest)
	util.Check(err)

	blockSize = blockSizeResponse.BlockSize

	fileHandler, err := os.Open(sourceFilePath)
	util.Check(err)

	// buffer 每次只读全局设定的block size 或 更少的数据
	dataStagingBytes := make([]byte, blockSize)
	for _, pbMetaData := range writeResponse.NameNodeMetaDataList {
		metaData := converter.Pb2NameNodeMetaData(pbMetaData)

		// n代表着实际读到的byte数
		n, err := fileHandler.Read(dataStagingBytes)
		util.Check(err)
		dataStagingBytes = dataStagingBytes[:n]

		blockId := metaData.BlockId
		blockAddresses := metaData.BlockAddresses

		startingDataNode := blockAddresses[0]
		remainingDataNodes := blockAddresses[1:]

		// data node 此时真正的准备写入数据
		dataNodeConn, rpcErr := grpc.Dial(
			startingDataNode.Host+":"+startingDataNode.ServicePort,
			grpc.WithTransportCredentials(insecure.NewCredentials()),
		)
		defer dataNodeConn.Close()
		util.Check(rpcErr)

		dataNodeInstance := datanode_pb.NewDataNodeClient(dataNodeConn)

		var remainingPBDataNodes []*datanode_pb.DataNodeInstance

		for _, remainingPBDataNode := range remainingDataNodes {
			remainingPBDataNodes = append(remainingPBDataNodes, namenode.DataNodeInstance2PB(remainingPBDataNode))
		}

		request := &datanode_pb.PutRequest{
			FilePath:         destFilePath,
			BlockId:          blockId,
			Data:             string(dataStagingBytes),
			ReplicationNodes: remainingPBDataNodes,
		}

		// 写入数据
		putResponse, err := dataNodeInstance.Put(context.Background(), request)
		util.Check(rpcErr)

		putStatus = putResponse.Success
	}
	return
}

func Get(nameNodeConn *grpc.ClientConn, sourceFilePath string) (fileContents string, getStatus bool) {
	defer nameNodeConn.Close()

	nameNodeInstance := namenode_pb.NewNameNodeServiceClient(nameNodeConn)

	nameNodeReadRequest := &namenode_pb.ReadRequest{FileName: sourceFilePath}

	// name node 并不是真的读数据, 返回的reply包含每一个文件的block 存放在data node 的地址
	readResponse, err := nameNodeInstance.ReadData(context.Background(), nameNodeReadRequest)
	if err != nil {
		getStatus = false
		return
	}

	for _, pbMetaData := range readResponse.NameNodeMetaDataList {
		metaData := converter.Pb2NameNodeMetaData(pbMetaData)

		//blockId := metaData.BlockId
		blockAddresses := metaData.BlockAddresses
		// block被获取的标志位
		blockFetchStatus := false

		// 每一个block, 都被备份了x次, 但只需要拿一次.
		for _, selectedDataNode := range blockAddresses {
			// data node 此时真正的准备读数据
			dataNodeConn, rpcErr := grpc.Dial(
				selectedDataNode.Host+":"+selectedDataNode.ServicePort,
				grpc.WithTransportCredentials(insecure.NewCredentials()),
			)
			defer dataNodeConn.Close()
			util.Check(rpcErr)

			dataNodeInstance := datanode_pb.NewDataNodeClient(dataNodeConn)

			request := &datanode_pb.GetRequest{
				FilePath: sourceFilePath,
				BlockId:  metaData.BlockId,
			}

			// 读数据
			getResponse, rpcErr := dataNodeInstance.Get(context.Background(), request)
			if rpcErr != nil {
				continue
			}
			// 追加内容
			fileContents += getResponse.Data
			// 读取成功后, 将标志位置为true, 此block不再获取
			blockFetchStatus = true
			break
		}

		// 如果一个block重试x次都没有拿到数据, 则返回文件没有get到
		if !blockFetchStatus {
			getStatus = false
			return
		}
	}

	// 所有block被拿到, 返回文件成功get到
	getStatus = true
	return
}

func Stat(nameNodeConn *grpc.ClientConn, destFilePath string) (stats string, statStatus bool) {
	return "", true
}
