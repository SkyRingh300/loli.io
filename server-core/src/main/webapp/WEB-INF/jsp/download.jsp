<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:directive.include file="taglib.jsp" />
<head>
<title>下载客户端-萝莉图床</title>
<link href="static/ext/uploader/style.css" rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="static.jsp"></jsp:include>
<style type="text/css">
.download {
	width: 50%;
	margin-left: 25%%;
	padding-top: 30px;
	border-width: 1px;
	border-color: rgb(221, 221, 221);
}
</style>
</head>
<body>
	<jsp:include page="./top.jsp"></jsp:include>
	<div class="container">
		<div class="download">
			<h2>客户端下载</h2>
			<br />
			<p>
				这是一个<strong>截图客户端</strong>，可以运行在XP之后的(不包括XP)Windows、Mac、Linux上。<br />
				需要安装<a target="_blank"
					href="http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html"><strong>Java
						8</strong></a>或以上版本才能运行<br /> 首次运行请打开设置-连接配置一下 <br />
			</p>
			<p>
				<span> 最新版本为0.0.1-ALPHA，问题反馈请发送邮件至<a
					href="mailto:loli@linux.com" target="_blank">loli@linux.com</a></span>
			</p>
			<ul>
				<li><a href="http://1.loli.io/io.loli.sc-0.0.1.zip">zip</a></li>
				<li><a href="http://1.loli.io/io.loli.sc-0.0.1.tar.gz">tar.gz</a></li>
				<li><a href="http://1.loli.io/io.loli.sc-0.0.1-win.zip">Windows
						zip</a></li>
				<li><a href="http://1.loli.io/io.loli.sc-0.0.1.exe">Windows
						installer</a></li>
			</ul>

		</div>
	</div>

	<jsp:include page="bottom.jsp"></jsp:include>
</body>