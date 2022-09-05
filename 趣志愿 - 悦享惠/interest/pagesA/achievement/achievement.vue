<template>
  <view class="page">
    <u-navbar
      back-icon-size="48"
      :border-bottom="false"
      title="成就勋章"
      title-color="white"
      :is-back="true"
      back-icon-color="white"
      :background="background"
    >
    </u-navbar>
    <view class="content">
      <view class="c-box">
        <view class="c-img">
          <u-image
            width="352rpx"
            height="352rpx"
            :src="info.img"
          ></u-image
        ></view>
        <view class="c-name color2">{{info.badge_name}}</view>
        <view class="c-active color2 "> {{info.badge_introduce}} </view>
        <view class="c-msg"
          >{{info.access}}即可获得该成就
         </view>
      </view>

      <!-- <button class="btn">分享成就</button> -->
    </view>
  </view>
</template>

<script>
const db = wx.cloud.database();
export default {
  data() {
    return {
      background: {
        backgroundColor: "#4575F6 ",
      },
      info:{},
    };
  },
  onLoad(option) {
    this.id = option.id;
    this.getInfo(option.id)
    console.log("id", this.id);
  },
 methods:{
   getInfo(id){
    db.collection('badge').doc(id).get().then(res=>{
      this.info=res.data;
      console.log("res",this.info)
    })
  }
 }
};
</script>

<style scoped lang="scss">
.page {
  display: flex;
  flex-direction: column;
  background: #f9f9f9;
  height: 100vh;
  .content {

    background: #f9f9f9;
    padding: 32rpx 42rpx 88rpx 32rpx;
    .posi{
      top:40rpx;
      right:48rpx;
      position: absolute;
    }
    .c-box {
      display: flex;
      position: relative;
      flex-direction: column;
      background: #ffffff;
      align-items: center;
      justify-content: center;
      border-radius: 16rpx;
      .c-img {
        margin-top: 124rpx;
      }
      .c-name {
        margin-top: 64rpx;
        font-size: 64rpx;
        font-weight: bold;
      }
      .color1 {
        color: #8b5af8;
      }
      .acolor1 {
        color: #bb9cff;
      }
      .color2 {
        color: #333333;
        opacity: 0.4;
      }
      .color3 {
         color: #519CFF ;
      }
      .c-active {
        margin-top: 24rpx;
        display: flex;
        text-align: center;
    
        font-size: 32rpx;
        font-weight: bold;
      }
      .c-msg {
        margin-top: 236rpx;
        padding-bottom: 80rpx;
        width: 598rpx;
        font-size: 28rpx;
        text-align: center;
        font-weight: 500;
        color: #3f3f3f;

      }
    }
  }
  .btn {
    color: white;
    margin-top: 64rpx;
    width: 560rpx;
    height: 96rpx;
    border-radius: 56rpx;
    background: #4575f6;
  }
  button::after {
    border: none;
  }
  button::after {
    border: none;
  }
  .button-hover {
    background: #4575f6;
  }
}
</style>
