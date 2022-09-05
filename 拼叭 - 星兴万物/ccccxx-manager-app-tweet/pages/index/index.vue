<template>
  <view class="page-container">
    <tips
      v-if="showTips"
      class="page-tips"
      @closeTip="closeTip"
      :Top="searchMarginTop + searchHeight + 17"
    ></tips>
    <u-sticky>
      <view class="head-search" :style="{ paddingTop: searchMarginTop + 'px' }">
        <view class="find">首页</view>
        <view
          class="search"
          :style="{ width: searchWidth + 'px', height: searchHeight + 'px' }"
        >
          <d-search :kewords="keywords" placeholder="请输入活动/入驻商家" />
        </view>
      </view>
    </u-sticky>

    <view class="page-main">
      <!-- 卡片 -->
      <view class="home-box-card">
        <image
          class="home-box-card-item"
          @click="$u.throttle(getuserinfo, 500)"
          src="http://image-1306191496.cos.ap-nanjing.myqcloud.com/ce33c360-73f6-403c-9950-87a9cfb27f5d.png"
        />
        <image
          class="home-box-card-item"
          @tap="establish()"
          src="http://image-1306191496.cos.ap-nanjing.myqcloud.com/12c8eb5f-9bb9-421a-acc6-4af594b1471a.png"
        />
      </view>
      <!-- 筛选条件 -->
      <u-sticky :offset-top="searchMarginTop * 2 + searchHeight * 2">
        <view class="home-top">
          <view class="tab-bg">
            <u-tabs
              custum-tab
              :list="tabs"
              height="72"
              bar-width="52"
              font-size="30"
              active-color="#000000"
              bold
              inactive-color="#999999"
              bg-color="#FFFFFF"
              :bar-style="barStyle"
              :current="current"
              @change="changeTab"
            />
          </view>
          <view class="more-btn" @click="categoryShow = true">
            <view class="more-btn-bg">
              筛选
              <image
                class="more-icon"
                src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/6042326b-84d6-447d-9f2e-30bf09dd9687.png"
              ></image>
            </view>
          </view>
        </view>
        <view class="newData">
          <view
            class="date"
            v-for="(v, i) in days"
            :key="i"
            :class="{ checked: v.i === searchParm.days }"
            @click="selectDate(v.i)"
          >
            <view class="week checked_text">{{ v.week }}</view>
            <view class="day checked_text">{{ v.day }}
              {{v.i}}
            </view>
          </view>
        </view>
      </u-sticky>
      <view
        class="card-item"
        style="padding-left: 22upx"
        v-for="(item, index) in list"
        :key="index"
      >
        <barCard @goDetail="goDetail" :bar="item" :isShow="false"></barCard>
      </view>
      <view class="smallTip" v-if="!list.length">
        <div class="top">
          <image
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/e08bc21e-f918-4791-923b-d6b6b63260a3.png"
          ></image>
        </div>
        <view class="bottom">
          <view>暂无相关组局，自己发个组局试试</view>
        </view>
      </view>
      <view class="empty-tip" style="margin-bottom: 100upx" v-if="isEnd">
        <image
          class="empty-icon"
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/empty-icon.png"
        ></image>
        已经到底了呢！
      </view>
    </view>
    <u-popup
      v-model="categoryShow"
      mode="bottom"
      border-radius="32"
      :mask-custom-style="maskCustomStyle"
    >
      <category
        class="p-top"
        :tabs="tabs"
        @subParam="subParam"
        @changeTab="changeTab"
      ></category>
    </u-popup>
    <view
      class="create-circle"
      @tap="$u.throttle(getuserinfo, 500)"
      v-if="scrollTop >= 100"
      :scroll-top="scrollTop"
    >
      <view style="font-size: 40rpx">+</view>
      <view>创建局</view>
    </view>
    <!-- 底部导航栏 -->
    <view-tabbar v-model="tableIndex"></view-tabbar>
  </view>
