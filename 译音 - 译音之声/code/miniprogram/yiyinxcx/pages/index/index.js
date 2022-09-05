Page({
  data: {

  },
  onShareAppMessage: function () {
    return {
      title: '译音小程序，用心说话',
      desc: '语音识别，语音合成，画板涂鸦，译音小程序— 致力于为听障者服务。',
      path: '/pages/index/index',
      imageUrl:'/images/logo.png'
    }
  },
  //事件处理函数
  gotoSmallTalk: function() {
    wx.navigateTo({
      url: '/pages/index/talk/talk'
    })
  },
  gotoLongTalk: function () {
    wx.navigateTo({
      url: '/pages/index/longtalk/longtalk'
    })
  },
  gotoDraw: function () {
    wx.navigateTo({
      url: '/pages/index/draw/draw'
    })
  },
  onLoad(){
  }
})
