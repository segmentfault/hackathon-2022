<template>
  <view>
    <u-sticky>
			<!-- 只能有一个根元素 -->
			<view class="sticky">
				<u-search placeholder="请输入商家名字" 
        v-model="query.supplierName"  
        height="70" 
        :action-style="actionStyle"
         @search="search()"
         @custom="search()"
        ></u-search>
			</view>
		</u-sticky>
    <view
      class="activity-discount-center merchan-sites"
      v-if="cardList.length || cardList1.length"
    >
      <card
        v-model="activeTab"
        style="width: 100%"
        :tabs="[]"
        :cardList="cardList"
        @loadMore="getList"
        :activeIndex="activeSupplierId"
        @handlerDetail="handlerDetail"
        @handlerChange="handlerChange"
        :status="status"
        :query="query"
      >
        <!-- <view name="tag" class="tag" v-if="query.activityId">
          <image
            style="width: 32rpx; height: 32rpx; margin-right: 8rpx"
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/0021151a-d428-48d0-8fcc-4d81fdb0cdbb.png"
          ></image>
          <view>已展示参与“{{ time }} 日半价活动”全部商家</view>
        </view> -->
      </card>
      <!-- 目前先这样，后续优化 -->
      <view class="footer" v-show="showCard()">
        <!-- <view class="title">—— 不参与活动的入驻商家 ——</view> -->
        <card
          style="width: 100%"
          :tabs="[]"
          :cardList="cardList1"
          :activeIndex="activeSupplierId"
          :status="status"
          @handlerDetail="handlerDetail"
          @handlerChange="handlerChange"
          :query="query"
        >
        </card>
      </view>
    </view>
    <view class="no-data" v-else>
      <noData />
    </view>
    <view class="btn-wrapper">
      <view class="btn" @click="openLoaction()">
        <view class="text">其他地点</view>
        <view class="desc">想去哪玩儿就去哪玩儿</view>
      </view>
    </view>
  </view>
</template>

