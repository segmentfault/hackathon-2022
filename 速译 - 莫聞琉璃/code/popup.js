//test

$(document).ready(function () {

	$(document).keydown(function(e) {
		if (e.keyCode == 13) {
			$('#queryWord').click();
		}
  	});
	
	// 载入数据
	chrome.storage.sync.get(['allDic', 'defDic', 'openType', 'syncHistory', 'tempHistory'], function(data) {
		// 默认词典
		updateSearchTextPlaceHolder();
		
		for (var i in data.allDic) {
			if (data.defDic == data.allDic[i].id) {
				$('#allDic').append('<label class="btn btn-primary active"><input type="radio" name="defDic" value="' + data.allDic[i].id + '" autocomplete="off" checked>' + data.allDic[i].name + '</label>');
			} else {
				$('#allDic').append('<label class="btn btn-primary"><input type="radio" name="defDic" value="' + data.allDic[i].id + '" autocomplete="off">' + data.allDic[i].name + '</label>');
			}
		}
		
		// 切换词典按钮添加事件
		allDicAddClick();

		// 标签打开方式
		if (data.openType) {
			if (data.openType == "prev") {
				$('#openType1').attr("checked", true);
			} else if (data.openType == "next") {
				$('#openType2').attr("checked", true);
			}
		} else {
			// 异常没有数据的处理
			$('#openType2').attr("checked", true);
		}
		
		// 保存历史
		if (data.syncHistory) {
			$('#syncHistory').attr("checked", true);
		} else {
			$('#syncHistory').attr("checked", false);
		}
		
		// 临时查询数据
		var showCount = 0;
		for (var i in data.tempHistory) {
			var tempWord = data.tempHistory[i].word;
			var dstWord = data.tempHistory[i].dst;
			console.log('tempWord:' + tempWord);
			
			++showCount;
			if (showCount > 6) {
				break;
			}
			
			if (showCount % 2 == 0) {
				$('#tempHistory tbody').append('<tr><th scope="row">' + showCount + '</th><td class="linkWord"><a href="#">' + tempWord + '</a></td><td class="queryword' + showCount + '">' + dstWord + '</td></tr>');
			} else {
				$('#tempHistory tbody').append('<tr class="active"><th scope="row">' + showCount + '</th><td class="linkWord"><a href="#">' + tempWord + '</a></td><td class="queryword' + showCount + '">' + dstWord + '</td></tr>');
			}
		}
		
		// 添加事件
		addEventListener();
	});

	// 查词
	$('#queryWord').click(function() {
		
		var word = $('#word').val();
		
		console.log('word:' + word);
		
		var port = chrome.extension.connect({name: "ContentScripts"});
		port.postMessage({mtype: "queryWord", word: word});
		port.onMessage.addListener(function(msg) {
			// ...
		});
	});
	
	
	
	// 打开历史
	$('#openHistory').click(function() {
		chrome.tabs.create({url : "http://47.104.105.151/dic/public/index/index.html"}, function(tab) {
			console.log('createTab:' + tab.id);
		});
	});
	
	// 打开更新地址
	$('#openUpdate').click(function() {
		chrome.tabs.create({url : "https://gitee.com/hZiegler/QueryWord"}, function(tab) {
			console.log('createTab:' + tab.id);
		});
	});

	// 标签打开方式
	$('input[name="openType"]').click(function() {
		var openType = $('input[name="openType"]:radio:checked').val();
		chrome.storage.sync.set({'openType': openType}, function(){
			console.log('sync openType:' + openType);
		});
	});
	
	// 保存历史方式
	$('#syncHistory').click(function() {
		var syncHistory = $('#syncHistory').is(':checked');
		chrome.storage.sync.set({'syncHistory': syncHistory}, function(){
			console.log('sync syncHistory:' + syncHistory);
		});
	});
	
	$('#recover').click(function() {
		var port = chrome.extension.connect({name: "ContentScripts"});
		port.postMessage({mtype: "recover"});
		port.onMessage.addListener(function(msg) {
			// ...
		});
		
		// var gTable = new Array();
		// gTable.push({id: 'LONGMAN', name: '谷歌', url: 'https://www.ldoceonline.com/dictionary/{0}', enable: true});
		// gTable.push({id: 'MERRIAN', name: '韦氏', url: 'https://www.merriam-webster.com/dictionary/{0}', enable: true});
		// gTable.push({id: 'JINSHAN', name: '金山', url: 'http://www.iciba.com/{0}', enable: true});
		// gTable.push({id: 'YOUDAO', name: '有道', url: 'http://dict.youdao.com/w/{0}', enable: true});
		
		// var allDataName = {'allDic': gTable, 'defDic': 'LONGMAN', 'openType': 'prev', 'syncHistory': true, 'tempHistory': new Array()};
		// chrome.storage.sync.set(allDataName, function() {
		// 	console.log('init storage');
		// });
	});
	
});

// 切换词典按钮添加事件
// jQuery使用append添加的元素事件无效的问题
function allDicAddClick() {
	// 修改默认词典
	$('#allDic').on("click", "input", function() {
		var defDic = $('input[name="defDic"]:radio:checked').val();
		console.log('change defDic:' + defDic);
		chrome.storage.sync.set({'defDic': defDic}, function(){
			updateSearchTextPlaceHolder();
		});
	});
}

// 添加链接点击事件
function addEventListener() {
	$('.linkWord').on("click", "a", function() {
		var word = $(this).html();
		console.log(word);
		var port = chrome.extension.connect({name: "ContentScripts"});
		port.postMessage({mtype: "queryWord", word: word});
		port.onMessage.addListener(function(msg) {
			// ...
		});
	});
}

function updateSearchTextPlaceHolder() {
	chrome.storage.sync.get(['allDic', 'defDic'], function(data) {
		var dic = getDic(data.allDic, data.defDic);
		$('#word').attr('placeholder', dic.name);
	});
}

//////////////////////////////////////////////////////
// 功能函数
//////////////////////////////////////////////////////
// 通过id查询url
function getDic(allDic, id) {
	for (var i in allDic) {
		if (allDic[i].id == id) {
			return allDic[i];
		}
	}
	
	return null;
}