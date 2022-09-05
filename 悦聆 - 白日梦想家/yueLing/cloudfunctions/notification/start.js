const tencentcloud = require("tencentcloud-sdk-nodejs");
const CryptoJS = require("crypto-js");
const axios = require("axios")
// 导入 VMS 模块的 client models
const vmsClient = tencentcloud.vms.v20200902.Client;
var source = "market";
/* 实例化要请求 VMS 的 client 对象 */
const client = new vmsClient({
	credential: {
		/* 必填：腾讯云账户密钥对secretId，secretKey。
		 * 这里采用的是从环境变量读取的方式，需要在环境变量中先设置这两个值。
		 * 您也可以直接在代码中写死密钥对，但是小心不要将代码复制、上传或者分享给他人，
		 * 以免泄露密钥对危及您的财产安全。
		 * CAM密匙查询: https://console.cloud.tencent.com/cam/capi */
		secretId: 'AKID41oPsBIBrJndaMw63pOktnMl8ISQAg75',
		secretKey: 'qSs7B5nHeMURD7o5A3HLBqHYcU8Xhcw3',
	},
	/* 必填：地域信息，可以直接填写字符串ap-guangzhou，或者引用预设的常量 */
	region: "ap-guangzhou",
	/* 非必填:
	 * 客户端配置对象，可以指定超时时间等配置 */
	profile: {
		/* SDK默认用TC3-HMAC-SHA256进行签名，非必要请不要修改这个字段 */
		signMethod: "TC3-HMAC-SHA256",
		httpProfile: {
			/* SDK默认使用POST方法。
			 * 如果您一定要使用GET方法，可以在这里设置。GET方法无法处理一些较大的请求 */
			reqMethod: "POST",
			/* SDK有默认的超时时间，非必要请不要进行调整
			 * 如有需要请在代码中查阅以获取最新的默认值 */
			reqTimeout: 30,
			/**
			 * SDK会自动指定域名。通常是不需要特地指定域名的，但是如果您访问的是金融区的服务
			 * 则必须手动指定域名，例如vms的上海金融区域名： vms.ap-shanghai-fsi.tencentcloudapi.com
			 */
			endpoint: "vms.tencentcloudapi.com"
		},
	},
});

const start = async (e) => {
	/* 请求参数，根据调用的接口和实际情况，可以进一步设置请求参数
 * 属性可能是基本类型，也可能引用了另一个数据结构
 * 推荐使用IDE进行开发，可以方便的跳转查阅各个接口和数据结构的文档说明
 * 帮助链接：
 * 语音消息控制台：https://console.cloud.tencent.com/vms
 * vms helper：https://cloud.tencent.com/document/product/1128/37720
 */
console.log(e)
	if(e._openid === 'oTxS04tUAwkIgQuCiGOQZ2JgvGxs') {
		// 签名
		var datetime = (new Date()).toGMTString();
		var signStr = "x-date: " + datetime + "\n" + "x-source: " + source;
		var sign = CryptoJS.enc.Base64.stringify(CryptoJS.HmacSHA1(signStr, secretKey))
		var auth = 'hmac id="' + secretId + '", algorithm="hmac-sha1", headers="x-date x-source", signature="' + sign + '"';
		var headers = {
			"X-Source": source,
			"X-Date": datetime,
			"Authorization": auth,
		}
		// url参数拼接
		var url = 'https://service-hcum4qqt-1303928753.sh.apigw.tencentcs.com/release/VoiceNotification';
		
		axios.post(url, {
					'extend': 'test',
					'fileName': '20210513_1',
					'overtNumber': '2071048989',
					'playCount': '2',
					'toNumber': e.remindPhone
				},{
					headers
				},
		)
		
	} else {
		const params = {
			// 模板 ID，必须填写在控制台审核通过的模板 ID，可登录 [语音消息控制台] 查看模板 ID
			TemplateId: "1462037",
			TemplateParamSet: [e.content],
			/* 被叫手机号码，采用 e.164 标准，格式为+[国家或地区码][用户号码]
			 * 例如：+8613711112222，其中前面有一个+号，86为国家码，13711112222为手机号
			 */
			CalledNumber: "+86" + e.remindPhone,
			/* 在语音控制台添加应用后生成的实际SdkAppid，示例如1400006666 */
			VoiceSdkAppid: "1400698122",
			/* 播放次数，可选，最多3次，默认2次 */
			PlayTimes: 3,
			/* 用户的 session 内容，腾讯 server 回包中会原样返回 */
			SessionContext: "xxxx",
		};
		// 通过client对象调用想要访问的接口，需要传入请求对象以及响应回调函数
		client.SendTtsVoice(params, function (err, response) {
			// 请求异常返回，打印异常信息
			if (err) {
				console.log(err);
				return;
			}
			// 请求正常返回，打印response对象
			console.log(response);
		})
	}
}
module.exports = {
	start
}
