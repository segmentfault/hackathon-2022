<template>
  <view class="content">
     <view class="custom-bar" @click="goActive(2)" :style="{ height: navHeight + 'px' }">
      <view
        class="custom-bar__wrapper"
        :style="{
          marginTop: searchMarginTop + 'px',
          height: searchHeight + 'px',
          width: searchWidth + 'px',
        }"
      >
        <view class="search-group">
          <u-search
            placeholder="搜索活动名称"
            maxlength=20
            style="width: 100%"
            :show-action="false"
          ></u-search>
        </view>
      </view>
    </view>
    <view class="wrap">
      <u-swiper :list="list" height="410"></u-swiper>
    </view>
    <view class="choose">
      <view class="organization" @click="goActive(1)">
        <view class="or-t1">志愿者活动/组织</view>
        <view class="or-t2">项目组织都在这里</view>
      </view>
      <view class="register" @click="goTask()">
        <view class="or-t1">志愿任务</view>
        <view class="or-t2">像游戏主线一样通关</view>
      </view>
    </view>
    <u-tabs
      class="u-ta u-m-b-40"
      :list="tabList"
      :is-scroll="false"
      :current="current"
      @change="change"
      active-color="#4575F6"
      inactive-color="#333333"
      :gutter="20"
    ></u-tabs>

    <view class="waterfall u-m-l-32 u-m-r-32">
      <u-waterfall v-model="flowList" ref="uWaterfall" add-time="300">
        <template v-slot:left="{ leftList }">
          <view
            class="demo-warter"
            v-for="(item, index) in leftList"
            :key="index"
          >
            <view @click="goVolun(item._id)">
              <!-- 警告：微信小程序中需要hx2.8.11版本才支持在template中结合其他组件，比如下方的lazy-load组件 -->
              <u-lazy-load
                threshold="0"
                border-radius="10" 
                :image="item.img[0]"
                :index="index"
              >
              </u-lazy-load>
              <view class="text u-line-2 u-text-left" >{{ item.title }}</view>
              <view class="msg">
                <view class="u-flex"  >
                  <u-image
                    border-radius="90"
                    width="40rpx"
                    height="40rpx"
                    :src="item.userImage"
                  ></u-image>
                  <view class="name">{{ item.userName }}</view>
                </view>
                <view class="u-flex u-m-r-16">
                   <u-image src="/static/love2.png"
                    :show-loading="false"
                    width="36rpx"
                    height="36rpx"
                    class="u-m-r-4"
                  ></u-image>
                  {{item.likeNumber}}</view>
              </view>
            </view>
          </view>
        </template>
        <template v-slot:right="{ rightList }">
          <view
            class="demo-warter u-m-l-14"
            v-for="(item, index) in rightList"
            :key="index"
          >
            <view @click="goVolun(item._id)">
              <u-lazy-load
                 threshold="0"
                border-radius="10"
                :image="item.img[0]"
                :index="index"
              >
              </u-lazy-load>
              <view class="text u-line-2 u-text-left">{{ item.title }}</view>
              <view class="msg">
                <view class="u-flex">
                  <u-image
                    border-radius="90"
                    width="40rpx"
                    height="40rpx"
                    :src="item.userImage"
                  ></u-image>
                  <view class="name">{{ item.userName }}</view>
                </view>
                 <view class="u-flex u-m-r-16">
                   <u-image src="/static/love2.png"
                    :show-loading="false"
                    width="36rpx"
                    height="36rpx"
                    class="u-m-r-4"
                  ></u-image>
                  {{item.likeNumber}}</view>
              </view>
            </view>
          </view>
        </template>
      </u-waterfall>
    <u-loadmore margin-top="24" margin-bottom="24" :load-text="loadText" :status="loadStatus" @loadmore="addRandomData"></u-loadmore>
    </view>
  </view>
</template>

