/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.openservices.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.openservices.Constants.ORDER;
import eu.trentorise.smartcampus.openservices.Constants.SERVICE_STATE;
import eu.trentorise.smartcampus.openservices.dao.MethodDao;
import eu.trentorise.smartcampus.openservices.dao.OrganizationDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceHistoryDao;
import eu.trentorise.smartcampus.openservices.entities.Authentication;
import eu.trentorise.smartcampus.openservices.entities.Category;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;
import eu.trentorise.smartcampus.openservices.model.News;
import eu.trentorise.smartcampus.openservices.support.CategoryServices;
import eu.trentorise.smartcampus.openservices.support.TagCounter;

/**
 * Manager that retrieves data about service and organization for all users.
 * 
 * @author Giulia Canobbio
 * 
 */
@Component
public class CatalogManager {
	/**
	 * Instance of {@link ServiceDao} to retrieve service data using Dao
	 * classes.
	 */
	@Autowired
	private ServiceDao serviceDao;
	/**
	 * Instance of {@link OrganizationDao} to retrieve organization data using
	 * Dao classes.
	 */
	@Autowired
	private OrganizationDao orgDao;
	/**
	 * Instance of {@link MethodDao} to retrieve service method data using Dao
	 * classes.
	 */
	@Autowired
	private MethodDao methodDao;
	/**
	 * Instance of {@link ServiceHistoryDao} to retrieve service history data
	 * using Dao classes.
	 */
	@Autowired
	private ServiceHistoryDao shDao;
	/**
	 * Instance of {@link CategoryManager} to retrieve category data.
	 */
	@Autowired
	private CategoryManager categoryManager;

	/**
	 * Get list of published and deprecated services.
	 * 
	 * @param firstResult
	 *            : int, start index
	 * @param maxResult
	 *            : int, number of element in list
	 * @param param_order
	 *            : String, parameter order
	 * @return all {@link Service} instances
	 */
	public List<Service> catalogServices(int firstResult, int maxResult,
			String param_order) {
		try {
			if (param_order.equalsIgnoreCase(ORDER.id.toString())) {
				return cleanAuthenticationData(serviceDao.showPublishedService(
						firstResult, maxResult, ORDER.id));
			} else if (param_order.equalsIgnoreCase(ORDER.name.toString())) {
				return cleanAuthenticationData(serviceDao.showPublishedService(
						firstResult, maxResult, ORDER.name));
			} else if (param_order.equalsIgnoreCase(ORDER.namedesc.toString())) {
				return cleanAuthenticationData(serviceDao.showPublishedService(
						firstResult, maxResult, ORDER.namedesc));
			} else if (param_order.equalsIgnoreCase(ORDER.date.toString())) {
				return cleanAuthenticationData(serviceDao.showPublishedService(
						firstResult, maxResult, ORDER.date));
			}
			throw new SecurityException();
		} catch (DataAccessException d) {
			return null;
		}
	}

	/**
	 * Returns a list of services after cleaning authentication data.
	 * 
	 * @param services
	 *            : list of {@link Service}
	 * @return a list of {@link Service}
	 */
	private List<Service> cleanAuthenticationData(List<Service> services) {
		if (services != null) {
			for (Service s : services) {
				cleanAuthenticationData(s);
			}
		}
		return services;
	}

	/**
	 * Get a Service data and clean authentication.
	 * 
	 * @param s
	 *            : instance of {@link Service}
	 * @return instance of {@link Service}
	 */
	private Service cleanAuthenticationData(Service s) {
		if (s == null)
			return null;
		if (s.getAccessInformation() != null) {
			Authentication a = s.getAccessInformation().getAuthentication();
			if (a != null) {
				a.setAccessAttributes(null);
			}
		}
		return s;
	}

	/**
	 * Returns a list of methods after cleaning authentication data.
	 * 
	 * @param methods
	 *            : list of {@link Method}
	 * @return a list of {@link Method}
	 */
	private List<Method> cleanMethodAuthenticationData(List<Method> methods) {
		if (methods != null) {
			for (Method m : methods) {
				cleanMethodAuthenticationData(m);
			}
		}
		return methods;
	}

