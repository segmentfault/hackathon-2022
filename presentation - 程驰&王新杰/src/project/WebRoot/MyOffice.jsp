<%@ page language="java"  pageEncoding="gb2312"%>
<%@ include file="/pages/taglibs.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<HTML>
	<HEAD>
		<TITLE>大学生科技创新项目评审系统</TITLE>

		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<META content="MSHTML 6.00.2900.5848" name=GENERATOR>
	</HEAD>
	<FRAMESET id=index border=0 frameSpacing=0 rows=120,* frameBorder=no>
		<FRAME id=topFrame name=topFrame src="<%=basePath%>mainfile/top.jsp" noResize
			scrolling=no>
		<FRAMESET border=0 frameSpacing=0 frameBorder=no cols=20%,*>
			<FRAME id=leftFrame name=leftFrame src="<%=basePath%>mainfile/left.jsp" noResize
				scrolling=no>
			<FRAME id=mainFrame name=mainFrame src="<%=basePath%>mainfile/tishi.jsp" noResize
				scrolling=yes>
		</FRAMESET>
	</FRAMESET>
	<noframes></noframes>
</HTML>