<script>
import gameService from '../../service/game.js';
import card from './components/card.vue';
import noData from '../components//default';
export default {
  data() {
    return {
      status: 'loadMore',
      activeSupplierId: '-1',
      cardList: [],
      cardList1: [],
      time: '',
      query: {
        pageNum: 1,
        pageSize: 10,
        activityId: null,
        typeId: null,
        supplierName: null,
      },
    };
  },
  computed: {
    actionStyle() {
      return {
        color: '#fff',
        width: '160rpx',
        height: '64rpx',
        background: '#000000',
        'border-radius': '34rpx',
        'margin-left': '40upx',
        'font-size': '26rpx',
        'line-height': '64rpx',
        }
    }
  },
  onLoad(params = {}) {
    const time = params.time.replaceAll('-', '.');
    this.time = time.substr(time.length - 5, time.length);
    // this.query.activityId = params.activityId || '';
    this.query.typeId = params.typeId || null;
    this.activeSupplierId = params.supplierId || null;
    this.getList();
  },
  components: {
    card,
    noData,
  },
  onReachBottom() {
    if (this.status === 'noDataMore') return;
    this.query.pageNum++;
    this.getList();
  },
  methods: {
    search() {
      this.cardList = []
      this.cardList1= []
      this.getList()
    },
    handlerDetail(item) {
      let url = `/pagesA/businessDetail/index?supplierId=${item.supplierId}&typeId=${item.typeId}`;
      uni.navigateTo({
        url: url,
      });
    },
    init(params) {
      this.status === 'loadMore';
      this.query.pageNum = 1;
      this.cardList = [];
      this.cardList1 = [];
      this.getList(params);
    },
    openLoaction() {
      const _this = this;
      // 获取其他位置
      uni.chooseLocation({
        success: function (res) {
          _this.goBack({
            supplierId: null,
            companyAddress: res.address,
            companyName: null,
            lat: res.latitude,
            lng: res.longitude,
            typeId: null,
          });
        },
      });
    },
    showCard() {
      return this.cardList1.length;
    },
    goBack(item) {
      const pages = getCurrentPages();
      const prevPage = pages[pages.length - 2];
      uni.navigateBack();
      prevPage.onLoad({
        supplierId: item.supplierId || null,
        address: item.companyAddress,
        companyName: item.companyName || null,
        lat: item.lat,
        lng: item.lng,
        discountPrice: this.query.activityId
          ? item.discountPrice || null
          : item.originalPrice || item.discountPrice || null,
        typeId: item.typeId || null,
        activityId: item.activityId || null,
        cover: item.cover || null,
        back: 'back',
      });
    },
    handlerChange(item, index) {
      this.activeSupplierId = item.supplierId;
      setTimeout(() => { 
        const _this = this;
        if (item.activityId && _this.query.activityId) {
          this.activeSupplierId = item.supplierId;
          this.goBack(item);
        } else if (!item.activityId && !_this.query.activityId)  { 
          this.activeSupplierId = item.supplierId;
          this.goBack(item);
        } else {
          uni.showModal({
            content: '此商家不参与本次活动哦！',
            cancelText: '我再想想', // 取消按钮的文字
            confirmText: '确认', // 确认按钮文字
            success(res) {
              if (res.confirm) {
                _this.activeSupplierId = item.supplierId;
                _this.goBack(item);
              }
            },
          });
        }
      }, 500)
    },
    getList(params = {}) {
      if (params.status) this.status = params.status;
      if (params.cardList) {
        this.cardList = [];
        this.cardList1 = [];
        this.activeSupplierId = '';
      }
      const query = {
        ...this.query,
        ...params,
        supplierName: this.query.supplierName || null,
        includesAll: 1,
      };
      gameService.getSupplierList(query).then((res) => {
        if (res && res.length) {
          res.forEach((v) => {
            if (Array.isArray(v.cover)) {
              v.cover = this.$pictureUrl(v.cover[0], 49, 49);
            } else {
              v.cover = this.$pictureUrl(v.cover, 49, 49);
            }
          });
          const cardList = res.filter(
            (item) => item && item.hasOwnProperty('activityId')
          );
          const cardList1 = res.filter(
            (item) => item && !item.hasOwnProperty('activityId')
          );
          if (cardList.length) this.cardList.push(...cardList);
          if (cardList1.length) this.cardList1.push(...cardList1);
        } else {
          this.status = 'noDataMore';
        }
      });
    },
  },
  onPullDownRefresh() {
    this.init();
  },
};
</script>

<style lang="scss" scoped>
.activity-discount-center {
  min-height: 100vh;
  // padding-top: 32rpx;
  padding-bottom: 32rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #f5f5f5;
  padding-bottom: 200upx;
  .header {
    align-items: center;
    display: flex;
    font-size: 28upx;
    font-weight: 600;
    color: #fff;
    .logo {
      width: 68upx;
      height: 68upx;
      margin-right: 10upx;
      margin-left: 42upx;
    }
  }
}
.tag {
  width: 496upx;
  height: 48upx;
  background: #ffd8d8;
  font-size: 24upx;
  color: #ff4747;
  font-weight: 500;
  border-radius: 30upx;
  margin: 0 auto;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-content: center;
  padding-top: 8rpx;
}
.footer {
  width: 100%;
  margin-top: 20rpx;
  .title {
    color: #666666;
    text-align: center;
  }
}
.btn-wrapper {
  position: fixed;
  left: 0;
  right: 0;
  height: 132upx;
  bottom: 0;
  background: #fff;
}
.btn {
  width: 600upx;
  height: 100upx;
  border-radius: 48upx;
  border: 2upx solid #000000;
  margin: 0 auto;
  background: #fff;
  margin-bottom: 16upx;
  margin-top: 16upx;
  display: flex;
  flex-direction: column;
  align-content: center;
  justify-content: center;
  text-align: center;
  > .text {
    font-size: 36upx;
    color: #000000;
  }
  > .desc {
    font-size: 20upx;
    color: #000000 ;
  }
}
.sticky { 
  padding: 22rpx 42rpx;
  background: #fff;
  border-bottom: 2upx solid #eaecef;
}
</style>
