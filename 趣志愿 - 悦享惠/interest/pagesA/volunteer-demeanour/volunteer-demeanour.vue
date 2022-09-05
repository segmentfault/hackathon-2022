<template>
  <view class="box">
    <u-navbar back-icon-size="48" :border-bottom="false" title="志愿风采" title-color="white" :is-back="true"
			back-icon-color="white" :background="background"> </u-navbar>
    <view class="content">
      <view class="swiper">
        <u-swiper
          :list="info.img"
          :img-mode="widthFix"
          :height="height"
          mode="number"
          indicator-pos="bottomRight"
        ></u-swiper>
      </view>
      <view class="c-t1 u-m-t-40 fs32 fw600">{{ info.title }}</view>
      <view class="c-t2 u-m-t-24 fs28 fw400">{{ info.content }}</view>
      <view class="c-t1 u-m-t-40 fw700 fs32">评论区</view>
      <view class="comments u-m-t-24 u-m-b-80 u-p-b-50">
        <view
          class="mess u-m-b-20"
          v-for="(item, index) in comInfo"
          :key="index"
        >
          <view class="userImg">
            <u-image
              height="88rpx"
              border-radius="90"
              width="88rpx"
              :src="item.userImage"
            ></u-image>
          </view>
          <view class="message u-m-l-16">
            <view class="userName fw700 fs28">{{ item.userName }}</view>
            <view class="time fs22">{{ item.time }}</view>
            <view class="fs28 u-m-t-10">{{ item.value }}</view>
          </view>
        </view>
      </view>
    </view>
    <view class="fixedBom u-flex">
      <view class="iconbox">
        <image
          @click="cancelPraise()"
          v-if="praise"
          src="../../static/love1.png"
          mode="widthFix"
        ></image>
        <image
          @click="Praise()"
          v-else
          src="../../static/love2.png"
          mode="widthFix"
        ></image>
        <text class="u-line-1">{{ info.likeNumber }}</text>
      </view>
      <view class="iconbox" @click="show1(true)">
        <image src="../../static/huifu.png" mode="widthFix"></image>
        <text class="u-line-1">{{ comInfo.length }}</text>
      </view>
      <button open-type="share" class="u-reset-button iconbox">
        <image src="../../static/fx.png" mode="widthFix"></image>
        <text class="u-line-1">{{ info.share }}</text>
      </button>

      <view class="u-flex-1 u-margin-left-20" style="overflow: hidden">
        <view class="disIput u-flex u-col-center" @click="show1()">
          <image src="../../static/disicon.png" mode="widthFix"></image>
          <text>快说点什么...</text>
        </view>
      </view>
    </view>
    <view class="showModel" v-if="show" @click.stop="modelShow">
      <view class="comment u-flex u-col-center u-margin-right-24" @click.stop>
        <textarea
          :cursor-spacing="20"
          placeholder="最多输入140字..."
          :focus="true"
          placeholder-class="placsPlace"
          :show-confirm-bar="false"
          class="u-flex-1 inputs"
          style="height: 80upx"
          v-model="review.value"
        />
        <view class="btn" :class="isTrue ? 'bg2' : 'bg1'" @click="okValue"
          >发布</view
        >
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
      info: {},
      id: "",
      show: false,
      value: "",
      review: {
        userName: "",
        userImage: "",
        value: "",
        time: "",
        voId: "",
        creatTime: 0,
      },
      comInfo: {},
      userInof: {},
      praise: false,
      height: 0,
    };
  },
  onLoad(option) {
    console.log("dsfs", option);
    this.id = option.id;
    this.review.voId = option.id;
    console.log("this.id", this.id);
    this.openid = wx.getStorageSync("openid");
    this.getInfo();
    this.getUserInfo();
    this.message();
    this.getheight();
  },
  methods: {
    getheight() {
      uni.getSystemInfo({
        success: (res) => {
          console.log(res.screenHeight); //屏幕高度  注意这里获得的高度宽度都是px 需要转换rpx
          console.log(res.windowWidth); //可使用窗口宽度
          console.log(res.windowHeight); //可使用窗口高度
          console.log(res.screenWidth); //屏幕宽度
          this.height = res.screenHeight * (750 / res.windowWidth) * 0.55; //将px 转换rpx
          console.log("rpx*********", this.height);
        },
      });
    },
    message() {
      db.collection("comments")
        .where({
          voId: this.id,
        })
        .orderBy("creatTime", "desc")
        .get()
        .then((res) => {
          console.log("评论", res);
          this.comInfo = res.data;
        });
    },
    getInfo() {
      db.collection("volunteerShow")
        .doc(this.id)
        .get()
        .then((res) => {
          this.info = res.data;
        });
    },
    //点赞
    Praise() {
      this.praise = true;
      db.collection("volunteerShow")
        .doc(this.id)
        .update({
          data: {
            likeNumber: ++this.info.likeNumber,
          },
        });
    },
    onShareAppMessage(res) {
      // 来自页面内分享按钮
      console.log("dsfsd");
      db.collection("volunteerShow")
        .doc(this.id)
        .update({
          data: {
            share: ++this.info.share,
          },
        });
    },
    //取消点赞
    cancelPraise() {
      this.praise = false;
      db.collection("volunteerShow")
        .doc(this.id)
        .update({
          data: {
            likeNumber: --this.info.likeNumber,
          },
        });
    },
    getUserInfo() {
      db.collection("userInfo")
        .where({
          _openid: this.openid,
        })
        .get()
        .then((res) => {
          this.review.userName = res.data[0].user_name;
          this.review.userImage = res.data[0].user_img;
        });
    },
    show1() {
      this.show = true;
    },
    okValue() {
      let time = new Date();
      this.review.creatTime = Date.now();
      console.log("sdfsdf", time);
      this.review.time = this.$u.timeFormat(time, "yyyy-mm-dd hh:MM");
      console.log("时间", time);
      this.addMess();
      this.modelShow();
    },
    addMess() {
      db.collection("comments")
        .add({
          data: this.review,
        })
        .then((res) => {
          uni.showToast({ title: "发布成功", icon: "none" });
          this.message();
        });
    },
    modelShow() {
      this.show = !this.show;
      this.review.value = "";
    },
  },
};
</script>

