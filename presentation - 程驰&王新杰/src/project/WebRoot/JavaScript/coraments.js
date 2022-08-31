function addcora(){
	$.ajax({       
		url: "CoramentsServlet?action=addcora",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("费用项目发布成功！！！");
			window.location.href="/property/manager/coraments/addcora.jsp";
		} ,
		error:function(e){
			alert(e);
		}
	});
}
function deletecora(id){
	$.ajax({       
		url: "CoramentsServlet?action=deletecora&ID="+id+"",
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("费用项目删除成功！！！");
			window.location.href="CoramentsServlet?action=findcora&sign=2";
		} ,
		error:function(e){
			alert(e);
		}
	});
}

function updatecora(){
	$.ajax({       
		url: "CoramentsServlet?action=updatecora",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("费用项目修改成功！！！");
			window.location.href="CoramentsServlet?action=findcora&sign=1";
		} ,
		error:function(e){
			alert(e);
		}
	});
}
