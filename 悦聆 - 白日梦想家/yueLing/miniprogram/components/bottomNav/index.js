'use strict'
const app = getApp()
Component({
	properties: {
		// 当前选中的导航 首页 -> home 我的 -> my
		selected: {
			type: String,
			value: 'home'
		},
	},
	lifetimes: {
    attached: function() {
			this.setData({
				homeIndicatorHeight: app.globalData.homeIndicatorHeight
			})
    },
  },
	methods: {
		jump(e) {
			let { type } = e.currentTarget.dataset;
			console.log("类型,",type)
			wx.cloud.callFunction({
				name:'point',
				data:{
					text:type
				}
			})
			wx.switchTab({
				url: type === 'myMenu' ? '/pages/my/kithAndKin/index': '/pages/memo/index',
			})
		},
		addMemo() {
			wx.cloud.callFunction({
				name:'point',
				data:{
					text:'createS'
				}
			}).then(res=>{
				console.log("成功",res)
			})
			wx.navigateTo({
				url: '/pages/reminder/reminder',
			})
		}
	}
})
