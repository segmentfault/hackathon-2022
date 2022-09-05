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
          :style="'color:' + (days.date == localDateCopy ? bgday : '') + ';'"
          v-for="(days, index) in dayArr"
          :key="index"
          :class="{ circle: days.flag }"
          @click="signToday(days, index)"
        >
          <!-- <view
            class="tag"
            v-show="activeDays.includes(days.date)"
          >{{ showText(activeDates, days.date) }}</view> -->
          {{ days.day }}
        </view>
      </view>
      <u-picker
        mode="time"
        v-model="isTimeShow"
        title="请选择时间"
        confirm-color="#333333"
        @confirm="onTimeSelect"
        :params="{
          hour: true,
        }"
      ></u-picker>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    lang: {
      type: String,
      default: "zh",
    },
    type: {
      type: String,
      default: "calendar",
    },
    checkDate: {
      type: Boolean,
      default: false,
    },
    bgweek: {
      type: String,
      default: "#FF442E",
    },
    activeDates: {
      type: Object,
      default: {},
    },
    activeText: {
      type: String,
      default: "半价",
    },
    bgday: {
      type: String,
      default: "#FF442E",
    },
    banner: {
      type: Array,
      default: [],
    },
    localDate: {
      type: String, // 今天日期
      default: "",
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
      dateDay: `00:00`,
      isTimeShow: false,
      weeked: "", // 今天周几
      localDateCopy: "",
      dayArr: [], // 当前月每日
      day: new Date().getDate(), // 当前日
      year: new Date().getFullYear(), // 当前年
      month: new Date().getMonth() + 1, // 当前月
      defaultMonth: new Date().getMonth() + 1,
      weekArr: ["日", "一", "二", "三", "四", "五", "六"], // 每周
    };
  },
  mounted() {
    let that = this;
    this.localDateCopy = this.localDate;
    // 初始日期
    that.initDate();
    // 中英切换
    if (that.lang != "zh")
      that.weekArr = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
    // 今天周几
    that.weeked = that.weekArr[new Date().getDay()];
  },
  methods: {
    handlerBannerClick(val) {},
    async clickBanner(item) {
      console.log(item);
      this.$u.throttle(() => {
        uni.navigateTo({
          url: item.jumpUrl,
        });
        // 埋点
        this.$eventRecord(81);
      }, 1000);
    },
    showText(item, date) {
      if (item[`${date}`] !== undefined) {
        return item[`${date}`][0].name;
      }
    },
    // 开局时间
    onTimeSelect(e) {
      this.dateDay = `${e.hour}:00`;
      this.isTimeShow = false;
    },
    // 选择年月
    changeDate(e) {
      let that = this;
      that.year = parseInt(e.detail.value.split("-")[0]);
      that.month = parseInt(e.detail.value.split("-")[1]);
      that.initDate();
    },
    // 点击签到
    signToday(obj, index) {
      const time1 = new Date().getTime();
      const time2 = new Date(obj.date).getTime();
      if (time2 < time1 && !(new Date().getDate() === new Date(obj.date).getDate())) return this.$toast("选择日期不能小于当前日期");
      this.day = obj.day;
      this.localDateCopy = obj.date;
      this.isTimeShow = true;
    },
    // 初始化日期
    initDate() {
      let that = this;
      that.dayArr = [];
      // 当前月总天数
      let totalDay = new Date(that.year, that.month, 0).getDate();
      // 遍历总天数将日期逐个添加至数组
      for (let i = 1; i <= totalDay; i++) {
        // 得到需补充天数
        let value = new Date(that.year, that.month - 1, i).getDay();
        // 补充前面空白日期
        if (i == 1 && value != 0) {
          let year = that.year;
          let month = that.month;
          if (that.month == 1) {
            year -= 1;
            month = 12;
          } else {
            month -= 1;
          }
          that.addBefore(value, `${year}-${month}`);
        }
        // 添加本月日期
        let obj = {};
        obj.date =
          that.year +
          "-" +
          that.formatNum(that.month) +
          "-" +
          that.formatNum(i);
        obj.day = i;
        that.dayArr.push(obj);
        // 补充后面空白日期
        if (i == totalDay && value != 6) {
          let year = that.year;
          let month = that.month;
          if (that.month == 1) {
            year += 1;
            month = 12;
          } else {
            month += 1;
          }
          that.addAfter(value, `${year}-${month}`);
        }
      }
    },
    // 补充前面空白日期
    addBefore(value, date = null) {
      let that = this;
      let totalDay = new Date(that.year, that.month - 1, 0).getDate();
      for (let i = 0; i < value; i++) {
        let obj = {};
        obj.date = date
          ? `${date}-${that.formatNum(totalDay - (value - i) + 1)}`
          : "";
        obj.day = totalDay - (value - i) + 1;
        that.dayArr.push(obj);
      }
    },
    // 补充后空白日期
    addAfter(value, date = null) {
      let that = this;
      for (let i = 0; i < 6 - value; i++) {
        let obj = {};
        obj.date = date ? `${date}-${that.formatNum(i + 1)}` : "";
        obj.day = i + 1;
        that.dayArr.push(obj);
      }
    },
    // 格式化日期位数
    formatNum(num) {
      return num < 10 ? "0" + num : num;
    },
    // 上一个月
    lastMonth() {
      if (this.month <= this.defaultMonth) return;
      let that = this;
      if (that.month == 1) {
        that.year -= 1;
        that.month = 12;
      } else {
        that.month -= 1;
      }
      that.initDate();
    },
    // 下一个月
    nextMonth() {
      if (this.month >= this.defaultMonth + 1) return;
      let that = this;
      if (that.month == 12) {
        that.year += 1;
        that.month = 1;
      } else {
        that.month += 1;
      }
      that.initDate();
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
  padding: 20rpx;
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
  width: 700rpx;
  font-weight: bolder;
}
.time-title {
  width: 700upx;
  margin: 30upx auto;
}
.month {
  margin: 30rpx 0;
  position: relative;
}

.picker {
  width: 160rpx;
  height: 40rpx;
  position: absolute;
  top: 20rpx;
  left: 50%;
  transform: translate(-50%, -50%);
}

.day {
  flex-wrap: wrap;
}

.week > view,
.day > view {
  width: 80rpx;
  height: 80rpx;
  margin-top: 2rpx;
  text-align: center;
  margin: 10rpx;
  position: relative;
  padding-top: 20rpx;
  line-height: 80rpx;
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

.circle {
  border-radius: 8upx;
  color: #ff442e;
}
.tag {
  display: "";
  position: absolute;
  color: #ff3c3c;
  font-size: 20upx;
  top: -20upx;
  left: 0;
  right: 0;
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
  font-weight: 32pux;
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
