<template>
  <view class="flex-box" style="align-items: flex-start">
    <view class="main flex-c-center aaaa">
      <image
        class="avatar"
        v-if="newBoostUser"
        :src="
          newBoostUser.head ||
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
      <div :class="['discount', 'flex-box']">
        <image
          class="discount-img"
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/coupon.png"
        ></image>
        <view style="color: #d6b7b7">新用户</view>
      </div>
      <view
        v-if="newBoostUser && newBoostUser.nickName"
        style="
          color: #a6a6a6;
          text-align: center;
          font-size: 20upx;
          overflow: hidden;
          text-overflow: ellipsis;
          width: 100upx;
        "
        >{{ newBoostUser.nickName }}</view
      >
    </view>
    <view
      class="main flex-c-center bbbb"
      :key="index"
      v-for="(item, index) in 4"
    >
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
          width: 100upx;
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
    newBoostUser: Object,
  },
  onShareAppMessage() {
    let boostUserId = "";
    if (this.detail) boostUserId = this.detail.userId;
    if (this.myDetail) boostUserId = this.myDetail.userId;
    // 邀请助力的埋点
    wx.reportAnalytics("invitation_help", {});
    return {
      title: "8月29日10家剧本店半价的优惠被抢的好快，快帮我助力一下",
      path: `/pages/activity/discount?shareUserId=${this.myUserId}&boostUserId=${boostUserId}&isHome=1`,
      imageUrl:
        "https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/share-img.png",
    };
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
