<template>
  <view class="login">
    <view class="login-main flex-box">
      <image
        class="login-main-logo flex-box"
        src="https://manager-test.pinbar.vip/img/logo.e982a56c.png"
      ></image>
			<view class="fs48 mt20">拼叭</view>
			<view class="fs36 color-666">组人组局用拼叭</view>
      <!-- <button
        v-if="hasLogin"
        class="login-main-btn flex-box"
        type="default"
        @click="loginSuccess"
      >
        <u-icon name="weixin-fill" size="50"></u-icon>
        <text class="login-main-btn-text">微信账号快捷登录</text>
      </button> -->
      <button
        class="login-main-btn flex-box"
        type="default"
        open-type="getPhoneNumber"
        @getphonenumber="getPhoneNumber"
				:disabled="isLoading"
      >
        <!-- <u-icon name="weixin-fill" size="50"></u-icon> -->
        <text class="login-main-btn-text">微信账号快捷登录</text>
      </button>
    </view>
		<view class="footer">
		  <view class="footer-tips">登录/注册即为同意</view>
		  <view class="footer-work">
		    <navigator hover-class="none" url="/pagesA/user/agreement-service">《拼叭用户服务协议》</navigator
		    >和<navigator hover-class="none" url="/pagesA/user/agreement-private">《隐私协议》</navigator>
		  </view>
		</view>
  </view>
</template>

<script>
import LoginService from "../../service/login.js"
import UserService from '../../service/user.js'
import { cosUploadFiles } from "../cos/utils"
export default {
  data() {
    return {
      code: "",
			path:'',
			isOnload:false,
			isJumpIndex:false,
			isLoading:false
    };
  },
  // async mounted() {
  //   await this.getCode();
  // },
  //监听返回
  onBackPress() {
    this.$toast("请先登录");
    return true;
  },
  destroyed() {
    // LoginService.getLoginCbPromise().reject();
  },
	onShow(){
		uni.hideHomeButton()
	},
	async onLoad(option){
		this.code = await LoginService.getWxCode()
		uni.removeStorageSync('loginPast')
		if(option.isOnload){
			this.isOnload = option.isOnload
		}
		if(option.isJumpIndex){
			this.isJumpIndex = option.isJumpIndex
		}
	},
  methods: {
    //手机号授权登录
    async getPhoneNumber(res) {
			const that = this
			this.isLoading = true;
      let { encryptedData, iv } = res.detail || {};
      if (!encryptedData || !iv) {
        this.$toast("请先授权登录！");
				this.isLoading = false;
        return;
      }
      uni.checkSession({
        success: (res) => {
					let params = {
					  code: that.code,
					  encryptedData,
					  iv,
					};
					that.goLogin(params);
        },
        fail: async (err) => {
          that.code = await LoginService.getWxCode()
					let params = {
					  code: that.code,
					  encryptedData,
					  iv,
					};
					that.goLogin(params);
        },
      })
			// let params = {
			//   code: this.code,
			//   encryptedData,
			//   iv,
			// };
			// this.goLogin(params);
    },
		jump(){
			if(this.isOnload){
				const pages = getCurrentPages()
				const prevPage = pages[pages.length - 2]
				uni.reLaunch({
					url:prevPage.$page.fullPath
				})
				// uni.navigateTo({
				// 	url:prevPage.$page.fullPath
				// })
			}else if(this.isJumpIndex){
				uni.switchTab({
					url:"/pages/index/index"
				})
			}else{
				uni.navigateBack();
			}
		},
    goLogin(data) {
			const that = this
      LoginService.sendLogin(data)
        .then(async (res) => {
					this.isLoading = false
					if(!res.ticket){
						this.code = await LoginService.getWxCode()
						return
					}
					uni.setStorageSync('myUserTicket', res.ticket)
					uni.setStorageSync('userInfo', res)
					if(res.head){
						that.jump()
					}else{
						uni.showModal({
							title:'登录',
							content:"拼叭想要获取您的头像",
							showCancel:false,
							success(res) {
								if(res.confirm){
									let userInfo = uni.getStorageSync("userInfo") || {}
									uni.getUserProfile({
										desc: "完善信息",
										async success(res) {
											uni.showLoading()
											const _Date = new Date()
											const key = `${_Date.getFullYear()}/${_Date.getMonth() + 1}/${
											  userInfo.ticket + Date.now()
											}`
											const head = res.userInfo.avatarUrl
											userInfo.nickName = res.userInfo.nickName
											userInfo.sex = res.userInfo.gender
											const headUrl = await uni.downloadFile({ url: head})
											cosUploadFiles([{path:headUrl[1].tempFilePath}], key, (err, result) => {
												if(err){
													this.$toast("头像上传失败！")
												}else{
													userInfo.head = result.files[0].data ? 'https://' + result.files[0].data.Location : head
													LoginService.bindInfo(userInfo)
													uni.setStorageSync("userInfo", userInfo)
													uni.hideLoading()
													that.jump()
												}
											})
										}
									})
								}
							}
						})
					}
          this.$store.commit('setIdentityType');
          // LoginService.getLoginCbPromise().resolve();
        })
        .catch((e) => {
					console.log(e)
          this.isLoading = false
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.login {
  background: #fff;
  height: 100vh;

  &-main {
    flex-flow: column;
    padding-top: 200upx;

    &-logo {
      width: 140upx;
      height: 140upx;
    }

    &-btn {
      width: 500upx;
      height: 88upx;
      background: linear-gradient(90deg, #FF4F3C 0%, #CD6CFD 100%);
      border-radius: 60upx;
      margin-top: 150upx;
      margin-bottom: 157rpx;
      font-size: 34upx;
      color: #ffffff;
      &-text {
        margin-left: 20upx;
      }
    }
  }
}

.footer {
  text-align: center;
  margin-top: 82upx;

  &-tips {
    font-size: 28upx;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #666666;
    line-height: 40upx;
  }

  &-work {
		display: flex;
		justify-content: center;
    margin-top: 20upx;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #333333;
    line-height: 40upx;
		font-size:22upx;
    navigator {
      cursor: pointer;
    }
  }
}
</style>
