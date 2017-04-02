package com.datamigration.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@SuppressWarnings("serial")
@ManagedBean(name = "hdfsSink")
@ViewScoped
public class HDFSSink implements Serializable {

	private String sinkType = "hdfs";
	private String path;
	private String filePrefix = "FlumeData";
	private String fileSuffix;
	private String inUsePrefix;
	private String inUseSuffix = ".tmp";
	private long rollInterval = 30;
	private long rollSize = 1024;
	private long rollCount = 10;
	private long idleTimeout;
	private long batchSize = 100;
	private String codeC;
	private List<String> codeCList;
	private String fileType;
	private long maxOpenFiles;
	private long minBlockReplicas;
	private String writeFormat;
	private long callTimeout;
	private long threadsPoolSize;
	private long rollTimerPoolSize;
	private String kerberosPrincipal;
	private String kerberosKeytab;
	private String proxyUser;
	private boolean round;
	private long roundValue;
	private String roundUnit;
	private String timeZone;
	private int closeTries;
	private int retryInterval;

	public String getSinkType() {
		return sinkType;
	}

	public void setSinkType(String sinkType) {
		this.sinkType = sinkType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilePrefix() {
		return filePrefix;
	}

	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getInUsePrefix() {
		return inUsePrefix;
	}

	public void setInUsePrefix(String inUsePrefix) {
		this.inUsePrefix = inUsePrefix;
	}

	public String getInUseSuffix() {
		return inUseSuffix;
	}

	public void setInUseSuffix(String inUseSuffix) {
		this.inUseSuffix = inUseSuffix;
	}

	public long getRollInterval() {
		return rollInterval;
	}

	public void setRollInterval(long rollInterval) {
		this.rollInterval = rollInterval;
	}

	public long getRollSize() {
		return rollSize;
	}

	public void setRollSize(long rollSize) {
		this.rollSize = rollSize;
	}

	public long getRollCount() {
		return rollCount;
	}

	public void setRollCount(long rollCount) {
		this.rollCount = rollCount;
	}

	public long getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(long idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public long getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(long batchSize) {
		this.batchSize = batchSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getMaxOpenFiles() {
		return maxOpenFiles;
	}

	public void setMaxOpenFiles(long maxOpenFiles) {
		this.maxOpenFiles = maxOpenFiles;
	}

	public long getMinBlockReplicas() {
		return minBlockReplicas;
	}

	public void setMinBlockReplicas(long minBlockReplicas) {
		this.minBlockReplicas = minBlockReplicas;
	}

	public String getWriteFormat() {
		return writeFormat;
	}

	public void setWriteFormat(String writeFormat) {
		this.writeFormat = writeFormat;
	}

	public long getCallTimeout() {
		return callTimeout;
	}

	public void setCallTimeout(long callTimeout) {
		this.callTimeout = callTimeout;
	}

	public long getThreadsPoolSize() {
		return threadsPoolSize;
	}

	public void setThreadsPoolSize(long threadsPoolSize) {
		this.threadsPoolSize = threadsPoolSize;
	}

	public long getRollTimerPoolSize() {
		return rollTimerPoolSize;
	}

	public void setRollTimerPoolSize(long rollTimerPoolSize) {
		this.rollTimerPoolSize = rollTimerPoolSize;
	}

	public String getKerberosPrincipal() {
		return kerberosPrincipal;
	}

	public void setKerberosPrincipal(String kerberosPrincipal) {
		this.kerberosPrincipal = kerberosPrincipal;
	}

	public String getKerberosKeytab() {
		return kerberosKeytab;
	}

	public void setKerberosKeytab(String kerberosKeytab) {
		this.kerberosKeytab = kerberosKeytab;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public boolean isRound() {
		return round;
	}

	public void setRound(boolean round) {
		this.round = round;
	}

	public long getRoundValue() {
		return roundValue;
	}

	public void setRoundValue(long roundValue) {
		this.roundValue = roundValue;
	}

	public String getRoundUnit() {
		return roundUnit;
	}

	public void setRoundUnit(String roundUnit) {
		this.roundUnit = roundUnit;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public int getCloseTries() {
		return closeTries;
	}

	public void setCloseTries(int closeTries) {
		this.closeTries = closeTries;
	}

	public int getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}

	public List<String> getCodeCList() {
		codeCList = new ArrayList<>();
		codeCList.add("gzip");
		codeCList.add("bzip2");
		codeCList.add("lzo");
		codeCList.add("lzop");
		codeCList.add("snappy");
		return codeCList;
	}

	public void setCodeCList(List<String> codeCList) {
		this.codeCList = codeCList;
	}

	public String getCodeC() {
		return codeC;
	}

	public void setCodeC(String codeC) {
		this.codeC = codeC;
	}

}
