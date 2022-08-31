	var code ; //在全局定义验证码    
	//-----------------------------------------------------
	var XMLHttpReq;
 	//创建XMLHttpRequest对象       
    function createXMLHttpRequest() {
		if(window.XMLHttpRequest) { //Mozilla 浏览器
			XMLHttpReq = new XMLHttpRequest();
		}
		else if (window.ActiveXObject) { // IE浏览器
			try {
				XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				try {
					XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {}
			}
		}
	}
	//产生验证码   
	window.onload = createCode;
	
	function createCode(){  
	     code = "";   
	     var codeLength = 4;//验证码的长度   
	     var checkCode = document.getElementById("code");   
	     var random = new Array(0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R', 'S','T','U','V','W','X','Y','Z');//随机数   
	    for(var i = 0; i < codeLength; i++) {//循环操作   
	        var index = Math.floor(Math.random()*36);//取得随机数的索引（0~35）   
	        code += random[index];//根据索引取得随机数加到code上   
	    }  
	    checkCode.value = code;//把code值赋给验证码   
	}  
	//校验验证码   
	function validate(flag){
		
		 var loginname = document.getElementById("userName").value;//清空文本框
		 var password =  document.getElementById("password").value;
		 var inputCode = document.getElementById("number").value.toUpperCase(); //取得输入的验证码并转化为大写         
		 document.getElementById("type").value=flag;
		 if(loginname==""){
			 alert("用户名不能为空！！！")
			 return false;
		 }else if(password==""){
			 alert("密码不能为空！！！！");
			 return false;
		 }else if(inputCode.length <= 0) { //若输入的验证码长度为0   
	        alert("请输入验证码！"); //则弹出请输入验证码   
	        return false;
	        
	    } else if(inputCode != code ) { //若输入的验证码与产生的验证码不一致时   
	        alert("验证码输入错误！"); //则弹出验证码输入错误   
	        createCode();//刷新验证码   
	        document.getElementById("number").value = "";//清空文本框 
	        return false;
	       
	    }else { //输入正确时   
	    	return true;
	    }             
	} 

	function form_reset(){
	   document.getElementById("userName").value ="";//清空文本框
	   document.getElementById("password").value="";
	   document.getElementById("number").value="";   
	}
	 //发送请求函数
	 function sendRequest(url) {
	  createXMLHttpRequest();
	  XMLHttpReq.open("POST", url, true);
	  XMLHttpReq.onreadystatechange = processResponse;//指定响应函数
	  XMLHttpReq.send(null);  // 发送请求
	 }
	 // 处理返回信息函数
	    function processResponse() {
	     if (XMLHttpReq.readyState == 4) { // 判断对象状态
	         if (XMLHttpReq.status == 200) { // 信息已经成功返回，开始处理信息
	             var res=XMLHttpReq.responseXML.getElementsByTagName("res")[0].firstChild.data;
	                if(res=="1"){
	                	window.location.href="MyOffice.htm";
	                }else{
	                	window.alert(res);
		                form_reset();
	                }
	            } else { //页面不正常
	                window.alert("您所请求的页面有异常。");
	            }
	        }
	    }
function adduser(){
	$.ajax({       
		url: "UserServlet?action=adduser",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("用户信息添加成功！！！");
			window.location.href="DepServlet?biao=1";
		} ,
		error:function(e){
			alert(e);
		}
	});
}

function updatePass(){
	
	
	var password = document.getElementById("password").value;
	
	if(password === ""){
		alert("密码不能为空")
		return;
	}
	
	$.ajax({       
		url: "JoblogServlet?action=updatelog",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("密码修改成功！！！");
			window.location.href="manager/updatePassword.jsp";
		} ,
		error:function(e){
			alert(e);
		}
	});
}
function deleteuser(id){
	$.ajax({       
		url: "UserServlet?action=deleteuser&id="+id+"",
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("人员信息删除成功！！！");
			window.location.href="UserServlet?action=finduser&sign=2";
		} ,
		error:function(e){
			alert(e);
		}
	});
}

function deletepeople(id){
	$.ajax({       
		url: "PeopleServlet?action=deletepeople&ID="+id+"",
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("业主信息删除成功！！！");
			window.location.href="PeopleServlet?action=findpeople&sign=2";
		} ,
		error:function(e){
			alert(e);
		}
	});
}

function updateuser(){
	var a = document.getElementById("a").Value;
	$.ajax({       
		url: "UserServlet?action=updateuser",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			if(a==2){
				alert("办公人员信息修改成功！！！");
				window.location.href="UserServlet?action=finduser&sign=1";
			}else{
				alert("个人信息修改成功！！！");
			}
		} ,
		error:function(e){
			alert(e);
		}
	});
}
