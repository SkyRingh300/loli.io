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
<title>登陆</title>
<!-- <script src="${rootPath}/static/js/jquery.js"></script> -->
<script src="${rootPath}/static/js/md5.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="${rootPath}/static/js/login.js"></script>
<script src="${rootPath}/static/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${rootPath}/static/css/bootstrap.min.css">
<link href="${pageContext.request.contextPath}/static/css/styles.css"
	type="text/css" rel='stylesheet' />

<style type="text/css">
.login-form {
	width: 70%;
	margin: auto;
	margin-top: 60px;
	padding: 3px 60px 60px 20px;
	border-width: 1px;
	border-color: rgb(221, 221, 221);
	border-radius: 4 4 4 4;
	box-shadow: none;
	border-style: solid;
}

.info {
	width: 70%;
	margin: auto;
}
</style>
</head>
<body>
	<jsp:include page="../top.jsp"></jsp:include>

	<div id="main">
		<div class="container">
			<c:if test="${info!=null}">
				<div class="alert alert-success info">
					<button type="button" class="close" data-dismiss="alert"
						aria-hidden="true">&times;</button>
					${info}
				</div>
			</c:if>
			<div class="login-form">
				<h2>登陆</h2>
				<form class="form-horizontal" action="login" role="form"
					method="POST" onsubmit="return md5password();">
					<div class="form-group">
						<label for="user-email" class="col-sm-4 control-label">邮箱</label>
						<div class="col-sm-4">
							<input type="email" name="email" class="form-control"
								id="user-email" value="${user.email}" placeholder="E-mail">
						</div>
						<span class="label label-danger" id="email-error">${message["email"]}</span>
					</div>
					<div class="form-group">
						<label for="user-password" class="col-sm-4 control-label">密码</label>
						<div class="col-sm-4">
							<input type="password" class="form-control" id="user-password"
								name="password_ori" placeholder="Password">
						</div>
						<span class="label label-danger" id="password_error"></span>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-6">
							<button type="submit" class="btn btn-primary">登陆</button>
						</div>
					</div>
					<input type="hidden" id="password_md5" name="password">

				</form>
			</div>
		</div>
	</div>
</body>
</html>