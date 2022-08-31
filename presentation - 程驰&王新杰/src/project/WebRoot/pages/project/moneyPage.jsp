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

			<font color="#ffffff"><a>资金添加 </a> </font>

		</h3>
		<div id="user" align="center">
			<form action="${base }/servlet/ProjectServlet?method=saveMoney"
				method="post" id="myform">
				<table border="1">
					<tr>
						<td width="20%">
							项目名：
						</td>
						<td>
							${project.name }
						</td>
					</tr>
					<tr>
						<td>
							负责人：
						</td>
						<td>
							${project.header}
						</td>
					</tr>

					<tr>
						<td>
							联系电话：
						</td>
						<td>
							${project.tel }
						</td>
					</tr>
					<tr>
						<td>
							所在学院：
						</td>
						<td>
							${project.college}
						</td>
					</tr>
					<tr>
						<td>
							项目参加人：
						</td>
						<td>
							${project.participant }
						</td>
					</tr>
					<tr>
						<td>
							指导教师：
						</td>
						<td>
							${project.teacher }
						</td>
					</tr>
					<tr>
						<td>
							联系电话：
						</td>
						<td>
							${project.teachertel }
						</td>
					</tr>

					<tr>
						<td>
							导师职称：
						</td>
						<td>
							${project.teachertitle}
						</td>
					</tr>
					<tr>
						<td>
							所在单位：
						</td>
						<td>
							${project.unit}
						</td>
					</tr>
					<tr>
						<td>
							填表日期：
						</td>
						<td>
							${project.signdate }
						</td>
					</tr>
					<tr>
						<td>
							申请表：
						</td>
						<td>
							<tag:upload base="${base}" fileValue="${project.fileurl}"
								downname="${project.name}" fileId="fileurl" fileName="fileurl"
								readOnly="true" fileType=".*" fileCss="g_box" btnCss="box"></tag:upload>
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
						
						<th>
							操作
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

							<td align="center"> 
									<a
										href="${base }/servlet/ProjectServlet?method=updatePage&id=${project.id}&mid=${item.id}&flag=3">修改</a>|
									<a
										href="${base }/servlet/ProjectServlet?method=deleteMoney&pid=${project.id}&id=${item.id}">删除</a> 
							</td>
						</tr>
					</c:forEach>
				</table>
				<table border="1">
					<tr>
						<td width="20%">
							资金金额：
						</td>
						<td>
							<input type="text" id="money" name="money" class="required"
								value="${money.money}" data-tip="请输入资金金额"
								data-valid="isNonEmpty||onlyInt" data-show="teachertitleshow"
								data-error="资金金额不能为空||金额只能是数字">
							<span style="color: red;">*</span>
							<span style="color: red;" id="teachertitleshow"></span>
						</td>
					</tr>
					<tr>
						<td width="20%">
							资金日期：
							
							<input id="pid" name="pid" value="${project.id }" type="hidden">
							
							<input id="id" name="id" value="${money.id }" type="hidden">
						</td>
						<td>
							<input type="text" id="givedate" name="givedate" class="required"
								onfocus="WdatePicker()" value="${money.givedate }"
								data-tip="请输入资金日期" data-valid="isNonEmpty"
								data-show="signdateshow" data-error="资金日期不能为空">
							<span style="color: red;">*</span>
							<span style="color: red;" id="signdateshow"></span>
						</td>
					</tr>
					<tr>
						<td width="20%">
							资金用途：
						</td>
						<td>
							<textarea rows="5" cols="50" id="purpose" name="purpose"
								class="required" data-tip="请输入资金用途" data-valid="isNonEmpty"
								data-show="unitshow" data-error="资金用途不能为空">${money.purpose}</textarea>
							<span style="color: red;">*</span>
							<span style="color: red;" id="unitshow"></span>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<input type="button" value="提  交" onclick="checkForm()" />
							<input class="box" type="button" value="返  回"
								onclick="location.href='${base}/servlet/ProjectServlet?method=findAllProject&flag=3&verify=1'" />
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
