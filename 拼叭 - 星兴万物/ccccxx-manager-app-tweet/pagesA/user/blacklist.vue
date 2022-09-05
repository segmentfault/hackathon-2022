<template>
  <view class="blacklist">
      <view>您已被禁用</view>
  </view>
</template>

<script>
export default {

}
</script>

<style>
.blacklist{
  position: relative;
  width: 100%;
  height: 100vh;
  background-color: #999;
}
.blacklist view{
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%,-50%);
  letter-spacing: 20upx;
  color: #fff;
  font-size: 40upx;
}
</style>