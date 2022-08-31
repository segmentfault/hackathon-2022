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

function findByMap(){
	$.ajax({       
		url: "BusmeetingServlet?action=addmeeting",
		data: $("#submitform").serializeArray(),
		type:"post",             
//		/property/CoramentsServlet?action=findcora&sign=1
		async:false,
		success:function(results){
			
			var a = results;
			var b = results;
			window.location.href="/property/BusmeetingServlet?action=addPager";
		} ,
		error:function(e){
			alert(e);
		}
	});
}

	 //发送请求函数
	 function addmeeting(url) {
	 	
	 	var I_YEAR = document.getElementById("I_YEAR").value;
	 	var I_TYPE = document.getElementById("I_TYPE").value;
	 	var I_OWNER_ID = document.getElementById("I_OWNER_ID").value;
	 	var I_ET_ID = document.getElementById("I_ET_ID").value;
	 	url = url+"&I_YEAR="+I_YEAR+"&I_TYPE="+I_TYPE
	 			+"&I_OWNER_ID="+I_OWNER_ID+"&I_ET_ID="+I_ET_ID;
	 	
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
	                	alert("物业费用添加成功");
	                	window.location.href="/property/BusmeetingServlet?action=addPager";
	                }else{
	                	 if(res=="3"){
		                	alert("物业费用修改成功");
		                	window.location.href="/property/BusmeetingServlet?action=findmeeting";
		                }else{
		                	window.alert("物业费用记录已经存在！");
		                }
	                }
	            } else { //页面不正常
	                window.alert("您所请求的页面有异常。");
	            }
	        }
	    }

function deletemeeting(id){
	$.ajax({       
		url: "BusmeetingServlet?action=deletemeeting&ID="+id+"",
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("物业费用删除成功！！！");
			window.location.href="/property/BusmeetingServlet?action=findmeeting&sign=2";
		} ,
		error:function(e){
			alert(e);
		}
	});
}
function updatemeeting(){
	$.ajax({       
		url: "BusmeetingServlet?action=updatemeeting",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("物业费用修改成功！！！");
			window.location.href="BusmeetingServlet?action=findmeeting&sign=1";
		} ,
		error:function(e){
			alert(e);
		}
	});
}