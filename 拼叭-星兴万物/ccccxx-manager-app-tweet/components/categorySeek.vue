<template>
  <view class="com-bg">
    <view class="flex-c">
      <view class="title">当前城市</view>
      <view class="city">长沙</view>
    </view>
    <view class="option-item" v-for="(item, idx) in paramList" :key="idx">
      <view class="title">{{ item.title }}</view>
      <view class="val-list">
        <view
          class="val-item"
          :class="paramObj[item.key] == val.val ? 'val-item-a' : ''"
          @click="paramObj[item.key] = val.val"
          v-for="(val, index) in item.value"
          :key="index"
          >{{ val.name }}</view
        >
      </view>
    </view>

    <view class="sub-btn" @click="subParam">确定</view>
  </view>
</template>

<script>
export default {
  props: {
    tabs: {
      type: Array,
      default: () => {
        return [];
      },
    },
  },
  data() {
    return {
      show: true,
      paramObj: {
        typeId: "", // 组局类型
        sortType: "",
        days: "",
        supplier:''
      },
      //具体参数自行配置
      paramList: [
        {
          title: "筛选条件",
          key: "sortType",
          value: [
            { name: "默认", val: "" },
            { name: "按距离", val: 2 },
            { name: "按价格", val: 3 },
          ],
        }
      ],
    };
  },
  methods: {
    changeTab(val, idx) {
      this.paramObj.typeId = val.typeId;
      this.$emit("changeTab", idx);
    },
    subParam() {
      // let minPeople = "";
      // let maxPeople = this.paramObj.joinType * 1;
      // if (maxPeople) {
      //   minPeople = maxPeople - 1;
      //   maxPeople == 10 && (minPeople = 8);
      // }
      let dest = JSON.parse(JSON.stringify(this.paramObj));
      // delete dest.joinType;
      this.$emit("subParam", {
        // maxPeople,
        // minPeople,
        ...dest,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.flex-c {
  display: flex;
  align-items: center;
}
.com-bg {
  position: relative;
  padding-bottom: 60upx;
  padding: 76upx 42upx calc(env(safe-area-inset-bottom) + 140upx) 42upx;
  .title {
    font-size: 26upx;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #999999;
  }
  .city {
    margin-left: 36upx;
    font-size: 26upx;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #333333;
  }
  .option-item {
    margin-top: 48upx;
  }
  .sub-btn {
    position: absolute;
    height: 84upx;
    line-height: 84upx;
    bottom: env(safe-area-inset-bottom);
    text-align: center;
    width: 652upx;
    background: #333333;
    border-radius: 26px;
    font-size: 40upx;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #ffffff;
    margin-bottom: 20rpx;
  }
  .val-list {
    display: flex;
    flex-wrap: wrap;
    .val-item {
      margin-top: 20upx;
      margin-right: 20upx;
      padding: 8upx 26upx;
      font-size: 26upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
      transition: all 0.3s;
      border-radius: 40upx;
      border: 2upx solid #979797;
    }
    .val-item-a {
      background: #333333;
      color: #fff;
      border: 2upx solid #333333;
    }
  }
}
</style>
