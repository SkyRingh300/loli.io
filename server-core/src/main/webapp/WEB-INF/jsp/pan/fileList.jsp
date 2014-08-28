<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />

<c:if test="${requestScope.begin}">
  <h4>
    当前路径: <a href="javascript:void(0)" onclick="loadFolder(0)" class="guide-link">全部文件</a>
    <c:forEach items="${requestScope.parentList}" var="folder">
      <a href="javascript:void(0)" onclick="loadFolder(${folder.id})" class="guide-link">${folder.name}</a>/</c:forEach>
  </h4>


  <table class="table table-hover file-list-table" tid="${requestScope.parent.id}">
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
      </c:if>

      <c:if test="${fn:length(requestScope.folderList)+fn:length(requestScope.fileList) eq 0 && requestScope.begin}">
        <tr>
          <td>该文件夹木有任何内容</td>
          <td>-</td>
          <td>-</td>
        </tr>
      </c:if>

      <c:forEach items="${requestScope.folderList}" var="folder">
        <tr class="folder-tr">
          <td><input type="checkbox" class="file-checkbox"><i class="glyphicon glyphicon-folder-close icon"></i><a href="javascript:void(0)"
            onclick="loadFolder(${folder.id})">${folder.name}</a></td>
          <td>-</td>
          <td><fmt:formatDate value="${folder.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
        </tr>
      </c:forEach>
      <c:forEach items="${requestScope.fileList}" var="file">
        <tr class="file-tr">
          <td><input type="checkbox" class="file-checkbox"><i class="glyphicon glyphicon-cloud icon"></i><a href="javascript:void(0)">${file.originName}</a></td>
          <td>${file.size}</td>
          <td><fmt:formatDate value="${file.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
        </tr>

      </c:forEach>


      <c:if test="${requestScope.begin}">
    </tbody>
  </table>


  <form enctype="multipart/form-data" method="post" action="${pageContext.request.contextPath}/pan/file/upload"
    class="upload-form">
    <div id="drop-area">
      <input type="file" name="file" multiple />
    </div>
    <input type="hidden" id="folderId" name="folderId" value="${requestScope.parent.id}">
  </form>
</c:if>
<script>
bindUploadFile();
<c:if test="${requestScope.begin}">
pageCount = ${fn:length(requestScope.folderList)};
fileCount = ${fn:length(requestScope.fileList)};
</c:if>
<c:if test="${not requestScope.begin}">
pageCount = pageCount + ${fn:length(requestScope.folderList)};
fileCount = fileCount + ${fn:length(requestScope.fileList)};
</c:if>
canList = true;

</script>