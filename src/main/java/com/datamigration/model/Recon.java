package com.datamigration.model;

import org.json.simple.JSONObject;

public class Recon {

	private int requestLogId;
	private String hostName;
	private String port;
	private String serverType;
	private String sourceDatabase;
	private String sourceTable;
	private String loadType;
	private String criteriaColumn;
	private String startDate;
	private String endDate;
	private String storageTarget;
	private String reconfile;
	private String storageFormat;
	private String storageCompression;
	private String targetDirectory;
	private String accessUserName;
	private String accessPassword;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getReconfile() {
		return reconfile;
	}

	public void setReconfile(String reconfile) {
		this.reconfile = reconfile;
	}

	public String getSourceDatabase() {
		return sourceDatabase;
	}

	public void setSourceDatabase(String sourceDatabase) {
		this.sourceDatabase = sourceDatabase;
	}

	public String getSourceTable() {
		return sourceTable;
	}

	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}

	public String getLoadType() {
		return loadType;
	}

	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}

	public String getCriteriaColumn() {
		return criteriaColumn;
	}

	public void setCriteriaColumn(String criteriaColumn) {
		this.criteriaColumn = criteriaColumn;
	}

	public String getStorageTarget() {
		return storageTarget;
	}

	public void setStorageTarget(String storageTarget) {
		this.storageTarget = storageTarget;
	}

	public String getStorageFormat() {
		return storageFormat;
	}

	public void setStorageFormat(String storageFormat) {
		this.storageFormat = storageFormat;
	}

	public String getStorageCompression() {
		return storageCompression;
	}

	public void setStorageCompression(String storageCompression) {
		this.storageCompression = storageCompression;
	}

	public String getTargetDirectory() {
		return targetDirectory;
	}

	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	public String getAccessUserName() {
		return accessUserName;
	}

	public void setAccessUserName(String accessUserName) {
		this.accessUserName = accessUserName;
	}

	public String getAccessPassword() {
		return accessPassword;
	}

	public void setAccessPassword(String accessPassword) {
		this.accessPassword = accessPassword;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getRequestLogId() {
		return requestLogId;
	}

	public void setRequestLogId(int requestLogId) {
		this.requestLogId = requestLogId;
	}

	public Recon fromJson(JSONObject json) {
		String loadType = "";

		if (json.get("requestLogId") != null) {
			this.requestLogId = (int) json.get("requestLogId");
		}
		this.hostName = (String) json.get("hostName");
		this.port = (String) json.get("port");
		this.sourceDatabase = (String) json.get("sourceDatabase");
		this.sourceTable = (String) json.get("sourceTable");
		this.loadType = (String) json.get("loadType");
		this.criteriaColumn = (String) json.get("criteriaColumn");
		this.storageTarget = (String) json.get("storageTarget");
		this.storageFormat = (String) json.get("storageFormat");
		this.storageCompression = (String) json.get("storageCompression");
		this.accessPassword = (String) json.get("accessPassword");
		this.accessUserName = (String) json.get("accessUserName");
		this.serverType = (String) json.get("serverType");
		this.reconfile = (String) json.get("reconfile");
		if (this.loadType != null) {
			loadType = this.loadType.toLowerCase();
		}
		if (this.getStorageTarget() != null) {
			if (this.getStorageTarget().equalsIgnoreCase("hive") || this.getStorageTarget().equalsIgnoreCase("hdfs")) {
				this.targetDirectory = (String) json.get("targetDirectory");
				switch (loadType) {
				case "daily":
				case "monthly":
				case "adhoc":
					this.startDate = (String) json.get("startDate");
					this.endDate = (String) json.get("endDate");
				case "full":
					this.startDate = null;
					this.endDate = null;
				}
			} else {
				/*
				 * this.columnFamily=(String)json.get("columnFamily");
				 * this.rowKey=(String)json.get("rowKey");
				 * this.hbaseTable=(String)json.get("hbaseTable");
				 */
				switch (loadType) {
				case "daily":
				case "monthly":
				case "adhoc":
					this.startDate = (String) json.get("startDate");
					this.endDate = (String) json.get("endDate");
				case "full":
					this.startDate = null;
					this.endDate = null;
				}
			}
		}
		System.out.println(this.getRequestLogId() + "%%%%%%%%%");
		return this;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJson(Recon obj) {
		String loadType = "";
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("requestLogId", obj.requestLogId);
		jsonObj.put("hostname", obj.hostName);
		jsonObj.put("port", obj.port);
		jsonObj.put("serverType", obj.serverType);
		jsonObj.put("sourceDatabase", obj.sourceDatabase);
		jsonObj.put("sourceTable", obj.sourceTable);
		jsonObj.put("loadType", obj.loadType);
		jsonObj.put("criteriaColumn", obj.criteriaColumn);
		jsonObj.put("storageTarget", obj.storageTarget);
		jsonObj.put("reconfile", obj.reconfile);
		jsonObj.put("storageFormat", obj.storageFormat);
		jsonObj.put("storageCompression", obj.storageCompression);
		jsonObj.put("accessPassword", obj.accessPassword);
		jsonObj.put("accessUserName", obj.accessUserName);
		if (this.loadType != null) {
			loadType = this.loadType.toLowerCase();
		}
		if (this.getStorageTarget() != null) {
			if (this.getStorageTarget().equalsIgnoreCase("hive") || this.getStorageTarget().equalsIgnoreCase("hdfs")) {
				jsonObj.put("targetDirectory", obj.targetDirectory);
				switch (loadType) {
				case "daily":
				case "monthly":
				case "adhoc":
					jsonObj.put("startDate", obj.startDate);
					jsonObj.put("endDate", obj.endDate);
				case "full":
					jsonObj.put("startDate", null);
					jsonObj.put("endDate", null);
				}

			}

			else {
				switch (loadType) {
				case "daily":
				case "monthly":
				case "adhoc":
					jsonObj.put("startDate", obj.startDate);
					jsonObj.put("endDate", obj.endDate);
				case "full":
					jsonObj.put("startDate", null);
					jsonObj.put("endDate", null);
				}
			}
		}
		return jsonObj;
	}

	public String toString(Recon obj) {

		return obj.getHostName() + "," + obj.getPort() + "," + obj.getServerType() + "," + obj.getSourceDatabase() + ","
				+ obj.getSourceTable() + "," + obj.getLoadType() + "," + obj.getCriteriaColumn() + ","
				+ obj.getStartDate() + "," + obj.getEndDate() + "," + obj.getStorageTarget() + "," + obj.getReconfile()
				+ "," + obj.getStorageFormat() + "," + obj.getStorageCompression() + "," + obj.getAccessUserName() + ","
				+ obj.getAccessPassword();

	}
}