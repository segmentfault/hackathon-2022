<template>
  <view class="create-merchant">
    <view class="imgs">
      <image
        class="imgs__header"
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/28f9156e-afe9-43f3-9745-b87a590ada94.png"
      />
      <image
        class="imgs__info"
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/9c18f57b-5643-459d-a448-1b44d0e892ce.png"
      />
      <image
        class="imgs__text"
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/1bad1c99-30bb-463a-a09d-c5007dfcc145.png"
      />
      <!-- <view class="merchant">活动商家正在准备中</view> -->
    </view>
    <view class="activity-list">
      <view class="activity-item" v-for="(item, i) in supplierList" :key="i">
        <activity-card :list="item" />
      </view>
    </view>
    <view class="none">
      <image
        class="none-img"
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/e08a584a-036a-438e-85f4-93f47cb04e68.png"
      />
      <text>已经到底了呢！</text>
    </view>

    <view class="create">
      <image
        class="create-img"
        @click="goCreate"
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/025f3af5-07f7-42ab-8d8a-5e6e74486c13.png"
      />
      <image
        class="create-img"
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/f4c1a241-1ce8-477a-bdd9-fc38a450a24f.png"
        @click="goIndex"
      ></image>
      <!-- <button @click="goIndex">加入别人的局</button> -->
    </view>

    <!-- <activityMerchant class="activityMerchant">
      <template #text>活动商家</template>
      <template #button>
        <view class="fiext">
          <view class="fiext-in">
            <button class="button" @click="goCreate">创建活动局</button>
          </view>
        </view>
      </template>
    </activityMerchant> -->
  </view>
</template>

<script>
import activityMerchant from "../components/activityMerchant.vue";
import activityCard from "../components/activity-card.vue";
import im from "../../service/im";
import GameService from "../../service/game";
export default {
  components: {
    activityMerchant,
    activityCard,
  },
  data() {
    return {
      url: "https://image-1306191496.cos.ap-nanjing.myqcloud.com/5163190c-05bb-4386-ac53-c062d9cf0542.png",
      supplierList: [],
    };
  },
	onLoad(o) {
		if(o.shareUserId && this.myUserId){
			GameService.bindRelation({parentId:o.shareUserId,parentType:1})
		}
	},
  onShow() {
    // 埋点
    let res = wx.getLaunchOptionsSync();
    res.scene = String(res.scene);
    this.$nextTick(() => {
      setTimeout(() => {
        if (res.scene === "1157") {
          // 埋点
          this.$eventRecord(86);
        } else if (res.scene === "1065") {
          // 埋点
          this.$eventRecord(91);
        } else if (res.scene === "1001") {
          this.$eventRecord(79);
        } else {
          this.$eventRecord(79);
        }
      }, 3000);
    });
  },
  created() {
    this.getSupplierList();
  },
  // 分享
  onShareAppMessage() {
    return {
      title: "小哥哥小姐姐喊你打本啦，剧本杀全场减30，快上车！",
      path: `/pagesA/create/index-merchant?shareUserId=${this.myUserId}`,
    };
  },
  methods: {
    goIndex() {
      uni.reLaunch({
        url: `/pages/index/index`,
      });
    },
    async goCreate() {
      await this.$authorization();
      let res = await GameService.currentActivity();
      uni.navigateTo({
        url: `/pagesA/create/index-game?activityId=${res.activityId}`,
      });
    },
    async getSupplierList() {
      this.supplierList = await GameService.getSupplierList({ activityId: 2 });
    },
  },
};
</script>

<style lang="scss" scopeds>
.create-merchant {
  width: 100%;
  background: #000;
  .imgs {
    &__header {
      width: 100vw;
      height: 772upx;
    }
    &__info {
      position: relative;
      margin-top: -380upx;
      margin-left: 26upx;
      width: 696upx;
      height: 658upx;
    }
    &__text {
      position: relative;
      margin-top: 20upx;
      width: 262upx;
      height: 56upx;
      margin-left: 50%;
      transform: translateX(-50%);
    }

    .merchant {
      line-height: 200upx;
      text-align: center;
      color: #fff;
    }
  }
  .none {
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 120upx;
    color: #fff;
    font-size: 23upx;
    font-weight: 300;
    margin-bottom: calc(142upx + env(safe-area-inset-bottom));
    .none-img {
      width: 64upx;
      height: 64upx;
      margin-right: 18upx;
    }
  }
  .create {
    display: flex;
    align-items: center;
    position: fixed;
    bottom: 0;
    width: 100%;
    left: 0;
    padding-bottom: env(safe-area-inset-bottom);
    background: #000;
    &-img {
      width: 100%;
      height: 92upx;
      margin: 26upx 20upx 26upx 25upx;
      &:last-child {
        margin-left: 0;
      }
    }
  }
  // 列表
  .activity-list {
    padding: 0 36upx;
  }
  .activity-item {
    & + .activity-item {
      overflow: hidden;
      margin-top: 32upx;
    }
  }
}
</style>