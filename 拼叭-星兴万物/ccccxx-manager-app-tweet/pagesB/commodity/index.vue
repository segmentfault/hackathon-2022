<template>
  <view class="page-detail">
    <view class="head-swiper">
      <d-swiper
        :list="data.pictures"
        imgKey="imgUrl"
        imgWidth="100%"
        previousMargin="25"
        nextMargin="25"
        height="160"
        imgRadius="5"
        dots="true"
      />
    </view>
    <button
      type="default"
      :open-type="openType"
      @click="openType !== 'share' && openPageBounty()"
      class="share flex flex-between flex-middle mt30"
      v-if="!shareUserId"
    >
      <image
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/1ce9cb14-ef93-46f9-b63f-48867ea8bd9b.png"
        class="share-icon"
      ></image>
      <view class="flex-1 pl20">
        <view class="fs32 color-white">分享好友下单</view>
        <view class="fs24 mt10 color-white" v-if="openType == 'share'">
          <text>最多赚取佣金</text><text>¥{{ shareMaxMoney }}</text>
        </view>
      </view>
      <view class="share-button">立即分享</view>
    </button>

    <view class="template" v-if="data.companyName">
      <view class="businessInfo">
        <view class="title">
          <view class="name">
            {{ data.companyName }}
          </view>
        </view>
        <view class="tags mt20">
          <view class="tag fs22" v-for="item in data.cates" :key="item">
            {{ item }}
          </view>
        </view>
        <view class="businessTime mt30" @click.stop="openLocation">
          <view style="color: #fff">
            营业时间:{{ data.startHour }}点到{{ data.endHour }}点
          </view>

          <image
            @click.stop="callPhone(data.companyPhone)"
            src="http://image-1306191496.cos.ap-nanjing.myqcloud.com/f41b7d32-c90e-4642-b7f2-4569adeaab04.png"
            mode=""
          ></image>
        </view>
        <view class="address tag mt20" @click.stop="openLocation">
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

    <view class="template" v-if="seckillList.length">
      <view class="businessInfo seckill">
        <view class="flex flex-between flex-middle">
          <image
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/477f2ad4-0331-45a9-911e-33738b27efe7.png"
            class="seckill-icon"
          ></image>
          <text class="more fs26 color-999" @tap="killMore">更多></text>
        </view>
        <view class="flex flex-between flex-middle">
          <view
            class="items mt20"
            v-for="item in seckillList"
            :key="item.goodsId"
            @tap="jump(item.goodsId)"
          >
            <image
              :src="item.goodsList[0].goodsPic"
              class="goods-cover"
            ></image>
            <view class="oneOmit color-white fs28 mt10">{{
              item.goodsList[0].goodsName
            }}</view>
            <view class="flex flex-middle color-tip mt20">
              <text class="fs32 mr20"
                >¥{{ item.goodsList[0].goodsPriceItem.salePrice }}</text
              >
              <text class="fs22 share-money"
								 v-if="openType == 'share'"
                >分享赚¥{{ item.goodsList[0].goodsPriceItem.shareRebate }}</text
              >
            </view>
            <view class="fs24 color-999 mt10"
              >门市价¥{{ item.goodsList[0].goodsPriceItem.marketPrice }}</view
            >
            <view class="flex flex-between flex-middle mt10">
              <view class="slider-view rel">
                <view
                  class="slider-rate abs"
                  :style="{
                    width: countPlan(item.goodsList[0].goodsPriceItem),
                  }"
                ></view>
                <image
                  src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/937d2f23-e4de-4e60-8810-f1acfb792689.png"
                  class="abs hot"
                  :style="{ left: countPlan(item.goodsList[0].goodsPriceItem) }"
                ></image>
              </view>
              <text class="rush fs26 color-white">抢购</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="scriptInfo" v-if="scriptList.length">
      <view class="title mt30">
        <view class="left">
          {{typeId == 1 ? "套餐" : "热门商品" }}（{{
            total
          }}）
        </view>
        <view class="right tag"  v-if="typeId != 1" style="background: #2a2f43" @click="next(2)">
          更多>
        </view>
      </view>
      <d-swiper
        :list="scriptList"
        :slots="false"
        previousMargin="25"
        nextMargin="120"
      >
        <template v-slot="{ data }">
          <view class="templateInfo" @click="handlerClick(data)">
            <view class="image">
              <image :src="data.goodsPic" mode=""></image>
            </view>
            <view class="info">
              <view class="name oneOmit">
                {{ data.goodsName }}
              </view>
              <view class="flex fs20">
                <view class=""
                  v-for="(item, index) in data.labelList"
                  :key="index"
                >
                  <text class="mr10">{{ item }}</text>
									<text class="mr10">|</text>
                </view>
              </view>
              <view class="price mt10 flex flex-middle">
                <text class="sale-price">¥{{ data.goodsPriceItem.salePrice }}</text>
                <text class="mt5">门市价 ¥{{ data.goodsPriceItem.marketPrice }}</text>
              </view>
              <view class="label flex flex-middle flex-between">
                <text class="share-rebate" v-if="data.openType == 'share'">
									分享赚{{ data.goodsPriceItem.shareRebate }}
								</text>
                <text class="reserve">预定</text>
              </view>
            </view>
            <view
              class="msg"
              v-html="data.scriptDesc || data.secretBrief || data.sesc"
            ></view>
            <!-- <jyf-parser style="padding:0 30rpx;margin-top:30rpx" :html="data.scriptDesc || data.desc" use-anchor /> -->
          </view>
        </template>
      </d-swiper>
    </view>

    <view class="DMList" v-if="DMList.length && typeId != 1">
      <view :class="[{ mt30: !scriptList.length.length }, 'title']">
        <view class="left">
          {{ typeId == 2 ? "人气DM" : "人气NPC" }}（{{ DMList.length }}）
        </view>
        <view class="right tag" style="background: #2a2f43" @click="next(1)"
          >更多></view
        >
      </view>
      <d-swiper
        :list="DMList"
        height="300"
        :slots="false"
        previousMargin="25"
        nextMargin="380"
      >
        <template v-slot="{ data }">
          <view class="DMInfo" @click="handlerOpen(data)">
            <view class="head-img">
              <image :src="data.head" mode=""></image>
            </view>
            <view class="introduce">
              <view class="name">
                {{ data.name }}
              </view>
              <view class="label">{{ data.label }}</view>
            </view>
          </view>
        </template>
      </d-swiper>
    </view>
		<jyf-parser
		  style="padding: 0 30rpx; margin-top: 30rpx"
		  :html="data.description"
		  use-anchor
		/>
    <!--    <view class="btm-bg">
      <view class="btm-btn" v-if="activityId" @click="goApply">
        <view class="t1">立即预约</view>
      </view>
    </view> -->
    <u-popup v-model="isShowPop" width="600rpx" mode="center" closeable>
      <view class="pop-center">
        <image :src="info.head" mode=""></image>
        <view class="html-nickname">{{ info.name }}</view>
        <view class="label html-name">
          <view class="html-name-label"> {{ info.label }} </view>
        </view>
        <view class="html-text" v-html="info.text"> </view>
        <!-- <jyf-parser style="padding:0 30rpx;margin-top:30rpx" :html="info.text" use-anchor /> -->
      </view>
    </u-popup>
    <!--    <xkq-canvas
      ref="canvas"
      @tempFilePath="tempFilePath"
      :canvasSize="canvasSize"
      :cancasImgs="cancasImgs"
      :fonts="fonts"
    /> -->
    <canvas canvas-id="cardCover" class="cardCanvas"></canvas>
  </view>
