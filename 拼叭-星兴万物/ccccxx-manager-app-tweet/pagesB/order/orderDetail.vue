<template>
  <view class="box" v-if="info.state">
    <view class="head" v-if="info.state == 4">
      <view class="headmsg">已退款</view>
    </view>
    <view class="refund-status" v-if="info.state == 7">
      <view class=""><text class="headmsg">退款中</text></view>
      <view class="mt10">
        <text class="refund-countdown fs26 color-white"
          >倒计时：{{ hour }} 时 {{ minutes }} 分</text
        >
      </view>
      <view class="fs24 mt20"><text>请耐心等待，商家正在处理中…</text></view>
    </view>
    <view class="head" v-if="info.state == 6">
      <view class="headmsg">已取消</view>
    </view>
    <view class="head color-white obligation" v-if="info.state == 1">
      <view>
        <view class="fs36">待付款</view>
        <view class="fs24">{{ minutes }}分{{ second }}秒后失效</view>
      </view>
    </view>

    <view
      class="stocks mt20"
      v-if="info.state == 3 || info.state == 5 || info.state == 2"
    >
      <view class="tx1 fs36 color-croci">{{
        info.state == 3 ? '已完成' : info.state == 5 ? '已过期' : '待使用'
      }}</view>
      <view class="qrcode rel">
        <view class="abs mark" v-if="info.state == 5 || info.state == 3"></view>
        <image v-if="codeUrl" :src="codeUrl"></image>
      </view>
      <view class="id fs24 color-666" v-if="info.orderNo != undefined"
        >订单ID：{{ info.orderNo }}</view
      >

      <view class="" v-if="info.state == 2">
        <view
          class="fs26 color-white refund"
          @tap="refund"
          v-if="!info.notRefundDesc"
          >申请退款</view
        >
        <view class="fs22 color-666 mt30">请让商家扫描上面二维码完成核销</view>
      </view>
    </view>

    <view
      class="userinfo p30 mt20"
      v-if="info.state == 2 && info.notRefundDesc"
    >
      <view class="flex flex-middle fs26">
        <view class="">商家已驳回退款：</view>
      </view>
      <view class="fs22 color-tip mt20">{{ info.notRefundDesc }}</view>
      <view class="fs22 color-tip mt10"
        >如有疑问请联系平台客服微信号：bb5310bb</view
      >
    </view>

    <view class="userinfo p30 mt20">
      <view class="flex flex-middle fs26">
        <view class="">{{ info.callName }}</view>
        <view class="phone">{{ info.phone }}</view>
      </view>
      <view class="fs22 color-666 mt20">· 名称方便商家称呼您 </view>
      <view class="fs22 color-666"
        >· 对商家隐藏您的真实手机号，保护您的隐私</view
      >
    </view>

    <view class="pay p30 mt20">
      <view class="pay-top flex flex-middle">
        <view class="fs28 fw600 oneOmit" @click="goDetail">{{
          info.supplierDetailResponseDto.companyName
        }}</view>
        <u-icon name="arrow-right" class="ml20"></u-icon>
        <img
          @click.stop="callPhone(info.supplierDetailResponseDto.companyPhone)"
          class="phone-icon"
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/5685faee-c1ef-400c-abe7-62cf8f1d520f.png"
          alt=""
        />
      </view>
      <view class="pay-mid flex mt40">
        <view class="pay-mid-left" @click.stop="jump">
          <image :src="info.goodsPic"></image>
        </view>

        <view class="pay-mid-right flex-1 pl20">
          <view class="fs32 fw600">{{ info.skuName }}</view>
          <view class="mt10 fs24 color-333">预约日期：{{ info.bookDate }}</view>
          <view class="price">
            <text class="fs32 color-croci">￥{{ info.salePrice }}</text>
            <text class="fs20 color-999 ml10 mt10"></text>
            <view class="number-box"> x <text>{{ info.goodsNum || 1 }}</text></view>
          </view>
        </view>
      </view>
      <view class="flex flex-right mt20 flex-baseline">
        <text class="fs28">
          {{ info.state === 2 || info.state === 4 ? '实付款' : '应付款' }}
        </text>
        <text class="color-croci fs32 ml20">¥</text>
        <text class="fs50 color-croci">{{ info.actualAmount }}</text>
      </view>
    </view>
    <view class="data-info flex flex-between flex-middle p30 fs26 mt20">
      <view>预定日期</view>
      <view>{{ info.bookDate }}</view>
    </view>
    <view
      class="remark-msg flex flex-between flex-middle p30 fs26 mt20"
      v-if="info.remark"
    >
      <view class="label">备注</view>
      <view class="flex-1">{{ info.remark }}</view>
    </view>
    <view class="indent fs26 p30 mt20">
      <view>订单信息 </view>
      <view class="mt30">
        <text class="color-999">订单编号</text
        ><text class="ml40"> {{ info.orderNo }}</text>
      </view>
      <view class="mt30">
        <text class="color-999">下单时间</text
        ><text class="ml40">{{
          $utils.dayjs(info.createTime).format('YYYY-MM-DD HH:mm:ss')
        }}</text>
      </view>
      <view
        class="mt30"
        v-if="
          info.state == 4 ||
          info.state == 7 ||
          (info.state == 2 && info.notRefundDesc)
        "
      >
        <text class="color-999">申请退款时间</text
        ><text class="ml40">{{
          $utils.dayjs(info.applyRefundDate).format('YYYY-MM-DD HH:mm:ss')
        }}</text>
      </view>
      <view class="mt30" v-if="info.state == 4">
        <text class="color-999">退款时间</text
        ><text class="ml40">{{
          $utils.dayjs(info.refundDate).format('YYYY-MM-DD HH:mm:ss')
        }}</text>
      </view>
      <view class="mt30" v-if="info.state == 3">
        <text class="color-999">核销时间</text
        ><text class="ml40">{{
          $utils.dayjs(info.writeDate).format('YYYY-MM-DD HH:mm:ss')
        }}</text>
      </view>
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

    <view
      class="fixed foot flex flex-right flex-middle pr40"
      v-if="info.state == 2 || info.state == 6"
    >
      <view
        class="btns"
        @tap="refund"
        v-if="info.state == 2 && !info.notRefundDesc"
        >申请退款</view
      >
      <view class="dele-btn mr20" @tap="deleteOrder" v-if="info.state == 6"
        >删除</view
      >
      <view class="btns" @tap="createOrderAgain" v-if="info.state == 6"
        >重新下单</view
      >
    </view>
    <view
      class="fixed foot flex flex-right flex-middle pr40"
      v-if="info.state == 3 || info.state == 4 || info.state == 5"
    >
      <view class="dele-btn mr20" @tap="deleteOrder">删除</view>
    </view>
    <view
      class="fixed foot flex flex-between flex-middle pay-again"
      v-if="info.state == 1"
    >
      <view class="">
        <text class="fs28">应付款</text>
        <text class="color-croci fs32 ml20">¥</text>
        <text class="fs50 color-croci">{{ info.amount }}</text>
      </view>
      <view class="btns pay-btn" @tap="payAgain">去付款</view>
    </view>
  </view>
