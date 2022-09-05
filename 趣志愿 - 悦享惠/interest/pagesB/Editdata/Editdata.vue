<template>
  <view>
    <u-navbar
      back-icon-size="48"
      :border-bottom="false"
      title="编辑资料"
      title-color="white"
      :is-back="true"
      back-icon-color="white"
      :background="background"
    >
    </u-navbar>
    <view class="form">
      <view class="item">
        <view class="tx1">用户名</view>
        <view class="input">
          <u-input
            input-align="right"
            v-model="info.account"
            :border="border"
            style="width: 500rpx"
            maxlength="14"
            placeholder="以字母开头，由数字和字母组成"
         
          />
        </view>
      </view>
      <view class="item">
        <view class="tx1">密码</view>
        <view class="input">
          <u-input
            input-align="right"
            type="password"
            v-model="info.password"
            style="width: 500rpx"
            :border="border"
             maxlength="15"
            placeholder="必须包含大小写字母和数字"
          />
        </view>
      </view>
      <view class="item">
        <view class="tx1">确认密码</view>
        <view class="input">
          <u-input
            input-align="right"
            type="password"
            v-model="inpassword"
             style="width: 500rpx"
            :border="border"
            maxlength="15"
            placeholder="请再次输入密码"
          />
        </view>
      </view>
      <view class="item">
        <view class="tx1">电子邮箱</view>
        <view class="input">
          <u-input
            input-align="right"
            type="text"
            v-model="info.email"
            style="width: 500rpx"
            :border="border"
            placeholder="请输入邮箱"
          />
        </view>
      </view>
      <view class="item u-m-t-16">
        <view class="tx1">团体名称</view>
        <view class="input u-flex">
          <u-input
            input-align="right"
            type="text"
            v-model="info.group_name"
             style="width: 500rpx"
            maxlength="40"
            :border="border"
            placeholder="请输入团体名称"
          />
        </view>
      </view>
      <view class="box" @click="btnImage">
        <view class="btn u-flex">
          <u-icon name="plus" size="28"></u-icon>
          <view class="add">点击上传组织图片</view>
        </view>
      </view>
      <view class="item">
        <view class="tx1">登记状态</view>
        <view class="input u-flex">
          <u-input
            v-model="info.regStatus"
            input-align="right"
            style="width: 370rpx"
            placeholder="请选择登记状态"
            type="select"
            :border="border"
            @click="show = true"
          />
          <u-action-sheet :list="temaType" v-model="show" @click="activeType()">
          </u-action-sheet>
        </view>
      </view>
      <view class="item">
        <view class="tx1">联络团体</view>
        <view class="input u-flex">
          <u-input
            v-model="info.contactGroup"
            input-align="right"
            style="width: 370rpx"
            placeholder="请选择联络团体"
            type="select"
            :border="border"
            @click="Tshow = true"
          />
          <u-action-sheet
            :list="contactType"
            v-model="Tshow"
            @click="contactTy()"
          >
          </u-action-sheet>
        </view>
      </view>
      <view class="item">
        <view class="tx1">团体注册码</view>
        <view class="input u-flex">
          <u-input
            input-align="right"
            type="text"
            v-model="info.code"
            style="width: 500rpx"
            maxlength="15"
            :border="border"
            placeholder="请与联络团体沟通并申请团体注册码"
          />
        </view>
      </view>
      <view class="item">
        <view class="tx1">主管单位类型</view>
        <view class="input u-flex">
          <u-input
            v-model="info.type"
            input-align="right"
            style="width: 370rpx"
            placeholder="请选择主管单位类型"
            type="select"
            @click="typeShow = true"
          />
          <u-action-sheet
            :list="Utype"
            v-model="typeShow"
            @click="unitType"
          >
          </u-action-sheet>
        </view>
      </view>
      <view class="item">
        <view class="tx1">主管单位名称</view>
        <view class="input u-flex">
          <u-input
            input-align="right"
            type="text"
            v-model="info.name"
             style="width: 400rpx"
            maxlength="40"
            :border="border"
            placeholder="主管单位名称"
          />
        </view>
      </view>
      <view class="item">
        <view class="tx1">所在区域</view>
        <view class="input u-flex">
          <u-input
            v-model="info.area"
            input-align="right"
            style="width: 370rpx"
            placeholder="请选择所在区域"
            type="select"
            :border="border"
            @click="adShow = true"
          />
          		<u-picker mode="region" v-model="adShow" @confirm="regionConfirm"></u-picker>
          <!-- <u-select v-model="adShow" mode="mutil-column-auto" :list="list"  @confirm="confirm"></u-select> -->
        </view>
      </view>
      <view class="item">
        <view class="tx1">详细地址</view>
        <view class="input u-flex">
          <u-input
            v-model="info.address"
            input-align="right"
            style="width: 370rpx"
            placeholder="请使用地图定位"
            type="select"
            :border="border"
            @click="getAddress"
          />
          <u-action-sheet
            v-model="Sshow"
          >
          </u-action-sheet>
        </view>
      </view>
      <view class="item">
        <view class="tx1">服务类别</view>
        <view class="input u-flex">
          <u-input
             v-model="info.serviceCa"
            input-align="right"
            style="width: 370rpx"
            placeholder="请选择服务类别"
            type="select"
            @click="serShow = true"
          />
           <u-action-sheet
            :list="serviceType"
            v-model="serShow"
            @click="chooseSertype"
          >
          </u-action-sheet>
        </view>
      </view>
      <view class="item u-m-t-16">
        <view class="tx1">联系人姓名</view>
        <view class="input u-flex">
          <u-input
            input-align="right"
            type="text"
            v-model="info.contact"
            style="width: 400rpx"
            :border="border"
            placeholder="请输入真实姓名"
          />
        </view>
      </view>
      <view class="item">
        <view class="tx1">联系人电话</view>
        <view class="input u-flex">
          <u-input
            input-align="right"
            type="text"
            v-model="info.phone"
            style="width: 300rpx"
            :border="border"
            placeholder="请填写联系人电话"
          />
          <u-switch class="u-m-l-24" v-model="checked"></u-switch>
          <view class="public u-m-l-10">公开</view>
        </view>
      </view>
      <view class="item u-m-t-16">
        <view class="tx1">负责人验证码</view>
        <view class="input u-flex">
          <u-input
            input-align="right"
            type="text"
            v-model="phonecode"
            style="width: 265rpx"
            :border="border"
            placeholder="请输入负责人验证码"
          />
          <view class="wrap">
            <u-toast ref="uToast"></u-toast>
            <u-verification-code
              :keep-running="true"
              :seconds="seconds"
              @end="end"
              @start="start"
              ref="uCode"
              @change="codeChange"
            ></u-verification-code>
            <u-button @click="getCode" size="mini" type="primary">{{
              tips
            }}</u-button>
          </view>
        </view>
      </view>
      <view class="item">
        <view class="tx1">组织简介</view>
        <view class="input u-flex">
          <u-input v-model="info.value"  style="width: 400rpx" type="textarea" height="50" auto-height="true" />
        </view>
      </view>
    </view>
    <view class="u u-m-t-48">
     			<u-checkbox v-model="check" @change="checkboxChange"></u-checkbox>
       <text class="tx">已阅读并同意《用户协议》和《隐私政策》</text>
        
    </view>
    <button class="btn2" @click="goSuccess2()" v-if="id != 1">保存</button>
    <button class="btn2" @click="goSuccess2()" v-else>
      申请成为志愿者团体
    </button>
    <view style="height:40rpx;width:20rpx"></view>
  </view>
