"use strict";
// 查词
var gTable = new Array();

gTable.push({id: 'GOOGLE', name: '谷歌', url: 'https://translate.google.cn/{0}', enable: true});
gTable.push({id: 'BAIDU', name: '百度', url: 'https://fanyi.baidu.com/{0}', enable: true});
gTable.push({id: 'JINSHAN', name: '金山', url: 'http://www.iciba.com/{0}', enable: true});
gTable.push({id: 'YOUDAO', name: '有道', url: 'http://dict.youdao.com/w/{0}', enable: true});
	
$(document).ready(function () {
	//console.log(gTable);
	
	// chrome.contextMenus.removeAll(function() {
		// console.log('contextMenus are removeAll.');
	// });
	
	// 添加右键菜单
	for (var i in gTable) {
		chrome.contextMenus.create({
			type: 'normal',
			title: gTable[i].name,
			id: gTable[i].id,
			contexts: ['all'],
			onclick: genericOnClick
		}, function () {
			console.log('contextMenus are create.id:' + gTable[i].id + ',title:' + gTable[i].name);
		});
	}

	// 设置初始数据
	// var allDataName = {'allDic': gTable, 'defDic': 'LONGMAN', 'openType': 'prev', 'syncHistory': true, 'tempHistory': new Array()};
	// chrome.storage.sync.set(allDataName, function() {
	// 	console.log('init storage');
	// });
	
	// 清楚多个数据
	// chrome.storage.sync.remove('syncHistory', function() {
		
	// });
	
	// 清空所有数据
	// chrome.storage.sync.clear(function() {
		
	// });
	
	// chrome.storage.sync.set({'tempHistory': ''}, function() {
	// 	console.log('init storage complete.');
	// });
	
	// 设置初始数据
	chrome.storage.sync.get(['allDic', 'defDic', 'openType', 'syncHistory', 'tempHistory'], function(data) {
		console.log('old storage data:');
		console.log(data);
		
		var allDataName = {};
		
		if (isEmpty(data.allDic)) {
			console.log('init allDic');
			allDataName['allDic'] = gTable;
		}
		
		if (isEmpty(data.defDic)) {
			console.log('init defDic');
			allDataName['defDic'] = 'LONGMAN';
		}
		
		if (isEmpty(data.openType)) {
			console.log('init openType');
			allDataName['openType'] = 'prev';
		}
		
		if (isEmpty(data.syncHistory)) {
			console.log('init syncHistory');
			allDataName['syncHistory'] = true;
		}
		
		if (isEmpty(data.tempHistory) || !isArray(data.tempHistory)) {
			console.log('init tempHistory');
			allDataName['tempHistory'] = new Array();
		}
		
		if (!isEmptyObj(allDataName)) {
			chrome.storage.sync.set(allDataName, function() {
				console.log('init storage complete.');
			});
		}
	});
});

// storage监听
chrome.storage.onChanged.addListener(function(changes, namespace) {
	for (var key in changes) {
		var storageChange = changes[key];
		console.log('Storage key "%s" in namespace "%s" changed. ' +
			'Old value was "%o", new value is "%o".',
			key,
			namespace,
			storageChange.oldValue,
			storageChange.newValue);
	}
});

// 消息监听
chrome.extension.onConnect.addListener(function(port) {
	port.onMessage.addListener(function(msg) {
		//debugLog("mtype:" + msg.mtype);
		
		if ('queryWord' == msg.mtype) {
			var word = msg.word.trim();
			
			chrome.storage.sync.get(['allDic', 'defDic'], function(data) {
				var url = getUrlById(data.allDic, data.defDic);
				if (null == url) {
					console.log('null == url.data.defDic:' + data.defDic);
					return ;
				}
				
				queryWord(word, url, -1);
			});
		} else if ('recover' == msg.mtype) {
			var allDataName = {'allDic': gTable, 'defDic': 'LONGMAN', 'openType': 'prev', 'syncHistory': true, 'tempHistory': new Array()};
			chrome.storage.sync.set(allDataName, function() {
				console.log('init storage');
			});
		}
	});
});

// 菜单点击函数
function genericOnClick(info, tab) {
	//debugLog("selectionText:" + info.selectionText + ",menuItemId:" + info.menuItemId)
	
	if ('undefined' == typeof info.selectionText) {
		console.log('undefined == typeof info.selectionText');
		return ;
	} else if (info.selectionText == null) {
		console.log('info.selectionText == null');
		return ;
	} else {
		if (info.selectionText == "") {
			console.log('info.selectionText == ""');
			return ;
		}
	}
	
	var word = info.selectionText.trim();
	
	chrome.storage.sync.get(['allDic', 'defDic'], function(data) {
		var url = getUrlById(data.allDic, info.menuItemId);
		if (null == url) {
			console.log('null == url' + info.menuItemId);
			return ;
		}

		queryWord(word, url, tab.index);
	});
}

