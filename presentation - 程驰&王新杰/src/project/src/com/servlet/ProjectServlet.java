package com.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.File;
import com.bean.Money;
import com.bean.PagingModel;
import com.bean.Project;
import com.bean.Student;
import com.dao.MoneyDao;
import com.dao.ProjectDao;
import com.dao.StudentDao;
import com.tool.Tools;

public class ProjectServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ProjectDao projectDao = new ProjectDao();
	StudentDao studentDao = new StudentDao();
	MoneyDao moneyDao = new MoneyDao();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");
		if (methodName != null && methodName != "") {
			 if (methodName.equals("findAllProject")) {
				findAllProject(request, response);
			} else if (methodName.equals("updatePage")) {
				updatePage(request, response);
			} else if (methodName.equals("deleteProject")) {
				deleteProject(request, response);
			} else if (methodName.equals("deleteMoney")) {
				try {
					deleteMoney(request, response);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (methodName.equals("saveProject")) {
				try {
					saveOrUpdateProject(request, response);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (methodName.equals("addProject")) {
				request.setAttribute("flag", "1");
				request.getRequestDispatcher("/pages/project/projectPage.jsp")
						.forward(request, response);
			} else if (methodName.equals("verifyListPage")) {
				findAllProject(request, response);
			} else if (methodName.equals("verifyPage")) {
				updatePage(request, response);
			} else if (methodName.equals("showPage")) {
				updatePage(request, response);
			} else if (methodName.equals("verify")) {
				try {
					verify(request, response);
				} catch (NoSuchAlgorithmException e) {

					e.printStackTrace();
				}
			} else if (methodName.equals("saveMoney")) {
				try {
					saveMoney(request, response);
				} catch (NoSuchAlgorithmException e) {

					e.printStackTrace();
				}
			} else if (methodName.equals("saveCheckinfo")) {
				try {
					saveCheckinfo(request, response);
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
	 
	private void saveOrUpdateProject(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			NoSuchAlgorithmException {
		String flag = Tools.encode(request.getParameter("flag"));
		String id = Tools.encode(request.getParameter("id"));
		String uid = request.getSession().getAttribute("userid") + "";
		String header = Tools.encode(request.getParameter("header"));
		String tel = Tools.encode(request.getParameter("tel"));
		String name = Tools.encode(request.getParameter("name"));
		String college = Tools.encode(request.getParameter("college"));
		String participant = Tools.encode(request.getParameter("participant"));
		String teacher = Tools.encode(request.getParameter("teacher"));
		String teachertel = Tools.encode(request.getParameter("teachertel"));
		String teachertitle = Tools
				.encode(request.getParameter("teachertitle"));
		String unit = Tools.encode(request.getParameter("unit"));
		String signdate = Tools.encode(request.getParameter("signdate"));
		String fileurl = Tools.encode(request.getParameter("fileurl"));
		String verify = Tools.encode(request.getParameter("verify"));
		Project project = new Project();
		project.setName(name);
		project.setHeader(header);
		project.setTel(tel);
		project.setCollege(college);
		project.setParticipant(participant);
		project.setTeacher(teacher);
		project.setTeachertel(teachertel);
		project.setTeachertitle(teachertitle);
		project.setUnit(unit);
		project.setSigndate(signdate);
		project.setFileurl(fileurl);
		project.setVerify((verify == null || verify.equals("")) ? "0" : verify);
		project.setUid(Integer.parseInt(uid));
		int i = 0;
		if (id != null && !id.equals("")) {
			project.setId(Integer.parseInt(id));
			i = projectDao.updateProject(project);
		} else {
			i = projectDao.insertProject(project);
		}

		if (i > 0) {
			request.setAttribute("url",
					"servlet/ProjectServlet?method=findAllProject" + "&flag="
							+ flag);

			request.setAttribute("message", "保存成功");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}

	}

	private void deleteProject(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if (id != null && !id.equals("")) {
			int tid = Integer.parseInt(id);
			projectDao.deleteProjectById(tid);
			request.getRequestDispatcher("ProjectServlet?method=findAllProject&flag=1")
					.forward(request, response);
		} else {
			request.setAttribute("message", "删除失败，请确认");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}

	}

	private void updatePage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if (id != null && !id.equals("")) {
			int tid = Integer.parseInt(id);
			Project project = projectDao.findProjectById(tid);
			request.setAttribute("project", project);
			int allRecord = projectDao.findAllCounts(null);
			List<Project> list = projectDao.findAllProject(null, 0, allRecord);
			String flag = request.getParameter("flag");
			request.setAttribute("list", list);
			request.setAttribute("flag", flag); 
			List<Money> listM=moneyDao.findMoneyByPid(tid);
			request.setAttribute("listM",listM);  
			if (flag.equals("1")) {
				request.getRequestDispatcher("/pages/project/projectPage.jsp")
						.forward(request, response);
			}
			if (flag.equals("2")) {
				request.getRequestDispatcher(
						"/pages/project/projectVerifyPage.jsp").forward(
						request, response);
			}
			if (flag.equals("3")) {
				String mid = request.getParameter("mid");
				mid=(mid==null||mid.equals("")?"0":mid);
				Money money=moneyDao.findMoneyById(Integer.parseInt(mid));
				request.setAttribute("money",money);  
				request.getRequestDispatcher("/pages/project/moneyPage.jsp")
						.forward(request, response);
			}
			if (flag.equals("4")) {
				request.getRequestDispatcher("/pages/project/checkPage.jsp")
						.forward(request, response);
			}
			if (flag.equals("5")) {
				request.getRequestDispatcher("/pages/project/showPage.jsp")
						.forward(request, response);
			}
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

	public void findAllProject(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String showpage = request.getParameter("showpage");
		if (showpage == "" || showpage == null) {// 当前页
			showpage = "1";
		}
		String uid = request.getSession().getAttribute("userid").toString();
		Project project = new Project();
		String flag = request.getParameter("flag");
		if (flag.equals("1")||flag.equals("6")) {
			project.setUid(Integer.parseInt(uid));
		}
		project.setVerify(Tools.encode(request.getParameter("verify")));
		project.setName(Tools.encode(request.getParameter("name")));
		project.setTeacher(Tools.encode(request.getParameter("teacher")));
		int allRecord = projectDao.findAllCounts(project);
		int pageIndex = Integer.parseInt(showpage);
		int pageSize = Tools.PageSize;
		int firstRecord = (pageIndex - 1) * pageSize;
		int lastRecord = pageIndex * pageSize;
		List<Project> list = projectDao.findAllProject(project, firstRecord,
				lastRecord);
		PagingModel pagingModel = new PagingModel();
		pagingModel.setPerR(pageSize);
		pagingModel.setCurrentP(showpage);
		pagingModel.setAllR(allRecord);
		pagingModel.setAllP();
		pagingModel.setPageInfo();
		pagingModel
				.setPageLink("servlet/ProjectServlet?method=findAllProject&flag="
						+ flag
						+ "&name="
						+ project.getName()
						+ "&teacher="
						+ project.getTeacher()
						+ "&verify="
						+ project.getVerify());
		request.setAttribute("list", list);
		request.setAttribute("flag", flag);
		request.setAttribute("pagingModel", pagingModel);
		request.setAttribute("currentp", pageSize);
		request.setAttribute("pagenum", showpage);
		request.setAttribute("project", project);
		if (flag.equals("1"))
			request.getRequestDispatcher("/pages/project/projectlist.jsp")
					.forward(request, response);
		if (flag.equals("2"))
			request
					.getRequestDispatcher(
							"/pages/project/projectVerifylist.jsp").forward(
							request, response);
		if (flag.equals("3"))
			request.getRequestDispatcher("/pages/project/projectMoneylist.jsp")
					.forward(request, response);
		if (flag.equals("4"))
			request.getRequestDispatcher("/pages/project/projectChecklist.jsp")
					.forward(request, response);
		if (flag.equals("5"))
			request.getRequestDispatcher("/pages/project/projectAlllist.jsp")
					.forward(request, response);
		if (flag.equals("6"))
			request.getRequestDispatcher("/pages/project/projectMoneylist.jsp")
					.forward(request, response);
	}

	public void verify(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NoSuchAlgorithmException {
		String id = request.getParameter("id");
		String verify = request.getParameter("verify");
		Project project = new Project();
		project = projectDao.findProjectById(Integer.parseInt(id));
		project.setVerify(Tools.encode(verify));
		int i = projectDao.updateProject(project);
		if (i > 0) {
			request
					.setAttribute("url",
							"servlet/ProjectServlet?method=verifyListPage&flag=2&verify=0");
			request.setAttribute("message", "审批成功");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}

	public void saveMoney(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			NoSuchAlgorithmException {
		String pid = request.getParameter("pid");
		String id = request.getParameter("id");
		String money = request.getParameter("money");
		String givedate = request.getParameter("givedate"); 
		String purpose = request.getParameter("purpose");
		Money moneyinfo = new Money(); 
		moneyinfo.setPid(Integer.parseInt(pid));
		moneyinfo.setMoney(Tools.encode(money));
		moneyinfo.setGivedate(Tools.encode(givedate));
		moneyinfo.setPurpose(Tools.encode(purpose));
		int i = 0;
		if (id != null && !id.equals("")&& !id.equals("0")) {
			moneyinfo.setId(Integer.parseInt(id));
			i = moneyDao.updateMoney(moneyinfo);
		} else {
			i = moneyDao.insertMoney(moneyinfo);
		}

		if (i > 0) {
			request.setAttribute("url",
					"servlet/ProjectServlet?method=updatePage" + "&flag=3&id="+pid);

			request.setAttribute("message", "资金保存成功");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
		
	}

	public void deleteMoney(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			NoSuchAlgorithmException {
		String id = request.getParameter("id");
		if (id != null && !id.equals("")) {
			int tid = Integer.parseInt(id);
			moneyDao.deleteMoneyById(tid);
			request.getRequestDispatcher("ProjectServlet?method=updatePage&flag=3&id="+request.getParameter("pid"))
					.forward(request, response);
		} else {
			request.setAttribute("message", "删除失败，请确认");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}

	public void saveCheckinfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			NoSuchAlgorithmException {
		String id = request.getParameter("id");
		String checkinfo = request.getParameter("checkinfo");
		String checkdate = request.getParameter("checkdate");
		String evaluate = request.getParameter("evaluate");
		Project project = new Project();
		project = projectDao.findProjectById(Integer.parseInt(id));
		project.setCheck(Tools.encode(checkinfo));
		project.setEvaluate(Tools.encode(evaluate));
		project.setCheckdate(Tools.encode(checkdate));
		int i = projectDao.updateProject(project);
		if (i > 0) {
			request
					.setAttribute("url",
							"servlet/ProjectServlet?method=findAllProject&flag=4&verify=1");
			request.setAttribute("message", "验收保存成功");
			request.getRequestDispatcher("/pages/message.jsp").forward(request,
					response);
		}
	}

}