	/**
	 * Get a method data and clean authentication.
	 * 
	 * @param m
	 *            : instance of {@link Method}
	 * @return instance of {@link Method}
	 */
	private Method cleanMethodAuthenticationData(Method m) {
		if (m == null)
			return null;
		if (m.getTestboxProperties() != null) {
			Authentication a = m.getTestboxProperties()
					.getAuthenticationDescriptor();
			if (a != null) {
				a.setAccessAttributes(null);
			}
		}
		return m;
	}

	/**
	 * Get Service data, searching by id.
	 * 
	 * @param service_id
	 *            : int service id
	 * @return a {@link Service} instance
	 */
	public Service catalogServiceById(int service_id) {
		Service s = new Service();
		try {
			s = serviceDao.getServiceById(service_id);
			if (s != null
					&& s.getState().equalsIgnoreCase(
							SERVICE_STATE.UNPUBLISH.toString())) {
				s = null;
			}
		} catch (DataAccessException d) {
			s = null;
		}
		return cleanAuthenticationData(s);
	}

	/**
	 * Get list of methods for a given Service. Search by service id.
	 * 
	 * @param service_id
	 *            : int service id
	 * @return all {@link Method} instances
	 */
	public List<Method> catalogServiceMethods(int service_id) {
		try {
			return cleanMethodAuthenticationData(methodDao
					.getMethodByServiceId(service_id));
		} catch (DataAccessException e) {
			return null;
		}
	}

	/**
	 * Get list of service history for a given Service. Search by service id.
	 * 
	 * @param service_id
	 *            : int service id
	 * @return all {@link ServiceHistory} instances
	 */
	public List<ServiceHistory> catalogServiceHistory(int service_id) {
		try {
			return shDao.getServiceHistoryByServiceId(service_id);
		} catch (DataAccessException d) {
			return null;
		}
	}

	/**
	 * Get list of all services. Their name contains token.
	 * 
	 * @param token
	 *            : String token compared with service name
	 * @param firstResult
	 *            : int, start index
	 * @param maxResult
	 *            : int, number of element in list of services
	 * @param param_order
	 *            : String, parameter order
	 * @return all {@link Service} instances
	 */
	public List<Service> catalogServiceSimpleSearch(String token,
			int firstResult, int maxResult, String param_order) {
		try {
			if (param_order.equalsIgnoreCase(ORDER.id.toString())) {
				return serviceDao.searchService(token, firstResult, maxResult,
						ORDER.id);
			} else if (param_order.equalsIgnoreCase(ORDER.name.toString())) {
				return serviceDao.searchService(token, firstResult, maxResult,
						ORDER.name);
			} else if (param_order.equalsIgnoreCase(ORDER.namedesc.toString())) {
				return serviceDao.searchService(token, firstResult, maxResult,
						ORDER.namedesc);
			} else if (param_order.equalsIgnoreCase(ORDER.date.toString())) {
				return serviceDao.searchService(token, firstResult, maxResult,
						ORDER.date);
			}
			throw new SecurityException();
		} catch (DataAccessException d) {
			return null;
		}
	}

	/**
	 * Get list of all services, searching by category.
	 * 
	 * @param category
	 *            : int, category id
	 * @param firstResult
	 *            : int, start index
	 * @param maxResult
	 *            : int, number of element in list of services
	 * @param param_order
	 *            : String, parameter order
	 * @return all {@link Service} instances
	 */
	public List<Service> catalogServiceBrowseByCategory(int category,
			int firstResult, int maxResult, String param_order) {
		try {
			if (param_order.equalsIgnoreCase(ORDER.id.toString())) {
				return serviceDao.browseService(category, firstResult,
						maxResult, ORDER.id);
			} else if (param_order.equalsIgnoreCase(ORDER.namedesc.toString())) {
				return serviceDao.browseService(category, firstResult,
						maxResult, ORDER.namedesc);
			} else if (param_order.equalsIgnoreCase(ORDER.date.toString())) {
				return serviceDao.browseService(category, firstResult,
						maxResult, ORDER.date);
			} else {
				return serviceDao.browseService(category, firstResult,
						maxResult, ORDER.name);
			}
		} catch (DataAccessException d) {
			return null;
		}
	}

