package com.qzccbank.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.DateUtil;

import com.base.ResultSetHandler;
import com.entity.qzccbank_order;
import com.entity.qzccbank_order_staf_cb;
import com.entity.qzccbank_staff;
import com.util.db.JdbcTemplate;
import com.util.db.TimeUtils;

public class OrderDaoImpl implements Order{
	private JdbcTemplate jdbcTemplate;
	private  String table_name = "";
	private String nowtime = "";
	public OrderDaoImpl(){
		jdbcTemplate = new JdbcTemplate();
		Calendar now = Calendar.getInstance();
		table_name   = "qzccbank_"+now.get(Calendar.YEAR)+"_"+(now.get(Calendar.MONTH) + 1)+"_order";
		System.out.println("当前操作表名:["+table_name+"]");
		int year  = now.get(Calendar.YEAR);
		int month = (now.get(Calendar.MONTH) + 1);
		int day   = now.get(Calendar.DAY_OF_MONTH);
		int hour  = now.get(Calendar.HOUR_OF_DAY);
		int min   = now.get(Calendar.MINUTE);
		int sec   = now.get(Calendar.SECOND);
		nowtime = year + "-" + month + "-" + day + "|" + hour + ":" + min + ":" + sec + "---";
		System.out.println("nowtime:["+nowtime+"]");
	}
	
	public void setTable_name(int month) {
		Calendar now = Calendar.getInstance();
		table_name = "qzccbank_"+now.get(Calendar.YEAR)+"_"+month+"_order";
	}
	
	@Override
	public int save(qzccbank_order qo) {
		//使用Calendar
		String sql = "INSERT INTO "+table_name+"(staff_id,staff_date,order_status)values(?,?,?)";
		System.out.println(nowtime+nowtime+"执行SQL:["+sql+"]");
		return jdbcTemplate.update(sql, qo.getStaff_id(),qo.getStaff_date(),qo.getOrder_status());
	}
	public int update(qzccbank_order qo){
		String sql = "update "+table_name+" set order_status=? WHERE staff_id=? AND staff_date=?";
		System.out.println(nowtime+"执行SQL:["+sql+"]");
		return jdbcTemplate.update(sql,qo.getOrder_status(),qo.getStaff_id(),qo.getStaff_date());
	
	}
	
