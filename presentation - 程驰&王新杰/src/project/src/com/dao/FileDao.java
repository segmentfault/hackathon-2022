package com.dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 

import com.bean.File; 

public class FileDao {
	PreparedStatement ps = null;
	ResultSet rs = null;
	DBMysql mydb = null;
	public FileDao() {
		mydb = new DBMysql();
	}
	
	public int deleteFileById(int id) {
		String sql = "delete from file where id = ?";
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
	
	

	public List<File> findAllFile(File file,int firstRecord, int lastRecord) {
		List<File> list = new ArrayList<File>();
		File file1 = null;
		String sql = "select id,url ,name   " +
				"  from File where  1=1 " ;
		if(file!=null)
		{
			if(file.getName()!=null)
				sql+=" and (name like '%"+file.getName()+"%' ) ";
		} 
		sql +=" LIMIT "+firstRecord+","+lastRecord;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				file1 = new File();
				file1.setId(rs.getInt(1));
				file1.setName(rs.getString(3));
				file1.setUrl(rs.getString(2)); 
				list.add(file1);
				
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

	public File findFileById(int id) {
		File file = null;
		String sql = "select id,url,name   " +
				"from File tt  where id = "+id;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				file = new File();
				file.setId(rs.getInt(1));
				file.setName(rs.getString(3));
				file.setUrl(rs.getString(2));
			
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
		return file;
	}

	
	public int updateFile(File file) { 
		String sql = "update file set name=?,url=?  where id=?";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			ps.setString(1, file.getName());
			ps.setString(2, file.getUrl());   
			ps.setInt(3, file.getId());
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
	public int findAllCounts(File file) {
		int count = 0;
		String sql = "select count(*) from File where 1=1";
		if(file!=null)
		{
			if(file.getName()!=null)
				sql+=" and (name like '%"+file.getName()+"%' ) ";
			
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
	public int insertFile(File file) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String sql = "insert into File ( name,url) values(?,?)";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql); 
			ps.setString(1, file.getName());
			ps.setString(2, file.getUrl());   
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
