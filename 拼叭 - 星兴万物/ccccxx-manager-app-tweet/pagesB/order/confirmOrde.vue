<template>
  <view class="box">
    <view class="userinfo">
      <view class="userinfo-top">
        <view class="message">
          <view class="hint">您的信息</view>
          <input
            type="text"
            class="name"
            v-model="username"
            placeholder="联系名称"
          />
          <input
            type="number"
            class="phone"
            v-model="phone"
            placeholder="联系电话"
            maxlength="11"
          />
        </view>
        <view class="userinfo-bottom">
          <view class="msg1">· 名称方便商家称呼您</view>
          <view class="msg2">· 对商家隐藏您的真实手机号，保护您的隐私</view>
        </view>
      </view>
    </view>
    <view class="pay">
      <view class="pay-top pr30">
        <view class="trade-name" @tap="jumpDetail">{{
          goodInfo.companyName
        }}</view>
        <u-icon name="arrow-right" style="margin-left: 16upx"></u-icon>
        <img
          @click.stop="callPhone(goodInfo.companyPhone)"
          class="phone-icon"
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/5685faee-c1ef-400c-abe7-62cf8f1d520f.png"
          alt=""
        />
      </view>
      <view class="pay-mid">
        <view class="pay-mid-left">
          <image :src="goodInfo.goodsPic"></image>
        </view>

        <view class="pay-mid-right flex-1">
          <view class="num multiOmit">{{ goodInfo.goodsName }}</view>
          <view class="discount">{{ goodInfo.scriptName }}</view>
          <view class="price1 flex-baseline" v-if="goodInfo.salePrice">
            ￥{{ goodInfo.salePrice }}
            <view class="price2">门市价￥{{ goodInfo.marketPrice }}</view>
            <view class="number-box">
              <u-number-box
                :min="1"
                :max="10"
                disabled-input
                v-model="goodsNum"
                @change="valChange"
              ></u-number-box
            ></view>
          </view>
        </view>
      </view>
    </view>
    <view class="data-info" @tap="showTime">
      <view class="data">预定日期</view>
      <view class="time">
        <text>{{ goodInfo.bookDate }}</text>
        <u-icon name="arrow-right" style="margin-left: 16upx"></u-icon>
      </view>
    </view>
    <view class="remark flex flex-between">
      <view class="msg">备注</view>
      <textarea class="txt" v-model="remark" auto-height />
    </view>
    <view class="reserve-info">
      <view class="hint">
        <view class="msg">
          <u-icon name="order" style="padding-right: 20rpx"></u-icon>预定须知
        </view>
      </view>
      <view class="info">
        <view class="tx1"
          >1、预定成功后，展示订单二维码给商家核销即可完成消费</view
        >
        <view class="txt mt20"
          >2、为保护未成年人健康成长，未成年用户请得到监护人知悉同意后再进行购买和体验，请勿购买不适合自身年龄段的产品</view
        >
        <view class="txt mt20"
          >3、预定成功后，平台暂不支持修改订单，如您有时间调整等需求，请联系商家进行协商</view
        >
        <view class=""
          >4、预定成功后，开场时间前，您可以随时退款，开场时间后，您不可以退款</view
        >
      </view>
    </view>
    <view class="bottom fixed">
      <view class="money flex flex-center">
        <view class="flex flex-baseline">
          <view class="tx1">应付款</view>
          <view class="price">
            <text style="font-size: 36upx">￥</text>
            {{ goodInfo.price * goodsNum }}
          </view>
        </view>
      </view>
      <view>
        <button class="but" @tap="payment">确认支付</button>
      </view>
    </view>
    <u-popup mode="bottom" v-model="gameStartTimeShow">
      <view class="content">
        <zwyCalendar
          ref="calendar"
          type="sign"
          :activeDates="activeDates"
          :localDate="makeTimer"
          :showTimeSelect="false"
        />
      </view>
      <view class="footer-pop">
        <view class="btn" @click="gameStartTimeShow = false">取消</view>
        <view class="btn confirm" @click="buy">确定</view>
      </view>
    </u-popup>
  </view>
</template>

<script>
import productService from '../../service/product';
import zwyCalendar from '../commodity/components/zwy-calendar';

