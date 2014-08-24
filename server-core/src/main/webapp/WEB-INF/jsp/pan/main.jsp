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

.left-menu {
    padding-top: 10px;
    height: 100%;
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

#drop-area input {
    display: none;
}

.upload-form {
    float: left;
}
</style>
<script>
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
                                .after(
                                    "<tr><td><form><input type='text' class='add-folder-input'>"
                                        + "&nbsp;<span class='glyphicon glyphicon-ok icon-button' onclick='saveFolder(this)'></span>&nbsp;"
                                        + "<span class='glyphicon glyphicon-remove icon-button' onclick='$(this).parent().parent().parent().remove()'></span>"
                                        + "</form></td><td></td><td></td></tr>");
                            $(".add-folder-input").focus();
                        });

                bindUploadFile();

            });

    function bindUploadFile() {
        $(".btn-upload-file").unbind("click");
        $(".btn-upload-file").click(function() {
            $(".upload-form").find('input').click();
        });
        $('#drop-area').fileupload({
            add : function(e, data) {
                alert("");
                var jqXHR = data.submit();
            },

            progress : function(e, data) {

            },

            fail : function(e, data) {

            },
            done : function(e, data) {

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

    function loadFolder(id) {
        $(".filelist").load("${pageContext.request.contextPath}/pan/file/list?pid=" + id);
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