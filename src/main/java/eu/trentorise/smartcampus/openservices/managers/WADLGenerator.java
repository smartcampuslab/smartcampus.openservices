package eu.trentorise.smartcampus.openservices.managers;

import it.smartcommunitylab.openservices.wadl.Application;
import it.smartcommunitylab.openservices.wadl.Param;
import it.smartcommunitylab.openservices.wadl.ParamStyle;
import it.smartcommunitylab.openservices.wadl.Representation;
import it.smartcommunitylab.openservices.wadl.Request;
import it.smartcommunitylab.openservices.wadl.Resource;
import it.smartcommunitylab.openservices.wadl.Resources;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Service;

@Component
public class WADLGenerator {

	@Autowired
	private ServiceManager serviceManager;

	private static final Map<String, String> mediaTypes = new HashMap<String, String>();

	private static final String DEFAULT_MEDIA_TYPE = "application/json";

	static {
		mediaTypes.put("JSON", "application/json");
		mediaTypes.put("XML", "application/xml");
		mediaTypes.put("TEXT", "text/plain");
	}

	public Resource appendWADLNodes(Resource res, String path) {
		Pattern p = Pattern.compile("/\\{(\\w+)\\}");
		Matcher m = p.matcher(path);
		while (m.find()) {
			Param token = new Param();
			token.setName(m.group(1));
			token.setStyle(ParamStyle.TEMPLATE);
			token.setRequired(true);
			res.getParam().add(token);
		}

		p = Pattern.compile("\\?(\\w+)=");
		m = p.matcher(path);
		while (m.find()) {
			Param token = new Param();
			token.setName(m.group(1));
			token.setStyle(ParamStyle.QUERY);
			res.getParam().add(token);
		}

		p = Pattern.compile("&(\\w+)=");
		m = p.matcher(path);
		while (m.find()) {
			Param token = new Param();
			token.setName(m.group(1));
			token.setStyle(ParamStyle.QUERY);
			res.getParam().add(token);
		}

		return res;
	}

	public String generateAsString(int serviceId) {
		Application app = generate(serviceId);

		String wadl = null;
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(Application.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbContext = JAXBContext.newInstance(Application.class);
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter stringWriter = new StringWriter();
			jaxbMarshaller.marshal(app, stringWriter);
			wadl = stringWriter.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return wadl;
	}

	public Application generate(int serviceId) {
		Service s = serviceManager.getServiceById(serviceId);
		List<Method> methods = serviceManager
				.getServiceMethodsByServiceId(serviceId);
		Application app = new Application();
		Resources resources = new Resources();
		String[] formats = new String[0];

		if (s.getAccessInformation() != null) {
			resources.setBase(s.getAccessInformation().getEndpoint());
			if (s.getAccessInformation().getFormats() != null) {
				formats = s.getAccessInformation().getFormats().split(",");
			}
		}
		app.getResources().add(resources);
		for (Method m : methods) {
			Resource res = new Resource();
			res.setId(m.getName());
			if (m.getExecutionProperties() != null) {
				String path = m.getExecutionProperties()
						.getRequestPathTemplate();
				if (path != null) {
					res.setPath(path);
				}
				res = appendWADLNodes(res, path);
				it.smartcommunitylab.openservices.wadl.Method method = new it.smartcommunitylab.openservices.wadl.Method();
				method.setName(m.getExecutionProperties().getHttpMethod());
				res.getMethodOrResource().add(method);
				Request req = new Request();
				it.smartcommunitylab.openservices.wadl.Response resp = new it.smartcommunitylab.openservices.wadl.Response();
				for (String format : formats) {
					Representation repr = new Representation();
					repr.setMediaType(mediaTypes.get(format) != null ? mediaTypes
							.get(format) : DEFAULT_MEDIA_TYPE);
					req.getRepresentation().add(repr);
					resp.getRepresentation().add(repr);
				}
				method.setRequest(req);
				method.getResponse().add(resp);
				resources.getResource().add(res);
			}
		}
		return app;
	}
}
