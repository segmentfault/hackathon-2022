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
    userArr: [],
    userTable: [],
    searchText: "",
    hotWordLi:[],
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
    this.geHotWord();
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
    if (this.data.searchText == "") {
      this.getTabelLi(this.data.pageNum);
    } else {
      this.searchShop(this.data.pageNum)
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },
  // 获取物品列表
  getTabelLi(number) {
    var that = this;
    wx.request({
      method: "GET",
      header: {
        //添加请求头
        "Authorization": wx.getStorageSync('userToken') || []
      },
      url: baseApi + '/xianzhi/goodsList?pageNum=' + number,
      success: function (e) {
        e.data.data.records.forEach((item) => {
          // console.log(item);
           item.goodsUrl = item.goodsUrl.split("&");         
          that.data.tableArr.push(item)
        })
        that.setData({
          tableLi: that.data.tableArr,
        })
        let arr = that.data.tableArr;
        arr.forEach((item) => {
          that.getUserDetaile(item.userId);
        })
      }
    })
  },
  // 获取物品列表
  //   getTabelLi(number){
  //   var that  = this;
  //   getRequest(api.getAllShop,{pageNum:number}).then((results) => {
  //     // console.log(results)
  //     results.data.data.records.forEach((item) => {
  //       that.data.tableArr.push(item)
  //     })
  //     that.setData({
  //       tableLi:that.data.tableArr,  
  //     })
  //     let arr = that.data.tableArr;
  //     arr.forEach((item) =>{
  //      that.getUserDetaile(item.userId);
  //     })
  //   })
  //   // wx.request({
  //   //   method:"GET",
  //   //   url: 'http://192.168.0.231:8888//xianzhi/goodsList?pageNum='+number,
  //   //   success:function(e){

  //   //   }
  //   // })
  // },
  //根据用户id获取头像和name
  getUserDetaile(userId) {
    var that = this;
    that.data.userArr = [];
    // getRequest(api.getUserDelites,{userId:userId}).then((e) => {
    //   console.log(e)
    // that.data.userArr.push(e.data.data);
    // console.log(e.data.data);
    // that.setData({
    //   userTable:that.data.userArr
    // });
    // console.log(that.data.userTable)
    // })
    wx.request({
      method: "GET",
      header: {
        //添加请求头
        "Authorization": wx.getStorageSync('userToken') || []
      },
      url: baseApi + '/user/getUserInfo?userId=' + userId,
      success: function (e) {
        that.data.userArr.push(e.data.data);
        // console.log(e.data.data);
        that.setData({
          userTable: that.data.userArr
        });
        // console.log(that.data.userTable)
      }
    })
  },
  // //
  gotoDetails(e) {
    wx.request({
      method: "GET",
      header: {
        //添加请求头
        "Authorization": wx.getStorageSync('userToken') || []
      },
      url: baseApi + '/xianzhi/select?goodsId=' + e.currentTarget.dataset.commoditydetails.goodsId,
      success: function (e) {
        console.log(e)
      }
    })
    wx.navigateTo({
      url: '/pages/home/home',
    })
    console.log(e.currentTarget.dataset.commoditydetails);
    wx.setStorageSync('commoditydetails', e.currentTarget.dataset.commoditydetails);

  },

  //搜索文字
  searchShopText(e) {
    this.setData({
      searchText: e.detail.value
    })
  },
  // 搜索接口
  searchShop(number) {
    let that = this;
    let goodsName = this.data.searchText;
    wx.request({
      method: "GET",
      header: {
        //添加请求头
        "Authorization": wx.getStorageSync('userToken') || []
      },
      url: baseApi + '/xianzhi/search?goodsName=' + goodsName + '&pageNum=' + number,
      success: function (e) {
        // console.log(e.data.data.records);
        // that.setData({
        //   tableLi:e.data.data.records
        // })
        e.data.data.records.forEach((item) => {
          that.data.tableArr.push(item)
        })
        console.log(that.data.tableArr)
        that.setData({
          tableLi: that.data.tableArr,
        })
        console.log(that.data.tableLi)
        let arr = that.data.tableArr;
        arr.forEach((item) => {
          that.getUserDetaile(item.userId);
        })
      }
    })
  },
  goSearchShop() {
    this.setData({
      tableArr: [],
      tableLi: [],
    })
    this.setData({
      pageNum: 1
    })
    this.searchShop(this.data.pageNum)
  },
  //获取热搜词
  geHotWord(){
    wx.request({
      method:"GET",
      url:baseApi + '/xianzhi/hot',
      header: {
        //添加请求头
        "Authorization": wx.getStorageSync('userToken') || [] 
    },
      success:(item) => {
        console.log(item.data.data);
        this.setData({
          hotWordLi : item.data.data
        })
      }
    })
  },
  searchHotWord(e){
    this.data.searchText = e.currentTarget.dataset.hotword;
    this.goSearchShop();
    console.log(e.currentTarget.dataset.hotword)
  }
})