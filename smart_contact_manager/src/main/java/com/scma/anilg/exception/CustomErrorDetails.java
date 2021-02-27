package com.scma.anilg.exception;

import java.util.Date;

public class CustomErrorDetails {
	
	private String message;
	private Date date;
	private String details;
	
	public CustomErrorDetails(String message, Date date, String details) {
		super();
		this.message = message;
		this.date = date;
		this.details = details;
	}

	public String getMessage() {
		return message;
	}

	public Date getDate() {
		return date;
	}

	public String getDetails() {
		return details;
	}
	
	
}
