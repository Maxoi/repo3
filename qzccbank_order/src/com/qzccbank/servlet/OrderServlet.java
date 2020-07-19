package com.qzccbank.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.entity.qzccbank_order;
import com.entity.qzccbank_order_staf_cb;
import com.entity.qzccbank_staff;
import com.entity.qzccbank_transfer_log;
import com.qzccbank.dao.OrderDaoImpl;
import com.qzccbank.dao.StaffDao;
import com.qzccbank.dao.StaffDaoImpl;
import com.qzccbank.dao.TransferDaoImpl;
import com.unity.Tools;
import com.util.db.ArrayUtils;
import com.util.db.TimeUtils;

public class OrderServlet extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{

		request.setAttribute("nowRealTime", TimeUtils.getNowTimeHms());
		Calendar now = Calendar.getInstance();
		int hour = TimeUtils.getHour();
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		/* 登陆五分钟自动失效(注销) */
		try {
			int login_seconds = Integer.parseInt(session.getAttribute("login_session_time").toString());
			SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
	        Date day=new Date();
	        int nowsend = Integer.parseInt(formatter.format(day));
	        if(nowsend>login_seconds+300){
	        	StaffServlet ss = new StaffServlet();
				request.setAttribute("error_info", "您已登陆超过五分钟,自动为您注销,为了安全");
	        	ss.logout(request, response);
	        	return;
	        }
		} catch (Exception e) {
			request.setAttribute("error_info", "请先登录");
			Tools.ReTo(request, response, "login");
			return;
		}
		/* 登陆五分钟自动失效(注销) end */
		if(session.getAttribute("u") == null){
			request.setAttribute("error_info", "请先登录");
			Tools.ReTo(request, response, "login");
			return;
		}
		System.out.println("Order页面的Session:["+session.getAttribute("u")+"]");
		qzccbank_staff qo = (qzccbank_staff) session.getAttribute("u");

