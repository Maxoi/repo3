package com.entity;

import java.util.List;

public class qzccbank_order_menu {
	int menu_id;
	String menu_name;
	String menu_img;
	int menu_date;
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
	public int getMenu_date() {
		return menu_date;
	}
	public void setMenu_date(int menu_date) {
		this.menu_date = menu_date;
	}
	@Override
	public String toString() {
		return "qzccbank_order_menu [menu_id=" + menu_id + ", menu_name="
				+ menu_name + ", menu_img=" + menu_img + ", menu_date="
				+ menu_date + "]";
	}
	public qzccbank_order_menu(int menu_id, String menu_name, String menu_img,
			int menu_date) {
		super();
		this.menu_id = menu_id;
		this.menu_name = menu_name;
		this.menu_img = menu_img;
		this.menu_date = menu_date;
	}
	public qzccbank_order_menu() {
		super();
	}
	
}
