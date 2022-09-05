<template>
  <view>
    <u-navbar
      back-icon-size="48"
      :border-bottom="false"
      title="发布活动"
      title-color="white"
      :is-back="true"
      back-icon-color="white"
      :background="background"
    >
    </u-navbar>
    <view class="form">
      <view class="item">
        <view class="tx1">活动名称 <text class="tx2">*</text></view>
        <view class="input">
          <u-input
            input-align="right"
            v-model="info.name"
            style="width: 450rpx"
            :border="border"
            maxlength="20"
            placeholder="请输入不超过20个字"
          />
        </view>
      </view>
      <view class="item">
        <view class="tx1">活动种类<text class="tx2">*</text></view>
        <view class="input" @click="show = true">
          <u-input
            input-align="right"
            v-model="info.type"
            style="width: 250rpx"
            placeholder="请选择活动种类"
            type="select"
            :border="border"
            @click="show = true"
          />
          <u-action-sheet :list="acList" v-model="show" @click="activeType">
          </u-action-sheet>
        </view>
      </view>
      <view class="item">
        <view class="tx1">活动类型<text class="tx2">*</text></view>
        <view class="input">
          <u-input
            input-align="right"
            v-model="info.species"
            style="width: 250rpx"
            placeholder="请选择活动类型"
            type="select"
            @click="Lshow = true"
          />
          <u-action-sheet
            :list="acSpecies"
            v-model="Lshow"
            @click="activeSpecies"
          >
          </u-action-sheet>
        </view>
      </view>
      <view class="item">
        <view class="tx1">活动地址<text class="tx2">*</text></view>
        <view class="input">
          <u-input
            input-align="right"
            style="width: 450rpx"
            v-model="info.address"
            placeholder="请选择活动地址"
            type="select"
            @click="getAddress"
          />
        </view>
      </view>
      <view class="item">
        <view class="tx1">开始时间<text class="tx2">*</text></view>
        <view class="input">
          <u-input
            v-model="info.time"
            input-align="right"
            style="width: 450rpx"
            placeholder="点击选择时间"
            type="select"
            @click="dataShow = true"
          />
          <u-picker
            mode="time"
            v-model="dataShow"
            @confirm="confirm"
            :params="params"
          ></u-picker>
          <u-action-sheet> </u-action-sheet>
        </view>
      </view>
      <view class="item">
        <view class="tx1">服务时长(小时) <text class="tx2">*</text></view>
        <view class="input">
          <u-input
            input-align="right"
            v-model="info.serverTime"
            style="width: 450rpx"
            :border="border"
            maxlength="2"
            placeholder="请输入参与本次活动的预计服务时长"
          />
        </view>
      </view>
      <view class="item">
        <view class="tx1">计划招募 <text class="tx2">*</text></view>
        <view class="input">
          <u-input
            input-align="right"
            maxlength="4"
            v-model="info.allNumber"
            style="width: 350rpx"
            :border="border"
            placeholder="请输入最高招募志愿者数量"
          />
        </view>
      </view>
      <view class="text-item u-m-t-32">
        <view class="tx1">活动计划 <text class="tx2">*</text></view>
        <view class="input u-m-r-24">
          <textarea
            style="width: 686rpx"
            class="inputText u-m-t-24"
            cols="30"
            rows="10"
            maxlength="300"
            v-model="info.introduce"
            placeholder="请输入活动详情、招募志愿者岗位、活动亮点等（不超过500个字）"
          ></textarea>
        </view>
      </view>
      <view class="text-item">
        <view class="tx1 u-m-t-48">活动封面 <text class="tx2">*</text></view>
         <u-upload
        ref="uUpload"
        :show-tips="false"
        :file-list="fileList"
        :action="action"
        :max-count="1"
        :before-upload="beforeUpload"
      ></u-upload>
      </view>
      <view class="item u-m-t-20" style="border:0px">
        <view class="tx1">联系电话 <text class="tx2">*</text></view>
        <view class="input">
          <u-input
            input-align="right"
            v-model="info.phone"
            style="width: 450rpx"
            :border="border"
            maxlength="15"
            placeholder="请输入参与本次活动联系人电话"
          />
        </view>
      </view>
      <view class="sign u-m-t-24">
        <view class="tx1">志愿者报名</view>
        <view class="right">
          <view :class="{ active: navId == 1 }" class="bt" @click="toggleNav(1)"
            ><text>实名认证</text></view
          >
          <view
            :class="{ active: navId == 2 }"
            class="bt u-m-l-16"
            @click="toggleNav(2)"
            ><text>无需实名</text></view
          >
        </view>
      </view>
      <view class="text-item u-m-t-40">
        <view class="tx1">其他备注 </view>
        <view class="input u-m-r-24">
           <textarea
            style="width: 686rpx"
            class="beizu u-m-t-24"
            cols="30"
            rows="10"
            maxlength="40"
            placeholder="其他活动规则或者会议链接"
          ></textarea>
        </view>
      </view>
      <button class="btn2" @click="goSuccess()">{{btnVal}}</button>
    </view>
  </view>
