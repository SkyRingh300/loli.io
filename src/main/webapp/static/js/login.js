
function validateEmail() {
	var email = $("#user-email").val();
	var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if (reg.test(email)) {
		$("#email-error").html("");
		return true;
	} else {
		$("#email-error").html("邮箱格式不正确");
		return false;
	}
}

function validatePassword() {
	var password1 = document.getElementById("user-password").value;
	if (password1.length < 1) {
		$("#password_error").html("请输入密码");
		return false;
	}else{
		$("#password_error").html("");
		return true;
	}
}

function md5password() {
	var password1 = document.getElementById("user-password").value;
	if (validateEmail() & validatePassword()) {
		var md5pw = hex_md5(password1);
		$("#password_md5").attr("value", md5pw);
		return true;
	} else {
		return false;
	}
}
