function addlog(){
	$.ajax({       
		url: "JoblogServlet?action=addlog",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("工作日志添加成功！！！");
			window.location.href="http://localhost:8080/oasys/comm/gzap/addlog.jsp";
		} ,
		error:function(e){
			alert(e);
		}
	});
}
function deletelog(id){
	$.ajax({       
		url: "JoblogServlet?action=deletelog&id="+id+"",
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("工作日志删除成功！！！");
			window.location.href="http://localhost:8080/oasys/JoblogServlet?action=findlog&sign=3";
		} ,
		error:function(e){
			alert(e);
		}
	});
}
function updatelog(){
	$.ajax({       
		url: "JoblogServlet?action=updatelog",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("工作日志修改成功！！！");
			window.location.href="http://localhost:8080/oasys/JoblogServlet?action=findlog&sign=2";
		} ,
		error:function(e){
			alert(e);
		}
	});
}