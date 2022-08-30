var ToOrganization="中国电子工业标准化技术协会信息技术应用创新工作委员会";//招标单位
var FromAddress="上海市浦东新区江南大道7号西南经协大厦16层";//招标单位
var FromOrganization="上海煦航网络科技有限公司";//授权厂商名称
var From2Address="江苏省南京市江南大道7号西南经协大厦16层";//投标单位名称
var From2Organization="上海XX网络科技有限公司";//投标单位地址
var ProjectName="全省组织系统综合管理信息化平台建设项目";//招标项目名称
var FromDate=" 2022年07月15日";//授权开始日期
var ToDate="2022年12月31日";//授权截止日期
var SignDate="2022年07月15日";//签署日期
var YearOfValidity="1";//质保有效时长

//授权书和服务承诺函中的书签名称
var BookMarkName_Enum={
    ToOrganization:"ToOrganization",
    FromAddress:"FromAddress",
    FromOrganization:"FromOrganization",
    From2Address:"From2Address",
    From2Organization:"From2Organization",
    ProjectName:"ProjectName",
    FromDate:"FromDate",
    ToDate:"ToDate",
    SignDate:"SignDate",
    YearOfValidity:"YearOfValidity"
}

//合同模板中的书签名称
var ContractName_Enum=
{
    bianhao:"bianhao",
    heji:"heji",
    heji2:"heji2",
    jiafang:"jiafang",
    jiafang2:"jiafang2",
    ntkolisense:"ntkolisense",
    ntkoproduct:"ntkoproduct",
    ntkoversion:"ntkoversion"
}

//wps 对象
var OfficeObject=(function(){
    const doc = wps.WpsApplication();
        if (!doc) {
            return null;
        }
        return doc; 
})();



/**
* @description 创建厂商授权书
* @param {boolean}IsAddSign 是否添加签名图片。true：添加；false(默认)：不添加
* @return {void} 无返回值
* @author peter 2022/08/24 
*/
function CreateCFA(IsAddSign=false)
{
    
    var Sel=OfficeObject.ActiveDocument.Application.Selection;
    InsertText({Text:"厂商授权书",Alignment:1,FontSize:22,FontBold:true,LineUnitAfter:1,Italic:true});
    InsertText({Text:"致：",TypeParagraphCount:1});
    InsertText({Text:ToOrganization,Alignment:0,FontBold:true,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.ToOrganization});
    InsertText({Text:"作为设在",CharacterUnitFirstLineIndent:2,TypeParagraphCount:1});
    InsertText({Text:"  "+FromAddress+"  ",Underline:1,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.FromAddress});
    InsertText({Text:"（制造厂商地址）的制造/生产 "});
    InsertText({Text:"  "+FromOrganization+"  ",Underline:1,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.FromOrganization});
    InsertText({Text:"（制造厂家名称）在此以制造厂的名义授权"});
    InsertText({Text:"  "+From2Address+"  ",Underline:1,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.From2Address});
    InsertText({Text:"的"});
    InsertText({Text:"  "+From2Organization+"  ",Underline:1,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.From2Organization});
    InsertText({Text:"在"});
    InsertText({Text:"  "+ProjectName+"  ",Underline:1,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.ProjectName});
    InsertText({Text:"代理我公司进行投标和后续的合同谈判和签署合同。"});
    InsertText({Text:"我们在此保证为上述公司就此次招标而提交的产品承担全部质量保证责任。",CharacterUnitFirstLineIndent:2,TypeParagraphCount:1});
    InsertText({Text:"本授权书的有效期限为",TypeParagraphCount:1});
    InsertText({Text:" "+FromDate+" ",IsAddBookMark:true,BookMarkName:BookMarkName_Enum.FromDate});
    InsertText({Text:"至"});
    InsertText({Text:" "+ToDate+" ",IsAddBookMark:true,BookMarkName:BookMarkName_Enum.ToDate});
    InsertText({Text:"。"});
    TypeParagraph(Sel,4);
    var table=AddTable(3,2,false);
    table.Rows.Item(1).Cells.Item(1).Range.Select();
    InsertText({Text:"出具授权书的制造厂家名称：",Alignment:2});
    Sel.MoveDown();
    InsertText({Text:"（公章）：",Alignment:2});
    Sel.MoveDown();
    InsertText({Text:"日期：",Alignment:2});
    table.Rows.Item(1).Cells.Item(2).Range.Select();
    InsertText({Text:" "+FromOrganization+" ",Underline:1});
    Sel.MoveDown();
    InsertText({Text:GetSpaceStr(FromOrganization.length*2+2),Underline:1});
    Sel.MoveDown();
    SignDate=" "+SignDate+" ";
    InsertText({Text:FormatStrWitdhSpaceStr(SignDate,FromOrganization.length*2-3),Underline:1,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.SignDate});
    table.Columns.Item(1).Width = Sel.Application.CentimetersToPoints(8.51);
    table.Columns.Item(2).Width = Sel.Application.CentimetersToPoints(6.52);
    if(IsAddSign)  //是否添加签名图片
    {
        table.Cell(2,2).Range.Select();
        Sel.MoveLeft();
        AddSignFromImages("sign.gif");
    }
    table.Range.Select();
    Sel.MoveDown();
}

