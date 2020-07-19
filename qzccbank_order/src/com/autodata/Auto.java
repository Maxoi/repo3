package com.autodata;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.entity.qzccbank_order;
import com.entity.qzccbank_order_staf_cb;
import com.entity.qzccbank_staff;
import com.qzccbank.dao.OrderDaoImpl;
import com.qzccbank.dao.StaffDaoImpl;
import com.util.db.TimeUtils;

public class Auto {
	private static  int month = 0;
	private static ResourceBundle rb = ResourceBundle.getBundle("com.util.db.db-config");
	public static int getMonth() {
		return month;
	}

	public static void setMonth(int month) {
		Auto.month = month;
	}

	public static HashMap<String, List<Integer>> getNameDateKV() {
		/* 组装k-v数据 */
		OrderDaoImpl odi = new OrderDaoImpl();
		StaffDaoImpl sdi = new StaffDaoImpl();
		List<qzccbank_staff> list_1;
		odi.setTable_name(month);//选定月份
		LinkedHashMap<String, List<Integer>> map = new LinkedHashMap<String, List<Integer>>();
		try {
			list_1 = sdi.findAll();
			for (int i = 0; i < list_1.size(); i++) {
				List<qzccbank_order_staf_cb> list_2 = odi.getAllDate(list_1
						.get(i).getStaff_id());
				map.put(list_1.get(i).getStaff_name(), list_2.get(0)
						.getDatelist());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* 组装k-v数据 end */
		return map;
	}

	/* 不存在或者修改时间超过10s就创建 */
	public static boolean isCreated(String filePath) {
		File f = new File(filePath);
		boolean iscre = false;
		if (f.exists()) {
			Calendar cal = Calendar.getInstance();
			long time = f.lastModified();
			SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
			cal.setTimeInMillis(time);
			Date day = new Date();
			int modtime = Integer.parseInt(formatter.format(cal.getTime()));
			int nowtime = Integer.parseInt(formatter.format(day));
			if (nowtime - modtime >= 10) {
				iscre = true;
			}
		} else {
			iscre = true;
		}
		return iscre;
	}

	public void hand() {
		HashMap<String, List<Integer>> map = getNameDateKV();
		File fi = new File("C:\\abc.xls");
		POIFSFileSystem fs;
		OrderDaoImpl odi = new OrderDaoImpl();
		odi.setTable_name(month);//选定月份
		try {
			fs = new POIFSFileSystem(new FileInputStream(fi));
			// 读取excel模板
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			// 读取了模板内所有sheet内容
			HSSFSheet sheet = wb.getSheetAt(0);
			// HSSFRow row = sheet.getRow(0);
			// sheet.removeRow(row);
			/* 拼装姓名 */
			List<String> names = new ArrayList<String>();
			for (String key : map.keySet()) {
				names.add(key);
			}
			for (int k = 0; k < names.size(); k++) {
				HSSFCell cellc = sheet.getRow(k + 3).getCell(32);
				cellc.setCellFormula("SUM(A" + (k + 4) + ":AF" + (k + 4) + ")");
				if (map.get(names.get(k)).size() == 0) {
					HSSFCell cell1 = sheet.getRow(k + 3).getCell(0);
					cell1.setCellValue(names.get(k));
				} else {
					for (int i = 1; i <= map.get(names.get(k)).size(); i++) {
						HSSFCell cell1 = sheet.getRow(k + 3).getCell(0);
						cell1.setCellValue(names.get(k));
						HSSFCell cell2 = sheet.getRow(k + 3).getCell(
								map.get(names.get(k)).get(i - 1));
						System.out.println("用户日期数量对应:[" + names.get(k) + "|||"
								+ map.get(names.get(k)).get(i - 1) + "]");
						/* 实现一人订多份统计 */
						cell2.setCellValue(odi.getCountByNameDate(
								names.get(k), map.get(names.get(k)).get(i - 1)) * 10);
					}
				}
			}
			List<String> list_table_word = makeTableWord();
			List<Integer> list_date_check = dateCheck();
			for (int j = 0; j < list_table_word.size(); j++) {
				String w_string = list_table_word.get(j);
				int i_date_check = list_date_check.get(j);
				/* 隐藏列统计值为0的列 */
				if (i_date_check == 0) {
					sheet.setColumnWidth(j + 1, 0);
				}
				/* 隐藏列统计值为0的列 end */
				HSSFCell celli = sheet.getRow(1).getCell(j + 1);
				celli.setCellFormula("IF(" + w_string + "3=" + i_date_check
						+ ",\"是\",\"否\")");

				HSSFCell cellr = sheet.getRow(2).getCell(j + 1);
				cellr.setCellFormula("SUM(" + w_string + "4:" + w_string
						+ (4 + names.size()) + ")");
			}

//			FileOutputStream out = new FileOutputStream("C:\\bbb.xls");
//			FileOutputStream out = new FileOutputStream("D:\\eclipse37_sisal\\wksp\\qzccbank_order\\WebContent\\"+TimeUtils.getNowTimeyLMInt(1)+".xls");

			String file_address   = rb.getString("file_address_product");
			FileOutputStream out = new FileOutputStream(file_address+TimeUtils.getNowTimeyLMInt(month)+".xls");
			
			wb.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<String> makeTableWord() {
		List<String> list_c = new ArrayList<String>();
		for (char i = 'B'; i <= 'Z'; i++) {
			String s = String.valueOf(i);
			list_c.add(s);
		}
		for (char i = 'A'; i <= 'G'; i++) {
			String s = "A" + String.valueOf(i);
			list_c.add(s);
		}
		return list_c;
	}

	public static List<Integer> dateCheck() {
		List<Integer> l_data = new ArrayList<Integer>();
		OrderDaoImpl odi = new OrderDaoImpl();
		odi.setTable_name(month);//选定月份
		for (int i = 1; i <= 31; i++) {
			l_data.add(odi.getCountByDate(i,3) * 10);
		}
		l_data.add(odi.getAllStaffCount() * 10);
		return l_data;
	}

	public static void main(String[] args) {
		Auto a = new Auto();
		a.setMonth(2);
		a.hand();
	}
}