	public qzccbank_order findByDate(int date,int staff_id){
		String sql = "SELECT id,staff_id,staff_date,order_status,status,sort FROM "+table_name+" WHERE staff_date=? AND staff_id=?";
		System.out.println(nowtime+"执行SQL:["+sql+"]"+"参数值:["+date+","+staff_id+"]");
		return (qzccbank_order)jdbcTemplate.query(sql, new ResultSetHandler() {
			public Object doHandler(ResultSet rs) throws SQLException {
				qzccbank_order qo = null;
				if(rs.next()){
					qo = new qzccbank_order();
					qo.setId(rs.getInt(1));
					qo.setStaff_id(rs.getInt(2));
					qo.setStaff_date(rs.getInt(3));
					qo.setOrder_status(rs.getInt(4));
					qo.setStatus(rs.getInt(5));
					qo.setSort(rs.getInt(6));
				}
				return qo;
			}
		}, date,staff_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<qzccbank_order> findAllByStaff(int staff_id){
		String sql = "SELECT id,staff_id,staff_date,order_status,status,sort FROM "+table_name+" WHERE staff_id=? AND order_status!=0 ORDER BY staff_date ASC";
		System.out.println(nowtime+"执行SQL:["+sql+"]"+"参数值:["+staff_id+"]");
		return (List<qzccbank_order>)jdbcTemplate.query(sql, new ResultSetHandler() {
			@Override
			public Object doHandler(ResultSet rs) throws SQLException {
				List<qzccbank_order> list = new ArrayList<qzccbank_order>();
				qzccbank_order qo = null;
				while(rs.next()){
					qo = new qzccbank_order();
					qo.setId(rs.getInt(1));
					qo.setStaff_id(rs.getInt(2));
					qo.setStaff_date(rs.getInt(3));
					qo.setOrder_status(rs.getInt(4));
					qo.setStatus(rs.getInt(5));
					qo.setSort(rs.getInt(6));
					list.add(qo);
				}
				return list;
			}
		},staff_id);
	}
	
	//根据工号统计当月次数
	public int getCountByStaff(int staff_id){
		String sql = "SELECT sum(order_status) FROM "+table_name+" WHERE staff_id=?";
		System.out.println(nowtime+"执行SQL:["+sql+"]"+"参数值:["+staff_id+"]");
		return (Integer)jdbcTemplate.query(sql, new ResultSetHandler() {
			@Override
			public Object doHandler(ResultSet rs) throws SQLException {
				int count = 0;
				if(rs.next()){
					count = rs.getInt(1);
				}
				return count;
			}
		},staff_id);
	}
	
	//统计当月订餐情况(次数)
	@SuppressWarnings("unchecked")
	public List<qzccbank_order_staf_cb> getAllCount(){
		String sql = "SELECT a.staff_id,b.staff_name,sum(a.order_status) FROM "+table_name+" a,qzccbank_staff b WHERE a.staff_id=b.staff_id GROUP BY a.staff_id";
		System.out.println(nowtime+"执行SQL:["+sql+"]");
		return (List<qzccbank_order_staf_cb>)jdbcTemplate.query(sql, new ResultSetHandler() {
			public Object doHandler(ResultSet rs) throws SQLException {
				List<qzccbank_order_staf_cb> list = new ArrayList<qzccbank_order_staf_cb>();
				qzccbank_order_staf_cb qosc = null;
				while(rs.next()){
					qosc = new qzccbank_order_staf_cb();
					qosc.setStaff_id(rs.getInt(1));
					qosc.setStaff_name(rs.getString(2));
					qosc.setCount(rs.getInt(3));
					list.add(qosc);
				}
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<qzccbank_order_staf_cb> getAllDate(final int staff_id){
		String sql = "SELECT staff_date,order_status FROM "+table_name+" WHERE staff_id=? and order_status>0 ORDER BY staff_date ASC";
		System.out.println(nowtime+"执行SQL:["+sql+"]"+"参数值:["+staff_id+"]");
		return (List<qzccbank_order_staf_cb>)jdbcTemplate.query(sql, new ResultSetHandler() {
			public Object doHandler(ResultSet rs) throws SQLException {
				List<Integer> list = new ArrayList<Integer>();
				List<String> liststr = new ArrayList<String>();
				List<qzccbank_order_staf_cb> l_qosc = new ArrayList<qzccbank_order_staf_cb>();
				qzccbank_order_staf_cb qosc = null;
				while(rs.next()){
					if(rs.getInt(2) > 1){
						liststr.add(String.valueOf(rs.getInt(1))+"X"+String.valueOf(rs.getInt(2)));
					}else{
						liststr.add(String.valueOf(rs.getInt(1)));
					}
					
					list.add(rs.getInt(1));
				}
				qosc = new qzccbank_order_staf_cb();
				qosc.setDatelist(list);
				qosc.setDatelistStr(liststr);
				qosc.setStaff_id(staff_id);
				qosc.setStaff_name(getNameByStaffId(staff_id));
				l_qosc.add(qosc);
				return l_qosc;
			}
		},staff_id);
	}
	

	@SuppressWarnings("unchecked")
	public List<qzccbank_order_staf_cb> getAllDate_1(final int staff_id){
		Calendar now = Calendar.getInstance();
		String table_name_2   = "qzccbank_"+now.get(Calendar.YEAR)+"_"+(now.get(Calendar.MONTH))+"_order";
		String sql = "SELECT staff_date FROM "+table_name+" WHERE staff_id=? and order_status!=0 ORDER BY staff_date ASC";
		System.out.println(nowtime+"执行SQL:["+sql+"]"+"参数值:["+staff_id+"]");
		return (List<qzccbank_order_staf_cb>)jdbcTemplate.query(sql, new ResultSetHandler() {
			public Object doHandler(ResultSet rs) throws SQLException {
				List<Integer> list = new ArrayList<Integer>();
				List<qzccbank_order_staf_cb> l_qosc = new ArrayList<qzccbank_order_staf_cb>();
				qzccbank_order_staf_cb qosc = null;
				while(rs.next()){
					list.add(rs.getInt(1));
				}
				System.out.println(list);
				qosc = new qzccbank_order_staf_cb();
				qosc.setDatelist(list);
				qosc.setStaff_id(staff_id);
				qosc.setStaff_name(getNameByStaffId(staff_id));
				l_qosc.add(qosc);
				return l_qosc;
			}
		},staff_id);
	}
	

	public int getCountByNameDate(String staff_name,int staff_date){
		String sql = "SELECT a.order_status FROM "+table_name+" a,qzccbank_staff b WHERE a.staff_id=b.staff_id AND b.staff_name=? AND a.staff_date=?";
		System.out.println(nowtime+"执行SQL:["+sql+"]"+"参数值:["+staff_name+"],["+staff_date+"]");
		return (Integer)jdbcTemplate.query(sql, new ResultSetHandler() {
			public Object doHandler(ResultSet rs) throws SQLException {
				int allSum = 0;
				if(rs.next()){
					allSum = rs.getInt(1);
				}
				return allSum;
			}
		},staff_name,staff_date);
	}
	
	/* 根据工号查询上月缴费情况 */
	@SuppressWarnings("unchecked")
	public qzccbank_order_staf_cb getFeeByStaff(final int staff_id){
		Calendar now = Calendar.getInstance();
		int y = 0;
		int m = 0;
		if(now.get(Calendar.MONTH) == 0){
			y = now.get(Calendar.YEAR)-1;
			m = 12;
		}else{
			y = now.get(Calendar.YEAR);
			m = now.get(Calendar.MONTH);
		}
		String prev_table_name = "qzccbank_"+y+"_"+(m)+"_order";
		System.out.println("FFF:["+prev_table_name+"]");
		String sql = "SELECT b.staff_id,b.staff_name,SUM(a.order_status),b.staff_fee_status,b.staff_fee_type FROM "+prev_table_name+" a,qzccbank_staff b WHERE a.staff_id =b.staff_id AND a.staff_id=?";
		System.out.println(nowtime+"执行SQL:["+sql+"]"+"参数值:["+staff_id+"]");
		return (qzccbank_order_staf_cb)jdbcTemplate.query(sql, new ResultSetHandler() {
			public Object doHandler(ResultSet rs) throws SQLException {
				qzccbank_order_staf_cb qosc = new qzccbank_order_staf_cb();
				if(rs.next()){
					qosc.setStaff_id(rs.getInt(1));
					qosc.setStaff_name(rs.getString(2));
					qosc.setCount(rs.getInt(3));
					qosc.setStaff_fee_status(rs.getInt(4));
					qosc.setStaff_fee_type(rs.getInt(5));
				}
				return qosc;
			}
		},staff_id);
	}
	//通过工号找姓名
	public String getNameByStaffId(int staff_id){
		String sql = "SELECT staff_name FROM qzccbank_staff WHERE staff_id=?";
		System.out.println(nowtime+"执行SQL:["+sql+"]"+"参数值:["+staff_id+"]");
		return (String)jdbcTemplate.query(sql, new ResultSetHandler() {
			public Object doHandler(ResultSet rs) throws SQLException {
				String staff_name = "";
				if(rs.next()){
					staff_name = rs.getString(1);
				}
				return staff_name;
			}
		},staff_id);
	}
	
	//统计所有人定饭量
	public int getAllStaffCount(){
		String sql = "SELECT SUM(order_status) FROM "+table_name;
		System.out.println(nowtime+"执行SQL:["+sql+"]");
		return (Integer)jdbcTemplate.query(sql, new ResultSetHandler() {
			@Override
			public Object doHandler(ResultSet rs) throws SQLException {
				int count = 0;
				if(rs.next()){
					count = rs.getInt(1);
				}
				return count;
			}
		});
	}

	/* 统计当日总量 */
	public int getDateCount(int staff_type){
		//staff_type 如果为3查全部
		String sql = "";
		if(staff_type == 3){
			sql = "SELECT SUM(order_status) as sum FROM "+table_name+" a LEFT JOIN qzccbank_staff b on a.staff_id=b.staff_id WHERE  staff_date="+TimeUtils.getDay();	
		}else{
			sql = "SELECT SUM(order_status) as sum FROM "+table_name+" a LEFT JOIN qzccbank_staff b on a.staff_id=b.staff_id WHERE b.staff_type="+staff_type+" and staff_date="+TimeUtils.getDay();
		}
		
		System.out.println(nowtime+"执行SQL:["+sql+"]");
		return (Integer)jdbcTemplate.query(sql, new ResultSetHandler() {
			@Override
			public Object doHandler(ResultSet rs) throws SQLException {
				int all_count = 0;
				if(rs.next()){
					all_count = rs.getInt(1);
				}
				return all_count;
			}
		});

	}
	
	/* 统计某日订餐量 */
	
	public int getCountByDate(int staff_date,int staff_type){
		String sql = "";
		if(staff_type == 3){
			sql = "SELECT SUM(order_status) FROM "+table_name+" a LEFT JOIN qzccbank_staff b on a.staff_id=b.staff_id WHERE  staff_date=?";	
		}else{
			sql = "SELECT SUM(order_status) FROM "+table_name+" a LEFT JOIN qzccbank_staff b on a.staff_id=b.staff_id WHERE b.staff_type="+staff_type+" and staff_date=?";
		}
		System.out.println(nowtime+"执行SQL:["+sql+"]"+"参数值:["+staff_date+"]");
		return (Integer)jdbcTemplate.query(sql, new ResultSetHandler() {
			@Override
			public Object doHandler(ResultSet rs) throws SQLException {
				int all_count = 0;
				if(rs.next()){
					all_count = rs.getInt(1);
				}
				return all_count;
			}
		},staff_date);
	}
	//logical delete
	/*public void delete(int id) throws SQLException {	
		String sql = " delete FROM friend_tab WHERE id=?";
		jdbcTemplate.update(sql,id);
	}*/
	
	public static void main(String[] args) throws SQLException {
		OrderDaoImpl od = new OrderDaoImpl();
//		qzccbank_order q = new qzccbank_order();
//		q.setStaff_id(23);
//		q.setStaff_date(26);
//		q.setOrder_status(1);
//		System.out.println(od.save(q));
//		System.out.println("aaa"+od.findByDate(28,3058));
//		System.out.println(od.update(q));
//		List<qzccbank_order> list = od.findAllByStaff(23);
//		System.out.println(list);
////		for (qzccbank_order qzccbank_order : list) {
////			System.out.println(list.size());
////		}
		
//		for(int i = 0 ; i < list.size() ; i++) {
//			System.out.println(list.get(i));
//		}
//		
		/*for(int i = 0 ; i < list.size() ; i++) {
			  System.out.println(list.get(i).getStaff_date());
		}*/
		
//		System.out.println(od.getCountByStaff(3058));
		/*System.out.println(od.getAllCount());
		List<qzccbank_order_staf_cb> list2 = od.getAllCount();
		for (int i = 0; i < list2.size(); i++) {
			System.out.println(list2.get(i).getStaff_name());
		}*/
//		System.out.println(od.getNameByStaffId(3059));
//		System.out.println(od.getAllDate(3058));
//		System.out.println(od.getAllStaffCount());
//		System.out.println(od.getDateCount());
//		System.out.println(od.getCountByStaff(3058));
		/*StaffDaoImpl sdi = new StaffDaoImpl();
		System.out.println(sdi.findAll());
		List<qzccbank_staff> l  = sdi.findAll();
		qzccbank_order qo = null;
		for (int i = 0; i < l.size(); i++) {
			qo = new qzccbank_order();
			qo.setStaff_id(l.get(i).getStaff_id());
			qo.setStaff_date(3);
			qo.setOrder_status(1);
			od.save(qo);
		}*/
		
		/*OrderDaoImpl odi = new OrderDaoImpl();
		qzccbank_order a = odi.findByDate(3,3058);
		qzccbank_order b = odi.findByDate(3,56);
		System.out.println(a.getOrder_status());
		System.out.println("|---------------|");
		System.out.println(b.getOrder_status());
		System.out.println();*/

	}
}
