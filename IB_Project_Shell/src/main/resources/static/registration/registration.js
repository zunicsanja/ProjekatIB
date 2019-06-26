$("#register").click(function(event) {
	
	var user = $("#reg_username").val();
	var pass = $("#reg_password").val();
	var last = $("#reg_lastfullname").val();
	var name = $("#reg_fullname").val();
	var email = $("#reg_email").val();
	
	
	var registerForm = {
			'firstname':name,
			'lastname':last,
			'username':user,
			'password':password,
			'email':email
			
	};
	
	console.log(registerForm);
	
	var xhr = new XMLHttpRequest();	
	xhr.open("POST", "http://localhost:8443/auth/register", true);
	xhr.setRequestHeader("Content-Type", "application/json;charsfet=utf-8")
	xhr.responseType = 'json';
	
	xhr.send(JSON.stringify(registerFrom));
	window.location = 'login.html';
	
}