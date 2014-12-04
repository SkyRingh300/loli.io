<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:directive.include file="../taglib.jsp" />

<head>
<title>修改密码-萝莉图床</title>
<link href="<spring:message code="staticPath"></spring:message>/style.css" rel="stylesheet" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="../static.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/static/js/md5.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/edit.js"></script>

<script>
    $(document).ready(function() {
        $("#edit-form").submit(function(e) {
            var var1 = md5password();
            if (!var1) {
                e.preventDefault();
            }
        });

        $("#edit-nickname-btn").click(function() {
            var obj = this;
            var t = $(this).text();
            if (t == "修改") {
                $(this).text("保存");
                var origin = $(this).prev("label").text();

                $(this).prev("label").remove();
                $(this).before("<input size='12' type='text' value='" + origin.trim() + "'>");
                $(this).prev("input").focus().select();
            } else {
                var nickName = $(this).prev().val();
                $.post("${pageContext.request.contextPath}/user/updateNickname", {
                    nickName : nickName
                }, function(result) {
                    if (result == "success") {
                        $(obj).text("修改");
                        $(obj).prev().remove();
                        $(obj).before("<label>" + nickName + "</label>");
                    }

                });
            }
        });
    });
</script>

<style>
.bg-info {
    padding: 15px;
}

.info-e {
    margin: 0 auto;
    width: 50%;
}

.edit-form {
    height: 380px;
}

.form-right-lable>label {
    padding-top: 7px;
}
</style>
</head>
<body>
  <jsp:include page="../top.jsp"></jsp:include>
  <div class="container">
    <div class="edit-form">
      <form class="form-horizontal" onsubmit="return false;" id="info-form" action="edit" method="POST">
        <fieldset>
          <legend>修改个人信息</legend>
          <div class="form-group">
            <label for="user-email" class="col-sm-4 control-label">昵称</label>
            <div class="col-sm-4 form-right-lable" id="edit-nickname-div">
              <label> <c:if test="${empty user.name}">
              昵称未设置
              </c:if> <c:if test="${not empty user.name}">
                  <c:out value="${user.name}"></c:out>
                </c:if>
              </label>
              <button type="button" id="edit-nickname-btn" class="btn btn-xs">修改</button>

            </div>
          </div>
        </fieldset>
      </form>
      <form class="form-horizontal" id="edit-form" action="edit" method="POST">
        <fieldset>
          <legend>修改密码</legend>

          <div class="form-group">
            <label for="user-email" class="col-sm-4 control-label">邮箱</label>
            <div class="col-sm-4">
              <span class="label label-default">${user.email}</span>
            </div>

          </div>

          <div class="form-group">
            <label for="user-password-old" class="col-sm-4 control-label">旧密码</label>
            <div class="col-sm-4">
              <input type="password" class="form-control" id="user-password-old" required placeholder="旧密码">
            </div>
            <span class="label label-danger" id="password_old_error"></span>

          </div>

          <div class="form-group">
            <label for="user-password" class="col-sm-4 control-label">新密码</label>
            <div class="col-sm-4">
              <input type="password" class="form-control" required id="user-password" placeholder="新密码">
            </div>
            <span class="label label-danger" id="password_error"></span>

          </div>
          <div class="form-group">
            <label for="password_re" class="col-sm-4 control-label">重复输入新密码</label>
            <div class="col-sm-4">
              <input type="password" class="form-control" required id="password_re" placeholder="重复输入新密码">
            </div>
            <span class="label label-danger" id="password_re_error"></span>
          </div>
          <div class="form-group">
            <div class="col-sm-offset-4 col-sm-6">
              <button id="edit-submit" type="submit" class="btn btn-primary">修改</button>
            </div>
          </div>
          <input type="hidden" id="password_md5" name="password"> <input type="hidden" id="password_re_md5"
            name="password_re"> <input type="hidden" id="password_old_md5" name="password_old">
        </fieldset>
      </form>
    </div>
    <c:if test="${not(message eq null) }">
      <div class="info-e">
        <div class="bg-info">${message }</div>
      </div>
    </c:if>


    <div class="social-div">

      <form class="form-horizontal" id="edit-form" action="edit" method="POST">
        <fieldset>
          <legend>绑定社交账号(绑定之后可以直接登陆)</legend>

          <div class="form-group">

            <label class="col-sm-4 control-label">新浪: </label>
            <div class="col-sm-4 form-right-lable">
              <c:if test="${empty weibo}">
                <a href="${pageContext.request.contextPath}/social/weibo/redirect"><img
                  src="${pageContext.request.contextPath}/static/img/weibo-btn.png"></a>
              </c:if>
              <c:if test="${not empty weibo}">
                <label> ${weibo.name}&nbsp;<a href="${pageContext.request.contextPath}/social/weibo/cancel">解除绑定
                </a></label>
              </c:if>
            </div>

          </div>
          <div class="form-group">
            <label class="col-sm-4 control-label">QQ: </label>
            <div class="col-sm-4 form-right-lable">
              <c:if test="${empty qq}">
                <a href="${pageContext.request.contextPath}/social/qq/redirect"><img
                  src="${pageContext.request.contextPath}/static/img/qq-btn.png"></a>
              </c:if>
              <c:if test="${not empty qq}">
                <label> ${qq.name}<!-- &nbsp;<a href="${pageContext.request.contextPath}/social/qq/cancel"></a> -->
                </label>
              </c:if>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-4 control-label">Github: </label>
            <div class="col-sm-4 form-right-lable">
              <c:if test="${empty github}">
                <a href="${pageContext.request.contextPath}/social/qq/redirect"><img
                  src="${pageContext.request.contextPath}/static/img/github-btn.png"></a>
              </c:if>
              <c:if test="${not empty github}">
                <label> ${github.name}<!-- &nbsp;<a href="${pageContext.request.contextPath}/social/qq/cancel"></a> -->
                </label>

              </c:if>
            </div>
          </div>

        </fieldset>

      </form>
    </div>

  </div>

  <jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>