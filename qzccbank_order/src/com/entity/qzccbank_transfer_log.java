package com.entity;

public class qzccbank_transfer_log {
	private int id;
	private String staff_name;
	private int trans_alienator;
	private int trans_assignee;
	private int trans_time;
	private int trans_status;
	private String op_time;
	private int sort;
	private int op_status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTrans_alienator() {
		return trans_alienator;
	}
	public void setTrans_alienator(int trans_alienator) {
		this.trans_alienator = trans_alienator;
	}
	public int getTrans_assignee() {
		return trans_assignee;
	}
	public void setTrans_assignee(int trans_assignee) {
		this.trans_assignee = trans_assignee;
	}
	public int getTrans_time() {
		return trans_time;
	}
	public void setTrans_time(int trans_time) {
		this.trans_time = trans_time;
	}
	public int getTrans_status() {
		return trans_status;
	}
	public void setTrans_status(int trans_status) {
		this.trans_status = trans_status;
	}
	public String getOp_time() {
		return op_time;
	}
	public void setOp_time(String op_time) {
		this.op_time = op_time;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getOp_status() {
		return op_status;
	}
	public void setOp_status(int op_status) {
		this.op_status = op_status;
	}
	public String getStaff_name() {
		return staff_name;
	}
	@Override
	public String toString() {
		return "qzccbank_transfer_log [id=" + id + ", staff_name=" + staff_name
				+ ", trans_alienator=" + trans_alienator + ", trans_assignee="
				+ trans_assignee + ", trans_time=" + trans_time
				+ ", trans_status=" + trans_status + ", op_time=" + op_time
				+ ", sort=" + sort + ", op_status=" + op_status + "]";
	}
	public qzccbank_transfer_log(int id, String staff_name,
			int trans_alienator, int trans_assignee, int trans_time,
			int trans_status, String op_time, int sort, int op_status) {
		super();
		this.id = id;
		this.staff_name = staff_name;
		this.trans_alienator = trans_alienator;
		this.trans_assignee = trans_assignee;
		this.trans_time = trans_time;
		this.trans_status = trans_status;
		this.op_time = op_time;
		this.sort = sort;
		this.op_status = op_status;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public qzccbank_transfer_log() {
		super();
	}
	
}
