package com.dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;  
import com.bean.Student; 
import com.bean.Users;

public class StudentDao {
	PreparedStatement ps = null;
	ResultSet rs = null;
	DBMysql mydb = null;
	public StudentDao() {
		mydb = new DBMysql();
	}
	
	public int deleteStudentById(int id) {
		String sql = "delete from Student where id = ?";
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
	
	

	public List<Student> findAllStudent(Student student1,int firstRecord, int lastRecord) {
		List<Student> list = new ArrayList<Student>();
		String sql = "select id,name,code,college,realname,tel,password    " +
				"  from Student s   where  1=1 " ;
		if(student1!=null)
		{
			if(student1.getName()!=null)
				sql+=" and s.name like '%"+student1.getName()+"%' ";
			if(student1.getCode()!=null)
				sql+=" and s.code like '%"+student1.getCode()+"%'  ";
			 
		} 
		sql +=" LIMIT "+firstRecord+","+lastRecord;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				Student student2 =  new Student();
				student2.setId(rs.getInt(1));
				student2.setName(rs.getString(2));
				student2.setCode(rs.getString(3));   
				student2.setCollege(rs.getString(4));
				student2.setRealname(rs.getString(5));
				student2.setTel(rs.getString(6));
				student2.setPassword(rs.getString(7));
				list.add(student2);
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
	public Student findStudentByNameAndPassword(String name,String password){

		Student student2 = new Student();
		String sql = "select id,name,code,college,realname,tel,password   " +
				"from Student tt  where name = '"+name+"' and password='"+password+"' ";
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				student2.setId(rs.getInt(1));
				student2.setName(rs.getString(2));
				student2.setCode(rs.getString(3));   
				student2.setCollege(rs.getString(4));
				student2.setRealname(rs.getString(5));
				student2.setTel(rs.getString(6));
				student2.setPassword(rs.getString(7));
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
		return student2;
	}
	public Student findStudentById(int id) {

		Student student2 = new Student();
		String sql = "select id,name,code,college,realname,tel,password    " +
				"from Student s  where id = "+id;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){ 
				student2.setId(rs.getInt(1));
				student2.setName(rs.getString(2));
				student2.setCode(rs.getString(3));   
				student2.setCollege(rs.getString(4));
				student2.setRealname(rs.getString(5));
				student2.setTel(rs.getString(6));
				student2.setPassword(rs.getString(7));
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
		return student2;
	}

	
	public int updateStudent(Student Student2) { 
		String sql = "update Student set name=?,code=?,college=?,realname=?,tel=?,password=? where id=?";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			ps.setString(1, Student2.getName());
			ps.setString(2, Student2.getCode()); 
			ps.setString(3, Student2.getCollege());
			ps.setString(4, Student2.getRealname()); 
			ps.setString(5, Student2.getTel());
			ps.setString(6, Student2.getPassword()); 
			ps.setInt(7, Student2.getId()); 
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
	public int findAllCounts(Student student1) {
		int count = 0;
		String sql = "select count(*) from Student s   where 1=1";
		if(student1!=null)
		{
			if(student1.getName()!=null)
				sql+=" and s.name like '%"+student1.getName()+"%' ";
			if(student1.getCode()!=null)
				sql+=" and s.code like '%"+student1.getCode()+"%'  ";
		
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
	public int insertStudent(Student Student2) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String sql = "insert into Student ( name,code,college,realname,tel,password) values(?,?,?,?,?,?)";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql); 
			ps.setString(1, Student2.getName());
			ps.setString(2, Student2.getCode()); 
			ps.setString(3, Student2.getCollege());
			ps.setString(4, Student2.getRealname()); 
			ps.setString(5, Student2.getTel());
			ps.setString(6, Student2.getPassword()); 
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
