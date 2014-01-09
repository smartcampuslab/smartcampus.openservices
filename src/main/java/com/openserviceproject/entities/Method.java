package com.openserviceproject.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="Method")
public class Method {
	
	@Id
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="synopsis")
	private String synopsis;
	@Column(name="documentation")
	private String documentation;
	@Column(name="id_service")
	private int id_service;
	@Column(name="testboxProperties")
	@Lob//TODO check if it is better having a JSON and if mysql can cast a JSON without problem
	private TestBoxProperties testboxProperties;
	
	public Method(){
		
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

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public int getId_service() {
		return id_service;
	}

	public void setId_service(int id_service) {
		this.id_service = id_service;
	}

	public TestBoxProperties getTestboxProperties() {
		return testboxProperties;
	}

	public void setTestboxProprieties(TestBoxProperties testboxProperties) {
		this.testboxProperties = testboxProperties;
	}

}