</template>

<script>
import ProductService from '../../service/product';
import basicService from '../../service/basic';
import qrcode from '../utils/qrcode';

export default {
  data() {
    return {
      orderNo: '',
      goodsNum: 1,
      info: {},
      isLoading: false,
      JUMP_URL: 'https://pinbar.vip/h5/supplier/home/',
      codeUrl: '',
      minutes: 0,
      second: 0,
      interId: null,
      hour: 0,
			writeInterId:null
    };
  },
  onLoad(option) {
    this.orderNo = option.orderNo;
    this.init();
  },
  onUnload() {
    if (this.interId) {
      clearInterval(this.interId);
    }
		if (this.writeInterId) {
		  clearInterval(this.writeInterId);
		}
  },
  methods: {
		createTimeLoad(usedCode) {
		  let that = this;
		  this.writeInterId = setInterval(() => {
		    return ProductService.getOrderWirteStatus({orderNo:this.orderNo}).then((res) => {
		      if (res === true) {
		        that.$toast("核销成功", "success");
		        clearInterval(that.writeInterId);
		        that.show = false;
						that.init();
		      }
		    });
		  }, 2000);
		},
    valChange(e) {
      this.goodsNum = e.value;
    },
    callPhone(phone) {
      uni.makePhoneCall({
        phoneNumber: phone,
      });
    },
    deleteOrder() {
      const that = this;
      uni.showModal({
        title: '温馨提示',
        content: '您确定要删除该订单吗?',
        success(res) {
          uni.showLoading();
          if (res.confirm) {
            ProductService.deleteOrder({ orderNo: that.orderNo })
              .then((res) => {
                that.$toast('订单删除成功');
                uni.hideLoading();
                setTimeout(() => {
                  uni.navigateBack();
                }, 1500);
              })
              .catch((err) => uni.hideLoading());
          }
        },
      });
    },
    jump() {
      uni.navigateTo({
        url: `/pagesB/commodity/sInfo?id=${this.info.goodsId}`,
      });
    },
    goDetail(item) {
      uni.navigateTo({
        url: `/pagesB/commodity/index?typeId=${this.info.supplierDetailResponseDto.typeId}&supplierId=${this.info.supplierDetailResponseDto.supplierId}`,
      });
    },
    init() {
      ProductService.orderDetail({ orderNo: this.orderNo }).then((res) => {
        this.info = res;
        if (res.state == 2 || res.state == 5 || res.state == 3) {
          this.showModal();
        }
        // this.countDown(60 * 12,new Date().getTime());
        if (res.state == 1) {
          this.countDown(15, res.createTime);
        }
        if (res.state == 7) {
          this.countDown(60 * 12, res.applyRefundDate);
        }
      });
    },
    refund() {
      const that = this;
      uni.showModal({
        title: '温馨提示',
        content: '您确定要退款吗?',
        success(res) {
          if (res.confirm) {
            if (!that.isLoading) {
              that.isLoading = true;
              ProductService.refund({ orderNo: that.orderNo })
                .then((res) => {
                  that.isLoading = false;
                  that.$toast('退款申请已提交!');
                  that.init();
                })
                .catch((err) => (that.isLoading = false));
            }
          }
        },
      });
    },
    countDown(m, timer) {
      const ms = 60 * 1000 * m;
      this.interId = setInterval(() => {
        const nowMs = new Date().getTime();
        const t = (ms - (nowMs - timer)) / 1000;
        if (t <= 0) {
          clearInterval(this.interId);
          if ((this.info.state = 1)) {
            this.info.state = 6;
          } else {
            this.info.state = 4;
          }
        } else {
          if (this.info.state == 7) {
            this.hour = parseInt(t / (60 * 60));
            this.minutes = parseInt((t - this.hour * 60 * 60) / 60);
          } else {
            this.minutes = parseInt(t / 60);
            this.second = parseInt(t - this.minutes * 60);
          }
        }
      }, 1000);
    },
    createOrderAgain() {
      const date = this.$utils.dayjs(new Date()).format('YYYY-MM-DD');
      if (this.info.isKill) {
        if (this.info.bookDate != date) {
          this.$toast('该秒杀商品已下架');
        } else {
          uni.navigateTo({
            url: `/pagesB/commodity/sInfo?id=${
              this.info.goodsId
            }&isSeckill=${true}`,
          });
        }
      } else {
        uni.navigateTo({
          url: `/pagesB/commodity/sInfo?id=${this.info.goodsId}`,
        });
      }
    },
    payAgain() {
      const that = this;
      if (!this.isLoading) {
        this.isLoading = true;
        ProductService.payment({ orderId: this.info.orderId }).then((res) => {
          uni.showLoading('正在支付');
          uni.requestPayment({
            provider: 'wxpay',
            timeStamp: res.timeStamp,
            nonceStr: res.nonceStr,
            package: 'prepay_id=' + res.prepayId,
            signType: res.signType,
            paySign: res.paySign,
            success(result) {
              that.isLoading = false;
              uni.hideLoading();
              uni.navigateTo({
                url: `/pagesB/order/confirmOrdeBackup?orderNo=${that.info.orderNo}&companyPhone=${that.info.supplierDetailResponseDto.companyPhone}`,
              });
            },
            fail(err) {
              console.log(err);
            },
            complete() {
              that.isLoading = false;
              uni.hideLoading();
            },
          });
        });
      }
    },
    async showModal() {
			// const chainResult = await ProductService.getOrderChain({orderNo:this.info.orderNo})
			let parmas = {
				appId:'wxa219bc5deabe9453',
				scene: this.info.orderNo,
				isHyaline: false,
				width: 300,
				autoColor: false,
				lineColor: {
					r: "0",
					g: "0",
					b: "0",
				},
				page: "pages/admin/index/index",
			};
			basicService.getMiniProgramQrcodeApi(parmas).then((res) => {
				this.codeUrl = "data:image/png;base64," + res;
				if(this.info.state === 2){
					this.createTimeLoad();
				}
			});
      // let url = `${this.JUMP_URL}?orderNo=${this.info.orderNo}&writeOffCode=${this.info.writeOffCode}`;
      // this.codeUrl = qrcode.drawImg(url, {
      //   typeNumber: 4, // 密度
      //   errorCorrectLevel: 'L', // 纠错等级
      //   size: 800, // 白色边框
      // });
    },
  },
};
</script>

