package com.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; 

import com.bean.Student;
import com.bean.Users; 
import com.dao.StudentDao;
import com.dao.UsersDao;

public class LoginServlet extends HttpServlet {

	UsersDao usersDao =  new UsersDao(); 
	StudentDao studentDao = new StudentDao();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");
		if (methodName != null && methodName.equals("login")) { 
			String type = request.getParameter("type");
			String name = request.getParameter("userName");
			String password = request.getParameter("password");
			Users users = new Users();
			if (!name.equals("") && name != null && !password.equals("")
					&& password != "") { 
				if(type.equals("1")){
					users = usersDao.findUserByNameAndPassword(name, password);
					if (users != null && users.getUid() != 0) {
						HttpSession session = request.getSession();
						session.setAttribute("username", name);
						session.setAttribute("userid", users.getUid());
						session.setAttribute("type", "1");
						users.setUtype("1");
						session.setAttribute("user", users);
						request.getRequestDispatcher("/MyOffice.jsp").forward(
								request, response);
					} else {
						request.setAttribute("message", "用户名或密码错误");
						request.getRequestDispatcher("/login.jsp").forward(
								request, response);
					}
				}else{
					Student student=studentDao.findStudentByNameAndPassword(name, password);
					if (student != null && student.getId() != 0) {
						HttpSession session = request.getSession();
						session.setAttribute("username", name);
						session.setAttribute("userid", student.getId());
						users.setUtype("2");
						users.setUname(name);
						users.setUrealname(student.getName());
						users.setUid(student.getId());
						session.setAttribute("user", users);
						request.getRequestDispatcher("/MyOffice.jsp").forward(
								request, response);
					} else {
						request.setAttribute("message", "用户名或密码错误");
						request.getRequestDispatcher("/login.jsp").forward(
								request, response);
					}
				}
				
				
			}
		}
		if (methodName != null && methodName.equals("loginout")) {
			request.setAttribute("username", "");
			request.setAttribute("userid", "");
			request.setAttribute("type", "");
			request.setAttribute("user", null);
			request.getRequestDispatcher("/pages/login.jsp").forward(request,
					response);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
