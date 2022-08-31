
<%@ page language="java"  pageEncoding="gb2312"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>大学生科技创新项目评审系统</title>
   <style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
.STYLE1 {
	color: #43860c;
	font-size: 12px;
}
-->
</style>
    <script>
function tick() {
var ye,mo,da
var hours, minutes, seconds, xfile;
var intHours, intMinutes, intSeconds;
var today;
today = new Date();
ye = today.getYear()+1900;
mo = today.getMonth()+1;
if (mo < 10) {
mo = "0"+mo;
}
da = today.getDate();
if (da < 10) {
da = "0"+da;
}
intHours = today.getHours();
intMinutes = today.getMinutes();
intSeconds = today.getSeconds();
if (intHours == 0) {
hours = "12:";
xfile = "PM";//午夜
} else if (intHours < 12) {
hours = intHours+":";
xfile = "AM";//上午
} else if (intHours == 12) {
hours = "12:";
xfile = "PM";//正午
} else {
intHours = intHours - 12
hours = intHours + ":";
xfile = "PM";//下午
}
if (intHours < 10) {
hours = "0"+intHours+":";
}
if (intMinutes < 10) {
minutes = "0"+intMinutes+":";
} else {
minutes = intMinutes+":";
}
if (intSeconds < 10) {
seconds = "0"+intSeconds+" ";
} else {
seconds = intSeconds+" ";
}
var color, size, face, out;
	var text = "black";
	var font_size = 1;
    var font_face = "Arial";
	color = (text)? text : "black";
	size = (font_size)? font_size : 1;
	face = (font_face)? font_face : "Arial";
timeString = "<font style='font-size:14px;'>";
timeString += ye+"-"+mo+"-"+da+"  ";
timeString += hours+minutes+seconds+" "+xfile+"&nbsp;&nbsp;&nbsp;";
Clock.innerHTML = timeString;
window.setTimeout("tick();", 100);
}
window.onload = tick;
</script>

  </head>

  <body >
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed;">
  <tr>
    <td height="9" style="line-height:9px; background-image:url(<%=basePath%>images/main_04.gif)"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="97" height="9" background="<%=basePath%>images/main_01.gif">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="47" background="<%=basePath%>images/main_09.gif"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="38" height="47" background="<%=basePath%>images/main_06.gif">&nbsp;</td>
        <td width="59" valign="top"><table width="100%" height="28" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td height="28" background="<%=basePath%>images/main_07.gif">&nbsp;</td>
          </tr>
        </table>
          <br /></td>
        <td width="155" background="<%=basePath%>images/main_08.gif">&nbsp;</td>
        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="92%" height="23" valign="bottom">&nbsp;</td>
            <td width="8%" valign="bottom"><a href="servlet/LoginServlet?method=loginout" target="_parent">退出</a></td>
          </tr>
        </table></td>
        <td width="200" background="<%=basePath%>images/main_11.gif"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="11%" height="23">&nbsp;</td>
            <td width="89%" valign="bottom"><font id="Clock" align='center'>&nbsp;
	  
	  </font></td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="5" style="line-height:5px; background-image:url(<%=basePath%>images/main_18.gif)"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="180" background="<%=basePath%>images/main_16.gif"  style="line-height:5px;">&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    </table></td>
  </tr>
</table>
  </body>
</html>

