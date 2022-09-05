<template>
  <view>
    <view class="invite-box">
      <view class="invite-box-header">
        <view
          class="invite-box-header-item"
          v-for="(item, index) in headerList"
          :key="index"
        >
          <view class="circle" :class="{ active: headerActive === index }">{{
            index + 1
          }}</view>
          <view
            class="text"
            :class="{ 'active-text': headerActive === index }"
            >{{ item.name }}</view
          >
        </view>
      </view>
      <view class="invite-box-border"></view>
      <image
        class="img"
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/e6544c61-286f-44ac-b007-370d7251d31b.png"
      ></image>
      <view class="text">助力成功！</view>
      <button class="boost-btn" @click="goDiscountCenter()">我也要优惠</button>
    </view>
  </view>
</template>
<script>
export default {
  data() {
    return {
      headerActive: 1,
      cardList: [
        {
          title: "组局大厅",
          url: "/pages/index/index",
          desc: "寻找精准拼车队友！",
        },
        {
          title: "一键创建局",
          url: "/pagesA/create/index",
          desc: "我的队友我挑选！",
        },
      ],
      headerList: [
        {
          name: "预约活动",
        },
        {
          name: "好友助力",
        },
        {
          name: "到店使用券",
        },
      ],
    };
  },
  props: {
    query: {
      type: Object,
    },
    activeDetail: {
      type: Object,
      default: () => {
        return {
          boostUsers: [],
        };
      },
    },
  },
  methods: {
    handlerCard(item) {
      if (item.url === "/pagesA/create/index") {
        this.$eventRecord(145);
      } else {
        this.$eventRecord(146);
      }
    },
    goDiscountCenter() {
      this.$eventRecord(148);
      if(this.activeDetail.statue === "活动已结束") return this.$toast("活动已结束");
      uni.navigateTo({
        url: `/pagesA/barActivity/discountCenter?activityId=${this.query.activityId}`,
      });
    },
    toCoupon() {
      // 埋点
      this.$eventRecord(144);
      uni.navigateTo({
        url: "/pagesA/user/coupon",
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.boost-btn {
  width: 608upx;
  height: 88upx;
  background: linear-gradient(270deg, #cd6cff 0%, #fe4a2a 100%);
  border-radius: 12upx;
  font-size: 40upx;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #ffffff;
  line-height: 56upx;
  letter-spacing: 2upx;
  text-shadow: 0px 4upx 8upx #e51b03;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}
.invite-box {
  margin-top: 120rpx;
  width: 680upx;
  background: #f9f9f9;
  border-radius: 16upx;
  margin-bottom: 16upx;
  padding-bottom: 80upx;
  position: relative;
  .img {
    width: 128upx;
    height: 128upx;
    display: block;
    margin: 0 auto;
    margin-top: 44upx;
    margin-bottom: 8upx;
  }
  &-border {
    height: 36upx;
    background: linear-gradient(
      180deg,
      rgba(252, 248, 248, 0.4) 0%,
      #d7c9c9 100%
    );
  }
  .progress {
    margin-top: 52upx;
    margin-bottom: 80upx;
  }
  .text {
    font-size: 40upx;
    color: #333;
    text-align: center;
    font-weight: bold;
    margin: 18upx 0;
  }
  .text-btn {
    width: 276upx;
    height: 52upx;
    background: #f0e9d5;
    border-radius: 30px;
    font-size: 24upx;
    color: #000000;
    line-height: 52upx;
    margin: 0 auto;
    text-align: center;
    margin-bottom: 62upx;
  }
  &-header {
    height: 102upx;
    display: flex;
    flex-direction: row;
    align-items: center; /*垂直居中*/
    justify-content: center; /*水平居中*/
    padding: 0 28upx;
    z-index: 1;
    background-color: #fff;
    &-item {
      flex: 1;
      .circle {
        display: inline-block;
        width: 48upx;
        height: 48upx;
        background: #ffe5e5;
        font-weight: 600;
        line-height: 48rpx;
        text-align: center;
        border-radius: 100%;
        margin-right: 20upx;
        color: #ff9696;
        font-weight: bold;
      }
      .text {
        display: inline-block;
        color: #999999;
        font-size: 24upx;
        font-weight: bold;
      }
      .active {
        background: linear-gradient(
          180deg,
          #fe4a2b 0%,
          #cd6cfe 100%
        ) !important;
        color: #ffffff !important;
      }
      .active-text {
        color: #000000 !important;
      }
    }
  }
}
</style>