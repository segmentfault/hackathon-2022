<template>
  <view style="display: flex; align-items: center; flex-direction: column">
    <template v-if="state === 6 || state === 3">
      <image
        src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/boost-success.png"
        style="width: 136upx; height: 136upx; margin-bottom: 14upx"
      ></image>
      <view
        style="
          font-size: 40upx;
          font-weight: 500;
          color: #333333;
          line-height: 56upx;
          letter-spacing: 2upx;
        "
      >
        {{ state === 3 || state === 6 ? "已完成预约！" : "报名成功!" }}</view
      >
    </template>
    <view class="coupon" v-if="state === 6 || state === 3" @click="toCoupon"
      >优惠券已发放，查看我的优惠券 <text></text
    ></view>

    <template v-if="state === 2 || state == 7 || state == 8">
      <count-down
        :timestamp="
          (activeDetail.applyEndTime - activeDetail.currentTime) / 1000
        "
      ></count-down>
      <view
        v-if="state == 2"
        style="
          font-size: 40upx;
          font-weight: 600;
          color: #a06426;
          line-height: 56upx;
          margin-top: 34upx;
        "
      >
        邀请好友助力享半价</view
      >
      <view class="tips" v-if="state == 7">
        <view class="tips-limit"
          >限量<text>{{ activeDetail.limitPeopleNum }}人</text
          ><text style="font-size: 12px; margin-left: 20px"
            >已抢{{ activeDetail.progressRate || 0 }}%</text
          ></view
        >
        <view class="tips-jd">
          <view
            class="tips-jdt"
            :style="{ width: `${activeDetail.progressRate || 0}%` }"
          >
            <image
              class="tips-jdt-fire"
              src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/fire.png"
            ></image>
          </view>
        </view>
        <view class="tips-msg">邀请好友助力，享<text>5折</text></view>
      </view>
      <view class="" v-else> </view>
      <!-- <view
				style="position:relative;width: 620upx;height: 204upx;background: #F5F2F1;border-radius: 16upx;margin-top: 20upx;padding: 20upx 20upx 20upx 28upx;font-size: 32upx;font-weight: 400;color: #A06426;line-height: 52upx;">
				<view>1.点击“邀请好友助力”分享给好友</view>
				<view>2.获得5个助力</view>
				<view>3.解锁半价优惠</view>
				<view
					style="position: absolute;bottom: 16upx;right: 16upx;font-size: 24upx;color:#fff;width: 150upx;height: 44upx;background: #A06426;border-radius: 22upx;"
					class="flex-box">
					限时3天
				</view>
			</view> -->
      <!-- <view style="font-size: 26upx;font-weight: 500;color: #333333;line-height: 36px;margin: 80upx 0 42upx;">
				······已助力的好友······</view> -->
    </template>
  </view>
</template>

<script>
export default {
  props: {
    state: Number,
    activeDetail: {
      type: Object,
      default: function () {
        return null;
      },
    },
  },
  methods: {
    toCoupon() {
      uni.navigateTo({
        url: `/pagesA/user/coupon`,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.tips {
  text-align: center;
  font-size: 24px;
  font-family: PingFangSC-Semibold, PingFang SC;
  font-weight: 600;
  color: #a06426;
  margin-top: 29px;
  margin-bottom: 26px;
  &-msg {
    margin-top: 35px;
    text {
      color: #ee5151;
    }
  }
  &-limit {
    font-size: 20px;
    font-weight: 500;
    margin-top: 7upx;
    color: #333333;
    display: flex;
    justify-content: center;
    align-items: center;
    text {
      color: #ee5151;
    }
  }
  &-jd {
    width: 152px;
    height: 11px;
    border-radius: 11px;
    background: #e8e8e8;
    margin: 4px auto;
  }
  &-jdt {
    width: 107px;
    height: 11px;
    border-radius: 11px;
    background: linear-gradient(270deg, #cd6cff 0%, #fe4a2a 100%);
    position: relative;
    &-fire {
      width: 32px;
      height: 32px;
      position: absolute;
      top: -12px;
      right: -16px;
    }
  }
}
.coupon {
  background-color: #f0e9d5;
  padding: 14upx 36upx;
  border-radius: 30upx;
  margin: 24upx 40upx;
  font-size: 24upx;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #000000;
  text {
    display: inline-block;
    width: 17upx;
    height: 17upx;
    border-top: 2upx solid #000;
    border-right: 2upx solid #000;
    transform: rotate(45deg);
    margin-left: 10upx;
  }
}
</style>
