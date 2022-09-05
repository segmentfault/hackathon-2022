<template>
  <view class="page-bg">
    <view class="edit-img">
      <view class="enterLimit"
        ><progress
          class="progress"
          stroke-width="52upx"
          :percent="form.rate"
          activeColor="#000"
          backgroundColor="#C2C2C2"
          active
          show-info
          @activeend="activeend"
        ></progress
        ><text class="text">资料完善度</text></view
      >
      <view class="pictureWall">
        <!-- 添加图片 -->
        <!-- v-if="oldPictures.length === 5" -->
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/1070e9e1-8682-43f3-9757-57b94466662c.png"
          @click="uploading"
        ></image>
      </view>
      <!-- class="headerBg" -->
      <imgsBanner
        :imgList.sync="pictures"
        :currentImg="currentImg"
        class="headerBg"
        @updateUserInfo="updateUserInfo"
      ></imgsBanner>
    </view>
    <view class="input-list">
      <view class="input-item input-item-photo flex-s">
        <view class="t1">头像</view>
        <view class="input-item-head">
          <uni-popup ref="popup" type="bottom" background-color="#fff">
            <button @tap="$u.throttle(getuserinfo, 2000)">微信授权头像</button>
            <button @click="getImg">从手机相册选择</button>
          </uni-popup>
          <button class="input-item-head" plain @click="open">
            <u-image
              class="header"
              width="100%"
              height="100%"
              border-radius="50%"
              :src="form && form.head"
            >
            </u-image>
          </button>
          <button
            v-if="canIUseGetUserProfile"
            class="input-item-head no-login"
            plain
            @tap="$u.throttle(getuserinfo, 2000)"
          >
            <view class="input-item-head no-login">点击获取</view>
          </button>
        </view>
      </view>
      <view class="input-item">
        <view class="t1">姓名</view>
        <view class="input-ctn" style="text-align: right">
          <u-input
            v-model="form.nickName"
            style="width: 100%"
            :maxlength="20"
            placeholder="请输入姓名"
          ></u-input>
        </view>
        <view class="input-ctn-icon"></view>
      </view>
      <view class="input-item">
        <view class="t1">性别</view>
        <view class="input-ctn">
          <picker
            class="input-picker"
            mode="selector"
            :range="sex"
            range-key="name"
            @change="radioGroupChange"
            >{{ sexLabel }}</picker
          >
        </view>
        <!-- <view class="input-ctn">
          <u-radio-group class="clearfix" v-model="form.sex" @change="radioGroupChange">
            <u-radio class="sex-item" v-for="(item, index) in sex" :key="index" :name="item.val">{{
              item.name
            }}</u-radio>
          </u-radio-group>
        </view> -->
        <view class="input-ctn-icon">
          <image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/917de11a-2b57-48a4-868e-e8e4783bd850.png"></image>
        </view>
      </view>
      <view class="input-item">
        <view class="t1">出生日期</view>
        <view class="input-ctn">
          <picker
            class="input-picker"
            mode="date"
            fields="day"
            :value="pickerDate.value"
            :start="pickerDate.start"
            :end="pickerDate.end"
            @change="getTime"
            >{{ pickerDate.birth }}
          </picker>
        </view>
        <view class="input-ctn-icon">
          <image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/917de11a-2b57-48a4-868e-e8e4783bd850.png"></image>
        </view>
      </view>
      <view class="input-item">
        <view class="t1">兴趣</view>
        <view class="input-ctn" @click="setInterest">
          <view class="input-interests clearfix">
            <view class="interests" v-for="(v, i) in form.interests" :key="i">{{
              v
            }}</view>
          </view>
        </view>
        <view class="input-ctn-icon">
          <image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/917de11a-2b57-48a4-868e-e8e4783bd850.png"></image>
        </view>
      </view>
      <view class="input-item">
        <view class="t1">心情</view>
        <view class="input-ctn" style="text-align: right">
          <u-input
            v-model="form.intro"
            style="width: 100%"
            :maxlength="50"
            placeholder="随便写点什么吧"
          ></u-input>
        </view>
        <view class="input-ctn-icon">
          <image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/917de11a-2b57-48a4-868e-e8e4783bd850.png"></image>
        </view>
      </view>
    </view>

    <view class="save-bottom">
      <view class="signout-btn" @click="updateUserInfo">保存</view>
    </view>
  </view>
</template>

<script>
// import comHead from "../../components/com-head.vue";
import userService from "../../service/user.js";
import login from "../../service/login";
import { cosUploadFiles } from "../cos/utils";
import moment from "moment";
import imgsBanner from "../components/imgsBanner-tag.vue";
import UniPopup from "../components/uni-popup/components/uni-popup/uni-popup.vue";

