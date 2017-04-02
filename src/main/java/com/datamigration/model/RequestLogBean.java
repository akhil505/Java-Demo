package com.datamigration.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@SuppressWarnings("serial")
@ManagedBean(name = "requestLogBean")
@ViewScoped
public class RequestLogBean implements Serializable {

	private List<RequestLogDetails> logList;
	private List<RequestLogDetails> lastRunList;
	private List<String> requestTypes;
	private String jsonLogList;

	@ManagedProperty(value = "#{restClient}")
	private RestClient restClient;
	private Response response;

	private RequestLogDetails selectedLog;
	private int tablesCount;
	private int successTablesCount;
	private int failedTablesCount;

	public void init() {
		System.out.println("requestLogbean");
		loadLogList();
	}

	public void loadLogList() {
		response = restClient.clientGetResponse("RequestsLogServices/requestlogList");
		jsonLogList = response.readEntity(String.class);
		System.out.println("----" + jsonLogList);
		RequestLogDetails[] details = new Gson().fromJson(jsonLogList, RequestLogDetails[].class);
		logList = Arrays.asList(details);
		requestTypes = new ArrayList<String>();
		for (RequestLogDetails requestLogs : logList) {
			String reqType = requestLogs.getRequesttype();
			if (!requestTypes.contains(reqType)) {
				requestTypes.add(reqType);
			}
		}
	}

	public void lastRunList() {
		response = restClient.clientGetResponse("RequestsLogServices/lastRunList");
		jsonLogList = response.readEntity(String.class);
	}

	public void reRunRequest() {
		System.out.println("rerun job:" + selectedLog.getRequestid() + "..." + selectedLog.getTablename());
	}

	public String getJsonLogList() {
		return jsonLogList;
	}

	public void setJsonLogList(String jsonLogList) {
		this.jsonLogList = jsonLogList;
	}

	public List<RequestLogDetails> getLogList() {
		return logList;
	}

	public void setLogList(List<RequestLogDetails> logList) {
		this.logList = logList;
	}

	public List<String> getRequestTypes() {
		return requestTypes;
	}

	public void setRequestTypes(List<String> requestTypes) {
		this.requestTypes = requestTypes;
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

	public List<RequestLogDetails> getLastRunList() {
		return lastRunList;
	}

	public void setLastRunList(List<RequestLogDetails> lastRunList) {
		this.lastRunList = lastRunList;
	}

}
