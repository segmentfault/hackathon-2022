//设备顶部状态高度
const statusBarHeight = uni.getSystemInfoSync().statusBarHeight + 'px'
//小程序头部与胶囊平行高度
const titleBarHeight = (uni.getMenuButtonBoundingClientRect().top - uni.getSystemInfoSync().statusBarHeight) * 2 + uni.getMenuButtonBoundingClientRect().height
//自定义头部高度
const topBarHeight = uni.getSystemInfoSync().statusBarHeight + titleBarHeight
let info = wx.getAccountInfoSync()
let envVersion = info.miniProgram.envVersion
export default {
    ...process.env.FRAMEWORK_CONFIG,
    statusBarHeight,
    titleBarHeight,
    topBarHeight,
    envVersion,
    // "baseUrl": "https://test-app.pinbar.vip"
    "baseUrl": "https://app.pinbar.vip"
}