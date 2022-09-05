<template>
  <view>
    <u-mask :show="show">
      <view class="warp">
        <view class="warp-bg">
          <slot></slot>
		  <image
		    class="close-icon"
		    @click="change"
		    src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/close.png"
		    mode=""
		  ></image>
        </view>
        
      </view>
    </u-mask>
  </view>
</template>

<script>
export default {
  data() {
    return {
      show: false,
    };
  },
  methods: {
    change() {
      this.show = !this.show;
    },
  },
};
</script>

<style scoped lang="scss">
.warp {
  position: relative;
  height: 100vh;
  width: 100%;
  
  .warp-bg {
    position: relative;
    margin: 150px 20upx 0px 20upx;
    width: calc(100% - 40upx);
    border-radius: 16upx;

  }
  .close-icon {
    height: 50upx;
    width: 50upx;
    position: absolute;
	top:-80upx;
	right: 0;
  }
}
</style>
