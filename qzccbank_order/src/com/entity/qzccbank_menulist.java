package com.entity;

public class qzccbank_menulist {
	private String table_name="";
	int id;
	int menu_id;
	String menu_name;
	String menu_img;
	int status;
	int sort;
	@Override
	public String toString() {
		return "qzccbank_menulist [table_name=" + table_name + ", id=" + id
				+ ", menu_id=" + menu_id + ", menu_name=" + menu_name
				+ ", menu_img=" + menu_img + ", status=" + status + ", sort="
				+ sort + "]";
	}
	public qzccbank_menulist(String table_name, int id, int menu_id,
			String menu_name, String menu_img, int status, int sort) {
		super();
		this.table_name = table_name;
		this.id = id;
		this.menu_id = menu_id;
		this.menu_name = menu_name;
		this.menu_img = menu_img;
		this.status = status;
		this.sort = sort;
	}
	public qzccbank_menulist() {
		super();
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
	public int getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public String getMenu_img() {
		return menu_img;
	}
	public void setMenu_img(String menu_img) {
		this.menu_img = menu_img;
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
