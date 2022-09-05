<template>
  <view class="seek-wrapper">
    <u-sticky v-if="searchWidth">
      <view class="head-search" :style="{ paddingTop: searchMarginTop + 'px' }">
        <view class="find">发现</view>
        <view
          class="search"
          :style="{ width: searchWidth + 'px', height: searchHeight + 'px' }"
          >
          <d-search :type="2" :kewords="keywords"/>
        </view>
      </view>
    </u-sticky>

    <!-- 轮播图 -->
    <view class="banner-bg">
      <swiper
        class="swiper-bg"
        v-if="banner && banner.length"
        indicatorDots
        indicator-color="rgba(0,0,0,.5)"
        indicator-active-color="#000000"
        autoplay
        interval="3000"
        circular
      >
        <swiper-item
          class="swiper-item"
          v-for="(item, index) in banner"
          :key="index"
        >
          <view class="swiper-item" @click="clickBanner(item)">
            <image :src="item.picUrl" />
          </view>
        </swiper-item>
      </swiper>
    </view>
    <!--秒杀-->
    <view class="seckill" v-if="kilist.length">
      <view class="box">
        <view class="seckill-top">
          <view class="left"
            ><img
              src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/dba68f38-1c8a-48ac-9e2b-e165ea2abe4d.png"
              alt=""
          /></view>
          <view class="right" @tap="more">更多＞</view>
        </view>
        <view class="seckill-bottom">
          <d-swiper :list="kilist" height="250" :slots="false" nextMargin="370">
            <template v-slot="{ data }">
              <view class="info" @tap="jumpDetail(data)">
                <img
                  style="with: 274upx; height: 274upx"
                  :src="data.goodsList[0].goodsPic"
                  alt=""
                />
                <view class="txt">{{ data.goodsList[0].goodsName }}</view>
                <view class="discount"
                  >￥{{ data.goodsList[0].goodsPriceItem.salePrice
                  }}<view class="cell-bottom-desc-tag"
										v-if="data.identityType != 0"
                    >分享赚 ¥
                    {{ data.goodsList[0].goodsPriceItem.shareRebate }}</view
                  >
                </view>
                <view class="price" decode="true"
                  >门市价&nbsp;￥{{
                    data.goodsList[0].goodsPriceItem.marketPrice
                  }}</view
                >
                <view class="btn">
                  <view class="progressBar">
                    <u-line-progress
                      active-color="#FF4F3A"
                      height="12"
                      :percent="
                        ((data.goodsList[0].goodsPriceItem.allStockNum -
                          data.goodsList[0].goodsPriceItem.stockNum) /
                          data.goodsList[0].goodsPriceItem.allStockNum) *
                        100
                      "
                    >
                      <slot>
                        <image
                          class="fire-icon"
                          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/fire.png"
                          mode=""
                          :style="{
                            paddingLeft:
                              ((data.goodsList[0].goodsPriceItem.allStockNum -
                                data.goodsList[0].goodsPriceItem.stockNum) /
                                data.goodsList[0].goodsPriceItem.allStockNum) *
                                100 +
                              10 +
                              'rpx',
                          }"
                        ></image>
                      </slot>
                    </u-line-progress>
                  </view>
                  <button class="btn1">抢购</button></view
                >
              </view>
            </template>
          </d-swiper>
        </view>
      </view>
    </view>
    <!-- tab -->
    <view class="tab-wrapper" v-if="tabs && tabs.length">
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
      <view class="more-btn" @click="openCategoryShow()">
        <view class="more-btn-bg">
          筛选
          <image
            class="more-icon"
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/6042326b-84d6-447d-9f2e-30bf09dd9687.png"
          ></image>
        </view>
      </view>
    </view>

    <view class="card-box">
      <u-card
        :show-head="false"
        class="card-wrapper"
        v-for="(item, index) in list"
        :key="index"
      >
        <view slot="body">
          <view
            class="card-item"
            style="margin-bottom: 54upx"
            @click="handerOpen(item, 1)"
          >
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
              @click.stop="handlerOpenInfo(target)"
              v-for="(target, i) in item.goodsList"
              v-show="parseInt(i) < 2"
              :key="i"
            >
              <view class="cell-bottom-left">
                <view class="bottom-price"
                  >¥ {{ target.goodsPriceItem.salePrice }}</view
                >
                <view class="bottom-sale"
                  >门市价 ¥ {{ target.goodsPriceItem.marketPrice }}</view
                >
              </view>
              <view class="cell-bottom-right">
                <view class="cell-bottom-name"> {{ target.goodsName }}</view>
                <view class="cell-bottom-desc">
                  <view class="cell-bottom-desc-num"
                    >剩余 {{ target.goodsPriceItem.stockNum }}</view
                  >
                  <view class="cell-bottom-desc-tag"
										v-if="identityType != 0"
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
    <u-popup
      v-model="categoryShow"
      mode="bottom"
      border-radius="32"
      :mask-custom-style="maskCustomStyle"
    >
      <categorySeek @subParam="subParam" />
    </u-popup>
    <!-- 底部导航栏 -->
    <view-tabbar v-model="tableIndex"></view-tabbar>
		<view class="pop-shade" v-if="showOpenstting">
			<view class="pop-body">
				<view class="content">按距离筛选需要使用定位功能</view>
				<view class="flx foot">
					<view @click="showOpenstting = false">取消</view>
					<button open-type="openSetting" @opensetting="openSetting">确定</button>
				</view>
			</view>
		</view>
  </view>
