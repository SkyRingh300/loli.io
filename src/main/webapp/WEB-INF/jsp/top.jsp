<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="taglib.jsp" />
<style>
.navbar-default {
	background-color: rgb(94, 94, 94) !important;
}

.navbar-nav>.active>a,.navbar-nav>.active>a:hover {
	color: rgb(255, 255, 255) !important;
	background-color: rgb(70, 50, 101);
}

.title {
	color: rgb(230, 230, 230) !important;
}

.open>a {
	background-color: rgb(77, 77, 77) !important;
}
</style>

<nav id="top" class="navbar navbar-default navbar-static-top"
	role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">点击下拉</span> <span class="icon-bar"></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="navbar-brand title"
				href="${pageContext.request.contextPath}/">SCREENSHOT.PICS(BETA)</a>
		</div>

		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="${pageContext.request.contextPath}/comment"
					class="title">留言和建议</a></li>
			</ul>

			<ul class="nav navbar-nav navbar-right">
				<c:if test="${sessionScope.user eq null}">
					<li><a href="${pageContext.request.contextPath}/user/regist"
						class="title">注册</a></li>
					<li><a href="${pageContext.request.contextPath}/user/login"
						class="title">登陆</a></li>
				</c:if>
				<c:if test="${sessionScope.user!=null}">
					<li><a href="#" class="title dropdown-toggle"
						data-toggle="dropdown">${sessionScope.user.email} <b
							class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="${pageContext.request.contextPath}/user/edit">修改密码</a></li>
							<li><a href="${pageContext.request.contextPath}/img/list">查看已上传图片</a></li>
							<li><a href="${pageContext.request.contextPath}/pan">网盘(BETA)</a></li>
						</ul></li>

					<li><a href="${pageContext.request.contextPath}/user/logout"
						class="title">登出</a></li>
				</c:if>
			</ul>
		</div>
	</div>
</nav>
