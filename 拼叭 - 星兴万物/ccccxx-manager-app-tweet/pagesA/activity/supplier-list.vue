<template>
  <view class="supplier-list">
    <supplier-item
      dark
      :supplier="item"
      v-for="(item, i) in supplierList"
      :key="i"
    ></supplier-item>
  </view>
</template>

<script>
import GameService from "../../service/game.js";
import supplierItem from "./components/supplier-item.vue";
export default {
  components: {
    supplierItem,
  },
  data() {
    return {
      supplierList: [],
    };
  },
  onLoad() {
    this.getSupplierList();
  },
  methods: {
    getSupplierList() {
      return GameService.getSupplierList({}).then((res) => {
        this.supplierList = res;
      });
    },
  },
};
</script>

<style lang="scss">
.supplier-list {
  padding: 20upx;
  min-height: 100vh;
  background: #131623;
  display: flex;
  align-items: center;
  flex-direction: column;
}
</style>
