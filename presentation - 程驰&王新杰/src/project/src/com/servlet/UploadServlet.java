package com.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet {
	/**
	 * Constructor of the object.
	 */
	public UploadServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	@SuppressWarnings( { "unchecked", "deprecation" })
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");
		if (methodName != null && methodName != "") {
			if (methodName.equals("uploadfile")) {
				uploadfile(request, response);
			} else if (methodName.equals("uploadPage")) {
				uploadPage(request, response);
			} else if (methodName.equals("downLoad")) {
				downLoad(request, response);
			}else {
				request.setAttribute("message", "口令出错");
				request.getRequestDispatcher("/pages/message.jsp").forward(
						request, response);
			}
		}

	}

	public void downLoad(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String fileId = request.getParameter("fileId");
		String fileType = request.getParameter("fileType");
		request.setAttribute("fileId", fileId);
		request.setAttribute("fileType", fileType);
		request.getRequestDispatcher("/upload.jsp").forward(request, response);
	}
	public void uploadPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String fileId = request.getParameter("fileId");
		String fileType = request.getParameter("fileType");
		request.setAttribute("fileId", fileId);
		request.setAttribute("fileType", fileType);
		request.getRequestDispatcher("/upload.jsp").forward(request, response);
	}
	@SuppressWarnings( { "unchecked", "deprecation" })
	public void uploadfile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String fileType = request.getParameter("fileType");
		String fileId = request.getParameter("fileId"); 
		String msg = "";
		String filePath = request.getRealPath("/upload");
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdir();
		}

		DiskFileItemFactory fileFactory = new DiskFileItemFactory(1024 * 1024,
				new File(filePath));
		ServletFileUpload upload = new ServletFileUpload(fileFactory);
		try {
			List<FileItem> list = upload.parseRequest(request);
			if (list.size() > 0) {
				FileItem item =list.get(0);
					if (item.isFormField()) {
						String fieldName = item.getFieldName();
						String fieldValue = item.getString("GBK");
						request.setAttribute(fieldName, fieldValue);
						msg = "wrong!";
						request.setAttribute(fieldName, fieldValue);
					} else {
						String fieldName = item.getFieldName();// 文件name属性值
						String fileName1 = item.getName();// 文件本地路径
						int lastIndex = fileName1.lastIndexOf("\\");
						String fileName = fileName1.substring(lastIndex + 1);// 实际文件名
						int pos = fileName.lastIndexOf(".");
						String extName = fileName.substring(pos);
						if (fileType.indexOf(extName) != -1
								|| fileType.equals(".*")) {
							String newfilename = new Date().getTime() + extName;
							request.setAttribute(fieldName, newfilename);
							InputStream inputStream = item.getInputStream();
							OutputStream outputStream = new FileOutputStream(
									new File(filePath, newfilename));
							int lenght = 0;
							byte[] b = new byte[1024 * 1024];
							while ((lenght = inputStream.read(b)) > 0) {
								outputStream.write(b, 0, lenght);
							}
							outputStream.close();
							inputStream.close();
							msg = "success!";
							filePath = "/upload/" + newfilename;
							request.setAttribute("msg", msg); 
						} else {
							msg = "type is diff!";
							request.setAttribute("msg", msg);
						}
					}

				
			} else {

				msg = "is null";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("filePath", filePath);
		request.setAttribute("fileId", fileId);
		request.getRequestDispatcher("/upload.jsp").forward(request, response);

	}

}
