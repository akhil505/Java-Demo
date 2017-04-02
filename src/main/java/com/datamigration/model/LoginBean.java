package com.datamigration.model;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.datamigration.util.PasswordCryption;
import com.google.gson.Gson;


/**
 * @author
 *
 */
@SuppressWarnings("serial")
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {
	/**
	 * 
	 */
	private static final Logger LOGGER = Logger.getLogger(LoginBean.class);
	/**
	 * 
	 */
	private String loginName;
	/**
	 * 
	 */
	private String password;

	@ManagedProperty(value = "#{restClient}")
	private RestClient restClient;
	private Response response;

	private String jsonUsersList;

	/**
	 * @return
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param loginname
	 */
	public void setLoginName(final String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * @return
	 */
	public String checkValidUser() {
		// LoginAction login = new LoginAction();
		// String status = login.validateLogin(loginname, password);
		LOGGER.info("Login Success");
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		externalContext.invalidateSession();
		UserDetails user = new UserDetails();
		response = restClient.clientGetResponse("Users/userbyname?username="
				+ loginName);
		int status = response.getStatus();
		if (status == 200) {
			jsonUsersList = response.readEntity(String.class);
			user = new Gson().fromJson(jsonUsersList, UserDetails.class);
			System.out.println(user);
		} else {
			String error = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			try {
				JSONObject obj = (JSONObject) parser.parse(error);
				error = obj.get("Message").toString();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FacesContext.getCurrentInstance().addMessage(
					"",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							error));
		}
		String encPwd = user.getPassword();
		System.out.println("____----____"+encPwd);
		ResourceBundle bundle = ResourceBundle.getBundle("parameters");
		String key = bundle.getString("key");
		String pwd = "";
		try {
			pwd = PasswordCryption.decrypt(encPwd, key);
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					"",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.getMessage()));
		}
		if (password.equals(pwd)) {
			LoginBean lbean = new LoginBean();
			lbean.setLoginName(loginName);
			externalContext.getSessionMap().put("loginBean", lbean);
			if ("Admin".equalsIgnoreCase(user.getRole())) {
				return "serverdetails?faces-redirect=true";
			}
			return "homepage?faces-redirect=true";
		}
		return "login?faces-redirect=true";
	}

	/**
	 * @return
	 */
	public String logout() {
		// LoginAction login = new LoginAction();
		// String logoutStatus = login.logout();
		LOGGER.info("Logout Success");
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		externalContext.invalidateSession();
		return "login?faces-redirect=true";
	}

	public String timeout() {
		// LoginAction login = new LoginAction();
		// String logoutStatus = login.logout();
		LOGGER.info("Logout Success");
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"OOPS", "Session TimedOut!! Please login again"));
		context.getExternalContext().invalidateSession();
		return "login";
	}

	public RestClient getRestClient() {
		return restClient;
	}

	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public String getJsonUsersList() {
		return jsonUsersList;
	}

	public void setJsonUsersList(String jsonUsersList) {
		this.jsonUsersList = jsonUsersList;
	}

}
