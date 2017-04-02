package com.datamigration.model;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.json.simple.JSONObject;

@SuppressWarnings("serial")
@ManagedBean(name = "requestDetails")
@ApplicationScoped
public class RequestDetails implements ModelUtil<RequestDetails>, Serializable {

	private int requestid;
	private String requestname;
	private int serverid;
	private String sourcedatabase;
	private String sourcetables;
	private String sourcetable;
	private String username;
	private String password;
	private String storagetarget;
	private String storageformat;
	private String storagecompression;
	private String targetdirectory;
	private String criteriacolumn;
	private int mappers;
	private String splitbycolumn;
	private String rowkey;
	private String loadtype;
	private String provisionstatus;
	private String requestedby;
	private ServerDetails serverdetails;
	private String servername;
	private String serverhost;
	private String serverport;
	private String type;

	public void setSourcetables(String sourcetables) {
		this.sourcetables = sourcetables;
	}

	public int getRequestid() {
		return requestid;
	}

	public void setRequestid(int requestid) {
		this.requestid = requestid;
	}

	public String getRequestname() {
		return requestname;
	}

	public void setRequestname(String requestname) {
		this.requestname = requestname;
	}

	public int getServerid() {
		return serverid;
	}

	public void setServerid(int serverid) {
		this.serverid = serverid;
	}

	public String getSourcedatabase() {
		return sourcedatabase;
	}

	public void setSourcedatabase(String sourcedatabase) {
		this.sourcedatabase = sourcedatabase;
	}

	public String getSourcetables() {
		return sourcetables;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStoragetarget() {
		return storagetarget;
	}

	public void setStoragetarget(String storagetarget) {
		this.storagetarget = storagetarget;
	}

	public String getStorageformat() {
		return storageformat;
	}

	public void setStorageformat(String storageformat) {
		this.storageformat = storageformat;
	}

	public String getStoragecompression() {
		return storagecompression;
	}

	public void setStoragecompression(String storagecompression) {
		this.storagecompression = storagecompression;
	}

	public String getTargetdirectory() {
		return targetdirectory;
	}

	public void setTargetdirectory(String targetdirectory) {
		this.targetdirectory = targetdirectory;
	}

	public String getCriteriacolumn() {
		return criteriacolumn;
	}

	public void setCriteriacolumn(String criteriacolumn) {
		this.criteriacolumn = criteriacolumn;
	}

	public int getMappers() {
		return mappers;
	}

	public void setMappers(int mappers) {
		this.mappers = mappers;
	}

	public String getSplitbycolumn() {
		return splitbycolumn;
	}

	public void setSplitbycolumn(String splitbycolumn) {
		this.splitbycolumn = splitbycolumn;
	}

	public String getRowkey() {
		return rowkey;
	}

	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}

	public String getProvisionstatus() {
		return provisionstatus;
	}

	public void setProvisionstatus(String provisionstatus) {
		this.provisionstatus = provisionstatus;
	}

	public String getRequestedby() {
		return requestedby;
	}

	public void setRequestedby(String requestedby) {
		this.requestedby = requestedby;
	}

	public String getLoadtype() {
		return loadtype;
	}

	public void setLoadtype(String loadtype) {
		this.loadtype = loadtype;
	}

	public ServerDetails getServerdetails() {
		return serverdetails;
	}

	public void setServerdetails(ServerDetails serverdetails) {
		this.serverdetails = serverdetails;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getServerhost() {
		return serverhost;
	}

	public void setServerhost(String serverhost) {
		this.serverhost = serverhost;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getServerport() {
		return serverport;
	}

	public void setServerport(String serverport) {
		this.serverport = serverport;
	}

	public String getSourcetable() {
		return sourcetable;
	}

	public void setSourcetable(String sourcetable) {
		this.sourcetable = sourcetable;
	}

	@Override
	public RequestDetails fromJson(JSONObject json) {
		if (json.get("requestid") != null) {
			this.requestid = (int) ((long) json.get("requestid"));
		}
		this.serverid = (int) ((long) json.get("serverid"));
		this.sourcedatabase = (String) json.get("sourcedatabase");
		this.sourcetables = (String) json.get("sourcetables");
		this.sourcetable = (String) json.get("sourcetable");
		this.criteriacolumn = (String) json.get("criteriacolumn");
		this.storagetarget = (String) json.get("storagetarget");
		this.storageformat = (String) json.get("storageformat");
		this.storagecompression = (String) json.get("storagecompression");
		this.requestname = (String) json.get("requestname");
		if (json.get("provisionstatus") != null)
			this.provisionstatus = (String) json.get("provisionstatus");
		else {
			this.provisionstatus = "INACTIVE";
		}
		this.mappers = (int) (long) json.get("mappers");
		this.splitbycolumn = (String) json.get("splitbycolumn");
		this.targetdirectory = (String) json.get("targetdirectory");
		this.rowkey = (String) json.get("rowkey");
		this.username = (String) json.get("username");
		this.password = (String) json.get("password");
		this.requestedby = (String) json.get("requestedby");
		this.loadtype = (String) json.get("loadtype");
		this.type = (String) json.get("type");
		this.servername = (String) json.get("servername");
		this.serverhost = (String) json.get("serverhost");
		this.serverport = (String) json.get("serverport");
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJson(RequestDetails obj) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("requestid", obj.requestid);
		jsonObj.put("serverid", obj.serverid);
		jsonObj.put("sourcedatabase", obj.sourcedatabase);
		jsonObj.put("sourcetables", obj.sourcetables);
		jsonObj.put("sourcetable", obj.sourcetable);
		jsonObj.put("criteriacolumn", obj.criteriacolumn);
		jsonObj.put("storagetarget", obj.storagetarget);
		jsonObj.put("storageformat", obj.storageformat);
		jsonObj.put("storagecompression", obj.storagecompression);
		jsonObj.put("provisionstatus", obj.provisionstatus);
		jsonObj.put("mappers", obj.mappers);
		jsonObj.put("splitbycolumn", obj.splitbycolumn);
		jsonObj.put("requestname", obj.requestname);
		jsonObj.put("targetdirectory", obj.targetdirectory);
		jsonObj.put("rowkey", obj.rowkey);
		jsonObj.put("username", obj.username);
		jsonObj.put("password", obj.password);
		jsonObj.put("requestedby", obj.requestedby);
		jsonObj.put("loadtype", obj.loadtype);
		if (obj.serverdetails != null) {
			jsonObj.put("servername", obj.serverdetails.getServername());
			jsonObj.put("serverhost", obj.serverdetails.getHostname());
			jsonObj.put("type", obj.serverdetails.getServertype());
			jsonObj.put("serverport", obj.serverdetails.getPort());
		}
		return jsonObj;
	}

	public Date convertToDate(String stringDate) {
		Date sqlDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date utilDate = dateFormat.parse(stringDate);
			sqlDate = new java.sql.Date(utilDate.getTime());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sqlDate;
	}
}
