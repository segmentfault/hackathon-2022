import {
  postRequest,
  getRequest,
  baseApi,
} from "../../utils/request.js"
import {
  api
} from "../../utils/api.js"
Page({

  data: {
    tableArr: [],
    tableLi: [],
    pageNum: 1,
    userTable: undefined,
    userId: wx.getStorageSync('userId')
  },

  goTopub() {
    wx.navigateTo({
      url: '/pages/xianzhi/uploadpage/uploadpage',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getTabelLi(this.data.pageNum);
    this.getUserDetaile(wx.getStorageSync('userId'));
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    this.data.pageNum += 1;
    console.log(this.data.pageNum);
    this.getTabelLi(this.data.pageNum);
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },
  // // 获取个人物品列表
  // getTabelLi(){
  //   var that  = this;
  //   wx.request({
  //     method:"GET",
  //     url: 'http://192.168.0.231:8888/xianzhi/getMyGoods?userId='+wx.getStorageSync("userId")+'&pageNum='+that.data.pageNum,
  //     success:function(e){
  //       that.setData({
  //         tableLi:e.data.data.records,  
  //       })
  //       console.log(e.data.data.records);
  //       let arr = e.data.data.records;
  //       arr.forEach((item) =>{
  //         console.log(item)
  //       })
  //     }
  //   })
  // },
  // 获取物品列表
  getTabelLi(number) {
    var that = this;
    wx.request({
      method: "GET",
      header: {
        //添加请求头
        "Authorization": wx.getStorageSync('userToken') || []
      },
      url: baseApi + '/xianzhi/getMyGoods?userId=' + wx.getStorageSync("userId") + '&pageNum=' + number,
      success: function (e) {
        console.log(e)
        e.data.data.records.forEach((item) => {
          item.goodsUrl = item.goodsUrl.split("&");
          that.data.tableArr.push(item)
        })
        console.log(that.data.tableArr);
        that.setData({
          tableLi: that.data.tableArr,
        })
        console.log(e.data.data.records);
        let arr = that.data.tableArr;
        arr.forEach((item) => {
          that.getUserDetaile(item.userId);
        });
      }
    })
  },
  //根据用户id获取头像和name‘
  getUserDetaile(userId) {
    wx.request({
      method: "GET",
      header: {
        //添加请求头
        "Authorization": wx.getStorageSync('userToken') || [] 
    },
      url:baseApi+ '/user/getUserInfo?userId=' + userId,
      success: function (e) {
        console.log(e.data)
      }
    })
  },
  //
  gotoDetails(e) {
    wx.navigateTo({
      url: '/pages/home/home',
    })
    console.log(e.currentTarget.dataset.commoditydetails);
    wx.setStorageSync('commoditydetails', e.currentTarget.dataset.commoditydetails)
  },
  //获取个人信息
  getUserDetaile(userId) {
    var that = this;
    wx.request({
      method: "GET",
      url:baseApi+ '/user/getUserInfo?userId=' + userId,
      header: {
        //添加请求头
        "Authorization": wx.getStorageSync('userToken') || [] 
    },
      success: function (e) {
        that.setData({
          userTable: e.data.data
        });
      }
    })
  },
})