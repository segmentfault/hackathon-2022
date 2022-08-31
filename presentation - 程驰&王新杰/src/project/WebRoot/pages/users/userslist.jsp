<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ include file="/pages/taglibs.jsp"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="${base }">

		<title>用户管理</title>
		<link href="${base }/css/list.css" rel="stylesheet" type="text/css" />
	</head>

	<body>

		
<form action="${base }/servlet/UsersServlet?method=findAllUsers"
				method="post">
				<div align="center" >
					<table border="1" >
					<tr >
						<th align="left" >	
							管理员名： 
							<input type="text" id="uname" name="uname" value="${users.uname }"  >	 	
							<input type="submit" value="搜  索" />
						</th>
						
						<th >
						<input type="button" onclick="location.href='${base }/servlet/UsersServlet?method=addUsers'" value="添  加" />
					<input class="box" type="button" value="返 回"
						onClick="javascript:history.go(-1);" />
						</th>

					</tr>
					</table>
					
				</div>
		</form>

		<center> 
			<table border="1">
				<tr>
					<th align="center">
							序号
						</th>
						<th>
							用户
						</th>
						<th>
							姓名
						</th> 
						<th>
							类别
						</th> 
						<th>
						联系电话
						</th> 
						<th>
							操作
						</th>
				</tr>
				<c:forEach var="item" varStatus="i" items="${list}"> 
						<tr>
							<td>
								${(pagenum-1)*currentp+i.index+1}
							</td>
							<td width="100px;">
								${item.uname}
							</td>
							
							<td>
								${item.urealname}
							</td> <td>
								<c:if test="${item.utype==1}">教学秘书</c:if>
								<c:if test="${item.utype==2}">领导</c:if> 
							</td> 
							<td>
								${item.utel}
							</td> 
							
								<td align="center" width="100px;">
									<a
										href="${base }/servlet/UsersServlet?method=updatePage&uid=${item.uid}">修改</a>|
									<a
										href="${base }/servlet/UsersServlet?method=deleteUsers&uid=${item.uid}">删除</a>
								</td>
							
						</tr>
					</c:forEach>
			</table>
			${pagingModel.pageLink }${pagingModel.pageInfo }

		</center>
	</body>
</html>