	/**
	 * Get list of all services, searching by categories.
	 * 
	 * @param categories
	 *            : int[] categories ids
	 * @param firstResult
	 *            : int, start index
	 * @param maxResult
	 *            : int, number of element in list of services
	 * @param param_order
	 *            : String, parameter order
	 * @return all {@link Service} instances
	 */
	public List<Service> catalogServiceBrowseByCategories(int[] categories,
			int firstResult, int maxResult, String param_order) {
		try {
			if (param_order.equalsIgnoreCase(ORDER.id.toString())) {
				return serviceDao.browseService(categories, firstResult,
						maxResult, ORDER.id);
			} else if (param_order.equalsIgnoreCase(ORDER.namedesc.toString())) {
				return serviceDao.browseService(categories, firstResult,
						maxResult, ORDER.namedesc);
			} else if (param_order.equalsIgnoreCase(ORDER.date.toString())) {
				return serviceDao.browseService(categories, firstResult,
						maxResult, ORDER.date);
			} else {
				return serviceDao.browseService(categories, firstResult,
						maxResult, ORDER.name);
			}
		} catch (DataAccessException d) {
			return null;
		}
	}

	/**
	 * Retrieve all service of a specific organization.
	 * 
	 * @param org
	 *            : id of organization
	 * @param firstResult
	 *            : int, start index
	 * @param maxResult
	 *            : int, number of element in list of services
	 * @param param_order
	 *            : String, parameter order
	 * @return all {@link Service} instances
	 */
	public List<Service> catalogServiceBrowseByOrg(int org, int firstResult,
			int maxResult, List<Integer> categoryIds, String token,
			String param_order) {
		List<Service> s = new ArrayList<Service>();
		try {
			if (param_order.equalsIgnoreCase(ORDER.id.toString())) {
				s = serviceDao.searchServiceByIdOrg(org, firstResult,
						maxResult, categoryIds, token, ORDER.id.toString());
			} else if (param_order.equalsIgnoreCase(ORDER.name.toString())) {
				s = serviceDao.searchServiceByIdOrg(org, firstResult,
						maxResult, categoryIds, token, ORDER.name.toString());
			} else if (param_order.equalsIgnoreCase(ORDER.namedesc.toString())) {
				s = serviceDao.searchServiceByIdOrg(org, firstResult,
						maxResult, categoryIds, token,
						ORDER.namedesc.toString());
			} else if (param_order.equalsIgnoreCase(ORDER.date.toString())) {
				s = serviceDao.searchServiceByIdOrg(org, firstResult,
						maxResult, categoryIds, token, ORDER.date.toString());
			}

			if (s != null) {
				for (Iterator<Service> iterator = s.iterator(); iterator
						.hasNext();) {
					Service service = iterator.next();
					if (SERVICE_STATE.UNPUBLISH.toString().equals(
							service.getState()))
						iterator.remove();
				}
			}
		} catch (DataAccessException d) {
			s = null;
		}
		return s;
	}

	/**
	 * Get list of all services, searching by tags.
	 * 
	 * @param tag
	 *            : String tag for searching in service tags
	 * @param firstResult
	 *            : int, start index
	 * @param maxResult
	 *            : int, number of element in list of services
	 * @param param_order
	 *            : String, parameter order
	 * @return all {@link Service} instances
	 */
	public List<Service> catalogServiceBrowseByTags(String tag,
			int firstResult, int maxResult, String param_order) {
		try {
			if (param_order.equalsIgnoreCase(ORDER.id.toString())) {
				return serviceDao.getServiceByTag(tag, firstResult, maxResult,
						ORDER.id);
			} else if (param_order.equalsIgnoreCase(ORDER.name.toString())) {
				return serviceDao.getServiceByTag(tag, firstResult, maxResult,
						ORDER.name);
			} else if (param_order.equalsIgnoreCase(ORDER.namedesc.toString())) {
				return serviceDao.getServiceByTag(tag, firstResult, maxResult,
						ORDER.namedesc);
			}
			throw new SecurityException();
		} catch (DataAccessException d) {
			return null;
		}
	}

