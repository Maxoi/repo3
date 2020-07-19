<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset='utf-8'>
  <meta http-equiv="refresh" content="10">
  <link rel='stylesheet' href='./css/style.css' />  
  <link rel='stylesheet' href='./css/cal.css' />
  <script src="./js/html5.js"></script>
  <title>泉州银行订餐系统-勾饭</title>
  <style>
	  .c_count{
	  	color:#fff;
	  	margin:0 auto;
	  	
	  }
	  .otherMonth{
	  	color:#666;
	  }
	  .c_count a{
	  	color:yellow;
	  	font-size:20px;
	  }
	  .currentMonth:hover{
	  	background:#fff;
	  	/*cursor:pointer;*/
	  	cursor:url('./image/hb.ico'),auto;
	  }
	  .currentDay:hover{
	  	background:#fff;
	  	/*cursor:pointer;*/
	  	cursor:url('./image/hb.ico'),auto;
	  }
	  .fee{
	  	color:#fff;
	  	font-weight:bold;
	  }
	  .fee_a{
	  	color:orange;
	  	text-decoration:none;
	  }
	  .log_list{
	  	color:#fff;
	  }
	  .red{
	  	color:red;
	  }
	  .green{
	  	color:green;
	  }
	  .orange{
	  	color:orange;
	  }
	  .pink{
	  	color:pink;
	  }
	  .yellow{
	  	color:yellow;
	  }
  </style>
</head>
<body>
  <div class="infom"><span class="fee"><% if("0".equals(request.getAttribute("isfee").toString())) out.print("上月存在未结清状态,请及时<a href=\"zf.html\" target=\"_blank\" class=\"fee_a\" >缴费</a>,上月餐费:["+request.getAttribute("fee")+"]元"); %></span>订饭结束后请及时刷新以保证最新状态!!!!!!!(当日已订:<%=request.getAttribute("dateCount") %>)</div>
  <div class='calendar' id='calendar'><span class="c_count">尊敬的<span class="red"><%=request.getAttribute("loginUser") %></span>,您好 您当月已订<span style="color:red;font-size:35px;"><%=request.getAttribute("orderCount") %></span>次,合计<span style="color:red;font-size:35px;"><%=request.getAttribute("orderCountMoney") %></span>元 已订日期请看下面日期列表 <a href="list">当月统计</a>>>><a href="index?method=logout">注销</a>>>><a href="zf.html" target="_blank" >缴费</a></span></div>
  <div id="myDiv" class="infom"><%=request.getAttribute("AllData") %></div>
	<div class="log_list">
	<c:forEach items="${TransLog}" var="answeres"  varStatus="ans" >
		${answeres.staff_name} 想把 ${answeres.trans_time} 的盒饭转让给你,目前状态:
		<c:choose>
			<c:when test="${answeres.trans_status == 0 }">
			<span>失效</span>
			</c:when>
			<c:when test="${answeres.trans_status == 1 }">
			<span>接受转让</span>
			</c:when>
			<c:when test="${answeres.trans_status == 2 }">
			<span>转让中,<a href='#' class='green'>接受</a>|<a href='#' class='red'>拒绝</a></span>
			</c:when>
			<c:when test="${answeres.trans_status == 3 }">
			<span>拒绝转让</span>
			</c:when>
		<c:otherwise>
		</c:otherwise>
		</c:choose>
		<span class="yellow">${answeres.op_time}</span>
		<br />
	</c:forEach>
	</div>
  <script type='text/javascript' src='./js/cal.js'></script>
  <script type='text/javascript'>
	<% if("0".equals(request.getAttribute("isfee").toString()))  out.print("alert('上月存在未结清状态,您需要交餐费:'+'"+request.getAttribute("fee")+"元')");%>
	
	var h = <%=request.getAttribute("nowRealHour") %>;
	var STime = new Date();
	if(Math.abs(parseInt(STime.getHours(),10) - h) > 1){
		alert('对不起,您的系统时间与服务器时间相差甚远,请修改!改完请重起浏览器~');
		 window.location.href="index?method=logout";
	}
  </script>
</body>
</html>