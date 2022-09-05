<template>
	<view>
		<comHead changeColor class="header" :custom="isHome" autoPadding :isHome="isHome">
			<view @click="goHome" v-if="isHome">
				<image
					src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/35cff1f6-d5ab-4780-81d2-f94ebcc1d065.png"
					class="logo">
				</image>
			</view>
		</comHead>
		<view class="banner">
			<view class="right" @click="openRule">
				<image
					src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/0c4a754c-17f1-4147-a084-e91dc7a865e3.png"
					mode=""></image>
			</view>
			<view class="uploadBtn" @click="uploadFile">
				<image
					src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/33c50227-d7b0-4d08-851c-518b057ffd79.png"
					mode=""></image>
			</view>
		</view>

		<view class="main">


			<view class="list">
				<view class="item" v-for="(item, index) in data">
					<view class="picture" @click="toUserInfo(item)">
						<image :src="item.pictures[0].url" mode=""></image>
						<view class="tag" :style="{
                backgroundImage:
                  'url(' +
                  (item.rank == 1
                    ? top1
                    : item.rank == 2
                    ? top2
                    : item.rank == 3
                    ? top3
                    : top) +
                  ')',
              }">
							{{ item.rank }}
						</view>
						<view class="isMe" v-if="item.userId == userInfo.userId">
							<image
								src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/fa281827-c5a8-4388-8bc7-e631d77f9ea5.png"
								mode=""></image>
						</view>
					</view>
					<view class="info">
						<view class="name">
							{{ item.nickName }}
						</view>
						<view class="label"> 人气值 </view>
						<view class="fire">
							<image
								src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/ebbc4a1b-0a14-4051-9226-3c0b76ea7574.png"
								mode=""></image>
							<text class="value">{{ item.popularity }}</text>
						</view>
						<view v-if="userInfo.userId != item.userId">
							<view class="notClick" v-if="!item.boost" @click="getuserinfo();boost(item, index)">
								<image
									src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/25c87dc4-1ef1-4ebe-8ba3-8edc4cb2cde3.png"
									mode=""></image>
							</view>
							<view class="click" v-else>
								<image
									src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/34a28e9c-3ad5-41e4-969c-6a4575d8be59.png"
									mode=""></image>
								<view class="tag">
									<image
										src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/94b2e426-d5f0-46fd-ace0-92fdba246bfb.png"
										mode=""></image>
								</view>
							</view>

							<view class="text">
								{{ item.boost ? "已投票" : "给TA投票" }}
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="empty-tip" v-if="status === 'nomore'">
				<image class="empty-icon"
					src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/empty-icon.png"></image>
				已经到底了呢！
			</view>
			<view v-else style="margin-bottom: 200upx; text-align: center; color: #000">
				- 正在加载 -
			</view>

			<!-- <view class="prize-top-r" @click="openRule">活动规则</view> -->

			<boost-popup ref="userCard">
				<view class="list">
					<view class="item">
						<view class="picture">
							<image :src="infoH.pictures[0].url" mode=""></image>
						</view>
						<view class="info">
							<view class="name">
								{{ infoH.nickName }}
							</view>
							<view class="label"> 人气值 </view>
							<view class="fire">
								<image
									src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/ebbc4a1b-0a14-4051-9226-3c0b76ea7574.png"
									mode=""></image>
								<text class="value">{{ infoH.popularity }}</text>
							</view>
							<view v-if="userId">
								<view class="notClick" v-if="!infoH.boost">
									<image @click="boostMe()"
										src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/25c87dc4-1ef1-4ebe-8ba3-8edc4cb2cde3.png"
										mode=""></image>
								</view>
								<view class="click" v-else>
									<image
										src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/34a28e9c-3ad5-41e4-969c-6a4575d8be59.png"
										mode=""></image>
									<view class="tag">
										<image
											src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/94b2e426-d5f0-46fd-ace0-92fdba246bfb.png"
											mode=""></image>
									</view>
								</view>
								<view class="text">
									{{ infoH.boost ? "已投票" : "给他投票" }}
								</view>
							</view>
						</view>
					</view>
				</view>
			</boost-popup>
		</view>
		<view class="nextBtn" @click="next" v-if="info.boostNum">
			<image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/020f73f7-60d4-4b33-8092-02130c0c0a2d.png"
				mode=""></image>
			<text>已有{{ info.boostNum }}人帮您助力 ></text>
		</view>
		<view class="share" v-if="info.pictures.length">
			<button type="default" @click="isShareDialog">分享到朋友圈</button>
			<button type="default" open-type="share" @click="$eventRecord(173)">
				分享到微信好友
			</button>
		</view>
		<share-two v-if="shareDialog" :info='info' :codeUrl="codeUrl" :bgImgUrl="bgImgUrl"
			@cancel="shareDialog = false">
			<view class="list">
				<view class="item">
					<view class="picture">
						<image :src="info.pictures[0].url" mode=""></image>
					</view>
					<view class="info">
						<view class="name">
							{{ info.nickName }}
						</view>
						<view class="label"> 人气值 </view>
						<view class="fire">
							<image
								src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/ebbc4a1b-0a14-4051-9226-3c0b76ea7574.png"
								mode=""></image>
							<text class="value">{{ info.popularity }}</text>
						</view>
						<view>
							<view class="notClick">
								<image
									src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/25c87dc4-1ef1-4ebe-8ba3-8edc4cb2cde3.png"
									mode=""></image>
							</view>
							<view class="text">
								给TA投票
							</view>
						</view>
					</view>
				</view>
			</view>
		</share-two>
	</view>
