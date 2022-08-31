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
	    function checkForm(flag){
	    var mess="驳回";
         if(flag==1){
          mess="通过";
         } 
            var aa = window.confirm("确定要"+mess+"吗？");
            if (aa == true) {
            $("#verify").val(flag);
                document.forms[0].submit();
            } else {
                return false;
            
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
			<form action="${base }/servlet/ProjectServlet?method=verify"
				method="post" id="myform">
				<table border="1">
					<tr>
						<td  width="20%">
							项目名：
						</td>
						<td> 
							<input id="id" name="id" value="${project.id }" type="hidden"> 
							<input id="verify" name="verify" value="0" type="hidden"> 
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
						<td colspan="2">
							<input type="button" value="通  过" onclick="checkForm(1)" />
<input type="button" value="驳  回" onclick="checkForm(2)" />
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
