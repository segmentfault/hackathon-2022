<template>
  <view class="page-bg">
    <comHead>{{ name }}</comHead>
    <view class="activity-image">
      <swiper class="mineBanner_swiper" autoplay="false" duration="500">
        <swiper-item v-for="(item, index) in list" :key="index">
          <image
            style="height: 100%; width: 100%"
            :src="info.image"
            mode="aspectFill"
          />
        </swiper-item>
      </swiper>
    </view>
    <view class="or-info">
      <view class="head">
        <view class="left">
          <view class="n-info">
            <u-image
              height="64rpx"
              border-radius="90"
              width="64rpx"
              :src="info.image"
              alt=""
            ></u-image>
            <view class="or-name u-p-l-16">{{ info.group_name }}</view>
            <u-image
              v-if="info.audit"
              class="u-m-l-8"
              height="48rpx"
              width="136rpx"
              src="/static/img/or-detail/rz.png"
            >
            </u-image>
          </view>
          <view class="type">
            <view class="t1">服务类别：</view>
            <view class="t2">{{ info.serviceCa }}</view>
          </view>
        </view>
        <u-image
          height="88rpx"
          width="88rpx"
          src="/static/img/or-detail/qr.png"
        ></u-image>
      </view>
      <view class="u-m-t-8 u-flex">
        <view class="t1">成立时间：</view>
        <viwe class="t3"
          >{{ info.createTime }} | 成员：{{ info.userNumber }}人</viwe
        >
      </view>
      <view class="u-m-t-8 u-flex">
        <view class="t1">联系人</view>
        <viwe class="t3">{{ info.contact }}</viwe>
      </view>
      <view class="u-m-t-8 end" style="display: flex">
        <view class="t1">介绍：</view>
        <viwe class="t3 " style="width: 450rpx">{{ info.value }}</viwe>
      </view>
      <view class="address u-m-t-16">
        <view class="left">
          <view class="ad u-line-1">{{ info.address }}</view>
          <view class="time">驾车{{distance}}，需要{{goTime}}分钟</view>
        </view>
        <view class="gps u-flex">
          <u-image
            class="u-m-r-24"
            src="/static/img/or-detail/gps.jpg"
            width="48rpx"
            height="70rpx"
            @click="mapFun()"
          >
          </u-image>
          <u-image
            @click="goPhoneBtn(info.phone)"
            src="/static/img/or-detail/phone.jpg"
            width="48rpx"
            height="70rpx"
          ></u-image>
        </view>
      </view>
    </view>
    <view class="active">
      <view class="top">
        <view class="left">组织活动</view>
        <view class="right">更多</view>
      </view>
      <view
        class="bom u-flex"
        v-if="acState"
        @click="goActive(activityInfo._id)"
      >
        <view class="b-left">
          <u-image
            :src="activityInfo.image"
            width="180rpx"
            height="180rpx"
          ></u-image>
        </view>
        <view class="b-righ u-m-l-12">
          <view class="name u-flex"
            >{{ activityInfo.name }}
            <u-image
              class="u-m-l-8"
                 :src="activityInfo.show?'/static/img/or-detail/start.png':'/static/img/or-detail/end.png'"
              width="88rpx"
              height="44rpx"
            ></u-image>
          </view>
          <view class="text">活动类型：{{ activityInfo.type }}</view>
          <view class="text">开始时间：{{ activityInfo.time }}</view>
          <view class="text"
            >计划招募：{{ activityInfo.userNumber }} /
            {{ activityInfo.allNumber }}</view
          >
        </view>
      </view>
      <view v-else class="noneAc u-flex">
        暂无活动
      </view>
    </view>
    <view class="u-flex u-p-l-32 u-p-b-24 u-m-t-24">
      <u-image
        src="/static/img/or-detail/shu.png"
        width="8rpx"
        height="28rpx"
      ></u-image>
      <view class="u-p-l-10">志愿风采</view>
    </view>
     <view v-if="Wlist.length" class="waterfall u-m-l-32 u-m-r-32">
      <u-waterfall v-model="flowList" ref="uWaterfall">
        <template v-slot:left="{ leftList }">
          <view
            class="demo-warter"
            v-for="(item, index) in leftList"
            :key="index"
          >
            <view @click="goVolun(item._id)">
              <!-- 警告：微信小程序中需要hx2.8.11版本才支持在template中结合其他组件，比如下方的lazy-load组件 -->
              <u-lazy-load
                threshold="0"
                border-radius="10"
                :image="item.img[0]"
                :index="index"
              >
              </u-lazy-load>
              <view class="text u-line-2 u-text-left" >{{ item.title }}</view>
              <view class="msg">
                <view class="u-flex"  >
                  <u-image
                    border-radius="90"
                    width="40rpx"
                    height="40rpx"
                    :src="item.userImage"
                  ></u-image>
                  <view class="name">{{ item.userName }}</view>
                </view>
                <view class="u-flex u-m-r-16">
                   <u-image src="/static/love2.png"
                    :show-loading="false"
                    width="36rpx"
                    height="36rpx"
                    class="u-m-r-4"
                  ></u-image>
                  {{item.likeNumber}}</view>
              </view>
            </view>
          </view>
        </template>
        <template v-slot:right="{ rightList }">
          <view
            class="demo-warter u-m-l-14"
            v-for="(item, index) in rightList"
            :key="index"
          >
            <view @click="goVolun(item._id)">
              <u-lazy-load
                threshold="0"
                border-radius="10"
                :image="item.img[0]"
                :index="index"
              >
              </u-lazy-load>
              <view class="text u-line-2 u-text-left">{{ item.title }}</view>
              <view class="msg">
                <view class="u-flex">
                  <u-image
                    border-radius="90"
                    width="40rpx"
                    height="40rpx"
                    :src="item.userImage"
                  ></u-image>
                  <view class="name">{{ item.userName }}</view>
                </view>
               <view class="u-flex u-m-r-16">
                   <u-image src="/static/love2.png"
                    :show-loading="false"
                    width="36rpx"
                    height="36rpx"
                    class="u-m-r-4"
                  ></u-image>
                  {{item.likeNumber}}</view>
              </view>
            </view>
          </view>
        </template>
      </u-waterfall>
       <u-loadmore
        margin-top="24" 
        margin-bottom="24"
        :status="loadStatus"
        @loadmore="addRandomData"
      ></u-loadmore>
    </view>
    <view v-else class="vo u-flex ">
      暂无志愿风采
    </view>
     <view style="height:1rpx"></view>
    <view class="bt" v-if="state">
      <button class="btn" @click="success">加入团体</button>
    </view>
  </view>
