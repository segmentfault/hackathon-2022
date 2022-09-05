<template>
  <view class="page-bg">
    <view class="invite-card">
      <view class="flex-c">
        <view class="invite-item">
          <view class="invite-item-n">{{
            (userNum && userNum.today) || 0
          }}</view>
          <view class="invite-item-t">当日拉新</view>
        </view>
        <view class="invite-item">
          <view class="invite-item-n">{{
            (userNum && userNum.month) || 0
          }}</view>
          <view class="invite-item-t">当月拉新</view>
        </view>
        <view class="invite-item">
          <view class="invite-item-n">{{ (userNum && userNum.all) || 0 }}</view>
          <view class="invite-item-t">累计拉新</view>
        </view>
      </view>
    </view>

    <view class="second-tab">
      <view
        @click="changeTabs(tab.id)"
        :class="['second-tab-item', { active: laxActive == tab.id }]"
        :key="tab.id"
        v-for="tab in laxTab"
        >{{ tab.name }}</view
      >
    </view>
    <template v-if="list && list.length">
      <user-item
        :showDetail="tabIdx === 0"
        v-for="(item, i) in list"
        :key="i"
        :user-info="item"
      ></user-item>
    </template>
    <view class="no-data" v-else> 暂无数据 </view>
  </view>
</template>

<script>
import userService from "../../service/user.js";
import userItem from "../components/user/user-item.vue";
export default {
  components: { userItem },
  data() {
    return {
      userNum: {},

      laxTab: [
        { name: "全部", id: 1 },
        { name: "队长", id: 2 },
        { name: "队员", id: 3 },
      ],
      laxActive: 1,
      pageNum: 1,
			pageSize:10,
      list: [],
      loading: false,
      finish: false,
    };
  },
  async mounted() {
    this.loading = true;

    await this.queryUserTotal();
    await this.loadingList();
  },
  onReachBottom() {
    if (this.finish){
			this.pageSize += 1;
			this.loading = true;
			this.loadingList();
		}
  },
  onShow() {
    // 埋点
    this.$eventRecord(68);
  },
  methods: {
    queryUserTotal() {
      return userService.queryUserTotal().then((res) => {
        this.userNum = res || {};
      });
    },
    loadingList() {
      return userService
        .queryUserRelation({
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          type: this.laxActive,
        })
        .then((res) => {
          this.list = this.list.concat(res);
          this.finish = this.list.length <= this.pageNum * this.pageSize;
          this.loading = false;
        });
    },
    changeTabs(tab) {
      this.pageNum = 1;
      this.list = [];
      this.finished = false;
      this.laxActive = tab;
      this.loadingList();
    },
  },
};
</script>

<style scoped lang="scss">
.second-tab {
  margin: 40upx 40upx 0upx 40upx;
  display: flex;
  align-items: center;
  &-item {
    margin-right: 20upx;
    padding: 8upx 24upx;
    line-height: 32upx;
    border-radius: 40upx;
    background: #999;
    color: #fff;
    font-size: 32upx;
  }
  .active {
    background: #000 !important;
  }
}
.flex-c {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}
.no-data {
  padding-top: 100upx;
  text-align: center;
}
.invite-card {
  position: relative;
  margin: 0upx 20upx 20upx 20upx;
  width: calc(100% - 40upx);
  padding: 0upx 40upx 0upx 40upx;
  background: #ffffff;
  border-radius: 16upx;
  &-t {
    font-size: 40upx;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #000000;
    line-height: 56upx;
  }
  .invite-item {
    margin-top: 58upx;
    flex: 1;
    &-n {
      font-size: 40upx;
      text-align: center;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #ff0000;
      line-height: 56upx;
    }
    &-t {
      margin-top: 18upx;
      text-align: center;
      font-size: 28upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #666666;
      line-height: 40upx;
    }
  }
  .invite-btn {
    height: 70upx;
    width: 256upx;
    margin-top: 40upx;
    position: relative;
    margin-left: auto;
    margin-right: auto;
    display: block;
  }
  .game-item {
    position: relative;
    overflow: hidden;
    margin-top: 28upx;
    padding: 20upx 24upx 40upx 20upx;
    background: linear-gradient(
      270deg,
      rgba(255, 255, 255, 0) 0%,
      #ffffff 100%
    );
    border-radius: 32upx;
    border: 2upx solid #979797;
    image {
      filter: blur(5px);
      opacity: 0.5;
      position: absolute;
      height: 100%;
      width: 100%;
      top: 0px;
      left: 0px;
      right: 0px;
      bottom: 0px;
    }
    &-bg {
      position: relative;
      z-index: 20;
    }
    &-title {
      font-size: 40upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
      line-height: 56upx;
    }
    &-time {
      display: flex;
      align-items: center;
      justify-content: space-between;
      &-v {
        font-size: 32upx;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #000000;
        line-height: 44upx;
      }
      &-state {
        height: 60upx;
        padding: 0px 24upx;
        border-radius: 30upx;
        line-height: 60upx;
        font-size: 32upx;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
        background: #000000;
      }
    }
    &-type {
      display: flex;
      flex-wrap: wrap;
      .type-item {
        margin-right: 10upx;
        margin-top: 14upx;
        padding: 3upx 12upx;
        background-color: rgba(0, 0, 0, 0.7);
        border-radius: 26upx;
        // border: 2upx solid #FFFFFF;
        font-size: 24upx;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
      }
    }
  }
}
</style>
