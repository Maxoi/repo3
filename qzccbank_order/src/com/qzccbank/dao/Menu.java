package com.qzccbank.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.entity.qzccbank_menulist;
import com.entity.qzccbank_order;
import com.entity.qzccbank_order_staf_cb;
import com.entity.qzccbank_staff;


public interface Menu{

	public String GetMenuList() throws SQLException;
}
