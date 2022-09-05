<template>
  <view class="share-two" catchtouchmove="preventMove">
    <view class="card">
      <canvas
        canvas-id="canvas"
        id="canvas"
        style="width: 321px; height: 622px; position: absolute; left: -1000px"
      ></canvas>
      <image :src="bgImgUrl"></image>
      <view class="info">
        <image :src="codeUrl"></image>
      </view>
      <view class="fixed">
        <button @click="cancel">取消</button>
        <button @click="save">保存相册</button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    codeUrl: {
      type: String,
      default: () => {
        return "";
      },
    },
		bgImgUrl: {
		  type: String,
		  default: () => {
		    return "";
		  },
		},
		postion:{
			type:Boolean,
			default:()=>{
				return false;
			}
		},
		left:{
			type:Number,
			default:()=>{
				return '';
			}
		},
		right:{
			type:Number,
			default:()=>{
				return '';
			}
		}
  },
  data() {
    return {
      image: true,
    };
  },
  methods: {
    //   关闭分享面板
    cancel() {
      this.$emit("cancel");
    },
    save() {
      let that = this;
      uni.authorize({
        scope: "scope.writePhotosAlbum",
        success(res) {
          console.log(res);
          that.saveImage();
        },
        fail(err) {
          //这里是用户拒绝授权后的回调
        },
      });
    },
    //base64转本地图片，将数据存储在本地
    base64ToSave(base64data, FILE_BASE_NAME = "tmp_base64src") {
      const fsm = uni.getFileSystemManager();
      return new Promise((resolve, reject) => {
        const [, format, bodyData] =
          /data:image\/(\w+);base64,(.*)/.exec(base64data) || [];
        if (!format) {
          reject(new Error("ERROR_BASE64SRC_PARSE"));
        }
        const filePath = `${wx.env.USER_DATA_PATH}/${FILE_BASE_NAME}.${format}`;
        const buffer = wx.base64ToArrayBuffer(bodyData);
        fsm.writeFile({
          filePath,
          data: buffer,
          encoding: "binary",
          success() {
            resolve(filePath);
          },
          fail() {
            reject(new Error("ERROR_BASE64SRC_WRITE"));
          },
        });
      });
    },
    // 保存图片函数
    async saveImage() {
      let that = this;
      let p1 = await uni.downloadFile({
        url: that.bgImgUrl,
      });
	  console.log(p1)
      p1 = p1[1].tempFilePath;

      let p2 = await this.base64ToSave(this.codeUrl, "tmp_base64src");
      // console.log(p2);
      // p2 = await uni.downloadFile({
      //   url: p2,
      // });
      // debugger;
      // p2 = p2[1].tempFilePath;

      // 由于这是自定义组件。使用canvas的时候  createCanvasContext  第二个参数一定要传this
      let cxt = uni.createCanvasContext("canvas", this);
			let r = 10,x = 115,y = 380,w = 90,h=90;
      //  小程序二维码图
      cxt.drawImage(p1, 0, 0, 321, 622);
			cxt.beginPath()
			cxt.setFillStyle('transparent')
			
			cxt.arc(x + r,y + r,r,Math.PI,Math.PI * 1.5)
			cxt.moveTo(x + r,y)
			cxt.lineTo(x + w - r,y)
			cxt.lineTo(x + w,y + r)
			cxt.arc(x + w - r,y + r,r,Math.PI * 1.5,Math.PI * 2)
			
			cxt.lineTo(x + w,y + h -r)
			cxt.lineTo(x + w - r,y + h)
			cxt.arc(x + w -r,y + h - r,r,0,Math.PI * .5)
			
			cxt.lineTo(x + r,y + h)
			cxt.lineTo(x, y + h - r)
			cxt.arc(x + r,y + h - r,r,Math.PI * .5,Math.PI)
			
			cxt.lineTo(x,y + r)
			cxt.lineTo(x + r,y)
			
			cxt.fill()
			cxt.closePath()
			cxt.clip()
			cxt.setFillStyle('#fff')
			cxt.fillRect(0,0,w,h)
			
      cxt.drawImage(p2, 115, 380, 90, 90);
      cxt.draw(true, timeId);
			const timeId = setTimeout(() => {
				//生成图片
				uni.canvasToTempFilePath(
				  {
				    width: 321,
				    height: 622,
				    canvasId: "canvas",
				    fileType: "jpg",
				    success(res) {
				      // 保存到本地
				      console.log(res);
				      that.image = false;
				      uni.saveImageToPhotosAlbum({
				        filePath: res.tempFilePath,
				        success(res) {
				          console.log(res);
				          that.$toast("保存成功");
				          that.cancel();
				        },
				      });
				    },
				    fail(e) {
				      console.log(e);
				    },
				  },
				  that
				);
			}, 1000)
      // 生产指定大小的图片
      
    },
  },
};
</script>

<style lang='scss' scoped>
.share-two {
  position: fixed;
  top: 0;
  left: 0;
	right:0;
  bottom:0;
  display: flex;
	justify-content: center;
	align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 9999;
  .card {
    background: #fff;
		width: 80%;
		padding:40upx;
		border-radius: 20upx;
		position: relative;
    image {
			width: 100%;
      height: 1000upx;
    }
    .info {
      left:0;
			right:0;
      position: absolute;
      bottom: 400upx;
      text-align: center;
      image {
        width: 160upx;
        height: 160upx;
        border-radius: 32upx;
      }
    }
    .fixed {
      display: flex;
      justify-content: space-around;
      margin-top: 44upx;
      button {
        margin: 0;
        width: 220upx;
        height: 76upx;
				line-height: 76upx;
        background: #000000;
        border-radius: 52upx;
        color: #fff;
      }
    }
  }
}
</style>
