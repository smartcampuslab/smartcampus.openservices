package com.openserviceproject.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ServiceHistory")
public class ServiceHistory {
	
	@Id
	private int id;
	@Column(name="operation")
	private String operation;
	@Column(name="id_service")
	private int id_service;
	@Column(name="id_serviceMethod")
	private int id_serviceMethod;
	@Column(name="date")
	private Date date;
	
	public ServiceHistory(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public int getId_service() {
		return id_service;
	}

	public void setId_service(int id_service) {
		this.id_service = id_service;
	}

	public int getId_serviceMethod() {
		return id_serviceMethod;
	}

	public void setId_serviceMethod(int id_serviceMethod) {
		this.id_serviceMethod = id_serviceMethod;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
