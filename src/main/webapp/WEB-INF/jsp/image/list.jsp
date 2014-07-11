<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<!DOCTYPE html>
<head>
<title>SCREENSHOT.PICS-查看已上传文件</title>
<link
	href="<spring:message code="staticPath"></spring:message>/style.css"
	rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="../static.jsp"></jsp:include>
<script type="text/javascript">
    $(document).ready(function(e) {
        $(".delete").click(function(e) {
            if (window.confirm("删除后无法恢复！ 确认删除吗？")) {
            } else {
                e.preventDefault();
            }
        });
    });
</script>
<style>
.search-form {
	padding-left: 0px !important;
}

.bg-info {
	padding: 15px;
}

.info {
	width: 100% !important;
	margin-bottom: 15px !important;
}
</style>
</head>
<jsp:include page="../top.jsp"></jsp:include>
<div id="imgList" class="container">
	<div class="tip">
		<h4>
			<strong>${user.email}</strong>一共上传了<strong>${count}</strong>个文件
			<c:if
				test="${not(requestScope.totalCount eq 0) and not(requestScope.fileName eq null)}">, 搜索出<strong>${requestScope.totalCount}</strong>个文件</c:if>
		</h4>
		<c:if test="${param.message!=null}">
			<div class="alert alert-success info">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
				${param.message}
			</div>
		</c:if>
		<div class="search-form col-md-4">
			<form role="form"
				action="${pageContext.request.contextPath}/img/search">
				<div class="input-group">
					<input type="text" name="fileName" value="${requestScope.fileName}"
						placeholder="文件名" class="form-control" required> <span
						class="input-group-btn">
						<button class="btn btn-default" type="submit">搜索</button>
					</span>
				</div>
			</form>
		</div>

	</div>
	<c:if test="${not(requestScope.totalCount eq 0)}">
		<table class="table table-hover">
			<thead>
				<tr>
					<th>文件名</th>
					<th>上传时间</th>
					<th>链接</th>
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
						<td><a type="button" class="btn-danger delete btn btn-xs"
							href="${pageContext.request.contextPath}/img/delete?id=${img.id}">删除</a></td>
					</tr>
				</c:forEach>

			</tbody>
		</table>
		<div class="pages">
			<ul class="pagination">

				<li <c:if test="${not hasLast}">class="disabled"</c:if>><a
					href="${pageContext.request.contextPath}/img/<c:if test="${requestScope.fileName eq null}">list/</c:if><c:if test="${not(requestScope.fileName eq null)}">search?fileName=${requestScope.fileName}&page=</c:if><c:if test="${hasLast}">${currentPage-1}</c:if><c:if test="${not hasLast}">#</c:if>">&laquo;</a></li>

				<c:forEach begin="1" end="${pageCount}" varStatus="status">
					<li
						<c:if test="${currentPage eq status.index}">class="active"</c:if>><a
						href="${pageContext.request.contextPath}/img/<c:if test="${requestScope.fileName eq null}">list/</c:if><c:if test="${not(requestScope.fileName eq null)}">search?fileName=${requestScope.fileName}&page=</c:if><c:if test="${currentPage eq status.index}">#</c:if><c:if test="${not (currentPage eq status.index)}">${status.index}</c:if>">${status.index}</a></li>

				</c:forEach>
				<li <c:if test="${not hasNext}">class="disabled"</c:if>><a
					href="${pageContext.request.contextPath}/img/<c:if test="${requestScope.fileName eq null}">list/</c:if><c:if test="${not(requestScope.fileName eq null)}">search?fileName=${requestScope.fileName}&page=</c:if><c:if test="${hasNext}">${currentPage+1}</c:if><c:if test="${not hasLast}">#</c:if>">&raquo;</a></li>
			</ul>
		</div>

	</c:if>



</div>
<c:if
	test="${(requestScope.totalCount eq 0) and not(requestScope.fileName eq null)}">
	<div class="container">
		<p class="bg-info  col-md-8">未搜索出结果</p>
	</div>
</c:if>
<jsp:include page="../bottom.jsp"></jsp:include>