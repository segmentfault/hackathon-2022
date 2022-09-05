<template>
  <view class="page">
    <view
      class="custom-bar"
      @click="goActive(2)"
      :style="{ height: navHeight + 'px' }"
    >
      <view
        class="custom-bar__wrapper"
        :style="{
          marginTop: searchMarginTop + 'px',
          height: searchHeight + 'px',
          lineHeight: searchHeight + 'px',
        }"
      >
        <view
          class="colorW fs36 text-center"
          :style="{ lineHeight: searchHeight + 'px' }"
        >
          志愿测试</view
        >
      </view>
    </view>
    <view class="box u-flex">
      <view class="fs40 u-flex title colorW"> 游戏化志愿服务工作 鉴定报告</view>
      <u-image
        class="u-m-t-58"
        border-radius="90"
        src="/static/us.png"
        width="178rpx"
        height="178rpx"
      ></u-image>
      <view class="fs36 colorW u-p-t-52">洞察自我</view>
      <view class="title fs28 u-flex colorW u-p-t-10"
        >精准定位趣志愿最适合我的公益活动</view
      >
      <view class="u-m-t-72 colorW fs28">先进行一个有趣的小测试吧</view>
      <view class="msg colorW fs20 u-m-t-16"
        >我适合参加什么类型的志愿活动<br /><br
      /></view>
      <view class="msg fs20"> 我和某个志愿活动是否契合</view>
      <view class="msg fs20">什么志愿工作适合我</view>
    </view>
    <button class="btn" @click="goTpic">立即测试</button>
    <view class="end colorW u-m-t-22 fs28" @click="goHome()"
      >不想测试直接开启公益之旅</view
    >
  </view>
</template>

