<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ include file="/pages/taglibs.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<html>
	<head>
		<base href="<%=basePath%>">

		<title>项目维护</title>
		<link href="<%=basePath%>/css/page.css" rel="stylesheet"
			type="text/css" />

		<script type="text/javascript" language="javascript">
	  function checkForm(){
         var validate=$('form').validate('submitValidate');
        if( validate== true){
            var aa = window.confirm('确定要保存吗？');
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

			<font color="#ffffff"><a><c:if test="${project==null}">添加项目</c:if>
					<c:if test="${project!=null}">修改项目</c:if>
			</a> </font>

		</h3>
		<div id="user" align="center">
			<form action="${base }/servlet/ProjectServlet?method=saveProject"
				method="post" id="myform">
				<table border="1">
					<tr>
						<td width="20%">
							项目名：
						</td>
						<td>
							<input id="uid" name="uid" value="${project.uid }" type="hidden">
							<input id="id" name="id" value="${project.id }" type="hidden"> 
							<input id="verify" name="verify" value="${project.verify }" type="hidden">
							<input id="flag" name="flag" value="${flag}" type="hidden">
							<input type="text" id="name" name="name"
								value="${project.name }"   class="required"
								data-tip="请输入项目名" data-valid="isNonEmpty" data-show="nameshow"
								data-error="项目名不能为空">

							<span style="color: red;">*</span>
							<span style="color: red;" id="nameshow"></span>
						</td>
					</tr>
					<tr>
						<td width="20%">
							负责人：
						</td>
						<td>
							<input type="text" id="header" name="header"
								value="${project.header}" class="required" data-tip="请输入负责人"
								data-valid="isNonEmpty" data-show="headershow"
								data-error="负责人不能为空">
							<span style="color: red;">*</span>
							<span style="color: red;" id="headershow"></span>
						</td>
					</tr>
					
					<tr>
						<td width="20%">
							联系电话：
						</td>
						<td>
							<input type="text" id="tel" name="tel" class="required"
								value="${project.tel }" data-tip="请输入联系电话"
								data-valid="isNonEmpty" data-show="telshow"
								data-error="联系电话不能为空">
							<span style="color: red;">*</span>
							<span style="color: red;" id="telshow"></span>
						</td>
					</tr>
					<tr>
						<td width="20%">
							所在学院：
						</td>
						<td>
							<input type="text" id="college" name="college" class="required"
								value="${project.college}" data-tip="请输入所在学院"
								data-valid="isNonEmpty" data-show="collegeshow"
								data-error="所在学院不能为空">
							<span style="color: red;">*</span>
							<span style="color: red;" id="collegeshow"></span>
						</td>
					</tr>
					<tr>
						<td width="20%">
							项目参加人：
						</td>
						<td>
							<input type="text" id="participant" name="participant" class="required"
								value="${project.participant }" data-tip="请输入项目参加人"
								data-valid="isNonEmpty" data-show="participantshow"
								data-error="项目参加人不能为空">
							<span style="color: red;">*</span>
							<span style="color: red;" id="participantshow"></span>
						</td>
					</tr>
					<tr>
						<td width="20%">
							指导教师：
						</td>
						<td>
							<input type="text" id="teacher" name="teacher" class="required"
								value="${project.teacher }" data-tip="请输入指导教师"
								data-valid="isNonEmpty" data-show="teachershow"
								data-error="指导教师不能为空">
							<span style="color: red;">*</span>
							<span style="color: red;" id="teachershow"></span>
						</td>
					</tr>
					<tr>
						<td width="20%">
							联系电话：
						</td>
						<td>
							<input type="text" id="teachertel" name="teachertel" class="required"
								value="${project.teachertel }" data-tip="请输入联系电话"
								data-valid="isNonEmpty" data-show="teachertelshow"
								data-error="联系电话不能为空">
							<span style="color: red;">*</span>
							<span style="color: red;" id="teachertelshow"></span>
						</td>
					</tr>
					
					<tr>
						<td width="20%">
							导师职称：
						</td>
						<td>
							<input type="text" id="teachertitle" name="teachertitle" class="required"
								value="${project.teachertitle}" data-tip="请输入导师职称"
								data-valid="isNonEmpty" data-show="teachertitleshow"
								data-error="导师职称不能为空">
							<span style="color: red;">*</span>
							<span style="color: red;" id="teachertitleshow"></span>
						</td>
					</tr>
					<tr>
						<td width="20%">
							所在单位：
						</td>
						<td>
							<input type="text" id="unit" name="unit" class="required"
								value="${project.unit}" data-tip="请输入所在单位"
								data-valid="isNonEmpty" data-show="unitshow"
								data-error="所在单位不能为空">
							<span style="color: red;">*</span>
							<span style="color: red;" id="unitshow"></span>
						</td>
					</tr>
					<tr>
						<td width="20%">
							填表日期：
						</td>
						<td>
							<input type="text" id="signdate" name="signdate" class="required" onfocus="WdatePicker()"
								value="${project.signdate }" data-tip="请输入填表日期"
								data-valid="isNonEmpty" data-show="signdateshow"
								data-error="填表日期不能为空">
							<span style="color: red;">*</span>
							<span style="color: red;" id="signdateshow"></span>
						</td>
					</tr>
					<tr>
						<td width="20%">
							申请表：
						</td>
						<td>
							<tag:upload base="${base}" fileValue="${project.fileurl}" downname="${project.name}" 
											fileId="fileurl" fileName="fileurl"
											fileType=".*" fileCss="g_box" btnCss="box"></tag:upload>
							<span style="color: red;">*</span>
							<span style="color: red;" id="fileurlshow"></span>
						</td>
					</tr>
					
					<tr>
						<td colspan="2">
							<input type="button" value="提  交" onclick="checkForm()" />

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