<style lang="scss" scoped>
.box {
  background: #f5f5f5;
  display: flex;
  flex-direction: column;
  padding: 0 22upx;
  padding-bottom: 150upx;
}
.head,
.obligation {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20upx;
  margin-top: 28upx;
  min-height: 120upx;
  background: #ffffff;
  border-radius: 16upx;
  .headmsg {
    color: #ff4f3a;
    font-size: 36upx;
  }
}
.obligation {
  background: #ff4f3a;
  text-align: center;
}
.stocks {
  border-radius: 16upx;
  background: #fff;
  text-align: center;
  padding: 60upx 0;
  .qrcode {
    width: 240upx;
    height: 240upx;
    margin: 40upx auto 20upx auto;
    image {
      width: 240upx;
      height: 240upx;
    }
    .mark {
      top: 0;
      bottom: 0;
      width: 100%;
      background: rgba(255, 255, 255, 0.9);
    }
  }
  .refund {
    margin-top: 80upx;
    background: #999999;
    padding: 6upx 26upx;
    border-radius: 20upx;
    display: inline-block;
  }
}
.refund-countdown {
  padding: 2upx 16upx;
  border-radius: 4upx;
  background: #ff4f3a;
}
.refund-status {
  background: #ffffff;
  border-radius: 16upx;
  padding: 20upx;
  text-align: center;
  .headmsg {
    color: #ff4f3a;
    font-size: 36upx;
  }
}
.userinfo {
  background: #ffffff;
  border-radius: 16upx;
  .phone {
    margin-left: 80upx;
  }
}
.pay {
  background: #ffffff;
  border-radius: 16upx;
  .pay-mid {
    .pay-mid-left {
      image {
        width: 144upx;
        height: 200upx;
        border-radius: 12upx;
      }
    }
    .price {
      margin-top: 75upx;
    }
  }
  .pay-bottom {
    display: flex;
    margin-left: 474upx;
    align-items: center;
    .tx1 {
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 20px;
    }
    .price {
      margin-left: 14upx;
      font-size: 26px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ff4f3a;
      line-height: 37px;
    }
  }
}
.data-info,
.remark-msg,
.indent {
  height: 130upx;
  background: #ffffff;
  border-radius: 16upx;
  .label {
    width: 120upx;
  }
}
.remark-msg {
  height: auto;
}
.indent {
  height: auto;
}
.foot {
  height: 120upx;
  background: #fff;
  bottom: 0;
  left: 0;
  right: 0;
  .btns,
  .dele-btn {
    width: 230upx;
    text-align: center;
    line-height: 80upx;
    height: 80upx;
    border-radius: 50upx;
    border: 1px solid #ff4f3a;
    color: #ff4f3a;
  }
  .dele-btn {
    border: 1px solid #666;
    color: #666;
  }
  .pay-btn {
    background: linear-gradient(to right, #dd0829, #ff9b46);
    border: none;
    color: #fff;
  }
}
.pay-again {
  padding: 0 40upx;
}

.reserve-info {
  display: flex;
  flex-direction: column;
  margin-top: 24upx;
  margin-left: 0rpx;
  margin-right: 0rpx;
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
.phone-icon {
  width: 164upx;
  height: 54upx;
  position: absolute;
  right: 40upx;
}
.number-box{ 
  float: right;
  text { 
    color: #000000;
    font-size: 32upx;
  }
}
</style>
