<template>
  <view class="banner">
    <image :src="src" :mode="mode" class="banner-img"></image>
    <view class="rule" @click="$emit('handlerRule')">{{  ruleText }}</view>
  </view>
</template>
<script>
export default {
  props: { 
    src: { 
      type: String,
      required: true
    },
    mode: { 
      type: String,
      default: () => { 
        return 'widthFix'
      } 
    },
    ruleText: { 
      type: String,
       default: () => { 
         return '活动规则'
       }
    }
  }
}
</script>
<style lang="scss" scoped>
.banner {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  .banner-img {
    width: 100%;
    position: relative;
  }
  .btn-apply {
    position: absolute;
    top: 368upx;
    left: 452upx;
    width: 256upx;
    height: 76upx;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 28upx;
    font-weight: 500;
    color: #000000;
  }
  .rule {
    padding: 0 27upx;
    width: 98upx;
    height: 88upx;
    background: #323e6c;
    border-radius: 16upx;
    position: absolute;
    bottom: 100upx;
    right: 0;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #ffffff;
    font-size: 22upx;
    display: flex;
    align-items: center;
  }
}
</style>