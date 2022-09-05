<template>
  <view>
    <view>
      <u-navbar
        :is-back="true"
        title="上传风采"
        back-icon-color="white"
        title-color="white"
        :background="background"
        :border-bottom="false"
      >
      </u-navbar>
    </view>
    <view class="box">
      <view class="fw600 fs32">上传照片</view>
      <u-upload
        ref="uUpload"
        :show-tips="false"
        :file-list="fileList"
        :action="action"
        :max-count="6"
        :before-upload="beforeUpload"
      ></u-upload>
      <view class="u-flex">
        <view class="fs28 fw600">标题:</view>
        <u-input
          class="u-m-l-20"
          v-model="info.title"
          style="width: 450rpx"
          :border="border"
          maxlength="25"
          placeholder="填写本次活动的标题吧"
          @on-remove="onRemove"
        />
      </view>
      <view class="fs28 fw600 u-m-t56">正文</view>
      <textarea
        class="inputText u-m-t-28 u-p-l-16 u-p-t-16"
        cols="30"
        rows="10"
        v-model="info.content"
        placeholder="不超过500个字"
      ></textarea>
    </view>
    <view class="bm">
      <button class="btn" @click="Gosuccess()">发布风采</button>
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
      lists: [],
      info: {
        userName: "",
        userImage: "",
        acID: "",
        img: [],
        likeNumber:0,
        title: "",
        content:'',
        share:0,
        orID:'',
        type:'',
      },
      length:0,
      openid:'',
      action: "https://cos.ap-shanghai.myqcloud.com;https://image-1306191496.cos.ap-nanjing.myqcloud.com;https://image.pinbar.vip;",
    };
  },
  onReady() {
    // 得到整个组件对象，内部图片列表变量为"lists"
    this.lists = this.$refs.uUpload.lists;
  },
  onLoad(option){
    this.info.orID=option.orID
    this.info.acID=option.acID;
    this.info.type=option.type;
    console.log("sb",this.info)
    this.openid = wx.getStorageSync("openid");
    this.getUserInfo();
  },
  methods: {
    upload() {
      this.$refs.uUpload.upload();
    },
    beforeUpload(index, list) {
      console.log("图片地址", this.lists[0].url);
      this.length=list.length
      const path = "volunteer/" + Math.random() + ".png";
      console.log("sdfsdfsdf", list);
      wx.cloud.uploadFile({
        cloudPath: path,
        filePath: list[index].url,
        success: (res) => {
          this.info.img.push(res.fileID);
          console.log("dfsfsfs", this.info.img);
        },
        fail: (err) => {
          console.log(err);
          uni.showToast({ title: "上传失败，请重新上传", icon: "none" });
        },
      });
    },
  	onRemove(index, lists) {
				console.log('图片已被移除')
		},
    getUserInfo(){
       db.collection("userInfo")
        .where({
          _openid: this.openid,
        })
        .get()
        .then((res) => {
          this.info.userName = res.data[0].user_name;
          this.info.userImage=res.data[0].user_img;
        });
    },
    Gosuccess() {
      if(!this.info.img.length){
        uni.showToast({ title: "请上传图片", icon: "none" });
         return ;
      }
      console.log("萨达法师打发",this.info.img.length)
      if(!this.info.title){
         uni.showToast({ title: "请输入标题", icon: "none" });
         return ;
      }
      if(!this.info.content){
         uni.showToast({ title: "请输入正文", icon: "none" });
         return ;
      }
      if(this.length!=this.info.img.length){
         uni.showToast({ title: "图片还在上传中,请等待", icon: "none" });
         rerurn;
      }
      db.collection("volunteerShow")
        .add({
          data: this.info,
        })
        .then((res) => {
          console.log("sdfs", res);
          uni.redirectTo({
        url: `/pagesA/volunteer-demeanour/volunteer-demeanour?id=${res._id}`,
      });
        });
   
    },
  },
};
</script>
<style lang="scss" scoped>
.box {
  margin: 32rpx;
  display: flex;
  flex-direction: column;
}
.inputText {
  background: #f8f8f8;
  width: auto;
}
.btn {
  color: white;
  margin-top: 198rpx;
  width: 560rpx;
  height: 96rpx;
  border-radius: 56rpx;
  background: #4575f6;
}
</style>