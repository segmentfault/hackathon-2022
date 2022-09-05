<template>
  <view class="card-active merchan-sites">
    <view class="card-tabs" v-if="tabs.length">
      <view
        class="card-tab"
        v-for="(item, index) in tabs"
        :key="index"
        :class="{ active: value === item.value }"
        @click="handlerTab(item, index)"
        >{{ item.name }}</view
      >
      <view class="card-tab card-tab-title" @click="$emit('handlerMore')"
        >更多期待</view
      >
    </view>
    <slot slot="tag"></slot>
    <view class="card-box-center">
      <u-card
        :show-head="false"
        class="card-box-center-wrapper"
        v-for="(item, index) in cardList"
        :key="index"
      >
        <view
          slot="body"
          @click="$emit('handlerDetail', item)"
          class="card-item"
        >
         <image @click.stop="handlerChange(item,index)" :src="
          activeIndex === item.supplierId ? 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/checkbox-checked.png' : 
          'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/checkbox.png'" class="checkbox"></image>
          <image v-if="item.cover" :src="item.cover" mode="aspectFill" class="img"></image>
          <view class="center">
            <view class="title">{{ item.companyName }}</view>
            <!-- 隐藏助力值排名 -->
            <!-- <view class="rank" v-if=" item.popularity == 0 || item.popularity == 1 || item.popularity == 2">
                <image
                  class="rank-icon"
                  :src="`https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/top${item.popularity+1}.png`"
                  mode=""></image>人气第 {{ item.popularity+1 }} 名
              </view> -->
            <view class="price-wrapper" v-if="item">
              <view class="price-small">￥</view>
              <view class="price">{{ item.activityId ? item.discountPrice : item.originalPrice  }}</view>
              <view class="price-smalls" v-if="item.activityId">门市价: {{ item.originalPrice }}</view>
            </view>
            <view class="address" @click.stop="openLocation(item)">
              <image
                class="icon"
                src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/card-place-icon.png"
              ></image>
              <p>{{ item.companyAddress }}</p>
            </view>
          </view>
        </view>
      </u-card>
    </view>
  </view>
</template>
<script>
export default {
  props: {
    activeIndex:{ 
      type: [String, Number],
    },
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
        return "活动规则";
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
      this.$emit("loadMore", {
        pageNum: this.query.pageNum + 1,
        status: "loading",
      });
    },
    handlerChange(item, index) {
      this.$emit('handlerChange', item)
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
      this.$emit("input", item.value);
      this.$emit("loadMore", { pageNum: 1, status: "loading", cardList: [] });
    },
  },
};
</script>
<style lang="scss" scoped>
.card-tabs {
  display: flex;
  flex-direction: row;
  width: calc(100% - 80upx);
  margin: 0 auto;
  justify-content: space-between;
  margin-bottom: 32upx;
  margin-top: 32upx;
  .card-tab {
    flex: 1;
    border-radius: 36upx;
    font-size: 32upx;
    line-height: 56upx;
    font-weight: 400;
    color: #000000;
    text-align: center;
    border: 2upx solid #666666;;
    background: #fff;
    margin-right: 20upx;
  }
  .card-tab-title {
    border: 0;
    font-size: 24upx;
    line-height: 56upx;
    margin-right: 0;
    text-align: right;
    color: #000000;;
    background: transparent;
  }
}
.active {
  background: #000000 !important;
  color: #fff !important;
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
      color: #000000;
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
        color: #666666;
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
        margin-top: 6upx;
        margin-right: 8upx;
      }
      p {
        padding-left: 10upx;
        line-height: 36upx;
        font-size: 24upx;
        color: #666666;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
    }
  }
}
.card-box-center { 
  background: #fff;
}
.checkbox {
  height: 48upx;
  width: 48upx;
  margin-top: 74upx;
  margin-right: 28upx;
 }
</style>
