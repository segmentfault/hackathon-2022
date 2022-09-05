<template>
  <view v-if="showCanvas" class="pageBox">
    <view
      class="canvasBox"
      :style="{
        width: canvasSize.width + 'rpx',
        height: canvasSize.height + 'rpx',
        left: (750 - canvasSize.width) / 2 + 'rpx',
        top: (windowHeight / scale - canvasSize.height) / 2 + 'rpx',
      }"
    >
      <canvas
        :style="{
          width: canvasSize.width + 'rpx',
          height: canvasSize.height + 'rpx',
        }"
        canvas-id="myCanvas"
        id="myCanvas"
      ></canvas>
    </view>

    <slot></slot>
  </view>
</template>

<script>
export default {
  name: 'xkq-canvas',
  components: {},

  props: {
    /*  海报图展示的尺寸  */
    canvasSize: {
      type: Object,
      default: function () {
        return {
          width: 500,
          height: 750,
        }
      },
    },
    /* 绘制图片列表 */
    cancasImgs: {
      type: Array,
      default: () => {
        return [
          {
            img: '',
            left: 10,
            top: 10,
            width: 200,
            height: 200,
          },
        ]
      },
    },
    /* 绘制文本数据列表 */
    fonts: {
      type: Array,
      default: function () {
        return [
          {
            font: '',
            site: 28,
            color: '#222',
            left: 0,
            top: 0,
            textAlign: '',
            fontW: '', //字号加粗  默认写法 =》normal bold 32rpx SourceHanSansCN-Bold
          },
        ]
      },
    },

    /* 绘制长文本数据列表 */
    title: {
      type: Array,
      default: function () {
        return [
          {
            font: '',
            site: 28,
            color: '#222',
            left: null,
            top: null,
            textAlign: '',
            fontW: '',
            width: '300',
            lines: 3,
            lineH: 30,
          },
        ]
      },
    },
  },

  watch: {},
  data() {
    return {
      showCanvas: false, //是否显示
      scale: 0, //px 与 rpx的比值
      windowHeight: 720, //页面的安全高度
    }
  },

  created() {
    const { windowWidth, windowHeight } = uni.getSystemInfoSync()
    this.scale = windowWidth / 750
    this.windowHeight = windowHeight
  },
  mounted() {},

  methods: {
    draw() {
      this.showCanvas = true
      this.canvasDraw()
    },

    close() {
      uni.hideLoading()
      this.showCanvas = false
    },

    clear() {
      return false
    },

    getImageInfos(image) {
      //获取图片信息
      return new Promise((resole, reject) => {
        uni.getImageInfo({
          src: image,
          success: function (res) {
            resole(res)
          },
          fail(err) {
            console.error('获取图片信息失败，原因是：', err)
            reject(err)
          },
        })
      })
    },

    canvasToTempFilePath(param) {
      var self = this
      // #ifndef MP-WEIXIN
      uni.canvasToTempFilePath({
        x: param && param.x ? param.x : 0,
        y: param && param.y ? param.y : 0,
        width: param && param.width ? param.width : this.canvasSize.width,
        height: param && param.height ? param.height : this.canvasSize.height,
        destWidth:
          param && param.destWidth
            ? param.destWidth
            : this.canvasSize.width * 3,
        destHeight:
          param && param.destHeight
            ? param.destHeight
            : this.canvasSize.height * 3,
        fileType: param && param.fileType ? param.fileType : 'png',
        canvasId: 'myCanvas',
        success: function (res) {
          self.$emit('tempFilePath', res.tempFilePath)
        },
        fail(err) {
          console.error(err)
          uni.showToast({
            icon: 'none',
            title: '图片生成失败' + err,
          })
        },
      })
      // #endif

      // #ifdef MP-WEIXIN
      wx.canvasToTempFilePath(
        {
          x: param && param.x ? param.x : 0,
          y: param && param.y ? param.y : 0,
          width: param && param.width ? param.width : this.canvasSize.width,
          height: param && param.height ? param.height : this.canvasSize.height,
          destWidth:
            param && param.destWidth ? param.destWidth : this.canvasSize.width,
          destHeight:
            param && param.destHeight
              ? param.destHeight
              : this.canvasSize.height,
          fileType: param && param.fileType ? param.fileType : 'png',
          canvasId: 'myCanvas',
          success: function (res) {
            self.$emit('tempFilePath', res.tempFilePath)
          },
          fail(err) {
            console.error(err)
            uni.showToast({
              icon: 'none',
              title: '图片生成失败' + err,
            })
          },
        },
        this
      )
      // #endif
    },

    async canvasDraw() {
      uni.showLoading({
        title: '图片加载中...',
      })
      var self = this
      const ctx = uni.createCanvasContext('myCanvas', this)

      // 绘制图片
      for (let i in this.cancasImgs) {
        let imgs = await this.getImageInfos(this.cancasImgs[i].img)
        if (imgs.errMsg == 'getImageInfo:ok') {
          ctx.drawImage(
            imgs.path,
            this.cancasImgs[i].left * self.scale,
            this.cancasImgs[i].top * self.scale,
            this.cancasImgs[i].width * self.scale,
            this.cancasImgs[i].height * self.scale
          )
          ctx.save()
        }
      }

      // 绘制文字
      for (let i in this.fonts) {
        if (this.fonts[i].font) {
          if (this.fonts[i].color) {
            ctx.setFillStyle(this.fonts[i].color)
          }

          if (this.fonts[i].site && !this.fonts[i].fontW) {
            ctx.setFontSize(this.fonts[i].site)
          }

          if (this.fonts[i].textAlign) {
            ctx.setTextAlign(this.fonts[i].textAlign)
          } else {
            ctx.setTextAlign('left')
          }

          if (this.fonts[i].fontW) {
            ctx.font = this.fonts[i].fontW
          }

          ctx.fillText(
            this.fonts[i].font,
            this.fonts[i].left * self.scale,
            this.fonts[i].top * self.scale
          )

          ctx.save()
        }
      }

      // 绘制长文本
      for (let i in this.title) {
        if (this.title[i].font) {
          if (this.title[i].color) {
            ctx.setFillStyle(this.title[i].color)
          }

          if (this.title[i].site && !this.title[i].fontW) {
            ctx.setFontSize(this.title[i].site * self.scale)
          }

          if (this.title[i].fontW) {
            ctx.font = this.title[i].fontW
          }

          if (this.title[i].textAlign) {
            ctx.setTextAlign(this.title[i].textAlign)
          } else {
            ctx.setTextAlign('left')
          }

          var fontW = 0,
            lineHs = 0,
            curLines = 1,
            fontLength = this.title[i].font.length

          for (var num = 0; num <= fontLength; num++) {
            if (fontW >= this.title[i].width) {
              lineHs += this.title[i].lineH
              curLines += 1
              fontW = 0
            }
            if (
              curLines == this.title[i].lines &&
              this.title[i].width - fontW < 20
            ) {
              ctx.fillText(
                '...',
                this.title[i].left * self.scale + fontW,
                this.title[i].top * self.scale + lineHs
              )
              break
            }
            ctx.fillText(
              this.title[i].font[num],
              this.title[i].left * self.scale + fontW,
              this.title[i].top * self.scale + lineHs
            )
            fontW += ctx.measureText(this.title[i].font[num]).width
          }
          ctx.save()
        }
      }
      ctx.draw()
      setTimeout(() => {
        this.canvasToTempFilePath({})
      }, 500)
    },
  },
}
</script>
<style lang="scss" scoped>
.pageBox {
  position: fixed;
  z-index: 1;
  left: 0px;
  right: 0px;
  bottom: 0px;
  top: -900px;
  background-color: $uni-bg-color-mask;
}

.canvasBox {
  position: absolute;
  top: -700rpx;
  width: 500rpx;
  height: 700rpx;
  background-color: #f1f1f1;
  // z-index: ;
}
</style>
