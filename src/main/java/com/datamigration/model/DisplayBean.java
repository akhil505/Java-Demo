package com.datamigration.model;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.datamigration.util.HDFSUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "displayBean")
@ViewScoped
public class DisplayBean implements Serializable {
	private StringBuilder logData;
	private String fileName;
	
	
	public StringBuilder getLogData() {
		System.out.println("in get logData=========");
		return logData;
	}

	public void setLogData(StringBuilder logData) {
		this.logData = logData;
	}
	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void readLines(String fileName) {
		this.fileName=fileName;
		logData = new StringBuilder();
		System.out.println(">>>>" + fileName);
		try {
			HDFSUtil util = new HDFSUtil();
			logData = util.readFile(fileName);
		} catch (IOException e) {
			String error = e.getMessage();
			FacesContext.getCurrentInstance().addMessage(
					"",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							error));
		}
	}
}
