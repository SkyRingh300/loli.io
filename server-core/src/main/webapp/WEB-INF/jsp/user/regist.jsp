<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:directive.include file="../taglib.jsp" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户注册-萝莉图床</title>


<jsp:include page="../static.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/static/js/regist.js"></script>
<script src="${pageContext.request.contextPath}/static/js/md5.js"></script>
<script type="text/javascript">
    $(document).ready(function(e) {
        $("#sendEmail").click(function(e) {
            if (validateEmail()) {
                $("#sendEmail").val("邮件发送中");
                $("#sendEmail").attr("disabled", "disabled");
                $.post("${pageContext.request.contextPath}/mail/send", {
                    email : $("#user-email").val()
                }, function(e) {
                    if (e == "true") {
                        $("#user-token").removeAttr("disabled");
                        refreshTime(60000);
                    } else {
                        alert("邮件发送错误");
                    }
                }, "text");
            }
        });
    });
</script>

<style type="text/css">
</style>
<script type="text/javascript">
    
</script>
</head>
<body>
  <jsp:include page="../top.jsp"></jsp:include>

  <div id="main">
    <div class="container">
      <div class="register-form">
        <h2>用户注册</h2>
        <form class="form-horizontal" id="regist-form" action="regist" method="POST">
          <div class="form-group">
            <label for="user-email" class="col-sm-4 control-label">邮箱</label>
            <div class="col-sm-4">
              <input type="email" name="email" class="form-control" id="user-email" placeholder="邮箱">
            </div>
            <span class="label label-danger" id="email-error">${message["email"]} </span>
          </div>
          <div class="form-group">
            <label for="验证码" class="col-sm-4 control-label">验证码</label>

            <div class="col-sm-4">
              <input type="text" name="token" class="form-control" id="user-token" placeholder="验证码">
            </div>
            <input type="button" id="sendEmail" class="btn" value="发送验证码"> <input type="hidden"
              id="token-status"> <span class="label label-danger" id="token-error">${message["token"]} </span>
          </div>
          <div class="form-group">
            <label for="user-password" class="col-sm-4 control-label">密码</label>
            <div class="col-sm-4">
              <input type="password" class="form-control" name="password_ori" id="user-password" placeholder="密码">
            </div>
            <span class="label label-danger" id="password_error"></span>

          </div>
          <div class="form-group">
            <label for="password_re" class="col-sm-4 control-label">重复输入密码</label>
            <div class="col-sm-4">
              <input type="password" class="form-control" name="password_re_ori" id="password_re" placeholder="重复输入密码">
            </div>
            <span class="label label-danger" id="password_re_error"></span>
          </div>
          <div class="form-group">
            <label for="password_re" class="col-sm-4 control-label">使用条款</label>
            <div class="checkbox col-sm-4">
              <label><input type="checkbox" id="terms-checkbox">同意<a
                href="${pageContext.request.contextPath}/terms" target="_blank">使用条款</a></label>
            </div>
            <span class="label label-danger" id="terms-error"></span>
          </div>
          <div class="form-group">
            <div class="col-sm-offset-4 col-sm-6">
              <button id="regist-submit" type="submit" class="btn btn-primary">注册</button>
            </div>
          </div>

          <div class="form-group">
            <label class="col-sm-4 control-label">社交账号</label>
            <div class="col-sm-offset-4 col-sm-6">
              <a href="${pageContext.request.contextPath}/social/weibo/redirect"><img
                src="${pageContext.request.contextPath}/static/img/weibo_btn.png"></a> <a
                href="${pageContext.request.contextPath}/social/github/redirect"><img
                src="${pageContext.request.contextPath}/static/img/github-btn.png"></a>

            </div>
          </div>
          <input type="hidden" id="password_md5" name="password"> <input type="hidden" id="password_re_md5"
            name="password_re">
        </form>
      </div>
    </div>
  </div>
  <jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>
