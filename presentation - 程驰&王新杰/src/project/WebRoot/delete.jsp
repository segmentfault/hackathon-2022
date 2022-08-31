<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>显示所有文件</title>
    <link href="<%=basePath%>css/list.css" rel="stylesheet" type="text/css" />
  </head>

  <body>
  <h4 style="background-color:gray"><font color="#ffffff"><a> 请选择您要删除的文件</a></font></h4>
  	<center>
   <table>
   		<c:forEach items="${fileList}" var="fileName">
   	<tr>
   		<td>文件名：</td>
   		<td><a href="DeleteServlet?filename=${fileName }">${fileName }</a><br/></td>
   	</tr>
   	 	</c:forEach>
   </table>
    </center>
  </body>

