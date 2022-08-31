package com.bean;

public class PagingModel {
	private int CurrentP;			//当前页码
	private int AllP;				//总页数
	private int AllR;				//总记录数
	private int PerR;				//每页显示记录数
	private String pageLink;		//分页导航栏信息
	private String pageInfo;		//分页状态显示信息
	
	public PagingModel(){
		CurrentP=1;
		AllP=1;
		AllR=0;
		PerR=3;
		pageLink="";
		pageInfo="";
	}
	
	/** 设置每页显示记录数 */
	public void setPerR(int PerR){
		this.PerR=PerR;
	}
	
	/** 设置总记录数 */
	public void setAllR(int AllR){
		this.AllR=AllR;
	}
	/** 计算总页数 */
	public void setAllP(){
		AllP=(AllR%PerR==0)?(AllR/PerR):(AllR/PerR+1);
	}
	
	/** 设置当前页码 */
	public void setCurrentP(String currentP) {		
		if(CurrentP<1)
			CurrentP=1;
		if(CurrentP>AllP)
			CurrentP=AllP;	
		CurrentP = Integer.parseInt(currentP);
	}

	/** 设置分页状态显示信息 */
	public void setPageInfo(){
		if(AllP>1){
			pageInfo="<table border='0' cellpadding='3' ><tr><td style='width:100%;'>";
			pageInfo+="每页显示："+PerR+"/"+AllR+" 条记录！";
			pageInfo+="当前页："+CurrentP+"/"+AllP+" 页！";
			pageInfo+="</td></tr></table>";			
		}				
	}
	
    /** 设置分页导航栏信息 */
	public void setPageLink(String gowhich){
		if(gowhich==null)
			gowhich="";
		if(gowhich.indexOf("?")>=0)
			gowhich+="&";
		else
			gowhich+="?";
		if(AllP>1){
			pageLink="<table border='0' cellpadding='3' ><tr><td style='width:100%;'>";
			if(CurrentP>1){
				pageLink+="<a href='/project/"+gowhich+"showpage=1'>首页</a>&nbsp;";
				pageLink+="<a href='/project/"+gowhich+"showpage="+(CurrentP-1)+"'>上一页</a>&nbsp;";
			}
			if(CurrentP<AllP){
				pageLink+="<a href='/project/"+gowhich+"showpage="+(CurrentP+1)+"'>下一页</a>&nbsp;";
				pageLink+="<a href='/project/"+gowhich+"showpage="+AllP+"'>尾页</a>";
			}
			pageLink+="</td></tr></table>";			
		}		
	}
	
	/** 返回每页记录数 */
	public int getPerR(){
		return PerR;
	}
	
	/** 返回总记录数 */
	public int getAllR() {
		return AllR;
	}	
	
    /** 返回总页数 */
	public int getAllP() {		
		return AllP;
	}

	/** 返回当前页码 */
	public int getCurrentP() {
		return CurrentP;
	}
	
	/** 返回分页状态显示信息 */
	public String getPageInfo(){
		return pageInfo;
	}

	/** 返回分页导航栏信息 */
	public String getPageLink(){
		return pageLink;
	}
}
