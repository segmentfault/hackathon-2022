/**
 * 日期工具类选用day.js
 * 文档：https://dayjs.gitee.io/docs/zh-CN/parse/parse
 * 
 * 
 * 简单用法：
 * 	dayjs('2018-08-08') // parse

	dayjs().format('{YYYY} MM-DDTHH:mm:ss SSS [Z] A') // display

	dayjs().set('month', 3).month() // get & set

	dayjs().add(1, 'year') // manipulate

	dayjs().isBefore(dayjs()) // query
 */
!function(t,e){"object"==typeof exports&&"undefined"!=typeof module?module.exports=e():"function"==typeof define&&define.amd?define(e):t.dayjs=e()}(this,function(){"use strict";const t=60,e=60*t,s=24*e,r=7*s,n=(t,e,s)=>!t||t.length>=e?t:`${Array(e+1-t.length).join(s)}${t}`;class i{constructor(t){this.utc=!1;const e=this.parseConfig(t);this.date=new Date(e),this.timeZone=this.date.getTimezoneOffset()/60,this.timeZoneString=n(String(-1*this.timeZone).replace(/^(.)?(\d)/,"$10$200"),5,"+"),this.mYear=this.date.getFullYear(),this.mMonth=this.date.getMonth(),this.mDay=this.date.getDate(),this.mWeek=this.date.getDay(),this.mHour=this.date.getHours(),this.mMinute=this.date.getMinutes(),this.mSecond=this.date.getSeconds()}parseConfig(t){if(!t)return new Date;if(t instanceof Date)return t;if(/^(\d){8}$/.test(t)){return this.utc=!0,`${t.substr(0,4)}-${t.substr(4,2)}-${t.substr(6,2)}`}return t}year(){return this.mYear}month(){return this.mMonth}unix(){const t=this.utc?60*this.timeZone*60*1e3:0;return Math.floor((this.date.getTime()+t)/1e3)}toString(){return this.date.toUTCString()}startOf(t){switch(t){case"year":return new i(new Date(this.year(),0,1));case"month":return new i(new Date(this.year(),this.month(),1));default:return this}}add(n,a){let h;switch(a){case"m":case"minutes":h=t;break;case"h":case"hours":h=e;break;case"d":case"days":h=s;break;case"w":case"weeks":h=r;break;default:h=1}const u=this.unix()+n*h;return new i(1e3*u)}subtract(t,e){return this.add(-1*t,e)}format(t="YYYY-MM-DDTHH:mm:ssZ"){const e=["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];return t.replace(/Y{2,4}|M{1,2}|D{1,2}|d{1,4}|H{1,2}|m{1,2}|s{1,2}|Z{1,2}/g,t=>{switch(t){case"YY":return String(this.mYear).slice(-2);case"YYYY":return String(this.mYear);case"M":return String(this.mMonth+1);case"MM":return n(String(this.mMonth+1),2,"0");case"D":return String(this.mDay);case"DD":return n(String(this.mDay),2,"0");case"d":return String(this.mWeek);case"dddd":return e[this.mWeek];case"H":return String(this.mHour);case"HH":return n(String(this.mHour),2,"0");case"m":return String(this.mMinute);case"mm":return n(String(this.mMinute),2,"0");case"s":return String(this.mSecond);case"ss":return n(String(this.mSecond),2,"0");case"Z":return`${this.timeZoneString.slice(0,-2)}:00`;case"ZZ":return this.timeZoneString;default:return t}})}}return t=>new i(t)});