<template>
  <view class="page">
    <view class="list">
      <view class="DMInfo" v-for="(item, index) in DMList" :key="index" @click="handlerOpen(item)">
        <view class="head-img">
          <image :src="item.head" mode=""></image>
        </view>
        <view class="introduce">
          <view class="name">
            {{ item.name }}
          </view>
          <view class="msg">
            {{ item.label }}
          </view>
        </view>
      </view>
    </view>
    <u-popup v-model="isShowPop" width="600rpx" mode="center" closeable>
      <view class="pop-center">
        <image :src="info.head" mode=""></image>
        <view class="html-nickname">{{ info.name }}</view>
        <view class="label html-name">
          <view class="html-name-label"> {{ info.label }} </view>
        </view>
        <view class="html-text" v-html="info.text"> </view>
      </view>
    </u-popup>
  </view>
</template>

<script>
import DucumentService from '../../service/document.js';
export default {
  data() {
    return {
      DMList: [],
      isShowPop: false,
      info: {},
      params: {
        supplierId: '',
        ids: '',
        pageNum: 1,
        pageSize: 10,
        total: 0,
      },
    };
  },
  onLoad(o) {
    this.params.supplierId = o.supplierId || null;
    this.params.ids = o.ids || null;
    this.getDMList(this.params);
  },
  // 下拉刷新
  onPullDownRefresh() {
    this.DMList = [];
    this.params.pageNum = 1;
    this.params.pageSize = 10;
    this.getDMList(this.params);
  },
  // 下拉加载
  onReachBottom() {
    if (this.DMList.length < this.params.total) {
      this.params.pageNum++;
      this.getDMList(this.params);
    }
  },
  methods: {
    handlerOpen(item) { 
      this.info = item
      this.isShowPop = true
    },
    async getDMList(params) {
      const res = await DucumentService.DMList({ ...params });
      this.params.total = res.total;
      if (res.list.length) {
        this.DMList.push(...res.list);
      } else {
        this.DMList = res.list;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.page {
  background: #171c33;
  padding-top: 30rpx;
  height: 100vh;
}
.com-head {
  background: #171c33;
  color: #fff;
}
.head {
  line-height: 1em;
  font-size: 32upx;
  color: #fff;
}
.list {
  width: 100%;
	padding:0 3%;
  margin: auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
	background: #171c33;
}

.DMInfo {
  padding: 30rpx;
  background: #2a2f43;
  border-radius: 25rpx;
  width: 48%;
  margin-bottom: 20rpx;
  image {
    width: 280rpx;
    height: 350rpx;
    object-fit: cover;
    border-radius: 16upx;
  }
  .introduce {
    font-size: 35rpx;

    .name {
      font-weight: 600;
      margin-bottom: 20rpx;
      font-weight: bold;
      color: #ffffff;
      line-height: 40upx;
      font-size: 28upx;
    }

    .msg {
      font-weight: 100;
      line-height: 36rpx;
      color: #fff;
      font-size: 28upx;
      display: -webkit-box;
      -webkit-line-clamp: 3;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }
  }
}
.pop-center { 
  // background: #171c33;
  image { 
    width: 100%;
    border-radius: 16rpx 16rpx 0 0;
  }
  > .html-nickname { 
    padding: 15rpx 30rpx 0;
    font-weight: bold;
    color: #000000;
    line-height: 40rpx;
    font-size: 28rpx;
  }
  > .html-name { 
    padding: 15rpx 30rpx;
    margin-top: 0;
    > .html-name-label { 
        color: #333;
        text-align: center;
        border-radius: 10rpx;
        display: inline-flex;
    }
  } 
  > .html-text { 
    padding: 0 30rpx 15rpx;
  }
}
</style>
