<template>
  <view class="share" catchtouchmove="preventMove" @click="cancel">
    <view class="card">
      <!-- 背景图 -->
      <image
        class="backG"
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/150aaf3c-cd98-4014-8988-9dbda71c3296.png"
      ></image>
      <!-- 用户信息 -->
      <view class="info">
        <image
          :src="myUserInfo.head + '?imageView2/1/w/200/h/200/q/80/webp'"
        ></image>
        <view class="name">{{ myUserInfo.nickName }}</view>
      </view>
      <!-- 二维码 -->
      <view class="QRCode">
        <image :src="codeUrl"></image>
      </view>
      <!-- 关闭按钮 -->
      <view class="close">
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/c50f52b9-28ad-44be-8631-7f7090970700.png"
        ></image>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
      default: () => {
        return {};
      },
    },
    codeUrl: {
      type: String,
      default: () => {
        return "";
      },
    },
  },
  methods: {
    cancel() {
      this.$emit("cancel", false);
    },
  },
};
</script>

<style lang='scss' scoped>
.share {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  padding: 0 54upx;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 9999;
  .card {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    .backG {
      height: 1030upx;
    }
    // 用户信息
    .info {
      position: absolute;
      top: 90px;
      left: 50%;
      transform: translateX(-50%);
      image {
        width: 104upx;
        height: 104upx;
        border-radius: 52upx;
      }
      view {
        text-align: center;
        font-size: 32upx;
        font-family: AlibabaPuHuiTiR;
        color: #fff;
      }
    }
    // 二维码
    .QRCode {
      position: absolute;
      bottom: 320upx;
      width: 450upx;
      left: 50%;
      transform: translateX(-50%);
      height: 450upx;
      image {
        width: 100%;
        height: 100%;
        border-radius: 32upx;
      }
    }
    // 关闭按钮
    .close {
      width: 100%;
      text-align: center;
      margin-top: 36upx;
      image {
        width: 84upx;
        height: 84upx;
      }
    }
  }
}
</style>