<template>
	<view>
		<view class="u-padding-left-24 u-padding-right-24" style="overflow: hidden;	">
			<!-- 标题 start -->
			<view class="page_title">{{data.title}}</view>
			<view class="u-flex u-col-center u-row-between u-margin-top-20 fw400">
				<view class="lf u-flex u-col-center ">
					<text class="u-margin-right-25 u-font-22" style="color: #AEADB3;"># 拼叭 | 组人组局拼叭</text>
					<u-icon name="eye-fill" color="#aeadb3" size="40"></u-icon>
					<text class="u-font-22" style="color: #AEADB3;">{{data.readNum}}</text>
				</view>
				<view class="ri">{{formateDate(data.createTime)}}</view>
			</view>
			<!-- 标题 end -->

			<!-- 富文本 start -->
			<view class="main">
				<u-parse :html="data.content"></u-parse>
			</view>
			<!-- 富文本 end -->

			<view v-if="data.voteList.length>0">
				<view v-for="(item,index) in data.voteList" :key='index'>


					<view>
						<!-- 投票标题 start -->
						<view class="u-flex u-row-right u-margin-top-66">
							<view class="vote_tit">{{item.name}}</view>
						</view>
						<!-- 投票标题 end -->

						<!-- 投票 start -->
						<!--  未投票 -->
						<view class="vote" v-if="!item.isVote">
							<view class="vote_main u-padding-left-56   u-padding-right-56">
								<view @click="addRecord(item,items)" class="list u-flex u-col-center " :class="items.isSelect?'active':''"
									v-for="(items,indexs) in item.optionItemList" :key='indexs'>
									<image v-if='items.isSelect' src="../../static/istrue.png" mode="widthFix"></image>
									<image  v-else src="../../static/isfalse.png"
										mode="widthFix"></image>
									<!-- <view class="u-flex-1  u-margin-left-22 u-font-28">吃青团</view> -->
									<u-parse style="margin-left:20upx;color: #41B26B;" :html="items.content"></u-parse>
								</view>
								<!-- 单选 end -->
								<!-- 单选 start -->
							</view>
						</view>

						<view class="vote" v-else>
							<view v-for="(itmex,indexx) in voteList" :key='indexx'>
								<view class="vote_main u-padding-left-56  u-padding-right-56">
									<view class="lists u-margin-top-24" v-for="(items,indexs) in itmex.optionItemList"
										:key='indexs'>
										<u-parse style="margin-left:20upx;color: #41B26B;" :html="items.content">
										</u-parse>
										<view class="u-flex u-col-center">
											<u-line-progress class="u-flex-1"
												:active-color="items.isSelect?'#41b26b':'#b2b2b2'"
												:percent="items.percentage" :striped-active="true"></u-line-progress>
											<text class="u-margin-left-24">{{items.total}}票</text>
											<text class="u-margin-left-24">{{items.percentage}}%</text>
										</view>
									</view>

								</view>
							</view>
						</view>


						<!-- 投票 end -->
					</view>
				</view>
			</view>
			<!-- 推荐商家 stratr -->
			<!-- pagesB/commodity/index -->
			<view class="u-margin-top-32 recommend" v-if="storeList.length>0">
				<image class="title" src="../../static/twtit.png" mode="widthFix"></image>
				<view class="store_box u-flex u-col-top" v-for="(item,index) in storeList" :key='index'
					@click="storeUrl(item)">
					<image :src="item.cover" style="width: 152upx;height: 152upx;" mode="widthFix"></image>
					<view class=" u-col-top u-flex-1 u-margin-left-32 store_box_inner" style="overflow: hidden;">
						<view class="name u-line-1 u-margin-top-20">{{item.companyName}}</view>
						<view class="u-flex u-margin-top-12">
							<scroll-view scroll-x class="scroll u-flex-1" style="white-space: nowrap;overflow: hidden;">
								<view class="item" v-for="(items,indexs) in item.cates" :key='indexs'>{{items}}</view>
							</scroll-view>
							<view class="u-flex u-col-center">
								<image style="width:27upx;height: 27upx;" src="../../static/img1.png" mode="widthFix">
								</image>
								<text class="u-margin-left-11 u-font-26"
									style="color: #F14F40;">{{setMorKm(item.distance)}}</text>
							</view>
						</view>
						<view class="detail u-line-1">{{item.companyAddress}}</view>
					</view>
				</view>
			</view>
			<!-- 推荐商家 end -->

			<!-- banner strat -->
			<view class="banner u-margin-top-20" v-if="data.banners.length>0">
				<u-swiper @click='bannerBtn' circular autoplay name="picUrl" :list="data.banners"></u-swiper>
			</view>
			<!-- <view style="width: 100%;">
				<official-account></official-account>
			</view> -->
			<!-- banner end -->
			<view class="guanzhu">
				<image src="../../static/logo.png" class="imgs" mode="widthFix"></image>
				<view class="inner">拼叭 | 组人组局用拼叭</view>
				<view class="btn" @click="btngz">关注公众号</view>
			</view>


			<view class="live_list">
				<view @click="show2(item)" class="wrap_live u-flex u-col-top u-margin-bottom-20"
					v-for="(item,index) in tweetList" :key='index'>
					<image class="image" :src="item.userHead" mode="widthFix"></image>
					<view class="u-flex-1" style="overflow: hidden;">
						<view class="u-font-28 u-line-1" style="color: #838287;"><text>{{item.userName}}</text>
							<image v-if="item.isTop" class="u-margin-left-6" mode="widthFix"
								src="../../static/toptips.png"
								style="width: 64upx;height: 26upx;display: inline-block;vertical-align: middle;">
							</image>
						</view>
						<view class="u-margin-top-15 u-font-28" style="line-height: 35upx;color: #000000;">
							{{item.messageContent}}
						</view>
						<!-- 回复时间 wrap -->
						<view class="u-flex u-row-right u-margin-top-22">
							<view class="tips u-flex u-col-center u-row-between" style="width: 542upx;">
								<text class="u-font-26" style="color: #AEADB3;">{{timeFormat(item.createTime)}}</text>
								<view @click.stop="cancelPraise(2,item)" v-if="item.praise" class="u-flex u-col-center "
									style="height: 100%;padding-right: 20upx;">
									<text class="u-margin-right-10 u-font-24">{{likeNum(item.praiseNum)}}</text>
									<image :src="item.praise?'../../static/live2.png':'../../static/live1.png'"
										mode="widthFix"></image>
								</view>
								<view @click.stop="Praise(2,item)" v-else class="u-flex u-col-center "
									style="height: 100%;padding-right: 20upx;">
									<text class="u-margin-right-10 u-font-24">{{likeNum(item.praiseNum)}}</text>
									<image :src="item.praise?'../../static/live2.png':'../../static/live1.png'"
										mode="widthFix"></image>
								</view>
							</view>
						</view>
						<!-- 回复 -->
						<view v-show="item.isShow">
							<view @click.stop="show2(item)" class="wrap_live u-flex u-col-top u-margin-top-32"
								v-for="(items,indexs) in item.list" :key='indexs'>
								<image class="image" :src="items.replierHead" mode="widthFix"></image>
								<view class="u-flex-1" style="overflow: hidden;">
									<view class="u-font-28 u-line-1" style="color: #838287;">
										{{items.replierName}}
										<u-icon class="u-margin-left-4 u-margin-right-4" name="play-right-fill"
											color="#aeadb3" size="20"></u-icon> {{items.repliedName}}
									</view>
									<view class="u-margin-top-15 u-font-28" style="line-height: 35upx;color: #000000;">
										{{items.replyContent}}
									</view>
									<!-- 回复时间 wrap -->
									<view class="u-flex u-row-right u-margin-top-22">
										<view class="tips u-flex u-col-center u-row-between" style="width: 542upx;">
											<text class="u-font-26"
												style="color: #AEADB3;">{{timeFormat(items.replyTime)}}</text>
											<!-- <view class="u-flex u-col-center ">
												<text class="u-margin-right-10 u-font-24">{{items.praiseNum}}</text>
												<image @click.stop="cancelPraise(3,items)" v-if='items.praise'
													src="../../static/live2.png" mode="widthFix"></image>
												<image @click.stop="Praise(3,items)" v-else src="../../static/live1.png"
													mode="widthFix"></image>
											</view> -->
											<view @click.stop="cancelPraise(3,items)" v-if='items.praise'
												class="u-flex u-col-center " style="height: 100%;padding-right: 20upx;">
												<text
													class="u-margin-right-10 u-font-24">{{likeNum(items.praiseNum)}}</text>
												<image
													:src="items.praise?'../../static/live2.png':'../../static/live1.png'"
													mode="widthFix"></image>
											</view>
											<view @click.stop="Praise(3,items)" v-else class="u-flex u-col-center "
												style="height: 100%;padding-right: 20upx;">
												<text
													class="u-margin-right-10 u-font-24">{{likeNum(items.praiseNum)}}</text>
												<image
													:src="items.praise?'../../static/live2.png':'../../static/live1.png'"
													mode="widthFix"></image>
											</view>
										</view>
									</view>
								</view>
							</view>
						</view>
						<!-- 展开 -->
						<view v-if='item.replyNum>0'
							class="u-flex u-col-center u-font-28 u-padding-top-32 u-padding-bottom-32"
							style="color: #2C4C86;"><text v-if="item.isData"
								@click.stop="itemMessage(item)">展开剩余{{item.replyNum-item.list.length}}条回复
							</text><text style="text-decoration: underline ; margin-left: 10upx;font-size: 23upx;"
								@click.stop="clickShow(item)"
								v-if="item.list.length>0">{{!item.isShow?'展开':'收起'}}</text>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 底部悬浮 -->
		<view class="fixedBom u-flex">
			<view class="iconbox">
				<image @click="cancelPraise(1)" v-if="data.praise" src="../../static/live2.png" mode="widthFix"></image>
				<image @click="Praise(1)" v-else src="../../static/live3.png" mode="widthFix"></image>
				<text class="u-line-1">{{data.praiseNum}}</text>
			</view>
			<view class="iconbox" @click="show1(true)">
				<image src="../../static/huifu.png" mode="widthFix"></image>
				<text class="u-line-1">{{tweetListToat}}</text>
			</view>
			<button open-type="share" class="u-reset-button iconbox">
				<image src="../../static/fx.png" mode="widthFix"></image>
				<text class="u-line-1">{{data.shareNum}}</text>
			</button>

			<view class="u-flex-1 u-margin-left-20" style="overflow: hidden;">
				<view class="disIput u-flex u-col-center" @click="show1(false)">
					<image src="../../static/disicon.png" mode="widthFix"></image>
					<text>快说点什么...</text>
				</view>
			</view>
		</view>
		<!-- 底部占位块 start -->
		<view style="height: 180upx;"></view>
		<!-- 底部占位块 end -->
		<view class="showModel" v-if='show' @click.stop="modelShow">
			<view class="comment u-flex u-col-center u-margin-right-24 " @click.stop>
				<textarea :cursor-spacing='20' placeholder="最多输入140字..." :focus='true' placeholder-class="placsPlace" :show-confirm-bar='false'
					class="u-flex-1 inputs" style="height: 80upx;" v-model="value" />
				<view class="btn" :class="isTrue?'bg2':'bg1'" @click.stop="okValue">发布</view>
			</view>
		</view>


		<u-popup height="auto" v-model="shows" mode="center" @open='openUp' @close='gzclose' z-index='999999'>
			<view class="uModels ">
				<view class="title">关注公众号，及时收到回复消息</view>
				<view style="padding: 40upx;margin: 0 auto;">
				
					<image src="../../static/ewm.jpg" :show-menu-by-longpress='true' @longtap="longtap" style="width: 450upx; display: block;margin:  0 auto;" mode="widthFix" ></image>
				</view>
				<!-- <view style="width: 100%; " class="u-padding-left-34 u-padding-right-34">
					<official-account @bindload='bindload' @binderror='binderror'></official-account>
				</view> -->
				<view class="u-margin-top-48">
					<u-line color="#CCCCCC"></u-line>
				</view>
				<view class="btn" @click="shows = false">关闭</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	import api from "../../service/content.js";
	import game from '../../service/game.js';
	// import login form
	import LoginService from '../../service/login.js'



	import moment from 'moment';
	export default {

		computed: {
			isTrue() {
				if (this.$u.test.isEmpty(this.value)) {
					return false
				} else {
					return true
				}
			},
			setUrl(){
				return this.ewmUrl.replace(/[\r\n]/g, "");
			}
		},
		data() {
			return {
				shows: false,
				content: ``,
				id: '', //推文id
				data: {
					voteList: [],
					supplierIds: []
				},
				voteList: [],
				storeList: [], //推荐商家
				messagePage: { //推文分页
					pageNo: 1,
					pageSize: 10
				},
				tweetList: [],
				tweetListToat: 0, //共多少条留言
				value: '',
				show: false,
				showType: false, // true 回复的是推文 flase 回复的是 留言
				itemData: '', //保存回复数据
				ewmUrl:''

			}

		},
		onReachBottom() {
			this.message(this.messagePage)
		},
		computed: {
			isCheckLogin() {
				return !this.$checkLogin()
			}
		},
		onLoad(e) {
		  uni.showLoading({
		  	title: '加载中',
			mask:true
		  });
			// let p = new Promise((resolve, reject) => {
			// 	wx.getSetting({
			// 		success: res => {
			// 			console.log('用户权限列表:', res.authSetting)
			// 			if (res.authSetting['scope.userLocation']) {
			// 				resolve('已授权userLocation')
			// 			} else {
			// 				resolve('用户未授权userLocation')
			// 			}
			// 		}
			// 	})
			// })
			// p.then(res => {
			// 	this.getUserLoacton()
			// })
			
			this.id = e.id
			// this.getuserinfo()
			this.getContent()
			this.getRecord()
			this.message()

			//埋点
			game.getEventRecord({
				userId: uni.getStorageSync('userInfo').userId || 0,
				eventId: 262,
				articleId: e.id || ""
			});
		},
		methods: {
			 getBase64ImageUrl(base64Url) {
			        /// 获取到base64Data
			        var base64Data = base64Url;
			        /// 通过微信小程序自带方法将base64转为二进制去除特殊符号，再转回base64
			        base64Data = wx.arrayBufferToBase64(wx.base64ToArrayBuffer(base64Data));
			        /// 拼接请求头，data格式可以为image/png或者image/jpeg等，看需求
			        const base64ImgUrl = "data:image/png;base64," + base64Data;
			        /// 得到的base64ImgUrl直接给图片:src使用即可
			        return base64ImgUrl;
			    },
			longtap(){
				this.reCord({
					eventId: 266,
				})
			},
			openUp() {
				this.reCord({
					eventId: 271,
				})

			},
			async gzclose() {
				this.getContent()
				let data = await LoginService.getUserInfo()
				if (this.$u.test.isEmpty(data.subscription)) { //undefined

				} else if (data.subscription === 1) {
					this.reCord({
						eventId: 266,
					})
				}
			},
			btngz() {
				this.shows = true
			},
			likeNum(num) {
				if (num === 0) {
					num = 0;
				} else if (num > 9999 && num <= 9999999) {
					num = parseInt(num / 10000);
					num = parseInt(num) + "w";
				}
				// else if (num > 9999999) {
				//     num = parseInt(num / 10000000);
				//     num = parseInt(num) + "千万";
				// }
				return num;

			},
			bannerBtn(e) {
				let that = this;
				if (this.data.banners[e].jumpUrl != undefined) {
					uni.navigateTo({
						url: this.data.banners[e].jumpUrl,
						success() {
							that.reCord({
								eventId: 265,
								bannerId: that.data.banners[e].id
							})
						}
					})
				}
			},
			reCord(params) { //统一埋点
				let obj = {
					userId: uni.getStorageSync('userInfo').userId || 0,
					articleId: this.id

				}
				let objs = Object.assign(obj, params)
				game.getEventRecord(objs);

			},
			timeFormat(time) {
				if (time > 0) {
					var result
					time = parseInt(time);
					var minute = 1000 * 60;
					var hour = minute * 60;
					var day = hour * 24;
					var month = day * 30;
					var now = new Date().getTime();
					var diffValue = now - time;
					if (diffValue < 0) {
						return
					}
					var monthC = diffValue / month;
					var weekC = diffValue / (7 * day);
					var dayC = diffValue / day;
					var hourC = diffValue / hour;
					var minC = diffValue / minute;
					if (monthC >= 1) {
						if (monthC <= 12) {
							result = "" + parseInt(monthC) + "月前";
						} else {
							result = "" + parseInt(monthC / 12) + "年前";
						}
					} else if (weekC >= 1) {
						result = "" + parseInt(weekC) + "周前";
					} else if (dayC >= 1) {
						result = "" + parseInt(dayC) + "天前";
					} else if (hourC >= 1) {
						result = "" + parseInt(hourC) + "小时前";
					} else if (minC >= 1) {
						result = "" + parseInt(minC) + "分钟前";
					} else {
						result = "刚刚";
					}
					return result
				} else {
					return '';
				}
			},

			setMorKm(m) {
				var n = ''
				if (m) {
					if (m >= 1000) {
						n = (m / 1000).toFixed(2) + 'km'
					} else {
						n = m + 'm'
					}
				} else {
					n = '0m'
				}
				return n
			},
			storeUrl(item) {
				uni.$u.route({
					url: 'pagesB/commodity/index',
					params: {
						typeId: item.typeId,
						supplierId: item.supplierId
					}
				})
				this.reCord({
					eventId: 264,
					supplierId: item.supplierId
				})
			},
			show1(istrue) { //区分推文回复
				if (this.data.isForceFollow) { //是否为强制关注用户
					if (!this.data.subscription) { //是否已关注
						this.shows = true
						return uni.showToast({
							title: '请先关注公众号',
							icon: 'none'
						})
					}

				}
				if(istrue){
					this.reCord({
						eventId: 269,
					})
				}else{
					this.reCord({
						eventId: 270,
					})
				}
				this.show = true
				this.showType = true
				this.getuserinfo()
			},
			show2(item) { //区分留言回复
				if (this.data.isForceFollow) { //是否为强制关注用户
					if (!this.data.subscription) { //是否已关注
						this.shows = true
						return uni.showToast({
							title: '请先关注公众号',
							icon: 'none'
						})
					}

				}
				this.show = true
				this.showType = false
				this.itemData = item
				this.getuserinfo()
			},
			modelShow() {
				this.show = !this.show
				this.value = ''
			},
			clickShow(item) {
				item.isShow = !item.isShow
			},
			seleCT(item, index) { //投票点击选择 
				this.$nextTick(function() {
					this.list[index].istrue = !this.list[index].istrue
				})
			},

			//推文留言
			messageAdd() {
				api.messageAdd({
					articleId: this.id,
					messageContent: this.value,
				}).then(res => {
					uni.showToast({
						title: '发布成功',
					})
					this.modelShow()
					this.message()
				
				})
			},

			//回复留言
			itemessageAdd(id) {
				api.itemessageAdd({
					messageId: this.itemData.id,
					replyContent: this.value
				}).then(res => {
					uni.showToast({
						title: '发布成功',
					})
					this.modelShow()
					this.message()
					this.reCord({
						eventId: 270,
					})
				})
			},
			//投票
			addRecord(item, items) {
				if (this.data.isForceFollow) { //是否为强制关注用户
					if (!this.data.subscription) { //是否已关注
						this.shows = true
						return uni.showToast({
							title: '请先关注公众号',
							icon: 'none'
						})
					}

				}
				items.isSelect = !items.isSelect
				api.addRecord({
					articleId: this.id,
					voteId: item.id,
					optionList: [items.optionId]
				}).then(res => {
					this.getContent()
					this.getRecord()
					this.reCord({
						eventId: 263,
					}) // 投票埋点
				})
			},
			/*
			  发布按钮
			
			*/
			okValue(type) {
				if (this.$u.test.isEmpty(this.value)) {
					uni.showToast({
						title: '请输入内容',
						icon: 'none'
					})
				} else {
					if (this.showType) { //推文回复
						this.messageAdd()
					} else { //推文留言回复
						this.itemessageAdd()
					}
				}

			},




			/*
			  type ：(1-文章,2-留言,3-回复
			*/
			Praise(type, item) {
				if (this.data.isForceFollow) { //是否为强制关注用户
					if (!this.data.subscription) { //是否已关注
						this.shows = true
						return uni.showToast({
							title: '请先关注公众号',
							icon: 'none'
						})
					}

				}
				if (type === 1) { //推文点赞
					api.praise({
						businessId: this.id,
						type: type
					}).then(res => {
						this.data.praiseNum++
						this.data.praise = true
						this.reCord({
							eventId: 268
						})
					})
				} else if (type === 2) { //留言
					api.praise({
						businessId: item.id,
						type: type
					}).then(res => {
						item.praiseNum++
						item.praise = true
						this.reCord({
							eventId: 267
						})
					})
				} else if (type === 3) { //推文回复
					api.praise({
						businessId: item.id,
						type: type
					}).then(res => {
						item.praiseNum++
						item.praise = true
						this.reCord({
							eventId: 267
						})
					})
				}

			},



			/*
			  type ：(1-文章,2-留言,3-回复
			*/
			cancelPraise(type, item) {
				if (type === 1) { //推文点赞
					api.cancelPraise({
						businessId: this.id,
						type: type
					}).then(res => {
						this.data.praiseNum--
						this.data.praise = false
					})
				} else if (type === 2) { //留言
					api.cancelPraise({
						businessId: item.id,
						type: type
					}).then(res => {
						item.praiseNum--
						item.praise = false
						
					})
				} else if (type === 3) { //推文回复
					api.cancelPraise({
						businessId: item.id,
						type: type
					}).then(res => {
						item.praiseNum--
						item.praise = false
					})
				}

			},

			//获取推文留言列表
			message(params) {
				let isParams = uni.$u.test.isEmpty(params)
				console.log(isParams)
				let messagePage = {}
				if (isParams) { //空对象刷新重载
					this.messagePage.pageNo = 1
				}
				messagePage = Object.assign(this.messagePage, params)
				messagePage.articleId = this.id;
				api.message(messagePage).then(res => {
					res.list.map((item) => {
						item.list = []
						item.isData = true // 后续是否还有数据
						item.pageNo = 1
						item.pageSize = 10
						item.istrue = false //是否显示回复
						item.isShow = true //收起功能
						// if (item.replyId === undefined) { //是否有回复
						// } else {
						// 	item.list=item.list.push(item)

						// }
					})
					this.tweetListToat = res.total
					if (isParams) { //重载
						this.tweetList = res.list
					} else { //下载加载 分页
						this.tweetList = this.tweetList.concat(res.list)
					}
					if (res.list.length > 0) {
						this.messagePage.pageNo++
					}
				})
			},

			/*
			  加载评论的回复
			  item * 外层主留言数据
			*/
			itemMessage(item) {

				let params = {
					pageNo: item.pageNo,
					pageSize: item.pageSize,
					messageId: item.id
				}
				api.itemMessage(params).then(res => {
					if (res.list.length > 0) {
						item.list = item.list.concat(res.list)
						item.pageNo++
					}
					if (res.list.length === 0 || res.list.length < 10) { //加载出来的数据不足分页数量时提示
						item.isData = false
					}
				})

			},


			//获取投票列表
			async getRecord() {
				this.voteList = await api.getRecord({
					id: this.id,
				});
			},

			//获取用户信息
			async getuserinfo() {
				let res = await this.$authorization();
				res == true ? (this.flg = true) : (this.flg = false);
			},
			//获取文章详情
			async getContent() {
				this.data = await api.articleget({
					id: this.id,
				});
				setTimeout(item=>{
					uni.hideLoading();
				},1000)
				if (this.data.suppliers != undefined) {
					this.storeList = this.data.suppliers
				}
			},
			getUserLoacton() {
				uni.getLocation({
					type: 'gcj02',
					success(res) {
						let location = {
							lng: res.longitude,
							lat: res.latitude
						}
						api.updateInfo(location).then(rerult => {
							console.log(rerult)
						})
					}
				})
			},

			//格式化时间
			formateDate(time) {
				return moment(time).format("YYYY-MM-DD HH:mm:ss")
			}

		},
		onShareAppMessage(res) {
			if (res.from === 'button') { // 来自页面内分享按钮
				this.reCord({
					eventId: 235
				})
				this.data.shareNum++

			}
			return {
				title: this.data.title,
				imageUrl: this.data.bannerUrl,
				path: `/pagesA/tweet/tweet?id=${this.id}`
			}
		}

	}
