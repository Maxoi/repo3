package com.entity;

public class qzccbank_order {
	private String table_name="";
	int id;
	int staff_id;
	int staff_date;
	int order_status;
	int status;
	int sort;
	
	@Override
	public String toString() {
		return "qzccbank_order [table_name=" + table_name + ", id=" + id + ", staff_id=" + staff_id + ", staff_date="
				+ staff_date + ", order_status=" + order_status + ", status=" + status + ", sort=" + sort + "]";
	}
	public qzccbank_order() {
		super();
	}
	public qzccbank_order(String table_name) {
		super();
		this.table_name = table_name;
	}
	public qzccbank_order(String table_name, int id, int staff_id, int staff_date, int order_status, int status,
			int sort) {
		super();
		this.table_name = table_name;
		this.id = id;
		this.staff_id = staff_id;
		this.staff_date = staff_date;
		this.order_status = order_status;
		this.status = status;
		this.sort = sort;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(int staff_id) {
		this.staff_id = staff_id;
	}
	public int getStaff_date() {
		return staff_date;
	}
	public void setStaff_date(int staff_date) {
		this.staff_date = staff_date;
	}
	public int getOrder_status() {
		return order_status;
	}
	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
