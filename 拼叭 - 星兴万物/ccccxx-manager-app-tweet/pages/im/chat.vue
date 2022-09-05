<template>
  <view>
    <!-- 说话弹出层 -->
    <view v-if="isSpeak" class="speak-all" :catchtouchmove="true"></view>
    <view v-if="isSpeak" class="speak-time">
      <view class="speak-time-text">
<!--        <text>{{minStr}}</text>-->
<!--        <text>:</text>-->
        <text>{{secStr}}</text>
        <text>:</text>
        <text>{{msStr}}</text>
      </view>
    </view>
    <img v-if="isSpeak" src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fspeak-send.png" class="view-speak" id="noSend"/>
    <view v-if="isSpeak" class="view-speak_children">
      <image
          src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fspeak-no-send.png"
          class="img-speak"
      ></image>
      <text lines="1" class="text-speak">松开手指，取消发送</text>
    </view>
    <view class="im-chat" >
      <bureauDetails
          v-if="gameStartTime !== 'undefined' && gameStartTime"
          :groupData="groupData"
          :gameStartTime="gameStartTime"
      ></bureauDetails>
      <view class="message-list" :style="{marginBottom: showEmoji || showConsole ? '265px': (focus && env.indexOf('ios') === -1 ? pageMarginBottom + 'px':'')}" @click="hideKeyAndEmoji">
        <view v-for="(message, i) in messageList" >
          <view v-if="isShowMessageTime(message,i)" class="message-time">{{getMessageTime(message)}}</view>
          <chat-message
              :msg="message"
              :key="i"
              :index="i"
              :play="isPlay(i)"
              v-if="getIsShow(message)"
              @playSound="playSound"
          ></chat-message>
        </view>
      </view>
      <!-- 系统通知界面不能发消息 -->
        <view class="footer" :catchtouchmove="true" :class="showEmoji || showConsole ? 'footer-bottom':''" :style="{bottom: focus ? pageMarginBottom + 'px' : ''}" v-if="id !== 'system'">
        <image
            v-if="messageType === 1"
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/icon-voice.png"
            class="btn-voice"
            @click="sendVoice"
        ></image>
        <image
            v-if="messageType === 2"
            src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fkeyboard.png"
            class="btn-voice"
            @click="sendVoice"
        ></image>
        <textarea
            v-if="messageType === 1"
            v-model="message"
            class="input"
            @input="getCursor"
            :adjust-position=adjustPosition
            :fixed="true"
            :show-confirm-bar="false"
            :focus="focus"
            @focus="bindTextAreaFocus"
            @keyboardheightchange="updateKeyboard"
            @blur="bindBlur"
            :cursor="cursor"
            :cursor-spacing="10"
            confirm-type="send"
            @confirm="sendText"
        />
        <button
            v-else-if="messageType === 2"
            class="btn-sendVoice"
            @touchstart="handleTouchStart"
            @touchend="handleTouchEnd"
            @longpress="handleLongPress"
            @touchmove="handleTouchMove"
            @touchcancel="handleTouchCancel"
        >
          按住说话
        </button>
        <img v-if="!showEmoji" class="emoji-icon" src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fsmile%402x.png" @click="updateShowEmoji"/>
        <img v-else class="emoji-icon" src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fkeyboard.png" @click="keyboard"/>
        <button
            v-if="messageType === 1 && message"
            @click="sendText"
            class="btn-send"
        >
          发送
        </button>
        <button v-if="!message" plain @click="openConsole">
          <image
              src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/icon-add.png"
              class="btn-add"
          ></image>
        </button>
