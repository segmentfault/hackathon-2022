const recorderManager = wx.getRecorderManager()
const innerAudioContext = wx.createInnerAudioContext()
const db = wx.cloud.database();
var socket = null;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    src: '',
    recording: false,   //  表示当前是否正在录制
    hasRecord: false,
    sc: '',
    fileID: '',
    phone: '2',
    tex: 'today',
    token:'',
  
  },
  onLoad() {
    const client_secret = '5ca21654240f49a9977a707bf67d6035';
const client_id = 'a310d5fe699f4f9e91ff124ac2d1c7e9';
    let wsUrl = 'wss://openapi.data-baker.com/tts/wsapi';
    let tokenUrl = "https://openapi.data-baker.com/oauth/2.0/token";
    let url = `${tokenUrl}?grant_type=client_credentials&client_secret=${client_secret}&client_id=${client_id}`;
    var that = this;
    wx.request({
      url:url,
      success:function(res){
        that.setData({
          token:res.data.access_token
        })
        console.log(that.data.token)
      }
    })
    socket = wx.connectSocket({
      url: 'wss://openapi.data-baker.com/tts/wsapi	',
      success: res => {
        console.info('创建连接成功');
      }
    });
    socket.onOpen(function () {
      console.info('连接打开成功');
    });
    socket.onError(function () {
      console.info('连接报错');
    });
    socket.onMessage(function (e) {
      console.log(e);
      console.log(e.data)
      let a=JSON.parse(e.data);
      console.log(a)
  });
  },
  onTap(){
    console.log(this.data.token)
    let params = {
      access_token: this.data.token,
      version: '1.0',
      tts_params: {
        "domain":"1",
        "interval":"1",
        "language":"zh",
        "voice_name":"Jiaojiao",
        "text":"5qCH6LSd56eR5oqA77yM5LiT5rOo5LqO5pm66IO96K+t6Z+z5oqA5pyv"
      }
    };
    socket.send({
      data: JSON.stringify(params),
      success: res => {
          console.info('客户端发送成功');
      }
  });

  },
  async point() {
    const a = await db.collection('point').where({
      _id: "b69f67c062cf7de90b0a527d02974f39"
    }).get()
    let re = 'today';
    a.data[0].allM = 5;
    delete a.data[0]._id;
    delete a.data[0]._createTime;
    delete a.data[0]._updateTime;
    console.log("a", a.data[0])
    await db.collection('point').doc('b69f67c062cf7de90b0a527d02974f39').update({
      data: a.data[0],
    }).then(res => {
      console.log("sbsbs", res)
    })
  },
  start: function () {
    var that = this
    const options = {
      duration: 10000,//指定录音的时长，单位 ms
      sampleRate: 16000,//采样率
      numberOfChannels: 1,//录音通道数
      encodeBitRate: 96000,//编码码率
      format: 'mp3',//音频格式，有效值 aac/mp3
      frameSize: 50,//指定帧大小，单位 KB
    }
    //开始录音
    wx.authorize({
      scope: 'scope.record',
      success() {
        console.log("录音授权成功");
        //第一次成功授权后 状态切换为2
        that.setData({
          status: 2,
        })
        recorderManager.start(options);
        recorderManager.onStart(() => {
          console.log('recorder start')
        });
        //错误回调
        recorderManager.onError((res) => {
          console.log(res);
        })
      },
    })


  },
  send() {
    console.log("dfsd", this.data.phone)
    const randomString = () => Math.random().toString(10).slice(2, 6);
    console.log("dfd", randomString())
    wx.cloud.callFunction({
      name: 'sedMS',
      data: {
        phone: this.data.phone,
        content: randomString()
      }
    }).then(res => {
      console.log("成功", res)
    })
  },
  sb(e) {
    this.setData({
      phone: e.detail.value
    })
  },

 
  //暂停录音
  pause: function () {
    recorderManager.pause();
    recorderManager.onPause((res) => {

      console.log('暂停录音')

    })
  },
  //继续录音
  resume: function () {
    recorderManager.resume();
    recorderManager.onStart(() => {
      console.log('重新开始录音')
    });
    //错误回调
    recorderManager.onError((res) => {
      console.log(res);
    })
  },
  //停止录音
  stop: function () {
    recorderManager.stop();
    recorderManager.onStop((res) => {
      this.tempFilePath = res.tempFilePath;
      var tempFilePath = res.tempFilePath
      console.log('停止录音', res.tempFilePath)
      wx.cloud.uploadFile({
        cloudPath: '2.mp3',
        filePath: tempFilePath,
        success: res => {
          console.log("文件id", res.fileID)
          this.setData({
            fileID: res.fileID
          })
        },
        fail: err => {
          console.log("错误信息", err)
        }
      })
    })
  },
  //播放声音
  play: function () {
    innerAudioContext.autoplay = true
    innerAudioContext.src = this.tempFilePath,
      innerAudioContext.onPlay(() => {
        console.log('开始播放')
      })
    innerAudioContext.onError((res) => {
      console.log(res.errMsg)
      console.log(res.errCode)
    })
  },



  text() {
    const myaudio = wx.createInnerAudioContext();
    this.audioCtx = wx.createAudioContext('myAudio');
    console.log("sss")
    if (this.data.src == '') {
      wx.cloud.callFunction({
        name: 'getVideo',
        data: {
          text: '111'
        }
      }).then(res => {
        console.log("成功", res)
        this.setData({
          src: res.result.data.res.Audio
        })
        this.audioCtx.play();
      }).catch(res => {
        console.log("失败", res)
      })
    } else {
      this.audioCtx.play();
    }

  },

  audioPlay() {
    // wx.cloud.downloadFile({
    //   fileID: 'cloud://light-2g5nohni2ec62ee5.6c69-light-2g5nohni2ec62ee5-1305732279/2.mp3',
    //   success:res=>{
    //     console.log("文件下载成功",res)

    //   },
    //   fail:err=>{
    //     console.log("错误信息",err)
    //   }
    // })
    innerAudioContext.src = 'cloud://light-2g5nohni2ec62ee5.6c69-light-2g5nohni2ec62ee5-1305732279/2.mp3'
    innerAudioContext.play();
  },

})


