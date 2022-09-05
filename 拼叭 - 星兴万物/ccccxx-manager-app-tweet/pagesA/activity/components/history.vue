<template>
  <view class="history">
    <comHead changeColor>助力用户</comHead>
    <template v-if="!item">
      <view class="historyList" v-for="(v, i) in item" :key="i">
        <view class="left">
          <image :src="v.head"></image>
        </view>
        <view class="right">
          <view class="name"
            >{{ v.nickName }}
            <text>新用户</text>
            <view class="time"> {{ v.createTime }}</view>
          </view>
          <view class="msg">来自{{ v.boostSource }}助力</view>
        </view>
      </view>
    </template>
    <template v-else>
      <view class="smallTip">
        <div class="top">
          <image
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/e08bc21e-f918-4791-923b-d6b6b63260a3.png"
          ></image>
        </div>
        <view class="bottom">
          <view>暂无消息</view>
        </view>
      </view>
    </template>
  </view>
</template>

<script>
import comHead from "../../../components/com-head.vue";
import gameService from "../../../service/game";
import moment from "moment";
export default {
  components: {
    comHead,
  },
  data() {
    return {
      item: "",
      // 助力列表参数
      boostList: {
        pageNo: 1,
        pageSize: 10,
      },
    };
  },
  created() {
    this.getQueryBoostList();
  },
  methods: {
    async getQueryBoostList() {
      let res = await gameService.queryBoostList(this.boostList);
      res.list.filter((v) => moment(v.createTime).format("YYYY-MM-DD HH:mm"));
      this.item = res.list;
    },
  },
};
</script>

<style lang='scss'>
.history {
  .historyList {
    display: flex;
    align-items: center;
    margin-left: 30upx;
    padding: 20upx 0;
    border-bottom: 2upx solid #000;
    .left {
      width: 100upx;
      height: 100upx;
      image {
        width: 100%;
        height: 100%;
        border-radius: 50upx;
      }
    }
    .right {
      flex: 1;
      margin: 0 20upx;
      .name {
        display: flex;
        justify-content: space-between;
        position: relative;
        text {
          position: absolute;
          top: 0;
          left: 110upx;
          padding: 5upx 10upx;
          background-color: #000;
          color: #fff;
          border-radius: 16upx;
          font-size: 24upx;
        }
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
}
</style>