<template>
  <view
    class="dialog"
    @click="$emit('input', !value)"
    v-if="value"
    catchtouchmove="preventMove"
  >
    <view class="card">
      <view class="title"
        ><text>刚刚这些好友帮你助力啦！</text
        ><text>还差一位新用户解锁5折优惠券</text>
      </view>
      <view class="userInfo" v-if="value">
        <view class="list" v-for="(item, i) in dialogBoostUsers" :key="i">
          <text class="isNew">{{ item.isNew ? '新用户' : '老用户' }}</text>
          <image :src="item.head"></image>
          <view class="nickName">{{ item.nickName || '昵称' }}</view>
        </view>
      </view>
      <button class="btn-share" @click="$emit('input', !value)" open-type="share">
        继续邀请好友
      </button>
    </view>
  </view>
</template>
<script>
export default {
    props: {
        value: {
            type: Boolean,
        },
        dialogBoostUsers: { 
            type: Array,
            default: []
        },
    },
}
</script>

<style lang="scss" scoped>
.dialog {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.55);
  z-index: 99;
  padding: 0 40upx;
  .card {
    width: 90%;
    background-color: #fff;
    border-radius: 16upx;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    .title {
      display: flex;
      align-items: center;
      flex-direction: column;
      margin-top: 30upx;
    }
    .userInfo {
      margin: 30upx 20upx;
      display: flex;
      align-items: center;
      justify-content: center;
      .list {
        width: 120upx;
        text-align: center;
        margin: 0 10upx;
        .isNew {
          background-color: #000;
          color: #fff;
          border-radius: 16upx;
          padding: 5upx 10upx;
          font-size: 24upx;
          margin-left: 10upx;
        }
        image {
          width: 100upx;
          height: 100upx;
          border-radius: 50upx;
          border: 2upx solid #999;
          margin: 5upx 0;
        }
        .nickName {
          overflow: hidden;
          box-sizing: border-box;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
    .btn-share {
      width: 60%;
      height: 80upx;
      background-color: #000;
      color: #fff;
      line-height: 80upx;
      margin: 40upx auto;
      border-radius: 40upx;
      font-size: 32upx;
      text-align: center;
    }
  }
}
</style>
