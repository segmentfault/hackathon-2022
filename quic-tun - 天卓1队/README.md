# quic-tun

### 介绍

一个快速且安全的 TCP 隧道工具，能加速弱网环境下（如网络有丢包）TCP 的转发性能。

#### 原理概述

quic-tun 项目包含两个程序，quictun-server 和 quictun-client，其核心原理可以用一句话概述：quictun-server 把 server 端 TCP/UNIX-SOCKET 应用的传输层协议转为 [QUIC](https://www.chromium.org/quic/)，
然后 quictun-client 再把 QUIC 转为 TCP/UNIX-SOCKET，客户端应用程序只需要连接到 quictun-client 服务就可以和 server 端应用程序交互了。

因为 quictun-server 和 quictun-client 之间的网络传输使用的是 QUIC 协议，所以拥有了 QUIC 协议的所有能力，包括重传算法和拥塞控制算法等，进而能够轻松的应对复杂网络环境。因此对于整体的网络状况会有极大优化。关于 quic-tun 对网络传输的优化，我们做了一些测试，并编写了[测试报告](https://kungze.github.io/documents/quic-tun/performance-test/)，
想了解更多信息，请[点击查看](https://kungze.github.io/documents/quic-tun/performance-test/)。

#### 特点摘要

- 提高网络传输性能，特别是在存在网络丢包的环境下
- 对于内网中多个服务需要映射到外网的情况，可通过仅映射 quic-tun 服务端的方式实现，它只需要暴露一个 UDP 端口即可，减少因对外暴露的端口数过多而造成的安全问题
- 支持把 TCP 通过隧道转为 UNIX-SOCKET，反之 UNIX-SOCKET 可通过隧道转换为 TCP
- 客户端服务端采用 TLS1.3 加密通讯，保证数据的安全性，不需要 TCP 应用程序本身支持 TLS 即可实现安全通讯
- 提供 RestfulAPI 接口，能够获取隧道中流量的相关信息，目前仅支持识别 spice 协议，后续会支持更多的协议
- 可运行在 Linux、windows 64-bit 系统上
- 多线程、使用所有的 CPU
- 没有外部依赖，纯粹的 GO 语言编写
- 支持命令行和配置文件

为了能够让更多的开源爱好者参与进来，我们已经把 [quic-tun](https://github.com/kungze/quic-tun) 在 github 上开源。同时我们希望通过这次参赛的机会，能够得到各位专家评委老师的指点和建议，以及大家的关注。

#### 软件架构

quic-tun 不仅有优化 TCP 传输的作用，他还能把 TCP 应用转为 UNIX-SOCKET 应用，UNIX-SOCKET 应用转为 TCP 应用，其架构图如下：

![quic-tun](https://lplearn-note-pic.oss-cn-beijing.aliyuncs.com/note/2022/quic-tun.png)

#### 概念解释

* **tunnel**：隧道，`quic-tun`会为每个 TCP 连接创建一个 tunnel，一个 tunnel 对应一个 QUIC 的 `connection`（quic-tun 为实现多路复用提出的一个概念）。
* **client endpoint**：隧道的 client 端点，监听在 TCP 端口或者 UNIX-SOCKET 文件，用于接受 client 应用程序的请求并与 server endpoint 建立隧道。
* **server endpoint**：隧道的 server 端点，监听在 UDP(quic) 端口，与 client endpoint 建立隧道后把隧道传输的数据转发到 server 应用。
* **token**：用于告诉 server endpoint，client 应用程序需要连接到哪个 server 应用程序，在 client endpoint 接受 client 应用程序的连接
  后第一件事就是生成 token，然后把这个 token 发送到 server endpoint，server endpoint 从这个 token 中解析出 server 应用程序的地址，然后连接到应用
  程序，在然后与 client endpoint 建立隧道。quic-tun 提供了很多 token 的获取和解析插件，想了解关于 token 更多信息，请阅读我们[专门的章节](https://kungze.github.io/documents/quic-tun/token/)

### 使用说明

二进制包可直接从下面的连接获取(该包我们已经放在参赛相关文件的 bin 目录下，可直接取用，截止到该文档编写前最新版本是 v0.0.4)

```console
# linux
wget https://github.com/kungze/quic-tun/releases/download/v0.0.4/quic-tun_0.0.4_linux_amd64.tar.gz
```

```console
tar xvfz quic-tun_0.0.4_linux_amd64.tar.gz
```

启动 server 端程序

```console
./quictun-server --listen-on 172.18.31.36:7500
```

启动客户端程序

```console
./quictun-client --listen-on tcp:127.0.0.1:6500 --server-endpoint 172.18.31.36:7500 --token-source tcp:172.18.30.117:22
```

**注意**：上面参数 `--token-source` 指定一个 token，这个 token 用于告诉 server 端客户端应用程序想要连接到哪个应用程序。想了解关于 token 更多信息，请阅读[token](https://kungze.github.io/documents/quic-tun/token/)

这个时候我们可以使用 `ssh` 命令测试

```console
$ ssh root@127.0.0.1 -p 6500
root@127.0.0.1's password:
```

可以使用 `--help` 查看更多 `quictun-server` 和 `quictun-client` 支持的更多命令行参数。

### 测试数据

为了验证 quic-tun 对网络传输性能的影响，我们做了一些测试，通过下面的折线图能够看出 quic-tun 对于网络性能的提升还是很大的。
想了解更多信息以及测试方法，请[点击查看](https://kungze.github.io/documents/quic-tun/performance-test/)。

![q1](https://lplearn-note-pic.oss-cn-beijing.aliyuncs.com/note/2022/20220901155620.png)

![q2](https://lplearn-note-pic.oss-cn-beijing.aliyuncs.com/note/2022/20220901155635.png)

![q3](https://lplearn-note-pic.oss-cn-beijing.aliyuncs.com/note/2022/20220901220651.png)

### 使用到的 google 技术

[quic协议](https://baike.baidu.com/item/QUIC/17341272?fr=aladdin)

[go语言](https://baike.baidu.com/item/go/953521?fromtitle=Golang&fromid=2215139&fr=aladdin)

### 团队介绍

杨建峰
yangjianfeng@troila.com

刘鹏
liupeng2@troila.com

刘慧敏
liuhuimin@troila.com

万学军
wanxuejun@troila.com
