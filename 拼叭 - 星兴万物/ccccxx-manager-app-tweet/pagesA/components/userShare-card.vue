<template>
  <view class="share" catchtouchmove="preventMove" @click="cancel">
    <view class="card">
      <!-- 用户信息 -->
      <view class="info">
        <view>
          <image
            :src="myUserInfo.head + '?imageView2/1/w/200/h/200/q/80/webp'"
          ></image
        ></view>
        <view class="name">{{ myUserInfo.nickName }}</view>
      </view>
      <!--关闭按钮-->
      <view class="close">
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/c50f52b9-28ad-44be-8631-7f7090970700.png"
        ></image>
      </view>
      <!-- 二维码 -->
      <view class="QRCode">
        <image class="code" :src="url"></image>
      </view>
      <!-- 关闭按钮 -->
    </view>
    <view class="bottom">
      <view class="contont" @tap.stop="saveImg">
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/5277ad83-1480-4df7-98a6-9d6eacae09e0.png"
          alt=""
        />
        <view class="txt">保存图片</view>
      </view>
      <view class="contont" @tap.stop>
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/cd726a96-667e-4da3-b011-d37697f7f096.png"
          alt=""
        />
        <button class="txt"  open-type="share">分享好友</button>
      </view>
    </view>
		<canvas
		  canvas-id="canvas"
		  id="canvas"
		></canvas>
		<view class="pop-shade" v-if="showOpenstting">
			<view class="pop-body">
				<view class="content">保存图片到相册需要使用您的手机相册</view>
				<view class="flx foot">
					<view @click="showOpenstting = false">取消</view>
					<button open-type="openSetting" @opensetting="openSetting">确定</button>
				</view>
			</view>
		</view>
  </view>
</template>