</template>

<script>
const db = wx.cloud.database();
export default {
  data() {
    return {
      show: false,
      Lshow: false,
      dataShow: false,
      navId: 2,
      value: "",
      btnVal:'确认发布',
      action: "http://www.example.com",
      params: {
        year: true,
        month: true,
        day: true,
        hour: true,
        minute: true,
        second: false,
      },
      info: {
        mapid:0,
        name: "",
        type: "",
        species: "",
        address: "",
        time: "",
        serverTime: '',
        allNumber: "",
        introduce: "",
        image: "",
        authentication: false,
        note: "",
        orID: "",
        userNumber:0,
        show:true,
        addressName:'',
        latitude:'',
        longitude:'',
        creatTime:0,
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
      padding: "paddin-left:50rpx",
      background: {
        backgroundColor: "#4575F6 ",
      },
      acList: [
        {
          text: "线上活动",
        },
        {
          text: "线下活动",
        },
      ],
      acSpecies: [
        {
          text: "动物保护活动",
        },
        {
          text: "教育活动",
        },
        {
          text: "环保活动",
        },
      ],
      imageType:[
        "/static/img/map/animal.png",
        "/static/img/map/edu.png",
        "/static/img/map/leaves.png"
      ]
    };
  },
  onLoad(option) {
    this.info.orID = option.orid;
    if(option.acID){
      db.collection('activity').doc(option.acID).get().then(res=>{
        this.info=res.data;
        this.btnVal="保存"
        console.log("活动id",res.data)
      })
    }
    console.log("sdfsd", this.info.orID);
  },
  onReady() {
    // 得到整个组件对象，内部图片列表变量为"lists"
    this.lists = this.$refs.uUpload.lists;
  },
  methods: {
    checkMobile(mobile) {
      return RegExp(/^1[34578]\d{9}$/).test(mobile);
    },
     beforeUpload(index, list) {
      console.log("图片地址", this.lists[0].url);
      this.length=list.length
      const path = "activity/" + Math.random() + ".png";
      console.log("sdfsdfsdf", list);
      wx.cloud.uploadFile({
        cloudPath: path,
        filePath: list[index].url,
        success: (res) => {
          this.info.image=res.fileID
        },
        fail: (err) => {
          console.log(err);
          uni.showToast({ title: "上传失败，请重新上传", icon: "none" });
        },
      });
    },
    goSuccess() {
      if (!this.info.name) {
        uni.showToast({ title: "请输入活动名称", icon: "none" });
        return;
      }
      if (!this.info.type) {
        uni.showToast({ title: "请选择活动种类", icon: "none" });
        return;
      }
      if (!this.info.species) {
        uni.showToast({ title: "请选择活动类型", icon: "none" });
        return;
      }
      if (!this.info.address) {
        uni.showToast({ title: "请选择活动地址", icon: "none" });
        return;
      }
      if (!this.info.time) {
        uni.showToast({ title: "请选择活动开始时间", icon: "none" });
        return;
      }
      if(new Date().getTime()>new Date(this.info.time).getTime()){
        uni.showToast({ title: "请选择正确的活动开始日期", icon: "none" });
        return;
      }
      if (!this.info.serverTime) {
        uni.showToast({ title: "请输入服务时长", icon: "none" });
        return;
      }
      if (!this.info.allNumber) {
        uni.showToast({ title: "请输入计划招募人数", icon: "none" });
        return;
      }
      if (!this.info.allNumber) {
        uni.showToast({ title: "请输入计划招募人数", icon: "none" });
        return;
      }
      if (!this.info.introduce) {
        uni.showToast({ title: "请输入活动计划", icon: "none" });
        return;
      }
      if (!this.info.image) {
        uni.showToast({ title: "请上传活动封面", icon: "none" });
        return;
      }
      if (!this.checkMobile(this.info.phone)) {
        uni.showToast({ title: "手机号格式错误", icon: "none" });
        return;
      }
      if(this.btnVal=='保存'){
        let id=this.info._id;
        delete this.info._id;
        delete this.info._openid;
        db.collection('activity').doc(id).update({
          data:this.info
        }).then(res=>{
          uni.showToast({title:'保存成功',icon:'none'})
          setTimeout(()=>{
            uni.redirectTo({
            url: '../or-home/or-home',
          });
          },400)
        })
          console.log("保存");

      }else{
        db.collection('map').add({
          data:this.map
        }),
           db.collection("activity")
        .add({
          data: this.info,
        })
        .then((res) => {
          console.log("sdfs", res);
          uni.redirectTo({
            url: `/pagesA/signUp/release?acid=${res._id}`,
          });
        });
      }
   
    },
    activeType(index) {
      this.info.type = this.acList[index].text;
    },
    activeSpecies(index) {
      this.info.species = this.acSpecies[index].text;
      this.map.iconPath=this.imageType[index];
    },
    toggleNav(id) {
      if (id == 1) {
        this.authentication = true;
      }
      this.navId = id;
      this.aState = false;
    },
    confirm(e) {
      console.log(e);
      this.info.time =
        e.year +
        "-" +
        e.month +
        "-" +
        e.day +
        " " +
        e.hour +
        ":" +
        e.minute +
        ":00";
        this.info.creatTime=Date.now();
    },
    getAddress() {
      console.log("sdsff");
      wx.chooseLocation({}).then((res) => {
        console.log("sd", res);
        this.info.address = res.address;
        this.info.addressName=res.name;
        this.info.latitude=res.latitude;
        this.info.longitude=res.longitude;
        this.map.id=Math.random()+1;
        this.info.mapid=this.map.id.toString();
        this.map.latitude=res.latitude;
        this.map.longitude=res.longitude;
      });
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
              uni.showToast({ title: "上传成功", icon: "none" });
              this.info.image = res.fileID;
              // this.map.iconPath=res.fileID;
              console.log("dfsfsfs", this.info.image);
            },

            fail: (err) => {
              console.log(err)
              uni.showToast({ title: "上传失败，请重新上传", icon: "none" });
            },
          });
        },

        fail: (err) => {
          console.log(err);
        },
      });
    },
    radioChange(e) {
      // console.log(e);
    },
    // 选中任一radio时，由radio-group触发
    radioGroupChange(e) {
      // console.log(e);
    },
  },
};
</script>

