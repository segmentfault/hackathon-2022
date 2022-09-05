Page({
	onLoad() {
		//判断到哪一个页面

		const welecome = wx.getStorageSync('welcome');
			if (welecome) {
				wx.switchTab({
					url: '/pages/memo/index',
				})
			} else {
				
				wx.redirectTo({
					url: '/pages/welcomePage/index',
				})
			}
		
	}
})
