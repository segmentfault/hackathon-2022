<template>
  <view class="chat-message" :class="{ me: isMe }">
    <!-- 系统消息列表 -->
    <view
      v-if="msg.conversationID === $constants.MSG_TYPES.CONV_SYSTEM"
      class="sys-msg"
    >
      <view class="sys-tip">官方消息通知</view>
      <text>{{ decodeElement(msg).text }}</text>
      <!-- <text>{{"新成员加入："+ msg.nick}}</text> -->
    </view>
    <!-- 群聊或单聊里面的系统消息 -->
    <view
      v-else-if="msg.from === $constants.MSG_TYPES.CONV_SYSTEM"
      class="msg-system"
    >
      {{ decodeElement(msg).text }}
    </view>
    <!-- 非系统消息要展示 -->
    <template
      v-else-if="
        currentConversation.conversationID !== $constants.MSG_TYPES.CONV_SYSTEM
      "
    >
      <!-- 头像 -->
      <avatar
        :avatarUrl="getAvatar(msg.avatar)"
        im
        @click="goUserProfile"
      ></avatar>
      <view class="msg-content" :class="{ me: isMe }">
        <!-- 文本消息 -->
        <view
          v-if="msg.type === $constants.MSG_TYPES.MSG_TEXT"
          class="msg-text"
        >
          {{ msg.payload.text }}
        </view>
        <!-- 图片消息 -->
        <image
          v-else-if="msg.type === $constants.MSG_TYPES.MSG_IMAGE"
          class="msg-img"
          :src="msgInfo.url"
          @click="showOrigin"
          :style="{maxWidth:'236px',width:(msgInfo.width > 236 ? '236px' : msgInfo.width + 'px'),height:msgInfo.height + 'px'}"
        >
        </image>
        <!-- 语音消息-->
        <view
          v-else-if="msg.type === $constants.MSG_TYPES.MSG_SOUND"
          :class="isMe ? 'msg-sound' : 'msg-sound-me'"
          :style="{ width: getSoundWidth(msg.payload.second) + 'px' }"
          @click="playVoice(index,msg.payload.url)"
        >
          <text class="msg-sound-text"
            >{{ msg.payload.second === 0 ? 1 : msg.payload.second }}”</text
          >
          <view v-if="isMe" class="wifi">
            <img src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fsound_me.png" class="wifi-icon" />
          </view>
          <view
            v-else
            :style="{ margin: getSoundWifiLeft(msg.payload.second) }"
          >
            <img src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fsound.png" class="wifi-icon" />
          </view>
        </view>
        <!-- 地理位置消息 -->
        <view v-else-if="msg.type === 'TIMCustomElem' && msg.payload.description && msg.payload.description === 'TIMLocationElem'"
              class="msg-location"
              @click="openLocation(msg.payload.data)"
        >
          <view class="msg-location-first-view">
            <img src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Flocation-icon.png" class="msg-location-icon" />
            <text class="msg-location-icon-text">拼叭｜地理位置</text>
          </view>
          <view class="msg-location-address">
            {{getAddress(msg.payload.data)}}
          </view>
          <img v-bind:src="getCoordImg(msg.payload.data)" class="msg-location-map" />
        </view>
        <image
          v-if="msg.status === 'unSend'"
          src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/msg-sending.png"
          class="msg-icon loading"
        >
        </image>
        <image
          v-else-if="msg.status === 'fail'"
          src="/static/images/msg-error.png"
          class="msg-icon"
        ></image>
      </view>
    </template>
  </view>
</template>

<script>
import IMMixin from "../lib/mixin.js";
import { decodeElement } from "../lib/decodeElement.js";
import basicService from '../../../service/basic.js';

