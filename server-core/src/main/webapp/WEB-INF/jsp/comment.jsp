<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:directive.include file="taglib.jsp" />
<head>
<title>建议-萝莉图床</title>
<link href="${pageContext.request.contextPath}/static/ext/uploader/style.css" rel="stylesheet" />
<jsp:include page="meta.jsp"></jsp:include>

<jsp:include page="static.jsp"></jsp:include>
</head>
<body>

  <jsp:include page="top.jsp"></jsp:include>
  <div class="container">
    <h3>有神马建议或者想法都可以留言～</h3>
    <h4>
      如果不想被别人看到，可以发送邮件至<a href="mailto:loli@linux.com" target="_blank">loli@linux.com</a>
    </h4>
    <!-- 多说评论框 start -->
    <div class="ds-thread" data-thread-key="comment" data-title="评论和建议"
      data-url="${pageContext.request.contextPath}/comment"></div>
    <!-- 多说评论框 end -->
    <!-- 多说公共JS代码 start (一个网页只需插入一次) -->
    <script type="text/javascript">
                    var duoshuoQuery = {
                        short_name : "loliio"
                    };
                    (function() {
                        var ds = document.createElement('script');
                        ds.type = 'text/javascript';
                        ds.async = true;
                        ds.src = (document.location.protocol == 'https:' ? 'https:' : 'http:')
                            + '//static.duoshuo.com/embed.js';
                        ds.charset = 'UTF-8';
                        (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0])
                            .appendChild(ds);
                    })();
                </script>
    <!-- 多说公共JS代码 end -->






  </div>
  <jsp:include page="bottom.jsp"></jsp:include>
</body>
</html>