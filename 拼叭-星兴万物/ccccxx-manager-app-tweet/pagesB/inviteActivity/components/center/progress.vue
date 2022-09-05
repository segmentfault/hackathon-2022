<template>
  <view class="tips">
    <view class="tips-limit" >限量<text>{{ activeDetail.limitPeopleNum }}人</text>
    <text style="font-size: 12px; margin-left: 20px">已抢{{ activeDetail.progressRate || 0 }}%</text></view>
    <view class="tips-jd">
      <view class="tips-jdt" :style="{ width: `${activeDetail.progressRate || 0}%` }">
        <image
          class="tips-jdt-fire"
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/fire.png"
        ></image>
      </view>
    </view>
  </view>
</template>
<script>
export default {
  props: { 
    activeDetail: { 
      type: Object,
      default: () => { 
        return { 
          limitPeopleNum: 500,
          progressRate: 75,
        }
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.tips {
  text-align: center;
  font-size: 24px;
  font-family: PingFangSC-Semibold, PingFang SC;
  font-weight: 600;
  color: #a06426;
  margin-top: 29px;
  margin: 0 auto;
  &-msg {
    margin-top: 35px;
    text {
      color: #ee5151;
    }
  }
  &-limit {
    font-size: 20px;
    font-weight: 500;
    margin-top: 7upx;
    color: #333333;
    display: flex;
    justify-content: center;
    align-items: center;
    text {
      color: #ee5151;
    }
  }
  &-jd {
    width: 152px;
    height: 11px;
    border-radius: 11px;
    background: #e8e8e8;
    margin: 4px auto;
  }
  &-jdt {
    width: 107px;
    height: 11px;
    border-radius: 11px;
    background: linear-gradient(270deg, #cd6cff 0%, #fe4a2a 100%);
    position: relative;
    &-fire {
      width: 32px;
      height: 32px;
      position: absolute;
      top: -12px;
      right: -16px;
    }
  }
}
</style>