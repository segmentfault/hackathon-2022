<template>
  <view>
    <u-navbar
      :is-back="true"
      title="志愿榜单"
      back-icon-color="white"
      title-color="white"
      :background="background"
      :border-bottom="false"
    >
    </u-navbar>
    <view class="content">
        <view class="List">
          <view class="userMsg u-m-b-32" v-for="(item,index) in userList" :key="index" >
            <view class="u-flex">
			      	<u-image v-show="index<3" width="36rpx" height="36rpx" :src="rankImg[index]" ></u-image>
              <text v-show="index>=3" class="u-right" style="width:36rpx">{{index+1}}</text>
			      	<u-image class="u-m-l-16" width="48rpx" shape="circle" height="48rpx" :src="item.user_img" ></u-image>
			      	<text class="u-name u-m-l-16">{{item.user_name}}</text>
			       </view>
            <view class="u-right">影响力{{item.serviceTime}}</view>
          </view>
        </view>
     
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
      navId: 1,
      rankImg:['/static/img/my/1.png','/static/img/my/2.png','/static/img/my/3.png'],
      userList:{},
    };
  },
  onShow(){
    this.getuserList();
  },
  methods: {
    toggleNav(id) {
      this.navId = id;
    },
    getuserList(){
      db.collection('userInfo').orderBy("serviceTime","desc").get().then(res=>{
        console.log("结果",res)
        this.userList=res.data
      })
    },
  },
};
</script>

<style scoped lang="scss">
.content {
 
  .List {
    padding: 54rpx 40rpx 0 40rpx;
    .userMsg {
      display: flex;
      justify-content: space-between;
      align-items: center;
	  .u-name{
		font-size: 28rpx;
		font-weight: 600;
	  }
      .u-right {
        font-size: 24rpx;
        font-weight: 600;
        opacity: 0.4;
      }
    }
  }
}
</style>
