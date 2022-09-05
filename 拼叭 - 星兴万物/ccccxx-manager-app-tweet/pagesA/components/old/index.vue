<template>
  <view class="page-index">
    <!-- 分割线 -->
    <!-- <view class="hr" :style="{ height: headHeight + 'px' }"></view> -->
    <!-- <tips
      v-if="showTips"
      class="page-tips"
      @closeTip="closeTip"
      :joinList="joinListVal"
    ></tips> -->
    <view class="home-title" :slot="content">
      <!-- <image src="../../static/images/me-round.png" class="me" @click="goMyInfo"></image> -->
      <!-- 点击跳转搜索页面 -->
      <!-- <comSearch
        v-show="tab === 0"
        class="search-class"
        :style="marginTop"
        disabled
        @clickSearch="gotoSearch"
      ></comSearch> -->
    </view>
    <home
      :isJoin="isJoin"
      :days="days"
      :index="tab"
      ref="home"
      v-show="tab === 0"
      @joinList="joinList"
    ></home>
    <seek ref="seek" v-if="tab === 2" />
    <im ref="im" v-show="tab === 1"></im>
		<!-- <user ref="user" v-if="tab == 3"/> -->
    <view class="home-footer">
      <!-- 首页 -->
      <button
        class="tab"
        :class="{ selected: tab === 0 }"
        @click="changeTab(0)"
      >
        <image
          :src="`https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/index-home${
            tab === 0 ? '-selected' : ''
          }.png`"
        ></image>
        <text>首页</text>
      </button>
      <!-- 发现 -->
      <!--轮播-->
      <view
        class="swiper-view tab"
        style="height: inherit"
        :class="{ selected: tab === 2 }"
        @click="changeTab(2)"
      >
         <swiper
          class="swiper_container"
          vertical="true"
          autoplay="true"
          circular="true"
          interval="2000"
        >
            <block v-for="(item, index) in isImg" :key="index">
               <swiper-item>
                  <view class="swiper_item"><img :src="item.img" alt=""></view>
                 </swiper-item>
              </block>
           </swiper>
          <text style="padding-top:10upx">发现</text>
      </view>
      <!-- 消息 -->
      <button
        class="tab"
        :class="{ selected: tab === 1 }"
        @click="changeTab(1)"
      >
        <view class="badge" v-if="noReadMsg > 0 || (isCountImRead && getImMenuCount > 0)">{{
            (isCountImRead ? getImMenuCount : noReadMsg) > 99 ? "99+" : (isCountImRead ? getImMenuCount : noReadMsg)
          }}</view>
        <image
          :src="`https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/index-chat${
            tab === 1 ? '-selected' : ''
          }.png`"
        ></image>
        <text>消息</text>
      </button>
      <!-- 我的 -->
      <button
        class="tab"
        :class="{ selected: tab === 3 }"
        @click="changeTab(3)"
      >
        <image
          :src="`https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/me-${
            tab === 3 ? 'black' : 'white'
          }.png`"
        ></image>
        <text>我的</text>
      </button>
    </view>
    <one-key-create
      v-model="oneKeyCreateDialog"
      :applyDetail="applyDetail"
    ></one-key-create>
  </view>
</template>

<script>
import home from './home.vue';
import seek from './seek.vue';
import im from '../im/index.vue';
// import user from './user.vue';
import { config } from '@/framework';
import IMMixin from '../im/lib/mixin.js';
import LoginService from '../../service/login.js';
// import comSearch from '../../components/search.vue';
// import comHead from '../../components/com-head.vue';
// 腾新地图sdk
import QQMapWX from '../../static/qqmap/qqmap-wx-jssdk.min.js';
import oneKeyCreate from '../../components/oneKeyCreate.vue';
// 首页提示// 首页提示
import tips from './index-tips';
import { mapState } from 'vuex';
import UserService from '../../service/user';
import homeService from '../../service/home';
import GameService from '../../service/game.js';
import basic from '../../service/basic';
import gameService from '../../service/game';
import IMService from "../../service/im.js";

export default {
  mixins: [IMMixin],
  components: {
    // comSearch,
    // comHead,
    home,
    seek,
    im,
    tips,
    oneKeyCreate,
		// user
  },
  data() {
    return {
      tab: 0,
      statusBarHeight: config.statusBarHeight,
      // headHeight:
      //   (config.titleBarHeight + uni.getSystemInfoSync().statusBarHeight) / 2,
      headHeight:
        config.titleBarHeight + uni.getSystemInfoSync().statusBarHeight,
      searchStyle: {
        fontSize: '26upx',
        fontFamily: 'PingFangSC-Regular, PingFang SC',
        fontWeight: 400,
      },
      showTips: false,
      joinListVal: undefined,
      oneKeyCreateDialog: false,
      applyDetail: null,
      index: 0,
      days: [
        {
          week: '全部',
          day: '-',
          i: '',
        },
      ],
      isJoin: '',
      shareUserId: '',
      noReadMsg: null,
      isCountImRead: false,
      isImg: [{ img: "https://image-1306191496.cos.ap-nanjing.myqcloud.com/fcde48e2-ec56-4f5a-ba1c-9ee8fe0df64d.png" }, { img: "https://image-1306191496.cos.ap-nanjing.myqcloud.com/93459a2a-81d6-47f1-a738-9cc9d92b1e87.png" }],
    };
  },

  onLoad(options) {
		if(!uni.getStorageSync('userInfo')){
			this.$toast('请先登录')
			setTimeout(() => {
				uni.navigateTo({
					url:`/pagesA/user/login?isOnload=${true}`
				})
			})
			return
		}
		if(options.tab) this.tab = options.tab
		if(options.scene){
			const shareUserId = options.scene.split('shareUserId')[1]
			gameService.bindRelation({parentId:shareUserId,parentType:1})
		}
    this.GetTime();

    // 分享拉新
    if (options.shareUserId) {
      this.shareUserId = options.shareUserId;
    }
    if (this.shareUserId && this.myUserId) {
      this.$authorization();
      GameService.bindRelation({
        parentId: this.shareUserId,
        parentType: 1,
      }).then(() => {
        this.shareUserId = '';
      });
    }
    // 服务端消息
    const pages = getCurrentPages();
    const url = pages[pages.length - 1].$page.fullPath;
    const trackingID = this.$getParam(url, 'trackingID');

    if (trackingID) this.$eventRecord(trackingID);
    //公众号跳转来聊天列表
    if (options.tab && options.tab === 'IM') {
      this.changeTab(1);
    }
    if (options.tab && options.tab == '2') {
      this.changeTab(2);
    }
    config.titleBarHeight + uni.getSystemInfoSync().statusBarHeight;
    // 获取用户当前经纬度
	
	
	uni.getLocation({
	  success: (res) => {
	    let qqmapsdk = new QQMapWX({
	      key: '3Z3BZ-74NL4-UD2UR-DFAWK-BK6C3-7XB7B',
	    });
	    // 逆地址解析
	    qqmapsdk.reverseGeocoder({
	      location: {
	        latitude: res.latitude,
	        longitude: res.longitude,
	      },
	      success: (res) => {
	        // 判断是否在长沙
	        if (res.result.ad_info.city !== '长沙市') {
	          uni.redirectTo({
	            url: '/pageB/out-eervice/out-eervice',
	            success: (res) => {},
	          });
	        }
	      },
	    });
	  },
	

    
      fail: (res) => {
        uni.showModal({
          title: '是否授权定位',
          content: '请确认授权，否则无法使用小程序',
          success: function (tip) {
            if (tip.confirm) {
              uni.openSetting({
                success: (res) => {
                  if (res.authSetting['scope.userLocation'] === false) {
                    uni.redirectTo({
                      url: '/pageB/out-eervice/out-eervice',
                      success: (res) => {},
                      fail: (res) => {},
                    });
                  } else {
                    uni.getLocation({
                      success: (res) => {
                        let qqmapsdk = new QQMapWX({
                          key: '3Z3BZ-74NL4-UD2UR-DFAWK-BK6C3-7XB7B',
                        });
                        // 逆地址解析
                        qqmapsdk.reverseGeocoder({
                          location: {
                            latitude: res.latitude,
                            longitude: res.longitude,
                          },
                          success: (res) => {
                            // 判断是否在长沙
                            if (res.result.ad_info.city !== '长沙市') {
                              uni.redirectTo({
                                url: '/pageB/out-eervice/out-eervice',
                                success: (res) => {},
                                fail: (res) => {},
                              });
                            }
                          },
                        });
                      },
                    });
                  }
                },
                fail: (res) => {
                  res;
                },
              });
            } else {
              uni.redirectTo({
                url: '/pageB/out-eervice/out-eervice',
                success: (res) => {},
                fail: (res) => {},
              });
            }
          },
        });
      },
    });
  },
  onReady() {
    // 一键组局状态参数获取
    // if (uni.getStorageSync("oneKeyCreateDialog")) {
    // } else {
    //   GameService.applyActivityDetail(7).then((activity) => {
    //     this.$eventRecord(118);
    //     this.oneKeyCreateDialog = activity.state === 3 && !activity.gameId;
    //     this.applyDetail = activity;
    //     if (this.oneKeyCreateDialog) {
    //       uni.setStorageSync("oneKeyCreateDialog", 1);
    //     }
    //   });
    // }
  },
  async onShow() {
    let res = wx.getLaunchOptionsSync();
    res.scene = String(res.scene);
    if (res.scene === '1035') {
      this.$eventRecord(84);
    } else {
      this.$eventRecord(64);
    }
    if (this.tab === 1) {
      this.$eventRecord(65);
      this.updateTipsIsShow();
    }
    //TODO::2021.12.3注释。不知道干嘛用
    // this.myJoinList();
    // if (!uni.setStorageSync("gameList")) {
    //   this.$refs.home.getGameList(true);
    // }
    // let res = String(wx.getLaunchOptionsSync()) ;;
    // if (!uni.getStorageSync("showTips")) {
    //   this.showTips = true;
    // }
    // uni.setStorageSync("showTips", true);
    // if (this.tab === 1) {
    //   this.$refs.im.getRedPoint();
    // }
    // // 如果返回到首页没有组局信息则调用本地存储的数据、
    // if (!this.$refs.home.list) {
    //   uni.getStorage({
    //     key: "gameList",
    //     success: (res) => {
    //       this.$refs.home.list = res;
    //     },
    //   });
    // }
    // this.$refs.home.myJoinList();
    // // 由于用户第一次进入没有数据，使用延时器加载数据
    // this.$nextTick(() => {
    //   setTimeout(() => {
    //     if (res.scene === 1035) {
    //       // 埋点
    //       this.$eventRecord(84);
    //     } else {
    //       // 埋点
    //       this.$eventRecord(64);
    //     }
    //   }, 3000);
    // });
    //IM历史记录刷新列表（防止用户点击创建会话后未刷新数据）
    if (this.tab === 1) {
      await this.$refs.im.init();
      this.$refs.im.hisConversation = await this.$refs.im.queryHisConversation();
      this.isCountImRead = true;
    }else {
      await this.totalUnreadCount();
    }
  },
  onHide() {
    ('页面消失了');
  },

  onPullDownRefresh() {
    this.stopRefresh();
  },
  mounted() {
    if (!uni.getStorageSync('showTips')) {
      this.showTips = true;
    }
    uni.setStorageSync('showTips', true);

    // this.$nextTick(() => {
    //   this.$refs.im.getRedPoint();
    //   // 定时获取小红点
    //   setInterval(() => {
    //     //(this.$refs);
    //     this.$refs.im.getRedPoint();
    //   }, 5000);
    // });
  },
  onReachBottom() {
    if(this.tab == 0){
  		this.$refs.home.loadMore()
  	}else if(this.tab == 2){
  		this.$refs.seek.loadMore()
  	}
  },
  methods: {
    myJoinList() {
      let params = {
        pageNum: 1,
        pageSize: 100,
        completes: 1,
      };
      homeService.myJoin(params).then((res) => {
        this.isJoin = res.filter((v) => v.activityId === 3);
      });
    },
    // 获取未来七天时间
    GetTime() {
      var date = new Date();
      var base = Date.parse(date); // 转换为时间戳
      // var year = date.getFullYear(); //获取当前年份
      var mon = date.getMonth() + 1; //获取当前月份
      var week = date.getDay();
      var day = date.getDate(); //获取当前日
      var oneDay = 24 * 3600 * 1000;
      var daytime = `${mon >= 10 ? mon : '0' + mon}${
        day >= 10 ? day : '0' + day
      }${week}`; //今日时间
      this.$data.daytime = daytime; // 今日时间赋值给变量
      var time = `今天-${mon}-${day}`;
      var daytimeArr = [time];
      for (var i = 1; i < 7; i++) {
        //前七天的时间
        var now = new Date((base += oneDay));
        var month = now.getMonth() + 1;
        var mday = now.getDate();
        var week = now.getDay();
        var weeks = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
        daytimeArr.push(
          [
            weeks[week],
            month >= 10 ? month : '0' + month,
            mday >= 10 ? mday : '0' + mday,
          ].join('-')
        );
      }
      daytimeArr.forEach((v, i) => {
        this.days.push({
          week: v.split('-')[0],
          day: `${v.split('-')[1]}-${v.split('-')[2]} `,
          i,
        });
      });
    },
    updateTipsIsShow() {
      UserService.queryUserInfo().then((item) => {
        UserService.getUserMessageInfo().then((messageInfo) => {
          this.$refs.im.isTipsShow = !(
            item.subscription === 1 &&
            messageInfo.gameSuccess &&
            messageInfo.gameStart &&
            messageInfo.gameAudit &&
            messageInfo.imPrivate &&
            messageInfo.imGroup
          );
        });
      });
    },
    joinList(data) {
      this.joinListVal = data;
    },
    async stopRefresh() {
      if (this.tab === 0) {
        this.$refs.home.pageNum = 1;
        await this.$refs.home.init();
        uni.stopPullDownRefresh();
      } else if (this.tab === 1) {
        uni.stopPullDownRefresh();
      } else if (this.tab === 2) {
        await this.$refs.seek.init();
        uni.stopPullDownRefresh();
      }
    },
    // 关闭引导页
    closePage(num) {
      if (num === 0) {
        this.guidance = true;
      }
    },
    goCreate() {
      uni.navigateTo({
        url: '/pagesA/create/index',
      });
    },
    async changeTab(index) {
      if (index === this.tab) return;
      if (index === 0) {
        this.tab = 0;
        // 埋点
        this.$eventRecord(36);
      }
      if (index === 1) {
        // 如果用户没有头像的话 进入消息的时候获取头像
        await this.$authorization();
        this.$refs.im.hisConversation = await this.$refs.im.queryHisConversation();
        let im = this.$refs.im;
        uni.getSetting({
          withSubscriptions: true,
          success(res) {
            im.isDisplay = !Object.keys(res.subscriptionsSetting).includes(
              'NRoVmgyUE5Yiu5ruSVMfbRmNPtTsB4F6gQQiqP7zUT8',
              '5MOgln7ZAZnmC_Ds_c5vnNSgsEctd3F10txHJ5AEsgQ'
            );
            if (im.isDisplay) {
              // 进入消息需要弹出订阅消息授权
              uni.requestSubscribeMessage({
                tmplIds: [
                  'NRoVmgyUE5Yiu5ruSVMfbRmNPtTsB4F6gQQiqP7zUT8',
                  '5MOgln7ZAZnmC_Ds_c5vnNSgsEctd3F10txHJ5AEsgQ',
                ],
                success() {
                  im.isDisplay = false;
                },
                fail() {
                  im.isDisplay = false;
                },
              });
            }
          },
        });
        this.tab = 1;
        await this.$refs.im.init();
        await this.$refs.im.getRedPoint();
        //切换IM只能调用接口获取菜单数量
        // 埋点
        let res = wx.getLaunchOptionsSync();
        if (res.scene === 1157) {
          this.$eventRecord(83);
        } else if (res.scene === 1035) {
          this.$eventRecord(88);
        } else {
          this.$eventRecord(37);
        }
        this.updateTipsIsShow();
        this.isCountImRead = true;
      }
      if (index === 2) {
        this.tab = 2;
				await this.$authorization();
        this.$eventRecord(157);
        // this.$refs.seek.init();
      }
      if (index === 3) {
        // 如果需要登录 加上这一行
				// this.tab = 3;
        await this.$authorization();
				
        uni.navigateTo({
          url: '/pagesA/user/index',
        });
        // 埋点
        this.$eventRecord(38);
      }
      //不是IM界面则在外界调用，否则在内部调用防止im未初始化完成
      if (index !== 1){
        await this.totalUnreadCount();
      }
    },
    closeTip() {
      this.showTips = false;
    },
    // TODO 2期
    gotoSearch() {
      return this.$toast('功能开发中~');
    },
    showShare(gameId, codeUrl = null) {
      basic
        .getShareImg({ businessId: gameId, scene: 1 })
        .then((res) => {
          codeUrl = res.url
        })
      return codeUrl
    },
    // // 去个人页
    // goMyInfo() {
    //   uni.navigateTo({
    //     url: "/pagesA/user/index",
    //   });
    // },
    async totalUnreadCount() {
      if (this.myUserId){
        this.noReadMsg = await IMService.getImMenuCount(this.myUserId);
      }
    }
  },
  computed: {
    ...mapState({
      shareInfo: (state) => state.basic.shareInfo,
    }),
    marginTop() {
      return `margin-top: ${uni.getSystemInfoSync().statusBarHeight + 4}px`;
    },
    getImMenuCount(){
      let res = 0;
      this.allConversation.forEach((v) => {
        res += v.unreadCount;
      });
      let im = this.$refs.im;
      if (im.hisConversation) {
        im.hisConversation.forEach((v) => {
          if (im.isShowConversation(v,this.allConversation) && v.unreadCount) {
            res += v.unreadCount;
          }
        });
      }
      if (im.redpoint){
        res += im.redpoint;
      }
      return res;
      // return this.$refs.im.getMenuCount();
    }
  },
  async onShareAppMessage(res) {
    const _this = this;
    if (res.from === 'menu') {
      if (this.tab == 2) {
        return {
          title: '来拼叭玩剧本/密逃/酒吧，时时拼车，天天打折！',
          path: `pages/index/index?shareUserId=${this.myUserId}&tab=${this.tab}`,
          imageUrl:''
        };
      }else{
				return {
				  title: '',
				  path: `pages/index/index?shareUserId=${this.myUserId}`,
				  imageUrl:'',
				};
			}
    } else {
      let info = res.target.dataset.info;
      // 暂时解决首页分享问题
      let codeUrl = {}
      try {
        codeUrl = await basic.getShareImg({ businessId: info.gameId, scene: 1 }) || {}
      } catch (error) {
        codeUrl = {}
      }
      return {
        title: info.title,
        path: `/pagesA/game/detail?gameId=${info.gameId}`, //?shareUserId=${this.myUserId}&boostUserId=${boostUserId}
        imageUrl: codeUrl.url || info.cover || '',
      };
    }
  },
  onPageScroll(e) {
    let { scrollTop = 0 } = e;
    uni.$emit('onPageScroll', scrollTop);
  },
};
</script>

<style lang="scss" scoped>
.page-index {
  padding-bottom: calc(160upx + env(safe-area-inset-bottom));
  position: relative;
  .page-tips {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 1000;
  }
}

.search-class {
  width: calc(100% - 204upx);
  // margin-top: 50upx;
  margin-bottom: 18upx;
}
.swiper-view {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  border-radius: 5rpx;
  background: tomato;
    .swiper_container { 
      height: 40rpx;
      width: 90%;
      margin-left: 30rpx;
        .swiper_item {
          height: 40rpx;
          width: 108rpx;
          font-size: 26rpx;
          white-space: nowrap;
          display: flex;
          flex-direction: row;
          justify-content: center;
          align-items: center;
          color: yellowgreen;
            img{
             height: 38rpx;
             width: 102rpx; 
            }
}

}
}





.home-title {
  display: flex;
  align-items: center;
  padding: 0 40upx;
  background: #ebe8e8;

  .me {
    width: 68upx;
    height: 68upx;
  }
}

.hr {
  border-top: 1upx solid #e5e5e5;
}

.home-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: $pinbar-primary;
  z-index: 999;
  display: flex;
  align-items: center;
  height: calc(128upx + env(safe-area-inset-bottom));
  padding-bottom: env(safe-area-inset-bottom);

  .img-create {
    width: 100upx;
    height: 100upx;
  }

  .tab {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    font-size: 20upx;
    font-weight: 400;
    color: #939393;
    line-height: 28upx;
    position: relative;
    background: transparent;

    .badge {
      position: absolute;
      left: 110upx;
      top: 0;
    }

    &.selected {
      color: #000;
    }

    image {
      width: 60upx;
      height: 60upx;
    }
  }
}
</style>
