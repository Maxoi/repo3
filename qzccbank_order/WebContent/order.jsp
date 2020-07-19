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
	  	color:#fff;
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
	  	padding:10px;
	  	border:1px solid #fff;
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
	  .intel_order {
		text-align:center;
		padding:10px;
	  }
	  .intel_order #onekey_open,#onekey_close{
	  	background:#000;
	  	border:1px solid #00FF33;
	  	cursor:pointer;
	  	color:#00FF33;
	  	font-size:15px;
	  	height:30px;
	  	width:120px;
	  }
	  #onekey_close{
	  	color:red;
	  }
	  .intel_order #onekey_open:hover{
	  	background:#00FF33;
	  	color:#000;
	  }
	  .intel_order #onekey_close:hover{
	  	background:red;
	  	color:#fff;
	  }
	  body{
	    overflow:auto;
	  }
	  h2.logtitle{
	  	padding:10px;
	  	color:#fff;
	  }
  </style>
</head>
<body>
  <div class="infom"><span class="fee"><% if("0".equals(request.getAttribute("isfee").toString())) out.print("上月存在未结清状态,请及时<a href=\"zf.html\" target=\"_blank\" class=\"fee_a\" >缴费</a>,上月餐费:["+request.getAttribute("fee")+"]元"); %></span>订饭或<span style="color:#fff">转让</span>结束后请及时刷新以保证最新状态!!!!!!!(当日已订:<%=request.getAttribute("dateCount") %>)</div>
  <div class='calendar' id='calendar'><span class="c_count">尊敬的<span class="red"><%=request.getAttribute("loginUser") %></span>,您好 您当月已订<span style="color:red;font-size:35px;"><%=request.getAttribute("orderCount") %></span>次,合计<span style="color:red;font-size:35px;"><%=request.getAttribute("orderCountMoney") %></span>元 已订日期请看下面日期列表 <a href="list">当月统计</a>>>><a href="index?method=logout">注销</a>>>><a href="zf.html" target="_blank" >缴费</a>>>><a href="index?method=trans" target="_blank" style="background:red;color:#fff">转让(仅限当天)</a></span></div>
  <div class="intel_order">
	  <button id="onekey_open" onclick="requestIntel('?method=intel_order&=' + Math.random(),1,null,0);">一键智能订餐</button>
	  <button id="onekey_close" onclick="requestIntel('?method=intel_order&=' + Math.random(),0,null,0);">一键智能解订</button>
  	  <p style="color:#fff;display:none;">为防止遗忘订餐,建议大家使用此功能,自动过滤周末,节假日暂未实现请手动勾除,该按钮只能操作明天到月底,当日请手工选择</p>
  	  <p style="color:orange;font-size:20px;" id="blink">使用转让功能请及时提醒受让方接受，否则第二天自动撤销，若有转让申请往下滚动!!!</p>
  </div>
  <div class="tranlog">
	<c:if test="${TransLog1_count>'0'}">
	  	<h2 class="logtitle">转让记录(${TransLog1_count})</h2>
	  	<div class="log_list">
			<c:forEach items="${TransLog1}" var="answeres"  varStatus="ans" >
				你想把 ${answeres.trans_time} 的盒饭转让给${answeres.staff_name},目前状态:
				<c:choose>
					<c:when test="${answeres.trans_status == 0 }">
					<span>失效</span>
					</c:when>
					<c:when test="${answeres.trans_status == 1 }">
					<span>接受转让</span>
					</c:when>
					<c:when test="${answeres.trans_status == 2 }">
					<span>转让中<a href='#' class='green' onclick="requestIntel('?method=rebak&=' + Math.random(),1,null,2);" >撤销</a></span>
					</c:when>
					<c:when test="${answeres.trans_status == 3 }">
					<span>拒绝受让</span>
					</c:when>
				<c:otherwise>
				</c:otherwise>
				</c:choose>
				<span class="yellow">${answeres.op_time}</span>
				<br />
			</c:forEach>
		</div>
  </c:if>
  </div>
  <div class="tranlog">
  	<c:if test="${TransLog2_count>'0'}">
  	<h2 class="logtitle">受让记录(${TransLog2_count})</h2>
  	<div class="log_list">
	<c:forEach items="${TransLog2}" var="answeres"  varStatus="ans" >
		${answeres.staff_name} 想把 ${answeres.trans_time} 的盒饭转让给你,目前状态:
		<c:choose>
			<c:when test="${answeres.trans_status == 0 }">
			<span>失效</span>
			</c:when>
			<c:when test="${answeres.trans_status == 1 }">
			<span>接受转让</span>
			</c:when>
			<c:when test="${answeres.trans_status == 2 }">
			<span>转让中,<a href='#' class='green' onclick="requestIntel('?method=receive&=' + Math.random(),1,null,1);" >接受</a>|<a href='#' class='red' onclick="requestIntel('?method=reject&=' + Math.random(),0,null,1);">拒绝</a></span>
			</c:when>
			<c:when test="${answeres.trans_status == 3 }">
			<span>拒绝受让</span>
			</c:when>
		<c:otherwise>
		</c:otherwise>
		</c:choose>
		<span class="yellow">${answeres.op_time}</span>
		<br />
	</c:forEach>
	</div>
  </c:if>
  </div>
  <div id="myDiv" class="infom"><%=request.getAttribute("AllData") %></div>
	
  <script type='text/javascript' src='./js/cal.js'></script>
  <script type='text/javascript'>
  	function changeColor(){
            var color="#f00|#0f0|#00f|#880|#808|#088|yellow|green|blue|gray";
            color=color.split("|");
            document.getElementById("blink").style.color=color[parseInt(Math.random() * color.length)];
    }
   setInterval("changeColor()",100);
  	function refreshFunction()
	{
    	setTimeout(function(){history.go(0);},3000);
	}
    function requestIntel(url, status,obj,type) {
    	var msg = "该功能可以操作后续日期,当日请手动选择\n\n请确认！";
    	if(type == 1)
    		var msg = "确定要执行转让/键盘功能吗,\n\n请确认！";
    	if(type == 2)
    		var msg = "确定要撤销,\n\n请确认！";
	    if (confirm(msg) == false) {
	        return false;
	    } 
    	url = _apiurl + url;
        var xmlhttp;
        var oStr = '';
        var postData = {};
        postData = {
            "order_status": status
        };
        postData = (function(value) {
            for (var key in value) {
                oStr += key + "=" + value[key] + "&";
            };
            return oStr;
        } (postData));
        if (window.XMLHttpRequest) {
            //  IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
            xmlhttp = new XMLHttpRequest();
        } else {
            // IE6, IE5 浏览器执行代码
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function() {
			var _table = document.getElementById("calendarTable");
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	/*refreshFunction();
				document.getElementById("onekey_open").innerHTML="一键智能订餐";
				document.getElementById("onekey_close").innerHTML="一键智能解订";
		        document.getElementById("onekey_close").disabled=false;
		        document.getElementById("onekey_open").disabled=false;*/
		        if(xmlhttp.responseText == 'receive-success'){
		        	alert('接盘成功');
		        	history.go(0);
		        	return false;
		        }
		        if(xmlhttp.responseText == 'reject-success'){
		        	alert('拒绝成功');
		        	history.go(0);
		        	return false;
		        }
		        if(xmlhttp.responseText == 'rebak-success'){
		        	alert('撤销成功');
		        	history.go(0);
		        	return false;
		        }
		        if(xmlhttp.responseText == 'fail'){
		        	alert('操作失败');
		        	history.go(0);
		        	return false;
		        }
		        history.go(0);
                document.getElementById("myDiv").innerHTML += "<br />" + xmlhttp.responseText + "<br />";
            }else{
            	document.getElementById("onekey_open").disabled=true;
				document.getElementById("onekey_open").innerHTML="请等待...2s刷新";
            	document.getElementById("onekey_close").disabled=true;
				document.getElementById("onekey_close").innerHTML="请等待...2s刷新";
            }
            if(xmlhttp.readyState == 4 && xmlhttp.status == 0){
            	alert('请求后台失败,请联系管理员');
            }
        };
        xmlhttp.open("POST", url, true);
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xmlhttp.send(postData);
    }
  
  </script>
  <script type='text/javascript'>
	<% if("0".equals(request.getAttribute("isfee").toString()))  out.print("alert('上月存在未结清状态,您需要交餐费:'+'"+request.getAttribute("fee")+"元')");%>
  </script>
</body>
</html>