<template>
	<view>
		<u-navbar :is-back="false" title="发布活动" title-color="white" :background="background" :border-bottom="false">
			<view class="slot-wrap u-p-l-32">
				<u-icon @click="goOrhomr()" name="arrow-left" color="white" size="48"></u-icon>
			</view>
		</u-navbar>
		<view class="box">
			<view class="top">
				<u-image src="/static/img/release.png" width="176" height="176"></u-image>
				<view class="msg">活动发布成功</view>
				<button class="btn" @click="goActive()">活动详情</button>
				<view class="return" @click="goOrhomr">返回组织首页</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				background: {
					backgroundColor: '#4575F6 ',
				},
				acid:''
			};
		},
		onLoad(option){
		  console.log("option",option.acid)
		  this.acid=option.acid;
		},
		methods:{
			goOrhomr(){
				uni.redirectTo({
					url:'/pagesB/or-home/or-home'
				})
			},
			goActive(){
				uni.redirectTo({
					url:`/pagesB/detail/ac-detail?id=1&acid=${this.acid}`
				})
			}
		}
	}
</script>

<style scoped lang="scss">
	.box {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.top {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		margin-top: 198rpx;
			.msg{
				margin-top: 32rpx;
				font-size: 36rpx;
				font-family: PingFang SC-Semibold, PingFang SC;
				font-weight: 600;
				color: #2AC45E;
			}
			.btn{
			color:white;
				margin-top: 198rpx;
				width: 560rpx;
				height: 96rpx;
				border-radius: 56rpx;
				 background: #4575F6;
			}
			button::after {
			   border: none;
			}
			button::after{ 
				border: none; 
			}
			.button-hover{
			    background: #4575F6;; 
			}
			.return{
				margin-top: 48rpx;
				font-weight: 600;
				color: #4575F6;
			}
	}
</style>
