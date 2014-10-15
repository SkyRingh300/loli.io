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
        $(".delete").click(function(e) {
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
                $(td).parent().append($(html));
                $(td).next().focus();
            });
        });

    });
</script>
<style>
.search-form {
    padding-left: 0px !important;
}

.tag-input {
    padding: 0 !important;
    height: 22px !important;
}

.bg-info {
    padding: 15px;
}

.info {
    width: 100% !important;
    margin-bottom: 15px !important;
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

  </div>
  <c:if test="${not(requestScope.totalCount eq 0)}">
    <table class="table table-hover">
      <thead>
        <tr>
          <th width="40%">文件名</th>
          <th width="20%">上传时间</th>
          <th width="10%">分类</th>
          <th width="25%">链接</th>
          <th width="10%">操作</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${imgList}" var="img">
          <tr id="${img.id}">
            <td>${img.originName}</td>
            <td><fmt:formatDate value="${img.date}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            <td class="tag-td"><span class="tag-span label label-default" tag-id="${img.tag.id}"> <c:if
                  test="${img.tag eq null}">无</c:if> <c:if test="${img.tag ne null}">
						${img.tag.name}
						</c:if>
            </span></td>

            <td><c:if test="${img.redirectCode ne null}">
                <c:if test="${img.storageBucket.type eq 'weibo'}">
                  <a href="${img.path}" target="_blank">${img.path}</a>
                </c:if>
                <c:if test="${img.storageBucket.type ne 'weibo'}">
                  <a href="<spring:message code="redirectPath"></spring:message>${img.redirectCode}" target="_blank"><spring:message
                      code="redirectPath"></spring:message>${img.redirectCode}</a>
                </c:if>

              </c:if> <c:if test="${img.redirectCode eq null}">
                <a href="${img.path}" target="_blank">${img.path}</a>
              </c:if></td>
            <td><c:if test="${img.storageBucket.type ne 'weibo'}">
                <a type="button" class="btn-danger delete btn btn-xs"
                  href="${pageContext.request.contextPath}/img/delete?id=${img.id}">删除</a>
              </c:if></td>
          </tr>
        </c:forEach>

      </tbody>
    </table>
    <div class="pages">
      <ul class="pagination">

        <li <c:if test="${not hasLast}">class="disabled"</c:if>><a
          href="${pageContext.request.contextPath}/img/<c:if test="${requestScope.fileName eq null}">list/</c:if><c:if test="${not(requestScope.fileName eq null)}">search?fileName=${requestScope.fileName}&tag=${param.tag}&page=</c:if><c:if test="${hasLast}">${currentPage-1}</c:if><c:if test="${not hasLast}">#</c:if>">&laquo;</a></li>

        <c:forEach begin="1" end="${pageCount}" varStatus="status">
          <c:choose>
            <c:when
              test="${status.index==0||status.index==1||status.index==pageCount||status.index==pageCount-1||status.index==currentPage||status.index==currentPage-1||status.index==currentPage-2||status.index==currentPage-3||status.index==currentPage+1||status.index==currentPage+2||status.index==currentPage+3}">

              <li <c:if test="${currentPage eq status.index}">class="active"</c:if>><a
                href="${pageContext.request.contextPath}/img/<c:if test="${requestScope.fileName eq null}">list/</c:if><c:if test="${not(requestScope.fileName eq null)}">search?fileName=${requestScope.fileName}&tag=${param.tag}&page=</c:if><c:if test="${currentPage eq status.index}">#</c:if><c:if test="${not (currentPage eq status.index)}">${status.index}</c:if>">${status.index}</a></li>

            </c:when>
            <c:otherwise>

            </c:otherwise>
          </c:choose>

        </c:forEach>
        <li <c:if test="${not hasNext}">class="disabled"</c:if>><a
          href="${pageContext.request.contextPath}/img/<c:if test="${requestScope.fileName eq null}">list/</c:if><c:if test="${not(requestScope.fileName eq null)}">search?fileName=${requestScope.fileName}&tag=${param.tag}&page=</c:if><c:if test="${hasNext}">${currentPage+1}</c:if><c:if test="${not hasLast}">#</c:if>">&raquo;</a></li>
      </ul>
    </div>

  </c:if>



</div>
<c:if test="${(requestScope.totalCount eq 0) and not(requestScope.fileName eq null)}">
  <div class="container">
    <p class="bg-info  col-md-8">未搜索出结果</p>
  </div>
</c:if>
<jsp:include page="../bottom.jsp"></jsp:include>
