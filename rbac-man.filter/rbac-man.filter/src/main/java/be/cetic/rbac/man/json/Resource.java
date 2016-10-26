package be.cetic.rbac.man.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="resource")
public class Resource {
	private int id;
	private String name;
	private String url;
	private String description;

	@JsonProperty(value="id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@JsonProperty(value="uri")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@JsonProperty(value="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty(value="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
