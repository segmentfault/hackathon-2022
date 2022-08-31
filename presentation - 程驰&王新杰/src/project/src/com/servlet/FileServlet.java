package com.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bean.PagingModel;
import com.bean.File; 
import com.dao.FileDao; 
import com.tool.Tools;

public class FileServlet extends HttpServlet {

	FileDao fileDao = new FileDao();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");
		if(methodName != null&& methodName != ""){
			if (methodName.equals("findAllFile")) {
				findAllFile(request, response);
			} else if (methodName.equals("updatePage")) {
				updatePage(request, response);
			} else if (methodName.equals("deleteFile")) {
				deleteFile(request, response);
			} else if (methodName.equals("saveFile")) {
				try {
					saveOrUpdateFile(request, response);
				} catch (NoSuchAlgorithmException e) {
					
					e.printStackTrace();
				}
			} else if (methodName.equals("addFile")) {
				int allRecord = fileDao.findAllCounts(null);
				List<File> list = fileDao.findAllFile(null,0, allRecord);
				request.setAttribute("list", list);
				request.getRequestDispatcher("/pages/file/filePage.jsp")
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
	
	public void findAllFile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String showpage = request.getParameter("showpage");
		if (showpage == "" || showpage == null) {// 当前页
			showpage = "1";
		}

		String flag= request.getParameter("flag");
		File file = new File();
		file.setName(Tools.encode(request.getParameter("name")));
		int allRecord = fileDao.findAllCounts(file);
		int pageIndex = Integer.parseInt(showpage);
		int pageSize = Tools.PageSize;
		int firstRecord = (pageIndex - 1)*pageSize;
		int lastRecord = pageIndex * pageSize;
		List<File> list = fileDao.findAllFile(file,firstRecord, lastRecord);
		PagingModel pagingModel = new PagingModel();
		pagingModel.setPerR(pageSize);
		pagingModel.setCurrentP(showpage);
		pagingModel.setAllR(allRecord);
		pagingModel.setAllP();
		pagingModel.setPageInfo();
		pagingModel.setPageLink("servlet/FileServlet?method=findAllFile&flag="+flag+"&name="+file.getName());
		request.setAttribute("list", list);
		request.setAttribute("file", file);
		request.setAttribute("flag", flag);
		request.setAttribute("pagingModel", pagingModel);
		request.setAttribute("currentp", pageSize);
		request.setAttribute("pagenum", showpage);
		request.getRequestDispatcher("/pages/file/filelist.jsp").forward(
				request, response);
	}

	public void updatePage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if (id != null &&!id.equals("") ) {
			int tid= Integer.parseInt(id);
			File file = fileDao.findFileById(tid);
			request.setAttribute("file", file);
			int allRecord = fileDao.findAllCounts(null);
			List<File> list = fileDao.findAllFile(null,0, allRecord);
			request.setAttribute("list", list);
			request.setAttribute("flag", request.getParameter("flag"));
			request.getRequestDispatcher("/pages/file/filePage.jsp")
					.forward(request, response);

		} else {
			request.setAttribute("message", "出错啦");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}
	


	public void deleteFile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if (id != null &&!id.equals("") ) {
			int tid = Integer.parseInt(id);
			fileDao.deleteFileById(tid);
			request.getRequestDispatcher(
					"FileServlet?method=findAllFile").forward(
					request, response);
		} else {
			request.setAttribute("message", "删除失败，请确认");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}

	public void saveOrUpdateFile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			NoSuchAlgorithmException {
		String flag= request.getParameter("flag");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String url = request.getParameter("url"); 
		File file = new File();
		
		file.setName(Tools.encode(name)); 
		file.setUrl(Tools.encode(url));	 
		int i =0;
		if(id!=null&&!id.equals("")){
			file.setId(Integer.parseInt(id));
			i= fileDao.updateFile(file);
		}else{
			i= fileDao.insertFile(file);
		}
		
		if (i > 0) {
			if(flag!=null&&flag.equals("1")){
				request.setAttribute("url", "servlet/FileServlet?method=updatePage&id="+id+"&flag=1");
			}else{
				request.setAttribute("url", "servlet/FileServlet?method=findAllFile");
			}
			
			request.setAttribute("message", "保存成功");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}
	
	
}
	
