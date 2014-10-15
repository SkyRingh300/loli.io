<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:directive.include file="taglib.jsp" />
<head>
<title>关于-萝莉图床</title>
<link href="${pageContext.request.contextPath}/static/ext/uploader/style.css" rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<META content="图床,免费图床,屏幕截图" name="keywords" />
<meta name="description" content="一个好用的免费图床" />


<jsp:include page="static.jsp"></jsp:include>
</head>
<body>
  <jsp:include page="top.jsp"></jsp:include>
  <div class="container">

    <h3>关于loli.io</h3>
    <p>loli.io(以下均称作"本站")创建于2012年6月，最初是作为本站管理员天羽ちよこ(loli@linux.com，以下均称作"本人")的个人博客使用。</p>
    <p>本站于2014年3月正式开始提供图片存储和外链服务，致力于为小伙伴们提供方便快速稳定的图床服务。</p>
    <p>本站最初是作为本人毕业设计(《截图分享软件的设计与开发》)的服务端使用的，只是一个截图软件的服务端，后来干脆改造成图床了。目前客户端正在回炉重造，预计不久全新的图床客户端就会发布。</p>
    <p>目前本人的打算是一直努力将本站运营下去，将来也没有任何收费或者在网站上投放广告的打算。</p>
    <p>感谢各位用户的支持！</p>
  </div>
  <jsp:include page="bottom.jsp"></jsp:include>
</body>
</html>