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
package eu.trentorise.smartcampus.openservices.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.openservices.OrderBy;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;
import eu.trentorise.smartcampus.openservices.managers.CatalogManager;
import eu.trentorise.smartcampus.openservices.model.News;
import eu.trentorise.smartcampus.openservices.model.Service;
import eu.trentorise.smartcampus.openservices.support.CategoryServices;
import eu.trentorise.smartcampus.openservices.support.TagCounter;

/**
 * Catalog, a restful web services that retrieves data about Organization or
 * Services for all logged and not logged users.
 * 
 * A visible service is a deprecated or published service.
 * 
 * @author Giulia Canobbio
 * 
 */
@Controller
@RequestMapping(value = "/api/catalog")
public class CatalogController {

	private static final Logger logger = LoggerFactory
			.getLogger(CatalogController.class);

	/**
	 * Instance of {@link CatalogManager} to retrieve data using Dao classes.
	 */
	@Autowired
	private CatalogManager catalogManager;

	private static enum ORDER {
		ASC, DESC
	};

	/**
	 * Retrieve services count
	 * 
	 * @param response
	 *            : {@link HttpServletResponse} which is needed for status of
	 *            response (OK, BAD REQUEST or NOT FOUND)
	 * @return {@link ResponseObject} with services data, status (OK, BAD
	 *         REQUEST or NOT FOUND) and error message (if status is BAD REQUEST
	 *         or NOT FOUND).
	 */
	@RequestMapping(value = "/servicecount", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogServicesCount(HttpServletResponse response) {
		ResponseObject responseObject = new ResponseObject();
		try {
			Long count = catalogManager.countService();
			if (count == null) {
				responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseObject.setError("Services counter unavailable");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				responseObject.setStatus(HttpServletResponse.SC_OK);
				responseObject.setTotalNumber(count);// catalogManager.countService()
			}
		} catch (SecurityException s) {
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			responseObject.setError("Service counter unavailable");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return responseObject;
	}

	/**
	 * Retrieve list of all services or a partial one searching by free text in
	 * name and description, or searching by tag.
	 * 
	 * @param token
	 *            : String
	 * @param firstResult
	 *            : Integer start index
	 * @param maxResult
	 *            : Integer number of element in list
	 * @param param_order
	 *            : String parameter order
	 * @param tag
	 *            : String
	 * @param response
	 *            {@link HttpServletResponse} which is needed for status of
	 *            response (OK, BAD REQUEST or NOT FOUND)
	 * @return {@link ResponseObject} with services data, status (OK, BAD
	 *         REQUEST or NOT FOUND) and error message (if status is BAD REQUEST
	 *         or NOT FOUND).
	 */
	@RequestMapping(value = "/service", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogServices(
			@RequestParam(value = "q", required = false) String token,
			@RequestParam(value = "first", required = false, defaultValue = "0") Integer firstResult,
			@RequestParam(value = "last", required = false, defaultValue = "0") Integer maxResult,
			@RequestParam(value = "order", required = false, defaultValue = "name") String param_order,
			@RequestParam(required = false) String tag,
			HttpServletResponse response) {
		ResponseObject responseObject = new ResponseObject();
		List<Service> services = new ArrayList<Service>();
		try {
			if (token == null && tag == null) {
				services = Service.fromServiceEntities(catalogManager
						.catalogServices(firstResult, maxResult, param_order));
				if (services == null || services.size() == 0) {
					responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
					responseObject.setError("No service available");
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				} else {
					responseObject.setData(services);
					responseObject.setStatus(HttpServletResponse.SC_OK);
					responseObject
							.setTotalNumber(catalogManager.countService());
				}
			} else if (token != null && tag == null) {
				services = Service.fromServiceEntities(catalogManager
						.catalogServiceSimpleSearch(token, firstResult,
								maxResult, param_order));
				if (services == null || services.size() == 0) {
					responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
					responseObject
							.setError("No published service for this search by name");
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				} else {
					responseObject.setData(services);
					responseObject.setStatus(HttpServletResponse.SC_OK);
					responseObject.setTotalNumber(catalogManager
							.countServiceSimpleSearch(token, null));
				}
			} else if (token == null && tag != null) {
				List<Service> s = Service.fromServiceEntities(catalogManager
						.catalogServiceBrowseByTags(tag, firstResult,
								maxResult, param_order));
				if (s == null || s.size() == 0) {
					responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
					responseObject
							.setError("No service for this search by tags");
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				} else {
					responseObject.setData(s);
					responseObject.setStatus(HttpServletResponse.SC_OK);
					responseObject.setTotalNumber(catalogManager
							.countServiceByTagsSearch(tag));
				}
			}
		} catch (SecurityException s) {
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			responseObject
					.setError("Order value is wrong. It can be only id or name");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return responseObject;
	}

	/**
	 * Retrieve data of a specific service, which must be published or
	 * deprecated. Search is done by service id.
	 * 
	 * @param serviceId
	 *            : int id of published or deprecated service.
	 * @param response
	 *            : {@link HttpServletResponse} which is needed for status of
	 *            response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with services data, status (OK or NOT
	 *         FOUND) and error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/service/{serviceId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogServiceById(@PathVariable int serviceId,
			HttpServletResponse response) {
		Service service = Service.fromServiceEntity(catalogManager
				.catalogServiceById(serviceId));
		ResponseObject responseObject = new ResponseObject();
		if (service == null) {
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No published service with this id");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			responseObject.setData(service);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}

	/**
	 * Retrieve methods data of a specific published or deprecated service.
	 * Search by service id.
	 * 
	 * @param serviceId
	 *            : int id of published or deprecated service.
	 * @param response
	 *            : {@link HttpServletResponse} which is needed for status of
	 *            response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with services method data, status (OK or
	 *         NOT FOUND) and error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/service/{serviceId}/methods", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogServiceMethods(@PathVariable int serviceId,
			HttpServletResponse response) {
		List<Method> methods = catalogManager.catalogServiceMethods(serviceId);
		ResponseObject responseObject = new ResponseObject();
		if (methods == null || methods.size() == 0) {
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No methods for this published service");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			responseObject.setData(methods);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}

	/**
	 * Retrieve data history of a specific service. Search by service id.
	 * 
	 * @param serviceId
	 *            : int id of published or deprecated service.
	 * @param response
	 *            : {@link HttpServletResponse} which is needed for status of
	 *            response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with services history data, status (OK or
	 *         NOT FOUND) and error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/service/{serviceId}/history", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogServiceHistory(@PathVariable int serviceId,
			HttpServletResponse response) {
		List<ServiceHistory> history = catalogManager
				.catalogServiceHistory(serviceId);
		ResponseObject responseObject = new ResponseObject();
		if (history == null || history.size() == 0) {
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject
					.setError("No service history for this published service");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			responseObject.setData(history);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}

	/**
	 * /** Browse service in catalog by category. Every category has its own id
	 * and search is done by category id.
	 * 
	 * @param categoryId
	 *            : int category id
	 * @param firstResult
	 *            : start index
	 * @param maxResult
	 *            : number of elements
	 * @param param_order
	 *            : order parameter for resulted list
	 * @param response
	 *            : {@link HttpServletResponse} which is needed for status of
	 *            response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service data, status (OK or
	 *         NOT FOUND) and error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/service/category/{categoryId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogServiceByCategory(
			@PathVariable int categoryId,
			@RequestParam(value = "first", required = false, defaultValue = "0") Integer firstResult,
			@RequestParam(value = "last", required = false, defaultValue = "0") Integer maxResult,
			@RequestParam(value = "order", required = false, defaultValue = "name") String param_order,
			HttpServletResponse response) {
		List<Service> services = Service.fromServiceEntities(catalogManager
				.catalogServiceBrowseByCategory(categoryId, firstResult,
						maxResult, param_order));
		ResponseObject responseObject = new ResponseObject();
		if (services == null || services.size() == 0) {
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No published service for this category");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			responseObject.setData(services);
			responseObject.setStatus(HttpServletResponse.SC_OK);
			responseObject.setTotalNumber(catalogManager
					.countServiceByCategorySearch(categoryId));
		}
		return responseObject;
	}

	/**
	 * Browse service in catalog by multiple categories.
	 * 
	 * @param firstResult
	 *            : start index
	 * @param maxResult
	 *            : number of elements
	 * @param param_order
	 *            : order parameter for resulted list
	 * @param categoriesIds
	 *            : string of category id list
	 * @param response
	 *            : {@link HttpServletResponse} which is needed for status of
	 *            response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service data, status (OK or
	 *         NOT FOUND) and error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/service/category", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogServiceByCategories(
			@RequestParam(value = "first", required = false, defaultValue = "0") Integer firstResult,
			@RequestParam(value = "last", required = false, defaultValue = "0") Integer maxResult,
			@RequestParam(value = "order", required = false, defaultValue = "name") String param_order,
			@RequestParam(value = "categories", required = false) List<Integer> categoryIds,
			@RequestParam(value = "q", required = false) String token,
			HttpServletResponse response) {
		logger.debug("Service Catalog browse (categories)");

		List<Service> services = Service.fromServiceEntities(catalogManager
				.catalogServiceBrowseByCategories(categoryIds, firstResult,
						maxResult, token, param_order));
		ResponseObject responseObject = new ResponseObject();
		if (services == null || services.size() == 0) {
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject
					.setError("No published service for these categories");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			responseObject.setData(services);
			responseObject.setStatus(HttpServletResponse.SC_OK);
			Long count = catalogManager.countServiceSimpleSearch(token,
					categoryIds);
			responseObject.setTotalNumber(count);
		}
		return responseObject;
	}

	/**
	 * Browse service in catalog by organization. Search is done by organization
	 * id.
	 * 
	 * @param org
	 *            : int organization id
	 * @param firstResult
	 *            : start index
	 * @param maxResult
	 *            : number of elements
	 * @param param_order
	 *            : order parameter for resulted list
	 * @param response
	 *            : {@link HttpServletResponse} which is needed for status of
	 *            response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service data, status (OK or
	 *         NOT FOUND) and error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/service/org/{org}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogServiceByOrg(
			@PathVariable int org,
			@RequestParam(value = "q", required = false) String token,
			@RequestParam(value = "first", required = false, defaultValue = "0") Integer firstResult,
			@RequestParam(value = "last", required = false, defaultValue = "0") Integer maxResult,
			@RequestParam(value = "order", required = false, defaultValue = "name") String param_order,
			@RequestParam(value = "cats", required = false) List<Integer> cats,
			HttpServletResponse response) {

		logger.debug("Service Catalog browse by orgs");

		ResponseObject responseObject = new ResponseObject();
		try {
			List<Service> services = Service.fromServiceEntities(catalogManager
					.catalogServiceBrowseByOrg(org, firstResult, maxResult,
							cats, token, param_order));
			if (services == null || services.size() == 0) {
				responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseObject.setError("No service for this organization");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				responseObject.setData(services);
				responseObject.setStatus(HttpServletResponse.SC_OK);
				responseObject.setTotalNumber(catalogManager
						.countServiceByOrgSearch(org, cats, token));
			}
		} catch (SecurityException s) {
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			responseObject
					.setError("Order value is wrong. It can be only id or name");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return responseObject;
	}

	/**
	 * Retrieve list of all organizations or a partial one search by free text
	 * in name and description.
	 * 
	 * @param token
	 *            : String
	 * @param firstResult
	 *            : start index
	 * @param maxResult
	 *            : number of elements
	 * @param param_order
	 *            : order parameter for resulted list
	 * @param response
	 *            : {@link HttpServletResponse} which is needed for status of
	 *            response (OK, BAD REQUEST or NOT FOUND)
	 * @return {@link ResponseObject} with organizations data, status (OK, BAD
	 *         REQUEST or NOT FOUND) and error message (if status is BAD REQUEST
	 *         or NOT FOUND).
	 */
	@RequestMapping(value = "/org", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogOrgs(
			@RequestParam(value = "q", required = false) String token,
			@RequestParam(value = "first", required = false, defaultValue = "0") Integer firstResult,
			@RequestParam(value = "last", required = false, defaultValue = "0") Integer maxResult,
			@RequestParam(value = "order", required = false, defaultValue = "name") String param_order,
			@RequestParam(value = "cats", required = false) List<Integer> categoryIds,
			HttpServletResponse response) {
		logger.debug("Read organizations");

		ResponseObject responseObject = new ResponseObject();
		List<Organization> orgs = new ArrayList<Organization>();
		try {
			orgs = catalogManager.catalogOrg(token, categoryIds, firstResult,
					maxResult, param_order);
			if (orgs == null || orgs.size() == 0) {
				responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseObject.setError("No organization available");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				responseObject.setData(orgs);
				responseObject.setStatus(HttpServletResponse.SC_OK);
				responseObject.setTotalNumber(catalogManager.countOrg(token,
						categoryIds));
			}

		} catch (SecurityException s) {
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			responseObject
					.setError("Order value is wrong. It can be only id or name");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return responseObject;
	}

	/**
	 * Retrieve data of a specific organization. Search by its id.
	 * 
	 * @param id
	 *            : int organization id
	 * @param response
	 *            : {@link HttpServletResponse} which is needed for status of
	 *            response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with organization data, status (OK or NOT
	 *         FOUND) and error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/org/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogOrgById(@PathVariable int id,
			HttpServletResponse response) {
		logger.debug("Organization Catalog data");
		Organization org = catalogManager.catalogOrgById(id);
		ResponseObject responseObject = new ResponseObject();
		if (org == null) {
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No organization with this id");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			responseObject.setData(org);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}

	/**
	 * Browse organization by category. Every category has its own id and search
	 * is done by category id.
	 * 
	 * @param categoryId
	 *            : int path variable category id
	 * @param firstResult
	 *            : start index
	 * @param maxResult
	 *            : number of elements
	 * @param param_order
	 *            : order parameter for resulted list
	 * @param response
	 *            : {@link HttpServletResponse} which is needed for status of
	 *            response (OK, BAD REQUEST or NOT FOUND)
	 * @return {@link ResponseObject} with list of organization data, status (OK
	 *         or NOT FOUND) and error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/org/category/{categoryId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogOrgByCategory(
			@PathVariable int categoryId,
			@RequestParam(value = "first", required = false, defaultValue = "0") Integer firstResult,
			@RequestParam(value = "last", required = false, defaultValue = "0") Integer maxResult,
			@RequestParam(value = "order", required = false, defaultValue = "name") String param_order,
			HttpServletResponse response) {

		ResponseObject responseObject = new ResponseObject();
		try {
			List<Organization> orgs = catalogManager
					.catalogOrgBrowseByCategory(categoryId, firstResult,
							maxResult, param_order);
			if (orgs == null || orgs.size() == 0) {
				responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseObject
						.setError("No organization for this search by category");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				responseObject.setData(orgs);
				responseObject.setStatus(HttpServletResponse.SC_OK);
				responseObject.setTotalNumber(catalogManager.countOrg(null,
						null));
			}
		} catch (SecurityException s) {
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			responseObject
					.setError("Order value is wrong. It can be only id or name");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return responseObject;
	}

	/**
	 * Browse organization by categories.
	 * 
	 * @param firstResult
	 *            : start index
	 * @param maxResult
	 *            : number of element in list
	 * @param param_order
	 *            : parameter for ordering list
	 * @param categoriesIds
	 *            : id of categories
	 * @param response
	 *            : instance of {@link HttpServletResponse} that is needed for
	 *            status of response (OK, BAD REQUEST or NOT FOUND)
	 * @return {@link ResponseObject} with list of organization in the category,
	 *         status (OK, BAD REQUEST or NOT FOUND) and error message (if
	 *         status is BAD REQUEST or NOT FOUND).
	 */
	@RequestMapping(value = "/org/category", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogOrgByCategories(
			@RequestParam(value = "first", required = false, defaultValue = "0") Integer firstResult,
			@RequestParam(value = "last", required = false, defaultValue = "0") Integer maxResult,
			@RequestParam(value = "order", required = false, defaultValue = "name") String param_order,
			@RequestParam(value = "categories", required = false, defaultValue = "0") String categoriesIds,
			HttpServletResponse response) {
		logger.debug("Organization Catalog browse");

		String[] categoriesIdsStringArray = categoriesIds.split(",");
		int[] categoriesIdsArray = new int[categoriesIdsStringArray.length];
		List<Integer> listCats = new ArrayList<Integer>();
		for (int i = 0; i < categoriesIdsStringArray.length; i++) {
			try {
				categoriesIdsArray[i] = Integer
						.parseInt(categoriesIdsStringArray[i]);
				listCats.add(Integer.parseInt(categoriesIdsStringArray[i]));
			} catch (NumberFormatException nfe) {
				continue;
			}
		}

		ResponseObject responseObject = new ResponseObject();
		try {
			List<Organization> orgs = catalogManager
					.catalogOrgBrowseByCategories(categoriesIdsArray,
							firstResult, maxResult, param_order);
			if (orgs == null || orgs.size() == 0) {
				responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseObject
						.setError("No organization for this search by categories");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				responseObject.setData(orgs);
				responseObject.setStatus(HttpServletResponse.SC_OK);
				responseObject.setTotalNumber(catalogManager.countOrg(null,
						listCats));
			}
		} catch (SecurityException s) {
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			responseObject
					.setError("Order value is wrong. It can be only id or name");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return responseObject;
	}

	/**
	 * Browse services group by category.
	 * 
	 * @param response
	 *            {@link HttpServletResponse} which is needed for status of
	 *            response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service and category data,
	 *         status (OK or NOT FOUND) and error message (if status is NOT
	 *         FOUND).
	 */
	@RequestMapping(value = "/category/services", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogCategoryServices(HttpServletResponse response) {
		CategoryServices cat = catalogManager.getCategoryServices();
		ResponseObject responseObject = new ResponseObject();
		if (cat == null) {
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("There is no category.");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			responseObject.setData(cat);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}

	/**
	 * Retrieve news.
	 * 
	 * @param n
	 *            : int, number of news that are retrieved
	 * @param response
	 *            : {@link HttpServletResponse} which is needed for status of
	 *            response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service history (news),
	 *         status (OK or NOT FOUND) and error message (if status is NOT
	 *         FOUND).
	 */
	@RequestMapping(value = "/news", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject catalogNews(@RequestParam int n,
			HttpServletResponse response) {
		List<News> news = catalogManager.getNews(n);
		ResponseObject responseObject = new ResponseObject();
		if (news == null || news.size() == 0) {
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("There is no recent news");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			responseObject.setData(news);
			responseObject.setStatus(HttpServletResponse.SC_OK);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}

	/**
	 * Retrieve tag counter.
	 * 
	 * @param group
	 *            : String, its values are tag or counter. This parameter orders
	 *            list of tagged services
	 * @param order
	 *            : String, its values are ASC or DESC.
	 * @param response
	 *            : {@link HttpServletResponse} which is needed for status of
	 *            response (OK, BAD REQUEST or NOT FOUND)
	 * @return {@link ResponseObject} with list of tag counter, status (OK, BAD
	 *         REQUEST or NOT FOUND) and error message (if status is NOT FOUND
	 *         or BAD REQUEST).
	 */
	@RequestMapping(value = "/tagcloud", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject catalogTag(
			@RequestParam(value = "group", required = false, defaultValue = "tag") String group,
			@RequestParam(value = "order", required = false, defaultValue = "ASC") String order,
			HttpServletResponse response) {
		ResponseObject responseObject = new ResponseObject();
		if ((group.equalsIgnoreCase(OrderBy.tag.toString()) || group
				.equalsIgnoreCase(OrderBy.counter.toString()))
				&& (order.equalsIgnoreCase(ORDER.ASC.toString()) || order
						.equalsIgnoreCase(ORDER.DESC.toString()))) {
			try {
				List<TagCounter> tglist = catalogManager
						.getTagsServicesCounter(group, order);
				if (tglist == null || tglist.size() == 0) {
					responseObject
							.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					responseObject
							.setError("There is a problem in connecting with database");
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				} else {
					responseObject.setData(tglist);
					responseObject.setStatus(HttpServletResponse.SC_OK);
				}
			} catch (EntityNotFoundException e) {
				responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseObject.setError("There is no tags");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} else {
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			responseObject
					.setError("Wrong input parameters: group value can be tag or counter;"
							+ " order value can be ASC or DESC.");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return responseObject;
	}

}
