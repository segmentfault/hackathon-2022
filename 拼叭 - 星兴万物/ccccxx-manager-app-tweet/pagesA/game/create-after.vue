<template>
  <view class="create-after">
    <game-created
      :percent="percent"
      :head="head"
      :subscription="subscription"
    ></game-created>
    <view class="footer" v-if="subscription != 1">
      <button class="footer__btn" plain open-type="share" @click.stop="" @click="this.$eventRecord(44);">
        分享给朋友
      </button>
    </view>

    <view class="footer footer1" v-else>
      <button class="footer1__btn1" plain open-type="share" @click.stop="this.$eventRecord(44);" >
        分享给朋友
      </button>
      <button class="footer1__btn2" plain @click="jumpUserEdit">
        快速完善
      </button>
    </view>
  </view>
</template>

<script>
import arprogress from "../components/ar-circle-progress.vue";
import gameCreated from "../components/game-created.vue";
export default {
  components: {
    arprogress,
    gameCreated,
  },
  data() {
    return {
      // 资料完成率
      percent: 0,
      // 是否关注过公众号
      subscription: "",
      // 头像
      head: "",
      // 分享的标题
      title: "",
      // 分享的封面
      cover: "",
      //gameID
      gameId: "",
    };
  },
  methods: {
    // 跳转到完善用户资料
    jumpUserEdit() {
      uni.redirectTo({
        url: "/pagesA/user/edit",
      });
      // 埋点
      this.$eventRecord(45);
    },
  },
  onLoad(option) {
    this.percent = option.rate || 80;
    this.subscription = option.subscription || 1;
    this.head =
      option.head ||
      "https://thirdwx.qlogo.cn/mmopen/vi_32/EwK8sxVUROs4A5fM2DjlYuZJvLmnLwYWbHxRViah4BtGPvuX6icUA2brbunv7qdUM7ec7yhfZJ3GfFG3ONVAuiadQ/132";
    this.cover = option.cover;
    this.title = option.title;
    this.gameId = option.gameId;
  },
  onShareAppMessage() {
    return {
      title: this.title,
      path: `/pagesA/game/detail?gameId=${this.gameId}&shareUserId=${this.myUserId}&isHome=1`,
      imageUrl: this.cover,
    };
  },
};
</script>

<style lang="scss" scoped>
.create-after {
  background: #f5f5f5;
  width: 100vw;
  height: 100vh;
  padding: 40upx;
}
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
    margin-bottom: 80upx;
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
    position: absolute;
    margin-top: 60upx;
    margin-top: 60upx;
    transform: translateX(-50%);
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
    margin-top: 90upx;
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
    margin-top: 342upx;
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
    margin-top: 336upx;
  }
}
.footer {
  position: fixed;
  left: 0;
  bottom: 0;
  background: #fff;
  width: 100vw;
  padding: 40upx 0 calc(40upx + env(safe-area-inset-bottom)) 0;
  &__btn {
    width: 532upx;
    height: 108upx;
    line-height: 108upx;
    margin-left: 50%;
    transform: translateX(-50%);
    color: #fff;
    font-size: 40upx;
    background: #333333;
    border-radius: 24upx;
  }
}
.footer1 {
  display: flex;
  justify-content: space-between;
  padding-left: 40upx;
  padding-right: 40upx;
  &__btn1 {
    border: 1px solid #333333;
    width: 310upx;
    height: 108upx;
    line-height: 108upx;
    color: #333333;
    font-size: 40upx;
    background: #fff;
    border-radius: 24upx;
  }
  &__btn2 {
    width: 310upx;
    height: 108upx;
    line-height: 108upx;
    color: #fff;
    font-size: 40upx;
    background: #333333;
    border-radius: 24upx;
  }
}
</style>
