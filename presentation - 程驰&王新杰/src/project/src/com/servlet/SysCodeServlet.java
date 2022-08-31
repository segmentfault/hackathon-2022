package com.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bean.PagingModel;
import com.bean.SysCode;
import com.bean.SysCode; 
import com.dao.SysCodeDao;
import com.dao.SysCodeDao; 
import com.tool.Tools;

public class SysCodeServlet extends HttpServlet {

	SysCodeDao sysCodeDao = new SysCodeDao();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");
		if(methodName != null&& methodName != ""){
			if (methodName.equals("findAllSysCode")) {
				findAllSysCode(request, response);
			} else if (methodName.equals("updatePage")) {
				updatePage(request, response);
			} else if (methodName.equals("deleteSysCode")) {
				deleteSysCode(request, response);
			} else if (methodName.equals("saveSysCode")) {
				try {
					saveOrUpdateSysCode(request, response);
				} catch (NoSuchAlgorithmException e) {
					
					e.printStackTrace();
				}
			} else if (methodName.equals("addSysCode")) {
				int allRecord = sysCodeDao.findAllCounts(null);
				List<SysCode> list = sysCodeDao.findAllSysCode(null,0, allRecord);
				request.setAttribute("list", list);
				request.getRequestDispatcher("/pages/syscode/syscodePage.jsp")
						.forward(request, response);
			} else {
				request.setAttribute("message", "口令出错");
				request.getRequestDispatcher("/pages/message.jsp").forward(
						request, response);
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void findAllSysCode(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String showpage = request.getParameter("showpage");
		if (showpage == "" || showpage == null) {// 当前页
			showpage = "1";
		}
		SysCode sysCode = new SysCode();
		sysCode.setType(Tools.encode(request.getParameter("type")));
		int allRecord = sysCodeDao.findAllCounts(sysCode);
		int pageIndex = Integer.parseInt(showpage);
		int pageSize = Tools.PageSize;
		int firstRecord = (pageIndex - 1)*pageSize;
		int lastRecord = pageIndex * pageSize;
		List<SysCode> list = sysCodeDao.findAllSysCode(sysCode,firstRecord, lastRecord);
		PagingModel pagingModel = new PagingModel();
		pagingModel.setPerR(pageSize);
		pagingModel.setCurrentP(showpage);
		pagingModel.setAllR(allRecord);
		pagingModel.setAllP();
		pagingModel.setPageInfo();
		pagingModel.setPageLink("servlet/SysCodeServlet?method=findAllSysCode&type="+Tools.encode(request.getParameter("type")));
		request.setAttribute("list", list);
		request.setAttribute("syscode", sysCode);
		request.setAttribute("pagingModel", pagingModel);
		request.setAttribute("currentp", pageSize);
		request.setAttribute("pagenum", showpage);
		request.getRequestDispatcher("/pages/syscode/syscodelist.jsp").forward(
				request, response);
	}

	public void updatePage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if (id != null &&!id.equals("") ) {
			int tid= Integer.parseInt(id);
			SysCode sysCode = sysCodeDao.findSysCodeById(tid);
			request.setAttribute("syscode", sysCode);
			int allRecord = sysCodeDao.findAllCounts(null);
			List<SysCode> list = sysCodeDao.findAllSysCode(null,0, allRecord);
			request.setAttribute("list", list);
			request.setAttribute("flag", request.getParameter("flag"));
			request.getRequestDispatcher("/pages/syscode/syscodePage.jsp")
					.forward(request, response);

		} else {
			request.setAttribute("message", "出错啦");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}
	


	public void deleteSysCode(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if (id != null &&!id.equals("") ) {
			int tid = Integer.parseInt(id);
			sysCodeDao.deleteSysCodeById(tid);
			request.getRequestDispatcher(
					"SysCodeServlet?method=findAllSysCode").forward(
					request, response);
		} else {
			request.setAttribute("message", "删除失败，请确认");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}

	public void saveOrUpdateSysCode(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			NoSuchAlgorithmException {

		String flag= request.getParameter("flag");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String code = request.getParameter("code");
		String type = request.getParameter("type");
		SysCode sysCode = new SysCode();
		
		sysCode.setName(Tools.encode(name)); 
		sysCode.setCode(Tools.encode(code));	
		sysCode.setType(Tools.encode(type));
		int i =0;
		if(id!=null&&!id.equals("")){
			sysCode.setId(Integer.parseInt(id));
			i= sysCodeDao.updateSysCode(sysCode);
		}else{
			i= sysCodeDao.insertSysCode(sysCode);
		}
		
		if (i > 0) {
			if(flag!=null&&flag.equals("1")){
				request.setAttribute("url", "servlet/SysCodeServlet?method=updatePage&id="+id+"&flag=1");
			}else{
				request.setAttribute("url", "servlet/SysCodeServlet?method=findAllSysCode");
			}
			
			request.setAttribute("message", "保存成功");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}
	
	
}
	
