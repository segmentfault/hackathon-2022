<template>
  <view class="game-created">
    <view class="title">创建那种局?</view>
    <view class="created-tab">
      <u-tabs
        :list="typeList"
        bg-color="#fff"
        :show-bar="false"
        :active-item-style="activeLitemStyle"
        :is-scroll="false"
        :current="activeTypeId"
        @change="handlerChange"
      ></u-tabs>
    </view>
    <view class="conent">
      <view class="title">填写局详情</view>
      <cmdCellItem
        title="日期"
        @click="handlerCell($event, 'gameStartTime', 181)"
        arrow
        slotRight
        :border="false"
      >
        <view
          class="custom-input"
          :class="[{ 'value-class': form.gameStartTime }]"
        >{{ form.gameStartTime ? showGameStartTime(form.gameStartTime) : '请选择日期' }}</view>
        <!-- <view class="active-class">{{ form.activityName ? `(${form.activityName})` : '' }}</view> -->
      </cmdCellItem>
      <cmdCellItem title="同行人数" slotRight>
        <view
          class="custom-input center"
          :class="[{ 'value-class': form.boyNum }]"
          @click="handlerCell($event, 'boyNum', 182)"
        >{{ form.boyNum }}</view>
        <view class="custom-input center value-class">男</view>
        <view
          class="custom-input center"
          @click="handlerCell($event, 'girlNum', 182)"
          :class="[{ 'value-class': form.girlNum }]"
        >{{ form.girlNum }}</view>
        <view class="custom-input center value-class">女</view>
      </cmdCellItem>
      <cmdCellItem title="商家/地点" @click="handlerCell($event, 'address', 183)" slotRight arrow>
        <view
          class="custom-input"
          :class="[{ 'value-class': form.address }]"
        >{{ form.address || '请选择商家地点' }}</view>
      </cmdCellItem>
      <cmdCellItem
        :title="`选择${form.typeId == 2 ? '剧本' : '主题'}`"
        @click="handlerCell($event, 'businessId', 184)"
        slotRight
        v-if="form.typeId === 2 || form.typeId === 3"
        arrow
      >
        <view class="custom-input" :class="[{ 'value-class': form.businessName }]">
          {{
          form.businessName || `请选择${form.typeId == 2 ? '剧本' : '主题'}`
          }}
        </view>
      </cmdCellItem>
      <cmdCellItem title="期望人数" slotRight>
        <view
          class="custom-input center"
          :class="[{ 'value-class': form.minPeople }]"
          @click="handlerCell($event, 'minPeople', 189)"
        >{{ form.minPeople }}</view>
        <view class="custom-input center value-class">至</view>
        <view
          class="custom-input center"
          @click="handlerCell($event, 'maxPeople', 189)"
          :class="[{ 'value-class': form.maxPeople }]"
        >{{ form.maxPeople }}</view>
      </cmdCellItem>
    </view>
    <view>
      <u-collapse ref="collapseView">
        <u-collapse-item title="填写其他(包含局详情、入局方式、付款方式)">
          <cmdCellItem title="标题" slotRight :arrow="false">
            <u-input
              class="custom-input"
              @click="handlerClickTitle()"
              v-model="form.title"
              placeholder="请输入标题"
            />
          </cmdCellItem>
          <cmdCellItem title="付款方式" slotRight :arrow="false" bg="#f5f5f5">
            <view class="custom-input bg">
              <u-tabs
                :list="payTypeList"
                bg-color="#f5f5f5"
                class="custom-tab-right"
                :show-bar="false"
                :active-item-style="activeLitemStyle"
                :item-width="200"
                :current="activepayType"
                @change="handlerPayType"
              ></u-tabs>
            </view>
          </cmdCellItem>
          <cmdCellItem title="入局费用" v-if="form.payType !== 1" slotRight :arrow="false">
            <u-input
              type="number"
              class="custom-input"
              @click="() => $eventRecord(188)"
              v-model="form.price"
              placeholder="请填写入局费用"
            />
            <view class="custom-input-text">元/人</view>
          </cmdCellItem>
          <cmdCellItem title="入局方式" slotRight :arrow="false" bg="#f5f5f5">
            <view class="custom-input bg">
              <u-tabs
                :list="joinTypeList"
                bg-color="#f5f5f5"
                class="custom-tab-right-join"
                :show-bar="false"
                :active-item-style="activeLitemStyle"
                :item-width="200"
                :current="activejoinType"
                @change="handlerJoinType"
              ></u-tabs>
            </view>
          </cmdCellItem>
          <view
            class="password-wrapper"
            :style="{ visibility: form.joinType === 3 ? 'visible' : 'hidden' }"
          >
            <paycode-app :maxlength="4" :isPwd="false" ref="payCode" @finish="getCode"></paycode-app>
          </view>
        </u-collapse-item>
      </u-collapse>
    </view>
    <view class="footer">
      <view class="btn" @click="confirmGame()">{{ form.gameId ? '立即修改' : '立即创建' }}</view>
    </view>
    <u-popup mode="bottom" v-model="gameStartTimeShow">
      <view class="content">
        <zwyCalendar
          ref="calendar"
          type="sign"
          :banner="banner"
          @handlerBanner="handlerBanner"
          :activeDates="activeDates"
          :localDate="form.gameStartTime"
        />
      </view>
      <view class="footer-pop">
        <view class="btn" @click="gameStartTimeShow = false">取消</view>
        <view class="btn confirm" @click="$u.throttle(confirmData, 500)">确定</view>
      </view>
    </u-popup>

    <u-picker
      mode="selector"
      v-model="peopleNumShow"
      title="请选择同行人数"
      confirm-color="#333333"
      :default-selector="[minSelector.indexOf(form.minPeople)]"
      :range="minSelector"
      @confirm="onPeopleSelect"
    ></u-picker>
    <u-picker
      mode="selector"
      v-model="minPeopleShow"
      title="请选择人数"
      confirm-color="#333333"
      :default-selector="[minSelector.indexOf(form.minPeople)]"
      :range="minSelector"
      @confirm="onMinSelect"
    ></u-picker>
    <u-picker
      mode="selector"
      v-model="maxPeopleShow"
      title="请选择人数"
      confirm-color="#333333"
      :default-selector="[maxSelector.indexOf(form.maxPeople)]"
      :range="maxSelector"
      @confirm="onMaxSelect"
    ></u-picker>
    <u-picker
      mode="selector"
      v-model="boyNumShow"
      title="请选择线下已拼成的人数(包括自己)"
      confirm-color="#333333"
      :default-selector="[maxSelector.indexOf(form.boyNum)]"
      :range="boyPeolpe"
      @confirm="onBoylNumSelect"
    ></u-picker>
    <u-picker
      mode="selector"
      v-model="girlNumShow"
      title="请选择线下已拼成的人数(包括自己)"
      confirm-color="#333333"
      :default-selector="[maxSelector.indexOf(form.gir)]"
      :range="boyPeolpe"
      @confirm="onGirlNumSelect"
    ></u-picker>
  </view>
