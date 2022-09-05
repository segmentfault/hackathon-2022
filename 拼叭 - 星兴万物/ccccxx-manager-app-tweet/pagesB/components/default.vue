<template>
  <view class="default">
    <image :src="image" mode=""></image>
    <text>{{ text }}</text>
  </view>
</template>

<script>
export default {
  props: {
    image: {
      type: String,
      default:
        'https://image-1306191496.cos.ap-nanjing.myqcloud.com/bacc4150-2e68-4d7a-9fe9-ea7b189bfb7f.png',
    },
    text: {
      type: String,
      default: '暂无数据',
    },
  },
  data() {
    return {};
  },
};
</script>

<style lang="scss" scoped>
.default {
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: #fff;
  text-align: center;
  border-radius: 32upx 32upx 0 0;
  position: absolute;
  bottom: 0;
  width: 100%;
  bottom: 0;
  top: 0;
  left: 0;
  align-items: center;
  image {
    height: 200upx;
    width: 200upx;
   display: inline-flex;
  }
  text {
    color: #999;
    display: inline-flex;
  }
}
</style>
