<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>网盘(内测中)-萝莉图床</title>


<jsp:include page="../static.jsp"></jsp:include>

<script src="<spring:message code="staticPath"></spring:message>/jquery.knob.js"></script>
<script src="<spring:message code="staticPath"></spring:message>/jquery.ui.widget.js"></script>
<script src="<spring:message code="staticPath"></spring:message>/jquery.iframe-transport.js"></script>
<script src="<spring:message code="staticPath"></spring:message>/jquery.fileupload.js"></script>
<link href="${pageContext.request.contextPath}/static/css/pan.css" rel="stylesheet" />

</head>
<body>
  <jsp:include page="head.jsp"></jsp:include>
  <jsp:include page="main.jsp"></jsp:include>
  <jsp:include page="uploader.jsp"></jsp:include>
</body>
</html>