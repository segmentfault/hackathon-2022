<template>
	<view class="page-bg">
		<comHead changeColor>{{isMe?'我的主页':'TA的主页'}}</comHead>

			<view class="activity-image" v-if="userInfo">
			<swiper class="mineBanner_swiper" :indicator-dots="indicatorDots" :autoplay="autoplay" :interval="interval"
				:duration="duration" :indicator-color="indicatorColor" :indicator-active-color="indicatorActiveColor">
				<swiper-item v-for="(item,index) in userInfo.pictures" :key='index'>
					<image style="height: 100%; width: 100%" :src="item.url" mode="aspectFill" @click='preview(item.url)'>
				</swiper-item>
			</swiper>
			<!-- 暂时屏蔽这个入口，后面的版本上 -->
			<!-- <view class="set-btn" @click="gotoSet()" v-if="isMe">
				<image class="set-icon" src="../../static/images/game/set-icon.png"></image>设置
			</view> -->
		</view>
		<view class="page-ctn" v-if="!isMe">
      <view class="fixed">
			  <!-- <view class="left"><button @click="invite">邀请组局</button></view> -->
			  <view class="right"><button  @click="toChat">打招呼</button></view>
      </view>
   </view>

		<view class="page-ctn">
			<view class="card-bg">
				<view class="man-item-l">
					<image class="header" :src="userInfo.head||'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/icon-pinbar2.png'" v-if="userInfo.head">
					</image>
					<template v-else-if="isMe">
						<button v-if="canIUseGetUserProfile" class='user_button' plain='true' @click="getUserInfo">
							<image src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/icon-pinbar2.png">
						</button>
						<button v-else class='user_button' plain='true' open-type="getUserInfo"
							@getuserinfo="getUserInfo">
							<image src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/icon-pinbar2.png">
						</button>
					</template>
					<image v-else class="header" src='https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/icon-pinbar2.png'></image>

				</view>
				<view class="man-item-r">
					<view class="flex-c">
						<view class="name">{{userInfo.nickName || member.userId}}</view>
						<!-- <view class="tip">未实人认证</view> -->
					</view>
					<view class="flex-c" style="margin-top: 10upx;">
						<view class="man-t">长沙市</view>
						<view class="man-t" v-if="userInfo.age">{{userInfo.age}}岁</view>
						<!-- <view class="man-t" v-if="userInfo.sex==0||userInfo.sex==1">{{userInfo.sex==1?'男':'女'}}</view> -->
						<view class="man-t" v-if="userInfo.constellation">{{userInfo.constellation}}</view>
					</view>
					<view class="flex-s" style="margin-top: 9upx;">
						<view class="flex-c interest-line">
							兴趣：<view class="interest" v-for="(item,index) in userInfo.interests" :key="index">{{item}}</view>
						</view>
					</view>
					<view class="flex-c btm-btn" v-if="!isMe">
						<view class="w-btn" v-if="!friendship.isFriend" @click="addFriends">关注 TA</view>
						<view class="w-btn" v-else @click="deleteFriends">{{friendship.isRelation?'已互关':'已关注'}}</view>
					</view>
				</view>
			</view>
		</view>

		<view class="game-ctn" v-if="!isMe&&coupon.length">
			<view class="title">我的优惠券 </view>
			<view class="list">
				<view class="list-item" v-for="(item,i) in coupon" :key="i">
					<view class="list-item-left">{{item.discount}}<text v-if="item.type === 1">折</text><text v-else>元</text></view>
					<view class="list-item-right">
						<view class="flex-row">
							<view class="shop-coupon" v-if="item.type === 1">商家{{item.discount}}折券</view>
							<view class="shop-coupon" v-else>商家{{item.discount}}元抵用券</view>
							<view class="go-use" :class="[item.status!=1&&'has-used']" @click="showModal(item)">
								{{item.status==1?'可使用':'已使用'}}
							</view>
						</view>
						<view class="flex-row shop-tips">仅限 {{item.invaliddate|formatTime}}当天</view>
					</view>
				</view>
			</view>
		</view>

		<view class="game-ctn" v-if="isMe&&list.length">
			<view class="title">我的组局 </view>
			<view class="game-card" v-for="(item,i) in list" @click="goGameDetail(item)" :key="i">
				<view class="game-place">{{item.title}} {{item.typeName}}</view>
				<view class="game-time flex-s">
					<view class="l-val">{{item.formatTime}} 开始</view>
					<!-- 已完成状态 灰色 -->
					<view class="game-state" :class="false?'state-success':''">{{stateMap[item.complete]}}</view>
				</view>
				<view class="game-tag">
					<!-- <view class="tab-item" v-for="item in 10">蹦迪</view> -->
				</view>
			</view>
		</view>

		<view class="game-ctn" v-else>
			    <view class="title">Ta的组局 </view>
          <view v-for="(item,index) in list" :key="index">
            <barCard :bar="item"></barCard>
          </view>
          <view class="tip" v-if="!list">TA还没有活动哦，邀请TA一起吧</view>
		</view>

		<u-popup class="poup-box" v-model="showCode" mode="center">
			<view class="modal">
				<view class="modal-tips">请将二维码出示给商家核销备份</view>
				<canvas class="modal-img" canvas-id="qrcode"></canvas>
			</view>
		</u-popup>
		<view class="fixed-close" v-if="showCode" @click="showCode=false">
			<image src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/close.png" mode="scaleToFill" />
		</view>
	</view>
