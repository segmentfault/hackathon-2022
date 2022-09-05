<template>
  <!-- 活动规则-->
  <view
    class="stateRule"
    v-if="value"
    @click="this.$emit('input', !value)"
    catchtouchmove="preventMove"
  >
    <view class="card">
      <view class="title">
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/317c2805-2575-4cc2-bbce-7b036959ded1.png"
        ></image>
      </view>
      <view class="centent">
         <view v-html="rule"></view>
      </view>
    </view>
    <view class="img">
      <image
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/f1fea3a6-1b89-4bd2-92e8-65c2dea837fe.png"
      ></image>
    </view>
  </view>
</template>
<script>
export default {
  props: {
    value: {
      type: Boolean,
    },
    rule: { 
      type:[String]
    }
  },
};
</script>
<style lang="scss" scoped>
.stateRule {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999999;
  padding: 56rpx 160rpx;
  .card {
    width: 85%;
    background-color: #fff;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    padding: 52rpx 78rpx;
    border-radius: 16rpx;
    .title {
      width: 268rpx;
      height: 48rpx;
      margin: 0 auto 32rpx;
      image {
        width: 100%;
        height: 100%;
      }
    }
    .centent {
      view {
        margin-bottom: 10rpx;
        font-size: 28rpx;
        letter-spacing: 2rpx;
      }
    }
  }
  .img {
    position: absolute;
    bottom: 120rpx;
    left: 50%;
    width: 100rpx;
    height: 100rpx;
    transform: translateX(-50%);
    image {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