	/**
	 * Get list of organizations.
	 * 
	 * @param firstResult
	 *            : int, start index
	 * @param maxResult
	 *            : int, number of element in list of services
	 * @param param_order
	 *            : String, parameter order
	 * @return all {@link Organization} instances
	 */
	public List<Organization> catalogOrg(int firstResult, int maxResult,
			String param_order) {
		try {
			if (param_order.equalsIgnoreCase(ORDER.id.toString())) {
				return orgDao.showOrganizations(firstResult, maxResult,
						ORDER.id);
			} else if (param_order.equalsIgnoreCase(ORDER.namedesc.toString())) {
				return orgDao.showOrganizations(firstResult, maxResult,
						ORDER.namedesc);
			} else if (param_order.equalsIgnoreCase(ORDER.name.toString())) {
				return orgDao.showOrganizations(firstResult, maxResult,
						ORDER.name);
			}
			throw new SecurityException();
		} catch (DataAccessException d) {
			return null;
		}
	}

	/**
	 * Simple search for organization. Their names contains token.
	 * 
	 * @param token
	 *            : String token for comparing it with organization name
	 * @param firstResult
	 *            : int, start index
	 * @param maxResult
	 *            : int, number of element in list of services
	 * @param param_order
	 *            : String, parameter order
	 * @return all {@link Organization} instances
	 */
	public List<Organization> catalogOrgSimpleSearch(String token,
			int firstResult, int maxResult, String param_order) {
		try {
			if (param_order.equalsIgnoreCase(ORDER.id.toString())) {
				return orgDao.searchOrganization(token, firstResult, maxResult,
						ORDER.id.toString());
			} else if (param_order.equalsIgnoreCase(ORDER.name.toString())) {
				return orgDao.searchOrganization(token, firstResult, maxResult,
						ORDER.name.toString());
			}
			throw new SecurityException();
		} catch (DataAccessException d) {
			return null;
		}
	}

	/**
	 * Get list of organization searching by category.
	 * 
	 * @param category
	 *            : int category id
	 * @param firstResult
	 *            : int, start index
	 * @param maxResult
	 *            : int, number of element in list of services
	 * @param param_order
	 *            : String, parameter order
	 * @return all {@link Organization} instances
	 */
	public List<Organization> catalogOrgBrowseByCategory(int category,
			int firstResult, int maxResult, String param_order) {
		try {
			if (param_order.equalsIgnoreCase(ORDER.id.toString())) {
				return orgDao.browseOrganization(category, null, firstResult,
						maxResult, ORDER.id);
			} else if (param_order.equalsIgnoreCase(ORDER.namedesc.toString())) {
				return orgDao.browseOrganization(category, null, firstResult,
						maxResult, ORDER.namedesc);
			} else if (param_order.equalsIgnoreCase(ORDER.name.toString())) {
				return orgDao.browseOrganization(category, null, firstResult,
						maxResult, ORDER.name);
			}
			throw new SecurityException();
		} catch (DataAccessException d) {
			return null;
		}
	}

	/**
	 * Get list of organization searching by categories.
	 * 
	 * @param categories
	 *            : int[] categories ids
	 * @param firstResult
	 *            : int, start index
	 * @param maxResult
	 *            : int, number of element in list of services
	 * @param param_order
	 *            : String, parameter order
	 * @return all {@link Organization} instances
	 */
	public List<Organization> catalogOrgBrowseByCategories(int[] categories,
			int firstResult, int maxResult, String param_order) {
		try {
			if (param_order.equalsIgnoreCase(ORDER.id.toString())) {
				return orgDao.browseOrganization(categories, null, firstResult,
						maxResult, ORDER.id);
			} else if (param_order.equalsIgnoreCase(ORDER.namedesc.toString())) {
				return orgDao.browseOrganization(categories, null, firstResult,
						maxResult, ORDER.namedesc);
			} else if (param_order.equalsIgnoreCase(ORDER.name.toString())) {
				return orgDao.browseOrganization(categories, null, firstResult,
						maxResult, ORDER.name);
			}
			throw new SecurityException();
		} catch (DataAccessException d) {
			return null;
		}
	}

