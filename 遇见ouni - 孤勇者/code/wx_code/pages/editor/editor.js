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
    formats: {},
    readOnly: false,
    placeholder: '开始输入...',
    editorHeight: 300,
    keyboardHeight: 0,
    isIOS: false
  },
  readOnlyChange() {
    this.setData({
      readOnly: !this.data.readOnly
    })
  },
  onLoad() {
    const platform = wx.getSystemInfoSync().platform
    const isIOS = platform === 'ios'
    this.setData({
      isIOS
    })
    const that = this
    this.updatePosition(0)
    let keyboardHeight = 0
    wx.onKeyboardHeightChange(res => {
      if (res.height === keyboardHeight) return
      const duration = res.height > 0 ? res.duration * 1000 : 0
      keyboardHeight = res.height
      setTimeout(() => {
        wx.pageScrollTo({
          scrollTop: 0,
          success() {
            that.updatePosition(keyboardHeight)
            that.editorCtx.scrollIntoView()
          }
        })
      }, duration)

    })
  },
  getEditor() {
    let that = this;
    wx.showModal({
      title: '提示',
      content: '是否发布？',
      success(res) {
        if (res.confirm) {
          that.editorCtx.getContents().then((e) => {
            console.log(e);
            if (e.html == "<p><br></p>") {
              return
            }
            postRequest(api.vomitslotPub, {
              userId: wx.getStorageSync("userId"),
              content: e.html + "&&" + e.text
            }).then((e) => {
              console.log(e);
              if (e.data.code == 200) {
                wx.showToast({
                  title: '发布成功',
                  icon: 'success',
                  duration: 2000 //持续的时间
                });
                setTimeout(() => {
                  wx.navigateTo({
                    url: '/pages/sign_in/sing_in',
                  })
                }, 2000);
              } else {
                wx.showToast({
                  title: '发布失败',
                  icon: 'loading',
                  duration: 2000 //持续的时间
                })
              }
            })
          })
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })

  },
  updatePosition(keyboardHeight) {
    const toolbarHeight = 50
    const {
      windowHeight,
      platform
    } = wx.getSystemInfoSync()
    let editorHeight = keyboardHeight > 0 ? (windowHeight - keyboardHeight - toolbarHeight) : windowHeight
    this.setData({
      editorHeight,
      keyboardHeight
    })
  },
  calNavigationBarAndStatusBar() {
    const systemInfo = wx.getSystemInfoSync()
    const {
      statusBarHeight,
      platform
    } = systemInfo
    const isIOS = platform === 'ios'
    const navigationBarHeight = isIOS ? 44 : 48
    return statusBarHeight + navigationBarHeight
  },
  onEditorReady() {
    const that = this
    wx.createSelectorQuery().select('#editor').context(function (res) {
      that.editorCtx = res.context
    }).exec()
  },
  blur() {
    this.editorCtx.blur()
  },
  format(e) {
    let {
      name,
      value
    } = e.target.dataset
    if (!name) return
    // console.log('format', name, value)
    this.editorCtx.format(name, value)

  },
  onStatusChange(e) {
    console.log(e.detail)
    const formats = e.detail
    this.setData({
      formats
    })
  },
  insertDivider() {
    this.editorCtx.insertDivider({
      success: function () {
        console.log('insert divider success')
      }
    })
  },
  clear() {
    this.editorCtx.clear({
      success: function (res) {
        console.log("clear success")
      }
    })
  },
  removeFormat() {
    this.editorCtx.removeFormat()
  },
  insertDate() {
    const date = new Date()
    const formatDate = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`
    this.editorCtx.insertText({
      text: formatDate
    })
  },
  insertImage() {
    const that = this
    wx.chooseImage({
      count: 1,
      success: function (res) {
        that.editorCtx.insertImage({
          src: res.tempFilePaths[0],
          data: {
            id: 'abcd',
            role: 'god'
          },
          width: '80%',
          success: function () {
            console.log('insert image success')
          }
        })
      }
    })
  }
})