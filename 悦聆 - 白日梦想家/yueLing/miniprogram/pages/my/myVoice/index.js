//index.js
const app = getApp()

Page({
	data: {
		type: 'def'
	},
	onLoad: function () {},
	jump() {
		wx.showToast({
			title: '人声模拟完成',
			icon: 'none'
		})
		setTimeout(() => {
			wx.redirectTo({
				url: '/pages/my/addkak/index'
			})
		}, 400)
	},
	modifyType() {
		this.setData({
			type: 'underway'
		})
		setTimeout(() => {
			this.setData({
				type: 'def'
			})
		}, 4000)
	}
})
