<template>
  <view class="supplier">
    <Tips></Tips>
    <view>
      <view class="flex-c select-btn" @click="changeSelectAll()">
        <image
          :src="
            selectedAll
              ? 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/checkbox-checked.png'
              : 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/checkbox.png'
          "
          class="checkbox"
        ></image>
        <view class="select-btn-t">任意商家</view>
      </view>
      <view v-for="(item, i) in list" :key="i" class="list">
        <ActivityMerchant
          ref="supplierList"
          :index="i"
          :merchantList="item"
          :list="list"
          @changeCheck="changeCheck"
          @submit="submit"
        >
          <template #button><button>确定</button></template>
        </ActivityMerchant>
      </view>
    </view>
  </view>
</template>

<script>
import GameService from "../../service/game";
import Tips from "./components/tips.vue";
import ActivityMerchant from "../components/activityMerchant.vue";
export default {
  components: {
    ActivityMerchant,
    Tips,
  },

  data() {
    return {
      list: [],
      typeId: 2,
      selectedAll: false,
      supplierIds: [],
      activityId:''
    };
  },

  onLoad(o) {
    this.typeId = o.typeId;
    this.activityId = o.activityId
    let supplierIds = [];
    this.supplierIds = [];
    if (o.supplierIds && !o.supplierIds.includes("null")) {
      supplierIds = o.supplierIds.split(",");
    }
    supplierIds.forEach((e) => {
      this.supplierIds.push(parseInt(e));
    });
    this.getSupplierList();
  },

  methods: {
    //   获取商家列表
    async getSupplierList() {
      this.list = [];
        const query = {
        pageNum: 1,
        pageSize: 100,
        activityId: this.activityId,
        typeId: this.typeId,
      };
      let res = await GameService.getSupplierList(query);

      res.forEach((e) => {
        e.cover = this.$pictureUrl(e.cover, 100, 100);
        if (this.supplierIds.includes(e.supplierId)) {
          e.isCheck = true;
        } else {
          e.isCheck = false;
        }
        this.list.push(e);
      });
    },
    changeSelectAll() {
      this.selectedAll = !this.selectedAll;
      if (this.selectedAll) {
        this.list.forEach((e) => {
          e.isCheck = true;
        });
      } else {
        this.list.forEach((e) => {
          e.isCheck = false;
        });
      }
    },
    changeCheck(e) {
      this.list[e.index].isCheck = !this.list[e.index].isCheck;
    },
    submit() {
      let supplierIds = [];
      this.list.forEach((e) => {
        if (e.isCheck) {
          supplierIds.push(e.supplierId);
        }
      });
      supplierIds = supplierIds.join();
      const pages = getCurrentPages();
      const prevPage = pages[pages.length - 2];
      uni.navigateBack();
      prevPage.onLoad({ supplierIds });
    },
  },
};
</script>

<style lang='scss'>
.supplier {
  .flex-c {
    display: flex;
    align-items: center;
  }
  .list{
    &:last-child{
      margin-bottom: 130upx;
    }
  }
  .select-btn {
    margin-top: 30upx;
    margin-left: 40upx;

    .checkbox {
      height: 48upx;
      width: 48upx;
    }

    &-t {
      margin-left: 24upx;
      font-size: 36upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #000000;
    }
  }
}
</style>