/**
* @description 创建服务承诺函
* @param {boolean}IsAddSign 是否添加签名图片。true：添加；false(默认)：不添加
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function CreateSLC(IsAddSign=false)
{

    var Sel=OfficeObject.ActiveDocument.Application.Selection;
    InsertText({Text:"服务承诺函",Alignment:1,FontSize:22,FontBold:true,LineUnitAfter:1});
    InsertText({Text:"致：",TypeParagraphCount:1});
    InsertText({Text:ToOrganization,Alignment:0,FontBold:true,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.ToOrganization});
    InsertText({Text:"我公司郑重承诺：",TypeParagraphCount:1,CharacterUnitFirstLineIndent:2});
    InsertText({Text:"1、参加本次投标的",CharacterUnitFirstLineIndent:2,TypeParagraphCount:1});
    InsertText({Text:"  "+From2Organization+"  ",Underline:1,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.From2Organization});
    InsertText({Text:"所用我公司产品，均为专门针对在 "});
    InsertText({Text:"  "+ProjectName+"  ",Underline:1,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.ProjectName});
    InsertText({Text:",提供的正规合法产品。"});
    InsertText({Text:"2、本次投标响应产品均享受",CharacterUnitFirstLineIndent:2,TypeParagraphCount:1});
    InsertText({Text:" "+NumToChinese(YearOfValidity)+" ",Underline:1,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.YearOfValidity});
    InsertText({Text:"年的系统及设备免费质保服务，服务内容包括但不限于电话服务、损坏部件的更换。"});
    TypeParagraph(Sel,2);
    InsertText({Text:"对于以下情况造成的损坏不在保修范围内：",CharacterUnitFirstLineIndent:2,TypeParagraphCount:1});
    InsertText({Text:"（1）因用户人为因素造成损坏的；",CharacterUnitFirstLineIndent:2,TypeParagraphCount:1});
    InsertText({Text:"（2）由于不可抗拒因素造成损坏的。",CharacterUnitFirstLineIndent:2,TypeParagraphCount:1});
    TypeParagraph(Sel,4);
    var table=AddTable(3,2,false);
    table.Rows.Item(1).Cells.Item(1).Range.Select();
    InsertText({Text:"制造厂家名称：",Alignment:2});
    Sel.MoveDown();
    InsertText({Text:"（公章）：",Alignment:2});
    Sel.MoveDown();
    InsertText({Text:"日期：",Alignment:2});
    table.Rows.Item(1).Cells.Item(2).Range.Select();
    InsertText({Text:" "+FromOrganization+" ",Underline:1,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.FromOrganization});
    Sel.MoveDown();
    InsertText({Text:GetSpaceStr(FromOrganization.length*2+2),Underline:1});
    Sel.MoveDown();
    SignDate=" "+SignDate+" ";
    InsertText({Text:FormatStrWitdhSpaceStr(SignDate,FromOrganization.length*2-3),Underline:1,IsAddBookMark:true,BookMarkName:BookMarkName_Enum.SignDate});
    Sel.MoveDown();
    table.Columns.Item(1).Width = Sel.Application.CentimetersToPoints(8.51);
    table.Columns.Item(2).Width = Sel.Application.CentimetersToPoints(6.52);

    if(IsAddSign)
    {
        table.Cell(2,2).Range.Select();
        Sel.MoveLeft();
        AddSignFromImages("sign.gif");
    }
    table.Range.Select();
    Sel.MoveDown();
}

/**
* @description 打开templatefile文件夹下的文件
* @param {string}filename 文件名称
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function OpenTemplateFile(filename)
{
    OfficeObject.ActiveDocument.Application.Documents.Open(GetTemplateFileUrl().concat(filename),false,false,false);
}



/**
* @description 将指定文件插入到光标处
* @param {string}filename 文件的url地址
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function CombineFile(filename)
{
    OfficeObject.ActiveDocument.Application.Selection.InsertFile(filename);
}

/**
* @description 在光标处插入分页符
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function InsertBreak()
{
    var Sel=OfficeObject.ActiveDocument.Application.Selection;
    Sel.InsertBreak(7);
}

/**
* @description 在光标处插入文字
* @param {string}Text 要插入的文字
* @param {string}TypeParagraphCount 插入前要回车的次数，默认是0
* @param {string}FontName 设置段落中文字体的名称，默认“宋体”
* @param {string}FontNameEn 设置段落英文字体的名称，默认“Times New Roman”
* @param {number}FontSize 设置段落字体的大小 ，默认12
* @param {number}FontBold 设置段落字体是否加粗，默认false
* @param {number}Underline 设置段落下划线样式，默认0
* @param {boolean}Italic 设置段落是否倾斜，默认false
* @param {number}Alignment 设置段落对齐方式，默认0
* @param {number}OutlineLevel 设置段落大纲级别 ，默认10
* @param {number}LineUnitBefore 设置段落的段前间距（以网格线为单位），默认0
* @param {number}LineUnitAfter 设置段落的段后间距（以网格线为单位），默认0
* @param {number}LineSpacingRule 设置段落的行距格式，默认1
* @param {number}CharacterUnitFirstLineIndent 设置段落首行或悬挂缩进的值，默认0
* @param {number}SpaceBefore 设置段落的段前间距（以磅为单位），默认0
* @param {number}SpaceAfter 设置段落的段后间距（以磅为单位），默认0
* @param {number}LineSpacing 设置指定段落的行距，默认12
* @param {boolean}IsAddBookMark 是否添加为书签，默认false
* @param {string}BookMarkName 书签名称
* @return {void} 无返回值
* @author peter 2022/08/24 
*/
function InsertText({Text,TypeParagraphCount=0,FontName="宋体",FontNameEn="Times New Roman",FontSize=12,FontBold=false,Underline=0,Italic=false,Alignment=0,OutlineLevel=10,LineUnitBefore=0,LineUnitAfter=0,LineSpacingRule=1,CharacterUnitFirstLineIndent=0,SpaceBefore=0,SpaceAfter=0,LineSpacing,IsAddBookMark=false,BookMarkName}={})
{
    var Sel=OfficeObject.ActiveDocument.Application.Selection;
    TypeParagraph(Sel,TypeParagraphCount);
    ApplyFormat({Selection:Sel,FontName:FontName,FontNameEn:FontNameEn,FontSize:FontSize,FontBold:FontBold,Underline:Underline,Italic:Italic,Alignment:Alignment,OutlineLevel:OutlineLevel,LineUnitBefore:LineUnitBefore,LineUnitAfter:LineUnitAfter,LineSpacingRule:LineSpacingRule,CharacterUnitFirstLineIndent:CharacterUnitFirstLineIndent,SpaceBefore:SpaceBefore,SpaceAfter:SpaceAfter,LineSpacing:LineSpacing});
    var start=Sel.Start;
    Sel.TypeText(Text);
    if(IsAddBookMark)
    {
        var end =Sel.End;
        OfficeObject.ActiveDocument.Bookmarks.Add(GetBookMarkName(BookMarkName),OfficeObject.ActiveDocument.Range(start,end));
        OfficeObject.ActiveDocument.ActiveWindow.ActivePane.View.ShowBookmarks=true;
    }
}


