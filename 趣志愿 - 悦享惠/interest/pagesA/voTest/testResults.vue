<template>
  <view class="page">
    <comHead>志愿测试</comHead>
    <view class="box u-flex">
      <view class="fs36 colorW fw600">您属于</view>
      <button class="btn2 fs28 u-m-t-16">硬核实力志愿者</button>
      <view class="user po">
        <u-image
           border-radius="90"
          :src="userInfo.user_img"
          width="108rpx"
          height="108rpx"
        ></u-image>
        <view class="life box1 po">
          <view class="fs28 colorW fw600">人生观</view>
          <view class="fs20 fw400 colorW">{{choose1[answer[0]].name}}</view>
        </view>
        <view class="communication box1 po">
          <view class="fs28 colorW fw600">沟通</view>
          <view class="fs20 fw400 colorW">{{choose2[answer[1]].name}}</view>
        </view>
        <view class="character box1 po">
          <view class="fs28 colorW fw600">性格</view>
          <view class="fs20 fw400 colorW">{{choose3[answer[2]].name}}</view>
        </view>
      </view>
      <view class="field" style="marign-top:88rpx">
        <view class="message colorW fw600 fs32">服务领域：</view>
        <view class="massage2 colorW fs32  fw600 u-m-l-10 u-flex select ">
          <view class="message3" v-for="(item,index) in answer4" :key="index">{{choose4[item].name}}{{index+1==answer4.length?'':'、'}}</view>
        </view>
      </view>
      <view class="field u-m-t-8">
        <view class="message colorW fw600 fs32">擅长技能：</view>
        <view class="massage2 colorW fs32  fw600 u-m-l-10 u-flex select ">
             <view class="message3" v-for="(item,index) in answer5" :key="index">{{choose5[item].name}}{{index+1==answer5.length?'':'、'}}</view>
        </view>
      </view>
    </view>
    <button class="btn" @click="end()">开启公益之旅</button>
  </view>
</template>

<script>
import comHead from "@/components/com-head.vue";
const db = wx.cloud.database();
export default {
  components: {
    comHead,
  },
  data() {
    return {
      background: {
        backgroundColor: "#7d9fed ",
      },
      choose1:[{
        name:'喜好探索'
      },
      {
        name:'目标坚定'
      },
      {
        name:'自我实现'
      },
      {
        name:'安于现状'
      },
      ],
       choose2:[{
        name:'好玩有趣'
      },
      {
        name:'安全稳妥'
      },
      {
        name:'乐于挑战'
      },
      {
        name:'希望方便'
      },
      ],
       choose3:[{
        name:'略显浮夸'
      },
      {
        name:'善于沟通'
      },
      {
        name:'不善言辞'
      },
      {
        name:'害怕得罪'
      },
      ],
      choose4: [
        {
          name: "扶贫济困",
        },
        {
          name: "助老助残",
        },
        {
          name: "社区服务",
        },
        {
          name: "生态建设",
        },
        {
          name: "大型活动",
        },
        {
          name: "抢险救灾",
        },
        {
          name: "社会管理",
        },
        {
          name: "文化建设",
        },
        {
          name: "西部开发",
        },
        {
          name: "综合/其他",
        },
      ],
      choose5: [
        { name: "设计/平面" },
        { name: "摄影剪辑" },
        { name: "活动策划" },
        { name: "线下执行" },
        { name: "文化艺术" },
        { name: "IT互联网技术" },
        { name: "新媒体运营" },
        { name: "教育培训" },
        { name: "应急救援" },
        { name: "医疗护理" },
        { name: "交通运输" },
        { name: "环境科学" },
        { name: "体育" },
        { name: "会计审计" },
        { name: "文化和旅游" },
        { name: "我都可以" },
      ],
      index: 0,
      navId: -1,
      answer: [],
      answer4: [],
      answer5: [],
      userInfo:{},
    };
  },
  onLoad(option) {
    this.answer=option.answer.split(",").map(Number);
    this.answer4=option.answer4.split(",").map(Number);
    this.answer5=option.answer5.split(",").map(Number);
    this.getuser();
    console.log("sdf",this.answer,this.answer4,this.answer5)
  },
  methods: {
    end(){
       uni.switchTab({
            url: "/pages/tabbar/tabbar-4/tabbar-4",
          });
    },
    getuser(){
      	this.userInfo=getApp().globalData.user;
    }
  },
};
</script>

<style scoped lang="scss">
.page {
  display: flex;
  flex-direction: column;
  background: url(https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/%E5%BF%97%E6%84%BF%E6%B5%8B%E8%AF%95.jpg?sign=3385c4169b98766ee8db6faf660521f0&t=1661482999)
    no-repeat center;
  background-size: 100% 100%;
  min-height: 100vh;
  .box {
    display: flex;
    flex-direction: column;
    padding: 48rpx 48rpx 74rpx 48rpx;
    margin: 224rpx 32rpx 0 32rpx;
    min-height: 956rpx;
    background: rgba(255, 255, 255, 0.05);
    box-shadow: 0px 0px 106rpx 0px rgba(77, 119, 237, 0.39);
    opacity: 1;
    border-radius: 32rpx;
    border: 2rpx solid;
    border-color: white;
    .title {
      width: 100%;
      align-items: baseline;
    }
    .line {
      width: 100%;
    }
  }
  .po{
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
  .box1 {
    position:absolute;
    width: 116rpx;
    height: 116rpx;
    background: url("../../static/bg2.png") no-repeat center;
    background-size: 100% 100%;
  }
  .user {
    position: relative;
    margin-top: 72rpx;
    background: url("../../static/ba.png") no-repeat center;
    background-size: 100% 100%;
    width: 434rpx;
    height: 434rpx;
      .life{
        right:0;
        top:0;
      }
      .communication{
        left:-50rpx;
        top:134rpx;
      }
      .character{
        right: 0;
        bottom:0;
      }
  }
  .colorW {
    color: #ffffff;
  }
  .btn {
    color: white;
    margin-top: 194rpx;
    border: 2rpx solid;
    border-color: white;
    width: 560rpx;
    height: 96rpx;
    border-radius: 56rpx;
    background: rgba(69, 117, 246, 0.5);
  }
  .btn2 {
    width: 260rpx;
    height: 76rpx;
    line-height: 76rpx;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 114rpx;
    color: white;
    border: 2rpx solid;
    border-color: white;
  }
  .field{
    width: 100%;
    display: flex;
    margin-top: 88rpx;
    padding-left:74rpx;
     .message{
      opacity: 0.7;
     }
     .massage2{
      width:320rpx;
     }
  }
  .select {
    flex-wrap: wrap;
  }

}
</style>
