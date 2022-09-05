//index.js
const app = getApp()
const db = wx.cloud.database()
const _ = db.command
Page({
  data: {},
  async onLoad() {
    // 获取openid
    wx.setStorageSync('welcome',true);	
    let res = await wx.cloud.callFunction({
      name: 'getOpenId',
    })
    wx.setStorageSync('openid', res.result.openId)
		// 如果openid存在用户表 则直接跳主页  否则跳转引导页
    db.collection('userInformation').where({
      _openid: res.result.openId
    }).get().then(res => {
    })
  },
  onShow(){
	  wx.hideHomeButton();
	},
  // 开始体验
  start() {
    wx.navigateTo({
      url: '/pages/index/index',
    })
  },
  isUserExist(purePhoneNumber) {
    db.collection('userInformation').where({
      phone: purePhoneNumber
    }).get().then((res) => {
      console.log(res)
      if (res && res.data && res.data.length > 0) {
        app.globalData.userInfo = res.data[0]
        wx.navigateTo({
          url: '/pages/memo/index',
        })
      } else {
        wx.navigateTo({
          url: '/pages/index/index',
        })
      }
    })
  },
  getPhoneNumber(e) {
    wx.cloud.callFunction({
      name: 'getMobile',
      data: {
        weRunData: wx.cloud.CloudID(e.detail.cloudID),
      }
    }).then(res => {
      let purePhoneNumber = res.result.weRunData.data.purePhoneNumber
      // 存起来
      wx.setStorageSync('purePhoneNumber', purePhoneNumber)
      this.isUserExist(purePhoneNumber)
    }).catch(err => {
      console.error(err);
    });

  },
})