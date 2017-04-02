package com.datamigration.model;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.json.simple.JSONObject;

@SuppressWarnings("serial")
@ManagedBean(name = "requestLogDetails")
@ApplicationScoped
public class RequestLogDetails implements ModelUtil<RequestLogDetails>,
		Serializable {

	private int requestlogid;
	private int requestid;
	private String requesttype;
	private String startdate;
	private String enddate;
	private String tablename;
	private String targetdirectory;
	private int count;
	private String executiontime;
	private String jobstatus;
	private String schemamismatch;
	private String mismatchtable;
	private String reconstatus;
	private String reconfile;
	private String logfile;

	public String getMismatchtable() {
		return mismatchtable;
	}

	public void setMismatchtable(String mismatchtable) {
		this.mismatchtable = mismatchtable;
	}

	public String getReconfile() {
		return reconfile;
	}

	public void setReconfile(String reconfile) {
		this.reconfile = reconfile;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public int getRequestlogid() {
		return requestlogid;
	}

	public void setRequestlogid(int requestlogid) {
		this.requestlogid = requestlogid;
	}

	public int getRequestid() {
		return requestid;
	}

	public void setRequestid(int requestid) {
		this.requestid = requestid;
	}

	public String getRequesttype() {
		return requesttype;
	}

	public void setRequesttype(String requesttype) {
		this.requesttype = requesttype;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getExecutiontime() {
		return executiontime;
	}

	public void setExecutiontime(String executiontime) {
		this.executiontime = executiontime;
	}

	public String getTargetdirectory() {
		return targetdirectory;
	}

	public void setTargetdirectory(String targetdirectory) {
		this.targetdirectory = targetdirectory;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getJobstatus() {
		return jobstatus;
	}

	public void setJobstatus(String jobstatus) {
		this.jobstatus = jobstatus;
	}

	public String getReconstatus() {
		return reconstatus;
	}

	public void setReconstatus(String reconstatus) {
		this.reconstatus = reconstatus;
	}

	public String getSchemamismatch() {
		return schemamismatch;
	}

	public void setSchemamismatch(String schemamismatch) {
		this.schemamismatch = schemamismatch;
	}

	public String getLogfile() {
		return logfile;
	}

	public void setLogfile(String logfile) {
		this.logfile = logfile;
	}

	@Override
	public RequestLogDetails fromJson(JSONObject json) {
		this.requestlogid = (int) json.get("requestlogid");
		this.requestid = (int) (long) json.get("requestid");
		this.requesttype = (String) json.get("requesttype");
		this.startdate = (String) json.get("startdate");
		this.enddate = (String) json.get("enddate");
		this.count = (int) (long) json.get("count");
		this.targetdirectory = (String) json.get("targetdirectory");
		this.jobstatus = (String) json.get("jobstatus");
		this.schemamismatch = (String) json.get("schemamismatch");
		this.mismatchtable = (String) json.get("mismatchtable");
		this.reconstatus = (String) json.get("reconstatus");
		this.reconfile = (String) json.get("reconfile");
		this.logfile = (String) json.get("logfile");
		this.tablename = (String) json.get("tablename");
		this.executiontime = (String) json.get("executiontime");
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJson(RequestLogDetails obj) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("requestlogid", obj.requestlogid);
		jsonObj.put("requestid", obj.requestid);
		jsonObj.put("requesttype", obj.requesttype);
		jsonObj.put("startdate", obj.startdate);
		jsonObj.put("enddate", obj.enddate);
		jsonObj.put("count", obj.count);
		jsonObj.put("targetdirectory", obj.targetdirectory);
		jsonObj.put("jobstatus", obj.jobstatus);
		jsonObj.put("schemamismatch", obj.schemamismatch);
		jsonObj.put("mismatchtable", obj.mismatchtable);
		jsonObj.put("reconstatus", obj.reconstatus);
		jsonObj.put("reconfile", obj.reconfile);
		jsonObj.put("logfile", obj.logfile);
		jsonObj.put("tablename", obj.tablename);
		jsonObj.put("executiontime", obj.executiontime);
		return jsonObj;
	}
}
