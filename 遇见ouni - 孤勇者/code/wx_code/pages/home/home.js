// pages/home/home.js
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
        imageLi: [
            "re0vamb9g.hn-bkt.clouddn.com/TYK4omG1xbli878557f70ebe7b2365861db4571999f7_568e9487-4937-4a39-ba24-396a030245f7.jpg",
            "re0vamb9g.hn-bkt.clouddn.com/TYK4omG1xbli878557f70ebe7b2365861db4571999f7_568e9487-4937-4a39-ba24-396a030245f7.jpg"
        ],
        commoditydetails: undefined,
        userId: wx.getStorageSync("userId"),
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.setData({
            commoditydetails: wx.getStorageSync("commoditydetails")
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

    },
    goToChat(e) {
        wx.navigateTo({
            url: '/pages/chat/chat',
        })
        console.log(e.currentTarget.dataset.gotochatuser);
        wx.setStorageSync('toUserId', e.currentTarget.dataset.gotochatuser)
    },
    //删除商品
    deleteShop(e) {
        var that = this;
        console.log(e.currentTarget.dataset.deleteid);
        console.log(e)
        wx.showModal({
            title: '提示',
            content: '确认删除?',
            success: function (res) {
                if (res.confirm) {
                    wx.request({
                        method: "GET",
                        url:baseApi+ '/xianzhi/del/' + e.currentTarget.dataset.deleteid,
                        success: function (e) {
                            if(e.data.code == 200){
                                wx.showToast({
                                    title: '删除成功',
                                    icon: 'success',
                                    duration: 1000 //持续的时间
                                  })
                                  wx.switchTab({
                                    url: '/pages/xianzhi/xianzhi',
                                    success: (res) => {
                                        let page = getCurrentPages().pop();
                                        if (page == undefined || page == null) return;
                                        page.onLoad();
                                      },
                                  })
                            }
                            console.log(e)
                        }
                    })
                    console.log('用户点击确定')
                } else if (res.cancel) {
                    console.log('用户点击取消')
                }

            }

        })
    },
    //修改商品
    choseShop(e){
        console.log(e.currentTarget.dataset.choseshop);
        wx.setStorageSync('commoditydetails', e.currentTarget.dataset.choseshop);
        let arr = [];
        if(e.currentTarget.dataset.choseshop.goodsUrl1 != null){
            arr.push("http://"+e.currentTarget.dataset.choseshop.goodsUrl1)
        };
        if(e.currentTarget.dataset.choseshop.goodsUrl2 != null){
            arr.push("http://"+e.currentTarget.dataset.choseshop.goodsUrl2)
        };
        if(e.currentTarget.dataset.choseshop.goodsUrl3 != null){
            arr.push("http://"+e.currentTarget.dataset.choseshop.goodsUrl3)
        }
        console.log(arr);
        wx.setStorageSync('img_arr', arr);
        wx.navigateTo({
          url: '/pages/chose/chose',
        })
    }
})