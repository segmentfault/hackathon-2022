<template>
  <view class="page-detail">
    <view class="head-swiper">
      <d-swiper
        :list="data.pictures"
        imgKey="imgUrl"
        imgWidth="100%"
        previousMargin="34"
        nextMargin="35"
        height="160"
        imgRadius="5"
        dots="true"
      />
    </view>
    <view class="template">
      <view class="businessInfo">
        <view class="title">
          <view class="name">
            {{ data.companyName }}
          </view>
        </view>
        <view class="tags">
          <view class="tag" v-for="item in data.cates" :key="item">
            {{ item }}
          </view>
        </view>
        <view class="businessTime" @click.stop="openLocation">
          <view style="color: #fff">
            营业时间:{{ data.startHour }}点到{{ data.endHour }}
          </view>

          <image
            @click.stop="callPhone(data.companyPhone)"
            src="http://image-1306191496.cos.ap-nanjing.myqcloud.com/f41b7d32-c90e-4642-b7f2-4569adeaab04.png"
            mode=""
          ></image>
        </view>
        <view class="address tag" @click.stop="openLocation">
          <view style="line-height: 1.4">
            <image
              src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/card-place-icon.png"
              mode=""
            ></image
            >{{ data.companyAddress }}
          </view>
        </view>
      </view>
    </view>
     <view class="scriptInfo" v-if="scriptList.length">
      <view class="title">
        <view class="left">
          {{ typeId == 2 ? '热门剧本' : '热门主题' }}（{{
            data.businesses.length
          }}）
        </view>
        <view class="right tag" style="background: #2a2f43" @click="next(2)">
          更多>
        </view>
      </view>
      <d-swiper
        :list="scriptList"
        :slots="false"
        previousMargin="50"
        nextMargin="120"
      >
        <template v-slot="{data}">
          <view class="templateInfo" @click="handlerClick(data)">
            <view class="image">
              <image :src="data.pic" mode=""></image>
            </view>
            <view class="info">
              <view class="name oneOmit">
                {{ data.name }}
              </view>
              <view class="price"> <text>{{ data.price }}¥</text>门市价 </view>
              <view class="label">
                {{ data.label }}
              </view>
            </view>
            <view class="msg">
              {{ data.brief }}
            </view>
          </view>
        </template>
      </d-swiper>
    </view>
    <jyf-parser class="html-description" :html="data.description" use-anchor />
    <view class="DMList" v-if="DMList.length">
      <view class="title">
        <view class="left"> 人气DM（{{ DMList.length }}） </view>
        <view class="right tag" style="background: #2a2f43" @click="next(1)"
          >更多></view
        >
      </view>
      <d-swiper
        :list="DMList"
        height="300"
        :slots="false"
        previousMargin="25"
        nextMargin="320"
      >
        <template v-slot="{ data }">
          <view class="DMInfo" @click="handlerOpen(data)">
            <view class="head-img">
              <image :src="data.head" mode=""></image>
            </view>
            <view class="introduce">
              <view class="name oneOmit">
                {{ data.name }}
              </view>
              <view class="label">{{ data.label }}</view>
              <!-- <view class="msg" v-html="data.text"> </view> -->
            </view>
          </view>
        </template>
      </d-swiper>
    </view>
    <view class="btm-bg">
      <view class="btm-btn" v-if="activityId" @click="goApply">
        <view class="t1">立即预约</view>
      </view>
    </view>
    <u-popup v-model="isShowPop" width="600upx" mode="center" closeable>
      <view class="pop-center">
        <image :src="info.head" mode=""></image>
         <view class="html-nickname">{{ info.name }}</view>
        <view class="label html-name">
          <view class="html-name-label"> {{ info.label }} </view>
        </view>
          <view class="html-text" v-html="info.text"> </view>
      </view>
		</u-popup>
  </view>
</template>

