<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<table>
	<c:forEach items="${requestScope.folderList}" var="folder">

		<tr>
			<td>${folder.name}</td>
		</tr>

	</c:forEach>


</table>