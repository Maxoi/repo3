package com.qzccbank.dao;

import java.sql.SQLException;
import java.util.List;

import com.entity.qzccbank_staff;

public interface StaffDao{
	public List<qzccbank_staff> findAll() throws SQLException;
	public boolean checkResetPass(int staff_id);
	public qzccbank_staff checkUserPass(int staff_id,String pass);
	public qzccbank_staff findByStaffid(int staff_id);
	public int resetPassword(qzccbank_staff qs);
	public int addStaff(qzccbank_staff qs);
	public int lockStaff(int staff_status,int staff_id);
	public int getAllStaffCount();
}
