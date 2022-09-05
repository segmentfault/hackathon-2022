// miniprogram/pages/reminder/reminder.js
const DatePickerUtil = require('../../utils/DatePicker.js')
const db = wx.cloud.database()
import dayjs from "dayjs";
const app = getApp()
const _ = db.command
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // checked: true,
    memo: {
      content: '',
      needRemind: false,
      remindPhone: 0,
      updatePhone: '',
      title: '',
      audio:'',
      updateTimeDate: '',
      remindPerson:'',
      creatTime:'',
      sAudio:''
    },
   changeState:false,
    show:true,
    time: '选择预约时间',
    // 被提醒人index
    memberIndex: null,
    // 当前用户信息
    userInfo: {},
    multiArray: [], //piker的item项
    multiIndex: [], //当前选择列的下标
    year: '', //选择的年
    month: '', //选择的月
    day: '', //选择的日
    hour: '', //选择的时
    minute: '', //选择的分
    delta: 1,
    showDialog:false
  },
  onLoad: function (options) {
    console.log("新增option",options)
    let id = options.id;
    this.setData({
      type: id ? 'edit' : 'create',
      homeIndicatorHeight: app.globalData.homeIndicatorHeight
    })
    if(options.content){
        this.setData({
          memo:options
        })
        this.data.memo.creatTime=Date.parse(new Date());
    }
    if (id) {
      this.setData({
        changeState:true,
      })
      db.collection('memo').where({
        _id: id
      }).get().then(res => {
        console.log(res)
        this.setData({
          memo: res.data[0]
        })
        this.onShow(dayjs(res.data[0].remindTimeDate).format('YYYY-MM-DD HH:mm:ss'));
      })
    } else if (!id) {
      wx.setNavigationBarTitle({
        title: '新建备忘录'
      })
      const openid =wx.getStorageSync('openid');
      wx.cloud.callFunction({
        name:'point',
        data:{
          text:'creatOn'
        }
      })
      db.collection('point').where({
        creatID:openid
      }).get().then(res=>{
        console.log("ressfs",res)
        console.log("sbbs",res.data[0])
        if(!res.data[0]){
          console.log("添加添加")
          db.collection('point').where({
            _id:'b69f67c062cf7de90b0a527d02974f39'
          }).update({
            data:{
              creatID:_.push(openid)
            }
          })
          wx.cloud.callFunction({
            name:'point',
            data:{
              text:'creatOnload'
            }
          })
        } 
      })
    }
    this.getMembers()

    // if (options.memo) {
    //   this.data.memo = JSON.parse(options.memo)
    //   this.data.memo.content = options.context || ''
    //   this.setData({
    //     memo: this.data.memo
    //   })
    // }
  },

  onShow: function (date) {
    let type = this.data.type;
    var loadPickerData = type == 'create' ? DatePickerUtil.loadPickerData() : DatePickerUtil.loadPickerData(date);
    var getCurrentDate = type == 'create' ? DatePickerUtil.getCurrentDate() : DatePickerUtil.getCurrentDate(date);
    var GetMultiIndex = DatePickerUtil.GetMultiIndex();
    //这里写的是为了记录当前时间
    let year = parseInt(getCurrentDate.substring(0, 4));
    let month = parseInt(getCurrentDate.substring(5, 7));
    let day = parseInt(getCurrentDate.substring(8, 10));
    let hour = parseInt(getCurrentDate.substring(11, 13));
    let minute = parseInt(getCurrentDate.substring(14, 16));
    this.setData({
      multiArray: loadPickerData, //picker数组赋值，格式 [years, months, days, hours, minutes]
      multiIndex: GetMultiIndex, //设置pickerIndex，[0,0,0,0,0]
      time: date, //设置当前时间 ，currentYears+'-'+mm+'-'+dd+' '+hh+':'+min
      year: year, //记录选择的年
      month: month, //记录选择的月
      day: day, //记录选择的日
      hour: hour, //记录选择的时
      minute: minute, //记录选择的分
    });
  },

  // 获取成员
  async getMembers() {
    const openid = wx.getStorageSync('openid')
    const res = await db.collection('userInformation').where({
      _openid: openid
    }).get()

    let family = res.data[0].family?res.data[0].family:[];
    // 将自己也加进去
    console.log("res",res)
    family.push({
      name: res.data[0].nickName,
      phone: res.data[0].phone
    })
    this.data.userInfo = res.data[0]
    this.setData({
      family
    })
    console.log(this.data)
  },
  openDailog() {
    this.setData({
      showShadow: true
    })
    this.show=true;
    this.animate('#shadow', [
      {opacity: 0},
      {opacity: 1},
    ], 100)
    this.animate('#dialog', [
      {bottom: '-540rpx'},
      {bottom: 0},
    ], 200)
  },
  closeDialog() {
    this.setData({
      showShadow: false
    })
    this.animate('#shadow', [
      {opacity: 1},
      {opacity: 0},
    ], 100)
    this.animate('#dialog', [
      {bottom: 0},
      {bottom: '-540rpx'},
    ], 200)
  },
  change(e) {
    this.data.memo.content = e.detail
    this.setData({
      memo: this.data.memo
    })
  },
  //关闭语音框
  stop(e){
    if(e){
      this.setData({
        showShadow: false
      })
      this.animate('#shadow', [
        {opacity: 1},
        {opacity: 0},
      ], 100)
      this.animate('#dialog', [
        {bottom: 0},
        {bottom: '-540rpx'},
      ], 200)
    }
  },
  //获取录音下载地址
  async getAudio(e){
    console.log("拿值",e.detail);
    this.data.memo.audio=e.detail;
  },
  onChange({
    detail
  }) {
    this.data.memo.needRemind = detail
    this.setData({
      memo: this.data.memo
    })
  },
  bindTitle(e) {
    this.setData({
      ['memo.title']: e.detail.value
    })
  },
  bindContent(e) {
    this.setData({
      ['memo.content']: e.detail.value
    })
  },
  bindRemindPersonChange(e) {
    console.log("ww",e)
    this.setData({
      memberIndex: e.detail.value
    })
  },
  handleDelete() {
    this.setData({
      showDialog:true
    })
  },
  handleCancel() {
    this.setData({
      showDialog:false
    })
  },
  handleConfirm() {
    this.delete()
  },
  delete() {
    let that = this
    let id = this.data.memo._id;
    wx.cloud.deleteFile({
      fileList:[this.data.memo.audio]
    })
    db.collection('memo').doc(id).remove({
      success: function (res) {
        wx.showToast({
          title: '删除成功',
          icon: 'none'
        })
        that.handleCancel()
        setTimeout(() => {
          wx.navigateBack({
            delta: 10
          })
        }, 400)
      }
    })
  },
  jump() {
    let that = this;
    if(!that.data.memo.title) {
			return wx.showToast({
				title: '请输入备忘录标题！',
				icon: 'none'
			})
		}
		if(!that.data.memo.content) {
			return wx.showToast({
				title: '请输入备忘录内容！',
				icon: 'none'
			})
    }
    if(that.data.memo.needRemind){
      if(!that.data.memberIndex&&that.data.memo.remindPhone=='') {
        return wx.showToast({
          title: '请选择被提醒人',
          icon: 'none'
        })
      }
      if(!that.data.time) {
        return wx.showToast({
          title: '请选择提醒时间',
          icon: 'none'
        })
      }
     
    }
   
    if(that.data.memo.needRemind) {
      let iosData = that.data.time.replace(/-/g, '/')
      that.data.memo.remindTimeDate = new Date(iosData);
    } else {
      that.data.memo.remindTimeDate = ''
    }
    that.data.memo.updateTimeDate = new Date(DatePickerUtil.getCurrentDate());
    that.data.memo.initiator = this.data.userInfo.nickName
    that.data.memo.avatarUrl = this.data.userInfo.avatarUrl
    //判断无改动保存
    if(this.data.memberIndex===null){
      this.data.memo.remindPhone = this.data.memo.remindPhone;
      this.data.memo.remindPerson =this.data.memo.remindPerson;
    } else{
      this.data.memo.remindPhone = this.data.family[this.data.memberIndex].phone;
      this.data.memo.remindPerson =this.data.family[this.data.memberIndex].name;
    }


    if (that.data.type === 'edit') {
      if (that.data.memo.needRemind) {
        let remindTime = that.data.memo.remindTimeDate.getTime()
        let now = new Date().getTime()
        console.log(remindTime)
        console.log(now)
        if (remindTime > now) {
          that.data.memo.isfinish = false
        } else {
          that.data.memo.isfinish = true
        }
      }

      let id = that.data.memo._id;
      delete that.data.memo._id
      delete that.data.memo._openid
      this.getVideo();
      console.log("数据",that.data.memo);
      setTimeout(() => {
        db.collection('memo').doc(id).update({
          data: that.data.memo
        }).then((res) => {
          console.log(res);
          wx.showToast({
            title: '已保存',
            icon: 'none'
          })
          setTimeout(() => {
            // wx.redirectTo({
            //   url: '/pages/memo/index',
            // })
            console.log(this.data.delta)
            wx.navigateBack({
              delta: 10
            })
          }, 400)
        })
      }, 1000);
      


    } else {

      if (that.data.memo.needRemind) {
        that.data.memo.isfinish = false
      }
      this.getVideo();
      db.collection('memo').add({
          // data 字段表示需新增的 JSON 数据
          data: this.data.memo
        })
        .then(res => {
          wx.switchTab({
            url: '/pages/memo/index?active=all',
          })
        })

    }
  },
  //获取合成音频文件
  async getVideo(){
    wx.cloud.callFunction({
      name:'getVideo',
      data:{
        text:this.data.memo.content
      }
      }).then(res=>{
        this.data.memo.sAudio=res.result.data.res.Audio
      })
  },
  bindMultiPickerChange: function (e) { //时间日期picker选择改变后，点击确定
    // console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      multiIndex: e.detail.value
    })
    const index = this.data.multiIndex; // 当前选择列的下标
    const year = this.data.multiArray[0][index[0]];
    const month = this.data.multiArray[1][index[1]];
    const day = this.data.multiArray[2][index[2]];
    const hour = this.data.multiArray[3][index[3]];
    const minute = this.data.multiArray[4][index[4]];
    // console.log(`${year}-${month}-${day} ${hour}:${minute}`);
    console.log(year + '-' + month + '-' + day + ' ' + hour + ':' + minute)
    this.setData({
      time: year + '-' + month + '-' + day + ' ' + hour + ':' + minute,
      year: year, //记录选择的年
      month: month, //记录选择的月
      day: day, //记录选择的日
      hour: hour, //记录选择的时
      minute: minute, //记录选择的分
    })
    //console.log(this.data.time);
  },
  bindMultiPickerColumnChange: function (e) { //监听picker的滚动事件

    // console.log('修改的列为', e.detail.column, '，值为', e.detail.value);

    let getCurrentDate = DatePickerUtil.getCurrentDate(); //获取现在时间
    let currentYear = parseInt(getCurrentDate.substring(0, 4));
    let currentMonth = parseInt(getCurrentDate.substring(5, 7));
    let currentDay = parseInt(getCurrentDate.substring(8, 10));
    let currentHour = parseInt(getCurrentDate.substring(11, 13));
    let currentMinute = parseInt(getCurrentDate.substring(14, 16));

    if (e.detail.column == 0) { //修改年份列

      let yearSelected = parseInt(this.data.multiArray[e.detail.column][e.detail.value]); //当前选择的年份

      this.setData({
        multiIndex: [0, 0, 0, 0, 0], //设置pickerIndex
        year: yearSelected //当前选择的年份
      });

      if (yearSelected == currentYear) { //当前选择的年份==当前年份
        var loadPickerData = DatePickerUtil.loadPickerData();

        this.setData({
          multiArray: loadPickerData, //picker数组赋值
          multiIndex: [0, 0, 0, 0, 0] //设置pickerIndex
        });

      } else { // 选择的年份！=当前年份

        // 处理月份
        let monthArr = DatePickerUtil.loadMonths(1, 12)
        // 处理日期
        let dayArr = DatePickerUtil.loadDays(currentYear, currentMonth, 1)
        // 处理hour
        let hourArr = DatePickerUtil.loadHours(0, 24);
        // 处理minute
        let minuteArr = DatePickerUtil.loadMinutes(0, 60)

        // 给每列赋值回去
        this.setData({
          ['multiArray[1]']: monthArr,
          ['multiArray[2]']: dayArr,
          ['multiArray[3]']: hourArr,
          ['multiArray[4]']: minuteArr
        });
      }
    }
    if (e.detail.column == 1) { //修改月份列
      let mon = parseInt(this.data.multiArray[e.detail.column][e.detail.value]); //当前选择的月份
      this.setData({
        month: mon // 记录当前列
      })

      if (mon == currentMonth) { //选择的月份==当前月份
        if (this.data.year == currentYear) {

          // 处理日期
          let dayArr = DatePickerUtil.loadDays(currentYear, mon, currentDay)
          // 处理hour
          let hourArr = DatePickerUtil.loadHours(currentHour, 24);
          // 处理minute
          let minuteArr = DatePickerUtil.loadMinutes(currentMinute, 60)

          this.setData({
            ['multiArray[2]']: dayArr,
            ['multiArray[3]']: hourArr,
            ['multiArray[4]']: minuteArr
          })
        } else {
          // 处理日期
          let dayArr = DatePickerUtil.loadDays(currentYear, mon, 1)
          // 处理hour
          let hourArr = DatePickerUtil.loadHours(0, 24);
          // 处理minute
          let minuteArr = DatePickerUtil.loadMinutes(0, 60)

          this.setData({
            ['multiArray[2]']: dayArr,
            ['multiArray[3]']: hourArr,
            ['multiArray[4]']: minuteArr
          });
        }
      } else { // 选择的月份！=当前月份
        // 处理日期
        let dayArr = DatePickerUtil.loadDays(currentYear, mon, 1) // 传入当前年份，当前选择的月份去计算日
        // 处理hour
        let hourArr = DatePickerUtil.loadHours(0, 24);
        // 处理minute
        let minuteArr = DatePickerUtil.loadMinutes(0, 60)

        this.setData({
          ['multiArray[2]']: dayArr,
          ['multiArray[3]']: hourArr,
          ['multiArray[4]']: minuteArr
        });
      }
    }
    if (e.detail.column == 2) { //修改日
      let dd = parseInt(this.data.multiArray[e.detail.column][e.detail.value]); //当前选择的日
      this.setData({
        day: dd
      })
      if (dd == currentDay) { //选择的日==当前日
        if (this.data.year == currentYear && this.data.month == currentMonth) { //选择的是今天

          // 处理hour
          let hourArr = DatePickerUtil.loadHours(currentHour, 24);
          // 处理minute
          let minuteArr = DatePickerUtil.loadMinutes(currentMinute, 60)

          this.setData({
            ['multiArray[3]']: hourArr,
            ['multiArray[4]']: minuteArr
          });

        } else { //选择的不是今天
          // 处理hour
          let hourArr = DatePickerUtil.loadHours(0, 24);
          // 处理minute
          let minuteArr = DatePickerUtil.loadMinutes(0, 60)

          this.setData({
            ['multiArray[3]']: hourArr,
            ['multiArray[4]']: minuteArr
          });
        }
      } else { // 选择的日！=当前日
        // 处理hour
        let hourArr = DatePickerUtil.loadHours(0, 24);
        // 处理minute
        let minuteArr = DatePickerUtil.loadMinutes(0, 60)

        this.setData({
          ['multiArray[3]']: hourArr,
          ['multiArray[4]']: minuteArr
        });
      }
    }
    if (e.detail.column == 3) { //修改时
      let hh = parseInt(this.data.multiArray[e.detail.column][e.detail.value]); //当前选择的时
      this.setData({
        hour: hh
      })
      if (hh == currentHour) { //选择的时==当前时
        if (this.data.year == currentYear && this.data.month == currentMonth && this.data.month == currentMonth) { // 选择的是今天

          // 处理minute
          let minuteArr = DatePickerUtil.loadMinutes(currentMinute, 60)
          this.setData({
            ['multiArray[4]']: minuteArr
          });
        } else { // 选择的不是今天

          // 处理minute
          let minuteArr = DatePickerUtil.loadMinutes(0, 60)
          this.setData({
            ['multiArray[4]']: minuteArr
          });
        }
      } else { //选择的时！=当前时
        // 处理minute
        let minuteArr = DatePickerUtil.loadMinutes(0, 60)
        this.setData({
          ['multiArray[4]']: minuteArr
        });
      }
    }
    var data = {
      multiArray: this.data.multiArray,
      multiIndex: this.data.multiIndex
    };
    data.multiIndex[e.detail.column] = e.detail.value; //将值赋回去

    this.setData(data); //将值赋回去
  }
})