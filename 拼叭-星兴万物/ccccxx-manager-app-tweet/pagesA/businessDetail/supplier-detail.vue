<template>
  <view class="supplier-detail" v-if="supplier">
    <!-- banner-ctn -->

    <view class="banner-ctn">
      <swiper
        class="swiper-block"
        :indicator-dots="true"
        :autoplay="true"
        :interval="3000"
        :duration="1000"
        circular="true"
        next-margin="90upx"
        current="0"
        @change="swiperChange"
      >
        <swiper-item
          class="swiper-item"
          :key="index"
          v-for="(item, index) in supplier.pictures"
        >
          <image
            :src="item"
            :class="['slide-image', currentIndex === index ? 'active' : '']"
            mode="aspectFill"
          >
          </image>
        </swiper-item>
      </swiper>
    </view>

    <view class="game-card">
      <view class="game-t">{{ supplier.companyName }}</view>
      <view class="game-tag">
        <view class="tag-item" :key="i" v-for="(item, i) in supplier.cates">{{
          item
        }}</view>
      </view>
      <view class="flex-s">
        <view class="word-day"
          >营业时间：{{ supplier.startHour }}点到{{ supplier.endHour }}点</view
        >
        <view class="call-icon"></view>
        <image
          class="call-icon"
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/call-icon.png"
          @click="callSupplier"
        ></image>
      </view>
      <view class="address-bg" @click="openMap">
        <image
          class="icon-navigate"
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/icon-navigate.png"
        ></image>
        {{ supplier.companyAddress }}
      </view>
    </view>

    <view style="margin: 36upx 36upx 160upx">
      <jyf-parser :html="supplier.description" use-anchor />
      <!-- <rich-text :nodes="supplier.description"></rich-text> -->
    </view>
    <view class="btm-bg" @click="goApply" v-if="activityId">
      <view class="btm-btn">
        <view class="t1">立即预约</view>
      </view>
    </view> 
  </view>
</template>

<script>
import GameService from "../../service/game.js";
import JyfParser from "../components/jyf-parser/jyf-parser.vue";
export default {
  components: {
    JyfParser,
  },
  data() {
    return {};
  },
  data() {
    return {
      supplier: null,
      currentIndex: 0,
      activityId: null,
    };
  },
  onLoad(o) {
    this.supplierId = o.supplierId;
    if (o.activityId) this.activityId = o.activityId;
    this.getSupplierDetail();
  },
  onShow() {
    // 埋点
    this.$eventRecord(80);
  },
  methods: {
    openMap() {
      // 打开地图
      uni.openLocation({
        latitude: Number(this.supplier.lat),
        longitude: Number(this.supplier.lng),
        name: this.supplier.companyAddress,
      });
    },
    getSupplierDetail() {
      return GameService.getSupplierDetail(this.supplierId).then((res) => {
        this.supplier = res;
      });
    },
    callSupplier() {
       // 周三半价活动
       if (this.activityId == 3) {
        // 活动3打电话埋点 
        this.$eventRecord(139)
      } else { 
        this.$eventRecord(107)
      }
      // 联系商家报名的埋点
      wx.reportAnalytics("contact_shop", {});
     
     
     
     
     
     
     
      uni.makePhoneCall({
        phoneNumber: String(this.supplier.companyPhone),
      });
    },
    swiperChange(event) {
      this.currentIndex = event.detail.current;
    },
    goApply() {
      // 立即预约埋点
      this.$eventRecord(140)
      // 商家详情报名的埋点
      let url = '/pagesA/activity/apply'
      // 活动类型B
      if(this.activityId) { 
        url = `/pagesA/activity/applyCenter?&activityId=${this.activityId}`
      }
      wx.reportAnalytics("apply_supplier_home", {});
      uni.navigateTo({ url });
    },
  },
};
</script>

<style lang="scss">
.supplier-detail {
  background: #171c33;
  min-height: 100vh;
  position: relative;
  padding-top: 36upx;
  padding-bottom: 1upx;

  .banner-ctn {
    margin-left: 36upx;
    position: relative;
    width: calc(100% - 72upx);
    height: 320upx;
    border-radius: 16upx;
    margin-right: 36upx;
  }

  .game-card {
    margin-top: 28upx;
    margin-left: 34upx;
    margin-right: 34upx;
    width: calc(100% - 68upx);
    padding: 18upx 64upx 18upx 18upx;
    background: #2a2f43;
    box-shadow: 0px 4upx 76upx 0px rgba(0, 0, 0, 0.1);
    border-radius: 16upx;

    .game-t {
      font-size: 36upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #ffffff;
    }

    .game-tag {
      display: flex;
      align-items: center;
      margin-top: 20upx;

      .tag-item {
        margin-right: 12upx;
        padding: 4upx 18upx;
        background: #ffffff;
        border-radius: 26upx;
        font-size: 24upx;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #333333;
      }
    }

    .flex-s {
      margin-top: 52upx;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .word-day {
        font-size: 28upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #ffffff;
      }

      .call-icon {
        height: 32upx;
        width: 32upx;
      }
    }

    .address-bg {
      margin-top: 34upx;
      display: inline-block;
      padding: 0px 16upx;
      background: #ececec;
      border-radius: 18upx;

      .icon-navigate {
        margin-top: 4upx;
        margin-right: 8upx;
        display: inline-block;
        height: 28upx;
        width: 30upx;
      }
    }
  }

  .hot-t {
    margin-top: 64upx;
    margin-left: 72upx;
    font-size: 40upx;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #ffffff;
  }

  .card-item {
    margin: 32upx 36upx 0px 36upx;
    width: calc(100% - 72upx);
    background-color: rgba(216, 216, 216, 0.1);
    border-radius: 16upx;
    position: relative;
    overflow: hidden;
    display: flex;

    &-l {
      height: 196upx;
      width: 196upx;
      margin-right: 24upx;
      position: relative;
      overflow: hidden;
      border-radius: 16upx;
    }

    &-r {
      width: calc(100% - 230upx);

      .title {
        font-size: 36upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #ffffff;
      }

      .tag-list {
        margin-top: 16upx;
        display: flex;
        align-items: center;

        .tag:last-child {
          border: none !important;
        }

        .tag {
          height: 24upx;
          line-height: 24upx;
          border-right: 2upx solid #fff;
          padding-right: 10upx;
          font-size: 24upx;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #ffffff;
          margin-right: 10upx;
        }
      }

      .desc {
        margin-top: 28upx;
        font-size: 24upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #ffffff;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
      }
    }
  }

  .swiper-block {
    position: relative;
    height: 320upx;
    width: 100%;
  }

  .swiper-item {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
  }

  .slide-image {
    height: 320upx;
    width: 100%;
    border-radius: 9upx;
    z-index: 1;
  }
}

.btm-bg {
  position: fixed;
  left: 0px;
  padding-top: 20upx;
  bottom: 0px;
  width: 100%;
  // background: #171C33;
  background-color: rgba(23, 28, 51, 0.9);
  height: 160upx;
  padding-bottom: env(safe-area-inset-bottom);

  .btm-btn {
    // padding: 14upx 0px;
    margin: 0upx 68upx;
    height: 107upx;
    width: calc(100% - 136upx);
    background: #fde5a4;
    border-radius: 54upx;

    .t1 {
      text-align: center;
      font-size: 40upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 107upx;
    }
  }
}
</style>
