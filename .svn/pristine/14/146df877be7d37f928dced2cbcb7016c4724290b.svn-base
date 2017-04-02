package com.datamigration.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
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

import com.google.gson.Gson;

@SuppressWarnings("serial")
@ManagedBean(name = "serversBean")
@ViewScoped
public class ServersBean implements Serializable {

	private List<ServerDetails> serversList;
	private String jsonServersList;

	@ManagedProperty(value = "#{restClient}")
	private RestClient restClient;
	private Response response;

	private ServerDetails selectedServer;
	private ServerDetails newServer = new ServerDetails();

	public ServerDetails getNewServer() {
		return newServer;
	}

	public void setNewServer(ServerDetails newServer) {
		this.newServer = newServer;
	}

	public ServerDetails getSelectedServer() {
		return selectedServer;
	}

	public void setSelectedServer(ServerDetails selectedServer) {
		this.selectedServer = selectedServer;
	}

	@PostConstruct
	public void init() {
		try {
			loadServerDetails();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public List<ServerDetails> getServersList() {
		if (serversList.isEmpty()) {
			try {
				loadServerDetails();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return serversList;
	}

	public void setServersList(List<ServerDetails> serversList) {
		this.serversList = serversList;
	}

	public void loadServerDetails() throws ParseException {
		response = restClient.clientGetResponse("Servers/serverList");
		int status = response.getStatus();
		if (status == 200) {
			jsonServersList = response.readEntity(String.class);
			ServerDetails[] details = new Gson().fromJson(jsonServersList, ServerDetails[].class);
			serversList = Arrays.asList(details);
			System.out.println(serversList);
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
			FacesContext.getCurrentInstance().addMessage("",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", error));
		}
	}

	public void addServer() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		JSONObject jsonRequest = newServer.toJson(newServer);
		response = restClient.clientPostResponse("Servers/addServer", jsonRequest.toJSONString());
		int status = response.getStatus();
		if (status == 200) {
			try {
				externalContext.redirect(externalContext.getRequestContextPath() + "/faces/serverdetails.xhtml");
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
			FacesContext.getCurrentInstance().addMessage("",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", error));
		}
	}

	public RestClient getRestClient() {
		return restClient;
	}

	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}

	public String getJsonServersList() {
		return jsonServersList;
	}

	public void setJsonServersList(String jsonServersList) {
		this.jsonServersList = jsonServersList;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
}
