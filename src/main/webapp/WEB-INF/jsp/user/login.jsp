<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
    String rootPath = request.getServletContext().getContextPath();
    request.setAttribute("rootPath", rootPath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
<script src="${rootPath}/static/js/jquery.js"></script>
<script src="${rootPath}/static/js/md5.js"></script>
<script src="${rootPath}/static/js/login.js"></script>
</head>
<body>
	<form action="login" method="POST" onsubmit="return md5password();">
		<table border="0">
			<tr>
				<td><label for="user.email">E-mail</label ></td>
				<td><input type="email" id="user.email" name="email"></td>
			<tr>
				<td><label for="user.password">Password</label></td>
				<td><input type="password" id="user.password" name="password_ori"></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" name="submit" value="登录"></td>
			</tr>
		</table>
		<input type="hidden" id="password_md5" name="password">
	</form>
</body>
</html>