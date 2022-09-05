<template>
  <view style="background-color: #f4f5fa; height: 100vh">
    <u-navbar
      :is-back="false"
      title-color="white"
      :background="background"
      :border-bottom="false"
    >
      <view @click="goHome()" class="slot-wrap u-p-l-32">
        <u-image
          width="166rpx"
          height="60rpx"
          src="/static/img/zhiyuan.png"
        ></u-image>
      </view>
    </u-navbar>
    <view class="top">
      <view class="userMsg">
        <view class="msg">
          <image class="image" :src="info.image"></image>
          <!-- <u-image
            border-radius="90"
            width="128rpx"
            height="128rpx"
            :src="info.image"
          >
          </u-image> -->
          <view class="right u-m-l-16">
            <view class="name u-flex">
              {{info.group_name}}
              <u-image
               v-if="info.audit"
                class="u-m-l-8"
                src="/static/img/or-detail/rz.png"
                width="128rpx"
                height="48rpx"
              >
              </u-image>
            </view>
            <view class="u-flex">
              <u-image
                @click="goDetail()"
                src="/static/img/home.png"
                width="168rpx"
                height="48rpx"
              ></u-image>
              <u-image
                @click="goEditor()"
                class="u-m-l-16"
                src="/static/img/editor.png"
                width="168rpx"
                height="48rpx"
              >
              </u-image>
              <u-image
                class="u-m-l-16"
                @click="scan()"
                src="/static/scan.png"
                width="144rpx"
                height="48rpx"
              ></u-image>
            </view>
          </view>
        </view>
      </view>
      <ivew class="mid">
        <view class="min-content" @click="goData()">
          <view class="msg"
            ><text class="t1">{{info.voNumber}}</text><text class="t2">人</text></view
          >
          <view class="t3 msg">志愿人数</view>
        </view>
        <view class="min-content" @click="goData()">
          <view class="msg"
            ><text class="t1">3</text><text class="t2">次</text></view
          >
          <view class="t3 msg">人均参与</view>
        </view>
        <view class="min-content" @click="goData()">
          <view class="msg"
            ><text class="t1">{{info.serviceTime}}</text><text class="t2">小时</text></view
          >
          <view class="t3 msg">服务时长</view>
        </view>
      </ivew>
      <view class="bom u-flex">
        <view class="scan" @click="toast()">
          <view class="left">
            <u-image
              src="/static/img/people2.png"
              width="88rpx"
              height="88rpx"
            ></u-image>
          </view>
          <view class="ritht u-m-l-2">
            <view class="s-t1">公共人才才库</view>
            <view class="s-t2">涵盖平台志愿者</view>
          </view>
        </view>
        <view class="scan u-m-l-18" @click="goFactive">
          <view class="left">
            <u-image
              src="/static/img/f-active.png"
              width="88rpx"
              height="88rpx"
            ></u-image>
          </view>
          <view class="ritht u-m-l-2">
            <view class="s-t1">发布活动</view>
            <view class="s-t2">个性化活动选项</view>
          </view>
        </view>
      </view>
    </view>
    <u-tabs
      :list="tabs2"
      height="72"
      bar-width="52"
      font-size="30"
      active-color="#4575F6 "
      bold
      inactive-color="#333333 "
      bg-color="#F9F9F9"
      active-item-style="font-size:36rpx"
      item-style="	opacity=0.4;"
      @change="changeTab2"
      :current="current2"
    />
    <view class="newData">
      <view
        class="date"
        v-for="(v, i) in days"
        :key="i"
        :class="{ checked: v.i === searchParm.days }"
        @click="selectDate(v.i,v.week)"
      >
        <view class="week checked_text">{{ v.week }}</view>
        <view class="day checked_text">{{ v.day }}</view>
      </view>
    </view>
    <view class="content"  v-for="(item,index) in activityInfo" :key="index">
      <view class="u-flex" @click="goAcdetail(item._id)">
     <view class="left">
        <u-image
          height="180rpx"
          border-radius="8rpx"
          width="180rpx"
          :src="item.image"
          alt=""
        >
        </u-image>
      </view>
      <view class="right">
        <view class="name"
          >{{item.name}}
          <u-image
            class="u-m-l-8"
            height="40rpx"
            width="88rpx"
            :src="item.show?'/static/img/or-detail/start.png':'/static/img/or-detail/end.png'"
          ></u-image>
        </view>
        <view class="ti">活动类型：{{item.type}}</view>
        <view class="ti">开始时间：{{item.time}}</view>
        <view class="address"><text class="t2">计划招募：{{item.userNumber}} / {{item.allNumber}}</text></view>
      </view>
      </view>  
      <view class="change u-m-t-24" v-if="item.show">
        <view class="changeBtn bt1" @click="deleteAc(item)">删除</view>
        <view class="changeBtn u-m-l-24 bt1"  @click="goActivity(item._id)">编辑</view>
        <view class="changeBtn u-m-l-24 bt2" @click="getCode2(item._id)">签到二维码</view>
      </view>  
       <view class="change u-m-t-24" v-else>
        <view class="changeBtn bt1" @click="deleteAc(item)">删除</view>
        <view class="changeBtn u-m-l-24 bt1" @click="goMail(item._id)">信箱回信</view>
        <view class="changeBtn u-m-l-24 bt2"  @click="goStime(item._id)">评审服务时长</view>
      </view>  
    </view>
    <view  class="none mt60" v-show="!activityInfo.length">
      暂无活动
    </view>
    <u-modal v-model="deleteShow" show-cancel-button="true" @confirm="confirm" :content="content"></u-modal>
     <!--签到二维码-->
    <view class="shadow" v-if="show">
      <view class="shadow-body p30 code">
          <tki-qrcode :cid="cid" ref="qrcode2" :val="val" :size="size" :onval="onval" :loadMake="loadMake" :usingComponents="true"  />
           <view
          class="fs38 fw600 text-center close"
          style="background: #4575f6;margin-left:0;"
          @tap="show = false"
          >关闭</view
        >
      </view>
		</view>
  </view>
