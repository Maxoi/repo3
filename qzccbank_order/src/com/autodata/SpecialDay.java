package com.autodata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

public class SpecialDay {
	private static ResourceBundle rb = ResourceBundle.getBundle("com.util.db.db-config");
	public static String readToString(String fileName) {  
        String encoding = "UTF-8";  
        File file = new File(fileName);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
            return null;  
        }  
    }
	public static void main(String[] args) {
		String file_address   = rb.getString("file_address_dev")+"day.data";
		String whole_content = SpecialDay.readToString(file_address);
		
		String[] rowBuf = whole_content.split("\r\n");
		String[] colBuf = new String[8];
		for (int i = 0; i < rowBuf.length; i++) {
			colBuf = rowBuf[i].split("\\,", -1);
			for (int j = 0; j < 2; j++) {
				String monthday = colBuf[0];
				String status = colBuf[1];
				System.out.println("MD:["+monthday+"]status:["+status+"]");
			}
//			table_head += "\r\n";
		}
	}
}