/**
* @description 设置光标处的段落格式
* @param {object}Selection word的Selection对象
* @param {string}FontName 设置段落中文字体的名称，默认“宋体”
* @param {string}FontNameEn 设置段落英文字体的名称，默认“Times New Roman”
* @param {number}FontSize 设置段落字体的大小 ，默认12
* @param {number}FontBold 设置段落字体是否加粗，默认false
* @param {number}Underline 设置段落下划线样式，默认0
* @param {boolean}Italic 设置段落是否倾斜，默认false
* @param {number}Alignment 设置段落对齐方式，默认0
* @param {number}OutlineLevel 设置段落大纲级别 ，默认10
* @param {number}LineUnitBefore 设置段落的段前间距（以网格线为单位），默认0
* @param {number}LineUnitAfter 设置段落的段后间距（以网格线为单位），默认0
* @param {number}LineSpacingRule 设置段落的行距格式，默认1
* @param {number}CharacterUnitFirstLineIndent 设置段落首行或悬挂缩进的值，默认0
* @param {number}SpaceBefore 设置段落的段前间距（以磅为单位），默认0
* @param {number}SpaceAfter 设置段落的段后间距（以磅为单位），默认0
* @param {number}LineSpacing 设置指定段落的行距，默认12
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function ApplyFormat({Selection,FontName="宋体",FontNameEn="Times New Roman",FontSize="16",FontBold=true,Underline=0,Italic=false,Alignment=0,OutlineLevel=10,LineUnitBefore=1,LineUnitAfter=1,LineSpacingRule=1,CharacterUnitFirstLineIndent=0,SpaceBefore=0,SpaceAfter=0,LineSpacing=0}={})
{
    //设置段落中文字体的名称
    //设置段落英文字体的名称
    //设置段落字体的大小
    //设置段落字体是否加粗
    //设置段落对齐方式
    //设置段落大纲级别
    //设置段落的段前间距（以网格线为单位）
    //设置段落的段后间距（以网格线为单位）
    //设置段落格式的行距
    //设置段落首行或悬挂缩进的值
    //设置段落的段前间距（以磅为单位）
    //设置段落的段后间距（以磅为单位）
    //设置段落的行距（以磅为单位）

    Selection.Font.Name = FontName;
    Selection.Font.Name = FontNameEn;
    Selection.Font.Size = FontSize;
    Selection.Font.Bold = FontBold;
    Selection.Font.Underline = Underline;
    Selection.Font.Italic=Italic;
    Selection.Font.ItalicBi=Italic;
    Selection.ParagraphFormat.Alignment = Alignment;
    Selection.ParagraphFormat.OutlineLevel = OutlineLevel;
    Selection.ParagraphFormat.LineUnitBefore = LineUnitBefore;
    Selection.ParagraphFormat.LineUnitAfter = LineUnitAfter;
    Selection.ParagraphFormat.LineSpacingRule = LineSpacingRule;
    Selection.ParagraphFormat.CharacterUnitFirstLineIndent = CharacterUnitFirstLineIndent;
    Selection.ParagraphFormat.SpaceBefore = SpaceBefore;
    Selection.ParagraphFormat.SpaceAfter = SpaceAfter;
    Selection.ParagraphFormat.LineSpacing = LineSpacing;
}

/**
* @description 在光标处插入images文件夹下指定的图片
* @param {string}filename 图片的名称
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function AddSignFromImages(filename)
{
    var Sel=OfficeObject.ActiveDocument.Application.Selection;
    var range=OfficeObject.ActiveDocument.Range(Sel.Start,Sel.End);
    var url=GetUrlPath().replace("ui",'images/').concat(filename);
    if (url.startsWith("file:///"))
    {
        url = navigator.userAgent.indexOf("Linux") > -1?url.substr("file://".length):url.substr("file:///".length);
    }
    var shape=Sel.InlineShapes.AddPicture(url,false,true,range);
    shape.Height=19;
}

/**
* @description 在光标处插入表格
* @param {number}row 表格的行数
* @param {number}col 表格的列数
* @param {boolean}ishowborder 表格是否显示边框
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function AddTable(row,col,ishowborder) 
{
    var Sel=OfficeObject.ActiveDocument.Application.Selection;
    var table=OfficeObject.ActiveDocument.Tables.Add(Sel.Range, row, col)
    table.Borders.Enable=ishowborder;
    return table;
}

/**
* @description 给指定表格单元格设置值
* @param {object}table word中的表格对象
* @param {number}row 单元格的行号
* @param {number}cole 单元格的列号
* @param {string}value 要设置的单元格的内容
* @param {number}Alignment 对齐方式
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function SetCellVlue(table,row,cole,value,Alignment=0)
{
    table.Cell(row,cole).Range.Text=value;
    table.Cell(row,cole).Range.ParagraphFormat.Alignment=Alignment;
}

/**
* @description 合并表格中的指定区域
* @param {object}table word中的表格对象
* @param {number}row1 起始单元格的行号
* @param {number}col1 起始单元格的列号
* @param {number}row2 结束单元格的行号
* @param {number}col2 结束单元格的列号
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function CombinTableCells(table,row1,col1,row2,col2)
{ 
     var startcell=table.Cell(row1,col1).Range.Start; 
     var endcell=table.Cell(row2,col2).Range.End; 
     OfficeObject.ActiveDocument.Range(startcell, endcell).Select();
     OfficeObject.ActiveDocument.Application.Selection.Cells.Merge ();
}

/**
* @description 保护文档
* @param {boolean}isreadonly 是否保护文档
* @param {string}password 保护或接触保护的密码
* @param {number}type 保护的类型
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function SetReadOnly(isreadonly,password,type=2)
{
    if(isreadonly)
    {
        OfficeObject.ActiveDocument.Protect(type,false,password);
    }else{
        OfficeObject.ActiveDocument.Unprotect(password);
    }
    
}

/**
* @description 插入文本框
* @param {string}text 文本框内容
* @param {number}left 文本框X坐标
* @param {number}top 文本框Y坐标
* @param {number}width 文本框宽度
* @param {number}heigth 文本框高度
* @param {string}fontName 字体名称
* @param {string}fontSize 字体大小
* @param {boolean}fontBold 是否加粗
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function AddTextbox(text,left,top,width,heigth,fontName="宋体",fontSize=20,fontBold=true)
{
    var shape=OfficeObject.ActiveDocument.Shapes.AddTextbox(1,left,top,width,heigth);
    shape.TextFrame.TextRange.Text=text;
    shape.TextFrame.TextRange.Font.Name=fontName;
    shape.TextFrame.TextRange.Font.Size=fontSize;
    shape.TextFrame.TextRange.Font.Bold=fontBold;
    shape.Fill.Visible=false;
    shape.Line.Visible=false;
}


/**
* @description 在光标处回车指定次数
* @param {object}Selection word的Selection对象
* @param {number}count 回车的次数
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function TypeParagraph(Selection , count)
{
    for (var num = 1; num <= count; num++)
    {
        Selection.TypeParagraph();
    }
}

/**
* @description 是否显示书签标记
* @param {boolean}isshow 是否显示书签标记
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function IsShowBookMark(isshow)
{
    if( OfficeObject.ActiveDocument)
    {
        OfficeObject.ActiveDocument.ActiveWindow.ActivePane.View.ShowBookmarks=isshow;
        wps.ribbonUI.InvalidateControl("btnIsShowBookMark")
    }
}


/**
* @description 获取文档中服务承诺函或者授权书的所有书签，每份一组
* @return {array} 返回一个数组，每个数组包含每份授权书或服务承诺函构成的所有书签
* @author peter 2022/08/24 
*/
function GetProjectBookMarks()
{
    var i=1;
    var Bookmarks =[];
    var temp="";
    var temps={};
    for(var a=1;a<=OfficeObject.ActiveDocument.Bookmarks.Count;a++)
    {
        var bookmark=OfficeObject.ActiveDocument.Bookmarks.Item(a);
        if(bookmark.Name.indexOf(BookMarkName_Enum.From2Organization)>-1)
        {
            var index=bookmark.Name.replace(BookMarkName_Enum.From2Organization,"");
            var bookmark={};
            for (var key in BookMarkName_Enum) {
                temp= BookMarkName_Enum[key]+index;
                if(OfficeObject.ActiveDocument.Bookmarks.Exists(temp))
                {
                    bookmark[temp]=OfficeObject.ActiveDocument.Bookmarks.Item(temp).Range.Text;
                }
            }
            Bookmarks.push(bookmark);
        }
    }
    return Bookmarks;
}

