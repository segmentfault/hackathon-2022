<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>人员的添加</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link href="<%=basePath%>css/list.css" rel="stylesheet" type="text/css" />
  </head>
  
  <body>
    <h4 style="background-color:gray"><font color="#ffffff"><a>人员的添加</a></font></h4>
    <center>
    <form action="/gshszsys/UserServlet" method="post">
  	<input type="hidden" name="action" value="addUser">
  	<table>
  	<tr>
  	<td>用户名：</td><td><input type="text" name="username"/></td>
  	</tr>
  	<tr>
  	<td>密码：</td><td><input type="password" name="userpass"/></td>
  	</tr>
  	<tr>
  	<td>性别：</td><td><input type="radio" name="usersex" checked="checked" value="1"/>男<input type="radio" name="usersex"  value="2"/>女 </td>
  	</tr>
  	<tr>
  	<td>联系方式：</td><td><input type="text" name="userphone"/></td>
  	</tr>
  	<tr>
  	<td>电子邮箱：</td><td><input type="text" name="useremail"/></td>
  	</tr>
  	</table>
  	<br/>
  	<input type="submit" value="注 - 册">
  	</form>
    </center>
  </body>
</html>
