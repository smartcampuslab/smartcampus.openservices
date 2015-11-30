package eu.trentorise.smartcampus.openservices.managers;

import it.smartcommunitylab.openservices.wadl.Application;
import it.smartcommunitylab.openservices.wadl.Param;
import it.smartcommunitylab.openservices.wadl.ParamStyle;
import it.smartcommunitylab.openservices.wadl.Representation;
import it.smartcommunitylab.openservices.wadl.Request;
import it.smartcommunitylab.openservices.wadl.Resource;
import it.smartcommunitylab.openservices.wadl.Resources;
import it.smartcommunitylab.openservices.wadl.ext.Tags;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.openservices.AuthProtocol;
import eu.trentorise.smartcampus.openservices.entities.Authentication;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.Tag;

@Component
public class WADLGenerator {

	private static final Logger logger = LoggerFactory
			.getLogger(WADLGenerator.class);
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
		Service s = serviceManager.getServiceById(serviceId);
		String wadl = null;

		if (s.getWadlDocContent() != null) {
			wadl = s.getWadlDocContent();
		} else {
			Application app = generate(serviceId);

			try {
				JAXBContext jaxbContext = JAXBContext
						.newInstance(
								Application.class,
								it.smartcommunitylab.openservices.wadl.ext.Authentication.class,
								Tags.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbContext = JAXBContext.newInstance(Application.class);
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);
				jaxbMarshaller.setProperty(
						"com.sun.xml.bind.namespacePrefixMapper",
						new WadlNamespacePrefixMapper());
				StringWriter stringWriter = new StringWriter();
				jaxbMarshaller.marshal(app, stringWriter);
				wadl = stringWriter.toString();
			} catch (JAXBException e) {
				logger.error("", e);
			}
		}
		return wadl;
	}

	public Application generate(int serviceId) {
		Service s = serviceManager.getServiceById(serviceId);
		return generate(s);
	}

	public Application generate(Service s) {
		List<Method> methods = serviceManager.getServiceMethodsByServiceId(s
				.getId());
		Application app = new Application();
		Resources resources = new Resources();
		String[] formats = new String[0];
		it.smartcommunitylab.openservices.wadl.ext.Authentication authWadlTag = null;

		if (s.getAccessInformation() != null) {
			resources.setBase(s.getAccessInformation().getEndpoint());
			if (s.getAccessInformation().getFormats() != null) {
				formats = s.getAccessInformation().getFormats().split(",");
			}

			// set authentication fields
			Authentication auth = s.getAccessInformation().getAuthentication();
			if (auth != null) {
				authWadlTag = new it.smartcommunitylab.openservices.wadl.ext.Authentication();
				if (auth.getAccessProtocol().equalsIgnoreCase(
						AuthProtocol.Public.toString())) {
					authWadlTag.setRequired("false");
				} else {
					authWadlTag.setRequired("true");
					authWadlTag.setValue(auth.getAccessProtocol());
				}
			}
		}
		app.getResources().add(resources);

		// tags
		Tags t = null;
		if (s.getTags() != null) {
			t = new Tags();
			for (Tag tag : s.getTags()) {
				it.smartcommunitylab.openservices.wadl.ext.Tag wadlTag = new it.smartcommunitylab.openservices.wadl.ext.Tag();
				wadlTag.setValue(tag.getName());
				t.getTag().add(wadlTag);
			}

		}
		for (Method m : methods) {
			Resource res = new Resource();
			res.setId(m.getName());
			if (t != null) {
				res.getAny().add(t);
			}
			if (authWadlTag != null) {
				res.getAny().add(authWadlTag);
			}
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

	public boolean isValidWADL(String wadl) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Application.class);
			Unmarshaller unmarshal = jaxbContext.createUnmarshaller();
			StringReader stringReader = new StringReader(wadl);

			return unmarshal.unmarshal(stringReader) != null;

		} catch (JAXBException e) {
			return false;
		}
	}

	private class WadlNamespacePrefixMapper extends
			com.sun.xml.bind.marshaller.NamespacePrefixMapper {

		private static final String WADL_PREFIX = ""; // DEFAULT NAMESPACE
		private static final String WADL_URI = "http://wadl.dev.java.net/2009/02";

		private static final String EXT_WADL_PREFIX = "xwadl";
		private static final String EXT_WADL_URI = "wadl-extensions";

		@Override
		public String getPreferredPrefix(String namespaceUri,
				String suggestion, boolean requirePrefix) {
			if (WADL_URI.equals(namespaceUri)) {
				return WADL_PREFIX;
			} else if (EXT_WADL_URI.equals(namespaceUri)) {
				return EXT_WADL_PREFIX;
			} else {
				return suggestion;
			}

		}

	}
}
