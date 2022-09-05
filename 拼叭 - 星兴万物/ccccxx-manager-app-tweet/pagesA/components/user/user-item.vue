<template>
  <view class="com-bg">
    <image
      class="avatar"
      :src="
        userInfo.user && userInfo.user.head
          ? userInfo.user.head
          : 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/user/logo.png'
      "
      mode=""
    ></image>
    <view class="user-info">
      <view class="user-name">
        <view class="flex-c">
          {{
            userInfo.user && userInfo.user.nickName
              ? userInfo.user.nickName
              : "用户id" + userInfo.userId
          }}
          <view
            class="user-name-state"
            v-if="showDetail"
            :class="{ active: userInfo.state === 6 }"
            >{{ userInfo.state === 6 ? "未核销" : "已核销" }}</view
          >
        </view>

        <a
          class="mobile"
          v-if="userInfo.user.mobile"
          :href="`tel:${userInfo.user.mobile}`"
        >
          <van-icon name="phone-circle-o" size="24px" />
        </a>
      </view>
      <view class="user-text">
        <view class="user-text-l"
          >长沙市
          <text class="tag" v-if="userInfo.user.age">{{
            userInfo.user.age + "岁"
          }}</text>
          <text class="tag" v-if="userInfo.user.sex">{{
            userInfo.user.sex == 1 ? "男" : "女"
          }}</text>
          <text class="tag" v-if="userInfo.user.constellation">{{
            userInfo.user.constellation
          }}</text>
        </view>
        <!-- <view class="user-text-r">{{ $utils.dayjs(userInfo.createTime).format('MM/DD HH:mm') }}</view> -->
      </view>
      <view
        class="like-ctn"
        v-if="userInfo.user.interests && userInfo.user.interests.length"
      >
        <view class="like-ctn-t">兴趣</view>
        <view
          class="like-item"
          :key="i"
          v-for="(item, i) in userInfo.user.interests"
        >
          {{ item }}
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {};
  },
  props: {
    userInfo: Object,
    showDetail: Boolean, // 是否展示用户的详细信息
  },
};
</script>

<style scoped lang="scss">
.flex-c {
  display: flex;
  align-items: center;
}
a {
  font-size: 0px !important;
}
.com-bg {
  display: flex;
  margin: 60upx 36upx 0px 42upx;
  width: calc(100% - 78upx);
  position: relative;
  .avatar {
    height: 112upx;
    width: 112upx;
    flex: none;
    object-position: center;
    object-fit: cover;
    border-radius: 50%;
  }
  .user-info {
    margin-left: 30upx;
    width: calc(100% - 142upx);
    .user-name {
      font-size: 30upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
      line-height: 42upx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
      &-state {
        padding: 0 20upx;
        background: #999;
        color: #444;
        font-size: 24upx;
        height: 42upx;
        line-height: 42upx;
        margin-left: 20upx;
        &.active {
          background: #000 !important;
          color: #fff !important;
        }
      }
    }
    .user-text {
      margin-top: 10upx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      &-l {
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
        line-height: 36upx;
      }
      &-r {
        font-size: 24upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
        line-height: 34upx;
      }
      .tag {
        margin-left: 16upx;
      }
    }
    .like-ctn {
      display: flex;
      flex-wrap: wrap;
      &-t {
        margin-top: 14upx;
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
        line-height: 36upx;
      }
      .like-item {
        margin-top: 14upx;
        padding: 4upx 16upx;
        font-size: 26upx;
        line-height: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        background: #d8d8d8;
        border-radius: 18upx;
        margin-left: 12upx;
      }
    }
    .mobile {
      padding: 20upx 0px;
      color: #333;
      font-size: 28upx;
      display: contents;
    }
  }
}
</style>