	/**
	 * Retrieve organization data searching by its id.
	 * 
	 * @param id
	 *            : int organization id
	 * @return {@link Organization} instance
	 */
	public Organization catalogOrgById(int id) {
		try {
			return orgDao.getOrganizationById(id);
		} catch (DataAccessException d) {
			return null;
		}
	}

	/**
	 * Browse services by category.
	 * 
	 * @return {@link CategoryServices} instance
	 */
	public CategoryServices getCategoryServices() {
		CategoryServices res = new CategoryServices();
		try {
			List<Category> list = categoryManager.getCategories();
			res.setCategories(list);
			if (res.getCategories() != null) {
				Map<Integer, Integer> counts = serviceDao
						.findCategoryServices();
				res.setServices(new ArrayList<Integer>());
				for (Category c : res.getCategories()) {
					Integer count = counts.get(c.getId());
					res.getServices().add(count == null ? 0 : count);
				}
			}
		} catch (DataAccessException d) {
			res = null;
		}

		return res;
	}

	/**
	 * Count services saved in db.
	 * 
	 * @return number of services saved in database
	 */
	public Long countService() {
		return serviceDao.countService();
	}

	/**
	 * Count services resulted from simple search.
	 * 
	 * @param token
	 *            : String
	 * @return number of services retrieved by simple search
	 */
	public Long countServiceSimpleSearch(String token) {
		return serviceDao.countServiceSimpleSearch(token);
	}

	/**
	 * Count services resulted from search by organization id.
	 * 
	 * @param id_org
	 *            : organization id
	 * @param categoryIds
	 *            : list of category ids, put null to retrieve all values
	 * @param token
	 *            : filter by service name, set null to unable
	 * @return number of services retrieved by organization search
	 */
	public Long countServiceByOrgSearch(int id_org, List<Integer> categoryIds,
			String token) {
		return serviceDao.countServiceByOrgSearch(id_org, categoryIds, token);
	}

	/**
	 * Count services resulted from search by tag.
	 * 
	 * @param tag
	 *            : String
	 * @return number of services retrieved by tags search
	 */
	public Long countServiceByTagsSearch(String tag) {
		return serviceDao.countServiceTagsSearch(tag);
	}

	/**
	 * Count services resulted from search by category.
	 * 
	 * @param category
	 *            : int category id
	 * @return number of services retrieved by category search
	 */
	public Long countServiceByCategorySearch(int category) {
		return serviceDao.countServiceCategorySearch(category);
	}

	/**
	 * Count organization saved in db.
	 * 
	 * @return number of organizations saved in database
	 */
	public Long countOrg() {
		return orgDao.countOrganization();
	}

	/**
	 * Retrieve news from database.
	 * 
	 * @param n
	 *            : int, number of news in resulted list
	 * @return list of {@link ServiceHistory} instances
	 */
	public List<News> getNews(int n) {
		try {
			List<News> news = new ArrayList<News>();
			List<ServiceHistory> shlist = shDao.getNews(n);
			for (ServiceHistory sh : shlist) {
				News s = new News(sh.getServiceName(), sh.getMethodName(),
						sh.getOperation(), sh.getDate(), sh.getId_service(),
						sh.getId_serviceMethod());
				news.add(s);
			}
			return news;
		} catch (DataAccessException d) {
			return null;
		}
	}

	/**
	 * Retrieve counter of tags.
	 * 
	 * @param order
	 *            : String, value tag or counter
	 * @param group
	 *            : String, value ASC or DESC
	 * @return a list of {@link TagCounter} instances
	 */
	public List<TagCounter> getTagsServicesCounter(String group, String order) {
		List<TagCounter> list = new ArrayList<TagCounter>();
		try {
			Map<String, Integer> tags = serviceDao
					.findTagServices(group, order);
			if (!tags.isEmpty()) {
				Iterator<Map.Entry<String, Integer>> iter = tags.entrySet()
						.iterator();
				while (iter.hasNext()) {
					String key = iter.next().getKey();
					TagCounter tc = new TagCounter();
					tc.setTag(key);
					tc.setCounter(tags.get(key));
					list.add(tc);
				}
			} else {
				throw new EntityNotFoundException();
			}
		} catch (DataAccessException d) {
			list = null;
		}

		return list;
	}

}
