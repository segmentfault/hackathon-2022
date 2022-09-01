// pages/sign_in/sing_in.js

import {
    postRequest,
    wsbaseApi,
    getRequest
  } from "../../utils/request"
  import {
    api
  } from "../../utils/api.js"
  
Page({

    /**
     * 页面的初始数据
     */
    data: {
        contentData: [],
        agentContentData:[],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.getAllVomitslot();
        // this.getUserDelites(4);
    },
    getAllVomitslot:function(e){
        var that = this;
        getRequest(api.getAllVomitslot ,{
            pageNum:1,
        }).then((item) => {
            item.data.data.forEach((element,index) => {
                element.vomitSlot.content = element.vomitSlot.content.split("&&")[1];
                that.data.agentContentData.push (element);
                that.getUserDelites(element.vomitSlot.userId,index);
            });
            that.setData({
                contentData :that.data.agentContentData
            });
            console.log(that.data.contentData);
        })
    },
    getUserDelites:function(userId,index){
        var that = this;
        getRequest(api.getUserDelites,{
            userId:userId,
        }).then((item) => {
            that.data.agentContentData[index].userName = item.data.data.nickname;
        });
        // console.log(this.data.agentContentData);
    },
    gotoDelites:function(e){
        wx.navigateTo({
            url: '/pages/content/content?contentId=' + e.currentTarget.dataset.contentid,
          })
    },
    gotoEditor:function(e){
        wx.navigateTo({
          url: '/pages/editor/editor',
        })
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

    }
})