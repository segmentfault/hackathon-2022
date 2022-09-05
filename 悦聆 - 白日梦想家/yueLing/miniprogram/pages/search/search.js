// miniprogram/pages/search.js
const app = getApp()
const db = wx.cloud.database()
const _ = db.command
Page({

  /**
   * 页面的初始数据
   */
  data: {
    memoList:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  jump1(e) {
		let id = e.currentTarget.dataset.item._id
		wx.navigateTo({
			url: '/pages/reminder/reminder?id=' + id,
		})
	},
  search(e) {
    let key = e.detail.value
    if(!key) return
    db.collection('memo').where(_.or([{
        title: db.RegExp({
          regexp: '.*' + key
        })
      },
      {
        content: db.RegExp({
          regexp: '.*' + key
        })
      }
    ]).and({
      _openid: app.globalData.userInfo._openid
    })).get().then(res => {
      this.setData({
        memoList: res.data
      })
    })
  }
})