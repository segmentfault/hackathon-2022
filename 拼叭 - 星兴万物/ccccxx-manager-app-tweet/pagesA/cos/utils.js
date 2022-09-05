import { COS_BUCKET, COS_REGION, COS_BUCKET_MAPS } from '../../constants'
import * as COS from '../cos/cos-wx-sdk-v5'
import basicService from '../../service/basic'
export const getAuthorization = (key) => {
    return (options, cb) => {
        basicService.getCosAuth(key).then(res => {
            cb({
                TmpSecretId: res.credentials.tmpSecretId,
                TmpSecretKey: res.credentials.tmpSecretKey,
                XCosSecurityToken: res.credentials.sessionToken,
                StartTime: res.startTime, // 时间戳，单位秒，如：1580000000，建议返回服务器时间作为签名的开始时间，避免用户浏览器本地时间偏差过大导致签名错误
                ExpiredTime: res.expiredTime, // 时间戳，单位秒，如：1580000900
            });
        })
    }
}
export const cosUploadFiles = (tempFiles, key, cb) => {
    // 获取上传cos的签名
    let cos = new COS.default({ getAuthorization: getAuthorization(key) })
    const fileList = tempFiles.map(function (file, index) {
        return Object.assign(file, {
            Bucket: COS_BUCKET_MAPS[COS_BUCKET],
            Region: COS_REGION,
            Key: `${key}${index}.${file.path.split('.')[1]}`,
            FilePath: file.path,
        });
    });
    cos.uploadFiles({
        files: fileList,
        SliceSize: 1024 * 1024 * 5, // 文件大于5mb自动使用分块上传
    }, function (err, data) {
        cb(err, data)
    });
}
