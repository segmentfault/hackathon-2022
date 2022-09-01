package client

import (
	"bufio"
	"go-fs/client"
	"go-fs/pkg/util"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"net"
	"os"
)

func PutHandler(nameNodeAddress string, sourceFilePath string, destFilePath string) bool {
	rpcClient, err := initializeClientUtil(nameNodeAddress)
	util.Check(err)
	defer rpcClient.Close()
	return client.Put(rpcClient, sourceFilePath, destFilePath)
}

func GetHandler(nameNodeAddress string, sourceFilePath string) (string, bool) {
	rpcClient, err := initializeClientUtil(nameNodeAddress)
	util.Check(err)
	defer rpcClient.Close()
	return client.Get(rpcClient, sourceFilePath)
}

func StatHandler(nameNodeAddress string, destFilePath string) (string, bool) {
	rpcClient, err := initializeClientUtil(nameNodeAddress)
	util.Check(err)
	defer rpcClient.Close()
	return client.Stat(rpcClient, destFilePath)
}

func initializeClientUtil(nameNodeAddress string) (*grpc.ClientConn, error) {
	host, port, err := net.SplitHostPort(nameNodeAddress)
	util.Check(err)

	return grpc.Dial(host+":"+port, grpc.WithTransportCredentials(insecure.NewCredentials()))
}

func HandlePutOperation(nameNodeAddress string, sourceFilePath string, destFilePath string) bool {
	return PutHandler(nameNodeAddress, sourceFilePath, destFilePath)
}

func HandleGetOperation(nameNodeAddress string, sourceFilePath string, destFilePath string) bool {
	contents, status := GetHandler(nameNodeAddress, sourceFilePath)
	if !status {
		return false
	}
	fileWriteHandler, err := os.Create(destFilePath)
	defer fileWriteHandler.Close()
	util.Check(err)

	fileWriter := bufio.NewWriter(fileWriteHandler)
	_, err = fileWriter.WriteString(contents)
	util.Check(err)
	fileWriter.Flush()

	return true
}

func HandleStatOperation(nameNodeAddress string, destFilePath string) (stats string, status bool) {
	return "", true
}