</template>
<script>
import gameService from '@/service/game.js'
import homeService from '@/service/home'
const BOY_PEOPLE = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
const MIN_PEOPLE = [2, 3, 4, 5, 6, 7, 8, 9, 10]
const MAX_PEOPLE = [3, 4, 5, 6, 7, 8, 9, 10, 11]
import paycodeApp from './components/paycodeApp.vue'
import zwyCalendar from './components/zwy-calendar/zwy-calendar.vue'
import userService from '@/service/user.js'
import cmdCellItem from './components/cmd-cell-item/cmd-cell-item.vue'
export default {
  components: {
    paycodeApp,
    zwyCalendar,
    cmdCellItem,
  },
  data() {
    return {
      girlNumShow: false,
      boyNumShow: false,
      gameStartTimeShow: false, // 日期弹出窗
      isShowPop: false, // 时间弹出窗口
      minPeopleShow: false, //最小
      maxPeopleShow: false, //最大
      peopleNumShow: false, // 同行人数弹出窗
      minSelector: MIN_PEOPLE, // 最小数组
      maxSelector: MAX_PEOPLE, // 最大数据
      boyPeolpe: BOY_PEOPLE,
      typeList: [], // 标签
      payTypeList: [
        {
          name: '我请客',
          value: 1,
        },
        {
          name: '所有人AA',
          value: 2,
        },
        {
          name: '男生AA',
          value: 3,
        },
        {
          name: '女生AA',
          value: 4,
        },
      ],
      joinTypeList: [
        {
          name: '申请入局',
          value: 1,
        },
        {
          name: '直接入局',
          value: 2,
        },
        {
          name: '密码入局',
          value: 3,
        },
      ],
      form: {
        gameStartTime: '', // 创建时间
        gameId: null, // 编辑进来的ID
        activityId: '', // 活动ID数组
        title: '', // 标题
        address: '', // 商家地址
        minPeople: 2, //最少人数
        maxPeople: 10, // 最多人数
        peopleNum: '', // 同行人数
        joinType: 2, // 入局方式
        payType: 2, // 付款方式
        price: 100, // 人均费用
        supplierId: '', // 商家ID
        lat: '', // 位置坐标
        lng: '', // 位置坐标
        boyNum: 0,
        girlNum: 0,
        typeId: '', // 类型
        businessName: '', // 剧本信息
        businessId: '', // 剧本id
        password: '', // 面
        cover: '', // 封面
        activityName: '', // 活动标志
      },
      activeDates: {}, // 活动日期列表
      activeTypeId: '', // 选中tab
      activepayType: 1, // 请客方式
      activejoinType: 1, //入局方式
      price: '',
      banner: [],
      sharePic: '', // 分享的图
    }
  },
  onLoad(options) {
    this.$eventRecord(202)
    this.getBannerList()
    // 如果是商家局
    if (options.gameId) {
      // 如果是编辑
      uni.setNavigationBarTitle({
        title: '编辑局',
      })
      this.gameId = options.gameId
      this.getDetail()
    } else {
      // 如果是新建
      // 默认为当前位置
      uni.getLocation({
        success: (res) => {
          this.form.lat = res.latitude
          this.form.lng = res.longitude
        },
      })
    }
    // 商家返回
    if (options.back) this.getBack(options)
    // 剧本返回
    if (options.backTheme) this.getbackTheme(options)
    // 新创建
    if (!this.typeList.length && !options.gameId) {
      this.init(true)
      // 获取活动日期
    }
  },
  computed: {
    activeLitemStyle() {
      return {
        'background-color': '#000000',
        'border-radius': '12rpx',
        color: '#fff',
      }
    },
  },
  methods: {
    handlerClickTitle() {
      this.$eventRecord(186)
    },
    showGameStartTime(time) {
			console.log(time)
      const gameTime = time.split(' ')
      const gameArr = gameTime[0].split('-')
      const gameArr1 = gameTime[1].split(':')
      return `${gameArr[0]} 年 ${gameArr[1]} 月 ${gameArr[2]} 日 ${gameArr1[0]}:${gameArr1[1]}`
    },
    handlerBanner(val) {
      this.$u.throttle(() => {
        uni.navigateTo({
          url: this.banner[val].jumpUrl,
        })
        // 埋点
        this.$eventRecord(81)
      }, 1000)
    },
    async getBannerList() {
      let res = await homeService.bannerList()
      let banner = res || []
      // 图片压缩居中裁剪
      banner.forEach((e) => {
        e.image = e.picUrl + '?imageView2/1/w/670/h/220/q/80/webp'
      })
      this.banner = banner
    },
    init(first = false) {
      this.form.gameStartTime = `${this.$utils
        .dayjs(new Date())
        .format('YYYY-MM-DD HH')}`
      this.form.title = `${this.form.gameStartTime}:00 剧本`
      this.getTypeList(first)
    },
    getbackTheme(options) {
      const typeName = this.typeList.filter(
        (item) => item.typeId === this.form.typeId
      )[0].name
      this.form.businessName = options.businessName || null
      this.form.businessId = options.businessId || null
      this.form.title = `${this.showGameStartTime(this.form.gameStartTime)}【${
        options.businessName || this.form.companyName || typeName
      }】`
      this.form.minPeople = options.minPeople || 2
      this.sharePic = options.pic
    },
    getBack(options) {
      const typeName = this.typeList.filter(
        (item) => item.typeId === this.form.typeId
      )[0].name
      this.form.businessName = options.businessName || null
      this.form.businessId = options.businessId || null
      this.form.supplierId = options.supplierId || null
      this.form.address = options.address || null
      this.form.lat = options.lat || null
      this.form.lng = options.lng || null
      this.form.activityId = options.activityId || ''
      this.form.cover = options.cover || null
      // 优先剧本标题 商家名字
      this.form.title = `${this.showGameStartTime(this.form.gameStartTime)} 【${
        this.form.businessName || options.companyName || typeName
      }】`
      // 付款方式 1 买单 1 其他AA
      if (this.form.payType != 1) {
        this.price = options.discountPrice || 100
        this.form.price = options.discountPrice || 100
      }
    },
    getActivityDate() {
      gameService.getActivityDate(this.form.typeId).then((res) => {
        this.activeDates = res
      })
    },
    handlerCell(data, type = null, maidian) {
      this.$eventRecord(maidian)
      if (type !== 'gameStartTime' && !this.form.gameStartTime) {
        return this.$toast('请选择开局日期')
      }
      // 判断日期函数初始化时间
      if (type === 'gameStartTime' && this.form.gameStartTime) {
        const gameStartTime = this.form.gameStartTime.split(' ')
        this.$refs.calendar.localDateCopy = gameStartTime[0]
        this.$refs.calendar.dateDay = gameStartTime[1].substring(0, 5)
      }
      if (type === 'businessId') {
        this.handlerOpenThemes()
      }
      if (type === 'address') {
        this.handlerOpenAddress()
      } else {
        this[`${type}Show`] = true
      }
    },
    openLoaction() {
      const _this = this
      // 获取其他位置
      uni.chooseLocation({
        success: function (res) {
          console.log('sdfsdf', res)
          if (!res.name.includes('长沙'))
            return _this.$toast('目前只支持在长沙!')
          _this.form.address = res.address
          _this.form.lat = res.latitude
          _this.form.lng = res.longitude
        },
      })
    },
    confirmData() {
      const gameStartTime = `${this.$refs.calendar.localDateCopy} ${this.$refs.calendar.dateDay}:00`
      this.form.gameStartTime = gameStartTime
      if (
        this.activeDates[`${this.$refs.calendar.localDateCopy}`] !== undefined
      ) {
        this.form.activityId = this.activeDates[
          `${this.$refs.calendar.localDateCopy}`
        ]
          .map((item) => item.activityId)
          .join(',')
        this.form.activityName = `${
          this.activeDates[`${this.$refs.calendar.localDateCopy}`][0].name
        }`
      } else {
        this.form.activityName = ''
        this.form.activityId = ''
      }
      // 设置标题
      const typeName = this.typeList.filter(
        (item) => item.typeId === this.form.typeId
      )[0].name
      this.form.title = `${this.showGameStartTime(this.form.gameStartTime)} 【${
        this.form.businessName || this.form.companyName || typeName
      }】`
      // 初始化并清空
      this.form.businessName = null
      this.form.businessId = null
      this.form.address = null
      this.supplierId = null
      this.gameStartTimeShow = false
    },
    // 获取局详情
    getDetail() {
      gameService.gameDetail(this.gameId).then((res) => {
        const { script, supplier, secretEscape } = res
        res.gameStartTime = this.$utils
          .dayjs(new Date(res.gameStartTime))
          .format('YYYY-MM-DD HH:mm:ss')
        if (res.typeId == 2 && script) {
          res.businessId = script.id
          res.businessName = script.name
        }
        if (res.typeId == 3 && secretEscape) {
          res.businessId = secretEscape.id
          res.businessName = secretEscape.name
        }
        // 请客方式
        if (res.payType) {
          this.activepayType = this.payTypeList.findIndex(
            (item) => item.value === res.payType
          )
        }
        // 入局方式
        if (res.joinType) {
          this.activejoinType = this.joinTypeList.findIndex(
            (item) => item.value === res.joinType
          )
        }
        if (supplier) {
          res.supplierId = supplier.supplierId
        }
        this.form = res
        this.form.activityId = res.activityId || ''
        this.form.supplierId = res.supplierId || ''
        if (res.joinType === 3) {
          this.$refs.payCode.codeArr = [...res.password]
        }
        this.getTypeList()
      })
    },
    getCode(val) {
      this.form.password = val
    },
    openPopup(componentName, show) {
      this[componentName] = show
    },
    onPeopleSelect(e) {
      this.$set(this.form, 'peopleNum', MIN_PEOPLE[e[0]])
    },
    onMinSelect(e) {
      this.$set(this.form, 'minPeople', MIN_PEOPLE[e[0]])
    },
    onMaxSelect(e) {
      this.$set(this.form, 'maxPeople', MAX_PEOPLE[e[0]])
    },
    onBoylNumSelect(e) {
      if (e.includes(-1)) {
        this.$set(this.form, 'boyNum', BOY_PEOPLE[0])
      } else {
        this.$set(this.form, 'boyNum', BOY_PEOPLE[e[0]])
      }
    },
    onGirlNumSelect(e) {
      if (e.includes(-1)) {
        this.$set(this.form, 'girlNum', BOY_PEOPLE[0])
      } else {
        this.$set(this.form, 'girlNum', BOY_PEOPLE[e[0]])
      }
    },
    handlerOpenAddress() {
      const gameStartTime = this.form.gameStartTime.split(' ')
      uni.navigateTo({
        url: `/pagesA/merchantSites/list?typeId=${this.form.typeId}&activityId=${this.form.activityId}&supplierId=${this.form.supplierId}&time=${gameStartTime[0]}`,
      })
    },
    handlerOpenThemes() {
      // if (!this.form.supplierId) return this.$toast('请先选择商家地点');
      uni.navigateTo({
        url: `/pagesA/hotScript/list?typeId=${this.form.typeId}&supplierId=${
          this.form.supplierId
        }&isSelect=${true}`,
      })
    },
    getTypeList(first = false) {
      gameService.typeList().then((res) => {
        const typeList = res || []
        this.typeList = typeList
        if (this.form.gameId) {
          this.activeTypeId = this.typeList.findIndex(
            (item) => item.typeId === this.form.typeId
          )
        } else {
          this.form.typeId = this.typeList[0].typeId
        }
        this.getActivityDate()
        // 第一次
        if (first) {
          const gameStartTime = this.form.gameStartTime.split(' ')
          this.$refs.calendar.localDateCopy = gameStartTime[0]
          this.$refs.calendar.dateDay = gameStartTime[1].substring(0, 5)
          this.confirmData()
        }
        // if (this.form.gameId) {
        //   this.confirmData(true);
        // }
      })
    },
    handlerChange(val) {
      this.$eventRecord(180)
      // if(this.form.gameId) return this.$toast('编辑不能修改局类型');
      if (val === 4) return this.$toast('敬请期待...')
      this.activeTypeId = val
      const gameId = this.form.gameId || null
      const form = this.defaultForm()
      form.typeId = this.typeList[val].typeId
      form.gameId = gameId || null
      Object.assign(this.form, form)
      this.getActivityDate()
    },
    handlerPayType(val) {
      this.form.payType = this.payTypeList[val].value
      this.form.price = 100
      this.activepayType = val
    },
    handlerJoinType(val) {
      this.$eventRecord(187)
      this.form.joinType = this.joinTypeList[val].value
      this.activejoinType = val
    },
    defaultForm() {
      return {
        title: '', // 标题
        address: '', // 商家地址
        minPeople: 2, //最少人数
        maxPeople: 10, // 最多人数
        peopleNum: '', // 同行人数
        girlNum: 0,
        boyNum: 0,
        price: 100, // 人均费用
        supplierId: '', // 商家ID
        lat: '', // 位置坐标
        lng: '', // 位置坐标
        typeId: '', // 类型
        businessName: '', // 剧本信息
        businessId: '', // 剧本id
        cover: '', // 封面
      }
    },
    confirmGame() {
      this.$eventRecord(190)
      if (!this.form.gameStartTime) return this.$toast('请选择开局日期')
      if (this.form.boyNum + this.form.girlNum === 0)
        return this.$toast('请选择同行人数')
      if (!this.form.address) return this.$toast('请选择开局地点')
      if (
        !this.form.activityId &&
        this.form.address &&
        (this.typeId === 2 || this.typeId === 3)
      ) {
        if (!this.form.businessId) return this.$toast('请选择剧本')
      }
      if (this.form.title.length < 5) return this.$toast('局名称不能少于5个字')
      if (this.form.payType !== 1) {
        if (!this.form.price) {
          return this.$toast('请填写入局费用')
        }
        if (this.form.price > 10000) {
          return this.$toast('价格不得高于10000元')
        }
      }
      if (this.form.minPeople > this.form.maxPeople) {
        return this.$toast('人数不合理')
      }
      if (this.form.maxPeople < this.form.boyNum + this.form.girlNum) {
        return this.$toast('同行人数不合理')
      }
      if (this.form.joinType === 3) {
        if (this.form.password.length !== 4)
          return this.$toast('请输入入局密码')
      }
      // 酒吧
      if (this.form.typeId === 1 && !this.form.cover) {
        this.form.cover = 'https://static.image.pinbar.vip/type/21.jpg'
      }
      // 剧本
      if (this.form.typeId === 2 && !this.form.cover) {
        this.form.cover =
          this.sharePic || 'https://static.image.pinbar.vip/type/22.jpg'
      }
      // 密室
      if (this.form.typeId === 3 && !this.form.cover) {
        this.form.cover =
          this.sharePic || 'https://static.image.pinbar.vip/type/23.jpg'
      }
      // 公益
      if (this.form.typeId === 4 && !this.form.cover) {
        this.form.cover = 'https://static.image.pinbar.vip/type/41.jpg'
      }
      // 分享的图片
      // const arrImage = ["https://static.image.pinbar.vip/type/21.jpg", "https://static.image.pinbar.vip/type/22.jpg","https://static.image.pinbar.vip/type/23.jpg"]
      const formCopy = JSON.parse(JSON.stringify(this.form))
			formCopy.gameStartTime = formCopy.gameStartTime + ':00'
      if (this.form.joinType !== 3) delete formCopy.password
      if (this.form.payType === 1) delete formCopy.price
      const pages = getCurrentPages()
      const prevPage = pages[pages.length - 2]

      if (!this.form.gameId) {
        gameService.createGame(formCopy).then((gameInfo) => {
					if(gameInfo instanceof Array) return
          this.$toast('创建成功!', 'success').then(() => {
            userService.queryUserInfo().then((res) => {
              // 如果用户没有关注公众号 或者用户资料不足90% 跳转到引导关注页面
              if (res.subscription !== 1 || res.rate < 90) {
                // 将局标题和图片传过去用于分享
                uni.reLaunch({
                  url: `/pagesA/game/create-after?subscription=${res.subscription}&gameId=${gameInfo}&rate=${res.rate}&head=${res.head}&title=${formCopy.title}&cover=${formCopy.cover}`,
                })
              } else {
                // prevPage.onLoad({ gameId: gameInfo })
                // uni.navigateBack()
                uni.navigateTo({
                  url: `/pagesA/game/detail?gameId=${gameInfo}`,
                })
              }
            })
          })
        })
      } else {
        gameService.editGroupBureau(formCopy).then(() => {
          this.$toast('组局修改成功', 'success')
          prevPage.onLoad({ gameId: formCopy.gameId })
          setTimeout(() => {
            uni.navigateBack()
          }, 1000)
        })
      }
    },
  },
}
</script>
<style lang="scss" scoped>
.game-created {
  padding: 44upx 22upx;
  background: #f5f5f5;
  padding-bottom: 180rpx;
  min-height: 100vh;
  .title {
    height: 50upx;
    font-size: 36upx;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #000000;
    line-height: 50upx;
    margin-bottom: 24upx;
  }
  .conent {
    padding-top: 60upx;
  }
  .footer {
    position: fixed;
    z-index: 2;
    bottom: 0;
    left: 0;
    right: 0;
    height: 160upx;
    background: #fff;
    .btn {
      height: 100upx;
      background: #000000;
      border-radius: 54px;
      margin: 0 84upx;
      text-align: center;
      font-weight: 500;
      color: #ffffff;
      line-height: 100upx;
      font-size: 36upx;
      margin-top: 30upx;
    }
  }
  .password-label {
    font-size: 32upx;
    font-weight: bold;
    color: #666666;
    min-height: 100upx !important;
    line-height: 100upx !important;
    text-align: left;
  }
  .footer-pop {
    height: 160upx;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    justify-items: center;
    padding: 0 76upx;
    padding-top: 30upx;
    .btn {
      flex-basis: 260upx;
      height: 100upx;
      line-height: 100upx;
      color: #000000;
      font-weight: bold;
      font-size: 36upx;
      text-align: center;
      background: rgba(153, 153, 153, 0.1);
      border-radius: 50px;
    }
    .confirm {
      background: #000000;
      color: #fff;
    }
  }
}
.custom-input {
  flex: 1;
  margin-left: 24upx;
  color: #dcdfe6;
  font-size: 28rpx;
  text-align: center;
}
.bg {
  background: #f5f5f5;
  margin: 0;
}
.custom-input-text {
  display: inline-flex;
  padding-right: 12upx;
  color: #303133;
  font-weight: bold;
}
.center {
  text-align: center;
}
.value-class {
  color: #303133;
}
.custom-tab-right-join /deep/ .u-tabs {
  width: calc(100% - 70upx);
  .u-tab-item {
    background: #fff;
    margin-right: 15upx;
    border-radius: 12rpx;
  }
}
.custom-tab-right /deep/ .u-tabs {
  width: calc(100% - 290upx);
  overflow: scroll;
  .u-tab-item {
    background: #fff;
    margin-right: 15upx;
    border-radius: 12rpx;
  }
}
.password-wrapper {
  margin: 20upx 0;
  height: 160upx;
}
.active-class {
  color: red;
  font-size: 28rpx;
}
</style>