</script>
<style>
	page {
		height: 100%;
		background-color: #f1f0f5;
	}
</style>
<style lang="scss" scoped>
	.guanzhu {
		padding: 0 32upx 0 32upx;
		background-color: white;
		height: 124upx;
		border-radius: 0 0 40upx 40upx;
		display: flex;
		align-items: center;

		.imgs {
			width: 72upx;
			height: 72upx;
			margin-right: 24upx;
		}

		.inner {
			flex: 1;
			font-size: 30upx;
			color: #000000;
		}

		.btn {
			width: 168upx;
			height: 56upx;
			background-color: #41B26B;
			border-radius: 12upx;
			line-height: 56upx;
			text-align: center;
			font-size: 20upx;
			color: #FFFFFF;
		}
	}

	.uModels {
		width: 670upx;
		overflow: hidden;

		.title {
			font-size: 32upx;
			line-height: 44upx;
			color: #000000;
			font-weight: bold;
			text-align: center;
			margin-top: 32upx;
			margin-bottom: 45upx;

		}

		.btn {
			height: 108upx;
			line-height: 108upx;
			text-align: center;
			font-size: 32upx;
			color: #7FC555;
			font-weight: bold;
		}
	}

	.showModel {
		position: fixed;
		bottom: 0;
		left: 0;
		z-index: 999;
		width: 100vw;
		height: 100vh;
		background-color: rgba(0, 0, 0, .6);

		.comment {
			position: absolute;
			left: 0;
			bottom: 0;
			width: 100%;
			padding: 24upx;
			background-color: #F1F0F5;

			.inputs {
				box-sizing: border-box;
				background-color: #ffffff;
				border-radius: 24upx;
				margin-right: 24upx;
				padding-left: 30upx;
				padding-top: 10upx;
				padding-bottom: 10upx;
				line-height: 1.2;

			}

			.btn {
				width: 104upx;
				height: 72upx;
				border-radius: 36upx;
				line-height: 72upx;
				font-size: 28upx;
				color: #FFFFFF;
				text-align: center;
			}

			.btn.bg1 {
				background-color: #838287;
			}

			.btn.bg2 {
				background-color: #020002;
			}

		}
	}

	.fixedBom {
		position: fixed;
		z-index: 100;
		left: 0;
		bottom: 0;
		width: 100%;
		// height: 160upx;
		background-color: white;
		padding: 24upx 56upx 56upx 26upx;

		.iconbox {
			display: flex;
			flex-direction: column;
			align-items: center;
			padding: 0 20upx;

			image {
				width: 36upx;
				height: 36upx;
			}

			text {
				color: #000000;
				font-size: 22upx;
			}
		}

		.disIput {
			border-radius: 40upx;
			box-sizing: border-box;
			height: 80upx;
			background-color: #F1F0F5;
			padding-left: 31upx;

			image {
				width: 36upx;
				height: 36upx;
			}

			text {
				font-size: 28upx;
				color: #AEADB3;
			}
		}

	}

	.live_list {
		margin-top: 56upx;
		padding-right: 20upx;

		.wrap_live {
			.image {
				width: 80upx;
				height: 80upx;
				border-radius: 50%;
				background-color: #ccc;
				margin-right: 16upx;
			}

			.tips {
				height: 80upx;
				border-radius: 0 40upx 40upx 0;
				box-sizing: border-box;
				padding-left: 20upx;
				background: linear-gradient(to left, #ffffff, rgba(255, 255, 255, .2));

				image {
					width: 30upx;
					height: 28upx;
				}

				.active {
					text {
						color: #FFB400;
					}
				}
			}

		}
	}

	.banner {
		padding: 32upx;
		background-color: white;
		border-radius: 20upx 20upx 0 0;

	}

	.recommend {
		background-color: #ffffff;
		border-radius: 20upx;
		padding: 0 32upx;
		overflow: hidden;
		padding-top: 32upx;

		.title {
			width: 167upx;
			height: 36upx;
		}

		.store_box {
			padding: 25upx 0;

			>image {
				width: 152upx;
				height: 152upx !important;
				background-color: #ccc;
				border-radius: 12upx;
				overflow: hidden;
			}

			.store_box_inner {
				.name {
					font-size: 32upx;
					color: #000000;
					line-height: 34upx;
				}

				.scroll {
					.item {
						display: inline-block;
						padding: 11upx 19upx;
						background-color: #fff4f4;
						border-radius: 22upx;
						font-size: 22upx;
						color: #F14F40;
						margin-right: 40upx;
					}
				}

				.detail {
					font-size: 26upx;
					font-weight: 500;
					color: #000000;
					line-height: 43upx;
				}

			}
		}

	}

	.list {
		padding: 24upx 0;

		>image {
			width: 32upx;
			height: 32upx;
		}
	}

	.list.active {
		color: #41B26B;
	}

	.vote {
		position: relative;
		z-index: 5;
		width: 678upx;
		border-radius: 24upx;
		background-color: white;
		min-height: 100upx;
		margin-top: -120upx;
		padding: 40upx 0;

		.lists {
			text {
				font-size: 22upx;
				color: #AEADB3;

			}
		}

	}

	.vote_tit {
		background-color: #41b26b;
		border-radius: 24upx;
		padding: 28upx 39upx;
		width: 550upx;
		color: white;
		padding-bottom: 130upx;
	}

	.fw400 {
		font-weight: 400;
	}

	.page_title {
		margin-top: 30upx;
		font-size: 38upx;
		color: #000000;
		font-weight: bold;
	}

	.main {
		border-radius: 24upx;
		background-color: #ffffff;
		padding: 24upx;
		margin-top: 25upx;
	}
</style>
