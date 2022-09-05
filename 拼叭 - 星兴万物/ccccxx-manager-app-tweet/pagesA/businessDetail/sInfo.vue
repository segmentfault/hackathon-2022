<template>
  <view>
    <comHead
      changeColor
      autoPadding
      class="com-head"
      :isHome="isHome"
    ></comHead>
    <view
      class="main"
      :style="{
        background: `url(${info.pic}) no-repeat`,
        backgroundSize: 'cover',
        backgroundPosition: 'center center',
      }"
    ></view>
    <view class="card">
      <view class="title" v-if="info.name">
        <view class="pt5 mr10" v-if="info.type"
          ><img :src="typeImg[info.type - 1]" alt=""
        /></view>
        {{ info.name }}</view
      >
      <view class="label" v-if="info.people">
        <view>{{ info.people }}人本</view>
        <view v-if="info.labels && info.labels.length"
          >| {{ info.labels.map((item) => item.label).join("、") }}</view
        >
        <view v-if="info.peopleSet">| {{ info.peopleSet }}</view>
        <view v-else-if="!info.peopleSet && info.man && info.woman"
          >| {{ info.man }} 男 {{ info.woman }} 女</view
        >
        <view v-if="info.duration"
          >|
          {{
            info.duration.includes("h") ? info.duration : `${info.duration} h`
          }}</view
        >
        <view v-if="info.diffiulty">| {{ info.difficulty }} h</view>
        <view v-if="info.role == 0 || info.role == 1"
          >| {{ info.role ? "不可反串" : "可反串" }}</view
        >
      </view>
       <view v-if="info.author">发行:{{ info.author }}</view>
      <view v-if="info.price" class="price mt10 flex flex-middle">
        <text class="sale-price">￥{{ info.price }}</text>
      </view>
      <view class="grade mt40 mb30" v-if="info.rateNum">
        <view class="grade-top">
          <view class="ml30"
            ><img
              src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/4f40c0ed-839e-415a-831c-bdcdfde58768.png"
              alt=""
          /></view>
        </view>
        <view class="grade-btm">
          <view class="g-left ml20 mb10">
            <view class="fs28 fw400">{{ info.rateNum }}人评分</view>
            <view style="color: #ea634f" class="fs60">{{
              Number.isInteger(info.rate) ? info.rate + ".0" : info.rate
            }}</view>
          </view>
          <view class="g-rigth">
            <view
              class="rata mr30"
              v-for="(item, index) in rateStar"
              :key="item.index"
            >
              <u-rate
                :current="index"
                active-color="#F9D0CA"
                inactive-color="#EA634F"
                :gutter="9"
                inactive-icon="star-fill"
                :disabled="true"
              ></u-rate>
              <view class="ml10" style="width: 250upx">
                <u-line-progress
                  :show-percent="false"
                  active-color="#EA634F"
                  inactive-color="#FFDBD5"
                  height="12"
                  :percent="info[item]"
                ></u-line-progress>
              </view>
            </view>
          </view>
        </view>
      </view>
      <view class="desc">【剧情简介】</view>
      <view>{{ info.brief }}</view>
      <view class="detail" v-html="info.text"></view>
    </view>
    <!-- <view class="btm-bg" v-if="isHome"> -->
    <!-- <view class="btm-btn"> -->
    <!-- <view class="t1" @click="goApply">确定</view> -->
    <!-- </view> -->
    <!-- </view> -->
  </view>
</template>

