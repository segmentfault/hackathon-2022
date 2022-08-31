/*
 * UploadDisplayer.java
 *
 * Created on Oct 11, 2008, 2008, 1:35:31 PM
 */
package com.tool;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 描述:文件上传 
 */
public class UploadDisplayer extends TagSupport {

    private String fileId;
    private String fileName;
    private String fileType;
    private String fileValue;
    private String fileCss;
    private String btnCss;
    private String base;
    private String readOnly;
    
    private String downname;

    public int doStartTag() throws JspException {
        try {
            String show = "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"" + base +"/servlet/FileDownLoadServlet?name="+downname+"&url="+ fileValue + "\" target=_blank style='border:hidden;border-width=0px'><img src=\"" + base + "/images/rar.gif\" border='0px'  style='border:hidden;border-width=0px'/></a>";
            String outstr = "";
            if (readOnly != null && readOnly.equals("true")) {
                outstr = show;
            } else {
                outstr += "<input type=\"text\" class=\"" + fileCss + "\" value=\"" + (fileValue == null ? "" : fileValue) + "\" name=\"" + fileName + "\" id=\"" + fileId + "\" readonly=\"true\" />";
                outstr += "<input type=\"button\" value=\"上传文件\" class=\"" + btnCss + "\" onclick=\"fileupload('" + fileId + "','" + fileType + "')\" />";
                if (fileValue != null && !fileValue.equals("")) {
                    outstr += show;
                }
            }
            pageContext.getOut().println(outstr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_BODY_INCLUDE;
    }

    public String getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public String getBtnCss() {
        return btnCss;
    }

    public void setBtnCss(String btnCss) {
        this.btnCss = btnCss;
    }

    public String getFileCss() {
        return fileCss;
    }

    public void setFileCss(String fileCss) {
        this.fileCss = fileCss;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileValue() {
        return fileValue;
    }

    public void setFileValue(String fileValue) {
        this.fileValue = fileValue;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

	public String getDownname() {
		return downname;
	}

	public void setDownname(String downname) {
		this.downname = downname;
	}
}
