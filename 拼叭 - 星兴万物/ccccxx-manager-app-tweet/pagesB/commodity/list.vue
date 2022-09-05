<template>
  <view>
    <view class="activity-discount-center merchan-sites hot-script">
      <view class="header">
        <u-search
          v-model="query.goodsName"
          input-align="left"
          height="70"
          :action-style="actionStyle"
          :input-style="inputStyle"
          @search="search()"
          @custom="search()"
        ></u-search>
      </view>
      <card
        v-model="query.label"
        :class="[{ mt120: params.typeId == 1 }, 'card-wrapper']"
        :tabs="tabs"
        :cardList="cardList"
        @loadMore="getList"
        :activeId="activeId"
        @handlerDetail="handlerDetail"
        @handlerChange="handlerChange"
        :status="status"
        :query="query"
        :isSelect="isSelect"
      >
        <view class="card-tabs" v-if="params.typeId != 1">
          <view class="name">人数</view>
          <view
            class="card-tab"
            v-for="(item, index) in tabs1"
            :key="index"
            :class="{ active: query.peopleNum === item.label }"
            @click="handlerTab(item, index)"
            >{{ item.label }}</view
          >
        </view>
      </card>
    </view>
    <!-- 目前先这样，后续优化 -->
    <!-- 目前先这样，后续优化 -->
    <!--      <view class="footer-hot" :class="{ mtop: !cardList.length }" v-show="showCard()">
        <view class="title">以下剧本不在商家支持范围！您也可以打电话咨询商家是否更新该剧本</view>
        <card
          style="width: 100%"
          :tabs="[]"
          :isTop="false"
          :cardList="cardList1"
          :activeId="activeId"
          :status="status"
          @handlerDetail="handlerDetail"
          @handlerChange="handlerChange"
          :query="query"
          :isSelect="isSelect"
        ></card>
      </view>
      <view class="no-data" v-if="!cardList1.length && !cardList.length">
        <noData />
      </view>
    </view>-->
    <!--    <view class="btn-wrapper" v-if="!isCreate">
      <view class="btn" @click="handlerShow()">
        <view class="text">{{params.typeId == 2 ? '没有找到剧本' : '没找到合适的主题'}},手动输入标题</view>
      </view>
    </view>-->
    <!--    <prompt
      :isMutipleLine="false"
      :visible.sync="promptVisible"
      title="标题"
      placeholder="请输入剧本标题"
      :defaultValue="title"
      @confirm="clickPromptConfirm"
    ></prompt>-->
  </view>
</template>

