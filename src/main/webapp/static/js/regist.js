function md5password() {
	var password1 = document.getElementById("user.password").value;
	var password2 = document.getElementById("password_re").value;
	if (password1 == password2) {
		var md5pw = hex_md5(password1);
		$("#password_md5").attr("value", md5pw);
		$("#password_re_md5").attr("value", md5pw);
		return true;
	} else {
		return false;
	}
}