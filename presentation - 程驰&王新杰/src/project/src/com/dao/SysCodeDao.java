package com.dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 

import com.bean.SysCode; 

public class SysCodeDao {
	PreparedStatement ps = null;
	ResultSet rs = null;
	DBMysql mydb = null;
	public SysCodeDao() {
		mydb = new DBMysql();
	}
	
	public int deleteSysCodeById(int id) {
		String sql = "delete from syscode where id = ?";
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
	
	

	public List<SysCode> findAllSysCode(SysCode sysCode,int firstRecord, int lastRecord) {
		List<SysCode> list = new ArrayList<SysCode>();
		SysCode sysCode1 = null;
		String sql = "select id,name,code ,type   " +
				"  from syscode where  1=1 " ;
		if(sysCode!=null)
		{
			if(sysCode.getType()!=null)
				sql+=" and (type like '%"+sysCode.getType()+"%' ) ";
		} 
		sql +=" LIMIT "+firstRecord+","+lastRecord;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				sysCode1 = new SysCode();
				sysCode1.setId(rs.getInt(1));
				sysCode1.setName(rs.getString(2));
				sysCode1.setCode(rs.getString(3)); 
				sysCode1.setType(rs.getString(4)); 
				list.add(sysCode1);
				
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

	public SysCode findSysCodeById(int id) {
		SysCode SysCode = null;
		String sql = "select id,name,code,type   " +
				"from syscode tt  where id = "+id;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				SysCode = new SysCode();
				SysCode.setId(rs.getInt(1));
				SysCode.setName(rs.getString(2));
				SysCode.setCode(rs.getString(3));

				SysCode.setType(rs.getString(4)); 
			
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
		return SysCode;
	}

	
	public int updateSysCode(SysCode SysCode) { 
		String sql = "update syscode set name=?,code=?,type=? where id=?";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			ps.setString(1, SysCode.getName());
			ps.setString(2, SysCode.getCode());  
			ps.setString(3, SysCode.getType()); 
			ps.setInt(4, SysCode.getId());
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
	public int findAllCounts(SysCode SysCode) {
		int count = 0;
		String sql = "select count(*) from SysCode where 1=1";
		if(SysCode!=null)
		{
			if(SysCode.getName()!=null)
				sql+=" and (name like '%"+SysCode.getName()+"%' ) ";
			
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
	public int insertSysCode(SysCode SysCode) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String sql = "insert into syscode ( name,code,type) values(?,?,?)";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql); 
			ps.setString(1, SysCode.getName());
			ps.setString(2, SysCode.getCode());  
			ps.setString(3, SysCode.getType());  
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
