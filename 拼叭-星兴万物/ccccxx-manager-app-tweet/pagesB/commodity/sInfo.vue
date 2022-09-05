<template>
  <view>
    <comHead changeColor class="com-head" :isHome="isHome"></comHead>
    <view
      class="main"
      :style="{
        background: `url(${info.goodsPic}) no-repeat`,
        backgroundSize: 'cover',
        backgroundPosition: 'center center',
      }"
    ></view>
    <view class="card">
      <view class="title">{{ info.goodsName }}</view>
      <view class="flex flex-between flex-bottom" style="width: 100%">
        <view class="" style="width: 100%">
          <view class="label fs24 mt10">
            <view v-if="info.scriptPeopleNum"
              >{{ info.scriptPeopleNum }}人本</view
            >
            <text class="ml5 mr15" v-if="info.scriptPeopleNum">|</text>
            <view v-if="info.labelList && info.labelList.length">
              {{ info.labelList.join("·") }}
            </view>
            <text class="ml5 mr15" v-if="info.labelList.length">|</text>
            <view v-if="info.scriptMan && info.scriptWoman"
              >{{ info.scriptMan }}男{{ info.scriptWoman }}女</view
            >
            <text class="ml5 mr15" v-if="info.scriptMan && info.scriptWoman"
              >|</text
            >
            <view v-if="info.scriptDuration">{{ info.scriptDuration }}h</view>
            <text class="ml5 mr15" v-if="info.scriptDifficulty">|</text>
            <view v-if="info.scriptDifficulty">{{
              info.scriptDifficulty
            }}</view>
            <view v-if="info.secretName">{{ info.secretName }}</view>
            <text class="ml5 mr15" v-if="info.secretName">|</text>
            <view v-if="info.secretPeopleNum">{{ info.secretPeopleNum }}</view>
            <text class="ml5 mr15" v-if="info.secretPeopleNum">|</text>
            <view v-if="info.secretMinPeople"
              >{{ info.secretMinPeople }}起订</view
            >
            <text class="ml5 mr15" v-if="info.secretMinPeople">|</text>
            <view v-if="info.secretDuration">{{ info.secretDuration }}h</view>
            <text class="ml5 mr15" v-if="info.secretDuration">|</text>
            <view v-if="info.secretDifficulty">{{
              info.secretDifficulty
            }}</view>
          </view>
          <view class="mt40">
            <text class="color-croci fs32" v-if="info.goodsPriceItem.salePrice"
              >¥{{ info.goodsPriceItem.salePrice }}/人</text
            >
            <text
              class="fs20 color-999 ml10"
              v-if="info.goodsPriceItem.marketPrice"
              >门市价 ¥{{ info.goodsPriceItem.marketPrice }}</text
            >
          </view>

          <!-- <view class="">
					<view class="share-money color-croci">分享赚¥22</view>
					<button type="default" open-type="share" class="share-button mt10">分享</button>
				</view> -->
          <!-- </view>  -->

          <button
            type="default"
            open-type="share"
            class="share flex flex-between flex-middle mt30"
            v-if="!shareUserId"
          >
            <image
              src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/1ce9cb14-ef93-46f9-b63f-48867ea8bd9b.png"
              class="share-icon"
            ></image>
            <view class="flex-1 pl20">
              <view class="fs32 color-white">分享好友下单</view>
              <view class="fs24 mt10 color-white" v-if="info.goodsPriceItem.shareRebate">
                <text v-if="identityType != 0">分享赚取</text
                ><text>¥{{ info.goodsPriceItem.shareRebate }}</text>
              </view>
            </view>
            <view class="share-button">立即分享</view>
          </button>

          <view class="desc">{{
            info.goodsClass == 1
              ? "【剧情简介】"
              : info.goodsClass == 2
              ? "【主题简介】"
              : "【简介】"
          }}</view>
          <!-- <view class="intro" v-html="info.scriptDesc || info.desc"></view> -->
          <jyf-parser
            style="padding: 0 30rpx; margin-top: 30rpx"
            :html="info.scriptDesc || info.secretBrief || info.desc"
            use-anchor
          />
          <view class="supplier mt30" @tap="jump">
            <view class="fs32">{{
              info.supplierDetailResponseDto.companyName
            }}</view>
            <view class="flex flex-between flex-middle cates mt10">
              <view class="">
                <text
                  v-for="item in info.supplierDetailResponseDto.cates"
                  :key="item"
                  class="fs22 mr10"
                  >{{ item }}</text
                >
              </view>
              <image
                src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/d3fd0a04-3477-4465-90f5-21cb0eadc003.png"
                class="phone"
                @tap.stop="call(info.supplierDetailResponseDto.companyPhone)"
              ></image>
            </view>
            <view class="flex flex-middle flex-between mt20">
              <view class="fs24 color-666 address flex flex-middle">
                <image
                  src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/7654ca94-35a9-4812-9df6-67812f6e628e.png"
                  class="position"
                ></image>
                <text class="oneOmit">{{
                  info.supplierDetailResponseDto.companyAddress
                }}</text>
              </view>
              <view class="fs28">联系商家</view>
            </view>
            <!-- <view class="mt20 fs22 color-croci discount-price">最低{{info.supplierDetailResponseDto.discountPrice}}元享1人3折券</view> -->
          </view>
          <view class="desc">【使用规则】</view>
          <jyf-parser
            style="padding: 0 30rpx; margin-top: 30rpx"
            :html="info.userRules"
            use-anchor
          />
        </view>

        <view class="fixed foot flex flex-middle flex-between" v-if="isBtn">
          <view class="">
            <text class="fs36 color-croci">¥</text
            ><text class="discount-price color-croci">{{
              info.goodsPriceItem.salePrice
            }}</text>
            <text class="fs20 color-999 ml10 ml10"
              >门市价 ¥{{ info.goodsPriceItem.marketPrice }}</text
            >
          </view>
          <view class="fs32 color-white buy" @tap="showDate">立即预定</view>
        </view>

        <u-popup mode="bottom" v-model="gameStartTimeShow">
          <view class="content">
            <zwyCalendar
              ref="calendar"
              type="sign"
              :activeDates="activeDates"
              :localDate="makeTimer"
              :showTimeSelect="false"
            />
          </view>
          <view class="footer-pop">
            <view class="btn" @click="gameStartTimeShow = false">取消</view>
            <view class="btn confirm" @click="buy">立即预定</view>
          </view>
        </u-popup>
        <!-- 		<xkq-canvas
		  ref="canvas"
		  @tempFilePath="tempFilePath"
		  :canvasSize="canvasSize"
		  :cancasImgs="cancasImgs"
		  :fonts="fonts"
		/> -->
        <canvas canvas-id="cardCover" class="cardCanvas"></canvas>
      </view>
    </view>
  </view>
	</view>
