$("#register").click(function(event) {
	
	var user = $("#username").val();
	var pass = $("#password").val();
	var email = $("#email").val();
	
	
	var registerForm = {
			'username':user,
			'password':password,
			'email':email
			
	};
	
	console.log(registerForm);
	
	var xhr = new XMLHttpRequest();	
	xhr.open("POST", "http://localhost:8083/auth/register", true);
	xhr.setRequestHeader("Content-Type", "application/json;charsfet=utf-8")
	xhr.responseType = 'json';
	
	xhr.send(JSON.stringify(registerFrom));
	window.location = 'login/login.html';
	
}