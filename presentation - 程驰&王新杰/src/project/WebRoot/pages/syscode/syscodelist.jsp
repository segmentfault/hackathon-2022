<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ include file="/pages/taglibs.jsp"%>


<!DOCSysCode HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="${base }">

		<title>用户管理</title>
		<link href="${base }/css/list.css" rel="stylesheet"
			type="text/css" />
	</head>

	<body>
		<form action="${base }/servlet/SysCodeServlet?method=findAllSysCode" method="post">
			<div align="center">
				<h1>
					代码列表
				</h1>


			</div>
			
			
				<div align="center" >
					<table border="1" >
					<tr >
						<th align="left" >
							类别：<select id="type"  name="type"   >
							<option value="">--代码类别--</option>
							</select>
							<input type="submit" value="搜  索" />
						</th>
						
						<th >
							<input type="button" onclick="location.href='${base }/servlet/SysCodeServlet?method=addSysCode'" value="添  加" />
					<input class="box" type="button" value="返  回"
						onClick="javascript:history.go(-1);" />
						</th>

					</tr>
					</table>
					
				</div>
			<div align="center" id="studentlist">
				<table border="1"  >
					<tr>
						<th align="center">
							序号
						</th>
						<th>
							代码类别
						</th>
						
						<th>
							代码值
						</th> 
						<th>
							代码名称
						</th>
						<th>
							操作
						</th>

					</tr>
					<c:forEach var="item" varStatus="i" items="${list}"> 
						<tr>
							<td>
								${(pagenum-1)*currentp+i.index+1}
							</td>
							<td width="100px;">
								<span class="codetype" >${item.type}</span>
							</td>
							<td width="100px;">
								${item.code}
							</td>
							
							<td>
								${item.name}
							</td>
							
								<td align="center" width="100px;">
									<a
										href="${base }/servlet/SysCodeServlet?method=updatePage&id=${item.id}">修改</a>|
									<a
										href="${base }/servlet/SysCodeServlet?method=deleteSysCode&id=${item.id}">删除</a>
								</td>
							
						</tr>
					</c:forEach>
				</table>
				${pagingModel.pageLink }${pagingModel.pageInfo }
			</div>
		</form>
		
    <script type="text/javascript" src="${base }/js/data.js"></script>
		<script type="text/javascript">
		$("#myform").validate({});
		var $type=$("#type"); 
		var type_v='${syscode.type}'; 
		$(codetype.value).each(function (i){
             var $opt = $("<option>").text(codetype.pram[i]).val(this);
             if (type_v == this) {
            	$opt.attr("selected", "selected");
       		 }
              $opt.appendTo($type);
        });
    	 
		 var $codetype=$("#studentlist").find(".codetype");
		    $.each($codetype, function(index, el) {
		 	 var $el = $(el);
		 	 var value=$el.html();
            $(codetype.value).each(function (i){
             if(value==this){
             $el.html(codetype.pram[i]);
             }
        });
        });
		  
		</script>
	</body>
</html>
