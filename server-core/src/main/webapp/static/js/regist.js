function validateEmail() {
    var email = $("#user-email").val();
    var reg = /^.+@.+$/;
    if (reg.test(email)) {
        $("#email-error").html("");
        return true;
    } else {
        $("#email-error").html("邮箱格式不正确");
        return false;
    }
}

function validateToken() {
    var token = $("#user-token").val();
    if (token == null || token == "" || token.length != 32) {
        $("#token-error").html("请输入邮箱验证码");
        return false;
    } else {
        return true;
    }
}

function validatePassword() {
    var password1 = document.getElementById("user-password").value;
    var password2 = document.getElementById("password_re").value;

    if (password1.length < 6) {
        $("#password_error").html("密码不能少于六位");
        return false;
    } else {
        $("#password_error").html("");
        if (password1 != password2) {
            $("#password_re_error").html("两次密码输入不一致");
            return false;
        } else {
            $("#password_re_error").html("");
            return true;
        }
    }

}

function md5password() {
    var password1 = document.getElementById("user-password").value;
    if (validateEmail() & validatePassword() & validateToken()) {
        var md5pw = hex_md5(password1);
        $("#password_md5").attr("value", md5pw);
        $("#password_re_md5").attr("value", md5pw);
        return true;
    } else {
        return false;
    }
}