</template>
<script>
import comHead from "../../components/com-head.vue";
const db = wx.cloud.database();
export default {
  components: {
    comHead,
  },
  data() {
    return {
      flowList: [],
      type: false,
      name: "",
      openid: "",
      userInfo: {},
      state: true,
      acState: true,
      list: {
        url: "https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/animal.png?sign=8a0505a93044c83bccb10d1d7ba45712&t=1654840177",
      },
      info: {},
      activityInfo: {},
      id: "",
      Wlist: [],
      longitude: "",
      latitude: "",
      distance:'',
      goTime:"",
      mapid:'',
    };
  },
  onLoad(option) {
      uni.showLoading({
		  	title: '加载中',
			mask:true
	  });
   this.openid = wx.getStorageSync("openid");
    this.type = option.type;
    this.id = option.id;
    if(option.mapId){
      this.mapid=option.mapId.toString()
      this.getOrm()
    }else{
    this.getAuthorizeInfo();
    this.getVo();
   
    this.getuserInfo();
    this.getState();
    this.getacInfo();
    }
   
    console.log("sd", this.type);
    this.name = this.type == "true" ? "组织详情" : "预览主页";
    this.addRandomData();
    this.getInfo();
  },
  onShow() {
  },
  mounted() {
			
		},
  methods: {
    //获取当前位置
    getAuthorizeInfo() {
      const that = this;
      uni.authorize({
        scope: "scope.userLocation",
        success() {
          // 允许授权
          that.getLocationInfo();
        },
        fail() {
          // 拒绝授权
          that.openConfirm();
          console.log("你拒绝了授权，无法获得周边信息");
        },
      });
    },
    //根据mapid获取组织信息
    getOrm(){
       db.collection("organization")
        .where({
          mapid:this.mapid
        })
        .get()
        .then((res) => {
          this.info = res.data[0];
          this.id=res.data[0]._id;
          this.getuserInfo();
          this.getState();
          this.getacInfo();
            this.getInfo();
        this.getVo();
        });
    },
    getLocationInfo() {
            const that = this;
      uni.getLocation({
        type: "wgs84",
        success(res) {
          console.log(res);
          that.latitude=res.latitude;
          that.longitude=res.longitude;
          setTimeout(()=>{
            that.getDistance()
          },100)
      
        },
      });
    },
    openConfirm() {
      uni.showModal({
        title: "请求授权当前位置",
        content: "需要获取您的地理位置，请确认授权",
        success: (res) => {
          if (res.confirm) {
            uni.openSetting(); // 打开地图权限设置
          } else if (res.cancel) {
            uni.showToast({
              title: "你拒绝了授权，无法获得周边信息",
              icon: "none",
              duration: 1000,
            });
          }
        },
      });
    },
    //获取志愿风采
     getVo(){
      db.collection('volunteerShow')
      .where({
        orID:this.id
      })
      .get().then(res=>{
        this.Wlist=res.data
        console.log("sb",this.Wlist)
         this.addRandomData();
      })
    },
    getDistance() {
      console.log("地址", this.latitude, this.longitude);
      uni.request({
        url: "https://apis.map.qq.com/ws/distance/v1/?parameters",
        method: "GET",
        data: {
          mode: "driving",
          from: `${this.latitude},${this.longitude}`,
          to: `${this.info.latitude},${this.info.longitude}`,
          key: "E5IBZ-2CF63-4OW3C-3KUWB-ZMOQO-WTFGF", //获取key
        },
        success: (res) => {
          console.log("snb", res);
          let hw = res.data.result.elements[0].distance; //拿到距离(米)
          this.goTime=(res.data.result.elements[0].duration/60).toFixed(0);
          console.log("thos.gs",this.goTime)
          if (hw && hw !== -1) {
            if (hw < 1000) {
              hw = hw + "m";
            }
            //转换成公里
            else {
              hw = (hw / 2 / 500).toFixed(2) + "km";
            }
          } else {
            hw = "距离太近或请刷新重试";
          }
          this.distance=hw;
          console.log(hw);
          
        },
      });
    },
    getacInfo() {
      db.collection("activity")
        .where({
          orID: this.id,
        })
        .get()
        .then((res) => {
          if (res.data.length == 0) {
            this.acState = false;
          }
          this.activityInfo = res.data[0];
          console.log("活动列表", this.activityInfo);
          	uni.hideLoading();
        });
    },
    mapFun() {
      uni.openLocation({
        latitude: this.info.latitude, //纬度
        longitude: this.info.longitude, //经度
        name: this.info.addressName,
        address: this.info.address,
      });
    },
    goPhoneBtn(val) {
      uni.makePhoneCall({
        // 手机号
        phoneNumber: val,
        // 成功回调
        success: (res) => {},
        // 失败回调
        fail: (res) => {},
      });
    },
    getInfo() {
      db.collection("organization")
        .doc(this.id)
        .get()
        .then(async (res) => {
          this.info = res.data;
          console.log("组织信息", this.info);
        });
    },
    //获取用户加入组织状态
    getState() {
      db.collection("userOrganization")
        .where({
          _openid: this.openid,
          orID: this.id,
        })
        .get()
        .then((res) => {
          if (res.data.length) {
            console.log("状态", this.state);
            this.state = false;
          }
        });
    },
    //获取用户信息
    getuserInfo() {
      db.collection("userInfo")
        .where({
          _openid: this.openid,
        })
        .get()
        .then((res) => {
          this.userInfo = res.data[0];
          console.log("dfds", res);
        });
    },
    goVolun(data) {
      console.log("sdfds")
       uni.navigateTo({
        url: `/pagesA/volunteer-demeanour/volunteer-demeanour?id=${data}`,
      });
    },
    goActive(data) {
      uni.navigateTo({
        url: `ac-detail?acid=${data}`,
      });
    },
    change(index) {
      this.current = index;
    },
    success() {
      if (!uni.getStorageSync("login")) {
        uni.showToast({ title: "请登录", icon: "none" });
        setTimeout(() => {
          uni.navigateTo({
            url: "/pagesA/sign/sign?id=1",
          });
        }, 700);
        return;
      }
      delete this.info._id;
      delete this.info._openid;
      this.info.userNumber++;
      db.collection("organization")
        .doc(this.id)
        .update({
          data: this.info,
        })
        .then((res) => {
          console.log("参与用户添加成功");
          console.log("df", res);
        });
      db.collection("userOrganization")
        .add({
          data: {
            orID: this.id,
            name: this.userInfo.user_name,
            image: this.userInfo.user_img,
          },
        })
        .then((res) => {
          uni.navigateTo({
            url: "../../pagesA/signUp/success",
          });
        });
    },
    addRandomData() {
      for (let i = 0; i < this.Wlist.length; i++) {
        let index = this.$u.random(0, this.Wlist.length - 1);
        // 先转成字符串再转成对象，避免数组对象引用导致数据混乱
        let item = JSON.parse(JSON.stringify(this.Wlist[index]));
        item.id = this.$u.guid();
        this.flowList.push(item);
      }
    },
  },
};
</script>
<style scoped lang="scss">
.page-bg {
  position: relative;
  min-height: 100vh;
   background: #f2f2f2;
  width: 100%;
}

