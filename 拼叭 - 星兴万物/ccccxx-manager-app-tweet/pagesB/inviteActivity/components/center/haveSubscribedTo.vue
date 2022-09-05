<template>
  <view>
    <!-- 顶部说明文案 -->
    <banner :src="src" @handlerRule="$emit('handlerRule')"></banner>
    <view class="invite-box">
      <view class="invite-box-header">
        <view class="invite-box-header-item" v-for="(item, index) in headerList" :key="index">
          <view class="circle" :class="{ active: headerActive === index }">{{ index + 1 }}</view>
          <view class="text" :class="{ 'active-text': headerActive === index }">{{ item.name }}</view>
        </view>
      </view>
      <view class="invite-box-border"></view>
      <view class="progress">
        <progress />
      </view>
      <view
        class="text"
        style="margin-bottom: 42upx"
      >还差 {{ isNewUsers ? 1 : 5 - showNum.length }} 位解锁半价券～</view>
      <boostUser :boostUsers="activeDetail.boostUsers" />
      <!-- <view class="count-down-wrapper">
        <count-down :timestamp="(activeTime.applyEndTime - activeTime.currentTime) / 1000"></count-down>
      </view>-->
      <!-- 邀请助力按钮-->
      <button class="subscribe" v-if="!share" open-type="share"></button>
      <button class="subscribes" v-else @click="$emit('handlerHelp')"></button>
      <view class="btn" v-if="!share" @click="lookHistory()">查看历史助力＞</view>
    </view>
  </view>
</template>
<script>
import gameService from '../../../../service/game.js'
import progress from './progress.vue'
import banner from './banner.vue'
import boostUser from './boost-users.vue'
export default {
  components: {
    progress,
    boostUser,
    banner,
  },
  data() {
    return {
      headerActive: 1,
      activeTime: {},
      headerList: [
        {
          name: '预约活动',
        },
        {
          name: '好友助力',
        },
        {
          name: '到店使用券',
        },
      ],
    }
  },
  components: {
    banner,
    boostUser,
  },
  props: {
    src: {
      type: String,
      required: true,
    },
    query: {
      type: Object,
    },
    activeDetail: {
      type: Object,
      default: () => {
        return {
          boostUsers: [],
        }
      },
    },
    share: {
      type: Number,
      default: false,
    },
  },
  mounted() {
    // this.queryActivityDetail()
  },
  computed: {
    showNum() {
      return this.activeDetail.boostUsers.filter((item) => item.nickName)
    },
    isNewUsers() {
      return (
        this.activeDetail.boostUsers.length >= 5 &&
        !this.activeDetail.boostUsers.filter(
          (item) => item.nickName && item.isNew
        ).length
      )
    },
  },
  methods: {
    // 查看历史助力
    lookHistory() {
      uni.navigateTo({
        url: `/pagesA/activity/my-popularity?activityId=${this.query.activityId}`,
      })
    },
    queryActivityDetail() {
      gameService.queryActivityDetail(this.query.activityId).then((res) => {
        // 最多百分之百
        if (res.progressRate > 100) {
          res.progressRate = 100
        }
        this.activeTime = res
      })
    },
    getBoost() {
      gameService.boost()
    },
    handlerOpen() {
      this.$eventRecord(143)
      uni.navigateTo({
        url: `/pagesB/inviteActivity/applyCenter?activityId=${this.query.activityId}&state=${this.activeDetail.state}`,
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.subscribe {
  width: 608upx;
  height: 88upx;
  background: url('https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/btn-share.png')
    no-repeat center;
  background-size: 100% 100%;
  margin: 0 auto;
  margin-top: 20rpx;
  z-index: 1;
}
.subscribes {
  width: 608upx;
  height: 88upx;
  background: url('https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/btn.png')
    no-repeat center;
  background-size: 100% 100%;
  margin: 0 auto;
  margin-top: 20rpx;
  z-index: 1;
}
.subscribe-text {
  margin-top: 28upx;
  margin-bottom: 72upx;
  font-size: 26upx;
  font-weight: 500;
  color: #ffffff;
  line-height: 26upx;
  text-align: center;
}
.invite-box {
  margin-top: 380rpx;
  width: 680upx;
  // height: 798upx;
  background: #f9f9f9;
  border-radius: 16upx;
  margin-bottom: 16upx;
  position: relative;
  padding-bottom: 24upx;
  &-border {
    height: 36upx;
    background: linear-gradient(
      180deg,
      rgba(252, 248, 248, 0.4) 0%,
      #d7c9c9 100%
    );
  }
  .progress {
    margin-top: 52upx;
    margin-bottom: 80upx;
  }
  .text {
    font-size: 28upx;
    color: #000000;
    text-align: center;
    // margin-bottom: 46upx;
  }
  &-header {
    height: 102upx;
    display: flex;
    flex-direction: row;
    align-items: center; /*垂直居中*/
    justify-content: center; /*水平居中*/
    padding: 0 28upx;
    z-index: 1;
    background-color: #fff;
    &-item {
      flex: 1;
      .circle {
        display: inline-block;
        width: 48upx;
        height: 48upx;
        background: #ffe5e5;
        font-weight: 600;
        line-height: 48rpx;
        text-align: center;
        border-radius: 100%;
        margin-right: 20upx;
        color: #ff9696;
        font-weight: bold;
      }
      .text {
        display: inline-block;
        color: #999999;
        font-size: 24upx;
        font-weight: bold;
      }
      .active {
        background: linear-gradient(
          180deg,
          #fe4a2b 0%,
          #cd6cfe 100%
        ) !important;
        color: #ffffff !important;
      }
      .active-text {
        color: #000000 !important;
      }
    }
  }
}
.btn {
  width: 680upx;
  height: 88upx;
  line-height: 88upx;
  border-radius: 16upx;
  color: #000000;
  font-size: 28upx;
  background: #fff;
  text-align: center;
  font-weight: bold;
}
.count-down-wrapper {
  margin-bottom: 22upx;
  margin-top: 42upx;
}
</style>