</template>

<script>
import zwyCalendar from "./components/zwy-calendar";
import ProductService from "../../service/product";
import gameService from "../../service/game";
import comHead from "../../components/com-head";
import JyfParser from "../components/jyf-parser/jyf-parser";
// import xkqCanvas from '../components/xkq-canvas/xkq-canvas'
// import shareMix from '../components/xkq-canvas/shareMix'

export default {
  // mixins: [shareMix],
  components: {
    comHead,
    zwyCalendar,
    JyfParser,
    // xkqCanvas
  },
  data() {
    return {
      info: {},
      id: "",
      shareImg: "",
      scenne: 1001,
      isHome: false,
      gameStartTimeShow: false,
      activeDates: {},
      makeTimer: "",
      isBtn: true,
      shareUserId: "",
      isSeckill: false,
    };
  },
  onLoad(o) {
    this.$eventRecord(256);
    this.id = o.id;
    // if (o.typeId) this.typeId = o.typeId;
    if (o.activityId) this.activityId = o.activityId;
    if (o.isHome) this.isHome = o.isHome;
    if (o.isBtn) this.isBtn = false;
    if (o.isSeckill) this.isSeckill = o.isSeckill;
    // this.getShareInfo()
    if (uni.getStorageSync("userInfo").head) {
      if (o.shareUserId) {
        this.$eventRecord(247);
        gameService.bindRelation({ parentId: o.shareUserId, parentType: 1 });
        this.shareUserId = o.shareUserId;
      }
      if (!this.isSeckill || this.isSeckill == "false") {
        this.getInfo(o.id);
        this.getActivityDate(o.id);
      } else {
        this.getSeckillDetail();
      }
    } else {
      uni.navigateTo({
        url: `/pagesA/user/login?isOnload=${true}`,
      });
    }
		this.$store.commit('setIdentityType');
  },
	computed:{
		identityType(){
			return this.$store.state.user.identityType
		}
	},
  onShareAppMessage(from) {
    if (from.from === "button") {
      this.$eventRecord(244);
    }
    uni.showLoading();
    const promise = new Promise((resolve) => {
      setTimeout(() => {
        uni.hideLoading();
        resolve({
          title: this.info.goodsName,
          imageUrl: this.shareImg,
          path: `/pagesB/commodity/sInfo?id=${
            this.id
          }&isHome=${true}&shareUserId=${this.myUserId}&isSeckill=${
            this.isSeckill
          }`,
        });
      }, 1500);
    });
    return {
      title: this.info.goodsName,
      path: `/pagesB/commodity/sInfo?id=${this.id}&isHome=${true}&shareUserId=${
        this.myUserId
      }&isSeckill=${this.isSeckill}`,
      imageUrl: this.info.goodsPic,
      promise,
    };
  },
  methods: {
    async createCardImg() {
      const btnIcon =
        "https://image-1306191496.cos.ap-nanjing.myqcloud.com/e2716185-f8ea-4e1d-9655-c8eb835a9713.png";
      let d = await uni.downloadFile({ url: this.info.goodsPic });
      const gbImg = d[1].tempFilePath;
      let b = await uni.downloadFile({ url: btnIcon });
      const btnImg = b[1].tempFilePath;
      // let iinfo = await uni.getImageInfo({src:gbImg})
      // const imgInfo = await iinfo[1]
      // const imgHeight = 270
      // let sh = 270 * (imgInfo.width / 500)
      // let sy = (imgInfo.height - sh) / 2
      const ctx = uni.createCanvasContext("cardCover");
      // await ctx.setFillStyle('#fff')
      // await ctx.fillRect(0,0,500,400)

      // let r = 10,x = 0,y = 0,w = 500,h = imgHeight;
      // ctx.setFillStyle('#fff')
      // ctx.fillRect(0,0,w,h)
      await ctx.drawImage(gbImg, 0, 0, 500, 400);
      ctx.setFillStyle("rgba(0,0,0,.7)");
      ctx.fillRect(0, 0, 500, 400);
      ctx.font = "normal bold 30px sans-serif";
      ctx.fillStyle = "#fff";
      if (this.info.goodsName.length > 13) {
        await ctx.fillText(this.info.goodsName.substr(0, 13) + "...", 20, 40);
      } else {
        await ctx.fillText(this.info.goodsName, 20, 40);
      }
      let markWidth = 10;
      let labels = "";
      ctx.font = "normal 24px sans-serif";
      for (let i = 0, len = this.info.labelList.length; i < len; i++) {
        if (i + 1 !== len) {
          labels += `${this.info.labelList[i]}、`;
        } else {
          labels += `${this.info.labelList[i]}`;
        }
        // const w = this.info.supplierDetailResponseDto.cates[i].length * 30 + 10
        // if(i > 0){
        // 	markWidth += w
        // }
        // await ctx.setFillStyle('#FFF5F5')
        // await ctx.fillRect((i + 1) * 10 + markWidth,60,w,28)
        // await ctx.setFontSize(22)
        // ctx.fillStyle = "#E92F29"
        // await ctx.fillText(this.info.supplierDetailResponseDto.cates[i],(i + 1) * 10 + markWidth + 12,82)
        // await ctx.restore()
      }
      await ctx.fillText(labels, 20, 82);
      let desc =
        this.info.scriptDesc || this.info.secretBrief || this.info.desc;
      await ctx.setFontSize(22);
      if (desc.length > 21) {
        await ctx.fillText(desc.substr(0, 21), 20, 120);
        await ctx.fillText(desc.substr(21, 20) + "...", 20, 145);
      } else {
        await ctx.fillText(desc, 20, 120);
      }
      ctx.font = "normal bold 46px sans-serif";
      ctx.fillStyle = "#ff4f3a";
      await ctx.fillText(
        `¥${this.info.goodsPriceItem.salePrice}`,
        20,
        desc.length > 21 ? 197 : 172
      );
      ctx.font = "normal 20px sans-serif";
      const s = this.info.goodsPriceItem.salePrice
        .toString()
        .replace(/\./g, "");
      await ctx.fillText(
        "拼叭价",
        (s.length + 1) * 38,
        desc.length > 21 ? 194 : 169
      );
      ctx.fillStyle = "#eee";
      const px = (s.length + 1) * 38 + 70;
      const mlen = this.info.goodsPriceItem.marketPrice.toString().length;
      const slen = this.info.goodsPriceItem.salePrice.toString().length;
      await ctx.fillText(
        `¥${this.info.goodsPriceItem.marketPrice}`,
        px,
        desc.length > 21 ? 194 : 169
      );
      ctx.moveTo(px - 5, desc.length > 21 ? 186 : 160);
      ctx.lineTo(px + mlen * 22, desc.length > 21 ? 186 : 160);
      ctx.setStrokeStyle("#fff");
      ctx.stroke();
      // await ctx.fillText(desc,20,155)
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
    getShareInfo() {
      gameService.getShareInfo({ type: 6, businessId: this.id }).then((res) => {
        this.shareParams.path = `/pagesB/commodity/sInfo?id=${this.id}&activityId=${this.activityId}&isHome=1&shareUserId=${this.myUserId}`;
        this.shareParams.title = res.shareTitle;
        this.record = 244;
        this.initShareParems({
          img: res.shareImage,
          title: res.shareTitle,
          fonts: [res.shareDesc, `${this.myUserInfo.nickName}邀请您`],
        });
      });
    },
    jump() {
      uni.navigateTo({
        url: `/pagesB/commodity/index?supplierId=${this.info.supplierId}`,
      });
    },
    confirmData() {
      const gameStartTime = `${this.$refs.calendar.localDateCopy}`;
      this.makeTimer = gameStartTime;
    },
    getActivityDate(id) {
      // this.$toast(this.id.toString())
      ProductService.getGoodsDate({ goodsId: id }).then((res) => {
        this.activeDates = res;
      });
      // .catch(err => {
      // 	if(err.code == 500101){
      // 		if(!this.isAgain){
      // 			this.isAgain = true
      // 			LoginService.defLogin()
      // 			this.getActivityDate()
      // 		}else{
      // 			uni.showLoading()
      // 			setTimeout(() => {
      // 				this.getActivityDate()
      // 				uni.hideLoading()
      // 			},1000)
      // 		}
      // 	}
      // })
    },
    getSeckillDetail() {
      ProductService.seckillDetail({ id: this.id }).then((res) => {
        this.info = res;
        this.createCardImg();
      });
      // .catch(err => {
      // 	if(err.code == 500101){
      // 		if(!this.isAgain){
      // 			this.isAgain = true
      // 			LoginService.defLogin()
      // 			this.getSeckillDetail()
      // 		}else{
      // 			uni.showLoading()
      // 			setTimeout(() => {
      // 				this.getSeckillDetail()
      // 				uni.hideLoading()
      // 			},1000)
      // 		}
      // 	}
      // })
    },
    async getInfo(id) {
      ProductService.getGoodsDetail({ id }).then((res) => {
        this.info = res;
        this.createCardImg();
      });
      // .catch(err => {
      // 	if(err.code == 500101){
      // 		if(!this.isAgain){
      // 			this.isAgain = true
      // 			LoginService.defLogin()
      // 			this.getInfo()
      // 		}else{
      // 			uni.showLoading()
      // 			setTimeout(() => {
      // 				this.getInfo()
      // 				uni.hideLoading()
      // 			},1000)
      // 		}
      // 	}
      // })
    },
    goApply() {
      uni.navigateBack();
    },
    call(phone) {
      uni.makePhoneCall({
        phoneNumber: phone,
      });
    },
    showDate() {
      if (this.info.goodsStatus === 3) {
        this.$toast("该商品已下架!");
      } else if (!this.isSeckill || this.isSeckill == "false") {
        this.gameStartTimeShow = true;
      } else {
        const goodInfo = this.info.skuItem;
        goodInfo.goodsId = this.id;
        goodInfo.bookDate = this.$utils.dayjs(new Date()).format("YYYY-MM-DD");
        goodInfo.salePrice = this.info.goodsPriceItem.salePrice;
        goodInfo.marketPrice = this.info.goodsPriceItem.marketPrice;
        goodInfo.supplierId = this.info.supplierDetailResponseDto.supplierId;
        goodInfo.companyName = this.info.supplierDetailResponseDto.companyName;
        goodInfo.goodsPic = this.info.goodsPic;
        goodInfo.goodsName = this.info.goodsName;
        goodInfo.scriptName = this.info.scriptName;
        goodInfo.companyPhone =
          this.info.supplierDetailResponseDto.companyPhone;
        goodInfo.shareUserId = this.shareUserId || "";
        goodInfo.isSeckill = this.isSeckill;
        uni.navigateTo({
          url: `/pagesB/order/confirmOrde?goodInfo=${JSON.stringify(goodInfo)}`,
        });
      }
    },
    buy() {
      this.$eventRecord(245);
      this.$u.throttle(this.confirmData, 500);
      if (!this.makeTimer) return this.$toast("请选择预定日期!");
      if (this.activeDates[this.makeTimer].stock === 0)
        return this.$toast("该日已预定完!");
      const goodInfo = this.activeDates[this.makeTimer];
      goodInfo.goodsId = this.id;
      goodInfo.bookDate = this.makeTimer;
      goodInfo.supplierId = this.info.supplierDetailResponseDto.supplierId;
      goodInfo.companyName = this.info.supplierDetailResponseDto.companyName;
      goodInfo.goodsPic = this.info.goodsPic;
      goodInfo.goodsName = this.info.goodsName;
      goodInfo.scriptName = this.info.scriptName;
      goodInfo.companyPhone = this.info.supplierDetailResponseDto.companyPhone;
      goodInfo.shareUserId = this.shareUserId || "";
      this.gameStartTimeShow = false;
      goodInfo.isSeckill = this.isSeckill;
      uni.navigateTo({
        url: `/pagesB/order/confirmOrde?goodInfo=${JSON.stringify(goodInfo)}`,
      });
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
.share {
  background: linear-gradient(to right, #e85240, #ef8269);
  padding: 10upx 30upx;
  border-radius: 50upx;
  // width: calc(100% - 50upx);
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
.card {
  position: absolute;
  top: 30%;
  width: 100%;
  min-height: 70%;
  background-color: #fff;
  border-radius: 32upx 32upx 0 0;
  background: linear-gradient(
    to bottom,
    rgba(255, 255, 255, 0.9) 0%,
    rgba(255, 255, 255, 1) 40%,
    rgba(255, 255, 255, 1) 100%
  );
  padding: 40upx 30upx 140upx 30upx;
  .title {
    font-size: 40upx;
  }

  .label {
    display: flex;
    line-height: 45upx;
    > view {
      display: inline-flex;
      margin-right: 10upx;
      color: #000000;
    }
    > .right {
      flex: none;
      flex-basis: 220upx;
    }
  }

  // .share-money{
  // 	padding:0 8upx;
  // 	border:1px solid #ff4f3a;
  // 	border-radius: 6upx;
  // }

  // .share-button{
  // 	margin:0 auto;
  // 	padding:0;
  // 	width: 112upx;
  // 	height:42upx;
  // 	border-radius:22upx;
  // 	line-height: 42upx;
  // 	font-size:26upx;
  // 	color:#333;
  // 	border: 1px solid #DEDEDE;
  // }

  .desc {
    display: flex;
    margin: 20upx 0;
    line-height: 45upx;
    margin-left: -12upx;
  }
  .intro {
    text-indent: 2em;
  }

  .supplier {
    width: 100%;
    background: #f6f5f5;
    border-radius: 16upx;
    padding: 25upx;
    .cates {
      text {
        padding: 4upx 16upx;
        border-radius: 20upx;
        color: #e92f29;
        background: linear-gradient(90deg, #fbedec 0%, #fff5f5 100%);
      }
    }
    .phone {
      width: 35upx;
      height: 35upx;
      margin-right: 40upx;
    }
    .address {
      width: 80%;
      .position {
        width: 28upx;
        height: 28upx;
        margin-right: 5upx;
      }
      text {
        width: 100%;
      }
    }
    .discount-price {
      display: inline-block;
      padding: 2upx 12upx;
      border: 1px solid #ff4f3a;
      border-radius: 4upx;
    }
  }
}

.foot {
  bottom: 0;
  left: 0;
  right: 0;
  height: 120upx;
  padding: 0 40upx;
  box-shadow: 0 0 10upx 0 #ccc;
  background: #fff;
  .discount-price {
    font-size: 56upx;
  }
  .buy {
    width: 220upx;
    height: 80upx;
    line-height: 80upx;
    text-align: center;
    background: linear-gradient(to right, #dd0829, #ff9b46);
    border-radius: 50upx;
  }
}
.footer-pop {
  height: 160upx;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  justify-items: center;
  padding: 0 76upx;
  padding-top: 30upx;
  .btn {
    flex-basis: 260upx;
    height: 100upx;
    line-height: 100upx;
    color: #000000;
    font-weight: bold;
    font-size: 36upx;
    text-align: center;
    background: rgba(153, 153, 153, 0.1);
    border-radius: 50px;
  }
  .confirm {
    background: linear-gradient(to right, #dd0829, #ff9b46);
    color: #fff;
  }
}
.cardCanvas {
  width: 500px;
  height: 400px;
  position: absolute;
  left: -1000px;
  bottom: 0;
}
</style>