</template>

<script>
const db = wx.cloud.database();
export default {
  data() {
    return {
      tips: "",
      id: "",
      // refCode: null,
      seconds: 60,
      checked: false,
      navId: 1,
      agreement: false,
      show: false,
      Tshow: false,
      typeShow:false,
      adShow:false,
      value: false,
      check: false,
      serShow:false,
      inpassword:'',
      orid:'',
      oldAccount:'',
      chekcAC:false,
      randomNumber:0,
      phonecode:'',
      info: {
        mapid:0,
        account: "",
        password: "",
        email: "",
        group_name: "",
        image:'',//组织图片地址
        regStatus: "",//登记状态
        contactGroup: "",//联络团体
        code: "",
        type: "", //主管单位类型
        name: "", //主管单位名称
        area: "",
        address: "",
        serviceCa: "",
        contact: "",
        phone: "",
        audit:false,
        createTime:'',
        value:'',//组织介绍
        userNumber:1,
        addressName:'',
        latitude:'',
        longitude:'',
        voNumber:0,
        serviceTime:0
      },
      map:{
        id:0,
        latitude: 0, //纬度
        longitude: 0, //经度
        iconPath: "", //显示的图标
        rotate: 0, // 旋转度数
        width: 40, //宽
        height: 40, //高
        alpha: 1, //透明度
      },
      background: {
        backgroundColor: "#4575F6 ",
      },
      contactType: [
        {
          text: "长沙基金会",
        },
        {
          text: "长沙红十字会",
        },
      ],
      temaType: [
        {
          text: "未登记的志愿服务组织",
        },
        {
          text: "做民政部门登记的法人组织",
        },
        {
          text: "其他法人组织",
        },
      ],
      Utype:[
           {
          text: "政党机关",
        },
        {
          text: "教育事业单位",
        },
        {
          text: "国有企业",
        },
         {
          text: "非国有企业",
        },
         {
          text: "其他",
        },
      ],
      serviceType:[
         {
          text: "动物保护",
        },
        {
          text: "环境保护",
        },
        {
          text: "适老化",
        },
         {
          text: "在线志愿服务",
        },
         {
          text: "其他",
        },
      ],
    };
  },
  onLoad(option) {
    this.id = option.id;
    if(option.orid){
      this.orid=option.orid
       db.collection('organization').doc(this.orid).get().then(res=>{
        console.log("dsa",res)
        this.info=res.data;
        this.inpassword=res.data.password;
        this.oldAccount=res.data.account
        console.log("sdf",this.info)
      })
    }
   

    console.log("this.id", this.id);
  },
  methods: {
    getAddress(){
      console.log("sdsff");
    wx.chooseLocation({

			}).then(res=>{
        console.log("sd",res);
        this.info.address=res.address;
        this.info.addressName=res.name;
        this.info.latitude=res.latitude;
        this.info.longitude=res.longitude;
        this.map.id=Math.random();
        this.info.mapid=this.map.id.toString();
        this.map.latitude=res.latitude;
        this.map.longitude=res.longitude;
      })
    },
    btnImage() {
      wx.chooseImage({
        //添加一张图片
        count: 1,
        //在电脑上只会直接弹出选择图片，没有拍照功能
        sourceType: ["album", "camera"],
        success: (res) => {
          console.log(res);
          //创建一个上传给云储存的地址，每一张图片的储存名称不能一样，所以通过Math.random()方法获取一个不同的名称，该方法可以得到一个从0到1的一个double值
          const path = "images/" + Math.random() + ".png";
          //虚拟地址tempFilePaths[0];点击先把图片储存在一个虚拟地址，然后从虚拟地址把图片上传到云储存中。
          const virtualPath = res.tempFilePaths[0];

          //上传到云储存中

          wx.cloud.uploadFile({
            cloudPath: path,

            filePath: virtualPath,

            success: (res) => {
              uni.showToast({title: '上传成功',icon: 'none'});
                this.info.image=res.fileID;
                this.map.iconPath=res.fileID;
                console.log("dfsfsfs",this.info.image)
            },

            fail: (err) => {
                 uni.showToast({title: '上传失败，请重新上传',icon: 'none'});
            },
          });
        },

        fail: (err) => {
          console.log(err);
        },
      });
    },
    goSuccess() {
      wx.setStorage({
        key: "login",
        data: "true",
      });
      uni.navigateTo({
        url: "/pagesA/signUp/saveSccess",
      });
    },
    //校验用户名
    checkAccount(account){
      let time=(new Date()).toLocaleDateString();
       time =time.replace(/\//g,'-');
      this.info.createTime=time;
      return RegExp(/^[a-zA-Z][a-zA-Z0-9]{5,11}$/).test(account)
    },  
    //校验手机格式
    checkMobile(mobile){
    return RegExp(/^1[34578]\d{9}$/).test(mobile);
    },
    //校验邮箱格式
     checkEmail(email){
    return RegExp(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(email);
    },
    //密码校验
    checkPassworld(password){
      return RegExp(/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$/).test(password)
    },
   async goSuccess2() {
     
   
      if(!this.checkAccount(this.info.account)){
         uni.showToast({title: '用户名格式为6-14位,必须以字母开头，由数字和字母组成',icon: 'none'});
            return;
      }
      const chekcAC=false;
      const re=await db.collection('organization').where({
        account:this.info.account
      }).get().then(res=>{
        //判断用户名是否更改
         if(res.data.length&&res.data[0].account!=this.oldAccount) {
             const chekcAC=true
         }
      })
      if(chekcAC){
         uni.showToast({title: '用户名已经存在',icon: 'none'});
            return;
      }
      if(!this.checkPassworld(this.info.password)){
            uni.showToast({title: '密码格式为6-15位，必须有大小写字母和数字组成 ',icon: 'none'});
            return;
        }
    if(!(this.info.password==this.inpassword)){
        console.log("")
      uni.showToast({title: '两次密码输入不一致',icon: 'none'});
            return;
     }
      if(!this.checkEmail(this.info.email)){
           uni.showToast({title: '邮箱格式错误',icon: 'none'});
            return;
      }
      if(!this.info.group_name){
        console.log("sdfsdf",!this.info.group_name)
         uni.showToast({title: '请输入团体名称',icon: 'none'});
            return;
      }
      if(!this.info.image){
         uni.showToast({title: '请上传组织图片',icon: 'none'});
            return;
      }
      if(!this.info.regStatus){
         uni.showToast({title: '请选择登记状态',icon: 'none'});
            return;
      }
      if(!this.info.contactGroup){
         uni.showToast({title: '请选择联络团体',icon: 'none'});
            return;
      }
      if(!this.info.code){
         uni.showToast({title: '请输入团体注册码',icon: 'none'});
            return;
      }
      if(!this.info.type){
         uni.showToast({title: '请选择主管单位类型',icon: 'none'});
            return;
      }
      if(!this.info.name){
         uni.showToast({title: '请输入主管单位名称',icon: 'none'});
            return;
      }
      if(!this.info.area){
         uni.showToast({title: '请选择所在区域',icon: 'none'});
            return;
      }
      if(!this.info.address){
         uni.showToast({title: '请选择详细地址',icon: 'none'});
            return;
      }
      if(!this.info.serviceCa){
         uni.showToast({title: '请选择服务类类别',icon: 'none'});
            return;
      }
      if(!this.info.contact){
         uni.showToast({title: '请输入联系人姓名',icon: 'none'});
            return;
      }     
     if(!(this.randomNumber==Number(this.phonecode))){
          uni.showToast({title: '请输入正确的验证码',icon: 'none'});
            return;
      }
      if(!this.check){
         uni.showToast({title: '请勾选协议内容',icon: 'none'});
            return;
      }
      if(!this.checkMobile(this.info.phone)){
            uni.showToast({title: '手机号格式错误',icon: 'none'});
            return;
      }
    
      if(!this.info.value){
         uni.showToast({title: '请输入组织介绍',icon: 'none'});
            return;
      }     
      if(this.orid){
        delete this.info._id;
        delete this.info._openid;
        delete this.info.createTime;
        db.collection('organization').doc(this.orid).update({
        data:this.info
      }).then(res=>{
          uni.showToast({title: '保存成功',icon: 'none'});
        uni.navigateTo({
        url: "/pagesA/signUp/saveSccess",
      });
      })
      } else{
        db.collection('map').add({
          data:this.map
        }),
        db.collection('organization').add({
        data:this.info
        }).then(res=>{
      wx.setStorage({
        key: "Ologin",
        data: "true",
      });
          uni.showToast({title: '注册成功',icon: 'none'});
          console.log("res",res)  
            wx.setStorage({
        		      key: "or",
       		         data: res._id,
      	        	});
         db.collection("organization")
                .doc(res._id)
                .get()
                .then((res) => {
                  getApp().globalData.orInfo = res.data;
                });
        uni.redirectTo({
        url: `/pagesA/signUp/saveSccess?orid=${res._id}`,
      });
      })
      }
    
    
    },
    regionConfirm(e) {
			this.info.area = e.province.label + '-' + e.city.label + '-' + e.area.label;
		},
   	confirm(e) {
				console.log(e);
			},  
    chooseSertype(index){
     console.log("sb",this.serviceType[index].text)
      this.info.serviceCa = this.serviceType[index].text;
    },
    contactTy(index) {
      this.info.contactGroup = this.contactType[index].text;
    },
    checkboxChange(e) {
			this.agreement = e.value;
		},
    activeType(index) {
      this.info.regStatus = this.temaType[index].text;
    },
     unitType(index){
      this.info.type = this.Utype[index].text;
    },
    codeChange(text) {
      this.tips = text;
    },
    getCode() {
      if(!this.info.phone){
        this.$u.toast("请输入电话号号码");
        return;
      } else if(!this.$u.test.mobile(this.info.phone)){
          this.$u.toast("请输入正确手机号码");
          return;
      }
      if (this.$refs.uCode.canGetCode) {
        // 模拟向后端请求验证码
        this.send();
        setTimeout(() => {
          uni.hideLoading();
          // 这里此提示会被this.start()方法中的提示覆盖
          this.$u.toast("验证码已发送");
          // 通知验证码组件内部开始倒计时
          this.$refs.uCode.start();
        }, 500);
      } else {
        this.$u.toast("倒计时结束后再发送");
        
      }
    },
     send(){
      this.randomNumber= Math.random().toString(10).slice(2,6);
      console.log("dfd",this.randomNumber);
      wx.cloud.callFunction({
      name: 'sendSMS',
      data: {
        phone: this.info.phone,
        content:this.randomNumber
      }
    }).then(res => {
      console.log("成功", res)
    })
  },
    end() {
      this.$u.toast("倒计时结束");
    },
    start() {
    },
  },
};
</script>

<style scoped lang="scss">
.form {
  background-color: #f2f2f2;
  .box {
    display: flex;
    height: 108rpx;
    align-items: center;
    justify-content: center;
    border: 2rpx solid #333333;
    opacity: 0.6;
    border-radius: 8rpx;
  }
  .item {
    padding: 15rpx 32rpx 15rpx 32rpx;
    display: flex;
    background-color: white;
    align-items: center;
    border-bottom: 2rpx solid rgba(51, 51, 51, 0.1);
    justify-content: space-between;

    .input {
      display: flex;
    }

    .tx1 {
      font-size: 32rpx;
      font-weight: 600;
    }
  }
}

.btn2 {
  color: white;
  margin-top: 48rpx;
  width: 560rpx;
  height: 96rpx;
  border-radius: 56rpx;
  background: #4575f6;
  margin-bottom:40rpx;
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

.public {
  font-size: 32rpx;
  font-weight: 400;
  color: rgba(0, 0, 0, 0.26);
}

.wrap {
  padding-left: 24rpx;
}
.u {
  display: flex;
  justify-content: center;
  align-items: center;
  .tx {
    font-size: 24rpx;
    opacity: 0.6;
    font-weight: 400;
    color: #333333;
  }
}
</style>
