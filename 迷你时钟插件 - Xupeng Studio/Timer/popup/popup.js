let timerend = 0;
let coutdowntimer;
let alarmtimer;
let wakeup = 0;
let timerStatustimer;
let pageloader;
load();

/* 
 * Title栏变化控件
 * 页面切换准则。
 */
document.getElementById("alarm").addEventListener("click",function()
    {
        this.setAttribute("class","selected");
        document.getElementById("countdown").setAttribute("class","unselected");
        document.getElementById("timer").setAttribute("class","unselected");
        document.getElementById("countDowniFrame").setAttribute("hidden","hidden");
        document.getElementById("alarmiFrame").removeAttribute("hidden");
        document.getElementById("timeriFrame").setAttribute("hidden","hidden");
        chrome.storage.local.set({"default_tab":document.getElementsByClassName("selected")[0].getAttribute("id")});
    }
);
document.getElementById("countdown").addEventListener("click",function()
    {
        this.setAttribute("class","selected");
        document.getElementById("alarm").setAttribute("class","unselected");
        document.getElementById("timer").setAttribute("class","unselected");
        document.getElementById("countDowniFrame").removeAttribute("hidden");
        document.getElementById("alarmiFrame").setAttribute("hidden","hidden");
        document.getElementById("timeriFrame").setAttribute("hidden","hidden");
        chrome.storage.local.set({"default_tab":document.getElementsByClassName("selected")[0].getAttribute("id")});
    }
);
document.getElementById("timer").addEventListener("click",function()
{
    this.setAttribute("class","selected");
    document.getElementById("alarm").setAttribute("class","unselected");
    document.getElementById("countdown").setAttribute("class","unselected");
    document.getElementById("countDowniFrame").setAttribute("hidden","hidden");
    document.getElementById("alarmiFrame").setAttribute("hidden","hidden");
    document.getElementById("timeriFrame").removeAttribute("hidden");
    chrome.storage.local.set({"default_tab":document.getElementsByClassName("selected")[0].getAttribute("id")});
}
)

/**
 * Countdown iFrame  UI改变代码
 */
document.getElementById("plusHour").addEventListener("click",function()
{
    let obj='countdownHours';
    plusnumber(obj)
    
});
document.getElementById("plusMin").addEventListener("click",function()
{
    let obj='countdownMins';
    plusnumber(obj)
});
document.getElementById("plusSec").addEventListener("click",function()
{
    let obj='countdownSeconds';
    plusnumber(obj)
});
document.getElementById("minusHour").addEventListener("click",function()
{
    let obj='countdownHours';
    minusnumber(obj)
    
});
document.getElementById("minusMin").addEventListener("click",function()
{
    let obj='countdownMins';
    minusnumber(obj)
});
document.getElementById("minusSec").addEventListener("click",function()
{
    let obj='countdownSeconds';
    minusnumber(obj)
});
function plusnumber (obj)
{
    let changeObj = document.getElementById(obj);
    
    if(obj!='countdownHours'){
    if(parseInt(changeObj.innerHTML)>=59)
    {
        changeObj.innerHTML="00";
    }else{
        changeObj.innerHTML=pluszero(parseInt(changeObj.innerHTML)+1);
    }}else{
        changeObj.innerHTML=pluszero(parseInt(changeObj.innerHTML)+1);
    }
}
function minusnumber (obj)
{
    let changeObj = document.getElementById(obj);
    
    if(obj!='countdownHours'){
    if(parseInt(changeObj.innerHTML)<=0)
    {
        changeObj.innerHTML="59";
    }else{
        changeObj.innerHTML=pluszero(parseInt(changeObj.innerHTML)-1);
    }}else{
        if(parseInt(changeObj.innerHTML)>0)
            changeObj.innerHTML=pluszero(parseInt(changeObj.innerHTML)-1);
        else
            changeObj.innerHTML="00";
    }
}