<!--        <button v-if="!message" plain @click="uploadImage">-->
<!--          <image-->
<!--              src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/icon-add.png"-->
<!--              class="btn-add"-->
<!--          ></image>-->
<!--        </button>-->
      </view>
      <view v-if="showConsole" class="console-view">
        <view class="console-view-img-view first-img-margin" @click="uploadImage">
          <img src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fimg-icon.png" class="console-view-img-icon" />
          <view class="console-view-img-text">图片</view>
        </view>
        <view class="console-view-img-view img-margin" @click="cameraImage">
          <img src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fphoto-icon.png" class="console-view-img-icon" />
          <view class="console-view-img-text">拍照</view>
        </view>
        <view class="console-view-img-view img-margin" @click="selectLocation">
          <img src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Fweizhi-icon.png" class="console-view-img-icon" />
          <view class="console-view-img-text">位置</view>
        </view>
      </view>
      <view v-if="showEmoji" class="emoji-view" :catchtouchmove="true">
        <scroll-view class="emoji-scroll-view" scroll-y="true" scroll-top="1" scroll-with-animation=true>
            <text v-for="(emoji, index) in emojiData" :key="index" @click="selemoji(emoji)" class="slider-emoji-icon">{{ emoji }}</text>
        </scroll-view>
        <img v-if="showEmoji" src="https://static-1306191496.cos.ap-nanjing.myqcloud.com/static%2Femoji_del.png" class="emoji-del" @click="delMsg">
      </view>
    </view>
  </view>
</template>

<script>
import TIM from "tim-wx-sdk";
import IM from "./lib";
import IMMixin from "./lib/mixin.js";
import ChatMessage from "./components/chat-message.vue";
import bureauDetails from "./components/bureauDetails.vue";
import loginService from "../../service/login.js";
import imService from "../../service/im";
import emoji from "../../pagesA/im/component/emoji.js";
import basicService from '../../service/basic.js';
import { utils } from "../../framework";

