package be.cetic.rbac.man.server;

import be.cetic.rbac.man.json.User;

public interface PIP {
	
	public User getUser(String username);
	
}
