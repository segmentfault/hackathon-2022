<template>
  <view class="card-active">
    <view class="half-tips">
      <image
        class="half-tips-img"
        src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/half-bg.png"
      ></image>
      <view class="half-tips-text">活动商家</view>
    </view>
    <view class="card-tabs">
      <view
        class="card-tab"
        v-for="(item, index) in tabs"
        :key="index"
        :class="{ active: value === item.value }"
        @click="handlerTab(item, index)"
        >{{ item.name }}</view
      >
    </view>
    <view class="card-box-center">
      <u-card
        :show-head="false"
        class="card-box-center-wrapper"
        v-for="(item, index) in cardList"
        :key="index"
      >
        <view
          slot="body"
          class="card-item"
          @click="$emit('handlerDetail', item)"
        >
          <image :src="item.cover" mode="aspectFill" class="img"></image>
          <view class="center">
            <view class="title">{{ item.companyName }}</view>
            <!-- 隐藏助力值排名 -->
            <!-- <view class="rank" v-if=" item.popularity == 0 || item.popularity == 1 || item.popularity == 2">
                <image
                  class="rank-icon"
                  :src="`https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/top${item.popularity+1}.png`"
                  mode=""></image>人气第 {{ item.popularity+1 }} 名
              </view> -->
            <view class="price-wrapper">
              <view class="price-small">￥</view>
              <view class="price">{{ item.discountPrice }}</view>
              <view class="price-smalls">门市价: {{ item.originalPrice }}</view>
            </view>
            <view class="address" @click.stop="openLocation(item)">
              <image
                class="icon"
                src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/card-place-icon.png"
              ></image>
              <p>{{ item.companyAddress }}</p>
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
                <view v-else class="rank-icon" style="width: 40rpx;"></view>
                <view class="text"> 人气第 {{ item.rank }} 名</view>
              </view>
              <view class="rank-line-r flex-c"
                ><image
                  class="fire-icon"
                  src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/fire.png"
                  mode=""
                ></image
                >{{ item.popularity }}</view
              >
            </view>
						<view class="tags" v-if="item.tag">{{item.tag}}</view>
          </view>
        </view>
      </u-card>
    </view>
  </view>
</template>
<script>
export default {
  data() {
    return {};
  },
  props: {
    status: {
      type: String,
    },
    value: {
      type: [String, Number],
    },
    tabs: {
      type: Array,
      required: true,
    },
    cardTitle: {
      type: String,
      default: () => {
        return '活动规则';
      },
    },
    cardList: {
      type: Array,
      required: true,
    },
    query: {
      type: Object,
      required: true,
    },
  },
  methods: {
    getMoreList() {
      this.$emit('loadMore', {
        pageNum: this.query.pageNum + 1,
        status: 'loading',
      });
    },
    openLocation(item) {
      // 打开地图显示位置
      uni.openLocation({
        latitude: Number(item.lat),
        longitude: Number(item.lng),
        name: item.companyAddress,
      });
    },
    handlerTab(item) {
      this.$emit('input', item.value);
      this.$emit('loadMore', { pageNum: 1, status: 'loading', cardList: [] });
    },
  },
};
</script>
<style lang="scss" scoped>
.half-tips {
  margin: 0 auto;
  width: 262upx;
  height: 120upx;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  &-img {
    width: 262upx;
    height: 12upx;
  }
  &-text {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 120upx;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 40upx;
    color: #fde5a4;
  }
}
.rank {
  display: flex;
  flex-direction: row;
  font-size: 24upx;
  color: #fff;
  margin-top: 8upx;
  .rank-icon {
    display: inline-block;
    width: 28upx;
    height: 28upx;
    margin-top: 4upx;
    margin-right: 10upx;
  }
}
.card-tabs {
  display: flex;
  flex-direction: row;
  width: calc(100% - 80upx);
  margin: 0 auto;
  justify-content: space-between;
  margin-bottom: 32upx;
  .card-tab {
    flex: 1;
    border-radius: 36upx;
    font-size: 32upx;
    line-height: 56upx;
    font-weight: 400;
    color: #fde5a4;
    text-align: center;
    border: 2upx solid #fde5a4;
    background: #171c33;
    margin-right: 20upx;
  }
  .card-tab-title {
    border: 0;
    font-size: 24upx;
    line-height: 56upx;
    margin-right: 0;
    text-align: right;
    color: #fde5a4;
    background: transparent;
  }
}
.active {
  background: #fde5a4 !important;
  color: #171c33 !important;
}
.card-item {
  display: flex;
  flex-direction: row;
  .img {
    width: 196upx;
    height: 196upx;
    display: inline-block;
    border-radius: 8upx;
  }
  .center {
    flex: 1;
    padding-left: 36upx;
    display: flex;
    flex-direction: column;
    width: 300rpx;

    .title {
      height: 44upx;
      font-size: 32upx;
      font-weight: 600;
      line-height: 44upx;
      color: #000;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
    .price-wrapper {
      padding: 22upx 0;
      display: flex;
      flex-direction: row;
      vertical-align: bottom;
      .price-small {
        font-size: 28upx;
        font-weight: 600;
        color: #ff4f3a;
        line-height: 40upx;
        align-self: flex-end;
        margin-right: 16rpx;
      }
      .price-smalls {
        font-size: 24upx;
        // color: #fff;
        line-height: 40upx;
        align-self: flex-end;
        text-decoration: line-through;
      }
      .price {
        font-size: 40upx;
        font-weight: 600;
        color: #ff4f3a;
        line-height: 40upx;
        align-self: flex-end;
        margin-right: 16rpx;
      }
    }
    .address {
      .icon {
        width: 28upx;
        height: 28upx;
        float: left;
        margin-top: 8rpx;
        margin-right: 8rpx;
      }
      p {
        padding-left: 10upx;
        line-height: 36upx;
        font-size: 24upx;
        // color: #fff;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
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
.tags{
	margin-top: 20upx;
	padding:6upx 28upx;
	display: inline-block;
	border-radius: 20px;
	background: #eee;
	color:#333;
}
.flex-s {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
