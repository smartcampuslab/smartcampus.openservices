package it.smartcommunitylab.openservices.usdl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class Tags {
	private static Model m_model = ModelFactory.createDefaultModel();
	public static final String NS = "http://www.holygoat.co.uk/owl/redwood/0.1/tags#";

	public static String getURI() {
		return NS;
	}

	public static final Resource NAMESPACE = m_model.createResource(NS);

	public static final Property tag = m_model.createProperty(NS + "tag");
}