</template>

<script>
	import comHead from "../../components/com-head.vue";
	import boostPopup from "./component/boost-popup.vue";
	import shareTwo from "./component/Share-two.vue";
	import api from "../../service/game.js";
	import user from "../../service/user.js";
	import basic from "../../service/basic";
	import moment from "moment";
	import {
		cosUploadFiles
	} from "../cos/utils";
	export default {
		components: {
			comHead,
			boostPopup,
			shareTwo,
		},
		data() {
			return {
				query: {
					pageNum: 1,
					pageSize: 10,
				},
				status: "more",
				data: [], //列表
				info: {}, //排行榜个人信息
				infoH: {},
				pictures: [], //图片上传列表
				userInfo: {}, //个人信息
				userId: "",
				top1: "https://image-1306191496.cos.ap-nanjing.myqcloud.com/c443a72d-01e6-4001-a1be-1132543dc4b7.png",
				top2: "https://image-1306191496.cos.ap-nanjing.myqcloud.com/06a589bb-dc42-4897-8b5c-473511c2dd61.png",
				top3: "https://image-1306191496.cos.ap-nanjing.myqcloud.com/73013f55-4cbb-4d2a-9b77-67eb85fe12f4.png",
				top: "https://image-1306191496.cos.ap-nanjing.myqcloud.com/034ee50d-6844-4a49-b45a-6d916365bbd0.png",
				shareDialog: false,
				codeUrl: "", //分享朋友圈二维码
				bgImgUrl: "https://image-1306191496.cos.ap-nanjing.myqcloud.com/75e13ddc-b34c-41b8-a85c-00c4cc05e884.png", //分享朋友圈背景图
				form: {
					birth: "",
					birthDate: "",
					constellation: "",
					head: "",
					intro: "",
					nickName: "",
					pictures: [],
					rate: '',
					sex: 0,
					flag: false
				},
				isHome:false
			};
		},
		onPullDownRefresh() {
			this.query.pageNum = 1;
			this.data = [];
			this.status = "more";
			this.getList();
			setTimeout(() => {
				this.getPictureDetail();
			}, 500)
			uni.stopPullDownRefresh();
		},
		onReachBottom() {
			this.query.pageNum++;
			this.getList();

		},
		onLoad(o) {
			this.$eventRecord(177)
			if(o.isHome) this.isHome = o.isHome
			this.getuserinfo()
			this.userId = o.userId;
			this.getList();
			this.getUserInfo();
			if (this.userId) {
				this.bindRelation(this.userId)
				this.getPictureUserDetail(this.userId);
			}
			setTimeout(() => {
				this.getPictureDetail();
			}, 500)
		},
		onShareAppMessage(res) {
			return {
				title: "关爱尾款人，1-5名送399元铁粉潮玩卡！帮我投个票吧！",
				path: `/pagesB/rankingList/index?userId=${this.userInfo.userId}&isHome=1`,
				imageUrl: "https://image-1306191496.cos.ap-nanjing.myqcloud.com/dcc907bd-4cf6-4598-853e-e8a070f6fcf6.png",
			};
		},
		methods: {
			async getuserinfo() {
				let res = await this.$authorization()
				this.flag = res
				console.log(this.flag)
			},
			//返回
			goHome() {
				uni.reLaunch({
					url: "/pages/index/index",
				});
			},
			//前往助力列表页
			next() {
				uni.navigateTo({
					url: "boostList?userId=" + this.info.userId,
				});
			},
			//拉新
			async bindRelation(id) {
				let res = await api.bindRelation({
					parentId: id,
					parentType: 1
				})
			},
			//助力
			async boost(item, index) {
				if (!this.flag) return
				if (item.boost) return;
				let form = {
					activityUserId: item.userId,
					boostSource: 1,
				};
				let res = await api.pictureBoost(form);
				if (res) {
					uni.showToast({
						icon: 'none',
						title: '助力成功'
					})
					this.data[index].boost = true;
					this.data[index].popularity += res;
				}
			},
			//给自己助力or分享助力
			async boostMe() {
				this.$eventRecord(171);
				if (!this.flag) return
				if (this.infoH.boost) return;
				let form = {
					activityUserId: this.infoH.userId,
					boostSource: 1,
				};
				let res = await api.pictureBoost(form);
				if (res) {
					uni.showToast({
						icon: 'none',
						title: '助力成功'
					})
					this.infoH.boost = true;
					this.infoH.popularity += res;
				}
			},
			//获取本用户助力信息
			async getPictureDetail() {
				let res = await api.getPictureDetail();
				if (res) {
					this.info = res;
					this.info.pictures[0].url = this.$pictureUrl(res.pictures[0].url, 170, 170);
					this.data.splice(1, 0, this.info);
				}
			},
			//获取其他用户助力信息（分享出去的链接点进来获取分享人的信息）
			async getPictureUserDetail(id) {
				let res = await api.getPictureUserDetail({
					pictureUserId: id,
				});
				if (res) {
					this.infoH = res;
					if (this.userInfo.userId == id) return
					this.infoH.pictures[0].url = this.$pictureUrl(res.pictures[0].url, 170, 170);
					this.$refs.userCard.change();
				}
			},
			//获取用户信息
			async getUserInfo() {
				let res = await user.queryUserInfo();
				this.userInfo = res;
			},
			//获取照片墙列表
			async getList() {
				if (this.status == "nomore") return;
				let res = await api.getPictureList(this.query);
				if (res.list.length > 0) {
					res.list.forEach((e) => {
						e.pictures.forEach((r) => {
							r.url = this.$pictureUrl(r.url, 170, 170);
						});
						this.data.push(e);
					});


					this.status = "more";
				} else {
					this.status = "nomore";
				}
			},
			// 上传照片后保存用户信息
			async updateInfo(file) {
				if (!this.flag) return
				let res = await user.updatePicture({
					pictures: file
				});
				this.data = [];
				this.query.pageNum = 1;
				this.getList();
				setTimeout(() => {
					this.getPictureDetail();
				}, 500)
			},
			//打开活动规则弹出框
			openRule() {
				// this.$refs["rule-popup"].change();
				uni.navigateTo({
					url: './rule'
				})
			},
			//
			toUserInfo(item) {
				uni.navigateTo({
					url: "./profile?boostId=" +
						item.userId +
						"&boost=" +
						item.boost +
						"&id=" +
						item.userId,
				});
			},
			isShareDialog() {
				this.$eventRecord(172);
				this.shareDialog = true;
				let parmas = {
					scene: `userId${this.info.userId}`,
					isHyaline: false,
					width: 300,
					autoColor: false,
					lineColor: {
						r: "0",
						g: "0",
						b: "0",
					},
					page: "pagesB/rankingList/index",
				};
				basic.getMiniProgramQrcodeApi(parmas).then((res) => {
					this.codeUrl = "data:image/png;base64," + res;
				});
			},

			//上传图片
			uploadFile() {
				this.$eventRecord(170);
				uni.chooseImage({
					count: 6, //默认9
					sizeType: ["original", "compressed"], //可以指定是原图还是压缩图，默认二者都有 //从相册选择
					success: (res) => {
						let userinfo = uni.getStorageSync("userInfo");
						if (!userinfo) return;
						let _Date = new Date();
						let key = `${_Date.getFullYear()}/${_Date.getMonth() + 1}/${
            userinfo.ticket + Date.now()
          }`;
						try {
							cosUploadFiles(res.tempFiles, key, (err, data) => {
								if (!err) {
									// 上传成功，回到页面
									let pictures = [];
									data.files.forEach((file) => {
										pictures.push(`https://${file.data.Location}`);
									});
									this.pictures.forEach((e) => {
										if (!pictures.includes(e)) {
											pictures.unshift(e);
										}
									});
									this.updateInfo([...pictures]);
								}
							});
						} catch (error) {}
					},
				});
			},
		},
	};
