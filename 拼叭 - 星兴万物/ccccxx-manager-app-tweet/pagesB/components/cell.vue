<template>
  <view class="cell" @click="click">
    <view class="cell-title">
      <span>{{ title }}</span>
    </view>
    <view class="view-title" v-if="title === '消息设置'">公众号/服务通知消息设置</view>
    <image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/917de11a-2b57-48a4-868e-e8e4783bd850.png"></image>
  </view>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: "",
    },
  },
  methods: {
    click() {
      if (this.title === "微信权限设置") {
        this.$emit("authority");
      }else if(this.title === "消息设置"){
        this.$emit("setMessage");
      } else {
        this.$emit("agreement");
      }
    },
  },
};
</script>

<style lang="scss">
.view-title{
  color: #999999;
  font-weight: 400;
  font-size: 14px;
  margin-right: 26upx;
}
.cell {
  position: relative;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  width: 100%;
  height: 96upx;
  padding: 20upx 32upx;
  overflow: hidden;
  color: #323233;
  font-size: 32upx;
  line-height: 48upx;
  // border-bottom: 2upx solid #ccc;
  background-color: #fff;
  .cell-title {
    flex: 1;
  }
  image {
    width: 24upx;
    height: 32upx;
  }
}
</style>