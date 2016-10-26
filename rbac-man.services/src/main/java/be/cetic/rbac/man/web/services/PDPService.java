package be.cetic.rbac.man.web.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;

import be.cetic.rbac.man.json.Request;
import be.cetic.rbac.man.json.User;
import be.cetic.rbac.man.pip.SqlitePIP;
import be.cetic.rbac.man.server.PAP;
import be.cetic.rbac.man.server.PDP;
import be.cetic.rbac.man.server.PIP;
import be.cetic.rbac.man.server.Util;

@Path("/pdp")
public class PDPService {

	private ObjectMapper mapper = new ObjectMapper();
	private static Logger logger = Logger.getLogger(PDPService.class.getName());
	
	@Context 
	private ServletContext context;
	
	@POST	
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/evaluate")
	public Response evaluateRequest(String data){
		try{
			logger.log(Level.INFO, "Evaluate request {0}", data);
			Request request = mapper.readValue(data,  Request.class);
			String value = context.getInitParameter("policies_directory");
			File policiesDirectory  = Util.getPoliciesDirectory();
			if(value != null && new File(value).exists())
				policiesDirectory = new File(value);		
			 
			PAP pap = new PAP(policiesDirectory);
			PDP pdp = new PDP(pap);
			PIP pip = new SqlitePIP();
			User user = pip.getUser(request.getSubject().getUsername());
			if(user!= null)
				request.getSubject().setRole(user.getRole());
			ResponseCtx responseCtx = pdp.evaluateRequest(Util.buildRequest(request.getSubject(), request.getAction(), request.getResource()));
			be.cetic.rbac.man.json.Response response = new be.cetic.rbac.man.json.Response();
			response.setPermit(true);
			if(!responseCtx.getResults().isEmpty()){
				Result result = (Result)responseCtx.getResults().iterator().next();
				response.setDecision(result.getDecision());
				response.setMessage(result.getStatus().getMessage());				
				response.setPermit(result.getDecision()!=Result.DECISION_DENY);				
				logger.log(Level.INFO, "Response of PDP {0} - {1} - {2} ", new Object[]{response.getDecision(), response.getMessage(), response.isPermit()});
			}
						
			return Response.ok(mapper.writeValueAsString(response)).build();
			
		}
		catch(Exception ex){
			logger.log(Level.WARNING,  ex.getMessage(),  ex);
			return Response.serverError().build();
		}
	}
	
	@Path("/logs")
	@GET
	@Produces({MediaType.TEXT_PLAIN})
	public Response getLogs(){
		try{
			// Read the logs file
			File tomcatDirectory = new File(System.getProperty("catalina.base"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			File logFile = new File(tomcatDirectory, "logs/rbacman"+ format.format(new Date()) + ".log");
			BufferedReader reader = new BufferedReader(new FileReader(logFile));
			String line;
			StringBuffer buffer = new StringBuffer();		
			while((line=reader.readLine())!= null){
				buffer.append(line);
			}
			reader.close();
			return Response.ok().entity(buffer.toString()).build();
		}
		catch(Exception ex){
			logger.log(Level.WARNING,  ex.getMessage(),  ex);
			return Response.serverError().build();
		}
	}
	
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response ping(){
		return Response.ok("WELCOME TO RBAC MAN").build();
	}
}
