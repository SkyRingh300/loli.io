<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<jsp:include page="../taglib.jsp" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../meta.jsp"></jsp:include>
<jsp:include page="../static.jsp"></jsp:include>

<title>系统内部错误-萝莉图床</title>
</head>
<body>
  <jsp:include page="../top.jsp"></jsp:include>
  <div class="container">
    <div class="alert alert-danger" role="alert">
      系统内部错误！<%=exception%></div>
  </div>
  <jsp:include page="../bottom.jsp"></jsp:include>

</body>
</html>