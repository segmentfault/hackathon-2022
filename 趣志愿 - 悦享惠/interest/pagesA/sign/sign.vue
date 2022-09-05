<template>
  <view class="">
    <view class="top" :style="{ paddingTop: searchMarginTop + 'px' }">
      <view class="head" :style="{ lineHeight: searchHeight + 'px' }">
        <!-- <u-image class="icon" height="60rpx" width="164rpx" src="/static/img/zhiyuan.png"></u-image> -->
        <view class="back" @click="goBack()"
          ><u-icon name="arrow-left" color="#FFFFFF" size="48"></u-icon
        ></view>
        <view class="find">{{ head }}</view>
      </view>
    </view>
    <view class="sign">
      <view class="logo">
        <u-image
          height="176rpx"
          width="176rpx"
          src="/static/img/logo.png"
        ></u-image>
      </view>
      <view class="name u-m-t-48">趣志愿</view>
      <view class="platform u-m-t-8">游戏化志愿者服务平台</view>
      <view class="in u-m-l-48">
        <view class="accont u-m-t-48" v-if="isShow">
          <input
            style="padding-left: 20px"
            selection-start="5"
            class="input"
            type="text"
            placeholder="请输入登录账号"
            v-model="account"
          />
        </view>
        <view class="password u-m-t-24" v-if="isShow">
          <input
            style="padding-left: 20px"
            class="input"
            type="password"
            placeholder="请输入密码"
            v-model="password"
          />
        </view>
      </view>
      <view style="width: 100vw" v-if="isShow">
        <view class="registered u-m-t-24" @click="goSign()">志愿组织注册</view>
      </view>
    </view>
    <button class="btn" v-if="id == 1" @click="Login()">{{ signMsg }}</button>
    <button class="btn" v-else @click="Login2()">{{ signMsg }}</button>
    <view class="agreement" v-if="isShow"
      >登录即表示同意《用户协议》和《隐私政策》</view
    >
  </view>
</template>

<script>
const db = wx.cloud.database();
export default {
  data() {
    return {
      searchMarginTop: "",
      searchWidth: 0,
      searchHeight: 0,
      head: "",
      signMsg: "微信账号授权登录",
      word: "账号密码登录/注册",
      isShow: false,
      id: "",
      ifCreat: true,
      account: "",
      password: "",
    };
  },
  async onLoad(option) {
    //获取openid
    let res = await wx.cloud.callFunction({
      name: "getOpenId",
    });
    if (option.id == 2) {
      this.isShow = true;
      this.signMsg = "登录";
      this.word = "忘记密码";
    }
    wx.setStorageSync("openid", res.result.openId);
    this.id = option.id;
    this.head = option.id == 1 ? "用户登录" : "组织登录";
    console.log("id:", option.id);
    console.log("name", this.name);
    const menuButtonInfo = uni.getMenuButtonBoundingClientRect();
    const { top, width, height, right } = menuButtonInfo;
    uni.getSystemInfo({
      success: (res) => {
        const { statusBarHeight } = res;
        const margin = top - statusBarHeight;
        this.searchMarginTop = statusBarHeight + margin;
        console.log("dfs", this.searchMarginTop);
        this.searchHeight = height;
        this.searchWidth = right - 2 * width;
      },
    });
  },
  methods: {
    goSign() {
      uni.navigateTo({
        url: "../../pagesB/Editdata/Editdata?id=1",
      });
    },
    Login() {
      if (this.signMsg == "微信账号授权登录") {
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
                        console.log("df",res.data[0]);
                        console.log("dfsdf", getApp().globalData.user)
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
                        console.log("df",res.data[0]);
                        console.log("dfsdf", getApp().globalData.user)
                    });
                });
            }

            wx.setStorage({
              key: "login",
              data: "true",
            });
            setTimeout(() => {
              uni.switchTab({
                url: "../../pages/tabbar/tabbar-5/tabbar-5",
              });
            }, 500);
          },
          fail: (res) => {
            wx.showToast({
              title: "您取消了授权",
              icon: "none",
              duration: 1000, //持续的时间
            });
          },
        });
      } else {
        wx.setStorage({
          key: "login",
          data: "true",
        });
        uni.switchTab({
          url: "../../pages/tabbar/tabbar-5/tabbar-5",
        });
      }
    },
    Login2() {
      console.log("sfsdf", this.account);
      db.collection("organization")
        .where({
          account: this.account,
        })
        .get()
        .then((res) => {
          if (res.data.length != 0) {
            if (res.data[0].password == this.password) {
              wx.setStorage({
                key: "or",
                data: res.data[0]._id,
              });
              wx.setStorage({
                key: "Ologin",
                data: "true",
              });
              db.collection("organization")
                .doc(res.data[0]._id)
                .get()
                .then((res) => {
                  console.log("大沙发",res)
                  getApp().globalData.orInfo = res.data;
                    uni.redirectTo({
                url: `/pagesB/or-home/or-home`,
              });
                });
            
            } else {
              uni.showToast({ title: "密码错误", icon: "none" });
            }
          } else {
            wx.showToast({
              title: "账号未注册！",
              icon: "none",
            });
          }
        });
    },
    goBack() {
      uni.switchTab({
        url: "/pages/tabbar/find/find",
      });
    },
    show() {
      this.isShow = true;
      this.signMsg = "登录";
      this.word = "忘记密码";
    },
  },
};
</script>

<style lang="scss">
.top {
  background: url(https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/bg1.png?sign=585fd2b5f5f5e734d89a40716db8ed95&t=1654872699)
    no-repeat center;
  background-size: 100% 100%;
  height: 336rpx;
  width: 100vw;

  .head {
    display: flex;
    position: relative;
    justify-content: center;
  }
  .back {
    position: absolute;
    left: 20rpx;
  }
  .icon {
    position: absolute;
    left: 32rpx;
  }

  .find {
    font-size: 36rpx;
    font-weight: 600;
    color: #ffffff;
  }
}

.input {
  width: 574rpx;
  height: 92rpx;
  border-radius: 45rpx;
  background: #f5f8fe;
}

.registered {
  position: absolute;
  font-size: 24rpx;
  right: 64rpx;
  border-bottom: 1px solid #4575f6;
  font-weight: 400;
  color: #4575f6;
}

.sign {
  display: flex;
  flex-direction: column;
  align-items: center;

  .name {
    font-size: 44rpx;
    font-weight: 600;
    color: #4575f6;
  }

  .platform {
    font-size: 28rpx;
    font-weight: 600;
    color: #4575f6;
    opacity: 0.46;
  }
}

.si {
  text-align: center;
  font-size: 32rpx;
  font-weight: 400;
  color: #4575f6;
}

.btn {
  color: white;
  margin-top: 124rpx;
  width: 560rpx;
  height: 96rpx;
  border-radius: 56rpx;
  background: #4575f6;
}

.agreement {
  position: absolute;
  width: 100%;
  opacity: 0.6;
  text-align: center;
  bottom: 50rpx;
}
</style>
