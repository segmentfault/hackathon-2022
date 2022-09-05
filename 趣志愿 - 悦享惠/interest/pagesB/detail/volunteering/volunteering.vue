<template>
  <view class="box">
    <u-navbar
      back-icon-size="48"
      :border-bottom="false"
      :is-back="true"
      back-icon-color="white"
      :background="background"
    >
      <view class="choose">
        <text :class="{ active: navId == 1 }" @click="toggleNav(1)">组织</text>
        <text
          :class="{ active: navId == 2 }"
          class="u-p-l-32"
          @click="toggleNav(2)"
          >活动</text
        >
      </view>
    </u-navbar>
    <view class="organization" v-if="navId == 1">
     <view class="search">
         <u-search
            placeholder="搜索组织名称"
            maxlength=20
            style="width: 100%"
            :show-action="false"
            height="80"
            v-model="searchVal"
            @search="search('organization')"
          ></u-search>
        <view class="searchBtn" @click="search('organization')">
          搜索
        </view>
     </view>
      <u-tabs
        :list="tabs"
        height="72"
        bar-width="52"
        font-size="30"
        active-color="#4575F6 "
        bold
        inactive-color="#333333 "
        bg-color="#F9F9F9"
        active-item-style="font-size:36rpx"
        item-style="	opacity=0.4;"
        @change="changeTab"
        :current="current"
      />
      <view
        class="content"
        v-for="(item, index) in orInfo"
        :key="index"
      >
        <view class="left"  @click="orDetail(item._id)">
          <u-image
            height="180rpx"
            border-radius="8rpx"
            width="180rpx"
            :src="item.image"
            alt=""
          >
          </u-image>
        </view>
        <view class="right">
          <view class="name"
           @click="orDetail(item._id)"
            >{{ item.group_name }}
            <u-image
              v-if="item.audit"
              height="43rpx"
              width="136rpx"
              src="/static/img/or-detail/rz.png"
            ></u-image>
          </view>
          <view class="ti"  @click="orDetail(item._id)">成立时间：{{ item.createTime }}</view>
          <view class="ti"  @click="orDetail(item._id)">成员：{{ item.userNumber }}人</view>
          <view class="address"
            @click="mapFun(item)"
            ><text class="t2" >地址：{{ item.address }}</text
            ><text class="gps" >导航</text></view
          >
        </view>
      </view>
    </view>
    <view v-else>
        <view class="search">
         <u-search
            placeholder="搜索活动名称"
            maxlength=20
            style="width: 100%"
            :show-action="false"
            height="80"
            v-model="searchVal"
            @search="search('activity')"
          ></u-search>
        <view class="searchBtn" @click="search('activity')">
          搜索
        </view>
     </view>
      <u-tabs
        :list="tabs2"
        height="72"
        bar-width="52"
        font-size="30"
        active-color="#4575F6 "
        bold
        inactive-color="#333333 "
        bg-color="#F9F9F9"
        active-item-style="font-size:36rpx"
        item-style="	opacity=0.4;"
        @change="changeTab2"
        :current="current2"
      />
      <view class="newData">
        <view
          class="date"
          v-for="(v, i) in days"
          :key="i"
          :class="{ checked: v.i === searchParm.days }"
          @click="selectDate(v.i, v.week)"
        >
          <view class="week checked_text">{{ v.week }}</view>
          <view class="day checked_text">{{ v.day }}</view>
        </view>
      </view>
      <view
        class="content"
        @click="goAcdetail(item._id)"
        v-for="(item, index) in acInfo"
        :key="index"
      >
        <view class="left">
          <u-image
            height="180rpx"
            border-radius="8rpx"
            width="180rpx"
            :src="item.image"
            alt=""
          >
          </u-image>
        </view>
        <view class="right">
          <view class="name"
            >{{ item.name }}
            <u-image
              class="u-m-l-8"
              height="40rpx"
              width="88rpx"
              :src="item.show?'/static/img/or-detail/start.png':'/static/img/or-detail/end.png'"
            ></u-image>
          </view>
          <view class="ti">活动类型：{{ item.type }}</view>
          <view class="ti">开始时间：{{ item.time }}</view>
          <view class="address"
            ><text class="t2"
              >计划招募：{{ item.userNumber }} / {{ item.allNumber }}</text
            ></view
          >
        </view>
      </view>
    </view>
  </view>
</template>

