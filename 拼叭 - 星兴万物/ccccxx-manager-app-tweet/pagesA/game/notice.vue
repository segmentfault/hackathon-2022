<template>
  <view class="page-bg">
    <view
      class="list"
      :style="{ paddingTop: `${paddingTop + 10}px` }"
      v-if="list && list.length"
    >
      <view class="list-item" :key="i" v-for="(item, i) in list">
        <barCard :bar="item"></barCard>
      </view>
    </view>
  </view>
</template>

<script>
import barCard from "../../components/barCard.vue";
import { config } from "@/framework";
import homeService from "../../service/home.js";

export default {
  components: {
    barCard,
  },
  data() {
    return {
      paddingTop: config.topBarHeight,
      pageNum: 1,
      pageSize: 5,
      isEnd: false,
      list: [],
    };
  },

  mounted() {
    this.resetParms();
    this.myJoinList();
  },
  methods: {
    resetParms() {
      this.pageNum = 1;
      this.isEnd = false;
      this.list = [];
    },
    myJoinList() {
      homeService
        .myJoin({
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        })
        .then((res) => {
          this.list = [...this.list, ...res];
          this.isEnd = (res || []).length < this.pageSize;
        });
    },
  },
  onReachBottom() {
    if (!this.isEnd) {
      this.pageNum++;
      this.myJoinList();
    }
  },
};
</script>

<style scoped lang="scss">
.page-bg {
  position: relative;
  min-height: 100vh;
  width: 100%;
  background-color: #f5f5f5;
  .list {
    padding-top: 1upx;
    position: relative;
    margin: 0px 42upx;
    width: calc(100% - 84upx);
    .list-item {
      position: relative;
      margin-top: 28upx;
      width: 100%;
    }
  }
}
</style>