<script>
import dSwiper from '../components/d-swiper.vue';
import GameService from '../../service/game';
import DucumentService from '../../service/document.js';
import basic from '../../service/basic';
import JyfParser from "../components/jyf-parser/jyf-parser.vue";
export default {
  components: {
    dSwiper,
    JyfParser
  },
  data() {
    return {
      isShowPop: false,
      data: {},
      scriptList: [],
      DMList: [],
      changeDMNum: 0,
      changeScNum: 0,
      activityId: '',
      shareImg: '',
      info: {},
      typeId: 2,
    };
  },
  onLoad(o) {
    this.$eventRecord(203)
    if (o.activityId) this.activityId = o.activityId;
    this.supplierId = parseInt(o.supplierId);
    if (o.typeId) {
      this.typeId = o.typeId;
    }
    this.getSupplierInfo();
  },
  onShareAppMessage() {
    return {
      title: this.data.companyName,
      path: `/pagesA/businessDetail/index?supplierId=${this.supplierId}`,
      imageUrl: '',
    };
  },
  methods: {
    handlerOpen(item) { 
      this.$eventRecord(193)
      this.info = item
      this.isShowPop = true
    },
    getShareImg() {
      basic
        .getShareImg({ businessId: this.supplierId, scene: 2 })
        .then((res) => {
          this.shareImg = res.url;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //保存画布图片
    openLocation() {
      this.$eventRecord(192)
      // 打开地图显示位置
      uni.openLocation({
        latitude: Number(this.data.lat),
        longitude: Number(this.data.lng),
        name: this.data.companyAddress,
      });
    },
    goApply() {
      // 立即预约埋点
      this.$eventRecord(140);
      // 商家详情报名的埋点
      let url = '/pagesA/barActivity/applyCenter';
      // 活动类型B
      if (this.activityId) {
        url = `/pagesA/barActivity/applyCenter?&activityId=${this.activityId}`;
      }
      wx.reportAnalytics('apply_supplier_home', {});
      uni.navigateTo({
        url,
      });
    },
    next(type) {
      if (type == 1) {
        uni.navigateTo({
          url:
            '/pagesA/businessDetail/list?supplierId=' +
            this.supplierId +
            '&ids=' +
            this.data.employeeIds +
            '&typeId=' +
            this.typeId +
            '&activityId' +
            this.activityId,
        });
      } else {
        uni.navigateTo({
          url:
            `/pagesA/hotScript/list?typeId=${this.typeId}&supplierId=${this.supplierId}&isCreate=1`
        });
      }
    },
    scroll: function (e) {
      console.log(e);
      this.old.scrollTop = e.detail.scrollTop;
    },
    async getScriptList() {
      const apiName = this.typeId == 2 ? 'scriptList': 'secretEscapeList'
      const res = await DucumentService[apiName]({
        pageNum: 1,
        pageSize: 6,
        supplierId: this.supplierId,
        ids: this.data.businessIds,
      });
      this.scriptList = res.list;
    },
    async getDMList() {
      let res = await DucumentService.DMList({
        pageNum: 1,
        pageSize: 6,
        supplierId: this.supplierId,
        ids: this.data.employeeIds,
      });
      this.DMList = res.list || [];
    },
    async getSupplierInfo() {
      let res = await GameService.getSupplierInfo({
        supplierId: this.supplierId,
      });
      res.pictures.filter((v) => {
        v = this.$pictureUrl(v, 375, 110);
      });
      this.data = res;
      this.getScriptList();
      this.getDMList();
      // this.getShareImg();
    },
    changeDM(i, length) {
      if (this.changeDMNum == 1) {
        uni.navigateTo({
          url:
            '/pagesA/businessDetail/list?supplierId=' +
            this.supplierId +
            '&ids=' +
            this.data.employeeIds +
            '&typeId=' +
            this.typeId +
            '&activityId' +
            this.activityId,
        });
      } else {
        this.changeDMNum++;
      }
    },
    handlerClick(data) {
      this.$eventRecord(194)
       uni.navigateTo({
         url:`/pagesA/businessDetail/sInfo?typeId=${this.typeId}&id=${data.id}`
       });
    },
    callPhone(p) {
      this.$eventRecord(191)
      uni.makePhoneCall({
        phoneNumber: p,
        success: (e) => {
          console.log(e);
        },
        fail(e) {
          console.log(e);
        },
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.page-detail {
  background: #171c33;
  min-height: 100vh;
  padding-top: 30upx !important;
}
.com-head {
  background: #171c33;
  color: #fff;
}

.head {
  line-height: 1em;
  font-size: 32upx;
  color: #fff;
}

// .head-swiper {
//   margin-top: 30upx;
// }

.template {
  width: 100%;
  margin: auto;
  display: flex;
  justify-content: space-between;
  padding: 0 34upx;
  .businessInfo {
    background: #2a2f43;
    width: 100%;
    margin: auto;
    margin-top: 30upx;
    padding: 30upx;
    box-shadow: 0px 2px 38px 0px rgba(0, 0, 0, 0.1);
    border-radius: 16upx;
    view {
      margin-bottom: 20upx;
    }

    .title {
      display: flex;
      .name {
        font-size: 36upx;
        font-weight: 600;
        color: #ffffff;
        line-height: 50upx;
      }
    }

    .tags {
      display: flex;
      margin-bottom: 0;
    }

    .businessTime {
      font-size: 30upx;
      display: flex;
      justify-content: space-between;
      margin-bottom: 0;

      image {
        width: 35upx;
        height: 35upx;
      }
    }

    .address {
      display: flex;
      font-size: 25upx;
      height: 45upx;
      overflow: hidden;
      color: #000000;
      view {
        margin-bottom: 0;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        word-break: break-all;
      }
      image {
        width: 35upx;
        height: 35upx;
        vertical-align: middle;
        margin-right: 10upx;
      }
      .distance {
        margin-left: 30upx;
      }
    }
  }

  .phone {
    text-align: center;
    display: flex;
    flex-direction: column;
    justify-content: center;
    color: #999999;

    image {
      display: block;
      margin: 0 auto;
    }
  }
}

.scriptInfo,
.DMList {
  padding-bottom: 35upx;
  .title {
    padding: 30upx 15upx 0 35upx;
    margin: auto;
    display: flex;
    font-size: 35upx;
    justify-content: space-between;
    align-content: center;
    align-items: center;
    .right {
      font-size: 24upx;
      color: #fff;
      line-height: 48upx;
      padding: 0 22upx;
      background: rgba(255, 255, 255, 0.23);
      border-radius: 12px;
    }
  }

  .DMInfo {
    margin-top: 30upx;
    margin-right: 30upx;
    padding: 30upx;
    background: #2a2f43;
    border-radius: 25upx;

    image {
      width: 300upx;
      height: 350upx;
      object-fit: cover;
      border-radius: 16upx;
    }
    .label { 
      color: #fff;
    }

    .introduce {
      font-size: 35upx;

      .name {
        font-weight: 600;
        margin-bottom: 20upx;
        font-weight: bold;
        color: #ffffff;
        line-height: 40upx;
        font-size: 28upx;
      }

      .msg {
        font-weight: 100;
        line-height: 36upx;
        height: 105upx;
        color: #fff;
        font-size: 28upx;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }
    }
  }

  .templateInfo {
    margin-top: 30upx;
    padding: 30upx;
    background: #2a2f43;
    border-radius: 25upx;
    margin-right: 30upx;
    display: flex;
    flex-wrap: wrap;

    image {
      width: 150upx;
      height: 200upx;
      object-fit: cover;
    }

    .info {
      width: calc(100% - 180upx);
      display: flex;
      color: #fff;
      flex-direction: column;
      margin-left: 30upx;

      view {
        margin-bottom: 15upx;
      }

      .name {
        font-size: 37upx;
        font-weight: bold;
        color: #fff;
        margin-bottom: 30upx;
      }

      .price {
        font-size: 20upx;
        color: #999;

        text {
          color: #ff4f3a;
          font-size: 35upx;
          margin-right: 30upx;
        }
      }
    }

    .msg {
      line-height: 30upx;
      margin-top: 20upx;
      height: 60upx;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      color: #fff;
    }
  }
}
.label { 
    margin-top: 24upx;
    font-size: 26upx;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #999999;
    line-height: 36upx;
}

.tag {
  background-color: rgba($color: #fff, $alpha: 1);
  color: #333;
  text-align: center;
  margin-right: 20upx;
  padding: 5upx 15upx;
  border-radius: 20upx;
}

.btm-bg {
  position: fixed;
  left: 0px;
  padding-top: 20upx;
  bottom: 0px;
  width: 100%;
  // background: #171C33;
  height: 160upx;
  padding-bottom: env(safe-area-inset-bottom);

  .btm-btn {
    // padding: 14upx 0px;
    margin: 0upx 68upx;
    height: 107upx;
    width: calc(100% - 136upx);
    background: #fde5a4;
    border-radius: 54upx;

    .t1 {
      text-align: center;
      font-size: 40upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      line-height: 107upx;
    }
  }
}
.left {
  font-weight: 600;
  color: #ffffff;
  font-size: 56upx;
}
.pop-center { 
  // background: #171c33;
  image { 
    width: 100%;
    border-radius: 16upx 16upx 0 0;
  }
  > .html-nickname { 
    padding: 15upx 30upx 0;
    font-weight: bold;
    color: #000000;
    line-height: 40upx;
    font-size: 28upx;
  }
  > .html-name { 
    padding: 15upx 30upx;
    margin-top: 0;
    > .html-name-label { 
        color: #333;
        text-align: center;
        border-radius: 10upx;
        display: inline-flex;
    }
  } 
  > .html-text { 
    padding: 0 30upx 15upx;
  }
}
.html-description { 
  padding: 34upx;
}
</style>
