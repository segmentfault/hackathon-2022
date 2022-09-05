<template>
	<view class="container">
		<view class="list" v-for="item in list" :key="item.id">
<!-- 			<image :src="item.head" class="head"></image>
			<view class="flex-1">
				<view class="flx">
					<view class="soure">{{item.soure}}</view>
					<view class="money">-¥{{item.money}}</view>
				</view>
				<view class="flx mt10">
					<view :class="[{'await':item.status == 1},{'success':item.status == 2},{'fail':item.status == 3},'status']">{{item.status | getStatus}}</view>
					<view class="balance">余额 ¥{{item.balance}}</view>
				</view>
				<view class="timer">{{item.timer}}</view>
			</view> -->
			<view class="left">
				<view class="soure">提现到微信零钱</view>
				<view class="timer">{{$utils.dayjs(item.createTime).format('YYYY/MM/DD HH:mm:ss')}}</view>
			</view>
			<view class="right money">
				- {{item.money}}
			</view>
		</view>
		<view class="empty-tip">
		  <image
		    class="empty-icon"
		    src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/empty-icon.png"
		  ></image>
		  已经到底了呢！
		</view>
	</view>
</template>

<script>
	import userService from '../../service/user';
	export default{
		data(){
			return {
				list:[
					// {
					// 	id:1,
					// 	head:"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKw4PibKlVlR04pAuamgpp3TldvdTbmt75kIHwn0oQNjE6d8jE2YoY2dVnr88Eluhfgc4tbmTPcAmg/132",
					// 	soure:"提现到微信零钱",
					// 	status:1,
					// 	timer:'2021-12-12 12:00:00',
					// 	money:200,
					// 	balance:0
					// },
				],
				query:{
					pageSize:10,
					pageNum:1
				}
			}
		},
		onReachBottom(){
			if (this.list.length < this.query.pageNum * this.query.pageSize) {
			  this.query.pageNum++
			  this.init()
			}
		},
		onPullDownRefresh(){
			this.pageNum = 1
			this.list = []
			this.init()
		},
		onLoad(){
			this.init()
		},
		methods:{
			init(){
				userService.depositRcord(this.query).then(res => {
					this.list = this.list.concat(res)
				})
			}
		},
		// filters:{
		// 	getStatus(val){
		// 		return val == 1 ? '提现中' : val == 2 ? '已到账' : '提现失败'
		// 	}
		// }
	}
</script>

<style lang="scss" scoped>
	.container{
		padding:40upx 30upx;
		.list{
			display: flex;
			padding:30upx 0;
			align-items: center;
			border-bottom: 1px solid #eee;
			.left,.right{
				flex:1;
			}
			.money{
				color:#F06F6F;
				font-size:32upx;
				text-align: right;
			}
			.timer{
				font-size:24upx;
				color:#666;
				margin-top: 20upx;
			}
			
			// .head{
			// 	width:100upx;
			// 	height:100upx;
			// 	border-radius: 50%;
			// }
			// .flex-1{
			// 	flex:1;
			// 	padding-left:20upx;
			// 	.flx{
			// 		justify-content: space-between;
			// 		display: flex;
			// 		.soure{
			// 			font-size: 28upx;
			// 			font-weight: 600;
			// 			color:#000;
			// 		}
			// 		.money{
			// 			color:#F06F6F;
			// 			font-size:24upx;
			// 		}
			// 		.status{
			// 			font-size:20upx;
			// 			border:1px solid #979797;
			// 			padding:1px 4px;
			// 			border-radius: 20px;
			// 		}
			// 		.success{
			// 			color:#25A41F;
			// 			border:1px solid #25A41F;
			// 		}
			// 		.fail{
			// 			color:#D43926;
			// 			border:1px solid #D43926;
			// 		}
			// 		.await{
			// 			color:#666;
			// 			border:1px solid #666;
			// 		}
			// 		.balance{
			// 			color:#666;
			// 			font-size:20upx;
			// 		}
			// 	}
			// 	.mt10{
			// 		margin:10upx 0;
			// 	}
			// 	.timer{
			// 		font-size:24upx;
			// 		color:#666;
			// 	}
			// }
		}
	}
</style>
