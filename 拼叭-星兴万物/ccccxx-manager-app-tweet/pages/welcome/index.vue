<template>
  <view class="welcome">
    <view class="top">
      <view class="logo">
        <image
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/icon-pinbar2.png"
        ></image>
      </view>
    </view>
    <view class="footer">
      <view class="text"> 拼叭 | 组人组局用拼叭</view>
      <view class="start">
        <button @click="start">点击开始组局之旅</button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {};
  },
  methods: {
    start() {
      uni.redirectTo({
        url: "../index/index",
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.welcome {
  .top {
    height: 40vh;
    position: relative;

    .logo {
      width: 100upx;
      height: 100upx;
      background-color: #000;
      border-radius: 10px;
      position: absolute;
      bottom: 50upx;
      left: 50%;
      transform: translateX(-50%);

      image {
        width: 100upx;
        height: 100upx;
      }
    }
  }

  .footer {
    height: 60vh;
    text-align: center;

    .text {
      font-size: 32upx;
      font-weight: 700;
    }

    .start {
      position: absolute;
      top: 60%;
      left: 50%;
      transform: translate(-50%, 60%);

      button {
        width: 350upx;
        height: 80upx;
        background-color: #000;
        color: #fff;
        border-radius: 40upx;
        font-size: 32upx;
      }
    }
  }
}
</style>
