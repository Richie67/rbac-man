package be.cetic.rbac.man.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONObject;

import be.cetic.rbac.man.json.Action;
import be.cetic.rbac.man.json.Resource;
import be.cetic.rbac.man.json.User;
import be.cetic.rbac.man.wrapper.RequestWrapper;

public class PEPFilter implements Filter{
	private String pdpEndpoint;
	public static final String PDP_ENDPOINT = "PDP_ENDPOINT";
	public static final Logger logger = Logger.getLogger(PEPFilter.class.getName());
	
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		pdpEndpoint = filterConfig.getInitParameter(PDP_ENDPOINT);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {	
		if((request instanceof HttpServletRequest)){
				accessControl((HttpServletRequest)request, (HttpServletResponse)response);
		}
		
	}
	
	private boolean accessControl(HttpServletRequest request, HttpServletResponse response){
		logger.log(Level.INFO, request.getRemoteAddr());
		boolean isPermit = true; //TODO change this default value
		ClientConfig config = new ClientConfig();
	    Client client = ClientBuilder.newClient(config);
	    ObjectMapper mapper = new ObjectMapper();
        WebTarget target;	        
	    target = client.target(pdpEndpoint);
	    Invocation.Builder invocationBuilder =target.request();
    	try{
    		be.cetic.rbac.man.json.Request jsonRequest = buildRequest(request);

    		Response r = invocationBuilder	    		
    				.post(Entity.entity(mapper.writeValueAsString(jsonRequest), MediaType.APPLICATION_JSON));
    		
    	}
    	catch(Exception ex){
    		logger.log(Level.WARN, ex.getMessage(), ex);
    	}
    	return isPermit;
	}
	
	/*private boolean accessControl(ServletRequest request, ServletResponse response){
		logger.log(Level.INFO, request.getRemoteAddr());
		boolean isPermit = true;
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		ObjectMapper mapper = new ObjectMapper();
		WebTarget target = client.target(pdpEndpoint);
		Invocation.Builder invocationbuilder = target.request();
		try{
			RequestWrapper wrapper = new RequestWrapper(request);
			Response r = invocationbuilder.post(Entity.entity(mapper.writeValueAsString(wrapper.getRequest()), MediaType.APPLICATION_JSON));
			logger.log(Level.INFO, r);
		}
		catch(Exception ex){
			logger.log(Level.WARN,  ex.getMessage(), ex);
		}
		return isPermit;
	}*/
	
	
	
	private be.cetic.rbac.man.json.Request buildRequest(HttpServletRequest request) throws IOException{
		be.cetic.rbac.man.json.Request jsonRequest = new be.cetic.rbac.man.json.Request();
		// Build the User
		User user = new User();
		// Build the action
		Action action = new Action();
		// Build the resource
		Resource resource = new Resource();
		
		String path = request.getPathInfo();
		resource.setUrl(path);
		String method = request.getMethod().toUpperCase();
		action.setName(method);
		// PUT /validate_user
	    if(method.equals("PUT") && path.equals("/fednet/eastBr/user/validate_user")){	    	
	    	RequestWrapper wrapper = new RequestWrapper(request);
	    	String content = wrapper.getData();
	    	JSONObject input=new JSONObject(content);
            //LOGGER.error("INPUT: "+input.toString());
            String username=((String)input.get("username")).split("@@")[1];
            String tenant=((String)input.get("username")).split("@@")[0];            
            String cmp_endpoint=input.getString("cmp_endpoint");
            user.setUsername(username);
            resource.setDescription(cmp_endpoint);
            resource.setName(tenant);            
	    }
	    else if(method.equals("PUT") && path.equals("/fednet/eastBr/network")){
	    	RequestWrapper wrapper = new RequestWrapper(request);
	    	String content = wrapper.getData();
	    	JSONObject input = new JSONObject(content);
            String OSF_token = (String) input.get("token");
            String OSF_network_segment_id = (String) input.get("network_segment_id");
            String OSF_cmp_endpoint = (String) input.get("cmp_endpoint");
            resource.setDescription(OSF_cmp_endpoint);
            resource.setName(OSF_network_segment_id);
            user.setUsername(OSF_token);
	    }
	    else if(method.equals("PUT") && path.equals("/fednet/eastBr/FA_Management")){
	    	RequestWrapper wrapper = new RequestWrapper(request);
	    	String content = wrapper.getData();
	    	JSONObject input = new JSONObject(content);
            user.setUsername((String) input.get("token"));//utilizzerò questo elemento per identificare fedten
            resource.setDescription((String) input.get("Command"));
            resource.setName((String)input.get("type"));
	    }
	
		jsonRequest.setAction(action);
		jsonRequest.setSubject(user);
		jsonRequest.setResource(resource);
		
		return jsonRequest;
	}
	
	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
