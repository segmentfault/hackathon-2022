<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.bean.Users"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title></title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<STYLE type=text/css>
		{
			FONT-SIZE:10px
		}
		#menuTree A {
			COLOR: #566984;
			TEXT-DECORATION: none
		}
		</STYLE>
		<SCRIPT src="<%=basePath%>mainfile/Left.files/TreeNode.js"
			type=text/javascript></SCRIPT>
		<SCRIPT src="<%=basePath%>mainfile/Left.files/Tree.js"
			type=text/javascript></SCRIPT>
	</head>

	<BODY style="BACKGROUND-POSITION-Y: -120px; BACKGROUND-IMAGE: url(<%=basePath%>images/bg.gif); BACKGROUND-REPEAT: repeat-x">
		<div style="overflow:auto; height:100%; width:100%">
		<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
    <TR>
      <TD width=10 height=29><IMG src="<%=basePath%>mainfile/Left.files/bg_left_tl.gif"></TD>
      <TD 
    style="FONT-SIZE: 18px; BACKGROUND-IMAGE: url(<%=basePath%>images/bg_left_tc.gif); COLOR: white; FONT-FAMILY: system">Main 
        Menu</TD>
      <TD width=10><IMG src="<%=basePath%>mainfile/Left.files/bg_left_tr.gif"></TD>
    </TR>
    <TR>
      <TD style="BACKGROUND-IMAGE: url(<%=basePath%>images/bg_left_ls.gif)"></TD>
      <TD id=menuTree 
    style="PADDING-RIGHT: 10px; PADDING-LEFT: 10px; PADDING-BOTTOM: 10px; PADDING-TOP: 10px; HEIGHT: 100%; BACKGROUND-COLOR: white" 
    vAlign=top></TD>
      <TD style="BACKGROUND-IMAGE: url(<%=basePath%>images/bg_left_rs.gif)"></TD>
    </TR>
    <TR>
      <TD width=10><IMG src="<%=basePath%>mainfile/Left.files/bg_left_bl.gif"></TD>
      <TD style="BACKGROUND-IMAGE: url(<%=basePath%>images/bg_left_bc.gif)"></TD>
      <TD width=10><IMG 
src="<%=basePath%>mainfile/Left.files/bg_left_br.gif"></TD>
    </TR>
  </TBODY>
		</TABLE>
		</div>
		 <%
 	Users userInfo = (Users) request.getSession().getAttribute("user");
 	if (userInfo != null) {
 		if ("1".equals(userInfo.getUtype())) {%> 
	<SCRIPT type=text/javascript>
		var tree = null;
		var root = new TreeNode('用户管理');
	
		var fun1 = new TreeNode('管理员管理', 'servlet/UsersServlet?method=findAllUsers',
				'tree_node.gif', null, 'tree_node.gif', null);
		var fun2 = new TreeNode('注册用户管理', 'servlet/StudentServlet?method=findAllStudent&flag=2',
				'tree_node.gif', null, 'tree_node.gif', null);
		root.add(fun1);
		root.add(fun2);  
		tree = new Tree(root);
		tree.show('menuTree') 
		var tree1 = null;
		var root = new TreeNode('文档管理');
	
		var fun1 = new TreeNode('文档管理', 'servlet/FileServlet?method=findAllFile',
				'tree_node.gif', null, 'tree_node.gif', null);
		root.add(fun1);
		
		tree1 = new Tree(root);
		tree1.show('menuTree');
		
		var tree2 = null;
		var root = new TreeNode('项目管理');
	
		var fun1 = new TreeNode('项目审批', 'servlet/ProjectServlet?method=findAllProject&flag=2&verify=0',
				'tree_node.gif', null, 'tree_node.gif', null);
		var fun2 = new TreeNode('项目资金管理', 'servlet/ProjectServlet?method=findAllProject&flag=3&verify=1',
				'tree_node.gif', null, 'tree_node.gif', null);
		var fun3 = new TreeNode('项目评审管理', 'servlet/ProjectServlet?method=findAllProject&flag=4&verify=1',
				'tree_node.gif', null, 'tree_node.gif', null);
		var fun4 = new TreeNode('项目查看', 'servlet/ProjectServlet?method=findAllProject&flag=5',
				'tree_node.gif', null, 'tree_node.gif', null);
		root.add(fun1);
		root.add(fun2);
		root.add(fun3);
		root.add(fun4);
		tree2 = new Tree(root);
		tree2.show('menuTree');
		
		var tree3 = null;
		var root = new TreeNode('个人信息管理'); 
		var fun1 = new TreeNode('个人信息修改', 'servlet/UsersServlet?method=updatePage&uid=${user.uid}&flag=2',
				'tree_node.gif', null, 'tree_node.gif', null);
		root.add(fun1);
		tree3 = new Tree(root);
		tree3.show('menuTree');
	</SCRIPT>

	<%} else if ("2".equals(userInfo.getUtype())) {%>
	
	<SCRIPT type=text/javascript>
		var tree = null;
		var root = new TreeNode('文档下载');
	
		var fun1 = new TreeNode('文档下载', 'servlet/FileServlet?method=findAllFile&flag=2',
				'tree_node.gif', null, 'tree_node.gif', null);
		root.add(fun1);
		
		//----------------------------
		var tree1 = null;
		var root1 = new TreeNode('项目管理');
		var fun1 = new TreeNode('项目管理', 'servlet/ProjectServlet?method=findAllProject&flag=1',
				'tree_node.gif', null, 'tree_node.gif', null);
		var fun2 = new TreeNode('项目资金查看', 'servlet/ProjectServlet?method=findAllProject&flag=6',
				'tree_node.gif', null, 'tree_node.gif', null);
		root1.add(fun1);
		root1.add(fun2);
		tree = new Tree(root);
		tree1 = new Tree(root1);
		tree.show('menuTree');
		tree1.show('menuTree'); 
		var tree2 = null;
		var root = new TreeNode('个人信息管理');
	var fun1 = new TreeNode('密码修改', 'servlet/StudentServlet?method=updatePasswordPage&id=${user.uid}',
				'tree_node.gif', null, 'tree_node.gif', null);
		var fun1 = new TreeNode('个人信息修改', 'servlet/StudentServlet?method=updatePage&id=${user.uid}',
				'tree_node.gif', null, 'tree_node.gif', null);
		root.add(fun1);
		tree2 = new Tree(root);
		tree2.show('menuTree');
	</SCRIPT>
	<%}}%>

	
	</BODY>
</html>
