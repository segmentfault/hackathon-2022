<template>
	<view class="conpon-list" v-if="list.length">
		<view :class="[{active:selectIdArr.includes(item.id),mt30:!ids.includes(item.id.toString())},'flex','flex-middle','flex-center']" v-for="(item,index) in list" :key="item.id">
			<view class="conpon-item p30 rel" v-if="!ids.includes(item.id.toString())">
				<view class=" flex flex-between">
					<view class="left color-tip flex flex-middle flex-center text-center">
						<view class="">
							<view class="fs24">
								<text class="fs36 fw600">{{item.discount == 0 ? '免单' : item.discount}}</text>
								<text v-if="item.discount != 0">{{item.couponType == 1 ? '折' : '元'}}</text>
							</view>
							<view class="fs24 color-tip">无门槛</view>
						</view>
					</view>
					<view class="right flex-1 pl30">
						<view class="flex flex-between">
							<view class="flex-1">
								<view class="fs24 pr40">
									<view class="fs36">{{item.couponName}}</view>
								</view>
								<view class="flex flex-between flex-middle">
									<view class="fs22 color-999 mt10">有效期:{{item.applicableEndTime ? $utils.dayjs(item.applicableEndTime).format('YYYY-MM-DD') : item.numberEffective + '天'}}</view>
									<view class="color-tip" v-if="giftType == 1">x{{item.quantity}}张</view>
									<view class="look" @tap="look" v-if="giftType == 2">查看</view>
								</view>
							</view>
						</view>
						<view class="flex flex-between flex-middle mt10">
							<view class="fs24 color-tip" @tap="toggleIndex(index)">
								<text @tap="lookRule(item.couponRule)">查看使用规则 ></text>
							</view>
						</view>
					</view>
				</view>
			</view>
			
			<view class="shadow" v-if="showDesc">
				<view class="shadow-body p30">
					<view class="text-center pb30"><image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/4824c3d4-3ce0-4e6e-8dd6-d2f43682779f.png" class="help-icon"></image></view>
					<view class="fs36 fw600 text-center">使用规则</view>
					<view class="fs28 color-999 mt30">
						{{desc}}
					</view>
					<view class="fs38 fw600 text-center close" @tap="showDesc = false">我知道了</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	
	export default{
		props:{
			list:{
				type:Array,
				default:[]
			},
			giftType:{
				type:String || Number,
				default:'1'
			},
			isSelect:{
				type:Boolean,
				default:false
			},
			ids:{
				type:Array,
				default:[]
			},
			isDeleteLocality:{
				type:Boolean,
				default:false
			}
		},
		data(){
			return {
				showIndex:-1,
				desc:'',
				showDesc:false,
				selectIdArr:[],
				selectArr:[]
			}
		},
		methods:{
			lookRule(rule){
				this.desc = rule
				this.showDesc = true
			},
			toggleIndex(i){
				this.showIndex = this.showIndex != i ? i : -1
			},
			look(){
				uni.navigateTo({
					url:"/pagesA/user/coupon"
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.conpon-list{
		.active{
			box-shadow: 0 0 6px 0 #FF9900;
		}
		.conpon-item{
			background: url('https://image-1306191496.cos.ap-nanjing.myqcloud.com/d661096f-fa72-41e7-b111-6b33cc4c87c5.png') no-repeat;
			background-size: 100%;
			width:685upx;
			height:180upx;
			.close{
				top:0;
				right:0;
				width:48upx;
				height:48upx;
			}
			.left{
				width:160upx;
			}
			.right{
				.edit{
					padding:4upx 20upx;
					border-radius: 30upx;
					border:1px solid #f43530;
				}
			}
			.look{
				width:100upx;
				height:50upx;
				border:1px solid #f43530;
				color:#f43530;
				text-align: center;
				line-height: 50upx;
				border-radius: 30upx;
			}
		}
	}
</style>
