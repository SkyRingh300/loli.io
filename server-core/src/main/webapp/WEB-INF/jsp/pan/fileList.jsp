<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />

<c:if test="${requestScope.begin}">
  <h4 class="guide-title">
    <span><c:if test="${fn:length(requestScope.parentList) gt 0}">
        <a href="javascript:void(0)" onclick="loadFolder(${requestScope.parent.parent.id})" class="guide-link">返回上一级</a>
      </c:if></span> 当前路径: <a href="javascript:void(0)" onclick="loadFolder(0)" class="guide-link">全部文件</a>
    <c:forEach items="${requestScope.parentList}" var="folder">
      <a href="javascript:void(0)" onclick="loadFolder(${folder.id})" class="guide-link">${folder.name}</a>/</c:forEach>
  </h4>
</c:if>


<table class="table table-hover file-list-table" tid="${requestScope.parent.id}">
  <thead>
    <tr>
      <th><input type="checkbox" class="file-checkbox-all">
      <div class="list-title">
          <span class="non-selected">文件名</span><span class="selected">选择了<span class="selected-count">0</span>个文件/文件夹
          </span>
        </div></th>
      <th>大小</th>
      <th>修改时间</th>
    </tr>
  </thead>
  <tbody>

    <c:if test="${fn:length(requestScope.folderList)+fn:length(requestScope.fileList) eq 0 && requestScope.begin}">
      <tr>
        <td>该文件夹木有任何内容</td>
        <td>-</td>
        <td>-</td>
      </tr>
    </c:if>

    <c:forEach items="${requestScope.folderList}" var="folder">
      <tr class="folder-tr">
        <td><input type="checkbox" class="file-checkbox"><i class="glyphicon glyphicon-folder-close icon"></i><a
          href="javascript:void(0)" onclick="loadFolder(${folder.id})">${folder.name}</a></td>
        <td>-</td>
        <td><fmt:formatDate value="${folder.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
      </tr>
    </c:forEach>
    <c:forEach items="${requestScope.fileList}" var="file">
      <tr class="file-tr">
        <td><input type="checkbox" class="file-checkbox"><i class="glyphicon glyphicon-cloud icon"></i><a
          href="javascript:void(0)">${file.originName}</a></td>
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


<script>
//只执行一次的script
$(".file-checkbox-all").click(function(){
    if(this.checked){
        $(".file-checkbox").each(function(){
            this.checked=true;
        });
        $(".folder-tr").each(function(){
            $(this).addClass("tr-selected");
        });
        $(".file-tr").each(function(){
            $(this).addClass("tr-selected");
        });
        
        
    }else{

        $(".file-checkbox").each(function(){
            $(this).removeAttr("checked");
        });
        $(".folder-tr").each(function(){
            $(this).removeClass("tr-selected");
        });

        $(".file-tr").each(function(){
            $(this).removeClass("tr-selected");
        });
        
        
    }
    updateSelected();
    
});


</script>
</c:if>
<script>
//每次加载都执行的script
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

$(".file-checkbox").unbind("click");
$(".file-checkbox").click(function(){
    if(this.checked){
        $(this).parent().parent().addClass("tr-selected");
        updateSelected();
        
        
    } else {
        $(".file-checkbox-all").removeAttr("checked");
        $(this).parent().parent().removeClass("tr-selected");
        updateSelected();
        
    }
});



</script>