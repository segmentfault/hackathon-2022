<template>
  <view class="box">
    <view class="head fixed">
      <text :class="{ active: navId == '' }" @tap="toggleNav('')">全部</text>
      <text :class="{ active: navId == 1 }" @tap="toggleNav(1)">未付款</text>
      <text :class="{ active: navId == 2 }" @tap="toggleNav(2)">待使用</text>
      <text :class="{ active: navId == 3 }" @tap="toggleNav(3)">已完成</text>
      <text :class="{ active: navId == 4 }" @tap="toggleNav(4)">已退款</text>
    </view>

    <view class="list">
      <view
        class="pay p30"
        v-for="item in list"
        :key="item.orderNo"
        @click="goOrder(item.orderNo)"
      >
        <view class="pay-top">
          <view
            class="flex flex-middle flex-left trade-name"
            @click.stop="goDetail(item.supplierDetailResponseDto)"
          >
            <view class="oneOmit">{{
              item.supplierDetailResponseDto.companyName
            }}</view>
            <u-icon name="arrow-right" style="margin-left: 16upx"></u-icon>
          </view>
          <view class="state-pay">{{ showState(item.state) }}</view>
        </view>
        <view class="pay-bottom">
          <view class="pay-bottom-left">
            <image :src="item.goodsPic"></image>
          </view>
          <view class="pay-bottom-mid flex-1">
            <view class="num multiOmit">{{ item.goodsName }}</view>
            <view class="discount">预约日期：{{ item.bookDate }}</view>
            <view class="price1">
              {{ item.state === 2 || item.state === 4 ? '实付款' : '应付款'
              }}<view class="price2">￥{{ item.actualAmount }}</view></view
            >
          </view>
          <view class="pay-bottom-right" v-if="item.state == 2">
            <view class="qrcode">
              <image
                src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/57ba2c0b-42f9-417d-a9c4-e7e221b78bf0.png"
              ></image>
            </view>
            <view class="pay-btn2" @tap.stop="showModal(item)">查看核销码</view>
          </view>
          <view class="pay-bottom-right" v-if="item.state == 1">
            <view class="pay-btn1">去付款</view>
          </view>
          <view class="pay-bottom-right" v-if="item.state == 3">
            <view class="qrcode">
              <image
                src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/57ba2c0b-42f9-417d-a9c4-e7e221b78bf0.png"
              ></image>
            </view>
            <view class="pay-btn3">已完成</view>
          </view>
          <view class="pay-bottom-right" v-if="item.state == 4">
            <view class="qrcode"> </view>
            <view class="refund"
              >已退款<view class="price">￥{{ item.actualAmount }}</view></view
            >
          </view>
        </view>
      </view>
    </view>

    <u-popup v-model="show" mode="center" border-radius="14" closeable>
      <view class="modal" v-if="codeUrl">
        <view class="modal-tips">请将二维码出示给商家核销备份</view>
        <image class="qr-image" :src="codeUrl" mode="aspectFit"></image>
        <view class="fs26 color-666 mt20">订单ID：{{ orderNo }}</view>
      </view>
    </u-popup>
  </view>
</template>
<script>
import ProductService from '../../service/product';
import basicService from '../../service/basic';
import qrcode from '../utils/qrcode';

