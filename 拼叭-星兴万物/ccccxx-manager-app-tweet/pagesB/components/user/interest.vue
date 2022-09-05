<template>
  <view class="interest">
    <view class="interest-header">
      <view class="select-label">选择兴趣吧！</view>
      <view class="text">平台将为您推荐最合适的活动哦！（可多选）</view>
    </view>
    <view class="interest-footer">
      <view class="interest-footer-label" v-for="(v, i) in list" :key="i">
        <button
          :class="{ active: interests.indexOf(v.interestId) > -1 }"
          @click="select(v.interestId)"
        >
          {{ v.caption }}
        </button>
      </view>
    </view>
    <view class="footer-fixed">
      <view class="fixed"
        ><button type="default" @click="submit">我选好啦</button></view
      >
    </view>
  </view>
</template>

<script>
import userService from "../../../service/user";
export default {
  data() {
    return {
      list: [],
      interests: [],
    };
  },
  created() {
    this.getqueryTagList();
  },
  methods: {
    async getqueryTagList() {
      const res = await userService.queryTagList();
      this.list = res;
    },
    select(val) {
      let index = this.interests.indexOf(val);
      if (index > -1) {
        this.interests.splice(index, 1);
      } else {
        this.interests.push(val);
      }
    },
    submit() {
      let interests = this.interests;
	  let arr = []
      interests.forEach((v,i)=>{
       let res = this.list.filter((item,index)=>{
         return item.interestId === v
        })
		arr.push(res)
      })
      uni.setStorageSync('interests',arr)
   
      userService.updateInfo({ interests }).then((res) => {
        this.$toast("修改成功", "success");
        if (res) {
          uni.navigateBack({});
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.interest {
  padding: 20upx 40upx;

  .interest-header {
    .select-label {
      font-weight: 700;
      font-size: 35upx;
    }

    .text {
      margin: 30upx 0;
    }
  }

  .interest-footer {
    margin-bottom: 20px;
    .interest-footer-label {
      margin-bottom: 30upx;
    }
    .active {
      background-color: #000;
      color: #fff;
    }
  }
  .footer-fixed {
    margin-top: 120upx;
    .fixed {
      position: fixed;
      bottom: 20px;
      left: 50%;
      transform: translateX(-50%);

      button {
        width: 400upx;
        height: 80upx;
        background-color: #000;
        color: #fff;
        border-radius: 40upx;
        line-height: 80upx;
      }
    }
  }
}
</style>
