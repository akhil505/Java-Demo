package com.datamigration.util;

public class FRDMException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FRDMException(String message, Exception e) {
		super(message, e);
	}

	public FRDMException(String e) {
		super(e);
	}

	public FRDMException() {
		super();
	}
}
