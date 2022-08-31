let endtime=0;
let timertitle = "";
let timerstatus=-1;
let alarmtimes = 0;
let alarmtitle = "";
let alarmstatus = 0;
let wakeup;
let starttime = 0;
let timer_status=-1;
chrome.runtime.onMessage.addListener(data => {
    if (data.type == 'startTimer') {
        //chrome.storage.local.set({"TimerendTime":data.endTime})
        endtime = data.endtime;
        timertitle = data.timertitle;
        timerstatus= 0;
        chrome.storage.local.set({"endtime":endtime,"timertitle":timertitle,"timerstatus":timerstatus});
        chrome.contextMenus.update("startCountdownPotato",{"enabled":false});
        chrome.alarms.create("timer",{
            when:endtime
        });
    }else if(data.type == 'pauseTimer')
    {
        timerstatus=1
        chrome.alarms.clear("timer");
        endtime = endtime - new Date().getTime();
        chrome.contextMenus.update("startCountdownPotato",{"enabled":false});
        chrome.storage.local.set({"endtime":endtime,"timertitle":timertitle,"timerstatus":timerstatus});
    }else if(data.type== 'continueTimer')
    {
        timerstatus=0;
        endtime = endtime + new Date().getTime();
        chrome.alarms.create("timer",{
            when:endtime
        });
        chrome.contextMenus.update("startCountdownPotato",{"enabled":false});
        chrome.storage.local.set({"endtime":endtime,"timertitle":timertitle,"timerstatus":timerstatus});
     }
     else if(data.type== 'stopTimer')
    {
        
        timerstatus=-1;
        chrome.alarms.clear("timer");
        chrome.contextMenus.update("startCountdownPotato",{"enabled":true});
        chrome.storage.local.set({"endtime":endtime,"timertitle":timertitle,"timerstatus":timerstatus});
    }else if(data.type== 'startAlarm')
    {
        let alarmEndTime = new Date();
        alarmEndTime.setHours(data.hour);
        alarmEndTime.setMinutes(data.min);
        alarmEndTime.setSeconds(0);
        if(Date.now()>alarmEndTime.getTime())alarmEndTime=new Date(alarmEndTime.getTime()+24*3600*1000);
        alarmtimes = alarmEndTime;
        alarmstatus=1;
        chrome.storage.local.set({"alarmendtime":alarmEndTime.getTime(),"alarmstatus":1,"alarmtitle":data.title});
        chrome.alarms.create("alarms",{when:alarmEndTime.getTime()})
    }
    else if(data.type== 'stopAlarm')
    {
        alarmstatus=0;
        chrome.storage.local.set({"alarmstatus":0});
        chrome.alarms.clear("alarms")
    }
    if (data.type == 'start_Timer') {
        //chrome.storage.local.set({"TimerendTime":data.endTime})
        starttime = Date.now();
        timer_status= 1;
        chrome.storage.local.set({"starttime":starttime,"timer_status":timer_status});
    }
    if (data.type == 'pause_Timer') {
        //chrome.storage.local.set({"TimerendTime":data.endTime})
        timer_status=0;
        starttime = new Date().getTime()-starttime;
        chrome.storage.local.set({"starttime":starttime,"timer_status":timer_status});
    }
    if (data.type == 'stop_Timer') {
        //chrome.storage.local.set({"TimerendTime":data.endTime})
        timer_status=-1;
        chrome.storage.local.set({"starttime":Date.now(),"timer_status":timer_status});
    }if(data.type=="continue_Timer")
    {
        timer_status=1;
        starttime = new Date().getTime()-starttime;
        chrome.storage.local.set({"starttime":starttime,"timer_status":timer_status});
    }
});

