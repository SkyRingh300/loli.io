<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<head>
<title>SCREENSHOT.PICS-网盘(BETA)</title>
<link href="static/ext/uploader/style.css" rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${pageContext.request.contextPath}/static/js/jquery.js"></script>
<script
	src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
<link href="${pageContext.request.contextPath}/static/css/styles.css"
	type="text/css" rel='stylesheet' />
<!-- Google web fonts -->
<link href="${pageContext.request.contextPath}/static/css/font.css"
	rel='stylesheet' />
<script type="text/javascript">
    
</script>
<style>

</style>
</head>
<jsp:include page="../top.jsp"></jsp:include>
<div class="container">
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
			<h3>拖动文件到这里或者</h3>
			<a class="btn">选择文件</a>&nbsp; <input type="file" name="image"
				multiple />
		</div>
		<button id="clear" type="button" class="btn btn-sm btn-primary">清空上传列表</button>
		&nbsp;

		<ul id="fileList">
		</ul>
	</form>
	<div id="message">
		<ul>
			<li>网盘还不稳定，上传成功后会生成URL，如果文件很大，请耐心等待数秒</li>
			<li>文件大小最大为200M，超过不会上传成功，请注意</li>
			<li>使用迅雷下载这里的文件可能会对服务器造成巨大压力</li>
		</ul>
	</div>
</div>


<jsp:include page="../bottom.jsp"></jsp:include>


<script src="static/ext/uploader/jquery.knob.js"></script>

<script src="static/ext/uploader/jquery.ui.widget.js"></script>
<script src="static/ext/uploader/jquery.iframe-transport.js"></script>
<script src="static/ext/uploader/jquery.fileupload.js"></script>

<script src="static/ext/uploader/file.script.js"></script>