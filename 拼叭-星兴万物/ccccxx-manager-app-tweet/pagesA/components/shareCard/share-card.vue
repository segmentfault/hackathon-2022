<template>
  <u-popup v-model="show" mode="bottom">
    <view class="box">
      <view class="share-txt">分享到</view>
      <view class="bottom">
        <view class="share">
          <u-icon name="weixin-fill" size="80"></u-icon>
          <view class="txt">分享好友</view>
        </view>
        <view class="share">
          <u-icon name="moments" size="80"></u-icon>
          <view class="txt">分享朋友圈</view>
        </view>
        <view class="share">
          <u-icon name="photo-fill" size="80"></u-icon>
          <view class="txt">保存图片</view>
        </view>
      </view>
    </view>
  </u-popup>
</template>

<script>
export default {
  data() {
    return {
      show: false,
    };
  },
  props: {
    ishow: {
      type: Boolean,
      default: false,
    },
    shareParams: { 
      type: Object,
      default: () => { 
        return { 
          title: '', // 分享标题
          imageUrl: '', // 分享图片
          path: '', // 分享路径
        }
      }
    }
  },
  watch: {
    ishow() {
      this.show = true;
    },
  },
};
</script>

<style lang="scss" scoped>
.box {
  display: flex;
  flex-direction: column;
  text-align: center;
  .share-txt {
    margin-top: 32upx;
    font-size: 28upx;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #666666;
    line-height: 40upx;
  }
  .bottom {
    display: flex;
    justify-content: space-around;
  }
  .share {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 66upx;
    padding-bottom: 60upx;
  }
  .txt {
    padding-top: 15rpx;
    font-size: 28upx;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #000000;
    line-height: 40upx;
    letter-spacing: 1upx;
  }
}
</style>