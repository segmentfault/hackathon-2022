<template>
  <view class="im-index">
    <view v-if="imErrorMsg">{{ imErrorMsg }}</view>
    <view v-else>
      <view
        v-if="isTipsShow && !isDisplay"
        class="tips"
        @click="openMessageSet"
      >
        <image
          class="tips-icon"
          src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Ftips-1.png"
        />
        <view class="tips-text">
          <view class="tips-text-1"> 开启通知 </view>
          <view class="tips-text-2"> 不再错过组局消息，和好友邀约！ </view>
        </view>
        <image
          class="tips-button"
          src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Ftips-2.png"
        />
      </view>
      <view v-if="isDisplay" class="v-image">
        <image
          src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fdesc.png"
        ></image>
      </view>
      <view class="flex">
        <view class="chat-item" @click="openStranger">
          <image
            src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fmosr.png"
          />
          <view class="badge-top" v-if="totalStrangerCount">
            {{ totalStrangerCount > 99 ? '99' : totalStrangerCount }}
          </view>
          <view class="content">
            <view class="title">
              <view class="name">陌生人</view>
            </view>
          </view>
        </view>
        <!-- 关注信息 -->
        <view class="chat-item" @tap="$u.throttle(goFriendsList, 500)">
          <image
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/type/user-heart-fill.png"
          />
          <view class="badge-top" v-if="redpoint">
            {{ redpoint > 99 ? '99+' : redpoint }}
          </view>
          <view class="content">
            <view class="title">
              <view class="name">我的关注</view>
            </view>
          </view>
        </view>
        <!-- 官方消息 -->
        <view class="chat-item" @tap="$u.throttle(openSystemChat, 500)">
          <image
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/type/message.png"
          />
          <view
            class="badge-top"
            v-if="
              allConversation[0].type === '@TIM#SYSTEM' &&
              allConversation[0].unreadCount
            "
          >
            {{
              allConversation[0].unreadCount > 99
                ? '99+'
                : allConversation[0].unreadCount
            }}
          </view>
          <view class="content">
            <view class="title">
              <view class="name">官方消息</view>
            </view>
          </view>
        </view>
      </view>
      <!-- 用户信息 -->
      <view
        v-for="(conversation, i) in allConversation"
        :key="i"
        class="chat-item"
        @click="chat(conversation)"
        @longpress="delConversation(conversation)"
        v-if="
          conversation.type !== '@TIM#SYSTEM' &&
          (conversation.type === 'GROUP' ||
            (conversation.type === 'C2C' && conversation.isFriend))
        "
      >
        <view v-if="conversation.type === 'C2C'">
          <avatar :avatarUrl="conversation.userProfile.avatar" im></avatar>
        </view>
        <view v-else>
          <avatar :avatarUrl="conversation.groupProfile.avatar" im></avatar>
        </view>
        <view class="badge" v-if="conversation.unreadCount">
          {{ conversation.unreadCount > 99 ? '99+' : conversation.unreadCount }}
        </view>
        <view class="content">
          <view class="title">
            <view class="name">{{ getConversationName(conversation) }}</view>
            <view class="time"
              >{{
                $utils
                  .dayjs(conversation.lastMessage.lastTime * 1000)
                  .format('MM/DD HH:mm')
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
        v-if="
          conversation.type !== '@TIM#SYSTEM' &&
          (conversation.type === 'GROUP' ||
            (conversation.type === 'C2C' && conversation.isFriend)) &&
          isShowConversation(conversation, allConversation)
        "
      >
        <view v-if="conversation.type === 'C2C'">
          <avatar :avatarUrl="conversation.userProfile.avatar" im></avatar>
        </view>
        <view v-else>
          <avatar :avatarUrl="conversation.groupProfile.avatar" im></avatar>
        </view>
        <view class="badge" v-if="conversation.unreadCount">
          {{ conversation.unreadCount > 99 ? '99+' : conversation.unreadCount }}
        </view>
        <view class="content">
          <view class="title">
            <view class="name">{{ getConversationName(conversation) }}</view>
            <view class="time"
              >{{
                $utils
                  .dayjs(conversation.lastMessage.lastTime * 1000)
                  .format('MM/DD HH:mm')
              }}
            </view>
          </view>
          <view class="text">{{ getContent(conversation) }}</view>
        </view>
      </view>
    </view>
    <!-- 底部导航栏 -->
    <view-tabbar v-model="tableIndex"></view-tabbar>
  </view>
</template>

<script>
import IM from './lib';
import IMMixin from './lib/mixin.js';
import IMService from '@/service/im.js';
import UserService from '@/service/user.js';
import pageMix from '@/framework/mixins/pageMix';

export default {
  mixins: [pageMix, IMMixin],
  data() {
    return {
      tableIndex: 2,
      redpoint: 0,
      isDisplay: false,
      isTipsShow: false,
      hisConversation: [],
    };
  },
  onLoad(options) {
		if (!uni.getStorageSync('userInfo')) {
		  this.$toast('请先登录');
		  setTimeout(() => {
		    uni.navigateTo({
		      url: `/pagesA/user/login?isOnload=${true}`,
		    });
		  });
		  return;
		}
    this.$eventRecord(options.trackingID || 65);
		this.init();
		this.getRedPoint();
    this.getBindRelation(options);
    // 分享拉新关系
    this.getshareInfo(options);
    // 获取用户当前经纬度
    this.getLocation();
    if(this.allConversation.length) { 
      this.queryHisConversation(this.allConversation)
    }
  },
  onShow() {
    let res = wx.getLaunchOptionsSync();
    res.scene = String(res.scene);
    if (res.scene === '1157') {
      // 埋点
      this.$eventRecord(83);
    } else if (res.scene === '1043') {
      // 埋点
      this.$eventRecord(88);
    }
  },
  async onPullDownRefresh() {
    await this.init();
    uni.stopPullDownRefresh()
  },
  computed: {
    totalStrangerCount() {
      let res = 0;
      this.allConversation.forEach((v) => {
        if (v.type !== '@TIM#SYSTEM' && v.type !== 'GROUP' && !v.isFriend) {
          res += v.unreadCount;
        }
      });
      this.hisConversation.forEach((v) => {
        if (
          this.isShowConversation(v, this.allConversation) &&
          v.type !== '@TIM#SYSTEM' &&
          v.type !== 'GROUP' &&
          !v.isFriend &&
          v.unreadCount
        ) {
          res += v.unreadCount;
        }
      });
      // 陌生人小红点数据
      return res;
    },
    totalMenuCount() {
      let res = 0;
      this.allConversation.forEach((v) => {
        if (v.unreadCount) {
          res += v.unreadCount;
        }
      });
      this.hisConversation.forEach((v) => {
        if (
            this.isShowConversation(v, this.allConversation) && v.unreadCount
        ) {
          res += v.unreadCount;
        }
      });
      // 小红点数据
      this.$store.commit('setRedpoint', res)
      return res;
    },
  },
  methods: {
    async init() {
      let tim = await IM.init();
    },
    async getRedPoint() {
      // 获取红点，每次切换tab的时候查一下
      this.redpoint = await IMService.checkFriendsRedPoint();
    },
    chat(conversation) {
      this.$u.throttle(() => {
        IM.openConversation(conversation);
        this.$eventRecord(57);
      }, 1000);
    },
    /**
     * 打开系统消息会话
     */
    openSystemChat() {
      let conversation;
      this.$eventRecord(175);
      this.allConversation.forEach((item) => {
        if (item.type === '@TIM#SYSTEM') {
          conversation = item;
        }
      });
      if (!conversation) {
        uni.showToast({
          title: '暂无官方消息',
          icon: 'none',
          duration: 1000,
        });
      } else {
        IM.openConversation(conversation);
      }
    },
    goFriendsList() {
      // 去我的关注列表
      uni.navigateTo({
        url: '/pagesA/im/friends',
      });
      //清空消息
      this.redpoint = 0;
      // 埋点
      this.$eventRecord(85);
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
          IM.deleteConversation(conversation);
        });
    },
    openMessageSet() {
      UserService.queryUserInfo().then((item) => {
        let isOpenWechat = item.subscription !== 1;
        this.$eventRecord(120);
        this.$u.throttle(() => {
          uni.navigateTo({
            url: '/pagesA/user/message?isOpenWechat=' + isOpenWechat,
          });
        }, 500);
      });
    },
    openStranger() {
      this.$u.throttle(() => {
        uni.navigateTo({
          url: '/pagesA/im/stranger',
        });
      }, 500);
    },
    queryHisConversation() {
      let conversationIDList = [];
      for (let conversation of this.allConversation) {
        conversationIDList.push(conversation.conversationID);
      }
      return IMService.queryUserConversationHis(conversationIDList);
    },
    //二次校验历史数据是否存在IM自带会话中
    isShowConversation(conversation, allConversation) {
      let isShow = true;
      for (let item of allConversation) {
        if (conversation.conversationID === item.conversationID) {
          isShow = false;
          break;
        }
      }
      return isShow;
    },
    getMenuCount() {
      let res = 0;
      this.allConversation.forEach((v) => {
        res += v.unreadCount;
      });
      this.hisConversation.forEach((v) => {
        if (v.unreadCount) {
          res += v.unreadCount;
        }
      });
      res += this.redpoint;
      return res;
    },
  },
  // onShareAppMessage(res) {
  //   return {
  //     title: '',
  //     path: `pages/index/index?shareUserId=${this.myUserId}`,
  //     imageUrl: '',
  //   };
  // },
};
</script>

