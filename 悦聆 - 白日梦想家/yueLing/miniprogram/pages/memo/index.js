//index.js
import dayjs from "dayjs";
const DatePickerUtil = require('../../utils/DatePicker.js')
// const calendar = require('../../utils/LunarDay.js')
import { calendar } from '../../utils/LunarDay.js'
const app = getApp()
const db = wx.cloud.database()
const _ = db.command
Page({
	data: {
		mainColor: '#00C7BD',
		refreshTody: false,
		showLoading: false,
		pageSize: 10,
		pageIndex: 0,
		pageSizeToday: 10,
		pageIndexToday: 0,
		memoList: [],
		todayMemoList: [],
		active: 'today',
		src: '',
		Asrc:''
	},
	async onLoad(options) {
		wx.showShareMenu({
			withShareTicket: true,
			menus: ['shareAppMessage']
		  })
		const id =wx.getStorageSync('openid');
		console.log(options)
		if (options.active) {
			this.setData({
				active: options.active
			})
		}
		this.getTodyInfo();
		//首页加载埋点（pv）
		wx.cloud.callFunction({
			name:'point',
			data:{
				text:'myMenuPv'
			}
		})
		//首页加载埋点（uv）
		db.collection('point').where({
			myID:id
		}).get().then(res=>{
			console.log("ressfs",res)
			console.log("sbbs",res.data[0])
			if(!res.data[0]){
				db.collection('point').where({
					_id:'b69f67c062cf7de90b0a527d02974f39'
				}).update({
					data:{
						myID:_.push(id)
					}
				})
				wx.cloud.callFunction({
					name:'point',
					data:{
						text:'myOnload'
					}
				})
			}
			
		})
	},
	async onShow() {
		// 隐藏返回第一页按钮
		wx.hideHomeButton()
		// 获取用户信息
		this.data.openid = wx.getStorageSync('openid')
		const res = await db.collection('userInformation').where({
			_openid: this.data.openid
		}).get()

		this.data.phone = res.data[0].phone
		await this.getData('init')
		this.getTodyData('init')
	},
	// jump(e) {
	// 	this.getReminder()
	// },
	// getReminder() {
	// 	let openId = app.globalData.openId
	// 	db.collection('family').where({
	// 		_openid: openid
	// 	}).get().then((res) => {
	// 		console.log(res)
	// 	})
	// },
	onShareAppMessage(){
		return {
			title: '关怀备忘录',
			path: 'pages/memo/index',
	  　　　 imageUrl:''
		  }
	},
	jump() {
		wx.navigateTo({
			url: '/pages/reminder/reminder',
		})
	},
	onTap: function () {
		this.triggerEvent('onTap')
	},
	 text(e) {
		//语音合成

		const detail = e.currentTarget.dataset.smile;	
		if (detail.audio) {
			const innerAudioContext = wx.createInnerAudioContext();
			innerAudioContext.src = detail.audio;
			console.log("aaa",innerAudioContext)
			console.log("sssss",innerAudioContext.src)
			innerAudioContext.play();	
			innerAudioContext.play();
		}
		else {
			const myAudio = wx.createInnerAudioContext();
			this.audioCtx = wx.createAudioContext('myAudio');
			wx.cloud.callFunction({
				name: 'getVideo',
				data: {
					text: detail.content
				}
			}).then(res => {
				console.log("成功", res)
				this.setData({
					src: res.result.data.res.Audio
				})
				this.audioCtx.play();
			}).catch(res => {
				console.log("失败", res)
			})
		}
	},
	jump0(e) {
		console.log(e)
		wx.navigateTo({
			url: '/pages/reminder/reminder?id=' + this.data.item._id,
		})
	},
	jump1(e) {
		let id = e.currentTarget.dataset.item._id;
		wx.cloud.callFunction({
			name:'point',
			data:{
				text:'Memo'
			}
		})
		wx.navigateTo({
			url: '/pages/reminder/reminder?id=' + id,
		})
	},
	jumpMy() {
		wx.switchTab({
			url: '/pages/my/kithAndKin/index',
		})
	},
	goToSearch() {
		wx.redirectTo({
			url: '/pages/search/search',
		})
	},
	getTodyInfo() {
		let date = new Date();
		let lunarDayObj = calendar.solar2lunar(2021, 5, 22)
		let lunarDay = `${lunarDayObj.gzYear}年${lunarDayObj.IMonthCn}${lunarDayObj.IDayCn}`
		let month = date.getMonth() + 1;
		let day = date.getDate();
		let week = calendar.getWeekByDate()
		let dayName = new Array("", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
		let monName = new Array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
		this.setData({
			lunarDay: lunarDay,
			month: month,
			day: day,
			week: week,
			dayEn: dayName[date.getDay()],
			monEn: monName[date.getMonth()]
		})
	},
	async getData(type) {
		// 暂时全查出来
		const res = await db.collection('memo').where(
			_.or([
				{ _openid: this.data.openid },
				{ remindPhone: this.data.phone }
			])).orderBy('creatTime', 'desc').get()
		res.data.forEach(e => {
			if (e.needRemind) {
				e.remindTimeDate = dayjs(e.remindTimeDate).format('YYYY-MM-DD HH:mm:ss')
			}
		})
		this.setData({
			memoList: res.data,
		})

		// let pageSize = this.data.pageSize
		// let pageIndex = type === 'init' ? 0 : this.data.pageIndex
		// db.collection('memo') .where(
		// 	_.or([
		// 		{_openid: this.data.openid},
		// 		{remindPhone: this.data.phone}
		// 	]),
		// ).orderBy('updateTime', 'desc').skip(pageIndex*pageSize).limit(pageSize).get().then(res => {
		// 	let list
		// 	if(type && type === 'init') {
		// 		list = res.data
		// 	}else{
		// 		list = this.data.memoList.concat(res.data)
		// 	}
		// 	this.setData({
		// 		memoList: list,
		// 		refreshTody:false,
		// 		showLoading:false,
		// 	})
		// })
	},
	getTodyData(type) {
		let todaymemo = [];
		this.data.memoList.forEach(e => {
			//判断今日日程
			if (e.needRemind) {
				let today = new Date().toLocaleDateString()
				today = new Date(today + " 00:00:00").getTime()
				let memoTime = new Date(e.remindTimeDate.replace(/\-/g, '/')).getTime()
				console.log("memoTime", new Date(e.remindTimeDate.replace(/\-/g, '/')).getTime())
				if (new Date(memoTime).setHours(0, 0, 0, 0) === new Date(today).setHours(0, 0, 0, 0)) {
					todaymemo.push(e)
				}
			}
		})
		this.setData({
			todayMemoList: todaymemo
		})
		// let pageSize = this.data.pageSizeToday
		// let pageIndex = type === 'init' ? 0 : this.data.pageIndexToday
		// var today = new Date().toLocaleDateString()
		// console.log(new Date(today+" 00:00:00"))
		// db.collection('memo').where(_.or([
		// 	{_openid: this.data.openid},
		// 	{remindPhone: this.data.phone}
		// ]), _.and([{
		// 	remindTimeDate: _.and(_.gte(new Date(today+" 00:00:00")),_.lte(new Date(today+" 23:59:59")))
		// }])).orderBy('updateTime', 'desc').skip(pageIndex*pageSize).limit(pageSize).get().then(res => {
		// 	console.log(res.data)
		// 	let list
		// 	if(type && type === 'init') {
		// 		list = res.data
		// 	}else{
		// 		list = this.data.todayMemoList.concat(res.data)
		// 	}
		// 	console.log(list)
		// 	this.setData({
		// 		todayMemoList: list,
		// 		refreshTody:false,
		// 		showLoading:false,
		// 	})
		// })
		// 暂时全部查出来
	},
	onChange(e) {
		this.setData({
			active: e.detail.name
		})
		wx.cloud.callFunction({
			name:'point',
			data:{
				text:e.detail.name
			}
		})
		console.log(this.data.active)
	},
	handleRefresh() {
		// if(this.data.active === 'today') {
		// 	this.setData({
		// 		pageIndexToday: 0
		// 	})
		// 	this.getTodyData('init')
		// }else{
		// 	this.setData({
		// 		pageIndex: 0
		// 	})
		// 	this.getData('init')
		// }
	},
	async handleGetMore() {
		// let countResult
		// let total
		// var today = new Date().toLocaleDateString()
		// if(this.data.active === 'today') {
		// 	countResult = await db.collection('memo').where({
		// 		_openid: this.data.openid,
		// 		remindTimeDate: _.and(_.gte(new Date(today+" 00:00:00")),_.lte(new Date(today+" 23:59:59")))
		// 	}).count()
		// 	total = countResult.total
		// 	if(this.data.pageIndexToday < Math.ceil(total / this.data.pageSize)) {
		// 		this.setData({
		// 			pageIndexToday: this.data.pageIndexToday +  1,
		// 			showLoading:true,
		// 		})
		// 		this.getTodyData()
		// 	}
		// }else{
		// 	countResult = await db.collection('memo').where({
		// 		_openid: this.data.openid
		// 	}).count()
		// 	total = countResult.total
		// 	if(this.data.pageIndex < Math.ceil(total / this.data.pageSize)) {
		// 		this.setData({
		// 			pageIndex: this.data.pageIndex +  1,
		// 			showLoading:true,
		// 		})
		// 		this.getData()
		// 	}
		// }
	}
})
