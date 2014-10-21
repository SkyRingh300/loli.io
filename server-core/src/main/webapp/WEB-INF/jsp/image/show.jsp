<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:directive.include file="../taglib.jsp" />
<head>
<title>${image.originName}-查看图片-萝莉图床</title>
<link href="${pageContext.request.contextPath}/static/ext/uploader/style.css" rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<META content="图床,免费图床,屏幕截图" name="keywords" />
<meta name="description" content="一个好用的免费图床" />


<jsp:include page="../static.jsp"></jsp:include>
<script>
    function writeCurrentUrl() {
        document.write(window.location.href);
    }
</script>
</head>
<body>
  <jsp:include page="../top.jsp"></jsp:include>
  <div class="container">
    <div class="image-show-container">
      <div class="image-show-left">
        <div class="image-show-body">
          <img src="<spring:message code="redirectPath"></spring:message>${image.redirectCode}" />
        </div>

        <div class="image-show-info">
          <span> 上传:<c:if test="${empty image.user.name}">
          &lt;昵称未设置&gt;
        </c:if>
          </span> <span>上传时间: <fmt:formatDate value="${image.date}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
        </div>
      </div>
      <div class="image-show-right">
        <div class="image-show-share-buttons"></div>
        <div class="image-show-links">
          <span>分享链接:<script>writeCurrentUrl()</script></span>
          <span>原图链接:<spring:message code="redirectPath"></spring:message>${image.redirectCode}</span>
          <span>Markdown:[LOLI.IO](<spring:message code="redirectPath"></spring:message>${image.redirectCode})</span>
          <span>HTML:<a href="" target="_blank"><input type="text" value="&lt;img src=&quot;<spring:message code="redirectPath"></spring:message>${image.redirectCode}&quot;&gt;"></a></span>
          <span>BB CODE:[URL=<script>writeCurrentUrl()</script>][IMG]<spring:message code="redirectPath"></spring:message>${image.redirectCode}[/IMG][/URL]</span>
          
        </div>
      </div>
    </div>
  </div>
  <jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>