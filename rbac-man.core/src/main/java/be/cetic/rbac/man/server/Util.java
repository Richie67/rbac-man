package be.cetic.rbac.man.server;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import com.sun.xacml.attr.AttributeFactory;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;

import be.cetic.rbac.man.json.Action;
import be.cetic.rbac.man.json.Resource;
import be.cetic.rbac.man.json.User;

public class Util {

	private RequestCtx requestCtx;
	
	
	public static RequestCtx buildRequest(User subject, Action action, Resource resource)
	throws Exception{		
		// build the xacml request		

			Set<com.sun.xacml.ctx.Subject> subjects = new HashSet<com.sun.xacml.ctx.Subject>();
			Set<Attribute> attributes = new HashSet<Attribute>();
			URI idURI = new URI("urn:oasis:names:tc:xacml:1.0:subject:username");
			URI typeURI = new URI("http://www.w3.org/2001/XMLSchema#string");
			AttributeValue attrValue = AttributeFactory.createAttribute(typeURI, subject.getUsername());
			Attribute subjectAttr = new Attribute(idURI, null,  null, attrValue);
			attributes.add(subjectAttr);
			if(subject.getRole() != null){
				idURI = new URI("urn:oasis:names:tc:xacml:1.0:subject:role");
				attrValue = AttributeFactory.createAttribute(typeURI, subject.getRole().getName());
				Attribute subjectRoleAttr = new Attribute(idURI, null,  null, attrValue);
				attributes.add(subjectRoleAttr);
			}
			com.sun.xacml.ctx.Subject xacmlSubject = new com.sun.xacml.ctx.Subject(attributes);
			subjects.add(xacmlSubject);
			
			Set<Attribute> xacmlResource = new HashSet<Attribute>();
			idURI = new URI("urn:oasis:names:tc:xacml:1.0:resource:resource-id");
			typeURI = new URI("http://www.w3.org/2001/XMLSchema#anyURI");
			attrValue = AttributeFactory.createAttribute(typeURI, resource.getUrl());
			Attribute resourceAttr = new Attribute(idURI, null,  null, attrValue);			
			xacmlResource.add(resourceAttr);			
			
			Set<Attribute> xacmlAction = new HashSet<Attribute>();
			idURI = new URI("urn:oasis:names:tc:xacml:1.0:action:action-name");
			typeURI = new URI("http://www.w3.org/2001/XMLSchema#string");
			attrValue = AttributeFactory.createAttribute(typeURI, action.getName());
			Attribute actionAttr = new Attribute(idURI, null,  null, attrValue);
			xacmlAction.add(actionAttr);
			
			Set<Attribute> environment = new HashSet<Attribute>();
			
			return  new RequestCtx(subjects, xacmlResource, xacmlAction, environment);		
	}
	
	
	public static File getPoliciesDirectory(){
		File tomcatRoot = new File(System.getProperty("catalina.home"));
		File policiesDirectory = new File(tomcatRoot, "_rbacman/policies");
		if(!policiesDirectory.exists())
			policiesDirectory.mkdirs();
		return policiesDirectory;
	}
}