/**
* @description 获取文档中服务承诺函或者授权书的所有投标单位的书签
* @return {array} 返回一个数组，每个书签成员包含投标单位的书签名称以及书签的内容
* @author peter 2022/08/24 
*/
function GetProjectBookMarksForName()
{
    var Bookmarks = [];
    for(var a=1;a<=OfficeObject.ActiveDocument.Bookmarks.Count;a++)
    {
        var book=OfficeObject.ActiveDocument.Bookmarks.Item(a);
        if(book.Name.indexOf(BookMarkName_Enum.From2Organization)>-1)
        {
            Bookmarks.push({ "bookmarkName":book.Name, "bookmarkValue":book.Range.Text });
        }
    }
    return Bookmarks;
}


/**
* @description 按数字顺序获取书签名称
* @return {string} 返回书签名称
* @author peter 2022/08/24 
*/
function GetBookMarkName(name)
{
    var i=0;
    var temp;
    do{
        i++;
        temp=name+i;
    }while(OfficeObject.ActiveDocument.Bookmarks.Exists(temp))
    return temp;
}


/**
* @description 检查合同模板中的要求的书签是否都存在
* @return {boolean} 返回是否合法
* @author peter 2022/08/24 
*/
function CheckContractBookMarks()
{
    for (const key in ContractName_Enum) {
        if(!OfficeObject.ActiveDocument.Bookmarks.Exists(ContractName_Enum[key]))
        {
            return false;
        }
    }
    return true;
}

