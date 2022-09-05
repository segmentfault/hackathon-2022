//index.js
const app = getApp()
const db = wx.cloud.database()
const _ = db.command
Page({
	data: {
		curIndex: -1
	},
	// 授权登录
	onGetUserInfo(e) {
		this.setData({
			curIndex: e.target.dataset.key
		})
		// 默认创建一个用户
		wx.getUserProfile({
			desc: '用于完善会员资料',
			success: res => {
				db.collection('userInformation').add({
					data: {
						phone: '',
						avatarUrl: res.userInfo.avatarUrl,
						nickName: res.userInfo.nickName,
						createTime: new Date()
					}
				})
				wx.redirectTo({
					url: `/pages/appellation/appellation?roleType=${this.data.curIndex}&nickName=${res.userInfo.nickName}`,
				})
			}
		})
	}
})
