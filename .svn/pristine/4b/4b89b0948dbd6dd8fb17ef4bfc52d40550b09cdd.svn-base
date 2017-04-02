package com.datamigration.model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "spoolDirectory")
@SuppressWarnings("serial")
@ViewScoped
public class SpoolDirectory implements Serializable {

	private String srcType = "spooldir";
	private String spoolDir;
	private String fileSuffix = ".COMPLETED";
	private String deletePolicy = "never";
	private boolean fileHeader;
	private String fileHeaderKey;
	private boolean basenameHeader;
	private String basenameHeaderKey;
	private String ignorePattern;
	private String trackerDir = ".flumespool";
	private String consumeOrder = "oldest";
	private long maxBackoff = 4000;
	private long batchSize = 100;
	private String inputCharset = "UTF-8";
	private String decodeErrorPolicy;

	public String getSrcType() {
		return srcType;
	}

	public void setSrcType(String srcType) {
		this.srcType = srcType;
	}

	public String getSpoolDir() {
		return spoolDir;
	}

	public void setSpoolDir(String spoolDir) {
		this.spoolDir = spoolDir;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getDeletePolicy() {
		return deletePolicy;
	}

	public void setDeletePolicy(String deletePolicy) {
		this.deletePolicy = deletePolicy;
	}

	public boolean isFileHeader() {
		return fileHeader;
	}

	public void setFileHeader(boolean fileHeader) {
		this.fileHeader = fileHeader;
	}

	public String getFileHeaderKey() {
		return fileHeaderKey;
	}

	public void setFileHeaderKey(String fileHeaderKey) {
		this.fileHeaderKey = fileHeaderKey;
	}

	public boolean isBasenameHeader() {
		return basenameHeader;
	}

	public void setBasenameHeader(boolean basenameHeader) {
		this.basenameHeader = basenameHeader;
	}

	public String getBasenameHeaderKey() {
		return basenameHeaderKey;
	}

	public void setBasenameHeaderKey(String basenameHeaderKey) {
		this.basenameHeaderKey = basenameHeaderKey;
	}

	public String getIgnorePattern() {
		return ignorePattern;
	}

	public void setIgnorePattern(String ignorePattern) {
		this.ignorePattern = ignorePattern;
	}

	public String getTrackerDir() {
		return trackerDir;
	}

	public void setTrackerDir(String trackerDir) {
		this.trackerDir = trackerDir;
	}

	public String getConsumeOrder() {
		return consumeOrder;
	}

	public void setConsumeOrder(String consumeOrder) {
		this.consumeOrder = consumeOrder;
	}

	public long getMaxBackoff() {
		return maxBackoff;
	}

	public void setMaxBackoff(long maxBackoff) {
		this.maxBackoff = maxBackoff;
	}

	public long getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(long batchSize) {
		this.batchSize = batchSize;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getDecodeErrorPolicy() {
		return decodeErrorPolicy;
	}

	public void setDecodeErrorPolicy(String decodeErrorPolicy) {
		this.decodeErrorPolicy = decodeErrorPolicy;
	}

}
