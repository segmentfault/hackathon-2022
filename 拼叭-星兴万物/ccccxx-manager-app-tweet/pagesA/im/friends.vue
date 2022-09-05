<template>
  <view>
    <comHead color="#333333" autoPadding>
      <view class="flex-c bar-bg">
        <view
          class="bar-item"
          @click="changeList(idx + 1)"
          :key="idx"
          v-for="(item, idx) in tabs"
        >
          {{ item }}
          <image
            v-if="queryType == idx + 1"
            class="bar-line"
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/bar-line.png"
          />
        </view>
      </view>
    </comHead>
    <!-- <view class="v-button">
      <view class="button-g" @click="changeList(1)">关注</view>
      <view class="button-f" @click="changeList(2)">粉丝</view>
    </view> -->
    <friend-item
      v-for="(item, i) in friendsList"
      :friend="item"
      :key="i"
      :queryType="queryType"
      card
    ></friend-item>

    <view class="smallTip" v-if="!friendsList.length">
      <div class="top">
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/e08bc21e-f918-4791-923b-d6b6b63260a3.png"
        ></image>
      </div>
      <view class="bottom">
        <view>暂无消息</view>
      </view>
    </view>
  </view>
</template>

<script>
import IMService from '../../service/im.js';
import friendItem from '../components/friend-item.vue';
import comHead from '../../components/com-head.vue';
export default {
  components: {
    friendItem,
    comHead,
  },
  events: {
    friendsUpdate: 'reload',
  },
  data() {
    return {
      tabs: ['我的关注', '我的粉丝'],
      queryType: 1,
      friendsList: [],
      pageNo: 1, // 页数
      pageSize: 10, // 页面大小
      total: 0,
    };
  },
  onLoad() {
    this.$eventRecord(85);
  },
  mounted() {
    this.getFriends();
    IMService.markFriendsRead();
  },
  onReachBottom() {
    // if (this.pageNo * this.pageSize < this.total) {
      this.getFriends();
    // }
  },
  onShow() {
    // 埋点
    let res = wx.getLaunchOptionsSync();
    res.scene = String(res.scene);
    this.$nextTick(() => {
      setTimeout(() => {
        if (res.scene === '1001') {
          this.$eventRecord(76);
        }
      }, 3000);
    });
  },
  methods: {
    changeList(queryType) {
      this.pageNo = 1
      this.queryType = queryType;
      this.friendsList = [];
      this.getFriends();
    },
    reload() {
      this.queryType = 1;
      this.friendsList = [];
      this.getFriends();
    },
    getFriends() {
      return IMService.queryFriendsPage({
        pageNo: this.pageNo,
        pageSize: this.pageSize,
        queryType: this.queryType,
      }).then((res) => {
        this.total = res.total;
        this.pageNo++
        this.friendsList = this.friendsList.concat(res.list || []);
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.flex-c {
  display: flex;
  align-items: center;
}

.bar-bg {
  position: relative;
  height: 100%;

  .bar-item {
    position: relative;
    margin: 0upx 32upx;

    .bar-line {
      position: absolute;
      bottom: -9upx;
      left: calc((100% - 58upx) / 2);
      height: 6upx;
      width: 58upx;
    }
  }
}

.v-button {
  height: 80upx;
}

.button-g {
  width: 50% !important;
  height: 80upx;
  line-height: 80upx;
  float: left;
  box-sizing: border-box;
  text-align: center;
  border-right: 3upx solid #ccc;
  border-bottom: 2upx solid #ccc;
}
.button-f {
  width: 50% !important;
  height: 80upx;
  line-height: 80upx;
  float: left;
  box-sizing: border-box;
  text-align: center;
  border-bottom: 2upx solid #ccc;
}

.smallTip {
  .top {
    width: 180upx;
    height: 180upx;
    margin: 120upx auto 80upx;
    image {
      width: 100%;
      height: 100%;
    }
  }
  .bottom {
    text-align: center;
    color: #999;
  }
}
</style>
