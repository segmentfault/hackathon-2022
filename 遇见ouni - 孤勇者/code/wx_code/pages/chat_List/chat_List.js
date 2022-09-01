// pages/chat_List/chat_List.js
import {
  postRequest,
  getRequest,
  baseApi,
} from "../../utils/request.js"
import {
	api
} from "../../utils/api.js"
Page({

  /**
   * 页面的初始数据
   */
  data: {
    fileId:[],
    userTable:[],
    charMessage:[],
    charMessageArr:[],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getHistoryData();
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

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },
  //获取用户详情
  getUser(data,index) {
    console.log(data)
    let that =this;
    wx.request({
      url: baseApi+'/user/getUserInfo?userId=' + data,
      method: "POST",
      header: {
        //添加请求头
        "Authorization": wx.getStorageSync('userToken') || [] 
    },
      success: function (e) {
        that.data.charMessage[index].imgUrl = e.data.data.imgUrl 
        that.data.charMessage[index].nickname = e.data.data.nickname 
        that.setData({
          charMessageArr:that.data.charMessage
        })
      },
    })
  },
  //获取历史信息
  getHistoryData(){
    this.data.charMessage = [];
    let allUser =  wx.getStorageSync("allUser");
    console.log(allUser);
    allUser.forEach((item,index) => {
      console.log(wx.getStorageSync(item.toString()))
      this.data.charMessage.push({
        content:JSON.parse(wx.getStorageSync(item.toString())).message,
        fromUserId:JSON.parse(wx.getStorageSync(item.toString())).fromUserId
      });
      this.getUser(item,index);
      console.log(item);
    });
    console.log(this.data.charMessageArr)
  },
  openChatDetails(e){
    wx.navigateTo({
      url: '/pages/chat/chat',
    })
    console.log(e.currentTarget.dataset.fromuserid);
    wx.setStorageSync('toUserId', e.currentTarget.dataset.fromuserid,)
  }
})