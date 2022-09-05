// miniprogram/pages/appellation/appellation.js
const app = getApp()
const db = wx.cloud.database()
const _ = db.command
Page({
  data: {
    text: '请输入称谓或姓名',
    showTip: false
  },
  onLoad: function (options) {
    this.setData({
      roleName: options.roleType === '1' ? 'TA' : '您',
      roleType: options.roleType
    })
    if (options.roleType === '2') {
      this.setData({
        name: options.nickName
      })
    }
  },
  onInput(e) {
    this.setData({
      name: e.detail.value
    })
  },
  getInfo() {
    db.collection('userInformation').where({
      phone: wx.getStorageSync('purePhoneNumber')
    }).get().then(res => {
      let name = res.data[0].nickName
      this.setData({
        name: name || ''
      })
    })
  },
  // 如果是给自己设置提醒 获取手机号
  async getPhoneNumber(e) {
    // let res = await wx.cloud.callFunction({
    //   name: 'getMobile',
    //   data: {
    //     weRunData: wx.cloud.CloudID(e.detail.cloudID),
    //   }
    // })
    // const purePhoneNumber = res.result.weRunData.data.purePhoneNumber
    // 将获取到的手机号存入用户信息
    // const openid = wx.getStorageSync('openid')
    // db.collection('userInformation').where({
    //   _openid: openid
    // }).update({
    //   data: {
    //     phone: purePhoneNumber
    //   }
    // })
    // 将手机号和昵称带入下一页
    let purePhoneNumber=wx.getStorageSync('purePhoneNumber')
    wx.redirectTo({
      url: `/pages/phone/phone?nickName=${this.data.name}&phone=${purePhoneNumber}`,
    })

  },
  // 如果是给他人设置提醒 跳转到下一页填手机号
  handleNext() {
    if (this.data.name) {
      this.setData({
        showTip: false
      })
      wx.redirectTo({
        url: `/pages/phone/phone?nickName=${this.data.name}&phone=`,
      })
    } else {
      this.setData({
        showTip: true
      })
    }
  }
})
