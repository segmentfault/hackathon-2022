// import { time } from "console";
import {
  postRequest,
  getRequest,
  baseApi,
} from "../../utils/request.js"
import {
  api
} from "../../utils/api.js"
const app = getApp()
var form_data;
var psw_vaule = [];
Page({
  data: {
    tempFilePaths: [],
    img_arr: [],
    text: undefined,
    address: undefined,
    price: undefined,
    array: ['1AB', '1CD', '1EF', '2ABC', '5AB', '5CD', '3AB',
      '3CD', '4AB', '4CD', '6ABC', '7AB', '7CD'
    ],
    images_list: []
  },

  cancel_btn() {
    wx.navigateTo({
      url: '/pages/xianzhi/xianzhi',
    })
  },

  /**
   *  点击下拉框
   */
  bindShowMsg() {
    this.setData({
      select: !this.data.select
    })
  },
  /**
   * 已选下拉框
   */
  mySelect(e) {
    console.log(e)
    var name = e.currentTarget.dataset.name
    this.setData({
      add_name: name,
      select: false
    })
  },
  //上传图片到服务器 并发布
  formSubmit: function (e) {
    var that = this;
    wx.showModal({
      title: '提示',
      content: '确认发布?',
      success: function (res) {
        if (res.confirm) {
          var images_list = []; //设置了一个空数组进行储存服务器端图片路径
          console.log("img_arr", that.data.img_arr)
          for (var i = 0; i < that.data.img_arr.length; i++) {
            var _that = that;
            wx.uploadFile({
              header: {
                //添加请求头
                "Authorization": wx.getStorageSync('userToken') || []
              },
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
                // that.setData({
                //   images_list:[].push(path)
                // })

                if (that.data.img_arr.length == images_list.length) {
                  let dataUrl = {};
                  if (images_list.length == 3) {
                    dataUrl = {
                      goodsUrl: images_list[0].data +"&"+images_list[1].data+"&"+images_list[2].data,
                    };
                  }
                  if (images_list.length == 2) {
                    dataUrl = {
                      goodsUrl: images_list[0].data+"&"+images_list[1].data,
                    };
                  };
                  if (images_list.length == 1) {
                    dataUrl = {
                      goodsUrl: images_list[0].data,
                    };
                  }
                  that.issue(dataUrl);
                }
              }
            })
          };
          console.log('用户点击确定')
        } else if (res.cancel) {
          console.log('用户点击取消')
        }

      }

    })



  },
  //发布
  issue: function (data) {
    console.log(data);
    wx.request({
      method: "POST",
      url: baseApi+ '/xianzhi/pub',
      header: {
        //添加请求头
        "Authorization": wx.getStorageSync('userToken') || [] 
    },
      data: {
        userId: wx.getStorageSync("userId"),
        goodsDetail: this.data.text,
        goodsAdd: this.data.address,
        goodsPrice: this.data.price,
        ...data
      },
      success: function (res) {
        console.log(res);
        if (res.data.code == 200) {
          wx.switchTab({
            url: '/pages/xianzhi/xianzhi',
            success: (res) => {
              let page = getCurrentPages().pop();
              if (page == undefined || page == null) return;
              console.log(page);
            },
          })
          wx.showToast({
            title: '发布成功！', // 标题
            icon: 'success', // 图标类型，默认success
            duration: 1500 // 提⽰窗停留时间，默认1500ms
          })
        }
      },

    })
  },
  //从本地获取照片 
  upimg: function () {
    var that = this;
    if (this.data.img_arr.length < 3) {
      wx.chooseImage({
        count: 3, //一次性上传到小程序的数量     
        sizeType: ['original', 'compressed'],
        sourceType: ['album', 'camera'],
        success(res) {
          console.log(res.tempFilePaths);
          that.setData({
            img_arr: that.data.img_arr.concat(res.tempFilePaths),
          })
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

  //删除照片功能与预览照片功能 
  deleteImg: function (e) {
    var that = this;
    var img_arr = that.data.img_arr;
    var index = e.currentTarget.dataset.index;
    wx.showModal({
      title: '提示',
      content: '确定要删除此图片吗？',
      success: function (res) {
        if (res.confirm) {
          console.log('点击确定了');
          img_arr.splice(index, 1);
        } else if (res.cancel) {
          console.log('点击取消了');
          return false;
        }
        that.setData({
          img_arr: img_arr
        });
      }
    })
  },
  previewImg: function (e) {
    var index = e.currentTarget.dataset.index;
    var img_arr = this.data.img_arr;
    wx.previewImage({
      current: img_arr[index],
      urls: img_arr
    })
  },
  //设置地址
  bindPickerChange: function (e) {
    console.log(e)
    this.setData({
      address: this.data.array[e.detail.value],
    });
    console.log(this.data.address)
  },
  //设置标题
  setText: function (e) {
    this.setData({
      text: e.detail.value
    });
    console.log(e.detail.value)
  },
  //设置价格
  setPrice: function (e) {
    this.setData({
      price: e.detail.value
    })
  },
})