<script>
import documentService from '../../service/document.js';
import ProductService from '../../service/product';
import card from './components/card.vue';
import noData from '../components/default';
import prompt from '../../components/zz-prompt/index';
export default {
  data() {
    return {
      activeId: -1,
      // promptVisible: false,
      tabs: [],
      tabs1: [
        { label: '全部' },
        { label: '6人以下' },
        { label: '6人' },
        { label: '7人以上' },
      ],
      title: '',
      cardList: [],
      // cardList1: [],
      query: {
        pageNum: 1,
        pageSize: 10,
        peopleNum: '',
        labelId: null,
        goodsName: '',
        // typeId: '',
      },
      isCreate: null,
      params: {
        // typeId: null,
        supplierId: null,
      },
      total: 0,
      isSelect: false,
    };
  },
  onLoad(options) {
    if (options.isSelect) this.isSelect = Boolean(options.isSelect);
    this.supplierId = parseInt(options.supplierId);
    this.isCreate = options.isCreate || null;
    // this.params.typeId = options.typeId || null;
    uni.setNavigationBarTitle({
      title:
        options.typeId == 2
          ? '选择剧本'
          : options.typeId == 2
          ? '选择套餐'
          : '选择主题',
    });
    this.params.supplierId = parseInt(options.supplierId) || null;
    this.getList();
    if (options.typeId == 2) {
      this.getScriptLabel();
    } else if (options.typeId == 3) {
      this.getPenetraliumTypeList();
    }
  },
  components: {
    card,
    noData,
    prompt,
  },
  onReachBottom() {
    if (this.cardList.length < this.total) {
      this.query.pageNum++;
      this.getList();
    }
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
    inputStyle() {
      return {
        'min-height': '42rpx',
        'line-height': '42rpx',
        'font-size': '28rpx',
        background: '#FFFFFF',
      };
    },
  },
  methods: {
    getScriptLabel() {
      documentService.scriptTypeList().then((res) => {
        this.tabs = res;
        this.tabs.unshift({ labelId: null, id: 'all' });
      });
    },
    getPenetraliumTypeList() {
      documentService.penetraliumTypeList().then((res) => {
        this.tabs = res;
        this.tabs.unshift({ labelId: null, id: 'all' });
      });
    },
    // handlerShow() {
    //   this.promptVisible = true
    // },
    // clickPromptConfirm(val) {
    //   this.title = val
    //   this.promptVisible = false
    //   this.goBack({ name: val })
    // },
    // showCard() {
    //   return this.cardList1.length
    // },
    search() {
      this.cardList = [];
      this.cardList1 = [];
      this.query.pageNum = 1;
      this.getList({ labelId: null, peopleNum: 'all' });
    },
    handlerTab(item, index) {
      this.cardList = [];
      this.cardList1 = [];
      this.query.peopleNum = item.labelId;
      this.getList({ pageNum: 1, status: 'loading', cardList: [] });
    },
    handlerChange(item) {
      this.activeId = item.id;
      setTimeout(() => {
        // this.goBack(item)
      }, 100);
    },
    // goBack(item) {
    //   const pages = getCurrentPages()
    //   const prevPage = pages[pages.length - 2]
    //   uni.navigateBack()
    //   prevPage.onLoad({
    //     businessId: item.id || null,
    //     businessName: item.name,
    //     backTheme: 'back',
    //     minPeople: item.minPeople || item.people || 2,
    //   })
    // },
    handlerDetail(item) {
      uni.navigateTo({
        url: `/pagesB/commodity/sInfo?typeId=${this.params.typeId}&id=${item.id}&supplierId=${this.supplierId}`,
      });
    },
    getList(params = {}) {
      if (params.status) this.status = params.status;
      if (params.cardList && Array.isArray(params.cardList))
        this.cardList = params.cardList;
      if (params.cardList1 && Array.isArray(params.cardList1))
        this.cardList1 = params.cardList1;
      const query = {
        ...this.query,
        ...this.params,
        ...params,
        labelId: this.query.labelId === 'all' ? null : this.query.labelId,
        peopleNum: this.query.peopleNum === 'all' ? null : this.query.peopleNum,
      };
      // const apiName =
      //   this.params.typeId === '2'
      //     ? 'documentPageInfoApi'
      //     : 'secretEscopePageInfo'
      uni.showLoading({
        title: '加载中',
      });
      ProductService.getCanPlayPlay(query).then((res) => {
        uni.hideLoading();
        this.total = res.total;
        res = res.list || [];
        res.forEach((v) => {
          v.goodsPic = this.$pictureUrl(v.goodsPic, 49, 49);
        });
        if (res && res.length) {
          this.cardList = this.cardList.concat(res);
        } else {
          this.status = 'noDataMore';
        }
      });
    },
  },
  onPullDownRefresh() {
    this.query.pageNum = 1;
    this.getList();
  },
};
</script>

<style lang="scss" scoped>
.activity-discount-center {
  min-height: 100vh;
  padding-bottom: 32rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #fff !important;
  .header {
    padding: 22upx 42upx !important;
    background: #171c33 !important;
    width: 100%;
    position: fixed;
    z-index: 1;
  }
  .card-wrapper {
    width: 100%;
    margin-top: 94upx;
  }
  .mt120 {
    margin-top: 120upx;
  }
}
.card-tabs {
  display: flex;
  flex-direction: row;
  margin: 0 auto;
  justify-content: space-between;
  padding: 0 15upx 32upx 15upx;
  background: #171c33;
  overflow-x: scroll;
  position: relative;
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
    color: #000000;
    text-align: center;
    border: 2upx solid #fde5a4;
    background: #171c33;
    color: #fde5a4;
    margin-right: 20upx;
    min-width: 100upx;
    padding: 6upx 16upx;
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
.active {
  background: #fde5a4 !important;
  color: #171c33 !important;
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
  background: #000000;
  margin-bottom: 16upx;
  margin-top: 16upx;
  display: flex;
  flex-direction: column;
  align-content: center;
  justify-content: center;
  text-align: center;
  > .text {
    font-size: 36upx;
    color: #fff;
  }
  > .desc {
    font-size: 20upx;
    color: #fff;
  }
}
.card-bottom {
  border-radius: 0;
}
.footer-hot {
  width: 100%;
  .title {
    color: #666666;
    text-align: center;
    margin-bottom: 70rpx;
    padding: 0 100upx;
  }
}
.hot-script {
  padding-bottom: 100rpx;
}
.mtop {
  margin-top: 100rpx;
}
.activity-discount-center /deep/ .u-content {
  background: #fff !important;
}
.card-tabs::-webkit-scrollbar {
  display: none;
}
</style>
