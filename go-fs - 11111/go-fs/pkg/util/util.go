package util

import (
	"fmt"
	"github.com/hashicorp/consul/api"
	"log"
	"os"
	"path"
	"strings"
)

type DataNodeInstance struct {
	Host        string
	ServicePort string
}

func Check(e error) {
	if e != nil {
		log.Println(e)
		//panic(e)
	}
}

func CheckStatus(e bool) {
	if !e {
		log.Println(e)
		//panic(e)
	}
}

// PathExist 判断文件或目录是否存在
// 如果 error != nil, 则出现了不是不存在的错误, 此时无法判断文件是否存在
// bool 当error == nil 时, 用来判断文件或目录是否存在
// 所以你总是想:
// filePathExist, err := util.PathExist(filePath)
// if err != nil {
//   panic(err)
// }
// if !filePathExist {
// // Do something
//  err := os.MkdirAll(filePath, 0750)
//  if err != nil {
//  panic(err)
//  }
// }
func PathExist(path string) (bool, error) {
	_, err := os.Stat(path)
	if err == nil {
		return true, nil
	}
	//isnotexist来判断，是不是不存在的错误
	if os.IsNotExist(err) { //如果返回的错误类型使用os.isNotExist()判断为true，说明文件或者文件夹不存在
		return false, nil
	}
	return false, err //如果有错误了，但是不是不存在的错误，所以把这个错误原封不动的返回
}

func SplitFileNameAndExt(fileName string) (name, ext string) {
	ext = path.Ext(fileName)

	name = strings.TrimSuffix(fileName, ext)

	return
}

func NewConsulClient(host string, port int) (*api.Client, error) {
	// 创建consul地址的的客户端
	defaultConfig := api.DefaultConfig()
	defaultConfig.Address = fmt.Sprintf("%s:%d", host, port)

	// 创建consul地址的的客户端
	client, err := api.NewClient(defaultConfig)
	if err != nil {
		return nil, err
	}

	return client, nil
}