<script>
import comHead from "@/components/com-head.vue";
const db = wx.cloud.database();
export default {
  components: {
    comHead,
  },
  data() {
    return {
      background: {
        backgroundColor: "#7d9fed ",
      },
      info: {},
      navHeight: "",
      ifCreat: true,
      searchMarginTop: 0, // 搜索框上边距
      searchWidth: 0, // 搜索框宽度
      searchHeight: 0,
    };
  },
  async onLoad(option) {
    this.getSystemInfo();
     let res = await wx.cloud.callFunction({
      name: "getOpenId",
    });
     wx.setStorageSync("openid", res.result.openId);
  },
  methods: {
    goHome() {
      uni.getUserProfile({
        desc: "你的授权信息",
        success: async (res) => {
          console.log("sdf", res);
          const openid = wx.getStorageSync("openid");

          console.log("sb", openid);
          const re = await db
            .collection("userInfo")
            .where({
              _openid: openid,
            })
            .get()
            .then((res) => {
              console.log("sb", res);
              if (res.data.length) {
                this.ifCreat = false;
                console.log("修改", this.ifCreat);
              }
            });
          console.log("你好呀", this.ifCreat);
          if (this.ifCreat) {
            db.collection("userInfo")
              .add({
                data: {
                  user_img: res.userInfo.avatarUrl,
                  user_name: res.userInfo.nickName,
                  _createTime: new Date().valueOf(),
                  serviceTime: 0,
                },
              })
              .then((res) => {
                db.collection("userInfo")
                  .where({
                    _openid: openid,
                  })
                  .get()
                  .then((res) => {
                    getApp().globalData.user = res.data[0];
                    console.log("df", res.data[0]);
                    console.log("dfsdf", getApp().globalData.user);
                  });
              });
          } else {
            db.collection("userInfo")
              .where({
                _openid: openid,
              })
              .update({
                data: {
                  user_img: res.userInfo.avatarUrl,
                  user_name: res.userInfo.nickName,
                },
              })
              .then((res) => {
                db.collection("userInfo")
                  .where({
                    _openid: openid,
                  })
                  .get()
                  .then((res) => {
                    getApp().globalData.user = res.data[0];
                    console.log("df", res.data[0]);
                    console.log("dfsdf", getApp().globalData.user);
                  });
              });
          }

          wx.setStorage({
            key: "login",
            data: "true",
          });
          uni.switchTab({
            url: "/pages/tabbar/tabbar-4/tabbar-4",
          });
        },
        fail: (res) => {
          wx.showToast({
            title: "您取消了授权",
            icon: "none",
            duration: 1000, //持续的时间
          });
        },
      });
    },
    getSystemInfo() {
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
    goTpic() {
      uni.getUserProfile({
        desc: "你的授权信息",
        success: async (res) => {
          console.log("sdf", res);
          const openid = wx.getStorageSync("openid");

          console.log("sb", openid);
          const re = await db
            .collection("userInfo")
            .where({
              _openid: openid,
            })
            .get()
            .then((res) => {
              console.log("sb", res);
              if (res.data.length) {
                this.ifCreat = false;
                console.log("修改", this.ifCreat);
              }
            });
          console.log("你好呀", this.ifCreat);
          if (this.ifCreat) {
            db.collection("userInfo")
              .add({
                data: {
                  user_img: res.userInfo.avatarUrl,
                  user_name: res.userInfo.nickName,
                  _createTime: new Date().valueOf(),
                  serviceTime: 0,
                },
              })
              .then((res) => {
                db.collection("userInfo")
                  .where({
                    _openid: openid,
                  })
                  .get()
                  .then((res) => {
                    getApp().globalData.user = res.data[0];
                    console.log("df", res.data[0]);
                    console.log("dfsdf", getApp().globalData.user);
                  });
              });
          } else {
            db.collection("userInfo")
              .where({
                _openid: openid,
              })
              .update({
                data: {
                  user_img: res.userInfo.avatarUrl,
                  user_name: res.userInfo.nickName,
                },
              })
              .then((res) => {
                db.collection("userInfo")
                  .where({
                    _openid: openid,
                  })
                  .get()
                  .then((res) => {
                    getApp().globalData.user = res.data[0];
                    console.log("df", res.data[0]);
                    console.log("dfsdf", getApp().globalData.user);
                  });
              });
          }

          wx.setStorage({
            key: "login",
            data: "true",
          });
          this.$u.route("/pagesA/voTest/topic");
        },
        fail: (res) => {
          wx.showToast({
            title: "您取消了授权",
            icon: "none",
            duration: 1000, //持续的时间
          });
        },
      });
    },
    // goTpic() {
    //   this.$u.route("/pagesA/voTest/topic");
    // },
  },
};
</script>

<style scoped lang="scss">
.page {
  display: flex;
  flex-direction: column;
  background: url(https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/%E5%BF%97%E6%84%BF%E6%B5%8B%E8%AF%95.jpg?sign=3385c4169b98766ee8db6faf660521f0&t=1661482999)
    no-repeat center;
  background-size: 100% 100%;
  min-height: 100vh;
  .box {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-bottom: 74rpx;
    margin: 48rpx 32rpx 0 32rpx;
    background: rgba(255, 255, 255, 0.05);
    box-shadow: 0px 0px 106rpx 0px rgba(77, 119, 237, 0.39);
    opacity: 1;
    border-radius: 32rpx;
    border: 4rpx solid;
    border-color: white;
    .title {
      padding-top: 86rpx;
      flex-wrap: wrap;
      width: 374rpx;
      text-align: center;
    }

    .msg {
      line-height: 36rpx;
      opacity: 0.8;
      color: #ffffff;
    }
  }
  .colorW {
    color: #ffffff;
  }
  .btn {
    color: white;
    margin-top: 194rpx;
    border: 2rpx solid;
    border-color: white;
    width: 560rpx;
    height: 96rpx;
    border-radius: 56rpx;
    background: rgba(69, 117, 246, 0.5);
  }
  .end {
    text-align: center;
    margin-bottom: 50rpx;
  }
}
</style>