const recorderManager = uni.getRecorderManager();
let innerAudioContext = uni.createInnerAudioContext();
uni.setInnerAudioOption({
  obeyMuteSwitch: false
});
export default {
  components: {
    ChatMessage,
    bureauDetails,
  },
  mixins: [IMMixin],
  data() {
    return {
      chatType: "", // 聊天类型，c2c单聊 group群聊 system系统消息
      message: "",
      nextReqMessageID: "", // 用于续拉，分页续拉时需传入该字段
      isCompleted: false, // 表示是否已经拉完所有消息
      hisIsCompleted: false,//表示以及拉完所有历史消息
      messageList: [], // 消息列表
      groupData: {},
      gameStartTime: "",
      id: "",
      messageType: 1, //1-文本消息，2-语音消息
      startTime: null,
      endTime: null,
      isSpeak: false,
      timer:null,//定时器
      min:0,
      minStr:'00',
      sec:0,
      secStr:'00',
      ms:0,
      msStr:'00',
      x: null,
      y: null,
      nodeY: null,
      scrollTop: 0, //屏幕滑动值
      play: [],
      userMap: new Map(),
      downNum: 0,
      emojiData: [],
      showEmoji: false,
      focus: false,
      cursor: 0,
      env: "",
      pageMarginBottom: 0,
      showConsole: false,
      adjustPosition: false,
      lastIndex: null,
    };
  },
  onUnload(){
    innerAudioContext.destroy();
  },
  onHide(){
	this.focus = false;
    innerAudioContext.destroy();
  },
  async onLoad(o) {
    let env = uni.getSystemInfoSync();
    this.env = env.system.toLowerCase();
    this.chatType = o.chatType;
    this.id = o.type;
    this.gameStartTime = o.gameId || "";
    this.tim = await IM.init();
    this.tim.on(TIM.EVENT.MESSAGE_RECEIVED, this.onMessageReceived);
    this.getMessageList();
    if (o.chatType === "group") {
      this.getGroupMsg();
    }
    const pages = getCurrentPages()
    const url = pages[pages.length - 1].$page.fullPath
    const trackingID = this.$getParam(url, 'trackingID')
    if(trackingID) this.$eventRecord(trackingID)
    innerAudioContext = uni.createInnerAudioContext();
    uni.setInnerAudioOption({
      obeyMuteSwitch: false
    });
    let that_ = this;
    recorderManager.onStart(() => {
      console.log("recorder start");
    });
    recorderManager.onError((res) => {
      this.cancelSpeak();
      that_.isSpeak = false;
      clearInterval(this.timer);
      that_.timer = null;
      console.log("录音错误");
      console.log(res);
    });
    recorderManager.onPause(() =>{
      that_.isSpeak = false;
      clearInterval(that_.timer);
      that_.timer = null;
    });
    recorderManager.onInterruptionBegin(() =>{
      that_.isSpeak = false;
      clearInterval(that_.timer);
      that_.timer = null;
    });
    recorderManager.onInterruptionEnd(() =>{
      that_.isSpeak = false;
      clearInterval(that_.timer);
      that_.timer = null;
    });
    recorderManager.onStop((res) => {
      let endTime = new Date().getTime();
      that_.isSpeak = false;
      clearInterval(that_.timer);
      that_.timer = null;
      console.log(
          "坐标：" + (that_.y - that_.scrollTop) + "，当前坐标：" + that_.nodeY
      );
      console.log(endTime);
      console.log(that_.startTime)
      if (endTime - that_.startTime < 1000) {
        that_.$toast("说话时间太短");
      }else {
        if (that_.y - that_.scrollTop <= this.nodeY) {
          console.log("取消说话");
        } else {
          //创建消息对象
          const message = that_.tim.createAudioMessage({
            to: that_.sendTo,
            conversationType: that_.currentConversation.type,
            payload: {
              file: res,
            },
            onProgress: function (progress) {
              console.log(progress);
            },
          });
          //发送消息
          that_.sendMessage(message);
          console.log("说话结束");
        }
      }
    });
    innerAudioContext.onStop(() =>{
      console.log("停止播放")
      this.play = [];
    });
    innerAudioContext.onPlay(() => {
      console.log("开始播放");
    });
    innerAudioContext.onError((res) => {
      this.play = [];
      console.log("错误码:" + res.errCode + ",错误信息:" + res.errMsg);
    });
    innerAudioContext.onEnded(() => {
      console.log("播放结束");
      this.play = [];
    });
    emoji.forEach(i => this.emojiData.push(i));
  },
  destroyed() {
    this.tim.off(TIM.EVENT.MESSAGE_RECEIVED, this.onMessageReceived);
    this.$store.commit("resetCurrentConversation");
  },
  async mounted() {
    let name = await this.getTitleConversationName(this.currentConversation);
    uni.setNavigationBarTitle({
      title: name,
    });
  },
  onPullDownRefresh() {
    this.downNum += 1;
    if (this.isCompleted && this.hisIsCompleted) {
      uni.stopPullDownRefresh();
      this.$toast("没有更多消息了");
    } else {
      this.getMessageList(true).then(() => {
        uni.stopPullDownRefresh();
      });
    }
  },
  onShow() {
    // 埋点
    this.$eventRecord(77);
  },
  watch:{
    sec:function (e){
      //当秒数等于50秒时提示用户录音即将结束
      if (e === 50){
        uni.showToast({
          title: "最大录音时长60s，录音即将结束",
          icon: "none",
        });
      }
    }
  },
  methods: {
    cancelSpeak(){
      console.log("调用我取消了");
      this.isSpeak = false;
      clearInterval(this.timer);
      this.timer = null;
    },
    isPlay(index){
      return this.play.includes(index);
    },
    getIsShow(message){
      if (this.chatType === 'c2c' && message.conversationType === 'C2C'){
        return true;
      }else if (this.chatType === 'group' && message.conversationType === 'GROUP'){
        return true;
      }else if (this.chatType === 'system' && message.conversationType === '@TIM#SYSTEM'){
        return true;
      }
      return false;
    },
    async getGroupMsg() {
      const res = await imService.getGroupData(this.id);
      this.groupData = res;
    },
    sendVoice() {
      if (this.messageType === 1) {
        let that_ = this;
        uni.getSetting({
          success(res) {
            //判断是否第一次获取录音功能
            if (!res.authSetting['scope.record']) {
              //调用后会立刻弹窗询问用户是否同意授权录音给小程序
              uni.authorize({
                scope: 'scope.record',
                success() {
                  // 用户已经同意小程序使用录音功能，后续调用 wx.startRecord 接口不会弹窗询问
                  uni.startRecord()
                }
              });
              //判断录音是否是开启状态false没开启就跳转到开启页面
              if (res.authSetting['scope.record'] === false) {
                uni.showModal({
                  title: "是否开启录音权限",
                  content: "请确认授权，否则无法发送语音消息",
                  success: function (tip) {
                    if (tip.confirm) {
                      uni.openSetting();
                    }
                  },
                });
              }
            } else if (res.authSetting['scope.record'] === true) {
              that_.showConsole = false;
              that_.showEmoji = false;
              that_.focus = false;
              that_.messageType = 2;
            }
          }
        });
      } else {
        this.messageType = 1;
      }
    },
    onMessageReceived(event) {
      // 收到推送的单聊、群聊、群提示、群系统通知的新消息，可通过遍历 event.data 获取消息列表数据并渲染到页面
      // event.name - TIM.EVENT.MESSAGE_RECEIVED
      // event.data - 存储 Message 对象的数组 - [Message]
      this.messageList = this.messageList.concat(event.data);
      this.play = [];
      this.messageList.forEach((e) => {
        this.play.push(true);
      });
      this._scrollBottom();
      this.tim.setMessageRead({
        conversationID: this.currentConversation.conversationID,
      });
    },
    uploadImage() {
      uni.chooseImage({
        count: 9,
        sizeType: ["original", "compressed"], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ["album"], // 从相册选择
        success: (res) => {
          this.sendMessage(
              this.tim.createImageMessage({
                to: this.sendTo,
                conversationType: this.currentConversation.type,
                payload: {
                  file: res,
                },
                onProgress: function (event) {
                  "file uploading:", event;
                },
              })
          );
        },
      });
    },
    getMessageList: function (pulldown) {
      // 拉取消息列表
      return this.tim
          .getMessageList({
            conversationID: this.currentConversation.conversationID,
            nextReqMessageID: this.nextReqMessageID,
            count: 15,
          })
          .then(async (imResponse) => {
            this.play = [];
            this.messageList = imResponse.data.messageList.concat(
                this.messageList
            );
            for(let message of this.messageList) {
              this.play.push(true);
              if (
                  message.type !== "TIMGroupTipElem" &&
                  message.type !== "TIMGroupSystemNoticeElem"
              ) {
                if (message.from !== '100020938128') {
                  let user = this.userMap.get(message.from);
                  if (user) {
                    message.nick = user.nickName;
                    message.avatar = user.head;
                  } else {
                    let user = await loginService.getUserInfoById(message.from)
                    this.userMap.set(message.from, user);
                    message.nick = user.nickName;
                    message.avatar = user.head;
                  }
                }
              }else if (message.type === "TIMGroupTipElem" && !message.nick){
                for (let element of message.elements){
                  for (let userId of element.content.userIDList){
                    let user = this.userMap.get(userId);
                    if (user){
                      message.nick = user.nickName;
                    }else {
                      let user = await loginService.getUserInfoById(userId);
                      this.userMap.set(userId,user);
                      message.nick = user.nickName;
                    }
                  }
                }
              }
            }

            this.nextReqMessageID = imResponse.data.nextReqMessageID;
            this.isCompleted = imResponse.data.isCompleted;
            if (imResponse.data.isCompleted && (this.downNum > 0 || this.messageList.length === 0)){
              let hisList = await this.queryUserMessageHis();
              if (!hisList || hisList.length === 0){
                this.hisIsCompleted = true;
              }else{
                //一次拉取15条消息，小于15则代表没有历史消息了
                if (hisList.length < 15){
                  this.hisIsCompleted = true;
                }
                this.messageList = hisList.concat(
                    this.messageList
                );
              }
            }
            this.lastIndex = this.messageList.length - 1;
            !pulldown && this._scrollBottom(); // 下拉加载信息的时候，不用滚到底部
          });
    },
    sendText() {
      if (!this.message) return this.$toast("请输入内容");
      this.sendMessage(
          this.tim.createTextMessage({
            to: this.sendTo,
            conversationType: this.currentConversation.type,
            payload: {
              text: this.message,
            },
          })
      );
      this.message = "";
    },
    sendMessage(msg) {
      let index = this.messageList.length; // 记录消息位置
      this.messageList.push(msg);
      this._scrollBottom();
      this.tim
          .sendMessage(msg)
          .then(() => {
            this.$set(this.messageList[index], "status", "success");
          })
          .catch((err) => {
            this.$toast(err);
            this.$set(this.messageList[index], "status", "fail");
          });
    },
    _scrollBottom() {
      // 滑动到页面底部
      setTimeout(() => {
        uni.pageScrollTo({
          scrollTop: 99999,
        });
      }, 100);
    },
    //touch start
    handleTouchStart: function (e) {
      let that_ = this;
      that_.startTime = new Date().getTime();
      that_.isSpeak = true;
      that_.x = e.touches[0].pageX;
      that_.y = e.touches[0].pageY;
      //("开始说话");
      // 录音部分参数
      const options = {
        duration: 60000, // 录音的时长，单位 ms，最大值 600000（10 分钟）
        sampleRate: 44100, // 采样率
        numberOfChannels: 1, // 录音通道数
        encodeBitRate: 192000, // 编码码率
        format: "aac", // 音频格式，选择此格式创建的音频消息，可以在即时通信 IM 全平台（Android、iOS、微信小程序和Web）互通
      };
      //开始录音
      recorderManager.start(options);
      that_.startTimer()
    },
    onPageScroll: function (e) {
      this.scrollTop = e.scrollTop;
    },
    handleTouchCancel() {
      this.isSpeak = false;
      clearInterval(this.timer);
      this.timer = null;
      let that_ = this;
      //停止录音
      recorderManager.stop();
      recorderManager.onStop((res) => {
        that_.isSpeak = false;
        clearInterval(that_.timer);
        that_.timer = null;
        console.log(
            "坐标：" + (that_.y - that_.scrollTop) + "，当前坐标：" + that_.nodeY
        );
        if (that_.y - that_.scrollTop <= this.nodeY) {
          console.log("取消说话");
        } else {
          //创建消息对象
          const message = that_.tim.createAudioMessage({
            to: this.sendTo,
            conversationType: that_.currentConversation.type,
            payload: {
              file: res,
            },
            onProgress: function (progress) {
              console.log(progress);
            },
          });
          //发送消息
          that_.sendMessage(message);
          console.log("说话结束");
        }
      });
    },
    handleTouchEnd: function (e) {
      this.endTime = e.timeStamp;
      this.isSpeak = false;
      clearInterval(this.timer);
      this.timer = null;
      this.min = 0
      this.minStr = '00';
      this.sec = 0;
      this.secStr = '00'
      this.ms = 0;
      this.msStr = '00';
      let that_ = this;
      //停止录音
      recorderManager.stop();
    },
    //设置坐标
    ready() {
      const query = uni.createSelectorQuery().in(this);
      query
          .select("#noSend")
          .boundingClientRect((rect) => {
            this.nodeY = rect.bottom;
          })
          .exec();
    },
    handleLongPress() {
      //设置元素坐标
      this.ready();
      //("长按");
      uni.showToast({
        title: "按住录音，松开发送",
        icon: "none",
      });
    },
    handleTouchMove: function (e) {
      this.x = e.touches[0].pageX;
      this.y = e.touches[0].pageY;
    },
    queryUserMessageHis(){
      let isGroup = this.currentConversation.type === 'GROUP';
      let first = this.messageList[0];
      return imService.queryUserMessageHis(isGroup ? 2:1,isGroup ? this.currentConversation.groupProfile.groupID : null
          ,first? first.sequence : null,isGroup ? null : this.currentConversation.userProfile.userID);
    },
    async playSound(index,filePath){
      console.log(filePath)
      innerAudioContext.offCanplay();
      if (innerAudioContext.src !== filePath) {
        this.play = []
      }
      if (innerAudioContext.paused) {
        this.play.push(index);
        if (innerAudioContext.src === filePath){
          innerAudioContext.play();
        }else {
          innerAudioContext.src = filePath;
          innerAudioContext.onCanplay(() => {
            innerAudioContext.play();
          });
        }
      } else {
        innerAudioContext.stop();
        if (innerAudioContext.src !== filePath) {
          this.play.push(index);
          innerAudioContext.src = filePath;
          innerAudioContext.onCanplay(() => {
            innerAudioContext.play();
          });
        }
      }
    },
    updateShowEmoji(){
      this.showConsole = false;
      this.messageType = 1;
      this.showEmoji = true;
      this._scrollBottom();
    },
    selemoji(m) {
      this.message = this.message.slice(0, this.cursor) + m + this.message.slice(this.cursor);
      //下标后移两个字符
      this.cursor += 2;
    },
    getCursor: function (e){
      this.cursor = e.detail.cursor;
    },
    delMsg(){
      console.log(this.message)
      let length = this.message.length;
      if (length > 0){
        if (this.cursor > 0){
          let startStr = this.message.slice(0, this.cursor);
          let endStr = this.message.slice(this.cursor);
          let emojiStr = startStr.substr(startStr.length - 2, startStr.length);
          if (emoji.includes(emojiStr)) {
            startStr = startStr.substr(0, startStr.length - 2);
            this.cursor -= 2;
          }else{
            this.cursor--;
            startStr = startStr.substr(0, startStr.length - 1);
          }
          this.message = startStr + endStr;
        }else {
          let emojiStr = this.message.substr(length - 2, length);
          if (emoji.includes(emojiStr)) {
            this.message = this.message.substr(0, length - 2);
          } else {
            this.message = this.message.substr(0, length - 1);
          }
        }
        //校验下标
        this.cursor < 0 ? this.cursor = 0 : null;
      }
    },
    async keyboard(){
      this.showConsole = false;
      this.showEmoji = false;
      this.focus = true;
      this._scrollBottom();
    },
    bindTextAreaFocus(e) {
      this.focus = true;
      this.showConsole = false;
      this.showEmoji = false;
      if (e.detail.height === 0){
        return this.textAreaOnclick();
      }
      this.pageMarginBottom = e.detail.height - 1;
      this._scrollBottom();
    },
    async textAreaOnclick(){
      this.showEmoji = false;
      if (this.showEmoji){
          this.focus = true;
      }
      this._scrollBottom();
    },
    updateKeyboard(e){
      console.log("键盘高度发生变化")
      console.log(e);
      this.pageMarginBottom = e.detail.height - 1;
    },
    bindBlur(e){
      this.focus = false;
      this.cursor = e.detail.cursor;
      this.pageMarginBottom = 0;
      this._scrollBottom();
    },
    hideKeyAndEmoji(){
      this.focus = false;
      this.showEmoji = false;
      this.showConsole = false;
      this._scrollBottom();
    },
    openConsole(){
      this.showEmoji = false;
      if (this.showConsole){
        this.showConsole = false;
      }else {
        this.messageType = 1;
        this.showConsole = true;
      }
      this._scrollBottom();
    },
    cameraImage(){
      uni.chooseImage({
        count: 9,
        sizeType: ["original"], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ["camera"], // 拍摄
        success: (res) => {
          this.sendMessage(
              this.tim.createImageMessage({
                to: this.sendTo,
                conversationType: this.currentConversation.type,
                payload: {
                  file: res,
                },
                onProgress: function (event) {
                  "file uploading:", event;
                },
              })
          );
        },
      });
      //勿删，后续可能录像会用到
      // uni.getSetting({
      //   success: res => {
      //     if (res.authSetting['scope.camera']) {
      //       // 用户已经授权
      //       uni.navigateTo({
      //         url: "/pagesA/im/camera"
      //       });
      //     } else {
      //       // 用户还没有授权，向用户发起授权请求
      //       wx.authorize({
      //         scope: 'scope.camera',
      //         success() { // 用户同意授权
      //           uni.navigateTo({
      //             url: "/pagesA/im/camera"
      //           });
      //         },
      //         fail(e) { // 用户不同意授权
      //           console.log(e)
      //           uni.showModal({
      //             title: "是否开启摄像头权限",
      //             content: "请确认开启权限，否则无法进行拍照",
      //             success: function (tip) {
      //               if (tip.confirm) {
      //                 uni.openSetting();
      //               }
      //             },
      //           });
      //         }
      //       })
      //     }
      //   },
      //   fail(e){
      //     console.log("错误");
      //     console.log(e)
      //   }
      // });
    },
    selectLocation() {
      // 选择位置
      uni.chooseLocation({latitude: this.latitude, longitude: this.longitude,})
          .then(async (res) => {
            if (res[1] && res[1].address.includes("湖南省长沙市")) {
              if (!res[1]) {
                uni.getSetting().then((dd) => {
                  let setting = dd[1];
                  if (!setting.authSetting["scope.userLocation"]) {
                    uni.showModal({
                      title: "是否授权定位",
                      content: "请确认授权，否则无法获取当前地址",
                      success: function (tip) {
                        if (tip.confirm) {
                          uni.openSetting();
                        }
                      },
                    });
                  }
                });
                return;
              }
              console.log(res[1]);
              let { address, latitude, longitude, name } = res[1];
              if (latitude && longitude && name){
                let staticUrl = await basicService.getMapStaticImg(latitude,longitude);
                this.sendMessage(
                    this.tim.createCustomMessage({
                      to: this.sendTo,
                      conversationType: this.currentConversation.type,
                      payload: {
                        data: JSON.stringify({"lat": latitude, "lon": longitude, "desc": name, "staticUrl": staticUrl}),
                        //自定义地理位置消息，小程序SDK暂时不支持地理位置消息
                        description: "TIMLocationElem",
                        extension: null
                      },
                    })
                );
              }
            }
          });
    },
    startTimer:function(){
        clearInterval(this.timer);
        this.timer=setInterval(this.show,10);
    },
    show:function(){
      this.ms++;
      // if(this.sec===60){
      //   this.min++;this.sec=0;
      // }
      if(this.ms===100){
        this.sec++;this.ms=0;
      }
      if(this.ms<10){
        this.msStr= "0"+this.ms;
      }else{
        this.msStr= ""+this.ms;
      }
      if(this.sec<10){
        this.secStr="0"+this.sec;
      }else{
        this.secStr=""+this.sec;
      }
      // if(this.min<10){
      //   this.minStr="0"+this.min;
      // }else{
      //   this.minStr=""+this.min;
      // }
    },
    getMessageTime(message){
      let day = Number(utils.dayjs(message.time * 1000).format("DD"));
      let currDay = Number(new Date().getDate());
      if (day === currDay){
        return utils.dayjs(message.time * 1000).format("HH:mm");
      }else if (currDay - 1 === day){
        return "昨天 " + utils.dayjs(message.time * 1000).format("HH:mm");
      }
      else {
        return utils.dayjs(message.time * 1000).format("YYYY年MM月DD日 HH:mm");
      }
    },
    isShowMessageTime(message,i){
      if (i === 0){
        return true
      }
      let index = i - 1;
      let upMessage = this.messageList[index];
      let time = message.time - upMessage.time;
      //每隔三分钟不发消息显示一次
      if (time > 60 * 3){
        return true;
      }
      return false;
    }
  },
  computed: {
    sendTo() {
      if (this.chatType === "c2c") {
        return this.currentConversation.userProfile.userID;
      } else if (this.chatType === "group") {
        return this.currentConversation.groupProfile.groupID;
      }
    },
  },
};
</script>

