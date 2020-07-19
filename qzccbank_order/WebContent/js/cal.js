//var _apiurl = "http://168.168.213.32:3058/qzccbank_order/order";
//var _apiurl = "http://168.168.241.158:3058/qzccbank_order/order";
//var _apiurl = "http://172.172.18.24:3058/qzccbank_order/order";
//var _apiurl = "http://192.168.0.109:8080/qzccbank_order/order";
//var _apiurl = "http://127.0.0.1:3058/qzccbank_order/order";
//var _apiurl = "http://172.172.241.47:7666/qzccbank_order/order";
//var _apiurl = "http://168.168.241.158:7666/qzccbank_order/order";
var _apiurl = "http://106.53.251.120:3058/qzccbank_order/order";
(function() {
	var obj = {};
    /*
   * 用于记录日期，显示的时候，根据dateObj中的日期的年月显示
   */
    var dateObj = (function() {
        var _date = new Date(); // 默认为当前系统时间
        return {
            getDate: function() {
                return _date;
            },
            setDate: function(date) {
                _date = date;
            }
        };
    })();
    // 设置calendar div中的html部分
    renderHtml();
    // 表格中显示日期
    showCalendarData();
    // 绑定事件
    bindEvent();

    /**
   * 渲染html结构
   */
    function renderHtml() {
        var calendar = document.getElementById("calendar");
        var titleBox = document.createElement("div"); // 标题盒子 设置上一月 下一月 标题
        var bodyBox = document.createElement("div"); // 表格区 显示数据
        // 设置标题盒子中的html
        titleBox.className = 'calendar-title-box';
        /*titleBox.innerHTML = "<span class='prev-month' id='prevMonth'></span>" +
      "<span class='calendar-title' id='calendarTitle'></span>" +
      "<span id='nextMonth' class='next-month'></span>";*/
        titleBox.innerHTML = "<span  id='prevMonth'></span>" + "<span class='calendar-title' id='calendarTitle'></span>" + "<span id='nextMonth' ></span>";
        calendar.appendChild(titleBox); // 添加到calendar div中
        // 设置表格区的html结构
        bodyBox.className = 'calendar-body-box';
        var _headHtml = "<tr>" + "<th>日</th>" + "<th>一</th>" + "<th>二</th>" + "<th>三</th>" + "<th>四</th>" + "<th>五</th>" + "<th>六</th>" + "</tr>";
        var _bodyHtml = "";

        // 一个月最多31天，所以一个月最多占6行表格
        for (var i = 0; i < 6; i++) {
            _bodyHtml += "<tr>" + "<td></td>" + "<td></td>" + "<td></td>" + "<td></td>" + "<td></td>" + "<td></td>" + "<td></td>" + "</tr>";
        }
        bodyBox.innerHTML = "<table id='calendarTable' class='calendar-table'>" + _headHtml + _bodyHtml + "</table>";
        // 添加到calendar div中
        calendar.appendChild(bodyBox);
    }

    /* Gphon */
    function loadXMLDoc(url, date, status,obj) {
        /*
	  for(var i=0; i<arguments.length; i++){
	      console.log("参数个数"+arguments.length);
	  }*/
        var xmlhttp;
        var oStr = '';
        var postData = {};
        postData = {
            "date": date,
            "status": status
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
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            	var str_res = xmlhttp.responseText;
            	var str_suc = "订饭成功";
            	var str_fail = "不订饭成功";
            	if(countSubstr(str_res,str_suc)>0){
            		obj.setAttribute('id', 'active');
            		if(parseInt(status) > 1){
            			obj.innerHTML = obj.innerText + "<span style='font-size:18px;color:yellow'>("+status+")</span>";
            		}
            	}
            	if(countSubstr(str_res,str_fail)>0){
            		obj.setAttribute('id', '');
            		var n=obj.innerText.indexOf("(");
            		if(n != -1){
                		obj.innerHTML = obj.innerText.substring(0,n);
            		}
            	}
            		
                document.getElementById("myDiv").innerHTML += "<br />" + xmlhttp.responseText + "<br />";
            }
            if(xmlhttp.readyState == 4 && xmlhttp.status == 0){
            	alert('请求后台失败,请联系管理员');
            }
        };
        xmlhttp.open("POST", url, true);
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xmlhttp.send(postData);
    }
    
    function countSubstr(str, substr) {  
        var reg = new RegExp(substr, "g");  
        return str.match(reg) ? str.match(reg).length : 0;//若match返回不为null，则结果为true，输出match返回的数组(["test","test"])的长度  
    }  
    /* Gphon */
    /**
   * 表格中显示数据，并设置类名
   */
    function showCalendarData() {
        var allDates = document.getElementById("myDiv").innerHTML;
        var _allDates = allDates.split(",");
        var _year = dateObj.getDate().getFullYear();
        var _month = dateObj.getDate().getMonth() + 1;
        var _dateStr = getDateStr(dateObj.getDate());

        // 设置顶部标题栏中的 年、月信息
        var calendarTitle = document.getElementById("calendarTitle");
        var titleStr = _dateStr.substr(0, 4) + "年" + _dateStr.substr(4, 2) + "月";
        calendarTitle.innerText = titleStr;

        // 设置表格中的日期数据
        var _table = document.getElementById("calendarTable");
        var _tds = _table.getElementsByTagName("td");
        var _firstDay = new Date(_year, _month - 1, 1); // 当前月第一天
        for (var i = 0; i < _tds.length; i++) {
            var _thisDay = new Date(_year, _month - 1, i + 1 - _firstDay.getDay());

            var _thisDayStr = getDateStr(_thisDay);
            _tds[i].innerText = _thisDay.getDate();
            _tds[i].data = _thisDayStr;
            _tds[i].setAttribute('data', _thisDayStr);
            if (_thisDayStr == getDateStr(new Date())) { // 当前天
                _tds[i].className = 'currentDay';
                _tds[i].onclick = function() {
                    var STime = new Date();
                    //if((parseInt(STime.getHours()) < 8 || parseInt(STime.getHours()) > 15) && parseInt(new Date().getDate(),10)==parseInt(this.getAttribute('data').substring(6, 8),10)){
                    if(parseInt(new Date().getDate(),10)==parseInt(this.getAttribute('data').substring(6, 8),10)){
                    	alert('请勿操作今天');
                        return false;
                    }

                    
                    /* 对选中进行勾选设置 Gphon */
                    if (this.getAttribute('id') == null || this.getAttribute('id') == '') {
                    	var countNum;
                        countNum = prompt("请输入订餐份数：",1);
                        loadXMLDoc(_apiurl + "?method=enable&=" + Math.random(), this.getAttribute('data'), countNum,this);
//                        this.setAttribute('id', 'active');
                    } else {
                        loadXMLDoc(_apiurl + "?method=disabled&=" + Math.random(), this.getAttribute('data'), 0,this);
//                        this.setAttribute('id', '');
                    }
                }
            } else if (_thisDayStr.substr(0, 6) == getDateStr(_firstDay).substr(0, 6)) {
                _tds[i].className = 'currentMonth'; // 当前月
                /* 对选中进行勾选设置 Gphon */
                _tds[i].onclick = function() {
                    if (parseInt(new Date().getDate(),10) >= parseInt(this.getAttribute('data').substring(6, 8),10)) {
                        alert('只能操作今天以后的日期');
                        return false;
                    }
                    if (this.getAttribute('id') == null || this.getAttribute('id') == '') {
                        var countNum;
                        countNum = prompt("请输入订餐份数：",1);
                        loadXMLDoc(_apiurl + "?method=enable&=" + Math.random(), this.getAttribute('data'), countNum,this);
//                        this.setAttribute('id', 'active');
                    } else {
                        loadXMLDoc(_apiurl + "?method=disabled&=" + Math.random(), this.getAttribute('data'), 0,this);
//                        this.setAttribute('id', '');
                    }
                }
            } else { // 其他月
                _tds[i].className = 'otherMonth';
                /*_tds[i].onclick = function() {
                	console.log(this.getAttribute('data'));
                }*/
            }
            /* 对当前月进行高亮显示 Gphon */
            for (var k = 0; k < _allDates.length; k++) {
                if (parseInt(_tds[i].innerHTML) == parseInt(_allDates[k]) && (_tds[i].className == 'currentMonth' || _tds[i].className == 'currentDay')) {
                   var strDates = new Array();
                   strDates = _allDates[k].split("|");
                   if(parseInt(strDates[1]) > 1){
                	   _tds[i].innerHTML = _tds[i].innerText + "<span style='font-size:18px;color:yellow'>("+strDates[1]+")</span>";
                   }
                   _tds[i].setAttribute('id', 'active');
                }
            }
            /*Gphon*/
        }
    }

    /**
   * 绑定上个月下个月事件
   */
    function bindEvent() {
        var prevMonth = document.getElementById("prevMonth");
        var nextMonth = document.getElementById("nextMonth");
        addEvent(prevMonth, 'click', toPrevMonth);
        addEvent(nextMonth, 'click', toNextMonth);
    }

    /**
   * 绑定事件
   */
    function addEvent(dom, eType, func) {
        if (dom.addEventListener) { // DOM 2.0
            dom.addEventListener(eType,
            function(e) {
                func(e);
            });
        } else if (dom.attachEvent) { // IE5+
            dom.attachEvent('on' + eType,
            function(e) {
                func(e);
            });
        } else { // DOM 0
            dom['on' + eType] = function(e) {
                func(e);
            }
        }
    }

    /**
   * 点击上个月图标触发
   */
    function toPrevMonth() {
        var date = dateObj.getDate();
        dateObj.setDate(new Date(date.getFullYear(), date.getMonth() - 1, 1));
        showCalendarData();
    }

    /**
   * 点击下个月图标触发
   */
    function toNextMonth() {
        var date = dateObj.getDate();
        dateObj.setDate(new Date(date.getFullYear(), date.getMonth() + 1, 1));
        showCalendarData();
    }

    /**
   * 日期转化为字符串， 4位年+2位月+2位日
   */
    function getDateStr(date) {
        var _year = date.getFullYear();
        var _month = date.getMonth() + 1; // 月从0开始计数
        var _d = date.getDate();

        _month = (_month > 9) ? ("" + _month) : ("0" + _month);
        _d = (_d > 9) ? ("" + _d) : ("0" + _d);
        return _year + _month + _d;
    }
})();