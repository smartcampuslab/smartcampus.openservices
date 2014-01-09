package com.openserviceproject.entities;

import javax.persistence.*;

@Entity
@Table(name="TemporaryLink")
public class TemporaryLink {
	
	@Id
	private int id;
	/*@Column(name="id_user")
	private int id_user;*/
	@Column(name="id_org")
	private int id_org;
	@Column(name="\"key\"")
	private String key;
	@Column(name="email")
	private String email;
	
	public TemporaryLink(){
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	/*
	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	*/
	public int getId_org() {
		return id_org;
	}

	public void setId_org(int id_org) {
		this.id_org = id_org;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