</template>

<script>
const db = wx.cloud.database();
const _ = db.command
import tkiQrcode from '../../components/tki-qrcode.vue'
export default {
   components: {
    tkiQrcode
  },
  data() {
    return {
      bgColor: "#001f3f",
      background: {
        backgroundColor: "#4575F6 ",
      },
      id:'',
      acid:'',
      deleteShow:false,
      show:false,
      content: '确认删除该活动',
      info:{},
      activityInfo:{},
      searchParm: {
        days: "",
      },
      current2: 0,
      tabs2: [
        {
          name: "全部",
          typeId: 1,
        },
        {
          name: "",
          typeId: 2,
        },
        {
          name: "",
          typeId: 3,
        },
      ],
      days: [
        {
          week: "全部",
          day: "-",
          i: "",
        },
      ],
      cid:"id3",
      val: '', // 要生成的二维码值
			size: 400, // 二维码大小
			unit: 'upx', // 单位
			icon: '', // 二维码图标
			iconsize: 40, // 二维码图标大小
			lv: 3, // 二维码容错级别 ， 一般不用设置，默认就行
			onval: false, // val值变化时自动重新生成二维码
			loadMake: true, 
      mapId:0,
    };
  },
  async onLoad(option) {
      uni.showLoading({
		  	title: '加载中',
			mask:true
	  });
    this.GetTime();
    console.log("id",option.id);
    console.log("option",option)
    console.log("fsdf",)
    this.getInfo();
    this.getacInfo();
  },
  async onShow(){
    this.getInfo();
    this.getacInfo();
    console.log("sdfsdf", uni.getStorageSync('or'))
  },
  methods: {
   async getacInfo(){
        uni.hideLoading();
     let ra=await db.collection('activity').where({
        orID:this.id
      })
      .orderBy("creatTime","desc")
      .get().then(res=>{
        this.activityInfo=res.data;
        console.log("活动列表",this.activityInfo);
      })
        let taskRes = await db
        .collection("activity")
        .where({
          orID:this.id,
          show: true,
        })
        .get()
        .then((res) => {
           uni.hideLoading();
          this.tabs2[1].name = "进行中(" + res.data.length + ")";
          this.tabs2[2].name =
            "已结束（" + (this.activityInfo.length - res.data.length) + ")";
        });
    },
    //获取日期活动列表
    getdataInfo(data, day){
      if (data == "全部") {
        db.collection("activity")
          .where({
            orID:this.id
          })
          .get()
          .then((res) => {
            this.activityInfo = res.data;
            console.log("this.acInfo", this.activityInfo);
          });
      } else {
        db.collection("activity")
          .where({
             orID:this.id,
            time: {
              $regex: day,
            },
          })
          .get()
          .then((res) => {
            this.activityInfo = res.data;
          });
      }
    },
    getInfo(){
      this.info=getApp().globalData.orInfo;
      console.log("sdfasd",getApp().globalData.orInfo)
      this.id=uni.getStorageSync('or');
    },
    goAcdetail(data) {
        console.log("dsfdsf",data)
      uni.navigateTo({
        url: `/pagesB/detail/ac-detail?id=1&acid=${data}`,
      });
    },
    goEditor() {
      uni.navigateTo({
        url: `../Editdata/Editdata?orid=${this.id}`,
      });
    },
    goData() {
      console.log("sdfsd");
      uni.navigateTo({
        url: "/pagesA/data-center/data-center",
      });
    },
    goFactive() {
      uni.navigateTo({
        url: `../f-active/f-active?orid=${this.id}`,
      });
    },
    scan() {
      wx.scanCode({
        success(res) {
          console.log("扫码结果222",res);
           db.collection('userActivity').doc(res.result).update({
        data:{
          signIN:true
        }
          }).then(res=>{
            console.log("res");
            uni.showToast({title:'签到成功',icon:'none'})
          })
        },
      });
    },
    //预览主页
    goDetail() {
      uni.navigateTo({
        url: `../detail/or-detail?type=false&id=${this.id}`,
      });
    },
    goHome() {
      console.log("dss");
      uni.switchTab({
        url: "../../pages/tabbar/tabbar-4/tabbar-4",
      });
    },
    changeTab2(val) {
      this.current2 = val;
        if (val == 1) {
        db.collection("activity")
          .where({
            orID:this.id,
            show: true,
          })
          .get()
          .then((res) => {
            this.activityInfo = res.data;
          });
      } else if (val == 2) {
        db.collection("activity")
          .where({
            orID:this.id,
            show: false,
          })
          .get()
          .then((res) => {
            this.activityInfo = res.data;
          });
      } else {
        db.collection("activity").where({
          orID:this.id,
        })
          .get()
          .then((res) => {
            this.activityInfo = res.data;
          });
      }
    },
    async selectDate(v,da) {
      this.searchParm.days = v;
      this.getdataInfo(da, this.days[v + 1].day)
    },
    GetTime() {
      this.days = [
        {
          week: "全部",
          day: "-",
          i: "",
        },
      ];
      var date = new Date();
      var base = Date.parse(date); // 转换为时间戳
      // var year = date.getFullYear(); //获取当前年份
      var mon = date.getMonth() + 1; //获取当前月份
      var week = date.getDay();
      var day = date.getDate(); //获取当前日
      var oneDay = 24 * 3600 * 1000;
      var daytime = `${mon >= 10 ? mon : "0" + mon}${
        day >= 10 ? day : "0" + day
      }${week}`; //今日时间
      this.$data.daytime = daytime; // 今日时间赋值给变量
      var time = `今天-${mon}-${day}`;
      var daytimeArr = [time];
      for (var i = 1; i < 6; i++) {
        //前七天的时间
        var now = new Date((base += oneDay));
        var month = now.getMonth() + 1;
        var mday = now.getDate();
        var week = now.getDay();
        var weeks = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];
        daytimeArr.push(
          [
            weeks[week],
            month >= 10 ? month : "0" + month,
            mday >= 10 ? mday : "0" + mday,
          ].join("-")
        );
      }
      daytimeArr.forEach((v, i) => {
        this.days.push({
          week: v.split("-")[0],
          day: `${v.split("-")[1]}-${v.split("-")[2]} `,
          i,
        });
      });
    },
    deleteAc(data){
      this.deleteShow=true;
      this.acid=data._id;
      this.mapId=Number(data.mapid)
       console.log("dasb",this.mapId)
      console.log("类型",typeof this.mapId)
    },
    toast(){
       uni.showToast({title:'功能正在内测中',icon:'none'})
    },
    //删除活动
    confirm(){
      db.collection('activity').doc(this.acid).remove().then(res=>{
        console.log("删除成功")
         uni.showToast({title:'删除成功',icon:'none'})
         this.getacInfo()
      })
      console.log("类型",typeof this.mapId)
      db.collection('map').where({
        id:_.eq(this.mapId)
      }).remove()
      console.log("删除活动")
    },
     getCode2(data){
      this.val=data
      this.show=true;
    },
    //活动编辑页
   goActivity(id){
      uni.navigateTo({
        url: `../f-active/f-active?acID=${id}`,
      });
    },
   goStime(id) {
      uni.navigateTo({
        url: `../service-time/service-time?acid=${id}`,
      });
    },
   goMail(id) {
      uni.navigateTo({
        url: `../mail-reply/mail-reply?acID=${id}`,
      });
     },
  },
};
</script>
<style scoped lang="scss">
.top {
  width: 100%;
  background-color: #4575f6;
  display: flex;
  flex-direction: column;
  padding: 46rpx 32rpx 32rpx 32rpx;
  border-radius: 0 0 16rpx 16rpx;

  .text {
    text-align: center;
    margin-top: 70rpx;
    font-size: 36rpx;
    font-weight: 600;
    color: #ffffff;
  }

  .userMsg {
    display: flex;

    .msg {
      display: flex;
      .image{
        width:128rpx;
        height:128rpx;
        border-radius:90px;
      }
    }

    .name {
      font-size: 36rpx;
      font-weight: 600;
      color: #ffffff;
    }

    .right {
      display: flex;
      flex-direction: column;
      padding-top: 10rpx;
    }
  }
}

