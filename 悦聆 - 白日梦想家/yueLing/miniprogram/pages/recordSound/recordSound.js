// miniprogram/pages/recordSound/recordSound.js
var plugin = requirePlugin('WechatSI')
let manager = plugin.getRecordRecognitionManager()
Page({
  data: {
    soundMessage:'',
    list:[],
    type:'derway',
    role:'',
    name:'',
    phone:'',
    memo: '',
    content:[],
    audio:''
  },
  onLoad: function (options) {
    // if(options.memo) {
    //   this.data.memo = options.memo
    //   this.setData({
    //     soundMessage: JSON.parse(options.memo).content
    //   })
    // }
    this.setData({
      name:options.name,
      phone:options.phone
    })
    console.log(options)

  },
  getAudio(e){
    console.log("拿值",e.detail);
    this.data.audio=e.detail;
  },
  recordSoundStart() {
    let that = this;
    that.setData({
      type: 'underway'
    })
    manager.onRecognize = function(res) {
      console.log(res)
      that.setData({
        soundMessage: res.result,
      })
      console.log("current result", res.result)
    }
    manager.onStart = function (res) {
      console.log(res)
      console.log("成功开始录音识别", res)
    }
    manager.onError = function(res) {
      console.error("error msg", res.msg)
    }
    manager.start({ duration: 30000, lang: "zh_CN" })
  },
  change(e) {
    this.setData({
      soundMessage: e.detail
    })
  },
  recordSoundEnd(e) {
    manager.stop()
    var _this = this
    _this.setData({
      type: 'derway'
    })
    manager.onStop = function (res) {
      console.log("record file path", res.tempFilePath)
      console.log("result", res.result)
    }
    
  },
  ceshi() {
    var _this = this
    wx.cloud.callFunction({
      name: "tecentai",
      data: {
        action: 'recognition',
        url:'',
      },
      success: function(res) {
        console.log('====')
        console.log(res)
      },
      fail: console.error
    })
  },
  bindSoundMessage(e){
    this.setData({
      soundMessage: e.detail.value
    })
  },
  handleNext() {
    let that = this;
    if(that.data.soundMessage!==''){
      // let id = ''
      // if(this.data.memo) {
      //   id = JSON.parse(this.data.memo)._id || ''
      // }
      wx.redirectTo({
        url: `/pages/reminder/reminder?content=${that.data.soundMessage}&remindPhone=${that.data.phone}&remindPerson=${this.data.name}&needRemind=true&audio=${this.data.audio}`,
      })
    } else {
      wx.showToast({
        title: '请先录音或输入',
        icon: 'none'
      })
    }
  }
})
