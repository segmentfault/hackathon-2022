// pages/content/content.js
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
        html:undefined,
        mainContent:undefined,
        agentMainContent:undefined,
        isLike:undefined,
        userId : wx.getStorageSync("userId"),
        pageNum:1,
        options:undefined,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.data.options = options;
        console.log(options);
        let data = {
            vomitslotId:options.contentId,
            pageNum:this.data.pageNum,
            userId:this.data.userId
        };
        console.log(data);
        getRequest(api.vomitslotSelect,data).then((item) => {
            this.setData({
                mainContent:item.data.data,
                html:item.data.data.VomitSlotVo.vomitSlot.content.split("&&")[0],
                isLike:item.data.data.isLike,
            });
            console.log(this.data.mainContent)
        })
        // console.log(options.contentId);
    },
    likeAndnoLike :function(e){
        getRequest(api.vomitslotLike,{
            vomitslotId:this.data.mainContent.VomitSlotVo.vomitSlot.id,
            userId:this.data.userId
        }).then(results => {
            console.log(results);
            if(results.data.code == 200){
                this.setData({
                    isLike:!this.data.isLike,
                })
            }
        })
    },
    //获取二级评论详情
    getContentDelites(id){
        console.log(id);
        this.data.agentMainContent = this.data.mainContent;
        getRequest(api.vomitslotCommentDetail , {
            vomitslotId:this.data.options.contentId,
            parentId:id.target.dataset.partantid,
            pageNum:1
        }).then(results => {
            this.data.agentMainContent.comments.forEach((item,index) => {
                console.log(item);
                if(item.id == id.target.dataset.partantid){
                    this.data.agentMainContent.comments[index].children = results.data.data;
                };
            });
            console.log(this.data.agentMainContent);
            this.setData({
                mainContent:this.data.agentMainContent,
            })
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