function pluszero(str)
{
    str=parseInt(str)
    if(str<10)
    return "0"+str;
    return str;
}
//启动定时器
document.getElementById("start").addEventListener("click",function()
{
    let min,sec,hour;
    hour=parseInt(document.getElementById("countdownHours").innerHTML);
    min=parseInt(document.getElementById("countdownMins").innerHTML);
    sec=parseInt(document.getElementById("countdownSeconds").innerHTML);
    if(hour==0&&min==0&&sec==0)return;
    timerend = new Date().getTime()+hour*3600000+min*60000+sec*1000;
    let allElements = document.getElementsByClassName("upCountdown");
    for(let i = 0;i<allElements.length;i++)
    {
        allElements[i].setAttribute("hidden","hidden");
    }
    allElements = document.getElementsByClassName("downCountdown");
    for(let i = 0;i<allElements.length;i++)
    {
        allElements[i].setAttribute("hidden","hidden");
    }
    var caption = document.getElementById('countdownName').innerHTML;
    chrome.runtime.sendMessage({
        "type":"startTimer",
        "endtime" :timerend,
        "timertitle":caption
    });
    document.getElementById("start").style.display="none";
    document.getElementById('startPotatoClock').style.display="none";
    document.getElementById("pause").style.display="inline-block";
    document.getElementById("stop").style.display="inline-block";
    document.getElementById('countdownName').removeAttribute("contenteditable");
    coutdowntimer = setInterval(function() {timer();},100);
});
//来一个番茄钟
document.addEventListener('contextmenu',
event => event.preventDefault());
document.getElementById("startPotatoClock").addEventListener("click",function()
{
    potatoClock();
});
function potatoClock()
{timerend = new Date().getTime()+25*60000;
    let allElements = document.getElementsByClassName("upCountdown");
    for(let i = 0;i<allElements.length;i++)
    {
        allElements[i].setAttribute("hidden","hidden");
    }
    allElements = document.getElementsByClassName("downCountdown");
    for(let i = 0;i<allElements.length;i++)
    {
        allElements[i].setAttribute("hidden","hidden");
    }
    chrome.runtime.sendMessage({
        "type":"startTimer",
        "endtime" :timerend,
        "timertitle":"番茄钟"
    });
    document.getElementById('countdownName').innerHTML="番茄钟";
    document.getElementById("start").style.display="none";
    document.getElementById('startPotatoClock').style.display="none";
    document.getElementById("pause").style.display="inline-block";
    document.getElementById("stop").style.display="inline-block";
    document.getElementById('countdownName').removeAttribute("contenteditable");
    coutdowntimer = setInterval(function() {timer();},100);

}
//消息接收及修改
function timer(){
    chrome.runtime.sendMessage({"type":"wakeup"},function (response){
        if(response.wakeup=="wakeup")wakeup=1;else wakeup=0;
chrome.runtime.sendMessage({"type":"getTimer"},function (response){
    if(response.timerstatus!=-1){
        document.getElementById("countdownHours").innerHTML = response.hour;
        document.getElementById("countdownMins").innerHTML = response.min;
        document.getElementById("countdownSeconds").innerHTML = response.sec;}
        judgeTimerIsEnd(response.end);
        if(response.end=="end")
        {
            if(response.timerstatus==-1){
            let allElements = document.getElementsByClassName("upCountdown");
            for(let i = 0;i<allElements.length;i++)
            {
                allElements[i].removeAttribute("hidden");
            }
            allElements = document.getElementsByClassName("downCountdown");
            for(let i = 0;i<allElements.length;i++)
            {
                allElements[i].removeAttribute("hidden");
            }
            allElements = document.getElementsByClassName("numpad")
            for(let i = 0;i<allElements.length;i++)
            {
                allElements[i].innerHTML="00";
            }
            
            document.getElementById("start").style.display="inline-block";
            document.getElementById('startPotatoClock').style.display="inline-block";
            clearInterval(coutdowntimer);
            document.getElementById("stop").style.display="none";
            document.getElementById("pause").style.display="none";
            document.getElementById("continue").style.display="none";
            document.getElementById('countdownName').setAttribute("contenteditable","true");}
        }
        
});});
}
function judgeTimerIsEnd(end)
{
    if(end!="end")
    {
        let allElements = document.getElementsByClassName("upCountdown");
        for(let i = 0;i<allElements.length;i++)
    {
        allElements[i].setAttribute("hidden","hidden");
    }
    allElements = document.getElementsByClassName("downCountdown");
    for(let i = 0;i<allElements.length;i++)
    {
        allElements[i].setAttribute("hidden","hidden");
    }}
}
//页面加载
function load(){
    chrome.runtime.sendMessage({"type":"wakeup"},function (response){
            if(response.wakeup=="wakeup")wakeup=1;
            
            chrome.runtime.sendMessage({"type":"getTimer"},function (response){
        
                if(response.timerstatus!=1)
                    document.getElementById("continue").style.display="none";
                if(response.timerstatus!=-1)
                {
                   
                    let allElements = document.getElementsByClassName("upCountdown");
                    for(let i = 0;i<allElements.length;i++)
                    {
                        allElements[i].setAttribute("hidden","hidden");
                    }
                    allElements = document.getElementsByClassName("downCountdown");
                    for(let i = 0;i<allElements.length;i++)
                    {
                        allElements[i].setAttribute("hidden","hidden");
                    }
                    
                    
                    document.getElementById('countdownName').innerHTML=response.timertitle;
                    document.getElementById('countdownName').removeAttribute("contenteditable");
                    
                    timer();
                    coutdowntimer = setInterval(function() {timer();},100);
                    document.getElementById("start").style.display="none";
                    document.getElementById('startPotatoClock').style.display="none";
                       
                }else
                {
                    document.getElementById('stop').style.display="none";
                }
                if(response.timerstatus!=0)
                {
                    document.getElementById("pause").style.display="none";
                    
                }
            });
            chrome.runtime.sendMessage({"type":"getAlarms"},function (response){
                if(response.alarmstatus!=1)
                {
                    let hours = new Date().getHours();
                    let min = new Date().getMinutes();
                    document.getElementById('alarmHours').innerHTML=pluszero(hours);
                    document.getElementById('alarmMins').innerHTML=pluszero(min);
                    document.getElementById("stopAlarm").style.display="none";
                }
                else
                {
                    let allElements = document.getElementsByClassName("upAlarms");
                    for(let i = 0;i<allElements.length;i++)
                    {
                        allElements[i].setAttribute("hidden","hidden");
                    }
                    allElements = document.getElementsByClassName("downAlarms");
                    for(let i = 0;i<allElements.length;i++)
                    {
                        allElements[i].setAttribute("hidden","hidden");
                    }
                    let hours = response.hours;
                    let mins = response.mins;
                    let alarmTitle = response.title;
                    document.getElementById('alarmHours').innerHTML=pluszero(hours);
                    document.getElementById('alarmMins').innerHTML=pluszero(mins);
                    document.getElementById('alarmName').innerHTML=alarmTitle;
                    document.getElementById("startAlarm").style.display="none";
                }
                alarmtimer = setInterval(function() {alarmTimer();},100);
            });
        chrome.runtime.sendMessage({"type":"get_timer"},function (response){
            
            if(response.status!=-1)
            {
                document.getElementById("timerTimes").innerHTML=""+response.Str;
                timerStatustimer = setInterval(function() {timerTimer();},10);
            }
            if(response.status==1)
            {
                
                document.getElementById('startTimer').style.display="none";
                document.getElementById('pauseTimer').style.display="inline-block";
                document.getElementById('continueTimer').style.display="none";
                document.getElementById("stopTimer").style.display="inline-block";
                document.getElementById("clearTimer").style.display="none";
            }
            else if(response.status==0)
            {
                
                document.getElementById('startTimer').style.display="none";
                document.getElementById('pauseTimer').style.display="none";
                document.getElementById('continueTimer').style.display="inline-block";
                document.getElementById("stopTimer").style.display="inline-block";
                document.getElementById("clearTimer").style.display="none";
            }
            else if(response.status==-1)
            {
                document.getElementById('startTimer').style.display="inline-block";
                document.getElementById('pauseTimer').style.display="none";
                document.getElementById('continueTimer').style.display="none";
                document.getElementById("stopTimer").style.display="none";
            }
        });
    });
    chrome.storage.local.get("default_tab",function (data){
        if(data.default_tab==undefined)return;
        document.getElementsByClassName("selected")[0].setAttribute("class","unselected");
        document.getElementById(data.default_tab).setAttribute("class","selected");
        document.getElementById("countDowniFrame").setAttribute("hidden","hidden");
        if(data.default_tab=="countdown")
        document.getElementById("countDowniFrame").removeAttribute("hidden");
        if(data.default_tab=="alarm")
        document.getElementById("alarmiFrame").removeAttribute("hidden");
        if(data.default_tab=="timer")
        document.getElementById("timeriFrame").removeAttribute("hidden");
    });
}
//暂停键事件
document.getElementById("pause").addEventListener("click",function()
{
    chrome.runtime.sendMessage({"type":"pauseTimer"});
    this.style.display="none";
    document.getElementById("continue").style.display="inline-block";
});
document.getElementById("continue").addEventListener("click",function()
{
    chrome.runtime.sendMessage({"type":"continueTimer"});
    this.style.display="none";
    document.getElementById("pause").style.display="inline-block";
});
document.getElementById("stop").addEventListener("click",function()
{
    chrome.runtime.sendMessage({"type":"stopTimer"});
    this.style.display="none";
    document.getElementById("pause").style.display="none";
    document.getElementById("continue").style.display="none";
    document.getElementById("start").style.display="inline-block";
    document.getElementById('startPotatoClock').style.display="inline-block";
    
    clearInterval(coutdowntimer);
    let allElements = document.getElementsByClassName("upCountdown");
            for(let i = 0;i<allElements.length;i++)
            {
                allElements[i].removeAttribute("hidden");
            }
            allElements = document.getElementsByClassName("downCountdown");
            for(let i = 0;i<allElements.length;i++)
            {
                allElements[i].removeAttribute("hidden");
            }
            allElements = document.getElementsByClassName("numpad")
            for(let i = 0;i<allElements.length;i++)
            {
                allElements[i].innerHTML="00";
            }
            document.getElementById('countdownName').setAttribute("contenteditable","true");
});