export default {
  mixins: [IMMixin],
  data() {
    return {
      music: null,
    };
  },
  props: {
    msg: Object,
    index: Number,
    play: Boolean,
  },
  onLoad(){
  },
  methods: {
    // 处理头像
    getAvatar(avatar) {
      if (avatar && avatar.startsWith("https://image-1306191496")) {
        return (avatar += "?imageView2/1/w/200/h/200/q/80/webp");
      } else {
        return avatar;
      }
    },
    decodeElement,
    showOrigin() {
      uni.previewImage({
        urls: [this.msg.payload.imageInfoArray.find((v) => v.type === 1).url],
      });
    },
    goUserProfile() {
      //  (this.isMe);
      // if (this.isMe) return
      uni.navigateTo({
        url: "/pagesA/user/profile?id=" + this.msg.from,
      });
    },
    //播放语音
    playVoice(index,filePath) {
      this.$emit("playSound", index,filePath);
    },
    //获取语音条长度
    getSoundWidth(second) {
      let max = 242;
      let soundWidth = 88;
      switch (second) {
        case 1:
          return soundWidth;
        case second >= 60:
          return max;
        default:
          let avg = (max - soundWidth) / 60;
          return soundWidth + (second - 1) * avg;
      }
    },
    //获取语音喇叭左移距离
    getSoundWifiLeft(second) {
      //获取语音条长度
      let width = this.getSoundWidth(second);
      return "3px 0 0 " + (width - 88 + 13) + "px";
    },
    openLocation(data) {
      let obj = JSON.parse(data);
      // 打开地图显示位置
      uni.openLocation({
        latitude: Number(obj.lat),
        longitude: Number(obj.lon),
        name: obj.desc,
      });
    },
    getAddress(data){
      let obj = JSON.parse(data);
      let address = obj.desc;
      if (obj.desc.length > 12){
        address = address.substr(0,10) + "...";
      }
      return address;
    },
    getCoordImg(data){
      let obj = JSON.parse(data);
      let url;
      if (obj.staticUrl) {
        url = obj.staticUrl;
      } else {
        url = "https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Flocation-map.png";
      }
      return String(url);
    }
  },
  computed: {
    isMe() {
      // 是否我自己发的消息
      return this.msg.from === this.imUserInfo.userID;
    },
    msgInfo() {
      // 缩略图信息
      if (this.msg.type !== this.$constants.MSG_TYPES.MSG_IMAGE) return;
      let image = this.msg.payload.imageInfoArray.find((v) => v.type === 1);
      if (image.width > 236){
        image.height = (image.height * 236) / image.width;
      }
      return image;
    },
  },
};
</script>

