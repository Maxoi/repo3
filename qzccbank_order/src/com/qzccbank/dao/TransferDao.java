package com.qzccbank.dao;

import java.sql.SQLException;
import java.util.List;

import com.entity.qzccbank_staff;
import com.entity.qzccbank_transfer_log;

public interface TransferDao{
	public List<qzccbank_transfer_log> getTransInfo(int staff_id,int query_type) throws SQLException;
	public int addTrans(qzccbank_transfer_log qtl);
}
