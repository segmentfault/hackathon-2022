<template>
	<view>
		<view class="header" :style="{
        height: navHeight + 'px',
        'padding-top': statusBarHeight + 'px',
      }">
			<view class="back">
				<u-icon class="back-icon" @click="back" name="arrow-left" size="36"></u-icon>
			</view>
			<view class="title">
				奖励说明
			</view>
		</view>
		<view class="rules">
			1、1-5名送铁粉潮玩卡 <br/>
			2、5-30名送剧本杀/密室/酒吧优惠券
		</view>
		<view class="card">
			<image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/191ac292-d345-4fa4-a142-94d7879c193a.png"
				mode="widthFix"></image>
		</view>
		<view class="rule">
			<image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/a6cb715d-c6fb-4326-b1e6-2ce7f5b4aa1f.png"
				mode="widthFix"></image>
		</view>
	</view>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
				statusBarHeight: "",
				navHeight: "",
			};
		},
		onLoad() {
			//获取手机系统的信息（在这主要是获取状态栏和胶囊的高度）
			let {
				statusBarHeight,
				system
			} = uni.getSystemInfoSync();
			this.statusBarHeight = statusBarHeight;
			this.navHeight = statusBarHeight + (system.indexOf("iOS") > -1 ? 44 : 48);
		},
		methods: {
			back() {
				uni.navigateBack(1);
			}
		}
	};
</script>

<style lang="scss">
	.header {
		display: flex;
		align-items: center;
		position: relative;

		.back {
			margin-left: 50upx;
			z-index: 1;
		}

		.title {
			position: absolute;
			width: 100%;
			display: flex;
			justify-content: center;

			image {
				width: 300upx;
				height: 40upx;
			}
		}
	}

	.card {
		width: 90%;
		margin: auto;
		
		image {
			width: 100%;
		}
	}
	.rules{
		width: 90%;
		margin: auto;
		background-color: #26D2CF;
		font-family: PingFangSC-Semibold, PingFang SC;
		font-weight: 600;
		color: #000000;
		line-height: 50upx;
		padding: 30upx;
		margin-bottom: 40upx;
	}
	.rule {
		width: 96%;
		margin: auto;

		image {
			width: 100%;
		}

	}
</style>
