// miniprogram/pages/phone/phone.js
import util from "../../utils/util"
const app = getApp()
const db = wx.cloud.database()
const _ = db.command
Page({
  data: {
    phone: '',
    showTip: false,
  },
  onLoad: function (options) {
    this.setData({
      nickName: options.nickName,
      phone: options.phone,
      // 如果是给自己设置提醒 成员的手机号就是自己
      userPhone: options.phone
    })
  },
  onInput(e) {
    this.setData({
      phone: e.detail.value
    })
  },
  // 选择电话
  choose() {
    wx.chooseContact({
      success: (res) => {
        this.setData({
          ['phone']: res.phoneNumber,
        })
      }
    })
  },
  addRecord() {
    const app = getApp()
    const db = wx.cloud.database()
    const _ = db.command
    // 如果是给别人设置提醒 增加一个用户
    if(this.data.role !== 0) {
      db.collection('userInformation').add({
        data: {
          phone: '',
          avatarUrl: '',
          city: '',
          country: '',
          gender: '',
          nickName: '',
          province: '',
          family: [],
          createTime: new Date()
        }
      })
    }
  },
  // 添加成员
  async addMember() {
    // 如果是给他人设置提醒 将他人加入自己的亲友列表
    if(!this.data.userPhone) {
      const openid = wx.getStorageSync('openid')
      const res = await db.collection('userInformation').where({
        _openid:openid
      }).get()
      console.log("res",res)
      let family = res.data[0].family || []
      family.push({
        name: this.data.nickName,
        phone: this.data.phone,
        role:''
      })
      const id = res.data[0]._id
      db.collection('userInformation').doc(id).update({
        data: {
          family:family,
        }
      })
    }
    wx.redirectTo({
      url: `/pages/recordSound/recordSound?name=${this.data.nickName}&phone=${this.data.phone}`,
    })
  },
  handleNext() {
    if (this.data.phone && (/^1[23456789]\d{9}$/.test(this.data.phone))) {
      this.setData({
        showTip: false
      })
      this.addMember()
    } else {
      this.setData({
        showTip: true
      })
    }
  }
})