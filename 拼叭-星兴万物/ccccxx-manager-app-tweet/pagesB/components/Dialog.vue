<!-- 弹出框 -->
<template>
  <view class="Dialog">
    <view class="pinpa-dialog">
      <view class="pinpa-dialog-title">{{ title }}</view>
      <view class="pinpa-dialog-msg">{{ text }}</view>
      <view class="pinpa-dialog-handle">
        <button class="button cancel" @click="cancelHandle">
          {{ cancelHandleMsg }}
        </button>
        <button class="button confirm" @click="handle" v-if="!bool">
          {{ handleMsg }}
        </button>
        <button class="button confirm" @click="goCoupon" v-else>
          进入"我的优惠券"
        </button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    dialog: {
      type: Boolean,
      default: () => {
        return false;
      },
    },
    text: {
      type: String,
      default: () => {
        return "";
      },
    },
    title: {
      type: String,
      default: "",
    },
    bool: {
      type: Boolean,
      default: () => {
        return false;
      },
    },
    handleMsg: {
      type: String,
      default: () => {
        return "确定";
      },
    },
    cancelHandleMsg: {
      type: String,
      default: () => {
        return "取消";
      },
    },
  },
  data() {
    return {};
  },
  methods: {
    handle() {
      this.$emit("confirm");
    },
    cancelHandle() {
      this.$emit("close", false);
    },
    goCoupon() {
      uni.redirectTo({
        url: "/pagesA/user/coupon",
      });
      this.cancelHandle();
    },
  },
};
</script>

<style lang='scss'>
.Dialog {
  position: fixed;
  top: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.7);
  width: 100%;
  height: 100%;
  z-index: 9999999;
  .pinpa-dialog {
    background-color: #fff;
    border: 1px solid #fff;
    border-radius: 28upx;
    position: fixed;
    top: 45%;
    right: 80upx;
    left: 80upx;
    overflow: hidden;
    &-title {
      margin-top: 30upx;
      text-align: center;
      font-weight: 700;
      font-size: 40upx;
    }
    &-msg {
      text-align: center;
      padding: 48upx 48upx 80upx 48upx;
      font-size: 32upx;
      color: #333;
    }
    &-handle {
      text-align: center;
      padding-bottom: 50upx;
      .button {
        display: inline-block;
        padding: 0 40upx;
        line-height: 64upx;
        border: 2upx solid #d3d3d3;
        border-radius: 36upx;
        outline: none;
        font-weight: 600;
        font-size: 30upx;
      }
      .cancel {
        background: #fff;
        color: #000;
        margin-right: 48upx;
      }
      .confirm {
        background: #333;
        color: #fff;
      }
    }
  }
}
</style>