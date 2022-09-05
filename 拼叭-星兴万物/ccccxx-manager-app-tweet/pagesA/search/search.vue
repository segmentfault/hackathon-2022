<template>
  <view class="box">
    <view class="custom-bar" :style="{ height: navHeight + 'px' }">
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
            placeholder="请输入商品或商家名称"
            v-model="searchContent"
            :showAction="true"
            maxlength=20
            @change="change()"
            @search="goBack()"
            @custom="goBack()"
            style="width: 100%"
          ></u-search>
        </view>
      </view>
    </view>
    <view class="history" :style="{ marginTop: searchMarginTop + 56 + 'px' }">
      <view class="title"
        >搜索历史
        <img
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/3461aeb3-e3ae-45ba-8424-aa0f7c7ea8cc.png"
          alt=""
          @click="deleteHistory()"
      /></view>
      <view class="record" v-if="show">
        <view
          class="historyItem"
          v-for="item in historyList"
          :key="item"
          @click="itemGoback(item)"
          >{{ item }}</view
        >
      </view>
    </view>
  </view>
</template>
<script>
export default {
  data() {
    return {
      searchContent: "",
      show: true,
      historyList: [],
      navHeight: '',
      menuButtonInfo: {},
      searchMarginTop: 0, // 搜索框上边距
      searchWidth: 0, // 搜索框宽度
      searchHeight: 0, // 搜索框高度
    };
  },
  methods: {
    change(){
      if(this.searchContent.length==20){
         this.searchContent.push('')
      }   
      console.log(this.searchContent)
    },
    goBack() {
      this.pushHistory(this.searchContent);
      const pages = getCurrentPages();
      const prevPage = pages[pages.length - 2];
      prevPage.onLoad({ keywords: this.searchContent || null })
      uni.navigateBack();
    },
    itemGoback(val) {
      const pages = getCurrentPages();
      const prevPage = pages[pages.length - 2];
      prevPage.onLoad({ keywords: val })
      uni.navigateBack();
    },
    getHistory() {
      uni.getStorage({
        key: 'historys',
        success: (res) => {
          if (res.data) {
            this.historyList = res.data;
          }
        },
      });
    },
    pushHistory(val) {
      var arr = this.historyList;
      var history = this.historyList;
      if (history.length < 20 && val != '') {
        history.unshift(val);
        arr = [...new Set(history)];
      } else if (val != '') {
        history.unshift(val);
        arr = [...new Set(history)];
        if (arr.length == 20) {
          arr.pop();
        }
      }
      uni.setStorage({
        key: 'historys',
        data: arr,
      });
    },
    deleteHistory() {
      this.historyList = [];
      uni.removeStorageSync('historys');
    },
  },

  // watch: {
  //   searchContent(newValue) {
  //     this.searchContent = newValue;
  //   },
  // },
  onReady() {
    this.getHistory();
  },
  onLoad(o) {
    if(o.type == 2) { 
      this.$eventRecord(257);
    }
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
};
</script>
<style lang="scss" scoped>
view {
  box-sizing: border-box;
  overflow: hidden;
}
.box {
  height: 100vh;
  background: linear-gradient(360deg, rgba(236, 231, 231, 0) 0%, #ece9e9 100%);
}
.custom-bar {
  /* background-color: #aaa; */
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  .custom-bar__wrapper {
    padding: 0 10rpx;
    display: flex;
    justify-content: center;
    align-items: center;
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
.history {
  display: flex;
  flex-direction: column;
  width: 100%;
  .title {
    display: flex;
    justify-content: space-between;
    margin-left: 40upx;
    font-size: 30upx;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #000000;
    img {
      margin-right: 46upx;
      height: 32upx;
      width: 32upx;
    }
  }
  .record {
    display: flex;
    flex-wrap: wrap;
    margin-left: 20rpx;
    margin-top: 46upx;
    .historyItem {
      margin-bottom: 20upx;
      margin-left: 20upx;
      padding: 8upx 16upx;
      border-radius: 34upx;
      border: 2upx solid #000000;
      font-size: 26upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #000000;
      line-height: 36upx;
    }
  }
}
</style>
