# cinder-metal-csi

在 K8s 环境中部署 [cinder-metal-csi](https://github.com/kungze/cinder-metal-csi) 插件，该插件 Driver 支持两种认证策略：

- keystone openstack 环境需对接 keystone 认证
- noauth openstack 环境只需部署 cinder 相关组件（cinder-api、cinder-volume、cinder-scheduler）

cinder-metal-csi 插件存储后端目前支持 ceph、 lvm、 local 三种方式 

## 快速部署

```
helm repo add kungze https://kungze.github.io/cinder-metal-csi
helm install cinder-metal-csi kungze cinder-metal-csi
```

## Parameters

### Cloud configuration

| Name                      | Form title               | Description                            | Value                                                   |
|---------------------------| ------------------------ | -------------------------------------- | ------------------------------------------------------- |
| `cloud.authStrategy`      | Cloud AuthStrategy       | The strategy to use for authentication | `keystone`                                              |
| `cloud.username`          |  Cloud Username          | Keystone authentication username       | `admin`                                                 |
| `cloud.userPassword`      | Cloud Password           | Keystone authentication password       | `o2DkgbcwDZ`                                            |
| `cloud.tenantName`        | Cloud TenantName         | Keystone authentication tenantName     | `admin`                                                 |
| `cloud.authUrl`           | Cloud AuthURL            | Keystone authentication authUrl        | `http://keystone-api.default.svc.cluster.local:5000/v3` |
| `cloud.cinderListenAddr`  | Cloud Cinder Listen Addr | Cinder api listen addr                 | `""`                                                    |


### backend configuration

| Name            | Form title           | Description                  | Value  |
| --------------- | -------------------- | ---------------------------- | ------ |
| `backend.lvm`   | Enable LVM Backend   | Enable lvm storage backend   | `true` |
| `backend.local` | Enable Local Backend | Enable local storage backend | `true` |
| `backend.ceph`  | Enable Ceph Backend  | Enable ceph storage backend  | `true` |


### Ceph Related Configuration

| Name               | Form title            | Description                                  | Value                                                                                  |
| ------------------ | --------------------- | -------------------------------------------- | -------------------------------------------------------------------------------------- |
| `ceph.keyringName` | Ceph Secrets Name     | The cinder-metal-csi mount ceph secrets name | `cinder-volume-rbd-keyring`                                                            |
| `ceph.keyring`     | Ceph Secrets Data     | The user keyring using the Ceph pool         | `W2NsaWVudC5hZG1pbl0Ka2V5ID0gQVFBc3hjOWlwVTFFTGhBQWY5elpLWnZ5VlBMTmV2MVhrRVdlS2c9PQo=` |
| `ceph.monAddr`     | Ceph Monitors Address | The ceph cluster monitors service address    | `10.111.43.63:6789`                                                                    |


### Storage Class Configuration

| Name                                | Form title                        | Description                                  | Value  |
| ----------------------------------- | --------------------------------- | -------------------------------------------- | ------ |
| `storageClass.enabled`              | Create StorageClass               | Whether to create storageClass               | `true` |
| `storageClass.allowVolumeExpansion` | StorageClass allowVolumeExpansion | Set up the storageClass allowVolumeExpansion | `true` |