<style scoped lang="scss">
.form {
  padding-bottom: 50rpx;
  .item {
    padding: 0 32rpx 0 32rpx;
    height: 96rpx;
    display: flex;
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

    .tx2 {
      color: #ff0000;
    }
  }

  .text-item {
    padding: 0 32rpx 0 32rpx;
    justify-content: space-between;
  }

  .top {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .text-item {
    .tx1 {
      font-size: 32rpx;
      font-weight: 600;
    }

    .tx2 {
      color: #ff0000;
    }

    .box {
      display: flex;
      width: 686rpx;
      height: 108rpx;
      align-items: center;
      justify-content: center;
      border: 2rpx solid #333333;
      opacity: 0.6;
      border-radius: 8rpx;
    }
  }
.beizu{
  height: 92rpx;
  background-color: #f2f2f2;
  padding-top:24rpx;
  padding-left:24rpx;
  border-radius: 16rpx;
}
  .inputText {
    height: 260rpx;
    background-color: #f2f2f2;
    padding-top:27rpx;
    padding-left:24rpx;
    line-height: 44rpx;
    border-radius: 16rpx;
  }
}
.sign {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32rpx 0 32rpx;
  .tx1 {
    font-size: 32rpx;
    font-weight: 600;
  }
  .right {
    display: flex;
    .bt {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 160rpx;
      height: 56rpx;
      color: #4575f6;
      border: 1px solid #4575f6;
      background: white;
      border-radius: 120rpx;
    }
    .active {
      background: #4575f6;
      color: white;
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
  margin-bottom: 50rpx;
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
