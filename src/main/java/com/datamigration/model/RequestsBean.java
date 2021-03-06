package com.datamigration.model;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DualListModel;

import com.datamigration.util.PasswordCryption;
import com.google.gson.Gson;

@SuppressWarnings("serial")
@ManagedBean(name = "requestsBean")
@ViewScoped
public class RequestsBean implements Serializable {

	private List<RequestDetails> requestList;
	private String jsonRequestList;
	private Map<String, List<RequestLogDetails>> logMap;
	private Map<String, List<RequestLogDetails>> lastRunMap;
	private String jsonLogList;

	@ManagedProperty(value = "#{restClient}")
	private RestClient restClient;
	private Response response;

	@ManagedProperty(value = "#{requestDetails}")
	private RequestDetails selectedRequest;
	private RequestDetails newRequest = new RequestDetails();

	@ManagedProperty(value = "#{serverDetails}")
	private ServerDetails selectedServer;

	private Map<String, String> databases;
	private Map<String, String> columns;
	private DualListModel<String> tables = new DualListModel<String>(
			new ArrayList<String>(), new ArrayList<String>());
	private RequestLogDetails selectedLog;
	private int tablesCount;
	private int successTablesCount;
	private int failedTablesCount;
	private Map<String, String> criteriaCols;
	private Map<String, String> loadTypes;
	private boolean displaysplit = false;
	private boolean displayrowKey = false;
	private boolean displayMappers = false;
	private String loginUser;

