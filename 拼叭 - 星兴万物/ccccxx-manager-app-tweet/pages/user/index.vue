<template>
	<view class="page-bg">
		<comHead hideBack changeColor >
      <v-slot class="com-head">我的主页</v-slot>
    </comHead>
		<view class="activity-image" v-if="userInfo">
			<swiper class="mineBanner_swiper" :indicator-dots="indicatorDots" :autoplay="autoplay" :interval="interval"
				:duration="duraetion" :indicator-color="indicatorColor" :indicator-active-color="indicatorActiveColor">
				<swiper-item v-for="(item,index) in userInfo.pictures" :key='index'>
					<image style="hight: 100%; width: 100%" :src="item.url" mode="aspectFill" @click='preview(item.url)'>
				</swiper-item>
			</swiper>
			<view class="set-btn" @click="gotoSet">
				<image class="set-icon" src="http://image-1306191496.cos.ap-nanjing.myqcloud.com/f4a36f67-595b-47d7-9490-fe06d9e3f71d.png"/>
			</view>
		</view>

		<view class="user-card">
			<image class="user-head" v-if="userInfo.head" :src="userInfo.head" mode="aspectFill"></image>
			<image class="user-head" v-else
				src="http://image-1306191496.cos.ap-nanjing.myqcloud.com/476a1bd0-4b48-4d3b-a2eb-5a13a5f20664.png"></image>
			<view class="user-ctn">
				<view class="user-name">
          <image v-if="userType==1" class="user-state" src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/0236e5c3-1124-4006-ae79-d4b6d73e249d.png"></image>
          <image v-if="userType==2" class="user-state" src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/c364e5ae-3ac2-4b30-bdb5-551f0098a4ae.png"></image>
					{{ userInfo.nickName }}
          <view class="edit-btn" @click="gotoEdit">
				<image class="set-icon" src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/52c59754-071e-4302-aff1-d5cd6efa3a3e.png"/>
			</view>
      <view class="share" @click="share">	<image class="set-icon" src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/0c739cff-3113-4e22-a871-81bd1ab4766b.png"/></view>
				</view>
				<view class="user-tag flex-c">
					<view class="tag">长沙市</view>
					<view v-if="userInfo.age" class="tag">{{ userInfo.age }}岁</view>
					<view v-if="userInfo.sex != undefined || userInfo.sex != null" class="tag">{{
            userInfo.sex == 1 ? "男" : "女"
          }}</view>
					<view v-if="userInfo.constellation" class="tag">{{
            userInfo.constellation
          }}</view>
				</view>
				<view class="like flex-c">
					<view class="like-t">兴趣</view>
					<view v-show="userInfo.interests.length" class="like-item" v-for="(item,i) in userInfo.interests" :key="i">
						{{ item }}</view>
				</view>
			</view>
		</view>
		<view class="big-coupon-tip">
			<view class="coupon-tip" @click="gotoCoupon">
				<view class="flex-c coupon-tip-l">我的优惠券
				</view>
				<view class="flex-c coupon-tip-r">
					<u-icon class="arrow-right" color="#999999" name="arrow-right"></u-icon>
				</view>
			</view>
		</view>
    <view class="coupon-tip" @click="gotoLaxin" v-if="false">
			<view class="flex-c coupon-tip-l">我的团队</view>
			<view class="flex-c coupon-tip-r">
					<u-icon class="arrow-right" color="#999999" name="arrow-right"></u-icon>
				</view>
		</view>
		<view class="coupon-tip" @click="lookOrder">
			<view class="flex-c coupon-tip-l">我的订单
			</view>
			<view class="flex-c coupon-tip-r">
				<u-icon class="arrow-right" color="#999999" name="arrow-right"></u-icon>
			</view>
		</view>
		
		<view class="coupon-tip" @click="accountCenter">
			<view class="flex-c coupon-tip-l">账户中心
				<!-- <view class="red-yuan"></view> -->
			</view>
			<view class="flex-c coupon-tip-r">
				<text class="color-666 fs28">我的收益/邀请/团队</text>
				<u-icon class="arrow-right" color="#999999" name="arrow-right"></u-icon>
			</view>
		</view>
		<view class="invite-card">
			<view class="invite-card-t">组局信息</view>
			<view class="game-item" @click="goDetail(item)" v-for="item in gameList" :key="item.gameId"
				v-show="item.self.state === 1">
				<image :src="item.cover"></image>
				<view class="game-item-bg">
					<view class="game-item-title">{{ item.title }}</view>
					<view class="game-item-time">
						<view class="game-item-time-v">{{ $formatGameTime(item.gameStartTime) }}开始</view>
						<view class="game-item-time-state">{{ state[item.complete] }}</view>
					</view>
					<view class="game-item-type">
						<view class="type-item" v-if="item.self && item.self.identity == 1">我是局主</view>
						<view class="type-item">{{ item.typeName }}</view>
					</view>
				</view>
			</view>
			<view class="no-game" v-if="!gameList.length">
				<view class="tip">~年轻人要去参加活动，别总肥宅~</view>
				<view class="activity" @click="go(1)">
					<view>发起组局</view>
					<image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/75d0c616-c00b-400e-845e-d00714aa0a4e.png"></image>
				</view>
				<view class="activity" @click="go(2)">
					<view>平台活动</view>
					<image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/75d0c616-c00b-400e-845e-d00714aa0a4e.png"></image>
				</view>
				<view class="activity" @click="go(3)">
					<view>看看别人的局</view>
					<image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/75d0c616-c00b-400e-845e-d00714aa0a4e.png"></image>
				</view>
			</view>
		</view>
    <share
      v-if="shareDialog"
      codeUrl="https://www.baidu.com/?tn=02003390_14_hao_pg"
      @cancel="shareDialog = false"
    />
     <!-- 底部导航栏 -->
    <view-tabbar v-model="tableIndex"></view-tabbar>
	</view>
   
