function addlog(){
	$.ajax({       
		url: "JobplaceServlet?action=addlog",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("日常工作安排添加成功！！！");
			window.location.href="http://localhost:8080/oasys/comm/gzappp/addlog.jsp";
		} ,
		error:function(e){
			alert(e);
		}
	});
}
function deletelog(id){
	$.ajax({       
		url: "JobplaceServlet?action=deletelog&id="+id+"",
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("日常工作安排删除成功！！！");
			window.location.href="http://localhost:8080/oasys/JobplaceServlet?action=findlog&sign=3";
		} ,
		error:function(e){
			alert(e);
		}
	});
}
function updatelog(){
	$.ajax({       
		url: "JobplaceServlet?action=updatelog",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("日常工作安排修改成功！！！");
			window.location.href="http://localhost:8080/oasys/JobplaceServlet?action=findlog&sign=2";
		} ,
		error:function(e){
			alert(e);
		}
	});
}