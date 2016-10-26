package be.cetic.rbac.man.json;



import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="rule")
public class Rule {

	private int id;
	private String name;
	private Role role;
	private Action action;
	private Resource resource;
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
	
	@JsonProperty(value="role")
	public Role getRole() {
		return role;
	}
		
	public void setRole(Role role){
		this.role = role;
	}
	
	@JsonProperty(value="action")
	public Action getAction() {
		return action;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	@JsonProperty(value="resource")
	public Resource getResource() {
		return resource;
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	@JsonProperty(value="isPermit")
	public boolean isPermit() {
		return isPermit;
	}
	
	public void setPermit(boolean isPermit) {
		this.isPermit = isPermit;
	}	
}
