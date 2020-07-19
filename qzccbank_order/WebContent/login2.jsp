<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>订餐系统传新版本</title>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/vector.js"></script>
<link rel="stylesheet" type="text/css" href="css/style_login.css">
<link rel="stylesheet" type="text/css" href="css/style_lay.css">

</head>
<body>
<div id="container">
<div id="modal-container">
  <div class="modal-background">
    <div class="modal">
      <h2>明日份</h2>
      <p>
   
      </p>
    </div>
  </div>
</div>
	<div id="output">
		<div class="containerT">
			<h1 class="title">订餐系统传新版本</h1>
			<h1 class="tj"><a href="list">当月统计</a> <a href="<%=request.getAttribute("YM") %>.xls" >上月报表</a> <a href="<%=request.getAttribute("CYM") %>.xls" style="<%=request.getAttribute("IsLastDay") %>">当月报表</a> <% if(request.getAttribute("LastDayInfo") != null) out.print(request.getAttribute("LastDayInfo")); %><% if(request.getAttribute("dateCount_in") != null || request.getAttribute("dateCount_out") != null) out.print("<p class='today_count'>今天已订: "+request.getAttribute("dateCount")+" (行内 "+request.getAttribute("dateCount_in")+",外包 "+request.getAttribute("dateCount_out")+")<br />下日已定: "+request.getAttribute("dateCount_tom")+" (行内 "+request.getAttribute("nextdateCount_in")+",外包 "+request.getAttribute("nextdateCount_out")+")</p>"); %></h1>
			<form class="form" id="entry_form" action="index?method=login" method="post">
				<input type="text" placeholder="工号" id="entry_name" name="username" required >
				<input type="password" placeholder="密码" id="entry_password" name="password" required>
				<button type="submit" id="entry_btn">登录</button><button type="button" onclick="forbid()" class="lock">锁止</button>
				<div id="prompt" class="prompt"><% if(request.getAttribute("error_info") != null) out.print(request.getAttribute("error_info")); %></div>
			</form>
			<div style="margin-top:-20px;"><img src="image/ysf2.png" height="200px" /></div>
		</div>
	</div>
</div>

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
		function forbid() {
	    var msg = "您真的确定要冻结所有用户吗？一经点击所有人无法订餐\n\n请确认！";
	    if (confirm(msg) == true) {
	        window.location.href = 'index?method=lock';
	        //return true; 
	    } else {
	        return false;
	    }
		}
    $(function(){
        Victor("container", "output");   //登录背景函数
        $("#entry_name").focus();
        $(document).keydown(function(event){
            if(event.keyCode==13){
                $("#entry_btn").click();
            }
        });
    });
</script>
<div style="text-align:center;">
</div>
</body>
</html>