function queryWord(word, url, tabIndex) {
	if (word.length == 0) {
		console.log('word is empty.');
		return ;
	}
	
	var urlFormat = String.format(url, word);
	
	chrome.storage.sync.get(['openType', 'syncHistory', 'tempHistory'], function(data) {

		if (data.openType) {
			if (tabIndex > 0) {
				if (data.openType == "prev") {
					chrome.tabs.create( {index: tabIndex, url : urlFormat}, function(tab) {
						console.log('createTab:' + tab.id + ", url:" + tab.url);
					});
				} else if (data.openType == "next") {
					chrome.tabs.create( {index: tabIndex + 1, url : urlFormat}, function(tab) {
						console.log('createTab:' + tab.id + ", url:" + tab.url);
					});
				}
			} else {
				chrome.tabs.create( {url : urlFormat}, function(tab) {
					console.log('createTab:' + tab.id + ", url:" + tab.url);
				});
			}
		} else {
			// 异常没有数据的处理
			//console.log('exception: openType is undefined.');
			
			chrome.tabs.create( {index: tabIndex + 1, url : urlFormat}, function(tab) {
				console.log('createTab:' + tab.id + ", url:" + tab.url);
			});
		}

		// 同步历史
		if (data.syncHistory) {
			if (data.syncHistory == true) {
				// 添加查询记录
				var recordUrl = "http://47.104.105.151/dic/public/index/search/text/" + word;
				$.post(recordUrl);
			}
		} else {
			console.log('exception: syncHistory is undefined.');
		}
		
		// 翻译
		queryWordByAPI(word, function(result) {
			var dstWord = "";
			for (var i in result.trans_result) {
				dstWord += result.trans_result[i].dst;
				dstWord += ";";
			}
			
			console.log('queryWordByAPI word:%s, result:%o', word, result);

			addHistory({'word': word, 'dst': dstWord});
		});
	});
}

// 添加历史记录
function addHistory(value) {
	chrome.storage.sync.get(['tempHistory'], function(data) {
		var tempHistory = data.tempHistory;

		// 存在删除
		var bExist = false;
		for (var i in tempHistory) {
			if (tempHistory[i].word == value.word) {
				bExist = true;
				tempHistory.splice(i, 1)
				break;
			}
		}
		
		// 新查询的添加末尾
		tempHistory.unshift(value);

		if (tempHistory.length >= 100) {
			tempHistory.pop();
			console.log('tempHistory pop 1');
		}

		chrome.storage.sync.set({'tempHistory': tempHistory}, function() {
			//console.log('set tempHistory: %o', tempHistory);
		});
	});
}

//////////////////////////////////////////////////////
// 功能函数
//////////////////////////////////////////////////////
// 通过id查询url
function getUrlById(allDic, id) {
	for (var i in allDic) {
		if (allDic[i].id == id) {
			return allDic[i].url;
		}
	}
	
	return null;
}

////////////////////////////////////////////////////////////////
// 工具函数
////////////////////////////////////////////////////////////////
//判断字符是否为空的方法
function isEmpty(obj){
	// if(typeof obj == "undefined" || obj == null || obj == ""){
    if(typeof obj == "undefined" || obj == null){
        return true;
    }else{
        return false;
    }
}

function isEmptyObj(obj){
    for(var key in obj) {
		return false;
    }
	
    return true;
}

function isArray(obj){
    if (obj instanceof Array) {
    	return true;
    } else {
    	return false;
    }
}



var g_logCount = 0;
function debugLog(log) {
	var nowTime = new Date().Format("yyyy-MM-dd hh:mm:ss");
	console.log('[' + nowTime + '][' + (++g_logCount) + ']' + log);
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
// 例子： 
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

// 参数格式化 String.format("look at {0}", jack)
String.format = function() {
    if (arguments.length == 0)
        return null;
    var str = arguments[0];
    for ( var i = 1; i < arguments.length; i++) {
        var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
        str = str.replace(re, arguments[i]);
    }
    return str;
};

// 调用百度api翻译单词
function queryWordByAPI(query, callback) {
	var appid = '20190903000331677';
	var key = 'JQZe1KhuIIyG7XUFEhJO';
	var salt = (new Date).getTime();
	//var query = 'apple';
	// 多个query可以用\n连接  如 query='apple\norange\nbanana\npear'
	var from = 'en';
	var to = 'zh';
	var str1 = appid + query + salt +key;
	var sign = MD5(str1);
	
	var url = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=" + query + "&appid=" + appid + "&salt=" + salt + "&from=" + from + "&to=" + to + "&sign=" + sign;
	
	$.get(url, function (result) {
		//console.log(result);
		callback(result);
	});
}