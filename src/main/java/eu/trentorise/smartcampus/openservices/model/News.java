package eu.trentorise.smartcampus.openservices.model;

import java.util.Date;

public class News {
	
	private String service;
	private String method;
	private String state;
	private Date date;
	
	public News(String service, String method, String state, Date date) {
		this.service=service;
		this.method=method;
		this.state=state;
		this.date=date;
	}
	
	public String getService() {
		return service;
	}
	
	public void setService(String service) {
		this.service = service;
	}
	
	public String getMethod() {
		return method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

}
