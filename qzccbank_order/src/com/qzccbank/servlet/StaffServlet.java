package com.qzccbank.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import com.autodata.Auto;
import com.entity.qzccbank_order;
import com.entity.qzccbank_order_menu;
import com.entity.qzccbank_staff;
import com.entity.qzccbank_transfer_log;
import com.qzccbank.dao.MenuDaoImpl;
import com.qzccbank.dao.OrderDaoImpl;
import com.qzccbank.dao.StaffDao;
import com.qzccbank.dao.StaffDaoImpl;
import com.qzccbank.dao.TransferDaoImpl;
import com.unity.Tools;
import com.util.db.TimeUtils;

public class StaffServlet extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{

		request.setAttribute("nowRealTime", TimeUtils.getNowTimeHms());
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String remoteIp=request.getRemoteAddr();//获取客户端的ip
		int remotePort=request.getRemotePort();//获取客户端的端口号
		String serverName=request.getRemoteHost();//获取远程计算机的名字
		System.out.println("远程ip:["+remoteIp+"] 端口:["+remotePort+"]计算机名:["+serverName+"]");
		if("login".equals(request.getParameter("method"))){
			
			int staff_id = 0;
			try {
				staff_id = Integer.parseInt(request.getParameter("username"));
			} catch (Exception e) {
//				request.getRequestDispatcher("login.jsp").forward(request, response); 
//				response.sendRedirect("login.jsp");
				Tools.ReTo(request, response, "login");
			}
			String staff_pass = request.getParameter("password");
			if(staff_pass == null){
				return;
			}
			login(staff_id,staff_pass,request,response);
			return;
		}else if("reset".equals(request.getParameter("method"))){
			System.out.println("正式进入:[reset]");
			String password = request.getParameter("password");
			String re_password = request.getParameter("re_password");
			resetPassword(request,response,password,re_password);
			return;
		}else if("logout".equals(request.getParameter("method"))){
			System.out.println("正式进入:[logout]");
			logout(request, response);
			return;
		}else if("manage".equals(request.getParameter("method"))){
			System.out.println("正式进入:[manage]");
			manage(request, response);
			return;
		}else if("lock".equals(request.getParameter("method"))){
			System.out.println("正式进入:[lock]");
			lock(request,response);
			return;
		}
		else if("trans".equals(request.getParameter("method"))){
			System.out.println("进入:[trans]");
			trans(request,response);
			return;
		}

