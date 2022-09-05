<template>
  <view class="man-item" :class="{ card }">
    <view
      class="man-item-l"
      :class="[member.sex !== 1 ? 'girl' : 'boy']"
      @click="goUserDetail(member.userId)"
    >
			<image
			  :src="`../../static/images/${member.sex == 1 ? 'man' : 'girl'}-icon.png`"
			></image>
      <view class="leader" v-if="member.identity === 1">局主</view>
      <avatar shape="circle" :avatarUrl="member.head"></avatar>
    </view>
    <view class="man-item-r">
      <view class="flex-s">
        <view class="name">{{ member.nickName || member.userId }} </view>
        <view class="name location" v-if="member.userId === myUserId"
          >你在这里
        </view>
        <!-- 只有群主能发起单聊和打电话  v-if="showContact && member.identity!==1"-->
        <view class="flex-c">
          <!-- 建议后面接入三方电话后再开放此功能，免得暴露 -->
          <image
            class="call-icon"
            v-if="showContact && member.identity !== 1"
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/call-icon.png"
            @click="goCall(member.userId)"
          />
          <image
            class="talk-icon"
            v-if="member.userId !== myUserId"
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/talk-icon.png"
            @click="goChat(member.userId)"
          />
        </view>
      </view>
      <view class="flex-c" style="margin-top: 10upx">
        <view class="man-t">长沙市</view>
        <view class="man-t" v-if="member.age && member.userId != -1">{{ member.age + "岁" }}</view>
        <view class="man-t">{{ member.sex === 1 ? "男" : "女" }}</view>
<!--        <view class="man-t" v-if="member.constellation !== 'undefined'">{{
          member.constellation
        }}</view> -->
      </view>
      <view class="flex-s" style="margin-top: 14upx">
        <view class="flex-c interest-line" v-if="member.interests.length">
          兴趣:
          <view
            class="interest"
            v-for="(item, index) in member.interests"
            :key="index"
            >{{ item }}</view
          >
        </view>
        <view  v-else class="flex-c interest-line">
           兴趣:
           <view
            class="interest"
            >{{ detail.typeName }}</view
          >
        </view>
        <!-- <view class="space">500m</view> -->
      </view>

      <view class="buttons" v-if="actions && member.identity !== 1">
        <view class="flex-box">
          <!-- 报名中的按钮 -->
          <template v-if="member.state === 2">
            <button class="btn" @click="$emit('audit', 4, member)">拒绝</button>
            <button class="btn active" @click="$emit('audit', 1, member)">
              通过
            </button>
          </template>
          <!-- 已报名的按钮 -->
          <template v-if="member.state === 1">
            <button class="btn active" @click="$emit('kick', member)">
              踢出
            </button>
          </template>
        </view>
        <view class="state-name" v-if="stateName">{{ stateName }}</view>
      </view>
    </view>
  </view>
</template>
    

<script>
import IM from "../../pages/im/lib/index.js";
import gameService from "../../service/game";
export default {
  props: {
    member: Object,
    showContact: Boolean,
    actions: Boolean,
    card: Boolean,
    detail:Object,
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
      return namesMap[this.member.state] || "";
    },
  },
  created() {},

  methods: {
    goUserDetail(id) {
      this.$eventRecord(200)
      // 去用户主页
			if(id == -1){
				this.$toast('该玩家为同行线下玩家!');
				return
			}
      uni.navigateTo({
        url: "/pagesA/user/profile?id=" + this.member.userId,
      });
    },
    goChat(id) {
			if(id == -1){
				this.$toast('该玩家为同行线下玩家!');
				return
			}
      // 打开跟用户的聊天
      IM.chatWithUser(this.member.userId);
      // 埋点
      this.$eventRecord(201);
    },
    goCall() {
      let { userId } = this.member;
			if(userId == -1) return this.$toast('该玩家为同行线下玩家')
      gameService.getPhoneNumber({ userId }).then((res) => {
        // 打电话给用户
        uni.makePhoneCall({
          phoneNumber: res,
        });
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
    width: 160upx;
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
	.man-item-l {
	  flex: none;
	  position: relative;
	  height: 80upx;
	  border-radius: 50%;
	  margin-right: 20upx;
	  width: 80upx;
	
	  image {
	    width: 26upx;
	    height: 26upx;
	    position: absolute;
	    top: 30px;
	    left: 12px;
	  }
	}
	.boy {
	  border: 4upx solid #0076ff;
	}
	
	.girl {
	  border: 4upx solid #d765d4;
	}
  &.card {
    // 卡片模式
    width: 666upx;
    margin: 42upx 42upx 0;
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
      text {
        background-color: #ccc;
        margin-left: 20upx;
        border-radiou: 10px;
        border-radius: 6px;
        font-size: 24upx;
        color: #000;
      }
    }
    .location {
      background-color: #000;
      color: #fff;
      width: 135upx;
      height: 50upx;
      text-align: center;
      line-height: 50upx;
      border-radius: 40upx;
      font-size: 25upx;
    }

    .call-icon {
      height: 40upx;
      width: 40upx;
      margin-right: 66upx;
    }

    .talk-icon {
      height: 40upx;
      width: 40upx;
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
      padding: 0 16upx;
      font-size: 26upx;
      line-height: 36upx;
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
