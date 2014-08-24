<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<h4>
  当前路径: <a href="javascript:void(0)" onclick="loadFolder(0)">全部文件</a>
  <c:forEach items="${requestScope.parentList}" var="folder">
    <a href="javascript:void(0)" onclick="loadFolder(${folder.id})">${folder.name}</a>/</c:forEach>

</h4>
<table class="table table-hover" tid="${requestScope.parent.id}">
  <thead>
    <tr>
      <th>文件名</th>
      <th>大小</th>
      <th>修改时间</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><a href="javascript:void(0)" onclick="loadFolder(${requestScope.parent.parent.id})">...返回上一级</a></td>
      <td></td>
      <td></td>
    </tr>
    <c:forEach items="${requestScope.folderList}" var="folder">
      <tr>
        <td><a href="javascript:void(0)" onclick="loadFolder(${folder.id})">${folder.name}</a></td>
        <td>-</td>
        <td><fmt:formatDate value="${folder.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
      </tr>
    </c:forEach>
  </tbody>
</table>


<form enctype="multipart/form-data" method="post" action="${pageContext.request.contextPath}/pan/file/upload"
  class="upload-form">
  <div id="drop-area">
    <input type="file" name="file" multiple />
  </div>
  <input type="hidden" id="folderId" name="folderId" value="${requestScope.parent.id}">
</form>

<script>
bindUploadFile();

</script>