/**
* @description 给指定书签赋值
* @param {string}name 书签名称
* @param {string}value 书签的值
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function SetBookMarkValue(name,value)
{
    if(OfficeObject.ActiveDocument.Bookmarks.Exists(name))
    {
        var bookmark=OfficeObject.ActiveDocument.Bookmarks.Item(name);
        bookmark.Select();
        var start=bookmark.Range.Start;
        var end=start+value.length;
        bookmark.Range.Text=value;
        var Range=OfficeObject.ActiveDocument.Range(start,end);
        OfficeObject.ActiveDocument.Bookmarks.Add(name,Range);
    }else{
        ShowMessage("指定书签不存在："+name);
    }
}

/**
* @description 启动或关闭强制留痕
* @param {boolean}istrack 启动或关闭留痕
* @param {string}password 启动或关闭留痕的密码
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function EnterTrackRevisions(istrack,password)
{
    if(istrack)
    {
        if(OfficeObject.ActiveDocument.ProtectionType==-1)
        {
            OfficeObject.ActiveDocument.Protect(0,false,password);
        }
    }else{
        if(OfficeObject.ActiveDocument.ProtectionType==0)
        {
            OfficeObject.ActiveDocument.Unprotect(password);
        }
    }
    //刷新一下开启强制留痕按钮和关闭强制留痕按钮状态
    InvalidateRevisionsControl();
}

/**
* @description 接受或拒绝所有修订
* @param {boolean}isaccept 接受或者拒绝
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function DoAllRevisions(isaccept) {
    var type=OfficeObject.ActiveDocument.ProtectionType;
    if(type==0)
    {
        EnterTrackRevisions(false,GlobalData.PassWord);
    }
    if(isaccept) {
        OfficeObject.ActiveDocument.AcceptAllRevisions(); //接受所有的痕迹修订
    }
    else {
        OfficeObject.ActiveDocument.RejectAllRevisions(); //拒绝所有痕迹修订
    }
    if(type==0)
    {
        EnterTrackRevisions(true,GlobalData.PassWord);
    }
}
/**
* @description 清空文档中的内容
* @return {boolean} 是否清除成功，文档被保护无法清除
* @author peter 2022/08/24 
*/
function ClearDocument()
{
    if(CheckDocumentCanEdit())
    {
        OfficeObject.Selection.WholeStory();
        OfficeObject.Selection.TypeBackspace();
        return true;
    }else
    {
        return false;
    }
}