<style scoped lang="scss">
.box {
  display: flex;
  flex-direction: column;
  padding: 32rpx;
}
.mess {
  display: flex;
  .message {
    .userName {
      color: #838287;
      line-height: 44rpx;
    }
    .time {
      color: #b3b3b3;
      line-height: 36rpx;
    }
  }
}
.content {
  height: 100vh;
  height: 100%;
  display: flex;
  flex-direction: column;
  .ct1 {
    font-size: 32rpx;
    font-weight: 600;
    color: #333333;
  }
  .c-t2 {
    font-size: 28rpx;
    font-weight: 400;
    color: #333333;
  }
}
.fixedBom {
  position: fixed;
  z-index: 100;
  left: 0;
  bottom: 0;
  width: 100%;
  // height: 160upx;
  background-color: white;
  padding: 24upx 56upx 68upx 26upx;

  .iconbox {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 0 20upx;

    image {
      width: 48upx;
      height: 48upx;
    }

    text {
      color: #000000;
      font-size: 22upx;
    }
  }

  .disIput {
    border-radius: 40upx;
    box-sizing: border-box;
    height: 80upx;
    background-color: #f1f0f5;
    padding-left: 31upx;

    image {
      width: 36upx;
      height: 36upx;
    }

    text {
      font-size: 28upx;
      color: #aeadb3;
    }
  }
}
.showModel {
  position: fixed;
  bottom: 0;
  left: 0;
  z-index: 999;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.6);

  .comment {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
    padding: 24upx;
    background-color: #f1f0f5;

    .inputs {
      box-sizing: border-box;
      background-color: #ffffff;
      border-radius: 24upx;
      margin-right: 24upx;
      padding-left: 30upx;
      padding-top: 10upx;
      padding-bottom: 10upx;
      line-height: 1.2;
    }

    .btn {
      width: 104upx;
      height: 72upx;
      border-radius: 36upx;
      line-height: 72upx;
      font-size: 28upx;
      color: #ffffff;
      text-align: center;
    }

    .btn.bg1 {
      background-color: #4575f6;
    }

    .btn.bg2 {
      background-color: #020002;
    }
  }
}
</style>
