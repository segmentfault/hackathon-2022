'use strict'
const recorderManager = wx.getRecorderManager()  // 获取全局唯一的录音管理器 RecorderManager
const innerAudioContext = wx.createInnerAudioContext()  // 创建内部 audio 上下文 InnerAudioContext 对象。
let plugin = requirePlugin("QCloudAIVoice");  //引入语音识别插件
plugin.setQCloudSecret(1258337059, "AKID72h6E5g03pLaYFfpiKeiV0ECtJSKvG1V", "GEvLVO0LPCixvd4E8nBI8i13oIXVCbkc", false);//设置腾讯云账号信息，其中appid是数字，secret是字符串，openConsole是布尔值(true/false)，为控制台打印日志开关
let manager = plugin.getRecordRecognitionManager();  //获取全局唯一的语音识别管理器
Component({
	properties: {
		// 组件大小 单位rpx
		size: {
			type: Number,
			value: 400
		},
		id:{
			type:Number,
			value:'',
		}
	},
	data: {
		// 正在说话
		underway: false,
		// 内容
		content: '',
		fileID:'',
		src:'',
	},
	attached() {
		this.initStyle()
		manager.onStart((res) => {})
		manager.onStop((res) => {})
		manager.onError((res) => {})
	},
	methods: {
		// 初始化样式
		initStyle() {
			let size = this.data.size
			let recordStyle = `width: ${size}rpx;height: ${size}rpx;margin-left: calc(50% - ${size / 2}rpx)`
			let recordRippleStyle = `width: ${size / 2}rpx;height: ${size / 2}rpx;left: calc(50% - ${size / 4}rpx);top: calc(50% - ${size / 4}rpx)`
			let recordImgStyle = `width: ${size}rpx;height: ${size}rpx`
			this.setData({
				recordStyle,
				recordRippleStyle,
				recordImgStyle
			})
		},
		// 按下录音
		recordSoundStart(e) {
			console.log("按下")
			wx.authorize({
				scope: 'scope.record',
				success() {
				  console.log("录音授权成功");
				  //第一次成功授权后 状态切换为2
				  that.setData({
					status: 2,
				  })
				  recorderManager.start(options);
				  recorderManager.onStart(() => {
					console.log('recorder start')
				  });
				  //错误回调
				  recorderManager.onError((res) => {
					console.log(res);
				  })
				},
			  })
			this.setData({
				underway: true
			})
			var that=this
			//开始识别
			manager.start({
				duration: 600000,
				engine_model_type: '16k_zh',
				// 以下为非必填参数，可跟据业务自行修改
				// hotword_id = '08003a00000000000000000000000000',
				// filter_dirty = 0,
				// filter_modal = 0,
				// filter_punc = 0,
				// convert_num_mode = 0,
				// needvad = 1
			});
			//获取识别内容
			manager.onRecognize((res) => {
				if(res.result || res.resList){
					this.triggerEvent('change', res.result)
				}else if(res.errMsg){
					console.log("recognize error", res.errMsg);
				}
			})
		
		},
		// 松开录音
		recordSoundEnd(e) {
			console.log("松开")
			manager.stop();
			this.triggerEvent('stop', {show:"true"})
			this.setData({
				underway: false
			})
			recorderManager.stop();
			recorderManager.onStop((res) => {
			  this.tempFilePath = res.tempFilePath;
			  console.log("isidfhihi",res)
			  var tempFilePath=res.tempFilePath
			  console.log('停止录音', res.tempFilePath)
			  var timestamp = (new Date()).valueOf()+Math.floor(Math.random()*100+1);
			  //上传录音，并获取到fileID
			  wx.cloud.uploadFile({
				cloudPath:"audio/"+timestamp+".mp3",
				filePath:tempFilePath,
				success:res=>{
				  console.log("文件id",res.fileID)
				  //把fileID传到父组件
				  this.triggerEvent('setAudio',res.fileID)
				this.setData({
					fileID:res.fileID
				  })
				},
				fail:err=>{
				  console.log("错误信息",err)
				}
			  })
			  
			  console.log("文件id",this.data.fileID)
			
			})
			
		}
	}
})
