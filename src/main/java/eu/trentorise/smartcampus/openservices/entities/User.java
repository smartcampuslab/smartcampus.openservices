/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.openservices.entities;

import javax.persistence.*;

@Entity
@Table(name="User")
public class User {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name="username")
	private String username;
	@Column(name="password")
	private String password;
	@Column(name="email")
	private String email;
	@Column(name="enabled")
	private int enabled;
	//adding an object profile - cast to Blob is doing by JPA
	@Column(name="profile")
	@Lob
	private Profile profile;
	@Column(name="role")
	private String role;
	
	//OneToMany: Users -> Organization_Owner -> Organization
	/*@OneToMany(fetch=FetchType.LAZY, cascade= CascadeType.REFRESH)
	@JoinTable(name="UserRole", joinColumns={ @JoinColumn(name="id_user", referencedColumnName="id") },
	inverseJoinColumns={ @JoinColumn(name="id_org", referencedColumnName="id", unique=true) })
	private List<Organization> orgs; //this are all organizations of users having roles service_owner and organization_owner
	*/
	public User(){
		
	}
	
	public User(int id, String username, String password, String email, int enabled, String role){
		this.id=id;
		this.username=username;
		this.password=password;
		this.email=email;
		this.enabled=enabled;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	/*
	public List<Organization> getOrgs() {
		return orgs;
	}

	public void setOrgs(List<Organization> orgs) {
		this.orgs = orgs;
	}
*/
	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}
