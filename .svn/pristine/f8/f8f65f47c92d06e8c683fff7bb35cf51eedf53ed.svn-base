package com.datamigration.model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@SuppressWarnings("serial")
@ManagedBean(name = "flumeSinkBean")
@ViewScoped
public class FlumeSinkBean implements Serializable {
	private String sinkSelected;
	@ManagedProperty(value = "#{kafkaSink}")
	private KafkaSink kafkaSink;
	@ManagedProperty(value = "#{hdfsSink}")
	private HDFSSink hdfsSink;
	private boolean parquetReqrd;

	public boolean isParquetReqrd() {
		return parquetReqrd;
	}

	public void setParquetReqrd(boolean parquetReqrd) {
		this.parquetReqrd = parquetReqrd;
	}

	public HDFSSink getHdfsSink() {
		return hdfsSink;
	}

	public void setHdfsSink(HDFSSink hdfsSink) {
		this.hdfsSink = hdfsSink;
	}

	public String getSinkSelected() {
		return sinkSelected;
	}

	public void setSinkSelected(String sinkSelected) {
		this.sinkSelected = sinkSelected;
	}

	public KafkaSink getKafkaSink() {
		return kafkaSink;
	}

	public void setKafkaSink(KafkaSink kafkaSink) {
		this.kafkaSink = kafkaSink;
	}
}
