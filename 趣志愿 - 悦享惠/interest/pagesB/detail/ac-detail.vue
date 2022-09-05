<template>
  <view class="page-bg">
    <comHead :returnBack="false">活动详情</comHead>
		<canvas canvas-id="canvas"  style="width:750px; height:490px; position:fixed;left:100%;" id="canvas"></canvas>
    <view class="activity-image">
      <swiper class="mineBanner_swiper" autoplay="false" duration="500">
        <swiper-item v-for="(item, index) in list" :key="index">
          <image
            style="height: 100%; width: 100%"
            :src="info.image"
            mode="aspectFill"
            @click="preview(item.url)"
          />
        </swiper-item>  
      </swiper>
    </view>
    <view class="or-info">
      <view class="head">
        <view class="left">
          <view class="n-info">
            <view class="or-name">{{ info.name }}</view>
            <u-image
              class="u-m-l-8"
              height="40rpx"
              width="88rpx"
              :src="info.show?'/static/img/or-detail/start.png':'/static/img/or-detail/end.png'"
            >
            </u-image>
          </view>
          <view class="type">
            <view class="t1">活动类别：</view>
            <view class="t1">{{ info.type }}</view>
          </view>
        </view>
      </view>
      <view class="u-m-t-8" style="display: flex">
        <view class="t1">开始时间：</view>
        <viwe class="t1">{{ info.time }}</viwe>
      </view>
      <view class="u-m-t-8 u-flex">
        <view class="t1">服务时长：</view>
        <viwe class="t1">{{ info.serverTime }}小时</viwe>
      </view>
      <view class="u-m-t-8" style="display: flex">
        <view class="t1">计划招募：</view>
        <viwe class="t1">{{info.userNumber}}/{{ info.allNumber }}</viwe>
        <view class="u-m-l-16 text-color fw600" @click="goMembers()" v-if="!id">查看成员</view>
      </view>
      <view class="address u-m-t-26">
        <view class="left">
          <view class="ad">{{ info.address }}</view>
          <view class="time">驾车{{distance}}，需要{{goTime}}分钟</view>
        </view>
        <view class="gps u-flex">
          <u-image
            @click="mapFun()"
            src="/static/img/or-detail/gps.jpg"
            width="48rpx"
            height="70rpx"
          >
          </u-image>
        </view>
      </view>
    </view>
    <view class="active">
      <view class="top">
        <view class="a-t1">主办方</view>
      </view>
      <view class="bom u-m-t-16">
        <view class="logo u-flex">
          <u-image
            border-radius="90"
            width="64rpx"
            height="64rpx"
            :src="orinfo.image"
          ></u-image>
          <text class="a-t2 u-m-l-16">{{ orinfo.group_name }}</text>
        </view>
        <view class="phone">
          <u-image
            @click="goPhoneBtn(orinfo.phone)"
            src="/static/img/or-detail/phone.jpg"
            width="48rpx"
            height="70rpx"
          ></u-image>
        </view>
      </view>
    </view>
    <view class="bom-btn">
      <view :class="{ ac: navId == 1 }" class="bt1" @click="toggleNav(1)"
        >活动介绍</view
      >
      <view :class="{ ac: navId == 2 }" class="bt1" @click="toggleNav(2)"
        >志愿小队</view
      >
      <view :class="{ ac: navId == 3 }" class="bt1" @click="toggleNav(3)"
        >公益信箱</view
      >
    </view>
    <view v-if="navId == 1" class="cs1">
      <view class="introduce">
        {{ info.introduce}}
      </view>
    </view>
    <view v-if="navId == 2" class="team cs1">
      <view v-if="ActivityUser.length>=2">
        <view class="t-top">
        <view class="t-top-bt">
          <u-image
            width="128rpx"
            height="128rpx"
            border-radius="90"
            :src="ActivityUser[0].image"
          ></u-image>
          <view class="group-name group1">
              志愿A队
          </view>
        </view>
      
        <view class="t-top-bt"
          ><u-image
             width="128rpx"
            height="128rpx"
             border-radius="90"
            :src="ActivityUser[1].image"
          ></u-image>
            <view class="group-name group2">
              志愿B队
          </view>
        </view>
       
      </view>
      <view class="u-m-t-32">
         <view class="pk">
			<view class="leftPk" :style="{width:pkWith+'%'}">
					<view class="u-p-l-24">{{leftNum}}</view>
						<view class="impact">影响力</view>
			</view>
					<view class="u-p-r-24">{{rightNum}}</view>
		</view>
      </view>
      <view class="t-msg u-m-t-32">
        <view class="msg-t1" @tap="showStates">如何提高影响力?</view>
      </view>
      <view class="Tname u-m-t-52">
        <view class="T-content">
          <view class="t-name tn1">志愿A队</view>
          <view class="players p-bg1">
            <view class="u-flex u-m-t-24" v-for="(item,index) in userLeft" :key="index">
                <view class="captain">
                  <u-image
                width="64rpx"
                height="64rpx"
                border-radius="90"
                :src="item.image"
               ></u-image>
                <view class="one color1" v-if="index==0">队长</view>
              </view>   
              <text class="pl-msg">{{item.name}}</text>
             
            </view>
          </view>
        </view>
        <view class="T-content u-m-l-20">
          <view class="t-name tn2">志愿B队</view>
          <view class="players p-bg2">
            <view class="u-flex u-m-t-24"  v-for="(item,index) in userRight" :key="index">
              <view class="captain">
                  <u-image
                    width="64rpx"
                    height="64rpx"
                    border-radius="90"
                     :src="item.image"
                   ></u-image>
                   <view class="one color2" v-if="index==0">队长</view>
              </view>
              <text class="pl-msg">{{item.name}}</text>
            </view>
          </view>
        </view>
      </view>
      </view>
      <view class="noneTeam u-flex" v-else>
        暂无志愿小队
      </view>
    </view>
    <view v-if="navId == 3" class="mail cs1">
      <view class="m-left">
        <u-image
          width="104rpx"
          height="104rpx"
          src="https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/Slice%2094%402x.png?sign=4d41662155e19923d185985a48c71755&t=1655217597"
        >
        </u-image>
      </view>
      <view class="m-right u-m-l-10" @tap="eshowStates">
        <view class="m-msg1">FOR {{userAc.name?userAc.name:userInfo.user_name}}</view>
        <view class="m-msg2">{{userAc.mail}}</view>
      </view>
    </view>
    <view class="shadow" v-if="showState">
      <view class="shadow-body p30">
        <view class="fs36 fw600 text-center">如何提高影响力</view>
        <view class="fs28 color-222 mt30">
          影响力将通过分享活动后，浏览的用户数，以及填写的服务时长共同决定！
        </view>
        <view
          class="fs38 fw600 text-center close"
          style="background: #4575f6"
          @tap="showState = false"
          >我知道了
        </view>
      </view>
    </view>
    <view class="shadow" v-if="eshow">
      <view class="shadow-body p30">
        <view class="fs36 fw600 text-center">FOR {{userAc.name?userAc.name:userInfo.user_name}}</view>
        <view class="fs28 color-222 mt30">
          {{userAc.mail?userAc.mail:'暂无信息'}}
        </view>
        <view
          class="fs38 fw600 text-center close"
          style="background: #4575f6"
          @tap="eshow = false"
          >我知道了</view
        >
      </view>
    </view>
    <view v-if="id">
      <view class="bm" v-show="state == 1">
        <u-button shape="circle" type="primary" @click="goSuccess()"
          >立即报名</u-button
        >
      </view>
      <view class="bm bm u-flex u-p-l-32 u-p-r-32" v-show="state == 2&&!signIN">
             <view class="bt2" style="padding:0" @click="scan">扫码签到</view>
           <view class="bt2 u-m-l-20" style="padding:0" @click="getCode()">签到码</view>
           <view class="bt2 u-m-l-20" style="padding:0" @click="popup = true"  >咨询通道</view>
      </view>
      <view class="bm u-flex u-p-l-32 u-p-r-32" v-show="state&&signIN">
        <view class="bt2" style="padding:0" @click="changeTime()" >填写服务时长</view>
           <view class="bt2 u-m-l-20" style="padding:0" @click=saveImg()>下载电子证书</view>
           <view class="bt2 u-m-l-20" style="padding:0" @click="goFvolunteer()" >上传风采</view>
      </view>
    </view>
    <view v-else>
      <view v-if="info.show">
        <view class="bm u-p-l-32 u-p-r-32 u-flex">
           <view class="bt2" style="padding:0" @click="getCode2(info._id)">签到二维码</view>
           <view class="bt2 u-m-l-20" style="padding:0" @click="goActivity()">编辑</view>
           <view class="bt2 u-m-l-20" style="padding:0" @click="deleteShow=true" >删除</view>
        </view>
      </view>
     <view v-else>
       <view class="bm u-p-l-32 u-p-r-32 u-flex">
        <view class="bt2" @click="goStime()">评审服务时长</view>
        <view class="bt2 u-m-l-30" @click="goMail()">信箱回信</view>
      </view>
     </view>
        </view>
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
     	<u-modal v-model="deleteShow" show-cancel-button="true" @confirm="confirm" :content="content"></u-modal>
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
     <view class="shadow" v-if="popup">
      <view class="shadow-body p30">
        <view class="fs36 fw600 text-center">联系电话</view>
        <view class="fs28 color-222 mt30">
         {{info.phone}}
        </view>
        <view
          class="fs38 fw600 text-center close"
          style="background: #4575f6"
          @tap="popup = false"
          >我知道了
        </view>
      </view>
    </view>
  </view>
