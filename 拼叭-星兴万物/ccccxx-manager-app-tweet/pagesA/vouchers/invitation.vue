<template>
  <view class="invitation">
    <comHead changeColor autoPadding class="com-head">
      <view class="head"> </view>
    </comHead>
    <image
      class="head-img"
      mode="widthFix"
      :style="{ top: 0 - headHeight + 'px' }"
      src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/15eb9e1f-b55d-4b88-ad36-250fa1a3eade.png"
    ></image>
    <view class="steps" :style="{ marginTop: 0 - headHeight - 30 + 'px' }">
      <text class="steps__text">分享给好友</text>
      <image class="steps__icon" src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/bfb89e5c-2ff4-4945-8f8d-64ae6f6b0217.png" />
      <text class="steps__text">好友购买券包获得返佣</text>
      <image class="steps__icon" src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/bfb89e5c-2ff4-4945-8f8d-64ae6f6b0217.png" />
      <text class="steps__text">发起提现</text>
    </view>
    <view class="box1">
      <view class="box1__btn" @click="isShareDialog(1)">
        <image
          class="box1__img"
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/d7e09968-2f67-46b8-8239-2c13b3449a24.png"
        />
        <view class="box1__text">
          <view>面对面</view>
          <view>邀请好友</view>
        </view>
      </view>

      <button class="box1__btn" open-type="share">
        <image
          class="box1__img"
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/5957a17a-b0a3-429a-8824-48d10438a09e.png"
        />
        <view class="box1__text">
          <view>分享</view>
          <view>微信好友</view>
        </view>
      </button>

      <view class="box1__btn" @click="isShareDialog(2)">
        <image
          class="box1__img"
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/821fe2de-4b1a-4c29-8c93-eac5f8472a5b.png"
        />
        <view class="box1__text">
          <view>分享</view>
          <view>朋友圈</view>
        </view>
      </view>
    </view>
    <view class="box2">
      <view class="box2__left">
        <text class="box2__text">我的成就</text>
      </view>
      <view class="box2__btn" @click="depositRecord">提现记录 ></view>
    </view>

    <view class="box3">
      <view class="box3__item">
        <view class="box3__text">
          <text class="box3-num">{{ number }}</text
          >人
        </view>
        <view>邀请购买人次</view>
      </view>

      <view class="box3__item">
        <view class="box3__text">
          <text class="box3-num">{{ gainMoney }}</text
          >元
        </view>
        <view>已获得奖励</view>
      </view>

      <view class="box3__item">
        <view class="box3__text">
          <text class="box3-num">{{ gainMoney - myBalance }}</text
          >元
        </view>
        <view>已提现奖励</view>
      </view>

      <view class="box3__item">
        <view class="box3__text">
          <text class="box3-num">{{ myBalance }}</text
          >元
        </view>
        <view>余额</view>
      </view>
    </view>
    <!-- 购买优惠券用户列表 -->
    <view class="list" :key="item" v-for="item in myShareList">
      <view class="list__left">
        <image class="list__head" :src="item.buyUser.head" />
        <view class="list__info">
          <view class="list-name">{{ item.buyUser.nickName }}</view>
          <text class="list-time">已购买{{ item.desc }}</text>
        </view>
      </view>
      <view class="list-text">
        <view class="award">+{{ item.money }}元</view>
        <view class="">{{
          $utils.dayjs(item.createTime).format('YYYY/MM/DD HH:mm:ss')
        }}</view>
      </view>
    </view>
    <view class="empty-tip" v-if="myShareList.length >= number">
      <image
        class="empty-icon"
        src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/empty-icon.png"
      ></image>
      已经到底了呢！
    </view>
    <!-- 分享好友界面 -->
    <share
      v-if="shareDialog"
      :codeUrl="codeUrl"
      @cancel="shareDialog = false"
    />
    <!-- 分享朋友圈界面 -->
    <share-two
      v-if="shareTwoDialog"
      :codeUrl="codeUrl"
      :bgImgUrl="bgImgUrl"
      @cancel="shareTwoDialog = false"
    />
    <!-- 提现弹窗 -->
    <view class="money shadow" catchtouchmove="preventMove" v-if="moneyDialog">
			<view class="shadow-body">
        <view class="title">发起提现</view>
        <view class="content">
					<view>注:每次提现金额需在20~200元之间</view>
					<input type="number" v-model="money" placeholder="请输入提现金额"/>
				</view>
        <view class="btns">
          <view class="cancel" @click="moneyDialog = false">取消</view>
          <view @tap="deposit" class="confirm">确定</vieww>
        </view>
      </view>
			</view>
    </view>

    <view class="foot">
      <view class="deposit" @click="showMoneyDialog">发起提现</view>
    </view>
  </view>
