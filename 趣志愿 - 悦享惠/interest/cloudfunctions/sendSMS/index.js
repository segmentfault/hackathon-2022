// 云函数入口文件
const cloud = require('wx-server-sdk')
const CryptoJS = require("crypto-js");
const axios = require("axios")
const tencentcloud = require("tencentcloud-sdk-nodejs");
cloud.init()

// 云函数入口函数
exports.main = async (event, context) => {
  const wxContext = cloud.getWXContext()
/* 实例化要请求 VMS 的 client 对象 */
const SmsClient = tencentcloud.sms.v20210111.Client;

// 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
// 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
const clientConfig = {
  credential: {
    secretId: "AKID41oPsBIBrJndaMw63pOktnMl8ISQAg75",
    secretKey: "qSs7B5nHeMURD7o5A3HLBqHYcU8Xhcw3",
  },
  region: "ap-guangzhou",
  profile: {
    httpProfile: {
      endpoint: "sms.tencentcloudapi.com",
    },
  },
};
const client = new SmsClient(clientConfig);
const params = {
  "PhoneNumberSet": [
      "+86"+event.phone
  ],
  "SmsSdkAppId": "1400549555",
  "SignName": "星兴万物",
  "TemplateId": "1481223",
  "TemplateParamSet": [
      event.content
  ]
};
client.SendSms(params).then(
  (data) => {
    console.log(data);
  },
  (err) => {
    console.error("error", err);
  }
);
  return {
    event,
    openid: wxContext.OPENID,
    appid: wxContext.APPID,
    unionid: wxContext.UNIONID,
  }
}