</template>
<script>
const db = wx.cloud.database();
const _ = db.command
import comHead from "../../components/com-head.vue";
import tkiQrcode from '../../components/tki-qrcode.vue'
export default {
  components: {
    comHead,
    tkiQrcode
  },
  data() {
    return {
      state: 1,
      id: 1,
      acid: "",
      flowList: [],
      openid: "",
      info: {},
      orinfo: {},
      ActivityUser:{},
      vlaue:'',
      mapid:0,
      popup:false,
      deleteShow:false,
      userInfo: {},
      showState: false,
      eshow: false,
      type: false,
      show:false,
      Tshow:false,
      name: "",
      content: '确认删除该活动',
      list: {
        url: "https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/dog.png?sign=26de0216a571bed7685e66cec6b4f216&t=1655197259",
      },
      navId: 1,
      longitude: "",
      latitude: "",
      distance:'',
      goTime:"",
      cid:"id2",
      val: '', // 要生成的二维码值
			size: 400, // 二维码大小
			unit: 'upx', // 单位
			background: '#b4e9e2', // 背景色
			foreground: '#309286', // 前景色
			pdground: '#32dbc6', // 角标色
			icon: '', // 二维码图标
			iconsize: 40, // 二维码图标大小
			lv: 3, // 二维码容错级别 ， 一般不用设置，默认就行
			onval: false, // val值变化时自动重新生成二维码
			loadMake: true, 
      userAcID:'',
      signIN:false,
      value:0,
      userAc:{},
      userLeft:[],
      userRight:[],
      leftNum:0,
      rightNum:0,
      pkWith:0,
    };
  },

  async onLoad(option) {
     uni.showLoading({
		  	title: '加载中',
			mask:true
	  });
    this.acid = option.acid;
    this.id = option.id ? 0 : 1;
    if(option.mapId){
      this.mapid=option.mapId.toString()
      let a =await this.getAcM();
       this.getuserInfo();
    }else{
       this.getAcinfo();
      this.getuserInfo();
      this.getState();
      this.getAcUser()
    }
    this.getAuthorizeInfo();
  },
  onShow(){

  },
  methods: {
    //获取用户信息
    getuserInfo() {
      this.openid = wx.getStorageSync("openid");
      db.collection("userInfo")
        .where({
          _openid: this.openid,
        })
        .get()
        .then((res) => {
          this.userInfo = res.data[0];
        });
    },
    
    //获取活动信息
    getAcinfo() {
      db.collection("activity")
        .doc(this.acid)
        .get()
        .then((res) => {
          this.info = res.data;
          this.getorInfo();
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
    goMembers(){
      console.log("sdf")
       uni.navigateTo({
        url: `/pagesB/detail/allMembers?acid=${this.acid}`,
      });
    },
    getAcM(data){
       db.collection("activity")
        .where({
          mapid:this.mapid
        })
        .get()
        .then((res) => {
          this.info = res.data[0];
          this.acid=res.data[0]._id;
          this.getorInfo();
          this.getState()
        });
    },
    //获取用户报名状态
    getState() {
      db.collection("userActivity")
        .where({
          _openid: this.openid,
          activityID: this.acid,
        })
        .get()
        .then((res) => {
          if (res.data.length) {
            this.userAc=res.data[0];
            this.userAcID=res.data[0]._id;
            this.val=res.data[0]._id;
            this.state = 2;
            this.signIN=res.data[0].signIN;
             if(this.userAc.passTime){
             }
          }
        });
    },
    //获取组织信息
    getorInfo() {
      db.collection("organization")
        .doc(this.info.orID)
        .get()
        .then((res) => {
          this.orinfo = res.data;
        })
        .catch((err) => {
          console.log("错误信息", err);
        });
    },
    //导航
     mapFun() {
      uni.openLocation({
        latitude: this.info.latitude, //纬度
        longitude: this.info.longitude, //经度
        name: this.info.addressName,
        address: this.info.address,
      });
    },
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
    getLocationInfo() {
            const that = this;
            uni.hideLoading();
            
      uni.getLocation({
        type: "wgs84",
        success(res) {
          console.log(res);
          that.latitude=res.latitude;
          that.longitude=res.longitude;
         that.getDistance()
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
          let that=this;
          let hw = res.data.result.elements[0].distance; //拿到距离(米)
          this.goTime=(res.data.result.elements[0].duration/60).toFixed(0);
           this.CreateCode();
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
        },
      });
    },
    goStime() {
      uni.navigateTo({
        url: `../service-time/service-time?acid=${this.info._id}`,
      });
    },
    goFvolunteer(){
      uni.navigateTo({
        url: `/pagesA/volunteer-demeanour/releaseVolunteer?acID=${this.info._id}&orID=${this.orinfo._id}&type=${this.info.species}`,
      });
    },
    goMail() {
      uni.navigateTo({
        url: `../mail-reply/mail-reply?acID=${this.acid}`,
      });
    },
    getCode(){
      this.show=true;
    },
    getCode2(data){
      this.val=data
      this.show=true;
    },
    goSuccess() {
      if (!uni.getStorageSync("login")) {
        uni.showToast({ title: "请登录", icon: "none" });
        setTimeout(() => {
          uni.navigateTo({
            url: "/pagesA/sign/sign?id=1",
          });
        }, 700);
        return;
      }
      if(this.info.allNumber==this.info.userNumber){
        uni.showToast({ title: "报名人数已满", icon: "none" });
        return;
      }
      if(!this.info.show){
         uni.showToast({ title: "活动已结束，不可报名", icon: "none" });
        return;
      }
      this.$store.commit("change");
      delete this.info._id;
      delete this.info._openid;
      this.info.userNumber++;
      db.collection("activity")
        .doc(this.acid)
        .update({
          data: this.info,
        })
        .then((res) => {
        });
       db.collection('organization').doc(this.info.orID).update({
         data:{voNumber:_.inc(1)}
       })
      db.collection("userActivity")
        .add({
          data: {
            activityID: this.acid,
            orID:this.info.orID,
            name: this.userInfo.user_name,
            image: this.userInfo.user_img,
            signIN:false,
            serviceTime:0
          },
        })
        .then((res) => {
          uni.navigateTo({
            url: "../../pagesA/signUp/active-success",
          });
        });
    },
    changeTime(){
      if(this.userAc.passTime){
        uni.showToast({ title: "服务时长已通过审核，无需填写", icon: "none" });
        return;
      }
      this.Tshow=true;
    },
    toggleNav(id) {
      this.navId = id;
    },
    showStates() {
      this.showState = true;
    },
    eshowStates() {
      this.eshow = true;
    },
    change(index) {
      this.current = index;
    },
    //扫码处理
    scan() {
      const info=this.info
      const id=this.userAcID
      wx.scanCode({
        success:(res) => {
          console.log("扫码结果222",res);
          console.log("that.info",info)
           if(res.result==info._id){
              db.collection('userActivity').doc(id).update({
            data:{
            signIN:true
           }
            }).then(res=>{
            uni.showToast({title:'签到成功',icon:'none'});
            this.signIN=true
          })
           }else{
             uni.showToast({title:'选择签到活动有误，请重新选择',icon:'none'})
           }
         
        },
      });
    },
    //去活动编辑页
    goActivity(){
      uni.navigateTo({
        url: `../f-active/f-active?acID=${this.info._id}`,
      });
    },
    //删除活动
    confirm(){
      db.collection('activity').doc(this.info._id).remove().then(res=>{
         uni.showToast({title:'删除成功',icon:'none'})
          setTimeout(()=>{
            uni.redirectTo({
            url: '../or-home/or-home',
          });
          },400)
      })
      console.log("删除活动")
    },
    checkNumber(mobile){
       return RegExp(/^[0-9]+.?[0-9]*$/).test(mobile);
    },
    serverTime(){
      if(!this.checkNumber(this.value)){
          uni.showToast({ title: "填写失败，请输入数字", icon: "none" });
          return ;
      }else{
        db.collection('userActivity').doc(this.userAcID).update({
          data:{
            serviceTime:Number(this.value)
          }
        }).then(res=>{
             uni.showToast({ title: "填写成功", icon: "none" });
        })
        
      }
      
    },
    //保存志愿证书
    saveImg(){
       if(this.userAc.passTime){   
        this.savaNotice();
      }else{
        uni.showToast({ title: "暂时不可下载证书，请等待管理员确认服务时长", icon: "none" });
        return;
      } 
    },
    //保存画布图片
	  savaNotice(ctx) {
			const that = this
			uni.showLoading()
			const timeId = setTimeout(() => {
				//生成图片
				uni.canvasToTempFilePath({
					canvasId: 'canvas',
					fileType: 'jpg',
					quality: 1,
					success(res) {
						uni.hideLoading()
						// 保存到相册
						uni.saveImageToPhotosAlbum({
							filePath: res.tempFilePath,
							success(data) {
								console.log("保存成功")
							},
							fail(err) {
								if (err.errMsg == 'saveImageToPhotosAlbum:fail auth deny') {
									that.showOpenstting = true
								}
							}
						})
					},
					fail(err) {
						console.log(err)
						uni.hideLoading()
					}
				}, that)
			}, 500)
			this.ctx.draw(true, timeId)
		},
   async CreateCode() {
			const bguri = 'https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/%E5%BF%97%E6%84%BF%E8%AF%81%E4%B9%A6/Frame%2021728.png?sign=9d9948b43379927802ebc5caafa85769&t=1659667629'
			const bgimgInfo = await uni.downloadFile({ url: bguri })
			const bgUri = bgimgInfo[1].tempFilePath
			const ctx = uni.createCanvasContext('canvas', this)
			this.ctx = ctx
		ctx.setFillStyle('#fff')
		ctx.drawImage(bgUri, 0, 0, 750, 490)
		ctx.font="20px Arial";
    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
		ctx.fillStyle = "rgba(0, 0, 0, 1)";
    ctx.fillText(this.orinfo.group_name,375,36);
		ctx.font="bold 32px Arial";
    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
		ctx.fillStyle = "rgba(69, 117, 246, 1)";
    ctx.fillText(`${this.info.species}志愿者服务记录证明`,375,84);
		ctx.textAlign = "start"	
		//名称
		ctx.font="20px Arial";
		ctx.textBaseline = "middle"
		ctx.fillStyle = "rgba(153, 153, 153, 1)";
    ctx.fillText("志愿者:",276,184);
		ctx.fillText("志愿者ID:",276,228);
		ctx.fillText("服务时长:",276,272);
		ctx.fillText("开具日期:",276,316);

		ctx.font="20px Arial";
		ctx.textBaseline = "middle"
		ctx.fillStyle = "rgba(51, 51, 51, 1)";
    ctx.fillText(this.userInfo.user_name,383,184);
    var disLength = this.userInfo._openid.length;
		var id=this.userInfo._openid.substring(disLength-5,disLength);
		ctx.fillText(id,383,228);
		ctx.fillText(this.userAc.serviceTime,383,272);

    let time =this.$u.timeFormat(new Date(), 'yyyy-mm-dd');
	  ctx.fillText(time,383,316);
		
 
		//感谢
		ctx.font="16px Arial";
    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
		ctx.fillStyle = "rgba(153, 153, 153, 1)";
    
    ctx.fillText("感谢您的积极参与，特发此证，以致感谢!",375,438);
		},
    //获取活动用户列表
    getAcUser(){
      db.collection('userActivity').where({
        activityID:this.acid
      }).get().then(res=>{
        this.ActivityUser=res.data
        for(let i =1;i<=this.ActivityUser.length;i++){
          if(i%2){
            i<9?this.userLeft.push(this.ActivityUser[i-1]):0
            this.leftNum+=this.ActivityUser[i-1].serviceTime
          }else{
            i<9?this.userRight.push(this.ActivityUser[i-1]):0
            this.rightNum+=this.ActivityUser[i-1].serviceTime;
          }
        }
        if(this.rightNum==this.leftNum){
          this.pkWith=50
        }else{
          let number=this.rightNum+ this.leftNum;
          let pk =Math.trunc(this.leftNum/number*100)    
          this.pkWith=pk<15?15:pk>85?85:pk
          console.log("大手笔",this.pkWith)
        }
   
        
      })
    },
  },
};
</script>
<style scoped lang="scss">
.page-bg {
  position: relative;
  background-color: #f2f2f2;
  min-height: 100vh;
  width: 100%;
}

.box {
  width: 100%;
  height: 16rpx;
  background: #f2f2f2;
}

.t1 {
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
  margin: -50rpx 32rpx 32rpx 32rpx;
  background-color: #fff;
  border-radius: 16rpx;
  display: flex;
  flex-direction: column;

  .head {
    display: flex;

    .n-info {
      display: flex;
      align-items:center;
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
  margin: 32rpx;
  padding: 16rpx 24rpx 22rpx 24rpx;
  background: #fff;
  border-radius: 16rpx;
  height: 168rpx;

  .top {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-radius: 24rpx;
  }

  .bom {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .a-t1 {
    font-size: 32rpx;
    font-weight: 600;
    opacity: 0.8;
  }

  .a-t2 {
    font-size: 28rpx;
    font-weight: 600;
    color: #333333;
    opacity: 0.8;
  }
}

.bt {
  position: fixed;
  width: 100%;
  display: flex;
  align-items: center;
  bottom: 92upx;
}

.btn {
  color: white;
  margin-top: 198rpx;
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

.bom-btn {
  margin: 32rpx 32rpx 0 32rpx;
  padding: 16rpx 24rpx 22rpx 24rpx;
  background: #fff;
  border-radius: 16rpx;
  height: 80rpx;
  display: flex;
  justify-content: space-between;

  .bt1 {
    display: flex;
    padding: 12rpx 10rpx 12rpx 10rpx;
    font-size: 32rpx;
    justify-content: center;
    align-items: center;
    width: 160rpx;
    height: 56rpx;
    background: white;
    color: #333333;
    border-radius: 120rpx;
  }

  .ac {
    background: #4575f6;
    color: white;
  }
}

.cs1 {
  background: #fff;
  margin-left: 32rpx;
  margin-right: 32rpx;
  padding-bottom: 200rpx !important;
  padding: 24rpx;
}

.introduce {
  opacity: 0.8;
  font-size: 28rpx;
}

.team {
  display: flex;
  flex-direction: column;
  .noneTeam{
    justify-content: center;
    align-items: center;
    height: 200rpx;
    opacity:0.3;
  }
  .pk{
    width: 638rpx;
    height: 62rpx;
    display: flex;
    color:#ffff;
    line-height: 62rpx;
    justify-content: space-between;
    background: linear-gradient(270deg, #EB626A 0%, #FFC1F9 100%);
  border-radius: 100px 100px 100px 100px;
	.leftPk{
	position: relative;
	display: flex;
	justify-content: space-between;
	height: 62rpx;
	border-radius: 100px 0 0 100px;
	background: linear-gradient(270deg, #A965FF 0%, #5D6EFD 58%, #4575F6 100%);
}
.impact{
	position: absolute;
	right: -20px;
}
} 
  .t-top {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

.t-top-bt{
  position: relative;
  padding-left:12rpx;
}
  .group-name{
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 152rpx;
    height: 52prx;
    color: #fff;
    font-size: 28rpx;
    font-weight: 600;
    border-radius: 112rpx;
    bottom:0;
    left:0;
  }
  .group1{
    background: #4575F6;
  }
  .group2{
    background: #EB626A;
  }

  .t-msg {
    display: flex;
    justify-content: flex-end;

    .msg-t1 {
      border-bottom: 1px solid #4575f6;
      font-size: 24rpx;
      font-weight: 600;
      color: #4575f6;
    }
  }

  .Tname {
    display: flex;

    .T-content {
      display: flex;
      flex-direction: column;
      align-items: center;

      .t-name {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 224rpx;
        height: 76rpx;
        color: #fff;
        font-size: 32rpx;
        font-weight: 600;
        border-radius: 112rpx;
      }

      .tn1 {
        background: #4575f6;
      }

      .tn2 {
        background: #eb626a;
      }

      .players {
        margin-top:24rpx;
        padding: 0rpx 0 24rpx 28rpx;
        width: 310rpx;
        border-radius: 16rpx; 
          .captain{
            position:relative;
            padding-left:4rpx;
          }
          .one{
            position: absolute;
             display: flex;
             align-items: center;
             justify-content: center;
             width: 72rpx;
             height: 28rpx;           
             color: #fff;
             font-size: 20rpx;
             font-weight: 600;
             border-radius: 96rpx;
             bottom:-10rpx;
             left:0;
          }
          .color1{
            background: #4575F6;
          }
          .color2{
            background: #EB626A;
          }
      }

      .p-bg1 {
        background: rgba(69, 117, 246, 0.1);
      }

      .p-bg2 {
        background: rgba(235, 98, 106, 0.1);
      }

      .pl-msg {
        padding-left: 20rpx;
        font-size: 28rpx;
        font-weight: 600;
        color: #333333;
      }
    }
  }
}

.mail {
  display: flex;
  align-items: center;

  .m-msg1 {
    font-size: 32rpx;
    font-weight: 600;
    color: #333333;
  }

  .m-msg2 {
    font-size: 28rpx;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: #000000;
    opacity: 0.4;
  }
}

.bm {
  position: fixed;
  align-items: center;
  bottom: 0;
  width: 100vw;
  padding-top: 30rpx;
  padding-left: 96rpx;
  padding-right: 96rpx;
  padding-bottom:80rpx;
  background: #ffffff;
}
.bt2 {
  display: flex;
  padding: 16rpx 60rpx 16rpx 60rpx;
  font-size: 32rpx;
  justify-content: center;
  align-items: center;
  width: 370rpx;
  height: 80rpx;
  background: #4575f6;
  color: white;
  border-radius: 120rpx;
}
.code{
     display: flex;
    flex-direction: column;
    align-items: center;
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
.popup{
  display:flex;
  align-items: center;
  margin-top:100rpx;
  height:400rpx;
}
</style>
