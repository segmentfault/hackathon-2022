package com.bean;

public class Money {
	
	int id;
	
	int pid;
	
	String pname;
 
	String money;//资金金额
	String givedate;//资金日期
	String  purpose;//资金用途

	String  header;//资金用途
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getGivedate() {
		return givedate;
	}
	public void setGivedate(String givedate) {
		this.givedate = givedate;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	 
	 

	 
}
