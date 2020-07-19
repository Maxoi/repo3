package com.qzccbank.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.base.ResultSetHandler;
import com.entity.qzccbank_staff;
import com.entity.qzccbank_transfer_log;
import com.util.db.JdbcTemplate;
import com.util.db.TimeUtils;

public class TransferDaoImpl implements TransferDao {
	private JdbcTemplate jdbcTemplate;

	public TransferDaoImpl() {
		jdbcTemplate = new JdbcTemplate();
	}

	// 获取日志
	@SuppressWarnings("unchecked")
	public List<qzccbank_transfer_log> getTransInfo(int staff_id, int query_type)
			throws SQLException {

		String sql = "SELECT a.id,b.staff_name,a.trans_alienator,a.trans_assignee,a.trans_time,a.trans_status,a.op_time,a.op_status,a.sort FROM qzccbank_transfer_log a,qzccbank_staff b ";
		if (query_type == 1) {
			// 转让人
			sql += "WHERE trans_alienator=? ";
			sql += "AND a.trans_assignee=b.staff_id ";
		} else if (query_type == 2) {
			// 受让人
			sql += "WHERE trans_assignee=? ";
			sql += "AND a.trans_alienator=b.staff_id ";
		}
		sql += "AND op_status = 1 AND trans_time = ? ORDER BY op_time DESC";
		System.out.println("TransferDaoImpl执行SQL:[" + sql + "]");
		return (List<qzccbank_transfer_log>) jdbcTemplate.query(sql,
				new ResultSetHandler() {
					@Override
					public Object doHandler(ResultSet rs) throws SQLException {
						List<qzccbank_transfer_log> list = new ArrayList<qzccbank_transfer_log>();
						qzccbank_transfer_log qtl = null;
						while (rs.next()) {
							qtl = new qzccbank_transfer_log();
							qtl.setId(rs.getInt(1));
							qtl.setStaff_name(rs.getString(2));
							qtl.setTrans_alienator(rs.getInt(3));
							qtl.setTrans_assignee(rs.getInt(4));
							qtl.setTrans_time(rs.getInt(5));
							qtl.setTrans_status(rs.getInt(6));
							qtl.setOp_time(rs.getString(7));
							qtl.setOp_status(rs.getInt(8));
							qtl.setSort(rs.getInt(9));
							list.add(qtl);
						}
						return list;
					}
				}, staff_id,TimeUtils.getNowTimeyMdInt());
	}