</template>
<script>
import dSearch from '@/components/d-search.vue';
import pageMix from '@/framework/mixins/pageMix';
import dSwiper from '@/components/d-swiper.vue';
import homeService from '@/service/home.js';
import categorySeek from '@/components/categorySeek';
import productService from '@/service/product.js';
import app from '../../App.vue';
import IM from '../im/lib/mixin';

let params = {};

export default {
  mixins: [pageMix, IM],
  components: {
    dSearch,
    dSwiper,
    categorySeek,
  },
  data() {
    return {
			showOpenstting:false,
      menuButtonInfo: {},
      searchMarginTop: '',
      searchWidth: 0,
      searchHeight: 0,
      height: 0,
      categoryShow: false,
      tableIndex: 1,
      status: 'loadmore',
      maskCustomStyle: {
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
      },
      barStyle: {
        background: 'linear-gradient(180deg, #CD6CFF 0%, #FF4F3A 100%)',
      },
      banner: [], // 轮播图
      list: [],
      kilist: [],
      current: 0,
      tabs: [],
      typeId: '',
      typeId1: '',
      query: {
        pageNum: 1,
        pageSize: 20,
        sortType: '',
        keywords: null,
        // activityId: 4,
      },
      query1: {
        pageNum: 1,
        pageSize: 20,
        sortType: '',
        keywords: '',
      },
      keywords: null,
    };
  },
  watch: {
    myUserId: {
      handler(val) {
        if (val) {
          this.totalUnreadCount();
        }
      },
      immediate: true,
      deep: true,
    },
  },
  computed: {
    actionStyle() {
      return {
        color: '#171C33',
        width: '160rpx',
        height: '64rpx',
        background: '#FFFFFF',
        'border-radius': '34rpx',
        'margin-left': '20upx',
        'font-size': '26rpx',
        'line-height': '64rpx',
      };
    },
		identityType(){
			return this.$store.state.user.identityType
		},
    inputStyle() {
      return {
        'min-height': '42rpx',
        'line-height': '42rpx',
        'font-size': '28rpx',
        background: '#FFFFFF',
      };
    },
  },
  async onLoad(options) {
    // if (!uni.getStorageSync('userInfo')) {
    //   this.$toast('请先登录');
    //   setTimeout(() => {
    //     uni.navigateTo({
    //       url: `/pagesA/user/login?isOnload=${true}`,
    //     });
    //   });
    //   return;
    // }
		if(!app.globalData.latitude){
			const data = await app.methods.getLocation();
			const latitude = data.latitude || 28.22778
			const longitude = data.longitude || 112.93886
			app.globalData.latitude = latitude;
			app.globalData.longitude = longitude;
		}
    this.$eventRecord(174);
    this.getTrackingID();
    this.menuButtonInfo = uni.getMenuButtonBoundingClientRect();
    const { top, width, height, right } = this.menuButtonInfo;
    uni.getSystemInfo({
      success: (res) => {
        const { statusBarHeight } = res;
        const margin = top - statusBarHeight;
        this.searchMarginTop = statusBarHeight + margin;
        this.searchHeight = height;
        this.searchWidth = right - 2 * width;
      },
    });
    this.keywords = options.keywords || null;
    this.init();
    if(this.allConversation.length) { 
      this.queryHisConversation(this.allConversation)
    }
		this.$store.commit('setIdentityType');
  },
  // onShow() {
  //   this.refershParams();
  // },
  onReachBottom() {
    this.loadMore();
  },
  async onPullDownRefresh() {
    this.query.pageNum = 1;
    this.list = [];
    this.kilist = [];
    await this.init();
  },
  methods: {
    // 页面刷新初始化参数
    // refershParams() {
    //   console.log('页面刷新重置数据');
    // },
    openCategoryShow() {
      this.$eventRecord(236);
      this.categoryShow = true;
    },
    more() {
			const isAuth = this.$authorization()
			if(!isAuth) return
      uni.navigateTo({
        url: `/pagesB/commodity/seckillList`,
      });
    },
    jumpDetail(item) {
			const isAuth = this.$authorization()
			if(!isAuth) return
      uni.navigateTo({
        url: `/pagesB/commodity/sInfo?id=${
          item.goodsId
        }&isSeckill=${true}&supplierId=${item.supplierId}`,
      });
    },
    handerOpen(item, tag) {
			const isAuth = this.$authorization()
			if(!isAuth) return
      if (tag === 1) {
        this.$eventRecord(237);
      } else {
        this.$eventRecord(239);
      }
      uni.navigateTo({
        url: `/pagesB/commodity/index?typeId=${item.typeId}&supplierId=${item.supplierId}`,
      });
    },
    handlerOpenInfo(row) {
			const isAuth = this.$authorization()
			if(!isAuth) return
      this.$eventRecord(238);
      uni.navigateTo({
        url: `/pagesB/commodity/sInfo?id=${row.id}&supplierId=${row.supplierId}`,
      });
    },
		openSetting(e){
			this.showOpenstting = false
			if(e.detail.authSetting['scope.userLocation']){
				this.subParam(params)
			}
		},
    async subParam(e) {
			const data = await app.methods.getLocation()
			if(data.errMsg == "getLocation:fail auth deny"){
				this.showOpenstting = true
				params = e
			}else{
				console.log(data)
				const latitude = data.latitude || 28.22778
				const longitude = data.longitude || 112.93886
				app.globalData.latitude = latitude;
				app.globalData.longitude = longitude;
			}
      this.list = [];
      this.query.sortType = e.sortType || '';
      this.query.pageNum = 1;
      this.query.keywords = '';
      this.categoryShow = false;
      this.getList();
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
    search() {
      this.list = [];
      this.query.pageNum = 1;
      this.typeId = null;
      this.current = 0;
      this.getList();
      // this.cardList = []
      // this.cardList1= []
    },
    loadMore() {
      if (this.list.length >= this.query.pageNum * this.query.pageSize) {
        this.query.pageNum++;
        this.getList();
      }
    },
    //点击banner
    async clickBanner(item) {
			const isAuth = this.$authorization()
			if(!isAuth) return
      let activityId = item.jumpUrl.split('?')[1];
      this.$u.throttle(() => {
        uni.navigateTo({
          url: `${item.jumpUrl}?activityId=${activityId}`,
        });
        // 埋点
        this.$eventRecord(152);
      }, 1000);
    },
    handlerDetail(item) {
			const isAuth = this.$authorization()
			if(!isAuth) return
      this.$eventRecord(156);
      uni.navigateTo({
        url: `/pagesA/businessDetail/index?supplierId=${item.supplierId}&typeId=${item.typeId}`,
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
    async getList(params) {
      if (this.isLoading) return;
      this.isLoading = true;
      const query = {
        ...this.query,
        ...params,
        typeId: this.typeId,
        keywords: this.keywords || null,
				lat:app.globalData.latitude,
				lng:app.globalData.longitude
      };
      if (!query.keywords) delete query.keywords;
      if (!query.typeId) delete query.typeId;
      if (this.keywords) query.keywords = this.keywords;
      productService
        .getGoodsFindPage(query)
        .then((res) => {
          this.isLoading = false;
          res = res.list || [];
          res.forEach((v) => {
            this.$pictureUrl(v.cover, 50, 50);
          });
          this.list = this.list.concat(res);
          if (
            !res.length ||
            this.list.length < this.query.pageNum * this.query.pageSize
          ) {
            this.status = 'nomore';
          } else {
            this.status = 'loadmore';
          }
        })
        .catch((err) => (this.isLoading = false));
    },
    async getkiList(params) {
      const query = {
        ...this.query1,
        ...params,
        typeId: this.typeId,
				lat:app.globalData.latitude,
				lng:app.globalData.longitude
      };
      productService.getSeckillPage(query).then((res) => {
        res = res.list || [];
        res.forEach(item => item.identityType = this.identityType);
        this.kilist = [];
        this.kilist = this.kilist.concat(res);
      });
    },
    handlerCard(item) {
      uni.navigateTo({
        url: item.url,
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
    changeTab(val) {
      this.typeId = this.tabs[val].typeId;
      this.query.pageNum = 1;
      this.list = [];
      this.status = 'loadmore';
      this.query.keywords = '';
      this.keywords = null;
      this.$eventRecord(155);
      this.current = val;
      this.getList();
    },
    initTabs() {
      this.tabs = [
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
        // {
        //   name: '公益',
        //   typeId: 4,
        // },
      ];
    },
    init() {
      this.list = [];
      this.initTabs();
      this.getList();
      this.getkiList();
      this.getBannerList();
    },
    // banner
    async getBannerList() {
      let res = await homeService.bannerListBuy();
      let banner = res || [];
      // 图片压缩居中裁剪
      banner.forEach((e) => {
        e.picUrl += '?imageView2/1/w/670/h/220/q/80/webp';
      });
      this.banner = banner;
    },
  },
  onShareAppMessage(res) {
    return {
      title: '来拼叭玩剧本/密逃/酒吧，时时拼车，天天打折！',
      path: `pages/index/index?shareUserId=${this.myUserId}&tab=2`,
      imageUrl:
        'https://image-1306191496.cos.ap-nanjing.myqcloud.com/087478f7-fddf-4538-ba72-e95218dee63c.png',
    };
  },
};
</script>
<style lang="scss" scoped>
.seek-wrapper {
  .head-search {
    display: flex;
    align-items: center;
    padding-bottom: 20upx;
    background: #ffff;
    .find {
      padding-left: 46upx;
      font-size: 40upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
    }
    .search {
      width: 300upx;
      margin-left: 20upx;
    }
  }

  .banner-bg {
    padding: 0upx 40upx 0upx 40upx;
    width: 100%;
    .swiper-bg {
      margin-top: 30upx;
      height: 220upx;
      width: 100%;
      .swiper-item {
        width: 100%;
        height: 100%;
        border-radius: 32upx;
        position: relative;
        overflow: hidden;
        image {
          height: 100%;
          width: 100%;
        }
      }
    }
  }
  .seckill {
    display: flex;
    margin-top: 24upx;
    background: linear-gradient(180deg, #ffc4c4 0%, #ffffff 100%);
    box-shadow: 0px 4px 76px 0px rgba(0, 0, 0, 0.1);
    border-radius: 16upx;
    .box {
      display: flex;
      flex-direction: column;
      margin: 40upx 22upx 10upx 22upx;
      background: #ffffff;
      width: 100%;
      border-radius: 16upx;
      border: 2upx solid #ffffff;
      .seckill-top {
        display: flex;
        justify-content: space-between;
        .left {
          img {
            padding-left: 53upx;
            padding-top: 42upx;
            width: 182upx;
            height: 60upx;
          }
        }
        .right {
          margin-top: 50upx;
          margin-right: 54upx;
          padding-left: 12rpx;
          padding-right: 6rpx;
          border-radius: 34upx;
          border: 2px solid #979797;
          height: 44upx;
          font-size: 26upx;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #999999;
        }
      }
      .seckill-bottom {
      }
    }
  }

  .tab-wrapper {
    padding: 26upx 0;
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
.info {
  margin-left: 54upx;
  img {
    width: 274upx;
    height: 274upx;
    border-radius: 16px;
  }
  .txt {
    padding-top: 12upx;
    font-size: 26upx;
    width: 276upx;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #000000;
    line-height: 36upx;
  }
  .discount {
    display: flex;
    margin-top: 16upx;
    font-size: 32upx;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #ff4f3a;
    line-height: 44upx;
    .cell-bottom-desc-tag {
      margin-left: 12upx;
      margin-top: 6rpx;
      height: 32upx;
      line-height: 32upx;
      font-size: 22upx;
      font-weight: bold;
      color: #ff4f3a;
      padding: 0 12upx;
      border-radius: 8upx;
      border: 2upx solid #ff4f3a;
    }
  }
  .price {
    padding-top: 6upx;
    font-size: 24upx;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #999999;
    line-height: 34upx;
  }
  .btn {
    display: flex;
    justify-content: space-between;
    .progressBar {
      width: 124upx;
      .fire-icon {
        position: fixed;
        padding-bottom: 14rpx;
        height: 28upx;
        width: 22upx;
      }
    }
    .btn1 {
      padding-top: 6upx;
      padding-bottom: 6upx;
      background: #ff4f3a;
      border-radius: 34px;
      font-size: 26upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #ffffff;
      line-height: 36upx;
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
	.pop-shade{
		position: fixed;
		top:0;
		left:0;
		right:0;
		bottom:0;
		background: rgba(0,0,0,.6);
		z-index: 999;
		.pop-body{
			text-align: center;
			position: absolute;
			background: #fff;
			width: 80%;
			height:300upx;
			left:10%;
			top:50%;
			margin-top: -150upx;
			border-radius: 12upx;
			.content{
				padding:0 20upx;
				height:200upx;
				display: flex;
				align-items: center;
				justify-content: center;
			}
			.foot{
				display: flex;
				height:98upx;
				line-height: 98upx;
				border-top:1px solid #eee;
				view,button{
					width: 50%;
				}
				button{
					font-size: 16px;
					line-height: 100upx;
					font-weight: bold;
					background: transparent;
					border:0;
					font-size:16px;
					font-weight: 400;
					border-left:1px solid #eee;
					border-radius: 0;
				}
				button::after{
					border:0;
					border-radius: 0;
				}
			}
		}
	}
</style>