//
document.getElementById("plusAlarmHour").addEventListener("click",function()
{
    let obj=document.getElementById('alarmHours');
    if(parseInt(obj.innerHTML)>=23)
    {
        obj.innerHTML="00";
    }else{
        obj.innerHTML=pluszero(parseInt(obj.innerHTML)+1);
    }
    
});
document.getElementById("plusAlarmMin").addEventListener("click",function()
{
    let obj='alarmMins';
    plusnumber(obj)
});
document.getElementById("minusAlarmHour").addEventListener("click",function()
{
    let obj=document.getElementById('alarmHours');
    if(parseInt(obj.innerHTML)<=0)
    {
        obj.innerHTML="23";
    }else{
        obj.innerHTML=pluszero(parseInt(obj.innerHTML)-1);
    }    
});
document.getElementById("minusAlarmMin").addEventListener("click",function()
{
    let obj='alarmMins';
    minusnumber(obj)
});
document.getElementById("startAlarm").addEventListener("click",function ()
{
    let min,sec,hour;
    hour=parseInt(document.getElementById("alarmHours").innerHTML);
    min=parseInt(document.getElementById("alarmMins").innerHTML);
    let allElements = document.getElementsByClassName("upAlarms");
    for(let i = 0;i<allElements.length;i++)
    {
        allElements[i].setAttribute("hidden","hidden");
    }
    allElements = document.getElementsByClassName("downAlarms");
    for(let i = 0;i<allElements.length;i++)
    {
        allElements[i].setAttribute("hidden","hidden");
    }
    var caption = document.getElementById('alarmName').innerHTML;
    chrome.runtime.sendMessage({
        "type":"startAlarm",
        "hour":hour,
        "min":min,
        "title":caption
    });
    document.getElementById("startAlarm").style.display="none";
    document.getElementById('stopAlarm').style.display="inline-block";
    document.getElementById("alarmName").removeAttribute("contenteditable");
    alarmtimer = setInterval(function() {alarmTimer();},100);
}

)
document.getElementById("stopAlarm").addEventListener("click",function ()
{
    chrome.runtime.sendMessage({
        "type":"stopAlarm"
    });
}

)
function alarmTimer()
{
    chrome.runtime.sendMessage({"type":"wakeup"},function (response){
        if(response.wakeup=="wakeup")wakeup=1;else wakeup=0;
        chrome.runtime.sendMessage({"type":"getAlarms"},function (response){
            
        if(response.alarmstatus!=1)
        {
            let allElements = document.getElementsByClassName("upAlarms");
            for(let i = 0;i<allElements.length;i++)
            {
                allElements[i].removeAttribute("hidden");
            }
            allElements = document.getElementsByClassName("downAlarms");
            for(let i = 0;i<allElements.length;i++)
            {
                allElements[i].removeAttribute("hidden");
            }
                document.getElementById('startAlarm').style.display="inline-block";
                document.getElementById("stopAlarm").style.display="none";
                document.getElementById("alarmName").setAttribute("contenteditable","true");
        clearInterval(alarmtimer);
        }
        });
    });
}
function timerTimer()
{
    chrome.runtime.sendMessage({"type":"wakeup"},function (response){
        if(response.wakeup=="wakeup")wakeup=1;
        chrome.runtime.sendMessage({"type":"get_timer"},function (response){
        if(response.status!=-1)
        {
            document.getElementById("timerTimes").innerHTML=""+response.Str;
        }
        else{
            clearInterval(timerStatustimer);
        }
        
    });
});
}
document.getElementById("startTimer").addEventListener("click",function()
{
    chrome.runtime.sendMessage({"type":"start_Timer"});
    timerStatustimer = setInterval(function() {timerTimer();},10);
    
    document.getElementById('startTimer').style.display="none";
    document.getElementById('pauseTimer').style.display="inline-block";
    document.getElementById('continueTimer').style.display="none";
    document.getElementById("stopTimer").style.display="inline-block";
    document.getElementById("clearTimer").style.display="none";
}
);
document.getElementById("pauseTimer").addEventListener("click",function()
{
    chrome.runtime.sendMessage({"type":"pause_Timer"});
    document.getElementById('startTimer').style.display="none";
    document.getElementById('pauseTimer').style.display="none";
    document.getElementById('continueTimer').style.display="inline-block";
    document.getElementById("stopTimer").style.display="inline-block";
    document.getElementById("clearTimer").style.display="none";
    
}
);
document.getElementById("continueTimer").addEventListener("click",function()
{
    chrome.runtime.sendMessage({"type":"continue_Timer"});
    document.getElementById('startTimer').style.display="none";
    document.getElementById('pauseTimer').style.display="inline-block";
    document.getElementById('continueTimer').style.display="none";
    document.getElementById("stopTimer").style.display="inline-block";
    document.getElementById("clearTimer").style.display="none";
}
);
document.getElementById("stopTimer").addEventListener("click",function()
{
    chrome.runtime.sendMessage({"type":"stop_Timer"});
    document.getElementById('startTimer').style.display="inline-block";
    document.getElementById('pauseTimer').style.display="none";
    document.getElementById('continueTimer').style.display="none";
    document.getElementById("stopTimer").style.display="none";
    document.getElementById("clearTimer").style.display="inline-block";
    
}
);

document.getElementById("clearTimer").addEventListener("click",function()
{
    document.getElementById("timerTimes").innerHTML="0:00:00.00"
}
);
