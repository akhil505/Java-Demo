package com.datamigration.model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "flumeChannel")
@SuppressWarnings("serial")
@ViewScoped
public class FlumeChannel implements Serializable {
	private String chnlSelected;
	@ManagedProperty(value = "#{memChannel}")
	private MemoryChannel memChannel;
	@ManagedProperty(value = "#{fileChannel}")
	private FileChannel fileChannel;

	public String getChnlSelected() {
		return chnlSelected;
	}

	public void setChnlSelected(String chnlSelected) {
		this.chnlSelected = chnlSelected;
	}

	public MemoryChannel getMemChannel() {
		return memChannel;
	}

	public FileChannel getFileChannel() {
		return fileChannel;
	}

	public void setFileChannel(FileChannel fileChannel) {
		this.fileChannel = fileChannel;
	}

	public void setMemChannel(MemoryChannel memChannel) {
		this.memChannel = memChannel;
	}

}
