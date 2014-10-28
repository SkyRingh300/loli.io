<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<!DOCTYPE html>
<head>
<title>查看已上传文件-萝莉图床</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="../static.jsp"></jsp:include>
<script type="text/javascript">
    function change(tag) {
        if ((!$(tag).attr("type")) && $(tag).val() == 0) {
            var html = '<input type="text" class="col-md-2 form-control tag-input" onblur="updateTag(this)">';
            $(tag).after(html);
            $(tag).parent().find("input").focus();
            $(tag).remove();
            return;
        }
    }
    function updateTag(tag) {
        if ($(tag).val() != "" && $(tag).val().trim() != "") {
            var imageId = $(tag).parent().parent().attr("id");

            var span = $(tag).parent().find("span");

            if ($(tag).attr("type")) {

                if ($(tag).val() == "" || $(tag).val().trim() == "") {
                } else {
                    span.text($(tag).val());
                    var name = $(tag).val();

                    $(tag).remove();
                    $.post("${pageContext.request.contextPath}/tag/add", {
                        name : $(tag).val(),
                        imageId : imageId
                    }, function(result) {
                        span.attr("tag-id", result);
                    });
                }
                span.show();

            } else {
                if ($(tag).val() == 0) {
                    return;
                }
                if (span.attr("tag-id") != $(tag).val()) {
                    span.text($(tag).find("option:selected").text());
                    span.show();
                    var id = $(tag).val();

                    $(tag).remove();
                    $.post("${pageContext.request.contextPath}/tag/add", {
                        id : id,
                        imageId : imageId
                    }, function(result) {
                        span.attr("tag-id", $(tag).val());
                    });
                } else {
                    $(tag).remove();
                    span.show();
                }
            }
        } else {
            $(tag).parent().find("span").show();
            $(tag).remove();
        }

    }
    $(document).ready(function(e) {
        $(".image-list-delete-btn").click(function(e) {
            if (window.confirm("删除后无法恢复！ 确认删除吗？")) {
            } else {
                e.preventDefault();
            }
        });
        $(".tag-span").click(function(e) {
            var td = this;
            $.post("${pageContext.request.contextPath}/tag/list", function(result) {
                var html = "";
                if (result.length > 0) {
                    html += "<select onchange='change(this)' onblur='updateTag(this)'>";
                    for (i = 0; i < result.length; i++) {

                        html += "<option value='" + result[i].id + "' ";
                        if ($(td).attr("tag-id") == result[i].id) {
                            html += "selected";
                        }
                        html += ">";
                        html += result[i].name;
                        html += "</option>"
                    }
                    html += "<option value='0'>添加新分类</option>"
                    html += "</select>";
                } else {
                    html += '<input type="text" class="col-md-2 form-control tag-input" onblur="updateTag(this)">';
                }
                $(td).parent().find("span").hide();
                $(td).after($(html));
                $(td).next().focus();
            });
        });

        $("#add-gallery-submit").click(function() {
            $("#add-gallery-form").submit();
        });

    });
</script>
<style>
.search-form {
    padding-left: 0px !important;
}

.tag-input {
    padding: 0 !important;
    height: 20px !important;
    max-width: 50px;
    display: inline;
    float: none;
}

.bg-info {
    padding: 15px;
}

.info {
    width: 100% !important;
    margin-bottom: 15px !important;
}

.image-list-table-show {
    height: 150px;
    width: 150px;
}

.image-list-table-single {
    float: left;
    padding: 10px;
    margin: 5px;
    overflow: hidden;
    border-radius: 2px;
    background-color: rgb(230, 230, 230);
}

.image-list-table {
    float: left;
    margin-top: 40px;
}

.image-list-delete-btn {
    float: right;
}

.image-list-table-single-control {
    margin-top: 5px;
}

