function md5password() {
	var password1 = document.getElementById("user.password").value;
	var md5pw = hex_md5(password1);
	$("#password_md5").attr("value", md5pw);
	return true;
}