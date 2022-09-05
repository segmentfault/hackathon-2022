<template>
  <view class="page-ctn">
    <view class="tab">
      <view
        class="label"
        v-for="(item, i) in tabs"
        :key="i"
        @click="changeTab(item.status)"
        ><text :class="{ checked: index === item.status }">{{
          item.label
        }}</text></view
      >
    </view>
    <template>
      <view class="list-item" v-for="(item, i) in list" :key="i">
        <image
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/user/coupon-bg.png"
        ></image>
        <view class="info-bg">
          <view class="list-item-left">
            <view v-if="item.discount">
              <text> {{ item.discount }}</text>
              <text class="t1" v-if="item.type === 1">折</text>
              <text class="t1" v-else>元</text>
            </view>
						<view class="fs36" v-else>免单</view>
            <text @click="look(item)">使用规则</text>
          </view>

          <view class="list-item-right">
            <!-- 优惠劵状态 0已核销 1可使用 2取消报名(核销)-->
            <view class="flex-row">
              <view class="shop-coupon">{{ item.name }}</view>
              <view
                class="go-use"
                :class="[item.status != 1 && 'has-used']"
                @click="showModal(item)"
              >
                {{ $getEnumsLabel("couponState", item.status) }}
              </view>
            </view>
            <view class="flex-row shop-tips">
              {{
                item.couponTemplate
                  ? `有效期${new $utils.dayjs(item.validdate).format(
                      "YYYY-MM-DD HH:mm"
                    )}至${new $utils.dayjs(item.invaliddate).format(
                      "YYYY-MM-DD HH:mm"
                    )}`
                  : `仅限${new $utils.dayjs(item.invaliddate).format(
                      "YYYY-MM-DD"
                    )}当天`
              }}
            </view>
          </view>
        </view>
      </view>
    </template>

    <template v-if="list.length < 1">
      <view class="none-t">暂无优惠券</view>
      <view class="none-tip"> 可多关注拼叭公众号获取更多优惠券活动信息 </view>
    </template>

    <u-popup
      v-model="show"
      mode="center"
      @close="getUserCouponList(0)"
      border-radius="14"
      closeable
    >
      <view class="modal" v-if="codeUrl">
        <view class="modal-tips">请将二维码出示给商家核销备份</view>
        <image class="qr-image" :src="codeUrl" mode="aspectFit"></image>
      </view>
    </u-popup>

    <view class="dialog" v-if="dialogSate">
      <view class="card" v-if="lookItem.couponTemplate.couponRule">
        <view>
          <view class="title"> 优惠券使用规则 </view>
          <view
            ><text>{{ lookItem.couponTemplate.couponRule }}</text></view
          >
        </view>

        <view class="btn">
          <!-- 暂时隐藏 -->
          <image
            @click="dialogSate = !dialogSate"
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/type/ok.png"
          ></image>
          <image
            @click="toMerchant"
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/type/look.png"
          ></image>
        </view>
      </view>
      <view class="card" v-else>
        <view>
          <view class="title"> 优惠券使用规则 </view>
          <text
            >1、本券可以在商家所有盒装本享受原价基础上抵扣<text>{{
              lookItem.discount
            }}</text
            >{{ lookItem.type === 1 ? "折" : "元" }}<text></text>。</text
          ></view
        >
        <view
          ><text
            >2、使用流程：到商家店后->点击优惠券打开核销码->商家扫码核销->完成用券。</text
          ></view
        >
        <view><text>3、本券可用于拼叭平台本次活动的所有合作商家</text></view>
        <view class="btn">
          <image
            @click="dialogSate = !dialogSate"
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/type/ok.png"
          ></image>
          <image
            @click="toMerchant"
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/type/look.png"
          ></image>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import userService from "../../service/user.js";
