<template>
  <view
    class="vouchers"
    :style="{ 'background-size': '100% ' + height + 'rpx' }"
  >
    <comHead
      changeColor
      autoPadding
      :isHome="isHome"
      style="position: absolute; top: 0"
      class="com-head"
    >
      <view class="head"> 省钱神券 </view>
    </comHead>
    <!-- 头部 -->
    <view class="card-top"></view>
    <view class="progress" style="position: relative" v-if="false">
      <u-line-progress
        :percent="88"
        :show-percent="true"
        inactive-color="#FF8B8B"
        active-color="#000"
        height="36"
        :round="false"
      >
        已经抢 88%
      </u-line-progress>
      <u-badge
        bgColor="#FFD6B1"
        color="#8B0716"
        :offset="[-30, 100]"
        count="限量500人"
      ></u-badge>
    </view>
    <!-- <view class="card-time">发放时间：2021年 9 月 9 ~12 日</view> -->
    <!-- 我购买的 -->
    <view class="card-box-top card-box-top-buy" v-if="false">
      <view class="card-box-top-cell" @click="handlerOpen()">
        <view class="left-title"> 我购买的券</view>
        <view style="flex: 1"></view>
        <view class="right-text">剩余 {{ couponLength }} 张</view>
        <image class="right" src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/bfb89e5c-2ff4-4945-8f8d-64ae6f6b0217.png" alt />
      </view>
      <view class="card-box-top-content">
        <view class="card-box-top-content-wrapper">
          <!-- 					<view class="card-item-buy" v-for="(item, index) in getMyCouponList.filter((v, i) => i < 4)"
						:key="index">
						<image v-if="item.status === 0" class="haveUsed"
							src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/ba96765a-75d7-4ffb-a548-ceca67cca05a.png">
						</image>
						<image :src="item.url" />
						<view class="text">{{ item.couponTemplate.couponName}}</view>
						<view class="text">{{ item.text }}</view>
					</view> -->
          <view
            class="card-items"
            :class="{ 'flex-1': item.packageId == 3 }"
            v-for="(item, index) in getMyCouponList.filter((v, i) => i < 4)"
            :key="index"
          >
            <view
              class="card-item-buy"
              :class="{ 'inline-block': item.packageId != 3 }"
              v-if="item.packageId != 3"
            >
              <view class="flx">
                <view class="bar-card-left">
                  <image
                    v-if="item.status === 0"
                    class="haveUsed"
                    src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/ba96765a-75d7-4ffb-a548-ceca67cca05a.png"
                  >
                  </image>
                  <image :src="item.url" />
                </view>
                <view class="">
                  <view class="text">{{ item.couponTemplate.couponName }}</view>
                  <view class="text">有效期：{{ item.text }}</view>
                </view>
              </view>
              <view class="text npl">{{ item.desc }}</view>
            </view>

            <view class="bar-card-item-buy" v-else>
              <view class="bar-card-left">
                <view class="">
                  <image
                    v-if="item.status === 0"
                    class="haveUsed"
                    src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/ba96765a-75d7-4ffb-a548-ceca67cca05a.png"
                  >
                  </image>
                  <image :src="item.url"></image>
                </view>
                <view class="text">{{ item.desc }}</view>
                <view class="hr"></view>
                <text class="arrow arrow-top"></text>
                <text class="arrow arrow-bottom"></text>
              </view>
              <view class="bar-card-right">
                <view class="card-name">{{ item.templateName }}</view>
                <view class="text">有效期：{{ item.text }}</view>
                <view class="text jump" @click="jump"
                  >点击查看支持商家/套餐></view
                >
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
    <!-- 加油包 (购买后才会出现这个标题)-->
    <view class="card-title" v-if="couponLength > 0">
      <image
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/1cf4f60d-fc7a-4a7f-86c2-9b58a90b5f05.png"
      >
      </image>
      <view>加油包</view>
    </view>
    <view class="card-box-top">
      <view class="card-box-top-btn">
        <view
          class="btn"
          :class="{ 'btn-active': pid == item.value }"
          v-for="(item, index) in voucherslist"
          :key="index"
          @click="checkPackage(item)"
          >{{ item.name }}</view
        >
      </view>
      <view class="card-box-top-content">
        <view class="card-box-top-content-wrapper">
          <view
            class="card-items"
            :class="{ 'flex-1': pid == 3 }"
            v-for="(item, index) in couponList.filter((v, i) => i < 4)"
            :key="index"
          >
            <view class="card-item-buy" v-if="pid != 3">
              <view>
                <view class="bar-card-left">
                  <view class="card-money">
                    <text class="money">{{
                      item.templateName.replace(/[^0-9]/gi, '')
                    }}</text>
                    <text>元</text>
                  </view>
                  <image :src="item.url" />
                </view>
                <view class="">
                  <view class="text">{{ item.desc }}</view>
                  <view class="text">{{ item.text }}</view>
                </view>
              </view>
            </view>

            <view class="bar-card-item-buy" v-else>
              <view class="bar-card-left">
                <view class="bold"
                  ><text
                    >{{ item.templateName.replace(/[^0-9]/gi, '') }}元</text
                  ></view
                >
                <view class="text">{{ item.desc }}</view>
                <view class="hr"></view>
                <text class="arrow arrow-top"></text>
                <text class="arrow arrow-bottom"></text>
              </view>
              <view class="bar-card-right">
                <view class="card-name">{{ item.templateName }}</view>
                <view class="text">有效期：{{ item.text }}</view>
                <view class="text jump" @click="jump"
                  >点击查看支持商家/套餐></view
                >
              </view>
            </view>
          </view>
        </view>
        <view
          @click="buy"
          class="card-box-top-content-btn"
          v-if="couponList.length"
          ><text>预付</text> {{ price }}
          <text>元（立省 {{ saveMoney }} 元）</text></view
        >
        <view
          class="card-box-top-content-rule"
          @click="handlerOpenRule()"
          v-if="couponList.length"
          >点击查看规则 ></view
        >
      </view>
    </view>
    <!-- 邀请返现 -->
    <view class="card-yq" @click="handlerInvitation()">
      <image
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/d138c492-5be7-4568-8c67-04a9d4ba3678.png"
      />
      <view class="card-yq-center">
        <view class="card-yq-center-title">邀请返现</view>
        <view class="card-yq-center-desc">好友购买券包给你5-15元</view>
      </view>
      <view class="card-yq-end">
        <view class="card-yq-end-title">
          {{ gainMoney }}
          <view class="card-yq-end-title-price">元</view>
        </view>
        <view class="card-yq-end-desc">已获得</view>
      </view>
    </view>
    <!-- 支持商家列表 -->
    <view class="card-title" id="box">
      <image
        src="http://image-1306191496.cos.ap-nanjing.myqcloud.com/5ba953aa-8581-4ed2-8fe2-a558604ce213.png"
      >
      </image>
      <view style="color: #b2394a">支持商家</view>
    </view>
    <view class="card-tabs" v-if="false">
      <view
        class="card-tab"
        v-for="(item, index) in tabs"
        :key="index"
        :class="{ active: active === index }"
        @click="handlerTab(item, index)"
        >{{ item.name }}</view
      >
      <view class="card-tab card-tab-title"> 更多敬请期待</view>
    </view>
    <view class="card-box">
      <u-card
        :show-head="false"
        class="card-wrapper"
        v-for="(item, index) in list"
        :key="index"
      >
        <view slot="body" class="card-item" @click="handlerDetail(item)">
          <image :src="item.cover" mode="aspectFill"></image>
          <view class="card-content">
            <view class="card-content-title">{{showText(item.companyName, 16)}}</view>
						<view class="tags" v-if="item.marks.length && item.marks[0] != null">{{item.marks[0]}}</view>
						<view class="card-content-address" @click.stop="openLocation(item)">
						  <image
						    class="map-icon"
						    src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/card-place-icon.png"
						  ></image>
						  <view class="map-desc">{{ showText(item.companyAddress) }}</view>
						  <!-- <view class="map-loaction">5{{ item.location }}</view> -->
						</view>
            <view class="card-content-price-wrapper">
							<view class="price-small">￥</view>
							<view class="price-desc">{{ item.discountPrice }}</view>
							<!-- <view class="price-small">券后</view> -->
							<view class="ml10">门市价：</view>
							<view class="price-unwanted">¥{{item.originalPrice}}</view>
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

    <view class="my-card">
      <view class="surplus-card">
        <view class="">剩余</view>
        <view class="surplus">
          <text class="surplus-num">{{ couponLength }}</text>
          <text>张</text>
        </view>
      </view>
      <image
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/54766d78-227f-4103-b912-e77142d8c83b.png"
        @click="handlerOpen"
      ></image>
    </view>
  </view>
