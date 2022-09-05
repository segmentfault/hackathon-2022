<template>
  <view class="box">
    <view class="pay">
      <view class="pay-img">
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/00eb5ec4-ed3a-424d-b19a-41374c351dac.png"
          class="icon"
        ></image>
      </view>
      <view class="pay-msg">支付完成!</view>
      <view class="txt">请等待商家联系，或者可以主动联系商家确定场次</view>
      <view class="bt">
        <view class="button2 button1" @tap="call">联系商家</view>
        <view class="button2" @tap="jumpOrderDetail">查看订单</view>
      </view>
    </view>
    <view class="msg">推荐</view>

    <view class="card-box mt20">
      <u-card
        :show-head="false"
        class="card-wrapper"
        v-for="(item, index) in list"
        :key="index"
      >
        <view slot="body" @click="handerOpen(item)">
          <view class="card-item" style="margin-bottom: 54upx">
            <image :src="item.cover" mode="aspectFill"></image>
            <view class="card-content">
              <view class="card-content-title">{{
                showText(item.companyName, 16)
              }}</view>
              <view
                class="tags"
                v-if="item.marks.length && item.marks[0] != null"
                >{{ item.marks[0] }}</view
              >
              <view
                class="card-content-address"
                @click.stop="openLocation(item)"
              >
                <image
                  class="map-icon"
                  src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/card-place-icon.png"
                ></image>
                <view class="map-desc">{{
                  showText(item.companyAddress)
                }}</view>
              </view>
              <view class="card-content-price-wrapper">
                <view class="price-small">￥</view>
                <view class="price-desc">{{ item.originalPrice }}</view>
                <view class="price-small">门市价</view>
              </view>
              <view
                class="rank-line flex-s"
                :class="
                  item.rank == 1
                    ? 'top1'
                    : item.rank == 2
                    ? 'top2'
                    : item.rank == 3
                    ? 'top3'
                    : 'other-top'
                "
              >
                <view class="rank-line-l flex-c">
                  <image
                    class="rank-icon"
                    v-if="item.rank == 1 || item.rank == 2 || item.rank == 3"
                    :src="`https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/top${item.rank}.png`"
                    mode=""
                  ></image>
                  <view v-else class="rank-icon" style="width: 40rpx"></view>
                  <view class="text"> 人气第 {{ item.rank }} 名</view>
                </view>
                <view class="rank-line-r flex-c">
                  <image
                    class="fire-icon"
                    src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/fire.png"
                    mode=""
                  ></image
                  >{{ item.popularity }}</view
                >
              </view>
            </view>
          </view>
          <view v-if="item.goodsList && item.goodsList.length">
            <view
              class="cell-bottom"
              v-for="(target, i) in item.goodsList"
              v-show="i < 2"
              :key="i"
            >
              <view class="cell-bottom-left">
                <view class="bottom-price"
                  >¥ {{ target.goodsPriceItem.costPrice }}
                </view>
                <view class="bottom-sale"
                  >门市价 ¥ {{ target.goodsPriceItem.marketPrice }}</view
                >
              </view>
              <view class="cell-bottom-right">
                <view class="cell-bottom-name">
                  {{ target.scriptName || showName(target.goodsClass) }}</view
                >
                <view class="cell-bottom-desc">
                  <view class="cell-bottom-desc-num"
                    >剩余 {{ target.goodsPriceItem.stockNum }}</view
                  >
                  <view class="cell-bottom-desc-tag"
                    >分享赚 ¥ {{ target.goodsPriceItem.shareRebate }}</view
                  >
                </view>
              </view>
            </view>
            <view class="cell-bottom-more">
              <view class="cell-bottom-more-btn" @click.stop="handerOpen(item)"
                >查看更多＞</view
              >
            </view>
          </view>
        </view>
      </u-card>

      <view class="empty-tip" v-if="status === 'nomore'">
        <image
          class="empty-icon"
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/empty-icon.png"
        ></image>
        已经到底了呢！
      </view>
      <u-loadmore :status="status" v-else />
    </view>
  </view>
