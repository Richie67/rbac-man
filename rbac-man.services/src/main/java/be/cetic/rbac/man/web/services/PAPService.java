package be.cetic.rbac.man.web.services;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;

import be.cetic.rbac.man.datasource.SqliteManager;
import be.cetic.rbac.man.datasource.StorageManager;
import be.cetic.rbac.man.json.Action;
import be.cetic.rbac.man.json.Resource;
import be.cetic.rbac.man.json.Role;
import be.cetic.rbac.man.json.Rule;
import be.cetic.rbac.man.json.User;
import be.cetic.rbac.man.xaml.generator.XACMLGenerator;

@Path("/")
public class PAPService {

	private static Logger logger = Logger.getLogger(PAPService.class.getName());
	
	@Context 
	private ServletContext context;
	
	private ObjectMapper mapper = new ObjectMapper();
	private StorageManager storageManager = new SqliteManager();
	
	@POST	
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/users")
	public Response createUser(String data){
		try{
			logger.log(Level.INFO, "Create user "+ data);
			User user = mapper.readValue(data, User.class);
			storageManager.createUser(user);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch(Exception ex){
			logger.log(Level.WARNING, ex.getMessage(), ex);
			return Response.serverError().entity(ex.getMessage()).build();
		}
	}
	
	@GET
	@Path("/users")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUsers(
			@QueryParam("query") String query,
			@DefaultValue("10") @QueryParam("limit") int limit,
			@DefaultValue("id") @QueryParam("orderBy") String orderBy,
			@DefaultValue("-1") @QueryParam("ascending") int ascending,
			@DefaultValue("0") @QueryParam("byColumn") int byColumn
			){
		try{
			List<User> users = storageManager.getUsers(orderBy, ascending, limit);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("data", users);
			data.put("count", users.size());
			return Response.ok(mapper.writeValueAsString(data)).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@GET
	@Path("/users/{userId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUser(@PathParam("userId") int userId){
		logger.log(Level.INFO, "Get user "+ userId);
		try{
			User user = storageManager.getUser(userId); 
		
			return Response.ok(mapper.writeValueAsString(user)).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@DELETE
	@Path("/users/{userId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deleteUser(@PathParam("userId") int userId){
		try{
			storageManager.deleteUser(userId);
			return Response.noContent().build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@PUT
	@Path("/users/{userId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateUser(@PathParam("userId") int userId, String data){
		logger.log(Level.INFO, "Update user "+ userId);
		try{
			User user = mapper.readValue(data, User.class);
			user.setId(userId);
			storageManager.updateUser(user);
			return Response.noContent().build();
		}catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@POST	
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/actions")
	public Response createAction(String data){
		try{
			logger.log(Level.INFO, "Create action " + data);
			Action action = mapper.readValue(data, Action.class);
			action = storageManager.createAction(action);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@GET
	@Path("/actions")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getActions(
			@QueryParam("query") String query,
			@DefaultValue("10") @QueryParam("limit") int limit,
			@DefaultValue("id") @QueryParam("orderBy") String orderBy,
			@DefaultValue("-1") @QueryParam("ascending") int ascending,
			@DefaultValue("0") @QueryParam("byColumn") int byColumn
			){
		try{
			List<Action> actions = storageManager.getActions(orderBy, ascending, limit);	
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("data", actions);
			data.put("count", actions.size());
			return Response.ok(mapper.writeValueAsString(data)).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@GET
	@Path("/actions/{actionId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAction(@PathParam("actionId") int actionId){
		logger.log(Level.INFO, "Get action " + actionId);
		try{
			Action action = storageManager.getAction(actionId);
			return Response.ok(mapper.writeValueAsString(action)).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@DELETE
	@Path("/actions/{actionId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deleteAction(@PathParam("actionId") int actionId){
		logger.log(Level.INFO, "Delete action " + actionId);
		try{
			storageManager.deleteAction(actionId);
			return Response.noContent().build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@PUT
	@Path("/actions/{actionId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateAction(@PathParam("actionId") int actionId, String data){
		logger.log(Level.INFO, "Update action "+ actionId);
		try{
			Action action = mapper.readValue(data, Action.class);
			action.setId(actionId);;
			storageManager.updateAction(action);
			return Response.noContent().build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	
	@POST	
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/resources")
	public Response createResource(String data){
		
			logger.log(Level.INFO, "Create resource " + data);
		try{
			Resource resource = mapper.readValue(data, Resource.class);
			storageManager.createResource(resource);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@GET
	@Path("/resources")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getResources(
			@QueryParam("query") String query,
			@DefaultValue("10") @QueryParam("limit") int limit,
			@DefaultValue("id") @QueryParam("orderBy") String orderBy,
			@DefaultValue("-1") @QueryParam("ascending") int ascending,
			@DefaultValue("0") @QueryParam("byColumn") int byColumn
			){
		try{
			List<Resource> resources = storageManager.getResources(orderBy, ascending, limit);	
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("data", resources);
			data.put("count", resources.size());
			return Response.ok(mapper.writeValueAsString(data)).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@GET
	@Path("/resources/{resourceId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getResource(@PathParam("resourceId") int resourceId){
		logger.log(Level.INFO, "Get resource " + resourceId);
		try{
			Resource resource = storageManager.getResource(resourceId);	
			return Response.ok(mapper.writeValueAsString(resource)).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@DELETE
	@Path("/resources/{resourceId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deleteResource(@PathParam("resourceId") int resourceId){
		logger.log(Level.INFO, "Delete resource " + resourceId);
		try{
			storageManager.deleteResource(resourceId);	
			return Response.noContent().build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@PUT
	@Path("/resources/{resourceId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateResource(@PathParam("resourceId") int resourceId, String data){
		logger.log(Level.INFO, "Update resource " + resourceId);
		try{
			Resource resource = mapper.readValue(data, Resource.class);
			resource.setId(resourceId);
			storageManager.updateResource(resource);	
			return Response.noContent().build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@POST	
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/roles")
	public Response createRole(String data){
		
			logger.log(Level.INFO, "Create role " + data);
		try{
			Role role = mapper.readValue(data, Role.class);
			storageManager.createRole(role);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@GET
	@Path("/roles")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getRoles(
			@QueryParam("query") String query,
			@DefaultValue("10") @QueryParam("limit") int limit,
			@DefaultValue("id") @QueryParam("orderBy") String orderBy,
			@DefaultValue("-1") @QueryParam("ascending") int ascending,
			@DefaultValue("0") @QueryParam("byColumn") int byColumn
			){
		try{
			List<Role> roles = storageManager.getRoles(orderBy, ascending, limit);	
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("data", roles);
			data.put("count", roles.size());
			return Response.ok(mapper.writeValueAsString(data)).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@GET
	@Path("/roles/{roleId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getRole(@PathParam("roleId") int roleId){
		logger.log(Level.INFO, "Get role " + roleId);
		try{
			Role role = storageManager.getRole(roleId);	
			return Response.ok(mapper.writeValueAsString(role)).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@DELETE
	@Path("/roles/{roleId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deleteRole(@PathParam("roleId") int roleId){
		logger.log(Level.INFO, "Delete role " + roleId);
		try{
			storageManager.deleteRole(roleId);	
			return Response.noContent().build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@PUT
	@Path("/roles/{roleId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateRole(@PathParam("roleId") int roleId, String data){
		logger.log(Level.INFO, "Update role " + roleId);
		try{
			Role role = mapper.readValue(data, Role.class);
			role.setId(roleId);
			storageManager.updateRole(role);	
			return Response.noContent().build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@POST	
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/rules")
	public Response createRule(String data){
		
			logger.log(Level.INFO, "Create rule " + data);
		try{
			Rule rule = mapper.readValue(data, Rule.class);
			storageManager.createRule(rule);
			updateXACMLPolicies();
			return Response.status(Status.NO_CONTENT).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@GET
	@Path("/rules")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getRules(
			@QueryParam("query") String query,
			@DefaultValue("10") @QueryParam("limit") int limit,
			@DefaultValue("id") @QueryParam("orderBy") String orderBy,
			@DefaultValue("-1") @QueryParam("ascending") int ascending,
			@DefaultValue("0") @QueryParam("byColumn") int byColumn
			){
		try{
			List<Rule> rules = storageManager.getRules(orderBy, ascending, limit);	
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("data", rules);
			data.put("count", rules.size());
			return Response.ok(mapper.writeValueAsString(data)).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@GET
	@Path("/rules/{ruleId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getRule(@PathParam("ruleId") int ruleId){
		logger.log(Level.INFO, "Get rule " + ruleId);
		try{
			Rule rule = storageManager.getRule(ruleId);	
			return Response.ok(mapper.writeValueAsString(rule)).build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@DELETE
	@Path("/rules/{ruleId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deleteRule(@PathParam("ruleId") int ruleId){
		logger.log(Level.INFO, "Delete rule " + ruleId);
		try{
			storageManager.deleteRule(ruleId);
			updateXACMLPolicies();
			return Response.noContent().build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	@PUT
	@Path("/rules/{ruleId}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateRule(@PathParam("ruleId") int ruleId, String data){
		logger.log(Level.INFO, "Update rule " + ruleId);
		try{
			Rule rule = mapper.readValue(data, Rule.class);
			rule.setId(ruleId);
			storageManager.updateRule(rule);
			updateXACMLPolicies();
			return Response.noContent().build();
		}
		catch(Exception ex){
			return sendException(ex);
		}
	}
	
	
	private Response sendException(Exception ex){
		logger.log(Level.WARNING, ex.getMessage(), ex);
		return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
	}
	
	private void updateXACMLPolicies() throws SQLException{
		logger.log(Level.INFO,  "Synchronize XACML Policies");
		XACMLGenerator generator = new XACMLGenerator(storageManager);
		generator.synchronize();
		logger.log(Level.INFO,  "Synchronization XACML Policies succedeed");
		
	}
	
}
