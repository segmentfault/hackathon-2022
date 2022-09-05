<template>
  <view class="page-bounty">
    <comHead changeColor style="margin">
      <v-slot class="com-head"></v-slot>
    </comHead>
    <img
      src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/1b017d66-1348-4652-aa68-3171977a226e.png"
      alt=""
      style="margin-top: 100upx"
    />
    <view class="content">
      <view class="title">· 赏金猎人的任务及赏金 ·</view>
      <view class="desc"
        >拼叭赏金猎人是拼叭聚集的一部分爱玩、有想法、有号召力的忠实用户，并进行平台支持，培养赏金猎人的粉丝群体，共同来赚取收益。</view
      >
      <view class="desc" style="margin-top: 50upx"
        >① 分享商品给好友下单，即可获得每单10-50元的赏金。</view
      >
      <view class="desc"
        >② 分享一次绑定的关系，该用户未来主动下单，赏金猎人也能在家躺赚佣金。</view
      >
      <view class="desc" style="margin-bottom: 24upx"
        >③ 该佣金在账户中心可以随时提现，以现金方式转给微信零钱账户。</view
      >
      <view class="title">· 参与步骤 ·</view>
      <img
        class="bottom-img"
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/163548cc-3998-4c32-9221-ef1aaba569c6.png"
        alt=""
      />
    </view>
    <view class="footer">
      <button
        class="btn"
        open-type="getPhoneNumber"
        @getphonenumber="getPhoneNumber"
      >
        立即参与
      </button>
    </view>
    <u-popup v-model="showPopup" mode="center" border-radius="0">
      <view class="alert-content">
        <image
          class="top"
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/e3472c4c-e076-4cea-8ce7-29621f7b1db0.png"
        >
        </image>
        <view class="text-content">
          <view class="text">恭喜你!</view>
          <image
            class="icon"
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/3e49658e-26d1-4181-88f4-c4cbf7293e94.png"
          ></image>
          <view class="text" style="margin-top: 30upx; margin-bottom: 100upx"
            >您已成为赏金猎人！</view
          >
          <view class="text-btn" @click="back">赚佣金</view>
          <view class="text-home" @click="goHome()">返回首页</view>
        </view>
      </view>
    </u-popup>
  </view>
</template>
<script>
import comHead from '@/components/com-head.vue';
import UserService from '@/service/user.js';
export default {
  components: {
    comHead,
  },
  data() {
    return {
      showPopup: false,
    };
  },
  onLoad() { 
    this.$eventRecord(258);
  },
  methods: {
    handlerConfirm() {
      this.showPopup = true;
    },
    goHome() {
      uni.switchTab({
        url: '/pages/index/index',
      });
    },
		back(){
			 uni.switchTab({
        url: '/pages/seek/seek',
      });
		},
    //手机号授权登录
    async getPhoneNumber(res) {
      this.$eventRecord(259);
      const _this = this
      const { detail } = res;
      if (detail.errMsg.includes('ok')) {
        uni.showModal({
          title: '温馨提示',
          content: '亲,授权微信头像后才能正常使用小程序功能',
          success(result) {
            if (result.confirm) {
              uni.getUserProfile({
                desc: 'Wexin', // 这个参数是必须的
                success: (userInfo) => {
                  UserService.joinIdentity().then((results) => {
                    if(res[0] !== null) { 
                      _this.showPopup = true;
											_this.$store.commit('setIdentityType');
                    }
                  });
                },
                fail: (err) => {
                  console.log(err, '取消授权');
                },
              });
            }
            if (result.cancel) {
              console.log('授权信息失败');
            }
          },
        });
      } else {
        console.log('授权电话失败');
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.page-bounty {
  padding: 0 20upx;
  background: url('https://image-1306191496.cos.ap-nanjing.myqcloud.com/7561d4e2-7100-4812-a4c2-0f6308b5a1b8.png')
    no-repeat center;
  background-size: 100% 100%;
  min-height: 100vh;
  text-align: center;
  padding-bottom: 24upx;
  > img {
    width: calc(100% - 32upx);
    margin-bottom: 24upx;
  }
  .content {
    width: 100%;
    background: #ffffff;
    border-radius: 48upx;
    padding: 52upx 32upx;
    margin-bottom: 140upx;
    .title {
      font-size: 40upx;
      font-weight: bold;
      color: #5d4343;
      margin-bottom: 24upx;
    }
    .desc {
      font-size: 28upx;
      color: #5d4343;
      text-align: left;
      font-family: AlibabaPuHuiTiR;
      line-height: 50upx;
    }
    .bottom-img {
      width: 100%;
      height: 64upx;
    }
  }
  .footer {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    height: 122upx;
    background: #fff;
    text-align: center;
    > .btn {
      height: 94upx;
      color: #fff;
      font-weight: 600;
      font-size: 40upx;
      width: calc(100% - 120upx);
      background: #653b2e;
      border-radius: 24px;
      margin: 0 auto;
      line-height: 94upx;
      margin-top: 14upx;
    }
  }
}
.alert-content {
  width: 624upx;
  height: 942upx;
  background: #ffffff;
  padding: 50upx 66upx;
  position: relative;
  .top {
    width: 100%;
    height: 276upx;
  }
  .text-content {
    position: absolute;
    left: 0;
    right: 0;
    top: 160upx;
    .text {
      font-size: 40upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: bold;
      color: #5d4343;
      line-height: 56upx;
    }
    .icon {
      width: 180upx;
      height: 214upx;
      margin-top: 54upx;
    }
    .text-btn {
      width: 322upx;
      height: 94upx;
      line-height: 94upx;
      background: #653b2e;
      border-radius: 44upx;
      font-weight: 600;
      color: #ffffff;
      font-size: 40upx;
      margin: 0 auto;
      margin-bottom: 48upx;
    }
    .text-home {
      color: #5d4343;
      line-height: 44upx;
      font-size: 32upx;
    }
  }
}
</style>
