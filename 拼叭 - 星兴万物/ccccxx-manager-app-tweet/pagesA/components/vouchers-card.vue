<template>
  <view class="card-list">
    <view class="card" v-for="(item, index) in cardList" :key="index" @click="handlerCard(item)">
      <image :src="item.bg" v-if="item.bg" alt />
      <view class="card-title">
        <text>{{ item.title }}</text>
        <image
          class="right"
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/bfb89e5c-2ff4-4945-8f8d-64ae6f6b0217.png"
        />
      </view>
      <view class="card-desc">{{ item.desc }}</view>
    </view>
  </view>
</template>
<script>
import GameService from '../../service/game'
export default {
  props: {
    activityId: {
      type: Number,
      default: 7,
    },
    cardList: {
      type: Array,
      default: () => {
        return [
          // {
          // bg: 'https://image-1306191496.cos.ap-nanjing.myqcloud.com/a219e47d-fff0-45b6-8e8e-f78f3de982b4.png',
          // title: '潮玩卡',
          // url: '/pagesA/vouchers/buy',
          // desc: '最高立省350元',
          // },
          {
            bg: 'https://image-1306191496.cos.ap-nanjing.myqcloud.com/aed88ace-c463-4bbc-9352-dced8f4642ec.png',
            title: '邀请返现',
            url: '/pagesA/vouchers/invitation',
            desc: '好友买券包给你5-15元',
          },
        ]
      },
    },
  },
  methods: {
    handlerCard(item) {
      this.$emit('handlerCard', item)
      if (item.url === '/pages/index/index') {
        uni.reLaunch({
          url: item.url,
        })
      } else if (item.title.includes('一键创建局')) {
        this.oneKeyCreate()
      } else {
        if (item.url == '/pagesA/vouchers/buy') {
          this.$eventRecord(153)
        } else if (item.url == '/pagesA/vouchers/invitation') {
          this.$eventRecord(154)
        }
        uni.navigateTo({
          url: item.url,
        })
      }
    },

    // 一键创建局
    oneKeyCreate() {
      GameService.releaseActivity(this.activityId).then((res) => {
        if (res.tag === 1 && res.gameId) {
          uni.navigateTo({
            url: '/pagesA/game/detail?gameId=' + res.gameId,
          })
        } else {
          uni.navigateTo({
            url: '/pagesA/create/index?activityId=' + this.activityId,
          })
        }
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.card-list {
  margin: 20upx 20upx 0 20upx;
  display: flex;
  padding: 20upx 10upx 30upx 10upx;
  justify-content: space-between;
  border-radius: 16upx;
  background: #fff;
  .card {
    width: 320upx;
    height: 120upx;
    position: relative;
    background: #ffd8d8;
    border-radius: 16upx;
    overflow: hidden;
    .card-title {
      position: absolute;
      right: 0;
      left: 32upx;
      top: 26upx;
      font-size: 28upx;
      font-weight: 600;
      color: #000000;
      .right {
        width: 24upx;
        height: 24upx;
        margin-left: 15upx;
      }
    }
    .card-desc {
      position: absolute;
      top: 66upx;
      left: 32upx;
      font-size: 20upx;
      color: #000000;
    }
    > image {
      width: 100%;
      height: 100%;
    }
  }
}
</style>