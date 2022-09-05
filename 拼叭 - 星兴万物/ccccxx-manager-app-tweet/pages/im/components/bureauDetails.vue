<template>
  <view class="bureauDetails" @click="goDetail">
    <view class="containers">
      <view class="left">
        <view class="title">{{ groupData.name }}</view>
        <view class="msg">{{
          $formatGameTime(Number(gameStartTime)) + "开始"
        }}</view>
      </view>
      <view class="right">
        <button @click="toDetail">进行中</button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    groupData: {
      type: Object,
      default: () => {
        return {};
      },
    },
    gameStartTime: {
      type: String,
      default: () => {
        return "";
      },
    },
  },
  data() {
    return {
      time: "",
    };
  },
  methods: {
    goDetail() {
      uni.navigateTo({
        url: "/pagesA/game/detail?gameId=" + this.groupData.gameId,
      });
    },
  },
};
</script>

<style lang="scss">
.bureauDetails {
  padding: 20upx 50upx;

  .containers {
    border: 3upx solid #fff;
    border-radius: 16upx;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    background: linear-gradient(to right, #ffe9e6, #ffcec8);

    .left {
      margin: 20upx 0;
      font-size: 30upx;

      .title {
        font-weight: 700;
      }

      .msg {
        margin: 10upx 0;
      }
    }

    .right {
      display: flex;
      align-items: center;

      button {
        background-color: rgba(0, 0, 0, 0.4);
        color: #fff;
        width: 150upx;
        height: 60upx;
        border-radius: 30upx;
        font-size: 24upx;
        line-height: 60upx;
      }
    }
  }
}
</style>
