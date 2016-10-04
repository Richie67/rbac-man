package be.cetic.rbac.man.xaml.generator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.sun.xacml.attr.DateTimeAttribute;
import com.sun.xacml.ctx.Attribute;

public class XacmlRequestEntity {
	public static final String PREFIX = "urn:oasis:names:tc:xacml:1.0:";
	protected Set<Attribute> attributes;
	
	private static final URI getType(String type){
		try{
			return new URI(type);
		}
		catch(URISyntaxException e){
			return null;
		}
	}
	
	public static final URI getString(){
		return getType("http://www.w3.org/2001/XMLSchema#string");
	}
	
	public static final URI getAnyURI(){
		return getType("http://www.w3.org/2001/XMLSchema#anyURI");
	}
	
	
	public XacmlRequestEntity(){
		attributes = new HashSet<Attribute>();
	}
	
	public Set<Attribute> getAttributes(){
		return attributes;
	}
	
	public void setAttributes(Set<Attribute> attributes){
		this.attributes = attributes;
	}
	
	public void addAttribute(Attribute attribute){
		attributes.add(attribute);
	}
	
	public DateTimeAttribute getIssueInstant(){
		DateTimeAttribute issueInstant = new DateTimeAttribute(new Date());
		return issueInstant;
	}
}
