<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<head>
<title>SCREENSHOT.PICS-截图上传</title>
<link
	href="${pageContext.request.contextPath}/static/ext/uploader/style.css"
	rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="static/js/jquery.js"></script>
<script
	src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
<!-- Google web fonts -->
<link
	href="http://fonts.googleapis.com/css?family=PT+Sans+Narrow:400,700"
	rel='stylesheet' />
<style>
#upload ul li span {
	background:
		url('${pageContext.request.contextPath}/static/ext/uploader/icons.png')
		no-repeat !important;
}

#drop {
	border-image:
		url('${pageContext.request.contextPath}/static/ext/uploader/border-image.png')
		25 repeat !important;
}
</style>
</head>
<body>
	<jsp:include page="../top.jsp"></jsp:include>
	<c:if test="${info!=null}">
		<div class="alert alert-success info">
			<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">&times;</button>
			${info}
		</div>
	</c:if>
	<form id="upload" method="post"
		action="${pageContext.request.contextPath}/api/upload"
		enctype="multipart/form-data">
		<div id="drop">
			<h3>拖动图片到这里或者</h3>
			<a class="btn">选择图片</a>&nbsp; <input type="file" name="image"
				multiple />
		</div>
		<button id="clear" type="button" class="btn btn-sm btn-primary">清空上传列表</button>
		&nbsp;

		<ul id="fileList">
		</ul>

	</form>
	<img id="img">


	<script src="static/ext/uploader/jquery.knob.js"></script>

	<script src="static/ext/uploader/jquery.ui.widget.js"></script>
	<script src="static/ext/uploader/jquery.iframe-transport.js"></script>
	<script src="static/ext/uploader/jquery.fileupload.js"></script>

	<script src="static/ext/uploader/script.js"></script>