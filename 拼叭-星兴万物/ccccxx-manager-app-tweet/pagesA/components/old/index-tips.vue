<template>
  <view class="tips">
    <view class="tips-mask"></view>
    <view class="tips-content">
      <!-- 第一步 -->
      <view v-if="showFirst" class="first-tip" :style="firstTipStyle">
        <image class="first-img" src="../../static/images/7.png" />
        <view class="arrow">↑</view>
        <view class="info">如果你想在平台找小伙伴，赶紧发个局吧！</view>
        <button class="confirm first-btn" @click="confirm('first')">知道了</button>
      </view>
      <!-- 第二步 -->
      <view v-if="showSecond" class="second-tip">
        <view class="info">消息都在这里哦!</view>
        <view class="second-container">
          <view class="arrow">↓</view>
          <button class="confirm second-btn" @click="confirm('second')">知道了</button>
        </view>
        <view class="img-container second-img">
          <image
            class="tip-img"
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/0ba0c62c-c480-416e-a6ad-466506938612.png"
          />
        </view>
      </view>
      <!-- 第三步 -->
      <view v-if="showThird" class="third-tip">
        <view class="info">创建/申请的局都在这里哦！</view>
        <view class="third-container">
          <button class="confirm third-btn" @click="confirm('third')">知道了</button>
          <view class="arrow">↓</view>
        </view>
        <view class="img-container third-img clearfix">
          <image
            class="tip-img"
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/ae6e446b-3ec5-4bac-9384-9369be94688c.png"
          />
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { config } from '@/framework'
export default {
  data() {
    return {
      statusBarHeight: config.statusBarHeight,
      headHeight:
        (config.titleBarHeight + uni.getSystemInfoSync().statusBarHeight) / 2,
      showFirst: true,
      showSecond: false,
      showThird: false,
    }
  },
  props: {
    joinList: {
      type: Array,
    },
  },
  computed: {
    firstTipStyle() {
      if (this.joinList.length > 0) {
        return `margin-top:116upx`
      }
    },
  },
  methods: {
    confirm(step) {
      if (step === 'first') {
        this.showFirst = false
        this.showSecond = true
      } else if (step === 'second') {
        this.showSecond = false
        this.showThird = true
      } else if (step === 'third') {
        this.showThird = false
        this.$emit('closeTip')
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.tips-mask {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 1;
  background-color: rgba(0, 0, 0, 0.6);
}
.tips-content {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 2;
  color: #fff;
  .first-tip {
    position: absolute;
    left: 32upx;
    // top: 16upx;
    top: 265upx;
    .first-img {
      width: 320upx;
      height: 120upx;
      border-radius: 40upx;
      border: 10upx solid #fff;
    }
    .arrow {
      padding: 14upx 0 6upx 146upx;
    }
    .first-btn {
      margin: 22upx 0 0 410upx;
    }
  }
  .second-tip {
    position: absolute;
    bottom: calc(env(safe-area-inset-bottom) - 12upx);
    left: 350upx;
    .second-container {
      display: flex;
      align-items: center;
      padding-bottom: 24upx;
      .arrow {
        padding: 0 94upx;
      }
    }
    .img-container {
      margin-left: 40upx;
    }
  }
  .third-tip {
    position: absolute;
    bottom: calc(env(safe-area-inset-bottom) - 12upx);
    right: 38upx;
    .third-container {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 14upx 0 4upx 0;
      .arrow {
        padding: 0 64upx;
      }
    }
    .img-container {
      float: right;
    }
  }
}

.arrow {
  font-size: 40upx;
}
.info {
  font-size: 32upx;
  font-weight: 600;
  line-height: 44upx;
}
.img-container {
  width: 166upx;
  height: 144upx;
  background-color: #fff;
  border-radius: 30upx;
  display: flex;
  align-items: center;
  justify-content: center;
}
.tip-img {
  width: 90upx;
  height: 90upx;
}
.confirm {
  display: inline-block;
  padding: 14upx 44upx;
  font-size: 28upx;
  font-weight: 600;
  color: #fff;
  line-height: 40upx;
  border-radius: 34upx;
  border: 2upx solid #fff;
  background-color: transparent;
}
</style>
