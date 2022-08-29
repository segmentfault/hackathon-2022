
//任务窗格对象，包含名称和url两个属性。
var TaskPanelData={
    CFA:{Name:"taskpane_cfa",Url:"/ui/CreateCFA.html"},
    SLC:{Name:"taskpane_slc",Url:"/ui/CreateSLC.html"},
    Certification:{Name:"taskpane_certification",Url:"/ui/CreateCertification.html"},
    Contract:{Name:"taskpane_contract",Url:"/ui/CreateContract.html"},
    Surprise:{Name:"taskpane_surprise",Url:"/ui/Surprise.html"},
    Browser:{Name:"taskpane_browser",Url:"/ui/Browser.html"},
}

/**
* @description 加载加载项时发生
* @param ribbonUI {any}: RibbonControl对象
* @return void
* @author peter 2022/08/24 
*/
function OnAddinLoad(ribbonUI){
    if (typeof (wps.ribbonUI) != "object"){
		wps.ribbonUI = ribbonUI
    }
    wps.ApiEvent.AddApiEventListener("DocumentChange", OnDocumentChange);
    return true
}

/**
* @description 在创建新文档、打开已有文档或激活其他文档后执行的操作
* @return {void} 无返回值
* @author peter 2022/08/24 
*/
function OnDocumentChange()
{
    //判断文档的痕迹状态，刷新状态开关的状态
    InvalidateRevisionsControl();
}

/**
* @description Ribbon按钮点击后触发
* @param  {object} control：可能是控件对象也可能是控件对象的状态值
* @return {void} 无返回值
* @author peter 2022/08/24 
*/
function OnAction(control) {
    var eleId;
    if (typeof control == "object" && arguments.length == 1) { 
        eleId = control.Id;//针对Ribbon的按钮的
    }  else if (typeof control == "boolean" && arguments.length > 1) { 
        eleId = arguments[1].Id;//针对toggleButton的
    } 
    switch (eleId) {
        case "btnCreateCFA":
            {
                ShowTaskPanel(TaskPanelData.CFA);
                break;
            }
        case "btnCreateSLC":
            {
                ShowTaskPanel(TaskPanelData.SLC);
                break;
            }
        case "btnCreateCertification":
            {
                ShowTaskPanel(TaskPanelData.Certification);
                break;
            }
        case "btnCreateContract":
            {
                ShowTaskPanel(TaskPanelData.Contract);
                break;
            }
        case "btnSurprise":
            {
                ShowTaskPanel(TaskPanelData.Surprise,500);
                break;
            }
        case "btnAbout":
           {
                let tsId=wps.PluginStorage.getItem(TaskPanelData.Surprise.Name);
                if(tsId)
                {
                    wps.PluginStorage.removeItem(TaskPanelData.Surprise.Name);
                    let tskpane = wps.GetTaskPane(tsId);
                    tskpane.Delete();
                }
                wps.ShowDialog(GetUrlPath() + "/ui/About.html", "关于", 350* window.devicePixelRatio, 150 * window.devicePixelRatio, true);
                break;
           }
        case "btnIsShowBookMark":
            {
                wps.WpsApplication().ActiveWindow.ActivePane.View.ShowBookmarks=!wps.WpsApplication().ActiveWindow.ActivePane.View.ShowBookmarks;
                wps.ribbonUI.InvalidateControl(eleId)
                break;
            }
        case "btnEnterTrackRevisions":
            {
                EnterTrackRevisions(true,GlobalData.PassWord);
                InvalidateRevisionsControl();
                break;
            }
        case "btnCloseTrackRevisions":
            {
                EnterTrackRevisions(false,GlobalData.PassWord);
                InvalidateRevisionsControl();
                break;
            }   
        case "btnAcceptAllRevisions":
            {
                DoAllRevisions(true);
                break;
            }
        case "btnRejectAllRevisions":
            {
                DoAllRevisions(false);
                break;
            }
        case "btnShowBrowser":
            {
                ShowTaskPanel(TaskPanelData.Browser);
                break;
            }
        case "btnShowSetting":
            {
                let tsId=wps.PluginStorage.getItem(TaskPanelData.Surprise.Name);
                if(tsId)
                {
                    wps.PluginStorage.removeItem(TaskPanelData.Surprise.Name);
                    let tskpane = wps.GetTaskPane(tsId);
                    tskpane.Delete();
                }
                wps.ShowDialog(GetUrlPath() + "/ui/Setting.html", "数据维护", wps.WpsApplication().Width* window.devicePixelRatio*0.8, wps.WpsApplication().Height* window.devicePixelRatio*0.8, true);
                break;
            }
        default:
            break
    }
    return false;
}

/**
* @description 刷新一下开启强制留痕按钮和关闭强制留痕按钮状态
* @return {void} 无返回值
* @author peter 2022/08/24 
*/
function InvalidateRevisionsControl()
{
    wps.ribbonUI.InvalidateControl("btnCloseTrackRevisions");
    wps.ribbonUI.InvalidateControl("btnEnterTrackRevisions");
}


