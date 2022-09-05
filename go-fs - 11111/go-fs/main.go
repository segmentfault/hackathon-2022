package main

import (
	"flag"
	"go-fs/deamon/client"
	"go-fs/deamon/datanode"
	"go-fs/deamon/namenode"
	"log"
	"os"
)

func main() {
	dataNodeCommand := flag.NewFlagSet("datanode", flag.ExitOnError)
	nameNodeCommand := flag.NewFlagSet("namenode", flag.ExitOnError)
	clientCommand := flag.NewFlagSet("client", flag.ExitOnError)

	dataNodePortPtr := dataNodeCommand.Int("port", 7000, "DataNode communication port")
	dataNodeDataLocationPtr := dataNodeCommand.String("data-location", ".", "DataNode data storage location")

	nameNodePortPtr := nameNodeCommand.Int("port", 9000, "NameNode communication port")
	nameNodeBlockSizePtr := nameNodeCommand.Int("block-size", 32, "Block size to store")
	nameNodeReplicationFactorPtr := nameNodeCommand.Int("replication-factor", 1, "Replication factor of the system")

	clientNameNodePortPtr := clientCommand.String("namenode", "localhost:9000", "NameNode communication port\n\t required: ALL")
	clientOperationPtr := clientCommand.String("operation", "", "Operation to perform\n\t required: ALL")
	clientSourceFilePathPtr := clientCommand.String("source-file-path", "", "Source path of the file\n\t required: GET, PUT")
	clientDestFilePathPtr := clientCommand.String("dest-file-path", "", "Destination path of the file\n\t required: GET, PUT, STAT")

	if len(os.Args) < 2 {
		log.Println("sub-command is required")
		os.Exit(1)
	}

	switch os.Args[1] {
	case "datanode":
		_ = dataNodeCommand.Parse(os.Args[2:])
		datanode.InitializeDataNodeUtil(*dataNodePortPtr, *dataNodeDataLocationPtr)

	case "namenode":
		_ = nameNodeCommand.Parse(os.Args[2:])
		namenode.InitializeNameNodeUtil(*nameNodePortPtr, *nameNodeBlockSizePtr, *nameNodeReplicationFactorPtr)

	case "client":
		_ = clientCommand.Parse(os.Args[2:])

		switch {
		// put
		case *clientOperationPtr == "put":
			if *clientNameNodePortPtr == "" || *clientSourceFilePathPtr == "" || *clientDestFilePathPtr == "" {
				log.Println("Need required arguments, use \"go-fs client -help\" for more details")
				return
			}
			status := client.HandlePutOperation(*clientNameNodePortPtr, *clientSourceFilePathPtr, *clientDestFilePathPtr)
			log.Printf("Put status: %t\n", status)

		// get
		case *clientOperationPtr == "get":
			if *clientNameNodePortPtr == "" || *clientSourceFilePathPtr == "" || *clientDestFilePathPtr == "" {
				log.Println("Need required arguments, use \"go-fs client -help\" for more details")
				return
			}
			status := client.HandleGetOperation(*clientNameNodePortPtr, *clientSourceFilePathPtr, *clientDestFilePathPtr)
			log.Printf("Get status: %t\n", status)
			if !status {
				log.Println("Please check remote file path")
				return
			}

		// stat
		case *clientOperationPtr == "stat":
			if *clientNameNodePortPtr == "" || *clientDestFilePathPtr == "" {
				log.Println("Need required arguments, use \"go-fs client -help\" for more details")
				return
			}

			client.HandleStatOperation(*clientNameNodePortPtr, *clientDestFilePathPtr)

		}
	}
}
