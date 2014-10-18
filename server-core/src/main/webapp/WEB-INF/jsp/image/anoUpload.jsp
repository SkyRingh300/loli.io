<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<!DOCTYPE html>
<style>
#upload ul li span {
    background: url('${pageContext.request.contextPath}/static/ext/uploader/icons.png') no-repeat !important;
}

#drop {
    border-image: url('${pageContext.request.contextPath}/static/ext/uploader/border-image.png') 25 repeat !important;
}
</style>


<jsp:include page="../top.jsp"></jsp:include>
<div class="container">
  <c:if test="${info!=null}">
    <div class="alert alert-success info">
      <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
      ${info}
    </div>
  </c:if>

  <form id="upload" method="post" action="${pageContext.request.contextPath}/api/upload" enctype="multipart/form-data">
    <div id="drop">
      <h3>拖动图片到这里或者</h3>
      <a class="btn">选择图片</a>&nbsp; <input type="file" name="image" multiple />
    </div>
    <button id="clear" type="button" class="btn btn-sm btn-primary">清空上传列表</button>
    &nbsp;
    <c:if test="${sessionScope.user ne null}">
      <button id="html" type="button" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#htmlSelect">获取链接</button>
      <button id="url" type="button" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#urlFetch">url下载</button>

      <c:if test="${not empty param.weibo}">
        <button type="button" id="weibo1" onclick="window.location.href='${pageContext.request.contextPath}/'"
          class="btn btn-sm btn-primary">原图床</button>
      </c:if>
      <button type="button" id="weibo2" onclick="window.location.href='${pageContext.request.contextPath}/?weibo=weibo'"
        class="btn btn-sm btn-primary">微博图床</button>
    </c:if>
    <c:if test="${not empty param.weibo}">
      <input type="hidden" id="weibo" name="type" value="${param.weibo}">
    </c:if>
    <ul id="fileList">
    </ul>
  </form>
  <div id="message">
    <ul>
      <li>所有外链均已启用CDN</li>
      <li>使用条款有更新，见页面底部链接</li>
    </ul>
    <c:if test="${not empty param.weibo}">
      <ul>
        <li>微博图床文件最大为5M</li>
        <li>微博图床：通过微博API接口上传，有上传频率限制：1小时内还可以上传<span class="label label-danger">${requestScope.limit.key}</span>张图片，24小时内还可以上传<span
          class="label  label-danger">${requestScope.limit.value}</span>张图片。当次数为0时，请使用原图床上传。
        </li>
        <li>如果微博图床总是上传失败，请使用原图床</li>
      </ul>
    </c:if>

  </div>
</div>

<div class="modal fade" id="htmlSelect">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title">获取代码</h4>
      </div>
      <div class="modal-body">
        <div class="btn-group" data-toggle="buttons">
          <label class="btn btn-primary" id="url-btn"> <input type="radio" name="options">URL
          </label> <label class="btn btn-primary" id="html-btn"> <input type="radio" name="options">HTML
          </label> <label class="btn btn-primary" id="img-btn"> <input type="radio" name="options">[img]
          </label>
        </div>
        <textarea class="form-control col-md-6" rows="8" id="result-area">
				</textarea>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>


<div class="modal fade" id="urlFetch">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title">请输入图片url(一行一个), 如果没反应，请耐心等待20秒</h4>
      </div>
      <div class="modal-body">

        <textarea class="form-control col-md-6" rows="8" id="upload-area"></textarea>
        <button type="button" id="fetch-confirm" class="btn btn-primary">确认</button>
        <textarea class="form-control col-md-6" rows="8" id="fetch-result-area"></textarea>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<script>
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

        var lock = true;
        $("#fetch-confirm").click(function() {
            var urls = new Array();
            var urlStr = document.getElementById("upload-area").value.replace(/^\s*[\r\n]/gm, "");
            var urlArray = urlStr.split("\n");
            for (i = 0; i < urlArray.length; i++) {
                if (urlArray[i].length < 3) {
                    break;
                }
                $.post("${pageContext.request.contextPath}/api/fetch", {
                    path : urlArray[i]
                }, function(result) {
                    if (result && result.error != "") {
                        alert(result.origin + "获取失败:" + result.error);
                    } else {
                        if (result && result.origin != "") {
                            while (!lock) {
                            }
                            var newValue = $("#upload-area").val();
                            newValue = newValue.replace(result.origin, "");
                            $("#upload-area").val(newValue);
                            lock = true;
                            $("#fetch-result-area").append($("#redirectPath").val() + result.redirect + "\n");
                        }
                    }
                }, "json");
            }

        });

    });
</script>

<script src="${pageContext.request.contextPath}/static/ext/uploader/jquery.knob.min.js"></script>
<script src="${pageContext.request.contextPath}/static/ext/uploader/jquery.ui.widget.min.js"></script>
<script src="${pageContext.request.contextPath}/static/ext/uploader/jquery.iframe-transport.min.js"></script>
<script src="${pageContext.request.contextPath}/static/ext/uploader/jquery.fileupload.min.js"></script>
<script src="${pageContext.request.contextPath}/static/ext/uploader/script.js"></script>
<c:if test="${empty param.weibo}">
  <input type="hidden" id="redirectPath" value="<spring:message code="redirectPath"></spring:message>">
</c:if>