export default {
  components: {
    zwyCalendar,
  },
  data() {
    return {
      gameStartTimeShow: false,
      goodsNum: 1,
      username: '',
      phone: '',
      remark: '',
      goodInfo: {},
      isLoading: false,
      activeDates: {},
      makeTimer: '',
    };
  },
  async onLoad(option) {
    await this.setDefLogin();
    this.username = this.myUserInfo.nickName;
    this.phone = this.myUserInfo.mobile;
    if (option.goodInfo) {
      this.goodInfo = JSON.parse(option.goodInfo);
      this.makeTimer = this.goodInfo.bookDate;
    }
    this.getActivityDate();
  },
  methods: {
    valChange(e) {
      this.goodsNum = e.value;
    },
    callPhone(phone) {
      uni.makePhoneCall({
        phoneNumber: phone,
      });
    },
    showTime() {
      if (!this.goodInfo.isSeckill) {
        this.gameStartTimeShow = true;
      }
    },
    jumpDetail() {
      // url:`/pagesB/commodity/sInfo?id=${data.id}`
      uni.navigateTo({
        url: `/pagesB/commodity/sInfo?id=${this.goodInfo.goodsId}&isBtn=1`,
      });
    },
    getActivityDate() {
      productService
        .getGoodsDate({ goodsId: this.goodInfo.goodsId })
        .then((res) => {
          this.activeDates = res;
        });
    },
    confirmData() {
      const gameStartTime = `${this.$refs.calendar.localDateCopy}`;
      this.makeTimer = gameStartTime;
    },
    buy() {
      this.$u.throttle(this.confirmData, 500);
      if (!this.makeTimer) return this.$toast('请选择预定日期!');
      const obj = this.activeDates[this.makeTimer];
      this.goodInfo.price = obj.price;
      this.goodInfo.salePrice = obj.salePrice;
      this.goodInfo.marketPrice = obj.marketPrice;
      this.goodInfo.skuId = obj.skuId;
      this.goodInfo.skuTimeId = obj.skuTimeId;
      this.goodInfo.stock = obj.stock;
      this.goodInfo.bookDate = this.makeTimer;
      this.gameStartTimeShow = false;
    },
    payment() {
      const that = this;
      if (!this.username) {
        this.$toast('请填写您联系的名称');
      } else if (!this.phone) {
        this.$toast('请填写您的联系电话');
      } else if(this.goodInfo.stock < this.goodsNum) { 
        this.$toast(`购买库存量不能超过${this.goodInfo.stock}`);
      } else if (!this.isLoading) {
        this.isLoading = true;
        const params = {
          goodsId: this.goodInfo.goodsId,
          skuId: this.goodInfo.skuId,
          skuTimeId: this.goodInfo.skuTimeId,
          bookDate: this.goodInfo.bookDate,
          callName: this.username,
          phone: this.phone,
          remark: this.remark,
          goodsNum: this.goodsNum,
        };
        // 分享出来的
        if (this.goodInfo.shareUserId) {
          params.shareUserId = this.goodInfo.shareUserId;
          params.shareUserType = 1;
        }
        productService
          .createOrder(params)
          .then((res) => {
            uni.requestPayment({
              provider: 'wxpay',
              timeStamp: res.timeStamp,
              nonceStr: res.nonceStr,
              package: 'prepay_id=' + res.prepayId,
              signType: res.signType,
              paySign: res.paySign,
              success(result) {
                uni.redirectTo({
                  url: `/pagesB/order/confirmOrdeBackup?orderNo=${res.orderNo}&companyPhone=${that.goodInfo.companyPhone}`,
                });
              },
              fail(err) {
                if (err.errMsg == 'requestPayment:fail cancel') {
                  uni.navigateTo({
                    url: `/pagesB/order/orderDetail?orderNo=${res.orderNo}`,
                  });
                }
              },
              complete() {
                that.isLoading = false;
              },
            });
          })
          .catch((err) => (this.isLoading = false));
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.box {
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
  padding-bottom: 140upx;
  .userinfo {
    display: flex;
    flex-direction: column;
    margin-top: 28upx;
    margin-left: 22rpx;
    margin-right: 20rpx;
    height: 208upx;
    background: #ffffff;
    border-radius: 16upx;
    .message {
      position: relative;
      display: flex;
      margin-left: 36upx;
      margin-top: 34upx;
      font-size: 26upx;
      color: #000000;
      .hint {
        padding-top: 17upx;
      }
      .name {
        margin-left: 44upx;
        width: 236upx;
        height: 72upx;
        text-align: center;
        background: #f5f5f5;
        border-radius: 8upx;
      }
      .phone {
        margin-left: 12upx;
        width: 236upx;
        height: 72upx;
        text-align: center;
        background: #f5f5f5;
        border-radius: 8upx;
      }
    }
    .userinfo-bottom {
      margin-top: 20upx;
      margin-left: 36upx;
      margin-left: 36upx;
      margin-top: 20upx;
      font-size: 22upx;
      color: #999999;
      line-height: 16px;
    }
  }
  .pay {
    display: flex;
    margin-top: 24upx;
    flex-direction: column;
    margin-left: 22rpx;
    margin-right: 20rpx;
    background: #ffffff;
    border-radius: 8px;
    padding-bottom: 30upx;
    .pay-top {
      display: flex;
      margin-left: 32upx;
      margin-top: 32upx;
      .trade-name {
        font-weight: 500;
        color: #000000;
        line-height: 40upx;
        font-size: 28upx;
      }
    }
    .pay-mid {
      display: flex;
      margin-left: 32upx;
      margin-top: 44upx;
      .pay-mid-left {
        image {
          width: 144upx;
          height: 200upx;
        }
      }
      .pay-mid-right {
        display: flex;
        flex-direction: column;
        margin-left: 18upx;
        .num {
          font-size: 32upx;
          font-weight: 600;
          color: #000000;
          height: 80upx;
        }
        .discount {
          margin-top: 12upx;
          font-size: 24upx;
          color: #333333;
          line-height: 34upx;
        }
        .price1 {
          display: flex;
          margin-top: 40upx;
          font-size: 32upx;
          color: #ff4f3a;
          line-height: 44upx;
        }
        .price2 {
          margin-left: 8upx;
          font-size: 20upx;
          color: #999999;
          line-height: 28upx;
          // text-decoration: line-through;
        }
      }
    }
  }
  .data-info {
    display: flex;
    position: relative;
    margin-top: 24upx;
    margin-left: 22rpx;
    margin-right: 20rpx;
    height: 130upx;
    background: #ffffff;
    border-radius: 16upx;
    .data {
      position: absolute;
      top: 48upx;
      left: 32upx;
      font-size: 26upx;
      color: #000000;
    }
    .time {
      position: absolute;
      top: 48upx;
      right: 36upx;
      font-size: 26upx;
      color: #000000;
    }
  }
  .remark {
    display: flex;
    position: relative;
    margin-top: 24upx;
    margin-left: 22rpx;
    margin-right: 20rpx;
    background: #ffffff;
    border-radius: 16upx;
    padding: 30upx;
    .msg {
      font-size: 26upx;
      color: #000000;
    }
    .txt {
      padding: 10upx;
      width: 488upx;
      background: #f5f5f5;
      border-radius: 8upx;
      font-size: 24upx;
      color: #999999;
      text-align: left;
      min-height: 80upx;
    }
  }
  .reserve-info {
    display: flex;
    flex-direction: column;
    margin-top: 24upx;
    margin-left: 22rpx;
    margin-right: 20rpx;
    background: #ffffff;
    border-radius: 16upx;
    padding-bottom: 20upx;
    .hint {
      display: flex;
      padding-left: 36upx;
      .msg {
        margin-top: 22upx;
        font-size: 26upx;
        color: #4b9afc;
        line-height: 36upx;
      }
    }
    .info {
      width: 636upx;
      padding-top: 24upx;
      padding-left: 36upx;
      .tx1 {
        display: inline-block;
        word-wrap: break-word;
        font-size: 26upx;
        color: #333333;
        line-height: 36upx;
      }
    }
  }
  .bottom {
    bottom: 0;
    left: 0;
    right: 0;
    display: flex;
    height: 124upx;
    background: #ffffff;
    box-shadow: 0px -1px 0px 0px rgba(218, 218, 218, 0.5);
    .money {
      display: flex;
      position: relative;
      align-items: center;
      .tx1 {
        margin-left: 44upx;
        font-size: 14px;
        color: #333333;
        line-height: 20px;
      }
      .price {
        margin-left: 14upx;
        font-size: 26px;
        color: #ff4f3a;
        line-height: 37px;
      }
    }
    .but {
      position: absolute;
      margin-top: 20upx;
      right: 40upx;
      width: 228upx;
      height: 80upx;
      background: linear-gradient(270deg, #ff9b46 0%, #dd0829 100%);
      border-radius: 60upx;
      font-size: 32upx;
      font-weight: 600;
      color: #ffffff;
      line-height: 82upx;
    }
  }
}
.footer-pop {
  height: 160upx;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  justify-items: center;
  padding: 0 76upx;
  padding-top: 30upx;
  .btn {
    flex-basis: 260upx;
    height: 100upx;
    line-height: 100upx;
    color: #000000;
    font-weight: bold;
    font-size: 36upx;
    text-align: center;
    background: rgba(153, 153, 153, 0.1);
    border-radius: 50px;
  }
  .confirm {
    background: linear-gradient(to right, #dd0829, #ff9b46);
    color: #fff;
  }
}
.phone-icon {
  width: 164upx;
  height: 54upx;
  position: absolute;
  right: 40upx;
}
.number-box {
  position: absolute;
  right: 40upx;
}
</style>
