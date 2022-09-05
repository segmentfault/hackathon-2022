window.onload = init
const timeMap =sessionStorage.getItem('timeMap')? new Set(Array.from(sessionStorage.getItem('timeMap').split(',')).map(Number)) : new Set()
function init () {
    const videoList = document.getElementsByTagName('video')
    const arrList = Array.from(videoList)
    const speedMarkUrl = chrome.runtime.getURL("/src/assets/images/speed-mark.svg")
    arrList.map((item,index)=>{
        const wrapper = document.createElement("div");
        const aside = document.createElement("div");
        const speedListAside = document.createElement("aside")
        wrapper.classList.add("video-controller-container");
        const boxTemplate = `
            <div class="video-controller">
                <div class="speed-text">${item.playbackRate.toFixed(1)}</div>
                <div class="control-operation video-controller-hide">
                    <button data-op="fall-back">«</button>
                    <button data-op="slower">-</button>
                    <button data-op="faster">+</button>
                    <button data-op="fast-forward">»</button>
                    <button data-op="mark">mark</buttom>
                    <button data-op="record-speed">record-speed</button>
                </div>
            </div>
        `;
    
        wrapper.innerHTML = boxTemplate;
    
        let asideul = document.createElement('ul')
        asideul.classList.add('time-list')
        const speedListUl = document.createElement('ul')
        speedListAside.classList.add('speed-list')
        speedListUl.classList.add('speed-list-ul')
        aside.appendChild(asideul)
        speedListAside.appendChild(speedListUl);
        item.parentElement.insertBefore(wrapper,item)
        item.parentElement.insertBefore(aside,item)
        item.parentElement.insertBefore(speedListAside,item)
        // window.onload = ()=>{
            wrapper.addEventListener('mouseenter',showControllerBtn)
            wrapper.addEventListener('mouseleave',hideControllerBtn)
            const controllerOperation = wrapper.querySelector('.control-operation')
            controllerOperation.addEventListener('click',handleClick)
            controllerOperation.addEventListener('dblclick',stopProp)
            dragPatch.patch(wrapper) // 非纯函数，修改了dom
    
            aside.addEventListener('click',goto)
            speedListAside.addEventListener('click',gotoSpeed)
            document.addEventListener('keyup', keyboard)
        // }

        renderList()
        renderSpeedList(speedListUl)
    })
}
const operation = {
    faster: (target)=>{
        const tar = target.parentElement.parentElement.parentElement.parentElement.getElementsByTagName('video')[0]
        tar.playbackRate = Math.round((tar.playbackRate+0.1)*10)/10
        target.parentElement.parentElement.querySelector('.speed-text').innerText = tar.playbackRate.toFixed(1)
    },
    slower: (target)=>{
        const tar = target.parentElement.parentElement.parentElement.parentElement.getElementsByTagName('video')[0]
        tar.playbackRate = Math.round((tar.playbackRate-0.1)*10)/10
        target.parentElement.parentElement.querySelector('.speed-text').innerText = tar.playbackRate.toFixed(1)
    },
    "fall-back": (target)=>{
        const tar = target.parentElement.parentElement.parentElement.parentElement.getElementsByTagName('video')[0]
        tar.currentTime -= 10;
    },
    "fast-forward": (target)=>{
        const tar = target.parentElement.parentElement.parentElement.parentElement.getElementsByTagName('video')[0]
        tar.currentTime += 10;
    },
    mark: (target)=>{    
        const tar = target.parentElement.parentElement.parentElement.parentElement.getElementsByTagName('video')[0]
        let time = formateSecond(formateTime(tar.currentTime))
        timeMap.add(time)
        sessionStorage.setItem('timeMap',Array.from(timeMap))
        renderList()
    },
    "record-speed": (target)=>{
        const tar = target.parentElement.parentElement.parentElement.parentElement;
        const sessions = JSON.parse(sessionStorage.getItem('speed_list'));
        let speedList = sessions?sessions:[];
        const item = tar.getElementsByTagName('video')[0].playbackRate.toFixed(1);
        if(!speedList.includes(item)){
            speedList.push(item);
        }
        sessionStorage.setItem('speed_list',JSON.stringify(speedList))
        renderSpeedList(tar.querySelector('.speed-list-ul'))
    },
    reset: (target)=> {
        const tar = target.parentElement.parentElement.parentElement.parentElement;
        tar.getElementsByTagName('video')[0].playbackRate = 1.0;
        tar.querySelector('.speed-text').innerText = "1.0"
    }
}

