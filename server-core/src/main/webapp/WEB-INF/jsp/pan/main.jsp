<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
.left {
    width: 200px;
    background-color: rgb(240, 240, 240);
}

#top {
    margin-bottom: 0px !important;
    height: auto%;
    height: 51px;
}

.navbar-nav.navbar-right:last-child {
    margin-right: 0px !important;
}

.main {
    width: 100%;
    margin: 0 auto;
    text-align: left;
    height: 100%;
    padding-top: 51px;
    position: absolute;
    top: 0;
}

.file-label, .file-buttons {
    display: inline;
}

.file-label {
    
}

.file-label, .file-checkbox {
    float: left;
}

.file-buttons {
    float: right;
    display: none;
}

.left-menu {
    padding-top: 10px;
    height: 100%;
}

.mouse-on-tr .file-buttons {
    display: flex !important;
}

.file-buttons button {
    margin-right: 5px;
}

.left-menu>li.active>a, .left-menu>li.active>a:hover, .left-menu>li.active>a:focus {
    color: rgb(0, 0, 0);
    background-color: rgb(200, 200, 200);
}

.left-menu a {
    border-bottom-left-radius: 0px !important;
    border-bottom-right-radius: 0px !important;
    border-top-left-radius: 0px !important;
    border-top-right-radius: 0px !important;
}

.left-menu>li>a {
    color: rgb(0, 0, 0);
}

.left-menu>li>a:hover, .left-menu>li>a:focus {
    color: rgb(0, 0, 0);
    background-color: rgb(220, 220, 220);
}

.controller {
    height: 40px;
    background-color: rgb(240, 240, 240);
    padding: 5;
    box-shadow: 0 0 3px rgba(0, 0, 0, 0.3);
}

.left {
    float: left;
    height: 100%;
    box-shadow: 0 0 3px rgba(0, 0, 0, 0.3);
}

.right {
    width: 100%;
    height: 100%;
    padding-left: 200px;
    background-color: rgba(255, 255, 255, 1);
}

.icon-button {
    cursor: pointer;
}

.filelist>table>tbody>tr>td:first-child {
    width: 70%;
}

.filelist {
    overflow: auto;
    padding-top: 40px;
    margin-top: -40px;
    height: 100%;
    margin-top: -40px;
}

.file-list-table tr {
    margin-left: 10px;
}

#drop-area input {
    display: none;
}

.upload-form {
    float: left;
}

.file-list-table {
    margin-bottom: 0px !important;
}

.file-list-table .tr-selected {
    background-color: rgb(245, 245, 245);
}

.icon {
    margin-left: 10px;
    margin-right: 5px;
}

.file-list-table tr a, a:visited {
    color: rgb(95, 95, 95);
}

.guide-link {
    color: rgb(95, 95, 95);
}

.guide-title {
    margin-left: 10px;
}

.list-title {
    margin-left: 10px;
    float: left;
}

.file-checkbox-all {
    float: left;
}

.list-title .selected {
    display: none;
}

#uploader {
    display: none;
    position: absolute;
    bottom: 0;
    right: 0;
    width: 600px;
    height: 400px;
    left: auto;
    top: auto;
    background: none repeat scroll 0% 0% #FFF;
    box-shadow: 0px 0px 20px -2px rgba(0, 0, 0, 0.5);
    border: 0px none;
    top: auto;
}

#uploader-header {
    display: inline;
    height: auto;
    overflow: auto;
    zoom: 1;
    z-index: 100;
}

#uploader-header-title {
    padding: 5px;
    margin: 5px;
    display: block;
    z-index: 100;
    left: 0px;
}

#uploader-header-control {
    padding: 5px;
    margin: 5px;
    display: block;
    right: 0px;
    left: auto;
    position: absolute;
    top: 0;
    z-index: 100;
}

#uploader-content {
    height: 100%;
    top: 0px;
    position: absolute;
    margin-top: 0px;
    padding-top: 35px;
    width: 100%;
    z-index: 99;
}

#uploader-content table {
    overflow: scroll;
}

#uploader-header-control span {
    margin-left: 10px;
}

.upload-filelist-name div {
    width: 240px;
    overflow: hidden;
}

.upload-filelist-size div {
    width: 90px;
}

.upload-filelist-folder div {
    width: 80px;
}

.upload-filelist-progress div {
    width: 55px;
}

.upload-filelist-button div {
    width: 75px;
}

.uploader-min #uploader-content {
    display: none !important;
}

.uploader-min {
    height: auto !important;
}

.file-buttons a {
    background-color: rgb(221, 221, 221);
    margin-right: 5px;
}
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
        $("#uploader-header-max").hide();
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
                        data.context.find("td").eq(3).text(progress);
                        data.context.find("td").eq(4).find("a").eq(0).hide();
                    });
                    var jqXHR = data.submit();

                },

                progress : function(e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    if (progress == 100) {
                        data.context.find("td").eq(4).html("请等待");
                    }
                    progress += "%";
                    data.context.find("td").eq(3).text(progress);
                },

                fail : function(e, data) {
                    data.context.find("td").eq(4).html("上传失败");
                },
                done : function(e, data) {
                    var folderId = $("#folderId").val();
                    var progress = "完成";
                    data.context.find("td").eq(4).html(progress);
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

        } else {
            $(".list-title .non-selected").hide();
            $(".list-title .selected .selected-count").text(selectedCount);
            $(".list-title .selected").show();
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