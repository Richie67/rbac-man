package be.cetic.rbac.man.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="role")
public class Role {

	private int id;
	private String name;
	
	@JsonProperty(value="name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty(value="id")
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}	
}
