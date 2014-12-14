<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<!DOCTYPE html>
<html>
<head>
<title>搜索图片-萝莉图床</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../static.jsp"></jsp:include>
<style>
.img-search-div {
    min-height: 500px;
}
</style>
</head>
<body>
  <jsp:include page="../top.jsp"></jsp:include>
  <div class="container">
    <h3>搜索图片</h3>
    <div class="img-search-div">
      <form class="navbar-form navbar-left" role="search">
        <div class="form-group">
          <input type="text" class="form-control" required placeholder="文件名(不区分大小写)">
        </div>
        <button type="submit" class="btn btn-default">搜索</button>
      </form>
    </div>
  </div>
</body>
<jsp:include page="../bottom.jsp"></jsp:include>