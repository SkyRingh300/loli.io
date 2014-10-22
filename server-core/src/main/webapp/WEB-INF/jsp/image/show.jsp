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
        document.write("<input type='text' class='col-sm-8' value='"+window.location.href+"'/>");
    }

    $(document).ready(function() {
        $(".image-show-links input").mouseover(function() {
            $(this).focus();
            $(this).select();
        });
        $("#share-input").val(window.location.href);

        var v = $("#bb-input").val();

        v = "[URL=" + window.location.href + "]" + v + "[/URL]";
        $("#bb-input").val(v);

    });
</script>

<style>
.image-show-container {
    width: 95%;
    min-width: 1000px;
}

.image-show-left {
    width: 65%;
    max-width: 65%;
    min-width: 650px;
    float: left;
}

.image-show-right {
    width: 33%;
    min-width: 330px;
    float: left
}

.image-show-info {
    margin-right: 20px;
    margin-top: 10px;
    padding: 5px;
}

.image-show-body {
    margin-right: 20px;
    text-align: center;
}

.image-show-links {
    padding-left: 10px;
    padding-top: 10px;
    padding-bottom: 10px;
}

.image-show-info, .image-show-body, .image-show-links {
    /*border: 2px solid rgb(120, 120, 120);*/
    border-radius: 1px;
    background-color: rgb(230, 230, 230);
    color: rgb(90, 90, 90);
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
}

/*
.image-show-info, .image-show-body, .image-show-links {
    background-color: rgb(120, 120, 120);
    color: rgb(231, 231, 231);
}

.image-show-links input {
    background-color: rgb(90, 90, 90);
}
*/
.image-show-links input {
    width: 80%;
}

.image-show-body>a>img {
    max-width: 95%;
    max-height: 95%;
    margin: 5px;
}
</style>
</head>
<body>
  <jsp:include page="../top.jsp"></jsp:include>
  <div class="container">
    <div class="image-show-container">
      <div class="image-show-left">
        <div>
          <div class="image-show-body">
            <a target="_blank" href="<spring:message code="redirectPath"></spring:message>${image.redirectCode}"><img
              src="<spring:message code="redirectPath"></spring:message>${image.redirectCode}" /></a>
          </div>

          <div class="image-show-info">
            <span> 上传:<c:if test="${empty image.user.name}">
          &lt;昵称未设置&gt;
        </c:if>
            </span> <span>上传时间: <fmt:formatDate value="${image.date}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
          </div>
        </div>
      </div>
      <div class="image-show-right">
        <div class="image-show-share-buttons"></div>
        <div class="image-show-links">
          <div>
            <h3>分享链接</h3>
            <input class="image-show-url" id="share-input" readonly value="">
          </div>
          <div>
            <h3>原图链接</h3>
            <input class="image-show-url" readonly id="origin-input"
              value="<spring:message code="redirectPath"></spring:message>${image.redirectCode}">
          </div>

          <div>
            <h3>Markdown</h3>
            <input class="image-show-url" readonly id="md-input"
              value="[LOLI.IO](<spring:message code="redirectPath"></spring:message>${image.redirectCode})">
          </div>
          <div>
            <h3>HTML</h3>
            <input type="text" class="image-show-url" readonly id="html-input"
              value="&lt;img src=&quot;<spring:message code="redirectPath"></spring:message>${image.redirectCode}&quot;&gt;">
          </div>
          <div>
            <h3>BB CODE</h3>
            <input class="image-show-url" id="bb-input" readonly
              value="[IMG]<spring:message code="redirectPath"></spring:message>${image.redirectCode}[/IMG]">
          </div>
        </div>
      </div>
    </div>
  </div>
  <jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>