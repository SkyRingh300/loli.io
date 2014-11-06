<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 新建相册模态框 -->
<div class="modal" id="newGallery">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title">新建相册</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" id="add-gallery-form" action="${pageContext.request.contextPath}/gallery/edit/add"
          method="post" role="form">
          <div class="form-group">
            <label for="title" class="col-sm-2 control-label">相册名</label>
            <div class="col-sm-10">
              <input type="text" name="title" class="form-control" id="title" placeholder="相册名(如不填将以当前时间为相册名)">
            </div>
          </div>
          <div class="form-group">
            <label for="description" class="col-sm-2 control-label">描述</label>
            <div class="col-sm-10">
              <input type="text" name="description" class="form-control" id="description" placeholder="描述(可不填)">
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="add-gallery-submit">创建</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      </div>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>
<!-- /.modal -->