</template>

<script>
import Share from "./components/userShare-card.vue";
import pageMix from '@/framework/mixins/pageMix';
import comHead from "@/components/com-head.vue";
import userService from "@/service/user.js";
import IM from '../im/lib/mixin';
export default {
  mixins: [pageMix, IM],
  components: {
    comHead,
    Share,
  },
  data() {
    return {
      tableIndex: 3,
      shareDialog: false,
      pageNum: 1,
      userInfo: {}, //用户信息
      gameList: [], //我的组局大厅
      userNum: {}, //用户拉新统计
      state: [
        //1已通过 2待通过 3自退 4未通过 5被踢
        "",
        "待开始",
        "已开始",
        "已结束",
        "已解散",
        "被踢",
      ],
      imgList: [
        "../../static/images/bg.png",
        "../../static/images/bg.png",
        "../../static/images/bg.png",
      ],
      pictures: [],
      indicatorDots: true,
      autoplay: false,
      interval: 2000,
      duration: 500,
      indicatorColor: "rgba(255,255,255,.5)",
      indicatorActiveColor: "rgba(255,255,255,1)",
    };
  },
  async onShow() {
    this.init();
    // 埋点
    let res = wx.getLaunchOptionsSync();
    res.scene = String(res.scene);
    this.$nextTick(() => {
      setTimeout(() => {
        if (res.scene === "1001") {
          this.$eventRecord(66);
        }
      }, 3000);
    });
  },
  onShareAppMessage() {
		return {
			title: '来拼叭玩剧本/密逃/酒吧，时时拼车，天天打折！',
			path: `pages/index/index?shareUserId=${this.myUserId}`,
			imageUrl:'https://image-1306191496.cos.ap-nanjing.myqcloud.com/087478f7-fddf-4538-ba72-e95218dee63c.png',
		}
	},
  onLoad(options) {
    if (!uni.getStorageSync('userInfo')) {
      this.$toast('请先登录');
      setTimeout(() => {
        uni.navigateTo({
          url: `/pagesA/user/login?isOnload=${true}`,
        });
      });
      return;
    }
    // 销售关系
    this.getBindRelation(options);
    // 分享拉新关系
    this.getshareInfo(options);
    // 获取用户当前经纬度
    this.getLocation();
    // 服务端消息
    const pages = getCurrentPages();
    const url = pages[pages.length - 1].$page.fullPath;
    const trackingID = this.$getParam(url, "trackingID");
    if (trackingID) {
      this.$eventRecord(trackingID);
    } else {
      this.$eventRecord(66);
    }
    if(this.allConversation.length) { 
      this.queryHisConversation(this.allConversation)
    }
  },
  onReachBottom() {
    // this.pageNum++;
    // this.queryGameList();
  },
	onPullDownRefresh() {
		this.init()
	},
  computed:{
    userType(){
      return this.$store.state.user.identityType;
    }
  },
  mounted(){
      if (uni.getStorageSync('userInfo')){
         this.$store.commit('setIdentityType')
      }
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
  methods: {
    share() {
      this.shareDialog = true;
    },
    lookOrder() {
      this.$eventRecord(248);
      uni.navigateTo({
        url: "/pagesB/order/myHomepage",
      });
    },
    accountCenter() {
      this.$eventRecord(249);
      uni.navigateTo({
        url: "/pagesB/accont/accont",
      });
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
    go(val) {
      switch (val) {
        case 1:
          uni.navigateTo({
            url: "/pagesA/create/index",
          });
          // 埋点
          this.$eventRecord(33);
          break;
        case 2:
          uni.redirectTo({
            url: "/pages/activity/discount?activityId=4",
          });
          // 埋点
          this.$eventRecord(34);
          break;
        case 3:
          uni.redirectTo({
            url: "/pages/index/index",
          });
          // 埋点
          this.$eventRecord(35);
          break;

        default:
          break;
      }
    },
    init() {
      this.queryUserInfo();
      this.queryGameList();
      this.queryUserTotal();
    },
    gotoSet() {
      uni.navigateTo({
        url: "/pagesA/user/set",
      });
    },
    gotoEdit() {
      uni.navigateTo({
        url: "/pagesA/user/edit",
      });
      // 埋点
      this.$eventRecord(60);
    },
    queryUserInfo() {
      return userService.queryUserInfo().then((res) => {
        this.userInfo = res || {};
        // 处理头像
        if (
          this.userInfo.head &&
          this.userInfo.head.startsWith("https://image-1306191496")
        ) {
          this.userInfo.head += "?imageView2/1/w/200/h/200/q/80/webp";
        }
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
    queryUserTotal() {
      return userService.queryUserTotal().then((res) => {
        this.userNum = res || {};
      });
    },
    queryGameList() {
      return userService
        .queryGameList({
          pageNum: this.pageNum,
          pageSize: 100,
          states: "1,2",
        })
        .then((res) => {
          //  ("resawesdf",res);
          res.filter((v, i) => {
            if (v.self.state !== 1) {
              res.splice(i, 1);
              return v;
            }
            if (v.complete === 1) {
              res.splice(i, 1);
              res.unshift(v);
            }
            // 图片处理
            v.cover = this.$pictureUrl(v.cover, 666, 222);
          });
          this.gameList = res;
        });
    },
    goDetail(data) {
      uni.navigateTo({
        url: "/pagesA/game/detail?gameId=" + data.gameId,
      });
      // 埋点
      this.$eventRecord(32);
    },
    gotoCoupon() {
      uni.navigateTo({
        url: "/pagesA/user/coupon",
      });
      // 埋点
      this.$eventRecord(30);
    },
    gotoLaxin() {
      uni.navigateTo({
        url: "/pagesA/user/invite",
      });
      // 埋点
      this.$eventRecord(31);
    },
    // async getUserInfo(e) {
    //   let userInfo = {};
    //   if (this.canIUseGetUserProfile) {
    //     let res = (await uni.getUserProfile({ desc: "用于完善资料" })) || [];
    //     userInfo = (res[1] && res[1].userInfo) || {};
    //   } else {
    //     userInfo = e.detail.userInfo || {};
    //   }
    //   let { avatarUrl, nickName } = userInfo || {};
    //   this.$checkLogin()
    //     .then((res) => {
    //       LoginService.bindInfo({ head: avatarUrl, nickName }).then(
    //         (res) => {}
    //       );
    //     })
    //     .catch((e) => {
    //       uni.navigateTo({ url: "./login" });
    //     });
    // },
  },
};
</script>

<style lang="scss" scoped>
.com-head{
 padding-left: 75upx;
 padding-top: 23upx;
}
.flex-c {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}
.tab-wrapper {
  padding: 26upx 40upx 20upx 40upx;
  position: relative;
}
.page-bg {
  position: relative;
  min-height: 100vh;
  width: 100%;
  background: #f5f5f5;
  padding-bottom: 150upx;
  .mineBanner_swiper {
    height: 100%;
  }

  .activity-image {
    position: relative;
    height: 512upx;
    width: 100%;

    .set-btn {
      position: absolute;
      z-index: 99;
      right: 42upx;
      bottom: 70upx;
      font-size: 26upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #ffffff;
      display: flex;
      align-items: center;
      .set-icon {
        height: 48upx;
        width: 48upx;
      }
    }
    .share {
    }

    .set-btn {
      bottom: 70upx;
    }

    .edit-btn {
      left: 320upx;
    }
  }

  .user-card {
    position: relative;
    z-index: 22;
    padding: 32upx 28upx 28upx 28upx;
    margin: -32px 20upx 0px 20upx;
    background-color: #fff;
    border-radius: 16upx;
    display: flex;

    .user-head {
      height: 110upx;
      width: 110upx;
      border-radius: 24upx;
      object-fit: cover;
      object-position: center;
      flex: none;
    }

    .user-ctn {
      margin-left: 30upx;
      width: calc(100% - 140upx);

      .user-name {
        font-size: 30upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #333333;
        display: flex;
        align-items: center;
        .user-state{
          width: 72upx;
          height: 66upx;
          margin-right: 2upx;
        }
        .edit-btn {
          margin-left: 28upx;
          image {
            width: 48upx;
            height: 48upx;
          }
        }
        .share {
          margin-left: 28upx;
          image {
            width: 48upx;
            height: 48upx;
          }
        }

        .tips {
          font-size: 26upx;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #333333;
          padding: 2upx 22upx;
          display: inline;
          background: #d8d8d8;
          border-radius: 22upx;
          margin-left: 22upx;
        }
      }

      .user-tag {
        margin-top: 16upx;
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;

        .tag {
          margin-right: 20upx;
        }
      }

      .like {
        margin-top: 18upx;
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;
        line-height: 36upx;

        &-t {
          margin-right: 44upx;
          height: 36upx;
        }

        &-item {
          height: 36upx;
          margin-right: 12upx;
          padding: 0px 16upx;
          background: #d8d8d8;
          border-radius: 18upx;
          background: #d8d8d8;
        }
      }
    }
  }

  .coupon-tip {
    position: relative;
    margin: 20upx 20upx 0px 20upx;
    padding: 36upx 40upx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: #ffffff;
    border-radius: 16upx;

    &-l {
      position: relative;
      font-size: 40upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
      line-height: 56upx;

      .red-yuan {
        position: absolute;
        height: 22upx;
        width: 22upx;
        top: 4px;
        right: -26upx;
        background: #ff0000;
        border-radius: 50%;
      }
    }

    &-r {
      font-size: 32upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #999999;
      line-height: 44upx;

      .arrow-right {
        margin-left: 12upx;
        margin-top: 2upx;
      }
    }
  }
  .big-coupon-tip {
    border-radius: 16upx;
    background: #fff;
    margin: 20upx 20upx 0 20upx;
    .coupon-tip {
      margin: 0;
    }
  }
  .invite-card {
    position: relative;
    margin: 20upx 20upx 20upx 20upx;
    width: calc(100% - 40upx);
    padding: 30upx 40upx 32upx 40upx;
    background: #ffffff;
    border-radius: 16upx;

    &-t {
      font-size: 40upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
      line-height: 56upx;
    }

    .invite-item {
      margin-top: 58upx;
      flex: 1;

      &-n {
        font-size: 40upx;
        text-align: center;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #ff0000;
        line-height: 56upx;
      }

      &-t {
        margin-top: 18upx;
        text-align: center;
        font-size: 28upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #666666;
        line-height: 40upx;
      }
    }

    .invite-btn {
      height: 70upx;
      width: 256upx;
      margin-top: 40upx;
      position: relative;
      margin-left: auto;
      margin-right: auto;
      display: block;
    }

    .game-item {
      position: relative;
      overflow: hidden;
      margin-top: 28upx;
      padding: 20upx 24upx 40upx 20upx;
      background: linear-gradient(
        270deg,
        rgba(255, 255, 255, 0) 0%,
        #ffffff 100%
      );
      border-radius: 32upx;
      border: 2upx solid #979797;

      image {
        filter: blur(5px);
        opacity: 0.5;
        position: absolute;
        height: 100%;
        width: 100%;
        top: 0px;
        left: 0px;
        right: 0px;
        bottom: 0px;
      }

      &-bg {
        position: relative;
        z-index: 20;
      }

      &-title {
        font-size: 40upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #000000;
        line-height: 56upx;
      }

      &-time {
        display: flex;
        align-items: center;
        justify-content: space-between;

        &-v {
          font-size: 32upx;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #000000;
          line-height: 44upx;
        }

        &-state {
          height: 60upx;
          padding: 0px 24upx;
          border-radius: 30upx;
          line-height: 60upx;
          font-size: 32upx;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #ffffff;
          background: #000000;
        }
      }

      &-type {
        display: flex;
        flex-wrap: wrap;

        .type-item {
          margin-right: 10upx;
          margin-top: 14upx;
          padding: 0 16upx;
          background-color: rgba(0, 0, 0, 0.7);
          border-radius: 26upx;
          // border: 2upx solid #FFFFFF;
          font-size: 24upx;
          line-height: 44upx;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #ffffff;
        }
      }
    }
  }

  .no-game {
    color: #000;
    font-weight: 700;

    .tip {
      text-align: center;
      font-size: 28upx;
      font-weight: 400;
      color: #666666;
      margin: 60upx 0;
    }

    .activity {
      width: 100%;
      height: 120upx;
      margin-bottom: 16upx;
      border-radius: 16upx;
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: linear-gradient(270deg, #fffbfc 0, #f8e4e6 100%);

      image {
        width: 52upx;
        height: 36upx;
        margin-right: 46upx;
      }

      view {
        margin-left: 46upx;
      }
    }
  }
}

</style>
