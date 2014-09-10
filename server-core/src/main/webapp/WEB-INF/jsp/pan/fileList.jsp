<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />

<c:if test="${requestScope.begin}">
  <h4 class="guide-title">
    <span><c:if test="${fn:length(requestScope.parentList) gt 0}">
        <a href="javascript:void(0)" onclick="loadFolder(${requestScope.parent.parent.id})" class="guide-link">返回上一级</a>
      </c:if></span> 当前路径: <a href="javascript:void(0)" onclick="loadFolder(0)" class="guide-link">全部文件</a> <span id="full-foldername">
      <c:forEach items="${requestScope.parentList}" var="folder">
        <a href="javascript:void(0)" onclick="loadFolder(${folder.id})" class="guide-link">${folder.name}</a>/</c:forEach>
    </span>
  </h4>


  <table class="table table-hover file-list-table" tid="${requestScope.parent.id}">
    <thead>
      <tr>
        <th><input type="checkbox" class="file-checkbox-all">
          <div class="list-title">
            <span class="non-selected">文件名</span><span class="selected">选择了<span class="selected-count">0</span>个文件/文件夹
            </span>
          </div>
          <div class="list-control">
            <button type="button" class="btn btn-xs list-control-delete">删除</button>
            <button type="button" class="btn btn-xs">下载</button>
            <button type="button" class="btn btn-xs">分享</button>
          </div></th>
        <th>大小</th>
        <th>修改时间</th>
      </tr>
    </thead>
    <tbody>
      </c:if>

      <c:if test="${fn:length(requestScope.folderList)+fn:length(requestScope.fileList) eq 0 && requestScope.begin}">
        <tr>
          <td>该文件夹木有任何内容</td>
          <td>-</td>
          <td>-</td>
        </tr>
      </c:if>

      <c:forEach items="${requestScope.folderList}" var="folder">
        <tr class="folder-tr data-tr">
          <td><input type="checkbox" class="file-checkbox">
            <div class="file-label">
              <i class="glyphicon glyphicon-folder-close icon"></i><a href="javascript:void(0)" class="folder-a"
                onclick="loadFolder(${folder.id});event.stopPropagation();">${folder.name}</a>
            </div></td>
          <td>-</td>
          <td><fmt:formatDate value="${folder.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
        </tr>
      </c:forEach>
      <c:forEach items="${requestScope.fileList}" var="file">
        <tr class="file-tr data-tr" data="${file.id}">
          <td><input type="checkbox" class="file-checkbox">
            <div class="file-label">
              <i class="glyphicon glyphicon-cloud icon"></i><a href="javascript:void(0)">${file.originName}</a>
            </div>
            <div class="file-buttons">
              <a target="_blank" href="#" data="${file.id}" class="btn btn-xs btn-dl">下载</a>
              <button type="button" class="btn btn-xs btn-sh">分享</button>
            </div></td>
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
$(".data-tr").unbind("mouseenter");
$(".data-tr").unbind("mouseleave");

$(".data-tr").mouseenter(function(){
    $(this).addClass("mouse-on-tr");
});
$(".data-tr").mouseleave(function(){
    $(this).removeClass("mouse-on-tr");
});

$(".data-tr").unbind("click");
$(".file-checkbox").unbind("click");
$(".file-checkbox").click(function(e){
    if(this.checked){
        $(this).parent().parent().addClass("tr-selected");
        updateSelected();        
    } else {
        $(".file-checkbox-all").removeAttr("checked");
        $(this).parent().parent().removeClass("tr-selected");
        updateSelected();
        
    }
    e.stopPropagation();
});
$(".data-tr").unbind("click");

$(".data-tr").click(function(){
    $(this).find("td").eq(0).find("input[type=checkbox]").click();
});

$(".btn-dl").unbind("click");
$(".btn-dl").click(function(event){
    var a = this;
    if($(a).attr("href")=="#"){
        $.ajax({ 
            url: "${pageContext.request.contextPath}/pan/file/getPermentLinkByFileId", 
            async: false,
            data:{fileId:$(a).attr("data")},
            dataType:'text',
            success:function(result){
                $(a).attr("href","${pageContext.request.contextPath}/"+result);
            }
        });
    }
    event.stopPropagation();
});


$(".list-control-delete").click(function(){
    if(!confirm("删除后无法恢复, 确认删除吗？")){
        return;
    }
    var datas = new Array();
    $(".file-tr.data-tr.tr-selected").each(function(i,e){
        datas[i]=$(this).attr("data");
    });
    var ids=datas.join(",");
    $.post("${pageContext.request.contextPath}/pan/file/delete",{ids:ids},function(result){
        if(result=="success"){
            alert("删除成功");
        }

        var folderId = $("#folderId").val();
        loadFolder(folderId);
    });
    
});

</script>