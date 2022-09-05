<template>
  <view class="man-item" :class="{ card }">
    <view
      class="man-item-l"
      :class="[friend.sex === 1 ? 'girl' : 'boy']"
      @click="goUserDetail"
    >
      <avatar shape="circle" :avatarUrl="friend.userInfo.head"></avatar>
    </view>
    <view class="man-item-r">
      <view class="flex-s">
        <view class="name">{{ friend.userInfo.nickName }}</view>
        <!-- 只有群主能发起单聊和打电话 -->
        <view class="flex-c">
          <image
            v-if="friend.mobile"
            class="call-icon"
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/call-icon.png"
            @click="goCall"
          />
          <image
            class="talk-icon"
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/talk-icon.png"
            @click="goChat"
          />
        </view>
      </view>
      <view class="flex-c" style="margin-top: 10upx">
        <view class="man-t">长沙市</view>
        <view class="man-t">{{ friend.userInfo.age ? friend.userInfo.age + "岁":"" }}</view>
        <view class="man-t">{{ friend.userInfo.sex === 1 ? "男" : "女" }}</view>
        <view class="man-t">{{ friend.userInfo.constellation }}</view>
      </view>
      <view class="flex-s" style="margin-top: 14upx">
        <view class="flex-c interest-line">
          兴趣
          <view
            class="interest"
            v-for="(item, index) in friend.userInfo.interests"
            :key="index"
            >{{ item }}</view
          >
        </view>
      </view>

      <view class="buttons">
        <view class="flex-box">
          <button class="btn" v-if="queryType === 1" @click="deleteFriends">
            {{ friend.isRelation === 1 ? "已互关" : "已关注" }}
          </button>
          <button class="btn" v-if="queryType === 2 && friend.isRelation === 1" @click="deleteFriends">
            已互关
          </button>
          <button class="btn active" v-if="queryType === 2 && friend.isRelation === 0" @click="addFriends">关注</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import IM from "../../pages/im/lib/index.js";
import IMService from "../../service/im.js";
import IMMixin from "../../pages/im/lib/mixin.js";

export default {
  mixins: [IMMixin],
  props: {
    friend: Object,
    actions: Boolean,
    card: Boolean,
    queryType: Number,
  },
  data() {
    return {};
  },
  computed: {
    stateName() {
      let namesMap = {
        [this.$constants.MEMBER_STATES.JOINED]: "已通过",
        [this.$constants.MEMBER_STATES.REJECT]: "已拒绝",
        [this.$constants.MEMBER_STATES.KICK]: "已踢出",
      };
      return namesMap[this.friend.state] || "";
    },
  },
  methods: {
    addFriends() {
      // 添加好友
      IMService.addFriends(this.friend.toAccount).then((res) => {
        this.allConversation.forEach(conversation =>{
          if (conversation.conversationID === ('C2C' + this.friend.toAccount)){
            conversation.isFriend = true;
          }
        });
        this.$toast("关注成功", "success");
        uni.$emit("friendsUpdate");
      });
    },
    deleteFriends() {
      // 删除好友
      this.$showModal({
        confirmColor: "#ff0000",
        content: "是否确认取消关注？",
        confirmText: "取消关注",
        cancelColor: "#666",
        cancelText: "再想想",
      }).then(() => {
        IMService.deleteFriends(this.friend.toAccount).then((res) => {
          this.allConversation.forEach(conversation =>{
            if (conversation.conversationID === ('C2C' + this.friend.toAccount)){
              conversation.isFriend = false;
            }
          });
          this.$toast("已取消关注");
          uni.$emit("friendsUpdate");
        });
      });
    },
    goUserDetail() {
      // 去用户主页
      uni.navigateTo({
        url: "/pagesA/user/profile?id=" + this.friend.toAccount,
      });
    },
    goChat() {
      // 打开跟用户的聊天
      IM.chatWithUser(this.friend.toAccount);
      // 埋点
      this.$eventRecord(48);
    },
    goCall() {
      // 打电话给用户
      uni.makePhoneCall({
        phoneNumber: this.friend.mobile,
      });
      // 埋点
      this.$eventRecord(47);
    },
  },
};
</script>

<style lang="scss" scoped>
.buttons {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 32upx;

  .btn {
    width: 220upx;
    height: 64upx;
    border-radius: 36upx;
    border: 1upx solid #d3d3d3;
    font-weight: 600;
    color: #000000;
    font-size: 30upx;
    display: flex;
    align-items: center;
    justify-content: center;

    &.active {
      background: #333333;
      color: #fff;
    }

    &:nth-child(1) {
      margin-right: 36upx;
    }
  }

  .state-name {
    font-size: 30upx;
    font-weight: 600;
    color: #999999;
    line-height: 42upx;
  }
}

.flex-c {
  display: flex;
  align-items: center;
}

.flex-s {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.man-item {
  margin: 60upx 40upx 0px 40upx;
  width: calc(100% - 80upx);
  display: flex;
  justify-content: space-between;

  &.card {
    // 卡片模式
    width: 666upx;
    margin: 60upx 42upx 0;
    padding: 36upx 36upx 42upx 36upx;
    background: #ffffff;
    box-shadow: 0 4upx 76upx 0 rgba(0, 0, 0, 0.1);
    border-radius: 32upx;
    border: 1upx solid #e4e4e4;
  }

  &-l {
    position: relative;
    height: 110upx;
    width: 110upx;
    border-radius: 50%;

    .leader {
      position: absolute;
      display: inline-block;
      right: -10upx;
      top: -10upx;
      font-size: 16upx;
      color: #fff;
      padding: 3upx 12upx;
      background: #000000;
      border-radius: 28upx;
      border: 2upx solid #ffffff;
      z-index: 20;
    }
  }

  &-r {
    width: calc(100% - 142upx);

    .name {
      font-size: 30upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
    }

    .call-icon {
      height: 50upx;
      width: 50upx;
      margin-right: 66upx;
    }

    .talk-icon {
      height: 50upx;
      width: 50upx;
    }

    .man-t {
      margin-right: 10upx;
      font-size: 26upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #999999;
    }

    .interest-line {
      font-size: 26upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #999999;
    }

    .interest {
      padding: 1upx 8upx;
      font-size: 26upx;
      line-height: 26upx;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #666666;
      margin-left: 12upx;
      background: #d8d8d8;
      border-radius: 18upx;
    }
  }
}
</style>