<script>
const db = wx.cloud.database();
export default {
  data() {
    return {
      loadStatus: "loadmore",
      flowList: [],
      	loadText: {
					loadmore: '没有更多了',
					loading: '努力加载中',
					nomore: '实在没有了'
				},
      tabList: [
        {
          name: "全部",
        },
        {
          name: "动物保护",
        },
        {
          name: "教育",
        },
        {
          name: "环保",
        },
        
      ],
      praise:false,
      current: 0,
      list: [
        {
          image:
            "https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/f3.png?sign=a858bb612a678abbcc50c96b324ade61&t=1655280768",
        },
        {
          image:
            "https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/f2.png?sign=024ff6793c90e30d0658e3e6aa783650&t=1655280804",
        },
        {
          image:
            "https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/4.png?sign=b95c4c364b1a757f9e921e04777ce0b0&t=1655280819",
        },
      ],
      Wlist: [
        {
          image:
            "https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/%E6%B4%BB%E5%8A%A8%E5%9B%BE%E7%89%87/Rectangle%204953-1.jpg?sign=9863edae4652f109b5f6c090d5ddb9bd&t=1654680151",
          msg: "慈善是一个人的行为，公益是一群人的行为。",
          userImg:
            "https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/%E5%A4%B4%E5%83%8F/image%2024.jpg?sign=d4c1e2de7baf83a92c00b61e9de42827&t=1654680275",
          name: "你好",
        },
        {
          image:
            "https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/%E6%B4%BB%E5%8A%A8%E5%9B%BE%E7%89%87/Rectangle%204953.jpg?sign=d900f6e2372f7da202abf74a3724b733&t=1654680213",
          msg: "慈善是一个人的行为，公益是一群人的行为。",
          userImg:
            "https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/%E5%A4%B4%E5%83%8F/image%2025.jpg?sign=872c42f6bfaa2d4bbb4024731487597f&t=1654680296",
          name: "呀呼",
        },
        {
          image:
            "http://pic.sc.chinaz.com/Files/pic/pic9/202002/zzpic23327_s.jpg",
          msg: "慈善是一个人的行为，公益是一群人的行为。",
          userImg:
            "https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/%E5%A4%B4%E5%83%8F/image%2027.jpg?sign=980cfb36b27b6afb4a7c3a2d65a6a04a&t=1654680311",
          name: "嗯哼",
        },
      ],
      voList:[],
      navHeight: '',
      searchMarginTop: 0, // 搜索框上边距
      searchWidth: 0, // 搜索框宽度
      searchHeight: 0, // 搜索框高度
    };
  },
  async onLoad() {
     uni.showLoading({
		  	title: '加载中',
			mask:true
	  });
    this.getVo('全部');
    this.getSystemInfo();
     let res = await wx.cloud.callFunction({
      name: 'getOpenId',
    })
    console.log("ww",res);
    this.getSwiper();
    
  },
  onShow(){
    // this.getVo('全部');
  },
  // onReachBottom() {
  //   this.loadStatus = "loading";
  //   // 模拟数据加载
  //   setTimeout(() => {
  //     this.addRandomData();
  //     this.loadStatus = "loadmore";
  //   }, 1000);
  // },
  methods: {
    getSystemInfo(){
       this.menuButtonInfo = uni.getMenuButtonBoundingClientRect();
    const { top, width, height, right } = this.menuButtonInfo;
         uni.getSystemInfo({
      success: (res) => {
        const { statusBarHeight } = res;
        const margin = top - statusBarHeight;

        this.navHeight = height + statusBarHeight + margin * 2;
        (this.searchMarginTop = statusBarHeight + margin), // 状态栏 + 胶囊按钮边距
          (this.searchHeight = height), // 与胶囊按钮同高
          (this.searchWidth = right - width); // 胶囊按钮右边坐标 - 胶囊按钮宽度 = 按钮左边可使用宽度
      },
    });
    },
    change(index) {
      this.current = index;
      console.log("dfsdfsdf",this.tabList[index].name)
      this.getVo(this.tabList[index].name);
    },
     getSwiper(){
      db.collection('swiper').get().then(res=>{
        this.list=res.data;
      })
        setTimeout(()=>{
      uni.hideLoading();
    },800)
    },
    getVo(data){
      this.$refs.uWaterfall.clear();
      if(data=='全部'){
         db.collection('volunteerShow').get().then(res=>{
        this.Wlist=res.data
        console.log("sb",this.Wlist)
         this.addRandomData();
    
      })
      } else{
          db.collection('volunteerShow').where({
            type:data+'活动'
          }).get().then(res=>{
        this.Wlist=res.data
        console.log("sb",this.Wlist)
         this.addRandomData();
      })
      }
     
    },
    goTask() {
      uni.navigateTo({
        url: "/pagesA/vo-task/vo-task",
      });
    },
    addRandomData() {
      for (let i = 0; i < this.Wlist.length; i++) {
        let index = this.$u.random(0, this.Wlist.length - 1);
        // 先转成字符串再转成对象，避免数组对象引用导致数据混乱
        let item = JSON.parse(JSON.stringify(this.Wlist[index]));
        item.id = this.$u.guid();
        this.flowList.push(item);
      }
    },
    goVolun(data) {
      uni.navigateTo({
        url: `../../../pagesA/volunteer-demeanour/volunteer-demeanour?id=${data}`,
      });
    },
    goActive(data) {
      uni.navigateTo({
        url: `/pagesB/detail/volunteering/volunteering?navId=${data}`,
      });
    },
  },
};
</script>

