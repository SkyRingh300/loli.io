<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<!DOCTYPE html>
<head>
<title>查看已上传文件-萝莉图床</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="../static.jsp"></jsp:include>
<script type="text/javascript">
    var page = 1;
    var gid = 0;
    var totalPage;
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
            var title = $("#title").val();
            var description = $("#description").val();
            $.post("${pageContext.request.contextPath}/gallery/edit/addWithJsonResponse", {
                title : title,
                description : description
            }, function(result) {

                if (result && result.id) {
                    $('#newGallery').modal('hide')
                    alert("创建成功");
                    reloadGalleryList(function() {
                        $("a[gid=" + result.id + "]").click();
                    });
                }
            });

        });

        $(".img-upload-btn").click(function() {
            var gid = getGid();
            $("#drop").show();
            $("#upload-batch-url").hide();
            $("#fileList").html("");
            $(".url-list").hide();
            if (gid == 0) {
                $("#upload-to-label").text("未选择相册");
            } else {
                var text = $(".dropdown-gallery-toggle").prev().text();
                $("#upload-to-label").show();
                $("#upload-to-label").text("上传到相册:" + text);
                $("#gid-input").val(gid);
            }

        });

        $("#upload-new-gallery").click(function() {
            $("#uploadModal").modal("hide");

        });

        $("#upload-continue").click(function() {
            $(".url-list").hide();
            $("#drop").show();
            $("#upload-batch-url").hide();
            $("#fileList").html("");
            $(this).hide();
        });

        rebindGalleryClick();
        reloadImages(0, 1);
        loadPage(gid);
    });

    function resetUploadForm() {
        $("#drop").show();
        $("#fileList").html("");
        $(".url-list").hide();
        $("#upload-batch-url").hide();
    }

    // 重新加载下拉列表中的gallery
    function reloadGalleryList(fun, param) {
        $
            .post(
                "${pageContext.request.contextPath}/gallery/fetch/jsonList",
                function(result) {
                    var fobj = $(".dropdown-divider").parent();
                    $(".dropdown-gallery").remove();
                    for (i = 0; i < result.length; i++) {
                        var obj = $("<li class='dropdown-gallery' gid='"+result[i].id+"'><a href='javascript:void(0)' class='dropdown-gallery-a'></a></li>");
                        obj.find("a").attr("gid", result[i].id);
                        obj.find("a").text(result[i].title);
                        fobj.append(obj);
                    }

                    rebindGalleryClick();
                    if (fun && !param) {
                        fun();
                    }
                    if (fun && param) {
                        fun(param);
                    }
                });
    }

    function rebindGalleryClick() {
        $(".dropdown-gallery").click(function() {
            var gid = $(this).find("a").attr("gid");
            changeMainGallery($("a[gid=" + gid + "]").parent().get(0));
            //reloadGalleryList();
            //loadImageList(gid, page);
        });

        $(".dropdown-all").click(function() {
            if ($(".dropdown-default").attr("gid")) {
                var move = $(".dropdown-default");
                var gid = move.attr("gid");
                move.remove();
                $("li[gid=" + gid + "]").append(move);
                move.removeAttr("class");
                $("li[gid=" + gid + "]").show();
                move.attr("href", "javascript:void(0)");
            }
            if ($(this).parent().attr("class") == "dropdown-all-li") {

                $(this).parent().hide();
                $(".dropdown-gallery-toggle").before($(this));
                $(this).attr("class", "btn btn-default dropdown-all");
            }
            gid = 0;
            page = 1;
            reloadImages(0, 1);
            loadPage(gid);
        });
    }

    function getGid() {
        if ($(".dropdown-gallery-toggle").prev().attr("gid")) {
            return parseInt($(".dropdown-gallery-toggle").prev().attr("gid"));
        } else {
            return 0;
        }
    }

    function changeMainGallery(obj) {
        obj = $(obj).find("a");
        $(obj).parent().hide();
        if ($(".dropdown-default").attr("gid")) {
            var move = $(".dropdown-default");
            var gid = move.attr("gid");
            move.remove();
            $("li[gid=" + gid + "]").append(move);
            move.removeAttr("class");
            move.attr("href", "javascript:void(0)");
        } else {
        }

        gid = $(obj).attr("gid");
        page = 1;
        reloadImages(gid, page);
        loadPage(gid);
        $("li[gid=" + gid + "]").show();
        $(".dropdown-gallery-toggle").before($(obj));
        $(obj).attr("class", "btn btn-default dropdown-default");
        $(obj).removeAttr("href");
        $(".dropdown-all").removeClass("btn btn-default");
        $(".dropdown-all").attr("href", "javascript:void(0)");
        $(".dropdown-all-li").append($(".dropdown-all"));
        $(".dropdown-all-li").show();
    }

    function loadPage(gid) {
        $.post("${pageContext.request.contextPath}/img/pageCount", {
            gid : gid
        }, function(result) {
            $(".page-ul").html("");
            if (parseInt(result) > 0) {
                var count = parseInt(result);
                for (i = 0; i < count; i++) {
                    var obj = $('<li><a>' + (i + 1) + '</a></li>');
                    var a = obj.find("a");
                    if (page == i + 1) {
                        obj.attr("class", "active");
                    }
                    a.attr("href", "javascript:void(0)");
                    a.click(function() {
                        reloadImages(gid, parseInt($(this).text()));
                        page = parseInt($(this).text());
                        loadPage(gid);
                    });
                    $(".page-ul").append(obj);
                }
            } else {
            }
        });

        //setPage(total, current);
    }

    function reloadImages(galleryId, currentPage) {
        $
            .post(
                "${pageContext.request.contextPath}/img/jsonList",
                {
                    gid : galleryId,
                    page : currentPage
                },
                function(result) {
                    var redirectPage = "<spring:message code='redirectPath'/>";
                    $(".image-list-table").html("");
                    for (i = 0; i < result.length; i++) {
                        var img = result[i];

                        var obj = $('<div class="image-list-table-single"><div class="image-list-table-single-img"><a><img class="image-list-table-show"></a></div><div class="image-list-table-single-control"><a type="button" class="btn-danger image-list-delete-btn btn btn-xs">删除</a></div></div>');
                        if (img.smallName) {
                            obj.find("img").attr("src", redirectPage + img.smallSquareName);
                        } else {
                            obj.find("img").attr("src", redirectPage + img.generatedName);
                        }
                        $(".image-list-table").append(obj);
                    }
                });
    }

    // url get
    function getPaths() {
        var result = new Array();
        $(".path").each(function(i, e) {
            var h = $(this).text();
            if (h.length >= 0) {
                result.push(h);
            }
        });

        return result;
    }

    function getCode(prefix, suffix, array) {
        var html = "";
        for (var i = 0; i < array.length; i++) {
            var line = prefix;
            line += array[i];
            line += suffix;
            html += line;
        }
        return html;
    }
    $(document).ready(function() {
        $("#url-btn").click(function() {
            $("#result-area").html("");
            var result = getPaths();
            var html = getCode("", "\n", result);
            $("#result-area").html(html);
        });
        $("#html-btn").click(function() {
            $("#result-area").html("");
            var result = getPaths();
            var html = getCode("<img src='", "'>\n", result);
            $("#result-area").html(html);
        });
        $("#img-btn").click(function() {
            $("#result-area").html("");
            var result = getPaths();
            var html = getCode("[img]", "[/img]\n", result);
            $("#result-area").html(html);
        });

        $('#html').on('click', function() {
            $("#result-area").html("");
            var result = getPaths();
            var html = getCode("", "\n", result);
            $("#result-area").html(html);
            $('.modal .btn-group label').eq(0).click();
        });

        $("#upload-batch-url").click(function() {
            $(".url-list").show();
            $("#result-area").html("");
            $("#url-btn").click();
        });
    });
