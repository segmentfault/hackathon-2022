<template>
  <view class="page-bg">
    <comHead></comHead>
    <view class="activity-image">
      <view class="title">选择你的标签</view>
      <image
        class="img-bg"
        src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/game/tag-bh.png"
      ></image>
    </view>

    <view class="tag-list">
      <view
        class="tag-item"
        :class="
          activeList.findIndex((v) => v.id == item.interestId) != -1
            ? 'tag-item-a'
            : ''
        "
        @click="checkTag(item)"
        v-for="item in tagList"
        >{{ item.caption }}</view
      >
    </view>

    <view class="btm-tbn">
      <view class="goNext" v-if="from == 'guide'">跳过</view>
      <view
        class="sub-btn"
        :class="activeList.length ? 'can-sub' : ''"
        @click="subTags"
        >确定</view
      >
    </view>
  </view>
</template>

<script>
import comHead from "../../components/com-head.vue";
import userService from "../../service/user.js";
export default {
  components: {
    comHead,
  },
  data() {
    return {
      from: "", //来源：1引导页
      tagList: [],
      activeList: [],
    };
  },
  onLoad(e) {
    let { from = "" } = e;
    this.from = from;

    this.queryTagList();
  },
  methods: {
    queryTagList() {
      return userService.queryTagList().then((res) => {
        this.tagList = res || [];
      });
    },
    checkTag(data) {
      let idx = this.activeList.findIndex((v) => v.id == data.interestId);
      if (idx == -1) {
        this.activeList.push({
          name: data.caption,
          id: data.interestId,
        });
        return;
      }
      this.activeList.splice(idx, 1);
    },
    subTags() {
      if (!this.activeList.length) {
        return;
      }
      //通知个人中心设置页
      uni.$emit("updateTag", this.activeList);
      uni.navigateBack();
    },
  },
};
</script>

<style scoped lang="scss">
.page-bg {
  min-height: 100vh;
  width: 100%;
  background-color: #fff;
  .activity-image {
    position: relative;
    height: 368upx;
    width: 100%;
    .img-bg {
      height: 100%;
      width: 100%;
    }
    .title {
      position: absolute;
      left: 54upx;
      bottom: 60upx;
      font-size: 40upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ffffff;
    }
  }
  .tag-list {
    padding-top: 40tpx;
    margin-left: 54upx;
    margin-right: 54upx;
    width: calc(100% - 108upx);
    .tag-item {
      display: inline-block;
      padding: 7upx 28upx;
      font-size: 32upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      margin-right: 24upx;
      margin-top: 40upx;
      border-radius: 38upx;
      border: 2upx solid #dedede;
    }
    .tag-item-a {
      background: #333333 !important;
      opacity: 1 !important;
      color: #fff;
    }
  }
  .btm-tbn {
    position: absolute;
    bottom: 182upx;
    margin: 0px 54upx;
    width: calc(100% - 108upx);
    display: flex;
    align-items: center;
    justify-content: space-between;
    .goNext {
      margin-right: 28upx;
      padding: 18upx 0px;
      width: 200upx;
      flex: none;
      font-size: 40upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #333333;
      text-align: center;
      border: 2upx solid #dedede;
      border-radius: 46upx;
    }
  }
  .sub-btn {
    width: 100%;
    padding: 20upx 0px;
    text-align: center;
    background: #333333;
    border-radius: 46upx;
    opacity: 0.5;
    color: #fff;
    font-size: 40upx;
  }
}
.can-sub {
  background: #333333 !important;
  opacity: 1 !important;
  color: #fff !important;
}
</style>
