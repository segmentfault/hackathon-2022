<template>
  <view class="page-bg">
    <comHead changeColor></comHead>

    <image
      class="head-bg"
      src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/renqi-bg.png"
      mode=""
    ></image>

    <view class="rank-list">
      <view class="top-ctn">
        <view class="top-item" v-if="list[1]">
          <view class="top-item-n margin-a">人气之星*榜眼 </view>
          <image
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/top-2.png"
            class="top-item-i margin-a"
          ></image>
          <image
            :src="list[1].head"
            class="top-item-h margin-a"
            mode="aspectFill"
          ></image>
          <view class="top-item-t margin-a">{{ list[1].nickName }}</view>
          <view class="top-item-v margin-a">
            <image
              class="v-fire"
              src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/fire.png"
              mode=""
            ></image>
            {{ list[1].popularity }}</view
          >
          <image :src="list[1].rewardIcon" class="top-item-g margin-a"></image>
          <view class="top-item-tip">即将获得</view>
        </view>
        <view class="top-item first-c" v-if="list[0]">
          <view class="top-item-n margin-a">人气之星*状元</view>
          <image
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/top-1.png"
            class="top-item-i margin-a"
          ></image>
          <image
            :src="list[0].head"
            class="top-item-h margin-a"
            mode="aspectFill"
          ></image>
          <view class="top-item-t margin-a">{{ list[0].nickName }}</view>
          <view class="top-item-v margin-a"
            ><image
              class="v-fire"
              src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/fire.png"
              mode=""
            ></image
            >{{ list[0].popularity }}</view
          >
          <image :src="list[0].rewardIcon" class="top-item-g margin-a"></image>
          <view class="top-item-tip">即将获得</view>
        </view>
        <view class="top-item" v-if="list[2]">
          <view class="top-item-n margin-a">人气之星*探花</view>
          <image
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/top-3.png"
            class="top-item-i margin-a"
          ></image>
          <image
            :src="list[2].head"
            class="top-item-h margin-a"
            mode="aspectFill"
          ></image>
          <view class="top-item-t margin-a">{{ list[2].nickName }}</view>
          <view class="top-item-v margin-a"
            ><image
              class="v-fire"
              src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/fire.png"
              mode=""
            ></image
            >{{ list[2].popularity }}</view
          >
          <image :src="list[2].rewardIcon" class="top-item-g margin-a"></image>
          <view class="top-item-tip">即将获得</view>
        </view>
      </view>
      <view class="line-item col-t">
        <view class="col-1">排名</view>
        <view class="col-2">用户名</view>
        <view class="col-3">人气值</view>
        <view class="col-4">帮 TA</view>
        <view class="col-5">即将获得</view>
      </view>
      <template v-if="list && list.length">
        <view
          class="line-item child-t"
          v-if="index > 2"
          v-for="(item, index) in list"
          :key="index"
        >
          <view class="col-1"
            ><view class="nums">{{ item.rank }}</view></view
          >
          <view class="col-2">{{ item.nickName }}</view>
          <view class="col-3 flex-c">
            <image
              class="fire"
              src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/fire.png"
              mode=""
            ></image
            >{{ item.popularity }}
          </view>
          <button
            v-if="canIUseGetUserProfile && !myUserInfo.head"
            class="col-4"
            @click="(v) => boost(item, index, v)"
          >
            <view class="add-num" v-if="item._addNums"
              >+{{ item._addNums }}</view
            >
            <image
              v-if="item.help"
              class="add-icon"
              src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/added.png"
              mode=""
            ></image>
            <image
              v-else
              class="add-icon"
              src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/add.png"
              mode=""
            ></image>
          </button>
          <button
            else-if="!myUserInfo.head"
            class="col-4"
            open-type="getUserInfo"
            @getuserinfo="(v) => boost(item, index, v)"
          >
            <view class="add-num" v-if="item._addNums"
              >+{{ item._addNums }}</view
            >
            <image
              v-if="item.help"
              class="add-icon"
              src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/added.png"
              mode=""
            ></image>
            <image
              v-else
              class="add-icon"
              src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/add.png"
              mode=""
            ></image>
          </button>
          <view class="col-5">
            <image
              v-if="item.rewardIcon"
              class="top-4"
              :src="item.rewardIcon"
              mode=""
            ></image>
          </view>
        </view>
      </template>
    </view>
    <view class="finish-t" v-if="finish">已加载全部</view>
    <!-- 人气玩家排行榜 -->

    <view class="btm-bg" v-if="false">
      <div class="btm-bg-top">
        <view class="haed-bg">
          <image
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/bangdan-banner.png"
            mode=""
          ></image>
        </view>
        <view class="haed-c">
          <view class="flex-c">
            <view class="user-n">阿萨大阿萨大阿萨大啊啊</view>
            <view class="flex-c zl-val">
              <image
                class="fire"
                src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/rank/fire.png"
                mode=""
              ></image>
              5883
            </view>
          </view>
          <view class="other-rank">还有 23 人气值即将晋升 1名</view>
        </view>
        <view class="figt-tip">再努力努力有可能获得 iPad</view>
      </div>
    </view>
  </view>
