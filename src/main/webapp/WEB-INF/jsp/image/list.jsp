<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<head>
<title>SCREENSHOT.PICS-查看已上传图片</title>
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
</head>
<jsp:include page="../top.jsp"></jsp:include>

<div id="imgList" class="container">
	<c:if test="${message!=null}">
		<div class="alert alert-success info">
			<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">&times;</button>
			${info}
		</div>
	</c:if>
	<div class="tip">
		<h4>
			<strong>${user.email}</strong>一共上传了<strong>${totalCount}</strong>张图片
		</h4>
	</div>
	<table class="table table-hover">
		<thead>
			<tr>
				<th>图片名</th>
				<th>上传时间</th>
				<th>图片链接</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${imgList}" var="img">
				<tr>
					<td>${img.originName}</td>
					<td><fmt:formatDate value="${img.date}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><a href="${img.path}" target="_blank">${img.path}</a></td>
					<td><a
						href="${pageContext.request.contextPath}/img/delete?id=${img.id}"
						class="delete">删除</a></td>
				</tr>
			</c:forEach>

		</tbody>
	</table>
	<div class="pages">
		<ul class="pagination">

			<li <c:if test="${not hasLast}">class="disabled"</c:if>><a
				href="${pageContext.request.contextPath}/img/list/<c:if test="${hasLast}">${currentPage-1}</c:if><c:if test="${not hasLast}">#</c:if>">&laquo;</a></li>

			<c:forEach begin="1" end="${pageCount}" varStatus="status">
				<li
					<c:if test="${currentPage eq status.index}">class="active"</c:if>><a
					href="${pageContext.request.contextPath}/img/list/<c:if test="${currentPage eq status.index}">#</c:if><c:if test="${not (currentPage eq status.index)}">${status.index}</c:if>">${status.index}</a></li>

			</c:forEach>
			<li <c:if test="${not hasNext}">class="disabled"</c:if>><a
				href="${pageContext.request.contextPath}/img/list/<c:if test="${hasNext}">${currentPage+1}</c:if><c:if test="${not hasLast}">#</c:if>">&raquo;</a></li>
		</ul>
	</div>

</div>
<jsp:include page="../bottom.jsp"></jsp:include>