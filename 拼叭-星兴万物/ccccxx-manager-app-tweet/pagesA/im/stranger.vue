<template>
  <view class="im-index">
    <view v-if="imErrorMsg">{{ imErrorMsg }}</view>
    <view v-else>
      <view v-if="isData(allConversation)" class="not-message">暂无陌生人消息</view>
      <view
        v-for="(conversation, i) in allConversation"
        :key="i"
        class="chat-item"
        @click="chat(conversation)"
        @longpress="delConversation(conversation)"
        v-if="conversation.type !== '@TIM#SYSTEM' && conversation.type !== 'GROUP' && !conversation.isFriend && isShow"
      >
        <view v-if="conversation.type === 'C2C'">
          <avatar :avatarUrl="conversation.userProfile.avatar" im></avatar>
        </view>
        <view v-else>
          <avatar :avatarUrl="conversation.groupProfile.avatar" im></avatar>
        </view>
        <view
          class="badge"
          v-if="conversation.unreadCount"
        >{{ conversation.unreadCount > 99 ? "99+" : conversation.unreadCount }}</view>
        <view class="content">
          <view class="title">
            <view class="name">{{ getConversationName(conversation) }}</view>
            <view class="time">
              {{
              $utils
              .dayjs(conversation.lastMessage.lastTime * 1000)
              .format("MM/DD HH:mm")
              }}
            </view>
          </view>
          <view class="text">{{ getContent(conversation) }}</view>
        </view>
      </view>
      <view
          v-for="(conversation, i) in hisConversation"
          :key="i"
          class="chat-item"
          @click="chat(conversation)"
          @longpress="delConversation(conversation)"
          v-if="conversation.type !== '@TIM#SYSTEM' && conversation.type !== 'GROUP' && !conversation.isFriend"
      >
        <view v-if="conversation.type === 'C2C'">
          <avatar :avatarUrl="conversation.userProfile.avatar" im></avatar>
        </view>
        <view v-else>
          <avatar :avatarUrl="conversation.groupProfile.avatar" im></avatar>
        </view>
        <view
            class="badge"
            v-if="conversation.unreadCount"
        >{{ conversation.unreadCount > 99 ? "99+" : conversation.unreadCount }}</view>
        <view class="content">
          <view class="title">
            <view class="name">{{ getConversationName(conversation) }}</view>
            <view class="time">
              {{
                $utils
                    .dayjs(conversation.lastMessage.lastTime * 1000)
                    .format("MM/DD HH:mm")
              }}
            </view>
          </view>
          <view class="text">{{ getContent(conversation) }}</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import IM from '../../pages/im/lib';
import IMMixin from '../../pages/im/lib/mixin.js';
import IMService from "../../service/im.js";

export default {
  mixins: [IMMixin],
  data() {
    return {
      hisConversation: [],
      isShow: false,
    };
  },
  onLoad() {
    this.$eventRecord(92)
  },
  async onShow(){
    uni.showLoading({
      title: "加载中...",
      mask: true,
    });
    let conversationIDList = [];
    for (let conversation of this.allConversation) {
      conversationIDList.push(conversation.conversationID);
    }
    this.hisConversation = await IMService.queryUserConversationHis(conversationIDList);
    this.isShow = true;
    uni.hideLoading();
  },
  methods: {
    isData(allConversation) {
      let isData = true
      allConversation.forEach((item) => {
        if (
          item.type !== '@TIM#SYSTEM' &&
          item.type !== 'GROUP' &&
          !item.isFriend
        ) {
          isData = false
        }
      })
      this.hisConversation.forEach((item) => {
        if (
            item.type !== '@TIM#SYSTEM' &&
            item.type !== 'GROUP' &&
            !item.isFriend
        ) {
          isData = false
        }
      })
      return isData
    },
    chat(conversation) {
      this.$u.throttle(() => {
        IM.openConversation(conversation)
        this.$eventRecord(57)
      }, 500)
    },
    //删除会话
    delConversation(conversation) {
      if (conversation.type !== '@TIM#SYSTEM')
        this.$showModal({
          confirmColor: '#ff0000',
          content: '是否确认删除会话？',
          confirmText: '确定',
          cancelColor: '#666',
          cancelText: '取消',
        }).then(() => {
          IM.deleteConversation(conversation)
        })
    },
  },
}
</script>

<style lang="scss" scoped>
.im-index {
  background: #ffffff;
}
.not-message {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  min-height: 100vh;
}
.flex {
  display: flex;
  align-items: center;
  justify-content: space-around;
  margin-top: 42upx;

  .chat-item {
    &:nth-child(1),
    &:nth-child(2),
    &:nth-child(3) {
      display: flex;
      flex-direction: column;

      .content {
        display: block;
        margin-left: 0;
      }
    }

    /deep/ image {
      width: 72upx;
      height: 72upx;
    }
  }
}
.chat-item-first {
  height: 176upx;
  padding: 0 42upx 0 48upx;
  display: flex;
  align-items: center;
  position: relative;
}
.chat-item-first-img {
  width: 72upx;
  height: 72upx;
  flex-shrink: 0;
}
.chat-item {
  height: 176upx;
  padding: 0 42upx 0 48upx;
  display: flex;
  align-items: center;
  position: relative;

  &:first-child {
    // margin-top: 80upx;
  }
}
.badge {
  position: absolute;
  left: 125upx;
  top: 10upx;
}
.content {
  flex: 1;
  height: 100%;
  border-bottom: 1upx solid #f5f5f5;
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-top: 10upx;
  margin-left: 32upx;
  overflow: hidden;
  zoom: 1;

  .text {
    margin-top: 10upx;
    font-size: 26upx;
    font-weight: 400;
    color: #666666;
    line-height: 36upx;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
}
.title {
  height: 42upx;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .name {
    font-weight: 600;
    color: #000000;
    font-size: 30 upx;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .time {
    font-weight: 400;
    color: #999999;
    line-height: 42upx;
    white-space: nowrap;
  }
}
</style>
