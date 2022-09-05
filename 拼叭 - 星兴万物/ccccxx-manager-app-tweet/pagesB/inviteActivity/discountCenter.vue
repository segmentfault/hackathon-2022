<template>
  <view class="activity-discount-center">
    <comHead changeColor autoPadding :isHome="isHome">
      <view class="header">
        <text>组人组局用拼叭</text>
      </view>
    </comHead>
    <!-- 预约活动-->
    <subscribe
      v-if="activeStatus === statusArr[2]"
      @handlerSubscribe="handlerSubscribe"
      @handlerRule="handlerRule"
      :src="picture"
    />
    <!-- 好友助力 -->
    <haveSubscribedTo
      v-if="activeStatus === statusArr[0]"
      :share="share"
      :activeDetail="activeDetail"
      @handlerHelp="queryBoost"
      @handlerRule="handlerRule"
      :query="query"
      :src="picture"
    />
    <!-- 全部助力完成 -->
    <subscribeAlerady
      :query="query"
      :activeDetail="activeDetail"
      v-if="activeStatus === statusArr[1]"
    />
    <!-- 活动锁定 -->
    <activityOfLock
      v-if="activeStatus === statusArr[3]"
      :activeDetail="activeDetail"
      :query="query"
    />
    <!-- 单个他人助力成功 -->
    <powerSuccess :query="query" v-if="activeStatus === statusArr[4]" :activeDetail="activeDetail" />
    <haveAlerty v-if="activeStatus === statusArr[5]" :activeDetail="activeDetail" :query="query" />
    <card
      v-model="activeTab"
      style="width: 100%"
      :tabs="tabs"
      :cardList="cardList"
      @loadMore="getList"
      @handlerDetail="handlerDetail"
      :status="status"
      :query="query"
    ></card>
    <!-- 活动规则-->
    <rule v-model="stateRule" :rule="rule" />
    <!-- 助力弹出窗-->
    <dialogs v-model="isDialog" :dialogBoostUsers="dialogBoostUsers"></dialogs>
  </view>
</template>

