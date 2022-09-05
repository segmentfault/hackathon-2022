<template>
  <view class="activity">
    <ComHead changeColor custom autoPadding>组局大厅</ComHead>
    <!-- banner栏 -->
    <view class="activity-banner">
      <image src="https://image.pinbar.vip/banner1.png"></image>
    </view>
    <!-- 助力信息 -->
    <view class="activity-message">
      <view class="activity-message-top"> </view>
      <view class="activity-message-bottom">
        <view class="top"
          ><!-- 基本用法 -->
          <uni-steps :active="active" :options="options"></uni-steps>
        </view>
        <view class="look">
          <view>还差5位解锁5折优惠券~</view>
          <view @click="lookHistory(myActivityDetail.boostUsers)"
            >历史助力用户 ></view
          ></view
        >
        <view class="add">
          <image
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/1070e9e1-8682-43f3-9757-57b94466662c.png"
            v-for="(v, i) in 5"
            :key="i"
          ></image>
        </view>
        <view class="btn">
          <button open-type="share">邀请好友助力</button>
          <button>不差钱，立即预约</button>
        </view>
      </view>
    </view>
    <!-- 收到好友助力 -->
    <!-- 活动商家 -->
  </view>
</template>

<script>
import ComHead from "../../components/com-head.vue";
import gameService from "../../service/game";

export default {
  components: {
    ComHead,
  },
  data() {
    return {
      options: [
        { title: "分享助力" },
        { title: "预约报名" },
        { title: "到店核销" },
      ],
      active: 0,
      myActivityDetail: "",
    };
  },
  created() {
    this.getActivityDetail();
  },
  methods: {
    // 跳转到用户助力历史页
    lookHistory(item) {
      uni.navigateTo({
        url: "/pagesA/activity/components/history",
      });
    },
    // 获取当前用户的活动助力信息
    async getActivityDetail() {
      this.myActivityDetail = await gameService.applyActivityDetail();
    },
  },
};
</script>

<style lang='scss'>
.activity {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 20upx 32upx;
  &-banner {
    width: 100%;
    height: 302upx;
    position: relative;
    image {
      width: 100%;
      height: 100%;
    }
  }
  &-message {
    width: 100%;
    height: 708upx;
    border: 1px solid #ccc;
    border-radius: 16upx;
    padding: 10upx;
    background-color: #ccc;
    margin-bottom: 50upx;
    &-top {
      height: 50upx;
    }
    &-bottom {
      width: 100%;
      background-color: #fff;
      border-radius: 16upx;
      .top {
        padding: 30upx 0;
      }
      .look {
        margin: 30upx 0;
        text-align: center;
      }
      .add {
        display: flex;
        justify-content: space-around;
        margin-bottom: 40upx;
        image {
          width: 100upx;
          height: 100upx;
          border-radius: 50upx;
        }
      }
      .btn {
        padding: 10upx 100upx;
        button {
          background-color: #000;
          color: #fff;
          height: 80upx;
          border-radius: 40upx;
          line-height: 80upx;
          &:last-child {
            margin: 20upx 0;
          }
        }
      }
    }
  }
}
</style>