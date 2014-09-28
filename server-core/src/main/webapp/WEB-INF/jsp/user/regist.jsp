<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:directive.include file="../taglib.jsp" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户注册</title>


<jsp:include page="../static.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/static/js/regist.js"></script>
<script src="${pageContext.request.contextPath}/static/js/md5.js"></script>

<style type="text/css">
.register-form {
    width: 70%;
    margin: auto;
    margin-top: 60px;
    padding: 3px 60px 60px 20px;
    border-width: 1px;
    border-color: rgb(221, 221, 221);
    border-radius: 4 4 4 4;
    box-shadow: none;
    border-style: solid;
}

a#regist-weibo, a#regist-qq {
    color: white;
}

a#regist-weibo:visited, a#regist-qq:visited {
    color: white;
}
</style>
<script type="text/javascript">
    $(document).ready(function() {
        $("#user-token").attr("disabled", "disabled");
        $("#sendEmail").click(function(e) {
            if (validateEmail()) {
                $("#sendEmail").val("邮件发送中");
                $("#sendEmail").attr("disabled", "disabled");
                $.post("${rootPath}/mail/send", {
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

        $("#regist-form").submit(function(e) {
            var var1 = md5password();
            var var2 = $("#token-status").val() == "true";
            if (!(var1 && var2)) {
                e.preventDefault();
            }
        });

        $("#user-token").on("input", function(e) {
            if (e.target.value.length == 32) {
                validateTokenTrue();
            }
        });

    });

    function validateTokenTrue() {
        $.post("${rootPath}/mail/validate", {
            token : $("#user-token").val()
        }, function(e) {
            if (e == "true") {
                $("#token-status").val("true");
            }
        }, "text");
    }

    function refreshTime(time) {
        if (time > 0) {
            $("#sendEmail").val("等待" + time / 1000 + "秒");
            time = time - 1000;
            var t = setTimeout("refreshTime(" + time + ")", 1000);
        } else {
            $("#sendEmail").removeAttr("disabled");
            $("#sendEmail").val("发送验证码");
        }
    }
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
            <div class="col-sm-offset-4 col-sm-6">
              <button id="regist-submit" type="submit" class="btn btn-primary">注册</button>
            </div>
          </div>

          <div class="form-group">
            <label for="password_re" class="col-sm-4 control-label">社交账号</label>
            <div class="col-sm-offset-4 col-sm-6">
              <a id="regist-weibo" class="btn btn-primary"
                href="${pageContext.request.contextPath}/social/weibo/redirect">微博登陆</a> <a id="regist-qq"
                class="btn btn-primary" href="">QQ登陆</a>
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
