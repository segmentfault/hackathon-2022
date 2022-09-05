<template>
  <view class="page-apply">
    <u-cell-group>
      <Tips></Tips>
      <!-- 活动类型 -->
      <view class="margin-t">
        <u-cell-item
          style="background-color: #fff"
          required
          :arrow="false"
          :border-bottom="false"
        >
          <view slot="title" class="flex-c">
            <view class="c-title">活动类型</view>
          </view>
        </u-cell-item>
        <view class="cates-list flex-box">
          <view
            v-for="(item, i) in typeList"
            :key="i"
            @click="!disabled && handlerType(item)"
          >
            <view
              class="select-item"
              :class="{ active: gameType === item.typeId }"
              >{{ item.typeName }}</view
            >
          </view>
        </view>
      </view>

      <!-- 期望主题 -->
      <view class="margin-t" v-if="cates.length">
        <u-cell-item
          style="background-color: #fff"
          required
          :arrow="false"
          :border-bottom="false"
        >
          <view slot="title" class="flex-c">
            <view class="c-title">
              期望主题
              <text style="margin-left: 20upx; color: #999">（多选）</text>
            </view>
          </view>
        </u-cell-item>
        <view class="cates-list flex-box">
          <view v-for="(item, i) in cates" :key="i">
            <view
              class="select-item"
              :class="{ active: selectCates.includes(item) }"
              @click="!disabled && handlerSelectCate(item)"
              >{{ item }}</view
            >
          </view>
        </view>
        <view class="remark">
          <view class="title">
            <input
              type="text"
              placeholder="主题备注：如《一点半》"
              v-model="remark"
              :disabled="disabled"
            />
          </view>
        </view>
      </view>
    </u-cell-group>
    <!-- 选择商家 -->
    <view class="margin-t suppLier" style="padding: 0">
      <u-cell-item
        style="background-color: #fff"
        required
        :arrow="false"
        :border-bottom="false"
      >
        <view slot="title" class="flex-c">
          <view class="c-title">选择商家</view>
        </view>
      </u-cell-item>
      <view @click="!disabled && goSupplierItem()">
        {{
          supplierIds.length ? `已选择${supplierIds.length}家商家` : ' 点击选择'
        }}
        <text></text>
      </view>
    </view>
    <!-- 到店时间 -->
    <view class="margin-t">
      <!-- 到店日期 -->
      <u-cell-item
        style="background-color: #fff"
        required
        :arrow="false"
        :border-bottom="false"
      >
        <view slot="title" class="flex-c">
          <view class="c-title">
            到店日期
            <!-- <text style="margin-left: 20upx; color: #999">（多选）</text> -->
          </view>
        </view>
      </u-cell-item>
      <!-- 时间选择 -->
      <view class="cates-list flex-box">
        <view
          v-for="(item, i) in timeDate"
          :key="i"
          @click="!disabled && timeDateRangesClick(item)"
        >
          <view
            class="select-item"
            :class="{ active: arriveDate.includes(item.date),invalid:!item.enable && !arriveDate.includes(item.date)}"
            >{{ item.caption }}</view
          >
        </view>
      </view>
    </view>
    <!-- 活动通知 -->
    <view class="margin-t" style="padding-bottom: 0">
      <u-cell-item title="活动开始前通知我" :arrow="false">
        <view slot="right-icon">
          <u-switch
            v-model="notification"
            active-color="#3abf3e"
            inactive-color="#eee"
            :disabled="disabled"
            @change="subscribe"
          ></u-switch>
        </view>
      </u-cell-item>
    </view>

    <!-- 同行人数 -->
    <view class="margin-t">
      <u-cell-item
        style="background-color: #fff"
        required
        :arrow="false"
        :border-bottom="false"
      >
        <view slot="title" class="flex-c">
          <view class="c-title">同行人数(包括自己)</view>
        </view>
      </u-cell-item>
      <view class="cates-list flex-box">
        <view v-for="(item, i) in peoples" :key="i">
          <view
            class="select-item"
            :class="{ active: peoplesType === item.value }"
            @click="!disabled && peopleClick(item)"
            >{{ item.label }}</view
          >
        </view>
        <view class="picher">
          <picker
            mode="selector"
            :range="num"
            :value="boyNum"
            :disabled="disabled"
            @change="change($event, 'boyNum')"
            >{{ boyNum }}男</picker
          >
          <picker
            mode="selector"
            :range="num"
            :value="girlNum"
            :disabled="disabled"
            @change="change($event, 'girlNum')"
            >{{ girlNum }}女</picker
          >
        </view>
      </view>
    </view>
    <view class="btm-bg">
      <view
        class="btm-btn"
        :class="{ w48: applyState === 'apply' }"
        @click="handlerEdit(state)"
        v-if="applyState === 'apply'"
        style="margin-right: 4%"
        >{{ disabled ? '修改预约' : '更新预约' }}</view
      >
      <view
        class="btm-btn"
        @click="
          checkAndShow(state, applyState === 'apply' ? 'cancle' : 'confirm')
        "
        :class="{ w48: applyState === 'apply' }"
        >{{ applyState === 'apply' ? '取消预约' : '提交预约' }}</view
      >
    </view>
  </view>
