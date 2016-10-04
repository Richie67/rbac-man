package be.cetic.rbac.man.xaml.generator;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import com.sun.xacml.attr.AttributeFactory;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.DateTimeAttribute;
import com.sun.xacml.ctx.Attribute;

public class Subject extends XacmlRequestEntity{
	private static final String SUBJECT_PREFIX = PREFIX + "subject:";
	public static final String SUBJECT_ID = SUBJECT_PREFIX + "username";
	public static final String SUBJECT_ROLE = SUBJECT_PREFIX  +"role"; 
	
	
	private String subjectId;
	private String subjectRole;
	
	public void setSubjectId(String subjectId){
		this.subjectId = subjectId;
	}
	
	public String getSUbjectId(){
		return subjectId;
	}
	
	public void setSubjectRole(String subjectRole){
		this.subjectRole = subjectRole;
	}
	
	public String getRoleName(){
		return subjectRole;
	}
	
	public Attribute getSubjectRole() throws RoleException{
		try{
			URI id = new URI(SUBJECT_ROLE);
			String issuer = null;
			URI type = getString();
			DateTimeAttribute issueInstant = getIssueInstant();
			AttributeValue attrValue = AttributeFactory.createAttribute(type, subjectRole);
			Attribute userRole = new Attribute(id, issuer, issueInstant, attrValue);
			return userRole;
		}
		catch(Exception e){
			throw new RoleException(e);
		}
	}
	
	private Attribute getSubjectId() throws SubjectIdException{
		try{
			URI id = new URI(SUBJECT_ID);
			URI type = getString();
			String issuer = null; 
			DateTimeAttribute issueInstant = getIssueInstant();
			AttributeValue attrValue = AttributeFactory.createAttribute(type, subjectId);
			Attribute attribute = new Attribute(id, issuer, issueInstant, attrValue);
			return attribute;
		}
		catch(Exception e){
			throw new SubjectIdException(e);
		}		
	}
	
	public com.sun.xacml.ctx.Subject getSubject() throws XacmlEntityException{
		Set<Attribute> attributes = new HashSet<Attribute>();
		attributes.add(getSubjectId());
		attributes.add(getSubjectRole());
		attributes.addAll(this.attributes);
		com.sun.xacml.ctx.Subject subject = new com.sun.xacml.ctx.Subject(attributes);
		return subject;
	}
}
