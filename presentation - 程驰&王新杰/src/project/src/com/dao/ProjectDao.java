package com.dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 

import com.bean.Project; 

public class ProjectDao {
	PreparedStatement ps = null;
	ResultSet rs = null;
	DBMysql mydb = null;
	public ProjectDao() {
		mydb = new DBMysql();
	}
	
	public int deleteProjectById(int id) {
		String sql = "delete from Project where id = ?";
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
	
	

	public List<Project> findAllProject(Project project1,int firstRecord, int lastRecord) {
		List<Project> list = new ArrayList<Project>();
		Project project = null;
		String sql = "select p.name, header, p.tel, p.college, participant, teacher, teachertel, " +
		//           8          9      10      11       12      13      14
				"teachertitle, unit, signdate, fileurl, verify ," +
				//  15        16      17         18
				"   checkinfo, evaluate, checkdate ,p.id,uid,u.realname   " +
				"  from Project p left join student u on u.id=p.uid where  1=1 " ;
		if(project1!=null)
		{
			if(project1.getName()!=null)
				sql+=" and (p.name like '%"+project1.getName()+"%' ) ";
			if(project1.getTeacher()!=null)
				sql+=" and (p.teacher like '%"+project1.getTeacher()+"%' ) ";
			if(project1.getUid()!=0){
				sql+=" and uid="+project1.getUid();
			}
			if(project1.getVerify()!=null&&!project1.getVerify().equals("")){
				sql+=" and verify='"+project1.getVerify()+"' ";
			}
			if(project1.getCheck()!=null&&!project1.getCheck().equals("")){
				sql+=" and checkinfo is  null ";
			}
			
		} 
		sql +=" LIMIT "+firstRecord+","+lastRecord;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				project = new Project();
				project.setName(rs.getString(1));
				project.setHeader(rs.getString(2)); 
				project.setTel(rs.getString(3));
				project.setCollege(rs.getString(4)); 
				project.setParticipant(rs.getString(5));
				project.setTeacher(rs.getString(6)); 
				project.setTeachertel(rs.getString(7));
				project.setTeachertitle(rs.getString(8)); 
				project.setUnit(rs.getString(9));
				project.setSigndate(rs.getString(10)); 
				project.setFileurl(rs.getString(11));
				project.setVerify(rs.getString(12)); 
				 
				project.setCheck(rs.getString(13)); 
				project.setEvaluate(rs.getString(14));
				project.setCheckdate(rs.getString(15)); 
				project.setId(rs.getInt(16));
				project.setUid(rs.getInt(17));
				project.setUname(rs.getString(18));
				list.add(project);
				
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

	public Project findProjectById(int id) {
		Project project = null;
		String sql = "select name, header, tel, college, participant, teacher, teachertel, " +
		//           8          9      10      11       12      13      14
				"teachertitle, unit, signdate, fileurl, verify ," +
				//  15     16      17         18
				"  checkinfo, evaluate, checkdate ,id,uid" +
				" from Project tt  where id = "+id;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				project = new Project();
				project.setName(rs.getString(1));
				project.setHeader(rs.getString(2)); 
				project.setTel(rs.getString(3));
				project.setCollege(rs.getString(4)); 
				project.setParticipant(rs.getString(5));
				project.setTeacher(rs.getString(6)); 
				project.setTeachertel(rs.getString(7));
				project.setTeachertitle(rs.getString(8)); 
				project.setUnit(rs.getString(9));
				project.setSigndate(rs.getString(10)); 
				project.setFileurl(rs.getString(11));
				project.setVerify(rs.getString(12)); 
				project.setCheck(rs.getString(13)); 
				project.setEvaluate(rs.getString(14));
				project.setCheckdate(rs.getString(15)); 
				project.setId(rs.getInt(16));
				project.setUid(rs.getInt(17)); 
			
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
		return project;
	}

	
	public int updateProject(Project project) { 
		String sql = "update Project set  name=?, header=?, tel=?, college=?, " +
				"participant=?, teacher=?, teachertel=?, teachertitle=?, " +
				"unit=?, signdate=?, fileurl=?, verify=? ," +
				" checkinfo=?, evaluate=?, checkdate=?,uid=?  where id=?";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			ps.setString(1, project.getName());
			ps.setString(2, project.getHeader()); 
			ps.setString(3, project.getTel());
			ps.setString(4, project.getCollege()); 
			ps.setString(5, project.getParticipant());
			ps.setString(6, project.getTeacher()); 
			ps.setString(7, project.getTeachertel());
			ps.setString(8, project.getTeachertitle()); 
			ps.setString(9, project.getUnit());
			ps.setString(10, project.getSigndate()); 
			ps.setString(11, project.getFileurl());
			ps.setString(12, project.getVerify()); 
			 
			ps.setString(13, project.getCheck()); 
			ps.setString(14, project.getEvaluate());
			ps.setString(15, project.getCheckdate()); 
			ps.setInt(16, project.getUid()); 
			ps.setInt(17,project.getId());
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
	public int findAllCounts(Project project1) {
		int count = 0;
		String sql = "select count(*) from project where 1=1";
		if(project1!=null)
		{
			if(project1.getName()!=null)
				sql+=" and (name like '%"+project1.getName()+"%' ) ";
			if(project1.getTeacher()!=null)
				sql+=" and (teacher like '%"+project1.getTeacher()+"%' ) ";
			if(project1.getUid()!=0){
				sql+=" and uid="+project1.getUid();
			}
			if(project1.getVerify()!=null&&!project1.getVerify().equals("")){
				sql+=" and verify='"+project1.getVerify()+"' ";
			}
			if(project1.getCheck()!=null&&!project1.getCheck().equals("")){
				sql+=" and check is  null ";
			}
			 
			
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
	public int insertProject(Project project) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//                                      1   2        3     4        5           6         7
		String sql = "insert into Project (  name, header, tel, college, participant, teacher, teachertel, " +
		//           8          9      10      11       12      13      14
				"teachertitle, unit, signdate, fileurl, verify, " +
				//  15     16      17         18
				"   checkinfo, evaluate, checkdate,uid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql); 
			ps.setString(1, project.getName());
			ps.setString(2, project.getHeader()); 
			ps.setString(3, project.getTel());
			ps.setString(4, project.getCollege()); 
			ps.setString(5, project.getParticipant());
			ps.setString(6, project.getTeacher()); 
			ps.setString(7, project.getTeachertel());
			ps.setString(8, project.getTeachertitle()); 
			ps.setString(9, project.getUnit());
			ps.setString(10, project.getSigndate()); 
			ps.setString(11, project.getFileurl());
			ps.setString(12, project.getVerify()); 
			ps.setString(13, project.getCheck()); 
			ps.setString(14, project.getEvaluate());
			ps.setString(15, project.getCheckdate()); 
			ps.setInt(16, project.getUid()); 
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
