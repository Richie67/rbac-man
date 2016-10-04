package be.cetic.rbac.man.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="user")
public class User {
	
	private String username;	
	private int id;
	private Role role;
	
	@JsonProperty(value="username")
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@JsonProperty(value="id")
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@JsonProperty(value="role")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	
	
}
