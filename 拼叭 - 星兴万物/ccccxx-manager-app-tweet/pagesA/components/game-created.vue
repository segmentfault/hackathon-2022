<template>
  <view class="main">
    <image
      class="main__img"
      src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/boost-success.png"
    />
    <view class="main__text">{{ title || "创建成功！" }}</view>
    <!-- 提示关注二维码 -->
    <view class="qrcode" v-if="subscription != 1">
      <view>关注公众号，及时获取组局信息</view>
      <image
        class="qrcode__img"
        @click="previewImage"
        src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/qrcode_for_gh_9dd2f455ac44_258.jpg"
      ></image>
      <view class="qrcode__text">点击保存后微信扫一扫</view>
    </view>
    <!-- 提示完善信息 -->
    <view class="info" v-else>
      <view class="info__text">完善资料和照片</view>
      <view class="info__text">更容易拼成局！</view>
      <arprogress
        class="info__arprogress"
        :percent="percent"
        width="338"
        inactiveColor="#fff"
        activeColor="#333"
      />
      <image class="info__header" :src="head" />
      <cover-view class="info__progress">{{ percent }}%</cover-view>
      <!-- 解决安卓cover-view 边框兼容问题 -->
      <cover-view class="info__progress1">{{ percent }}%</cover-view>
    </view>
    <slot />
  </view>
</template>
<script>
import arprogress from "./ar-circle-progress.vue";
export default {
  components: {
    arprogress,
  },
  props: {
    // 资料完成率
    percent: {
      type: Number,
      default: 20,
    },
    // 是否关注过公众号
    subscription: {
      type: Number | String,
      default: 1,
    },
    head: {
      type: String,
      default:
        "https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/logo.png",
    },
    title: {
      type: String,
    },
  },
  methods: {
    // 预览图片保存
    previewImage() {
      uni.previewImage({
        urls: [
          "https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/qrcode_for_gh_9dd2f455ac44_258.jpg",
        ],
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.main {
  padding: 40upx;
  width: calc(100vw - 80upx);
  height: calc(100vh - 280upx - env(safe-area-inset-bottom));
  background: #fff;
  border-radius: 16upx;
  text-align: center;
  &__img {
    margin-top: 50upx;
    width: 80upx;
    height: 80upx;
  }
  &__text {
    font-size: 40upx;
    margin-top: 24upx;
    margin-bottom: 20upx;
  }
}
.qrcode {
  font-size: 32upx;
  width: calc(100% - 80upx);
  margin-left: 40upx;
  padding: 96upx 0;
  background: #fef4e6;
  &__img {
    width: 240upx;
    height: 240upx;
    margin: 40upx 0;
  }
  &__text {
    width: 400upx;
    border-radius: 40upx;
    background: #fff;
    line-height: 60upx;
    margin-left: 50%;
    transform: translateX(-50%);
    font-size: 28upx;
    color: #6c5e4b;
  }
}
.info {
  padding-top: 30upx;
  text-align: center;
  font-size: 40upx;
  &__arprogress {
    position: relative;
    margin-top: 60upx;
    margin-top: 60upx;
    display: flex;
    justify-content: center;
    // transform: translateX(-50%);
  }
  // .border {
  //   margin: 60upx 0 0 50%;
  //   transform: translateX(-50%);
  //   width: 304upx;
  //   height: 304upx;
  //   border-radius: 50%;
  //   border: 8upx solid red;
  // }
  &__header {
    position: absolute;
    margin-top: -308upx;
    width: 280upx;
    height: 280upx;
    border-radius: 50%;
    left: 50%;
    transform: translateX(-50%);
  }
  &__progress {
    position: absolute;
    z-index: 2;
    left: 50%;
    transform: translateX(-50%);
    font-size: 34upx;
    color: #fff;
    width: 128upx;
    line-height: 56upx;
    background: #333;
    border-radius: 36px;
    margin-top: -58upx;
  }
  &__progress1 {
    position: absolute;
    z-index: 1;
    left: 50%;
    transform: translateX(-50%);
    font-size: 34upx;
    width: 138upx;
    height: 68upx;
    background: #fff;
    border-radius: 36px;
    margin-top: -50upx;
  }
}
</style>