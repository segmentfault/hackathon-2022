<template>
  <view class="outEervice">
    <view class="title">不好意思，长沙以外的地区暂未开放哦！</view>
    <view class="btn">
      <button type="default" @click="getLocation">重新获取定位</button>
    </view>
  </view>
</template>

<script>
// 腾新地图sdk
export default {
  data() {
    return {};
  },
  methods: {
    getLocation: () => {
      uni.redirectTo({
        url: "/pages/index/index",
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.outEervice {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  height: 100vh;
  .title {
    margin: 0 auto;
    font-size: 32upx;
    text-align: center;
  }

  button {
    // margin: 0 auto;
    width: 350upx;
    height: 80upx;
    background-color: #000;
    border-radius: 40upx;
    color: #fff;
    line-height: 80upx;
  }
}
</style>