</script>
<style>
.search-form {
    padding-left: 0px !important;
}

.dropdown-default {
    width: 160px;
    text-align: left;
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
    margin-top: 40px;
    overflow: hidden;
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

.dropdown-all-li {
    display: none;
}

.image-gallery-list>.dropdown-all {
    width: 160px;
    text-align: left;
}

/* upload form*/
.name {
    margin-left: 0px !important;
    width: 40%;
    overflow: hidden;
    white-space: nowrap;
}

.working {
    list-style: none;
}

#upload-continue {
    display: none;
}

.path {
    color: rgb(79, 79, 79);
}

.url-list {
    display: none;
    margin-top: 2px !important;
}

.url-list>textarea {
    margin-bottom: 5px;
}

#upload-batch-url {
    display: none;
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
      <a class="btn btn-default dropdown-all">全部图片</a>
      <button type="button" class="btn btn-default dropdown-toggle dropdown-gallery-toggle" data-toggle="dropdown">
        <span class="caret"></span> <span class="sr-only"></span>
      </button>
      <ul class="dropdown-menu" role="menu">
        <li><a href="javascript:void(0)" id="create-gallery-btn" data-toggle="modal" data-target="#newGallery">新建相册</a></li>
        <c:if test="${not empty galleries}">
          <li class="dropdown-all-li"></li>
          <li class="divider dropdown-divider"></li>
        </c:if>
        <c:forEach items="${galleries}" var="gal">
          <li class="dropdown-gallery" gid="${gal.id}"><a href="javascript:void(0)" gid="${gal.id}">${gal.title}</a></li>
        </c:forEach>
      </ul>

    </div>
    <a class="btn btn-default img-upload-btn" data-target="#uploadModal" data-toggle="modal">上传图片</a>

  </div>

  <div class="image-list-table"></div>

  <div class="pages">
    <ul class="pagination page-ul">
    </ul>
  </div>