</template>

<script>
import productService from '../../service/product';

export default {
  data() {
    return {
      list: [],
      total: 0,
      status: 'loadmore',
      orderNo: '',
      companyPhone: '',
      query: {
        pageNum: 1,
        pageSize: 10,
        lng: 112.93886,
        lat: 28.22778,
      },
    };
  },
  onLoad(option) {
    this.orderNo = option.orderNo;
    this.companyPhone = option.companyPhone;
    this.getList();
  },
  onReachBottom() {
    if (this.list.length < this.total) {
      this.query.pageNum++;
      this.getList();
    }
  },
  methods: {
    call() {
      const that = this;
      uni.makePhoneCall({
        phoneNumber: that.companyPhone,
      });
    },
    jumpOrderDetail() {
      uni.navigateTo({
        url: `/pagesB/order/orderDetail?orderNo=${this.orderNo}`,
      });
    },
    getList() {
      productService.getGoodsFindPage(this.query).then((res) => {
        this.total = res.total;
        res = res.list || [];
        res.forEach((v) => {
          this.$pictureUrl(v.cover, 50, 50);
        });
        this.list = this.list.concat(res);
        if (!res.length || this.list.length < res.total) {
          this.status = 'nomore';
        } else {
          this.status = 'loadmore';
        }
      });
    },
    showText(text, length = 0) {
      if (text && text.length > 16) {
        if (length > 0) {
          return `${text.substring(0, length)}...`;
        } else {
          return `${text.substring(0, 13)}...`;
        }
      } else {
        return text;
      }
    },
    showName(goodsClass) {
      if (goodsClass === 1) {
        return '剧本';
      } else if (goodsClass === 2) {
        return '密室';
      } else {
        return '酒吧';
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.box {
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
  .pay {
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 450upx;
    margin-top: 24upx;
    margin-left: 22rpx;
    margin-right: 22rpx;
    background: #ffffff;
    border-radius: 16upx;
    .icon {
      height: 100upx;
      width: 100upx;
      margin-top: 46upx;
    }
    .pay-msg {
      margin-top: 6upx;
      font-size: 26upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #000000;
      line-height: 36upx;
    }
    .txt {
      margin-top: 52rpx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #000000;
      line-height: 18px;
    }
    .bt {
      display: flex;
      margin-right: 24upx;
      margin-top: 24upx;
      .button2 {
        text-align: center;
        width: 228upx;
        height: 80upx;
        margin-left: 60upx;
        font-size: 32upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        background: #ff4f3a;
        border-radius: 60upx;
        color: #ffffff;
        line-height: 76upx;
        border: 2rpx solid #ff4f3a;
      }
      .button1 {
        background: #fff;
        color: #ff4f3a;
      }
    }
  }
  .msg {
    margin-top: 48upx;
    padding-left: 22upx;
    font-size: 30upx;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 600;
    color: #000000;
    line-height: 42upx;
  }
  .tab-wrapper {
    padding: 26upx 40upx 20upx 40upx;
    position: relative;
  }
  .card-item {
    display: flex;
    flex-direction: row;
    > image {
      width: 200upx;
      height: 200upx;
      border-radius: 16upx;
      display: inline-flex;
    }
    .card-content {
      flex: 1;
      padding-left: 20upx;
      position: relative;
      .card-content-price-wrapper {
        position: relative;
        margin-top: 12upx;
        display: flex;
        flex-direction: row;
        vertical-align: bottom;
        .price-small {
          font-size: 28upx;
          font-weight: 600;
          color: #ff4f3a;
          line-height: 40upx;
        }
        .price-desc {
          padding: 0 16upx;
          font-size: 40upx;
          font-weight: 600;
          line-height: 40upx;
          color: #ff4f3a;
          align-self: flex-end;
        }
      }
      .card-content-title {
        font-size: 32upx;
        font-weight: 600;
        color: #000000;
      }
      .card-content-title-popularity {
        margin-left: 12upx;
        margin-top: 12upx;
        background: linear-gradient(90deg, #ffe0df 0%, #fff5f5 100%);
        border-radius: 6upx;
        display: flex;
        width: 240upx;
        > image {
          width: 32upx;
          height: 32upx;
          margin-top: 4upx;
          margin-left: 8upx;
          margin-right: 8upx;
          display: inline-flex;
        }
        .card-content-title-popularity-desc {
          flex: 1;
          font-size: 24upx;
          font-weight: 500;
          color: #e92f29;
          line-height: 40upx;
          height: 40upx;
          overflow: hidden;
        }
      }
      .card-content-address {
        display: flex;
        flex-direction: row;
        margin-top: 10upx;
        .map-icon {
          height: 30upx;
          margin-top: 6upx;
          width: 28upx;
          display: inline-flex;
        }
        .map-desc {
          flex: 1;
          font-size: 24upx;
          line-height: 40upx;
          color: #666666;
          padding-left: 10upx;
          height: 40upx;
          overflow: hidden;
        }
        .map-loaction {
          display: inline-flex;
          font-size: 24upx;
          line-height: 40upx;
          color: #666666;
          padding-left: 10upx;
        }
      }
    }
  }
  .empty-tip {
    margin-top: 39upx;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 26upx;
    font-family: PingFangSC-Light, PingFang SC;
    font-weight: 300;
    color: #000000;
    .empty-icon {
      height: 64upx;
      width: 64upx;
      margin-right: 18upx;
    }
  }
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
  background: #ffedbd !important;
  .rank-line-l {
    color: #7c3e05 !important;
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
    display: flex;
    flex-direction: row;
    align-items: center;
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
.tags {
  margin-top: 10upx;
  padding: 6upx 18upx;
  display: inline-block;
  border-radius: 20px;
  background: #fef3f3;
  color: #ff4f3a;
  font-size: 24upx;
}
.flex-s {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.cell-bottom {
  display: flex;
  flex-direction: row;
  margin-bottom: 24upx;
  .cell-bottom-left {
    flex-basis: 200upx;
    text-align: center;
    .bottom-price {
      font-size: 32upx;
      line-height: 44upx;
      font-weight: bold;
      color: #ff4f3a;
    }
    .bottom-sale {
      font-size: 24upx;
      color: #999999;
      line-height: 40upx;
    }
  }
  .cell-bottom-right {
    flex: 1;
    .cell-bottom-name {
      font-size: 26upx;
      color: #000000;
      line-height: 44upx;
      font-family: PingFangSC-Regular, PingFang SC;
    }
    .cell-bottom-desc {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      .cell-bottom-desc-num {
        color: #666666;
        font-size: 24upx;
        line-height: 40upx;
        padding-left: 12upx;
      }
      .cell-bottom-desc-tag {
        line-height: 36upx;
        font-size: 22upx;
        font-weight: bold;
        color: #ff4f3a;
        padding: 0 12upx;
        border-radius: 8upx;
        border: 2upx solid #ff4f3a;
      }
    }
  }
}
.cell-bottom-more {
  .cell-bottom-more-btn {
    width: 164upx;
    height: 34upx;
    background: #ebebeb;
    line-height: 34upx;
    font-size: 24upx;
    border-radius: 18upx;
    margin: 0 auto;
    text-align: center;
  }
}
.more-btn {
  position: absolute;
  right: 24rpx;
  top: 26rpx;
  height: 94upx;
  width: 110upx;
  display: flex;
  justify-content: flex-end;
  font-size: 24upx;
  font-family: PingFangSC-Semibold, PingFang SC;
  font-weight: 600;
  color: #666666;
  box-shadow: -5px 0px 4px -4px #e1e1e1;
  margin-top: -14upx;
  .more-btn-bg {
    display: flex;
    align-items: center;
  }
  .more-icon {
    height: 32upx;
    width: 32upx;
    margin-top: 4upx;
    margin-left: 7upx;
  }
}
</style>