	// 转让操作(准让方)
	public int addTrans(qzccbank_transfer_log qtl) {
		System.out.println("是否存在:["
				+ checkTranRelation(qtl.getTrans_alienator(),
						qtl.getTrans_time()) + "]");
		qzccbank_transfer_log oldTransferLogobj = checkTranRelation(
				qtl.getTrans_alienator(), qtl.getTrans_time());
		if (oldTransferLogobj != null) {
			// 已存在数据进行数据更新
			int update_result = 0;
			String sql = "UPDATE qzccbank_transfer_log set trans_status=0,op_time=? where op_time=?  AND trans_status<>0";
			update_result = jdbcTemplate.update(sql, qtl.getOp_time(),
					oldTransferLogobj.getOp_time());
			System.out.println("旧数据更新结果:[" + update_result + "]");
		}
		String sql = "INSERT INTO qzccbank_transfer_log(trans_alienator,trans_assignee,trans_time,trans_status,op_time,op_status,sort)values(?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql, qtl.getTrans_alienator(),
				qtl.getTrans_assignee(), qtl.getTrans_time(),
				qtl.getTrans_status(), qtl.getOp_time(), qtl.getOp_status(),
				qtl.getSort());
	}

	public qzccbank_transfer_log checkTranRelation(int trans_alienator_id,
			int trans_time) {
		// 根据转让人,转让日期返回转让状态判断转让关系是否已经存在,返回转让时间
		String sql = "SELECT id,trans_alienator,trans_assignee,trans_time,trans_status,op_time,op_status,sort FROM qzccbank_transfer_log WHERE trans_alienator=? AND trans_time=? AND op_status=1";
		return (qzccbank_transfer_log) jdbcTemplate.query(sql,
				new ResultSetHandler() {
					@Override
					public Object doHandler(ResultSet rs) throws SQLException {
						qzccbank_transfer_log qtl = null;
						while (rs.next()) {
							qtl = new qzccbank_transfer_log();
							qtl.setId(rs.getInt(1));
							qtl.setTrans_alienator(rs.getInt(2));
							qtl.setTrans_assignee(rs.getInt(3));
							qtl.setTrans_time(rs.getInt(4));
							qtl.setTrans_status(rs.getInt(5));
							qtl.setOp_time(rs.getString(6));
							qtl.setOp_status(rs.getInt(7));
							qtl.setSort(rs.getInt(8));
						}
						return qtl;
					}
				}, trans_alienator_id, trans_time);
	}

	// 根据受让人和时间查找转让人,为更新转让人做准备
	public qzccbank_transfer_log getTransalienator(int trans_assignee_id,
			int trans_time) {
		String sql = "SELECT id,trans_alienator,trans_assignee,trans_time,trans_status,op_time,op_status,sort FROM qzccbank_transfer_log WHERE trans_assignee=? AND trans_time=? AND trans_status=2";
		return (qzccbank_transfer_log) jdbcTemplate.query(sql,
				new ResultSetHandler() {
					@Override
					public Object doHandler(ResultSet rs) throws SQLException {
						qzccbank_transfer_log qtl = null;
						while (rs.next()) {
							qtl = new qzccbank_transfer_log();
							qtl.setId(rs.getInt(1));
							qtl.setTrans_alienator(rs.getInt(2));
							qtl.setTrans_assignee(rs.getInt(3));
							qtl.setTrans_time(rs.getInt(4));
							qtl.setTrans_status(rs.getInt(5));
							qtl.setOp_time(rs.getString(6));
							qtl.setOp_status(rs.getInt(7));
							qtl.setSort(rs.getInt(8));
						}
						return qtl;
					}
				}, trans_assignee_id, trans_time);
	}
	//修改受让人状态
	public int updateTranStatus(int staff_id,int time,int trans_status){
		int update_result = 0;
		String sql = "UPDATE qzccbank_transfer_log set trans_status=? WHERE trans_assignee=? AND trans_time=?  AND trans_status=2";
		update_result = jdbcTemplate.update(sql, trans_status,staff_id,time);
		return update_result;
	}
	//修改转让人状态
	public int updateTranStatusAT(int staff_id,int time,int trans_status){
		int update_result = 0;
		String sql = "UPDATE qzccbank_transfer_log set trans_status=? WHERE trans_alienator=? AND trans_time=?  AND trans_status=2";
		update_result = jdbcTemplate.update(sql, trans_status,staff_id,time);
		return update_result;
	}
	// 查找转(受)让记录
	/*
	 * public boolean checkResetPass(int staff_id){ String sql =
	 * "SELECT staff_id,staff_password FROM qzccbank_staff WHERE staff_id=?";
	 * return (Boolean)jdbcTemplate.query(sql, new ResultSetHandler() {
	 * 
	 * @Override public Object doHandler(ResultSet rs) throws SQLException {
	 * String isPassNull = ""; int staff_ids = 0; if(rs.next()){ staff_ids =
	 * rs.getInt(1); isPassNull = rs.getString(2); } if(isPassNull.length() > 0)
	 * return false; else if(staff_ids > 0) return true; else return false;
	 * 
	 * } }, staff_id); }
	 */
	// 查找受让记录
	/**/

	public static void main(String[] args) {
		TransferDaoImpl dao = new TransferDaoImpl();
		/* 转 */
		/*
		 * qzccbank_transfer_log qtl = new qzccbank_transfer_log();
		 * qtl.setTrans_alienator(60); qtl.setTrans_assignee(3058);
		 * qtl.setTrans_time(20180421); qtl.setOp_time("20180419135600");
		 * qtl.setTrans_status(2); qtl.setSort(0); qtl.setOp_status(0);
		 * System.out.println("转让日志处理结果:"+dao.addTrans(qtl));
		 */

		try {
			System.out.println(dao.getTransInfo(3058, 2));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
