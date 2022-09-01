import {
	postRequest
} from "./utils/request.js"
import {
	api
} from "./utils/api.js"

App({
	globalData: {
		chattingRecords:undefined,
	},
	onLaunch: function () {
		wx.login({
			success: res => {
				console.log(res)
				postRequest(api.findMemberBaseInfoByCode, {
					code: res.code
				}).then(result => {
					console.log(result)
					if (result.data.code == 200) {
						wx.setStorageSync('sessionKey', result.data.data.session_key);
						wx.setStorageSync('openId', result.data.data.openid);
						wx.setStorageSync('myUserId', result.data.data.userid);
						postRequest(api.getToken, {
							openId: result.data.data.openid,
							sessionKey: result.data.data.session_key
						}).then(tokenResult => {
							if (tokenResult.data.code == 200) {
								const token = tokenResult.data.data;
								console.log(token);
								wx.setStorageSync('userToken', token);
							}
						})
					} else {
						wx.showToast({
							title: '获取登陆凭证失败',
							icon: 'none',
							duration: 2000
						})
					}
				})
			}
		})
	},
})
