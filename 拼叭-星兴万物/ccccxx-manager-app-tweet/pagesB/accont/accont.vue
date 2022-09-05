<template>
  <view class="box">
    <view class="withdraw">
      <view class="my">我的收益</view>
      <view class="money">
        <view class="extracting-money">
          <view class="mon1">￥{{ turnString(res.balanceMoney) }}</view>

          <view class="mon2">可提余额</view>
        </view>
        <view class="frozen-money">
          <view class="mon1">￥{{ turnString(res.frozen) }}</view>
          <view class="mon2">待结算金额</view>
        </view>
      </view>
      <view class="withdraw-button">
        <button class="btn" @click="change">发起提现</button>
      </view>
      <view class="record" @click="depositRecord()">
        提现记录
        <u-icon name="arrow-right"></u-icon>
      </view>
    </view>
    <view class="choose">
      <text :class="{ active: navId == 1 }" @click="toggleNav(1)">账单</text>
      <text :class="{ active: navId == 2 }" @click="toggleNav(2)">团队</text>
    </view>
    <view v-if="list.length">
      <view class="list" v-for="(item, index) in list" :key="index">
        <view class="message" v-if="(item.money && navId == 1) || navId == 2">
          <img class="image" :src="item[keyValue].head" />
          <view class="buy-msg">
            <view class="name">
              {{ item[keyValue].nickName }}
              <view v-if="item.settlement === 0" class="state wait">已到账</view>
              <view v-if="item.settlement === 1" class="state fail">待结算</view>
              <view v-if="item.settlement === 2" class="state success">已销毁</view>
            </view>
            <view class="buy">{{ item.desc || '' }}</view>
            <view class="banlance">
              {{
              $utils.dayjs(item.createTime).format('YYYY/MM/DD HH:mm:ss')
              }}
            </view>
          </view>
          <view class="push-money" v-if="item.money">+￥{{ turnString(item.money) }}</view>
        </view>
      </view>
    </view>
    <view v-else class="no-more">没有更多数据了</view>

    <u-modal
      v-model="show"
      :show-confirm-button="false"
      :show-cancel-button="false"
      :negative-top="350"
      :show-title="false"
      mode="top"
    >
      <view class="pop-up">
        <view class="tx1">提现金额</view>
        <text class="tx2">
          可提现余额￥{{
          res.balanceMoney
          }}，提现金额单笔需要在20～200元之内
        </text>
        <view class="tx3">
          费率{{res.rate*100}}%，减￥{{loseMoney}}
        </view>
        <input type="number" v-model="value" class="input" />
        <view style="height: 20upx">
          <view class="warning" v-show="warning">输入金额不在提现限定额度范围内</view>
        </view>

        <view class="btn">
          <button class="bt1" @click="show = false">取消</button>
          <button class="bt2" @click="$u.debounce(confirm),500">确认</button>
        </view>
      </view>
    </u-modal>
  </view>
</template>
<script>
import userService from '../../service//user'
import gameService from '../../service/game'
export default {
  data() {
    return {
      navId: 1,
      value: '',
      show: false,
      keyshow: false,
      warning: false,
      loseMoney:'0',
      list: [],
      keyValue: 'buyUser',
      res: {
        number: 0,
        balanceMoney: 0,
        gainMoney: 0,
        frozen: 0,
      },
      query: {
        pageSize: 10,
        pageNum: 1,
      },
      query1: {
        pageNum: 1,
        pageSize: 10,
        type: 1,
      },
    }
  },
  onShow(){
    this.getMyBalance()
  },
  onLoad(o) {
    this.$eventRecord(250);
    this.getAwardDetail()
    const userInfo = uni.getStorageSync('userInfo')
  },
  watch: {
    value(n) {
      this.loseMoney=this.turnString(Math.floor(n*this.res.rate*100+0.9)/100)
      this.warning = false
      if (n < 20 || n > 200) this.warning = true
    },
  },
  methods: {
    turnString(num) {
      var re = /^[0-9]+$/
      var re2 = /^[0-9]+(\.[0-9]{1})$/
      if (re.test(num)) {
        num += '.00'
      }
      if (re2.test(num)) {
        num += '0'
      }
      return num
    },
    getAwardDetail() {
      userService.getAwardDetail(this.query.pageNum).then((res) => {
        this.list = this.list.concat(res)
      })
    },
    brokerage() {
      gameService.brokerage(this.query1).then((res) => {
        this.list = this.list.concat(res)
      })
    },
    depositRecord() {
      uni.navigateTo({
        url: '/pagesB/accont/moneyRecord',
      })
    },
    getMyBalance() {
      userService.getBalance(this.myUserId).then((res) => {
        Object.assign(this.res, res, {
          gainMoney: res.balanceMoney + res.drawMoney,
        })
      })
    },
    toggleNav(id) {
      this.navId = id
      if (id === 1) {
        this.$eventRecord(254);
        this.keyValue = 'buyUser'
        this.list = []
        this.query.pageNum = 1
        this.getAwardDetail()
      } else {
        this.$eventRecord(252);
        this.keyValue = 'user'
        this.list = []
        this.query1.pageNum = 1
        this.brokerage()
      }
    },
    change() {
      this.$eventRecord(251);
      if (this.res.balanceMoney < 20) {
        return this.$toast('当前暂无可提现余额或提现不足20元')
      }
      this.show = true
    },
    confirm() {
      if (!this.value) return this.$toast('请输入提现金额')
      if (this.value < 20 || this.value > 200) return this.$toast('提现金额单笔需要在20～200元之内')
      const userInfo = uni.getStorageSync('userInfo')
      try {
        userService
          .applyDeposit(this.myUserId, userInfo.mobile, this.value)
          .then((res) => {
            if(!res[0]) { 
             this.$toast('提现成功!')
             this.show=false;
            setTimeout(() => this.depositRecord(), 1500)
            }

          })
      } catch {
        this.$toast('提现失败!')
      }
    },
    valChange(val) {
      this.value += val
    },
    backspace() {
      if (this.value.length)
        this.value = this.value.substr(0, this.value.length - 1)
    },
  },
  onReachBottom() {
    if (this.navId === 1) {
      this.query.pageNum++
      this.getAwardDetail()
    } else {
      this.query1.pageNum++
      this.brokerage()
    }
  },
  onPullDownRefresh() {
    this.list = []
    if (this.navId === 1) {
      this.query.pageNum = 1
      this.getAwardDetail()
    } else {
      this.query.pageNum = 1
      this.brokerage()
    }
  },
}
</script>

