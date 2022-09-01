// pages/map/map.js
import {
    postRequest,
    getRequest
} from "../../utils/request.js";
import {
	api
} from "../../utils/api.js"
Page({

    /**
     * 页面的初始数据
     */
    data: {
        path: "http://www.taokene.cn",
        nearListBar:[],
        nearList:[],

    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.GPSsubmit();
        setTimeout(() => {
            console.log(JSON.stringify(this.data.nearList));
            wx.navigateTo({
                url: '/pages/near_char/near_char?info='+encodeURIComponent(JSON.stringify(this.data.nearList))
              })
        
        }, 3000);
    },
    GPSsubmit: function (e) {
        wx.getLocation({
            type: 'wgs84',
            success: (res) => {
                var latitude = res.latitude
                var longitude = res.longitude
                var speed = res.speed
                var accuracy = res.accuracy
                this.setData({
                    latitude: latitude,
                    longitude: longitude
                });
                // wx.showModal({
                //     title: '当前位置',
                //     content: '经度' + res.longitude + '纬度' + res.latitude,
                // })
                let data = {
                    latitude: latitude,
                    longitude: longitude,
                    userId:wx.getStorageSync("userId"),
                }
                this.getNear(data);
                
            }

        })
    },
    //获取附近的人
    getNear:function(data){
        getRequest(api.postLngLat,data).then(res => {
            console.log(res.data.data);
            res.data.data.forEach((element) => {
                this.data.nearListBar.push(element)
            });
            this.setData({
                nearList :this.data.nearListBar
            });
            console.log(this.data.nearList)
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