<script>
const db = wx.cloud.database();
export default {
  data() {
    return {
      orInfo: {},
      acInfo: {},
      navId: 1,
      searchVal:'',
      background: {
        backgroundColor: "#4575F6 ",
      },
      current: 0,
      current2: 0,
      barStyle: {
        background: "linear-gradient(180deg, #CD6CFF 0%, #FF4F3A 100%)",
      },
      start: 0,
      end: 0,
      days: [
        {
          week: "全部",
          day: "-",
          i: "",
        },
      ],
      searchParm: {
        days: "",
      },
      tabs: [
        {
          name: "全部",
          typeId: 1,
        },
        {
          name: "动物保护",
          typeId: 2,
        },
        {
          name: "环境保护",
          typeId: 3,
        },
        {
          name: "适老化",
          typeId: 4,
        },
      ],
      tabs2: [
        {
          name: "全部",
          typeId: 1,
        },
        {
          name: "",
          typeId: 2,
        },
        {
          name: "",
          typeId: 3,
        },
      ],
    };
  },
  async onLoad(option) {
    this.navId=option.navId
    this.GetTime();
    await this.getInfo();
    this.getStart();
  },
  methods: {
    async getInfo() {
      db.collection("organization")
        .get()
        .then((res) => {
          this.orInfo = res.data;
          console.log("this.orInfo", res.data);
        });
      let a = await db
        .collection("activity")
        .orderBy("creatTime","desc")
        .get()
        .then((res) => {
          this.acInfo = res.data;
          console.log("this.acInfo", this.acInfo);
        });
      let taskRes = await db
        .collection("activity")
        .where({
          show: true,
        })
        .orderBy("creatTime","desc")
        .get()
        .then((res) => {
          this.tabs2[1].name = "进行中(" + res.data.length + ")";
          this.tabs2[2].name =
            "已结束（" + (this.acInfo.length - res.data.length) + ")";
        });
    },
    search(data){
      console.log("类型",data)
      console.log("搜索只",this.searchVal)
     if(data=='activity'){
           db.collection(data)
          .where({
            name: {
              $regex: this.searchVal,
            },
          })
          .get()
          .then((res) => {
            this.acInfo = res.data;
            console.log("this.acInfo", this.acInfo);
          });
     }else{
            db.collection(data)
          .where({
            group_name: {
              $regex: this.searchVal,
            },
          })
          .get()
          .then((res) => {
            this.orInfo = res.data;
            console.log("this.orInfo", this.orInfo);
          });
     }
    
    },
    getOrinfo(data) {
      if (data == "全部") {
        db.collection("organization")
          .get()
          .then((res) => {
            this.orInfo = res.data;
            console.log("this.orInfo", res.data);
          });
      } else {
        db.collection("organization")
          .where({
            serviceCa: data,
          })
          .get()
          .then((res) => {
            this.orInfo = res.data;
            console.log("this.orInfo", res.data);
          });
      }
    },
    async getStart() {},
    getAcinfo(data, day) {
      if (data == "全部") {
        db.collection("activity")
          .orderBy("creatTime","desc")
          .get()
          .then((res) => {
            this.acInfo = res.data;
            console.log("this.acInfo", this.acInfo);
          });
      } else {
        db.collection("activity")
          .where({
            time: {
              $regex: day,
            },
          })
          .orderBy("creatTime","desc")
          .get()
          .then((res) => {
            this.acInfo = res.data;
            console.log("this.acInfo", this.acInfo);
          });
      }
    },
    toggleNav(id) {
      this.navId = id;
      this.searchVal=''
      this.aState = false;
    },
    goAcdetail(data) {
      uni.navigateTo({
        url: `/pagesB/detail/ac-detail?acid=${data}`,
      });
    },
    //跳转组织详情
    orDetail(data) {
      uni.navigateTo({
        url: `/pagesB/detail/or-detail?type=true&id=${data}`,
      });
    },
    changeTab(val) {
      this.current = val;
      this.getOrinfo(this.tabs[val].name);
      console.log("dfsdf", this.tabs[val].name);
    },
     mapFun(data){
		uni.openLocation({
			latitude: data.latitude,//纬度
			longitude:data.longitude,//经度
			name: data.addressName,
			address: data.address
		});
  	},
    changeTab2(val) {
      this.current2 = val;
      if (val == 1) {
        db.collection("activity")
          .where({
            show: true,
          })
          .orderBy("creatTime","desc")
          .get()
          .then((res) => {
            this.acInfo = res.data;
          });
      } else if (val == 2) {
        db.collection("activity")
          .where({
            show: false,
          })
          .orderBy("creatTime","desc")
          .get()
          .then((res) => {
            this.acInfo = res.data;
          });
      } else {
        db.collection("activity")
          .orderBy("creatTime","desc")
          .get()
          .then((res) => {
            this.acInfo = res.data;
          });
      }
      console.log("val", val);
    },
    async selectDate(v, da) {
      this.searchParm.days = v;
      console.log("v", this.searchParm.days);
      console.log("日期", this.days[v + 1].day);
      console.log("dsfsd", da);
      this.getAcinfo(da, this.days[v + 1].day);
    },
    // 获取未来七天时间
    GetTime() {
      this.days = [
        {
          week: "全部",
          day: "-",
          i: "",
        },
      ];
      var date = new Date();
      var base = Date.parse(date); // 转换为时间戳
      // var year = date.getFullYear(); //获取当前年份
      var mon = date.getMonth() + 1; //获取当前月份
      var week = date.getDay();
      var day = date.getDate(); //获取当前日
      var oneDay = 24 * 3600 * 1000;
      var daytime = `${mon >= 10 ? mon : "0" + mon}${
        day >= 10 ? day : "0" + day
      }${week}`; //今日时间
      this.$data.daytime = daytime; // 今日时间赋值给变量
      var time = `今天-${mon}-${day}`;
      var daytimeArr = [time];
      for (var i = 1; i < 6; i++) {
        //前七天的时间
        var now = new Date((base += oneDay));
        var month = now.getMonth() + 1;
        var mday = now.getDate();
        var week = now.getDay();
        var weeks = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];
        daytimeArr.push(
          [
            weeks[week],
            month >= 10 ? month : "0" + month,
            mday >= 10 ? mday : "0" + mday,
          ].join("-")
        );
      }
      daytimeArr.forEach((v, i) => {
        this.days.push({
          week: v.split("-")[0],
          day: `${v.split("-")[1]}-${v.split("-")[2]} `,
          i,
        });
      });
    },
  },
};
</script>