.box {
  width: 100%;
  height: 16rpx;
  background: #f2f2f2;
}

.t1 {
  width: 140rpx;
  opacity: 0.4;
  font-size: 28rpx;
  font-weight: 600;
  color: #333333;
}

.t2 {
  margin-left: 8rpx;
  padding: 5rpx 8rpx 5rpx 8rpx;
  color: #4575f6;
  background-color: rgba(69, 117, 246, 0.1);
  border-radius: 8rpx;
}

.t3 {
  margin-left: 10rpx;
  font-size: 28rpx;
  font-family: PingFang SC-Semibold, PingFang SC;
  font-weight: 600;
  color: #333333;
  opacity: 0.8;
}

.mineBanner_swiper {
  height: 100%;
}

.activity-image {
  position: relative;
  height: 512upx;
  width: 100%;
}

.or-info {
  position: relative;
  z-index: 22;
  padding: 32upx 32upx 16upx 32upx;
  margin: -10px 0upx 10rpx 0upx;
  background-color: #fff;
  border-radius: 16upx 16upx 0 0;
  display: flex;
  flex-direction: column;

  .head {
    display: flex;
    justify-content:space-between;
    .n-info {
      display: flex;

      .or-name {
        font-size: 36rpx;
        font-weight: 600;
        color: #333333;
      }
    }

    .type {
      margin-top: 20rpx;
      display: flex;
      align-items: center;
    }
  }

  .end {
    padding-bottom: 32rpx;
    border-bottom: 1rpx solid rgba(200, 200, 200, 0.1);
  }
}

