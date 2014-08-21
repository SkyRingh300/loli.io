<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<table class="table table-hover" tid="${requestScope.currentFolder.id}">
	<thead>
		<tr>
			<th>文件名</th>
			<th>大小</th>
			<th>修改时间</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><a>...返回上一级</a></td>
			<td></td>
			<td></td>
		</tr>
		<c:forEach items="${requestScope.folderList}" var="folder">
			<tr>
				<td>${folder.name}</td>
				<td>-</td>
				<td><fmt:formatDate value="${folder.createDate}"
						pattern="yyyy-MM-dd HH:mm:ss" /></td>
			</tr>
		</c:forEach>
	</tbody>
</table>