/**
* @description 判断文档是否处于保护模式
* @return {boolean} 是否被保护
* @author peter 2022/08/24 
*/
function CheckDocumentCanEdit()
{
    if(OfficeObject.ActiveDocument.ProtectionType==-1||OfficeObject.ActiveDocument.ProtectionType==0)
    {
        return true;
    }else
    {
        ShowMessage("文档被保护，无法修改！");
        return false;
    }

}
/**
* @description 产生指定空格数量的字符串
* @param {number}count 空格的数量
* @return {string} 返回指定长度的空格字符串
* @author peter 2022/08/24 
*/
function GetSpaceStr(count)
{
    var result="";
    for(var i=0;i<count;i++)
    {
        result+=" ";
    }
    return result;
}


/**
* @description 将字符串写入指定文件
* @param {string}filename 文件名称，不需文件的路径，只写名称。
* @param {string}value 内容
* @param {boolean}showmsg 是否显示保存结果
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function WriteFile(filename,value,showmsg=false)
{
    wps.FileSystem.writeFileString(filename,value);
    if(showmsg)
    {
        if(wps.FileSystem.readFileString(filename)!=null)
        {
            ShowMessage("保存成功");
        }else
        {
            ShowMessage("保存失败");
        }
    }
    
}

/**
* @description 从指定文件中读取内容
* @param {string}filename 文件名称，不需文件的路径，只写名称。
* @param {boolean}showerr 是否显示错误信息
* @return {string} 读取的内容
* @author peter 2022/08/24 
*/
function ReadFile(filename,showerr=false)
{
    let tt=wps.FileSystem.readFileString(filename);
    if(tt==null&&showerr)
    {
        ShowMessage("数据不存在");
    }
    return tt;
}

