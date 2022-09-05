<template>
  <view>
    <view v-if="list.length">
      <view class="box" v-for="(item, index) in list" :key="index">
        <view class="left">
          <view class="tx1">
            提现到微信零钱
            <view class="state wait">已到账</view>
          </view>
          <view class="time">
            {{
            $utils.dayjs(item.createTime).format("YYYY/MM/DD HH:mm:ss")
            }}
          </view>
        </view>
        <view class="right">
          <view class="push">+￥{{ turnString(item.money) }}</view>
          <!-- <view class="balance"> 余额￥33元</view> -->
        </view>
      </view>
    </view>
    <view v-else class="no-more">没有更多数据了</view>
  </view>
</template>

<script>
import userService from '../../service/user'

export default {
  data() {
    return {
      list: [],
      query: {
        pageSize: 10,
        pageNum: 1,
      },
    }
  },
  onLoad() {
    this.depositRcord()
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
    depositRcord() {
      userService.depositRcord(this.query).then((res) => {
        this.list = this.list.concat(res)
      })
    },
  },
  onReachBottom() {
    this.query.pageNum++
    this.depositRcord()
  },
  onPullDownRefresh() {
    this.list = []
    this.query.pageNum = 1
    this.depositRcord()
  },
}
</script>
<style lang="scss" scoped>
.box {
  display: flex;
  justify-content: space-between;
  margin: 20upx 40upx 0 38upx;
  background: #fbf7f7;
  border-radius: 12px;
  .left {
    display: flex;
    flex-direction: column;
    margin-left: 30upx;
    margin-top: 30upx;
    padding-bottom: 30upx;
    .tx1 {
      display: flex;
      font-size: 28upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
      line-height: 40upx;
      letter-spacing: 1upx;
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
    }
    .time {
      margin-top: 14upx;
      font-size: 24upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #000000;
      line-height: 34upx;
    }
  }
  .right {
    display: flex;
    flex-direction: column;
    margin-right: 26upx;
    margin-top: 30upx;
    text-align: right;
    .push {
      font-size: 32upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #ff5665;
      line-height: 44upx;
    }
    .balance {
      margin-top: 6upx;
      font-size: 24upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #999999;
      line-height: 34upx;
    }
  }
}
.no-more {
  margin-top: 70upx;
  text-align: center;
  font-size: 25upx;
  background: #fff;
}
</style>
