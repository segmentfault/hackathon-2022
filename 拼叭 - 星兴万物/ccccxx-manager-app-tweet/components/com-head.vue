<template>
  <view style="position: relative">
    <view
      class="head-custom"
      v-if="custom"
      :style="{
        paddingTop: headHeight + 5 + 'px',
        background: background,
        borderBottom: '5px solid ' + background,
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
      <image
        v-if="!isHome && !hideBack"
        class="back-icon"
        :style="{ paddingTop: titleBarHeight + statusBarHeight + 'upx' }"
        @click="back"
				src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/10d350b5-da8d-41b1-85cf-cdc5d86cdef9.png"
      ></image>
			<view class="house flex flex-center flex-middle" @click="back" v-else-if="!hideBack">
				<image
				  src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/eeebfa36-b79d-4253-9d3b-f4748adb0926.png"
				  class="logo"
				></image>
			</view>
      <view
        :class="[{'mr110':isHome},'page-title']"
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
      scrollTop: 0
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
// .logo {
// 	width: 68upx;
// 	height: 60upx;
// 	margin-top:50upx;
// }
.head-custom {
  box-sizing: content-box;
  position: fixed;
  left: 0;
  right: 0;
  z-index: 999;
  display: flex;
  align-items: center;
}
.house{
	width: 68upx;
	height:60upx;
	border-radius: 50%;
	background: #fff;
	margin-top: 50upx;
	image{
		width:45upx;
		height:40upx;
	}
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
  width: 44upx;
  height: 44upx;
  padding-top: 56upx;
}

.page-title {
  width: calc(100% - 88upx);
  display: flex;
  justify-content: center;
  margin-right: 66upx;
  padding-top: 56upx;
  font-size: 40upx;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
}
.mr110{
	margin-right:110upx;
}
</style>
