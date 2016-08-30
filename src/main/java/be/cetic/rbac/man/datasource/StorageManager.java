package be.cetic.rbac.man.datasource;

import java.sql.SQLException;
import java.util.List;

import be.cetic.rbac.man.json.Action;
import be.cetic.rbac.man.json.Resource;
import be.cetic.rbac.man.json.Rule;
import be.cetic.rbac.man.json.User;

public interface StorageManager {

	public User getUser(int id) throws SQLException;
	public List<User> getUsers(String orderBy, int ascending, int limit) throws SQLException;
	public User createUser(User user) throws SQLException;
	public User updateUser(User user) throws SQLException;
	public boolean deleteUser(int id) throws SQLException;
	
	public Action getAction(int id) throws SQLException;
	public List<Action> getActions(String orderBy, int ascending, int limit) throws SQLException;
	public Action createAction(Action action) throws SQLException;
	public Action updateAction(Action action) throws SQLException;
	public boolean deleteAction(int id) throws SQLException;
	
	public Resource getResource(int id) throws SQLException;
	public List<Resource> getResources(String orderBy, int ascending, int limit) throws SQLException;
	public Resource createResource(Resource resource) throws SQLException;
	public Resource updateResource(Resource resource) throws SQLException;
	public boolean deleteResource(int id) throws SQLException;
	
	public Rule getRule(int id) throws SQLException;
	public List<Rule> getRules(String orderBy, int ascending, int limit) throws SQLException;
	public Rule createRule(Rule rule) throws SQLException;
	public Rule updateRule(Rule rule) throws SQLException;
	public boolean deleteRule(int id) throws SQLException;
	
	
	
}
