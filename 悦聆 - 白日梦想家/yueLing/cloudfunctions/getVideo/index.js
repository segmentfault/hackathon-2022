// 云函数入口文件
const cloud = require('wx-server-sdk')
cloud.init({
	env: cloud.DYNAMIC_CURRENT_ENV
})

// 云函数入口函数

exports.main = async (event, context) => {
  const wxContext = cloud.getWXContext()
  const tencentcloud = require("tencentcloud-sdk-nodejs");

const TtsClient = tencentcloud.tts.v20190823.Client;

const clientConfig = {
  credential: {
    secretId: "AKID41oPsBIBrJndaMw63pOktnMl8ISQAg75",
    secretKey: "qSs7B5nHeMURD7o5A3HLBqHYcU8Xhcw3",
  },
  region: "ap-guangzhou",
  profile: {
    httpProfile: {
      endpoint: "tts.tencentcloudapi.com",
    },
  },
};
console.log('穿值',event.text)
const client = new TtsClient(clientConfig);
const params = {
    "Text": event.text,
    "SessionId": "qwe23",
    "Volume": 8,
    "VoiceType": 1003
};
var video;
const res=await client.TextToVoice(params).then(
  (res) => {
   video = Object.assign({}, {res});

    console.log("赋值",video)
    console.log("sdsd",res);
  },
  (err) => {
    console.error("error", err);
  }
);
res;
  return {
    data:video
  }
}