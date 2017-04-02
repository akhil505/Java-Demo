package com.datamigration.model;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.json.simple.JSONObject;

@SuppressWarnings("serial")
@ManagedBean(name = "userDetails")
@ApplicationScoped
public class UserDetails implements ModelUtil<UserDetails>, Serializable {
	private String password;
	private String confirmpassword;
	private String username;
	private String role;
	private int userid;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmpassword() {
		return confirmpassword;
	}

	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	@Override
	public UserDetails fromJson(JSONObject json) {

		// ServerDetails server=new ServerDetails();
		this.userid = (int) (long) json.get("userid");
		this.role = (String) json.get("role");
		this.username = (String) json.get("username");
		this.password = (String) json.get("password");
		this.confirmpassword = (String) json.get("confirmpassword");
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJson(UserDetails obj) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("userid", obj.userid);
		jsonObj.put("role", obj.role);
		jsonObj.put("username", obj.username);
		jsonObj.put("password", obj.password);
		jsonObj.put("confirmpassword", obj.confirmpassword);
		return jsonObj;
	}

}
