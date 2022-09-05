<template>
  <view class="flex-box" style="align-items: flex-start">
    <view class="main flex-c-center" :key="index" v-for="(item, index) in 5">
      <image
        class="avatar"
        v-if="boostUsers[index]"
        :src="
          boostUsers[index].head ||
          'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/boost-user.png'
        "
      >
      </image>
      <button v-else plain class="avatar" open-type="share">
        <image
          class="avatar-img"
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/boost-none.png"
        ></image>
      </button>
      <div :class="['discount', 'flex-box']" v-if="boostUsers[index].isNew">
        <image
          class="discount-img"
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/coupon.png"
        ></image>
        <view style="color: #d6b7b7">{{
          boostUsers[index].isNew ? "新用户" : ""
        }}</view>
      </div>
      <view
        v-if="boostUsers[index] && boostUsers[index].nickName"
        style="
          color: #a6a6a6;
          text-align: center;
          font-size: 20upx;
          overflow: hidden;
          text-overflow: ellipsis;
          width: 100rpx;
        "
        >{{ boostUsers[index].nickName }}</view
      >
    </view>
  </view>
</template>

<script>
export default {
  props: {
    boostUsers: Array,
  },
};
</script>

<style lang="scss" scoped>
.main {
  position: relative;
}
.avatar {
  width: 100upx;
  height: 100upx;
  border-radius: 50%;
  background: #f5f5f5;
  display: flex;
  margin: 0 13upx;
  &-img {
    width: 100upx;
    height: 100upx;
    border-radius: 50%;
  }
}
.discount {
  position: absolute;
  width: 100upx;
  height: 44upx;
  top: -20upx;
  left: 12upx;
  z-index: 1;
  font-size: 24upx;
  color: #fff;
  &-img {
    width: 100upx;
    height: 44upx;
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
  }
}
.discounts {
  color: #d6b7b7;
}
</style>
