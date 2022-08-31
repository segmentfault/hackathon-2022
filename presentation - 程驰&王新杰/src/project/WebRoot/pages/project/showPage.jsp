<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ include file="/pages/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCClasses HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>用户维护</title>
		<link href="<%=basePath%>/css/page.css" rel="stylesheet"
			type="text/css" />

		<script type="text/javascript" language="javascript">
	     function checkForm(){
         var validate=$('form').validate('submitValidate');
        if( validate== true){
            var aa = window.confirm("确定要提交吗？");
            if (aa == true) { 
                document.forms[0].submit();
            } else {
                return false;
            
        }
	}
	}
	 
</script>
	</head>

	<body>
		<h3 style="background-color: gray">

			<font color="#ffffff"><a>项目审批
			</a> </font>

		</h3>
		<div id="user" align="center">
			<form action="${base }/servlet/ProjectServlet?method=saveCheckinfo"
				method="post" id="myform">
				<table border="1">
					<tr>
						<td  width="20%">
							项目名：
						</td>
						<td> 
							<input id="id" name="id" value="${project.id }" type="hidden">  
							${project.name }
						</td>
					</tr>
					<tr>
						<td >
							负责人：
						</td>
						<td>
							${project.header}
						</td>
					</tr>
					
					<tr>
						<td >
							联系电话：
						</td>
						<td>
							${project.tel }
						</td>
					</tr>
					<tr>
						<td >
							所在学院：
						</td>
						<td>
							${project.college}
						</td>
					</tr>
					<tr>
						<td >
							项目参加人：
						</td>
						<td>
							${project.participant }
						</td>
					</tr>
					<tr>
						<td >
							指导教师：
						</td>
						<td>
							${project.teacher }
						</td>
					</tr>
					<tr>
						<td >
							联系电话：
						</td>
						<td>
							${project.teachertel }
						</td>
					</tr>
					
					<tr>
						<td >
							导师职称：
						</td>
						<td>
							${project.teachertitle}
						</td>
					</tr>
					<tr>
						<td >
							所在单位：
						</td>
						<td>
							${project.unit}
						</td>
					</tr>
					<tr>
						<td >
							填表日期：
						</td>
						<td>
							${project.signdate }
						</td>
					</tr>
					<tr>
						<td >
							申请表：
						</td>
						<td>
							<tag:upload base="${base}" fileValue="${project.fileurl}" downname="${project.name}" 
											fileId="fileurl" fileName="fileurl" readOnly="true"
											fileType=".*" fileCss="g_box" btnCss="box"></tag:upload> 
						</td>
					</tr>
					<tr>
						<td >
							审批情况：
						</td>
						<td>
							<c:if test="${project.verify=='0'}">未审核</c:if>
								<c:if test="${project.verify=='1'}">通过</c:if>
								<c:if test="${project.verify=='2'}">未通过</c:if>
						</td>
					</tr>
					
					<tr>
						<td width="20%">
							验收情况：
						</td>
						<td>
							${project.check}
						</td>
					</tr>
					
					<tr>
						<td width="20%">
							日期：
						</td>
						<td>
							${project.checkdate}
						</td>
					</tr>
					<tr>
						<td width="20%">
							评价情况：
						</td>
						<td> 
							<textarea rows="5" cols="50" id="evaluate" name="evaluate" class="required"
								 data-tip="请输入评价情况"
								data-valid="isNonEmpty" data-show="unitshow" readonly="readonly"
								data-error="评价情况不能为空">${project.evaluate}</textarea>
							<span style="color: red;">*</span>
							<span style="color: red;" id="unitshow"></span>
						</td>
					</tr>
					
				</table>
				<table border="1">
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
							资金金额
						</th>
						<th>
							资金日期
						</th>
						<th>
							资金用途
						</th>
						
						 
					</tr>
					<c:forEach var="item" varStatus="i" items="${listM}">
						<tr  >
							<td align="center">
								${(pagenum-1)*currentp+i.index+1}
							</td>
							<td align="center">
								${item.pname}
							</td>

							<td align="center">
								${item.header}
							</td>
							<td align="center">
								${item.money }
							</td>
							<td align="center">
								${item. givedate}
							</td>
							<td align="center">
								${item.purpose }
							</td>

							 
						</tr>
					</c:forEach>
					<tr>
						<td colspan="6">
							 
							<input class="box" type="button" value="返  回"
								onclick="javascript:history.go(-1);" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		<script type="text/javascript">
		$("#myform").validate({


    });
		</script>
	</body>

</html>
