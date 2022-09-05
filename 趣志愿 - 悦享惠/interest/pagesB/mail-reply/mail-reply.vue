<template>
  <view class="page">
    <u-navbar
      back-icon-size="48"
      :border-bottom="false"
      title="信箱回信"
      title-color="white"
      :is-back="true"
      back-icon-color="white"
      :background="background"
    >
    </u-navbar>
    <view class="content" v-if="userInfo.length">
      <view class="user" v-for="(item,index) in userInfo" :key="index">
        <view class="u-left">
          <u-image width="96rpx" border-radius="90" height="96rpx" :src="item.image"></u-image>
          <view class="ms u-m-l-24">
            <view class="msg1">{{item.name}}</view>
          </view>
        </view>
        <view class="u-right">
          <view class="bt" @click="Mail(item._id)">信箱回信</view>
        </view>
      </view>
    </view>
    <view class="none u-flex" v-else>
      <image class="img" src="cloud://qzycloud-1grvormi21122cee.717a-qzycloud-1grvormi21122cee-1312385448/空白页.png"></image>
    </view>
    <u-modal
       v-model="Tshow"
      :show-confirm-button="true"
      :show-cancel-button="true"
      :negative-top="350"
      :show-title="false"
      @confirm="setMail()"
    >
       <view class="pop-up">
        <view class="tx1">请输入信箱内容</view>
        <view class="u-m-l-20">
           <textarea
            class="inputText u-m-t-24"
            cols="10"
            rows="7"
            v-model="value"
            placeholder="请输入文字"
          ></textarea>
        </view>
      </view>
    </u-modal>
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
      acID:"",
      userInfo:{},
      Tshow:false,
      id:'',
      value:''
    };
  },
  onLoad(option){
    console.log("sdfsdfsadfasdf",option)
    this.acID=option.acID;
    this.getUser();
  },
  methods:{
    //获取用户列表
    getUser(){
      db.collection('userActivity').where({
        activityID:this.acID
      }).get().then(res=>{
        console.log("er",res);
        this.userInfo=res.data
      })
    },
    Mail(id){
      this.id=id;
      this.Tshow=true;
    },
    setMail(){
      db.collection('userActivity').doc(this.id).update({
         data:{
          mail:this.value
         }
      }).then(res=>{
        uni.showToast({title:'回信成功',icon:'none'})
      })
      console.log(this.id)
    }
  }
};
</script>

<style scoped lang="scss">
.page {
  display: flex;
  flex-direction: column;
  min-height:100vh;
  .user {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 32rpx 32rpx 36rpx 32rpx;
    border-bottom: rgba(51, 51, 51, 0.1);
    .u-left {
      display: flex;
      align-items: center;
      .msg1 {
        font-size: 32rpx;
        font-weight: 600;
        opacity: 0.8;
      }
    }
    .u-right {
      display: flex;
      .bt {
        color: #ffffff;
		text-align: center;
        margin-right: 16rpx;
        background: #4575f6;
        width: 160rpx;
        height: 44rpx;
        line-height: 44rpx;
        font-weight: 600;
        border-radius: 8rpx;
        font-size: 28rpx;
        padding-left: 16rpx;
        padding-right: 16rpx;
      }
    }
  }
  .none{
    height:80vh;
    flex-direction: column;
    align-items: center;
    justify-content: center;
     .img{
      width:750rpx;
      height:600rpx;
     }
  }
   .pop-up {
    display: flex;
    flex-direction: column;
    align-items: center;
   
    background: #ffffff;
    border-radius: 16px;
    .tx1 {
      margin-top: 90upx;
      font-size: 48upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #000000;
      line-height: 66upx;
    }
  }

}
</style>