<style lang="scss" scoped>
.box {
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
  .tan {
    margin-top: 80upx;
  }
  .withdraw {
    display: flex;
    flex-direction: column;
    background-color: #ffffff;
    .my {
      padding-top: 18upx;
      padding-left: 42upx;
      font-size: 30upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
      line-height: 42upx;
    }
    .money {
      display: flex;
      margin-top: 38upx;
      align-items: center;
      .extracting-money {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding-left: 146upx;
      }
      .frozen-money {
        display: flex;
        flex-direction: column;
        margin-left: 210upx;
      }
      .mon1 {
        font-size: 32upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #ff5665;
        line-height: 44upx;
      }
      .mon2 {
        padding-top: 12upx;
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
        line-height: 36upx;
      }
    }
    .withdraw-button {
      margin-top: 38upx;
      padding-bottom: 60upx;
      .btn {
        background: #000000;
        border-radius: 44px;
        margin-left: 90upx;
        margin-right: 90upx;
        font-size: 28upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #ffffff;
        line-height: 64upx;
      }
    }
    .record {
      margin-top: 38upx;
      padding-bottom: 40upx;
      text-align: center;
      font-size: 26upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #333333;
      line-height: 36upx;
    }
  }
  .choose {
    display: flex;
    margin-top: 16upx;
    height: 102upx;
    padding-top: 24upx;
    background-color: #ffffff;
    text {
      margin-left: 34upx;
      width: 80upx;
      text-align: center;
      font-size: 28upx;
      font-weight: 400;
      color: #666666;
      border-bottom: 4upx solid transparent;
    }
    .active {
      font-weight: 600 !important;
      color: #000000 !important;
      border-bottom: 8upx solid #000000;
    }
  }
  .list {
    padding-top: 16upx;
    width: 100%;
    background-color: #ffffff;
    .message {
      display: flex;
      position: relative;
      margin: 16upx 36upx 0 38upx;
      background: #fbf7f7;
      border-radius: 12upx;
      .image {
        margin-left: 32upx;
        margin-top: 34upx;
        width: 94upx;
        height: 94upx;
      }
      .buy-msg {
        display: flex;
        margin-top: 34upx;
        flex-direction: column;
        margin-left: 20upx;
        .name {
          font-size: 28upx;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #000000;
          line-height: 40upx;
          display: inline-flex;
        }
        .buy {
          font-size: 24upx;
          width: 380rpx;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #000000;
          line-height: 34upx;
        }
        .banlance {
          padding-top: 16upx;
          padding-bottom: 30upx;
          font-size: 24upx;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #000000;
          line-height: 34upx;
        }
      }
      .push-money {
        margin-top: 30upx;
        font-size: 32upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #ff5665;
        line-height: 44upx;
      }
    }
  }
  .pop-up {
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 684upx;
    background: #ffffff;
    border-radius: 16px;
    .tx1 {
      margin-top: 90upx;
      font-size: 48upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #000000;
      line-height: 66upx;
    }
    .tx2 {
      margin-top: 28upx;
      width: 378upx;
      display: flex;
      font-size: 28upx;
      text-align: center;
      height: 80upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #000000;
    }
    .tx3{
      padding-top: 14upx;
      font-size: 28upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #FF4F3A;
    }
    .input {
      margin-top: 34upx;
      width: 512upx;
      height: 96upx;
      font-size: 50upx;
      background: #ffffff;
      border-radius: 24upx;
      border: 2upx solid #c0b6b6;
      padding: 0 40rpx;
    }
    .warning {
      margin-top: 14upx;
      font-size: 28upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #ff5665;
      line-height: 40upx;
    }
    .btn {
      display: flex;
      margin-top: 100upx;
      padding-bottom: 60upx;
      .bt1 {
        border-radius: 44upx;
        border: 2upx solid #000000;
        padding: 22upx 92upx 22upx 92upx;
        font-size: 28upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #000000;
        line-height: 40upx;
      }
      .bt2 {
        margin-left: 32upx;
        background: #000000;
        border-radius: 44upx;
        padding: 22upx 92upx 22upx 92upx;
        font-size: 28upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #ffffff;
        line-height: 40upx;
      }
    }
  }
}
.no-more {
  text-align: center;
  line-height: 80upx;
  background: #fff;
}

.state {
  margin-left: 20upx;
  padding-left: 16upx;
  padding-right: 16upx;
  border-radius: 20upx;
  font-size: 24upx;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  line-height: 34upx;
}
.wait {
  border: 2upx solid #2eaf30;
  color: #2eaf30;
}
.success {
  border: 2upx solid #22a7d5;
  color: #22a7d5;
}
.fail {
  border: 2upx solid #ff5665;
  color: #ff5665;
}
</style>
