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

import be.cetic.rbac.man.json.Action;
import be.cetic.rbac.man.json.Resource;
import be.cetic.rbac.man.json.User;

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
    	be.cetic.rbac.man.json.Request jsonRequest = buildRequest(request);
    	try{
    		Response r = invocationBuilder	    		
    				.post(Entity.entity(mapper.writeValueAsString(jsonRequest), MediaType.APPLICATION_JSON));
    	}
    	catch(Exception ex){
    		logger.log(Level.WARN, ex.getMessage(), ex);
    	}
    	return isPermit;
    
	    
		
	}
	
	
	private be.cetic.rbac.man.json.Request buildRequest(HttpServletRequest request){
		be.cetic.rbac.man.json.Request jsonRequest = new be.cetic.rbac.man.json.Request();
		// Build the User
		User user = new User();
		
		String username = "";
		user.setUsername(username);
		// Build the action
		Action action = new Action();
		
		// Build the resource
		Resource resource = new Resource();
		
		jsonRequest.setAction(action);
		jsonRequest.setSubject(user);
		jsonRequest.setResource(resource);
		
		return jsonRequest;
	}
	
	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
