<template>
  <view class="create-game">
    <view class="head">
      <!-- 步骤1，选组局类型和封面 -->
      <template>
        <view class="t1">选择参与的活动类型</view>
        <view class="type-list">
          <view
            v-for="(type, i) in typeList"
            :key="i"
            @click="onTypeSelect(type, i)"
            class="type-item"
            :class="{ active: typeId === i }"
          >
            <view>
              <image
                class="bar-icon"
                :src="typeId === i ? type.icon2 : type.icon1"
              />
              <view class="tab-t">{{ type.name }}</view>
            </view>
          </view>
          <view class="type-item disabled">
            <view class="tab-t">敬请<br />期待</view>
          </view>
        </view>
        <view class="t1" style="padding-bottom: 64upx">选择参与的活动商家</view>
      </template>
    </view>
    <activityMerchant
      v-for="(item, index) in merchantList"
      :key="index"
      :merchantList="item"
      :index="index"
      @getFormData="getFormData"
    >
      <template #button>
        <view class="fiext">
          <view class="fiext-in">
            <button class="button" @click="submit">确定</button>
          </view>
        </view>
      </template>
    </activityMerchant>
  </view>
</template>

<script>
import activityMerchant from "../components/activityMerchant.vue";
import gameService from "../../service/game";
export default {
  components: {
    activityMerchant,
  },
  data() {
    return {
      typeList: [],
      // 活动列表
      merchantList: [],
      // 所有的商家
      allMerchantList: [],
      form: {},
      typeName: "剧本",
      typeId: 0,
      activityId: "",
    };
  },
  created() {
    this.getTypeList();
  },
  mounted() {
    gameService.typeList().then((res) => {
      this.typeList = [];
      res.forEach((e) => {
        if (e.typeId === 2 || e.typeId === 3) {
          this.typeList.push(e);
        }
      });
      this.typeList;
    });
  },
  methods: {
    submit() {
      let form = {};
      this.merchantList.forEach((e) => {
        if (e.sel) {
          form = e;
        }
      });
      form.activityId = this.activityId;
      form.typeId = this.typeList[this.typeId].typeId + "";
      uni.navigateTo({
        url: `/pagesA/create/index?form=${encodeURIComponent(
          JSON.stringify(form)
        )}`,
      });
    },
    getFormData(index) {
      this.merchantList.forEach((e) => (e.sel = false));
      this.merchantList[index].sel = true;
      this.merchantList = [...this.merchantList];
      this.form = this.merchantList[index];
    },
    onTypeSelect(type, i) {
      this.typeId = i;
      // 选择组局类型回调
      this.typeName = type.name;
      this.$set(this.form, "typeId", type.typeId);
      this.merchantList = this.allMerchantList.filter(
        (n) => n.typeName === this.typeName
      );
      this.merchantList[0].sel = true;
    },
    getMerchantList() {
      this.merchantList = [];
      gameService
        .getSupplierList({
          activityId: this.activityId,
        })
        .then((res) => {
          res.forEach((e) => {
            e.sel = false;
          });
          res.forEach((e) => {
            if (e.cover) {
              e.cover =
                e.cover.split("?")[0] + "?imageMogr2/crop/98x98/gravity/center";
            }
          });
          this.allMerchantList = res;
          let typeName = this.typeName || 1;
          this.merchantList = this.allMerchantList.filter(
            (n) => n.typeName === typeName
          );
          this.merchantList[0].sel = true;
        });
    },
    async getTypeList() {
      const res = await gameService.typeList();
      this.typeList = res;
    },
  },
  onLoad(option) {
    this.activityId = option.activityId;
    this.getMerchantList();
  },
};
</script>

<style lang="scss">
.create-game {
  .fiext {
    position: fixed;
    padding: 0 40upx;
    bottom: 0;
    left: 0;
    right: 0;
    width: 100%;
    background-color: #fff;
    z-index: 99;
    .fiext-in {
      padding: 10upx 0 env(safe-area-inset-bottom) 0;
    }
    .button {
      width: 100%;
      height: 96upx;
      color: #fff;
      font-size: 40upx;
      border-radius: 53upx;
      background-color: #000;
    }
  }
  .head {
    width: 100%;
    background-color: #f6f6f6;
    .t1 {
      padding-top: 64upx;
      margin-left: 42upx;
      font-size: 40upx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #000000;
    }

    .type-list {
      margin: 36upx 42upx 0px 42upx;
      width: calc(100% - 84upx);
      display: flex;
      background: #f5f5f5;

      .active {
        background: #000000 !important;

        .tab-t {
          color: #fff !important;
        }
      }

      .type-item:last-child {
        margin-right: 0upx !important;
      }

      .type-item {
        margin-right: 20upx;
        padding: 38upx 0 26upx 0;
        width: calc((100% - 80upx) / 5);
        background: #ffffff;
        border-radius: 24upx;
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        justify-content: center;

        &.disabled {
          padding: 0;
          .tab-t {
            color: #999;
            margin-top: 0;
          }
        }

        .bar-icon {
          position: relative;
          margin: 0px auto;
          height: 48upx;
          width: 48upx;
          display: block;
        }

        .tab-t {
          margin-top: 20upx;
          line-height: 44upx;
          font-size: 32upx;
          font-weight: 500;
          color: #333333;
          text-align: center;
        }
      }
    }
  }
}
</style>