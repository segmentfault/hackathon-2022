<template>
  <view>
    <!-- 顶部说明文案 -->
    <banner :src="src" @handlerRule="$emit('handlerRule')"></banner>

    <!-- 预约活动按钮-->
    <view class="subscribe" @click="$emit('handlerSubscribe')"></view>
    <!-- 预约活动按钮-->
    <view class="subscribe-text">可选择拼场/包场，早预约早锁场</view>
  </view>
</template>
<script>
import banner from "./banner.vue";
export default {
  components: {
    banner,
  },
  props: {
    src: {
      type: String,
      required: true,
    },
  },
};
</script>
<style lang="scss" scoped>
.subscribe {
  width: 608upx;
  height: 88upx;
  background: url("https://image-1306191496.cos.ap-nanjing.myqcloud.com/3958a15a-3aaf-4d6f-b06f-0be61f9dda91.png")
    no-repeat center;
  background-size: 100% 100%;
  margin-top: 380rpx;
  z-index: 1;
  position: relative;
}
.subscribe-text {
  margin-top: 28upx;
  margin-bottom: 72upx;
  font-size: 26upx;
  font-weight: 500;
  color: #ffffff;
  line-height: 26upx;
  text-align: center;
}
</style>