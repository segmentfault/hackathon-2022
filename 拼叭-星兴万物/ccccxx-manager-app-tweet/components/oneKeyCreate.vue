<template>
  <view v-if="value">
    <view class="body">
      <text class="body__text">您已有一条预约信息，是否一键创建组局？</text>
      <view
        class="card"
        :style="{ height: applyDetail.cates.length > 2 ? '380upx' : '352upx' }"
      >
        <view v-if="applyDetail.monopoly === 1" class="card__text1"
          >{{ applyDetail.boyNum }}男{{ applyDetail.girlNum }}女，{{
            applyDetail.gameType === 2 ? "剧本杀" : "密室"
          }}</view
        >
        <view v-else class="card__text1"
          >拼局，{{ applyDetail.gameType === 2 ? "剧本杀" : "密室" }}</view
        >
        <view>
          <text
            :key="item"
            v-for="item in applyDetail.cates"
            class="card__text2"
            >{{ item }}</text
          >
          <text v-if="applyDetail.cates.length <= 2" class="card__tex31"
            >{{ date(applyDetail) }} 下午 开始</text
          >
          <view v-else class="card__tex31" style="margin-bottom: 10px"
            >{{ date(applyDetail) }} 下午 开始</view
          >
        </view>
        <!-- 用户列表-->
        <view
          class="card__user"
          v-if="applyDetail.boostUsers && applyDetail.boostUsers.length > 0"
        >
          <view
            class="list"
            v-for="(item, i) in applyDetail.boostUsers"
            v-if="i < 5"
          >
            <image
              class="isNew"
              v-if="item.isNew"
              src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fnew.png"
            />
            <image
              class="isNew"
              v-else
              src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fnot_new.png"
            />
            <image class="head-image" :src="item.head"></image>
            <image
              v-if="item.sex === 1"
              class="head-sex"
              src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/3f1b294a-0f6e-48a1-8671-333ac929f3ef.png"
            />
            <image
              v-else-if="item.sex === 2"
              class="head-sex"
              src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/85c5424e-13a6-4863-9d59-4573c8b7f325.png"
            />
          </view>
          <view
            class="list"
            v-if="applyDetail.boostUsers.length > 5"
            @click="openPopularity"
          >
            <view class="head-image"
              ><text class="head-image-text"
                >+{{ applyDetail.boostUsers.length - 5 }}</text
              ></view
            >
          </view>
        </view>
      </view>
      <view class="body-btn">
        <view @click="jumpCreate(1)" class="body-btn__1">创建新局</view>
        <view @click="jumpCreate(2)" class="body-btn__2">一键组局</view>
      </view>
    </view>
    <view @click="$emit('input', false)" class="shadow"></view>
  </view>
</template>

<script>
import { utils } from "@/framework";
import GameService from "@/service/game";

export default {
  props: {
    value: {
      type: Boolean,
      default: false,
    },
    applyDetail: {
      type: Object,
      default: {},
    },
  },
  computed: {
    time() {
      if (this.value === false) {
        return "";
      }
      if (this.applyDetail.hours.length === 3) {
        return "不限时间";
      } else {
        let text = "";
        this.applyDetail.hours.forEach((e) => {
          text += `${e.startHour}至${e.endHour}点 `;
        });
        return text + "开始";
      }
    },
  },
  methods: {
    date(applyDetail) {
      if (!applyDetail.arriveDate) {
        return "10/1至10/3";
      } else {
        let DAY = new Date(applyDetail.arriveDate);
        let weekArr = ["日", "一", "二", "三", "四", "五", "六"];
        let md = utils.dayjs(DAY).format("MM/DD");
        let d = utils.dayjs(DAY).format("d");
        let t = utils.dayjs(DAY).format("HH:mm");
        return `${md} 周${weekArr[d]}`;
      }
    },
    openPopularity() {
      uni.navigateTo({
        url: "/pagesA/activity/my-popularity",
      });
    },
    jumpCreate(tab) {
      if (tab === 1) {
        uni.navigateTo({
          url: "/pagesA/create/index?activityId=7",
        });
        this.$eventRecord(100);
      } else {
        GameService.releaseActivity(7).then((res) => {
          if (res.tag === 1 && res.gameId) {
            uni.navigateTo({
              url: "/pagesA/game/detail?gameId=" + res.gameId,
            });
          } else {
            uni.navigateTo({
              url: "/pagesA/create/index?activityId=7",
            });
          }
          this.$eventRecord(101);
        });
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.body {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 670upx;
  background: #fff;
  border-radius: 10upx;
  z-index: 9999;
  &__text {
    display: block;
    padding: 40upx 120upx;
    text-align: center;
    font-size: 38upx;
  }
  .card {
    background: #fdefef;
    margin-left: 30upx;
    width: calc(100% - 60upx);
    padding: 20upx;
    &__text1 {
      margin-top: 32upx;
      font-size: 38upx;
    }
    &__text2 {
      color: #fff;
      padding: 5upx 21upx;
      background: #333;
      line-height: 80upx;
      border-radius: 20upx;
      margin-right: 10upx;
    }
    &__tex31 {
      margin-left: 26upx;
      font-size: 24upx;
      font-weight: 500;
      color: #000000;
    }
    &__user {
      height: 108upx;
      display: flex;
      .list {
        width: calc(100% -15upx);
        margin: 0 10upx;
        .isNew {
          height: 28upx;
          width: 72upx;
          position: absolute;
        }
        .head-image {
          display: flex;
          width: 70upx;
          height: 70upx;
          border-radius: 50upx;
          border: 2upx solid #999;
          margin: 28upx 0;
          justify-content: center;
        }
        .head-sex {
          width: 22upx;
          height: 22upx;
          position: absolute;
          margin: -38upx 0 0 26upx;
        }
        .head-image-text {
          margin-top: 10upx;
        }
      }
    }
  }
  .body-btn {
    margin: 50upx;
    width: 570upx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    &__1 {
      display: block;
      width: 260upx;
      color: #333;
      border: 1px solid #333;
      border-radius: 60upx;
      text-align: center;
      background: #fff;
      line-height: 100upx;
    }
    &__2 {
      width: 260upx;
      display: block;
      color: #fff;
      background: #333;
      border: 1px solid #333;
      border-radius: 60upx;
      text-align: center;
      line-height: 100upx;
    }
  }
}
.shadow {
  position: fixed;
  z-index: 9998;
  left: 0;
  top: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
}
</style>
