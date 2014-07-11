<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:directive.include file="../taglib.jsp" />

<head>
<title>SCREENSHOT.PICS-修改密码</title>
<link
	href="<spring:message code="staticPath"></spring:message>/style.css"
	rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="../static.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/static/js/md5.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/js/edit.js"></script>

<link
	href="<spring:message code="staticPath"></spring:message>/font.css"
	rel='stylesheet' />

<script>
    $(document).ready(function() {
        $("#edit-form").submit(function(e) {
            var var1 = md5password();
            if (!var1) {
                e.preventDefault();
            }
        });

    });
</script>

<style>
.bg-info {
	padding: 15px;
}

.info-e {
	margin: 0 auto;
	width:50%;
}

.edit-form{
    height:380px;
}
</style>
</head>

<jsp:include page="../top.jsp"></jsp:include>
<div class="container">
	<div class="edit-form">

		<form class="form-horizontal" id="edit-form" action="edit"
			method="POST">
			<fieldset>
				<legend>修改密码</legend>

				<div class="form-group">
					<label for="user-email" class="col-sm-4 control-label">邮箱</label>
					<div class="col-sm-4">
						<span class="label label-default">${user.email}</span>
					</div>

				</div>

				<div class="form-group">
					<label for="user-password-old" class="col-sm-4 control-label">旧密码</label>
					<div class="col-sm-4">
						<input type="password" class="form-control" id="user-password-old"
							required placeholder="旧密码">
					</div>
					<span class="label label-danger" id="password_old_error"></span>

				</div>

				<div class="form-group">
					<label for="user-password" class="col-sm-4 control-label">新密码</label>
					<div class="col-sm-4">
						<input type="password" class="form-control" required
							id="user-password" placeholder="新密码">
					</div>
					<span class="label label-danger" id="password_error"></span>

				</div>
				<div class="form-group">
					<label for="password_re" class="col-sm-4 control-label">重复输入新密码</label>
					<div class="col-sm-4">
						<input type="password" class="form-control" required
							id="password_re" placeholder="重复输入新密码">
					</div>
					<span class="label label-danger" id="password_re_error"></span>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-4 col-sm-6">
						<button id="edit-submit" type="submit" class="btn btn-primary">修改</button>
					</div>
				</div>
				<input type="hidden" id="password_md5" name="password"> <input
					type="hidden" id="password_re_md5" name="password_re"> <input
					type="hidden" id="password_old_md5" name="password_old">
			</fieldset>
		</form>
	</div>
	<c:if test="${not(message eq null) }">
		<div class="info-e">
			<div class="bg-info">${message }</div>
		</div>
	</c:if>

</div>

<jsp:include page="../bottom.jsp"></jsp:include>
