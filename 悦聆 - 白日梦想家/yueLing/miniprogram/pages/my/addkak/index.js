//index.js
const app = getApp()
const db = wx.cloud.database()
Page({
	data: {
		index: 0,
		familyList: [],
		family: {
			name: '',
			phone: '',
			role: '',
			record: '',
		},
		type: '',
		userInfo:{}
	},
	onShow(){
		wx.hideHomeButton();
	  },
	onLoad: function (options) {
		let phone = options.phone
		console.log(options)
		this.setData({
			type: phone ? 'edit' : 'create',
			myself: !!options.myself,
			homeIndicatorHeight: app.globalData.homeIndicatorHeight
		})
		const openid = wx.getStorageSync('openid')
		db.collection('userInformation').where({
			_openid: openid
		}).get().then(res => {
			//判断是否是编辑自己的信息
			if(this.data.myself){
				this.setData({
				    userInfo:res,
					id:res.data[0]._id
				})
				this.setData({
					family:options,
				})
			} else if (phone) {
				let index = 0
				let allFamily = res.data[0].family
				let editTarget = allFamily.find(v => v.phone === phone)
				console.log("edit",editTarget)
				this.setData({
					allFamily: allFamily,
					family: editTarget,
					id: res.data[0]._id
				})
			} else {
				// let data = this.data.content
				this.setData({
					family: {
						name: '',
						phone: '',
						role: '',
						record: '',
					},
					index: 0,
				})
			}
		})
	},
	bindName(e) {
		this.setData({
			['family.name']: e.detail.value,
		})
	},
	bindRole(e){
		this.setData({
			['family.role']: e.detail.value,
		})
	},
	bindTel(e) {
		this.setData({
			['family.phone']: e.detail.value,
		})
	},
	delete() {
		let that = this;
		wx.showModal({
			title: '提示',
			content: '确认要删除当前亲友么？',
			success(res) {
				if (res.confirm) {
					let id = that.data.id;
					let targetIndex = that.data.allFamily.findIndex(v => v.phone === that.data.family.phone)
					that.data.allFamily.splice(targetIndex,1)
					db.collection('userInformation').doc(id).update({
						data: {
							family: that.data.allFamily
						}
					})
					wx.showToast({
						title: '删除成功',
						icon: 'none'
					})
					setTimeout(() => {
						wx.navigateBack({
						  delta: 10
						})
					  }, 400)
				} else if (res.cancel) {}
			}
		})
	},
	save() {
		let that = this;
		if (!this.data.family.name.trim()) {
			return wx.showToast({
				title: '请输入亲友昵称！',
				icon: 'none'
			})
		}
		if (!this.data.family.phone.trim() || !(/^1[23456789]\d{9}$/.test(this.data.family.phone))) {
			return wx.showToast({
				title: '请输入正确的亲友联系电话！',
				icon: 'none'
			})
		}
		if(that.data.myself){
			let id = that.data.id;
			let target =this.data.userInfo;
			target.nickName=this.data.family.name;
			target.phone = this.data.family.phone
			db.collection('userInformation').doc(id).update({
				data:{
					nickName:this.data.family.name,
					phone:this.data.family.phone
				}
			})
			wx.showToast({
				title: '保存成功',
				icon: 'none'
			})
			setTimeout(() => {
				wx.navigateBack({
				  delta: 10
				})
			  }, 400)
		} else if (that.data.type === 'edit') {
			let id = that.data.id;
			let target = this.data.allFamily.find(v => v.phone === this.data.family.phone)
			target.name = this.data.family.name
			target.phone = this.data.family.phone
			target.role = this.data.family.role
			db.collection('userInformation').doc(id).update({
				data: {
					family: this.data.allFamily
				}
			})
			wx.showToast({
				title: '保存成功',
				icon: 'none'
			})
			setTimeout(() => {
				wx.navigateBack({
				  delta: 10
				})
			  }, 400)
		} else {
			const openid = wx.getStorageSync('openid')
			db.collection('userInformation').where({
				_openid: openid
			}).get().then(res => {
				let id = res.data[0]._id
				let family = res.data[0].family?res.data[0].family:[];
				let editTarget = family.find(v => v.phone === this.data.family.phone)
				if (editTarget) {
					wx.showToast({
						title: '亲友已存在！',
						icon: 'none'
					})
				} else {
					family.push(this.data.family)
					db.collection('userInformation').doc(id).update({
						data: {
							family: family
						}
					})
					wx.showToast({
						title: '新增成功',
						icon: 'none'
					})
					wx.switchTab({
						url: '/pages/my/kithAndKin/index'
					})
				}
			})
		}
	},
	jump() {
		wx.redirectTo({
			url: '/pages/my/myVoice/index'
		})
	},
	choose() {
		let that = this
		wx.chooseContact({
			success: function (res) {
				that.setData({
					['family.phone']: res.phoneNumber,
				})
			},
			fail: function () {

			}
		})
	}
})