<style lang="scss">
.box {
  display: flex;
  flex-direction: column;
  background-color: #f9f9f9;
  padding: 36rpx 32rpx 0 32rpx;
  height: 100vh;
}

.choose {
  padding-left: 212rpx;
  display: flex;
  align-items: center;
  width: 100wh;
  /* 如果您想让slot内容占满整个导航栏的宽度 */
  flex: 1;

  text {
    font-size: 32rpx;
    font-weight: 600;
    color: #ffffff;
    opacity: 0.6;
  }

  .active {
    font-size: 36rpx;
    opacity: 1 !important;
  }
}
.search{
  display: flex;
  position: relative;
  margin-bottom: 15rpx;
  .searchBtn{
    z-index:9990;
    position: absolute;
    width: 104rpx;
    height: 60rpx;
    top:10rpx;
    right:10rpx;
    line-height: 60rpx;
    text-align: center;
    color:white;
    
    background: #4575F6;
    border-radius: 124px 124px 124px 124px;
  }
}
.content {
  display: flex;
  margin-top: 24rpx;
  padding: 24rpx 0 24rpx 24rpx;
  background-color: #ffffff;
  border-radius: 24px;

  .right {
    display: flex;
    flex-direction: column;
    margin-left: 16rpx;

    .name {
      display: flex;
      font-size: 32rpx;
      font-weight: 600;
      color: #333333;
      opacity: 0.8;
    }

    .ti {
      margin-top: 8rpx;
      font-size: 24rpx;
      font-weight: 600;
      color: #333333;
      opacity: 0.4;
    }

    .address {
      margin-top: 8rpx;

      .t2 {
        font-size: 24rpx;
        font-weight: 600;
        color: #333333;
        opacity: 0.4;
      }

      .gps {
        padding-left: 8rpx;
        font-size: 24rpx;
        font-weight: 600;
        color: #4575f6;
      }
    }
  }
}

.newData {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 24rpx;
  margin-bottom: 18rpx;
  padding-left: 27rpx;
  padding-bottom: 18upx;


  .date {
    text-align: center;
    border: 1rpx solid #4575f6;
    border-radius: 24rpx;
    font-size: 24rpx;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    width: 96rpx;
    height: 124rpx;
    margin-right: 10rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;

    .week {
      color: #4575f6;
    }

    .day {
      color: #4575f6;
    }
  }

  .checked {
    background-color: #4575f6;

    .checked_text {
      color: #fff;
    }
  }
}
</style>
