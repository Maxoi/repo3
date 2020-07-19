package com.unity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Tools {
	public static void ReTo(HttpServletRequest request,HttpServletResponse response,String page){
		System.out.println("进入页面:["+page+"]");
		try {
			String header = request.getHeader("User-Agent");
			System.out.println(header);
			if(header.toLowerCase().contains("msie")){
				System.out.println("你使用的IE游览器");
				request.getRequestDispatcher(page+".jsp").forward(request, response);
			}else if(header.toLowerCase().contains("firefox")){
				System.out.println("你使用的火狐游览器");
				request.getRequestDispatcher(page+"2.jsp").forward(request, response);
			}else if(header.toLowerCase().contains("chrome")){
				System.out.println("你使用的是谷歌游览器");
				request.getRequestDispatcher(page+"2.jsp").forward(request, response);
			}else{
				request.getRequestDispatcher(page+"2.jsp").forward(request, response);
				System.out.println("你使用的是其他游览器");
			}
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
