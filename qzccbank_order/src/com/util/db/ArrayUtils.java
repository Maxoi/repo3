package com.util.db;

public class ArrayUtils {
	private ArrayUtils(){}
	
	public static String getTranStatus(int tran_status){
		String str[];
        str = new String[4];
        str[1] = "接受准让";
        str[2] = "准让中";
        str[3] = "拒绝转让";
        return str[tran_status];
	}
	
	public static String getOpStatus(int tran_status){
		String str[];
        str = new String[3];
        str[0] = "无效";
        str[1] = "有效";
        return str[tran_status];
	}
	public static void main(String[] args) {
		System.out.println(getOpStatus(1));
	}
}
