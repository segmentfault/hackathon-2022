<template>
  <view class="vouchers">
    <view class="card">
      <image
        class="card__icon"
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/145c14ea-a592-4380-a2bb-e5265f91193d.png"
      />
      <view class="card__info info">
        <text class="info__text">购买成功!</text>
        <view @click="jumpCoupon" class="info__view">查看我的优惠券 ></view>
      </view>
    </view>
    <vouchers-card />
    <view class="game">
      <!-- <text class="game__title">可使用券的商家</text> -->
      <!-- <view class="tab-bg" v-if="tabs && tabs.length">
        <u-tabs
          :list="tabs"
          height="72"
          bar-width="52"
          font-size="30"
          active-color="#000000"
          bold
          inactive-color="#666666"
          bg-color="#FFFFFF"
          :bar-style="barStyle"
          :current="current"
          @change="changeTab"
        />
      </view> -->
      <!-- <view class="card-item" v-for="(item, index) in list" :key="index">
        <barCard :bar="item" />
      </view> -->
      <!-- tab -->
      <!-- 支持商家列表 -->
      <view class="card-title"
        ><image
          class="img"
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/139728b1-5198-43b6-964e-9f77b533e38b.png"
        ></image
        ><image
          class="imgTwo"
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/7713e48c-d180-4961-98ab-2ce17a6d4e34.png"
        ></image
      ></view>
      <view class="card-tabs" v-if="false">
        <view
          class="card-tab"
          v-for="(item, index) in tabs"
          :key="index"
          :class="{ active: active === index }"
          @click="handlerTab(item, index)"
          >{{ item.name }}</view
        >
        <view class="card-tab card-tab-title"> 更多期待</view>
      </view>
      <ActivityMerchant
        v-for="(v, i) in list"
        :key="i"
        :checkBool="false"
        :fiexdBool="false"
        :merchantList="v"
        :activityBool="false"
      ></ActivityMerchant>
    </view>
    <!-- <view class="smallTip" v-if="!list.length">
      <div class="top">
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/e08bc21e-f918-4791-923b-d6b6b63260a3.png"
        ></image>
      </div>
      <view class="bottom">
        <view>暂无相关组局，自己发个组局试试</view>
      </view>
    </view> -->
    <view class="empty-tip">
      <image
        class="empty-icon"
        src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/empty-icon.png"
      ></image>
      已经到底了呢！
    </view>
  </view>
