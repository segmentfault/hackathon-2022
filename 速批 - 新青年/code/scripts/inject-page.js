
//获取所有的元素
function getAllLinks(tag){
    var links = [].slice.apply(document.getElementsByTagName(tag));
    var oLinks = links.map(v => {
        var clink = '';
        switch(tag){
            case 'img' : clink = v.src; break;
            case 'video' : clink = v.src; break;
            case 'a' : clink = v.href; break;
            default: clink = v.src; break;
        }
        return clink;
    });
    return oLinks;
}



// 监听popup页面发送的请求
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    switch(request.type){
        case 'img':
            sendResponse(getAllLinks('img'));
            break;
        case 'video':
            sendResponse(getAllLinks('video'));
            break;
        case 'link':
            sendResponse(getAllLinks('a'));
            break;
        default:
            sendResponse(getAllLinks('img'));
            break;
    }
});





/*// 监听长连接
chrome.runtime.onConnect.addListener(function(port) {
    console.log(port);
    if(port.name == 'test-connect') {
        port.onMessage.addListener(function(msg) {
            console.log('收到长连接消息：', msg);
            if(msg.question == '你是谁啊？') port.postMessage({answer: '我是隔壁老王！'});
        });
    }
});
*/