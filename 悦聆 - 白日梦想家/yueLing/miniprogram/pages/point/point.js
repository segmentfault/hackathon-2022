Page({
  data:{
    showDialog:false
  },
  handleCancel(){
    this.setData({
      showDialog:false
    })
  },
  handleConfirm(){
    this.setData({
      showDialog:false
    })
  },
  delete:function(e){
    console.log('点击了删除')
    //弹窗显示
    this.setData({
      showDialog:true
    })
  }
})