</template>

<script>
import dSwiper from "../components/d-swiper.vue";
import GameService from "../../service/game";
import ProductService from "../../service/product";
import DucumentService from "../../service/document.js";
import JyfParser from "../components/jyf-parser/jyf-parser.vue";
import app from "../../App.vue";

export default {
  components: {
    dSwiper,
    JyfParser,
  },
  data() {
    return {
      isShowPop: false,
      data: {},
      // openType: "share1",
      scriptList: [],
      DMList: [],
      changeDMNum: 0,
      changeScNum: 0,
      activityId: "",
      info: {},
      typeId: 2,
      total: 0,
      shareMaxMoney: 0,
      seckillList: [],
      isAgain: false,
      shareImg: "",
      shareUserId: null,
    };
  },
  computed: {
    openType() {
      return this.$store.state.user.identityType == 0 ? 'btn': 'share';
    },
  },
  onLoad(o) {
    this.supplierId = parseInt(o.supplierId);
    if (o.activityId) {
      this.activityId = o.activityId;
    }
    if (o.typeId) {
      this.typeId = o.typeId;
    }
    // this.getShareInfo();
    if (uni.getStorageSync("userInfo").head) {
      if (o.shareUserId) {
        this.shareUserId = o.shareUserId;
        this.$eventRecord(246);
        GameService.bindRelation({ parentId: o.shareUserId, parentType: 1 });
      }
      this.getSeckillList();
      this.getSupplierInfo();
      this.getShareMaxMoney();
    } else {
      uni.navigateTo({
        url: `/pagesA/user/login?isOnload=${true}`,
      });
    }
    this.$store.commit('setIdentityType');
  },
  onShareAppMessage(from) {
    if (from.from === "button") {
      this.$eventRecord(240);
    }
    uni.showLoading();
    const promise = new Promise((resolve) => {
      setTimeout(() => {
        uni.hideLoading();
        resolve({
          title: this.data.companyName,
          imageUrl: this.shareImg,
          path: `/pagesB/commodity/index?supplierId=${this.supplierId}&shareUserId=${this.myUserId}&isHome=1`,
        });
      }, 1500);
    });
    return {
      title: this.data.companyName,
      path: `/pagesB/commodity/index?supplierId=${this.supplierId}&shareUserId=${this.myUserId}&isHome=1`,
      imageUrl: this.data.pictures[0],
      promise,
    };
  },
  methods: {
    openPageBounty() {
      uni.navigateTo({
        url: `/pagesB/commodity/bounty`,
      });
    },
    async createCardImg() {
      const btnIcon =
        "https://image-1306191496.cos.ap-nanjing.myqcloud.com/e2716185-f8ea-4e1d-9655-c8eb835a9713.png";
      let d = await uni.downloadFile({ url: this.data.pictures[0] });
      const gbImg = d[1].tempFilePath;
      let b = await uni.downloadFile({ url: btnIcon });
      const btnImg = b[1].tempFilePath;
      // console.log(gbImg)
      // const imgHeight = 400
      // let sh = 400 * (500 / imgInfo.width)
      // let sy = (imgInfo.height - sh) / 2
      const ctx = uni.createCanvasContext("cardCover");
      // await ctx.setFillStyle('#fff')
      // await ctx.fillRect(0,0,500,400)

      // let r = 10,x = 0,y = 0,w = 500,h = imgHeight;
      // ctx.beginPath()
      // ctx.setFillStyle('transparent')

      // ctx.arc(x + r,y + r,r,Math.PI,Math.PI * 1.5)
      // ctx.moveTo(x + r,y)
      // ctx.lineTo(x + w - r,y)
      // ctx.lineTo(x + w,y + r)
      // ctx.arc(x + w - r,y + r,r,Math.PI * 1.5,Math.PI * 2)

      // ctx.lineTo(x + w,y + h -r)
      // ctx.lineTo(x + w - r,y + h)
      // ctx.arc(x + w -r,y + h - r,r,0,Math.PI * .5)

      // ctx.lineTo(x + r,y + h)
      // ctx.lineTo(x, y + h - r)
      // ctx.arc(x + r,y + h - r,r,Math.PI * .5,Math.PI)

      // ctx.lineTo(x,y + r)
      // ctx.lineTo(x + r,y)

      // ctx.fill()
      // ctx.closePath()
      // ctx.clip()
      await ctx.drawImage(gbImg, 0, 0, 500, 400);
      // ctx.draw()
      ctx.setFillStyle("rgba(0,0,0,.7)");
      ctx.fillRect(0, 0, 500, 400);
      ctx.font = "normal bold 30px sans-serif";
      ctx.fillStyle = "#fff";
      if (this.data.companyName.length > 13) {
        await ctx.fillText(this.data.companyName.substr(0, 13) + "...", 20, 40);
      } else {
        await ctx.fillText(this.data.companyName, 20, 40);
      }
      // let markWidth = 10
      let marks = "";
      ctx.font = "normal 24px sans-serif";
      for (let i = 0, len = this.data.cates.length; i < len; i++) {
        if (i + 1 !== len) {
          marks += `${this.data.cates[i]}、`;
        } else {
          marks += `${this.data.cates[i]}`;
        }
        // marks += `this.data.cates[i]、`
        // const w = this.data.cates[i].length * 30 + 10
        // if(i > 0){
        // 	markWidth += w
        // }
        // await ctx.setFillStyle('#FFF5F5')
        // await ctx.fillRect((i + 1) * 10 + markWidth,60,w,28)
        // await ctx.setFontSize(22)
        // ctx.fillStyle = "#ff4f3a"
        // await ctx.fillText(this.data.cates[i],(i + 1) * 10 + markWidth + 12,82)
        // await ctx.restore()
      }
      await ctx.fillText(marks, 20, 82);
      await ctx.fillText(
        `营业时间：${this.data.startHour}点到${this.data.endHour}点`,
        20,
        120
      );
      if (this.data.companyAddress.length > 19) {
        await ctx.fillText(
          this.data.companyAddress.substr(0, 19) + "...",
          20,
          155
        );
      } else {
        await ctx.fillText(this.data.companyAddress, 20, 155);
      }
      // await ctx.fillText(this.data.companyAddress,20,155)
      ctx.font = "normal bold 46px sans-serif";
      ctx.fillStyle = "#ff4f3a";
      await ctx.fillText(`¥${this.data.discountPrice}`, 20, 215);
      ctx.font = "normal 20px sans-serif";
      const s = this.data.discountPrice.toString().replace(/\./g, "");
      await ctx.fillText("拼叭价", (s.length + 1) * 38, 215);
      ctx.fillStyle = "#eee";
      const px = (s.length + 1) * 38 + 70;
      const mlen = this.data.originalPrice.toString().length;
      const slen = this.data.discountPrice.toString().length;
      await ctx.fillText(`¥${this.data.originalPrice}`, px, 215);
      ctx.moveTo(px - 5, 207);
      ctx.lineTo(px + mlen * 22, 207);
      ctx.setStrokeStyle("#fff");
      ctx.stroke();
      await ctx.drawImage(btnImg, 175, 300, 150, 50);
      this.savaNotice(ctx);
    },
    //保存画布图片
    savaNotice(ctx) {
      const that = this;
      const timeId = setTimeout(() => {
        //生成图片
        uni.canvasToTempFilePath({
          canvasId: "cardCover",
          fileType: "jpg",
          quality: 1,
          success(res) {
            console.log(res.tempFilePath);
            that.shareImg = res.tempFilePath;
          },
          fail(err) {
            console.log(err);
          },
        });
      }, 1000);
      ctx.draw(false, timeId);
    },
    countPlan(item) {
      return item.allStockNum
        ? item.allStockNum / item.stockNum + "%"
        : 0 / item.stockNum + "%";
    },
    jump(id) {
      uni.navigateTo({
        url: `/pagesB/commodity/sInfo?id=${id}&isSeckill=${true}&supplierId=${
          this.supplierId
        }`,
      });
    },
    async getSeckillList() {
      const params = {
        supplierId: this.supplierId,
        pageNum: 1,
        pageSize: 2,
      };
      if (app.globalData.latitude) {
        params.lat = app.globalData.latitude;
        params.lng = app.globalData.longitude;
      } else {
        const data = await app.methods.getLocation();
				const latitude = data.latitude || 28.22778
				const longitude = data.longitude || 112.93886
				app.globalData.latitude = latitude;
				app.globalData.longitude = longitude;
        params.lat = latitude;
        params.lng = longitude;
      }
      ProductService.seckillList(params).then((res) => {
        this.seckillList = res.list;
      });
      // .catch(err => {
      // 	if(err.code == 500101){
      // 		if(!this.isAgain){
      // 			this.isAgain = true
      // 			LoginService.defLogin()
      // 			this.getSeckillList()
      // 		}else{
      // 			uni.showLoading()
      // 			setTimeout(() => {
      // 				this.getSeckillList()
      // 				uni.hideLoading()
      // 			},1000)
      // 		}
      // 	}
      // })
    },
    killMore() {
      uni.navigateTo({
        url: `/pagesB/commodity/seckillList?supplierId=${this.supplierId}`,
      });
    },
    getShareMaxMoney() {
      ProductService.getMaxPrice({ id: this.supplierId }).then((res) => {
        this.shareMaxMoney = res;
      });
      // .catch(err => {
      // 	if(err.code == 500101){
      // 		if(!this.isAgain){
      // 			this.isAgain = true
      // 			LoginService.defLogin()
      // 			this.getShareMaxMoney()
      // 		}else{
      // 			uni.showLoading()
      // 			setTimeout(() => {
      // 				this.getShareMaxMoney()
      // 				uni.hideLoading()
      // 			},1000)
      // 		}
      // 	}
      // })
    },
    handlerOpen(item) {
      this.info = item;
      this.isShowPop = true;
    },
    openLocation() {
      // 打开地图显示位置
      uni.openLocation({
        latitude: Number(this.data.lat),
        longitude: Number(this.data.lng),
        name: this.data.companyAddress,
      });
    },
    next(type) {
      if (type == 1) {
        uni.navigateTo({
          url:
            "/pagesA/businessDetail/list?supplierId=" +
            this.supplierId +
            "&ids=" +
            this.data.employeeIds +
            "&typeId=" +
            this.typeId +
            "&activityId" +
            this.activityId,
        });
      } else {
        this.$eventRecord(241);
        uni.navigateTo({
          url: `/pagesB/commodity/list?typeId=${this.typeId}&supplierId=${this.supplierId}&isCreate=1`,
        });
      }
    },
    getShareInfo() {
      GameService.getShareInfo({ type: 4, businessId: this.supplierId }).then(
        (res) => {
          this.shareParams.path = `/pagesB/commodity/index?supplierId=${this.supplierId}&shareUserId=${this.myUserId}&isHome=1`;
          this.shareParams.title = res.shareTitle;
          this.record = 240;
          this.initShareParems({
            img: res.shareImage,
            title: res.shareTitle,
            fonts: [res.shareDesc, `${this.myUserInfo.nickName}邀请您`],
          });
        }
      );
    },
    async getScriptList() {
      const res = await ProductService.getCanPlayPlay({
        pageNum: 1,
        pageSize: 6,
        supplierId: this.supplierId,
      });
			if(res.list){
				res.list.forEach(item => item.openType = this.openType)
			}
      this.scriptList = res.list;
      this.total = res.total;
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
      GameService.getSupplierInfo({
        supplierId: this.supplierId,
      }).then((res) => {
        res.pictures.filter((v) => {
          v = this.$pictureUrl(v, 375, 110);
        });
        this.data = res;
        this.createCardImg();
        this.getScriptList();
        this.getDMList();
      });
      // .catch(err => {
      // 	if(err.code == 500101){
      // 		if(!this.isAgain){
      // 			this.isAgain = true
      // 			LoginService.defLogin()
      // 			this.getSupplierInfo()
      // 		}else{
      // 			uni.showLoading()
      // 			setTimeout(() => {
      // 				this.getSupplierInfo()
      // 				uni.hideLoading()
      // 			},1000)
      // 		}
      // 	}
      // })
    },
    changeDM(i, length) {
      if (this.changeDMNum == 1) {
        uni.navigateTo({
          url:
            "/pagesA/businessDetail/list?supplierId=" +
            this.supplierId +
            "&ids=" +
            this.data.employeeIds +
            "&typeId=" +
            this.typeId +
            "&activityId" +
            this.activityId,
        });
      } else {
        this.changeDMNum++;
      }
    },
    handlerClick(data) {
      this.$eventRecord(242);
      uni.navigateTo({
        url: `/pagesB/commodity/sInfo?id=${data.id}&supplierId=${this.supplierId}`,
      });
    },
    callPhone(p) {
      this.$eventRecord(243);
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
  padding-top: 30rpx !important;
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

.share {
  background: linear-gradient(to right, #e85240, #ef8269);
  padding: 10upx 30upx;
  border-radius: 50upx;
  width: calc(100% - 50upx);
  margin-left: 25upx;
  line-height: inherit;
  text-align: left;
  .share-icon {
    width: 88upx;
    height: 88upx;
  }
  .share-button {
    text-align: center;
    width: 150upx;
    height: 50upx;
    color: #e85341;
    background: #fff;
    border-radius: 30upx;
    font-size: 26upx;
    line-height: 50upx;
    margin: 0;
    padding: 0;
  }
}

.template {
  width: 100%;
  margin: auto;
  display: flex;
  justify-content: space-between;
  padding: 0 25upx;
  .businessInfo {
    background: #2a2f43;
    width: 100%;
    margin: auto;
    margin-top: 30rpx;
    padding: 30rpx;
    box-shadow: 0px 2px 38px 0px rgba(0, 0, 0, 0.1);
    border-radius: 16rpx;

    .title {
      display: flex;
      .name {
        font-size: 36upx;
        color: #ffffff;
        line-height: 50upx;
      }
    }

    .tags {
      display: flex;
      margin-bottom: 0;
    }

    .businessTime {
      font-size: 30rpx;
      display: flex;
      justify-content: space-between;
      margin-bottom: 0;

      image {
        width: 35rpx;
        height: 35rpx;
      }
    }

    .address {
      display: flex;
      font-size: 25upx;
      height: 45rpx;
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
        width: 35rpx;
        height: 35rpx;
        vertical-align: middle;
        margin-right: 10rpx;
      }
      .distance {
        margin-left: 30rpx;
      }
    }
  }
  .seckill {
    .seckill-icon {
      width: 182upx;
      height: 60upx;
    }
    .more {
      font-size: 24rpx;
      color: #fff;
      line-height: 48rpx;
      padding: 0 35rpx;
      background: rgba(255, 255, 255, 0.23);
      border-radius: 16px;
      background: #171c33;
    }
    .goods-cover {
      width: 300upx;
      height: 300upx;
      border-radius: 16upx;
    }
    .share-money {
      padding: 2upx 12upx;
      border: 1px solid #ff4f3a;
      border-radius: 6upx;
    }
    .slider-view {
      background: #ffd8d8;
      width: 124upx;
      height: 12upx;
      border-radius: 6upx;
      .slider-rate {
        background: #ff4f3a;
        height: 12upx;
        top: 0;
        left: 0;
        border-radius: 6upx;
        width: 50%;
      }
      .hot {
        width: 26upx;
        height: 32upx;
        bottom: -4upx;
        left: 50%;
        margin-left: -13upx;
      }
    }
    .rush {
      padding: 6upx 30upx;
      border-radius: 30upx;
      background: #ff4f3a;
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
  .title {
    padding: 0upx 15upx 0 35upx;
    margin: auto;
    display: flex;
    font-size: 35rpx;
    justify-content: space-between;
    align-content: center;
    align-items: center;
    .right {
      font-size: 24upx;
      color: #fff;
      line-height: 48rpx;
      padding: 0 35rpx;
      background: rgba(255, 255, 255, 0.23);
      border-radius: 16px;
    }
  }

  .DMInfo {
    margin-top: 30rpx;
    margin-right: 30rpx;
    padding: 30rpx;
    background: #2a2f43;
    border-radius: 25rpx;
    width: 330upx;
    .head-img {
      width: 270upx;
      height: 354upx;
      image {
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-radius: 16rpx;
      }
    }
    .label {
      color: #fff;
    }

    .introduce {
      font-size: 35rpx;

      .name {
        margin-bottom: 20rpx;
        font-weight: bold;
        color: #ffffff;
        line-height: 40upx;
        font-size: 28upx;
      }

      .msg {
        font-weight: 100;
        line-height: 36rpx;
        height: 105rpx;
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
    margin-top: 30rpx;
    padding: 30rpx;
    background: #2a2f43;
    border-radius: 25rpx;
    margin-right: 30rpx;
    display: flex;
    flex-wrap: wrap;

    image {
      width: 150rpx;
      height: 200rpx;
      object-fit: cover;
      border-radius: 8upx;
    }

    .info {
      width: calc(100% - 180rpx);
      display: flex;
      color: #fff;
      flex-direction: column;
      margin-left: 30rpx;

      .name {
        font-size: 32rpx;
        color: #fff;
        margin-bottom: 10rpx;
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
      .share-rebate {
        font-size: 20upx;
        padding: 0 8upx;
        border: 1px solid #ff4f3a;
        border-radius: 6upx;
        color: #ff4f3a;
      }
      .reserve {
        background: #ff4f3a;
        color: #fff;
        border-radius: 20upx;
        font-size: 24upx;
        padding: 6upx 26upx;
      }
    }

    .msg {
      line-height: 30rpx;
      margin-top: 10rpx;
      height: 60rpx;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      color: #fff;
      font-size: 22upx;
    }
  }
}
.label {
  margin-top: 15rpx;
  font-size: 26rpx;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #999999;
  line-height: 36rpx;
}

.tag {
  background-color: rgba($color: #fff, $alpha: 1);
  color: #333;
  text-align: center;
  margin-right: 20rpx;
  padding: 5rpx 20rpx;
  border-radius: 30rpx;
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
  color: #ffffff;
  font-size: 40upx;
}
.pop-center {
  // background: #171c33;
  image {
    width: 100%;
    border-radius: 16rpx 16rpx 0 0;
  }
  > .html-nickname {
    padding: 15rpx 30rpx 0;
    font-weight: bold;
    color: #000000;
    line-height: 40rpx;
    font-size: 28rpx;
  }
  > .html-name {
    padding: 15rpx 30rpx;
    margin-top: 0;
    > .html-name-label {
      color: #333;
      text-align: center;
      border-radius: 10rpx;
      display: inline-flex;
    }
  }
  > .html-text {
    padding: 0 30rpx 15rpx;
  }
}
.html-description {
  padding: 34rpx;
}
.cardCanvas {
  width: 500px;
  height: 400px;
  position: absolute;
  left: -1000px;
  bottom: 0;
}
</style>
