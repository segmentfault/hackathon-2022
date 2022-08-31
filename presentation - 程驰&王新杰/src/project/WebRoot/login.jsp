<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/taglibs.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>登录界面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<LINK href="images/public.css" type=text/css rel=stylesheet>
	<LINK href="images/login.css" type=text/css rel=stylesheet>
	<script type="text/javascript" language="javascript">
	
	
</script>
	<script type="text/javascript" src="<%=basePath%>JavaScript/user.js"></script>
  </head>
  <body>
   <form id="submitform" action="servlet/LoginServlet?method=login"  method="post" onsubmit="return checkForm()">
   		<input type="hidden" name="action" value="login">
  <DIV id=div1>
  <TABLE id=login height="100%" cellSpacing=0 cellPadding=0 width=800 align=center>
    <TBODY>
      <TR id=main>
        <TD>
          <TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%">
            <TBODY>
              <TR>
                <TD colSpan=4>&nbsp;</TD>
              </TR>
              <TR height=30>
                <TD width=380>&nbsp;</TD>
                <TD>&nbsp;</TD>
                <TD>&nbsp;</TD>
                <TD>&nbsp;</TD>
              </TR>
              <TR height=40>
                <TD rowSpan=4>&nbsp;</TD>
                <TD>用户名：</TD>
                <TD>
                  <INPUT class=textbox id="userName" name="userName" value="">
                  <input type="hidden" name="type" id="type">
                </TD>
                <TD width=120>&nbsp;</TD>
              </TR>
              <TR height=40>
                <TD>密　码：</TD>
                <TD>
                  <INPUT class=textbox id="password" type="password" name="password" value="">
                </TD>
                <TD width=120>&nbsp;</TD>
              </TR>
              <TR height=40>
                <TD>验证码：</TD>
                <TD>
                	<input name="chknumber" type="text" id = "number" maxlength="4" class="chknumber_input" style="width: 55px"/>
        			<input type="button" id="code" onClick="createCode()" class="btn" style="background:url('<%=basePath %>images/yz.jpg');"/>
                </TD>
              </TR>
              <TR height=40>
                <TD><br></TD>
                <TD align="center">
                  <INPUT id=btnLogin type="submit" value="管理员登录 "  onclick="validate(1);">&nbsp;
                     <INPUT id=button type="submit" value="学生登录" onclick="validate(2);">
                    
                </TD>
                <TD width=120> <a href="${base }/servlet/StudentServlet?method=addStudent" style="left: 50px;font-size: 18px;">注册</a></TD>
              </TR>
              <TR height=110>
                <TD colSpan=4>&nbsp;</TD>
              </TR>
            </TBODY>
          </TABLE>
        </TD>
      </TR>
      <TR id=root height=104>
        <TD>&nbsp;</TD>
      </TR>
    </TBODY>
  </TABLE>
</DIV>
<DIV id=div2 style="DISPLAY: none"></DIV>
</form>
  </body>
</html>
