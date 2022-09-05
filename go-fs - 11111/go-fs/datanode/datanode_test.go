package datanode

import (
	"context"
	"go-fs/pkg/util"
	datanode_pb "go-fs/proto/datanode"
	"os"
	"strings"
	"testing"
)

// Test creating a DataNode Service
func TestDataNodeServiceCreation(t *testing.T) {
	testDataNodeService := new(Service)
	testDataNodeService.DataDirectory = "./"
	testDataNodeService.ServicePort = 8000

	if testDataNodeService.DataDirectory != "./" {
		t.Errorf("Unable to set DataDirectory correctly; Expected: %s, found: %s", "./", testDataNodeService.DataDirectory)
	}
	if testDataNodeService.ServicePort != 8000 {
		t.Errorf("Unable to set ServicePort correctly; Expected: %d, found: %d", 8000, testDataNodeService.ServicePort)
	}
}

// Test writing data in single file without dir paths within DataNode
func TestDataNodeServiceWriteFile(t *testing.T) {
	testDataNodeService := new(Service)
	testDataNodeService.DataDirectory = "./workdir/"
	testDataNodeService.ServicePort = 8000
	putRequestPayload := &datanode_pb.PutRequest{
		FilePath:         "./fileWithoutDir.txt",
		BlockId:          "1",
		Data:             "Hello world",
		ReplicationNodes: nil,
	}

	putResponse, err := testDataNodeService.Put(context.Background(), putRequestPayload)
	if err != nil {
		t.Error("Put Data filed")
	}

	if !putResponse.Success {
		t.Errorf("Unable to write data correctly; Expected: %t, found: %t", true, putResponse.Success)
	}

	// clean up
	filePathExist, err := util.PathExist("./workdir/fileWithoutDir1.txt")
	if err != nil {
		t.Error("Unexpected Error")
	}

	if !filePathExist {
		t.Error("Unable to write data correctly; No file putted")
	}

	err = os.RemoveAll("./workdir")
	if err != nil {
		t.Errorf("Unexpected Error! Please Clean up the test risidual file manually")
	}
}

// Test writing data in single file with dir paths within DataNode
func TestDataNodeServiceWriteFileWithDirPaths(t *testing.T) {
	testDataNodeService := new(Service)
	testDataNodeService.DataDirectory = "./workdir/"
	testDataNodeService.ServicePort = 8000

	putRequestPayload := &datanode_pb.PutRequest{
		FilePath:         "testfolder/testfolder2/fileWithDir.txt",
		BlockId:          "1",
		Data:             "Hello world",
		ReplicationNodes: nil,
	}

	putResponse, err := testDataNodeService.Put(context.Background(), putRequestPayload)
	if err != nil {
		t.Error("Put Data filed")
	}

	if !putResponse.Success {
		t.Errorf("Unable to write data correctly; Expected: %t, found: %t", true, putResponse.Success)
	}

	// clean up
	filePathExist, err := util.PathExist("./workdir/testfolder/testfolder2/fileWithDir1.txt")
	if err != nil {
		t.Error("Unexpected Error")
	}

	if !filePathExist {
		t.Error("Unable to write data correctly; No file putted")
	}

	err = os.RemoveAll("./workdir")
	if err != nil {
		t.Errorf("Unexpected Error! Please Clean up the test risidual file manually")
	}
}

//Test reading data within DataNode
func TestDataNodeServiceRead(t *testing.T) {
	testDataNodeService := new(Service)
	testDataNodeService.DataDirectory = "./workdir/"
	testDataNodeService.ServicePort = 8000
	putRequestPayload := &datanode_pb.PutRequest{
		FilePath:         "./fileWithoutDir.txt",
		BlockId:          "1",
		Data:             "Hello world",
		ReplicationNodes: nil,
	}

	testDataNodeService.Put(context.Background(), putRequestPayload)

	getRequestPayload := &datanode_pb.GetRequest{FilePath: "/fileWithoutDir.txt", BlockId: "1"}

	getResponse, err := testDataNodeService.Get(context.Background(), getRequestPayload)
	if err != nil {
		t.Error("Get Data filed")
	}

	if strings.Compare(getResponse.Data, "Hello world") != 0 {
		t.Errorf("Unable to read data correctly; Expected: %s, found: %s.", "Hello world", getResponse.Data)
	}

	// clean up
	filePathExist, err := util.PathExist("./workdir/fileWithoutDir1.txt")
	if err != nil {
		t.Error("Unexpected Error")
	}

	if !filePathExist {
		t.Error("Unable to write data correctly; No file putted")
	}

	err = os.RemoveAll("./workdir")
	if err != nil {
		t.Errorf("Unexpected Error! Please Clean up the test risidual file manually")
	}
}