import basicService from "../../service/basic.js";
import qrcode from "../utils/qrcode.js";
// let qrcode = require('../../frameword/utils/qrcode.js')
let state = null; //定时器对象
import moment from "moment";
export default {
  data() {
    return {
      tabs: [
        {
          label: "可使用",
          status: "1",
        },
        {
          label: "已核销",
          status: "0",
        },
        {
          label: "已失效",
          status: "2,3",
        },
      ],
      index: 1,
      show: false,
      JUMP_URL: "https://pinbar.vip/h5/supplier/home/?consumeCode=",
      codeUrl: "",
      list: [],
      dialogSate: false,
      queryState: null,
      lookItem: {},
      pageNum: 1,
			interId:null
    };
  },
  // mounted() {

  // },
  created() {
    this.changeTab(this.tabs[0].status);
  },
  onReachBottom() {
    this.pageNum++;
    this.getUserCouponList();
  },
  onLoad(o) {
    //埋点调用
    let res = wx.getLaunchOptionsSync();
    // 埋点
    if (res.scene === 1035) {
      this.$eventRecord(87);
    } else if (res.scene === 1065) {
      this.$eventRecord(90);
    } else {
      this.$eventRecord(67);
    }
  },
	onUnload() {
		if(this.interId){
			clearInterval(this.interId)
		}
	},
  methods: {
    changeTab(val) {
      this.list = [];
      this.index = val;
      this.pageNum = 1;
      this.getUserCouponList();
    },
    look(val) {
      this.dialogSate = true;
      this.lookItem = val;
    },
    toMerchant() {
      let item = [];
      let { discount, supplierType, discountRange, id } =
        this.lookItem.couponTemplate;

      if (discountRange === 2) {
        this.lookItem.branchs.filter((v) => item.push(v.branchId));
        item = JSON.stringify(item);
      }

      uni.navigateTo({
        url: `/pagesA/vouchers/supportList?activityBool=false&activityId=8&money=${discount}&item=${item}&supplierType=${supplierType}`,
      });
    },
    createTimeLoad(usedCode) {
      let that = this;
      this.interId = setInterval(() => {
        return userService.queryCouponState(usedCode).then((res) => {
          //1未使用 2已使用
          if (res == 2) {
            this.$toast("核销成功", "success");
            clearInterval(that.interId);
            that.show = false;
          }
        });
      }, 2000);
    },
    showModal(data) {
      if (data.status != 1) {
        return;
      }
      let now = Date.parse(new Date());
      if (data.validdate >= now && now <= data.invaliddate) {
        return this.$toast("当前优惠券未到使用时间");
      }
      //当前时间是否在使用期限内 （后端约定此规则） validdate开始时间 invaliddate结束时间
      if (data.validdate <= now && now <= data.invaliddate) {
        let url = this.JUMP_URL + data.id;
        this.codeUrl = qrcode.drawImg(url, {
          typeNumber: 4, // 密度
          errorCorrectLevel: "L", // 纠错等级
          size: 800, // 白色边框
        });
        this.show = true;
        this.createTimeLoad(data.usedCode);
      } else {
        this.$toast(
          `该优惠券已过期，有效期${new this.$utils.dayjs(
            data.couponTemplate.validdate
          ).format("YYYY-MM-DD")}至${new this.$utils.dayjs(
            data.couponTemplate.invaliddate
          ).format("YYYY-MM-DD")}`
        );
      }
    },
    getUserCouponList(i) {
      if (i === 0) {
        this.list = [];
				clearInterval(this.interId)
      }
      let parmas = {
        pageSize: 10,
        pageNum: this.pageNum,
        statuses: this.index,
      };
      basicService.getUserCouponListApi(parmas).then((res) => {
        this.list.push(...res.list);
      });
    },
    myCouponList() {
      // clearInterval(state);
      return userService.myCouponList().then((res) => {
        this.list = res || [];
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.page-ctn {
  padding: 26upx 32upx;
  // width: calc(100% - 64upx);
  position: relative;
  .dialog {
    letter-spacing: 4upx;
    z-index: 10;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.5);
    padding: 0 20upx;
    .card {
      width: 90%;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      background-color: #fff;
      border-radius: 30upx;
      padding: 40upx;
      .img {
        width: 60upx;
        height: 60upx;
        position: absolute;
        top: -25upx;
        right: -25upx;
        filter: brightness(0.5);
      }
      .title {
        text-align: center;
        font-weight: 700;
        font-size: 40upx;
        margin-bottom: 30upx;
      }
      view {
        &:nth-child(2) {
          margin: 60upx 0;
        }
        text {
          font-size: 30upx;
        }
      }
      .btn {
        display: flex;
        margin-top: 50upx;
        image {
          height: 100upx;
          &:first-child {
            flex: 60%;
            margin-right: 30upx;
          }
        }
      }
    }
  }
}
.tab {
  display: flex;
  align-items: center;
  text-align: center;
  margin-bottom: 10upx;
  .label {
    flex: 1;
    .checked {
      display: inline-block;
      border-bottom: 6upx solid #000;
      padding-block: 10upx;
    }
  }
}
.none-t {
  margin-top: 274upx;
  text-align: center;
  font-size: 32upx;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #999999;

  text-align: center;
}
.none-tip {
  margin: 100upx 0upx;
  background: #f5f2f1;
  border-radius: 16upx;
  font-size: 32upx;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #a06426;
  padding: 26upx 40upx;
  text-align: center;
}
.list {
  padding: 16px;
  &-item {
    display: flex;
    width: 100%;
    height: 180upx;
    position: relative;
    image {
      position: absolute;
      left: 0;
      right: 0;
      top: 0;
      bottom: 0;
      width: 100%;
      height: 100%;
    }
    .info-bg {
      display: flex;
      position: relative;
      z-index: 10;
      width: 100%;
    }
    &-left {
      flex: 3;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      flex-wrap: wrap;
      // 48,但是设计师没考虑到小数
      font-size: 76upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ff4f3a;
      line-height: 44upx;
      letter-spacing: 1px;
      .t1 {
        font-size: 32upx;
        position: relative;
        top: 10upx;
      }
      text {
        &:first-child {
          font-size: 76upx;
          margin-top: 10upx;
        }
        &:last-child {
          font-size: 25upx;
          color: #027db4;
          margin-top: 10upx;
        }
      }
    }
    &-right {
      padding-left: 10upx;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      flex: 7;
      .shop-coupon {
        font-size: 32upx;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 600;
        color: #333333;
      }
      .shop-tips {
        margin-top: 16upx;
        font-size: 24upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        padding-right: 110upx;
      }
      .go-use {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-right: 44upx;
        width: 150upx;
        height: 50upx;
        background: #ff4f3a;
        border-radius: 26upx;
        font-size: 28upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #ffffff;
      }
      .has-used {
        background: #ffb0a6;
      }
    }
    .flex-row {
      width: 100%;
      display: flex;
      justify-content: space-between;
    }
  }
}
.list-item + .list-item {
  margin-top: 20upx;
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
