<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
.left {
	max-width: 500px;
	width: 15%;
	min-width: 200px;
	height: 100%;
	background-color: rgb(240, 240, 240);
}

#top {
	margin-bottom: 0px !important;
}

.main {
	margin: 0;
	height: 100%
}

.left-menu {
	padding-top: 10px;
}

.left-menu>li.active>a,.left-menu>li.active>a:hover,.left-menu>li.active>a:focus
	{
	color: rgb(0, 0, 0);
	background-color: rgb(200, 200, 200);
}

.left-menu a {
	border-bottom-left-radius: 0px !important;
	border-bottom-right-radius: 0px !important;
	border-top-left-radius: 0px !important;
	border-top-right-radius: 0px !important;
}
.left-menu>li>a{
    color: rgb(0, 0, 0);
}
.left-menu>li>a:hover,.left-menu>li>a:focus {
	color: rgb(0, 0, 0);
	background-color: rgb(220, 220, 220);
}
</style>
<script>
    
</script>
<jsp:directive.include file="../taglib.jsp" />
<div class="main">
	<div class="left">
		<ul class="nav nav-pills nav-stacked left-menu" role="tablist">
			<li class="active"><a href="#">所有文件</a></li>
			<li><a href="#">图片</a></li>
		</ul>
	</div>
	<div class="right">
		<div class="controller"></div>
		<div class="filelist"></div>
	</div>
</div>