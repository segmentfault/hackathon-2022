<template>
  <view class="gameList">
    <barCard v-for="(v, i) in gameList" :key="i" :bar="v"></barCard>
  </view>
</template>

<script>
import barCard from "../../components/barCard.vue";
import homeSerice from "../../service/home";
export default {
  components: {
    barCard,
  },
  data() {
    return {
      activityId: "",
      gameList: [],
    };
  },
  onLoad(o) {
    this.activityId = o.activityId;
    this.getGameList();
  },
  methods: {
    getGameList() {
      let params = {
        pageNum: 1,
        pageSize: 100,
        activityId: this.activityId,
      };
      homeSerice.gameList(params).then((res) => {
        this.gameList = res;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.gameList {
  padding: 0 40rpx;
}
</style>
