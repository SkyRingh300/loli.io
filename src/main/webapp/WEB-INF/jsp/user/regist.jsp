<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String rootPath = request.getServletContext().getContextPath();
    request.setAttribute("rootPath", rootPath);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Regist</title>
<!-- <script src="${rootPath}/static/js/jquery.js"></script> -->
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="${rootPath}/static/js/md5.js"></script>
<script src="${rootPath}/static/js/regist.js"></script>
<script src="${rootPath}/static/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${rootPath}/static/css/bootstrap.min.css">

<style type="text/css">
.register-form {
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
</style>
</head>
<body>
	<jsp:include page="../top.jsp"></jsp:include>

	<div id="main">
		<div class="container">
			<div class="register-form">
				<h2>Register</h2>
				<form class="form-horizontal" action="regist" role="form"
					method="POST" onsubmit="return md5password();">
					<div class="form-group">
						<label for="user-email" class="col-sm-4 control-label">E-mail</label>
						<div class="col-sm-4">
							<input type="email" name="email" class="form-control"
								id="user-email" placeholder="E-mail">
						</div>
						<span class="label label-danger" id="email-error">${message["email"]}
						</span>

					</div>
					<div class="form-group">
						<label for="user-password" class="col-sm-4 control-label">Password</label>
						<div class="col-sm-4">
							<input type="password" class="form-control" name="password_ori"
								id="user-password" placeholder="Password">
						</div>
						<span class="label label-danger" id="password_error"></span>

					</div>
					<div class="form-group">
						<label for="password_re" class="col-sm-4 control-label">Repeat
							Password</label>
						<div class="col-sm-4">
							<input type="password" class="form-control"
								name="password_re_ori" id="password_re"
								placeholder="Repeat Password">
						</div>
						<span class="label label-danger" id="password_re_error"></span>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-6">
							<button type="submit" class="btn btn-primary">Register</button>
						</div>
					</div>
					<input type="hidden" id="password_md5" name="password"> <input
						type="hidden" id="password_re_md5" name="password_re">
				</form>
			</div>
		</div>
	</div>


</body>
</html>
