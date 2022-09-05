# cinder-metal-csi

## 介绍

[kubernetes](https://zh.wikipedia.org/wiki/Kubernetes) 是目前最流行的 Paas 服务，是用于自动部署、扩展和管理 "容器化应用程序" 的开源系统。
[openstack](https://zh.wikipedia.org/wiki/OpenStack) 是目前最流行的 Iaas 服务，主要提供虚拟服务器和其他资源给用户使用。
cinder-metal-csi 作为 kubernetes 中的一个 [csi](https://kubernetes-csi.github.io/docs/) 插件，可以让 kubernetes 更加简单、灵活的去使用 openstack 的存储服务。
cinder-metal-csi 与 [cinder-csi-plugin](https://github.com/kubernetes/cloud-provider-openstack/blob/master/docs/cinder-csi-plugin/using-cinder-csi-plugin.md) 的主要区别是它不依赖 nova 的元数据服务，可以在 baremetal 环境下运行。这填补了 kubernetes 集群和 openstack 集群互相独立的空白，即：k8s 集群不部署在 openstack 虚拟机上，而是部署在裸金属主机节点上，但 kubernetes 集群仍然希望使用 openstack cinder 块存储服务。

为了能够让更多的开源爱好者参与进来，我们已经在 github 开源 [cinder-metal-csi](https://github.com/kungze/cinder-metal-csi)。同时我们希望通过这次参赛的机会，能够得到各位专家评委老师的指点和建议，以及大家的关注。

## 特点

- cinder-metal-csi 是 kubernetes 中的一个 csi 存储插件，可以为kubernetes集群提供多个存储后端
- cinder-metal-csi 让 kubernetes 更加简单、灵活的去使用 openstack 的存储服务
- cinder-metal-csi 支持 noauth、keystone 两种认证方式，使用 noauth 认证方式进行部署，只需部署 openstack cinder 服务即可，无需部署 openstack 其他组件

## 架构

<img src="architecture.png" alt="cinder-metal-csi"/>

## 部署

*目前部署步骤相对比较复杂，简单的部署方式我们还在开发中。*

### 准备工作

*前提条件：已部署 kubernetes 集群*

如何快速在 k8s 集群搭建一个 openstack cinder 环境，如下所示：

```
kubectl create namespace openstack
helm repo add kungze https://charts.kungze.net
helm install openstack-password kungze/password --namespace openstack
helm install openstack-dependency kungze/openstack-dep --namespace openstack --set ceph.enabled=false --set mariadb.primary.persistence.enabled=false --set rabbitmq.persistence.enabled=false
helm install openstack-keystone kungze/keystone --namespace openstack
helm install openstack-cinder kungze/cinder --namespace openstack --set ceph.enabled=false --set lvm.loop_device_size=51200
```

查看安装进度，等待 pod 状态都变成 running，说明服务都正常启动了（可能有些pod的状态为 Init:Error，不用担心，这是一个正常状态）

```
watch -n 1 kubectl -n openstack get pods
```

创建 openstackrc 文件去执行 openstack 或 cinder 命令

```
echo "
export OS_USERNAME=$(kubectl get secret -n openstack openstack-keystone -o jsonpath="{.data.OS_USERNAME}" | base64 --decode)
export OS_PROJECT_DOMAIN_NAME=$(kubectl get secret -n openstack openstack-keystone -o jsonpath="{.data.OS_PROJECT_DOMAIN_NAME}" | base64 --decode)
export OS_USER_DOMAIN_NAME=$(kubectl get secret -n openstack openstack-keystone -o jsonpath="{.data.OS_USER_DOMAIN_NAME}" | base64 --decode)
export OS_PROJECT_NAME=$(kubectl get secret -n openstack openstack-keystone -o jsonpath="{.data.OS_PROJECT_NAME}" | base64 --decode)
export OS_REGION_NAME=$(kubectl get secret -n openstack openstack-keystone -o jsonpath="{.data.OS_REGION_NAME}" | base64 --decode)
export OS_PASSWORD=$(kubectl get secrets -n openstack openstack-password -o jsonpath="{.data.keystone-admin-password}" | base64 --decode)
export OS_AUTH_URL=$(kubectl get secret -n openstack openstack-keystone -o jsonpath="{.data.OS_CLUSTER_URL}" | base64 --decode)
export OS_INTERFACE=internal
" > openstackrc
source openstackrc
```

上述命令部署 openstack 集群仅用于准备实验环境。不要在生产环境中使用它。如果需要在生产环境中使用 cinder-metal-csi，建议使用 openstack [kolla-ansible](https://github.com/openstack/kolla-ansible) 部署稳定的openstack集群。

### 为 cinder-metal-csi 创建单独的 openstack 租户

```
openstack project create kubernetes
+-------------+----------------------------------+
| Field       | Value                            |
+-------------+----------------------------------+
| description |                                  |
| domain_id   | default                          |
| enabled     | True                             |
| id          | d74063d003c94ed7aa891434d1527ee6 |
| is_domain   | False                            |
| name        | kubernetes                       |
| options     | {}                               |
| parent_id   | default                          |
| tags        | []                               |
+-------------+----------------------------------+
# openstack user create --project kubernetes --password ChangeMe kubernetes
+---------------------+----------------------------------+
| Field               | Value                            |
+---------------------+----------------------------------+
| default_project_id  | d74063d003c94ed7aa891434d1527ee6 |
| domain_id           | default                          |
| enabled             | True                             |
| id                  | e40376719a7544a995e179f57d61c259 |
| name                | kubernetes                       |
| options             | {}                               |
| password_expires_at | None                             |
+---------------------+----------------------------------+
# openstack role add --project kubernetes --user kubernetes member
```

### 准备配置文件

为了让 cinder-metal-csi 可以连接到 openstack cinder 服务，需要在 kubernetes 集群中创建 openstack 认证 secrets

```
kind: Secret
apiVersion: v1
metadata:
  name: cinder-metal-csi-config
  namespace: kube-system
stringData:
  cloud.conf: |
    [Global]
    username = kubernetes
    password = ChangeMe
    user-domain-name = default
    project-domain-name = default
    project-name = kubernetes
    tenant-name = kubernetes
    # Replace this value with correct keystone url
    auth-url = http://keystone-api.openstack.svc.cluster.local:5000/v3
    region = RegionOne
    endpoint-type = internal

    [BlockStorage]
    # The value is same with cinder's configuration
    auth-strategy = keystone
    node-volume-attach-limit = 110
```

*注意： 将 cloud.conf 文件中的值替换为你的环境中的配置*

为 ceph volumes pool 创建 secrets

如果你的 openstack 环境支持使用 ceph rbd 作为cinder的后端。您需要在 k8s 的 secrets 中提供 `ceph client user` 和 `ceph keyring`，以便 cinder-metal-csi 能够正确访问 cinder volume rbd 池。

```
kind: Secret
apiVersion: v1
metadata:
  name: cinder-metal-csi-rbd
  namespace: kube-system
stringData:
  cephClientUser: cinder
  cephClientKey: AQATNwxjHYRXEhAAECkdgAV+Q1mhZRztWh0c2Q==
type: Opaque
```

*注意： 将 cephClientUser 和 cephClientKey 替换为你的环境中的配置*

## 部署 cinder-metal-csi

```
kubectl apply -f https://raw.githubusercontent.com/kungze/cinder-metal-csi/main/manifests/cinder-metal-csi-controllerplugin-rbac.yaml
kubectl apply -f https://raw.githubusercontent.com/kungze/cinder-metal-csi/main/manifests/cinder-metal-csi-controllerplugin.yaml
kubectl apply -f https://raw.githubusercontent.com/kungze/cinder-metal-csi/main/manifests/cinder-metal-csi-nodeplugin-rbac.yaml
kubectl apply -f https://raw.githubusercontent.com/kungze/cinder-metal-csi/main/manifests/cinder-metal-csi-nodeplugin.yaml
```

创建 StorageClass 和 CSIDriver

- CSIDriver

```BASH
kubectl apply -f https://raw.githubusercontent.com/kungze/cinder-metal-csi/main/manifests/cinder-metal-csi-driver.yaml
```

- StorageClass

目前的 cinder-metal-csi 插件只支持两种连接协议来连接 cinder 后端，分别是 rbd 和 iscsi，未来我们将支持 cinder 所支持的所有连接协议。

我们建议在创建 k8s 的 storageclass 时，一个 storageclass 对应一种 openstack cinder 的卷类型，使用以下命令查看 openstack 集群的卷类型

```
cinder type-list
+--------------------------------------+-------------+---------------------+-----------+
| ID                                   | Name        | Description         | Is_Public |
+--------------------------------------+-------------+---------------------+-----------+
| 879821e3-b45d-41b1-8e2d-6c15cc45bdf2 | __DEFAULT__ | Default Volume Type | True      |
| 8a8ea19d-8a44-442c-9076-e3b670e4600e | lvm         | -                   | True      |
| f6339aa8-c2de-41eb-b79d-62d7af12ffd6 | rbd         | -                   | True      |
+--------------------------------------+-------------+---------------------+-----------+
```

对于 iscsi 存储后端的卷，假如 cinder volume type 为 lvm，他在 cinder 中的配置如下所示：

```
[lvm]
volume_group = cinder-volumes
volume_driver = cinder.volume.drivers.lvm.LVMVolumeDriver
volume_backend_name = lvm
target_helper = tgtadm
target_protocol = iscsi
```

与此相对应的 StorageClass 配置如下所示：

```
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: cinder-metal-csi-lvm
  namespace: kube-system
provisioner: cinder.metal.csi
parameters:
  cinderVolumeType: lvm
```

*注意： 将 cinderVolumeType 和 cinder lvm 的卷类型保持一致*

对于 rbd 类型的卷，他在 cinder 中的配置如下所示：

```
[rbd]
volume_driver = cinder.volume.drivers.rbd.RBDDriver
volume_backend_name = rbd
rbd_pool = volumes
rbd_ceph_conf = /etc/ceph/ceph.conf
rbd_flatten_volume_from_snapshot = false
rbd_max_clone_depth = 5
rbd_store_chunk_size = 4
rados_connect_timeout = 5
rbd_secret_uuid = c9c17395-0186-4788-b73b-6d00b84a1c1b
report_discard_supported = True
image_upload_use_cinder_backend = False
```

*注意：无须配置 rbd_keyring_conf 和 rbd_user*

与此相对应的 StorageClass 配置如下所示：

```
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: cinder-metal-csi-rbd
  namespace: kube-system
provisioner: cinder.metal.csi
parameters:
  cinderVolumeType: rbd
  csi.storage.k8s.io/node-stage-secret-name: cinder-metal-csi-rbd
  csi.storage.k8s.io/node-stage-secret-namespace: kube-system
```

StorageClass 中的参数 `csi.storage.k8s.io/node-stage-secret-name` 和 `csi.storage.k8s.io/node-stage-secret-namespace` 是为 cinder-metal-csi 使用 cinder volume rbd pool 提供认证信息。

## 测试

通过以下命令创建 pod 测试

```
kubectl apply -f https://raw.githubusercontent.com/kungze/cinder-metal-csi/main/example/nginx-lvm.yaml
kubectl apply -f https://raw.githubusercontent.com/kungze/cinder-metal-csi/main/example/nginx-rbd.yaml
```

查看 pvc 的状态

```
# kubectl get pvc|grep cinder
cinder-pvc-lvm                               Bound    pvc-97e74caa-ab3a-4003-a8d9-b2f27c483eca   2Gi        RWO            cinder-metal-csi-lvm   2m22s
cinder-pvc-rbd                               Bound    pvc-00dee4fb-d976-4819-a601-19edee0563bc   2Gi        RWO            cinder-metal-csi-rbd   2m18s
```

检查上面 pvc 相关的 cinder volume

```
# cinder list
+--------------------------------------+----------------------------------+--------+------------------------------------------+------+----------------+-------------+----------+--------------------------------------+
| ID                                   | Tenant ID                        | Status | Name                                     | Size | Consumes Quota | Volume Type | Bootable | Attached to                          |
+--------------------------------------+----------------------------------+--------+------------------------------------------+------+----------------+-------------+----------+--------------------------------------+
| 76c75b7b-0f68-4c63-9b63-f1aa61ba53aa | d74063d003c94ed7aa891434d1527ee6 | in-use | pvc-00dee4fb-d976-4819-a601-19edee0563bc | 2    | True           | rbd         | false    | cf4a06c0-595e-4bf7-bc77-cf489b37a5ee |
| 9f2e11f2-c0cc-4628-91d5-438860f5affe | d74063d003c94ed7aa891434d1527ee6 | in-use | pvc-97e74caa-ab3a-4003-a8d9-b2f27c483eca | 2    | True           | lvm         | false    | 26c043f9-ee4f-4dc7-9f00-c4dff72461c9 |
+--------------------------------------+----------------------------------+--------+------------------------------------------+------+----------------+-------------+----------+--------------------------------------+
```

## 使用到的谷歌技术

[kubernetes](https://baike.baidu.com/item/kubernetes?fromModule=lemma_search-box)

[go 语言](https://baike.baidu.com/item/go/953521?fromModule=lemma_search-box)

## 团队介绍

豆亚丽 
douyali@troila.com

池蒙蒙 
chimengmeng@troila.com

曹挺伟 
caotingwei@troila.com

杨沐鑫 
yangmuxin@troila.com
