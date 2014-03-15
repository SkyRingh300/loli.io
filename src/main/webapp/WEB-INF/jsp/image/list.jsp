<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div id="imgList">
	<c:forEach items="${imgList}" var="img">
		<div id="singleImg">
			上传时间
			<fmt:formatDate value="${img.date}" pattern="yyyy-MM-dd HH:mm:ss" />
			上传人${img.user.email} 描述${img.description} <img src="${img.path}"
				alt="${img.description}">
		</div>
	</c:forEach>
</div>