/**
* @description Ribbon按钮获取按钮点击状态时触发事件后执行的操作
* @param {object} task TaskPanelData对象
* @param {number} width 任务窗格的宽度
* @return {void} 无返回值
* @author peter 2022/08/24 
*/
function ShowTaskPanel(task,width=700)
{
    let taskname=task.Name;
    let url=task.Url;
    HideTaskPane(taskname);
    let tsId = wps.PluginStorage.getItem(taskname);
    if (!tsId) {
        let tskpane = wps.CreateTaskPane(GetUrlPath()+url,"欢迎使用")
        let id = tskpane.ID;
        wps.PluginStorage.setItem(taskname, id);
        tskpane.Visible = true;
        tskpane.Width=width;
        tskpane.MinWidth=width;
        tskpane.MaxWidth=wps.WpsApplication().Width* window.devicePixelRatio*0.5;
    } else {
        let tskpane = wps.GetTaskPane(tsId);
        tskpane.Visible = !tskpane.Visible;
    }
}


/**
* @description 隐藏除了参数中指定的任务窗格之外的其他窗格
* @param {string}showname 要显示的任务窗格的名称
* @return {void} 无返回值
* @author peter 2022/08/24 
*/
function HideTaskPane(showname)
{
    for (var key in TaskPanelData) {
        if(showname!=TaskPanelData[key].Name)
        {
            let tsId = wps.PluginStorage.getItem(TaskPanelData[key].Name);
            if(tsId){
                let tskpane = wps.GetTaskPane(tsId);
                if(tskpane.Visible)
                {
                    tskpane.Visible = false;
                }
            }
        }
    }
    

}

/**
* @description Ribbon按钮获取图片时触发事件后执行的操作
* @param {object} control 可能是控件对象也可能是控件对象的状态值
* @return {string} 返回图片的路径
* @author peter 2022/08/24 
*/
function GetImage(control) {
    var eleId;
    if (typeof control == "object" && arguments.length == 1) { 
        eleId = control.Id;//针对Ribbon的按钮的
    }  else if (typeof control == "boolean" && arguments.length > 1) { 
        eleId = arguments[1].Id;//针对toggleButton的
    } 
    switch (eleId) {
        case "btnCreateCFA":
            return "images/1.svg";
        case "btnCreateSLC":
            return "images/2.svg";
        case "btnCreateCertification":
            return "images/3.svg";
        case "btnCreateContract":
            return "images/4.svg";
        case "btnSurprise":
            return "images/surprise.svg";
        case "btnAbout":
            return "images/about.svg";
        case "btnIsShowBookMark":
            return "images/5.svg";
        case "menuRevisions":
            return "images/6.svg";
        case "btnEnterTrackRevisions":
            return "images/7.svg";
        case "btnCloseTrackRevisions":
            return "images/8.svg";
        case "btnAcceptAllRevisions":
            return "images/9.svg";
        case "btnRejectAllRevisions":
            return "images/10.svg";
        case "btnShowBrowser":
            return "images/11.svg";
        case "btnShowSetting":
            return "images/12.svg";
        default:
            ;
    }
    return "images/default.svg"
}

/**
* @description Ribbon按钮获取按钮状态时触发事件后执行的操作
* @param {object} control 可能是控件对象也可能是控件对象的状态值
* @return {bool} 返回是否可用
* @author peter 2022/08/24 
*/
function OnGetEnabled(control) {
    var eleId;
    if (typeof control == "object" && arguments.length == 1) { 
        eleId = control.Id;//针对Ribbon的按钮的
    }  else if (typeof control == "boolean" && arguments.length > 1) { 
        eleId = arguments[1].Id;//针对toggleButton的
    } 
    switch (eleId) {
        case "btnCreateCFA":
            return true;
        case "btnCreateSLC":
            {
                return true;
            }
        default:
            break
    }
    return true
}

/**
* @description Ribbon按钮获取按钮标题时触发事件后执行的操作
* @param {object} control 可能是控件对象也可能是控件对象的状态值
* @return {string} 返回按钮要显示的标题
* @author peter 2022/08/24 
*/
function OnGetLabel(control)
{
    var eleId;
    if (typeof control == "object" && arguments.length == 1) { 
        eleId = control.Id;//针对Ribbon的按钮的
    }  else if (typeof control == "boolean" && arguments.length > 1) { 
        eleId = arguments[1].Id;//针对toggleButton的
    } 
    switch (eleId) {
        case "btnIsShowBookMark":
            return wps.WpsApplication().ActiveWindow.ActivePane.View.ShowBookmarks?"隐藏书签符":"显示书签符";
        default:
            break
    }
    return "";
}


/**
* @description Ribbon按钮获取按钮点击状态时触发事件后执行的操作
* @param {object} control 可能是控件对象也可能是控件对象的状态值
* @return {bool} 返回是否点击
* @author peter 2022/08/24 
*/
function GetPressed(control)
{
    var eleId;
    if (typeof control == "object" && arguments.length == 1) { 
        eleId = control.Id;//针对Ribbon的按钮的
    }  else if (typeof control == "boolean" && arguments.length > 1) { 
        eleId = arguments[1].Id;//针对toggleButton的
    } 
    switch (eleId) {
        case "btnEnterTrackRevisions":
            return wps.WpsApplication().ActiveDocument.ProtectionType==0?true:false;
        case "btnCloseTrackRevisions":
            return wps.WpsApplication().ActiveDocument.ProtectionType!=0?true:false;
        default:
            break
    }
    return false;
}

/**
* @description Ribbon按钮获取按钮可见状态时触发事件后执行的操作
* @param {object} control 可能是控件对象也可能是控件对象的状态值
* @return {bool} 返回是否可见
* @author peter 2022/08/24 
*/
function OnGetVisible(control){
    return true
}



