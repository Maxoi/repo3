package com.entity;

import java.util.List;

public class qzccbank_order_staf_cb {
	int staff_id;
	String staff_name;
	int count;
	List<Integer> datelist;
	List<String> dateliststr;
	int staff_fee_status;
	int staff_fee_type;
	public int getStaff_fee_status() {
		return staff_fee_status;
	}
	public void setStaff_fee_status(int staff_fee_status) {
		this.staff_fee_status = staff_fee_status;
	}
	public int getStaff_fee_type() {
		return staff_fee_type;
	}
	public void setStaff_fee_type(int staff_fee_type) {
		this.staff_fee_type = staff_fee_type;
	}
	public List<Integer> getDatelist() {
		return datelist;
	}
	public void setDatelist(List<Integer> datelist) {
		this.datelist = datelist;
	}
	public List<String> getDatelistStr() {
		return dateliststr;
	}
	public void setDatelistStr(List<String> dateliststr) {
		this.dateliststr = dateliststr;
	}
	public int getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(int staff_id) {
		this.staff_id = staff_id;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public qzccbank_order_staf_cb(int staff_id, String staff_name, int count,
			List<Integer> datelist, List<String> dateliststr,
			int staff_fee_status, int staff_fee_type) {
		super();
		this.staff_id = staff_id;
		this.staff_name = staff_name;
		this.count = count;
		this.datelist = datelist;
		this.dateliststr = dateliststr;
		this.staff_fee_status = staff_fee_status;
		this.staff_fee_type = staff_fee_type;
	}
	@Override
	public String toString() {
		return "qzccbank_order_staf_cb [staff_id=" + staff_id + ", staff_name="
				+ staff_name + ", count=" + count + ", datelist=" + datelist
				+ ", dateliststr=" + dateliststr + ", staff_fee_status="
				+ staff_fee_status + ", staff_fee_type=" + staff_fee_type + "]";
	}
	public qzccbank_order_staf_cb() {
		super();
	}
}
