package com.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bean.Student;
import com.bean.PagingModel;
import com.bean.Users;
import com.dao.StudentDao;
import com.tool.Tools;

public class StudentServlet extends HttpServlet {
	StudentDao studentDao = new StudentDao();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");
		if (methodName != null && methodName != "") {
			if (methodName.equals("findAllStudent")) {
				findAllStudent(request, response);
			} else if (methodName.equals("updatePage")) {
				updatePage(request, response);
			} else if (methodName.equals("deleteStudent")) {
				deleteStudent(request, response);
			} else if (methodName.equals("saveStudent")) {
				try {
					saveOrUpdateStudent(request, response);
				} catch (NoSuchAlgorithmException e) {

					e.printStackTrace();
				}
			} else if (methodName.equals("addStudent")) {
				int allRecord = studentDao.findAllCounts(null);
				List<Student> list = studentDao.findAllStudent(null, 0,
						allRecord);
				request.setAttribute("list", list);
				request.getRequestDispatcher("/pages/student/studentPage.jsp")
						.forward(request, response);
			} else if (methodName.equals("updatePasswordPage")) {
				updatePasswordPage(request, response);

			} else if (methodName.equals("updatePassword")) {
				try {
					updatePassword(request, response);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			} else {
				request.setAttribute("message", "口令出错");
				request.getRequestDispatcher("/pages/message.jsp").forward(
						request, response);
			}
		}
	}

	public void updatePassword(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			NoSuchAlgorithmException {
		String uid = request.getParameter("id");
		String password = request.getParameter("password");
		Student student = studentDao.findStudentById(Integer.parseInt(uid));
		student.setPassword(Tools.encode(password));
		int i = studentDao.updateStudent(student);
		if (i > 0) {
			request.setAttribute("message", "更新成功，请记住新密码！");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}

	public void updatePasswordPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Object uid = request.getSession().getAttribute("userid");
		if (uid != null) {
			int eid = Integer.parseInt(uid + "");
			Student student = studentDao.findStudentById(eid);
			request.setAttribute("users", student);
			request.getRequestDispatcher("/pages/users/updatepassword.jsp")
					.forward(request, response);

		} else {
			request.setAttribute("message", "出错啦");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void findAllStudent(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String showpage = request.getParameter("showpage");
		if (showpage == "" || showpage == null) {// 当前页
			showpage = "1";
		}
		Student student = new Student();
		student.setCode(Tools.encode(request.getParameter("code")));
		student.setName(Tools.encode(request.getParameter("name")));
		int allRecord = studentDao.findAllCounts(student);
		int pageIndex = Integer.parseInt(showpage);
		int pageSize = Tools.PageSize;
		int firstRecord = (pageIndex - 1) * pageSize;
		int lastRecord = pageIndex * pageSize;
		List<Student> list = studentDao.findAllStudent(student, firstRecord,
				lastRecord);
		PagingModel pagingModel = new PagingModel();
		pagingModel.setPerR(pageSize);
		pagingModel.setCurrentP(showpage);
		pagingModel.setAllR(allRecord);
		pagingModel.setAllP();
		pagingModel.setPageInfo();
		pagingModel
				.setPageLink("servlet/StudentServlet?method=findAllStudent&code="
						+ student.getCode() + "&name=" + student.getName());
		request.setAttribute("list", list);
		request.setAttribute("student", student);
		request.setAttribute("pagingModel", pagingModel);
		request.setAttribute("currentp", pageSize);
		request.setAttribute("pagenum", showpage);
		request.getRequestDispatcher("/pages/student/studentlist.jsp").forward(
				request, response);
	}

	public void updatePage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if (id != null && !id.equals("")) {
			int tid = Integer.parseInt(id);
			Student student = studentDao.findStudentById(tid);
			request.setAttribute("student", student);
			int allRecord = studentDao.findAllCounts(null);
			List<Student> list = studentDao.findAllStudent(null, 0, allRecord);
			request.setAttribute("list", list);
			request.setAttribute("flag", request.getParameter("flag"));
			request.getRequestDispatcher("/pages/student/studentPage.jsp")
					.forward(request, response);

		} else {
			request.setAttribute("message", "出错啦");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}

	public void deleteStudent(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if (id != null && !id.equals("")) {
			int tid = Integer.parseInt(id);
			studentDao.deleteStudentById(tid);
			request
					.getRequestDispatcher(
							"StudentServlet?method=findAllStudent").forward(
							request, response);
		} else {
			request.setAttribute("message", "删除失败，请确认");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}

	public void saveOrUpdateStudent(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			NoSuchAlgorithmException {

		String flag = request.getParameter("flag");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String code = request.getParameter("code");
		String college = request.getParameter("college");
		String realname = request.getParameter("realname");
		String tel = request.getParameter("tel");
		String password = request.getParameter("password");
		Student student = new Student();
		student.setName(Tools.encode(name));
		student.setCode(Tools.encode(code));
		student.setRealname(Tools.encode(realname));
		student.setTel(Tools.encode(tel));
		student.setCollege(Tools.encode(college));
		student.setPassword(Tools.encode(password));
		int i = 0;
		if (id != null && !id.equals("")) {
			student.setId(Integer.parseInt(id));
			i = studentDao.updateStudent(student);
			if (i > 0) {

				request.setAttribute("url",
						"servlet/StudentServlet?method=updatePage&id=" + id
								+ "&flag=1");

				

				request.setAttribute("message", "保存成功");
				request.getRequestDispatcher("/pages/message.jsp").forward(
						request, response);
			}
		} else {
			i = studentDao.insertStudent(student);
			if (i > 0) {
				request.setAttribute("url",
				"login.jsp");
				request.setAttribute("message", "保存成功");
				request.getRequestDispatcher("/pages/message.jsp").forward(
						request, response);
			}
		}

	}

}
