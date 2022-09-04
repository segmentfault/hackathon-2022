let urls = [];
chrome.contextMenus.create({
	title: '快捷翻译：%s', // %s表示选中的文字
	contexts: ['selection'], // 只有当选中文字时才会出现此右键菜单
	onclick: function (params) {
		chrome.tabs.create({
			url: 'https://fanyi.baidu.com/translate#zh/en/' + encodeURI(params.selectionText)
		});
	}
});
chrome.contextMenus.create({
	title: '暂存网页',
	onclick: function (params,tab) {
		chrome.storage.sync.get(['urls'], function (result) {
			urls = result.urls || [];
		});
		let flag = urls.findIndex((item) => {
			return item.url == tab.url;
		});

		if (flag == -1) {
			urls.push({
				title: tab.title,
				url: tab.url
			});
			chrome.storage.sync.set({
				urls: urls
			});
		}else{
			alert("你已经收藏过该网页");
		}
		
	}
});