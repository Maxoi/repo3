<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>泉州银行订餐系统</title>
	<link rel="stylesheet" href="./css/normalize.min.css">
	<link rel="stylesheet" href="./css/style.css">
	<!--<script src="./js/browser.js"></script>-->
	<script src="./js/prefixfree.min.js"></script>
	<script src="./js/html5.js"></script>
	<style>
		.update{
			color:#fff;
		}
		.tj{
			color:orange;
			font-size:15px;
		}
		.forbid{
			width:300px;
			width:85%;
			margin:0 auto;
		}
	    .nowtime{
			font-size:12px;
			color:pink;
	    }
	    body{
			overflow:auto; 
	    }
	    #BRO a{
	    	font-size:20px;
	    	color:#fff;
	    }
	</style>
	<script type="text/javascript">
	function tips_pop() {
	    var MsgPop = document.getElementById("winpop");
	    var popH = parseInt(MsgPop.style.height); //将对象的高度转化为数字
	    if (popH == 0) {
	        MsgPop.style.display = "block"; //显示隐藏的窗口
	        show = setInterval("changeH('up')", 2);
	    } else {
	        hide = setInterval("changeH('down')", 2);
	    }
	}
	
	function changeH(str) {
		var con_height = 150;//弹出总高度
	    var MsgPop = document.getElementById("winpop");
	    var popH = parseInt(MsgPop.style.height);
	    if (str == "up") {
	        if (popH <= con_height) {
	            MsgPop.style.height = (popH + 4).toString() + "px";
	        } else {
	            clearInterval(show);
	        }
	    }
	    if (str == "down") {
	        if (popH >= 4) {
	            MsgPop.style.height = (popH - 4).toString() + "px";
	        } else {
	            clearInterval(hide);
	            MsgPop.style.display = "none"; //隐藏DIV
	        }
	    }
	}
	window.onload = function () {
	    var oclose = document.getElementById("close");
	    var bt = document.getElementById("bt");
	    document.getElementById('winpop').style.height = '0px';
	    setTimeout("tips_pop()", 100);
	    oclose.onclick = function () {
	        tips_pop();
	    }
	    bt.onclick = function () {
	        tips_pop();
	    }
	    setTimeout("tips_pop()", 7000);
	}
	</script>
</head>
<body>
	<div id="BRO" style="color:#fff;display:none"><a href="http://168.168.241.158:3058/qzccbank_order/360cse_12.0.1053.0.exe">您的IE浏览器<=11,强烈交易要求您换浏览器，点击下载360极速浏览器，请选择极速模式</a></div>
	<div id="silu">
	 <button id="bt" style="display:none">3秒后会在右下角自动弹出窗口，如果没有弹出请点击这个按钮</button>
	</div>
	<div id="winpop">
	 <div class="title">您有新的短消息！<span class="close" id="close">X</span></div>
	 <!--<div class="con"><p>请大家每月月初按时清算上月费用,逾时关闭点餐功能!</p>v1.4 更新说明:<p>更新未缴费点餐bug,更新节假日智能过滤点餐规则！</p></div>-->
	 <div class="con"><p>明日份</p><p><%=request.getAttribute("menu") %></p></div>
	</div>
  <div class="login">
	<h1 style="font-size:24px;">泉行订餐系统-海信精简版</h1>
	<h1 style="display:none"><a href="zf.html" style="color:#fff" target="_blank" >缴费</a></h1>
	<h1><a href="list" class="tj">当月统计</a> <a href="<%=request.getAttribute("YM") %>.xls" class="tj">上月报表</a> <a href="<%=request.getAttribute("CYM") %>.xls" class="tj" style="<%=request.getAttribute("IsLastDay") %>">当月报表</a> <% if(request.getAttribute("LastDayInfo") != null) out.print(request.getAttribute("LastDayInfo")); %><% if(request.getAttribute("dateCount_in") != null || request.getAttribute("dateCount_out") != null) out.print("<p class='today_count'>("+request.getAttribute("dateCount")+")今天已订:"+request.getAttribute("dateCount_in")+"(行内)"+request.getAttribute("dateCount_out")+"(外包)<br />("+request.getAttribute("dateCount_tom")+")下日已定:"+request.getAttribute("nextdateCount_in")+"(行内)"+request.getAttribute("nextdateCount_out")+"(外包)</p>"); %></h1>
	<marquee style="color:yellow;display:none">特别说明:微信或银行转账缴费请写清楚备注如(小明 6月 餐费),否则无效！</marquee>
	<h1 class="error_info" style="color:red"><% if(request.getAttribute("error_info") != null) out.print(request.getAttribute("error_info")); %></h1>
    <form method="post" action="index?method=login">
    	<p class="forbid"><input type="button" onclick="forbid()" value="禁止订餐(仅限报餐人员点击,请注意!)" /></p>
    	<span id="label_info_u" style="display:none">工号</span><input type="text" name="username" placeholder="工号" id="username" required="required" />
    	<span id="label_info_p" style="display:none">密码</span><input type="password" name="password" placeholder="密码" id="password"  required="required" />
        <button type="submit" class="btn btn-primary btn-block btn-large"><span class="nowtime">服务器时间:<%=request.getAttribute("nowRealTime") %></span> 登录 <span class="nowtime">客户端时间:<script type='text/javascript'>var STime = new Date();document.write(STime.getHours()+":"+STime.getMinutes()+":"+STime.getSeconds());</script></span></button>
        <h1 class="login-author">design by xujh & youhl 2018 V1.4</h1>
        <h1 class="login-useinfo">可用时段:12:00 AM - 16:00 AM</h1>
        <h1 class="login-useinfo">请及时来此勾饭</h1>
        
    <div class="zfpng">
    	<img src="image/ysf2.png"  height="400px" align="center" />
    </div>
    </form>
	</div>
<!--<div class="update"><marquee>v1.3 更新说明:<p>实现转让功能,当天转让日志。来电确认前不可进行转让操作,请来电确认后或者中午12点以后进行转让操作!</p></marquee></div>-->
    <script src="js/index.js"></script>
	<script src="js/browser_type.js"></script>
	<script language=javascript>
	//alert('本站已迁移到http://168.168.241.158:7666/qzccbank_order/index请在登陆页进入,存储收藏夹');
	function forbid() {
	    var msg = "您真的确定要冻结所有用户吗？一经点击所有人无法订餐\n\n请确认！";
	    if (confirm(msg) == true) {
	        window.location.href = 'index?method=lock';
	        //return true; 
	    } else {
	        return false;
	    }
	}
	</script>
</body>
</html>