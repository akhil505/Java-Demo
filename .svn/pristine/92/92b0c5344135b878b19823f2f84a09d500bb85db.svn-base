package com.datamigration.model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "flumeSourceBean")
@SuppressWarnings("serial")
@ViewScoped
public class FlumeSourceBean implements Serializable {
	private String srcSelected;
	private boolean encryptReqrd;

	@ManagedProperty(value = "#{spoolDirectory}")
	private SpoolDirectory spoolDirectory;

	public String getSrcSelected() {
		return srcSelected;
	}

	public void setSrcSelected(String srcSelected) {
		this.srcSelected = srcSelected;
	}

	public SpoolDirectory getSpoolDirectory() {
		return spoolDirectory;
	}

	public void setSpoolDirectory(SpoolDirectory spoolDir) {
		spoolDirectory = spoolDir;
	}

	public boolean isEncryptReqrd() {
		return encryptReqrd;
	}

	public void setEncryptReqrd(boolean encryptReqrd) {
		this.encryptReqrd = encryptReqrd;
	}

}