</template>
<script>
import vouchersCard from "@/pagesA/components/vouchers-card.vue";
import homeService from "../../service/home.js";
import barCard from "../../components/barCard.vue";
import ActivityMerchant from "../components/activityMerchant.vue";
import gameService from "../../service/game";
export default {
  components: {
    vouchersCard,
    barCard,
    ActivityMerchant,
  },
  data() {
    return {
      list: [],
      parms: {
        pageNum: 1,
        pageSize: 100,
				typeId:'',
				packageId:''
      },
      active: 0,
      tabs: [
        {
          name: "全部",
          typeId: null,
        },
        {
          name: "剧本杀",
          typeId: 2,
        },
        {
          name: "密室逃脱",
          typeId: 3,
        },
				{
				  name: "酒吧",
				  typeId: 1,
				},
      ],
    };
  },
	onLoad(o){
		if(o.typeId){
			this.parms.typeId = o.typeId
			this.parms.packageId = o.pid
		}
	},
  mounted() {
    // this.getGameList();
    this.getList();
  },
  methods: {
    // handlerTab(item, index) {
    //   this.active = index;
    //   this.parms.typeId = item.typeId;
    //   this.getList();
    // },
    // 跳转优惠卷
    jumpCoupon() {
      uni.navigateTo({
        url: "/pagesA/user/coupon",
      });
    },
    // 获取商家列表
    getList() {
      this.list = [];
      // const query = {
      //   pageNum: 1,
      //   pageSize: 100,
      //   activityId: 8,
      //   typeId: this.parms.typeId,
      // };
      gameService.getSupplierList(this.parms).then((res) => {
        this.list = res || [];
        // this.status = "nomore";
      });
    },
    // 获取局列表
    async getGameList() {
      let parms = {
        pageNum: 1,
        pageSize: 100,
      };
      let res = await homeService.gameList(parms);
      res.forEach((v, i) => {
        if (v.minPeople - 1 === v.members.length) {
          v.status = "1"; // 还差一人
        } else if (v.minPeople <= v.members.length) {
          //("达到最低人数");
          v.status = "2"; // 达到最低人数
        } else if (v.maxPeople === v.members.length) {
          //("达到最大人数");
          v.status = "3"; //  达到最大人数
        } else {
          v.states = "99";
        }
        if (v.status === "1") {
          res.splice(i, 1);
          res.unshift(v);
        }
        // 图片裁剪
        v.cover = this.$pictureUrl(v.cover, 666, 220);
        v.members.forEach((item, index) => {
          // 处理头像
          if (item.head && item.head.startsWith("https://image-1306191496")) {
            item.head += "?imageView2/1/w/80/h/80/q/80/webp";
          }
          // 群主默认第一
          if (item.identity === 1) {
            v.members.splice(index, 1);
            v.members.unshift(item);
          }
        });
      });
      this.list = res;
    },
    // 获取局的筛选项
    // async getTypeList() {
    //   let res = await homeService.typeList();
    //   this.tabs = [
    //     {
    //       name: "推荐",
    //       typeId: 0,
    //     },
    //     ...res,
    //   ];
    //   this.typeId = "";
    // },
  },
};
</script>
<style lang="scss" scoped>
.vouchers {
  background-color: #f7f7f7;
}
.card {
  color: #fff;
  background: linear-gradient(45deg, #b2394b 0%, #ea5768 100%);
  border-radius: 16upx;
  padding: 40upx 100upx;
  width: calc(100% - 80upx);
  margin-left: 40upx;
  display: flex;
  &__icon {
    width: 84upx;
    height: 84upx;
  }
  &__info {
    margin-left: 50upx;
  }
  .info {
    &__text {
      font-size: 40upx;
    }
    &__view {
      margin-top: 10upx;
      padding: 6upx 20upx;
      border: 2upx solid #fff;
      border-radius: 40upx;
      font-size: 28upx;
    }
  }
}
.game {
  padding: 40upx;

  &__title {
    display: block;
    font-weight: bold;
    font-size: 36upx;
    margin: 40upx 0 20upx 0;
  }
  .card-title {
    position: relative;
    text-align: center;
    font-size: 40upx;
    font-weight: bold;
    color: #450f17;
    margin: 40upx 0 100upx 0;
    .img,
    .imgTwo {
      width: 232upx;
      height: 12upx;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
    .img {
      width: 232upx;
      height: 12upx;
    }
    .imgTwo {
      width: 158upx;
      height: 40upx;
    }
  }
  .card-tabs {
    display: flex;
    flex-direction: row;
    margin: 0 auto;
    justify-content: space-between;
    margin-bottom: 32upx;
    margin-top: 94upx;
    .card-tab {
      flex: 1;
      border-radius: 36upx;
      font-size: 26upx;
      line-height: 1.5;
      font-weight: 400;
      color: #af3346;
      text-align: center;
      border: 2upx solid #af3346;
      margin-right: 20upx;
    }
    .card-tab-title {
      border: 0;
      font-size: 24upx;
      line-height: 48upx;
      margin-right: 0;
      text-align: right;
    }
    .active {
      color: #fff !important;
      background: #b03348;
    }
  }
}

.smallTip {
  .top {
    width: 180upx;
    height: 180upx;
    margin: 120upx auto 80upx;
    image {
      width: 100%;
      height: 100%;
    }
  }
  .bottom {
    text-align: center;
    color: #999;
  }
}
.empty-tip {
  margin-top: 39upx;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 26upx;
  font-family: PingFangSC-Light, PingFang SC;
  font-weight: 300;
  color: #000000;
	padding-bottom:30upx;
  .empty-icon {
    height: 64upx;
    width: 64upx;
    margin-right: 18upx;
  }
}
</style>
