<template>
  <view style="position: relative">
    <view
      class="head-custom"
      v-if="custom"
      :style="{
        paddingTop: headHeight + 5 + 'px',
        background: background,
      }"
    >
      <view v-if="scrollTop" class="hr"></view>
      <slot></slot>
    </view>
    <view
      class="page-head"
      v-else
      :style="{
        paddingTop: statusBarHeight + 'px',
        height: headHeight + 'px',
        backgroundColor: changeColor
          ? 'rgba(249,248,248,' + ratio + ')'
          : background,
        color: changeColor ? (Number(ratio) >= 0.5 ? '#333' : '#FFF') : color,
      }"
    >
      <u-icon
        v-if="!hideBack"
        class="back-icon"
        :style="{ paddingTop: titleBarHeight + statusBarHeight + 'upx' }"
        @click="back"
        name="arrow-left"
        :color="changeColor ? (Number(ratio) >= 0.5 ? '#333' : '#FFF') : color"
        size="45"
      ></u-icon>
      <view
        class="page-title"
        :style="{ paddingTop: titleBarHeight + statusBarHeight + 'upx' }"
      >
        <slot></slot>
      </view>
    </view>

    <view
      v-if="autoPadding"
      :style="{ paddingTop: statusBarHeight + 'px', height: headHeight + 'px' }"
    ></view>
  </view>
</template>

<script>
import { config } from "@/framework";

export default {
  props: {
    custom: Boolean, // 自定义头部内容
    autoPadding: Boolean, // 是否自动添加跟头部一样高的padding
    //是否开启滑动渐变
    changeColor: {
      type: Boolean,
      default: false,
    },
    color: {
      type: String,
      default: "#FFF",
    },
    background: {
      type: String,
      default: "transparent",
    },
    hideBack: {
      type: Boolean,
      default: false,
    },
    // 如果有ishome 表示回到首页
    isHome: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      titleBarHeight: config.titleBarHeight,
      statusBarHeight: uni.getSystemInfoSync().statusBarHeight,
      headHeight:
        (config.titleBarHeight + uni.getSystemInfoSync().statusBarHeight) / 2,
      ratio: 0,
      headCustomStyle: "",
      scrollTop: 0,
    };
  },
  methods: {
    back() {
      if (this.isHome) {
        uni.reLaunch({
          url: "/pages/index/index",
        });
      } else {
        uni.navigateBack();
      }
    },
  },
  mounted() {
    var that = this;
    uni.$on("onPageScroll", (scrollTop) => {
      //接收参数
      let r = Number(scrollTop / (that.headHeight || 0));
      that.ratio = scrollTop === 0 ? 0 : r > 1 ? 1 : r;
      this.scrollTop = scrollTop;
    });
    let style = {
      paddingTop: this.headHeight * 2 + "upx",
      height: this.headHeight * 2 + "upx",
      background: this.background,
      borderRadius: `10upx solid ${this.background}`,
    };
    this.headCustomStyle = style;
  },
  // computed: {
  // 	headCustomStyle() {
  // 		let style = {
  // 			paddingTop: this.headHeight * 2 + 'upx',
  // 			height: this.headHeight * 2 + 'upx',
  // 			background: this.background,
  // 			borderRadius: `10upx solid ${this.background}`
  // 		}
  // 		 (style)
  // 		return JSON.stringify(style)
  // 	}
  // },
};
</script>

<style scoped lang="scss">
.head-custom {
  box-sizing: content-box;
  position: fixed;
  left: 0;
  right: 0;
  z-index: 999;
  display: flex;
  align-items: center;
}
.hr {
  position: absolute;
  display: block;
  background: #e5e5e5;
  width: 100%;
  height: 2upx;
  bottom: -12upx;
}
.page-head {
  position: fixed;
  padding-left: 22upx;
  padding-right: 22upx;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  z-index: 999;
}

.back-icon {
  position: relative;
  width: 32upx;
  height: 54upx;
  padding-top: 56upx;
}

.page-title {
  width: calc(100% - 88upx);
  display: flex;
  justify-content: center;
  margin-right: 44upx;
  padding-top: 56upx;
  font-size: 40upx;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
}
</style>
