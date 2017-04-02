package com.datamigration.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.datamigration.util.DataMigrationConstants;

@ManagedBean(name = "flumeBean")
@SuppressWarnings("serial")
@ViewScoped
public class FlumeBean implements Serializable {

	@ManagedProperty(value = "#{flumeSourceBean}")
	private FlumeSourceBean flumeSourceBean;
	@ManagedProperty(value = "#{flumeChannel}")
	private FlumeChannel flumeChannel;
	@ManagedProperty(value = "#{flumeSinkBean}")
	private FlumeSinkBean flumeSinkBean;
	private List<String> sourceList;
	private List<String> channelList;
	private List<String> sinkList;

	public FlumeSourceBean getFlumeSourceBean() {
		return flumeSourceBean;
	}

	public List<String> getSourceList() {
		sourceList = new ArrayList<>();
		sourceList.add(DataMigrationConstants.SPOOL_DIR_SRC);
		sourceList.add(DataMigrationConstants.EXEC_SRC);
		return sourceList;
	}

	public void setSourceList(List<String> sourceList) {
		this.sourceList = sourceList;
	}

	public List<String> getChannelList() {
		channelList = new ArrayList<>();
		channelList.add(DataMigrationConstants.FILE_CHNL);
		channelList.add(DataMigrationConstants.MEM_CHNL);
		return channelList;
	}

	public void setChannelList(List<String> channelList) {
		this.channelList = channelList;
	}

	public List<String> getSinkList() {
		sinkList = new ArrayList<>();
		sinkList.add(DataMigrationConstants.HDFS_SINK);
		sinkList.add(DataMigrationConstants.HIVE_SINK);
		return sinkList;
	}

	public void setSinkList(List<String> sinkList) {
		this.sinkList = sinkList;
	}

	public void setFlumeSourceBean(FlumeSourceBean flumeSourceBean) {
		this.flumeSourceBean = flumeSourceBean;
	}

	public FlumeChannel getFlumeChannel() {
		return flumeChannel;
	}

	public void setFlumeChannel(FlumeChannel flumeChannel) {
		this.flumeChannel = flumeChannel;
	}

	public FlumeSinkBean getFlumeSinkBean() {
		return flumeSinkBean;
	}

	public void setFlumeSinkBean(FlumeSinkBean flumeSinkBean) {
		this.flumeSinkBean = flumeSinkBean;
	}

}