</template>
<script>
import pageMix from '@/framework/mixins/pageMix';
import dSearch from '@/components/d-search';
import homeService from '@/service/home';
import barCard from '@/components/barCard.vue';
import category from '@/components/category.vue';
import tips from '@/components/index-tips';
import IM from '../im/lib/mixin';
export default {
  mixins: [pageMix, IM],
  components: {
    dSearch,
    barCard,
    category,
    tips,
  },
  data() {
    return {
      showTips: false,
      scrollTop: 0,
      tableIndex: 0,
      menuButtonInfo: {},
      searchMarginTop: 0,
      searchHeight: 0,
      searchWidth: 0,
      categoryShow: false,
      list: [], // 卡片列表
      tabs: [], // 标签
      days: [
        {
          week: '全部',
          day: '-',
          i: '',
        },
      ],
      searchParm: {
        days: '',
      },
      keywords: null,
      current: 0,
      pageNum: 1,
      typeId: '',
      pageSize: 10,
      isEnd: false,
      daysName: '',
      barStyle: {
        background: 'linear-gradient(180deg, #CD6CFF 0%, #FF4F3A 100%)',
      },
      maskCustomStyle: {
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
      },
    };
  },
  watch: {
    current(val) {
      this.searchParm.days = '';
    },
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
  mounted() {
    this.daysName = this.GetDateStr();
    if (uni.getStorageSync('showTips')) {
      this.showTips = true;
      uni.setStorageSync('showTips', false);
    }
  },
  onLoad(options) {
    this.keywords = options.keywords || null;
    //搜索栏到头部距离
    this.menuButtonInfo = uni.getMenuButtonBoundingClientRect();
    const { top, width, height, right } = this.menuButtonInfo;
    uni.getSystemInfo({
      success: (res) => {
        const { statusBarHeight } = res;
        const margin = top - statusBarHeight;
        this.searchMarginTop = statusBarHeight + margin;
        this.searchHeight = height;
        this.searchWidth = right - 1.9 * width;
      },
    });
		this.refershParams();
		this.GetTime();
		// 初始化数据
		this.initData();
    // this.refershParams();
    if (uni.getStorageSync('userInfo')) {
      this.getTrackingID();
      // 销售关系
      this.getBindRelation(options);
      // 分享拉新关系
      this.getshareInfo(options);
      // 获取用户当前经纬度
      this.getLocation();
			// IM 已经初始化调用历史IM历史消息
			if(this.allConversation.length) { 
			  this.queryHisConversation(this.allConversation)
			}
    }
  },
  onShow() {
    let res = wx.getLaunchOptionsSync();
    res.scene = String(res.scene);
    if (res.scene === '1035') {
      this.$eventRecord(84);
    } else {
      this.$eventRecord(64);
    }
    this.$eventRecord(65);
  },
  methods: {
    closeTip() {
      this.showTips = false;
    },
    refershParams() {
      this.current = 0;
      this.list = [];
      this.pageNum = 1;
      this.typeId = '';
      this.isEnd = false;
      this.searchParm = {
        days: '',
      };
    },
    subParam(e) {
      this.keywords = null;
      this.categoryShow = !this.categoryShow;
      this.resetParms();
      this.searchParm = e;
      //(e);
      this.searchParm.typeId && (this.typeId = this.searchParm.typeId);
      this.getGameList();
    },
    establish() {
      this.$toast('功能开发中~');
      // 埋点
      this.$eventRecord(27);
    },
    // 前往创建局 先获取用户信息
    async getuserinfo() {
			const isAuth = this.$authorization()
			if(!isAuth) return
      uni.navigateTo({
        url: '/pagesA/create/index',
      });
      // 埋点
      this.$eventRecord(26);
    },
    // 首页跳转详情埋点
    goDetail() {
      this.$eventRecord(29);
    },
    // 获取未来七天时间
    GetTime() {
      this.days = [
        {
          week: '全部',
          day: '-',
          i: '',
        },
      ];
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
    async selectDate(v) {
      this.$eventRecord(178);
      this.isCheck = false;
      this.keywords = null;
      this.searchParm.days = v;
      let params = {
        pageNum: 1,
        pageSize: 10,
        typeId: this.typeId !== 0 ? this.typeId : '',
        days: this.searchParm.days,
        sortType: 1,
      };
      homeService.gameList(params).then((res) => {
        res.forEach((v) => {
          this.$pictureUrl(v.cover, 375, 360);
        });
        this.list = res;
      });
    },
    resetParms() {
      this.list = [];
      this.pageNum = 1;
      this.typeId = this.tabs[this.current]['typeId'];
      this.isEnd = false;
      this.searchParm = {};
    },
    changeTab(e) {
      this.keywords = null;
      this.isCheck = false;
      this.isEnd = false;
      this.current = Number(e);
      this.resetParms();
      this.getGameList();
      // 埋点
      this.$eventRecord(28);
    },
    async getTypeList() {
      let res = await homeService.typeList();
      this.tabs = [
        {
          name: '推荐',
          typeId: 0,
        },
        ...res,
      ];
      this.typeId = '';
    },
    // 组局大厅的列表接口
    async getGameList(type) {
      let parms = {
        pageNum: this.pageNum,
        pageSize: 10,
        sortType: 1,
      };
      if (this.keywords) parms.keyword = this.keywords;
      this.typeId && (parms.typeId = this.typeId);
      parms = {
        ...parms,
        ...this.searchParm,
        sortType: 1,
      };
      let res = await homeService.gameList(parms);
      this.isLoading = false;
      const current = Date.now();
      //(res);
      res.forEach((v, i) => {
        if (v.minPeople - 1 === v.members.length) {
          v.status = '1'; // 还差一人
        } else if (v.minPeople <= v.members.length) {
          //("达到最低人数");
          v.status = '2'; // 达到最低人数
        } else if (v.maxPeople === v.members.length) {
          //("达到最大人数");
          v.status = '3'; //  达到最大人数
        } else {
          v.states = '99';
        }
        if (v.status === '1') {
          res.splice(i, 1);
          res.unshift(v);
        }
        // 图片裁剪
        v.cover = this.$pictureUrl(v.cover, 666, 220);
        v.members.forEach((item, index) => {
          // 处理头像
          if (item.head && item.head.startsWith('https://image-1306191496')) {
            item.head += '?imageView2/1/w/80/h/80/q/80/webp';
          }
          // 群主默认第一
          if (item.identity === 1) {
            v.members.splice(index, 1);
            v.members.unshift(item);
          }
        });
      });
      if (type === 'onReachBottom') {
        this.list = this.list.concat(res);
      } else {
        this.list = res;
      }
      this.isEnd = (res || []).length < this.pageSize;
      if (!this.list.length) this.isEnd = false;
    },
    getWeek(addDay) {
      const dd = new Date();
      dd.setDate(dd.getDate() + addDay);
      var m = dd.getMonth() + 1; //获取当前月份
      var d = dd.getDate(); //获取当前月份的日期
      m = m < 10 ? '0' + m : m;
      d = d < 10 ? '0' + d : d;
      return { m, d };
    },
    GetDateStr() {
      const dd = new Date();
      const weekDays = dd.getDay();
      switch (weekDays) {
        case 0:
          return `${this.getWeek(3).m}.${this.getWeek(3).d}`;
        case 1:
          return `${this.getWeek(2).m}.${this.getWeek(2).d}`;
        case 2:
          return `${this.getWeek(1).m}.${this.getWeek(1).d}`;
        case 3:
          return `${this.getWeek(0).m}.${this.getWeek(0).d}`;
        case 4:
          return `${this.getWeek(6).m}.${this.getWeek(6).d}`;
        case 5:
          return `${this.getWeek(5).m}.${this.getWeek(5).d}`;
        case 6:
          return `${this.getWeek(4).m}.${this.getWeek(4).d}`;
      }
    },
    initData() {
      // 获取标签
      this.getTypeList();
      this.getGameList();
    },
    async getTypeList() {
      let res = await homeService.typeList();
      this.tabs = [
        {
          name: '推荐',
          typeId: 0,
        },
        ...res,
      ];
      this.typeId = '';
    },
  },
  onShareAppMessage(res) {
    return {
      title: '来拼叭玩剧本/密逃/酒吧，时时拼车，天天打折！',
      path: `pages/index/index?shareUserId=${this.myUserId}`,
      imageUrl:
        'https://image-1306191496.cos.ap-nanjing.myqcloud.com/087478f7-fddf-4538-ba72-e95218dee63c.png',
    };
  },
  onReachBottom() {
    if (!this.isEnd) {
      this.pageNum++;
      this.getGameList('onReachBottom');
    }
  },
  onPullDownRefresh() {
    this.isEnd = false;
    this.list = [];
    this.pageNum = 1;
    this.getGameList();
  },
  onPageScroll(e) {
    this.scrollTop = e.scrollTop;
  },
};
</script>
<style lang="scss" scoped>
.page-container {
  position: relative;
  padding: 0upx 21upx 26upx 0;
  .head-search {
    display: flex;
    padding-bottom: 20upx;
    align-items: center;
    background: #ffff;
    .find {
      padding-left: 20upx;
      font-size: 40upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
    }
    .search {
      width: 450upx;
      margin-left: 20upx;
    }
  }
  .page-main::-webkit-scrollbar {
    height: 0;
    width: 0;
    color: transparent;
  }
  .page-main {
    padding-top: 20upx;
    height: 100%;
    overflow: scroll;
    .home-box-card {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      margin-bottom: 37upx;
      padding-left: 21upx;
      .home-box-card-item {
        width: 340upx;
        height: 120upx;
        flex-basis: 340upx;
      }
    }
    .home-top {
      display: flex;
      background: #fff;
      .tab-bg {
        height: 94upx;
        width: calc(100% - 110upx);
      }
      .more-btn {
        height: 94upx;
        width: 110upx;
        display: flex;
        justify-content: flex-end;
        font-size: 24upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #666666;
        box-shadow: -5px 0px 4px -4px #e1e1e1;
        // background: #ebe8e8;
        background: #fff;
        margin-top: -17rpx;

        .more-btn-bg {
          display: flex;
          align-items: center;
        }

        .more-icon {
          height: 32upx;
          width: 32upx;
          margin-top: 4upx;
          margin-left: 7upx;
        }
      }
    }
  }
  .newData {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 18rpx;
    padding-left: 27rpx;
    padding-bottom: 18upx;
    background: #fff;
    .date {
      background: #fff4f6;
      text-align: center;
      border-radius: 8rpx;
      font-size: 24rpx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      width: 76rpx;
      height: 88rpx;
      margin-right: 10rpx;
      display: flex;
      flex-direction: column;
      justify-content: center;
      .week {
        color: #ff6d6d;
      }
      .day {
        color: #813939;
      }
    }
    .checked {
      background-color: #ff5665;
      .checked_text {
        color: #fff;
      }
    }
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
}
.page-tips {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 1000;
}
.create-circle {
  position: fixed;
  background: #000000;
  right: 10upx;
  font-size: 20rpx;
  line-height: 30upx;
  bottom: 300upx;
  text-align: center;
  color: #fff;
  font-weight: bold;
  padding: 30upx;
  border-radius: 100%;
}
</style>