</template>

<script>
import Tips from './components/tips.vue';
import GameService from '../../service/game.js';
import moment from 'moment';
export default {
  components: {
    Tips,
  },
  data() {
    return {
      boyNum: 0, // 男
      girlNum: 0, // 女
      num: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      // cates: {
      //   2: ['不限', '情感', '恐怖', '本格', '欢乐', '硬核'],
      //   3: ['不限', '科幻', '恐怖', '机械', '古风', '真人'],
      //   1: [],
      // },
			cates:[],
      state: '', // 状态
      typeId: 2,
      typeList: [
        {
          label: '剧本',
          value: 2,
        },
        {
          label: '密室逃脱',
          value: 3,
        },
        {
          label: '酒吧',
          value: 1,
        },
      ],
      peoplesType: null,
      peoples: [
        { label: '拼场', value: 0 },
        { label: '包场 (5人起)', value: 1 },
      ],
      timeDate: [
        {
          label: '11-11(周四)',
          value: '2021-11-11 00:00:00',
        },
        {
          label: '11-12(周五)',
          value: '2021-11-12 00:00:00',
        },
        {
          label: '11-13(周六)',
          value: '2021-11-13 00:00:00',
        },
        {
          label: '11-14(周日)',
          value: '2021-11-14 00:00:00',
        },
      ],
      supplierIds: [], // 商家ids
      timeDateRanges: [],
      supplierIds: [], // 选中商家ids
      notification: false, // 活动通知
      activityId: 12,
      gameType: 2,
      arriveDate: [], // 到店日期
      arriveDateValue: [],
      selectCates: [],
      peopleNum: 0, //同行人数
      // 底部按钮的禁用状态
      disabled: false,
      // notApply 未预约 底部展示预约按钮
      // apply 已预约  底部展示修改和取消
      applyState: 'notApply',
    };
  },
  onLoad(o) {
    this.$eventRecord(151);
    if (o.activityId) this.activityId = o.activityId;
    if (o.state) this.state = parseInt(o.state);
    if (o.supplierIds && o.supplierIds.length) {
      this.supplierIds = o.supplierIds.split(',');
    }
		if(this.activityId){
			this.getAvtiveParams()
		}
  },
  // 回退
  onShow() {},
  mounted() {
    // 详情页面
    if (this.state === 2 || this.state === 4 || this.state === 3) {
      this.disabled = true;
      this.applyState = 'apply';
      this.setInfo();
    } else {
      this.disabled = false;
      this.applyState = 'notApply';
    }
  },
  methods: {
		getAvtiveParams(){
			GameService.getActiveParams(this.activityId).then(res => {
				res.applyTimeList.forEach(item => item.date = this.$utils.dayjs(item.date).format('YYYY-MM-DD HH:mm:ss'))
				this.typeList = res.typeList
				this.timeDate = res.applyTimeList
				console.log(this.timeDate)
				this.handlerType(res.typeList[0])
			})
		},
    handlerSelectCate(item) {
      if (this.selectCates.includes('不限') && item === '不限') {
        this.selectCates = [];
      } else if (!this.selectCates.includes('不限') && item === '不限') {
        this.selectCates = ['不限'];
      } else if (this.selectCates.includes(item)) {
        const index = this.selectCates.findIndex((e) => e === item);
        this.selectCates.splice(index, 1);
      } else {
        const index = this.selectCates.findIndex((e) => e === '不限');
        if (index > -1) {
          this.selectCates.splice(index, 1);
        }
        this.selectCates.push(item);
      }
    },
    // 设置信息
    setInfo() {
      // 详情
      GameService.applyActivityDetail(this.activityId).then((res) => {
        const {
          boyNum,
          girlNum,
          monopoly,
          peopleNum,
          notification,
          suppliers,
          gameType,
        } = res;
        this.gameType = gameType; // 1 酒吧
        this.selectCates = res.cates || []
        this.supplierIds = suppliers.map((item) => item.supplierId); // 商家地址
        this.arriveDateValue = res.arriveDate
          ? `${moment(res.arriveDate).format('YYYY-MM-DD HH:mm:ss')}`
          : null;
				// 到店日期
				this.timeDate.forEach(item => {
					if(item.date === this.arriveDateValue){
						this.arriveDate = [item.date]
					}
				})
        // this.arriveDate = [
        //   this.timeDate.filter((item) => item.date === this.arriveDateValue),
        // ]; 
				console.log(this.arriveDate)
        this.notification = !!notification; // 活动开始前通知
        this.peoplesType = monopoly; // 包场类型
        this.boyNum = boyNum; // 男生数量
        this.girlNum = girlNum; // 女生数量
        this.peopleNum = peopleNum; // 总人数
      });
    },
    handlerEdit(state) {
      const _this = this;
      if (this.disabled) {
        uni.showModal({
          title: '修改预约',
          content: '该活动是否需要修改',
          success(res) {
            if (res.confirm) {
              _this.disabled = false;
            }
          },
          fail() {
            return;
            console.log(456);
          },
        });
      } else {
        this.checkAndShow(state, 'update');
      }
    },
    // 跳转商家列表
    goSupplierItem() {
      let supplierIds = '';
      if (this.supplierIds.length) {
        supplierIds = this.supplierIds.join(',');
      }
      uni.navigateTo({
        url: `/pagesA/activity/supplierList?typeId=${
          this.gameType
        }&supplierIds=${supplierIds || null}&activityId=${this.activityId}`,
      });
    },
    subscribe() {
      const _this = this;
      // 订阅消息
      if (!this.notification) {
        return;
      }
      uni.requestSubscribeMessage({
        tmplIds: [
          '4lfzUTD4bNwEIPn0HA4cQoda3wkZCU4RfqVjH46z2kU',
          '9gG7_Aml_JGcSd61Oi8gJSclpqZ3WljZLqRjVrlYbcY',
          '9BfG_-7orSnNpcSjCON9qxXkWd4LJW6snq7oXf5-EpQ',
        ],
        success(res) {
          if (res['4lfzUTD4bNwEIPn0HA4cQoda3wkZCU4RfqVjH46z2kU'] === 'accept') {
            uni.reLaunch({
              url: '/pages/activity/discountCenter',
            });
          } else {
            _this.notification = false;
          }
        },
      });
    },
    handlerType(item) {
      this.gameType = item.typeId
			if(item.cates && !item.cates.includes('不限')){
				item.cates.unshift('不限')
			}
			this.cates = item.cates ? item.cates : []
      if (this.cates.length) {
        this.selectCates = [this.cates[0]]
      } else {
        this.selectCates = []
      }
    },
    // 到店日期
    timeDateRangesClick(item) {
			if(!item.enable) return
      this.arriveDate = [item.date];
      this.arriveDateValue = item.date;
    },
    peopleClick(item) {
      this.peoplesType = item.value;
    },
    change(val, type) {
      const { detail } = val;
      this[type] = detail.value;
    },
    async checkAndShow(state, stateText = 'confirm') {
      if (
        this.peoplesType === 1 &&
        parseInt(this.boyNum) + parseInt(this.girlNum) < 5
      ) {
        return this.$toast('包场人数需要大于5人哦');
      }
      if (this.applyState === 'apply' && stateText === 'cancle') {
        let that = this;
        uni.showModal({
          title: '取消预约',
          content: '取消预约后商家不会主动与您联系，如需参与活动需要再次预约',
          success(res) {
            if (res.confirm) {
              return GameService.cancelApplyActivity(that.activityId).then(
                (res) => {
                  // 取消报名埋点
                  that.$eventRecord(109);
                  that.$toast('取消成功');
                  uni.navigateBack();
                }
              );
            }
          },
          fail() {
            return;
            console.log(456);
          },
        });
      } else {
        if (this.supplierIds.length === 0) return this.$toast('请选择商家');
        if (parseInt(this.boyNum) + parseInt(this.girlNum) === 0) {
          this.$toast('同行人数(包括自己)不能为0');
          return;
        }
        if (this.peoplesType === null) {
          this.$toast('请选择拼场或包场');
          return;
        }
        const parmas = {
          gameType: this.gameType, // 1 酒吧
          monopoly: this.peoplesType, // 是否包场 1包场 0不包场
          boyNum: parseInt(this.boyNum), // 男数量
          girlNum: parseInt(this.girlNum), //女数量
          cates: this.selectCates, // 选中的类型，
          notification: this.notification ? 1 : 0, // 活动通知
          supplierIds: this.supplierIds, // 商家id
          peopleNum: parseInt(this.boyNum) + parseInt(this.girlNum), // 同行人数包括自己
          activityId: this.activityId,
          arriveDate: this.arriveDateValue, // 到店日期
        };
        let res = {};
        if (this.applyState === 'notApply') {
          // 如果是创建预约
          res = await GameService.applyActivity(parmas);
        } else {
          // 如果是修改预约
          res = await GameService.applyActivityEdit(parmas);
        }
        this.$toast('预约成功，请助力完领取优惠券');
        setTimeout(() => {
          uni.redirectTo({
            url: `/pagesA/barActivity/discountCenter?activityId=${this.activityId}`,
          });
        }, 1500);

        // 提交预约埋点
        this.$eventRecord(141);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.page-apply {
  background: #eeeeee;
  padding-bottom: calc(126upx + env(safe-area-inset-bottom));
  .remark {
    padding: 30upx 30upx 0;
    .title {
      display: flex;
      align-items: center;
    }
    input {
      flex: 1;
      height: 94upx;
      border: 2upx solid #ccc;
      padding: 0 30upx;
    }
  }
}

.title-remark {
  width: 100%;
  padding: 26upx 32upx 12upx;
  font-size: 28upx;
  line-height: 54upx;
  color: #606266;
}

.remark {
  .u-input {
    margin: 0 42upx 0 42upx;
  }

  textarea {
    padding: 20upx !important;
    height: 134upx;
    background: #f2f2f2;
    border-radius: 8upx;
  }
}

.popup-info {
  min-height: 156upx;
  padding: 20upx 32upx;
  background: #f5f2f1;
  border-radius: 16upx;
  font-size: 32upx;
  font-weight: 400;
  color: #000000;
  line-height: 44upx;
  margin-bottom: 12upx;
  justify-content: flex-start;
}

.flex-c {
  display: flex;
  align-items: center;
}

.header {
  height: 108upx;
  width: 108upx;
  border-radius: 50%;
  position: relative;
  overflow: hidden;
}

.no-login {
  background: #ffffff;
  border: 2upx solid #979797;
  font-size: 24upx;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #999999;
  line-height: 24upx;
  padding: 30upx 20upx;
}

.btn-phone {
  font-size: 32upx;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #999999;
}

.c-desc {
  margin-left: 24upx;
  font-size: 32upx;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #999999;
}

.cates-list {
  margin: 0px 40upx;
  width: calc(100% - 80upx);
  // width: 100%;
  justify-content: flex-start !important;
  display: -webkit-box;
  overflow-x: scroll;
  &:first-child {
    justify-content: flex-end;
  }
  .picher {
    display: flex;
    picker {
      width: 144rpx;
      height: 62rpx;
      border: 2rpx solid #ccc;
      border-radius: 38rpx;
      text-align: center;
      line-height: 62rpx;
      margin-right: 20rpx;
    }
  }
}

.cates-list::-webkit-scrollbar {
  display: none;
}

.margin-t {
  margin-top: 24upx;
  background: #fff;
  padding-bottom: 36upx;
  &.suppLier {
    display: flex;
    padding: 0;
    align-items: center;
    justify-content: space-between;
    view {
      margin-right: 20upx;
      color: #999;
      text {
        width: 20rpx;
        height: 20rpx;
        display: inline-block;
        border-right: 4rpx solid #000;
        border-top: 4rpx solid #000;
        transform: rotate(45deg);
        margin-left: 10rpx;
      }
    }
  }
  .tips {
    margin: 0upx 20upx;
    padding: 12upx 24upx;
    width: calc(100% - 40upx);
    background: rgba(248, 154, 21, 0.1);
    border-radius: 42upx;
    font-size: 28upx;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #c37910;
  }

  .select-btn {
    margin-top: 30upx;
    margin-left: 40upx;

    .checkbox {
      height: 56upx;
      width: 56upx;
    }

    &-t {
      margin-left: 24upx;
      font-size: 36upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #000000;
    }
  }
}

.item-card {
  display: flex;
  background-color: #fff;
  margin: 28upx 40upx 0upx 40upx;
  width: calc(100% - 80upx);
  align-items: center;

  &-check {
    height: 60upx;
    width: 60upx;
    flex: none;
  }
}

.cell {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 200upx;
  border-bottom: 1upx solid #f5f5f5;
  padding: 0 30upx;
}

.select-item {
  padding: 0 20upx 0 20upx;
  margin-right: 14upx;
  border: 2upx solid #979797;
  border-radius: 36upx;
  font-size: 26upx;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #333333;
  line-height: 60upx;

  &.active {
    color: #fff;
    background: #333333;
    border-color: #333;
  }
}
.invalid{
		background: #eee!important;
		border:1px solid #eee;
		color:#999;
	}

.btm-bg {
  position: fixed;
  left: 0px;
  padding: 0upx 40upx;
  bottom: 0px;
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  flex-wrap: wrap;
  z-index: 99;
  height: calc(126upx + env(safe-area-inset-bottom));
  background-color: #fff;
  padding-top: 20upx;
  padding-bottom: env(safe-area-inset-bottom);
  .w48 {
    width: 48% !important;
    display: inline !important;
  }
  .btm-btn {
    margin-top: 12upx;
    padding: 18upx 0px;
    flex: 1;
    background: #333333;
    border-radius: 48upx;
    text-align: center;
    font-size: 40upx;
    font-family: PingFangSC-Semibold, PingFang SC;
    font-weight: 600;
    color: #ffffff;
  }
}

.checkbox {
  width: 60upx;
  height: 60upx;
}

.number-box {
  margin-left: 40px;
}
/deep/ .u-icon-plus,
/deep/ .u-icon-minus {
  width: 25px !important;
  height: 25px;
  box-sizing: border-box;
  border-radius: 18px !important;
  border: 1px solid #979797;
}
</style>