.mid {
  margin-top: 64rpx;
  display: flex;
  justify-content: space-around;

  .min-content {
    display: flex;
    align-items: center;
    flex-direction: column;

    .msg {
      font-weight: 600;
      color: #ffffff;
    }

    .t1 {
      font-size: 52rpx;
    }

    .t2 {
      font-size: 24rpx;
    }

    .t3 {
      font-size: 32rpx;
    }
  }
}
.bom {
  display: flex;
  padding-top: 48rpx;
  .scan {
    display: flex;
    align-items: center;
    padding-left: 32rpx;
    width: 334rpx;
    height: 140rpx;
    background: #ffffff;
    border-radius: 24rpx;
    .right {
      display: flex;
      flex-direction: column;
      padding-left: 2rpx;
    }
    .s-t1 {
      font-size: 32rpx;
      font-weight: 600;
      color: #333333;
      opacity: 0.8;
    }
    .s-t2 {
      font-size: 24rpx;
      font-weight: 400;
      color: #333333;
      opacity: 0.6;
    }
  }
}
.content {
  display: flex;
  flex-direction: column;
  margin: 24rpx 32rpx 0 32rpx;
  padding: 24rpx 24rpx 24rpx 24rpx;
  background-color: #ffffff;
  border-radius: 24px;
  .right {
    display: flex;
    flex-direction: column;
    margin-left: 16rpx;

    .name {
      display: flex;
      font-size: 32rpx;
      font-weight: 600;
      color: #333333;
      opacity: 0.8;
    }

    .ti {
      margin-top: 8rpx;
      font-size: 24rpx;
      font-weight: 600;
      color: #333333;
      opacity: 0.4;
    }

    .address {
      margin-top: 8rpx;

      .t2 {
        font-size: 24rpx;
        font-weight: 600;
        color: #333333;
        opacity: 0.4;
      }

      .gps {
        padding-left: 8rpx;
        font-size: 24rpx;
        font-weight: 600;
        color: #4575f6;
      }
    }
  }
  .change{
    display: flex;
    justify-content: space-around;
     .changeBtn{
      width: 196rpx;
      text-align: center;
      height: 56rpx;
      line-height: 56rpx;
      border-radius:8rpx;
      font-weight: 600;
     }
      .bt1{
        background:rgba(69, 117, 246, 0.1);
        color: #4575F6;
      }
      .bt2{
        background: #4575F6;
        color: #FFFFFF;
      }
  }
}
.newData {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 24rpx;
  margin-bottom: 18rpx;
  padding-left: 27rpx;
  .date {
    text-align: center;
    border: 1rpx solid #4575f6;
    border-radius: 24rpx;
    font-size: 24rpx;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    width: 96rpx;
    height: 124rpx;
    margin-right: 10rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;

    .week {
      color: #4575f6;
    }

    .day {
      color: #4575f6;
    }
  }

  .checked {
    background-color: #4575f6;

    .checked_text {
      color: #fff;
    }
  }
}
.code{
     display: flex;
    flex-direction: column;
    align-items: center;
}
.none{
  text-align: center;
  opacity:0.7;
}
</style>
