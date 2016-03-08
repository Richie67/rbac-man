package be.cetic.rbac.man.json;

import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="resource")
public class Resource {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