export default {
  data() {
    return {
      navId: '',
      pageNum: 1,
      pageSize: 10,
      list: [],
      total: 0,
      isLoading: false,
      codeUrl: '',
      show: false,
      JUMP_URL: 'https://pinbar.vip/h5/supplier/home/',
      orderNo: '',
			interId:null
    };
  },
  onReachBottom() {
    if (this.list.length < this.total) {
      this.pageNum++;
      this.init();
    }
  },
  onShow() {
    this.list = [];
		this.pageNum = 1;
    this.init();
  },
	onHide() {
		if(this.interId){
			clearInterval(this.interId)
		}
	},
	onUnload() {
		if(this.interId){
			clearInterval(this.interId)
		}
	},
  methods: {
		createTimeLoad(usedCode) {
		  let that = this;
		  this.interId = setInterval(() => {
		    return ProductService.getOrderWirteStatus({orderNo:this.orderNo}).then((res) => {
		      if (res === true) {
		        that.$toast("核销成功", "success");
		        clearInterval(that.interId);
		        that.show = false;
						this.list = [];
						that.init();
		      }
		    });
		  }, 2000);
		},
    init() {
      uni.showLoading({ mask: true });
      ProductService.orderList({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        state: this.navId,
      })
        .then((res) => {
          uni.hideLoading();
          this.list = this.list.concat(res.list);
          this.total = res.total;
        })
        .catch((err) => uni.hideLoading());
    },
    goOrder(orderNo) {
			if(this.interId){
				clearInterval(this.interId)
			}
      uni.navigateTo({
        url: `/pagesB/order/orderDetail?orderNo=${orderNo}`,
      });
    },
    goDetail(item) {
      uni.navigateTo({
        url: `/pagesB/commodity/index?typeId=${item.typeId}&supplierId=${item.supplierId}`,
      });
    },
    toggleNav(id) {
			if(this.interId){
				clearInterval(this.interId)
			}
      this.navId = id;
      this.pageNum = 1;
      this.list = [];
      this.init();
    },
    showState(val) {
      return val == 1
        ? '未支付'
        : val == 2
        ? '待使用'
        : val == 3
        ? '已核销'
        : val == 4
        ? '已退款'
        : val == 5
        ? '已过期'
        : val == 6
        ? '已取消'
        : '退款中';
    },
    async showModal(data) {
      this.orderNo = data.orderNo;
			// const chainResult = await ProductService.getOrderChain({orderNo:this.orderNo})
			let parmas = {
				appId:'wxa219bc5deabe9453',
				scene: data.orderNo,
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
				this.show = true;
				this.createTimeLoad();
			});
      // let url = `${this.JUMP_URL}?orderNo=${data.orderNo}&writeOffCode=${data.writeOffCode}`;
      // this.codeUrl = qrcode.drawImg(url, {
      //   typeNumber: 4, // 密度
      //   errorCorrectLevel: 'L', // 纠错等级
      //   size: 800, // 白色边框
      // });
      // this.show = true;
    },
  },
};
</script>
<style>
page {
  background: #f5f5f5;
}
</style>
<style lang="scss" scoped>
.box {
  display: flex;
  flex-direction: column;
  background: #f5f5f5;

  .head {
    display: flex;
    padding-top: 24upx;
    height: 102upx;
    background: #ffffff;
    border: 2upx solid #ffffff;
    justify-content: space-around;
    top: 0;
    left: 0;
    right: 0;
    box-shadow: 0 0 12upx 0 #ccc;
    text {
      width: 100upx;
      text-align: center;
      font-size: 28upx;
      font-weight: 400;
      color: #666666;
      border-bottom: 4upx solid transparent;
    }
    .active {
      font-weight: 600 !important;
      color: #000000 !important;
      border-bottom: 4upx solid #000000;
    }
  }
  .list {
    margin-top: 102upx;
    padding-bottom: 20upx;
  }
  .pay {
    display: flex;
    margin-top: 24upx;
    flex-direction: column;
    width: calc(100% - 40upx);
    margin-left: 20upx;
    background: #ffffff;
    border-radius: 8px;
    .pay-top {
      display: flex;
      position: relative;
      .trade-name {
        width: 400upx;
      }
      .state-pay {
        position: absolute;
        right: 20upx;
        height: 40upx;
        width: 114upx;
        background: linear-gradient(90deg, #fbedec 0%, #fff5f5 100%);
        border-radius: 20upx;
        text-align: center;
        font-size: 22upx;
        color: #e92f29;
        line-height: 40upx;
      }
    }
    .pay-bottom {
      display: flex;
      position: relative;
      margin-top: 44upx;
      .pay-bottom-left {
        image {
          width: 144upx;
          height: 200upx;
          border-radius: 12upx;
        }
      }
      .pay-bottom-mid {
        display: flex;
        flex-direction: column;
        padding-left: 20upx;
        .num {
          height: 80upx;
          font-size: 32upx;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #000000;
        }
        .discount {
          margin-top: 12upx;
          font-size: 24upx;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #333333;
          line-height: 34upx;
        }
        .price1 {
          display: flex;
          align-items: center;
          margin-top: 45upx;
          font-size: 28upx;
          color: #333333;
        }
        .price2 {
          margin-left: 20upx;
          font-size: 32upx;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #ff4f3a;
          line-height: 28upx;
        }
      }
      .pay-bottom-right {
        display: flex;
        text-align: center;
        flex-direction: column;
        align-items: center;
        .qrcode {
          position: absolute;
          top: 95rpx;
          right: 98upx;
          width: 50upx;
          height: 50upx;
          image {
            width: 50upx;
            height: 50upx;
          }
        }
        .pay-btn1 {
          position: absolute;
          bottom: 2upx;
          right: 72upx;
          width: 132upx;
          height: 48upx;
          background: #ff4f3a;
          border-radius: 60upx;
          font-size: 26upx;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #ffffff;
          line-height: 48upx;
        }
        .pay-btn2 {
          position: absolute;
          right: 38upx;
          bottom: 2upx;
          width: 186upx;
          height: 48upx;
          border-radius: 60upx;
          border: 2upx solid #ff4f3a;
          font-size: 26upx;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #ff4f3a;
          line-height: 48upx;
        }
        .pay-btn3 {
          position: absolute;
          right: 54upx;
          bottom: 2upx;
          width: 150upx;
          height: 48upx;
          background: #dbdbdb;
          border-radius: 60upx;
          font-size: 26upx;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #ffffff;
          line-height: 48upx;
        }
        .refund {
          display: flex;
          position: absolute;
          right: 54upx;
          bottom: 0;
          align-items: center;
          font-size: 28upx;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 400;
          color: #999999;
        }
        .price {
          margin-left: 20upx;
          font-size: 32upx;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #ff4f3a;
        }
      }
    }
  }
}
.modal {
  width: 640upx;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #ffffff;
  text-align: center;
  padding-bottom: 50px;
  &-tips {
    margin-top: 100upx;
    margin-bottom: 90upx;
    font-size: 40upx;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #999999;
  }
  .qr-image {
    width: 328upx;
    height: 328upx;
  }
}
</style>
