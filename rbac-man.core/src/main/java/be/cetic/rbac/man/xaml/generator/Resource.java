package be.cetic.rbac.man.xaml.generator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.sun.xacml.ParsingException;
import com.sun.xacml.UnknownIdentifierException;
import com.sun.xacml.attr.AttributeFactory;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.DateTimeAttribute;
import com.sun.xacml.ctx.Attribute;

public class Resource extends XacmlRequestEntity{
	private static final String RESOURCE_PREFIX = PREFIX + "resource:";
	public static final String RESOURCE_ID = RESOURCE_PREFIX + "resource-id";
	
	
	private String resourceId;
	
	public void setResourceId(String resourceId){
		this.resourceId = resourceId;
	}
	
	private Attribute getResourceId() throws XacmlEntityException{
		try{
			URI id = new URI(RESOURCE_ID);
			URI type = getAnyURI();
			String issuer = null; 
			DateTimeAttribute issueInstant = new DateTimeAttribute(new Date());
			AttributeValue attrValue = AttributeFactory.createAttribute(type, resourceId);
			Attribute attribute = new Attribute(id, issuer, issueInstant, attrValue);
			return attribute;
		}
		catch(URISyntaxException e){
			throw new XacmlEntityException(e);
		}
		catch(ParsingException e){
			throw new XacmlEntityException(e);
		}
		catch(UnknownIdentifierException e){
			throw new XacmlEntityException(e);
		}	
	}
	
	public Set<com.sun.xacml.ctx.Attribute> getResource() throws XacmlEntityException{
		Set<Attribute> attributes = new HashSet<Attribute>();
		attributes.add(getResourceId());
		attributes.addAll(this.attributes);
		return attributes;
	}
}
