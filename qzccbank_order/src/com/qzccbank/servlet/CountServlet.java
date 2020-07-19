package com.qzccbank.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;

import com.entity.qzccbank_order_staf_cb;
import com.entity.qzccbank_staff;
import com.qzccbank.dao.OrderDaoImpl;
import com.qzccbank.dao.StaffDaoImpl;
import com.unity.Tools;
import com.util.db.TimeUtils;
public class CountServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		if("brief".equals(request.getParameter("method"))){
			getBriefList(request,response);
			return;
		}else if("xls".equals(request.getParameter("method"))){
			getExcelData(request,response);
			return;
		}
		else{
			getAllList(request,response);
			return;
		}
	}
	
	
	
	public void getBriefList(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		OrderDaoImpl odi = new OrderDaoImpl();
		List<qzccbank_order_staf_cb> list = odi.getAllCount();
		int allStaffCount = odi.getAllStaffCount();
		request.setAttribute("list", list);
		request.setAttribute("allStaffCount", allStaffCount);
		request.setAttribute("allStaffCountMoney", String.format("%.2f",allStaffCount*10.00));
		request.setAttribute("now_month", TimeUtils.getMonth());
		request.setAttribute("now_year", TimeUtils.getYear());
//		request.getRequestDispatcher("list-bef.jsp").forward(request, response);
		Tools.ReTo(request, response, "list-bef");
	}
	
	public void getAllList(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		
		HashMap<String, List<String>> map = getNameDateKVList();
		StaffDaoImpl sdi = new StaffDaoImpl();
		String staffMaxName = sdi.getMaxInnerStaffName();

		request.setAttribute("maxStaffName", staffMaxName);
		request.setAttribute("staffs", map);
		System.out.println("FUCK:["+map+"]");
		OrderDaoImpl odi = new OrderDaoImpl();
		int allStaffCount = odi.getAllStaffCount();
		request.setAttribute("allStaffCount", allStaffCount);
		request.setAttribute("allStaffCountMoney", String.format("%.2f",allStaffCount*10.00));
		request.setAttribute("now_month", TimeUtils.getMonth());
		request.setAttribute("now_year", TimeUtils.getYear());
		request.setAttribute("now_time", TimeUtils.getNowTime());
		Tools.ReTo(request, response, "list");
	}

	public void getExcelData(HttpServletRequest request,HttpServletResponse response) throws IOException {
//		
		HashMap<String, List<Integer>> map = getNameDateKV();

	    
	    //暂时不用线程
	    String downFileName = "C:\\"+TimeUtils.getYear()+TimeUtils.getMonth()+TimeUtils.getDay()+".xls";
	    
	    File fi=new File("C:\\abc.xls");
	    POIFSFileSystem fs;
	    try {
			fs = new POIFSFileSystem(new FileInputStream(fi));
		    //读取excel模板  
		    HSSFWorkbook wb = new HSSFWorkbook(fs);  
		    //读取了模板内所有sheet内容  
		    HSSFSheet sheet = wb.getSheetAt(0);
//		    HSSFRow row = sheet.getRow(0);
//		    sheet.removeRow(row);
		    /* 拼装姓名 */
		    if(isCreated(downFileName)){
			    OrderDaoImpl odi = new OrderDaoImpl();
				List<String> names = new ArrayList<String>();
			    for(String key : map.keySet()){
			    	names.add(key);
			    }
				for (int k = 0; k < names.size(); k++) {
				    HSSFCell cellc = sheet.getRow(k+3).getCell(32);  
				    cellc.setCellFormula("SUM(A"+(k+4)+":AF"+(k+4)+")");
				    if(map.get(names.get(k)).size() == 0){
						HSSFCell cell1 = sheet.getRow(k+3).getCell(0); 
						cell1.setCellValue(names.get(k));
				    }else{
						for (int i = 1; i <=map.get(names.get(k)).size(); i++) {
							HSSFCell cell1 = sheet.getRow(k+3).getCell(0); 
							cell1.setCellValue(names.get(k));
							HSSFCell cell2 = sheet.getRow(k+3).getCell(map.get(names.get(k)).get(i-1));
//							cell2.setCellValue(Integer.parseInt("10"));//不再只统计一份
							cell2.setCellValue(odi.getCountByNameDate(names.get(k), map.get(names.get(k)).get(i - 1)) * 10);
						}
				    }
				}
				List<String> list_table_word = makeTableWord();
				List<Integer> list_date_check = dateCheck();
			    for (int j = 0; j < list_table_word.size(); j++) {
			    	String w_string = list_table_word.get(j);
			    	int i_date_check = list_date_check.get(j);
			    	/* 隐藏列统计值为0的列 */
			    	if(i_date_check == 0){
			    		sheet.setColumnWidth(j+1, 0);
			    	}
			    	/* 隐藏列统计值为0的列 end */
			    	HSSFCell celli = sheet.getRow(1).getCell(j+1);
				    celli.setCellFormula("IF("+w_string+"3="+i_date_check+",\"是\",\"否\")");
				    
				    HSSFCell cellr = sheet.getRow(2).getCell(j+1);
				    cellr.setCellFormula("SUM("+w_string+"4:"+w_string+(4+names.size())+")");
			    }

		    FileOutputStream out = new FileOutputStream(downFileName);
		    wb.write(out);  
		    out.close(); 
		    
		    }
		    
		    down(request, response,downFileName);
		    /*ExportThread et = new ExportThread();
		    et.setMwb(request, response, wb);
		    et.start();
		    down(request, response);*/

		   
		    
//		    String d_path = path_p+"\\abcd.xls";
//			copyFile("C:\\abcd.xls",d_path);
//			PrintWriter out_p = response.getWriter();
//			System.out.println(d_path);
//			out_p.print("<script>window.location.href='"+d_path+"'</script>");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public static HashMap<String, List<Integer>> getNameDateKV(){
		/* 组装k-v数据 */
		OrderDaoImpl odi = new OrderDaoImpl();
		StaffDaoImpl sdi = new StaffDaoImpl();
		List<qzccbank_staff> list_1;
		LinkedHashMap<String, List<Integer>> map = new LinkedHashMap<String, List<Integer>>();
		try {
			list_1 = sdi.findAll();
			for (int i = 0; i < list_1.size(); i++) {
				List<qzccbank_order_staf_cb> list_2 = odi.getAllDate(list_1.get(i).getStaff_id());
				/* V0.0.1 实现单日单人订多份情况 
				List<Integer> ld = list_2.get(0).getDatelist();
				for (int o = 0; o < ld.size(); o++) {
					qzccbank_order qoqo = odi.findByDate(ld.get(o),list_1.get(i).getStaff_id());
					System.out.println(qoqo.getStaff_date()+"|"+qoqo.getOrder_status());
//					List<String> l_d_c = new ArrayList<String>();
				}
				 V0.0.1 实现单日单人订多份情况 end */
//				if(list_2.get(0).getDatelist().size()>0){
					map.put(list_1.get(i).getStaff_name(), list_2.get(0).getDatelist());
//				}else{
//					map.put(list_1.get(i).getStaff_name(), init);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* 组装k-v数据 end */
		return map;
	}
	//页面详细统计
	public static HashMap<String, List<String>> getNameDateKVList(){
		/* 组装k-v数据 */
		OrderDaoImpl odi = new OrderDaoImpl();
		StaffDaoImpl sdi = new StaffDaoImpl();
		List<qzccbank_staff> list_1;
		LinkedHashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		try {
			list_1 = sdi.findAll();
			for (int i = 0; i < list_1.size(); i++) {
				List<qzccbank_order_staf_cb> list_2 = odi.getAllDate(list_1.get(i).getStaff_id());
				map.put(list_1.get(i).getStaff_name(), list_2.get(0).getDatelistStr());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* 组装k-v数据 end */
		return map;
	}
	public static List<String> makeTableWord() {
		List<String> list_c = new ArrayList<String>();
		for (char i = 'B'; i <= 'Z'; i++) {
			String s = String.valueOf(i);
			list_c.add(s);
		}
		for (char i = 'A'; i <= 'G'; i++) {
			String s = "A"+String.valueOf(i);
			list_c.add(s);
		}
		return list_c;
	}
	
	public static List<Integer> dateCheck(){
		List<Integer> l_data = new ArrayList<Integer>();
		OrderDaoImpl odi = new OrderDaoImpl();
		for (int i = 1; i <= 31; i++) {
			l_data.add(odi.getCountByDate(i,3)*10);
		}
		l_data.add(odi.getAllStaffCount()*10);
		return l_data;
	}

	/*
	 * *
	 * 复制单个文件
	 * 
	 * @param oldPath String 原文件路径 如：c:/a.txt
	 * 
	 * @param newPath String 复制后路径 如：f:/a.txt
	 * 
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
	}
	
	public static void down(HttpServletRequest request,HttpServletResponse response,String path) throws Exception{
	    String filename = path.substring(path.lastIndexOf("\\")+1);  
	    //下载文件为中文文件，需要经过url编码  
	    response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename,"UTF-8"));  
	      
	    InputStream in =new FileInputStream(path);  
	    OutputStream out = response.getOutputStream();  
	    int len = 0;  
	    byte buffer[] = new byte[1024];  
	    while((len=in.read(buffer))>0)  
	    {  
	        out.write(buffer,0,len);  
	    }
	}
	
	public static void removeColumn(HSSFSheet sheet, int removeColumnNum,
			int removeColumnTotal) {

		if (sheet == null) {
			return;
		}
		for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator
				.hasNext();) {
			HSSFRow row = (HSSFRow) rowIterator.next();
			HSSFCell cell = row.getCell(removeColumnNum);
			if (cell == null) {
				continue;
			}
			row.removeCell(cell);

			for (int n = removeColumnNum; n < (removeColumnTotal + removeColumnNum); n++) {
				int columnWidth = sheet.getColumnWidth(n + 1);

				HSSFCell cell2 = row.getCell(n + 1);

				if (cell2 == null) {
					break;
				}
				sheet.setColumnWidth(n, columnWidth);
				row.moveCell(cell2, (short) n);
			}
		}
	}
	
	class ExportThread implements Runnable{
		private Thread t;
		private HSSFWorkbook m_wb;
		private HttpServletRequest m_request;
		private HttpServletResponse m_response;
		public void setMwb(HttpServletRequest request,HttpServletResponse response,HSSFWorkbook wb){
			m_request = request;
			m_response = response;
			m_wb = wb;
		}
		@Override
		public void run() {
			System.out.println("Thread运行...");
		    try {
			    //修改模板内容导出新模板  
				File file = new File("C:\\餐费统计.xls");
				if(!file.exists()){
				    FileOutputStream out = new FileOutputStream("C:\\餐费统计.xls");
				    m_wb.write(out);  
				    out.close(); 
	
					//down(m_request,m_response);下载暂时不加线程
					Thread.sleep(2000);
				}else{
					System.out.println("下载完成...");
				}
			} catch (InterruptedException e) {
				System.out.println("中断...");
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		    System.out.println("导出成功!");
		    try {
//				down(m_request,m_response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void start(){
			System.out.println("Thread就绪...");
			if(t == null){
				t = new Thread(this);
				t.start();
			}
		}
	}
	
	/* 不存在或者修改时间超过10s就创建 */
	public static boolean isCreated(String filePath){
		File f = new File(filePath);
		boolean iscre = false;
		if(f.exists()){
	        Calendar cal = Calendar.getInstance();
	        long time = f.lastModified();
	        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
	        cal.setTimeInMillis(time);
	        Date day=new Date();
	        int modtime = Integer.parseInt(formatter.format(cal.getTime()));
	        int nowtime = Integer.parseInt(formatter.format(day));
	        if(nowtime-modtime >=10){
	        	iscre = true;
	        }
		}else{
			iscre = true;
		}
		return iscre;
	}
	
	public static void main(String[] args) throws IOException {
//		copyFile("C:\\abcd.xls","E:\\abcd.xls");
//		CountServlet cs = new CountServlet();
//		HttpServletRequest request = null;
//		HttpServletResponse response = null;
//		cs.getExcelData(request, response);
//		HashMap<String, List<Integer>> lHashMap = getNameDateKV();
//		 for(String key : lHashMap.keySet()){
//			 System.out.println(key);
//		 }
		
//		System.out.println(isCreated("C:/2018227.xls"));
	}
	
}
