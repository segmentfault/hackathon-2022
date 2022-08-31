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
* {
	FONT-SIZE: 12px;
	COLOR: white
}

#logo {
	COLOR: white
}

#logo A {
	COLOR: white
}

FORM {
	MARGIN: 0px
}
</STYLE>
		<SCRIPT src="<%=basePath%>mainfile/Top.files/Clock.js"
			type=text/javascript></SCRIPT>
	</head>

	<BODY
		style="BACKGROUND-IMAGE: url(<%=basePath%>images/bg.gif); MARGIN: 0px; BACKGROUND-REPEAT: repeat-x">
		<form id="form1">
			<DIV id=logo
				style="BACKGROUND-IMAGE: url(<%=basePath%>images/logo.png); BACKGROUND-REPEAT: no-repeat">
				<DIV
					style="PADDING-RIGHT: 50px; BACKGROUND-POSITION: right 50%; DISPLAY: block; PADDING-LEFT: 0px; BACKGROUND-IMAGE: url(../images/bg_banner_menu.gif); PADDING-BOTTOM: 0px; PADDING-TOP: 3px; BACKGROUND-REPEAT: no-repeat; HEIGHT: 30px; TEXT-ALIGN: right">
				</DIV>
				
				<DIV
					style="BACKGROUND-IMAGE: url(<%=basePath%>images/bg_nav.gif); BACKGROUND-REPEAT: repeat-x; HEIGHT: 30px">
					<TABLE cellSpacing=0 cellPadding=0 width="100%">
						<TBODY>
							<TR>
								<TD>
									<DIV>
										<IMG src="<%=basePath%>mainfile/Top.files/nav_pre.gif"
											align=absMiddle>
										欢迎
										<SPAN id=lblBra style="font-size: 14px;"> <%
 	Users userInfo = (Users) request.getSession().getAttribute("user");
 	if (userInfo != null) {
 		if ("1".equals(userInfo.getUtype())) {
 %> 管理员--(<%=userInfo.getUname()%>)
											<%
 	} else if ("2".equals(userInfo.getUtype())) {
 %> 学生--(<%=userInfo.getUname()%>)
											<%
 	} 
 	}
 %> </SPAN>
										<SPAN id=lblDep> </SPAN>登录
									</DIV>
								</TD>
								<TD align=right width="70%">
									<SPAN style="PADDING-RIGHT: 50px"><A
										href="javascript:history.go(-1);"><IMG
												src="<%=basePath%>mainfile/Top.files/nav_back.gif"
												align=absMiddle border=0>后退</A> <A
										href="javascript:history.go(1);"><IMG
												src="<%=basePath%>mainfile/Top.files/nav_forward.gif"
												align=absMiddle border=0>前进</A> <A href="/project/login.jsp"
										target=_top><IMG
												src="<%=basePath%>mainfile/Top.files/nav_changePassword.gif"
												align=absMiddle border=0>重新登录</A>
									<IMG src="<%=basePath%>mainfile/Top.files/menu_seprator.gif"
											align=absMiddle></SPAN>
									</SPAN>
								</TD>
							</TR>
						</TBODY>
					</TABLE>
				</DIV>
			</DIV>
			<SCRIPT type=text/javascript>
	var clock = new Clock();
	clock.display(document.getElementById("clock"));
</SCRIPT>
		</form>
	</BODY>
</html>
