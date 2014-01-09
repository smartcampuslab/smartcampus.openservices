package com.openserviceproject.entities;

import javax.persistence.*;

@Entity
@Table(name="Organization")
public class Organization{
	
	@Id
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="namespace")
	private String namespace;
	@Column(name="descriptor")
	private String descriptor;
	@Column(name="activityArea")
	private String activityArea;
	@Column(name="category")
	private String category;
	@Column(name="contacts")
	@Lob
	private Contact contacts;
	@Column(name="id_owner")
	private int id_owner;
	
	public Organization(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public String getActivityArea() {
		return activityArea;
	}

	public void setActivityArea(String activityArea) {
		this.activityArea = activityArea;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Contact getContacts() {
		return contacts;
	}

	public void setContacts(Contact contacts) {
		this.contacts = contacts;
	}

	public int getId_owner() {
		return id_owner;
	}

	public void setId_owner(int id_owner) {
		this.id_owner = id_owner;
	}
	
}