</script>

<style lang="scss">
	page {
		background-color: #fff;
	}

	.header {
		position: absolute;
		align-items: center;
		display: flex;
		font-size: 28upx;
		font-weight: 600;
		color: #fff;

		.logo {
			position: relative;
			left: 50upx;
			width: 50upx;
			height: 50upx;
		}
	}

	.banner {
		height: 400upx;
		background-image: url("https://image-1306191496.cos.ap-nanjing.myqcloud.com/6ee88f75-cd0c-4522-ae25-2ea5a02b98a6.png");
		background-size: 100% 100%;
		display: flex;
		justify-content: flex-end;
		align-items: center;
		position: relative;

		.right {
			margin-top: 130upx;
			margin-right: 40upx;

			image {
				width: 120upx;
				height: 60upx;
			}
		}

		.uploadBtn {
			position: absolute;
			bottom: -70upx;
			left: 50upx;

			image {
				height: 120upx;
				width: 400upx;
			}
		}

	}

	.main {
		width: 94%;
		margin: auto;
		padding-bottom: 80upx;
		margin-top: 70upx;


	}

	.list {
		display: flex;
		flex-wrap: wrap;
		margin-top: 40upx;

		.item {
			width: 100%;
			background-color: #f5f5f5;
			border-radius: 12upx;
			margin-bottom: 20upx;
			padding: 20upx;
			display: flex;
			justify-content: space-between;

			.picture {
				width: 400upx;
				height: 400upx;
				position: relative;

				.tag {
					position: absolute;
					top: 10upx;
					left: 0;
					width: 100upx;
					height: 100upx;
					background-repeat: no-repeat;
					background-size: 100%;
					line-height: 110upx;
					text-align: center;
					font-weight: bold;
					color: #666;
				}

				.isMe {
					position: absolute;
					bottom: 10upx;
					right: 0;

					image {
						width: 150upx;
						height: 50upx;
					}
				}

				image {
					width: 100%;
					height: 100%;
					object-fit: cover;
					border-radius: 32upx;
				}
			}

			.info {
				width: 35%;

				.name {
					font-size: 35upx;
					font-weight: 600;
					overflow: hidden;
					text-overflow: ellipsis;
					white-space: nowrap;
					width: 260upx;
				}

				.label {
					margin-top: 20upx;
					color: #ff463a;
					font-weight: 600;
				}

				.fire {
					margin-top: 10upx;
					color: #ff463a;
					font-size: 40upx;

					image {
						vertical-align: middle;
						margin-right: 25upx;
						height: 40upx;
						width: 30upx;
					}
				}

				.text {
					color: #666666;
					margin-top: 20upx;
					font-size: 35upx;
					margin-left: 30upx;
					// text-align: center;
				}

				.notClick,
				.click {
					margin-top: 20upx;
					margin-left: 30upx;
					position: relative;

					image {
						height: 100upx;
						width: 100upx;
					}

					.tag {
						position: absolute;
						right: 60upx;
						top: -30upx;
						width: 50upx;
						height: 50upx;

						image {
							width: 70upx;
							height: 50upx;
						}
					}
				}
			}
		}
	}

	.prize-top-r {
		position: fixed;
		right: 0;
		top: 240upx;
		background-color: #5f52d2;
		color: #fff;
		height: 60upx;
		padding: 0 20upx;
		line-height: 60upx;
		border-radius: 60upx 0 0 60upx;
	}

	.rule-t {
		padding: 29upx 0;
		width: 100%;
		text-align: center;
		font-size: 40upx;
		font-family: PingFangSC-Semibold, PingFang SC;
		font-weight: 600;
		line-height: 52upx;
	}

	.nextBtn {
		position: fixed;
		bottom: 150upx;
		height: 80upx;
		width: 100%;
		justify-content: center;
		color: #fff;
		display: flex;
		align-items: center;
		background-color: #ff5959;

		image {
			height: 40upx;
			width: 30upx;
			margin-right: 20upx;
		}
	}

	.share {
		height: 150upx;
		background: #fff;
		position: fixed;
		bottom: 0;
		width: 100%;
		display: flex;
		justify-content: space-around;
		padding: 25upx 0;

		button {
			height: 100upx;
			width: 40%;
			color: #fff;
			background-color: #000;
			border-radius: 100upx;
			font-size: 35upx;
			line-height: 100upx;
		}
	}

	.empty-tip {
		margin-top: 39upx;
		margin-bottom: 200upx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 26upx;
		font-family: PingFangSC-Light, PingFang SC;
		font-weight: 300;
		color: #000;

		.empty-icon {
			height: 64upx;
			width: 64upx;
			margin-right: 18upx;
		}
	}

	.rule-v {
		.title {
			font-weight: bold;
		}

		.content {
			margin-bottom: 40upx;
		}
	}
</style>
