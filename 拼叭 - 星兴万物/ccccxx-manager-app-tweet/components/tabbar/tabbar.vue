<template>
  <view class="tab-block">
    <ul
      class="tab-list flex flex-center"
      :class="
        showMiddleButton === true ? 'tab-list-middle' : 'tab-list-default'
      "
    >
      <li
        v-for="(item, index) in tabList"
        :class="'list-item flex flex-column flex-middle ' + item.middleClass"
        @click.stop="handlePush(item, index)"
        :key="index"
      >
        <view class="item-img-box">
          <image
            class="item-img"
            :class="{ active: item.gif }"
            :src="value == index ? item.selectedIconPath : item.iconPath"
          ></image>
          <u-badge
            v-if="redpoint && item.msg"
            :offset="[-3, -6]"
            type="error"
            max="99"
            :count="redpoint"
          ></u-badge>
        </view>
        <view
          class="item-text font-20 color-black"
          :class="{ specialColor: tabIndex == index }"
        >
          {{ item.text }}
        </view>
      </li>
    </ul>

    <!-- 兼容iPhonex下面的小黑条 -->
    <view class="tab-bar" v-show="showTabBar === true"></view>
  </view>
</template>

<script>
export default {
  props: {
    value: {
      //当前选中的tab项
      type: Number,
      required: true,
    },
    message: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      /*
       * iconPath: 默认icon图片路径
       * selectedIconPath: 选中icon图片路径
       * text: tab按钮文字
       * pagePath:页面路径
       * middleClass:该按钮所有的特殊样式类名
       * tabList最小两项，最多五项
       * tabList长度为奇数时，中间按钮才会突出显示
       * msg是否开启消息提醒
       */
      isImg: [
        {
          img: 'https://image-1306191496.cos.ap-nanjing.myqcloud.com/fcde48e2-ec56-4f5a-ba1c-9ee8fe0df64d.png',
        },
        {
          img: 'https://image-1306191496.cos.ap-nanjing.myqcloud.com/93459a2a-81d6-47f1-a738-9cc9d92b1e87.png',
        },
      ],
      tabList: [
        {
          iconPath:
            'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/index-home.png',
          selectedIconPath:
            'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/index-home-selected.png',
          text: '首页',
          pagePath: '/pages/index/index',
          middleClass: '',
        },
        {
          iconPath:
            'https://image-1306191496.cos.ap-nanjing.myqcloud.com/a49e8958-ced9-4c9e-9346-d5da7284811d.gif',
          selectedIconPath:
            'https://image-1306191496.cos.ap-nanjing.myqcloud.com/a49e8958-ced9-4c9e-9346-d5da7284811d.gif',
          text: '发现',
          pagePath: '/pages/seek/seek',
          middleClass: '',
          gif: 'true',
        },
        {
          iconPath:
            'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/index-chat.png',
          selectedIconPath:
            'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/index-chat-selected.png',
          text: '消息',
          pagePath: '/pages/im/index',
          middleClass: '',
          msg: 'true',
        },
        {
          iconPath:
            'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/me-white.png',
          selectedIconPath:
            'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/me-black.png',
          text: '我的',
          pagePath: '/pages/user/index',
          middleClass: '',
        },
      ],
      showTabBar: false,
      showMiddleButton: false,
    };
  },
  computed: {
    getHeight() {
      return Number(this.height);
    },
    redpoint() {
      return this.$store.state.user.redpoint;
    },
  },
  mounted() {
    this.getSystemInfo();
    // this.setTabBar();
  },
  methods: {
    //判断中间按钮是否突出显示，奇数or偶数，奇数突出
    setTabBar() {
      let tabLength = this.tabList.length;
      if (tabLength % 2) {
        this.showMiddleButton = true;
        // 向上取整
        let middleIndex = Math.floor(tabLength / 2);
        // 给中间的添加mid-button
        this.tabList[middleIndex].middleClass = 'mid-button';
      }
    },

    //点击按钮
    handlePush(item, index) {
			const isAuth = this.$authorization()
			if(!isAuth) return
      if (this.value !== index) {
        uni.switchTab({
          url: item.pagePath,
          // success: async (res) => {
          //   const page = getCurrentPages().pop();
          //   if (page == undefined || page == null) return;
          //   if (page.$vm.refershParams) {
          //     await page.$vm.refershParams();
          //   }
          //   page.onLoad(page.options);
          // },
        });
      }
    },

    //兼容iPhoneX以上底部黑线条的显示
    getSystemInfo() {
      //获取系统信息
      uni.getSystemInfo({
        success: (res) => {
          // X及以上的异形屏top为44，非异形屏为20
          if (res.safeArea.top > 20) {
            this.showTabBar = true;
          }
        },
      });
    },
  },
};
</script>

<style lang="scss">
.active {
  width: 102upx !important;
  height: 38upx !important;
  margin-top: 16upx;
}
.specialColor {
  color: #000;
}
.flex {
  display: flex;
  flex-flow: row wrap;
}

.flex-center {
  align-items: center;
  justify-content: center;
}

.flex-column {
  flex-direction: column;
}

.flex-middle {
  align-items: center;
}
.font-20 {
  font-size: 20rpx;
  color: #939393;
}
.tab-block {
  position: fixed;
  bottom: 0;
  left: 0;
  z-index: 999;
  background-size: contain;
  width: 100vw;
  background: #fff;
  height: calc(128upx + env(safe-area-inset-bottom));
  padding-bottom: env(safe-area-inset-bottom);
  .tab-list {
    height: 130rpx;
  }
  .tab-list-default {
    background-color: #ffffff;
    // border-top: 1px #dddddd solid;
  }
  .tab-list-middle {
    position: relative;
    background: url('https://res.paquapp.com/popmartvip/home/nav_bar_bg_2x.png')
      no-repeat center center;
    background-size: cover;
  }
  .list-item {
    flex: 1;
    .item-img-box {
      height: 60rpx;
      margin-bottom: 9rpx;
      position: relative;
    }

    .item-img {
      width: 60rpx;
      height: 60rpx;
    }
  }

  .mid-button {
    position: relative;
    .item-img-box {
      width: 106rpx;
      height: 106rpx;
      margin-bottom: 9rpx;
      position: absolute;
      z-index: 1002;
      top: -104rpx;
    }

    .item-img {
      width: 106rpx;
      height: 106rpx;
    }

    .item-text {
      font-size: 20rpx;
      z-index: 1002;
      position: absolute;
      bottom: -40rpx;
    }
  }

  .tab-bar {
    height: 30rpx;
    background-color: #ffffff;
  }
  .color-black {
    color: #000;
  }
}
</style>
