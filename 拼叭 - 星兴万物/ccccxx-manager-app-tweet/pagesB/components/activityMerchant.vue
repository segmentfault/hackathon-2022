<template>
  <view class="activity-merchant" @click="isCheckChage(merchantList, index)">
    <view class="merchantList">
      <view class="left">
        <image
          v-if="checkBool"
          :src="
            merchantList.isCheck
              ? 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/checkbox-checked.png'
              : 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/checkbox.png'
          "
          class="check"
        />

        <image :src="merchantList.cover" class="bg" />
      </view>
      <view class="right">
        <view class="title">【{{ merchantList.typeName }}】{{ merchantList.companyName }}</view>
				<view class="tags" v-if="merchantList.marks.length">{{merchantList.marks[0]}}</view>
				<view class="adress" @click.stop="openLocation">
				  <image
				    class="map-icon"
				    src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/card-place-icon.png"
				  />
				  {{ merchantList.companyAddress }}
				</view>
        <view class="introduce">
          <text class="discountPrice" v-if="checkBool">
            ￥{{ merchantList.discountPrice }}</text
          >
          <text class="discountPrice" v-else>
            ￥{{ merchantList.discountPrice }}</text
          >
          <view class="originalPrice"
            >门市价：<text style="text-decoration: line-through"
              >￥{{ merchantList.originalPrice }}</text
            ></view
          >
        </view>
      </view>
    </view>
    <view class="fiexd" @click="submit" v-if="fiexdBool">
      <view class="but">
        <slot name="button"></slot>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    // 商家数据
    merchantList: {
      type: Object,
      default: () => {
        return {};
      },
    },
    index: Number,
    default: () => {
      return 0;
    },
    // 是否展示选择框
    checkBool: {
      type: Boolean,
      default: () => {
        return true;
      },
    },
    // 是否展示底部按钮
    fiexdBool: {
      type: Boolean,
      default: () => {
        return true;
      },
    },
    // 是否是活动商家
    activityBool: {
      type: Boolean,
      default: () => {
        return true;
      },
    },
    money: {
      type: String,
      default: '20',
    },
  },
  data() {
    return {};
  },
  methods: {
    submit() {
      this.$emit("submit");
    },
    isCheckChage(obj) {
      this.$emit("changeCheck", {
        isCheck: this.merchantList.isCheck,
        index: this.index,
      });
    },
    openLocation() {
      // 打开地图显示位置
      uni.openLocation({
        latitude: Number(this.merchantList.lat),
        longitude: Number(this.merchantList.lng),
        name: this.merchantList.companyAddress,
      });
    },
  },
};
</script>

<style lang="scss">
.activity-merchant {
  padding: 28upx 40upx;
  background: #fff;
  border-radius: 16upx;
  margin-bottom: 15upx;
  .text {
    font-weight: 700;
    margin-bottom: 44upx;
    font-size: 30upx;
  }

  .merchantList {
    display: flex;

    .left {
      height: 196upx;
      display: flex;
      align-items: center;

      .check {
        width: 48upx;
        height: 48upx;
        margin-right: 28upx;
      }

      .bg {
        width: 196upx;
        height: 196upx;
        border-radius: 20upx;
      }
    }

    .right {
      width: 370upx;
      margin-left: 34upx;
      flex-direction: column;
      justify-content: space-between;

      .title {
        font-size: 30upx;
        font-weight: 700;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
				margin-bottom: 10upx;
      }

      .introduce {
        display: flex;
        align-items: center;
        .discountPrice {
          font-size: 35upx;
          color: #fe4a2a;
          margin-left: 0upx;
        }
        .originalPrice {
          color: #666;
          font-size: 24upx;
          margin-left: 30upx;
        }
      }

      .adress {
        position: relative;
        text-indent: 1.3em;
        padding: 10upx 16upx;
        display: inline-block;
        background: #ececec;
        border-radius: 30upx;
        font-size: 26upx;
        line-height: 36upx;
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
				width:100%;
        .map-icon {
          position: absolute;
          height: 30upx;
          width: 28upx;
          left: 16upx;
          top: 10upx;
        }

        color: #000000;
      }
    }
  }

  .fiexd {
    width: 100%;
    height: 120upx;
    position: fixed;
    bottom: 0;
    left: 0;
    padding: 14upx 56upx;
    background-color: #fff;
    button {
      border-radius: 48upx;
      background-color: #000;
      color: #fff;
    }
  }
	.tags{
		padding:6upx 18upx;
		display: inline-block;
		border-radius: 15px;
		background: #FEF3F3;
		color:#ff4f3a;
		font-size:24upx;
		margin-bottom: 10upx;
	}
}
</style>
