<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<!DOCTYPE html>
<head>
<title>我的相册-萝莉图床</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../static.jsp"></jsp:include>

<script type="text/javascript">
    $(document).ready(function() {
        $("#add-gallery-submit").click(function() {
            var title = $("#title").val();
            var description = $("#description").val();
            $.post("${pageContext.request.contextPath}/gallery/edit/addWithJsonResponse", {
                title : title,
                description : description
            }, function(result) {

                if (result && result.id) {
                    $('#newGallery').modal('hide');
                    $('#title').html("");
                    $('#description').html("");
                    alert("创建成功");
                    window.location.reload();
                }
            });

        });

        $(".gal-list-edit").click(function() {
            var name = $(this).parent().prev().prev().prev().prev().text();
            var desc = $(this).parent().prev().prev().prev().text();
            var gid = $(this).parent().parent().attr("gid");
            $("#edit-title").val(name);
            $("#edit-description").val(desc);
            $("#edit-gid").val(gid);
        });

        $("#edit-gallery-submit").click(function() {
            $("#edit-gallery-form").submit();
        });

        if ($(".success-info")) {
            setTimeout(function() {
                $(".success-info").hide(600);
            }, 2000);
        }
    });
</script>
<style>
.success-info {
    padding: 15px;
}
</style>
</head>
<jsp:include page="../top.jsp"></jsp:include>

<div class="gal-list container">
  <c:if test="${message!=null}">
    <p class="bg-success success-info">${message}</p>
  </c:if>
  <div class="panel panel-default">
    <div class="panel-heading">
      <span>我的相册</span>

    </div>
    <table class="table table-bordered gal-list-table">
      <thead>
        <tr>
          <th>相册名</th>
          <th>描述</th>
          <th>创建时间</th>
          <th>最后上传</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${galList}" var="gal">
          <tr gid="${gal.id}">
            <td><c:out value="${gal.title}"></c:out></td>
            <td><c:out value="${gal.description}"></c:out></td>
            <td><fmt:formatDate value="${gal.date}" pattern="yyyy-MM-dd HH:mm"></fmt:formatDate></td>
            <td><fmt:formatDate value="${gal.lastUpdate}" pattern="yyyy-MM-dd HH:mm"></fmt:formatDate></td>
            <td>
              <button type="button" class="btn btn-default btn-xs">图片</button>
              <button type="button" class="btn btn-primary btn-xs gal-list-edit" data-toggle="modal"
                data-target="#editGallery">编辑</button>
              <button type="button" class="btn btn-danger btn-xs">删除</button>
            </td>
          </tr>
        </c:forEach>
        <tr>
          <td><a href="javascript:void(0)" class="btn btn-default" id="create-gallery-btn" data-toggle="modal"
            data-target="#newGallery">新建相册</a></td>
        </tr>
      </tbody>
    </table>
  </div>
</div>


<div class="modal" id="editGallery">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title">修改相册</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" id="edit-gallery-form"
          action="${pageContext.request.contextPath}/gallery/edit/update" method="post" role="form">
          <div class="form-group">
            <label for="edit-title" class="col-sm-2 control-label">相册名</label>
            <div class="col-sm-10">
              <input type="text" required name="title" class="form-control" id="edit-title"
                placeholder="相册名(如不填将以当前时间为相册名)">
            </div>
          </div>
          <div class="form-group">
            <label for="edit-description" class="col-sm-2 control-label">描述</label>
            <div class="col-sm-10">
              <input type="text" name="description" class="form-control" id="edit-description" placeholder="描述(可不填)">
            </div>
          </div>
          <input type="hidden" name="gid" id="edit-gid">
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="edit-gallery-submit">更新</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      </div>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>
<!-- /.modal -->



<jsp:include page="add.jsp"></jsp:include>
<jsp:include page="../bottom.jsp"></jsp:include>
