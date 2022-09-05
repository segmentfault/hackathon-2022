<template>
	<view>
		<u-navbar :is-back="true" title="编辑资料" title-color="white" :background="background" :border-bottom="false">
			<view class="slot-wrap u-p-l-32">
			</view>
		</u-navbar>
		<view class="box">
			<view class="top">
				<u-image src="/static/img/save.png" width="176" height="176"></u-image>
				<view class="msg">保存成功</view>
				<button class="btn" @click="goHome()">返回首页</button>
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
				orid:''
			};
		},
		onLoad(option){
		  this.orid=option.orid;
		  
		},
		methods:{
			goHome(){
			 wx.setStorage({
              key: "Ologin",
              data: "true",
            });
				uni.redirectTo({
					url:`../../pagesB/or-home/or-home?id=${this.orid}`
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
	}
</style>
