<template>
  <view :class="['index', { index__qrcode: isOpenWechat }]">
    <view v-if="isOpenWechat && isOpenWechat === 'true'" class="qrcode">
      <view class="qrcode__title">关注公众号，及时获取聊天信息</view>
      <image
        class="qrcode__img"
        @click="previewImage"
        src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/qrcode_for_gh_9dd2f455ac44_258.jpg"
      ></image>
      <view class="qrcode__text">点击保存后微信扫一扫</view>
    </view>
    <view v-else>
      <view class="left">
        <view class="view-text">消息提醒时间段</view>
        <view class="view-time">
          <view class="view-time-left">
            <view class="view-time-text">每日10-22点开启提醒</view>
            <view class="view-time-remind"
              >设置的提醒范围越长越有助于收到实时信息哦</view
            >
          </view>
          <view class="view-time-button">
            <picker
              mode="multiSelector"
              @change="change($event)"
              :value="index"
              :range="timeOptions"
            >
              <button
                :class="
                  startHour === 0 && endHour === 24
                    ? 'view-time-button-1'
                    : 'view-time-hover-1'
                "
              >
                {{ startHour === 0 ? 10 : startHour }}点~{{
                  endHour === 24 ? 22 : endHour
                }}点
              </button>
            </picker>
            <button
              @click="changeHour(0, 24)"
              :class="
                startHour !== 0 && endHour !== 24
                  ? 'view-time-button-2'
                  : 'view-time-hover-2'
              "
            >
              全天任意时间
            </button>
          </view>
        </view>
        <view class="game-message">组局消息</view>

        <view class="game-card">
          <view class="game-card-1-flex">
            <view class="game-card-1">
              <view class="game-card-1-text-1">组局已拼成</view>
              <view class="game-card-1-text-2">公众号：组局满员通知</view>
            </view>
            <view class="card-1-switch">
              <switch :checked="gameSuccess" @change="changeGameSuccess" />
            </view>
          </view>
          <view class="game-card-1-flex">
            <view class="game-card-1">
              <view class="game-card-1-text-1">组局开始提醒</view>
              <view class="game-card-1-text-2">公众号：活动开始提醒</view>
            </view>
            <switch
              class="card-1-switch"
              :checked="gameStart"
              @change="changeGameStart"
            />
          </view>
          <view class="game-card-1-flex">
            <view class="game-card-1">
              <view class="game-card-1-text-1">组局申请审核</view>
              <view class="game-card-1-text-3"
                >公众号：成员加入通知、组局审核成功/失败通知</view
              >
            </view>
            <switch
              class="card-3-switch"
              :checked="gameAudit"
              @change="changeGameAudit"
            />
          </view>
        </view>
      </view>

      <view class="im-text">聊天消息</view>

      <view class="im-view">
        <view class="game-card-1-flex">
          <view class="game-card-1">
            <view class="game-card-1-text-1">私聊未读消息提醒</view>
            <view class="game-card-1-text-3">公众号：未处理消息提醒</view>
          </view>
          <switch
            class="card-3-switch"
            :checked="imPrivate"
            @change="changeImPrivate"
          />
        </view>
        <view class="game-card-1-flex">
          <view class="game-card-1">
            <view class="game-card-1-text-1">群聊未读消息提醒</view>
            <view class="game-card-1-text-3">公众号：未处理消息提醒</view>
          </view>
          <switch
            class="card-3-switch"
            :checked="imGroup"
            @change="changeImGroup"
          />
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import UserService from "../../service/user.js";
import gameCreated from "../components/game-created.vue";

