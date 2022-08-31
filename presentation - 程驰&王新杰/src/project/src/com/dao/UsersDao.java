package com.dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 
import com.bean.Users;
import com.bean.Users;

public class UsersDao {
	PreparedStatement ps = null;
	ResultSet rs = null;
	DBMysql mydb = null;
	public UsersDao() {
		mydb = new DBMysql();
	}
	
	public int deleteUsersById(int id) {
		String sql = "delete from users where uid = ?";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			ps.setInt(1, id);
			i = ps.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			mydb.close();
		}
		return i;
	}
	
	public Users findUserByNameAndPassword(String name,String password){
		Users users = null;
		String sql = "select tt.uid,tt.uname,tt.upassword,tt.urealname,utel  " +
				"from users tt  where uname = '"+name+"' and upassword='"+password+"' ";
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				users = new Users();
				users.setUid(rs.getInt(1));
				users.setUname(rs.getString(2));
				users.setUpassword(rs.getString(3)); 
				users.setUrealname(rs.getString(4));
				users.setUtel(rs.getString(5)); 
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {				
				e.printStackTrace();
			}
			try {
				ps.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			mydb.close();
		}
		return users;
	}

	public List<Users> findAllUsers(Users user,int firstRecord, int lastRecord) {
		List<Users> list = new ArrayList<Users>();
		Users users = null;
		String sql = "select uid,uname,upassword,urealname,utel    " +
				"  from users where  1=1 " ;
		if(user!=null)
		{
			if(user.getUname()!=null)
				sql+=" and (uname like '%"+user.getUname()+"%' or urealname like '%"+user.getUname()+"%') ";
		}
		sql +=" LIMIT "+firstRecord+","+lastRecord;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				users = new Users();
				users.setUid(rs.getInt(1));
				users.setUname(rs.getString(2));
				users.setUpassword(rs.getString(3));  
				users.setUrealname(rs.getString(4)); 
				users.setUtel(rs.getString(5)); 
				list.add(users);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			try {
				ps.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			mydb.close();
		}
		return list;
	}

	public Users findUsersById(int id) {
		Users users = null;
		String sql = "select uid,uname,upassword,urealname,utel   " +
				"from users tt  where uid = "+id;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				users = new Users();
				users.setUid(rs.getInt(1));
				users.setUname(rs.getString(2));
				users.setUpassword(rs.getString(3));  
				users.setUrealname(rs.getString(4)); 
				users.setUtel(rs.getString(5)); 
			
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {				
				e.printStackTrace();
			}
			try {
				ps.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			mydb.close();
		}
		return users;
	}

	
	public int updateUsers(Users users) {
		//tt.tid,tt.tname,tt.trealname,tt.tpassword,tsex
		String sql = "update users set uname=?,upassword=? ,urealname=?,utel=? where uid=?";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			ps.setString(1, users.getUname());
			ps.setString(2, users.getUpassword());  
			ps.setString(3, users.getUrealname()); 
			ps.setString(4, users.getUtel()); 
			ps.setInt(5, users.getUid());
			i = ps.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			mydb.close();
		}
		return i;
	}
	public int findAllCounts(Users user) {
		int count = 0;
		String sql = "select count(*) from users where 1=1";
		if(user!=null)
		{
			if(user.getUname()!=null)
				sql+=" and (uname like '%"+user.getUname()+"%' ) ";
			
		}
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			mydb.close();
		}
		return count;
	}
	public int insertUsers(Users users) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String sql = "insert into users ( uname,upassword,urealname,utel) values(?,?,?,?)";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql); 

			ps.setString(1, users.getUname());
			ps.setString(2, users.getUpassword());  
			ps.setString(3, users.getUrealname()); 
			ps.setString(4, users.getUtel());
			i = ps.executeUpdate();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			mydb.close();
		}
		return i;
	}
	
	public int updatePassword(Users users) {
		//                                  1         2      
		String sql = "update users set  upassword=?   where uid=?";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			ps.setString(1, users.getUpassword());
			ps.setInt(2, users.getUid());
			i = ps.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			mydb.close();
		}
		return i;
	}
	
}
