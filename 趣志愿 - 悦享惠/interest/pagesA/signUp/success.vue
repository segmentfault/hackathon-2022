<template>
  <view>
    <u-navbar
      :is-back="true"
      title="组织详情"
      back-icon-color="white"
      title-color="white"
      :background="background"
      :border-bottom="false"
    >
    </u-navbar>
    <view class="box">
      <view class="top">
        <u-image src="/static/img/right.png" width="176" height="176"></u-image>
        <view class="msg">已成功加入该组织</view>
        <button class="btn" @click="goHome">返回首页</button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      background: {
        backgroundColor: "#4575F6 ",
      },
    };
  },
  methods: {
    goHome() {
     	uni.switchTab({
					url:"/pages/tabbar/tabbar-4/tabbar-4"
			})
    },
  },
};
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
  .msg {
    margin-top: 32rpx;
    font-size: 36rpx;
    font-family: PingFang SC-Semibold, PingFang SC;
    font-weight: 600;
    color: #4575f6;
  }
  .btn {
    margin-top: 198rpx;
    width: 560rpx;
    height: 96rpx;
    border-radius: 56rpx;
    color: white;
    background: #4575f6;
  }
  button::after {
    border: none;
  }
  button::after {
    border: none;
  }
  .button-hover {
    background: #4575f6;
  }
}
</style>
