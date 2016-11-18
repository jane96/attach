<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>aaa</title>
</head>
<body>
<p>aaa</p>
<hr/>
<form action="testService_saveSysUser.do" id="testform" method="post">
	<input name="form.sysUser.userName"/>&nbsp;<input name="form.sysUser.password"/>&nbsp;<input type="submit"/> 
	${ctx}//${pageContext.request.contextPath}//${form.returnMessage} 
<br style="float: left;margin-left: 5px;">
	<s:iterator status="status" value="form.sysUser.users">
				用户名:${userName} 	&nbsp; 密码:${password}<br>
	</s:iterator> 
	<%@ include file="/page.jsp"%>
</form>
</body>
</html>