</template>

<script>
import comHead from "../../components/com-head.vue";
import LoginService from "../../service/login.js";
import homeService from "../../service/home.js";
import IMService from "../../service/im.js";
import userService from "../../service/user";
import drawQrcode from "weapp-qrcode";
import IM from "../../pages/im/lib/index";
import barCard from "../../components/barCard.vue";
import IMMixin from "../../pages/im/lib/mixin.js";

export default {
  mixins: [IMMixin],
  components: {
    comHead,
    barCard,
  },
  data() {
    return {
      isMe: false,
      currentUserId: "",
      userInfo: {},
      pageNum: 1,
      pageSize: 5,
      isEnd: false,
      showCode: false,
      stateMap: {
        1: "待开始",
        2: "已开始",
        3: "已结束",
        4: "已解散",
      },
      list: [],
      coupon: [],
      canIUseGetUserProfile: false,
      friendship: {
        // 好友关系
        isFriend: false, // 当前查看的用户是否已加好友
      },
      params: {
        mineUserId: "",
        pageNum: 1,
        pageSize: 10,
      },
    };
  },
  async mounted() {
    this.canIUseGetUserProfile = uni.canIUse("getUserProfile");
  },
  onLoad(option) {
    this.currentUserId = option.id;
    this.params.mineUserId = option.id;
    this.isMe = this.currentUserId == this.myUserId;
    this.getCurrentUserInfo();
    this.resetParms();
    this.myJoinList();
    this.couponList();
    this.getGameList();
    if (!this.isMe) {
      this.checkFriends();
    }
  },
  onPageScroll(e) {
    let { scrollTop = 0 } = e;
    uni.$emit("onPageScroll", scrollTop);
  },
  methods: {
    // 邀请组局
    invite() {
      // 埋点
      this.$eventRecord(53);
    },
    preview(e) {
      let arr = [];
      this.userInfo.pictures.filter((v) => {
        arr.push(v.url);
      });
      wx.previewImage({
        urls: arr,
      });
    },
    // 获取组局列表
    getGameList() {
      if (this.isMe) {
        return;
      }
      this.params.sortType = 2;
      homeService.gameList(this.params).then((res) => {
        this.list = [...res, ...this.list];
      });
    },
    toChat() {
      // 打开跟用户的聊天
      IM.chatWithUser(this.userInfo.userId);
      // 埋点
      this.$eventRecord(54);
    },
    goGameDetail(game) {
      // 去组局详情
      uni.navigateTo({
        url: "/pages/game/detail?gameId=" + game.gameId,
      });
    },
    showModal(item) {
      if (item.status != 1) {
        return;
      }
      setTimeout(() => {
        drawQrcode({
          width: 180,
          height: 180,
          canvasId: "qrcode",
          text:
            "https://pinbar.vip/h5/supplier/home/?consumeCode=" + item.usedCode,
        });
        this.showCode = true;
      }, 200);
    },
    checkFriends() {
      // 检查好友关系
      IMService.checkFriends(this.myUserId, this.currentUserId).then((res) => {
        this.friendship = res;
      });
    },
    addFriends() {
      // 添加好友
      IMService.addFriends(this.currentUserId).then((res) => {
        this.allConversation.forEach((conversation) => {
          if (conversation.conversationID === "C2C" + this.currentUserId) {
            conversation.isFriend = true;
          }
        });
        this.$toast("关注成功", "success");
        this.checkFriends();
        uni.$emit("friendsUpdate");
      });
      this.$eventRecord(52);
    },
    // 我的优惠券
    couponList() {
      if (!this.isMe) {
        return;
      }
      homeService.myCouponList().then((res) => {
        this.coupon = res || [];
      });
    },
    deleteFriends() {
      // 删除好友
      this.$showModal({
        confirmColor: "#ff0000",
        content: "是否确认取消关注？",
        confirmText: "取消关注",
        cancelColor: "#666",
        cancelText: "再想想",
      }).then(() => {
        IMService.deleteFriends(this.currentUserId).then((res) => {
          this.allConversation.forEach((conversation) => {
            if (conversation.conversationID === "C2C" + this.currentUserId) {
              conversation.isFriend = false;
            }
          });
          this.$toast("已取消关注");
          this.checkFriends();
          uni.$emit("friendsUpdate");
        });
      });
      this.$eventRecord(52);
    },
    resetParms() {
      this.pageNum = 1;
      this.isEnd = false;
      this.list = [];
    },
    myJoinList() {
      if (!this.isMe) {
        return;
      }
      homeService
        .myJoin({
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        })
        .then((res) => {
          "reasdas", res;
          this.list = [...this.list, ...res];
          this.isEnd = (res || []).length < this.pageSize;
        });
    },

    getCurrentUserInfo() {
      LoginService.getUserInfoById(this.currentUserId).then((res) => {
        this.userInfo = res;
        // 处理背景图为空时，赋值默认图
        if (!this.userInfo.pictures || !this.userInfo.pictures.length) {
          this.userInfo.pictures = [
            {
              url: "https://image-1306191496.cos.ap-nanjing.myqcloud.com/825cac5b-c92e-4e2e-b43e-dc14e23ae7f6.png",
            },
          ];
        }
      });
    },
    gotoSet() {
      uni.navigateTo({
        url: "/pagesA/user/set",
      });
    },

    async getUserInfo(e) {
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
      LoginService.bindInfo({
        head: avatarUrl,
        nickName,
      }).then((res) => {
        this.userInfo = {
          ...this.userInfo,
          ...res,
        };
      });
    },
  },
  onReachBottom() {
    if (!this.isEnd) {
      this.pageNum++;
      this.params.pageNum++;
      this.myJoinList();
      this.getGameList();
    }
  },
  filters: {
    formatTime(time) {
      let curentDate = new Date(time);
      let y = curentDate.getFullYear();
      let M = curentDate.getMonth() + 1 + "";
      let D = curentDate.getDate() + "";
      return `${y}-${M.padStart(2, "0")}-${D.padStart(2, "0")}`;
    },
  },
};
</script>

