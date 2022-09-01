// JS页面
Page({
    data: {
   
    },
   
    onLoad: function (options) {
   
    },
   
    onGetCode:function(e){
      this.setData({
        code:e.detail.content
      })
    },
  })