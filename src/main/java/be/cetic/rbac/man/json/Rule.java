package be.cetic.rbac.man.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="rule")
public class Rule {

	private int id;
	private String name;
	private List<User> users;
	private List<Action> actions;
	private List<Resource> resources;
	private boolean isPermit;
	
	@JsonProperty(value="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@JsonProperty(value="name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty(value="users")
	public List<User> getUsers() {
		return users;
	}
		
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	@JsonProperty(value="actions")
	public List<Action> getActions() {
		return actions;
	}
	
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
	@JsonProperty(value="resources")
	public List<Resource> getResources() {
		return resources;
	}
	
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	
	@JsonProperty(value="isPermit")
	public boolean isPermit() {
		return isPermit;
	}
	
	public void setPermit(boolean isPermit) {
		this.isPermit = isPermit;
	}	
}