	@PostConstruct
	public void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		LoginBean lbean = (LoginBean) context.getExternalContext()
				.getSessionMap().get("loginBean");
		loginUser = "";
		if (lbean != null) {
			loginUser = lbean.getLoginName();
		} else {
			context.addMessage("", new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "OOPS",
					"Session TimedOut!! Please login again"));
			context.getExternalContext().invalidateSession();
			try {
				context.getExternalContext().redirect(
						context.getExternalContext().getRequestContextPath()
								+ "/faces/login.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		loadRequestDetails();
		if (!"admin".equalsIgnoreCase(loginUser)) {
			lastRunList();
		}
	}

	public void loadRequestDetails() {
		response = restClient
				.clientGetResponse("RequestsServices/requestList?user="
						+ loginUser);
		int status = response.getStatus();
		if (status == 200) {
			jsonRequestList = response.readEntity(String.class);
			System.out.println("----" + jsonRequestList);
			RequestDetails[] details = new Gson().fromJson(jsonRequestList,
					RequestDetails[].class);
			requestList = Arrays.asList(details);
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

	public void submitAdhocRequest() {

	}

	public void loadReqLogDetails() {
		response = restClient
				.clientGetResponse("RequestsLogServices/requestlogList?id="
						+ selectedRequest.getRequestid());
		int status = response.getStatus();
		if (status == 200) {
			jsonLogList = response.readEntity(String.class);
			System.out.println("----" + jsonLogList);
			RequestLogDetails[] details = new Gson().fromJson(jsonLogList,
					RequestLogDetails[].class);
			List<RequestLogDetails> logList = Arrays.asList(details);
			logMap = new HashMap<String, List<RequestLogDetails>>();
			for (RequestLogDetails requestLogs : logList) {
				System.out.println("++++" + requestLogs);
				String reqType = requestLogs.getRequesttype();
				List<RequestLogDetails> logs = new ArrayList<RequestLogDetails>();
				if (logMap.containsKey(reqType)) {
					logs = logMap.get(reqType);
				}
				logs.add(requestLogs);
				logMap.put(reqType, logs);
			}
			System.out.println(logMap);
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

	public void fetchDatabases() {
		System.out.println("serverid: " + selectedServer.getServerid());
		newRequest.setServerid(selectedServer.getServerid());
		JSONObject jsonRequest = newRequest.toJson(newRequest);
		System.out.println(newRequest.getUsername() + "...."
				+ newRequest.getRequestname());
		response = restClient.clientPostResponse(
				"MetaDataService/getDatabases", jsonRequest.toJSONString());
		int status = response.getStatus();
		if (status == 200) {
			String responseJson = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			try {
				JSONObject jObj = (JSONObject) parser.parse(responseJson);
				System.out.println("in Success");
				JSONArray databaseArray = (JSONArray) jObj.get("databases");
				databases = new HashMap<String, String>();
				for (int i = 0; i < databaseArray.size(); i++) {
					String database = databaseArray.get(i).toString();
					databases.put(database, database);
				}
			} catch (ParseException e) {
				String error = "Could not fetch the databases List";
				FacesContext.getCurrentInstance().addMessage(
						"",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
								error));
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
	}

	public String onFlowProcess(FlowEvent event) {
		return event.getNewStep();
	}

	public void fetchTables() {
		System.out.println("serverid: " + selectedServer.getServerid());
		newRequest.setServerid(selectedServer.getServerid());
		JSONObject jsonRequest = newRequest.toJson(newRequest);
		System.out.println(newRequest.getUsername() + "...."
				+ newRequest.getSourcedatabase());
		response = restClient.clientPostResponse("MetaDataService/getTables",
				jsonRequest.toJSONString());
		int status = response.getStatus();
		if (status == 200) {
			String tablesJson = response.readEntity(String.class);
			JSONParser parser = new JSONParser();
			try {
				JSONObject jObj = (JSONObject) parser.parse(tablesJson);
				JSONArray tableArray = (JSONArray) jObj.get("tables");
				List<String> tablesSource = new ArrayList<String>();
				List<String> tablesTarget = new ArrayList<String>();
				for (int i = 0; i < tableArray.size(); i++) {
					String table = tableArray.get(i).toString();
					tablesSource.add(table);
					System.out.println(table);
				}
				tables = new DualListModel<String>(tablesSource, tablesTarget);
			} catch (ParseException e) {
				String error = "Could not fetch the tables List";
				FacesContext.getCurrentInstance().addMessage(
						"",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
								error));
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
	}

	public void fetchTableDetails() {

		newRequest.setServerid(selectedServer.getServerid());
		List<String> targetTables = tables.getTarget();
		StringBuffer strTables = new StringBuffer();
		for (String tabs : targetTables) {
			strTables.append(" ," + tabs);
		}

		newRequest.setSourcetables(strTables.toString().substring(2));
		JSONObject jsonRequest = newRequest.toJson(newRequest);
		System.out.println(newRequest.getSourcetables() + "...."
				+ newRequest.getStoragetarget());
		System.out.println("tables: " + newRequest.getSourcetables());
		loadTypes = new HashMap<String, String>();
		loadTypes.put("Full", "Full");
		if (targetTables.size() == 1) {
			displayMappers = true;
			response = restClient.clientPostResponse(
					"MetaDataService/getTableDetails",
					jsonRequest.toJSONString());
			int status = response.getStatus();
			if (status == 200) {
				String tableDetailsJson = response.readEntity(String.class);
				JSONParser parser = new JSONParser();
				try {
					JSONObject jObj = (JSONObject) parser
							.parse(tableDetailsJson);
					JSONArray tableArray = (JSONArray) jObj.get("TableColumns");
					columns = new HashMap<String, String>();
					criteriaCols = new HashMap<String, String>();
					displayrowKey = true;
					displaysplit = true;
					for (int i = 0; i < tableArray.size(); i++) {
						JSONObject table = (JSONObject) tableArray.get(i);
						String dataType = table.get("DataType").toString();
						String primaryKey = table.get("PrimaryKey").toString();
						String columnName = table.get("ColumnName").toString();
						if ("hbase".equalsIgnoreCase(newRequest
								.getStoragetarget())) {
							displaysplit = false;
							if ("YES".equalsIgnoreCase(primaryKey)) {
								newRequest.setRowkey(columnName);
								newRequest.setSplitbycolumn(null);
								displayrowKey = false;
							} else {
								columns.put(columnName, columnName);
							}
						} else {
							displayrowKey = false;
							if ("YES".equalsIgnoreCase(primaryKey)) {
								newRequest.setSplitbycolumn(columnName);
								newRequest.setRowkey(null);
								displaysplit = false;
							} else {
								columns.put(columnName, columnName);
							}
						}
						if ("DATE".equalsIgnoreCase(dataType)
								|| "TIMESTAMP".equalsIgnoreCase(dataType)) {
							loadTypes.put("Daily", "Daily");
							loadTypes.put("Monthly", "Monthly");
							criteriaCols.put(columnName, columnName);
						}
					}
					System.out.println("RowKey:" + displayrowKey);
					System.out.println("SplitKey:" + displaysplit);
				} catch (ParseException e) {
					String error = "Could not fetch the tables List";
					FacesContext.getCurrentInstance().addMessage(
							"",
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"ERROR", error));
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
		} else {
			displayrowKey = false;
			displaysplit = false;
			displayMappers = false;
		}

	}

	public void updateRequest() {
		System.out.println("selected request: "
				+ selectedRequest.getRequestname());
		// String jsonRequest =
		// response =
		// restClient.clientGetResponse("RequestsServices/addRequest?json="+jsonRequest);
	}

	public void addRequest() {
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		LoginBean lbean = (LoginBean) externalContext.getSessionMap().get(
				"loginBean");
		if (lbean == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("", new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "OOPS",
					"Session TimedOut!! Please login again"));
			context.getExternalContext().invalidateSession();
			try {
				context.getExternalContext().redirect(
						externalContext.getRequestContextPath()
								+ "/faces/login.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			loginUser = lbean.getLoginName();
			System.out.println("username!!!!!!!!!!!!" + loginUser);
			newRequest.setRequestedby(loginUser);
		}
		ResourceBundle bundle = ResourceBundle.getBundle("parameters");
		try {
			String encPwd = PasswordCryption.encrypt(newRequest.getPassword(),
					bundle.getString("key"));
			newRequest.setPassword(encPwd);
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidKeySpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject jsonRequest = newRequest.toJson(newRequest);
		response = restClient.clientPostResponse("RequestsServices/addRequest",
				jsonRequest.toJSONString());
		int status = response.getStatus();
		if (status == 200) {
			try {
				externalContext.redirect(externalContext
						.getRequestContextPath()
						+ "/faces/requestdetails.xhtml");
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

	}

	public void runRequest() {
		System.out.println(selectedRequest.getRequestid());
		response = restClient.clientGetResponse("RequestsServices/runSqoop?id="
				+ selectedRequest.getRequestid());
		int status = response.getStatus();
		if (status == 200) {

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

	public void lastRunList() {
		lastRunMap = new HashMap<String, List<RequestLogDetails>>();
		System.out.println("00000000000"
				+ selectedRequest.toJson(selectedRequest));
		response = restClient
				.clientGetResponse("RequestsLogServices/lastRunList?id="
						+ selectedRequest.getRequestid());
		int status = response.getStatus();
		if (status == 200) {
			String requestJson = response.readEntity(String.class);
			RequestLogDetails[] details = new Gson().fromJson(requestJson,
					RequestLogDetails[].class);
			List<RequestLogDetails> logList = Arrays.asList(details);
			lastRunMap = new HashMap<String, List<RequestLogDetails>>();
			for (RequestLogDetails requestLogs : logList) {
				String jobstatus = requestLogs.getReconstatus();
				if ("Not Run".equalsIgnoreCase(jobstatus)) {
					jobstatus = "FAILED";
				}
				List<RequestLogDetails> logs = new ArrayList<RequestLogDetails>();
				if (lastRunMap.containsKey(jobstatus)) {
					logs = lastRunMap.get(jobstatus);
				}
				logs.add(requestLogs);
				List<RequestLogDetails> fullLogs = new ArrayList<RequestLogDetails>();
				if (lastRunMap.containsKey("ALL")) {
					fullLogs = lastRunMap.get("ALL");
				}
				fullLogs.add(requestLogs);
				lastRunMap.put(jobstatus, logs);
				lastRunMap.put("ALL", fullLogs);
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
	}

	public void reRunRequest() {
		System.out.println("rerun job:" + selectedLog.getRequestid() + "..."
				+ selectedLog.getRequestlogid());
		response = restClient.clientGetResponse("RequestsServices/runSqoop?id="
				+ selectedLog.getRequestid() + "&logId="
				+ selectedLog.getRequestlogid());
	}

	public void approveRequest() {
		System.out.println("approve Request:" + selectedRequest.getRequestid()
				+ "...");
		selectedRequest.setProvisionstatus("ACTIVE");
		JSONObject jsonRequest = new RequestDetails().toJson(selectedRequest);
		response = restClient.clientPostResponse("RequestsServices/addRequest",
				jsonRequest.toJSONString());
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		int status = response.getStatus();
		if (status == 200) {
			try {
				externalContext.redirect(externalContext
						.getRequestContextPath()
						+ "/faces/approverequests.xhtml");
			} catch (IOException e) {
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
	}

	public List<RequestDetails> getRequestList() {
		return requestList;
	}

	public void setRequestList(List<RequestDetails> requestList) {
		this.requestList = requestList;
	}

	public String getJsonRequestList() {
		return jsonRequestList;
	}

	public void setJsonRequestList(String jsonRequestList) {
		this.jsonRequestList = jsonRequestList;
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

	public RequestDetails getSelectedRequest() {
		return selectedRequest;
	}

	public void setSelectedRequest(RequestDetails selectedRequest) {
		this.selectedRequest = selectedRequest;
	}

	public RequestDetails getNewRequest() {
		return newRequest;
	}

	public void setNewRequest(RequestDetails newRequest) {
		this.newRequest = newRequest;
	}

	public ServerDetails getSelectedServer() {
		return selectedServer;
	}

	public void setSelectedServer(ServerDetails selectedServer) {
		this.selectedServer = selectedServer;
	}

	public Map<String, String> getDatabases() {
		return databases;
	}

	public void setDatabases(Map<String, String> databases) {
		this.databases = databases;
	}

	public DualListModel<String> getTables() {
		return tables;
	}

	public void setTables(DualListModel<String> tables) {
		this.tables = tables;
	}

	public Map<String, String> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, String> columns) {
		this.columns = columns;
	}

	public Map<String, String> getCriteriaCols() {
		return criteriaCols;
	}

	public void setCriteriaCols(Map<String, String> criteriaCols) {
		this.criteriaCols = criteriaCols;
	}

	public Map<String, String> getLoadTypes() {
		return loadTypes;
	}

	public void setLoadTypes(Map<String, String> loadTypes) {
		this.loadTypes = loadTypes;
	}

	public boolean isDisplaysplit() {
		return displaysplit;
	}

	public void setDisplaysplit(boolean displaysplit) {
		this.displaysplit = displaysplit;
	}

	public boolean isDisplayrowKey() {
		return displayrowKey;
	}

	public void setDisplayrowKey(boolean displayrowKey) {
		this.displayrowKey = displayrowKey;
	}

	public boolean isDisplayMappers() {
		return displayMappers;
	}

	public void setDisplayMappers(boolean displayMappers) {
		this.displayMappers = displayMappers;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public Map<String, List<RequestLogDetails>> getLogMap() {
		return logMap;
	}

	public void setLogMap(Map<String, List<RequestLogDetails>> logMap) {
		this.logMap = logMap;
	}

	public String getJsonLogList() {
		return jsonLogList;
	}

	public void setJsonLogList(String jsonLogList) {
		this.jsonLogList = jsonLogList;
	}

	public RequestLogDetails getSelectedLog() {
		return selectedLog;
	}

	public void setSelectedLog(RequestLogDetails selectedLog) {
		this.selectedLog = selectedLog;
	}

	public int getTablesCount() {
		return tablesCount;
	}

	public void setTablesCount(int tablesCount) {
		this.tablesCount = tablesCount;
	}

	public int getSuccessTablesCount() {
		return successTablesCount;
	}

	public void setSuccessTablesCount(int successTablesCount) {
		this.successTablesCount = successTablesCount;
	}

	public int getFailedTablesCount() {
		return failedTablesCount;
	}

	public void setFailedTablesCount(int failedTablesCount) {
		this.failedTablesCount = failedTablesCount;
	}

	public Map<String, List<RequestLogDetails>> getLastRunMap() {
		return lastRunMap;
	}

	public void setLastRunMap(Map<String, List<RequestLogDetails>> lastRunMap) {
		this.lastRunMap = lastRunMap;
	}

	public void onTabChange(TabChangeEvent event) {
		if (event.getTab().getId().equals("reqDetails")) {
			loadRequestDetails();
			lastRunList();
		} else if (event.getTab().getId().equals("reqLogDetails")) {
			loadReqLogDetails();
		} else if (event.getTab().getId().equals("adhocReq")) {
		}
	}

	public void deleteRequest() {

	}
}
