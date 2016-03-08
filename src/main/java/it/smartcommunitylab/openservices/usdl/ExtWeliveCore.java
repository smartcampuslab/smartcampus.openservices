package it.smartcommunitylab.openservices.usdl;

import it.smartcommunitylab.openservices.usdl.WeliveCore;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

public class ExtWeliveCore extends WeliveCore {
	private static Model m_model = ModelFactory.createDefaultModel();

	public static final Resource Owner = m_model.createResource(NS + "Owner");
	public static final Resource Provider = m_model.createResource(NS
			+ "Provider");
	
	public static final Resource WebService = m_model.createResource(NS + "WebService");

}