<style lang="scss" scoped>.im-chat {
  padding: 20upx 42upx calc(132upx + env(safe-area-inset-bottom));
  min-height: 100vh;
  background: #f5f5f5;
}

.message-list {
  width: 100%;
  height: 100%;
  .message-time{
    text-align: center;
    font-size: 24upx;
    font-weight: 400;
    color: #999999;
  }
}

.footer {
  z-index: 2;
  position: fixed;
  border-top: 1upx solid #f5f5f5;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  display: flex;
  height: calc(112upx + env(safe-area-inset-bottom));
  align-items: center;
  padding-bottom: env(safe-area-inset-bottom);

  .btn-voice{
    width: 62upx;
    height: 62upx;
    margin: 0 14upx 0 42upx;
  }

  .btn-add {
    width: 62upx;
    height: 62upx;
    padding-right: 21upx;
  }

  .btn-send {
    width: 120upx;
    height: 60upx;
    background: linear-gradient(270deg, #cd6cff 0%, #ff4f3a 100%);
    border-radius: 38upx;
    margin: 0 20upx 0 10upx;
    font-size: 30upx;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .input {
    flex: 1;
    height: 76upx;
    line-height: 1.5em;
    box-sizing: border-box;
    padding: 16upx 40upx 0 16upx;
    background: #f0f0f0;
    border-radius: 38upx;
    min-width: 400upx;
    display: inline-block;
  }
  .btn-sendVoice {
    z-index: 104;
    width: 470upx;
    height: 76upx;
    background: linear-gradient(90deg, #626262 0%, #333333 100%);
    justify-content: center;
    align-items: center;
    display: flex;
    flex-direction: column;
    border-radius: 38upx;
  }
}
.footer-bottom{
  margin-bottom: 510upx;
}
.view-speak {
  z-index: 100;
  height: 188upx;
  width: 750upx;
  position: fixed;
  bottom: 0;
  margin-bottom: calc(106upx + env(safe-area-inset-bottom));
}
.view-speak_children {
  z-index: 120;
  position: fixed;
  bottom: 0;
  margin-bottom: calc(145upx + env(safe-area-inset-bottom));
  width: 100%;
  text-align: center;
}
.img-speak {
  width: 100upx;
  height: 100upx;
  align-self: center;
}
.text-speak {
  display: block;
  color: rgba(102, 102, 102, 1);
  font-size: 30upx;
  white-space: nowrap;
}
.speak-all{
  position: fixed;
  background: #7777777a;
  height: 100%;
  width: 100%;
  z-index: 1;;
}
.speak-time{
  z-index: 99;
  position: fixed;
  margin: auto;
  left: 0;
  right: 0;
  top: 30%;
  width: 315rpx;
  height: 140rpx;
  background: linear-gradient(90deg, #626262 0%, #333333 100%);
  border-radius: 20rpx;
  font-size: 40upx;
  color: #FFFFFF;
  display: flex;
  align-items: center;

  .speak-time-text{
    padding-left: 100rpx;
  }
}

.slider {
  height: 510upx;
  &-emoji {
    width: 750upx;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content:center;
    &-icon {
      text-align: center;
      padding: 25upx;
      font-size: 64upx;
    }
  }
}
.lastbox{
  justify-content: flex-start;
}
.emoji-icon{
  width: 62upx;
  height: 62upx;
  padding-left: 18upx;
}
.voice-btn{
  width: 62upx;
  height: 62upx;
}
.focus-view{
  width: 100%;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
}
.emoji-view{
  width: 748upx;
  height: 510upx;
  background: #FFFFFF;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
}
.emoji-scroll-view{
  margin: 0 30upx 0 30upx;
}
.row-view{
  width: 100%;
  height: 76upx;
  margin-bottom: 15upx;
}
.emoji-del{
  width: 102upx;
  height: 72upx;
  position: absolute;
  bottom: 110upx;
  right: 45upx;
}
.console-view{
  background: #FFFFFF;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  width: 748upx;
  height: 510upx;
  display: flex;

  .console-view-img-view{
    text-align: center;
    margin-top: 20upx;
  }
  .console-view-img-icon{
    width: 108upx;
    height: 108upx;
  }
  .first-img-margin{
    margin-left: 58upx;
  }
  .img-margin{
    margin-left: 68upx;
  }
  .console-view-img-text{
    width: 64upx;
    height: 42upx;
    font-size: 30upx;
    font-weight: 400;
    color: #333333;
    line-height: 42upx;
    margin-left: 23upx;
  }
}
</style>
