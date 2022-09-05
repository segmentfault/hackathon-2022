const app = getApp() 
Page({
  data: {
    motto: '译音，让交流沟通无障碍',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  //事件处理函数
  gotoAbout: function () {
    wx.navigateTo({
      url: '/pages/user/about/about'
    })
  },
  goIhearing:function(){

  },
  gotoFeedback: function () {
    wx.navigateTo({
      url: '/pages/user/feedback/feedback'
    })
  },
  gotoSoe: function () {
    wx.navigateTo({
      url: '/pages/soe/list/index'
    })
  },
  gotoTranslate:function(){
    wx.navigateTo({
      url: '/pages/translate/translate'
    })
  },
  gotoShare: function () {
    this.onShareAppMessage()
  },
  onLoad: function () {
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse) {
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },
  getUserInfo: function (e) {
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },
  onShareAppMessage: function () {
    return {
      title: '译音小程序，用心说话',
      desc: '语音识别，语音合成，画板涂鸦，译音小程序 — 致力于为听障者服务。',
      path: '/pages/index/index',
      imageUrl: '/images/logo.png'
    }
  },
})
