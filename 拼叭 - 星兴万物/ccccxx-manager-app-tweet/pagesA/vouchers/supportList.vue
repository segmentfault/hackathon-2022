<template>
  <view class="supportList">
    <!-- 暂时隐藏 -->
    <view class="card-tabs" v-if="0">
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
    <activityMerchant
      v-for="(item, i) in cardList"
      :merchantList="item"
      :key="i"
      :checkBool="false"
      :activityBool="activityBool"
      :money="money"
      :fiexdBool="false"
    ></activityMerchant>
  </view>
</template>

<script>
import activityMerchant from "../components/activityMerchant.vue";
import gameService from "../../service/game";
export default {
  components: {
    activityMerchant,
  },
  data() {
    return {
      cardList: [],
      activityId: "",
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
      ],
      activityBool: "",
      params: {
        pageNum: 1,
        pageSize: 100,
        typeId: null,
      },
      active: 0,
      money: "",
    };
  },
  onLoad(o) {
    if (o.item) {
      this.params.supplierIds = JSON.parse(o.item);
    }
    if (o.supplierType) {
      this.params.typeId = o.supplierType.join(",");
    }
    this.activityBool = o.activityBool;
    // this.params.activityId = o.activityId;
    this.money = o.money;
  },
  onShow() {
    this.getSupplierList();
  },
  methods: {
    handlerTab(item, index) {
      this.active = index;
      this.params.typeId = item.typeId;
      this.getSupplierList();
    },
    getSupplierList() {
      gameService.getSupplierList(this.params).then((res) => {
        res.forEach((v) => {
          v.cover = this.$pictureUrl(v.cover, 49, 49);
        });
        this.cardList = res;
      });
    },
    handlerDetail(item) {
      this.$eventRecord(138);
      uni.navigateTo({
        url: `/pagesA/businessDetail/index?supplierId=${item.supplierId}&typeId=${item.typeId}`,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.supportList {
  padding: 0 40rpx;
  background: #f9f9f9;
  .card-tabs {
    display: flex;
    flex-direction: row;
    margin: 0 auto;
    justify-content: space-between;
    margin-bottom: 32upx;
    .card-tab {
      flex: 1;
      border-radius: 36upx;
      font-size: 28upx;
      line-height: 50rpx;
      height: 50rpx;
      font-weight: 400;
      color: #000;
      text-align: center;
      border: 2upx solid #000;
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
      background: #000;
      border: none;
    }
  }
}
</style>
