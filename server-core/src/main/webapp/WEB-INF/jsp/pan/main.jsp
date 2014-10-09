<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>

</style>
<script>
    var pageCount = 0;
    var fileCount = 0;
    var selectedCount = 0;
    $(document)
        .ready(
            function() {
                loadFolder(0);
                $(".btn-add-folder")
                    .click(
                        function() {
                            if ($(".add-folder-input").size() > 0) {
                                $(".add-folder-input").focus();
                                return;
                            }
                            $(".filelist tbody tr:first")
                                .before(
                                    "<tr><td><form><input type='text' class='add-folder-input'>"
                                        + "&nbsp;<span class='glyphicon glyphicon-ok icon-button' onclick='saveFolder(this)'></span>&nbsp;"
                                        + "<span class='glyphicon glyphicon-remove icon-button' onclick='$(this).parent().parent().parent().remove()'></span>"
                                        + "</form></td><td></td><td></td></tr>");
                            $(".add-folder-input").focus();
                        });

                bindUploadFile();
                bindFileListScroll();
                bindUploaderClick();
            });
    var canList = true;
    function bindFileListScroll() {
        var $win = $(".filelist");
        $win.scroll(function() {
            console.log(canList
                && $win.height() + $win.scrollTop() > $(".file-list-table").height() + $(".controller").height());
            if (canList
                && $win.height() + $win.scrollTop() > $(".file-list-table").height() + $(".controller").height()) {
                canList = false;
                // TODO, ajax，如果返回有值，canList = true，否则=false
                var folderId = $("#folderId").val();
                console.log(pageCount);
                loadFolder(folderId, pageCount);
            }
        });
    }

    function formatFileSize(bytes) {
        if (typeof bytes !== 'number') {
            return '';
        }

        if (bytes >= 1000000000) {
            return (bytes / 1000000000).toFixed(1) + ' GB';
        }

        if (bytes >= 1000000) {
            return (bytes / 1000000).toFixed(1) + ' MB';
        }

        return (bytes / 1000).toFixed(1) + ' KB';
    }

    function bindUploaderClick() {
        $(".uploader-header-min").click(function() {
            //$("#uploader-content").hide();
            $("#uploader").addClass("uploader-min");
            $(this).hide();
            $(".uploader-header-max").show();
        });

        $(".uploader-header-max").click(function() {
            //$("#uploader-content").show();
            $("#uploader").removeClass("uploader-min");
            $(this).hide();
            $(".uploader-header-min").show();
        });

        $(".uploader-header-close").click(function() {
            $("#uploader").hide();
        });
        $(".uploader-header-max").show();
    }

    function bindUploadFile() {
        $(".btn-upload-file").unbind("click");
        $(".btn-upload-file").click(function() {
            $(".upload-form").find('input').click();
        });
        $('#drop-area').fileupload(
            {
                add : function(e, data) {
                    $("#uploader").show();
                    var filename = data.files[0].name;
                    var filesize = formatFileSize(data.files[0].size);
                    var progress = "0%";
                    var folderName = $("#full-foldername").text();
                    var tpl = $('<tr><div><td class="upload-filelist-name"><div>' + filename
                        + '</div></td><td class="upload-filelist-size"><div>' + filesize
                        + '<div></td><td class="upload-filelist-folder"><div>' + folderName
                        + '</div></td><td class="upload-filelist-progress"><div>' + progress
                        + '</div></td><td class="upload-filelist-button"><div><a href="#">取消</a></div></td></tr>');
                    data.context = tpl.appendTo($("#uploader-content table tbody"));
                    data.context.find("td").eq(4).find("a").eq(0).click(function() {
                        jqXHR.abort();
                        var progress = "已取消";
                        data.context.find("td").eq(4).find("a").eq(0).hide();
                    });
                    var jqXHR = data.submit();

                },

                progress : function(e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    if (progress == 100) {
                        data.context.find("td").eq(4).html("<div>请等待</div>");
                    }
                    progress += "%";
                    data.context.find("td").eq(3).html("<div>" + progress + "</div>");
                },

                fail : function(e, data) {
                    data.context.find("td").eq(4).html("<div>上传失败</div>");
                },
                done : function(e, data) {
                    var folderId = $("#folderId").val();
                    var progress = "完成";
                    data.context.find("td").eq(4).html("<div>" + progress + "</div>");
                    loadFolder(folderId);
                }

            });
    }

    function saveFolder(obj) {
        var parentId = $(".filelist table").attr("tid");
        var name = $(obj).prev("input").val();
        if (parentId) {
            $.post("${pageContext.request.contextPath}/pan/file/add", {
                pid : parentId,
                name : name,
                "parent.id" : $("folderId").val()
            }, function(result) {
                $(".filelist").html(result);
            });
        }
    }

    function loadFolder(id, start) {
        if (start) {
            var type = "folder";
            if (fileCount > 0) {
                type = "file";
                start = fileCount;
            }
            $.post("${pageContext.request.contextPath}/pan/file/list?pid=" + id + "&start=" + start + "&type=" + type,
                function(result) {
                    $(".filelist tbody").append(result);
                });
        } else {
            $(".filelist").load("${pageContext.request.contextPath}/pan/file/list?pid=" + id);
        }

    }

    function updateSelected() {
        selectedCount = $(".tr-selected").size();
        if (selectedCount == 0) {
            $(".list-title .non-selected").show();
            $(".list-title .selected").hide();
            $(".list-control").hide();
        } else {
            $(".list-title .non-selected").hide();
            $(".list-title .selected .selected-count").text(selectedCount);
            $(".list-title .selected").show();
            $(".list-control").show();
        }

    }
</script>
<jsp:directive.include file="../taglib.jsp" />
<div class="main">
  <div class="left">
    <ul class="nav nav-pills nav-stacked left-menu" role="tablist">
      <li class="active"><a href="#">所有文件</a></li>
      <li><a href="#">图片</a></li>
    </ul>
  </div>
  <div class="right">

    <div class="controller">
      <button type="button" class="btn btn-primary btn-sm btn-upload-file">
        <span class="glyphicon glyphicon-circle-arrow-up"></span>上传文件
      </button>
      <button type="button" class="btn btn-sm btn-add-folder">
        <span class="glyphicon glyphicon-plus-sign"></span> 新建文件夹
      </button>
      <button type="button" class="btn btn-sm">
        <span class="glyphicon glyphicon-share"></span>分享
      </button>
    </div>
    <div class="filelist"></div>
  </div>
</div>