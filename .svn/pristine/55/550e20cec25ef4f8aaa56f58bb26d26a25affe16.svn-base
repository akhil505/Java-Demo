package com.datamigration.model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@SuppressWarnings("serial")
@ManagedBean(name = "memChannel")
@ViewScoped
public class MemoryChannel implements Serializable {

	private String channelType = "memory";
	private long capacity=100;
	private long transactionCapacity=100;
	private long keepAlive=3;
	private long byteCapacityBufferPercentage=20;
	private long byteCapacity;

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public long getCapacity() {
		return capacity;
	}

	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}

	public long getTransactionCapacity() {
		return transactionCapacity;
	}

	public void setTransactionCapacity(long transactionCapacity) {
		this.transactionCapacity = transactionCapacity;
	}

	public long getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(long keepAlive) {
		this.keepAlive = keepAlive;
	}

	public long getByteCapacityBufferPercentage() {
		return byteCapacityBufferPercentage;
	}

	public void setByteCapacityBufferPercentage(long byteCapacityBufferPercentage) {
		this.byteCapacityBufferPercentage = byteCapacityBufferPercentage;
	}

	public long getByteCapacity() {
		return byteCapacity;
	}

	public void setByteCapacity(long byteCapacity) {
		this.byteCapacity = byteCapacity;
	}

}
