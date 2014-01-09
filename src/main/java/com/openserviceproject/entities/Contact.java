package com.openserviceproject.entities;

import java.io.Serializable;

/**
 * Blob Object for Organization table:
 * email
 * phone
 * fax
 * web
 * @author Giulia Canobbio
 *
 */
public class Contact implements Serializable{
	
	private String email;
	private String phone;
	private String fax;
	private String web;
	
	public Contact(){
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

}