export default {
  components: {
    gameCreated,
  },
  data() {
    return {
      startHour: 10,
      endHour: 22,
      gameSuccess: true,
      gameStart: true,
      gameAudit: true,
      imPrivate: true,
      imGroup: true,
      timeOptions: [
        [
          "0点",
          "1点",
          "2点",
          "3点",
          "4点",
          "5点",
          "6点",
          "7点",
          "8点",
          "9点",
          "10点",
          "11点",
          "12点",
          "13点",
          "14点",
          "15点",
          "16点",
          "17点",
          "18点",
          "19点",
          "20点",
          "21点",
          "22点",
          "23点",
        ],
        [
          "1点",
          "2点",
          "3点",
          "4点",
          "5点",
          "6点",
          "7点",
          "8点",
          "9点",
          "10点",
          "11点",
          "12点",
          "13点",
          "14点",
          "15点",
          "16点",
          "17点",
          "18点",
          "19点",
          "20点",
          "21点",
          "22点",
          "23点",
          "24点",
        ],
      ],
      index: [10, 22 - 1],
      isOpenWechat: false,
    };
  },
  onLoad(o) {
    const pages = getCurrentPages()
    const url = pages[pages.length - 1].$page.fullPath
    const trackingID = this.$getParam(url, 'trackingID')
    if(trackingID) this.$eventRecord(trackingID)
    if (o.isOpenWechat) {
      this.isOpenWechat = o.isOpenWechat;
      if (this.isOpenWechat) {
        uni.setNavigationBarTitle({
          title: "关注公众号",
        });
      }
    }
    UserService.getUserMessageInfo().then((userMessageInfo) => {
      this.startHour = userMessageInfo.startHour;
      this.endHour = userMessageInfo.endHour;
      this.gameSuccess = userMessageInfo.gameSuccess;
      this.gameStart = userMessageInfo.gameStart;
      this.gameAudit = userMessageInfo.gameAudit;
      this.imPrivate = userMessageInfo.imPrivate;
      this.imGroup = userMessageInfo.imGroup;
      if (userMessageInfo.startHour !== 0 && userMessageInfo.endHour !== 24) {
        this.index = [userMessageInfo.startHour, userMessageInfo.endHour - 1];
      }
    });
  },
  methods: {
    // 预览图片保存
    previewImage() {
      uni.previewImage({
        urls: [
          "https://basic-1258404477.cos.ap-nanjing.myqcloud.com/activity/qrcode_for_gh_9dd2f455ac44_258.jpg",
        ],
      });
    },
    change: function (e) {
      let index = e.target.value;
      let startHour = this.timeOptions[0][index[0]].replace("点", "");
      let endHour = this.timeOptions[1][index[1]].replace("点", "");
      if (startHour > endHour) {
        uni.showToast({
          title: "开始时间不能大于结束时间",
          icon: "none",
        });
      } else {
        this.changeHour(startHour, endHour);
        this.index = index;
      }
    },
    changeHour(startHour, endHour) {
      this.startHour = startHour;
      this.endHour = endHour;
      UserService.updateUserMessage({
        startHour: this.startHour,
        endHour: this.endHour,
      });
    },
    changeGameSuccess() {
      this.gameSuccess = !this.gameSuccess;
      UserService.updateUserMessage({
        gameSuccess: this.gameSuccess,
      });
    },
    changeGameStart() {
      this.gameStart = !this.gameStart;
      UserService.updateUserMessage({
        gameStart: this.gameStart,
      });
    },
    changeGameAudit() {
      this.gameAudit = !this.gameAudit;
      UserService.updateUserMessage({
        gameAudit: this.gameAudit,
      });
    },
    changeImPrivate() {
      this.imPrivate = !this.imPrivate;
      UserService.updateUserMessage({
        imPrivate: this.imPrivate,
      });
    },
    changeImGroup() {
      this.imGroup = !this.imGroup;
      UserService.updateUserMessage({
        imGroup: this.imGroup,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.index {
  min-height: 100vh;
  width: 100%;
  height: 100%;
  background: #f5f3f3;
  &__qrcode {
    background: #ffffff;
  }
}
.qrcode {
  width: 600upx;
  margin-left: 50%;
  transform: translateX(-50%);
  text-align: center;
  background: #fef4e6;
  margin-top: calc(50vh - 420upx);
  &__title {
    padding: 60upx 0 30upx 0;
  }
  &__img {
    width: 480upx;
    height: 480upx;
  }
  &__text {
    padding: 30upx 0 60upx 0;
  }
}

.left {
  margin: 0 22upx 0 26upx;
}

.view-text {
  height: 108upx;
  font-size: 32upx;
  font-weight: 600;
  color: #000000;
  line-height: 108upx;
}

.view-time {
  width: 704upx;
  height: 274upx;
  background: #ffffff;
  border-radius: 12upx;
}

.view-time-left {
  margin-left: 30upx;
}

.view-time-text {
  font-size: 28upx;
  font-weight: 500;
  color: #000000;
  height: 84upx;
  line-height: 84upx;
}

.view-time-remind {
  font-size: 28upx;
  font-weight: 400;
  color: #999999;
  height: 40upx;
  line-height: 40upx;
}

.view-time-button {
  margin: 40upx 20upx 0 20upx;
  display: flex;
}

.view-time-button-1 {
  width: 292upx;
  height: 72upx;
  background: #f1f1f1;
  border-radius: 6upx;
  line-height: 72upx;
  font-size: 28upx;
  color: #000000;
  font-weight: 600;
}
.view-time-hover-1 {
  width: 292upx;
  height: 72upx;
  background: #000000;
  border-radius: 6upx;
  line-height: 72upx;
  font-size: 28upx;
  color: #ffffff;
  font-weight: 600;
}

.view-time-button-2 {
  width: 292upx;
  height: 72upx;
  background: #f1f1f1;
  border-radius: 6upx;
  line-height: 72upx;
  font-size: 28upx;
  color: #000000;
  font-weight: 600;
  margin-left: 30upx;
}

.view-time-hover-2 {
  width: 292upx;
  height: 72upx;
  background: #000000;
  border-radius: 6upx;
  line-height: 72upx;
  font-size: 28upx;
  color: #ffffff;
  font-weight: 600;
  margin-left: 30upx;
}
.game-message {
  width: 128upx;
  height: 44upx;
  font-size: 32upx;
  font-weight: 600;
  color: #000000;
  line-height: 44upx;
  margin: 50upx 576upx 24upx 0;
}

.game-card {
  width: 704upx;
  height: 460upx;
  background: #ffffff;
  border-radius: 12upx;
}
.game-card-1-flex {
  display: flex;
}

.game-card-1 {
  padding: 38upx 0 0 30upx;
}

.game-card-1-text-1 {
  width: 240upx;
  height: 40upx;
  font-size: 28upx;
  font-weight: 600;
  color: #000000;
  line-height: 40upx;
}

.game-card-1-text-2 {
  width: 280upx;
  height: 40upx;
  font-size: 28upx;
  font-weight: 400;
  color: #999999;
  line-height: 40upx;
}

.game-card-1-text-3 {
  width: 442upx;
  height: 40upx;
  font-size: 28upx;
  font-weight: 400;
  color: #999999;
  line-height: 40upx;
}

.card-1-switch {
  margin: 32upx 16upx 0 268upx;
  width: 110upx;
  height: 82upx;
  align-items: center;
  display: flex;
  justify-content: center;
}
.card-3-switch {
  margin: 36upx 16upx 0 106upx;
  width: 110upx;
  height: 82upx;
  align-items: center;
  display: flex;
  justify-content: center;
}

.im-text {
  width: 128upx;
  height: 44upx;
  font-size: 32upx;
  font-weight: 600;
  color: #000000;
  line-height: 44upx;
  margin: 24upx 598upx 0 24upx;
}

.im-view {
  width: 704upx;
  height: 296upx;
  background: #ffffff;
  border-radius: 12upx;
  margin: 24upx 22upx 0 24upx;
}
</style>