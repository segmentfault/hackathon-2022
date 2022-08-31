// pages/kcb/kcb.js
const Calendar = require('./util/Calendar.js');
const courseData = require('./courseData.js');

Page({
    
    /**
     * 页面的初始数据
     */
    data: {
        times:[
            {startTime:"08:00", endTime:"08:50"},
            {startTime:"09:00", endTime:"09:50"},
            {startTime:"10:00", endTime:"10:50"},
            {startTime:"11:00", endTime:"11:50"},
            {startTime:"12:00", endTime:"14:00", siesta: 1},
            {startTime:"14:00", endTime:"14:50"},
            {startTime:"15:00", endTime:"15:50"},
            {startTime:"16:00", endTime:"16:50"},
            {startTime:"17:00", endTime:"17:50"},
            {startTime:"18:00", endTime:"18:50"}
        ]
        
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
    
        //日历上方的星期
      var weekList = Calendar.generateWeekList();
      
      this.setData({
        weekList:weekList,
        courseList:courseData.courseList //课程表
      })

    }

})
