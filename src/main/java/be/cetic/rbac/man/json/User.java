package be.cetic.rbac.man.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="user")
public class User {
	
	private String username;
	private String firstname;
	private String lastname;
	private int id;
	private String role;
	
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

	@JsonProperty(value="firstname")
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@JsonProperty(value="lastname")
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@JsonProperty(value="role")
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
	
}
