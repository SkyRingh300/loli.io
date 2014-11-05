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
    $(document).ready(
        function() {
            $(".image-show-links input").mouseover(function() {
                $(this).focus();
                $(this).select();
            });
            //share buttons start
            $("#share-input").val(window.location.href);
            var href = $("#weibo-share-button").attr("link");
            href = href.replace("$(url)", encodeURI(window.location.href));
            $("#weibo-share-button").attr("link", href);

            href = $("#tt-share-button").attr("link");
            href = href.replace("$(url)", encodeURI(window.location.href));
            href = href.replace("$(pic)",
                encodeURI("<spring:message code="redirectPath"></spring:message>${image.redirectCode}"));

            $("#tt-share-button").attr("link", href);

            href = $("#renren-share-button").attr("link");
            href = href.replace("$(url)", encodeURI(window.location.href));
            href = href.replace("$(pic)",
                encodeURI("<spring:message code="redirectPath"></spring:message>${image.redirectCode}"));

            $("#renren-share-button").attr("link", href);

            href = $("#qz-share-button").attr("link");
            href = href.replace("$(url)", encodeURI(window.location.href));
            href = href.replace("$(pic)",
                encodeURI("<spring:message code="redirectPath"></spring:message>${image.redirectCode}"));

            $("#qz-share-button").attr("link", href);

            $(".image-share-buttons").click(
                function() {
                    window.open($(this).attr("link"), 'newwindow',
                        'toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
                });

            //share buttons end

            var v = $("#bb-input").val();

            v = "[URL=" + window.location.href + "]" + v + "[/URL]";
            $("#bb-input").val(v);

            // clip
            var client = new ZeroClipboard($(".copy-buttons"));
            client.on("copy", function(event) {
                var clipboard = event.clipboardData;
                clipboard.setData("text/plain", $(event.target).prev().val());
            });
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

.image-show-title {
    padding-bottom: 5px;
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
            <h3 class="image-show-title">
              <c:out value="${image.originName}"></c:out>
            </h3>
          </div>

          <div class="image-show-info">
            <span>上传:&nbsp;<c:if test="${empty image.user.name}">
          &lt;昵称未设置&gt;
        </c:if> <c:if test="${not empty image.user.name}">
                <c:out value="${image.user.name }"></c:out>
              </c:if>
            </span><span>所属相册:<c:if test="${empty image.gallery}">
          &lt;未设置&gt;
        </c:if> <c:if test="${not empty image.gallery}">
                <c:out value="${image.gallery.title }"></c:out>
              </c:if></span> <span>上传时间: <fmt:formatDate value="${image.date}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
          </div>
        </div>
      </div>
      <div class="image-show-right">
        <div class="image-show-links">

          <div class="image-show-share-buttons">
            <h3>分享到</h3>
            <a href="javascript:void(0)" id="weibo-share-button" class="image-share-buttons" target="_window"
              type="button"
              link="http://service.weibo.com/share/share.php?url=$(url)&appkey=&title=%E5%88%86%E4%BA%AB%E5%9B%BE%E7%89%87&pic=<spring:message code="redirectPath"></spring:message>${image.redirectCode}&ralateUid=&language=">
              <img src="${pageContext.request.contextPath}/static/img/weibo_share.png">
            </a> <a href="javascript:void(0)" id="tt-share-button" class="image-share-buttons" type="button"
              link="http://share.v.t.qq.com/index.php?c=share&a=index&url=$(url)&appkey=801547889&title=%E5%88%86%E4%BA%AB%E5%9B%BE%E7%89%87&pic=$(pic)&line1=">
              <img src="${pageContext.request.contextPath}/static/img/tt_share.png"> <a href="javascript:void(0)"
              id="renren-share-button" class="image-share-buttons" type="button"
              link="http://widget.renren.com/dialog/share?resourceUrl=$(url)&images=$(pic)&charset=utf-8"> <img
                src="${pageContext.request.contextPath}/static/img/renren_share.png"> <a href="javascript:void(0)"
                id="qz-share-button" class="image-share-buttons" type="button"
                link="http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=$(url)&showcount=1&desc=&summary=&title=%E5%88%86%E4%BA%AB%E5%9B%BE%E7%89%87&site=&pics=$(pic)&style=203&width=98&height=22&otype=share">
                  <img src="${pageContext.request.contextPath}/static/img/qz_share.png">
              </a>
          </div>
          <div>
            <h3>分享链接</h3>
            <input class="image-show-url" id="share-input" readonly value="">
            <button class="copy-buttons" type="button" class="btn btn-xs">复制</button>
          </div>
          <div>
            <h3>原图链接</h3>
            <input class="image-show-url" readonly id="origin-input"
              value="<spring:message code="redirectPath"></spring:message>${image.redirectCode}">
            <button class="copy-buttons" type="button" class="btn btn-xs">复制</button>
          </div>

          <div>
            <h3>Markdown</h3>
            <input class="image-show-url" readonly id="md-input"
              value="[LOLI.IO](<spring:message code="redirectPath"></spring:message>${image.redirectCode})">
            <button class="copy-buttons" type="button" class="btn btn-xs">复制</button>
          </div>
          <div>
            <h3>HTML</h3>
            <input type="text" class="image-show-url" readonly id="html-input"
              value="&lt;img src=&quot;<spring:message code="redirectPath"></spring:message>${image.redirectCode}&quot;&gt;">
            <button class="copy-buttons" type="button" class="btn btn-xs">复制</button>
          </div>
          <div>
            <h3>BB CODE</h3>
            <input class="image-show-url" id="bb-input" readonly
              value="[IMG]<spring:message code="redirectPath"></spring:message>${image.redirectCode}[/IMG]">
            <button class="copy-buttons" type="button" class="btn btn-xs">复制</button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <jsp:include page="../bottom.jsp"></jsp:include>
  <script src="${pageContext.request.contextPath}/static/ext/clip/ZeroClipboard.min.js"></script>
</body>
</html>