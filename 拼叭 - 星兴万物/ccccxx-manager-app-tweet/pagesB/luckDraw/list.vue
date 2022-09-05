<template>
  <view class="page-luckdraw pt20">
    <view class="main">
      <view class="main-content" v-if="list.length">
        <view
          class="card-list"
          v-for="(item, index) in list"
          :key="index"
          @click="openDetail(item)"
        >
          <view class="card-top">
            <img :src="item.cover" alt />
            <img
              style="position: absolute"
              class="img2"
              v-if="item.state === 2"
              src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/3ffbaee3-e93f-4b6e-9f9e-eb815c5e1941.png"
            />
            <view class="end" v-if="item.state === 2 || item.state === 3">
              <img
                src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/874b0be3-d8b2-45c3-a0e7-739211ea276f.png"
                alt=""
              />
            </view>
            <view class="end" v-if="item.state === 3">
              <img
                src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/c51768ff-7377-4c85-a415-331e867d1e57.png"
                alt=""
              />
            </view>
            <view class="card-right">
              <view class="title">{{ item.supplier.companyName }}</view>
              <view class="address" @click.stop="openLocation(item.supplier)">
                <img
                  class="icon"
                  src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/card-place-icon.png"
                  alt
                />
                <view class="text">{{ item.supplier.companyAddress }}</view>
              </view>
              <view class="text-time"
                >{{ item.luckType === 1 ? '截止时间' : '开奖时间' }}:
                {{
                  $utils.dayjs(item.endTime).format('YYYY-MM-DD HH:mm:ss')
                }}</view
              >
              <view class="win">
                <view class="number" v-if="item.luckType === 1"
                  >{{ item.luckNum }}/{{ item.userNum }}人参与</view
                >
                <view class="number" v-else>{{ item.luckNum }}人参与</view>
                <view class="draw">
                  {{ item.luckType === 1 ? '即抽即中' : '报名抽奖' }}
                </view>
              </view>
            </view>
          </view>
          <view class="card-content">
            <view class="card-content-left">
              <view class="cell mt" v-for="(e, i) in item.prizeList" :key="i">
                <view>{{ e.prizeName }}</view>
                <view>x{{ e.prizeNum }}</view>
              </view>
            </view>
            <view class="card-content-right">
              <view class="btn"> 查看活动 </view>
              <img
                v-if="item.isWin"
                class="right-icon"
                src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/823ac7e8-88ef-4824-b254-4bbede6853cd.png"
                alt
              />
            </view>
          </view>
        </view>
        <view v-if="list.length === query.total" class="no-more"
          >没有更多数据了</view
        >
      </view>
      <noData v-else></noData>
    </view>
  </view>