</template>
<script>
import gameService from '../../service/game';
import userService from '../../service/user';
import basicService from '../../service/basic';
import ComHead from '../../components/com-head.vue';
import user from '../../framework/store/user';
import login from '../../service/login';
export default {
  data() {
    return {
      shareUserId: undefined,
      total: [], //我购买的卷包总数
      status: 'nomore',
      isHome: false,
      timestamp: 86400,
      btnActive: 0,
      active: 0,
      voucherslist: [
        {
          name: '剧本包',
          value: 1,
          typeId: 2,
        },
        {
          name: '密逃包',
          value: 2,
          typeId: 3,
        },
        {
          name: '酒吧包',
          value: 3,
          typeId: 1,
        },
      ],
      tabs: [
        {
          name: '全部',
          typeId: null,
        },
        {
          name: '剧本杀',
          typeId: 2,
        },
        {
          name: '密室逃脱',
          typeId: 3,
        },
      ],
      list: [],
      query: {
        pageNum: 1,
        pageSize: 10,
        typeId: 2,
      },
      getMyCouponList: [],
      couponData: [],
      // 券包信息
      couponList: [],
      couponLength: 0,
      myShareList: '',
      height: 900,
      pid: 1,
      saveMoney: 0,
      price: 0,
      productId: '',
      rules: '',
      scolltop: 0,
      gainMoney: 0,
    };
  },
  components: {
    ComHead,
  },
  onReady() {
    this.getList();
    userService
      .updateHistoryList({
        pageNum: 1,
        pageSize: 100,
      })
      .then((res) => {
        res.filter((v) => {
          if (v.state === 2) {
            this.total.push(v);
          }
        });
        if (this.total.length > 0) {
          this.height = 1300;
        }
      });
  },
  onLoad(o) {
     this.$eventRecord(176)
    if (o.scene) {
      this.shareUserId = o.scene.replace('shareUserId', '')
			this.isHome = 1
    }
    if (o.shareUserId) {
      this.shareUserId = o.shareUserId;
    }
    if (o.isHome) {
      this.isHome = o.isHome;
    }
    if (this.shareUserId && this.myUserId) {
      this.$authorization()
      gameService.bindRelation({ parentId: this.shareUserId, parentType: 1 });
    }
    this.getAvailPackageNum();
    this.getMyBalance();
  },
  onShow() {
    const res = wx.getLaunchOptionsSync();
    this.couponPackageList(); // 获取券包信息
    this.getMyShareList(); // 获取下级购买优惠券列表
    this.getUserCouponList(); // 获取购买的券包是否已使用
    // this.getProductSoldNum()   // 已卖出的数量  进度写死,暂时不调用

    if (res.scene == 1007 || res.scene == 1008) {
      this.$eventRecord(165);
    }
  },
  onReachBottom() {
    if (this.list.length >= this.query.pageSize * this.query.pageNum) {
      this.query.pageNum++;
      this.getList();
    }
  },
  methods: {
    getMyBalance() {
      userService.getBalance(this.myUserId).then((res) => {
        this.gainMoney = res.balanceMoney + res.drawMoney;
      });
    },
    checkPackage(item) {
      const that = this;
      this.pid = item.value;
      this.query.typeId = item.typeId;
      this.query.pageNum = 1;
      this.list = [];
      this.getList();
      this.getCouponList();
      if (item.value == 1) {
        this.$eventRecord(158);
      } else if (item.value == 2) {
        this.$eventRecord(159);
      } else {
        this.$eventRecord(160);
      }
      setTimeout(() => {
        uni
          .createSelectorQuery()
          .select('#box')
          .boundingClientRect((rect) => {
            that.scolltop = rect.top;
          })
          .exec();
      }, 500);
    },
    jump() {
      uni.pageScrollTo({
        scrollTop: this.scolltop - 50,
      });
    },
    // 已卖出的数量
    getProductSoldNum() {
      userService.getProductSoldNumApi().then((res) => {
        console.log(res);
      });
    },
    // 获取下级购买优惠券列表
    async getMyShareList() {
      let res = await userService.getMyShareListApi();
      this.myShareList = res;
    },
    // 获取购买的券包总数量
    getAvailPackageNum() {
      userService.getAvailPackageNumApi().then((res) => {
        this.couponLength = res;
      });
    },
    // 获取券包信息
    couponPackageList() {
      basicService.couponPackageListApi().then((res) => {
        res.forEach((item) => {
          item.templates.forEach((v) => {
            if (v.num === 2) {
              item.templates.push(v);
            }
            // v.text = parseInt((item.offShelfTime - item.onShelfTime) / (1000 * 60 * 60 * 24)) + '天';
            v.text = '一个月';
            v.url =
              'https://image-1306191496.cos.ap-nanjing.myqcloud.com/9d8c31ed-1a91-4d06-bed1-62828dd745d8.png';
          });
        });
        this.couponData = res;
        this.getCouponList();
      });
    },
    getCouponList() {
      for (let i = 0, len = this.couponData.length; i < len; i++) {
        if (this.couponData[i].packageId == this.pid) {
          const reg = /↵/g;
          this.couponData[i].rule.replace(reg, '\n');
          this.productId = this.couponData[i].packageId;
          this.price = this.couponData[i].price;
          this.rules = this.couponData[i].rule;
          this.saveMoney =
            this.couponData[i].originalPrice - this.couponData[i].price;
          this.couponList = this.couponData[i].templates;

          break;
        } else {
          this.couponList = [];
        }
      }
    },
    // 获取购买的券包是否已使用
    getUserCouponList() {
      let parmas = {
        pageSize: 100,
        pageNum: 1,
        templateIds: '1271,1274,1275',
      };
      basicService.getUserCouponListApi(parmas).then((res) => {
        res.list.filter((v) => {
          // v.text = parseInt((v.offShelfTime - v.onShelfTime) / (1000 * 60 * 60 * 24)) + '天';
          v.text = '一个月';
          v.url =
            'https://image-1306191496.cos.ap-nanjing.myqcloud.com/9d8c31ed-1a91-4d06-bed1-62828dd745d8.png';
        });
        this.getMyCouponList = res.list;
      });
    },
    handlerOpenRule() {
      this.$eventRecord(162);
      uni.navigateTo({
        url: `/pagesA/vouchers/rules?rules=${this.rules}`,
      });
    },
    handlerOpen() {
      uni.navigateTo({
        url: `/pagesA/user/coupon?productId=1`,
      });
    },
    handlerDetail(item) {
      this.$eventRecord(164);
      uni.navigateTo({
        url: `/pagesA/activity/supplier-detail?supplierId=${item.supplierId}`,
      });
    },
    handlerInvitation() {
      this.$eventRecord(163);
      uni.navigateTo({
        url: `/pagesA/vouchers/invitation`,
      });
    },
    // 购买
    async buy() {
      const that = this;
      await this.$authorization();
      this.$eventRecord(161);
      let res = await userService.unifiedOrder({
        userId: this.myUserId,
        productType: 1,
        productId: this.productId,
        shareUserId: this.shareUserId || '',
      });
      uni
        .requestPayment({
          ...res,
          package: 'prepay_id=' + res.prepayId,
        })
        .then((res) => {
          if (!res[1]) {
            this.$toast('支付失败，请重新支付');
          } else {
            uni.navigateTo({
              url: `/pagesA/vouchers/buy-end?typeId=${that.query.typeId}&pid=${that.pid}`,
            });
          }
        });
    },
    // handlerTab(item, index) {
    // 	this.active = index;
    // 	this.query.typeId = item.typeId;
    // 	this.getList();
    // },
    showText(text) {
      if (text && text.length > 16) {
        return `${text.substring(0, 13)}...`;
      } else {
        return text;
      }
    },
    getList(params) {
      const query = {
        packageId: this.pid,
        ...this.query,
        ...params,
      };
      gameService.getSupplierList(query).then((res) => {
        this.list.push(...res);
        this.status = 'nomore';
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
  },
  onShareAppMessage(res) {
    return {
      title: '来拼叭玩剧本/密逃/酒吧，时时拼车，天天打折！',
      path: `pagesA/vouchers/buy?shareUserId=${this.myUserId}&isHome=1`,
      imageUrl:
        'https://image-1306191496.cos.ap-nanjing.myqcloud.com/aa363138-e569-4caf-b67b-3326244be718.png',
    };
  },
};
</script>

<style lang="scss" scoped>
.head {
  line-height: 1em;
  font-size: 32upx;
  color: #fff;
}
.vouchers {
  background: url('https://image-1306191496.cos.ap-nanjing.myqcloud.com/3a499a43-bb7d-4ebc-9e78-451daa0c7457.png')
    no-repeat center;
  background-size: 100% 900rpx;
  background-position: top;
  padding: 26upx 0 60upx 0;
  overflow: scroll;

  .card-top {
    text-align: center;
    width: calc(100% - 80upx);
    margin: 0 auto;
    height: 360upx;
    background: url('https://image-1306191496.cos.ap-nanjing.myqcloud.com/1d6cb956-2e40-4d93-b634-c1f7d73f7214.png')
      no-repeat center;
    background-size: 100% 100%;
    margin-top: 120upx;
  }

  .card-yq {
    min-height: 160upx;
    background: #fff;
    border-radius: 16upx;
    margin: 0 20upx;
    margin-bottom: 80upx;
    // position: relative;
    padding: 32upx 32upx;
    display: flex;
    flex-direction: row;
    box-shadow: 0px 2px 26px 0px rgba(0, 0, 0, 0.1);

    image {
      display: inline-flex;
      width: 88upx;
      height: 88upx;
    }

    .card-yq-center {
      flex: 1;
      padding-left: 22upx;

      .card-yq-center-title {
        font-weight: 500;
        color: #480404;
        font-size: 36upx;
      }

      .card-yq-center-desc {
        margin-top: 4upx;
        font-size: 28upx;
        color: #480404;
      }
    }

    .card-yq-end {
      display: inline-flex;
      flex-direction: column;

      .card-yq-end-title {
        font-weight: 500;
        color: #ff4949;
        font-size: 36upx;

        .card-yq-end-title-price {
          font-size: 20upx;
          color: #480404;
          font-weight: 500;
          display: inline-flex;
        }
      }

      .card-yq-end-desc {
        margin-top: 4upx;
        font-size: 20upx;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ff4949;
      }
    }
  }

  .card-time {
    font-size: 26upx;
    font-weight: 600;
    color: #ffffff;
    line-height: 1.5;
    text-align: center;
    margin-bottom: 24upx;
    margin-top: -160upx;
  }

  .progress {
    text-align: center;
    margin: 50upx 0;
    // top:-40upx;
    /deep/ u-line-progress {
      .u-progress {
        width: 418rpx;
        border-radius: 12rpx !important;
      }
    }
  }

  .card-box-top-buy {
    box-shadow: 0px 2px 26px 0px rgba(0, 0, 0, 0.1);

    .card-box-top-cell {
      border-top-right-radius: 16rpx;
      border-top-left-radius: 16rpx;
      display: flex;
      flex-direction: row;
      background: #fff;
      margin: 0 20upx;
      padding: 24upx 40upx 24upx;
      justify-content: center;

      .left-title {
        display: inline-block;
        font-size: 36upx;
        line-height: 36upx;
        font-weight: 500;
        color: #480404;
      }

      .right-text {
        display: inline-block;
        font-size: 28upx;
        line-height: 36upx;
        color: #480404;
      }

      > image {
        display: inline-block;
        width: 40upx;
        height: 40upx;
        margin-left: 10upx;
        margin-top: -4upx;
      }
    }

    .card-box-top-content {
      min-height: 310upx !important;
    }
  }

  .card-box-top-btn {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: space-between;
    margin: 0 20upx;
    text-align: center;
  }

  .card-box-top-btn > .btn {
    width: 33%;
    height: 80upx;
    background: rgba(255, 255, 255, 0.4);
    line-height: 80upx;
    font-size: 28upx;
    font-weight: 600;
    color: #fff;
    border-top-left-radius: 12upx;
    border-top-right-radius: 12upx;
  }

  .card-box-top-btn > .btn-active {
    background: #fff;
    color: #771421;
  }

  .card-box-top-content {
    // height: 490upx;
    background: #fff;
    border-radius: 0 0 16upx 16upx;
    margin: 0 auto;
    margin-bottom: 40upx;
    position: relative;
    box-shadow: 0px 2px 26px 0px rgba(0, 0, 0, 0.1);
    width: calc(100% - 40upx);
    .card-box-top-content-wrapper {
      padding: 40upx;
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;
      justify-content: space-between;
      width: 100%;
      .card-items {
        width: 23%;
        margin-top: 20upx;
        .bar-card-left {
          position: relative;
          .card-money {
            position: absolute;
            font-size: 18upx;
            top: 13upx;
            left: 36upx;
            color: #fff;
            .money {
              font-size: 28upx;
            }
          }
          .bold {
            color: #ff4c6c;
            font-size: 48upx;
            padding-top: 10upx;
          }
        }
        .inline-block {
          display: inline-block;
          .npl {
            padding-left: 0;
          }
        }
        .flx {
          display: flex;
        }
      }
      .flex-1 {
        flex: 1;
      }
      .card-item-buy,
      .bar-card-item-buy {
        display: flex;
        // height: 172upx;
        background: linear-gradient(360deg, #ffdcd1 0%, #fff3f3 100%);
        border-radius: 24upx;
        text-align: center;
        padding: 28upx 18upx;
        position: relative;
        height: 100%;
        > view {
          width: 100%;
        }
        image {
          width: 102upx;
          height: 66upx;
          margin-bottom: 10upx;
        }

        .haveUsed {
          width: 88rpx;
          height: 54rpx;
          top: -10px;
          right: -20px;
          position: absolute;
        }

        .text {
          font-size: 20upx;
          color: #c53e52;
        }
      }
      .bar-card-item-buy {
        width: 100%;
        display: flex;
        padding: 28upx;
        .card-items {
          width: 100%;
        }
        .bar-card-left {
          width: 120upx;
          .hr {
            position: absolute;
            height: 100%;
            top: 0;
            left: 140upx;
            border-left: 1px dashed #fff;
          }
          .arrow {
            display: inline-block;
            border: 15upx solid transparent;
            position: absolute;
            left: 150upx;
          }
          .arrow-top {
            border-top-color: #fff;
            top: -28upx;
          }
          .arrow-bottom {
            bottom: -28upx;
            border-bottom-color: #fff;
          }
        }
        .bar-card-right {
          text-align: left;
          flex: 1;
          padding-left: 50upx;
          .card-name {
            font-size: 32upx;
            color: #902030;
          }
          .text {
            width: 100%;
            margin-top: 15upx;
            padding-left: 0;
          }
          .jump {
            display: inline-block;
            padding: 6upx 22upx;
            border-radius: 20px;
            background: rgba(192, 65, 81, 0.3);
            width: auto;
          }
        }
      }
    }

    > .card-box-top-content-btn {
      width: 622upx;
      height: 82upx;
      font-size: 40upx;
      line-height: 82upx;
      background: linear-gradient(270deg, #ff9b46 0%, #dd0829 100%);
      border-radius: 60upx;
      margin: 0 auto;
      text-align: center;
      color: #fff;
      font-weight: bold;

      text {
        font-size: 28rpx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #ffffff;
      }
    }

    .card-box-top-content-rule {
      margin-top: 26upx;
      text-align: center;
      color: #8b0215;
      padding-bottom: 26upx;
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
      line-height: 1.5;
      font-weight: 400;
      color: #b03348;
      text-align: center;
      border: 2upx solid #b03348;
      margin-right: 20upx;
    }

    .card-tab-title {
      border: 0;
      color: #999;
      font-size: 24upx;
      line-height: 48upx;
      margin-right: 0;
      text-align: right;
    }
  }

  .card-title {
    margin-bottom: 25upx;
    text-align: center;
    font-size: 40upx;
    font-weight: bold;
    color: #fff;
    position: relative;

    image {
      width: 232rpx;
      height: 12rpx;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
  }

  .card-list {
    display: flex;
    padding: 26upx 40upx 0 40upx;
    justify-content: space-between;

    .card {
      width: 320upx;
      height: 120upx;
      position: relative;

      .card-title {
        position: absolute;
        right: 0;
        left: 32upx;
        top: 26upx;
        font-size: 28upx;
        font-weight: 600;
        color: #000000;
      }

      .card-desc {
        position: absolute;
        top: 66upx;
        left: 32upx;
        font-size: 20upx;
        color: #000000;
      }

      > image {
        width: 100%;
        height: 100%;
      }

      > .right {
        position: absolute;
        right: 0;
        width: 24upx;
        top: 32upx;
        left: 150upx;
        height: 24upx;
      }
    }
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
				margin-top: 12upx;
        position: relative;
        display: flex;
        flex-direction: row;
        align-items: flex-end;

        .price-small {
          font-size: 28upx;
          font-weight: 600;
          color: #ff4f3a;
          line-height: 40upx;
          align-self: center;
          font-family: PingFangSC-Regular, PingFang SC;
        }

        .price-men {
          font-size: 24upx;
          line-height: 40upx;
          color: #666666;
          margin-left: 10upx;
        }

        .price-desc {
          font-size: 40upx;
          line-height: 40upx;
          color: #ff4f3a;
					margin-right: 10upx;
					font-weight: 600;
          align-self: flex-end;
        }
				.ml10{
					margin-left: 10upx;
				}
				.price-unwanted{
					text-decoration: line-through;
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
        // margin-top: 10upx;
        // margin-left: 12upx;

        .map-icon {
          height: 30upx;
          margin-top: 5upx;
          width: 28upx;
          display: inline-flex;
        }

        .map-desc {
          flex: 1;
          font-size: 24upx;
          line-height: 40upx;
          color: #666666;
          padding-left: 10upx;
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

  .active {
    color: #fff !important;
    background: #b03348;
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
  color: #999 !important;

  .empty-icon {
    height: 64upx;
    width: 64upx;
    margin-right: 18upx;
    color: #fff !important;
  }
}
.my-card {
  position: fixed;
  top: 66%;
  right: 0;
  color: #9a3d3d;
  font-size: 20upx;
  z-index: 99999;
  image {
    width: 139upx;
    height: 168upx;
  }
  .surplus-card {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    padding-top: 12upx;
    text-align: center;
    .surplus {
      margin-top: -6upx;
      color: #ea5442;
      .surplus-num {
        font-size: 36upx;
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
	margin-top: 10upx;
	padding:6upx 18upx;
	display: inline-block;
	border-radius: 20px;
	background: #FEF3F3;
	color:#ff4f3a;
	font-size:24upx;
}
.flex-s {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
