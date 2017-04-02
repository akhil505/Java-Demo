package com.datamigration.model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@SuppressWarnings("serial")
@ManagedBean(name = "fileChannel")
@ViewScoped
public class FileChannel implements Serializable {
	private String channelType = "file";

	private String checkpointDir = "~/.flume/file-channel/checkpoint";
	private boolean useDualCheckpoints = false;
	private String backupCheckpointDir;
	private String dataDirs = "~/.flume/file-channel/data";
	private long transactionCapacity = 10000;
	private long checkpointInterval = 30000;
	private long maxFileSize = 2146435071;
	private long minimumRequiredSpace = 524288000;
	private long capacity = 1000000;
	private long keepAlive = 3;
	private boolean useLogReplayV1;
	private boolean checkpointOnClose = true;

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getCheckpointDir() {
		return checkpointDir;
	}

	public void setCheckpointDir(String checkpointDir) {
		this.checkpointDir = checkpointDir;
	}

	public boolean isUseDualCheckpoints() {
		return useDualCheckpoints;
	}

	public void setUseDualCheckpoints(boolean useDualCheckpoints) {
		this.useDualCheckpoints = useDualCheckpoints;
	}

	public String getBackupCheckpointDir() {
		return backupCheckpointDir;
	}

	public void setBackupCheckpointDir(String backupCheckpointDir) {
		this.backupCheckpointDir = backupCheckpointDir;
	}

	public String getDataDirs() {
		return dataDirs;
	}

	public void setDataDirs(String dataDirs) {
		this.dataDirs = dataDirs;
	}

	public long getTransactionCapacity() {
		return transactionCapacity;
	}

	public void setTransactionCapacity(long transactionCapacity) {
		this.transactionCapacity = transactionCapacity;
	}

	public long getCheckpointInterval() {
		return checkpointInterval;
	}

	public void setCheckpointInterval(long checkpointInterval) {
		this.checkpointInterval = checkpointInterval;
	}

	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public long getMinimumRequiredSpace() {
		return minimumRequiredSpace;
	}

	public void setMinimumRequiredSpace(long minimumRequiredSpace) {
		this.minimumRequiredSpace = minimumRequiredSpace;
	}

	public long getCapacity() {
		return capacity;
	}

	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}

	public long getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(long keepAlive) {
		this.keepAlive = keepAlive;
	}

	public boolean isUseLogReplayV1() {
		return useLogReplayV1;
	}

	public void setUseLogReplayV1(boolean useLogReplayV1) {
		this.useLogReplayV1 = useLogReplayV1;
	}

	public boolean isCheckpointOnClose() {
		return checkpointOnClose;
	}

	public void setCheckpointOnClose(boolean checkpointOnClose) {
		this.checkpointOnClose = checkpointOnClose;
	}

}
