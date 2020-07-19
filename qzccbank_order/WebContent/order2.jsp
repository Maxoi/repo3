<!DOCTYPE html>
<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>订餐系统传新版本</title>
<link rel="stylesheet" type="text/css" href="css/style_login.css">
<link rel='stylesheet' href='./css/cal2.css' />
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/vector.js"></script>
<style>
	  .c_count{
	  	color:#fff;
	  	margin:0 auto;
	  	
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
<div id="container">
	<div id="output">
		<div class="containerC">
			<div class='calendar' id='calendar'>
				<span class="fee"><% if("0".equals(request.getAttribute("isfee").toString())) out.print("上月存在未结清状态,请及时<a href=\"zf.html\" target=\"_blank\" class=\"fee_a\" >缴费</a>,上月餐费:["+request.getAttribute("fee")+"]元"); %></span><span>订饭或转让结束后请及时刷新以保证最新状态!!!!!!!(当日已订:<%=request.getAttribute("dateCount") %>)</span><br />
				<span class="c_count">尊敬的<span class="red"><%=request.getAttribute("loginUser") %></span>,您好 您当月已订<span style="color:red;"><%=request.getAttribute("orderCount") %></span>次,合计<span style="color:red;"><%=request.getAttribute("orderCountMoney") %></span>元  </span>
				<div class="path"><a href="list">当月统计</a>>>><a href="index?method=logout">注销</a>>>><a href="zf.html" target="_blank" >缴费</a><!--<a href="index?method=trans" target="_blank" style="color:#fff">转让(仅限当天)</a>--></div>
				<div class="intel_order">
					<button id="onekey_open" onclick="requestIntel('?method=intel_order&=' + Math.random(),1,null,0);">一键智能订餐</button>
					<button id="onekey_close" onclick="requestIntel('?method=intel_order&=' + Math.random(),0,null,0);">一键智能解订</button>
					<p style="color:#fff;display:none;">为防止遗忘订餐,建议大家使用此功能,自动过滤周末,节假日暂未实现请手动勾除,该按钮只能操作明天到月底,当日请手工选择</p>
					<!--<p style="color:orange;font-size:20px;" id="blink">使用转让功能请及时提醒受让方接受，否则第二天自动撤销，若有转让申请往下滚动!!!</p>-->
				</div>
			</div>
  		<div id="myDiv" class="infom"><%=request.getAttribute("AllData") %></div>
		</div>
	</div>
</div>
<script type='text/javascript' src='./js/cal.js'></script>
  <script type='text/javascript'>
  $(function(){
        Victor("container", "output");   //登录背景函数
        $("#entry_name").focus();
        $(document).keydown(function(event){
            if(event.keyCode==13){
                $("#entry_btn").click();
            }
        });
    });
  	/*function changeColor(){
            var color="#f00|#0f0|#00f|#880|#808|#088|yellow|green|blue|gray";
            color=color.split("|");
            document.getElementById("blink").style.color=color[parseInt(Math.random() * color.length)];
    }
   setInterval("changeColor()",100);*/
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
	//<% if("0".equals(request.getAttribute("isfee").toString()))  out.print("alert('上月存在未结清状态,您需要交餐费:'+'"+request.getAttribute("fee")+"元')");%>
  </script>
</body>
</html>