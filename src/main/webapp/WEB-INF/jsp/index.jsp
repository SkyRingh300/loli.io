<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head>
<title>SCREENSHOT.PICS-截图上传</title>
<link href="static/ext/uploader/style.css" rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script
	src="${pageContext.request.contextPath}/static/js/jquery.js"></script>
<script
	src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
<link
    href="${pageContext.request.contextPath}/static/css/styles.css" type="text/css"
    rel='stylesheet' />
<!-- Google web fonts -->
<link
	href="${pageContext.request.contextPath}/static/css/font.css"
	rel='stylesheet' />
</head>
<body>
	<jsp:include page="image/anoUpload.jsp"></jsp:include>
	<jsp:include page="bottom.jsp"></jsp:include>
</body>