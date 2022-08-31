<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ include file="/pages/taglibs.jsp"%>


<!DOCStudent HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="${base }">

		<title>班级管理</title>
		<link href="${base }/css/list.css" rel="stylesheet"
			type="text/css" />
	</head>

	<body>
		<form action="${base }/servlet/ProjectServlet?method=findAllProject" method="post">
			<div align="center">
				<h1>
					项目列表
				</h1>


			</div>
			
			
				<div align="center" >
					<table border="1" >
					<tr >
						<th align="left" >	
						审核状态:<select id="verify"  name="verify" class="required"  >
							<option value="">--状态--</option>
							<option value="0">未审核</option>
							<option value="1">通过审核</option>
							<option value="2">未通过</option> 
							</select>
						项目名称：<input type="text" id="name" name="name" value="${project.name }"  >
						
						指导教师：<input type="text" id="teacher" name="teacher" value="${project.teacher}"  >  
							<input id="flag" name="flag" value="${flag }" type="hidden">		
							<script type="text/javascript"> $("#verify").val(${project.verify});</script>	 	
							<input type="submit" value="搜  索" />
						</th>
						
						<th > 
					<input class="box" type="button" value="返 回"
						onClick="javascript:history.go(-1);" />
						</th>

					</tr>
					</table>
					
				</div>
			<div align="center" id="studentlist">
				<table border="1"  >
					<tr>
						<th align="center">
							序号
						</th>
						<th>
							项目名称
						</th>
						<th>
							负责人
						</th> 
						<th>
							电话
						</th>  
						<th>
							所在学院
						</th>
						<th>
							指导教师
						</th> 
						<th>
							电话
						</th> 
						<th>
							导师职称
						</th> 
						<th>
							填表日期
						</th> 
						<th>
							申请人
						</th> 
						
						<th>
							操作
						</th> 
					</tr>
					<c:forEach var="item" varStatus="i" items="${list}"> 
						<tr>
							<td >
								${(pagenum-1)*currentp+i.index+1}
							</td>
							<td  >
								${item.name}
							</td>
							
							<td>
								${item.header}
							</td>
							<td>
								${item. tel}
							</td>
							<td>
								${item.college }
							</td>
							<td>
								${item.teacher }
							</td>
							<td>
								${item.teachertel }
							</td><td>
								${item.teachertitle }
							</td>
							<td>
								${item.signdate }
							</td>
							<td>
								${item.uname}
							</td>
							
							
								
							<td><c:if test="${flag==3}">
								 	 	<a href="${base }/servlet/ProjectServlet?method=updatePage&id=${item.id}&flag=3">添加资金</a>  </c:if>
										  <a href="${base }/servlet/ProjectServlet?method=updatePage&id=${item.id}&flag=5">查看</a> 
							</td>
						</tr>
					</c:forEach>
				</table>
				${pagingModel.pageLink }${pagingModel.pageInfo }
			</div>
		</form>
	</body>
</html>
