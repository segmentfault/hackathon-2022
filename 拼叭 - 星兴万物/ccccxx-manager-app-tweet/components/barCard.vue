<template>
  <view class="bar-card" @tap="$u.throttle(goDetail, 500)">
    <view class="bar-card-img">
      <u-image width="100%" height="100%" :src="bar.cover"></u-image>

      <!-- 官方标 -->
      <!-- <image class="guanfang-icon" src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/guanfang-icon.png"></image> -->
    </view>

    <view class="bar-card-info">
      <!-- 当前组局状态 -->
      <view class="slices">
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/1e82635e-3b61-4bdf-9ce0-0ffbfcf06fd2.png"
          v-if="bar.status === '1'"
        ></image>
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/172d97ec-8fdd-47a2-8fd6-ea6f962c5be1.png"
          v-else-if="bar.status === '2'"
        ></image>
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/31d95971-a0a5-43e8-8823-3dc13c295ff0.png"
          v-else-if="bar.status === '3'"
        ></image>
      </view>
      <view class="tag-list">
        <view class="tag-list-item">{{ bar.typeName }}</view>
        <view class="tag-list-item" v-if="bar.joinType === 3">密码房</view>
      </view>
      <view class="place">
        <view class="place-name">{{ bar.title }}</view>
        <!-- 阻止下冒泡 -->

        <!-- <button plain >
          <button class="share-icon">分 享</button>
        </button> -->
      </view>

      <view class="date clearfix">
        <view class="date-info"
          >{{ $formatGameTime(bar.gameStartTime) }} 开始
        </view>
        <button
          v-if="isShow"
          plain
          open-type="share"
          class="share-btn"
          :data-info="bar"
        >
          <image
            class="share-icon"
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/bbead4d4-1dd3-4e6d-a9a2-1d85e328b658.png"
          ></image>
        </button>
      </view>
      <view class="type">
        {{
          bar.payType === 1
            ? '局主请客'
            : $constants.PAY_TYPES.find((v) => v.value === bar.payType).name +
              ` ¥${bar.price}/人`
        }}
      </view>
      <view class="adress" @click.stop="openLocation">
        <image
          class="map-icon"
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/card-place-icon.png"
        />
        <view class="address-name">{{ bar.address }}</view>
      </view>
      <view class="type">
        已入局 {{ bar.members && bar.members.length }} 人 &nbsp;|&nbsp; 期待
        {{
          bar.minPeople == bar.maxPeople
            ? bar.minPeople
            : bar.minPeople + '~' + bar.maxPeople
        }}
        人
      </view>
      <view class="user-list" v-if="bar.members && bar.members.length">
        <view
          class="user-item"
          :class="item.sex == 1 ? 'boy' : 'girl'"
          v-for="(item, idx) in bar.members"
          v-show="idx < (bar.members > 5 ? 4 : 5)"
          :key="idx"
        >
          <image
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/bb0efde5-9b01-4f1e-8cd6-098b14cbdb7f.png"
            v-if="item.sex == 1"
          ></image>
          <image
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/6b966e81-2320-4fc0-ac43-c9bfabf5b149.png"
            v-else
          ></image>
          <view class="leader" v-if="item.identity === 1">局主</view>
          <avatar :avatarUrl="item.head"></avatar>
        </view>
        <view class="user-item more-user" v-if="bar.members.length > 5">
          +{{ bar.members.length - 5 }}
        </view>
      </view>
    </view>

    <view class="active-img" v-if="bar.activityId">
      <image
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/e0d271f0-29fa-43c1-9026-8d588119a7a2.png"
      ></image>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    bar: {
      type: Object,
      default: () => {
        return {};
      },
    },
    isShow: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {};
  },
  methods: {
    goDetail() {
			const isAuth = this.$authorization()
			if(!isAuth) return
      uni.navigateTo({
        url: `/pagesA/game/detail?gameId=${this.bar.gameId}`,
      });
      this.$emit('goDetail');
    },
    openLocation() {
      // 打开地图显示位置
      uni.openLocation({
        latitude: Number(this.bar.lat),
        longitude: Number(this.bar.lng),
        name: this.bar.address,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.bar-card {
  position: relative;
  margin-bottom: 30upx;
  height: 925upx;
  background-color: #fff;
  box-shadow: 0px 2px 38px 0px rgba(0, 0, 0, 0.1);
  border-radius: 32upx;
  overflow: hidden;
  padding-bottom: 40upx;

  &-img {
    position: relative;
    height: 720upx;
    width: 100%;
  }
  .active-img {
    position: absolute;
    top: 0;
    right: 0;
    image {
      width: 112upx;
      height: 112upx;
    }
  }

  .guanfang-icon {
    height: 64upx;
    width: 64upx;
    position: absolute;
    right: 22upx;
    top: 22upx;
  }

  .tag-list {
    display: flex;
    align-items: center;
    width: calc(100% - 72upx);

    &-item {
      background: rgba(0, 0, 0, 0.8);
      border-radius: 26upx;
      font-size: 24upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      padding: 6upx 22upx;
      color: #ffffff;
      margin-right: 10upx;
    }
  }

  &-info {
    padding: 0px 36upx;
    width: calc(100% - 72upx);
    position: absolute;
    bottom: 0;
    top: 350rpx;
    left: 0;
    width: 100%;
    background: linear-gradient(
      180deg,
      rgba(255, 255, 255, 0) 0%,
			rgba(255,255,255,.43) 20%,
      rgba(255, 255, 255, 0.86) 40%,
      #ffffff 61%,
      #ffffff 100%
    );
    .slices {
      width: 100%;
      height: 56upx;
      position: relative;
      image {
        width: 140upx;
        height: 100%;
        position: absolute;
        right: 0;
        top: 0;
      }
    }
    .place {
      margin-top: 18upx;
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 36upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;

      // .share-icon {
      //   height: 50upx;
      //   width: 120upx;
      //   color: #fff;
      //   font-size: 20upx;
      //   line-height: 50upx;
      //   background-color: #000;
      // }
    }
    .share-btn {
      width: 36upx;
      height: 36upx;
      float: right;
      .share-icon {
        width: 36upx;
        height: 36upx;
      }
    }
    .date {
      margin-top: 6upx;
      font-size: 32upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #000000;
      line-height: 44upx;
      .date-info {
        width: calc(100% -48upx);
        float: left;
      }
    }

    .type {
      margin-top: 24upx;
      font-size: 26upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #999999;
      line-height: 36upx;
    }

    .adress {
      position: relative;
      text-indent: 1.3em;
      padding: 0 16upx;
      padding-left: 0;
      display: inline-block;
      margin-top: 34upx;
      // background: #ececec;
      border-radius: 18upx;
      // height: 36upx;
      line-height: 44upx;
      font-size: 26upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      display: inline-block;
      overflow: hidden;
      height: 44rpx;
      color: #000000;

      .map-icon {
        position: absolute;
        height: 30upx;
        width: 28upx;
        left: 16upx;
        top: 6upx;
      }
      .address-name {
        background: #ececec;
        height: 44rpx;
        max-width: 600rpx;
        padding:0 16rpx;
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
         border-radius: 18upx;
      }
    }

    .user-list {
      margin: 20upx 0 38rpx;
      display: flex;
      align-items: center;

      .user-item:nth-child(6n) {
        margin-right: 0upx !important;
      }

      .user-item {
        flex: none;
        position: relative;
        height: 80upx;
        border-radius: 50%;
        margin-right: 20upx;
        width: 80upx;

        image {
          width: 26upx;
          height: 26upx;
          position: absolute;
          top: 30px;
          left: 12px;
        }

        img {
          border-radius: 50%;
          height: 100%;
          width: 100%;
          object-fit: cover;
          object-position: center;
        }

        .leader {
          position: absolute;
          display: inline-block;
          right: -10upx;
          top: -10upx;
          font-size: 16upx;
          color: #fff;
          padding: 3upx 12upx;
          background: #000000;
          border-radius: 28upx;
          border: 2upx solid #ffffff;
        }
      }

      .boy {
        border: 4upx solid #0076ff;
      }

      .girl {
        border: 4upx solid #d765d4;
      }

      .more-user {
        border-radius: 50%;
        background: #d8d8d8;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24upx;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #ffffff;
      }
    }
  }
}
</style>