		/*if(!(hour >= 8 &&  hour <=11 ) && qo.getStaff_id() != 3058 && qo.getStaff_id() != 2704){
			request.setAttribute("error_info", "不在合理时间段");
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}*/
		int staff_id = qo.getStaff_id();
		if(staff_id >0 ){
			StaffDaoImpl sdi = new StaffDaoImpl();
			qzccbank_staff qss = sdi.checkUserPass(staff_id, qo.getStaff_password());
			//不从登陆限制,改为订餐请求限制
			/*if(qss.getstaff_status() != 1 && qo.getStaff_id() != 3058 && qo.getStaff_id() != 2704){
				request.setAttribute("error_info", "食堂已来电确认或工号已被冻结");
				request.getRequestDispatcher("login.jsp").forward(request, response);
				return;
			}*/
		}
		/* 遍历转让操作日志 */
		TransferDaoImpl tfdi = new TransferDaoImpl();
		try {
			List<qzccbank_transfer_log> tl1 = tfdi.getTransInfo(staff_id,1);
			List<qzccbank_transfer_log> tl2 = tfdi.getTransInfo(staff_id,2);
			request.setAttribute("TransLog1_count", tl1.size());
			request.setAttribute("TransLog2_count", tl2.size());
			request.setAttribute("TransLog1", tl1);
			request.setAttribute("TransLog2", tl2);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		int nowtime = now.get(Calendar.DAY_OF_MONTH);
		if("enable".equals(request.getParameter("method"))){
			int count  = Integer.parseInt(request.getParameter("status"));
			String date_str = request.getParameter("date");
			date_str = date_str.substring(6, 8);//日期
			int date = Integer.parseInt(date_str);
			OrderDaoImpl od = new OrderDaoImpl();
			qzccbank_order_staf_cb qosc = od.getFeeByStaff(staff_id);
			if(qosc.getStaff_fee_status() == 0){
				out.print("<span style='color:red'>----------订饭失败,请先结清上月欠费!----------</span>");
				return;
			}
			System.out.println("目前："+nowtime+"选中:"+date);
			if(nowtime >= date){
//				request.setAttribute("error_info", "非法操作,IP已记录");
				out.print("<span style='color:red'>----------非法操作,IP已记录!----------</span>");
//				request.getRequestDispatcher("login.jsp").forward(request, response);
				return;
			}
			//如果是周五判断下周一,否则明天
			boolean is_workday = false;
			if(now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
				is_workday = nowtime >= date-3;
			}else{
				is_workday = nowtime >= date-1;
			}
			if(orderTimeStatus(staff_id) != 1 && is_workday && (now.get(Calendar.HOUR_OF_DAY) >=12)){
				out.print("<span style='color:red'>----------食堂已来电确认或工号已被冻结!----------</span>");
				return;
			}
			qzccbank_order oqo = new qzccbank_order();
			oqo.setStaff_id(staff_id);
			oqo.setStaff_date(date);
			oqo.setOrder_status(count);
			
			if(od.findByDate(date, staff_id) == null){
				if(od.save(oqo) > 0)
					out.println("<span style='color:green'>----------订饭成功!----------</span>");
				else
					out.print("<span style='color:red'>----------订饭失败,请联系管理员!----------</span>");
				return;
			}else{
				if(od.update(oqo) > 0)
					out.print("<span style='color:green'>----------订饭成功!----------</span>");
				else
					out.print("<span style='color:red'>----------订饭失败,请联系管理员!----------</span>");
				return;
			}
		}else if("disabled".equals(request.getParameter("method"))){
			String date_str = request.getParameter("date");
			date_str = date_str.substring(6, 8);//日期
			int date = Integer.parseInt(date_str);
			if(nowtime >= date){
//				request.setAttribute("error_info", "非法操作,IP已记录");
				out.print("<span style='color:red'>----------非法操作,IP已记录!----------</span>");
//				request.getRequestDispatcher("login.jsp").forward(request, response);
				return;
			}
			//如果是周五判断下周一,否则明天
			boolean is_workday = false;
			if(now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
				is_workday = nowtime >= date-3;
			}else{
				is_workday = nowtime >= date-1;
			}
			if(orderTimeStatus(staff_id) != 1 && is_workday && (now.get(Calendar.HOUR_OF_DAY) >=12)){
				out.print("<span style='color:red'>----------食堂已来电确认或工号已被冻结!----------</span>");
				return;
			}
			OrderDaoImpl od = new OrderDaoImpl();
			qzccbank_order oqo = new qzccbank_order();
			oqo.setStaff_id(staff_id);
			oqo.setStaff_date(date);
			oqo.setOrder_status(0);
			if(od.update(oqo) > 0)
				out.print("<span style='color:orange'>----------不订饭成功!----------</span>");
			else
				out.print("<span style='color:red'>----------不订饭失败,请联系管理员!----------</span>");
			return;
		}else if("intel_order".equals(request.getParameter("method"))){
			//智能订餐
			int order_status = Integer.parseInt(request.getParameter("order_status"));
			intelOrder(request, response, staff_id, order_status);
		}else if("receive".equals(request.getParameter("method"))){
			int date_2 = now.get(Calendar.DAY_OF_MONTH);
			TransferDaoImpl tdi = new TransferDaoImpl();
			qzccbank_transfer_log qtl = tdi.getTransalienator(staff_id,TimeUtils.getNowTimeyMdInt());
			int alienator_id = qtl.getTrans_alienator();

			if(updateStaff(staff_id,1,date_2) && updateStaff(alienator_id,0,date_2) && tdi.updateTranStatus(qtl.getTrans_assignee(),qtl.getTrans_time(),1) > 0){
				out.print("receive-success");
			}else{
				out.print("fail");
			}
		}else if("reject".equals(request.getParameter("method"))){
			TransferDaoImpl tdi = new TransferDaoImpl();
			qzccbank_transfer_log qtl = tdi.getTransalienator(staff_id,TimeUtils.getNowTimeyMdInt());
			if(tdi.updateTranStatus(qtl.getTrans_assignee(),qtl.getTrans_time(),3) > 0){
				out.print("reject-success");
			}else{
				out.print("fail");
			}
		}else if("rebak".equals(request.getParameter("method"))){
			TransferDaoImpl tdi = new TransferDaoImpl();
			if(tdi.updateTranStatusAT(staff_id,TimeUtils.getNowTimeyMdInt(),0) > 0){
				out.print("rebak-success");
			}else{
				out.print("fail");
			}
		}
		else{
			try {
				request.setAttribute("loginUser",qo.getStaff_name());
				getAllDateByStaff(request,response,staff_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return;
	}
	
	protected boolean updateStaff(int staff_id,int staff_status,int date) {
		qzccbank_order oqo = new qzccbank_order();
		OrderDaoImpl od = new OrderDaoImpl();
		oqo.setStaff_id(staff_id);
		oqo.setStaff_date(date);
		oqo.setOrder_status(staff_status);
		if(od.findByDate(date, staff_id) == null){
			if(od.save(oqo) > 0)
				return true;
			else
				return false;
		}else{
			if(od.update(oqo) > 0)
				return true;
			else
				return false;
		}
	}
	//查询当月某员工所有订餐记录
	protected void getAllDateByStaff(HttpServletRequest request,HttpServletResponse response,int staff_id) throws IOException, ServletException {
		OrderDaoImpl od = new OrderDaoImpl();
		qzccbank_order_staf_cb qosc = od.getFeeByStaff(staff_id);
		
		List<qzccbank_order> list = od.findAllByStaff(staff_id);
		if(list.size() > 0){
			String allDateString = "";
			for(int i = 0 ; i < list.size() ; i++) {
//				allDateString += list.get(i).getStaff_date() + ",";
				allDateString += list.get(i).getStaff_date()+"|"+list.get(i).getOrder_status() + ",";
			}
			allDateString = allDateString.substring(0,allDateString.length() - 1);
			/*PrintWriter out = response.getWriter();
			out.print(allDateString);
			System.out.println("数据:["+allDateString+"]");*/
			request.setAttribute("AllData", allDateString);

			SimpleDateFormat sdf = new SimpleDateFormat("HH");  
		    String nowH = sdf.format(new Date());  
			request.setAttribute("nowRealHour", nowH);
		}else{
			request.setAttribute("AllData", "");
		}
		request.setAttribute("isfee", qosc.getStaff_fee_status());
		if(qosc.getStaff_fee_status() == 0){
			request.setAttribute("fee", String.format("%.2f",qosc.getCount()*10.00));
		}
		request.setAttribute("orderCount", od.getCountByStaff(staff_id));
		request.setAttribute("orderCountMoney", String.format("%.2f",od.getCountByStaff(staff_id)*10.00));
		request.setAttribute("dateCount", od.getDateCount(3));
		Tools.ReTo(request, response, "order");
		return;
	}
	
	//智能一键订餐或解除
	@SuppressWarnings("deprecation")
	protected void intelOrder(HttpServletRequest request,HttpServletResponse response,int staff_id,int order_status) throws IOException{
		qzccbank_order oqo = new qzccbank_order();
		OrderDaoImpl od = new OrderDaoImpl();
		qzccbank_order_staf_cb qosc = od.getFeeByStaff(staff_id);
		PrintWriter out = response.getWriter();

		if(qosc.getStaff_fee_status() == 0){
			out.print("<span style='color:red'>----------订饭失败,请先结清上月欠费!----------</span>");
			return;
		}
		oqo.setStaff_id(staff_id);
		oqo.setOrder_status(order_status);
		String statusPrintCn = "";
		if(order_status == 1){
			statusPrintCn = "订餐";
		}else{
			statusPrintCn = "解订";
		}
		for (Date date:TimeUtils.getWeekDates()){
			if(date.getDate()-1<=TimeUtils.getDay()) continue;
			System.out.println("订日:["+date.getDate()+"]");
			oqo.setStaff_date(date.getDate());
			if(od.findByDate(date.getDate(), staff_id) == null){
				if(od.save(oqo) > 0)
					out.print("<span style='color:green'>"+date.getDate()+"号"+statusPrintCn+"成功!</span>");
				else
					out.print("<span style='color:red'>"+date.getDate()+"号"+statusPrintCn+"失败,请联系管理员!</span>");
			}else{
				if(od.update(oqo) > 0)
					out.print("<span style='color:green'>"+date.getDate()+"号"+statusPrintCn+"成功!</span>");
				else
					out.print("<span style='color:red'>"+date.getDate()+"号"+statusPrintCn+"失败,请联系管理员!</span>");
			}
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	
	public static void main(String[] args) {
		/*TransferDaoImpl tfdi = new TransferDaoImpl();
		List<qzccbank_transfer_log> list = null;
		List<String> listStrData = new ArrayList<String>();
		try {
			list = tfdi.getTransInfo(3058,2);
			for (int i = 0; i < list.size(); i++) {
				listStrData.add(list.get(i).getStaff_name()+"想把"+list.get(i).getTrans_time()+"的盒饭转让给你,目前状态:"+ArrayUtils.getTranStatus(list.get(i).getTrans_status())+"操作状态:"+ArrayUtils.getOpStatus(list.get(i).getOp_status()));
			}
			System.out.println(listStrData);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(list);*/
		
	}
}
