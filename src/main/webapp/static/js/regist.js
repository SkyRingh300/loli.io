function md5password(input){
	var form = input.form;
	var password1 = form['user.password'].value;
	alert(password1);
	var password2 = form['password_re'].value;
	alert(password2);
	if(password1 == password2){
		form.submit();
	}
}