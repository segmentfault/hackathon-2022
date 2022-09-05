<template>
  <view>
    <view class="calendar-top" v-if="banner.length">
      <u-swiper
        :list="banner"
        mode="dot"
        bg-color="#fff"
        @click="$emit('handlerBanner', $event)"
      ></u-swiper>
    </view>
    <view class="flex flex-middle time-title" v-else>
      <text class="fs32 mr10">请确定预定日期</text
      ><text class="fs24 color-croci">日期不同价格可能有所波动</text>
    </view>
    <view class="calendar-box">
      <!-- 轮播图 -->

      <view class="month">
        <view @click="lastMonth">
          <u-icon
            class="icon"
            name="arrow-left"
            color="#666666"
            size="32"
          ></u-icon>
        </view>
        <view class="title">
          <view>{{ year }}年{{ month }}月</view>
          <view @click="isTimeShow = true" v-if="showTimeSelect">{{
            dateDay
          }}</view>
        </view>
        <view @click="nextMonth">
          <u-icon
            class="icon"
            name="arrow-right"
            color="#666666"
            size="32"
          ></u-icon>
        </view>
        <picker
          v-if="checkDate"
          class="picker"
          mode="date"
          fields="month"
          @change="changeDate"
        />
      </view>
      <view class="week">
        <view
          :style="
            'color:' + (weeks == weeked ? bgweek : 'rgba(0, 0, 0, 0.38);') + ';'
          "
          v-for="(weeks, index) in weekArr"
          :key="index"
          >{{ weeks }}</view
        >
      </view>
      <view class="day">
        <view
          v-for="(days, index) in dayArr"
          :key="index"
          :class="{ circle: days.date == localDateCopy }"
          @click="signToday(days, index)"
        >
          <view
            :class="[
              { today: days.date === nowTime },
              {
                'color-999':
                  new Date(days.date + ' 24:00:00').getTime() <
                    new Date().getTime() || activeDates[days.date].stock == 0,
              },
              'sky',
            ]"
            v-show="new Date(days.date).getMonth() + 1 == month"
          >
            {{ days.date === nowTime ? '今日' : days.day }}
          </view>
          <view
            :class="['tag', { 'color-999': activeDates[days.date].stock == 0 }]"
            v-if="
              activeDays.includes(days.date) &&
              new Date(days.date).getMonth() + 1 == month
            "
            >{{ showText(activeDates, days.date) }}</view
          >
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    lang: {
      type: String,
      default: 'zh',
    },
    type: {
      type: String,
      default: 'calendar',
    },
    checkDate: {
      type: Boolean,
      default: false,
    },
    bgweek: {
      type: String,
      default: '#FF442E',
    },
    activeDates: {
      type: Object,
      default: {},
    },
    activeText: {
      type: String,
      default: '半价',
    },
    bgday: {
      type: String,
      default: '#FF442E',
    },
    banner: {
      type: Array,
      default: [],
    },
    localDate: {
      type: String, // 今天日期
      default: '',
    },
    showTimeSelect: {
      type: Boolean,
      default: true,
    },
  },
  computed: {
    activeDays() {
      return Object.keys(this.activeDates);
    },
  },
  data() {
    return {
      nowTime: '',
      today: new Date().getDate(),
      dateDay: `00:00`,
      isTimeShow: false,
      weeked: '', // 今天周几
      localDateCopy: '',
      dayArr: [], // 当前月每日
      day: new Date().getDate(), // 当前日
      year: new Date().getFullYear(), // 当前年
      month: new Date().getMonth() + 1, // 当前月
      defaultMonth: new Date().getMonth() + 1,
      weekArr: ['日', '一', '二', '三', '四', '五', '六'], // 每周
    };
  },
  created() {
    this.nowTime = this.$utils.dayjs(new Date()).format('YYYY-MM-DD');
    this.localDateCopy = this.$utils.dayjs(new Date()).format('YYYY-MM-DD');
  },
  mounted() {
    // this.localDateCopy = this.localDate

    // 初始日期
    this.initDate();
    // 中英切换
    if (this.lang != 'zh')
      this.weekArr = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    // 今天周几
    this.weeked = this.weekArr[new Date().getDay()];
  },
  methods: {
    showText(item, date) {
      // console.log(item)
      if (item[`${date}`] !== undefined) {
        return item[`${date}`].stock == 0
          ? '已售罄'
          : '¥' + item[`${date}`].price;
      }
    },
    // 开局时间
    onTimeSelect(e) {
      this.dateDay = `${e.hour}:${e.minute}`;
      this.isTimeShow = false;
    },
    // 选择年月
    changeDate(e) {
      this.year = parseInt(e.detail.value.split('-')[0]);
      this.month = parseInt(e.detail.value.split('-')[1]);
      this.initDate();
    },
    // 点击签到
    signToday(obj, index) {
      const time1 = new Date(
        this.$utils.dayjs(new Date()).format('YYYY-MM-DD')
      ).getTime();
      const time2 = new Date(obj.date).getTime();
      if (time2 < time1) return this.$toast('选择日期不能小于当前日期');
      if (this.activeDates[obj.date].stock === 0)
        return this.$toast('该日商品已预订完,请选择其他日期!');
      this.day = obj.day;
      this.localDateCopy = obj.date;
    },
    // 初始化日期
    initDate() {
      this.dayArr = [];
      // 当前月总天数
      let totalDay = new Date(this.year, this.month, 0).getDate();
      // 遍历总天数将日期逐个添加至数组
      for (let i = 1; i <= totalDay; i++) {
        // 得到需补充天数
        let value = new Date(this.year, this.month - 1, i).getDay();
        // 补充前面空白日期
        if (i == 1 && value != 0) {
          let year = this.year;
          let month = this.month;
          if (this.month == 1) {
            year -= 1;
            month = 12;
          } else {
            month -= 1;
          }
          this.addBefore(value, `${year}-${month}`);
        }
        // 添加本月日期
        let obj = {};
        obj.date =
          this.year +
          '-' +
          this.formatNum(this.month) +
          '-' +
          this.formatNum(i);
        obj.day = i;
        this.dayArr.push(obj);
        // 补充后面空白日期
        if (i == totalDay && value != 6) {
          let year = this.year;
          let month = this.month;
          if (this.month == 1) {
            year += 1;
            month = 12;
          } else {
            month += 1;
          }
          this.addAfter(value, `${year}-${month}`);
        }
      }
    },
    // 补充前面空白日期
    addBefore(value, date = null) {
      let totalDay = new Date(this.year, this.month - 1, 0).getDate();
      for (let i = 0; i < value; i++) {
        let obj = {};
        obj.date = date
          ? `${date}-${this.formatNum(totalDay - (value - i) + 1)}`
          : '';
        obj.day = totalDay - (value - i) + 1;
        this.dayArr.push(obj);
      }
    },
    // 补充后空白日期
    addAfter(value, date = null) {
      for (let i = 0; i < 6 - value; i++) {
        let obj = {};
        obj.date = date ? `${date}-${this.formatNum(i + 1)}` : '';
        obj.day = i + 1;
        this.dayArr.push(obj);
      }
    },
    // 格式化日期位数
    formatNum(num) {
      return num < 10 ? '0' + num : num;
    },
    // 上一个月
    lastMonth() {
      if (
        this.month <= this.defaultMonth &&
        this.year <= new Date().getFullYear()
      )
        return;
      if (this.month == 1) {
        this.year -= 1;
        this.month = 12;
      } else {
        this.month -= 1;
      }
      this.initDate();
    },
    // 下一个月
    nextMonth() {
      const month =
        this.month < this.defaultMonth
          ? this.defaultMonth + this.month
          : this.month;
      if (month - this.defaultMonth >= 1) return;
      if (this.month == 12) {
        this.year += 1;
        this.month = 1;
      } else {
        this.month += 1;
      }
      this.initDate();
    },
  },
};
</script>

