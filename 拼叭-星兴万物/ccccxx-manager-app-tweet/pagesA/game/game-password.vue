<template>
  <view class="card">
    <view class="tip">请填写入局密码</view>
    <view class="text">请联系局长大人获取</view>
    <!-- v-if="form.joinType === 3" -->
    <view class="verificationCode">
      <input
        type="number"
        maxlength="4"
        @input="inputValue"
        focus
        adjust-position="true"
      />
      <view>{{ password[0] }}</view>
      <view>{{ password[1] }}</view>
      <view>{{ password[2] }}</view>
      <view>{{ password[3] }}</view>
    </view>
    <!-- <view class="password">
      <u-input
        :focus="focus[0]"
        type="number"
        maxlength="1"
        @input="(val) => passwordInput(val, 0)"
        confirm-type="done"
        class="name-input password-input"
        placeholder=" "
      />
      <u-input
        :focus="focus[1]"
        maxlength="1"
        @input="(val) => passwordInput(val, 1)"
        type="number"
        confirm-type="done"
        class="name-input password-input"
        placeholder=" "
      />
      <u-input
        :focus="focus[2]"
        maxlength="1"
        type="number"
        confirm-type="done"
        @input="(val) => passwordInput(val, 2)"
        class="name-input password-input"
        placeholder=" "
      />
      <u-input
        :focus="focus[3]"
        maxlength="1"
        type="number"
        confirm-type="done"
        @input="(val) => passwordInput(val, 3)"
        class="name-input password-input"
        placeholder=" "
      />
    </view> -->
  </view>
</template>

<script>
export default {
  props: ["detail", "isPasswoedBox"],
  data() {
    return {
      password: new Array(),
      focus: [false, false, false, false],
    };
  },
  methods: {
    // 输入密码
    inputValue(e) {
      let value = e.detail.value;
      let arr = [...value];
      // this.password = arr;
      this.password = arr.join("");
      this.$emit("changePassword", this.password);
    },
    // passwordInput(val, num) {
    //   this.password = this.password.split();
    //   this.password[num] = val;
    //   let focus = [false, false, false, false];
    //   focus[num + 1] = true;
    //   this.focus = focus;
    //   this.password = this.password.join("");
    //   this.$emit("changePassword", this.password);
    // },
  },
};
</script>

<style lang='scss'>
.card {
  width: 90vw;
  height: 600upx;
  border-radius: 16upx;
  padding: 100upx;
  background: #fff;
  text-align: center;
  .tip {
    font-weight: 700;
    font-size: 36upx;
    letter-spacing: 15upx;
  }
  .text {
    margin: 50upx 0;
  }

  // .password {
  //   width: 480upx;
  //   display: flex;
  //   justify-content: space-between;

  //   &-input {
  //     width: 108upx;
  //     height: 108upx;
  //     padding: 0;
  //     border: 2upx solid #000;
  //     border-radius: 20upx;
  //   }

  //   /deep/ input {
  //     width: 100%;
  //     height: 108upx;
  //     text-align: center;
  //   }
  //   .mini-input {
  //     padding: 12upx 40upx;
  //     width: calc((100% - 124upx) / 2);
  //     background: #ffffff;
  //     border-radius: 24upx;
  //     border: 2upx solid #c0b6b6;
  //   }
  // }
}
.verificationCode {
  overflow: hidden;
  position: relative;
  width: 100%;
  display: flex;
  justify-content: space-between;
  padding-top: 20px;
  input {
    width: 100vh;
    position: absolute;
    left: -500px;
    top: 20px;
    height: 60px;
    background: #e9e9e9;
    opacity: 0;
    font-size: 10px;
  }
  view {
    width: 55px;
    border: 1upx solid #979797;
    height: 60px;
    text-align: center;
    line-height: 60px;
    border-radius: 4px;
    font-size: 30px;
    color: #4dbf6e;
  }
}
</style>