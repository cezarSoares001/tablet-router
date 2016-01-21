package com.murrow.support;

public class LabException extends Exception {

	/**
	 * This is a generic exception class for the lab exceptions we may generate.
	 */
	private static final long serialVersionUID = 1L;

	public LabException(String errorMessage){
		super(errorMessage);
	}
}
