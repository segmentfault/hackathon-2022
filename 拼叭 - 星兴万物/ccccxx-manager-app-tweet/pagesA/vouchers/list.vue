<template>
  <view class="seek-wrapper">
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
            <view class="card-content-title">{{  showText(item.companyName, 16) }}</view>
            <view class="card-content-title-popularity" v-if="index === 0">
              <image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/f60a3d5f-6b74-400d-ac1f-2ae30c11c9f1.png" />
              <view class="card-content-title-popularity-desc">
                人气榜单第 1 名
              </view>
            </view>
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
              <view class="price-desc">{{ item.originalPrice }}</view>
              <view class="price-small">门市价</view>
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
import homeService from "../../service/home.js";
import vouchersCard from "@/pagesA/components/vouchers-card.vue";
import gameService from "../../service/game.js";
export default {
  components: {
    vouchersCard,
  },
  data() {
    return {
      status: "loadmore",
      keyword: "",
      barStyle: {
        background: "linear-gradient(180deg, #CD6CFF 0%, #FF4F3A 100%)",
      },
      banner: [], // 轮播图
      list: [
        {
          imgUrl:
            "https://img11.360buyimg.com/n7/jfs/t1/94448/29/2734/524808/5dd4cc16E990dfb6b/59c256f85a8c3757.jpg",
          title: "【剧本杀】迷雾剧本",
          hotTitle: "剧本杀人气榜单第 1 名",
          address: "侯家塘贺龙体育馆体育",
          location: "500米",
          price: "60 ~ 189",
        },
        {
          imgUrl:
            "https://img11.360buyimg.com/n7/jfs/t1/94448/29/2734/524808/5dd4cc16E990dfb6b/59c256f85a8c3757.jpg",
          title: "【剧本杀】迷雾剧本",
          hotTitle: "剧本杀人气榜单第 1 名",
          address: "侯家塘贺龙体育馆体育",
          location: "500米",
          price: "60 ~ 189",
        },
      ],
      current: 0,
      tabs: [],
      cardList: [
        {
          bg: "https://image-1306191496.cos.ap-nanjing.myqcloud.com/3a084b86-76a9-41b5-b705-a0976e210154.png",
          title: "省钱好券",
          url: "/pagesA/vouchers/buy",
          desc: "立省65元，1个月畅玩",
        },
        {
          bg: "https://image-1306191496.cos.ap-nanjing.myqcloud.com/aed88ace-c463-4bbc-9352-dced8f4642ec.png",
          title: "邀请返现",
          url: "/pagesA/vouchers/buy",
          desc: "好友买券包返你5元",
        },
      ],
      query: {
        pageNum: 1,
        pageSize: 20,
        activityId: 4,
      },
    };
  },
  onReady() {
    this.init();
  },
  methods: {
    handlerDetail(item) { 
      uni.navigateTo({ url:`/pagesA/businessDetail/index?supplierId=${item.supplierId}&typeId=${item.typeId}`});
    },
    showText(text, length = 0) {
      if (text && text.length > 16) {
        if( length > 0) { 
          return `${text.substring(0, length)}...`;
        } else { 
          return `${text.substring(0, 13)}...`;
        }
      } else {
        return text;
      }
    },
    getList(params) {
      const query = {
        ...this.query,
        ...params,
      };
      gameService.getSupplierList(query).then((res) => {
        this.list = res || [];
        this.status = "nomore";
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
      this.current = val;
      this.getList({ typeId: this.tabs[val].typeId });
    },
    initTabs() {
      this.tabs = [
        {
          name: "全部",
          typeId: null,
        },
        {
          name: "剧本",
          typeId: 2,
        },
        {
          name: "密室",
          typeId: 3,
        },
      ];
    },
    init() {
      this.initTabs();
      this.getList();
      this.getBannerList();
    },
    // banner
    async getBannerList() {
      let res = await homeService.bannerListBuy();
      let banner = res || [];
      // 图片压缩居中裁剪
      banner.forEach((e) => {
        e.picUrl += "?imageView2/1/w/670/h/220/q/80/webp";
      });
      this.banner = banner;
    },
  },
};
</script>
<style lang="scss" scoped>
.seek-wrapper {
  .tab-wrapper {
    padding: 26upx 40upx 20upx 40upx;
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
        position: absolute;
        bottom: 0;
        left: 34upx;
        display: flex;
        flex-direction: row;
        vertical-align: bottom;
        .price-small {
          font-size: 24upx;
          font-weight: 600;
          color: #ff4f3a;
          line-height: 40upx;
          align-self: flex-end;
        }
        .price-desc {
          padding: 0 16upx;
          font-size: 32upx;
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
        margin-left: 12upx;
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
</style>