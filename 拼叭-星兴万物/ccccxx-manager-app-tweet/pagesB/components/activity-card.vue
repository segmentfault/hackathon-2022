<template>
  <view class="activity-card__container aaa" @click="jumpDetail()">
    <image :src="list.cover" mode="aspectFill" class="card-image" />
    <view class="card-main">
      <view class="card-title"
        >【{{ list.typeName }}】{{ list.companyName }}</view
      >
      <view class="card-price">
        <view class="card-price-left">
          <view class="card-price--sale">{{ list.discountPrice }}</view>
          <view class="card-price--main"
            >门市价：¥{{ list.originalPrice }}</view
          >
        </view>
        <view class="card-price--tag">
          <image
            class="card-price--popularity"
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/f2d0608c-8923-41c3-b78a-3bb7ad1c28ae.png"
          />
          {{ list.popularity }}
        </view>
      </view>
      <view class="card-address">
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/ddba0665-0761-4564-aed5-e48a04adc57c.png"
          class="card-address--icon"
          mode="aspectFill"
        />
        <view class="card-address--text">{{ list.companyAddress }}</view>
        <!-- <view class="card-address--distance">500m</view> -->
      </view>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    list: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      img: "https://image.pinbar.vip/11901015.774431812/1.jpg?imageMogr2/crop/98x98/gravity/center",
    };
  },

  methods: {
    jumpDetail() {
      uni.navigateTo({
        url:
          "/pagesA/businessDetail/index?supplierId=" + this.list.supplierId + '&typeId=' + this.supplier.typeId,
      });
    },
  },
};
</script>

<style lang="scss">
.activity-card__container {
  display: flex;
  align-items: center;
  padding: 28upx;
  background: rgba(216, 216, 216, 0.1);
  border-radius: 16upx;
  .card-image {
    flex: none;
    width: 196upx;
    height: 196upx;
    border-radius: 24upx;
  }
  .card-main {
    flex: auto;
    width: 394upx;
    margin-left: 30upx;
  }
  .card-title {
    line-height: 44upx;
    font-weight: bold;
    color: #fff;
  }
  .card-price {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 22upx;
  }
  .card-price--popularity {
    width: 24upx;
    height: 28upx;
    margin-right: 6upx;
  }
  .card-price-left {
    flex: none;
    display: flex;
    align-items: flex-end;
  }
  .card-price--sale {
    flex: none;
    position: relative;
    color: #ff4f3a;
    line-height: 56upx;
    font-weight: bold;
    margin-right: 10upx;
    padding-left: 26upx;
    font-size: 40upx;
    &::before {
      content: "¥";
      position: absolute;
      left: 0;
      top: 8upx;
      font-size: 28upx;
      line-height: 40upx;
      font-weight: 400;
    }
  }
  .card-price--main {
    flex: none;
    padding-bottom: 12upx;
    font-size: 20upx;
    line-height: 28upx;
    color: #fff;
  }
  .card-price--tag {
    position: relative;
    font-size: 24upx;
    height: 52upx;
    display: flex;
    align-items: center;
    color: #e85858;
    padding-left: 30upx;
  }
  .card-address {
    display: flex;
    align-items: center;
    margin-top: 26upx;
    font-size: 24upx;
    line-height: 34upx;
    color: #fff;
    &--icon {
      flex: none;
      width: 28upx;
      height: 28upx;
      margin-right: 10upx;
    }
    &--text {
      flex: auto;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    &--distance {
      flex: none;
    }
  }
}
</style>
