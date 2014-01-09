package com.openserviceproject.support;

import java.util.List;

import com.openserviceproject.entities.ServiceHistory;

/**
 * Simple class for JSON conversion
 * List of Service History
 * @author Giulia Canobbio
 *
 */
public class ListServiceHistory {
	
	private List<ServiceHistory> lserviceh;
	
	public ListServiceHistory(){
	}

	public List<ServiceHistory> getLserviceh() {
		return lserviceh;
	}

	public void setLserviceh(List<ServiceHistory> lserviceh) {
		this.lserviceh = lserviceh;
	}

}
