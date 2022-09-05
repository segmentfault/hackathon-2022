<template>
  <view>
    <u-navbar
      :is-back="false"
      title="公益地图"
      title-color="white"
      :background="background"
      :border-bottom="false"
    >
      <view class="slot-wrap u-p-l-32" @click="goOr()">
        <u-image
          width="166rpx"
          height="60rpx"
          src="/static/img/switch.png"
        ></u-image>
      </view>
    </u-navbar>
    <view class="content">
      <map
        style="width: 100%; height: 90vh"
				:id="mapId"
        :layer-style="5"
        :style="{ height: mapheight }"
        :show-location="true"
        :latitude="latitude"
        :longitude="longitude"
        :markers="marker"
        :scale="scale"
        @markertap="markertap"

      >
        <cover-view class="choose">
				  <button :class="{ active: navId == 1 }" class="chooseBtn" @click="toggleNav(1)">公益活动</button>
					<button :class="{ active: navId == 2 }" class="chooseBtn" @click="toggleNav(2)">附近组织</button> 
        </cover-view>
        <cover-view class="cover-view">
          <cover-image
            @click="goActive(1)"
            class="cover-image"
            src="/static/img/map/huodong.png"
          ></cover-image>
          <cover-image
            @click="goActive(2)"
            class="cover-image"
            src="/static/img/map/search.png"
          ></cover-image>
          <cover-image
            class="cover-image"
            @click="backLocal"
            src="/static/img/map/gps.png"
          ></cover-image>
        </cover-view>
      </map>
    </view>
  </view>
</template>

<script>
const db = wx.cloud.database();
const _ = db.command
export default {
  data() {
    return {
      list: [
        {
          name: "公益活动",
        },
        {
          name: "附近组织",
        },
      ],
			 mapId: "myMap",
      current: 1,
      background: {
        backgroundColor: "#4575F6 ",
      },
			  navId: 1,
      latitude: 23.106574, //纬度
      longitude: 113.324587, //经度
      scale: 13, //缩放级别
      bottomData: false,
      marker: [
      ],
    };
  },
	onLoad(){
     this.mapCtx = wx.createMapContext('mapId')
      uni.showLoading({
		  	title: '加载中',
			mask:true
	  });
   this.getMarker();
   this.setUser();
	},
  methods: {
     goActive(data) {
      uni.navigateTo({
        url: `/pagesB/detail/volunteering/volunteering?navId=${data}`,
      });
    },
		   toggleNav(id) {
     this.marker=[]
      this.navId = id;
				if(id==1){
			db.collection('map')
			.where({
				id:_.gt(1)
			})
			.get().then(res=>{
				this.marker=res.data
				console.log("dsfd",this.marker)
			})
		}else{
				db.collection('map')
			.where({
				id:_.lt(1)
			})
			.get().then(res=>{
				this.marker=res.data
				console.log("dsfd",res)
			})
		}
    },
    getAuthorize() {
      return new Promise((resolve, reject) => {
        uni.authorize({
          scope: "scope.userLocation",
          success: () => {
            resolve(); // 允许授权
          },
          fail: () => {
            reject(); // 拒绝授权
          },
        });
      });
    },
	  getLocationInfo() {
      const that = this;
      uni.getLocation({
        type: "gcj02",
        success: function (res) {
          // 暂时
          that.longitude = res.longitude; //118.787575;
          that.latitude = res.latitude; //32.05024;
          that.markers = [
            {
              id: "",
              latitude: res.latitude,
              longitude: res.longitude,
              iconPath: "../../static/mark.png",
              width: that.markerHeight, //宽
              height: that.markerHeight, //高
            },
          ];
          that.getList();
        },
      });
    },
		openConfirm() {
      return new Promise((resolve, reject) => {
        uni.showModal({
          title: "请求授权当前位置",
          content: "我们需要获取地理位置信息，为你查看附近的活动和组织",
          success: (res) => {
            if (res.confirm) {
              uni.openSetting().then((res) => {
                if (res[1].authSetting["scope.userLocation"] === true) {
                  resolve(); // 打开地图权限设置
                } else {
                  reject();
                }
              });
            } else if (res.cancel) {
              reject();
            }
          },
        });
      });
    },
     setUser(){
       db.collection("userInfo")
        .where({
          _openid: wx.getStorageSync("openid"),
        })
        .get()
        .then((res) => {
          getApp().globalData.user=res.data[0];
        });
       db.collection('organization').doc(uni.getStorageSync('or')).get().then(res=>{
       getApp().globalData.orInfo=res.data;
      })
    },
		//获取地图上图标
		getMarker(){
			db.collection('map').get().then(res=>{
				this.marker=res.data
					console.log("dfsdf",this.marker)
           uni.hideLoading();
			})
		},
		markertap(e){
			console.log(e);

			let id =e.detail.markerId
			if(id>1){
				uni.navigateTo({
        url: `/pagesB/detail/ac-detail?mapId=${id}`,
      });
			}else{
			 uni.navigateTo({
        url: `/pagesB/detail/or-detail?type=true&mapId=${id}`,
      });
			}
		},
    // 彻底拒绝位置获取
    rejectGetLocation() {
      uni.showToast({
        title: "你拒绝了授权，无法获得周边信息",
        icon: "none",
        duration: 2000,
      });
    },
    getList() {
      console.log("获取您的位置信息");
    },
		 backLocal(e) {
			console.log("sdfsd")
      this.mapCtx = wx.createMapContext(this.mapId) // 这里一定要写你自己的map的id，类似于getElementById
      this.mapCtx.moveToLocation()
    },
		sectionChange(index){
		console.log(index)
		
		},
		 goOr() {
        if (!uni.getStorageSync("Ologin")) {
        uni.navigateTo({
          url: "/pagesA/sign/sign?id=2",
        }); } else{
           uni.navigateTo({
        url: "../../../pagesB/or-home/or-home",
         });
        }
    },
  },
  onReady() {
    //   wx请求获取位置权限
    this.getAuthorize()
      .then(() => {
        //   同意后获取
        this.getLocationInfo();
      })
      .catch(() => {
        //   不同意给出弹框，再次确认
        this.openConfirm()
          .then(() => {
            this.getLocationInfo();
          })
          .catch(() => {
            this.rejectGetLocation();
          });
      });
  },
  }
</script>
<style scoped lang="scss">
.cover-view {
  position: absolute;
  bottom: 120rpx;
  right: 32rpx;
}

.cover-image {
  margin-top: 16rpx;
  width: 84rpx;
  height: 84rpx;
}
.choose {
  display: flex;
  margin-top: 36rpx;
  justify-content: center;
	align-items: center;
}
.chooseBtn{
  margin:0 !important;
  padding-left:44rpx;
  padding-right:44rpx;
  height: 72rpx;
	display: flex;
	line-height: 72rpx;
	justify-content: center;
	border-radius: 25px;
		/* 这里可以改成渐变： background:linear-gradient(to right, #FFDE28,#FF3228) */
	font-size: 36rpx;
}
.active {
  background: #4575f6;
   color: white;
}
button::after {
	 border: none;
}
</style>