</template>
<script>
import gameService from '../../service//game.js';
import noData from '../components//default.vue';
export default {
  components: {
    noData,
  },
  data() {
    return {
      list: [],
      query: {
        pageNum: 1,
        pageSize: 5,
        total: 0,
        luckUserId: uni.getStorageSync('userInfo').userId,
      },
    };
  },
  methods: {
    handlerOpen() {
      uni.navigateTo({
        url: '/pagesB/luckDraw/list',
      });
    },
    openDetail(item) {
      if (item.state == 3) return this.$toast('该抽奖已废除!');
      uni.navigateTo({
        url: `/pagesB/luckDraw/drawDetail/drawDetail?id=${item.id}`,
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
    getLuckPage() {
      gameService.getLuckPage({ ...this.query }).then((res) => {
        this.list.push(...(res.list || []));
        this.query.total = res.total;
      });
    },
  },
  async onLoad() {
    await this.setDefLogin();
    uni.showLoading({ title: '加载中' });
    await this.getLuckPage();
    uni.hideLoading();
  },
  onReachBottom() {
    if (this.list.length < this.query.total) {
      this.query.pageNum++;
      this.getLuckPage();
    }
  },
  onPullDownRefresh() {
    this.query.pageNum = 1;
    this.query.pageSize = 10;
    this.getLuckPage();
  },
  onShareAppMessage(e) {
    return {
      title: '福利大放送，抽奖送免单!',
      path: '/pagesB/luckDraw/index',
      imageUrl:
        'https://image-1306191496.cos.ap-nanjing.myqcloud.com/a0501d7f-ff7e-44e9-a333-5b5d4a7b4302.png',
    };
  },
};
</script>
<style lang="scss" scoped>
.page-luckdraw {
  padding-bottom: 40upx;
  .main {
    padding: 0 24upx;
    .main-content {
      margin-top: -40upx;

      .card-list {
        padding: 40upx 34upx;
        background: #ffffff;
        box-shadow: 0px 4upx 76px 0px rgba(0, 0, 0, 0.1);
        border-radius: 16upx;
        border: 2upx solid #ffffff;
        display: flex;
        flex-direction: column;
        margin-bottom: 20upx;
        .card-top {
          display: flex;
          flex-direction: row;
          margin-bottom: 24upx;
          > img {
            margin-right: 26upx;
            border-radius: 16upx;
            object-fit: cover;
            width: 200upx;
            height: 200upx;
          }
          .end {
            position: absolute;
            padding-left: 46upx;
            padding-top: 44upx;
            img {
              height: 110upx;
              width: 110upx;
            }
          }
          > .card-right {
            flex: 1;
            display: flex;
            flex-direction: column;
            flex: 1;
            .title {
              font-size: 32upx;
              font-weight: 600;
              color: #000000;
              line-height: 44upx;
              margin-bottom: 16upx;
            }
            .address {
              display: flex;
              flex-direction: row;
              .icon {
                margin-top: 0;
                width: 28upx;
                height: 30upx;
                position: absolute;
              }
              .text {
                font-size: 24upx;
                font-weight: 400;
                color: #666666;
                line-height: 34upx;
                padding-left: 32rpx;
              }
            }
            .text-time {
              padding-left: 8upx;
              font-size: 24upx;
              color: #666666;
              line-height: 34upx;
              margin-top: 14upx;
            }
            .win {
              display: flex;
              margin-top: 18upx;
              margin-left: 8upx;
              .number {
                font-size: 24upx;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #666666;
                line-height: 38upx;
              }
              .draw {
                margin-left: 12upx;
                width: 130upx;
                height: 40upx;
                border-radius: 4px;
                text-align: center;
                border: 2upx solid #db2a15;
                font-size: 24upx;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #db2a15;
                line-height: 38upx;
              }
            }
          }
        }
        .card-content {
          display: flex;
          flex-direction: row;
          justify-content: space-between;

          .card-content-left {
            .cell {
              width: 296upx;
              background: url(https://image-1306191496.cos.ap-nanjing.myqcloud.com/0d5a75f1-061d-48f4-9c4e-c579204491a4.png)
                no-repeat center;
              background-size: 100% 100%;
              border-radius: 16upx;
              font-size: 28upx;
              font-weight: 500;
              color: #ff4f3a;
              line-height: 36upx;
              padding: 12upx 20upx;
              display: flex;
              justify-content: space-between;
              align-items: flex-end;
            }
            .mt {
              margin-bottom: 12upx;
            }
          }
          .card-content-right {
            padding-bottom: 12upx;
            display: flex;
            flex-direction: column-reverse;
            .right-icon {
              margin-top: -40rpx;
              padding-left: 52rpx;
              height: 116upx;
              width: 116upx;
              flex-basis: 116upx;
            }
            .btn {
              padding: 0 28upx;
              margin-top: 12upx;
              background: #fff;
              border-radius: 34upx;
              border: 2upx solid #df402f;
              font-weight: 600;
              font-size: 28upx;
              color: #df402f;
              text-align: center;
              line-height: 60upx;
              height: 60upx;
            }
          }
        }
      }
    }
  }
  .no-more {
    text-align: center;
  }
}
</style>