<style scoped>
.calendar-box {
  width: 100%;
  flex-direction: column;
  justify-content: center;
}
.calendar-top {
  padding: 20upx;
}

.calendar-box,
.month,
.week,
.day {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.month,
.week,
.day {
  width: 700upx;
  font-weight: bolder;
}
.time-title {
  width: 700upx;
  margin: 30upx auto;
}
.month {
  margin: 30upx 0;
  position: relative;
}

.picker {
  width: 160upx;
  height: 40upx;
  position: absolute;
  top: 20upx;
  left: 50%;
  transform: translate(-50%, -50%);
}

.day {
  flex-wrap: wrap;
}
.today {
  color: #ff442e;
  font-weight: Semibold;
}
.week > view,
.day > view {
  width: 80upx;
  height: 80upx;
  margin-top: 2upx;
  text-align: center;
  margin: 10upx;
  padding-top: 20upx;
  color: rgba(0, 0, 0, 0.87);
  font-size: 28upx;
  font-weight: bolder;
}

.checkday {
  color: #999;
}

.choose {
  color: #ffffff;
  border-radius: 50%;
}

.circle .sky {
  border-radius: 8upx;
  background: #ff442e;
  color: #fff;
}
.tag {
  display: '';
  color: #ff3c3c;
  font-size: 20upx;
}
.sign {
  background-color: #0089fe;
  border-radius: 100%;
}

.repair {
  background-color: #f4a01a;
}
.icon {
  font-weight: bold;
}
.title {
  color: #000000;
  font-weight: 500;
  font-weight: bold;
  display: flex;
  flex-direction: row;
}
.title view {
  text-align: center;
  margin-right: 20upx;
}
.disabled {
  cursor: not-allowed;
}
</style>