<style lang="scss" scoped>
.chat-message {
  display: flex;
  align-items: flex-start;
  margin: 40upx 0;

  .msg-content {
    display: flex;
    align-items: center;

    &.me {
      flex-direction: row-reverse;
    }
  }

  .msg-text {
    margin: 6upx 20upx;
    max-width: 410upx;
    padding: 28upx 30upx;
    font-size: 30upx;
    font-weight: 400;
    color: #000;
    line-height: 42upx;
    background: #ffffff;
    border: 1upx solid #e4e4e4;
    border-radius: 20upx;
    word-wrap: break-word;
    word-break: normal;
  }

  .msg-img {
    margin: 0 22upx;
    width: 472upx;
    border-radius: 16upx;
  }

  .msg-sound {
    margin: 6upx 20upx;
    height: 100upx;
    border-radius: 20upx;
    background: linear-gradient(90deg, #626262 0%, #333333 100%);
    color: #ffffff;
    display: flex;
    align-items: center;
  }
  .msg-sound-me {
    margin: 6upx 20upx;
    height: 100upx;
    border-radius: 20upx;
    background: #ffffff;
    color: #000000;
    display: flex;
    align-items: center;
  }
  .msg-sound-text {
    width: 52upx;
    height: 42upx;
    font-size: 30upx;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    line-height: 42upx;
    margin-left: 34upx;
  }

  .msg-icon {
    width: 46upx;
    height: 46upx;

    &.loading {
      animation: loading 1s linear infinite;
    }

    @keyframes loading {
      0% {
        -webkit-transform: rotate(0deg);
      }

      25% {
        -webkit-transform: rotate(90deg);
      }

      50% {
        -webkit-transform: rotate(180deg);
      }

      75% {
        -webkit-transform: rotate(270deg);
      }

      100% {
        -webkit-transform: rotate(360deg);
      }
    }
  }

  &.me {
    flex-direction: row-reverse;
  }

  .msg-system {
    width: 100%;
    color: #999;
    text-align: center;
  }

  .sys-msg {
    width: 100%;
    background: #fff;
    padding: 36upx 32upx;
    border: 1px solid #e4e4e4;
    font-weight: 400;
    color: #333333;
    line-height: 42upx;
    border-radius: 20upx;

    .sys-tip {
      font-size: 30upx;
      color: #999999;
      margin-bottom: 24upx;
    }
  }

  .msg-location{
    width: 400upx;
    height: 420upx;
    background: #ffffff;
    margin: 6upx 20upx;

    .msg-location-first-view {
      margin: 20upx 30upx;
      display: flex;

      .msg-location-icon {
        width: 38upx;
        height: 38upx;
      }
      .msg-location-icon-text {
        height: 34upx;
        font-size: 24upx;
        font-weight: 400;
        color: #999999;
        line-height: 34upx;
        margin: 2rpx 12rpx;
      }
    }

    .msg-location-address{
      margin: 20upx 0 0 30upx;
    }
    .msg-location-map{
      margin: 10rpx 18.5rpx;
      width: 365rpx;
      height: 280rpx;
      border-radius: 10rpx;
    }
  }
}
.wifi-icon{
  width: 52upx;
  height: 52upx;
}
.wifi {
  position: absolute;
  right: 185upx;
  padding-top: 6rpx;
  perspective: 1000;
  .wifi-symbol {
    width: 50px;
    height: 50px;
    box-sizing: border-box;
    overflow: hidden;
    transform: rotate(135deg);
    //transform: translate3d(0, 0, 0);
  }
  .wifi-circle {
    border: 3px solid #999999;
    border-radius: 50%;
    position: absolute;
  }

  .wifi-circle-me {
    border: 3px solid #000000;
    border-radius: 50%;
    position: absolute;
  }

  .first {
    width: 5px;
    height: 5px;
    background: #cccccc;
    top: 45px;
    left: 45px;
  }

  .first-me {
    width: 5px;
    height: 5px;
    background: #000000;
    top: 45px;
    left: 45px;
  }

  .second {
    width: 25px;
    height: 25px;
    top: 35px;
    left: 35px;
  }
  .second-hover {
    width: 25px;
    height: 25px;
    top: 35px;
    left: 35px;
    animation: fadeInOut 1s infinite 0.2s;
  }
  .third {
    width: 40px;
    height: 40px;
    top: 25px;
    left: 25px;
  }
  .third-hover {
    width: 40px;
    height: 40px;
    top: 25px;
    left: 25px;
    animation: fadeInOut 1s infinite 0.4s;
  }

  @keyframes fadeInOut {
    0% {
      opacity: 0; /*初始状态 透明度为0*/
    }
    100% {
      opacity: 1; /*结尾状态 透明度为1*/
    }
  }
}

.bg {
  background:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGAAAAAYCAYAAAAF6fiUAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NzlFRDZDRDNENzlFMTFFNkJDN0NFMjA2QTFFRTRDQkIiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NzlFRDZDRDJENzlFMTFFNkJDN0NFMjA2QTFFRTRDQkIiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6MTAxQkEzQ0RENzM2MTFFNjgyMEI5MTNDRkQ0OTM5QUEiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MTAxQkEzQ0VENzM2MTFFNjgyMEI5MTNDRkQ0OTM5QUEiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4K4iKVAAACUUlEQVR42uSazytEURTHvTHjR4kaU8xsSDZSdmbjx4oSK8XGQrJlpSwYTSmxEWWhUIpsZK3kD7DRNBuSBZFCNjZ+JPKcV6ecXu/d3sy7595bc+vbfXPue5/749z77o83lm3bZYYFC8RZqAbQAigP2tXNj5aZF7gdkAZNk9+7WvnOCCgxRUCb9n/o1sk3pUH6QDHF/GNsoM+QeYfiy6qkFeLZDBb0GlTB4AAR/xXT9nXxZVa0WCekQd9Y0HOJjg3CHySviiZmfjO3AyIhnu0gBc0wjAIR/wLtW8z87aAOWAI9gqaYRoAff4ZUoi7EKCiUP462j4CdSCrfK4N1Ahpi6I0i/hPa50M4oFB+Dbm/SzXfL5MD4rUogxP8+Itozynm59E+q5ovyuQdHxphWh568XvR5kxq1SEn40L4e0XMA1L4EcEe7RTjLqYdqRf/gezQUwr5LxjXq+aLHPCFcTmTA7z4tutIQhXfLiJPKXyRA/oxzgW8v9DgxU+S62eF/ATGr6r5fg26Corj9RHD4Z0fvwfjS9CbQn4bxrfK+R6TyzxZNk260solTL4i/g3al10TsMXIryA72T7VfK8MnJO8X9CKy14lafXjxx8jFUsSeyUzfxhtPwHPoqTy/TJJMJzJiPgNpJdsuNJizPwztB/q4JtwHN2KW3sn3HuMOouR30l6bbsOvgkOyGIBnaPbRldalJl/h2knuvgmOKAWNAFKMUz4Iv4O6Z1xXXxTPxtazHy6khnVyS/Fb8IDpHGyuvmWgX9L4Q4toDnQFWhNN/9PgAEAR4w1ULjdCbEAAAAASUVORK5CYII=) right 0 no-repeat;
  width: 48upx;
  height: 48upx;
  background-size: 400%;
}

.voicePlay {
  animation-name: voicePlay;
  animation-duration: 1s;
  animation-direction: normal;
  animation-iteration-count: infinite;
  animation-timing-function: steps(3);
}

@keyframes voicePlay {
  0% {
    background-position: 0;
  }
  100% {
    background-position: 100%;
  }
}
</style>
