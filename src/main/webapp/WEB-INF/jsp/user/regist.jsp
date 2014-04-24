<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<script type="text/javascript">
    $(document).ready(function() {
        $("#sendEmail").click(function(e){
            if(validateEmail()){
                $.post("${rootPath}/mail/send",{email:$("#user-email").val()},function(e){
                    alert(e);
                    },"text");
            }});
        
            
        
    });
</script>
</head>
<body>
	<jsp:include page="../top.jsp"></jsp:include>

	<div id="main">
		<div class="container">
			<div class="register-form">
				<h2>用户注册</h2>
				<form class="form-horizontal" action="regist" method="POST"
					onsubmit="return md5password();">
					<div class="form-group">
						<label for="user-email" class="col-sm-4 control-label">邮箱</label>
						<div class="col-sm-4">
							<input type="email" name="email" class="form-control"
								id="user-email" placeholder="邮箱">
						</div>
						<span class="label label-danger" id="email-error">${message["email"]}
						</span>
					</div>
					<div class="form-group">
						<label for="验证码" class="col-sm-4 control-label">验证码</label>

						<div class="col-sm-4">
							<input type="text" name="token" class="form-control"
								id="user-token" placeholder="验证码">
						</div>
						<input type="button" id="sendEmail" class="btn" value="发送验证码">
						<span class="label label-danger" id="token-error">${message["email"]}
						</span>
					</div>
					<div class="form-group">
						<label for="user-password" class="col-sm-4 control-label">密码</label>
						<div class="col-sm-4">
							<input type="password" class="form-control" name="password_ori"
								id="user-password" placeholder="密码">
						</div>
						<span class="label label-danger" id="password_error"></span>

					</div>
					<div class="form-group">
						<label for="password_re" class="col-sm-4 control-label">重复输入密码</label>
						<div class="col-sm-4">
							<input type="password" class="form-control"
								name="password_re_ori" id="password_re" placeholder="重复输入密码">
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