/**
* @description 保存GlobalData到本地
* @param {boolean} showmsg 是否显示保存结果
* @return  {void} 无返回值
* @author peter 2022/08/24 
*/
function SaveGlobalData(showmsg=false)
{
    WriteFile(GlobalData.JsonName,JSON.stringify(GlobalData),showmsg);
}

/**
* @description 从本地读取GlobalData
* @return {object} 返回GlobalData对象
* @author peter 2022/08/24 
*/
function ReadGlobalData()
{

    var  jsonstr =ReadFile(GlobalData.JsonName);
    if(jsonstr!=null)
    {
        var temp=JSON.parse(jsonstr);
        if(GlobalData.Version==temp.Version)
        {
            GlobalData = JSON.parse(jsonstr); 
        }
    }
    return GlobalData;
}



/**
* @description datagrid 单元格提示信息
* @param {string} value 提示内容
* @return {string} 返回html字符串
* @author peter 2022/08/24 
*/
function formatCellTooltip(value){  
    return "<span title='" + value + "'>" + value + "</span>";  
}  

/**
* @description 选择一个本地文件夹
* @return {string} 返回选择的文件夹路径
* @author peter 2022/08/24 
*/
function SelectSaveDir()
{
    var dialog=OfficeObject.FileDialog(4);
    dialog.Title="请选择要保存的目录";
    if(dialog.Show())
    {
        var dir=dialog.SelectedItems.Item(1).concat(navigator.userAgent.indexOf("Linux")>-1?"/":"\\");
        $('#saveDir').textbox("setValue",dir);
        return dir;
    }
    return null;
}

/**
* @description 选择一个本地文件
* @return {string} 返回选择的文件路径
* @author peter 2022/08/24 
*/
function SelectFilePath()
{
    var dialog=OfficeObject.FileDialog(3);
    dialog.Title="请选择文件";
    dialog.Filters.Add("Office文档","*.doc;*.docx;")
    if(dialog.Show())
    {
        var path=dialog.SelectedItems.Item(1);
        $('#FilePath').textbox("setValue",path);
        return path;
    }
    return null;
}

/**
* @description 根据当天已创建的合同数量获取新的合同编号
* @return {string} 返回合同编号
* @author peter 2022/08/24 
*/
function GetContractBianHao()
{
   var bianhao=0;
   var date= new Date().format("yyyyMMdd");
   if(GlobalData.Contract.LastDate==date)
   {
        bianhao=GlobalData.Contract.Count+1;
   }else
   {
        bianhao=1;
   }
   GlobalData.Contract.Count=bianhao;
   GlobalData.Contract.LastDate=date;
   SaveGlobalData();
   bianhao=bianhao+"";
   if(bianhao.length==1)
   {
       bianhao=date+"00"+bianhao;
   }else if(bianhao.length==2)
   {
        bianhao=date+"0"+bianhao;
   }else
   {
        bianhao=date+bianhao;
   }
   return bianhao;
}

            
//自动读取一下本地的数据            
ReadGlobalData();