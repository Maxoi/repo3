package com.qzccbank.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.base.ResultSetHandler;
import com.entity.qzccbank_order;
import com.entity.qzccbank_order_menu;
import com.util.db.JdbcTemplate;

public class MenuDaoImpl implements Menu{
	private JdbcTemplate jdbcTemplate;
	private String table_name = "";
	private String nowtime = "";
	public MenuDaoImpl(){
		jdbcTemplate = new JdbcTemplate();
		Calendar now = Calendar.getInstance();
		table_name   = "qzccbank_"+now.get(Calendar.YEAR)+"_"+(now.get(Calendar.MONTH) + 1)+"_menu";
		System.out.println("当前操作表名:["+table_name+"]");
	}
	@Override
	public String GetMenuList() {
			String sql = "SELECT menu_name FROM qzccbank_menulist WHERE menu_id=100";
			System.out.println(nowtime+"执行SQL:["+sql+"]");
			return (String)jdbcTemplate.query(sql, new ResultSetHandler() {
				public Object doHandler(ResultSet rs) throws SQLException {
					String menu_list = "";
					if(rs.next()){
						menu_list = rs.getString(1);
					}
					return menu_list;
				}
			});
	}

	@SuppressWarnings("unchecked")
	public List<qzccbank_order_menu> GetMenuListByDate(int date) {
		String sql = "SELECT b.menu_id,a.menu_name,a.menu_img from  "+table_name+" b  LEFT JOIN qzccbank_menulist a on a.menu_id=b.menu_id  where b.menu_date=?";
		System.out.println(nowtime+"执行SQL:["+sql+"]"+"参数值:["+date+"]");
		return (List<qzccbank_order_menu>)jdbcTemplate.query(sql, new ResultSetHandler() {
		public Object doHandler(ResultSet rs) throws SQLException {
			qzccbank_order_menu om = null;
			List<qzccbank_order_menu> list = new ArrayList<qzccbank_order_menu>();
			while(rs.next()){
				om = new qzccbank_order_menu();
				om.setMenu_id(rs.getInt(1));
				om.setMenu_name(rs.getString(2));
				om.setMenu_img(rs.getString(3));
				list.add(om);
			}
			return list;
		}
	}, date);
	}
	
	
	
}
