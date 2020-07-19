<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>泉州银行订餐系统</title>
	<link rel="stylesheet" href="./css/normalize.min.css">
	<link rel="stylesheet" href="./css/style.css">
	<script src="./js/prefixfree.min.js"></script>
	<style>
	.input{
		width: 100%; 
		margin-bottom: 10px; 
		background: rgba(0,0,0,0.3);
		border: none;
		outline: none;
		padding: 10px;
		font-size: 13px;
		color: #fff;
		text-shadow: 1px 1px 1px rgba(0,0,0,0.3);
		border: 1px solid rgba(0,0,0,0.3);
		border-radius: 4px;
		box-shadow: inset 0 -5px 45px rgba(100,100,100,0.2), 0 1px 1px rgba(255,255,255,0.2);
		-webkit-transition: box-shadow .5s ease;
		-moz-transition: box-shadow .5s ease;
		-o-transition: box-shadow .5s ease;
		-ms-transition: box-shadow .5s ease;
		transition: box-shadow .5s ease;
	}
	</style>


</head>
<body>
  <div class="login">
	<h1>订餐转让</h1>
	<h1 class="error_info" style="color:red"><% if(request.getAttribute("error_info") != null) out.print(request.getAttribute("error_info")); %></h1>
    <form method="post" action="index?method=trans">
    	<select class="input" name="staff_id" id="sel" >
    	<c:forEach items="${StaffIdName}" var="answeres"  varStatus="ans" >
    		<option value="${answeres.staff_id}">${answeres.staff_id}-${answeres.staff_name}</option>
    	</c:forEach>
    	</select>
    	
        <button type="submit" class="btn btn-primary btn-block btn-large">确认</button>
    </form>
</div>
	<script>
	(function() {

	//当不支持placeholder时
	if (!('placeholder' in document.createElement('input'))) {
		var sel = document.getElementById("sel");
		var fee_type = document.getElementById("fee_type");
		sel.style.color="#092756";
		fee_type.style.color="#092756";
	}
	
	})();
	
	
	</script>
    <script src="js/index.js"></script>
</body>
</html>
