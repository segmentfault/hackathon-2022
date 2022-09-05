<template>
	<view class="share-two" catchtouchmove="preventMove">
		<view class="card">
			<xWxmlToCanvas ref="xWxmlToCanvas" :hide="true" :width="321" :height="622" :xStyle="styleTemplate()"
				:xWxml="htmlTemplate()">
			</xWxmlToCanvas>
			<image :src="bgImgUrl" mode="widthFix"></image>
			<view class="card">
				<slot></slot>
			</view>
			<view class="info">
				<image :src="codeUrl"></image>
			</view>
			<view class="fixed">
				<button @click="cancel">取消</button>
				<button @click="save">保存相册</button>
			</view>
		</view>
	</view>
</template>

<script>
	import xWxmlToCanvas from './x-wxml-to-canvas/x-wxml-to-canvas';
	export default {
		components: {
			xWxmlToCanvas
		},
		props: {
			codeUrl: {
				type: String,
				default: () => {
					return "";
				},
			},
			bgImgUrl: {
				type: String,
				default: () => {
					return "";
				},
			},
			info: {
				type: Object,
				default: () => {
					return {};
				}
			},
		},
		mounted() {

		},
		data() {
			return {
				image: true,
			};
		},
		methods: {
			htmlTemplate() {
				return `
				<view class="container" >
				  <view class="bg">
				      <image class="img" src="` + this.bgImgUrl + `"></image>
				  </view>
				  <view class="card">
					<image class="picture" src="` + this.info.pictures[0].url + `"></image>
					<text class="name">
											 ` + this.info.nickName + `
					</text>
					<text class="label">
								人气值
					</text>
					<view class="fire">
					 					<image
										class="fireImg"
											src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/ebbc4a1b-0a14-4051-9226-3c0b76ea7574.png"
											mode=""></image>
									
					 				</view>
						<text class="value">
										<text>` + this.info.popularity + `</text>
						</text>
						<view class="notClick">
						 						<image
																class="notClickImg"
						 							src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/25c87dc4-1ef1-4ebe-8ba3-8edc4cb2cde3.png"
						 							mode=""></image>
						 					</view>
											<text class="boost">
														给TA投票
											</text>
				  </view>
				  <view class="code">
				  	<image class="codeImg" src="` + this.codeUrl + `"></image>
				  </view>
					 
				</view>
				
				`
			},
			styleTemplate() {
				return {
					container: {
						width: 321,
						height: 622,
						backgroundColor: '#ccc',
						'position': 'relative',
					},
					img: {
						width: 321,
						height: 622,
					},
					card: {
						width: 300,
						borderRadius: 12,
						backgroundColor: '#f5f5f5',
						'position': 'absolute',
						margin: 10,
						top: 200,
						padding: 5,
					},
					picture: {
						height: 180,
						width: 180,
						borderRadius: 8,
					},
					name: {
						bottom: 170,
						left: 190,
					},
					label: {
						bottom: 145,
						left: 190,
						color: '#ff463a',
						fontSize: 12,
					},
					fire: {
						bottom: 105,
						left: 190,
						fontSize: 18,
						'position': 'absolute',
					},
					fireImg: {
						width: 15,
						height: 18,
					},
					value: {
						bottom: 118,
						left: 210,
						color: '#ff463a',
					},
					notClick: {
						bottom: 60,
						left: 210,
						'position': 'absolute',
					},
					notClickImg: {
						height: 35,
						width: 35
					},
					boost: {
						bottom: 30,
						left: 190,
						color: '#999',
						fontSize: 13
					},
					code: {
						width: 90,
						height: 90,
						margin: 115,
						bottom: 100,
						'position': 'absolute',

					},
					codeImg: {
						width: 90,
						height: 90,
						'borderRadius': 12,
					}

				}
			},
			//   关闭分享面板
			cancel() {
				this.$emit("cancel");
			},
			save() {
				this.$refs.xWxmlToCanvas.getCanvasImage().then(res => {
					this.$refs.xWxmlToCanvas.onSaveImageToPhotosAlbum(res)
				})
			},

		},
	};
</script>

<style lang='scss' scoped>
	.share-two {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		display: flex;
		justify-content: center;
		align-items: center;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 9999;

		.card {
			width: 100%;
			border-radius: 20upx;
			position: relative;

			image {
				width: 100%;
			}

			.card {
				width: 96%;
				position: absolute;
				margin: 0 2%;
				top: 400upx;
			}

			.info {
				left: 0;
				right: 0;
				position: absolute;
				bottom: 250upx;
				text-align: center;

				image {
					width: 160upx;
					height: 160upx;
					border-radius: 32upx;
				}
			}

			.fixed {
				display: flex;
				justify-content: space-around;
				margin-top: 44upx;

				button {
					margin: 0;
					width: 220upx;
					height: 76upx;
					line-height: 76upx;
					background: #000000;
					border-radius: 52upx;
					color: #fff;
				}
			}
		}
	}
</style>
