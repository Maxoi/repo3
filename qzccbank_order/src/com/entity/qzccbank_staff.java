package com.entity;

public class qzccbank_staff {
	private int id;
	private int staff_id;
	private String staff_name;
	private String staff_password;
	private int staff_status;
	private int sort;
	private int staff_fee_status;
	private int staff_fee_type;
	private int staff_type;
	@Override
	public String toString() {
		return "qzccbank_staff [id=" + id + ", staff_id=" + staff_id
				+ ", staff_name=" + staff_name + ", staff_password="
				+ staff_password + ", staff_status=" + staff_status + ", sort="
				+ sort + ", staff_fee_status=" + staff_fee_status
				+ ", staff_fee_type=" + staff_fee_type + ", staff_type="
				+ staff_type + "]";
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
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getStaff_password() {
		return staff_password;
	}
	public void setStaff_password(String staff_password) {
		this.staff_password = staff_password;
	}
	public int getStaff_status() {
		return staff_status;
	}
	public void setStaff_status(int staff_status) {
		this.staff_status = staff_status;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
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
	public int getStaff_type() {
		return staff_type;
	}
	public void setStaff_type(int staff_type) {
		this.staff_type = staff_type;
	}
	public qzccbank_staff(int id, int staff_id, String staff_name,
			String staff_password, int staff_status, int sort,
			int staff_fee_status, int staff_fee_type, int staff_type) {
		super();
		this.id = id;
		this.staff_id = staff_id;
		this.staff_name = staff_name;
		this.staff_password = staff_password;
		this.staff_status = staff_status;
		this.sort = sort;
		this.staff_fee_status = staff_fee_status;
		this.staff_fee_type = staff_fee_type;
		this.staff_type = staff_type;
	}
	public qzccbank_staff() {
		super();
	}
	
	
}
