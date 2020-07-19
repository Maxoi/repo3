/* 

I built this login form to block the front end of most of my freelance wordpress projects during the development stage. 

This is just the HTML / CSS of it but it uses wordpress's login system. 

Nice and Simple

*/

(function() {
	//当不支持placeholder时
	if (!('placeholder' in document.createElement('input'))) {
		var label_info_u = document.getElementById("label_info_u");
		var label_info_p = document.getElementById("label_info_p");
		label_info_u.style.color='#fff';
		label_info_p.style.color='#fff';
		label_info_u.style.display='block';
		label_info_p.style.display='block';
        var inputUsername = document.getElementById("username");
        var inputPassword = document.getElementById("password");
        inputUsername.style.color = '#092756';
        inputPassword.style.color = '#092756';
	}
})();