package com.qzccbank.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.entity.qzccbank_order;
import com.entity.qzccbank_order_staf_cb;


public interface Order{

	public int save(qzccbank_order qo) throws SQLException;
	public qzccbank_order findByDate(int date,int staff_id) throws SQLException;
	public int update(qzccbank_order qo) throws SQLException;
	public List<qzccbank_order> findAllByStaff(int staff_id) throws SQLException;
	public int getCountByStaff(int staff_id) throws SQLException;
	public List<qzccbank_order_staf_cb> getAllCount() throws SQLException;
	public List<qzccbank_order_staf_cb> getAllDate(int staff_id) throws SQLException;
}
