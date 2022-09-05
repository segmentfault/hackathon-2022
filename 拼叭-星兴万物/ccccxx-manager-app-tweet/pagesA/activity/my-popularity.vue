<template>
  <view class="page-bg">
    <comHead changeColor>好友助力榜</comHead>

    <view class="head-bg"></view>
    <!-- <image
      class="head-bg"
      src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/my-zl-bg.png"
      mode=""
    ></image> -->

    <view class="rank-list">
      <template v-if="list && list.length">
        <view class="line-item" :key="i" v-for="(item, i) in list">
          <image class="header" :src="item.head" mode="aspectFill"></image>
          <view class="r-bg">
            <view class="flex-s">
              <view class="l-name"
                >{{ item.nickName }}
                <image
                  v-if="item.isNew"
                  src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/f54c59e0-43d5-4221-9fd7-59b805d2971f.png"
                />
                <image
                  v-if="!item.isNew"
                  src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/b2d824e3-7ab5-46b1-a0cf-049e30ae55e1.png"
                />
              </view>
              <view class="r-val flex-c">
                <image
                  class="fire"
                  src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/fire.png"
                  mode=""
                ></image
                >+{{ item.popularity }}
              </view>
            </view>
            <view class="flex-s">
              <view class="l-from"
                >来自{{ zlFrom[item.boostSource] }}“给TA助力”</view
              >
              <view class="r-time">
                {{
                  moment(item.createTime).format("YYYY-MM-DD HH:mm")
                }}
              </view>
            </view>
          </view>
        </view>
      </template>
      <template v-if="!list.length">
        <view class="smallTip">
          <div class="top">
            <image
              src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/e08bc21e-f918-4791-923b-d6b6b63260a3.png"
            ></image>
          </div>
          <view class="bottom">
            <view>暂无消息</view>
          </view>
        </view>
      </template>
    </view>
  </view>
</template>

<script>
import comHead from "../../components/com-head.vue";
import gameService from "../../service/game.js";
import moment from "moment";
export default {
  components: { comHead },
  data() {
    return {
      moment,
      pageNo: 1,
      list: [],
      finish: false,
      zlFrom: ["", "平台", "活动", "面对面扫码", "分享微信"],
    };
  },
  onLoad(o) {
    this.getGameList(o);
  },
  // onReachBottom() {
    // this.getGameList(o);
  // },
  methods: {
    getGameList(o) {
      if (this.finish) return;
      gameService
        .queryBoostList({
          pageNo: this.pageNo,
          pageSize: 10,
          activityId: o.activityId
        })
        .then((res) => {
          this.list = this.list.concat(res.list);
          this.pageNo++;
          if (res.list.length < 10) {
            this.finish = true;
          }
        });
    },
    reload() {
      this.pageNo = 1;
      this.list = [];
      this.finish = false;
      this.getGameList();
    },
  },
  onPullDownRefresh() {
    setTimeout(() => {
      uni.stopPullDownRefresh();
      this.reload();
    }, 1000);
  },
};
</script>

<style lang="scss" scoped>
.page-bg {
  position: relative;
  min-height: calc(100vh + env(safe-area-inset-bottom));
  width: 100%;
  background: #171b2a;
  padding-bottom: env(safe-area-inset-bottom);
  .head-bg {
    height: 572upx;
    width: 100%;
  }
  .flex-c {
    display: flex;
    align-items: center;
  }
  .flex-s {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .rank-list {
    padding-top: 1upx;
    position: relative;
    z-index: 99;
    margin: -394upx 28upx 0px 28upx;
    min-height: 1380upx;
    width: calc(100% - 56upx);
    border-radius: 16upx 16upx 0px 0px;
    background-color: #fff;
    .line-item {
      position: relative;
      display: flex;
      align-items: center;
      height: 100upx;
      margin-top: 40upx;
      padding: 0px 28upx 0px 28upx;
      .header {
        height: 100upx;
        width: 100upx;
        border-radius: 50%;
      }
      .r-bg {
        margin-left: 14upx;
        width: calc(100% - 114upx);
        .l-name {
          font-size: 28upx;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #000000;
          line-height: 52upx;
          image {
            width: 78upx;
            height: 28upx;
            margin-left: 10upx;
          }
        }
        .r-val {
          font-size: 24upx;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #f06f6f;
          line-height: 52upx;
          .fire {
            height: 28upx;
            width: 22upx;
            margin-right: 6upx;
          }
        }
        .l-from {
          font-size: 24upx;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #666666;
          line-height: 52upx;
        }
        .r-time {
          font-size: 20upx;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #666666;
          line-height: 52upx;
        }
      }
    }
  }
}
.finish-t {
  margin-top: 22upx;
  font-size: 24upx;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #666666;
  line-height: 34upx;
  text-align: center;
}
.none-t {
  margin-top: 22upx;
  font-size: 24upx;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #666666;
  line-height: 34upx;
  text-align: center;
}
.smallTip {
  .top {
    width: 180upx;
    height: 180upx;
    margin: 120upx auto 80upx;
    image {
      width: 100%;
      height: 100%;
    }
  }
  .bottom {
    text-align: center;
    color: #999;
  }
}
</style>
