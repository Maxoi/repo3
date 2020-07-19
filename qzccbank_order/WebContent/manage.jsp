<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
	<h1>qzccbank-管理员入口</h1>
	<h1 class="error_info" style="color:red"><% if(request.getAttribute("error_info") != null) out.print(request.getAttribute("error_info")); %></h1>
    <form method="post" action="index?method=manage">
    	<select class="input" name="op_type" id="sel" onchange="TypeChange(this.value)" >
    		<option value="0">当日报餐统计</option>
    		<option value="1">重置密码</option>
    		<option value="2">新增员工</option>
    		<option value="3">冻结员工</option>
    		<option value="4">解冻员工</option>
    		<option value="5">欠费冻结消除</option>
    		<option value="6">手动导出上月报表</option>
    		<option value="7">手动导出当月报表</option>
    	</select>
    	<span id="label_info_u" style="display:none">工号</span><input type="text" name="staff_id" id="username" placeholder="工号" />
    	<span id="label_info_p" style="display:none">姓名</span><input type="text" name="staff_name" id="password" placeholder="姓名"  />
    	<select class="input" name="staff_type" id="staff_type" style="display:none" >
    		<option value="0">外包</option>
    		<option value="1" selected>行内</option>
    	</select>
    	<select class="input" name="fee_type" id="fee_type" style="display:none" >
    		<option value="1" selected>银行卡</option>
    		<option value="2">微信</option>
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
	
	function TypeChange(value){
		var fee_type = document.getElementById("fee_type");
		var staff_type = document.getElementById("staff_type");
		if(value == 5){
			fee_type.style.display='block';
		}else{
			fee_type.style.display='none';
		}
		if(value == 2){
			staff_type.style.display='block';
		}else{
			staff_type.style.display='none';
		}
		
	}
	
	</script>
    <script src="js/index.js"></script>
</body>
</html>
