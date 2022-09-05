//index.js
const app = getApp()
const db = wx.cloud.database()
const _ = db.command
Page({
	data: {
		familyList: [],
		userInfo:{},
	},
	async onLoad(){
		wx.showShareMenu({
			withShareTicket: true,
			menus: ['shareAppMessage']
		  })
		const id =wx.getStorageSync('openid');
		wx.cloud.callFunction({
			name:'point',
			data:{
				text:'homeOn'
			}
		})
		//我的页面加载埋点（uv）
		db.collection('point').where({
			homeID:id
		}).get().then(res=>{
			console.log("ressfs",res)
			console.log("sbbs",res.data[0])
			if(!res.data[0]){
				db.collection('point').where({
					_id:'b69f67c062cf7de90b0a527d02974f39'
				}).update({
					data:{
						homeID:_.push(id)
					}
				})
				wx.cloud.callFunction({
					name:'point',
					data:{
						text:'homeOnload'
					}
				})
			}
			
		})
	},
	//关闭返回
    async onShow(){
	  wx.hideHomeButton();
	  this.getData()
	},
	onShareAppMessage(){
		return {
			title: '关怀备忘录',
			path: 'pages/my/kithAndKin/index',
	  　　　 imageUrl:''
		  }
	},
	edit(e) {
		let phone = e.currentTarget.dataset.item.phone;
		let name = e.currentTarget.dataset.item.nickName;
		let purePhoneNumber = wx.getStorageSync('purePhoneNumber');
		// 如果编辑自己不显示身份
		if (e.currentTarget.dataset.item.phone === this.data.userInfo.phone) {
			wx.navigateTo({
				url: '/pages/my/addkak/index?phone=' + phone + '&myself=' + true+'&name='+name
			})
		} else {
			wx.navigateTo({
				url: '/pages/my/addkak/index?phone=' + phone,
			})
		}
		//埋点
		wx.cloud.callFunction({
			name:'point',
			data:{
				text:'friendsChange'
			}
		})
	},
	createFamily() {
		//埋点
		wx.cloud.callFunction({
			name:'point',
			data:{
				text:'addFriends'
			}
		})
		wx.navigateTo({
			url: '/pages/my/addkak/index',
		})
	},
	jump() {
		wx.navigateTo({
			url: '/pages/reminder/reminder'
		})
	},
	goOut(){
		wx.navigateTo({
			url: '/pages/out/out'
		})
	},
	getData() {
	const openid = wx.getStorageSync('openid')
		db.collection('userInformation').where({
			_openid: openid
		}).get().then(res => {
			this.setData({
				familyList: res.data[0].family,
				userInfo:res.data[0]
			})
			
		})
	}
})