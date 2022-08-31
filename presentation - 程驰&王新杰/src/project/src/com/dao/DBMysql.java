package com.dao;

import java.sql.Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBMysql {
	Connection con = null;
	
	public DBMysql() {
		try { 
			String driver="com.mysql.jdbc.Driver";  
			Class.forName(driver); //
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Connection getCon(){
	
		try {  

		    String url="jdbc:mysql://localhost:3307/project?useUnicode=true&characterEncoding=utf8";  
		    String user="root";  
		    String password="sa";
			con=DriverManager.getConnection(url, user, password);  
            if (!con.isClosed()) {  
              
            }  
        } catch (SQLException e) {  
            System.out.println("Á´½ÓÊý¾Ý¿âÊ§°Ü: "+e.getMessage());  
        }  
		return con;
	}
	public void close(){
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
