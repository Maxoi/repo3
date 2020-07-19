<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>泉州银行订餐系统-数据统计</title>
<link rel="stylesheet" type="text/css" href="css/style_login.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/vector.js"></script>
  <style>
  table {
	  padding:25px;
	  color:#fff;
	  margin:0 auto;
  }
  .brief{
  	color:#000;
  	font-size:2em;
  }
  table{
  	z-index:9999;
  }
  </style>
</head>
<body>
<div id="container">
	<div id="output">
		<div class="containerL">
<table border="1">
<tr><th width="20%">员工</th><th colspan="31" width="80%">信息技术部-软件开发中心 已订情况(${now_year}年${now_month}月) 共计<span style="color:red">${allStaffCount}</span>份,<span style="color:red">${allStaffCountMoney}</span>元</th></tr>
<c:forEach items="${staffs}" var="answeres"  varStatus="ans" >
	<tr><c:set var="sum" value="0" />
	<td style="width:120px;">${answeres.key}</td>
	<c:forEach items="${answeres.value}" var="answeres2"  varStatus="ans2" >    
	<c:choose>
		<c:when test="${!fn:contains(answeres2,'X')}"><td style="width:40px;">${answeres2}日</c:when>
		<c:otherwise>
			<td style="width:40px;">${fn:substring(answeres2, 0, fn:indexOf(answeres2, 'X'))}日X${fn:substring(answeres2, fn:indexOf(answeres2, 'X')+1, fn:length(answeres2))}
		</c:otherwise> 
	</c:choose>
	<c:if test="${fn:contains(answeres2,'X')}"><c:set var="sum" value="${sum+fn:substring(answeres2, fn:indexOf(answeres2, 'X')+1, fn:length(answeres2))-1}" /></c:if></td>
	</c:forEach>
	<td style="width:50px;">总计:${fn:length(answeres.value)+sum}</td>
	<c:if test="${answeres.key == maxStaffName}"><tr><td colspan="100%" style="text-align:center;color:green">----------------------分割线----------------</td></tr></c:if>
	</tr>
</c:forEach>
<a class="brief" href="list?method=brief">简单版</a>>>>
<a class="brief" href="order">勾饭</a>>>>
<a class="brief" href="list?method=xls">导出xls(10s更新一次数据)</a>
</table>
</div>
</div>
</div>
<script type="text/javascript">
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
</body>
</html>