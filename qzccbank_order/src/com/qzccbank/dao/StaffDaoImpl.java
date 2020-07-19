package com.qzccbank.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.base.ResultSetHandler;
import com.entity.qzccbank_staff;
import com.util.db.JdbcTemplate;

public class StaffDaoImpl implements StaffDao{
	private JdbcTemplate jdbcTemplate;
	public StaffDaoImpl(){
		jdbcTemplate = new JdbcTemplate();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<qzccbank_staff> findAll() throws SQLException {
		String sql = "SELECT id,staff_id,staff_name,staff_password,staff_status,sort FROM qzccbank_staff ORDER BY staff_type DESC,sort ASC";
		return (List<qzccbank_staff>)jdbcTemplate.query(sql, new ResultSetHandler() {
			@Override
			public Object doHandler(ResultSet rs) throws SQLException {
				List<qzccbank_staff> list = new ArrayList<qzccbank_staff>();
				qzccbank_staff qs = null;
				while(rs.next()){
					qs = new qzccbank_staff();
					qs.setId(rs.getInt(1));
					qs.setStaff_id(rs.getInt(2));
					qs.setStaff_name(rs.getString(3));
					qs.setStaff_password(rs.getString(4));
					qs.setStaff_type(rs.getInt(5));
					qs.setSort(rs.getInt(6));
					list.add(qs);
				}
				return list;
			}
		});
	}
	
	//判断是否需要重设密码(密码字段是否为空)
	public boolean checkResetPass(int staff_id){
		String sql = "SELECT staff_id,staff_password FROM qzccbank_staff WHERE staff_id=?";
		return (Boolean)jdbcTemplate.query(sql, new ResultSetHandler() {
			@Override
			public Object doHandler(ResultSet rs) throws SQLException {
				String isPassNull = "";
				int staff_ids = 0;
				if(rs.next()){
					staff_ids = rs.getInt(1);
					isPassNull = rs.getString(2);
				}
				if(isPassNull.length() > 0)
					return false;
				else if(staff_ids > 0)
					return true;
				else
					return false;
				
			}
		}, staff_id);
	}
	
	public qzccbank_staff checkUserPass(int staff_id,String pass){
		String sql = "SELECT id,staff_id,staff_name,staff_password,staff_status FROM qzccbank_staff WHERE staff_id=? and staff_password=?";
		return (qzccbank_staff)jdbcTemplate.query(sql, new ResultSetHandler() {
			@Override
			public Object doHandler(ResultSet rs) throws SQLException {
				qzccbank_staff qs = null;
				if(rs.next()){
					qs = new qzccbank_staff();
					qs.setId(rs.getInt(1));
					qs.setStaff_id(rs.getInt(2));
					qs.setStaff_name(rs.getString(3));
					qs.setStaff_password(rs.getString(4));
					qs.setStaff_status(rs.getInt(5));
				}
				return qs;
			}
		}, staff_id,pass);
	}
	
	//根据工号内部查询员工信息(不需验密)
	public qzccbank_staff getStaffById(int staff_id){
		String sql = "SELECT id,staff_id,staff_name,staff_status,staff_fee_status,staff_fee_type FROM qzccbank_staff WHERE staff_id=?";
		return (qzccbank_staff)jdbcTemplate.query(sql, new ResultSetHandler() {
			@Override
			public Object doHandler(ResultSet rs) throws SQLException {
				qzccbank_staff qs = null;
				if(rs.next()){
					qs = new qzccbank_staff();
					qs.setId(rs.getInt(1));
					qs.setStaff_id(rs.getInt(2));
					qs.setStaff_name(rs.getString(3));
					qs.setStaff_status(rs.getInt(4));
					qs.setStaff_fee_status(rs.getInt(5));
					qs.setStaff_fee_type(rs.getInt(6));
				}
				return qs;
			}
		}, staff_id);
	}
	
	@Override
	public qzccbank_staff findByStaffid(int staff_id)  {
		String sql = "SELECT id,staff_id,staff_name,staff_password,staff_status,sort FROM qzccbank_staff where staff_id=?";
		return (qzccbank_staff)jdbcTemplate.query(sql, new ResultSetHandler() {
			@Override
			public Object doHandler(ResultSet rs) throws SQLException {

				qzccbank_staff qs = null;
				if(rs.next()){
					qs = new qzccbank_staff();
					qs.setId(rs.getInt(1));
					qs.setStaff_id(rs.getInt(2));
					qs.setStaff_name(rs.getString(3));
					qs.setStaff_password(rs.getString(4));
					qs.setStaff_status(rs.getInt(5));
					qs.setSort(rs.getInt(6));
				}
				return qs;
			}
		}, staff_id);
	}
	
	/* 员工总数 */
	public int getAllStaffCount(){
		String sql = "SELECT COUNT(*) as sum FROM qzccbank_staff";
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
	
	/* 获得最大员工排序 */
	public int getMaxStaffSort(){
		String sql = "SELECT MAX(sort) as max_sort FROM qzccbank_staff";
		return (Integer)jdbcTemplate.query(sql, new ResultSetHandler() {
			@Override
			public Object doHandler(ResultSet rs) throws SQLException {
				int sort = 0;
				if(rs.next()){
					sort = rs.getInt(1);
				}
				return sort;
			}
		});
	}
	/* 获得ID最大的行内员工姓名 */
	public String getMaxInnerStaffName(){
		String sql = "SELECT staff_name FROM qzccbank_staff where staff_type=1 order by id desc LIMIT 0,1";
		return (String)jdbcTemplate.query(sql, new ResultSetHandler() {
			@Override
			public Object doHandler(ResultSet rs) throws SQLException {
				String StaffName = "";
				if(rs.next()){
					StaffName = rs.getString(1);
				}
				return StaffName;
			}
		});
		
	}
	/* 密码重置 */

	public int resetPassword(qzccbank_staff qs) {
		String sql = "update qzccbank_staff set staff_password=? where staff_id=?";
		return jdbcTemplate.update(sql,qs.getStaff_password(),qs.getStaff_id());
	}
	
	/* 添加员工 */
	public int addStaff(qzccbank_staff qs){
		String sql = "insert into qzccbank_staff(staff_id,staff_name,staff_password,staff_type,sort)values(?,?,?,?,?)";
		return jdbcTemplate.update(sql, qs.getStaff_id(),qs.getStaff_name(),qs.getStaff_password(),qs.getStaff_type(),qs.getSort());
	}
	
	/* 冻结与解冻员工 */
	public int lockStaff(int staff_status,int staff_id){
		String sql = "UPDATE qzccbank_staff set staff_status=?";
		if(staff_id > 0){
			 sql += " WHERE staff_id=?";
			 return jdbcTemplate.update(sql, staff_status,staff_id);
		}else{
			 return jdbcTemplate.update(sql, staff_status);
		}
	}
	

	/* 欠费解冻员工 */
	public int unlockStaffStaffFeeStatus(int staff_id,String staff_name,int fee_type){
		String sql = "UPDATE qzccbank_staff SET staff_fee_status=1,staff_fee_type="+fee_type+" WHERE staff_id=? or staff_name=?";
		System.out.println("StaffDaoImpl执行SQL:["+sql+"]");
		return jdbcTemplate.update(sql, staff_id,staff_name);
	}
	
	public static void main(String[] args)  {
		StaffDaoImpl dao = new StaffDaoImpl();
		//登录案例
		//System.out.println(dao.checkUserPass(3058,"12"));
		//密码重置判断案例
//		System.out.println(dao.checkResetPass(3058));
		/*qzccbank_staff qs = new qzccbank_staff();
		qs.setStaff_id(3058);
		qs.setStaff_password("123");
		System.out.println(dao.resetPassword(qs)+1);*/
//		System.out.println(dao.lockStaff(0, 3058));
		System.out.println(dao.getAllStaffCount());
//		System.out.println();
	}

}