<style lang="scss" scoped>
.im-index {
  background: #ffffff;
  margin-bottom: 150rpx;
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
.badge-top {
  position: absolute;
  left: 125upx;
  top: -10upx;
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
.v-image {
  z-index: 99999;
  position: absolute;
  width: 680upx;
  height: 516upx;
  margin: -40upx 26upx 0 44upx;
}
.tips {
  width: 708upx;
  height: 104upx;
  background: linear-gradient(270deg, #fdf3f3 0%, #f9e4e4 100%);
  border-radius: 12upx;
  margin: 20upx 20upx 0 22upx;
}
.tips-text-1 {
  width: 104upx;
  height: 36upx;
  font-size: 26upx;
  font-weight: 600;
  color: #000000;
  line-height: 36upx;
  margin: 0 504upx 18upx 100upx;
  padding-top: 14upx;
  position: absolute;
}
.tips-text-2 {
  width: 390upx;
  height: 36upx;
  font-size: 26upx;
  font-weight: 400;
  color: #000000;
  line-height: 36upx;
  margin: 50upx 218upx 0 100upx;
  position: absolute;
}
.tips-icon {
  width: 36upx;
  height: 36upx;
  position: absolute;
  margin: 32upx 630upx 36upx 42upx;
}
.tips-button {
  width: 120upx;
  height: 44upx;
  position: absolute;
  margin: 30upx 28upx 30upx 570upx;
}
</style>
