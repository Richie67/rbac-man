package be.cetic.rbac.man.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="request")
public class Request {

	private User subject;
	private Resource resource;
	private Action action;
	
	@JsonProperty(value="subject")
	public User getSubject() {
		return subject;
	}
		
	public void setSubject(User subject) {
		this.subject = subject;
	}
	
	@JsonProperty(value="resource")
	public Resource getResource() {
		return resource;
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	@JsonProperty(value="action")
	public Action getAction() {
		return action;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}

}
