package com.datamigration.model;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.datamigration.util.PasswordCryption;
import com.google.gson.Gson;

@SuppressWarnings("serial")
@ManagedBean(name = "userBean")
@ViewScoped
public class UserBean implements Serializable {

	private List<UserDetails> usersList;
	private String jsonUsersList;

	@ManagedProperty(value = "#{restClient}")
	private RestClient restClient;
	private Response response;

	private UserDetails selectedUser;
	private UserDetails newUser = new UserDetails();

	public List<UserDetails> getUsersList() {
		if (usersList.isEmpty()) {
			try {
				loadUserDetails();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return usersList;
	}

	public void setUsersList(List<UserDetails> usersList) {
		this.usersList = usersList;
	}

	public String getJsonUsersList() {
		return jsonUsersList;
	}

	public void setJsonUsersList(String jsonUsersList) {
		this.jsonUsersList = jsonUsersList;
	}

	public UserDetails getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(UserDetails selectedUser) {
		this.selectedUser = selectedUser;
	}

	public UserDetails getNewUser() {
		return newUser;
	}

	public void setNewUser(UserDetails newUser) {
		this.newUser = newUser;
	}

	@PostConstruct
	public void init() {
		try {
			loadUserDetails();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void loadUserDetails() throws ParseException {
		response = restClient.clientGetResponse("Users/userList");
		int status = response.getStatus();
		if (status == 200) {
			jsonUsersList = response.readEntity(String.class);
			UserDetails[] details = new Gson().fromJson(jsonUsersList,
					UserDetails[].class);
			usersList = Arrays.asList(details);
			System.out.println(usersList);
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
	}

	public void addUser() {
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		ResourceBundle bundle = ResourceBundle.getBundle("parameters");
		String key = bundle.getString("key");
		String encPwd;
		try {
			encPwd = PasswordCryption.encrypt(newUser.getPassword(), key);
			newUser.setPassword(encPwd);
			JSONObject jsonRequest = newUser.toJson(newUser);
			response = restClient.clientPostResponse("Users/addUser",
					jsonRequest.toJSONString());
			int status = response.getStatus();
			if (status == 200) {
				try {
					externalContext.redirect(externalContext
							.getRequestContextPath()
							+ "/faces/userdetails.xhtml");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidKeySpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					"",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e1
							.getMessage()));
		}
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
}