export default {
  components: {
    // comHead,
    UniPopup,
    imgsBanner,
  },
  data() {
    return {
      pictures: [],
      oldPictures: [],
      currentImg: 0, //当前默认选中
      // 日历选择器参数
      pickerDate: {
        value: new Date(),
        birth: "请选择",
        state: "1900-01-01",
        end: "3000-12-12",
      },
      sex: [
        {
          name: "男",
          val: 1,
        },
        {
          name: "女",
          val: 2,
        },
      ],
      form: {
        intro: "",
        sex: 1,
      },
      birthDate: "",
      constellationOopup: false,
      data: "",
    };
  },
  onLoad(options) {
    this.canIUseGetUserProfile = uni.canIUse("getUserProfile");
    let self = this;
    uni.$on("updateTag", function (data) {
      self.$set(self.form, "interests", data);
      // self.form.interests = data || []
    });
    // this.queryUserInfo();
  },
  onShow() {
    if (!this.data) {
      this.queryUserInfo();
    } else {
      this.form = this.data;
    }
    let res = uni.getStorageSync("interests");
    if (res) {
      //  res 可能为undefined
      this.data.interests = [];
      res.forEach((v) => {
        v.forEach((item) => {
          this.data.interests.push(item.caption);
        });
      });
    }
    // 埋点
    this.$eventRecord(69);
  },
  mounted() {
    this.data = this.form;
  },

  methods: {
    // 上传图片
    uploading() {
      if (this.pictures.length >= 6) {
        this.$toast("最多上传6张");
        return;
      }
      uni.chooseImage({
        count: 6 - this.pictures.length,
        success: async (res) => {
          let userinfo = uni.getStorageSync("userInfo");
          if (!userinfo) return;
          let _Date = new Date();
          let key = `${_Date.getFullYear()}/${_Date.getMonth() + 1}/${
            userinfo.ticket + Date.now()
          }`;
          try {
            cosUploadFiles(res.tempFiles, key, (err, data) => {
              if (!err) {
                // 上传成功，回到页面
                let pictures = [];

                data.files.forEach((file) => {
                  pictures.push(`https://${file.data.Location}`);
                });
                this.pictures.forEach((e) => {
                  if (!pictures.includes(e)) {
                    pictures.push(e);
                  }
                });
                this.pictures = [...pictures];
								this.updateUserInfo()
              }
            });
          } catch (error) {}
        },
        fail: (err) => {
          // this.$toast("最多六张图片", "error");
        },
      });
    },
    //   进度条监听事件
    activeend() {
      1;
    },
    // 日期选择器
    getTime(e) {
      this.pickerDate.birth = e.detail.value;
    },
    // 打开prope组件
    open() {
      this.$refs.popup.open("bottom");
    },
    getImg() {
      uni.chooseImage({
        count: 1,
        sizeType: "original",
        sourceType: "album",
        success: (res) => {
          let _Date = new Date();
          let userinfo = uni.getStorageSync("userInfo");
          let key = `${_Date.getFullYear()}/${_Date.getMonth() + 1}/${
            userinfo.ticket + Date.now()
          }`;
          cosUploadFiles(res.tempFiles, key, (err, data) => {
            if (!err) {
              data.files.forEach((file) => {
                this.form.head = `https://${file.data.Location}`;
              });
              this.$refs.popup.close();
            }
          });
        },
        fail: (err) => {
          this.$refs.popup.close();
        },
      });
    },
    // 跳转到选择兴趣页面
    setInterest() {
      uni.navigateTo({
        url: `/pagesA/components/user/interest?form=${encodeURIComponent(
          JSON.stringify(this.form)
        )}`,
      });
    },
    // 获取用户信息
    async getuserinfo(e) {
      let res = await uni.getUserProfile({
        desc: "完善信息",
      });
      // 如果允许了
      if (res.length === 2) {
        let userInfo = res[1].userInfo;
        let { avatarUrl, nickName, gender } = userInfo || {};
        this.$set(this.form, "sex", gender);
        this.$set(this.form, "head", avatarUrl);
        this.$set(this.form, "nickName", nickName);
        this.$refs.popup.close();
      } else {
        // 如果拒绝
        this.$refs.popup.close();
      }
    },
    debounce(fn, interval) {
      var enterTime = 0; //触发的时间
      var gapTime = interval || 300; //间隔时间，如果interval不传值，默认为300ms
      return function () {
        var that = this;
        var backTime = new Date(); //第一次函数return即触发的时间
        if (backTime - enterTime > gapTime) {
          fn.call(that, arguments);
          enterTime = backTime; //赋值给第一次触发的时间 保存第二次触发时间
        }
      };
    },
    radioGroupChange(e) {
      if (e.detail.value === "0") {
        this.form.sex = 1;
      } else if (e.detail.value === "1") {
        this.form.sex = 2;
      }
    },
    // 修改用户信息
    updateUserInfo() {
      this.form.pictures = this.pictures;
      this.form.birth = this.pickerDate.birth;
      if (this.form.nickName.length > 20) {
        this.$toast("姓名不能超过20个字符");
        return;
      }
      if (this.form.birth === "请选择") {
        this.$toast("请填写出生日期");
        return;
      }
      if (this.form.intro.length > 50) {
        this.$toast("心情不能超过50个字符");
        return;
      }
      let param = {
        ...{},
        ...this.form,
        data: 1, // 用户来源
      };

      if (param.interests && param.interests.length && param.interests[0].id) {
        param.interests = param.interests.map((v) => v.id);
      } else {
        delete param.interests;
      }
      // if (!param.age || isNaN(param.age)) {
      //   this.$toast("请输入正确年龄", "error");
      //   return;
      // }
      return userService.updateInfo(param).then((res) => {
        this.$toast("操作成功", "success");
        this.queryUserInfo();
      });
    },
    // 查询用户信息
    queryUserInfo() {
      userService.queryUserInfo().then((res) => {
        let {
          nickName = "",
          constellation = "",
          interests = [],
          sex = "",
          intro = "",
          birthDate = "",
          pictures = [],
          rate = 0,
          head = "",
        } = res;
        birthDate = moment(birthDate).format("YYYY-MM-DD");
        this.pickerDate.value = birthDate;
        if (birthDate !== "Invalid date") {
          this.pickerDate.birth = birthDate;
        } else {
          this.pickerDate.birth = "请选择";
        }
        this.pictures = pictures.map((i) => i.url);
        this.form = {
          intro,
          nickName,
          birthDate,
          constellation,
          interests,
          sex,
          head,
          pictures,
          rate,
        };
        this.data = this.form;
      });
    },
  },
  computed: {
    sexLabel() {
      let label = "请选择";
      this.sex.forEach((e) => {
        if (e.val === this.form.sex) {
          label = e.name;
        }
      });
      return label;
    },
  },
};
</script>

