
class Popup{

    constructor() {

        // 定义图片数组
        this.imgArr = [];
        this.statuAll = false;
        this.imgLinks = [];
        this.type = 'img'; // img video link
        this.tabId = 0;


        //注入脚本
        this.injectScript();

    }

    // 向页面植入脚本
    injectScript(){
        
        let self = this;

        window.addEventListener('load',() => {
            
            //向当前窗口tab页面注入脚本
            chrome.windows.getCurrent(function (currentWindow) {
                chrome.tabs.query({active: true, windowId: currentWindow.id},function(activeTabs) {
                  self.tabId = activeTabs[0].id;

                  chrome.tabs.executeScript(self.tabId, {file: 'scripts/inject-page.js', allFrames: false},()=>{
                    self.addListenRequest();
                  });
                  
                });
            });

            //绑定资源状态切换点击事件
            this.changeType();
        })

    }
    
    // 添加监听
    addListenRequest() {
        chrome.tabs.sendMessage(this.tabId, {type: this.type},(res)=>{
            
            this.imgArr = res;
            //过滤不和规定的视频资源
            this.filterVideos();
            this.render();
        });
    }

    //过滤
    filterVideos(){
        if(this.type == 'video'){
            this.imgArr = this.imgArr.filter(v=>v.match(/[^\.]*\.mp4$/g));
        }
    }

    // 添加长连接监听inject脚本 * 改用短链接onMesage方式
    connectInject(){
        var port = chrome.tabs.connect(this.tabId, {type: this.type});
        port.postMessage({question: '你是谁啊？'});
        port.onMessage.addListener(function(msg) {
            alert('收到消息：'+msg.answer);
            if(msg.answer && msg.answer.startsWith('我是'))
            {
                port.postMessage({question: '哦，原来是你啊！'});
            }
        });
    }

    // 渲染列表结构
    render() {

        // 注入popup页面
        $('#img-wrap').html(this.getHTML());

        //初始化状态
        this.initStatus();

        // 绑定事件
        this.eventBind();
    }


    // 渲染图片、视频、链接
    getHTML(){
        var str = '';
        var info = {name: '', imgSrc: ''};

        switch(this.type){
            case 'img': info.name = '图片';info.imgSrc = ''; break;
            case 'video': info.name = '视频';info.imgSrc = './images/video.jpg'; break;
            case 'link': info.name = '链接';info.imgSrc = './images/link.png'; break;
            default: info.name = '图片';info.imgSrc = ''; break;
        }

        for(var i=0;i<this.imgArr.length;i++){
            str += `<dl class="img-item" data-source="${this.imgArr[i]}"><dt class="img-item-dt"><img src="${info.imgSrc ? info.imgSrc: this.imgArr[i]}" class="dt-img"/></dt><dd class="img-item-dd"><input type="checkbox" name="img-check" value="" class="checkBoxItem"/><span class="item-name">${info.name+'_'+(i+1)}</span></dd></dl>`;
        }

        return str;
    }



    // 初始化状态
    initStatus(){
        
        this.statuAll = false;

        $('#checkAll').get(0).checked = this.statuAll;

        $('.checkBoxItem').each(function(){
            $(this).get(0).checked = self.statuAll;
        });

        //初始化资源提示
        this.imgArr.length == 0 ? $('.label-extension-info').show() : $('.label-extension-info').hide();

        //注销事件 **** 非常重要  --- 不然会连续触发全选多次 --- 在这里浪费太多时间了 0.0
         $('#checkAll').off('change');
         $('.img-item').off('click');
         $('.btn-down').off('click');
         $('.btn-links').off('click');
    }

    // 事件绑定
    eventBind(){

        let self = this;

        // 单击全选按钮
        $('#checkAll').on('change',function(e){
            
            //$('#checkAll').get().checked = self.statuAll;
            self.statuAll = !self.statuAll;
            $('.checkBoxItem').each(function(){
                $(this).get(0).checked = self.statuAll;
            });

            e.stopPropagation();
            e.preventDefault();
        })

        //绑定单个图片元素
        $('.img-item').on('click',function(){
            let cstatus = $(this).find('.checkBoxItem').eq(0).get(0).checked;
            //console.log();
            $(this).find('.checkBoxItem').get(0).checked = !cstatus;

            //判断是否取消全选
            //self.checkCancelAllSelect();
            if(cstatus){
                self.statuAll = false;
                $('#checkAll').get(0).checked = self.statuAll;
            }

        });

        //绑定按钮下载
        $('.btn-down').on('click',function(){
            self.loadImgUrl();
            self.download();
        });

        //绑定复制链接按钮
        $('.btn-links').on('click',function(){
            self.loadImgUrl();
            self.copyLinks();
        });
    }


    // 点击单选框
    changeType(){

        let self = this;

        /* 切换资源 */
        $('input[name=optionsRadios]').click(function(){
            //初始化状态
            //self.statuAll = false;
           
            self.type = $(this).val();
            self.addListenRequest();
        })
    }


    // source array
    loadImgUrl(){
        let self = this;
        $('.dt-img').each(function(){
            self.imgLinks.push(this.src);
        });
    }

    //下载
    download(){
        
        //判断是否可以下载
        let info = {name: '', suffix: ''};

        switch(this.type){
            case 'img': info.name = '图片'; info.suffix = 'png'; break;
            case 'video': info.name = '视频'; info.suffix = 'mp4'; break;
            case 'link': alert('该类型无法下载'); return; break;
            default: info.name = '图片'; info.suffix = 'png'; break;
        }

        //获取文件夹名称
        let folderName = $('.folder-name').val().replace(/\s*/g,'');
        let folderPath = '';
        folderPath = (folderName.length > 0 ? ('download/'+ folderName +'/') : 'download/');

        let checkedArr = this.selectChecked();
        

        this.imgArr.length > 0 && this.imgArr.map((v,i)=>{
            if(checkedArr[i]){

                if(this.type == 'video' && v.search('blob') > 0){
                    var a = document.createElement('a');
                    document.body.appendChild(a);
                    a.download = folderPath + info.name+'_'+this.randomStr(10)+'.'+info.suffix;
                    a.href = window.URL.createObjectURL(v);
                    a.click();
                    document.body.removeChild(a);
                }else{
                    chrome.downloads.download( { url: v , filename: folderPath + info.name+'_'+this.randomStr(10)+'.'+info.suffix} , function(downloadId){
                        console.log(downloadId);
                    });
                }
            }
        });
    }


    //选择选中的资源
    selectChecked(){
        let checkedArr = [];
        
        $('.checkBoxItem').each(function(){
            checkedArr.push(this.checked);
        });

        for(let i=0;i<checkedArr.length;i++){
            if(checkedArr[i]){
                break;
            }else{
                if(i == checkedArr.length - 1){
                    return false;
                }
            }
            
        }

        return checkedArr;
    }

    //复制链接
    copyLinks(){
        
        let getLinks = [];
    
        if(!this.selectChecked()){
            alert('请选择资源');
            return;
        }
        let checkedArr = this.selectChecked();

        this.imgArr.length > 0 && this.imgArr.map((v,index)=>{
            if(checkedArr && checkedArr[index]){
                getLinks.push(this.imgArr[index]);
            }
        })

        let links = getLinks.join('\n');

        $('#textPanle').val(links);
        $('#textPanle').get(0).select();
        document.execCommand("Copy");
        alert('复制成功！');
    }

    
    

    //随机字符串
    randomStr(len){
        let str =  'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
        let nstr = '';
        for(let i=0;i<len;i++){
            nstr += str[Math.floor(Math.random()*len)];
        }

        return nstr;
    }
}

new Popup();


