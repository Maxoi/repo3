(function() {
    /* 
	  * 描述：判断浏览器信息 
	  * 编写：LittleQiang_w 
	  * 日期：2016.1.5 
	  * 版本：V1.1 
	  */
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串 
    var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器 
    //判断当前浏览类型 
    function BrowserType() {
        var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器 
        var isEdge = userAgent.indexOf("Windows NT 6.1; Trident/7.0;") > -1 && !isIE; //判断是否IE的Edge浏览器 
        var isFF = userAgent.indexOf("Firefox") > -1; //判断是否Firefox浏览器 
        var isSafari = userAgent.indexOf("Safari") > -1 && userAgent.indexOf("Chrome") == -1; //判断是否Safari浏览器 
        var isChrome = userAgent.indexOf("Chrome") > -1 && userAgent.indexOf("Safari") > -1; //判断Chrome浏览器 
        if (isIE) {
            var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
            reIE.test(userAgent);
            var fIEVersion = parseFloat(RegExp["$1"]);
            if (fIEVersion == 7) {
                return "IE7";
            } else if (fIEVersion == 8) {
                return "IE8";
            } else if (fIEVersion == 9) {
                return "IE9";
            } else if (fIEVersion == 10) {
                return "IE10";
            } else if (fIEVersion == 11) {
                return "IE11";
            } else {
                return "0"
            } //IE版本过低 
        } //isIE end 
        if (isFF) {
            return "FF";
        }
        if (isOpera) {
            return "Opera";
        }
        if (isSafari) {
            return "Safari";
        }
        if (isChrome) {
            return "Chrome";
        }
        if (isEdge) {
            return "Edge";
        }
    } //myBrowser() end 
    //判断是否是IE浏览器 
    function isIE() {
        var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串 
        var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器 
        if (isIE) {
            return "1";
        } else {
            return "-1";
        }
    }

    //判断是否是IE浏览器，包括Edge浏览器 
    function IEVersion() {
        var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串 
        var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器 
        var isEdge = userAgent.indexOf("Windows NT 6.1; Trident/7.0;") > -1 && !isIE; //判断是否IE的Edge浏览器 
        if (isIE) {
            var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
            reIE.test(userAgent);
            var fIEVersion = parseFloat(RegExp["$1"]);
            if (fIEVersion == 7) {
                return "IE7";
            } else if (fIEVersion == 8) {
                return "IE8";
            } else if (fIEVersion == 9) {
                return "IE9";
            } else if (fIEVersion == 10) {
                return "IE10";
            } else if (fIEVersion == 11) {
                return "IE11";
            } else {
                return "0";
            } //IE版本过低 
        } else if (isEdge) {
            return "Edge";
        } else {
            return "-1"; //非IE 
        }
    }
    var b_type =BrowserType();
    if(b_type.indexOf("IE") == 0){
    	var ie_version = parseInt(b_type.slice(2));
    	if(ie_version < 11){

    	    var ieUpd = document.getElementById("BRO");
    	    ieUpd.style.display='block';
    		alert('强烈不建议使用IE浏览器,如果非得用,请用11及以上版本...');
    		if(confirm("确定换浏览器吗？")){
    			
    	    }
    		if(confirm("很烦吧一直提示,还是换吧")){
    			window.location.href="http://168.168.241.158:3058/qzccbank_order/360cse_12.0.1053.0.exe";
    		}
    	}
    }

    
})();