function validatePassword() {
    var oldPassword = document.getElementById("user-password-old").value;
    if (oldPassword.length < 6) {
        $("#password_old_error").html("请输入正确的密码");
        return false;
    } else {
        $("#password_old_error").html("");
    }
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
    if (validatePassword()) {
        var md5pw = hex_md5(password1);
        var oldPassword = document.getElementById("user-password-old").value; 
        var oldMd5 = hex_md5(oldPassword);
        $("#password_md5").attr("value", md5pw);
        $("#password_re_md5").attr("value", md5pw);
        $("#password_old_md5").attr("value", oldMd5);
        return true;
    } else {
        return false;
    }
}