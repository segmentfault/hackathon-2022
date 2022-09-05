// pages/memo/components/memoItem.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    item: Object
  },

  /**
   * 组件的初始数据
   */
  data: {
    src: '',
  },
  /**
   * 组件的方法列表
   */
  methods: {
    onTap: function(){
      this.triggerEvent('onTap')
    },
    text(){
      const myaudio = wx.createInnerAudioContext();
      this.audioCtx = wx.createAudioContext('myAudio');
      console.log("sss")
    
      wx.cloud.callFunction({
        name:'getVideo',
        data:{
          text:'芜湖'
        }
      }).then(res=>{
        console.log("成功",res)
        this.setData({
          src:'https://upload.jxxhy.cn/uploads/mail/20190129/1548745225000.mp3'
        })
        console.log("ffd",this.audioCtx.play)
        this.audioCtx.play;
        // console.log("dfd",this.audioCtx)
        // console.log("值",this.data.src)
      }).catch(res=>{
        console.log("失败",res)
      })
    },
    jump0(e){
    console.log(e)
      wx.navigateTo({
        url: '/pages/reminder/reminder?id=' + this.data.item._id,
      })
    },
    
  }
})
