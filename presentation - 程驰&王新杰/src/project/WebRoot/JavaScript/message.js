function sendmess(){
	$.ajax({       
		url: "MessageServlet?action=sendmess",
		data: $("#submitform").serializeArray(),
		type:"post",             
		dataType:"JSON",
		async:false,
		success:function(results){
			alert("信息发送成功！！！");
		} ,
		error:function(e){
			alert(e);
		}
	});
}