const stopProp = (e)=>{
    e.stopPropagation();
}
const handleClick = (e)=>{
    if(e.target.nodeName!=='BUTTON') return
    e.stopPropagation();
    operation[e.target.dataset['op']](e.target);
}
const showControllerBtn = (e)=>{
    e.stopPropagation();
    const ele = e.target.querySelector('.control-operation')
    ele.classList.remove('video-controller-hide')
}
const hideControllerBtn = (e)=>{
    e.stopPropagation();
    const ele = e.target.querySelector('.control-operation')
    ele.classList.add('video-controller-hide')
}
const keyboard = (e)=>{
    if(e.keyCode===68){
        const tar = document.querySelector('.control-operation').children[0];
        operation.faster(tar)
    }
    else if(e.keyCode===65){
        const tar = document.querySelector('.control-operation').children[0]
        operation.slower(tar)
    }
    else if(e.keyCode===83){
        const tar = document.querySelector('.control-operation').children[0]
        operation.mark(tar)
    }
    else if(e.keyCode===90){
        const tar = document.querySelector('.control-operation').children[0]
        operation['record-speed'](tar)
    }
    else if(e.keyCode===82){
        const tar = document.querySelector('.control-operation').children[0]
        operation['reset'](tar)
    }
}
const renderList = ()=>{
    let list = document.getElementsByClassName('time-list')[0]
    list.innerHTML=''
    for(let t of timeMap){
        // list.innerHTML+=`<li><button>${t}</button></li>`  
        list.innerHTML+=`<li><button>${formateTime(t)}</button><button class="deletetime">×</button></li>`  
    }
}
const renderSpeedList = (target)=>{
    const sessions = JSON.parse(sessionStorage.getItem('speed_list'));
    target.innerHTML = ''
    sessions.map((item)=>{
        target.innerHTML += `<li><button>${item}</button><button class="delete-speed-list">×</button></li>`
    })
}
const goto=(e)=>{
    e.stopPropagation();
    if( e.target.classList.contains("deletetime")){
        let deltime = formateSecond(e.target.previousSibling.innerText)
        timeMap.delete(deltime)
        sessionStorage.setItem('timeMap',Array.from(timeMap))
        renderList()
     }
     else{
        let video = document.getElementsByTagName('video')[0]
        // video.currentTime=e.target.innerText
        video.currentTime=formateSecond(e.target.innerText)
     }

}
const gotoSpeed = (e)=>{
    e.stopPropagation();
    if(e.target.classList.contains("delete-speed-list")) {
        const arr = JSON.parse(sessionStorage.getItem('speed_list')).filter((item)=> item!==e.target.previousSibling.innerText)
        sessionStorage.setItem('speed_list',JSON.stringify(arr));
        renderSpeedList(e.target.parentElement.parentElement);
    }
    else {
        const video = document.getElementsByTagName('video')[0];
        video.playbackRate = Number(e.target.innerText);
        document.querySelector('.speed-text').innerText = e.target.innerText;
    }
}
const formateTime = (time)=>{
    const h = parseInt(time / 3600)
    const minute = parseInt(time / 60 % 60)
    const second = Math.ceil(time % 60)    
    const hours = h < 10 ? '0' + h : h
    const formatSecond = second > 59 ? 59 : second
    return `${hours > 0 ? `${hours}:` : ''}${minute < 10 ? '0' + minute : minute}:${formatSecond < 10 ? '0' + formatSecond : formatSecond}`
}

const formateSecond = (time)=>{
    var tt = '';
    tarr=time.split(':')
    if(tarr.length===3)
    {
        var hour = time.split(':')[0];
        var min = time.split(':')[1];
        var sec = time.split(':')[2];
        tt = Number(hour*3600) + Number(min*60) + Number(sec);
    }
    else{
        var min = time.split(':')[0];
        var sec = time.split(':')[1];
        tt =Number(min*60) + Number(sec);
    }
    return tt;
}
