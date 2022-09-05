<template>
  <view class="page">
    <u-navbar
      back-icon-size="48"
      :border-bottom="false"
      title="评审服务时长"
      title-color="white"
      :is-back="true"
      back-icon-color="white"
      :background="background"
    >
    </u-navbar>
    <view class="content" v-if="userInfo.length">
      <view class="user" v-for="(item,index) in userInfo" :key="index">
        <view class="u-left">
          <u-image width="96rpx" height="96rpx"  border-radius="90" :src="item.image"></u-image>
          <view class="ms u-m-l-24">
            <view class="msg1">{{item.name}}</view>
            <view class="msg2">{{item.serviceTime?item.serviceTime:0}}小时</view>
          </view>
        </view>
        <view class="u-right">
          <view v-if="item.passTime">
             <view class="msg2">评定 {{item.serviceTime}}小时</view>
          </view>
          <view v-else class="u-right">
            <view class="bt bg1" @click="passTime(item._id,index)">通过</view>
            <view class="bt bg2" @click="changeTime(item._id,index)">修改</view>
          </view>
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
      @confirm="serverTime()"
    >
       <view class="pop-up">
        <view class="tx1">请输入服务时长(小时)</view>
          <u-input
            v-model="value"
            :type="number"
            style="width: 500rpx"
            maxlength="3"
            :height="100"
          />
      </view>
    </u-modal>
  </view>
</template>

<script>
const db = wx.cloud.database();
const _ = db.command
export default {
  data() {
    return {
      background: {
        backgroundColor: "#4575F6 ",
      },
      userInfo:{},
      acid:'',
      Tshow:false,
      value:'',
      id:'',
      index:''
    };
  },
  onLoad(option){
        console.log("sb",option)
        this.acid=option.acid;
        this.getUserInfo();
  },
  methods:{
    getUserInfo(){
      db.collection('userActivity').where({
        activityID:this.acid
      }).get().then(res=>{
        console.log("er",res);
        this.userInfo=res.data
      })
    },
    passTime(id,index){
      console.log("sb",id,index)
      db.collection('userActivity').doc(id).update({
        data:{
          passTime:true
        }
      }).then(res=>{
         console.log("通过成功",res);
         this.userInfo[index].passTime=true;
         this.$forceUpdate();
          console.log("dsdf",this.userInfo);
          const number=Number(this.value)
          db.collection('userInfo').where({
            _openid:this.userInfo[0]._openid
          }).update({
            data:{serviceTime:_.inc(number)}
          }).then(res=>{
            console.log("dfs",res)
          })
          db.collection('organization').doc(this.userInfo.orID).update({
            data:{
              data:{serviceTime:_.inc(number)}
            }
          })
      })
    },
    changeTime(id,index){
      this.id=id;
      this.index=index;
      this.Tshow=true;
    },
     checkNumber(mobile){
       return RegExp(/^[0-9]+.?[0-9]*$/).test(mobile);
    },
    serverTime(){
      console.log("dfds",this.id,this.index)
       if(!this.checkNumber(this.value)){
          uni.showToast({ title: "修改失败，请输入数字", icon: "none" });
          return ;
      }else{
          this.userInfo[this.index].serviceTime=this.value;
          uni.showToast({ title: "修改成功", icon: "none" });
      }
    }
  },
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
	.msg2 {
        font-size: 28rpx;
        font-weight: 400;
      }
    .u-right {
      display: flex;
      .bt {
        width: 88rpx;
        height: 44rpx;
        line-height: 44rpx;
        font-weight: 600;
        border-radius: 8rpx;
        font-size: 28rpx;
        padding-left: 16rpx;
        padding-right: 16rpx;
      }
      .bg1 {
        color: #ffffff;
        margin-right: 16rpx;
        background: #4575f6;
      }
      .bg2 {
        color: #4575f6;
        background: rgba(69, 117, 246, 0.1);
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