<script>
import basic from "../../service/basic";
export default {
  props: {
    item: {
      type: Object,
      default: () => {
        return {};
      },
    },
    codeUrl: {
      type: String,
      default: () => {
        return '';
      },
    },
  },
  mounted() { 
    this.setCodeUrl(this.codeUrl)
  },
  data() {
    return {
      url: '',
			ctx:null,
			showOpenstting:false
    };
  },
  methods: {
		//授权回调
		openSetting(e){
			this.showOpenstting = false
			if(e.detail.authSetting['scope.writePhotosAlbum']){
				this.savaNotice()
			}
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
    setCodeUrl(val) {
      let parmas = {
				scene: `shareUserId${this.myUserId}`,
				isHyaline: false,
				width: 300,
				autoColor: false,
				lineColor: {
					r: "0",
					g: "0",
					b: "0",
				},
				page: "pages/index/index",
			};
			basic.getMiniProgramQrcodeApi(parmas).then((res) => {
				this.url = "data:image/png;base64," + res;
				this.CreateCode();
			});
    },
    cancel() {
      this.$emit('cancel', false);
    },
		//生成二维码图片
		async CreateCode(qrcodeUri){
			const bguri = 'https://image-1306191496.cos.ap-nanjing.myqcloud.com/795dfd4c-1dc6-483f-a003-75186ea8ee8f.png'
			const bgimgInfo = await uni.downloadFile({url:bguri})
			const bgUri = bgimgInfo[1].tempFilePath
			const headuri = this.myUserInfo.head
			const headimgInfo = await uni.downloadFile({url:headuri})
			const headImg = headimgInfo[1].tempFilePath
			const name = this.myUserInfo.nickName
			const codeImg = await this.base64ToSave(this.url, "tmp_base64src")
			const ctx = uni.createCanvasContext('canvas',this)
			this.ctx = ctx
			ctx.setFillStyle('#fff')
			ctx.fillRect(0,0,414,618)
			ctx.drawImage(bgUri,0,0,414,618)
			ctx.font = "normal bold 18px sans-serif"
			ctx.setTextAlign('center')
			ctx.fillStyle = '#000'
			ctx.fillText(name,207,174)
			ctx.drawImage(codeImg,40,204,334,334)
			ctx.save()
			ctx.beginPath()
			ctx.arc(207,92,52,0,Math.PI * 2)
			ctx.clip()
			ctx.drawImage(headImg,155,40,104,104)
			ctx.restore()
			ctx.draw()
		},
		saveImg(){
			this.savaNotice()
		},
		//保存画布图片
		savaNotice(ctx){
		  const that = this
			uni.showLoading()
			const timeId = setTimeout(() => {
				//生成图片
				uni.canvasToTempFilePath({
					canvasId: 'canvas',
					fileType: 'png',
					quality: 1,
					success(res) {
						console.log(res.tempFilePath)
						uni.hideLoading()
						//保存到相册
						uni.saveImageToPhotosAlbum({
							filePath: res.tempFilePath,
							success(data) {
								that.$toast(`保存成功`)
								that.$emit('cancel', false)
							},
							fail(err) {
								if(err.errMsg == 'saveImageToPhotosAlbum:fail auth deny'){
									that.showOpenstting = true
								}
							}
						})
					},
					fail(err){
						console.log(err)
						uni.hideLoading()
					}
				},that)
			}, 500)
			this.ctx.draw(true, timeId)
		},
  },
};
</script>

<style lang="scss" scoped>
.share {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 100vh;
  padding: 0 54upx;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 9999;
  .card {
    position: relative;
    display: flex;
    flex-direction: column;
    margin-top: 110upx;
    background: url('https://image-1306191496.cos.ap-nanjing.myqcloud.com/795dfd4c-1dc6-483f-a003-75186ea8ee8f.png')
      no-repeat center;
    background-size: 100% 100%;
    padding-bottom: 130upx;
    // 用户信息
    .info {
      image {
        width: 104upx;
        height: 104upx;
        border-radius: 52upx;
        margin-top: 84upx;
      }
      view {
        text-align: center;
        font-size: 32upx;
        font-family: AlibabaPuHuiTiR;
      }
      .name {
        z-index: 200;
        margin-bottom: 38upx;
      }
    }
    .close {
      position: absolute;
      top: 18upx;
      right: 14upx;
      image {
        width: 84upx;
        height: 84upx;
      }
    }
    // 二维码
    .QRCode {
      width: 494upx;
      height: 494upx;
      margin: 0 auto;
      border-radius: 32upx;
      overflow: hidden;
      .code {
        width: 100%;
        height: 100%;
      }
    }
  }
  .bottom {
    display: flex;
    justify-content: space-around;
    border-radius: 16px;
    background-color: #fff;
    height: 170rpx;
    width: 100%;
    text-align: center;
    margin-top: 16upx;
    .contont {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding-top: 32upx;
      image {
        width: 56upx;
        height: 56upx;
      }
      .txt {
        background-color: #fff;
        padding-top: 10upx;
        font-size: 32upx;
        font-family: AlibabaPuHuiTiR;
        color: #000000;
        line-height: 44upx;
      }
    }
  }
}
canvas{
	position: fixed;
	left:-1000px;
	width:414px;
	height: 618px;
	z-index: 99;
}
	.pop-shade{
		position: fixed;
		top:0;
		left:0;
		right:0;
		bottom:0;
		background: rgba(0,0,0,.6);
		.pop-body{
			text-align: center;
			position: absolute;
			background: #fff;
			width: 80%;
			height:300upx;
			left:10%;
			top:50%;
			margin-top: -150upx;
			border-radius: 12upx;
			.content{
				padding:0 20upx;
				height:200upx;
				display: flex;
				align-items: center;
				justify-content: center;
			}
			.foot{
				display: flex;
				height:98upx;
				line-height: 98upx;
				border-top:1px solid #eee;
				view,button{
					width: 50%;
				}
				button{
					font-size: 16px;
					line-height: 100upx;
					font-weight: bold;
					background: transparent;
					border:0;
					font-size:16px;
					font-weight: 400;
					border-left:1px solid #eee;
					border-radius: 0;
				}
				button::after{
					border:0;
					border-radius: 0;
				}
			}
		}
	}
</style>
