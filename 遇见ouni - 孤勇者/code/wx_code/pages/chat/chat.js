// pages/chat/chat.js
//xxx.js
import {
    postRequest,
    getRequest,
    baseApi,
} from "../../utils/request.js"
import {
    api
} from "../../utils/api.js"
var app = getApp(); //获取app变量
Page({
    data: {
        sendOrChose: false,
        img_arr: [],
        content: undefined,
        imageUrl: undefined,
        chatRecords: [],
        userId: wx.getStorageSync("userId"),
        pageNum: 1,
        scrollTop: 0,
        windowHeight: 0,
        temporaryChatRecords: [],
        // 刷新信息移动
        heightDistance: false,
        toUserImage:undefined,
        myUserImage:undefined,
    },
    onReady: function () {
        console.log(1111111)
        //动态获取高度

    },
    //动态获取高度
    autoHeight: function () {
        var that = this;
        var query = wx.createSelectorQuery();
        if (that.data.heightDistance) {
            return
        }
        query.select('#viewCommunicationBody').boundingClientRect(function (rect) {
            console.log("----w" + rect.height);
            that.data.heightDistance += rect.height;
            wx.pageScrollTo({
                scrollTop: rect.height,
                duration: 0 //滚动到顶部所需要的事件
            });
            that.setData({
                // windowHeight: rect.height -100,//获取页面高度
            });
            that.data.heightDistance = true
        }).exec();
    },
    /**
     * 组件的方法列表
     */
    _bindinput(e) {
        if (e.detail.value != "") {
            this.setData({
                sendOrChose: true,
                content: e.detail.value
            })
        } else {
            this.setData({
                sendOrChose: false,
            })
        }

    },
    choseImage(e) {

        var that = this;
        if (this.data.img_arr.length < 3) {
            wx.chooseImage({
                count: 1, //一次性上传到小程序的数量     
                sizeType: ['original', 'compressed'],
                sourceType: ['album', 'camera'],
                success(res) {
                    console.log(res.tempFilePaths);
                    that.setData({
                        img_arr: that.data.img_arr.concat(res.tempFilePaths),
                    });
                    that.formSubmit();
                }
            })
        } else {
            wx.showToast({
                title: '只能上传1-3张图片!',
                icon: 'none',
                duration: 2000
            })
        }
    },
    //上传图片到服务器 并发布
    formSubmit(e) {
        var images_list = []; //设置了一个空数组进行储存服务器端图片路径
        var that = this;
        console.log("img_arr", this.data.img_arr)
        for (var i = 0; i < this.data.img_arr.length; i++) {
            var _that = this;
            wx.uploadFile({
                url: baseApi + '/xianzhi/upload/img', //填写实际接口     
                filePath: that.data.img_arr[i],
                method: 'POST',
                name: 'file',
                header: {
                    'content-type': 'multipart/form-data',
                    "Authorization": wx.getStorageSync('userToken') || []
                },
                success: function (res) {
                    var path = JSON.parse(res.data);
                    console.log("path---->", path)
                    images_list.push(path); //把每次返回的地址放入数组
                    if (that.data.img_arr.length == images_list.length) {
                        let dataUrl = {};
                        if (images_list.length == 1) {
                            dataUrl = {
                                goodsUrl1: images_list[0].data,
                            };
                            that.setData({
                                imageUrl: dataUrl.goodsUrl1
                            });
                            that.sendMessage()
                            console.log(dataUrl)
                        }
                    }
                }
            })
        };
    },
    sendMessage() {
        this.setData({
            sendOrChose:false
        });
        this.data.temporaryChatRecords = [];
        var that = this;
        wx.setStorageSync({
            message: that.data.content,
            userId: wx.getStorageSync("userId"),
            touserId: wx.getStorageSync("toUserId"),
            imgUrl: that.data.imageUrl,
        });
        console.log(that.data.content)
        wx.sendSocketMessage({
            data: JSON.stringify({
                message: that.data.content,
                userId: wx.getStorageSync("userId"),
                touserId: wx.getStorageSync("toUserId"),
                imgUrl: that.data.imageUrl,
            }),
            success(e) {
                that.setData({
                    content: '',
                });
                setTimeout(() => {
                    that.getChatMessage(that.data.userId, wx.getStorageSync("toUserId"), 1);
                }, 100);
                that.data.imageUrl = null;
            }
        });
    },
    //获取聊天记录
    getChatMessage(fromUid, toUid, pageNum) {
        var that = this;
        wx.request({
            method: "GET",
            header: {
                //添加请求头
                "Authorization": wx.getStorageSync('userToken') || []
            },
            url: baseApi + "/chat_record?fromUid=" + fromUid + "&toUid=" + toUid + "&pageNum=" + pageNum,
            success: function (e) {
                e.data.data.records.forEach((item) => {
                    that.data.temporaryChatRecords.unshift(item);
                })
                that.setData({
                    chatRecords: that.data.temporaryChatRecords,
                });
                that.autoHeight();
                console.log(that.data.chatRecords)
            }
        })

    },
    onLoad: function () {
        // this.connectWebsocket();
        console.log(this.data.userId, wx.getStorageSync("toUserId"));
        this.getChatMessage(this.data.userId, wx.getStorageSync("toUserId"), this.data.pageNum);
        console.log(app.globalData.name);
        console.log(app.globalData.tel);
        this.getUser(wx.getStorageSync("toUserId"));
        this.getMyUser(this.data.userId);
    },
    //获取用户信息
    getUser(data) {
        console.log(data)
        let that = this;
        wx.request({
            url: baseApi + '/user/getUserInfo?userId=' + data,
            method: "POST",
            header: {
                //添加请求头
                "Authorization": wx.getStorageSync('userToken') || []
            },
            success: function (e) {
                console.log(e);
                that.setData({
                    toUserImage:e.data.data.imgUrl
                })
                wx.setNavigationBarTitle({
                    title: e.data.data.nickname,
                })
            },
        })
    },
    //获取用户信息
    getMyUser(data) {
        console.log(data)
        let that = this;
        wx.request({
            url: baseApi + '/user/getUserInfo?userId=' + data,
            method: "POST",
            header: {
                //添加请求头
                "Authorization": wx.getStorageSync('userToken') || []
            },
            success: function (e) {
                console.log(e);
                that.setData({
                    myUserImage:e.data.data.imgUrl
                })
            },
        })
    },
    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {
        this.data.pageNum += 1;
        this.getChatMessage(this.data.userId, wx.getStorageSync("toUserId"), this.data.pageNum);
    },
})