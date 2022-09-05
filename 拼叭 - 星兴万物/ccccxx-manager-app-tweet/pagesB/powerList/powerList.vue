<template>
  <view class="page">
    <view class="choose">
      <text :class="{ active: navId == 1 }" @click="toggleNav(1)"
        >助力排行榜</text
      >
      <text
        :class="{ active: navId == 2 }"
        style="margin-left: 106upx"
        @click="toggleNav(2)"
        >参与用户</text
      >
    </view>
    <view class="list" id="list">
      <view v-if="navId == 1">
        <view id="Top">
          <view class="progress">
            <view class="top">
              <view class="txt"
                >超过了&nbsp;<text style="color: #e15645ff">{{ getPresent}}%</text
                >&nbsp;的用户</view
              >
              <button class="btn" @tap="showStates">规则说明</button>
            </view>
            <view class="bottom">
              <u-line-progress
                active-color="#E15645"
                inactive-color="#FFDFDE"
                :percent="getPresent"
                :height="16"
                :show-percent="false"
              ></u-line-progress>
            </view>
          </view>
          <view class="rankingInfo">
            <text class="ranking"
              >排名 <text class="userName">用户名</text></text
            >
            <txet class="win">助力值</txet>
          </view>
        </view>
        <scroll-view
          class="scroll-view"
          scroll-y="true"
          lower-threshold="50"
          @scrolltolower="getmoreHelpList"
          @scroll="change"
          :style="{ height: listHeight - TopHeight + 'px' }"
        >
          <view class="user pub" v-if="userHelp.rank">
            <view class="number">
              <view class="rank" v-if="userHelp.rank == 1 || userHelp.rank  == 2 || userHelp.rank  == 3">
                <img :src="userHelp.rank  == 1 ? top[0] : userHelp.rank == 2 ? top[1] : top[2]" />
              </view>
              <view v-else class="num">{{userHelp.rank}}</view>
              <view class="userInfo">
                <img :src="userHelp.head" />
                <view class="userName">{{ userHelp.nickName }}</view>
              </view>
            </view>
            <view class="win">{{userHelp.popularity}}</view>
          </view>
          <view class="userList pub" :class="{color:(index)%2==1}" v-for="(item,index) in list" :key="item.index" >
            <view class="number">
              <view class="rank" v-if="item.rank == 1 || item.rank == 2 || item.rank == 3">
                <img :src="item.rank == 1 ? top[0] : item.rank == 2 ? top[1] : top[2]" />
              </view>
              <view v-else class="num">{{item.rank}}</view>
              <view class="userInfo">
                <img :src="item.head" />
                <view class="userName">{{ item.nickName }}</view>
              </view>
            </view>
            <view class="win">{{item.popularity}}</view>
          </view>
        </scroll-view>
      </view>
      <view v-else class="userCard">
        <scroll-view
          class="scroll-view"
          scroll-y="true"
          lower-threshold="50"
          @scrolltolower="getmoreUlist"
          @scroll="change"
          :style="{ height: listHeight + 'px' }"
        > 
        <view style="padding-top:42upx">
          <view class="card" v-for="item in userList" :key="item.index">
            <img :src="item.head" alt="" />
            <view class="info">
              <veiw class="name">{{ item.nickName }}</veiw>
              <view class="time"
                >抽奖时间：{{
                  $u.timeFormat(item.createTime, "yyyy-mm-dd hh:M")
                }}</view
              >
            </view>
          </view>
          </view>
        </scroll-view>
      </view>
      <view v-if="aState" class="all" :style="{ width: listWidth + 'px' }">
        <text class="txt">已加载全部</text>
      </view>
    </view>
    <view class="shadow" v-if="showState">
      <view class="shadow-body p30">
        <view class="fs36 fw600 text-center">规则说明</view>
        <view class="fs28 color-222 mt30">
          系统在开奖时会根据所有参与者的助力值,按比例算出中奖率进行开奖，助力值高不一定能中奖，但是中奖率会更高！
        </view>
        <view class="fs38 fw600 text-center close" @tap="showState = false"
          >我知道了</view
        >
      </view>
    </view>
  </view>
