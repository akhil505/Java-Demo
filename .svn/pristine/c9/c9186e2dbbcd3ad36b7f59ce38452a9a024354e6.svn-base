package com.datamigration.model;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.json.simple.JSONObject;

@SuppressWarnings("serial")
@ManagedBean(name = "serverDetails")
@ApplicationScoped
public class ServerDetails implements ModelUtil<ServerDetails>, Serializable {
	private int serverid;
	private String servertype;
	private String hostname;
	private String port;
	private String servername;

	public int getServerid() {
		return serverid;
	}

	public void setServerid(int serverid) {
		this.serverid = serverid;
	}

	public String getServertype() {
		return servertype;
	}

	public void setServertype(String servertype) {
		this.servertype = servertype;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	@Override
	public ServerDetails fromJson(JSONObject json) {

		// ServerDetails server=new ServerDetails();
		if (json.get("serverid") != null) {
			this.serverid = (int) ((long) json.get("serverid"));
		}
		this.servertype = ((String) json.get("servertype")).toLowerCase();
		this.hostname = (String) json.get("hostname");
		this.port = (String) json.get("port");
		this.servername = (String) json.get("servername");
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJson(ServerDetails obj) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("serverid", obj.serverid);
		jsonObj.put("servertype", obj.servertype.toUpperCase());
		jsonObj.put("hostname", obj.hostname);
		jsonObj.put("port", obj.port);
		jsonObj.put("servername", obj.servername);
		return jsonObj;
	}

}
