import {
  postRequest,
  wsbaseApi,
} from "../../utils/request"
import {
  api
} from "../../utils/api.js"

// index.js
Page({
  StartclickMe: function () {
    wx.getUserProfile({
      desc: '必须授权登录才可以使用',
      success: res => {
        console.log(res)
        let user = res.userInfo
        wx.setStorageSync('user', user)
        this.setData({
          nickName: res.userInfo.nickName,
          touxiang: res.userInfo.avatarUrl,
          login: false //记录登录的状态
        })
        console.log("nicheng:", res.userInfo.nickName)
        console.log("img:", res.userInfo.avatarUrl)
        postRequest(api.miniAppLogin, {
          openId: wx.getStorageSync('openId'),
          userName: res.userInfo.nickName,
          imgUrl: res.userInfo.avatarUrl
        }).then(res => {
          if (res.data.code == 200) {
            this.connectWebsocket();
            wx.setStorageSync('userId', res.data.data)
            console.log("user_id", res.data.data)
            wx.navigateTo({
              url: '/pages/map/map',
              success: (res) => {
                let page = getCurrentPages().pop();
                if (page == undefined || page == null) return;
                page.onLoad();
              },
            })
          }
        })
      },
      fail: res => {
        console.log('授权失败', res)
      }
    })
  },
  //连接websocket地址
  connectWebsocket() {
    console.log(wx.getStorageSync("userId"))
    var that = this;
    let url = wsbaseApi + wx.getStorageSync("userId");
    wx.connectSocket({
      url: url,
      success() {
        console.log('连接成功')
        that.initEventHandle() //websocket绑定的各种事件
      }
    })
  },
  // websocket绑定的各种事件
  initEventHandle() {
    var that = this;

    wx.connectSocket(function (res) { //监听 WebSocket 连接打开事件
      console.log('连接打开')
    })
    wx.onSocketMessage(function (res) { //监听 WebSocket 接受到服务器的消息事件 
      var allUserMessage = [];
      let arr = [];

      let allUser = wx.getStorageSync("allUser") == "" ? [] :wx.getStorageSync("allUser");
      let userId = JSON.parse(res.data).fromUserId;

      if(allUser.indexOf(userId.toString()) == -1){
        allUserMessage = allUser;
        allUserMessage.push(userId.toString());
        wx.setStorageSync("allUser", allUserMessage);
        wx.setStorageSync(userId.toString(), res.data);
      }else{
        wx.setStorageSync(userId.toString(), res.data);
      }
      console.log(res.data);
      let pages = getCurrentPages();
      let chatPage = undefined;
      pages.forEach(element => {
        if (element.route == "pages/chat/chat") {
          chatPage = element;
          chatPage.data.temporaryChatRecords = []
          chatPage.getChatMessage(wx.getStorageSync("userId"), wx.getStorageSync("toUserId"), 1)
        };
        if (element.route == "pages/chat_List/chat_List") {
          chatPage = element;
          chatPage.getHistoryData();
        }
      });
      console.log(chatPage);

      if (res.data == "heartOk") {} else {
        //你自己的数据处理
      }
    })
    wx.onSocketError(function (res) { //监听 WebSocket 错误事件
      console.log('WebSocket连接打开失败，请检查！');
      console.log(res.code)
      wx.showToast({
        title: '数据更新失败',
        duration: 2000
      })
      that.reconnect()
    })
    wx.onSocketClose(function (res) {
      console.log(res)
      console.log('WebSocket 已关闭！' + res.code)
    })
  },
  // 断线重连
  reconnect() {
    if (this.lockReconnect) return;
    this.lockReconnect = true;
    clearTimeout(this.timer)
    if (this.data.limit < 12) {
      this.timer = setTimeout(() => {
        this.linkSocket();
        this.lockReconnect = false;
      }, 5000);
      this.setData({
        limit: this.data.limit + 1
      })
    }
  },
  onLoad() {

  }
})