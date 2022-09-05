<template>
  <view class="">
    <image
      @tap="back"
      src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/10d350b5-da8d-41b1-85cf-cdc5d86cdef9.png"
      class="back-icon"
      v-if="!isHome"
    />
    <image
      @tap="goHome"
      src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/eeebfa36-b79d-4253-9d3b-f4748adb0926.png"
      class="back-icon"
      v-else
    />
    <view class="head">
      <view class="flex flex-middle flex-center mt30">
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/b4b3fe2b-0f35-4bb6-a288-bc9bbe601b31.png"
          class="seckill"
        />
      </view>
      <view class="count-down flex flex-middle flex-center mt40">
        <text class="fs32 color-white mr30">距结束</text>
        <text class="down-icon mr30">{{ hour < 10 ? '0' + hour : hour }}</text>
        <text class="fs color-white mr30">:</text>
        <text class="down-icon mr30">{{
          minute < 10 ? '0' + minute : minute
        }}</text>
        <text class="fs32 color-white mr30">:</text>
        <text class="down-icon">{{ sec < 10 ? '0' + sec : sec }}</text>
      </view>
    </view>
    <view class="tab-wrapper" v-if="showSelect">
      <u-tabs
        :list="tabs"
        height="72"
        bar-width="52"
        font-size="30"
        active-color="#000000"
        bold
        inactive-color="#666666"
        bg-color="#FFFFFF"
        :bar-style="barStyle"
        @change="changeTab"
        :current="current"
      />
      <view class="more-btn" @click="categoryShow = true">
        <view class="more-btn-bg">
          筛选
          <image
            class="more-icon"
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/6042326b-84d6-447d-9f2e-30bf09dd9687.png"
          ></image>
        </view>
      </view>
    </view>

    <view class="list">
      <view
        class="item mt20"
        v-for="item in seckillList"
        :key="item.goodsId"
        @tap="jump(item)"
      >
        <view class="flex flex-middle oneOmit">
          <view class="fs28 mr10" @tap.stop="goShop(item)">{{
            item.companyName
          }}</view>
          <image
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/917de11a-2b57-48a4-868e-e8e4783bd850.png"
            class="arrow"
          ></image>
        </view>
        <view
          class="adress flex flex-between flex-baseline"
          @tap.stop="openMap(item)"
        >
          <image
            class="map-icon"
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/card-place-icon.png"
          ></image>
          <view class="oneOmit flex-1">{{ item.companyAddress }}</view>
          <text class="pl30">{{
            item.distance > 1000
              ? (item.distance / 1000).toFixed(2) + 'km'
              : item.distance + 'm'
          }}</text>
        </view>
        <view class="flex info mt20">
          <image :src="item.goodsList[0].goodsPic" class="pro-cover"></image>
          <view class="flex-1 info-right pl30">
            <view class="oneOmit fs28 fw600">{{
              item.goodsList[0].goodsName
            }}</view>
            <view class="flex flex-baseline mt20">
              <text class="color-tip fs32"
                >¥{{ item.goodsList[0].goodsPriceItem.salePrice }}</text
              >
              <text class="fs24 color-tip mr10">人</text>
              <text class="fs24 color-999"
                >门市价¥{{ item.goodsList[0].goodsPriceItem.marketPrice }}</text
              >
            </view>
            <view class="share-money color-tip fs22 mt10"
							v-if="identityType != 0"
              >分享赚{{ item.goodsList[0].goodsPriceItem.shareRebate }}</view
            >
            <view class="flex flex-between flex-bottom mt10">
              <view class="flex flex-start flex-bottom">
                <view class="slider-view rel">
                  <view
                    class="slider-rate abs"
                    :style="{
                      width:
                        ((item.goodsList[0].goodsPriceItem.allStockNum -
                          item.goodsList[0].goodsPriceItem.stockNum) /
                          item.goodsList[0].goodsPriceItem.allStockNum) *
                          100 +
                        '%',
                    }"
                  ></view>
                  <image
                    src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/937d2f23-e4de-4e60-8810-f1acfb792689.png"
                    class="abs hot"
                    :style="{
                      left:
                        ((item.goodsList[0].goodsPriceItem.allStockNum -
                          item.goodsList[0].goodsPriceItem.stockNum) /
                          item.goodsList[0].goodsPriceItem.allStockNum) *
                          100 +
                        '%',
                    }"
                  ></image>
                </view>
                <text class="fs20 color-999 ml20"
                  >已售{{
                    ((item.goodsList[0].goodsPriceItem.allStockNum -
                      item.goodsList[0].goodsPriceItem.stockNum) /
                      item.goodsList[0].goodsPriceItem.allStockNum) *
                      100 +
                    '%'
                  }}</text
                >
              </view>
              <text class="rush fs26 color-white">抢购</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <u-popup
      v-model="categoryShow"
      mode="bottom"
      border-radius="32"
      :mask-custom-style="maskCustomStyle"
    >
      <categorySeek @subParam="subParam" />
    </u-popup>
  </view>
