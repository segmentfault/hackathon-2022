# 学习指南

## 学习顺序

namenode -> datanode -> deamon/namenode -> deamon/datanode -> client and (deamon/client) -> main

## 使用

在 go-fs 目录: `make build`

创建三个隐藏文件夹

```bash
mkdir .dndata1/ .dndata2/ .dndata3/
```

启动三个 data node 端口为 7001, 7002, 7003

```bash
./go-fs datanode --port 7001 --data-location .dndata1/
./go-fs datanode --port 7002 --data-location .dndata2/
./go-fs datanode --port 7003 --data-location .dndata3/
```

在另一个 terminal 下, 启动 name node, 端口为 7000

```bash
./go-fs namenode --port 7000  --block-size 10 --replication-factor 2
```

创建一个文件 hello.txt

```bash
echo "hello" > hello.txt && cat hello.txt
```

在另一个 terminal 下使用客户端 :

PUT

```bash
./go-fs client --namenode localhost:7000 --operation put --source-file-path ./hello.txt --dest-file-path /hello/hello2/hello2.txt
```

GET:

```bash
./go-fs client --namenode localhost:7000 --operation get --source-file-path /hello/hello2/hello2.txt --dest-file-path ./fromserver.txt
```

## Use it like a remote file system !

You can put the `binary file` to your server and then launch datanodes and namenode, then you can use this system locally like:

```bash
./go-fs client --namenode username@server_ip:7000 --operation put --source-file-path ./hello.txt --dest-file-path /hello/hello2/hello2.txt

or

./go-fs client --namenode username@server_ip:7000 --operation get --source-file-path /hello/hello2/hello2.txt --dest-file-path ./fromserver.txt

```
