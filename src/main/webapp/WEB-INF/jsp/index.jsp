<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<head>
<title>SCREENSHOT.PICS-好用的图床</title>
<link href="static/ext/uploader/style.css" rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<META content="图床,免费图床,屏幕截图" name="keywords" />
<meta name="description" content="一个好用的免费图床" />
<script
	src="<spring:message code="staticPath"></spring:message>/jquery.js"></script>
<script
	src="<spring:message code="staticPath"></spring:message>/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="<spring:message code="staticPath"></spring:message>/bootstrap.min.css">
<link href="${pageContext.request.contextPath}/static/css/styles.css"
	type="text/css" rel='stylesheet' />
<!-- Google web fonts -->
<link href="${pageContext.request.contextPath}/static/css/font.css"
	rel='stylesheet' />
</head>
<body>
	<jsp:include page="image/anoUpload.jsp"></jsp:include>
	<jsp:include page="bottom.jsp"></jsp:include>
</body>