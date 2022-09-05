<template>
	<view class="rules">
		<text v-text="rules"></text>
	</view>
  
<!--    <view class="title">1、数量：每一个省钱神券礼包，包含4张优惠券</view>
    <view class="title">
      2、可用券的店铺：可以在平台任意一家合作的剧本杀店铺使用,
      <view class="href" @click="handlerOpenList()">点这里查看店铺名单 ></view>
    </view>
    <view class="title">3、有效期：每一张优惠券的有效期是30天，从购买之日开始计算</view>
    <view class="title">
      4、用券
      <hr />（4.1）通过拼叭平台组局去指定店铺
      <hr />（4.2）到店消费
      <hr />（4.3）结账时点击拼叭小程序—我的—我的优惠券—点击要核销的优惠券
    </view>
    <view class="title">5、限制：每一局只能使用一张优惠券</view> -->
  
</template>
<script>
export default {
	props:{
		rules:{
			type:String
		}
	},
  methods: {
    handlerOpenList() {
      uni.navigateTo({ url: `/pagesA/vouchers/supportList?money=20&activityId=8` })
    },
  },
}
</script>
<style lang="scss" scoped>
.rules {
  padding: 32upx 40upx;
  .title {
    font-size: 30upx;
    color: #666666;
    margin-bottom: 20upx;
  }
  .href {
    color: #ea5768;
  }
}
</style>