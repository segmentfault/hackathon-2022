<template>
  <view
    class="supplier-item"
    :class="{ apply, dark, isRank }"
    @click="goSupplierDetail"
  >
    <image class="logo" mode="aspectFill" :src="supplier.cover"></image>
    <view class="content">
      <view class="t1"
        >【{{ supplier.typeName }}】{{ supplier.companyName }}</view
      >
      <view class="price-flex">
        <view class="price">￥{{ supplier.discountPrice || 0 }}</view>
        <view class="old-price"
          >门市价：￥{{ supplier.originalPrice || 0 }}</view
        >
      </view>
      <view class="price-flex" @click.stop="openMap">
        <image
          v-if="dark"
          class="icon"
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/icon-location.png"
        ></image>
        <image
          v-else
          class="icon"
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/icon-navigate.png"
        ></image>
        <view class="address">{{ supplier.companyAddress }}</view>
      </view>
      <view
        class="rank-line flex-s"
        v-if="isRank"
        :class="
          rank == 1
            ? 'top1'
            : rank == 2
            ? 'top2'
            : rank == 3
            ? 'top3'
            : 'other-top'
        "
      >
        <view class="rank-line-l flex-c">
          <image
            class="rank-icon"
            v-if="rank == 1 || rank == 2 || rank == 3"
            :src="`https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/top${rank}.png`"
            mode=""
          ></image
          >人气第 {{ rank }} 名
        </view>
        <view class="rank-line-r flex-c"
          ><image
            class="fire-icon"
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/fire.png"
            mode=""
          ></image
          >{{ supplier.popularity }}</view
        >
      </view>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    rank: Number,
    dark: Boolean,
    apply: Boolean,
    supplier: Object,
    isReport: Boolean,
    isRank: Boolean,
  },
  methods: {
    goSupplierDetail() {
      if (this.isReport) {
        // 报名页商家卡片的埋点
        wx.reportAnalytics("supplier_view", {});
      } else {
        // 助力页商家卡片的埋点
        wx.reportAnalytics("supplier_help_click", {});
      }

      uni.navigateTo({
        url:
          "/pagesA/businessDetail/index?supplierId=" +
          this.supplier.supplierId + '&typeId=' + this.supplier.typeId,
      });
    },
    openMap() {
      // 打开地图
      uni.openLocation({
        latitude: Number(this.supplier.lat),
        longitude: Number(this.supplier.lng),
        name: this.supplier.companyAddress,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.flex-c {
  display: flex;
  align-items: center;
}
.flex-s {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.supplier-item {
  display: flex;
  margin: 32upx auto;
  width: 100%;
  color: #fff;

  &.dark {
    background: rgba(236, 236, 236, 0.1);
    border-radius: 16upx;
    padding: 28upx;
    width: 678upx;

    .content {
      .t1,
      .old-price,
      .price-flex .address {
        color: #fff;
      }
      .top1 {
        background: #ffedbd !important;
      }
      .top2 {
        background: #d4dce3 !important;
      }
      .top3 {
        background: #e4d3c8 !important;
      }
      .other-top {
        padding-left: 20upx;
        background: #5f52d2 !important;
        .rank-line-l {
          color: #fff !important;
        }
      }
      .rank-line {
        border-radius: 34upx;
        width: 100%;
        padding-right: 32upx;
        background-color: #fff;
        margin-top: 20upx;
        height: 72upx;
        .rank-icon {
          height: 72upx;
          width: 72upx;
          margin-right: 6upx;
        }
        &-l {
          font-size: 28upx;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #7c3e05;
          line-height: 52upx;
        }
        &-r {
          font-size: 24upx;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #e85858;
          line-height: 52upx;
          .fire-icon {
            height: 28upx;
            width: 22upx;
            margin-right: 6upx;
          }
        }
      }
      .fire {
      }
    }
  }

  &.apply {
    background: #fff;
    color: #000;
    margin: 0 0 0 20upx;
  }
  &.isRank {
    background: #272628;
  }

  .content {
    width: calc(100% - 230upx);

    .price-flex {
      display: flex;
      margin-top: 20upx;

      .icon {
        margin-top: 2upx;
        height: 30upx;
        width: 30upx;
        margin-right: 14upx;
        flex-shrink: 0;
      }

      .address {
        font-size: 24upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #000000;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }

    .t1 {
      font-size: 36upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
    }

    .price {
      font-size: 40upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #fe4a2a;
      line-height: 40upx;
    }

    .old-price {
      margin-left: 30upx;
      font-size: 24upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #666666;
      line-height: 46upx;
    }
  }

  .logo {
    height: 196upx;
    width: 196upx;
    border-radius: 16upx;
    margin-right: 34upx;
    flex: none;
  }

  .icon {
    width: 28upx;
    height: 28upx;
  }
}
</style>