</div>
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

<!-- 新建相册模态框 -->
<div class="modal" id="uploadModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title">上传图片</h4>
      </div>
      <div class="modal-body">

        <form id="list-upload" method="post" action="${pageContext.request.contextPath}/api/upload"
          enctype="multipart/form-data">
          <div id="drop">
            <label id="upload-to-label">未选择相册</label>&nbsp;&nbsp;<a href="javascript:void(0)" data-toggle="modal"
              data-target="#newGallery" class="btn btn-primary btn-xs" id="upload-new-gallery">新建相册</a>
            <h3>拖动图片到这里或者</h3>
            <a class="btn btn-primary img-select-btn">选择图片</a>&nbsp; <input type="file" name="image" multiple /> <input
              id="gid-input" type="hidden" name="gid">
          </div>
          <ul id="fileList">
          </ul>
          <input type="hidden" id="redirectPath" value="<spring:message code="redirectPath"></spring:message>">

        </form>


        <div class="url-list">
          <div class="btn-group" data-toggle="buttons">
            <label class="btn btn-primary" id="url-btn"> <input type="radio" name="options">URL
            </label> <label class="btn btn-primary" id="html-btn"> <input type="radio" name="options">HTML
            </label> <label class="btn btn-primary" id="img-btn"> <input type="radio" name="options">[img]
            </label>
          </div>
          <textarea class="form-control col-md-6" rows="8" id="result-area">
        </textarea>

        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="upload-batch-url">获取链接</button>
        <button type="button" class="btn btn-primary" id="upload-continue">继续上传</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
      <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
  </div>
  <!-- /.modal -->
</div>
<script src="${pageContext.request.contextPath}/static/ext/uploader/jquery.knob.min.js"></script>
<script src="${pageContext.request.contextPath}/static/ext/uploader/jquery.ui.widget.min.js"></script>
<script src="${pageContext.request.contextPath}/static/ext/uploader/jquery.iframe-transport.min.js"></script>
<script src="${pageContext.request.contextPath}/static/ext/uploader/jquery.fileupload.min.js"></script>
<script src="${pageContext.request.contextPath}/static/ext/uploader/img.list.js"></script>
<jsp:include page="../bottom.jsp"></jsp:include>
