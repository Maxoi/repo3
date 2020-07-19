<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>泉州银行订餐系统-密码重置</title>
	<link rel="stylesheet" href="./css/normalize.min.css">
	<link rel="stylesheet" href="./css/style.css">
	<script src="./js/prefixfree.min.js"></script>
	<script src="./js/html5.js"></script>
</head>
<body>
  <div class="login">
	<h1>qzccbank-密码重置</h1>
	<h1 class="error_info" style="color:red"><% if(request.getAttribute("error_info") != null) out.print(request.getAttribute("error_info")); %></h1>
    <form method="post" action="index?method=reset">
		<span id="label_info_u" style="display:none">密码</span><input type="password" name="password" id="password" placeholder="密码" required="required" />
		<span id="label_info_p" style="display:none">确认密码</span><input type="password" name="re_password" id="re_password" placeholder="确认密码" required="required" />
        <button type="submit" class="btn btn-primary btn-block btn-large">确认修改</button>
    </form>
</div>
    <script src="js/index.js"></script>
    <script>
    (function() {
	//当不支持placeholder时
	if (!('placeholder' in document.createElement('input'))) {
        var inputRePassword = document.getElementById("password");
        var inputPassword = document.getElementById("re_password");
        inputRePassword.style.color = '#092756';
        inputPassword.style.color = '#092756';
	}
})();
    </script>
</body>
</html>
