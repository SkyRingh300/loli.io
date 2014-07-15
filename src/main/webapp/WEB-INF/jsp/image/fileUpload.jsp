<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<!DOCTYPE html>
<head>
<title>SCREENSHOT.PICS-网盘(BETA)</title>
<link
	href="<spring:message code="staticPath"></spring:message>/style.css"
	rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="../static.jsp"></jsp:include>
<script type="text/javascript">
    
</script>
<style>
#upload ul li p {
	left: 0px !important;
}
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


<script
	src="<spring:message code="staticPath"></spring:message>/jquery.knob.js"></script>
<script
	src="<spring:message code="staticPath"></spring:message>/jquery.ui.widget.js"></script>
<script
	src="<spring:message code="staticPath"></spring:message>/jquery.iframe-transport.js"></script>
<script
	src="<spring:message code="staticPath"></spring:message>/jquery.fileupload.js"></script>
<script
	src="${pageContext.request.contextPath}/static/ext/uploader/file.script.js"></script>
<input type="hidden" id="redirectPath"
	value="<spring:message code="redirectPath"></spring:message>">