</template>
<script>
import comHead from '../../components/com-head.vue';
import share from '../components/Share.vue';
import shareTwo from '../components/Share-two.vue';
import { config } from '@/framework';
import userService from '../../service/user';
import gameService from '../../service/game';
import basic from '../../service/basic';
export default {
  components: {
    comHead,
    share,
    shareTwo,
  },
  data() {
    return {
      statusBarHeight:
        uni.getSystemInfoSync().statusBarHeight + config.titleBarHeight,
      headHeight:
        (config.titleBarHeight + uni.getSystemInfoSync().statusBarHeight) / 2 +
        4,
      shareDialog: false,
      shareTwoDialog: false,
      moneyDialog: false,
      myShareList: [],
      bgImgUrl:
        'https://image-1306191496.cos.ap-nanjing.myqcloud.com/9bde5da3-140e-4a01-b305-86e170860780.png?imageView2/1/w/321/h/622/q/80/webp',
      codeUrl: '',
      code: '',
      myBalance: 0,
      mobile: '',
      gainMoney: 0,
      number: 0,
      pageNum: 1,
      shareUserId: '',
			money:''
    };
  },
  onLoad(o) {
    if (o.shareUserId) {
      this.shareUserId = o.shareUserId;
    }
    if (this.shareUserId && this.myUserId) {
      this.$authorization();
      gameService.bindRelation({ parentId: this.shareUserId, parentType: 1 });
    }
    const userInfo = uni.getStorageSync('userInfo');
    this.mobile = userInfo.mobile;
    this.getMyShareList();
    this.getMyBalance();
  },
  onShareAppMessage(res) {
    this.$eventRecord(168);
    return {
      title: '来拼叭玩剧本/密逃/酒吧，时时拼车，天天打折！',
      path: `pagesA/vouchers/buy?shareUserId=${this.myUserId}&isHome=1`,
      imageUrl:
        'https://image-1306191496.cos.ap-nanjing.myqcloud.com/aa363138-e569-4caf-b67b-3326244be718.png',
    };
  },
  onReachBottom() {
    if (this.myShareList.length < this.number) {
      this.pageNum++;
      this.getMyShareList();
    }
  },
  methods: {
    depositRecord() {
      uni.navigateTo({
        url: '/pagesA/vouchers/depositRecord',
      });
    },
    getMyBalance() {
      userService.getBalance(this.myUserId).then((res) => {
        this.number = res.number;
        this.myBalance = res.balanceMoney;
        this.gainMoney = res.balanceMoney + res.drawMoney;
      });
    },
    // getphonenumber(e) {
    //   const params = {
    //     code: this.code,
    //     encryptedData: e.detail.encryptedData,
    //     iv: e.detail.iv,
    //   };
    //   this.getUserPhone(params);
    // },
    // getUserPhone(params) {
    //   login
    //     .getUserPhone(params)
    //     .then((res) => {
    //       this.mobile = res.data;
    //       this.deposit();
    //     })
    //     .catch((err) => (this.loading = false));
    // },
    //提现
    deposit() {
			if(this.money < 20 || this.money > 200){
				this.$toast('提现金额需在20~200之间!')
			}else{
				uni.showLoading({mask:true})
				userService
				  .applyDeposit(this.myUserId, this.mobile,this.money)
				  .then((res) => {
				    this.$toast('提现申请成功!');
				    this.getMyBalance();
				    uni.hideLoading();
						this.moneyDialog = false
						setTimeout(() => uni.navigateTo({url:"/pagesA/vouchers/depositRecord"}),1500)
				  }).catch((err) => {
						this.moneyDialog = false
						setTimeout(() => uni.hideLoading(),1500)
					});
			}
    },
    isShareDialog(i) {
      if (i === 1) {
        this.$eventRecord(167);
        this.shareDialog = true;
      } else {
        this.$eventRecord(169);
        this.shareTwoDialog = true;
      }
      let parmas = {
        scene: `shareUserId${this.myUserId}`,
        isHyaline: false,
        width: 300,
        autoColor: false,
        lineColor: { r: '0', g: '0', b: '0' },
        page: 'pagesA/vouchers/buy',
      };
      basic.getMiniProgramQrcodeApi(parmas).then((res) => {
        this.codeUrl = 'data:image/png;base64,' + res;
      });
    },

    // 获取下级购买优惠券列表
    async getMyShareList() {
      let res = await userService.getAwardDetail(this.pageNum);
      this.myShareList = this.myShareList.concat(res);
    },
    // 发起提现按钮
    showMoneyDialog() {
      this.$eventRecord(166);
      const that = this;
      if (this.myBalance > 0) {
				this.moneyDialog = true
        // if(this.myBalance < 20){
        // 	this.moneyDialog = true
        // }else{
        // 	this.loading = true
        // 	this.deposit()
        // }
        // if (this.mobile && !this.loading) {
        //   this.loading = true;
        //   this.deposit();
        // } else if (!this.loading) {
        //   this.loading = true;
        //   this.moneyDialog = true;
        //   uni.login({
        //     provider: 'weixin',
        //     success(res) {
        //       that.code = res.code;
        //     },
        //     fail(err) {
        //       this.loading = false;
        //     },
        //   });
        // }
      } else {
        this.$toast('当前暂无可提现余额');
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.invitation {
  min-height: 100vh;
  padding-bottom: calc(20upx + env(safe-area-inset-bottom));
}
.head {
  line-height: 1em;
  font-size: 32upx;
  color: #fff;
}
.head-img {
  position: relative;
  z-index: -1;
  left: 0;
  top: 0;
  width: 100%;
}
.com-head {
  height: 430upx;
}
.steps {
  position: absolute;
  z-index: 999;
  width: 690upx;
  display: flex;
  align-items: center;
  justify-content: space-around;
  margin-left: 30upx;
  height: 104upx;
  background: #fdecec;
  border-radius: 20upx;
  &__text {
    width: 165upx;
    text-align: center;
    color: #771421;
    font-size: 24upx;
    font-weight: bold;
  }
  &__icon {
    width: 32upx;
    height: 32upx;
  }
}
.box1 {
  margin: 20upx 30upx 60upx;
  display: flex;
  justify-content: space-between;
  &__btn {
    display: flex;
    padding: 10upx 0 0 24upx;
    width: 210upx;
    height: 96upx;
    text-align: left;
    background: linear-gradient(270deg, #cd6cff 0%, #fe4a2a 100%);
    border-radius: 30upx;
  }
  &__img {
    margin-top: 14upx;
    width: 48upx;
    height: 48upx;
    margin-right: 16upx;
  }
  &__text {
    font-size: 24upx;
    line-height: 36upx;
    color: #fff;
  }
}
.box2 {
  padding: 0 30upx;
  width: 100%;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  &__icon {
    width: 32upx;
    height: 48upx;
    margin-right: 10upx;
  }
  &__left {
    display: flex;
    align-items: center;
    height: 60upx;
  }
  &__text {
    margin-top: -3px;
    font-size: 36upx;
    color: #771421;
    font-weight: 600;
  }
  &__btn {
    width: 200upx;
    line-height: 55upx;
    height: 55upx;
    text-align: center;
    color: #771421;
    font-size: 26upx;
    border-radius: 48upx;
    border: 1px solid #771421;
  }
}
.box3 {
  box-sizing: border-box;
  position: relative;
  text-align: center;
  margin: 32upx 0;
  padding-bottom: 24upx;
  width: calc(100% - 60upx);
  margin-left: 30upx;
  height: 150upx;
  background: linear-gradient(to bottom, #f8f0f0, #fff);
  border-radius: 10px;
  display: flex;
  &-num {
    font-size: 44upx;
    margin-right: 10upx;
  }
  &__item {
    display: inline-block;
    width: 50%;
    color: #771421;
    font-size: 20upx;
    font-weight: bold;
  }
  &__text {
    color: #ff0324;
    padding-top: 30upx;
  }
  &__hr {
    position: relative;
    top: 28upx;
    width: 2upx;
    height: 120upx;
  }
}
.money {
  width: 100%;
  height: 100vh;
  position: absolute;
  top: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1000;
  .card {
    margin: 0 98upx;
    background-color: #fff;
    border-radius: 32upx;
    text-align: center;
    position: relative;
    .title {
      padding: 40upx 25px 36upx;
      font-size: 40upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #000000;
      letter-spacing: 2px;
    }
    .content {
      padding: 0 40upx;
      margin-bottom: 40upx;
    }
    image {
      width: 340upx;
      height: 340upx;
      margin-bottom: 40upx;
    }
  }
  .close {
    width: 100%;
    position: absolute;
    bottom: -140upx;
    text-align: center;
    image {
      width: 84upx;
      height: 84upx;
    }
  }
}
.shadow {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
  .shadow-body {
    background: #fff;
    width: 80%;
    border-radius: 12upx;
    .title {
      text-align: center;
      line-height: 80upx;
      font-size: 32upx;
      font-weight: 600;
    }
    .content {
      padding: 10upx 40upx 40upx 40upx;
      font-size: 28upx;
      color: #999;
			input{
				border:1px solid #ccc;
				border-radius: 12upx;
				height:70upx;
				padding:0 10upx;
				margin-top: 20upx;
				font-size:28upx;
			}
    }
    .btns {
      display: flex;
      height: 100upx;
      line-height: 100upx;
      text-align: center;
      font-size: 32upx;
      border-top: 1px solid #eee;
			view{
				width: 50%;
			}
			.confirm{
				border-bottom-right-radius: 12upx;
				color:#ff0324;
			}
      .cancel {
        border-right: 1px solid #eee;
        color: #999;
      }
    }
  }
}
.list {
  margin: 0 30upx 48upx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  &-name {
    font-weight: bold;
    color: #771421;
    font-size: 28upx;
    line-height: 40upx;
  }
  &-time {
    font-size: 20upx;
    color: #666666;
  }
  &-text {
    color: #771421;
    font-size: 24upx;
  }
  &__left {
    height: 96upx;
    display: flex;
    align-items: center;
  }
  &__head {
    margin-right: 28upx;
    height: 96upx;
    width: 96upx;
    border-radius: 50%;
  }
  .award {
    color: red;
    font-size: 26upx;
    text-align: right;
  }
}
.empty-tip {
  margin-top: 39upx;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 26upx;
  font-family: PingFangSC-Light, PingFang SC;
  font-weight: 300;
  color: #999 !important;

  .empty-icon {
    height: 64upx;
    width: 64upx;
    margin-right: 18upx;
    color: #fff !important;
  }
}
.foot {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100upx;
  background: #fff;
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  .deposit {
    height: 80upx;
    line-height: 80upx;
    width: 90%;
    border-radius: 40px;
    color: #fff;
    text-align: center;
    background: linear-gradient(to right, #554a44, #292320);
  }
}
</style>
