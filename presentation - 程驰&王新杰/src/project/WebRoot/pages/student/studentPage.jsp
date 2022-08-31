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
    
    <title>学生注册</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="<%=basePath%>css/list.css" rel="stylesheet" type="text/css" />  
    <link href="<%=basePath%>/css/page.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript" language="javascript">
	  var uname =[<c:forEach items="${list}" var="item" varStatus="i"><c:if test="${i.index!=0}">,</c:if>{'id':'${item.id}','name':'${item.name}'}</c:forEach>];
       function checkForm(){
         var validate=$('form').validate('submitValidate');
          if(checkname()==true&&validate== true){
			if($("#password").val()!=$("#passwordagin").val()){ 
				$("#passwordaginshow").html("两次输入密码不一致！");
				$("#passwordagin").focus();
				return false;
			}else{
				 var aa = window.confirm('确定要保存吗？');
	            if (aa == true) {
	                document.forms[0].submit();
	            } else {
	                return false;
	            }
			}
			
		}else{
			return false;
		}
       
	}
	function checkname()
	{
		 var form = document.forms[0];
		var i ;
		var uid='${student.id }';
		for(i=0;i<uname.length;i++)
		{
			if(uname[i].name==$('#name').val()&&($('#id').val()!=uname[i].id))
			{
				$('#nameshow').html("用户名已存在！");
				$('#name').focus();
				flag=1;
				return false;
			}
			
		}
		return true;
	}
</script>
	</head>
    
	<body >
		<h3 style="background-color: gray">
			
			<font color="#ffffff"><a><c:if test="${Student==null}">学生注册</c:if>
				<c:if test="${Student!=null}">修改学生</c:if></a>
			</font>
			
		</h3>
		<div id="user" align="center">
			<form action="${base }/servlet/StudentServlet?method=saveStudent" method="post" id="myform" >
			    <table border="1"   >
					<tr>
						<td width="20%">
							用户名：
						</td>
						<td>
						<input id="id" name="id" value="${student.id }" type="hidden"> 
							<input id="flag" name="flag" value="${flag}" type="hidden">
						<c:if test="${student!=null}"><input type="text" id="name" name="name" value="${student.name }" readonly="readonly"></c:if>
						<c:if test="${student==null}">
							<input type="text" id="name" name="name" value="${student.name }" onblur="checkname()"
							  class="required" data-tip="请输入用户名"  
							 data-valid="isNonEmpty" data-show="unameshow"
                                                             data-error="用户名不能为空">	
							
								
							</c:if>	
						 <span style="color: red;">*</span>				
							<span style="color: red;" id="unameshow"></span></td>
					</tr>
					 
					<tr >
						<td width="20%">
							密码：
						</td>
						<td>
							<input type="password" id="password" name="password" value="${student.password }" 
							 class="required" data-tip="请输入密码"  
							 data-valid="isNonEmpty" data-show="passwordshow"
                                                             data-error="密码不能为空">	
							<span style="color: red;">*</span>	
							<span style="color: red;" id="passwordshow"></span>	
						</td>
					</tr>  
					<tr>
						<td width="20%">
							确认密码：
						</td>
						<td>	
							<input type="password" id="passwordagin" name="passwordagin" class="required" data-tip="请输入确认密码"  
							 data-valid="isNonEmpty" data-show="passwordaginshow" value="${student.password }"
                                                             data-error="确认密码不能为空">
                                                             <span style="color: red;" >*</span>
                                                             <span style="color: red;" id="passwordaginshow"></span>		
						</td>
						
					</tr>
					<tr>
						<td width="20%">
							姓名：
						</td>
						<td>
							<input type="text" id="realname" name="realname"  value="${student.realname }" 
							 class="required" data-tip="请输入姓名"  
							 data-valid="isNonEmpty" data-show="urealnameshow"
                                                             data-error="姓名不能为空"  > 
							<span style="color: red;">*</span>		
							<span style="color: red;" id="urealnameshow"></span>				
						</td>
					</tr>
					<tr>
						<td width="20%">
							学号：
						</td>
						<td>
							<input type="text" id="code" name="code"  value="${student.code}" 
							 class="required" data-tip="请输入学号"  
							 data-valid="isNonEmpty" data-show="codeshow"
                                                             data-error="学号不能为空"  > 
							<span style="color: red;">*</span>		
							<span style="color: red;" id="codeshow"></span>				
						</td>
					</tr>
					<tr>
						<td width="20%">
							学院：
						</td>
						<td>
							<input type="text" id="college" name="college"  value="${student.college}" 
							 class="required" data-tip="请输入学院"  
							 data-valid="isNonEmpty" data-show="collegeshow"
                                                             data-error="学院不能为空"  > 
							<span style="color: red;">*</span>		
							<span style="color: red;" id="collegeshow"></span>				
						</td>
					</tr>
					
					<tr>
						<td width="20%">
							联系电话：
						</td>
						<td>
							<input type="text" id="tel" name="tel" class="required" value="${student.tel }" data-tip="请输入联系电话"  
							 data-valid="isNonEmpty" data-show="utelshow"
                                                             data-error="联系电话不能为空"  > 
							<span style="color: red;">*</span>		
							<span style="color: red;" id="utelshow"></span>				
						</td>
					</tr>
					
					<tr>
						<td colspan="2">
							<input type="button" value="提  交" onclick="checkForm()"/>
							
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