.image-gallery-list>a {
    background-color: white;
}
</style>
</head>
<jsp:include page="../top.jsp"></jsp:include>
<div id="imgList" class="container">
  <div class="tip">
    <h4>
      <strong>${user.email}</strong>一共上传了<strong>${count}</strong>个文件
      <c:if test="${not(requestScope.totalCount eq 0) and not(requestScope.fileName eq null)}">, 搜索出<strong>${requestScope.totalCount}</strong>个文件</c:if>
    </h4>
    <c:if test="${param.message!=null}">
      <div class="alert alert-success info">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        ${param.message}
      </div>
    </c:if>
    <!-- 
    <div class="col-md-8">
      <form role="form" class="form-inline" action="${pageContext.request.contextPath}/img/search">
        <div class="input-group">
          <input type="text" name="fileName" value="${requestScope.fileName}" placeholder="文件名" class="form-control">
        </div>
        <div class="input-group col-md-3">
          <select name="tag" class="form-control">
            <option value="0">所有分类</option>
            <c:forEach items="${tagList}" var="tag">
              <option value="${tag.id}" <c:if test="${param.tag eq tag.id}">selected</c:if>><c:out
                  value="${tag.name}"></c:out></option>
            </c:forEach>
          </select>
        </div>
        <div class="input-group">
          <span class="input-group-btn">
            <button class="btn btn-default" type="submit">搜索</button>
          </span>
        </div>
      </form>
    </div>
 -->
  </div>

  <div class="image-list-controllers">
    <div class="btn-group image-gallery-list">
      <a class="btn btn-default">全部图片</a>
      <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
        <span class="caret"></span> <span class="sr-only"></span>
      </button>
      <ul class="dropdown-menu" role="menu">
        <li><a href="javascript:void(0)" id="create-gallery-btn" data-toggle="modal" data-target="#newGallery">新建相册</a></li>
        <c:if test="${not empty galleries}">
          <li class="divider"></li>
        </c:if>
        <c:forEach items="${galleries}" var="gal">
          <li><a href="${pageContext.request.contextPath}/gallery/img/${gal.id}">${gal.title}</a></li>
        </c:forEach>
      </ul>
    </div>

  </div>

  <div class="image-list-table">
    <c:forEach items="${imgList}" var="img">
      <div class="image-list-table-single">
        <div class="image-list-table-single-img">


          <a href="${pageContext.request.contextPath}/img/m/${img.generatedCode}"><img class="image-list-table-show"
            src="<spring:message code="redirectPath"></spring:message><c:if test="${not empty img.smallSquareName}">${img.smallSquareName}</c:if><c:if test="${empty img.smallSquareName}">${img.redirectCode}</c:if>"></a>
        </div>
        <div class="image-list-table-single-control">
          标签: <span class="tag-span label label-default" tag-id="${img.tag.id}"> <c:if test="${img.tag eq null}">无</c:if>
            <c:if test="${img.tag ne null}">
            ${img.tag.name}
          </c:if>
          </span>
          <c:if test="${img.storageBucket.type ne 'weibo'}">
            <a type="button" class="btn-danger image-list-delete-btn btn btn-xs"
              href="${pageContext.request.contextPath}/img/delete?id=${img.id}">删除</a>
          </c:if>
        </div>
      </div>
    </c:forEach>
  </div>


  <div class="pages">
    <ul class="pagination">

      <li <c:if test="${not hasLast}">class="disabled"</c:if>><a
        href="<c:if test="${requestScope.fileName eq null}"></c:if><c:if test="${not(requestScope.fileName eq null)}">search?fileName=${requestScope.fileName}&tag=${param.tag}&page=</c:if><c:if test="${hasLast}">${currentPage-1}</c:if><c:if test="${not hasLast}">#</c:if>">&laquo;</a></li>

      <c:forEach begin="1" end="${pageCount}" varStatus="status">
        <c:choose>
          <c:when
            test="${status.index==0||status.index==1||status.index==pageCount||status.index==pageCount-1||status.index==currentPage||status.index==currentPage-1||status.index==currentPage-2||status.index==currentPage-3||status.index==currentPage+1||status.index==currentPage+2||status.index==currentPage+3}">

            <li <c:if test="${currentPage eq status.index}">class="active"</c:if>><a
              href="<c:if test="${requestScope.fileName eq null}"></c:if><c:if test="${not(requestScope.fileName eq null)}">search?fileName=${requestScope.fileName}&tag=${param.tag}&page=</c:if><c:if test="${currentPage eq status.index}">#</c:if><c:if test="${not (currentPage eq status.index)}">${status.index}</c:if>">${status.index}</a></li>

          </c:when>
          <c:otherwise>

          </c:otherwise>
        </c:choose>

      </c:forEach>
      <li <c:if test="${not hasNext}">class="disabled"</c:if>><a
        href="<c:if test="${requestScope.fileName eq null}">list/</c:if><c:if test="${not(requestScope.fileName eq null)}">search?fileName=${requestScope.fileName}&tag=${param.tag}&page=</c:if><c:if test="${hasNext}">${currentPage+1}</c:if><c:if test="${not hasLast}">#</c:if>">&raquo;</a></li>
    </ul>
  </div>




</div>
<c:if test="${(requestScope.totalCount eq 0) and not(requestScope.fileName eq null)}">
  <div class="container">
    <p class="bg-info  col-md-8">未搜索出结果</p>
  </div>
</c:if>



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
        <form class="form-horizontal" id="add-gallery-form" action="${pageContext.request.contextPath}/gallery/add"
          method="post" role="form">
          <div class="form-group">
            <label for="title" class="col-sm-2 control-label">相册名</label>
            <div class="col-sm-10">
              <input type="text" name="title" class="form-control" id="title" placeholder="相册名(可不填)">
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


<jsp:include page="../bottom.jsp"></jsp:include>