chrome.runtime.onStartup.addListener(
function ()
{
    chrome.storage.local.get(null,function (datablocks)
            {
    if(datablocks.endtime==undefined)endtime=0;
    else endtime = datablocks.endtime;
    if(datablocks.timerstatus==undefined)timerstatus=-1; 
    else timerstatus = datablocks.timerstatus;
    if(datablocks.timertitle==undefined)timertitle="提醒一下"; 
    else timertitle = datablocks.timertitle;
    if(datablocks.alarmendtime==undefined)alarmtimes=0;
    else alarmtimes = datablocks.alarmendtime;
    if(datablocks.alarmstatus==undefined)alarmstatus=0; 
    else alarmstatus = datablocks.alarmstatus;
    if(datablocks.alarmtitle==undefined)alarmtitle="提醒一下"; 
    else alarmtitle = datablocks.alarmtitle;
    if(datablocks.starttime==undefined)starttime=0; 
    else starttime = datablocks.starttime;
    if(datablocks.timer_status==undefined)timer_status=-1; 
    else timer_status = datablocks.timer_status;
    if(alarmstatus==1)
        chrome.alarms.create("alarms",{when:(new Date(alarmtimes)).getTime()});}
    );
}
);
chrome.runtime.onMessage.addListener(
    function(data,sender,sendResponse){
        if (data.type =='getTimer') {
            chrome.storage.local.get(null,function (datablocks)
            {
                if(datablocks.endtime==undefined)endtime=0;
                else endtime = datablocks.endtime;
                if(datablocks.timerstatus==undefined)timerstatus=-1; 
                else timerstatus = datablocks.timerstatus;
                if(datablocks.timertitle==undefined)timertitle="提醒一下"; 
                else timertitle = datablocks.timertitle;
            });
            let remain = endtime - new Date().getTime();
            let hour = pluszero(parseInt(remain/3600000));
            let min= pluszero(parseInt(remain/60000%60));
            let sec = pluszero(parseInt(remain/1000%60));
            let end="";
            if(remain<0)
            {
                end="end";
            }
            if(timerstatus==1)
            {
            hour = pluszero(parseInt(endtime/3600000));
            min= pluszero(parseInt(endtime/60000%60));
            sec = pluszero(parseInt(endtime/1000%60));
            }
            sendResponse({"hour":hour,"min":min,"sec":sec,"end":end,"timertitle":timertitle,"timerstatus":timerstatus})
        }
        else if(data.type=="wakeup")
        {
            chrome.storage.local.get(null,function (datablocks)
            {
                if(datablocks.endtime==undefined)endtime=0;
                else endtime = datablocks.endtime;
                if(datablocks.timerstatus==undefined)timerstatus=-1; 
                else timerstatus = datablocks.timerstatus;
                if(datablocks.timertitle==undefined)timertitle="提醒一下"; 
                else timertitle = datablocks.timertitle;
                if(datablocks.alarmendtime==undefined)alarmtimes=0;
                else alarmtimes = datablocks.alarmendtime;
                if(datablocks.alarmstatus==undefined)alarmstatus=0; 
                else alarmstatus = datablocks.alarmstatus;
                if(datablocks.alarmtitle==undefined)alarmtitle="提醒一下"; 
                else alarmtitle = datablocks.alarmtitle;
                if(datablocks.starttime==undefined)starttime=0; 
                else starttime = datablocks.starttime;
                if(datablocks.timer_status==undefined)timer_status=-1; 
                else timer_status = datablocks.timer_status;
                wakeup="wakeup";
            });
            sendResponse({"wakeup":wakeup});
        }
        if (data.type =='getAlarms') {
            chrome.storage.local.get(null,function (datablocks)
            {
                if(datablocks.alarmendtime==undefined)alarmtimes=0;
                else alarmtimes = datablocks.alarmendtime;
                if(datablocks.alarmstatus==undefined)alarmstatus=0; 
                else alarmstatus = datablocks.alarmstatus;
                if(datablocks.alarmtitle==undefined)alarmtitle="闹钟提醒"; 
                else alarmtitle = datablocks.alarmtitle;
                
            });
            let hour = new Date(alarmtimes).getHours();
            let min= new Date(alarmtimes).getMinutes();
            sendResponse({"hours":hour,"mins":min,"title":alarmtitle,"alarmstatus":alarmstatus})
        }
        if(data.type=="get_timer")
        {
            chrome.storage.local.get(null,function (datablocks)
            {
                if(datablocks.starttime==undefined)starttime=0; 
                else starttime = datablocks.starttime;
                if(datablocks.timer_status==undefined)timer_status=-1; 
                else timer_status = datablocks.timer_status;
            });
            let calc
            if(timer_status==0)
            {
                calc = starttime;
            }else
            {calc = Date.now()-starttime;}
            
            let hour = parseInt(calc/3600000);
            let min = parseInt(calc/60000%60);
            let sec = parseInt(calc/1000%60);
            let millsec = parseInt(calc/10%100);
            let days;
            if(hour>=24){
                days = hour/24;
                hour=hour%24;
                let turnedbackStr = ""+days+":"+pluszero(hour)+":"+pluszero(min)+":"+pluszero(sec)+"."+pluszero(millsec);
                sendResponse({"Str":turnedbackStr,"status":timer_status});
            }else
            {
                let turnedbackStr = hour+":"+pluszero(min)+":"+pluszero(sec)+"."+pluszero(millsec);
                sendResponse({"Str":turnedbackStr,"status":timer_status});
            }
        }
    }
)

chrome.alarms.onAlarm.addListener(function (alarmInfo)
    {
        if(alarmInfo.name=="timer")
            {
                timerstatus= -1;
                chrome.storage.local.set({"timerstatus":timerstatus});
                chrome.contextMenus.update("startCountdownPotato",{"enabled":true});
                chrome.notifications.create('', {
                    type: 'basic',
                    title: '倒计时:到时间啦!',
                    message: timertitle,
                    iconUrl: '../Timer.png'
                }
                );
                
            }else if(alarmInfo.name=="alarms")
            {
                chrome.storage.local.get(null,function (datablocks)
                {
                    if(datablocks.alarmendtime==undefined)alarmtimes=0;
                    else alarmtimes = datablocks.alarmendtime;
                    if(datablocks.alarmstatus==undefined)alarmstatus=0; 
                    else alarmstatus = datablocks.alarmstatus;
                    if(datablocks.alarmtitle==undefined)alarmtitle="提醒一下"; 
                    else alarmtitle = datablocks.alarmtitle;
                    alarmstatus = 0;
                    chrome.storage.local.set({"alarmstatus":alarmstatus});
                    chrome.notifications.create('', {
                        type: 'basic',
                        title: '闹钟提示',
                        message: alarmtitle,
                        iconUrl: '../Timer.png'
                    });
                }
                );
                
            }
    }
    );


    chrome.runtime.onInstalled.addListener(() => {
        chrome.contextMenus.create({
        id: 'startCountdownPotato',
        title: "番茄钟",
        contexts:["page"]
        });
    });
    chrome.contextMenus.onClicked.addListener((info) => {
        if (info.menuItemId == "startCountdownPotato") {
            endtime = new Date().getTime()+25*60000;
            timertitle = "番茄钟";
            timerstatus= 0;
            chrome.storage.local.set({"endtime":endtime,"timertitle":timertitle,"timerstatus":timerstatus});
            chrome.alarms.create("timer",{
            when:endtime
        });
        var views = chrome.extension.getViews({type:'popup'});
        if(views.length > 0) {
            views[0].startCountdownPotato();
        }
        chrome.contextMenus.update("startCountdownPotato",{"enabled":false});
        }
        });



function pluszero(str)
{
    str=parseInt(str)
    if(str<10)
    return "0"+str;
    return str;
}