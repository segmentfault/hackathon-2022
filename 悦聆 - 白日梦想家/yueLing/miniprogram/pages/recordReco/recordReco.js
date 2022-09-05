// miniprogram/pages/recordReco/recordReco.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    role:'',
    name:'',
    phone:'',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  },

  doRecordReco(){
    var plugin = requirePlugin("WechatSI")
    let manager = plugin.getRecordRecognitionManager()
    manager.onRecognize = function(res) {
      console.log("current result", res.result)
    }
    manager.onStop = function(res) {
      console.log("record file path", res.tempFilePath)
      console.log("result", res.result)
    }
    manager.onStart = function(res) {
      console.log("成功开始录音识别", res)
    }
    manager.onError = function(res) {
      console.error("error msg", res.msg)
    }
    manager.start({duration:30000, lang: "zh_CN"})
  }
})
