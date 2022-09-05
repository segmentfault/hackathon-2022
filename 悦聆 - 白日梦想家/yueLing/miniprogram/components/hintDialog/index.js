'use strict'
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    // 标题
    title: {
      type: String,
      value: '确认删除吗？'
    },
  },
  attached() {
    this.setData({
      title: this.data.title
    })
  },
  /**
   * 组件的初始数据
   */
  data: {

  },

  /**
   * 组件的方法列表
   */
  methods: {
    // 点击modal的回调函数
    clickMask() {
      // 点击modal背景关闭遮罩层，如果不需要注释掉即可
      this.triggerEvent('cancel')
    },
   // 点击取消按钮的回调函数
    cancel() {
      this.triggerEvent('cancel')  //triggerEvent触发事件
    },
    // 点击确定按钮的回调函数
    confirm() {
      this.triggerEvent('confirm')
    }
  }

})
