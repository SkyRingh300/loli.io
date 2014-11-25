<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:directive.include file="taglib.jsp" />
<head>
<title>萝莉图床</title>
<link href="${pageContext.request.contextPath}/static/ext/uploader/style.css" rel="stylesheet" />
<jsp:include page="meta.jsp"></jsp:include>
<jsp:include page="static.jsp"></jsp:include>
<style>

</style>
</head>
<body>
  <jsp:include page="top.jsp"></jsp:include>
  <jsp:include page="image/anoUpload.jsp"></jsp:include>
  <jsp:include page="bottom.jsp"></jsp:include>
</body>
</html>