package be.cetic.rbac.man.datasource;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import be.cetic.rbac.man.json.Action;
import be.cetic.rbac.man.json.Resource;
import be.cetic.rbac.man.json.Rule;
import be.cetic.rbac.man.json.User;

public class SqliteManager implements StorageManager{
	public static final Logger logger = Logger.getLogger(SqliteManager.class.getName());
	
	private Connection connection;
	
	public SqliteManager(){
		try{
			Class.forName("org.sqlite.JDBC");
			logger.log(Level.INFO, "Driver loaded");
			String catalina_home = System.getProperty("catalina.base");
			File databaseFile = new File(catalina_home, "_rbacman/rbac-man.db");
			if(!databaseFile.exists()){
				logger.log(Level.INFO, "Instanciate database rbacman");
				new File(catalina_home, "_rbacman").mkdirs();
				File source = new File(catalina_home, "webapps/RbacMan/WEB-INF/classes/rbac-man.db");				
				Files.copy(source.toPath(), databaseFile.toPath());
			}
			connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
			
		}
		catch(Exception ex){
			logger.log(Level.WARN, ex.getMessage(), ex);
		}
	}
	
	public User getUser(int id) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM tbl_user WHERE id=?");
		statement.setInt(1,  id);
		ResultSet result = statement.executeQuery();
		result.next();
		User user = buildUser(result);
		result.close();
		statement.close();
		return user;
	}

	public List<User> getUsers(String orderBy, int ascending, int limit) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM tbl_user ORDER BY ? LIMIT ?");
		statement.setString(1,  orderBy);
		statement.setInt(2,  limit);
		ResultSet result = statement.executeQuery();
		List<User> users = new ArrayList<User>();
		while(result.next()){
			User user = buildUser(result);
			users.add(user);
		}
		result.close();
		statement.close();
		return users;
		
	}

	public User createUser(User user) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("INSERT INTO tbl_user (username, lastname, firstname) VALUES (?, ?, ?)");
		statement.setString(1,  user.getUsername());
		statement.setString(2,  user.getLastname());
		statement.setString(3,  user.getFirstname());
		statement.executeUpdate();
		statement.close();
		return null;
	}

	public User updateUser(User user) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("UPDATE tbl_user SET username=?, lastname=?, firstname=? WHERE id=?");
		statement.setString(1,  user.getUsername());
		statement.setString(2,  user.getLastname());
		statement.setString(3,  user.getFirstname());
		statement.setInt(4,  user.getId());
		statement.executeUpdate();
		statement.close();
		return user;
	}

	public boolean deleteUser(int id) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("DELETE FROM tbl_user WHERE id=?");
		statement.setInt(1,  id);
		statement.executeUpdate();
		return true;
	}

	public Action getAction(int id) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM tbl_action WHERE id=?");
		statement.setInt(1,  id);
		ResultSet result = statement.executeQuery();
		result.next();
		Action action = buildAction(result);
		result.close();
		statement.close();
		return action;
	}

	public List<Action> getActions(String orderBy, int ascending, int limit) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM tbl_action ORDER BY ? LIMIT ?");
		statement.setString(1,  orderBy);
		statement.setInt(2,  limit);
		ResultSet result = statement.executeQuery();
		List<Action> actions = new ArrayList<Action>();
		while(result.next()){
			Action action = buildAction(result);
			actions.add(action);
		}
		result.close();
		statement.close();
		return actions;
	}

	public Action createAction(Action action) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("INSERT INTO tbl_action (name) values (?)");
		statement.setString(1,  action.getName());
		statement.executeUpdate();
		statement.close();
		return null;
	}

	public Action updateAction(Action action) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("UPDATE tbl_action SET name=? WHERE id=?");
		statement.setString(1,  action.getName());
		statement.setInt(2,  action.getId());
		statement.executeUpdate();
		statement.close();
		return action;
	}

	public boolean deleteAction(int id) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("DELETE FROM tbl_action WHERE id=?");
		statement.setInt(1,  id);
		statement.executeUpdate();
		statement.close();
		return true;
	}

	public Resource getResource(int id) throws SQLException{
		// TODO Auto-generated method stub
		return null;
	}

	public List<Resource> getResources(String orderBy, int ascending, int limit) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM tbl_resource ORDER BY ? LIMIT ?");
		statement.setString(1,  orderBy);
		statement.setInt(2,  limit);
		List<Resource> resources = new ArrayList<Resource>();
		ResultSet result = statement.executeQuery();
		while(result.next()){
			Resource resource = buildResource(result);
			resources.add(resource);
		}
		result.close();
		statement.close();
		return resources;
		
	}

	public Resource createResource(Resource resource) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("INSERT INTO tbl_resource (name, url) VALUES (?, ?)");
		statement.setString(1,  resource.getName());
		statement.setString(2,  resource.getUrl());
		statement.executeUpdate();
		statement.close();
		return null;
	}

	public Resource updateResource(Resource resource) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("UPDATE tbl_resource SET name=?, url=? WHERE id=?");
		statement.setString(1,  resource.getName());
		statement.setString(2,  resource.getUrl());
		statement.setInt(3, resource.getId());
		statement.executeUpdate();
		statement.close();
		return resource;
	}

	public boolean deleteResource(int id) throws SQLException{
		PreparedStatement statement =  connection.prepareStatement("DELETE FROM tbl_resource WHERE id=?");
		statement.setInt(1,  id);
		statement.executeUpdate();
		statement.close();
		return true;
	}

	public Rule getRule(int id) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM tbl_rule WHERE id=?");
		statement.setInt(1,  id);
		ResultSet result = statement.executeQuery();
		result.next();
		Rule rule = buildRule(result);		
		result.close();
		statement.close();
		return rule;
	}

	public List<Rule> getRules(String orderBy, int ascending, int limit) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM tbl_rule ORDER BY ? LIMIT ?");
		statement.setString(1, orderBy);
		statement.setInt(2,  limit);
		ResultSet result = statement.executeQuery();
		List<Rule> rules = new ArrayList<Rule>();
		while(result.next()){
			Rule rule = buildRule(result);
			rules.add(rule);
		}
		result.close();
		statement.close();
		return rules;
	}

	public Rule createRule(Rule rule) throws SQLException{
		// TODO Auto-generated method stub
		return null;
	}

	public Rule updateRule(Rule rule) throws SQLException{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteRule(int id) throws SQLException{
		// TODO Auto-generated method stub
		return false;
	}

	
	private User buildUser(ResultSet result) throws SQLException{
		User user = new User();
		user.setId(result.getInt("id"));
		user.setUsername(result.getString("username"));
		user.setFirstname(result.getString("firstname"));
		user.setLastname(result.getString("lastname"));
		return user;
	}
	
	private Action buildAction(ResultSet result) throws SQLException{
		Action action = new Action();
		action.setName(result.getString("name"));
		action.setId(result.getInt("id"));
		return action;
	}
	
	private Resource buildResource(ResultSet result) throws SQLException{
		Resource resource = new Resource();
		resource.setId(result.getInt("id"));
		resource.setName(result.getString("name"));
		resource.setUrl(result.getString("url"));
		return resource;
	}
	
	private Rule buildRule(ResultSet result) throws SQLException{
		Rule rule = new Rule();
		rule.setId(result.getInt("id"));
		rule.setName(result.getString("name"));
		rule.setPermit(result.getBoolean("isPermit"));
		return rule;
	} 
	
	public  static void main(String[] args){
		SqliteManager manager = new SqliteManager();
		try{
		List<User> users = manager.getUsers("username", -1, 10);
		System.out.println(users.size());
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
