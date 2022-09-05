<template>
  <view class="card-active merchan-sites">
    <view class="card-tabs" v-if="tabs.length">
      <view class="name">类型</view>
      <view
        v-for="(item, index) in tabs"
        :key="index"
        :class="[{ active: value === item.label }, 'card-tab']"
        @click="handlerTab(item, index)"
        >{{ item.label }}</view
      >
    </view>
    <slot></slot>
    <div class="top-bg" v-if="isTop"></div>
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
          <image
            @click.stop="handlerChange(item, index)"
            :src="
              activeId === item.id
                ? 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/checkbox-checked.png'
                : 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/checkbox.png'
            "
            class="checkbox"
            v-if="isSelect"
          ></image>
          <image :src="item.goodsPic" mode="aspectFill" class="img"></image>
          <view class="center">
            <view class="title">{{ item.goodsName }}</view>
            <view class="price-wrapper">
              <view class="flex fs24 mt5">
                <view v-if="item.goodsClass == 1"
                  >{{ item.scriptPeopleNum }}人本</view
                >
                <text class="ml5 mr5" v-if="item.goodsClass == 1">|</text>
                <view v-if="item.labelList && item.labelList.length">
                  {{ item.labelList.join("·") }}
                </view>
                <text class="ml5 mr5" v-if="item.goodsClass == 1">|</text>
                <view
                  v-if="
                    item.scriptMan && item.scriptWoman && item.goodsClass == 1
                  "
                  >{{ item.scriptMan }}男{{ item.scriptWoman }}女</view
                >
                <text class="ml5 mr5" v-if="item.goodsClass == 1">|</text>
                <view v-if="item.scriptDuration && item.goodsClass == 1"
                  >{{ item.scriptDuration }}h
                </view>
                <text class="ml5 mr5" v-if="item.goodsClass == 1">|</text>
                <view v-if="item.scriptDifficulty && item.goodsClass == 1">{{
                  item.scriptDifficulty
                }}</view>
              </view>
            </view>
            <view
              class="flex flex-between flex-middle mt5"
              :class="{ mt20: item.goodsClass == 3 }"
            >
              <view class="">
                <view class="">
                  <text
                    class="color-croci fs32"
                    v-if="item.goodsPriceItem.salePrice"
                    >¥{{ item.goodsPriceItem.salePrice }}/</text
                  >
                  <text class="color-croci fs24">人</text>
                  <text
                    class="fs20 color-999 ml10"
                    v-if="item.goodsPriceItem.marketPrice"
                    >门市价 ¥{{ item.goodsPriceItem.marketPrice }}</text
                  >
                </view>
                <view
                  class="share-price fs22 color-croci"
                  v-if="item.goodsPriceItem.shareRebate && identityType != 0"
                  >分享赚{{ item.goodsPriceItem.shareRebate }}</view
                >
              </view>
              <view class="make">预定</view>
            </view>
            <view class="address mt10">
              <view class="flex">
                <view
                  class="content"
                  v-html="
                    showTitle(item.goodsClass) +
                    (item.scriptDesc || item.secretBrief || item.desc)
                  "
                ></view>
              </view>
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
    activeId: {
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
    isTop: {
      type: Boolean,
      default: false,
    },
    isSelect: {
      type: Boolean,
      default: true,
    },
  },
  mounted() {
    console.log(this.cardList);
		this.$store.commit('setIdentityType');
  },
	computed:{
		identityType(){
			return this.$store.state.user.identityType
		}
	},
  methods: {
    showTitle(type) {
      return type == 1 ? "剧情简介：" : type == 2 ? "主题简介：" : "简介：";
    },
    getMoreList() {
      this.$emit("loadMore", {
        pageNum: this.query.pageNum + 1,
        status: "loading",
      });
    },
    handlerChange(item, index) {
      this.$emit("handlerChange", item);
    },
    handlerTab(item) {
      console.log(item);
      this.$emit("input", item.label);
      this.$emit("loadMore", {
        pageNum: 1,
        status: "loading",
        cardList: [],
        cardList1: [],
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.hot-script {
  padding-bottom: 120rpx;
}
.card-tabs {
  display: flex;
  flex-direction: row;
  margin: 0 auto;
  justify-content: space-between;
  padding: 32upx 15upx;
  background: #171c33;
  overflow-x: scroll;
  .border {
    height: 32upx;
    background: 171c33;
  }
  .name {
    min-width: 70upx;
    font-size: 24upx;
    font-weight: bold;
    color: #8c91a9;
    line-height: 56upx;
  }
  .card-tab {
    border-radius: 36upx;
    font-size: 24upx;
    line-height: 44upx;
    font-weight: 400;
    text-align: center;
    border: 2upx solid #fde5a4;
    background: #171c33;
    color: #fde5a4;
    margin-right: 20upx;
    min-width: 100rpx;
    padding: 6rpx 16rpx;
    display: inline-block;
    flex-flow: wrap;
    flex-shrink: 0;
  }
  .card-tab-title {
    border: 0;
    font-size: 24upx;
    line-height: 56upx;
    margin-right: 0;
    text-align: right;
    color: #000000;
    background: transparent;
  }
}
.card-tabs::-webkit-scrollbar {
  display: none;
}
.active {
  background: #fde5a4 !important;
  color: #171c33 !important;
}
.card-item {
  display: flex;
  flex-direction: row;
  position: relative;
  .img {
    width: 144upx;
    height: 230upx;
    display: inline-block;
    border-radius: 8upx;
  }
  .make {
    width: 120upx;
    height: 50upx;
    background: #ff4f3a;
    border-radius: 30upx;
    font-size: 26upx;
    color: #fff;
    line-height: 50upx;
    text-align: center;
  }
  .share-price {
    padding: 0 10upx;
    border: 1px solid #ff4f3a;
    border-radius: 4upx;
    display: inline-block;
  }
  .center {
    flex: 1;
    padding-left: 20upx;
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
      display: flex;
      flex-direction: row;
      vertical-align: bottom;
      .price-smalls {
        font-size: 24upx;
        color: #666666;
        line-height: 40upx;
        align-self: flex-end;
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
    .price-top {
      padding-top: 0;
    }
    .address {
      .content {
        line-height: 36upx;
        font-size: 24upx;
        color: #999;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }
    }
  }
}
.card-box-center {
  // background: #fff;
  // border-radius: 32upx 32upx 0 0;
  background: #fff;
  // margin-top: -30upx;
}
.checkbox {
  height: 48upx;
  width: 48upx;
  margin-top: 34upx;
  margin-right: 28upx;
  position: absolute;
  right: 0;
  top: 0;
}
.top-bg {
  background: #171c33;
  height: 15upx;
  width: 100%;
}
</style>
