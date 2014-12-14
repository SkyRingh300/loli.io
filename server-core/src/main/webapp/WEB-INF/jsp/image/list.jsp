<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<!DOCTYPE html>
<head>
<title>我的图片-萝莉图床</title>
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
                    $('#newGallery').modal('hide');
                    $('#title').html("");
                    $('#description').html("");
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
        var gid = window.location.hash ? parseInt(window.location.hash.substring(1)) : 0;
        if (gid != 0) {
            $(".dropdown-gallery[gid=" + gid + "]").click();
        } else {
            $(".dropdown-all").click();
        }
        //reloadImages(gid, 1);
        //loadPage(gid);

        // 给全选、反选等绑定事件
        $(".img-select-all-btn").click(function() {
            $(".image-list-table-single").addClass("img-div-select");
            $(".image-list-select-btn").text("取消");
            countImgSelected();
        });
        $(".img-select-reverse-btn").click(function() {
            $(".image-list-table-single").each(function() {
                if ($(this).attr("class").indexOf("img-div-select") >= 0) {
                    $(this).removeClass("img-div-select")
                    $(this).find(".image-list-select-btn").text("选择");
                } else {
                    $(this).addClass("img-div-select");
                    $(this).find(".image-list-select-btn").text("取消");
                }
            });
            countImgSelected();
        });

        $(".img-select-cancel-btn").click(function() {
            $(".image-list-table-single").removeClass("img-div-select");
            $(".image-list-select-btn").text("选择");
            countImgSelected();
        });

        $(".img-select-delete-btn").click(function() {
            if (!confirm("删除后无法恢复，确认删除吗？")) {
                return;
            }

            var ids = "";
            $(".img-div-select").each(function() {
                var imgid = $(this).attr("img-id");
                ids += (imgid + ",");
            });

            batchDelete(ids);
            countImgSelected();
        });

        $(".img-select-batch-link-btn").click(function() {
            $("#linksModal").modal("show");
        });

        $(".dropdown-gal-list-div").find("li").click(function() {

            var gid = $(this).attr("gid");
            var ids = getSelectImageIds();
            $.post("${pageContext.request.contextPath}/img/batchMove", {
                ids : ids,
                gid : gid
            }, function(result) {
                if (result.status == "success") {
                    alert(result.message);
                } else {
                    alert("发生错误:" + result.message);
                }
                reloadImages(getGid(), page);
                loadPage(getGid());
                resetSelectStatus();

            });
        });

    });

    function resetUploadForm() {
        $("#drop").show();
        $("#fileList").html("");
        $(".url-list").hide();
        $("#upload-batch-url").hide();
    }

    function getSelectImageIds() {
        var ids = "";
        $(".img-div-select").each(function() {
            var imgid = $(this).attr("img-id");
            ids += (imgid + ",");
        });
        return ids;
    }

    // 重新加载下拉列表中的gallery
    function reloadGalleryList(fun, param) {
        $
            .post(
                "${pageContext.request.contextPath}/gallery/fetch/jsonList",
                function(result) {
                    var fobj = $(".dropdown-divider").parent();
                    $(".dropdown-gallery").remove();

                    var moveToList = $(".dropdown-gal-list");
                    moveToList.html("");

                    for (i = 0; i < result.length; i++) {
                        var obj = $("<li class='dropdown-gallery' gid='"+result[i].id+"'><a href='javascript:void(0)' class='dropdown-gallery-a'></a></li>");
                        obj.find("a").attr("gid", result[i].id);
                        obj.find("a").text(result[i].title);

                        var obj2 = $("<li gid='"+result[i].id+"'><a href='javascript:void(0)'>" + result[i].title
                            + "</a></li>");
                        moveToList.append(obj2);
                        fobj.append(obj);
                    }
                    rebindGalleryMoveClick();

                    rebindGalleryClick();

                    if (fun && !param) {
                        fun();
                    }
                    if (fun && param) {
                        fun(param);
                    }
                });
    }

    function rebindGalleryMoveClick() {
        $(".dropdown-gal-list-div").find("li").click(function() {

            var gid = $(this).attr("gid");
            var ids = getSelectImageIds();
            $.post("${pageContext.request.contextPath}/img/batchMove", {
                ids : ids,
                gid : gid
            }, function(result) {
                if (result.status == "success") {
                    alert(result.message);
                } else {
                    alert("发生错误:" + result.message);
                }
                reloadImages(getGid(), page);
                loadPage(getGid());
                resetSelectStatus();

            });
        });
    }
    // 绑定相册下拉列表的click事件
    function rebindGalleryClick() {
        $(".dropdown-gallery").click(function() {
            var gid = $(this).find("a").attr("gid");
            changeMainGallery($(".dropdown-gallery[gid=" + gid + "]").get(0));
            window.location.hash = "#" + gid;
            //reloadGalleryList();
            //loadImageList(gid, page);
        });

        $(".dropdown-all").click(function() {
            if ($(".dropdown-default").attr("gid")) {
                var move = $(".dropdown-default");
                var gid = move.attr("gid");
                move.remove();
                $(".dropdown-gallery[gid=" + gid + "]").append(move);
                move.removeAttr("class");
                $(".dropdown-gallery[gid=" + gid + "]").show();
                move.attr("href", "javascript:void(0)");
                window.location.hash = "#" + 0;
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

    // 获取当前的相册id
    function getGid() {
        if ($(".dropdown-gallery-toggle").prev().attr("gid")) {
            return parseInt($(".dropdown-gallery-toggle").prev().attr("gid"));
        } else {
            return 0;
        }
    }

    // 批量删除图片
    function batchDelete(ids) {
        $.post("${pageContext.request.contextPath}/img/batchDelete", {
            ids : ids
        }, function(result) {
            if (result.status == "success") {
                alert("删除成功");
            } else {
                alert("发生错误:" + result.message);
            }
            reloadImages(getGid(), page);
            loadPage(getGid());
            resetSelectStatus();
        });
    }

    // 改变相册
    function changeMainGallery(obj) {
        obj = $(obj).find("a");
        $(obj).parent().hide();
        if ($(".dropdown-default").attr("gid")) {
            var move = $(".dropdown-default");
            var gid = move.attr("gid");
            move.remove();
            $(".dropdown-gallery[gid=" + gid + "]").append(move);
            move.removeAttr("class");
            move.attr("href", "javascript:void(0)");
        } else {
        }

        gid = $(obj).attr("gid");
        page = 1;
        reloadImages(gid, page);
        loadPage(gid);
        $(".dropdown-gallery[gid=" + gid + "]").show();
        $(".dropdown-gallery-toggle").before($(obj));
        $(obj).attr("class", "btn btn-default dropdown-default");
        $(obj).removeAttr("href");
        $(".dropdown-all").removeClass("btn btn-default");
        $(".dropdown-all").attr("href", "javascript:void(0)");
        $(".dropdown-all-li").append($(".dropdown-all"));
        $(".dropdown-all-li").show();
    }

    // 加载分页
    function loadPage(gid) {
        $.post("${pageContext.request.contextPath}/img/pageCount", {
            gid : gid
        }, function(result) {
            resetSelectStatus();
            var pages = $(".page-ul");
            $(".page-ul").html("");
            if (parseInt(result) > 0) {
                var count = parseInt(result);
                var lastIsDisabled = false;
                for (i = 0; i < count; i++) {
                    if (count > 14) {
                        if ((i > 0 && i < page - 4) || (i<count-1 && i>page + 2)) {
                            if (!lastIsDisabled) {
                                var obj = $('<li class="disabled"><a>...</a></li>');
                                pages.append(obj);
                                lastIsDisabled = true;
                            }
                            continue;
                        }
                    }

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
                    pages.append(obj);
                    lastIsDisabled = false;
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
                        var galTitle = img.gallery ? img.gallery.title : "无";
                        var galId = img.gallery ? img.gallery.id : 0;
                        var obj = $('<div img-link="'
                            + redirectPage
                            + result[i].redirectCode
                            + '" img-id="'
                            + result[i].id
                            + '" class="image-list-table-single"><div class="image-list-table-single-img"><a href="${pageContext.request.contextPath}/img/m/'
                            + result[i].generatedCode
                            + '" target="_blank"><img class="image-list-table-show"></a></div><div class="img-name">'
                            + result[i].originName
                            + '</div><div class="image-list-table-single-control"><a class="btn-primary image-list-select-btn btn btn-xs">选择</a><a href="javascript:void(0)" title="'
                            + galTitle
                            + '" gid="'
                            + galId
                            + '" class="btn btn-xs btn-default image-list-gal-span">'
                            + galTitle
                            + '</a><a class="btn-danger image-list-delete-btn btn btn-xs">删除</a></div></div>');
                        if (img.smallName) {
                            obj.find("img").attr("src", redirectPage + img.smallSquareName);
                        } else {
                            obj.find("img").attr("src", redirectPage + img.generatedName);
                        }
                        $(".image-list-table").append(obj);
                    }

                    // 给选择按钮绑定事件
                    $(".image-list-select-btn").click(function() {
                        var nowtext = $(this).text();
                        if (nowtext == "选择") {
                            $(this).text("取消");
                            imgSelect($(this).parent().parent());
                        } else {
                            imgUnSelect($(this).parent().parent());
                            $(this).text("选择");
                        }
                    });

                    $(".image-list-delete-btn").click(function() {
                        if (!confirm("删除后无法恢复，确认删除吗？")) {
                            return;
                        }
                        var imgid = $(this).parent().parent().attr("img-id");
                        batchDelete(imgid);
                    });

                    $(".image-list-gal-span").click(function() {
                        var gid = $(this).attr("gid");
                        $(".dropdown-gallery[gid=" + gid + "]").find("a").click();
                    });

                    //modal位置稍微往下挪一点
                    $('.modal').on('shown.bs.modal', function() {
                        $(this).css("top", "20%");
                    });
                });
    }
    // img click select
    //选中该div
    function imgSelect(obj) {
        obj.addClass("img-div-select");
        countImgSelected();
    }

    function resetSelectStatus() {
        $(".img-select-label").text("");
        $(".img-select-batch-link-btn").hide();
        $(".img-select-delete-btn").hide();
        $(".dropdown-gal-list-div").hide();

    }

    // 解除该div的选中状态
    function imgUnSelect(obj) {
        obj.removeClass("img-div-select");
        countImgSelected();
    }

    // 计算现在有多少个图片是被选中的，并显示出来，如果没有图片被选中，那么就不显示
    function countImgSelected() {
        var count = $(".img-div-select").size();
        if (count == 0) {
            $(".img-select-label").text("");
            $(".img-select-batch-link-btn").hide();
            $(".img-select-delete-btn").hide();
            $(".dropdown-gal-list-div").hide();
        } else {
            $(".img-select-batch-link-btn").show();
            $(".img-select-delete-btn").show();
            $(".dropdown-gal-list-div").css("display", "inline-block");
            $(".img-select-label").text(count + "张图片被选中");
        }
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
        $("#batch-url-btn").click(function() {
            $("#batch-result-area").html("");
            var result = getPaths();
            var html = getCode("", "\n", result);
            $("#batch-result-area").html(html);
        });
        $("#batch-html-btn").click(function() {
            $("#batch-result-area").html("");
            var result = getPaths();
            var html = getCode("<img src='", "'>\n", result);
            $("#batch-result-area").html(html);
        });
        $("#batch-img-btn").click(function() {
            $("#batch-result-area").html("");
            var result = getPaths();
            var html = getCode("[img]", "[/img]\n", result);
            $("#batch-result-area").html(html);
        });

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

        $(".img-select-batch-link-btn").click(function() {
            $("#fileList").html("");
            $("#batch-result-area").html("")
            $(".img-div-select").each(function() {
                var link = $(this).attr("img-link");
                console.log(link);
                if (link) {
                    var obj = $("<p class='path'>" + link + "</p>");
                    $("#fileList").append(obj);
                }
            });

            $("#batch-result-area").html("");
            $("#batch-url-btn").click();

        });
    });
</script>
<style>
.img-name {
    white-space: nowrap;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    overflow: hidden;
    z-index: 3;
    float: left;
    margin-top: -19px;
    margin-left: 0px;
    position: relative;
    background-color: rgba(255, 255, 255, 0.71);
    width: 150px;
}

.search-form {
    padding-left: 0px !important;
}

#drop {
    background-color: white !important;
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

.control-info {
    margin-top: 50px;
}

.image-list-table {
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

.img-select-batch-link-btn, .img-select-delete-btn, .dropdown-gal-list-div {
    display: none;
}

.img-div-select {
    background-color: rgba(159, 159, 159, 1);
}

.image-list-controllers {
    
}

.controllers-left {
    float: left;
}

.controllers-right {
    float: left;
    margin-left: 20px;
}

.dropdown-share-div {
    display: none;
}

.image-list-gal-span {
    margin-left: 0.5em;
    max-width: 5em;
    white-space: nowrap;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    overflow: hidden;
}
</style>
</head>
<jsp:include page="../top.jsp"></jsp:include>
<div id="imgList" class="container">
  <div class="tip">
    <c:if test="${param.message!=null}">
      <div class="alert alert-success info">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        ${param.message}
      </div>
    </c:if>
  </div>

  <div class="image-list-controllers">
    <div class="controllers-left">
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
    <div class="controllers-right">
      <button type="button" class="img-select-all-btn btn btn-default">全选</button>
      <button type="button" class="img-select-reverse-btn btn btn-default">反选</button>
      <button type="button" class="img-select-cancel-btn btn btn-default">取消</button>
      <button type="button" class="btn btn-default img-select-batch-link-btn">生成链接</button>
      <div class="btn-group dropdown-share-div">
        <button type="button" data-toggle="dropdown" class="btn btn-default dropdown-toggle">
          分享到<span class="caret"></span>
        </button>
        <ul class="dropdown-menu dropdown-share-list" role="menu">
          <li><a href="javascript:void(0)">新浪微博</a></li>
          <li><a href="javascript:void(0)">腾讯微博</a></li>
          <li><a href="javascript:void(0)">人人网</a></li>
          <li><a href="javascript:void(0)">QQ空间</a></li>
        </ul>
      </div>


      <div class="btn-group dropdown-gal-list-div">
        <button type="button" data-toggle="dropdown" class="btn btn-default dropdown-toggle">
          移动到<span class="caret"></span>
        </button>
        <ul class="dropdown-menu dropdown-gal-list" role="menu">
          <c:forEach items="${galleries}" var="gal">
            <li gid="${gal.id}"><a href="javascript:void(0)" gid="${gal.id}">${gal.title}</a></li>
          </c:forEach>
        </ul>
      </div>
      <button type="button" class="img-select-delete-btn btn btn-danger">删除</button>

      <!-- 用于显示有多少个图片被选中的 -->
      <label class="img-select-label"> </label>


    </div>

  </div>
  <div class="control-info"></div>

  <div class="image-list-table"></div>
  <div class="pages">
    <ul class="pagination page-ul">
    </ul>
  </div>
</div>
<jsp:include page="../gallery/add.jsp"></jsp:include>

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


<div class="modal" id="linksModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title">生成链接</h4>
      </div>
      <div class="modal-body">
        <div class="batch-links">
          <div class="btn-group" data-toggle="buttons">
            <label class="btn btn-primary" id="batch-url-btn"> <input type="radio" name="options">URL
            </label> <label class="btn btn-primary" id="batch-html-btn"> <input type="radio" name="options">HTML
            </label> <label class="btn btn-primary" id="batch-img-btn"> <input type="radio" name="options">[img]
            </label>
          </div>
          <textarea class="form-control col-md-6" rows="8" id="batch-result-area"></textarea>

        </div>
      </div>

      <div class="modal-footer">
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
