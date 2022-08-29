
/**
* @description 获取templatefile文件夹的地址，如果插件是在线模式，地址是http的；如果插件是离线模式，地址是本地磁盘路径
* @return {string} 返回Templatefile文件夹的地址
* @author peter 2022/08/24 
*/
function GetTemplateFileUrl()
{
    var url=GetUrlPath().replace("ui",'templatefile/') ;
    if (url.startsWith("file:///"))
    {
        url = navigator.userAgent.indexOf("Linux") > -1?url.substr("file://".length):url.substr("file:///".length);
    }
    return url;
}

/**
* @description 获取当前页面路径
* @return {string} 返回当前页面路径
* @author peter 2022/08/24 
*/
function GetUrlPath() {
    let e = document.location.toString()
    return -1 != (e = decodeURI(e)).indexOf("/") && (e = e.substring(0, e.lastIndexOf("/"))), e
}


/**
* @description 弹出消息框
* @param {string} msg 消息的内容
* @return {void}}无返回值
* @author peter 2022/08/24 
*/
function ShowMessage(msg)
{
    $.messager.alert('提示',msg);
}

/**
* @description 在指定字符串后面追加指定数量的空格
* @param {string}text 字符串
* @param {number}count 空格的数量
* @return {string} 返回补齐空格后的字符串
* @author peter 2022/08/24 
*/
function FormatStrWitdhSpaceStr(text,count)
{
    var result="";
    if(text.length<count)
    {
        for(var i=0;i<count-text.length;i++)
        {
            result+=" ";
        }
    }
    return text+result;
}

/**
* @description 数字转人民币大写
* @param {number}num 要转换的数字
* @return {string}转人民币大写后的字符串
* @author peter 2022/08/24 
*/
function NumToChinese(num)
{
    let changeNum = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖']; //changeNum[0] = "零"
    let unit = ["", "拾", "佰", "仟", "万"];
    num = parseInt(num);
    let getWan = (temp) => {
        let strArr = temp.toString().split("").reverse();
        let newNum = "";
        for (var i = 0; i < strArr.length; i++) {
            newNum = (i == 0 && strArr[i] == 0 ? "" : (i > 0 && strArr[i] == 0 && strArr[i - 1] == 0 ? "" : changeNum[strArr[i]] + (strArr[i] == 0 ? unit[0] : unit[i]))) + newNum;
        }
        return newNum;
    }
    let overWan = Math.floor(num / 10000);
    let noWan = num % 10000;
    if (noWan.toString().length < 4) noWan = "0" + noWan;
    return overWan ? getWan(overWan) + "万" + getWan(noWan) : getWan(num);
}

/**
* @description 日期转中文年月日
* @param {string}date 日期字符串
* @example 2022-7-30转换后为2022年7月30日
* @return {string} 转换后的字符串
* @author peter 2022/08/24 
*/
function DateToChinesStr(date)
{
   var da= new Date(date);
   return da.getFullYear()+'年'+(parseInt(da.getMonth())+1)+'月' +da.getDate()+'日';

}

/**
* @description 中文年月日的日期转日期
* @param {string}str 日期字符串
* @example 2022年7月30日转换后为2022-7-30
* @return {date} 返回转换后的日期
* @author peter 2022/08/24 
*/
function ChinesStrToDate(str)
{
    str=str.replace('年',"-").replace('月',"-").replace('日',"");
    return new Date(str);
}


/**
* @description 实现日期格式化
* @param {string}fmt 格式化字符串
* @example new Date().format("yyyy-MM-dd")
* @return {string} 返回转换后的日期
* @author peter 2022/08/24 
*/
Date.prototype.format = function(fmt) { 
    var o = { 
       "M+" : this.getMonth()+1,                 //月份 
       "d+" : this.getDate(),                    //日 
       "h+" : this.getHours(),                   //小时 
       "m+" : this.getMinutes(),                 //分 
       "s+" : this.getSeconds(),                 //秒 
       "q+" : Math.floor((this.getMonth()+3)/3), //季度 
       "S"  : this.getMilliseconds()             //毫秒 
   }; 
   if(/(y+)/.test(fmt)) {
           fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
   }
    for(var k in o) {
       if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
   return fmt; 
}

/**
* @description 格式化字符串
* @param {string[]}args 格式化字符串
* @return {string} 返回字符串
* @author peter 2022/08/24 
*/
String.prototype.format = function(args) {
    var result = this;
    if (arguments.length > 0) {
        if (arguments.length == 1 && typeof (args) == "object") {
            for (var key in args) {
                if(args[key]!=undefined){
                    var reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }
        else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                    var reg= new RegExp("({)" + i + "(})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
}

/**
* @description 获取json对象长度
* @param {object}jsonData json对象
* @return {number} 返回json对象长度
* @author peter 2022/08/24 
*/
function GetJsonLength(jsonData) {
    var length=0;
    for(var ever in jsonData) {
        length++;
    }
    return length;
}

/**
* @description 将程序挂起指定时间
* @param {number}n :要挂起的时间，单位是毫秒
* @return {void} 无返回值
* @author peter 2022/08/24 
*/
function sleep(n) {
    var start = new Date().getTime();
    while (true) {
        if (new Date().getTime() - start > n) {
            break;
        }
    }
}
/**
* @description 删除左右两端的空格
* @param {string}str 要删除的字符串
* @return {string} 返回删除空格后的字符串
* @author peter 2022/08/24 
*/
function trim(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

/**
* @description 获取数组中指定元素的索引
* @param {object}val 数组中的元素
* @return {number} 返回指定元素的index
* @author peter 2022/08/24 
*/
Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
    if (this[i] == val) return i;
    }
    return -1;
    };

/**
* @description 移除数组中的指定元素
* @param {object}val 数组中的元素
* @return {array} 返回新的数组
* @author peter 2022/08/24 
*/
Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
    this.splice(index, 1);
    }
};
