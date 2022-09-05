<template>
  <view class="set">
    <cell :title="messageTitle" @setMessage="setMessage">123</cell>
    <cell :title="title" @authority="authority"></cell>
    <cell :title="title1" @agreement="agreement"></cell>
    <agreement class="agreement" v-if="showDialog" @close="close"></agreement>
    <view class="fiexd">
      <button @click="logoutHandle">退出登录</button>
    </view>
  </view>
</template>

<script>
import cell from "../components/cell.vue";
import agreement from "@/pagesA/components/user/agreement.vue";
import Dialog from "../components/Dialog.vue";
export default {
  components: {
    cell,
    agreement,
    Dialog,
  },
  data() {
    return {
      messageTitle: "消息设置",
      title: "微信权限设置",
      title1: "协议及隐私政策",
      showDialog: false,
      Dialog: false,
      mag: "退出登录",
    };
  },
  onShow() {
    // 埋点
    this.$eventRecord(72);
  },
  methods: {
    //设置消息通知权限
    setMessage() {
      uni.navigateTo({
        url: "/pagesA/user/message",
      });
    },
    //   设置微信权限
    authority() {
      uni.openSetting({});
    },
		logoutHandle(){
			const that = this
			uni.showModal({
				title:"退出登录",
				content:"您确定要退出登录吗?",
				success(res) {
					if(res.confirm){
						that.outLogin()
					}
				}
			})
		},
    // 退出登录
    outLogin() {
      try {
        uni.removeStorageSync("userInfo");
        uni.removeStorageSync("ticket");
        uni.removeStorageSync("myUserTicket");
      } catch (e) {
        // error
      }
      uni.reLaunch({
        url: `/pagesA/user/login?isJumpIndex=${true}`,
      });
    },
    // 关闭协议
    close() {
      this.showDialog = false;
    },

    agreement() {
      //(1);
      this.showDialog = true;
    },
  },
};
</script>

<style lang="scss">
.set {
  font-weight: 700;
  width: 100%;
  height: 100vh;
  position: relative;

  .agreement {
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.2);
    position: absolute;
    top: 50%;
    left: 0;
    transform: translateY(-50%);
    z-index: 1;
  }

  .fiexd {
    width: 100%;
    height: 84upx;
    padding: 0 100upx;
    position: fixed;
    bottom: 150upx;
    left: 0;

    button {
      border: 2upx solid #999;
      border-radius: 42upx;
      width: 100%;
      height: 100%;
      line-height: 84upx;
    }
  }
}
</style>
