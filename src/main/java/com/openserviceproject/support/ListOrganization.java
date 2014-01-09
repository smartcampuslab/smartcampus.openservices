package com.openserviceproject.support;

import java.util.List;

import com.openserviceproject.entities.Organization;
/**
 * Simple class just to return a list of organization
 * REST JSON use
 * @author Giulia Canobbio
 *
 */
public class ListOrganization{

	private List<Organization> orgs;
	
	public ListOrganization(){
		
	}

	public List<Organization> getOrgs() {
		return orgs;
	}

	public void setOrgs(List<Organization> orgs) {
		this.orgs = orgs;
	}
}