		request = saveStatus(request);
		request.setAttribute("YM",String.valueOf(TimeUtils.getNowTimeyLMInt(TimeUtils.getMonth()-1)));//上1个月
		//获取菜单列表
		MenuDaoImpl mdi = new MenuDaoImpl();
		request.setAttribute("menu",mdi.GetMenuList());
		//获得下个工作日菜单详情
		System.out.println("cdqk:["+mdi.GetMenuListByDate(TimeUtils.getTomDay())+"]");
		List<qzccbank_order_menu> lqm = mdi.GetMenuListByDate(TimeUtils.getTomDay());
//		request.setAttribute("menu_detail",lqm);
		List<String> k = new ArrayList<String>();
		k.add("1");
		k.add("2");
		request.setAttribute("menu_detail",k);
		request.setAttribute("CYM",String.valueOf(TimeUtils.getNowTimeyLMInt(TimeUtils.getMonth())));//当前
		String h = TimeUtils.getNowTimeH();
		int now_hour = Integer.parseInt(h);
		if(TimeUtils.isLastDayOfMonth(new Date()) && now_hour > 11)//超过11点才显示
			request.setAttribute("IsLastDay","display:inline");
		else
			request.setAttribute("IsLastDay","display:none");
		
		
		Tools.ReTo(request,response,"login");
//		response.sendRedirect("login.jsp");

	}
	//登录实现
	protected void login(int staff_id,String staff_password,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		int hour = TimeUtils.getHour();
		
		/* 不限制登录时间 */
		/*if(!(hour >= 8 &&  hour <=11 ) && staff_id != 3058 && staff_id != 2704){
			request.setAttribute("error_info", "不在合理时间段");
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}*/
		StaffDaoImpl sd = new StaffDaoImpl();
		HttpSession session = request.getSession();
		if(sd.checkResetPass(staff_id)){
			qzccbank_staff qs = sd.findByStaffid(staff_id);
			//如果数据库中存在工号则重置密码
			if(qs.getStaff_name().length() > 0){
				session.setAttribute("user", qs.getStaff_name());
				session.setAttribute("user_id", qs.getStaff_id());
				request.setAttribute("staff_name", qs.getStaff_name());
				request.getRequestDispatcher("reset.jsp").forward(request, response);
				return;
			}
		}
		qzccbank_staff qs = sd.checkUserPass(staff_id, staff_password);
		
		if(qs == null){
			request.setAttribute("error_info", "用户名或密码错误");
//			request.getRequestDispatcher("login.jsp").forward(request, response);
			Tools.ReTo(request, response, "login");
			return;
		}else{
			//不从登陆限制,改为订餐请求限制
			/*if(qs.getstaff_status() != 1 && staff_id != 3058 && staff_id != 2704){
				request.setAttribute("error_info", "食堂已来电确认或账户已被冻结");
				request.getRequestDispatcher("login.jsp").forward(request, response);
				return;
			}*/
			session.setAttribute("u", qs);
			System.out.println("用户名session:["+session.getAttribute("user")+"]");
			System.out.println("IDsession:["+session.getAttribute("user_id")+"]");
			System.out.println("用户对象session:["+session.getAttribute("u")+"]");

			SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
	        Date day=new Date();
	        int nowtime = Integer.parseInt(formatter.format(day));

			session.setAttribute("login_session_time", nowtime);
			System.out.println("登陆时间:["+session.getAttribute("login_session_time")+"]");
			response.sendRedirect("order");
			//request.getRequestDispatcher("order").forward(request, response);
			return;
		}
	}
	//重置密码实现
	protected void resetPassword(HttpServletRequest request,HttpServletResponse response,String password,String re_password) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int user_id = Integer.parseInt(session.getAttribute("user_id").toString());
		System.out.println("USERID:["+user_id+"]");
		if(!password.equals(re_password)){
			request.setAttribute("error_info", "密码与确认密码不一样!");
			request.getRequestDispatcher("reset.jsp").forward(request, response); 
		}else{
			StaffDaoImpl sd = new StaffDaoImpl();
			qzccbank_staff qs = new qzccbank_staff();
			qs.setStaff_id(user_id);
			qs.setStaff_password(re_password);
			if(sd.resetPassword(qs) == 0){
				request.setAttribute("error_info", "修改失败,请联系管理员!");
			}else{
				request.setAttribute("error_info", "修改成功,请使用新密码登录");
//				request.getRequestDispatcher("login.jsp").forward(request, response);
				Tools.ReTo(request, response, "login");
			}
		}
	}
	
	//注销
	protected void logout(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		session.invalidate();
		request = saveStatus(request);
//		request.getRequestDispatcher("login.jsp").forward(request, response);
		Tools.ReTo(request, response, "login");
//		response.sendRedirect("login.jsp");
	}
	
	protected void manage(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		System.out.println("进入[manage]");
		HttpSession session = request.getSession();
		try {
			qzccbank_staff qo = (qzccbank_staff) session.getAttribute("u");
			if(qo.getStaff_id() != 3058 && qo.getStaff_id() != 2704){
				request.setAttribute("error_info", "非法操作,IP已记录");
//				request.getRequestDispatcher("login.jsp").forward(request, response);
				Tools.ReTo(request, response, "login");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("login.jsp");
			return;
		}
		String op_type = request.getParameter("op_type");
		int staff_id = 0;
		String staff_name = "";
		StaffDaoImpl sdi = new StaffDaoImpl();
		qzccbank_staff qs = new qzccbank_staff();
		if("1".equals(op_type)){
			//重置密码
			try {
				staff_id = Integer.parseInt(request.getParameter("staff_id"));
			} catch (Exception e) {
				request.setAttribute("error_info", "操作失败");
				request.getRequestDispatcher("manage.jsp").forward(request, response); 
				return;
			}
			qs.setStaff_id(staff_id);
			qs.setStaff_password("");
			if(sdi.resetPassword(qs) > 0 && staff_id > 0){
				request.setAttribute("error_info", "重置"+staff_id+"密码成功");
			}else{
				request.setAttribute("error_info", "重置"+staff_id+"密码失败");
			}
		}else if("2".equals(op_type)){
			int staff_type = 1;
			//新增员工
			try {
				staff_id = Integer.parseInt(request.getParameter("staff_id"));
				staff_type = Integer.parseInt(request.getParameter("staff_type"));
				staff_name = request.getParameter("staff_name");
			} catch (Exception e) {
				request.setAttribute("error_info", "操作失败");
				request.getRequestDispatcher("manage.jsp").forward(request, response); 
				return;
			}
			System.out.println("waibao:["+staff_type+"]");
			qs.setStaff_id(staff_id);
			qs.setStaff_name(staff_name);
			qs.setStaff_password("");
			qs.setStaff_type(staff_type);
			qs.setSort(sdi.getMaxStaffSort()+1);
			if(staff_name.length() <=0 ){
				request.setAttribute("error_info", "姓名不能为空");
				request.getRequestDispatcher("manage.jsp").forward(request, response); 
				return;
			}
			
			if(sdi.addStaff(qs) > 0 && staff_id > 0){
				request.setAttribute("error_info", "新增"+staff_id+"---"+staff_name+"员工成功");
			}else{
				request.setAttribute("error_info", "新增"+staff_id+"---"+staff_name+"员工失败");
			}
		}
		else if("3".equals(op_type)){
			//冻结员工
			try {
				staff_id = Integer.parseInt(request.getParameter("staff_id"));
			} catch (Exception e) {
				staff_id = 0;
			}
			System.out.println("工号:"+staff_id);
			int result = 0;
			if(staff_id == 0){
				int allSum = sdi.getAllStaffCount();
				result = sdi.lockStaff(0,0);
				if(result == allSum){
					result = 1;
				}else{
					int failCount = allSum-result;
					request.setAttribute("error_info", "请注意,冻结操作失败"+failCount+"条");
				}
			}else{
				result = sdi.lockStaff(0,staff_id);
			}
			
			if(result == 1){
				request.setAttribute("error_info", "冻结操作成功");
			}else{
				request.setAttribute("error_info", "冻结操作失败");
			}
		}
		else if("4".equals(op_type)){
			//解冻员工
			try {
				staff_id = Integer.parseInt(request.getParameter("staff_id"));
			} catch (Exception e) {
				staff_id = 0;
			}
			int result = 0;
			if(staff_id == 0){
				int allSum = sdi.getAllStaffCount();
				result = sdi.lockStaff(1,0);
				if(result == allSum){
					result = 1;
				}else{
					int failCount = allSum-result;
					request.setAttribute("error_info", "请注意,解冻操作失败"+failCount+"条");
				}
			}else{
				result = sdi.lockStaff(1,staff_id);
			}
			
			if(result == 1){
				request.setAttribute("error_info", "解冻操作成功");
			}else{
				request.setAttribute("error_info", "解冻操作失败");
			}
		}
		else if("5".equals(op_type)){
			//解冻员工
			int fee_type = 0;
			try {
				staff_id = Integer.parseInt(request.getParameter("staff_id"));
			} catch (Exception e) {
				staff_id = 0;
				e.printStackTrace();
			}
			try {
 				staff_name = request.getParameter("staff_name");
			} catch (Exception e) {
				staff_name = "1";
				e.printStackTrace();
			}
			try {
				fee_type = Integer.parseInt(request.getParameter("fee_type"));
			} catch (Exception e) {
				fee_type = 0;
				e.printStackTrace();
			}
			int result = 0;
			result = sdi.unlockStaffStaffFeeStatus(staff_id,staff_name,fee_type);
			if(result == 1){
				request.setAttribute("error_info", "欠费冻结解除成功");
			}else{
				request.setAttribute("error_info", "欠费冻结解除失败");
			}
		}else if("6".equals(op_type)){
			System.out.println("手动导出上月报表");
			Auto auto = new Auto();
			auto.setMonth(TimeUtils.getMonth()-1);
			auto.hand();
			request.setAttribute("error_info", "生成成功!");
		}else if("7".equals(op_type)){
			System.out.println("手动导出当月报表");
			Auto auto = new Auto();
			auto.setMonth(TimeUtils.getMonth());
			auto.hand();
			request.setAttribute("error_info", "生成成功!");
		}
		else if("0".equals(op_type)){
			OrderDaoImpl odi = new OrderDaoImpl();
			request.setAttribute("error_info", "今日已订:"+odi.getDateCount(3));
		}
		request.getRequestDispatcher("manage.jsp").forward(request, response); 
	}
	
	protected void trans(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		System.out.println("正式进入[trans]");
		/* 判断登录状态 */
		HttpSession session = request.getSession();
		qzccbank_staff qo = (qzccbank_staff) session.getAttribute("u");
		if(qo == null){
			request.setAttribute("error_info", "请先登录");
//			request.getRequestDispatcher("login.jsp").forward(request, response);
			Tools.ReTo(request, response, "login");
			return;
		}		
		
		if(qo == null){
			request.setAttribute("error_info", "请先登录");
//			request.getRequestDispatcher("login.jsp").forward(request, response);
			Tools.ReTo(request, response, "login");
			return;
		}
		
		System.out.println("登录用户:"+qo.getStaff_id());
		int staff_id = 0;
		try {
			staff_id = Integer.parseInt(request.getParameter("staff_id"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println("转让人工号:["+qo.getStaff_id()+"]");
		System.out.println("受让人工号:["+staff_id+"]");
		StaffDaoImpl sdi = new StaffDaoImpl();
		int alienator_id = qo.getStaff_id();
		int assignee_id = staff_id;
		OrderDaoImpl odi = new OrderDaoImpl();
//		qzccbank_order alienatorObj = odi.findByDate(TimeUtils.getDay(),alienator_id);
		qzccbank_order alienatorObj = null;
		qzccbank_order assigneeObj = null;
		try {
			Calendar now = Calendar.getInstance();
			int nowtime = now.get(Calendar.DAY_OF_MONTH);
			alienatorObj = odi.findByDate(nowtime,alienator_id);
			assigneeObj = odi.findByDate(nowtime,assignee_id);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		System.out.println("转让:["+alienatorObj+"]受让:["+assigneeObj+"]时间:["+TimeUtils.getHour()+"]");
		if(orderTimeStatus(alienator_id) == 1 && TimeUtils.getHour() <12){
			request.setAttribute("error_info", "来电确认前不可进行转让操作,请来电确认后或者中午12点以后进行转让操作!");
		}else if(alienatorObj == null || alienatorObj.getOrder_status() != 1 || assigneeObj != null && assigneeObj.getOrder_status() != 0){
			request.setAttribute("error_info", "转让人当日没订或受让人当日已订");
		}
		else{
			if(staff_id > 0){
				/* 转让处理 */
				TransferDaoImpl dao = new TransferDaoImpl();
				qzccbank_transfer_log qtl = new qzccbank_transfer_log();
				qtl.setTrans_alienator(alienator_id);
				qtl.setTrans_assignee(assignee_id);
				qtl.setTrans_time(TimeUtils.getNowTimeyMdInt());
				qtl.setOp_time(TimeUtils.getNowTime2Int());
				qtl.setTrans_status(2);
				qtl.setSort(0);
				qtl.setOp_status(1);
				if(dao.addTrans(qtl) == 1){
					request.setAttribute("error_info", "准备转让成功,请提醒对方接受转让才可生效!(24小时内有效)");
				}else{
					request.setAttribute("error_info", "转让失败,请联系管理员!");
				}
			}
		}
		try {
			List<qzccbank_staff> qs_list = sdi.findAll();
			request.setAttribute("StaffIdName", qs_list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("trans.jsp").forward(request, response); 
	}
	
	//所有人可用,向食堂报数时一定先操作
	protected void lock(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
			StaffDaoImpl sdi = new StaffDaoImpl();
			int result = 0;
			int allSum = sdi.getAllStaffCount();
			result = sdi.lockStaff(0,0);
			if(result == allSum){
				result = 1;
			}else{
				int failCount = allSum-result;
				request.setAttribute("error_info", "请注意,冻结操作失败"+failCount+"条");
			}
			
			if(result == 1){
				request.setAttribute("error_info", "冻结操作成功");
			}else{
				request.setAttribute("error_info", "冻结操作失败");
			}
			request = saveStatus(request);
//			request.getRequestDispatcher("login.jsp").forward(request, response);
			Tools.ReTo(request, response, "login");
	}
	
	public HttpServletRequest saveStatus(HttpServletRequest request){
		OrderDaoImpl odi = new OrderDaoImpl();
		int dcOut = odi.getDateCount(0);
		int dcIn = odi.getDateCount(1);
		int tomDcOut = odi.getCountByDate(TimeUtils.getTomDay(),0);
		int tomDcIn = odi.getCountByDate(TimeUtils.getTomDay(),1);
		request.setAttribute("dateCount_in", dcIn);//行内
		request.setAttribute("dateCount_out",dcOut);//外包
		request.setAttribute("dateCount", dcOut+dcIn);//总计
		request.setAttribute("nextdateCount_in", tomDcIn);//下一工作日几份行内
		request.setAttribute("nextdateCount_out", tomDcOut);//下一工作日几份外包
		request.setAttribute("dateCount_tom", tomDcIn+tomDcOut);//下日总计
		return request;
	}
	//状态判断(或过时判断)是否有点餐权限
	protected int orderTimeStatus(int staff_id) {
		StaffDaoImpl sdi = new StaffDaoImpl();
		qzccbank_staff qs = sdi.getStaffById(staff_id);
		int staff_status = 0;
		try {
			staff_status = qs.getStaff_status();
		} catch (Exception e) {
			staff_status = 0;
			e.printStackTrace();
		}
		return staff_status;
	}
	
}
