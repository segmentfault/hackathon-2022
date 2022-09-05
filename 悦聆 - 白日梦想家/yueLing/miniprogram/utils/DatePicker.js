const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}
function getCurrentDate(datetime){// 获取当前时间
  let date;
  if(datetime){
    date = new Date(datetime); 
  }else {
    date = new Date(); 
  } 
  let currentYears=date.getFullYear();
  let currentMonths=date.getMonth()+1;
  let currentDay=date.getDate();
  let currentHours=date.getHours();
  let currentMinute=date.getMinutes();   
  
  var mm=[currentMonths].map(formatNumber)
  var dd=[currentDay].map(formatNumber)
  var hh=[currentHours].map(formatNumber)
  var min=[currentMinute].map(formatNumber) 
   
  return currentYears+'-'+mm+'-'+dd+' '+hh+':'+min;
}

function GetMultiIndex(){ //一点开picker的选中设置  
 
  return [0,0,0,0,0]; //现在全部列，默认选第一个选项,其实这一步多余，可以直接在data里面定义 
} 
 
function loadYears(startYear,endYear){//获取年份 
  /**
   * params参数
   * startYear 当前年份
   * endYear 循环结束月份 ，比如 5 年内 newDate().getFullYear() + 4，客户说只能预约两年内 
   * return 年份数组
  */
  let years=[];
  for (let i = startYear; i <= endYear; i++) {
    years.push("" + i);
  } 
  return years;//返回年份数组 
}


function loadMonths(startMonth,endMonth){//获取月份
   /**
   * params参数
   * startMonth 当前月份
   * endMonth 循环结束月份，一般为 12个月
   * return 月份数组
  */ 
  let months=[];
  for (let i = startMonth; i <= endMonth; i++) {
    if (i < 10) {
      i = "0" + i;
    }
    months.push("" + i);
  } 
  return months;//返回月份数组 
}


function loadDays(currentYears,selectedMonths,startDay){ //获取日期  
  /**
   * params参数
   * currentYears 当前年份
   * selectedMonths 当前选择的月份
   * startDay   循环开始日 一般为1号， 希望从当前月份开始 ，startDay=currentDay
   * return 日期数组
  */
    let days=[];
    if (selectedMonths == 1 || selectedMonths == 3 || selectedMonths == 5 || selectedMonths == 7 || selectedMonths == 8 || selectedMonths == 10 || selectedMonths == 12) { //判断31天的月份，可以用正则简化
      for (let i = startDay; i <= 31; i++) {
        if (i < 10) {
          i = "0" + i;
        }
        days.push("" + i);
      } 
    } else if (selectedMonths == 4 || selectedMonths == 6 || selectedMonths == 9 || selectedMonths == 11) { //判断30天的月份
      for (let i = startDay; i <= 30; i++) {
        if (i < 10) {
          i = "0" + i;
        }
        days.push("" + i);
      } 
    } else if (selectedMonths == 2) { //判断2月份天数
      let year = currentYears 
      if (((year % 400 == 0) || (year % 100 != 0)) && (year % 4 == 0)) {//闰年
        for (let i = startDay; i <= 29; i++) {
          if (i < 10) {
            i = "0" + i;
          }
          days.push("" + i);
        }
        
      } else {//不是闰年
        for (let i = startDay; i <= 28; i++) {
          if (i < 10) {
            i = "0" + i;
          }
          days.push("" + i);
        } 
      }
    }
    return days;//返回日期数组
}


function loadHours(startHour,endHour){//获取小时
   /**
   * params参数
   * startHour 循环开始小时 一般为 0点， 希望从当前小时开始 startHour=currentHours
   * endHour 循环当前小时 ,一般为24个小时
   * return 小时数组
  */ 
  let hours=[];
   for (let i = startHour; i < endHour ; i++) {
    // if (i < 10) {//看需求要不要加在前面+‘0’
    //   i = "0" + i;
    // }
    hours.push("" + i);
  }
  return hours;//返回小时数组 
}


function loadMinutes(startMinute,endMinute){//获取分钟
  /**
   * params参数
   * startMinute 循环开始分钟 一般为 0 开始，从当前分钟开始 startMinute=currentMinutes
   * endMinute 循环当前秒 ，一般为60分钟
   * return 分钟数组
  */  
 let minutes=[];
  for (let i = startMinute; i < endMinute ; i++) {
   if (i < 10) {
     i = "0" + i;
   }
   minutes.push("" + i);
 }
 return minutes;//返回分钟数组 
}

//我没有用到秒，可以参考分钟的写法

function loadPickerData(){ //将Picker初始数据，开始时间设置为当前时间
  let date1 = new Date();   
  let currentYears=date1.getFullYear();
  let currentMonths=date1.getMonth()+1;
  let currentDay=date1.getDate();
  let currentHours=date1.getHours();
  let currentMinute=date1.getMinutes();  
  
  // 下面调用 自定义方法 

  //获取年份  loadYears(startYear,endYear)  
  //获取月份  loadMonths(startMonth,endMonth)
  //获取日期  loadDays(currentYears,currentMonths,startDay)
  //获取小时  loadHours(startHour,endHour)
  //获取分钟  loadMinutes(startMinute,endMinute)
  
  let years = loadYears(currentYears,date1.getFullYear() + 1)  //客户说只能预约两年内,+1
  let months = loadMonths(currentMonths,12)   
  let days = loadDays(currentYears,currentMonths,currentDay)  
  let hours = loadHours(currentHours,24)  
  let minutes =  loadMinutes(currentMinute,60)
  
  return [years, months, days, hours, minutes]
}
 
//导出
module.exports = {
  loadPickerData:loadPickerData,
  getCurrentDate:getCurrentDate,
  GetMultiIndex:GetMultiIndex,
  loadYears:loadYears,
  loadMonths:loadMonths,
  loadDays:loadDays,
  loadHours:loadHours,
  loadMinutes:loadMinutes
}