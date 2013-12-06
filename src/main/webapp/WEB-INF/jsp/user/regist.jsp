<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
    String rootPath = request.getServletContext().getContextPath();
    request.setAttribute("rootPath", rootPath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Regist</title>
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="${rootPath}/static/js/regist.js"></script>
</head>
<body>
	<form action="regist" method="POST">
		<table border="0">
			<tr>
				<td><label for="user.email">E-mail</label></td>
				<td><input type="email" id="user.email" name="email"></td>
			<tr>
				<td><label for="user.password">Password</label></td>
				<td><input type="password" id="user.password" name="password"></td>
			</tr>
			<tr>
				<td><label for="password">Repeat Password</label></td>
				<td><input type="password" id="password_re" name="re_password"></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="button" name="button" value="注册"  onclick="return md5password(this);"></td>
			</tr>
		</table>
		<input type="hidden" id="password_md5" name="password">
	</form>
</body>
</html>