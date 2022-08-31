<%@ page language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://taglib.rk.com/displayer" prefix="tag" %>
<c:set var="base" value="${pageContext.request.scheme}://${pageContext.request.serverName }:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>
<script type="text/javascript">
var base="${base}";
</script>
<script type="text/javascript" src="${base }/js/DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base }/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="${base }/js/jquery-validate.js"></script>
<script type="text/javascript" src="${base }/js/upload.js"></script>
