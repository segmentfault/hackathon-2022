<template>
  <view>
    <u-navbar
      :is-back="true"
      title="志愿任务"
      back-icon-color="white"
      title-color="white"
      :background="background"
      :border-bottom="false"
    >
    </u-navbar>
    <view class="content">
      <view class="img1 im-tx" @click="showStates(3)">
        <view class="u-m-b-8">千恩万谢</view>
        <u-image
          width="134rpx"
          height="134rpx"
          src="https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/cloudbase-cms/upload/2022-08-25/g3g1kna1a5ti0hczxug70ecje2cechnx_.png"
        ></u-image>
      </view>
      <view class="img2 im-tx" @click="showStates(2)">
        <view class="u-m-b-8">宣传大使</view>
        <u-image
          width="134rpx"
          height="134rpx"
          src="https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/cloudbase-cms/upload/2022-08-09/j6ioxwu63lwwdfqz5lj1lixp38idgiym_.png"
        ></u-image>
      </view>
      <view class="img3 im-tx" @click="showStates(1)">
        <view class="u-m-b-8">小试牛刀</view>
        <u-image
          width="134rpx"
          height="134rpx"
          src="https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/cloudbase-cms/upload/2022-08-09/x8whk3fpia5gdbu16ogjlmjkwslffcao_.png"
        ></u-image>
      </view>
      <view class="img4 im-tx" @tap="showStates(0)">
        <view class="u-m-b-8">初出茅庐</view>
        <u-image
          width="134rpx"
          height="134rpx"
          src="https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/cloudbase-cms/upload/2022-08-09/5wbw2ua37n5i3lv4dphsu35b1kmom472_.png"
        ></u-image>
      </view>
    </view>
    <view class="shadow" v-if="showState" @click="showState=false">
      <view class="shadow-body p30">
        <view class="fs36 fw600 text-center">{{requirements[index].name}}</view>
        <view class="fs28 color-222 mt30">
          {{requirements[index].introduce}}
        </view>
       <view class="fs28 fw600 color-222 mt30">
         任务要求:{{requirements[index].require}}
        </view>
		 <view class="fs28 color-222 mt30">
		 <view class="fw600">任务奖励</view>
		  <view class="fw400">
			1.{{requirements[index].reward1}}<br/>
		  2.{{requirements[index].reward2}}
		  </view>
        </view>
        <view
          class="fs38 fw600 text-center close"
          style="background: #4575f6"
          @tap="goMap()"
          >选择志愿活动</view
        >
      </view>
    </view>
    <u-modal v-model="show" :content="content"></u-modal>
  </view>
</template>

<script>
export default {
  data() {
    return {
      background: {
        backgroundColor: "#4575F6 ",
      },
      showState: false,
      show:false,
      index:0,
      content:'暂未开放，尽请期待',
      requirements:[
        {
        name:'初出茅庐',
        introduce:'您现在刚刚开始您的公益之旅，第一步就是赶紧尝试一个任务开始吧',
        require:'参加一次活动',
        reward1:'影响力增加3330点',
        reward2:'解锁成就“初出茅庐”'
      },
       {
        name:'小试牛刀',
        introduce:'您已经不是一个新手志愿者了',
        require:'至少参与5次公益活动吧！',
        reward1:'影响力增加8730点',
        reward2:'解锁成就“小试牛刀””'
      },
       {
        name:'宣传大使',
        introduce:'宣传活动，你的影响力也许就能帮助很多人！',
        require:'请至少在单次公益活动中获得影响力榜首名次',
        reward1:'影响力增加9230点',
        reward2:'解锁成就“宣传大使”'
      },
       {
        name:'千恩万谢',
        introduce:'被帮助对象对你的认可，就是对公益服务的价值肯定',
        require:'获得100封感谢信',
        reward1:'影响力增加31330点',
        reward2:'解锁成就“千恩万谢”'
      },
      ]
    };
  },
  methods:{
	  showStates(data) {
      this.showState = true;
      this.index=data;
    },
		goMap(){
			uni.switchTab({
				url:"/pages/tabbar/tabbar-4/tabbar-4"
			})
		}
  }
};
</script>

<style scoped lang="scss">
.content {
  position: absolute;
  width: 750rpx;
  height: 1448rpx;
  background: url(https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/Slice%20105%402x.png?sign=98351b1f90bab2a4b36f6b5bf7253b86&t=1655265488)
    no-repeat center;
  background-size: 100% 100%;
}
.im-tx {
  position: relative;
  width: 150rpx;
  font-size: 28rpx;
  font-weight: 600;
  color: #ffffff;
  text-align: center;
}
.img1 {
  left: 170rpx;
  top: 30rpx;
}
.img2 {
  left: 502rpx;
  margin-top: 32rpx;
}
.img3 {
  left: 120rpx;
  margin-top: 138rpx;
}
.img4 {
  left: 502rpx;
  margin-top: 84rpx;
}
.shadow-body{
  z-index: 1200;
}
</style>