</template>

<script>
import comHead from "../../components/com-head.vue";
import gameService from "../../service/game.js";
export default {
  components: { comHead },
  data() {
    return {
      pageNo: 1,
      list: [],
      finish: false,
    };
  },
  methods: {
    loadingList() {},
  },
  onLoad() {
    this.canIUseGetUserProfile = uni.canIUse("getUserProfile");
    this.getGameList();
  },
  onReachBottom() {
    this.getGameList();
  },
  methods: {
    getGameList() {
      if (this.finish) return;
      gameService
        .queryUserList({
          pageNo: this.pageNo,
          pageSize: 20,
        })
        .then((res) => {
          this.list = this.list.concat(res.list);
          this.pageNo++;
          if (res.list.length < 10) {
            this.finish = true;
          }
        });
    },
    //帮他助力
    async boost(data, index, e) {
      if (data.help) {
        return;
      }
      //榜单-帮他助力埋点
      wx.reportAnalytics("rank_help_friends", {});
      if (this.myUserInfo.nickName && this.myUserInfo.head) {
        wx.reportAnalytics("give_invitation", {});
        gameService.boost(data.userId).then((res) => {
          this.$toast("助力成功", "success");
          this.$set(this.list[index], "_addNums", Number(res));
          this.list[index].popularity += Number(res);
          this.list[index].help = true;
          this.$forceUpdate();
        });
      } else {
        let userInfo = {};
        if (this.canIUseGetUserProfile) {
          let res =
            (await uni.getUserProfile({
              desc: "用于完善资料",
            })) || [];
          userInfo = (res[1] && res[1].userInfo) || {};
        } else {
          userInfo = e.detail.userInfo || {};
        }
        let { avatarUrl, nickName } = userInfo || {};
        //授权成功了进行助力
        if (avatarUrl && nickName) {
          gameService.boost(data.userId).then((res) => {
            this.$toast("助力成功", "success");
            this.$set(this.list[index], "_addNums", Number(res));
            this.list[index].popularity += Number(res);
            this.list[index].help = true;
            this.$forceUpdate();
          });
        }
      }
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

<style scoped lang="scss">
.page-bg {
  position: relative;
  min-height: calc(100vh + env(safe-area-inset-bottom));
  width: 100%;
  background: #f9eaff;
  padding-bottom: env(safe-area-inset-bottom);
  .head-bg {
    height: 572upx;
    width: 100%;
  }
  .rank-list {
    position: relative;
    z-index: 99;
    margin: -194upx 28upx 0px 28upx;
    width: calc(100% - 56upx);
    border-radius: 16upx 16upx 0px 0px;
    background-color: #fff;
    .top-ctn {
      position: relative;
      top: -150upx;
      display: flex;
      margin: 0px 60upx;
      .first-c {
        position: relative;
        margin-top: -40upx;
      }
      .top-item {
        flex: 1;
        &-n {
          font-size: 22upx;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #ffffff;
          line-height: 32upx;
        }
        &-i {
          height: 72upx;
          width: 76upx;
          border-radius: 50%;
          display: block;
        }
        &-h {
          height: 128upx;
          width: 128upx;
          border-radius: 50%;
          display: block;
        }
        &-v {
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #f06f6f;
          font-size: 24upx;
          display: flex;
          align-items: center;
          justify-content: center;
          .v-fire {
            height: 28upx;
            width: 22upx;
          }
        }
        &-t {
          width: 150upx;
          font-size: 24upx;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #666666;
          line-height: 52upx;
          position: relative;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        &-g {
          width: 100upx;
          height: 110upx;
          display: block;
        }
        &-tip {
          height: 26upx;
          width: 74upx;
          margin: 0px auto;
          text-align: center;
          background: #fa4f4f;
          border-radius: 14upx;
          font-size: 16upx;
          color: #fff;
        }
      }
    }
  }
  .col-t {
    position: relative;
    margin-top: -120upx;
    font-size: 24upx;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #5f52d2;
    line-height: 34upx;
    background: #fff5fd;
  }
  .line-item:nth-child(odd) {
    background-color: #fff;
  }
  .line-item:nth-child(even) {
    background: #fff5fd;
  }
  .child-t {
    .col-2 {
      padding-right: 8upx;
      font-size: 24upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #4a3e6c;
      position: relative;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .col-3 {
      font-size: 24upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #ff0000;
      .fire {
        height: 28upx;
        width: 22upx;
        display: block;
        margin-right: 10upx;
      }
    }
  }
  .line-item {
    height: 80upx;
    width: 100%;
    display: flex;
    align-items: center;
    .col-1 {
      margin-left: 20upx;
      width: 106upx;
    }
    .col-2 {
      width: 188upx;
    }
    .col-3 {
      width: 154upx;
    }
    .col-4 {
      position: relative;
      height: 100%;
      display: flex;
      align-items: center;
      width: 118upx;
      font-size: 24upx;
      background: none !important;
      .add-icon {
        position: relative;
        margin-left: 14upx;
        display: block;
        height: 32upx;
        width: 32upx;
      }
      .add-num {
        position: absolute;
        top: 0upx;
        width: 100%;
        text-align: center;
        font-size: 20upx;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ff2425;
        line-height: 28upx;
      }
    }
    .col-5 {
      width: calc(100% - 586upx);
    }
    .nums {
      margin-left: 4upx;
      height: 38upx;
      width: 37upx;
      background: #5f52d2;
      border-radius: 50%;
      line-height: 38upx;
      text-align: center;
      font-size: 24upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #ffffff;
    }
    .top-4 {
      height: 76upx;
      width: 76upx;
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
.flex-c {
  display: flex;
  align-items: center;
}
.margin-a {
  position: relative;
  margin: 0px auto;
  text-align: center;
}
.btm-bg {
  position: fixed;
  padding: 0px 34upx;
  bottom: 0px;
  left: 0px;
  right: 0px;
  bottom: 0px;
  height: calc(276upx + env(safe-area-inset-bottom));
  width: 100%;
  background: linear-gradient(360deg, #988aeb 0%, #5f52d2 100%);
  border-radius: 32upx 32upx 0px 0px;
  padding-bottom: env(safe-area-inset-bottom);
  &-top {
    display: flex;
    align-items: center;
    justify-content: space-between;
    .haed-c {
      width: calc(100% - 354upx);
      margin-left: 30upx;
      margin-right: 20upx;
      .other-rank {
        font-size: 20upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #ffffff;
        line-height: 28upx;
        border-radius: 20upx;
        border: 2upx solid #ffffff;
        padding: 2upx 6upx;
        display: inline-block;
      }
      .user-n {
        position: relative;
        width: 204upx;
        font-size: 32upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #ffffff;
        line-height: 52upx;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      .zl-val {
        font-size: 24upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #f06f6f;
        line-height: 52upx;
        .fire {
          height: 28upx;
          width: 24upx;
          flex: none;
          margin-right: 6upx;
        }
      }
    }
    .figt-tip {
      padding: 10upx 15upx;
      text-align: center;
      height: 82upx;
      width: 184upx;
      background: #5e51d1;
      border-radius: 16upx;
      font-size: 20upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #ffffff;
      line-height: 28upx;
    }
  }
  .haed-bg {
    position: relative;
    overflow: hidden;
    border-radius: 50%;
    height: 150upx;
    width: 150upx;
    border: 8upx solid #5f52d2;
  }
}
</style>
