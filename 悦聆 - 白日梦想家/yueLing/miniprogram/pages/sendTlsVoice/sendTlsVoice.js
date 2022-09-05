// miniprogram/pages/sendTlsVoice/sendTlsVoice.js
Page({

  /**
   * 页面的初始数据
   */
  data: {},

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  doSendTlsVoice() {
    let that = this;
    wx.cloud.callFunction({
      name: "tecentai",
      data: {
        action: 'sendTtsVoice',
        TemplateParam: "你好，今天是星期五",
        CalledNumber: "+8613764597746"
      },
      success: res => {
        console.log(that.data.labels)
      }
    })
  }
})
