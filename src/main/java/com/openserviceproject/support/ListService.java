package com.openserviceproject.support;

import java.util.List;

import com.openserviceproject.entities.Service;
/**
 * Simple class just to return a list of organization
 * REST JSON use
 * @author Giulia Canobbio
 *
 */
public class ListService{

	private List<Service> services;
	
	public ListService(){
		
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
}