</template>

<script>
import ProductService from '../../service/product';
import categorySeek from '../../components/categorySeek';
import app from '../../App.vue';

export default {
  components: {
    categorySeek,
  },
  data() {
    return {
      barStyle: {
        background: 'linear-gradient(180deg, #CD6CFF 0%, #FF4F3A 100%)',
      },
      maskCustomStyle: {
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
      },
      tabs: [
        {
          name: '全部',
          typeId: null,
        },
        {
          name: '剧本',
          typeId: 2,
        },
        {
          name: '密室',
          typeId: 3,
        },
        {
          name: '酒吧',
          typeId: 1,
        },
      ],
      current: 0,
      supplierId: '',
      seckillList: [],
      isHome: false,
      hour: 0,
      minute: 0,
      sec: 0,
      interId: null,
      categoryShow: false,
      total: 0,
      typeId: null,
      showSelect: false,
      query: {
        supplierId: null,
        pageNum: 1,
        pageSize: 10,
        sortType: '',
      },
    };
  },
	computed:{
		identityType(){
			return this.$store.state.user.identityType
		}
	},
  onLoad(option) {
    this.$eventRecord(255);
    if (option.isHome) this.isHome = option.isHome;
    if (option.showSelect) this.showSelect = option.showSelect;
    if (option.supplierId) {
      this.query.supplierId = option.supplierId;
    }
    if (uni.getStorageSync('userInfo') && uni.getStorageSync('userInfo').head) {
      this.getSeckillList();
      this.countDown();
    } else {
      uni.navigateTo({
        url: `/pagesA/user/login?isOnload=${true}`,
      });
    }
		this.$store.commit('setIdentityType');
  },
  onReachBottom() {
    if (this.seckillList.length < this.total) {
      this.query.pageNum++;
      this.getSeckillList();
    }
  },
  onUnload() {
    if (this.interId) {
      clearInterval(this.interId);
    }
  },
  methods: {
    openMap(item) {
      uni.openLocation({
        latitude: Number(item.lat),
        longitude: Number(item.lng),
        name: item.companyAddress,
      });
    },
    jump(item) {
      uni.navigateTo({
        url: `/pagesB/commodity/sInfo?id=${
          item.goodsId
        }&isSeckill=${true}&supplierId=${item.supplierId}`,
      });
    },
    goShop(item) {
      uni.navigateTo({
        url: `/pagesB/commodity/index?typeId=${item.typeId}&supplierId=${item.supplierId}`,
      });
    },
    back() {
      uni.navigateBack();
    },
    goHome() {
      uni.redirectTo({
        url: '/pages/index/index',
      });
    },
    // countPlan(item){
    // 	console.log(item)
    // 	return item.allStockNum ? (item.allStockNum / item.stockNum) + '%' : (0 / item.stockNum) + '%'
    // },
    countDown() {
      const now = new Date();
      const nextD = new Date(
        `${now.getFullYear()}/${now.getMonth() + 1}/${
          now.getDate() + 1
        } 00:00:00`
      ).getTime();
      this.interId = setInterval(() => {
        const nowD = new Date().getTime();
        const temsp = (nextD - nowD) / 1000;
        this.hour = parseInt(temsp / (60 * 60));
        this.minute = parseInt((temsp - this.hour * 3600) / 60);
        this.sec = parseInt(temsp - (this.hour * 3600 + this.minute * 60));
        if (this.hour == 0 && this.minute == 0 && this.sec == 0) {
          clearInterval(this.interId);
          this.seckillList = [];
          this.getSeckillList();
          this.countDown();
        }
      }, 1000);
    },
    async getSeckillList() {
      uni.showLoading();
      if (app.globalData.latitude) {
        this.query.lat = app.globalData.latitude;
        this.query.lng = app.globalData.longitude;
      } else {
        const data = await app.methods.getLocation();
				const latitude = data.latitude || 28.22778
				const longitude = data.longitude || 112.93886
				app.globalData.latitude = latitude;
				app.globalData.longitude = longitude;
        this.query.lat = latitude;
        this.query.lng = longitude;
      }
      this.query.typeId = this.typeId;
      ProductService.seckillList(this.query)
        .then((res) => {
          uni.hideLoading();
          this.seckillList = this.seckillList.concat(res.list);
          this.total = res.total;
        })
        .catch((err) => {
          uni.hideLoading();
          if (err.code == 500101) {
            if (!this.isAgain) {
              this.isAgain = true;
              LoginService.defLogin();
              this.getSeckillList();
            } else {
              uni.showLoading();
              setTimeout(() => {
                this.getSeckillList();
                uni.hideLoading();
              }, 1000);
            }
          }
        });
    },
    changeTab(val) {
      this.typeId = this.tabs[val].typeId;
      this.query.pageNum = 1;
      this.seckillList = [];
      this.status = 'loadmore';
      this.current = val;
      this.getSeckillList();
    },
    subParam(data) {
      this.seckillList = [];
      this.query.sortType = data.sortType || '';
      this.query.pageNum = 1;
      this.categoryShow = false;
      this.getSeckillList();
    },
  },
};
</script>

