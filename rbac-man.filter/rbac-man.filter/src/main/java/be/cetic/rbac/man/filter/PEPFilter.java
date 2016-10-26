package be.cetic.rbac.man.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
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

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {	
		if((req instanceof HttpServletRequest)){
				HttpServletRequest request = (HttpServletRequest) req;
				HttpServletResponse response = (HttpServletResponse) res;
				RequestWrapper wrapper = new RequestWrapper(request);
				if(accessControl(wrapper, response))
					chain.doFilter(wrapper, response);
			     else {    	 			    	 
			    	 response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			    	 response.sendError(HttpServletResponse.SC_FORBIDDEN);			    	 
			     }

		}
		
	}
	
	private boolean accessControl(RequestWrapper request, HttpServletResponse response){
		logger.log(Level.INFO, request.getRemoteAddr());
		boolean isPermit = false; 
    	try{
    		ObjectMapper mapper = new ObjectMapper();    		
    		be.cetic.rbac.man.json.Request jsonRequest = buildRequest(request);
    		DefaultHttpClient httpClient = new DefaultHttpClient();
    		HttpPost postRequest = new HttpPost(pdpEndpoint);
    		logger.log(Level.INFO, mapper.writeValueAsString(jsonRequest));
    		StringEntity input = new StringEntity(mapper.writeValueAsString(jsonRequest));
    		input.setContentType("application/json");
    		postRequest.setEntity(input);
    		
    		logger.log(Level.INFO, "Send request to  "+ pdpEndpoint);
    		HttpResponse res = httpClient.execute(postRequest);

    		if (res.getStatusLine().getStatusCode() != 200) {
    			throw new RuntimeException("Failed : HTTP error code : "
    				+ res.getStatusLine().getStatusCode());
    		}

    		BufferedReader br = new BufferedReader(
                    new InputStreamReader((res.getEntity().getContent())));

			String line;
			StringBuffer resp = new StringBuffer();
			logger.log(Level.INFO, "Output from Server .... \n");
			while ((line = br.readLine()) != null) {
				resp.append(line);
			}
    		
    		httpClient.getConnectionManager().shutdown();
    		

    		be.cetic.rbac.man.json.Response r = mapper.readValue(resp.toString(),  be.cetic.rbac.man.json.Response.class);
    		isPermit = r.isPermit();    		    	
    	}
    	catch(Exception ex){
    		logger.log(Level.WARNING, ex.getMessage(), ex);
    	}
    	return isPermit;
	}
	

	
	private be.cetic.rbac.man.json.Request buildRequest(RequestWrapper request) throws IOException{
		be.cetic.rbac.man.json.Request jsonRequest = new be.cetic.rbac.man.json.Request();
		// Build the User
		User user = new User();
		// Retrieve the username
		try{			
			String content = IOUtils.toString(request.getInputStream());
	    	JSONObject input = new JSONObject(content);
			Object username = input.get("user");
			if(username != null)
				user.setUsername(username.toString());	
		}
		catch(IOException ex){
			logger.log(Level.WARNING,  ex.getMessage(),  ex);
		}
		// Build the action
		Action action = new Action();
		// Build the resource
		Resource resource = new Resource();
		
		String path = request.getRequestURL().toString();
		resource.setUrl(path);
		String method = request.getMethod().toUpperCase();
		action.setName(method);

		jsonRequest.setAction(action);
		jsonRequest.setSubject(user);
		jsonRequest.setResource(resource);
		
		return jsonRequest;
	}
	
	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		try{
    		ObjectMapper mapper = new ObjectMapper();
    		be.cetic.rbac.man.json.Request jsonRequest = new be.cetic.rbac.man.json.Request();
    		User subject = new User();
    		subject.setUsername("user_1");
    		Resource r = new Resource();
    		r.setUrl("http://localhost:8080");
    		jsonRequest.setResource(r);
    		Action action = new Action();
    		action.setName("read");
    		jsonRequest.setAction(action);
    		jsonRequest.setSubject(subject);
    		String pdpEndpoint = "http://localhost:8080/rbacman/ws/pdp/evaluate";
    		DefaultHttpClient httpClient = new DefaultHttpClient();
    		HttpPost postRequest = new HttpPost(pdpEndpoint);
    		StringEntity input = new StringEntity(mapper.writeValueAsString(jsonRequest));
    		input.setContentType("application/json");
    		postRequest.setEntity(input);
    		
    		
    		logger.log(Level.INFO, "Send request to  "+ pdpEndpoint);
    		HttpResponse res = httpClient.execute(postRequest);

    		if (res.getStatusLine().getStatusCode() != 200) {
    			
    			throw new RuntimeException("Failed : HTTP error code : "
    				+ res.getStatusLine().getStatusCode());
    		}

    		BufferedReader br = new BufferedReader(
                    new InputStreamReader((res.getEntity().getContent())));

			String line;
			StringBuffer resp = new StringBuffer();
			logger.log(Level.INFO, "Output from Server .... \n");
			while ((line = br.readLine()) != null) {
				resp.append(line);
			}
    		
    		httpClient.getConnectionManager().shutdown();
    		

    		be.cetic.rbac.man.json.Response r2 = mapper.readValue(resp.toString(),  be.cetic.rbac.man.json.Response.class);
    		System.out.println(mapper.writeValueAsString(r2));   		    	
    	}
    	catch(Exception ex){
    		logger.log(Level.WARNING, ex.getMessage(), ex);
    	}
	}

}
