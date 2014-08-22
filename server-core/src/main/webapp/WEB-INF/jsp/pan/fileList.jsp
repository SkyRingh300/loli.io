<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<h4>
  当前路径:
  <c:forEach items="${requestScope.parentList}" var="folder">${folder.name}/</c:forEach>

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

<input type="hidden" id="folderId" value="${requestScope.parent.id}">