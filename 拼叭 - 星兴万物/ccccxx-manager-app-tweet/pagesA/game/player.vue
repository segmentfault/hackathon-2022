<template>
  <view class="page-bg">
    <comHead color="#333333" autoPadding>
      <view class="flex-c bar-bg">
        <view
          class="bar-item"
          @click="tabIndex = idx"
          :key="idx"
          v-for="(item, idx) in tabs"
        >
          {{ item }}
          <image
            v-if="tabIndex == idx"
            class="bar-line"
            src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/bar-line.png"
          />
        </view>
      </view>
    </comHead>

    <view class="num-tip"
      >已通过 {{ members.filter((v) => v.state === 1).length }} 人，期待
      {{ expect }} 人</view
    >

    <u-swipe-action
      :show="item.show"
      :index="index"
      v-for="(item, index) in showingList"
      :key="item.id"
      @click="click"
      @open="open"
      :options="options"
    >
      <game-player-item
        :member="item"
        showContact
        card
        actions
        @audit="audit"
        @kick="kick"
      >
      </game-player-item>
    </u-swipe-action>
  </view>
</template>

<script>
import comHead from "../../components/com-head.vue";
import GamePlayerItem from "../components/game-player-item.vue";
import GameService from "../../service/game.js";
export default {
  components: {
    comHead,
    GamePlayerItem,
  },
  data() {
    return {
      tabs: ["报名中", "已入局"],
      tabIndex: 0,
      options: [
        {
          text: "删除",
          style: {
            backgroundColor: "#dd524d",
          },
        },
      ],
      members: [], // 成员列表
      expect: "", // 期望多少人加入
    };
  },
  onLoad(o) {
    this.gameId = o.gameId;
    this.getMembers();
    this.expect = o.min === o.max ? o.min : o.min + "~" + o.max;
  },
  onShow() {
    // 埋点
    this.$eventRecord(75);
  },
  computed: {
    showingList() {
      // 根据tab判断要展示的列表
      if (this.tabIndex === 1) {
        return this.members.filter((v) => v.state === 1);
      } else {
        return this.members.filter((v) => v.state !== 1);
      }
    },
  },
  methods: {
    // 查询members
    async getMembers() {
      let res = await GameService.gameDetail(this.gameId);
      this.members = res.members;
    },
    audit(state, member) {
      // 审核
      GameService.audit({
        gameId: this.gameId,
        toUserId: member.userId,
        state,
      }).then((res) => {
        this._onMemberChanged(state, member);
      });
    },
    kick(member) {
      // 踢出
      GameService.kick({
        gameId: this.gameId,
        toUserId: member.userId,
      }).then(() => {
        this._onMemberChanged(5, member);
      });
    },
    _onMemberChanged(state, member) {
      // 人员操作以后的更新数据
      this.$toast("操作成功", "success");
      let index = this.members.findIndex((v) => v.userId === member.userId);
      this.members.splice(index, 1, {
        ...this.members[index],
        state,
      });
      uni.$emit("memberChanged");
    },
    open(index) {
      this.$set(this.members[index], "show", true);
      this.members.map((val, idx) => {
        if (index != idx) this.$set(this.members[idx], "show", false);
      });
    },
  },
};
</script>

<style scoped lang="scss">
.flex-c {
  display: flex;
  align-items: center;
}

.flex-s {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.page-bg {
  width: 100%;

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

  .num-tip {
    text-align: center;
    font-size: 30upx;
    font-weight: 400;
    color: #999999;
    margin-top: 108upx;
  }

  .card-bg {
    margin: 40upx 42upx 0upx 42upx;
    padding: 36upx 36upx 42upx 36upx;
    width: calc(100% - 84upx);
    background: #ffffff;
    box-shadow: 0px 4upx 76upx 0px rgba(0, 0, 0, 0.1);
    border-radius: 32upx;
    border: 2upx solid #ffffff;
    display: flex;
    justify-content: space-between;

    .man-item-l {
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

    .man-item-r {
      margin-left: 32upx;
      width: calc(100% - 32upx);

      .name {
        font-size: 30upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #000000;
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
        padding: 2upx 8upx;
        font-size: 26upx;
        line-height: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #666666;
        margin-left: 12upx;
        background: #d8d8d8;
        border-radius: 18upx;
      }

      .space {
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
      }

      .w-btn {
        padding: 6upx 42upx;
        border-radius: 36upx;
        border: 2upx solid #d3d3d3;
        font-size: 32upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #000000;
        margin-right: 36upx;
      }

      .b-btn {
        padding: 6upx 42upx;
        background: #333333;
        border-radius: 36upx;
        font-size: 32upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #fff;
        margin-right: 36upx;
      }

      .jujue {
        text-align: right;
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
      }
    }
  }
}

.order-item {
  width: 100%;
  display: flex;
  position: relative;

  .content {
    width: 100%;
  }
}

.remove {
  width: 160upx;
  height: calc(100% - 36upx);
  color: white;
  position: absolute;
  top: 36upx;
  right: -160upx;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 32upx;
  font-family: PingFangSC-Semibold, PingFang SC;
  font-weight: 600;
  color: #333333;

  .delete-icon {
    height: 52upx;
    width: 52upx;
  }

  .delete-t {
    line-height: 32upx;
    text-align: center;
  }
}
</style>
