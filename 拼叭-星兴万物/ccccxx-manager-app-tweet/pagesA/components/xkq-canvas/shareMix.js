// import xkqCanvas from './xkq-canvas';
export default {
  data() {
    return {
      shareParams: {
        title: '我要分享', // 分享标题
        path: '', // 分享路径
      },
      shareImgUrl: '', // 生成的分享图
      // 画布大小
      canvasSize: {
        width: 750,
        height: 600,
      },
      // 背景图
      cancasImgs: [],
      fonts: [], // 字体描述
    };
  },
  created() { },
  methods: {
    // 获取图片地址
    tempFilePath(e) {
      this.$refs.canvas.close();
      this.shareImgUrl = e;
      uni.hideLoading()
    },
    // 生成图片地址
    builderPic() {
			this.$refs.canvas.draw();
    },
    // 设置背景
    shareBg(img, params = {}) {
      params = {
        img,
        left: 0, // 左边距
        top: 0, // 右边边距
        width: this.canvasSize.width,
        height: this.canvasSize.height,
        ...params
      };
      return params;
    },
    // 分享标题
    shareTitle(title, params = {}) {
      params = {
        font: title,
        site: 15,
        color: '#fff',
        left: 30,
        top: 80,
        textAlign: '',
        fontW: 'normal bold 28rpx SourceHanSansCN-Bold',
        lines: 1,
        lineH: 30
      };
      return params;
    },
    async initShareParems({ img, title, fonts = [] }) {
			console.log(img,title,fonts)
      this.cancasImgs = [];
      this.font = [];
      //设置背景图
      const cancasImg = this.shareBg(img);
      const cancasImg1 = this.shareBg('https://image-1306191496.cos.ap-nanjing.myqcloud.com/bfe8bb5b-fbed-4183-bf63-32fd138d9b26.png', {
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
      });
      this.cancasImgs.push(cancasImg, cancasImg1);
      // 设置标题
      const titleObj = this.shareTitle(title);
      const arrFonts = []
      const top = 160
      for (let i = 0; i < fonts.length; i++) {
        arrFonts.push({
          font: fonts[i],
          top: top + (60 * i),
          left: 30,
          site: 22,
          color: 'rgba(255, 255, 255, .8)',
          textAlign: '',
          fontW: '',
        })
      }
      this.fonts.push(titleObj, ...arrFonts);
			// this.builderPic()
			setTimeout(() => this.builderPic(),1000)
    },
  },
  // 分享
  onShareAppMessage() {
    let imageUrl = this.shareImgUrl || '';
    console.log('imageUrl', imageUrl)
    return {
      imageUrl: imageUrl,
			path:this.shareParams.path || '',
			title:this.shareParams.title
    };
  },
};