<style scoped lang="scss">
.flex-s {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.flex-c {
  display: flex;
  align-items: center;
}

.page-bg {
  min-height: 100vh;
  width: 100%;
  background-color: #fff;
  .mineBanner_swiper {
    height: 100%;
  }

  .activity-image {
    position: relative;
    height: 512upx;
    width: 100%;
  }
}

.page-ctn {
  position: relative;
  margin: 0px 42upx;
  width: calc(100% - 84upx);

  .card-bg {
    margin: 40upx 0upx 0upx 0upx;
    width: 100%;
    background: #ffffff;
    display: flex;
    justify-content: space-between;

    .man-item-l {
      position: relative;
      overflow: hidden;
      height: 110upx;
      width: 110upx;
      border-radius: 24upx;
      flex: none;

      .header {
        height: 100%;
        width: 100%;
      }
    }

    .man-item-r {
      margin-left: 30upx;
      width: calc(100% - 30upx);

      .name {
        font-size: 30upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #000000;
      }

      .tip {
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;
        padding: 4upx 22upx;
        margin-left: 11upx;
        background: #d8d8d8;
        border-radius: 22upx;
        line-height: 26upx;
      }

      .man-t {
        margin-right: 10upx;
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
      }

      .interest-line {
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
        flex-wrap: wrap;
      }

      .btm-btn {
        margin-top: 6upx;
        display: flex;
        justify-content: flex-end;
      }

      .interest {
        padding: 0 16upx;
        font-size: 26upx;
        line-height: 36upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        margin-left: 12upx;
        background: #d8d8d8;
        border-radius: 18upx;
        margin-top: 6upx;
      }

      .w-btn {
        padding: 6upx 34upx;
        border-radius: 36upx;
        border: 2upx solid #d3d3d3;
        font-size: 32upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #000000;
      }
    }
  }
  .fixed {
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    padding: 30upx 0 70upx 0;
    background-color: #fff;
    display: flex;
    align-items: center;
    z-index: 99999;
    .left,
    .right {
      button {
        height: 82upx;
        border: 2upx solid #000;
        border-radius: 41upx;
        line-height: 82upx;
      }
    }
    .left {
      // flex: 50%;
      display: flex;
      justify-content: flex-end;
      button {
        width: 210upx;
        background-color: #fff;
        color: #000;
      }
    }
    .right {
      // flex: 50%;
      margin: 0 auto;
      button {
        width: 328upx;
        color: #fff;
        margin: 0 28upx;
        background-color: #000;
      }
    }
  }
}

.game-ctn {
  margin: 80upx 42upx 0px 42upx;
  width: calc(100% - 84upx);
  margin-bottom: 224upx;

  .title {
    font-size: 40upx;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #000000;
  }
  .tip {
    text-align: center;
    margin-top: 120upx;
  }

  .game-card {
    margin-top: 28upx;
    padding: 20upx 28upx 38upx 36upx;
    width: 100%;
    background-color: green;
    background: linear-gradient(
      270deg,
      rgba(255, 255, 255, 0) 0%,
      #ffffff 100%
    );
    border-radius: 32upx;
    border: 2upx solid #979797;

    .game-place {
      font-size: 40upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
    }

    .game-time {
      margin-top: 4upx;
      font-size: 32upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #000000;

      .state-success {
        background: rgba(0, 0, 0, 0.4) !important;
      }

      .game-state {
        padding: 8upx 28upx;
        background: #000000;
        border-radius: 30upx;
        font-size: 32upx;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
      }
    }

    .game-tag {
      margin-top: 24upx;
      display: flex;
      flex-wrap: wrap;

      .tab-item {
        margin-right: 8upx;
        padding: 4upx 18upx;
        background: rgba(0, 0, 0, 0.7);
        border-radius: 26upx;
        border: 1px solid #ffffff;
        font-size: 24upx;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
      }
    }
  }
}

.user_button {
  height: 110upx;
  width: 110upx;
  border-radius: 24upx;
}

button[plain] {
  border: none;
  border-color: transparent;
}

.list {
  padding: 32upx 0;

  &-item {
    display: flex;
    width: 100%;
    height: 180upx;
    background: url(https://basic-1258404477.cos.ap-nanjing.myqcloud.com/game/bg.png);
    background-size: cover;
    -moz-background-size: cover;
    /* 老版本的 Firefox */
    background-repeat: no-repeat;

    &-left {
      flex: 3;
      display: flex;
      justify-content: center;
      align-items: center;
      // 48,但是设计师没考虑到小数
      font-size: 72upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ff4f3a;

      text {
        font-size: 32upx;
        position: relative;
        top: 20upx;
      }
    }

    &-right {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      flex: 7;

      .shop-coupon {
        font-size: 32upx;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 600;
        color: #333333;
      }

      .shop-tips {
        margin-top: 16upx;
        font-size: 24upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
      }

      .go-use {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-right: 44upx;
        width: 150upx;
        height: 50upx;
        background: #ff4f3a;
        border-radius: 26upx;
        font-size: 28upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #ffffff;
      }

      .has-used {
        background: #999999;
      }
    }

    .flex-row {
      width: 100%;
      display: flex;
      justify-content: space-between;
    }
  }
}

.list-item + .list-item {
  margin-top: 20upx;
}

.modal {
  width: 640upx;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #ffffff;
  text-align: center;
  padding-bottom: 100upx;

  &-tips {
    margin-top: 100upx;
    margin-bottom: 90upx;
    font-size: 40upx;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #999999;
  }

  &-img {
    width: 328upx;
    height: 328upx;
    text-align: center;
  }
}

.fixed-close {
  position: fixed;
  left: calc(50% - 44upx);
  bottom: 160upx;
  z-index: 999999;

  image {
    width: 88upx;
    height: 88upx;
  }
}

.page-bg .poup-box /deep/ .u-mode-center-box {
  border-radius: 16upx;
  overflow: hidden;
}
</style>