<style scoped lang="less">
.content {
  background-color: #f4f5fa;
  text-align: center;
  display: flex;
  flex-direction: column;
   min-height: 100vh;
}

.choose {
  display: flex;
  margin-top: 40rpx;
  padding-left: 32rpx;
  padding-right: 32rpx;
}
.custom-bar {
  /* background-color: #aaa; */
  z-index:9999;
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  .custom-bar__wrapper {
    padding: 0 10rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    opacity: 0.6;
    .search-group {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: flex-start;
      align-items: center;
      border-radius: 34rpx;
      padding: 0 10rpx;
    }
  }
}
.v-tabs {
  height: 500rpx;
}

.organization {
  width: 344rpx;
  height: 144rpx;
  background: url(https://636c-cloud1-9gv76r918fe96fa0-1309460587.tcb.qcloud.la/%E9%A6%96%E9%A1%B5%E5%9B%BE%E7%89%87/group2.png?sign=bcaaafb332813ec0d9b2dd5c63dc0c69&t=1654587217)
    no-repeat center;
  background-size: 100% 100%;
}

.or-t1 {
  text-align: left;
  padding-left: 32rpx;
  padding-top: 24rpx;
  color: #ffffff;
  font-size: 32rpx;
  font-weight: 600;
}

.or-t2 {
  text-align: left;
  padding-left: 32rpx;
  padding-top: 8rpx;
  font-weight: 400;
  color: #ffffff;
  font-size: 24rpx;
}

.register {
  width: 344rpx;
  height: 144rpx;
  background: url(https://636c-cloud1-9gv76r918fe96fa0-1309460587.tcb.qcloud.la/%E9%A6%96%E9%A1%B5%E5%9B%BE%E7%89%87/group1.png?sign=34e2851431552737cc14d5725a17f01f&t=1654587198)
    no-repeat center;
  background-size: 100% 100%;
}

.u-ta {
  margin-top: 40rpx;
  margin-left: 32rpx;
  margin-right: 32rpx;
}
.waterfall {
  .demo-warter {
    margin-top: 16rpx;
    background:#ffffff;
  }
  .msg {
    display: flex;
    justify-content: space-between;
     margin-top: 16rpx;
    padding-bottom:16rpx;
    padding-left:16rpx;
    padding-right:16rpx;
  }
  .text {
    margin-left:12rpx;
    margin-top: 16rpx;
    font-size: 28rpx;
    font-weight: 400;
    color: #000000;
  }
  .name {
    padding-left: 8rpx;
    font-size: 24rpx;
  }
}
</style>
