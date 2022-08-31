function addpeople(){
	$.ajax({       
		url: "PeopleServlet?action=addpeople",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("业主信息添加成功！！！");
			window.location.href="adduser.jsp";
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
function updatepeople(){
	$.ajax({       
		url: "PeopleServlet?action=updatepeople",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("业主信息修改成功！！！");
			window.location.href="PeopleServlet?action=findpeople&sign=1";
		} ,
		error:function(e){
			alert(e);
		}
	});
}
function a(id){

window.location.href="http://localhost:8080/oasys/UserServlet?action=finduserid&id="+id+"&a=4";

}
