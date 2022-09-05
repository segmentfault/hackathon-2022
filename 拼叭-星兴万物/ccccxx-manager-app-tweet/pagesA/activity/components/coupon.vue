<template>
  <view class="coupon" :class="{ wider }">
    <image
      src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/coupon.png"
    ></image>
    <view v-if="boostCoupon || halfCoupon" class="ninety-content flex-between">
      <view class="left flex-box">
        <view class="number">{{ halfCoupon ? 5 : boostCoupon.discount }}</view
        >折
      </view>
      <view class="right flex-c-center">
        <view class="right-title"
          >{{halfCoupon ? `商家5折券`:boostCoupon.name}}</view
        >
        <view v-if="!halfCoupon" class="expire"
          >仅限
          {{
            new $utils.dayjs(boostCoupon.invaliddate).format("YYYY-MM-DD")
          }}当天</view
        >
        <view v-else class="expire">仅限 2021-08-29当天</view>
      </view>
      <!-- <view class="state">未激活</view> -->
    </view>
    <view v-else class="half-content">
      {{
        waitApply ? "TA报名后即可获得" : self ? "您获得了1 张" : "TA获得了1 张"
      }}
      <text style="color: #ff4f3a">半价票</text>！
    </view>
  </view>
</template>

<script>
export default {
  props: {
    halfCoupon: Boolean,
    waitApply: Boolean,
    boostCoupon: Object,
    self: Boolean,
  },
};
</script>

<style lang="scss" scoped>
.coupon {
  position: relative;
  width: 564upx;
  height: 180upx;
  display: flex;
  align-items: center;
  justify-content: center;

  &.wider {
    width: 564upx;
  }

  image {
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    width: 100%;
    height: 100%;
  }

  .half-content {
    font-size: 40upx;
    font-weight: 500;
    color: #333333;
    line-height: 56upx;
    letter-spacing: 2upx;
    z-index: 1;
  }

  .ninety-content {
    z-index: 1;
    position: relative;
    .left {
      width: 190upx;
      color: #ff4f3a;

      .number {
        font-size: 96upx;
        font-weight: 500;
        line-height: 134upx;
        letter-spacing: 6upx;
      }
    }

    .right {
      align-items: flex-start;

      .right-title {
        font-size: 32upx;
        font-weight: 500;
        color: #333333;
        line-height: 44upx;
        letter-spacing: 2upx;
      }

      .expire {
        margin-top: 4upx;
        font-size: 24upx;
        font-weight: 400;
        color: #666666;
        line-height: 40upx;
        letter-spacing: 2upx;
      }
    }
    .state {
      position: absolute;
      top: 0;
      right: 0;
      width: 52px;
      height: 18px;
      background: #a6a6a6;
      border-radius: 9px;
      color: #ffffff;
      font-size: 11px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
}
</style>