<style>
page {
  background-color: #eee;
}
</style>
<style lang="scss" scoped>
.back-icon {
  width: 44upx;
  height: 44upx;
  top: 60upx;
  left: 40upx;
  border-radius: 50%;
  position: fixed;
  z-index: 99;
}
.head {
  height: 288upx;
  background: linear-gradient(to right, #f19a60, #e04e4e);
  padding-top: 1px;
  .seckill {
    width: 266upx;
    height: 122upx;
  }
  .down-icon {
    width: 70upx;
    height: 40upx;
    line-height: 40upx;
    text-align: center;
    background: #fff;
    color: #e46152;
    border-radius: 6upx;
    font-size: 24upx;
  }
}
.tab-wrapper {
  // padding: 26upx 40upx 20upx 40upx;
  position: relative;
}
.more-btn {
  position: absolute;
  right: 24rpx;
  top: 10rpx;
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
.list {
  padding: 1px 30upx;
  background: #eee;
  padding-bottom: 20upx;
  .item {
    background: #fff;
    border-radius: 16upx;
    padding: 30upx;
    .arrow {
      width: 15upx;
      height: 25upx;
    }
    .adress {
      padding: 4upx 16upx;
      margin-top: 25upx;
      background: #ececec;
      border-radius: 60upx;
      font-size: 26upx;
      .map-icon {
        height: 24upx;
        width: 24upx;
        margin-top: 4upx;
        margin-right: 6upx;
      }
      color: #000000;
    }
    .info {
      .pro-cover {
        border-radius: 12upx;
        width: 200upx;
        height: 200upx;
      }
      .info-right {
        .share-money {
          border: 1px solid #ff4f3a;
          padding: 2upx 10upx;
          border-radius: 6upx;
          display: inline-block;
        }
        .slider-view {
          background: #ffd8d8;
          width: 124upx;
          height: 12upx;
          border-radius: 6upx;
          .slider-rate {
            background: #ff4f3a;
            height: 12upx;
            top: 0;
            left: 0;
            border-radius: 6upx;
            width: 50%;
          }
          .hot {
            width: 26upx;
            height: 32upx;
            bottom: -4upx;
            left: 50%;
            margin-left: -13upx;
          }
        }
        .rush {
          padding: 6upx 30upx;
          border-radius: 30upx;
          background: #ff4f3a;
        }
      }
    }
  }
}
</style>
