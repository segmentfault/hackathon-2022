package e

import "errors"

var (
	ErrHeartBeat        = errors.New("检测心跳错误")
	ErrFileDuplication  = errors.New("文件备份失败")
	ErrFileDoesNotExist = errors.New("文件不存在")
)
