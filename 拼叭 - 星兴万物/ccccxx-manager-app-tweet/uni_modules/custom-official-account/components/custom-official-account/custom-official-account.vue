<template>
	<view class="weixin-official-account" v-if="show" :style="{'opacity':opacity}">
		<text class="txt">{{msg}}</text>
		<button type="default" class="btn btn-official">立即关注<official-account class="official-account" id="official_account"></official-account>
		</button>
		<icon class="iconfont icon-close" @click="close"></icon>
	</view>
</template>
<script>
	export default {
		props: {
			msg: {
				type: [String],
				default: '关注微信公众号'
			},
			isFollow: {
				type: [Boolean],
				default: false
			},
			max: {
				type: [String, Number],
				default: 3
			}
		},
		data() {
			return {
				show: true,
				opacity: 0
			}
		},
		mounted() {
			setTimeout(() => {
				this.init();
			}, 800)
		},
		methods: {
			init() {
				let that = this;
				const query = uni.createSelectorQuery().in(this);
				query.select('#official_account').boundingClientRect(data => {
					if (data.width && data.height) {
						that.check();
					} else {
						that.show = false;
					}
				}).exec();
			},
			async check() {
				try {
					const num = uni.getStorageSync('CLOSE_OFFICIAL_ACCOUNT');
					if (+num >= this.max) {
						this.show = false;
						return false;
					}
					this.show = !this.isFollow;
					if (this.show) this.opacity = 1;
				} catch (e) {
					this.show = false;
				}
			},
			close() {
				this.show = false;
				const num = uni.getStorageSync('CLOSE_OFFICIAL_ACCOUNT') ? +uni.getStorageSync('CLOSE_OFFICIAL_ACCOUNT') + 1 : 1;
				uni.setStorageSync('CLOSE_OFFICIAL_ACCOUNT', num);
			}
		}
	}
</script>
<style scoped lang="scss">
	.weixin-official-account {
		display: flex;
		align-items: center;
		justify-content: space-between;
		height: 68rpx;
		padding: 0 10rpx 0 30rpx;
		background: rgba(0, 0, 0, .6);
		border-radius: 6rpx;

		.txt {
			font-size: 24rpx;
			color: #fff;
		}

		.btn {
			position: relative;
			width: 56px;
			height: 20px;
			line-height: 20px;
			padding: 0;
			background: #2878FF;
			border-radius: 10px;
			overflow: hidden;
			text-align: center;
			font-size: 20rpx;
			color: #fff;

			.official-account {
				position: absolute;
				z-index: 20211209;
				right: -14px;
				top: -40px;
				opacity: 0;
			}
		}

		.icon-close {
			display: flex;
			align-items: center;
			justify-content: center;
			width: 50rpx;
			height: 68rpx;
			font-size: 22rpx;
			color: #fff;
		}
	}

	@font-face {
		font-family: 'iconfont';
		src: url(data:font/truetype;charset=utf-8;base64,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) format('truetype');
	}

	.iconfont {
		font-family: "iconfont" !important;
		font-size: 32rpx;
		font-style: normal;
		font-weight: normal;
		vertical-align: middle;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
	}

	.icon-close:before {
		content: "\e670";
	}
</style>