<script>
import gameService from '../../service/game.js'
import comHead from '../../components/com-head.vue'
import card from './components/center/card.vue'
import rule from './components/center/rule.vue'
import subscribe from './components/center/subscribe.vue'
import haveSubscribedTo from './components/center/haveSubscribedTo.vue'
import subscribeAlerady from './components/center/subscribeAlerady.vue'
import activityOfLock from './components/center/activityOfLock.vue'
import powerSuccess from './components/center/powerSuccess.vue'
import LoginService from '../../service/login.js'
import haveAlerty from './components/center/haveAlerty.vue'
import dialogs from './components/center/dialog.vue'
export default {
  data() {
    return {
      stateRule: false,
      isDialog: false,
      dialogBoostUsers: [],
      activeTab: 'all',
      status: 'loadmore',
      tabs: [
        {
          name: '全部',
          value: 'all',
        },
        {
          name: '剧本杀',
          value: 2,
        },
        {
          name: '密室逃脱',
          value: 3,
        },
        {
          name: '酒吧',
          value: 1,
        },
      ],
      cardList: [],
      isHome: false,
      query: {
        pageNum: 1,
        pageSize: 10,
        activityId: 13,
      },
      activeDetail: {
        boostUsers: [],
      },
      picture: '',
      rule: '',
      shareUserId: '',
      shareTitle: '',
      sharePicture: '',
      share: false,
      statusArr: [2, 3, 4, 5, 1, 8],
      activeStatus: 2, // 1个人助力成功页面前端随便定的值 2助力中 3助力完成 4已取消报名 || 未报名  5已核销 1是表示自己已经助力过了
      gameId: null,
    }
  },
  onLoad(params = {}) {
    this.$eventRecord(150)
    if (params.gameId) this.gameId = Number(params.gameId)
    if (params.activityId) this.query.activityId = Number(params.activityId)
    if (params.isHome) {
      this.isHome = params.isHome
    }
    // 点击邀请卡片触发事件
    if (params.shareUserId) {
      this.$eventRecord(149)
      this.shareUserId = Number(params.shareUserId)
    }
    // this.init();
    this.getList()
    this.getActiveInfo()
  },
  onShow() {
    if (this.gameId) {
      this.init()
    }
  },
  components: {
    comHead,
    card,
    rule,
    subscribe,
    haveSubscribedTo,
    subscribeAlerady,
    activityOfLock,
    powerSuccess,
    dialogs,
    haveAlerty,
  },
  onReachBottom() {
    if (this.cardList.length >= this.query.pageNum * this.query.pageSize) {
      this.query.pageNum++
      this.getList()
    }
  },
  methods: {
    getActiveInfo() {
      gameService
        .queryActivityDetail(this.query.activityId)
        .then((res) => {
          this.picture = res.picture
          this.rule = res.rule
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 是否展示助力弹出窗
    showIsDialog(boostUsers) {
      if (boostUsers.length > 4) {
        boostUsers = boostUsers.slice(0, 4)
      }
      boostUsers.unshift({
        isNew: true,
        head: 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/boost-none.png',
      })
      this.dialogBoostUsers = boostUsers
      this.isDialog = true
    },
    //
    async init() {
      if (this.shareUserId && this.shareUserId !== this.myUserId) {
        // 登录
        await this.login(true)
      }
      if (!this.shareUserId) {
        await LoginService.defLogin()
        this.queryActivityDetail()
      }
      if (this.shareUserId === this.myUserId) {
        await this.queryActivityDetail()
      }
      if (this.shareUserId && this.myUserId) {
        gameService.bindRelation({ parentId: this.shareUserId, parentType: 1 })
      }
      uni.stopPullDownRefresh()
    },
    // 他人助力登录
    async login(share) {
      await LoginService.defLogin()
      await this.getShareActivityDetail()
      if (share) {
        this.share = this.myUserId !== this.shareUserId
      }
    },
    // Ta人助力
    async queryBoost() {
      this.$eventRecord(147)
      await this.$authorization()
      gameService
        .activityBoot(this.myUserId, this.gameId, this.shareUserId, 1)
        .then(() => {
          this.activeStatus = 1
        })
    },
    // 查看别人的助力是否完成
    getShareActivityDetail() {
      gameService
        .getGameActivity(this.gameId, this.shareUserId)
        .then((activeDetail) => {
          const newBoostUser = []
          const boostUsers = []
          activeDetail.boostUsers = activeDetail.boostUsers || []
          for (let i = 0; i < 5; i++) {
            if (
              activeDetail.boostUsers[i] !== undefined &&
              activeDetail.boostUsers[i].isNew
            ) {
              newBoostUser.push(activeDetail.boostUsers[i])
            } else {
              if (activeDetail.boostUsers[i] !== undefined) {
                boostUsers.push(activeDetail.boostUsers[i])
              }
            }
          }
          const isMy = activeDetail.boostUsers.some(
            (item) => item && item.userId === this.myUserId
          )
          // 状态都是从读取出来的 state
          if (isMy) {
            this.activeStatus = 8
          } else {
            this.activeStatus = activeDetail.state
          }
          if (newBoostUser.length) {
            boostUsers.unshift(...newBoostUser)
          } else {
            boostUsers.unshift({
              isNew: true,
              head: 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/boost-none.png',
            })
          }
          activeDetail.boostUsers = boostUsers
          this.activeDetail = activeDetail
          this.sharePicture = activeDetail.sharePicture
          this.shareTitle = activeDetail.shareTitle
        })
    },
    // 查看自己的助力是否完成
    queryActivityDetail() {
      gameService
        .getGameActivity(this.gameId)
        .then((activeDetail) => {
          const newBoostUser = []
          const boostUsers = []
          activeDetail.boostUsers = activeDetail.boostUsers || []
          for (let i = 0; i < 5; i++) {
            if (
              activeDetail.boostUsers[i] !== undefined &&
              activeDetail.boostUsers[i].isNew
            ) {
              newBoostUser.push(activeDetail.boostUsers[i])
            } else {
              if (activeDetail.boostUsers[i] !== undefined) {
                boostUsers.push(activeDetail.boostUsers[i])
              }
            }
          }
          // 判断助力情况 如果已经有4个及以上老用户助力 但无新用户 弹出提示
          if (boostUsers.length > 4 && !newBoostUser.length) {
            this.showIsDialog(boostUsers)
          }
          if (newBoostUser.length) {
            boostUsers.unshift(...newBoostUser)
          } else {
            boostUsers.unshift({
              isNew: true,
              head: 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/boost-none.png',
            })
          }
          activeDetail.boostUsers = boostUsers
          this.activeStatus = activeDetail.state
          this.activeDetail = activeDetail
          this.sharePicture = activeDetail.sharePicture
          this.shareTitle = activeDetail.shareTitle
        })
        .catch(() => {
          this.activeDetail.statue = '活动已结束'
          this.$toast('活动已结束')
        })
    },
    handlerSubscribe() {
      // 预约埋点
      this.$eventRecord(137)
      uni.navigateTo({
        url:
          '/pagesA/barActivity/applyCenter?activityId=' + this.query.activityId,
      })
    },
    handlerRule() {
      this.stateRule = !this.stateRule
    },
    goHome() {
      uni.reLaunch({
        url: '/pages/index/index',
      })
    },
    handlerDetail(item) {
      this.$eventRecord(138)
      uni.navigateTo({
        url: `/pagesA/activity/supplier-detail?supplierId=${item.supplierId}&activityId=${this.query.activityId}&activeStatus=${this.activeStatus}`,
      })
    },
    getList(params = {}) {
      if (params.status) {
        this.status = params.status
        this.query.pageNum = 1
      }
      if (params.cardList && Array.isArray(params.cardList))
        this.cardList = params.cardList
      const query = {
        ...this.query,
        ...params,
        typeId: this.activeTab === 'all' ? null : this.activeTab, // 酒吧活动
      }
      gameService.getSupplierList(query).then((res) => {
        res.forEach((v) => {
          v.cover = this.$pictureUrl(v.cover, 49, 49)
        })
        if (res && res.length) {
          this.cardList.push(...res)
          this.status = 'loadmore'
        } else {
          this.status = 'nomore'
        }
      })
    },
  },
  mounted() {
    console.log(this.myUserInfo)
  },
  // 分享必须放在主页面
  onShareAppMessage(e) {
    const that = this
    // 邀请预约埋点
    this.$eventRecord(142)
    // 自己邀请出去助力
    let path = `/pagesB/inviteActivity/discountCenter?shareUserId=${this.myUserId}&activityId=${this.query.activityId}&gameId=${this.gameId}&isHome=1`
    // 朋友邀请出去助力
    if (this.shareUserId) {
      path = `/pagesB/inviteActivity/discountCenter?shareUserId=${this.shareUserId}&activityId=${this.query.activityId}gameId=${this.gameId}&isHome=1`
    }
    return {
      title: that.shareTitle || '',
      path,
      imageUrl: that.sharePicture || '',
      // imageUrl:'https://image-1306191496.cos.ap-nanjing.myqcloud.com/c15f3e06-f942-4041-89dd-39c8e1e964d1.png',
    }
  },
  onPullDownRefresh() {
    this.init()
  },
}
</script>

<style lang="scss" scoped>
.activity-discount-center {
  min-height: 100vh;
  background: #131623;
  padding-bottom: 32rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: calc(20upx + env(safe-area-inset-bottom));
  .header {
    align-items: center;
    display: flex;
    font-size: 28upx;
    font-weight: 600;
    color: #fff;
  }
}
</style>