<script>
import documentService from "../../service/document.js";
import comHead from "../../components/com-head.vue";
import basic from "../../service/basic";
export default {
  components: {
    comHead,
  },
  data() {
    return {
      info: {},
      rateStar: ["rateFive", "rateFour", "rateThree", "rateTwe", "rateOne"],
      id: "",
      shareImg: "",
      scenne: 1001,
      isHome: false,
      typeImg: [
        "https://image-1306191496.cos.ap-nanjing.myqcloud.com/fb076f8d-87e7-46e8-b9f7-63016dd4e820.png",
        "https://image-1306191496.cos.ap-nanjing.myqcloud.com/b481fece-3390-4cbd-b96e-729cb8cbd3db.png",
        "https://image-1306191496.cos.ap-nanjing.myqcloud.com/136593e8-69b6-427b-8f1a-651d1eb3faae.png",
        "https://image-1306191496.cos.ap-nanjing.myqcloud.com/592a2ac8-f357-464d-80c1-fd2c8a676fba.png",
      ],
    };
  },
  onLoad(o) {
    this.$eventRecord(204);
    if (o.typeId) this.typeId = o.typeId;
    if (o.activityId) this.activityId = o.activityId;
    if (o.isHome) this.isHome = o.isHome;
    this.id = o.id;
    // 获取信息
    this.getInfo(o.id);
    // 获取图片
    // this.getShareImg()
  },
  onShareAppMessage() {
    return {
      title: this.info.name,
      path: `/pagesA/businessDetail/sInfo?id=${this.id}&typeId=${this.typeId}&activityId=${this.activityId}&isHome=1`,
      imageUrl: "",
    };
  },
  methods: {
    async getShareImg() {
      try {
        const res =
          (await basic.getShareImg({
            businessId: this.id,
            scene: 3,
          })) || {};
        if (res.shareImg) this.shareImg = res.url;
      } catch (error) {
        this.shareImg = "";
      }
    },
    async getInfo(id) {
      const apiName = this.typeId == 2 ? "scriptInfo" : "secretEscapeInfo";
      documentService[apiName](id).then((res) => {
        this.info = res;
        console.log(res);
      });
    },

    goApply() {
      // 立即预约埋点
      this.$eventRecord(140);
      // 商家详情报名的埋点
      uni.navigateBack();
    },
  },
};
</script>

<style lang="scss" scoped>
.com-head {
  position: absolute;
  top: 20upx;
}

.main {
  height: 100vh;
  width: 100%;
  position: fixed;
}

.card {
  position: absolute;
  top: 50%;
  width: 100%;
  min-height: 70%;
  background-color: #fff;
  border-radius: 32upx 32upx 0 0;
  background: linear-gradient(
    to bottom,
    rgba(255, 255, 255, 0.7) 0%,
    rgba(255, 255, 255, 1) 20%,
    rgba(255, 255, 255, 1) 100%
  );
  padding: 40upx 30upx;
  .title {
    display: flex;
    font-size: 50upx;
    font-weight: bold;
    img {
      width: 28upx;
      height: 50upx;
    }
  }

  .label {
    display: flex;
    flex-wrap: wrap;
    margin: 20upx 0;
    line-height: 45upx;
    > view {
      display: inline-flex;
      margin-right: 10rpx;
      color: #000000;
    }
    > .right {
      flex: none;
      flex-basis: 220upx;
    }
  }
  .desc {
    display: flex;
    margin: 20upx 0;
    line-height: 45upx;
    margin-left: -12rpx;
  }
  .grade {
    display: flex;
    flex-direction: column;
    background: linear-gradient(180deg, #ffe6e1 0%, #ffffff 100%);
    border-radius: 18px;
    .grade-top {
      display: flex;
      height: 82upx;
      img {
        width: 130upx;
        height: 82upx;
      }
    }
    .grade-btm {
      display: flex;
      justify-content: space-between;
      .g-left {
        display: flex;
        flex-direction: column-reverse;
        align-items: center;
      }
      .g-rigth {
        display: flex;
        flex-direction: column;
        .rata {
          display: flex;
        }
      }
    }
  }
}
.price {
  font-size: 20rpx;
  color: #999;
  .sale-price {
    color: #ff4f3a;
    font-size: 35rpx;
    margin-right: 10rpx;
  }
}
.btm-bg {
  position: fixed;
  left: 0px;
  padding-top: 20upx;
  bottom: 0px;
  width: 100%;
  background: linear-gradient(
    to bottom,
    rgba(0, 0, 0, 0.1) 0%,
    rgba(0, 0, 0, 1) 100%
  );
  height: 160upx;
  padding-bottom: env(safe-area-inset-bottom);

  .btm-btn {
    margin: 0upx 68upx;
    height: 107upx;
    width: calc(100% - 136upx);
    background: #ffffff;
    border-radius: 54upx;
    .t1 {
      text-align: center;
      font-size: 40upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #171c33;
      line-height: 107upx;
    }
  }
}
</style>
