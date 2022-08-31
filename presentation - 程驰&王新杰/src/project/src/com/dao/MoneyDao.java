package com.dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;  
import com.bean.Money; 
import com.bean.Users;

public class MoneyDao {
	PreparedStatement ps = null;
	ResultSet rs = null;
	DBMysql mydb = null;
	public MoneyDao() {
		mydb = new DBMysql();
	}
	
	public int deleteMoneyById(int id) {
		String sql = "delete from Money where id = ?";
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
	
	

	public List<Money> findMoneyByPid(int pid) {
		List<Money> list = new ArrayList<Money>();
		String sql = "select s.id,pid,s.money,s.givedate,s.purpose,p.name,p.header    " +
				"  from Money s left join project p on p.id = s.pid  where  1=1 " ;
		 
				sql+=" and s.pid=  "+pid;
		
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				Money money2 =  new Money();
				money2.setId(rs.getInt(1));
				money2.setMoney(rs.getString(3));
				money2.setGivedate(rs.getString(4));   
				money2.setPurpose(rs.getString(5));
				money2.setPname(rs.getString(6));
				money2.setPid(rs.getInt(2)); 
				money2.setHeader(rs.getString(7));
				list.add(money2);
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
	
	public Money findMoneyById(int id) {

		Money money2 = new Money();
		String sql = "select id,pid,money,givedate,purpose     " +
				"from Money s  where id = "+id;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){ 
				money2.setId(rs.getInt(1));
				money2.setMoney(rs.getString(3));
				money2.setGivedate(rs.getString(4));   
				money2.setPurpose(rs.getString(5)); 
				money2.setPid(rs.getInt(2)); 
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
		return money2;
	}

	
	public int updateMoney(Money money2) { 
		String sql = "update Money set pid=?,money=?,givedate=?,purpose=?  where id=?";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql);
			ps.setInt(1, money2.getPid());
			ps.setString(2, money2.getMoney()); 
			ps.setString(3, money2.getGivedate());
			ps.setString(4, money2.getPurpose());   
			ps.setInt(5, money2.getId()); 
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
	public int findAllCounts(Money money1) {
		int count = 0;
		String sql = "select count(*) from Money s left join Porject p on p.id=s.pid   where 1=1";
		if(money1!=null)
		{
			if(money1.getPname()!=null)
				sql+=" and p.name like '%"+money1.getPname()+"%' ";
			if(money1.getPurpose()!=null)
				sql+=" and p.teacher like '%"+money1.getPurpose()+"%'  ";
			 
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
	public int insertMoney(Money money2) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String sql = "insert into Money ( pid,money,givedate,purpose) values(?,?,?,?)";
		int i = 0;
		try {
			ps = mydb.getCon().prepareStatement(sql); 
			ps.setInt(1, money2.getPid());
			ps.setString(2, money2.getMoney()); 
			ps.setString(3, money2.getGivedate());
			ps.setString(4, money2.getPurpose());
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
