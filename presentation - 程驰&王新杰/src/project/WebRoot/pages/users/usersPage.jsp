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
    
    <title>用户维护</title>
    <link href="<%=basePath%>/css/page.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript" language="javascript">
	  var uname =[<c:forEach items="${list}" var="item" varStatus="i"><c:if test="${i.index!=0}">,</c:if>{'uid':'${item.uid}','uname':'${item.uname}'}</c:forEach>];
       function checkForm(){
         var validate=$('form').validate('submitValidate');
        if(checkname()==true&&validate== true){
           
            if($("#upassword").val()!=$("#upasswordagin").val()){ 
				$("#upasswordaginshow").html("两次输入密码不一致！");
				$("#upasswordagin").focus();
				return ;
			}else{
			 var aa = window.confirm('确定要保存吗？');
            if (aa == true) {
                document.forms[0].submit();
            } else {
                return ;
            }
            }
        }
	}
	function checkname()
	{
		 var form = document.forms[0];
		var i ;
		var uid='${users.uid }';
		for(i=0;i<uname.length;i++)
		{
			if(uname[i].uname==$('#uname').val()&&($('#uid').val()!=uname[i].uid))
			{
				$('#unameshow').html("用户名已存在！");
				$('#uname').focus();
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
			
			<font color="#ffffff"><a><c:if test="${users==null}">添加用户</c:if>
				<c:if test="${users!=null}">修改用户</c:if></a>
			</font>
			
		</h3>
		<div id="user" align="center">
			<form action="${base }/servlet/UsersServlet?method=saveUsers" method="post" id="myform" >
			    <table border="1"   >
					<tr>
						<td width="20%">
							用户名：
						</td>
						<td>
						<input id="uid" name="uid" value="${users.uid }" type="hidden"> 
							<input id="flag" name="flag" value="${flag}" type="hidden">
						<c:if test="${users!=null}"><input type="text" id="uname" name="uname" value="${users.uname }" readonly="readonly"></c:if>
						<c:if test="${users==null}">
							<input type="text" id="uname" name="uname" value="${users.uname }" onblur="checkname()"
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
							<input type="password" id="upassword" name="upassword" value="${users.upassword==null?'123456':users.upassword }" 
							 class="required" data-tip="请输入密码，默认123456"  
							 data-valid="isNonEmpty" data-show="upasswordshow"
                                                             data-error="密码不能为空">	
							<span style="color: red;">*</span>	
							<span style="color: red;" id="upasswordshow"></span>	
						</td>
					</tr> 
					<tr >
						<td width="20%">
							确认密码：
						</td>
						<td>
							<input type="password" id="upasswordagin" name="upasswordagin" value="${users.upassword==null?'123456':users.upassword }" 
							 class="required" data-tip="请输入确认密码"  
							 data-valid="isNonEmpty" data-show="upasswordaginshow"
                                                             data-error="确认密码不能为空">	
							<span style="color: red;">*</span>	
							<span style="color: red;" id="upasswordaginshow"></span>	
						</td>
					</tr> 
					<tr>
						<td width="20%">
							姓名：
						</td>
						<td>
							<input type="text" id="urealname" name="urealname"  value="${users.urealname }" 
							 class="required" data-tip="请输入姓名"  
							 data-valid="isNonEmpty" data-show="urealnameshow"
                                                             data-error="姓名不能为空"  > 
							<span style="color: red;">*</span>		
							<span style="color: red;" id="urealnameshow"></span>				
						</td>
					</tr>
					 
					<tr>
						<td width="20%">
							联系电话：
						</td>
						<td>
							<input type="text" id="utel" name="utel" class="required" value="${users.utel }" data-tip="请输入联系电话"  
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
