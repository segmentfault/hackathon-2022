<template>
  <view
    class="circle-progress"
    :style="{
      width: widthPx + 'px',
      height: widthPx + 'px',
      backgroundColor: bgColor,
    }"
  >
    <!-- 有的不支持canvas-id属性，必须用id属性 -->
    <canvas
      class="canvas-bg"
      :canvas-id="canvasId"
      :id="canvasId"
      :style="{
        width: widthPx + 'px',
        height: widthPx + 'px',
      }"
    ></canvas>
    <canvas
      class="canvas"
      :canvas-id="elId"
      :id="elId"
      :style="{
        width: widthPx + 'px',
        height: widthPx + 'px',
      }"
    ></canvas>
    <slot></slot>
  </view>
</template>

<script>
export default {
  name: "circle-progress",
  props: {
    // 圆环进度百分比值
    percent: {
      type: Number,
      default: 0,
      // 值在0到100之间
      validator: (val) => {
        return val >= 0 && val <= 100;
      },
    },
    // 圆环底色（灰色的圆环）
    inactiveColor: {
      type: String,
      default: "#ececec",
    },
    // 圆环激活部分的颜色
    activeColor: {
      type: String,
      default: "#009dff",
    },
    // 圆环线条的宽度，单位upx
    borderWidth: {
      type: [Number, String],
      default: 14,
    },
    // 整个圆形的宽度，单位upx
    width: {
      type: [Number, String],
      default: 200,
    },
    // 整个圆环执行一圈的时间，单位ms
    duration: {
      type: [Number, String],
      default: 1500,
    },
    // 圆环进度区域的背景色
    bgColor: {
      type: String,
      default: "#ffffff",
    },
  },
  data() {
    return {
      canvasId: "canvasId", //一个页面多个圆形进度
      elId: "elId",
      widthPx: uni.upx2px(this.width), // 转成px后的整个组件的背景宽度
      borderWidthPx: uni.upx2px(this.borderWidth), // 转成px后的圆环的宽度
      startAngle: -Math.PI / 2, // canvas画圆的起始角度，默认为3点钟方向，定位到12点钟方向
      progressContext: null, // 活动圆的canvas上下文
      newPercent: 0, // 当动态修改进度值的时候，保存进度值的变化前后值，用于比较用
      oldPercent: 0, // 当动态修改进度值的时候，保存进度值的变化前后值，用于比较用
    };
  },
  watch: {
    percent(nVal, oVal = 0) {
      if (nVal > 100) nVal = 100;
      if (nVal < 0) oVal = 0;
      this.newPercent = nVal;
      this.oldPercent = oVal;
      setTimeout(() => {
        this.drawCircleByProgress(oVal);
      }, 50);
    },
  },
  created() {
    // 赋值，用于加载后第一个画圆使用
    this.newPercent = this.percent;
    this.oldPercent = 0;
  },

  mounted() {
    this.drawProgressBg();
    this.drawCircleByProgress(this.oldPercent);
  },
  methods: {
    //一个页面多个progress时ID需不同
    randomId() {
      return "progressId" + parseInt(Math.random() * 1000000);
    },
    drawProgressBg() {
      let ctx = uni.createCanvasContext(this.canvasId, this);
      ctx.setLineWidth(this.borderWidthPx); // 设置圆环宽度
      ctx.setStrokeStyle(this.inactiveColor); // 线条颜色
      ctx.beginPath(); // 开始描绘路径
      let radius = this.widthPx / 2;
      ctx.arc(
        radius,
        radius,
        radius - this.borderWidthPx,
        0,
        2 * Math.PI,
        false
      );
      ctx.stroke(); // 对路径进行描绘
      ctx.draw();
    },
    drawCircleByProgress(progress) {
      let ctx = this.progressContext;
      if (!ctx) {
        ctx = uni.createCanvasContext(this.elId, this);
        this.progressContext = ctx;
      }
      // 表示进度的两端为圆形
      ctx.setLineCap("round");
      // 设置线条的宽度和颜色
      ctx.setLineWidth(this.borderWidthPx);
      ctx.setStrokeStyle(this.activeColor);
      // 计算过渡时间
      let time = Math.floor(this.duration / 200);
      let endAngle = ((2 * Math.PI) / 100) * progress + this.startAngle;
      ctx.beginPath();
      // 半径为整个canvas宽度的一半
      let radius = this.widthPx / 2;
      ctx.arc(
        radius,
        radius,
        radius - this.borderWidthPx,
        this.startAngle,
        endAngle,
        false
      );
      ctx.stroke();
      ctx.draw();
      // 增大了百分比
      if (this.newPercent > this.oldPercent) {
        progress++;
        if (progress > this.newPercent) return;
      } else {
        // 减少百分比
        progress--;
        if (progress < this.newPercent) return;
      }
      setTimeout(() => {
        // 定时器，为了让进度条有动画效果
        this.drawCircleByProgress(progress);
      }, time);
    },
  },
};
</script>

<style scoped>
.circle-progress {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}
.canvas-bg {
  position: absolute;
}
.canvas {
  position: absolute;
}
</style>
