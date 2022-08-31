<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ include file="/pages/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="${base }">
    
    <title>修改密码</title>
    <link href="${base }/css/page.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" language="javascript">
      function checkForm(){
         var form = document.forms[0];
         
		var validate=$('form').validate('submitValidate');
        if(validate== true){
			if($("#upasswordc").val()!=$("#upasswordold").val()){ 
				$("#upasswordoldshow").html("原始密码不正确！");
				$("#upasswordold").focus();
				return false;
			}
			else if($("#upassword").val()!=$("#upasswordagin").val()){ 
				$("#upasswordaginshow").html("两次输入密码不一致！");
				$("#upasswordagin").focus();
				return false;
			}else{
				return true;
			}
			
		}
		return false;
	}
	
</script>
	</head>

	<body >
		<div align="center">
			<h1>
				修改密码
			</h1>
		</div>
		<div id="user" align="center">
			<form action="${base }/servlet/UsersServlet?method=updatePassword" method="post"
				onsubmit="return checkForm()">
				<table border="1">
					<tr>
						
						<td width="20%">
							用户名：
						</td>
						<td>
							${users.uname }	
							<input type="hidden" name="uid" id="uid" value="${users.uid }">		
										
						</td>
					</tr>
					
					
					<tr>
						<td width="20%">
							原始密码：
						</td>
						<td>
							<input type="password"  id="upasswordold" name="upasswordold" class="required" data-tip="请输入原始密码"  
							 data-valid="isNonEmpty" data-show="upasswordcshow"
                                                             data-error="原始密码不能为空">
							<input type="hidden" id="upasswordc" name="upasswordc" value="${users.upassword }">	
							  <span style="color: red;" >*</span>
								<span style="color: red;" id="upasswordcshow"></span>			
						</td>
					</tr>
					
					<tr>
						<td width="20%">
							新密码：
						</td>
						<td>
							<input type="password"  id="upassword" name="upassword" class="required" data-tip="请输入新密码"  
							 data-valid="isNonEmpty" data-show="unameshow"
                                                             data-error="新密码不能为空">	
                                                               <span style="color: red;" >*</span>
							<span style="color: red;" id="upasswordshow"></span>	
						</td>
						
					</tr>
					<tr>
						<td width="20%">
							确认密码：
						</td>
						<td>	
							<input type="password" id="upasswordagin" name="upasswordagin" class="required" data-tip="请输入新密码"  
							 data-valid="isNonEmpty" data-show="upasswordaginshow"
                                                             data-error="新密码不能为空">
                                                             <span style="color: red;" >*</span>
                                                             <span style="color: red;" id="upasswordaginshow"></span>		
						</td>
						
					</tr>
					<tr>
						<td colspan="2">
							<input type="submit" value="提交" />
							
							<input class="box" type="button" value="返  回"
							onClick="javascript:history.go(-1);" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		<script type="text/javascript">
		$("form").validate({


    });
		</script>
  </body>
</html>
