<template>
  <div
    :class="['bubble', `bubble-${position}`]"
    v-if="bubbleShow"
    @click="hideBubble"
  >
    <span>{{ tips }}</span>
  </div>
</template>

<script>
export default {
  name: "bubble",
  props: {
    tips: {
      type: String,
      default: function () {
        return "新老用户都可以助力，快带上你的好友们一起玩吧！";
      },
    },
    position: {
      type: String,
      default: function () {
        return "bottom";
      },
    },
  },
  data() {
    return {
      bubbleShow: false,
      keyValue: `bubble${new Date().getDate()}`,
    };
  },
  mounted() {
    this.bubbleShow = !uni.getStorageSync(this.keyValue);
    if (this.bubbleShow) {
      uni.setStorageSync(this.keyValue, "1");
    }
  },
  methods: {
    //点击气泡
    hideBubble() {
      this.bubbleShow = false;
    },
  },
};
</script>

<style scoped lang="scss">
.bubble {
  max-width: 185px;
  text-align: left;
  padding: 10px 15px;
  font-size: 12px;
  color: #ffffff;
  background: rgba(0, 0, 0, 0.84);
  border-radius: 5px;
  position: absolute;
  top: -60px;
  right: 0;
  z-index: 99;
  &-top::after {
    content: "";
    position: absolute;
    left: 50%;
    top: -20px;
    border: 10px solid transparent;
    border-bottom-color: rgba(0, 0, 0, 0.84);
  }
  &-left::after {
    content: "";
    position: absolute;
    left: 12%;
    bottom: -20px;
    border: 10px solid transparent;
    border-top-color: rgba(0, 0, 0, 0.84);
  }
  &-right::after {
    content: "";
    position: absolute;
    left: 12%;
    bottom: -20px;
    border: 10px solid transparent;
    border-top-color: rgba(0, 0, 0, 0.84);
  }
  &-bottom::after {
    content: "";
    position: absolute;
    right: 20px;
    bottom: -20px;
    border: 10px solid transparent;
    border-top-color: rgba(0, 0, 0, 0.84);
  }
}
</style>