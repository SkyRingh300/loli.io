<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
			<h3>拖动图片到这里或者</h3>
			<a class="btn">选择图片</a>&nbsp; <input type="file" name="image"
				multiple />
		</div>
		<button id="clear" type="button" class="btn btn-sm btn-primary">清空上传列表</button>
		&nbsp;

		<ul id="fileList">
		</ul>

	</form>
	<div id="message">
		<ul>
			<li>2014-07-02: 增加文件名搜索功能</li>
			<li>2014-06-14: 图片使用域名"1.loli.io", 其余使用"2.loli.io"</li>
			<li>2014-06-13: 登陆后可以查看并删除已上传的图片</li>
			<!-- <li>2014-06-13: 保留原文件的扩展名(不再是统一的png)</li> -->
			<!-- <li>2014-06-13: 增加非图片类文件的上传(文件外链), 见页面底部</li> -->
		</ul>
	</div>
</div>


<script src="static/ext/uploader/jquery.knob.js"></script>

<script src="static/ext/uploader/jquery.ui.widget.js"></script>
<script src="static/ext/uploader/jquery.iframe-transport.js"></script>
<script src="static/ext/uploader/jquery.fileupload.js"></script>

<script src="static/ext/uploader/script.js"></script>