package com.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.PagingModel;
import com.bean.Users;
import com.dao.UsersDao;
import com.tool.Tools;

public class UsersServlet extends HttpServlet {

	UsersDao usersDao = new UsersDao();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");
		if(methodName != null&& methodName != ""){
			if (methodName.equals("findAllUsers")) {
				findAllUsers(request, response);
			} else if (methodName.equals("updatePage")) {
				updatePage(request, response);
			} else if (methodName.equals("deleteUsers")) {
				deleteUsers(request, response);
			} else if (methodName.equals("saveUsers")) {
				try {
					saveOrUpdateUsers(request, response);
				} catch (NoSuchAlgorithmException e) {
					
					e.printStackTrace();
				}
			} else if (methodName.equals("regedit")) {
				try {
					regedit(request, response);
				} catch (NoSuchAlgorithmException e) {
					
					e.printStackTrace();
				}
			} else if (methodName.equals("addUsers")) {
				int allRecord = usersDao.findAllCounts(null);
				List<Users> list = usersDao.findAllUsers(null,0, allRecord);
				request.setAttribute("list", list);
				request.getRequestDispatcher("/pages/users/usersPage.jsp")
						.forward(request, response);
			} else if (methodName.equals("regeditPage")) {
				int allRecord = usersDao.findAllCounts(null);
				List<Users> list = usersDao.findAllUsers(null,0, allRecord);
				request.setAttribute("list", list);
				request.getRequestDispatcher("/pages/users/regeditPage.jsp")
						.forward(request, response);
			} else if (methodName.equals("updatePasswordPage")) {
					updatePasswordPage(request, response);
				
			}else if (methodName.equals("updatePassword")) {
				try {
					updatePassword(request, response);
				} catch (NoSuchAlgorithmException e) {					
					e.printStackTrace();
				}
			}  else {
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
	
	public void findAllUsers(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String showpage = request.getParameter("showpage");
		if (showpage == "" || showpage == null) {// 当前页
			showpage = "1";
		}
		Users stu = new Users();
		stu.setUname(Tools.encode(request.getParameter("uname")));
		int allRecord = usersDao.findAllCounts(stu);
		int pageIndex = Integer.parseInt(showpage);
		int pageSize = Tools.PageSize;
		int firstRecord = (pageIndex - 1)*pageSize;
		int lastRecord = pageIndex * pageSize;
		List<Users> list = usersDao.findAllUsers(stu,firstRecord, lastRecord);
		PagingModel pagingModel = new PagingModel();
		pagingModel.setPerR(pageSize);
		pagingModel.setCurrentP(showpage);
		pagingModel.setAllR(allRecord);
		pagingModel.setAllP();
		pagingModel.setPageInfo();
		pagingModel.setPageLink("servlet/UsersServlet?method=findAllUsers&uname="+Tools.encode(request.getParameter("uname")));
		request.setAttribute("list", list);
		request.setAttribute("users", stu);
		request.setAttribute("pagingModel", pagingModel);
		request.setAttribute("currentp", pageSize);
		request.setAttribute("pagenum", showpage);
		request.getRequestDispatcher("/pages/users/userslist.jsp").forward(
				request, response);
	}

	public void updatePage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("uid");
		if (id != null &&!id.equals("") ) {
			int uid= Integer.parseInt(id);
			Users users = usersDao.findUsersById(uid);
			request.setAttribute("users", users);
			int allRecord = usersDao.findAllCounts(null);
			List<Users> list = usersDao.findAllUsers(null,0, allRecord);
			request.setAttribute("list", list);
			request.setAttribute("flag", request.getParameter("flag"));
			request.getRequestDispatcher("/pages/users/usersPage.jsp")
					.forward(request, response);

		} else {
			request.setAttribute("message", "出错啦");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}
	
	public void updatePasswordPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Object uid = request.getSession().getAttribute("userid");
		if (uid != null ) {
			Integer eid = (Integer) uid;
			Users users = usersDao.findUsersById(eid);
			request.setAttribute("users", users);
			int allRecord = usersDao.findAllCounts(null);
			List<Users> list = usersDao.findAllUsers(null,0, allRecord);
			request.setAttribute("list", list);
			request.getRequestDispatcher("/pages/users/updatepassword.jsp")
					.forward(request, response);

		} else {
			request.setAttribute("message", "出错啦");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}

	public void deleteUsers(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("uid");
		if (id != null &&!id.equals("") ) {
			int uid = Integer.parseInt(id);
			usersDao.deleteUsersById(uid);
			request.getRequestDispatcher(
					"UsersServlet?method=findAllUsers").forward(
					request, response);
		} else {
			request.setAttribute("message", "删除失败，请确认");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}

	public void saveOrUpdateUsers(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			NoSuchAlgorithmException {

		String flag= request.getParameter("flag");
		String uid = request.getParameter("uid");
		String uname = request.getParameter("uname");
		String utype = request.getParameter("utype");
		String upassword = request.getParameter("upassword");
		String urealname = request.getParameter("urealname");  
		String utel = request.getParameter("utel"); 
		Users users = new Users();
		
		users.setUname(Tools.encode(uname)); 
		users.setUtype(Tools.encode(utype));	
		users.setUpassword(Tools.encode(upassword));	
		users.setUrealname(Tools.encode(urealname));	 
		users.setUtel(Tools.encode(utel));	
		int i =0;
		if(uid!=null&&!uid.equals("")){
			users.setUid(Integer.parseInt(uid));
			i= usersDao.updateUsers(users);
		}else{
			i= usersDao.insertUsers(users);
		}
		
		if (i > 0) {
			if(flag!=null&&flag.equals("2")){
				request.setAttribute("url", "servlet/UsersServlet?method=updatePage&uid="+uid+"&flag=2");
			}else{
				request.setAttribute("url", "servlet/UsersServlet?method=findAllUsers");
			}
			
			request.setAttribute("message", "保存成功");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}
	public void regedit(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			NoSuchAlgorithmException { 
		String uname = request.getParameter("uname");
		String utype = request.getParameter("utype");
		String upassword = request.getParameter("upassword");
		String urealname = request.getParameter("urealname");  
		String utel = request.getParameter("utel"); 
		Users users = new Users();
		
		users.setUname(Tools.encode(uname)); 
		users.setUtype(Tools.encode(utype));	
		users.setUpassword(Tools.encode(upassword));	
		users.setUrealname(Tools.encode(urealname)); 
		users.setUtel(Tools.encode(utel));	
		int i =0;
		i= usersDao.insertUsers(users);
		if (i > 0) {
			request.getRequestDispatcher("/pages/login.jsp")
			.forward(request, response);
		}
	}
	public void updatePassword(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, NoSuchAlgorithmException {
		String uid = request.getParameter("uid");
		String upassword = request.getParameter("upassword");
		Users users = new Users();
		users.setUid(Integer.parseInt(uid));
		users.setUpassword(Tools.encode(upassword));
		int i = usersDao.updatePassword(users);
		if (i > 0) {
			request.setAttribute("url", "pages/sy.jsp");
			request.setAttribute("message", "更新成功，请记住新密码！");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}
}
	
