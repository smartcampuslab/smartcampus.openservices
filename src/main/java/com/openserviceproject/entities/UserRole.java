package com.openserviceproject.entities;

import javax.persistence.*;

@Entity
@Table(name="UserRole")
public class UserRole {
	
	@Id
	private int id;
	@Column(name="id_user")
	private int id_user;
	@Column(name="id_org")
	private int id_org;
	@Column(name="role")
	private String role;
	
	/*@OneToOne()
	@JoinColumn(name="id")
	private Users user;
	*/
	public UserRole(){
		
	}
	
	public UserRole(int id_user, int id_org, String role){
		this.id_user=id_user;
		this.id_org=id_org;
		this.role=role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public int getId_org() {
		return id_org;
	}

	public void setId_org(int id_org) {
		this.id_org = id_org;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
