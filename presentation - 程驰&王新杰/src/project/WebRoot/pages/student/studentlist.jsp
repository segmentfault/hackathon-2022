<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ include file="/pages/taglibs.jsp"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="${base }">

		<title>学生列表理</title>
		<link href="${base }/css/list.css" rel="stylesheet" type="text/css" />
	</head>

	<body>
	<form action="${base }/servlet/StudentServlet?method=findAllStudent"
				method="post">
<div align="center" >
					<table border="1" >
					<tr >
						<th align="left" >	
							学生名： 
							<input type="text" id="name" name="name" value="${student.name }"  >	 	
							<input type="submit" value="搜  索" />
						</th>
						
						<th >
						 
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
							用户名
						</th>
						<th>
							姓名
						</th> 
						<th>
							学号
						</th> 
						<th>
						联系电话
						</th> 
						<th>
						学院
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
								${item.name}
							</td>
							
							<td>
								${item.realname}
							</td> 
							<td>
								${item.code}
							</td> 
							<td>
								${item.tel}
							</td> 
							
							<td>
								${item.college}
							</td> 	
							<td align="center" width="100px;">
									 <a
										href="${base }/servlet/StudentServlet?method=deleteStudent&id=${item.id}">删除</a>
								</td>
							
						</tr>
					</c:forEach>
			</table>
			${pagingModel.pageLink }${pagingModel.pageInfo }

		</center>
	</body>
</html>