</template>
<script>
import gameServe from "../../service/game";
export default {
  data() {
    return {
      navId: 1,
      drawId: "",
      query: {
        pageNum: 1,
        pageSize: 8,
        id: 0,
        total: 0,
      },
      Hquery:{
         pageNum: 1,
         pageSize: 15,
         id:0,
         total:0,
      },
      userQuery:{
        id:0
      },
      listHeight: "",
      listWidth: "",
      TopHeight: "",
      showState: 0,
      aState: false,
      top: [
        "https://image-1306191496.cos.ap-nanjing.myqcloud.com/6b0d0b89-706e-4392-87ba-9c1f7878b2e5.png",
        "https://image-1306191496.cos.ap-nanjing.myqcloud.com/ec25720a-3e6b-4075-8422-064c12fb414a.png",
        "https://image-1306191496.cos.ap-nanjing.myqcloud.com/2d9df472-fc9e-435f-96d0-0217c6804274.png",
      ],
      list: {},
      userList: {},
      userHelp:{},
    };
  },
  onLoad(option) {
    this.userQuery.id = option.id;
    this.Hquery.id=option.id;
    this.query.id=option.id
    this.getUserList();
    this.getHelpList();
    this.getUserhelp();
  },
  mounted() {
    const query = uni.createSelectorQuery().in(this);
    query
      .select("#list")
      .boundingClientRect((data) => {
        this.listHeight = data.height;
        this.listWidth = data.width;
      })
      .exec();
    query
      .select("#Top")
      .boundingClientRect((data) => {
        this.TopHeight = data.height;
      })
      .exec();
  },
  computed:{
    getPresent(){
       let pre;
       pre=(this.Hquery.total+1-this.userHelp.rank)/this.Hquery.total*100;
       return pre?parseInt(pre):0;
    }
  },
  methods: {
    change(e) {
      var scrollfy = e.detail.deltaY;
      if (scrollfy > 0) {
        this.aState = false;
      }
    },
    getmoreUlist() {
      if (this.query.pageSize < this.query.total) {
        setTimeout(() => {
          this.query.pageSize += 5;
          this.getUserList();
        }, 150);
      } else {
        this.aState = true;
      }
    },
    getmoreHelpList() {
       if (this.Hquery.pageSize < this.Hquery.total) {
        setTimeout(() => {
          this.query.pageSize += 8;
          this.getHelpList();
        }, 150);
      } else {
        this.aState = true;
      }
    },
    toggleNav(id) {
      this.navId = id;
      this.aState = false;
    },
    //获取用户助力
    getUserhelp(){
       gameServe.getuserhelp(this.userQuery).then((res)=>{
        this.userHelp=res;
      })
    },
    //获取助力列表
    getHelpList(){
      gameServe.gethelpList(this.Hquery).then((res)=>{
        this.list = res.list;
        this.Hquery.total = res.total;
      })
    },
    getUserList() {
      gameServe.getDrawUser(this.query).then((res) => {
        this.userList = res.list;
        this.query.total = res.total;
      });
    },
    showStates() {
      this.showState = true;
    },
  },
};
</script>
<style lang="scss" scoped>
.page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100%;
  background-color: #e15645;
  .choose {
    display: flex;
    margin-top: 62upx;
    justify-content: center;
    text {
      font-size: 32upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 400;
      color: #ffffff;
    }
    .active {
      font-weight: 600 !important;
      border-bottom: 8upx solid #ffff;
    }
  }
  .list {
    margin: 34upx 38upx 192upx 38upx;
    height: 100%;
    border-radius: 16upx;
    background-color: #ffff;
    overflow: hidden;
    .progress {
      padding-top: 46upx;
      .top {
        display: flex;
        justify-content: space-between;
        .txt {
          padding-left: 38upx;
          font-size: 28upx;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #666666;
        }
        .btn {
          margin-right: 30upx;
          padding: 0;
          width: 124upx;
          height: 40upx;
          line-height: 40upx;
          font-size: 24upx;
          background: #dddddd;
          border-radius: 24upx;
        }
      }
      .bottom {
        padding-left: 38upx;
        padding-top: 4upx;
        width: 268upx;
      }
    }
    .rankingInfo {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 35upx;
      padding-top: 24upx;
      padding-bottom: 22upx;
      background-color: #fff5fd;
      font-size: 24upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #e15645;
      .ranking {
        padding-left: 42upx;
      }
      .userName {
        padding-left: 72upx;
      }
      .win {
        width: 75upx;
        padding-right: 78upx;
      }
    }
    .user {
      height: 128upx;
      background-color: #ffe8c1ff;
      margin-bottom: 20upx;
    }
    .pub {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .number {
        display: flex;
        align-items: center;
      }
      .rank {
        margin-left: 24upx;
        img {
          width: 86upx;
          height: 82upx;
        }
      }
      .num {
        margin-left: 48upx;
        color: #ffff;
        width: 38upx;
        height: 38upx;
				line-height: 38upx;
        text-align: center;
        font-size: 24upx;
        font-weight: 600;
        background: #cf4f3f;
        border-radius: 90px;
      }
      .userInfo {
        position: absolute;
        display: flex;
        padding-left: 164upx;
        align-items: center;
        img {
          border-radius: 90px;
          width: 48upx;
          height: 48upx;
        }
        .userName {
          padding-left: 20upx;
          font-size: 24upx;
          font-weight: 400;
          color: #4a3e6c;
        }
      }
      .win {
        margin-right: 78upx;
        font-size: 24upx;
        font-weight: 600;
        color: #f06f6f;
        text-align: center;
        width: 75upx;
      }
    }
    .color{
      background-color: #fff5fd !important;
    }
    .userList {
      height: 80upx;
    }
    .userCard {
      overflow: hidden;
      .card {
        display: flex;
        align-items: center;
        margin-bottom: 20upx;
        height: 148upx;
        background-color: #fbf7f7ff;
        img {
          margin-left: 52upx;
          width: 92upx;
          height: 92upx;
          border-radius: 90px;
        }
        .info {
          display: flex;
          flex-direction: column;
          padding-left: 20upx;
          font-size: 28upx;
          .name {
            font-weight: 800;
          }
          .time {
            padding-top: 12upx;
            font-weight: 400;
          }
        }
      }
    }
  }
  .all {
    position: absolute;
    padding-top: 102upx;
    text-align: center;
    color: #fff;
    font-size: 24upx;
    font-weight: 400;
  }
  .shadow {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    background: rgba(0, 0, 0, 0.5);
    z-index: 999;
  }
  ::-webkit-scrollbar {
    width: 0;
    height: 0;
    color: transparent;
  }
}
</style>