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

public class Action extends XacmlRequestEntity{
	private static final String ACTION_PREFIX = PREFIX + "action:";
	public static final String ACTION_NAME = ACTION_PREFIX + "action-name";
	
	
	private String actionName;
	
	public void setActionId(String actionId){
		this.actionName = actionId;
	}
	
	private Attribute getActionId() throws XacmlEntityException{
		try{
			URI id = new URI(ACTION_NAME);
			URI type = getString();
			String issuer = null; 
			DateTimeAttribute issueInstant = new DateTimeAttribute(new Date());
			AttributeValue attrValue = AttributeFactory.createAttribute(type, actionName);
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
	
	public Set<com.sun.xacml.ctx.Attribute> getAction() throws XacmlEntityException{
		Set<Attribute> attributes = new HashSet<Attribute>();
		attributes.add(getActionId());
		attributes.addAll(this.attributes);
		return attributes;
	}
}
