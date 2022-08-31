<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ include file="/pages/taglibs.jsp"%>


<!DOCFile HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="${base }">

		<title>用户管理</title>
		<link href="${base }/css/list.css" rel="stylesheet"
			type="text/css" />
	</head>

	<body>
		<form action="${base }/servlet/FileServlet?method=findAllFile" method="post">  
				<div align="center" >
					<table border="1" >
					<tr >
						<th align="left" >	
							文档名名： 
							<input type="text" id="name" name="name" value="${file.name }"  >	 	
							<input type="text" id="flag" name="flag" value="${flag }"  >	
							<input type="submit" value="搜  索" />
						</th>
						
						<th >
						<c:if test="${flag!='2'}">
						<input type="button" onclick="location.href='${base }/servlet/FileServlet?method=addFile'" value="添  加" /></c:if>
					<input class="box" type="button" value="返 回"
						onClick="javascript:history.go(-1);" />
						</th>

					</tr>
					</table>
					
				</div>
		</form>
			<div align="center" id="studentlist">
				<table border="1"  >
					<tr>
						<th align="center">
							序号
						</th>
						<th>
							文件名
						</th>
						
						<th>
							下载
						</th> 
						<c:if test="${flag!='2'}">
						<th>
							操作
						</th>
						</c:if>

					</tr>
					<c:forEach var="item" varStatus="i" items="${list}"> 
						<tr>
							<td>
								${(pagenum-1)*currentp+i.index+1}
							</td>
							<td width="100px;">
								<span class="codetype" >${item.name}</span>
							</td>
							
							<td>
								<a href="${base }/servlet/FileDownLoadServlet?name=${item.name}&url=${item.url}">下载</a>
							</td>
							
						<c:if test="${flag!='2'}">
								<td align="center" width="100px;">
									<a
										href="${base }/servlet/FileServlet?method=updatePage&id=${item.id}">修改</a>|
									<a
										href="${base }/servlet/FileServlet?method=deleteFile&id=${item.id}">删除</a>
								</td>
								</c:if>
							
						</tr>
					</c:forEach>
				</table>
				${pagingModel.pageLink }${pagingModel.pageInfo }
			</div>
		</form>
		
    <script type="text/javascript" src="${base }/js/data.js"></script>
		<script type="text/javascript">
		$("#myform").validate({});
		</script>
	</body>
</html>