<style lang="scss">
.flex-s {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.flex-c {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.page-bg {
  min-height: 100vh;
  width: 100%;
  background-color: #fff;

  .edit-img {
    width: 100%;
    height: 512upx;
    position: relative;

    .headerBg {
      width: 100%;
      height: 460upx;

      .imgsBanner_swiper {
        height: 460upx;
        position: relative;

        image {
          height: 460upx;
        }
      }

      .imgLength {
        display: none;
      }

      .scrollImgBox {
        position: absolute;
        bottom: 34upx;
        left: 57upx;
        width: ceil(750upx - 288upx);
      }
    }

    .pictureWall {
      width: 160upx;
      height: 160upx;
      position: absolute;
      bottom: 34upx;
      right: 57upx;
      z-index: 2;

      image {
        position: relative;
        top: -10upx;
        width: 180upx;
        height: 180upx;
      }
    }
  }

  .enterLimit {
    background-color: #c2c2c2;

    .progress {
      position: relative;
    }

    .text {
      position: absolute;
      top: 0;
      left: 50upx;
      font-size: 28upx;
      font-weight: 600;
      color: #fff;
    }
  }

  .input-item-head {
    margin-right: 24upx;
  }

  .input-list {
    padding: 0 44upx 158upx 54upx;

    .input-picker {
      height: 35px;
      line-height: 35px;
    }

    .input-interests {
      padding: 10upx 0;
      min-height: 55upx;
      display: flex;
      justify-content: flex-end;
    }

    .input-item {
      position: relative;
      display: flex;
      align-items: center;

      // border-bottom: 2upx solid #e5e5e5;
      &.input-item-photo {
        padding: 20upx 0;
      }

      &:after {
        content: "";
        display: block;
        position: absolute;
        bottom: 0;
        left: 96upx;
        right: 0;
        height: 2upx;
        background-color: #e5e5e5;
      }

      .input-ctn-icon {
        width: 24upx;
        height: 36upx;

        image {
          width: 24upx;
          height: 36upx;
        }
      }

      .t1 {
        flex: none;
        font-size: 32upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #999999;
      }

      .input-ctn {
        padding: 31upx 26upx 31upx 0upx;
        margin-left: 20upx;
        text-align: right;
        width: calc(100% - 84upx);
        font-size: 32upx;
        font-weight: 600;
        color: #333333;

        .sex-item {
          float: right;
        }

        .interests {
          width: 126upx;
          height: 50upx;
          background-color: #000;
          color: #fff;
          text-align: center;
          padding: 0 20upx;
          border-radius: 25upx;
          line-height: 50upx;
          float: right;
          margin: 0 5upx;
        }

        .arrow-right {
          color: #999999;
        }

        ::v-deep input {
          text-align: right;
          font-size: 32upx;
          font-weight: 600;
          color: #333333;
        }

        .tag-item {
          padding: 5upx 28upx;
          font-size: 32upx;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #ffffff;
          background: #333333;
          border-radius: 32upx;
          margin-right: 28upx;
          margin-top: 5upx;
        }
      }

      &-head {
        height: 100upx;
        width: 100upx;

        &:first-child {
          border: 2upx solid #000;
          border-radius: 50%;
          color: #000;
          line-height: 100upx;
          font-size: 24upx;
        }

        .header {
          height: 100%;
          width: 100%;
        }
      }
    }
  }

  .save-bottom {
    position: fixed;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 2;
    padding: 30upx 100upx 86upx 100upx;
    background-color: #fff;

    .signout-btn {
      width: 100%;
      padding: 14upx 0px;
      border-radius: 46upx;
      background-color: #fff;
      border: 2upx solid #999999;
      font-size: 40upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #333333;
      text-align: center;
    }
  }
}
</style>