.address {
  display: flex;
  justify-content: space-between;

  .ad {
    font-size: 24rpx;
    font-weight: 600;
    color: #333333;
  }

  .time {
    font-size: 24rpx;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
  }
}

.active {
  margin-top: 16rpx;
  padding: 32rpx;
  background:#FFFFFF;
  .top {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-radius: 24rpx;

    .left {
      font-size: 32rpx;
      font-weight: 600;
      color: #333333;
    }

    .right {
      font-size: 24rpx;
      font-weight: 600;
      color: #333333;
      opacity: 0.4;
    }
  }

  .bom {
    margin-top: 16rpx;
    border-radius: 24rpx;
    border: 2rpx solid black;
    border-color: rgba(0, 0, 0, 0.1);
    padding: 24rpx;

    .name {
      font-size: 32rpx;
      font-weight: 600;
      opacity: 0.8;
    }

    .text {
      margin-top: 6rpx;
      font-size: 24rpx;
      font-weight: 600;
      color: #333333;
      opacity: 0.4;
    }
  }
  .msg {
   margin-top: 16rpx;
    display: flex;
    justify-content: space-between;
  }

}
.noneAc{
    justify-content: center;
    align-items: center;
    margin-top: 16rpx;
    padding: 24rpx;
    height:200rpx;
    border-radius: 24rpx;
    border: 2rpx solid black;
    border-color: rgba(0, 0, 0, 0.1);
    opacity: 0.5;
}
.bt {
  position: fixed;
  width: 100%;
  height: 96rpx;
  display: flex;
  align-items: center;
  bottom: 30rpx;
}
.vo{
  justify-content: center;
  padding-bottom:100rpx;
   opacity: 0.5;
}
.waterfall {
  .demo-warter {
    margin-top: 16rpx;
    background: #ffffff;
  }
  .msg {
    margin-top: 16rpx;
    display: flex;
    justify-content: space-between;
    padding-bottom:16rpx;
    padding-left:16rpx;
    padding-right:16rpx;
  }
  .text {
    margin-left:12rpx;
    margin-top: 16rpx;
    font-size: 28rpx;
    font-weight: 400;
    color: #000000;
  }
  .name {
    padding-left: 8rpx;
    font-size: 24rpx;
  }
}
.btn {
  color: white;
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
</style>
