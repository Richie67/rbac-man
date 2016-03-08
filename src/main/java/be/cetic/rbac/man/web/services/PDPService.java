package be.cetic.rbac.man.web.services;

import java.io.File;
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
import be.cetic.rbac.man.server.PAP;
import be.cetic.rbac.man.server.PDP;
import be.cetic.rbac.man.server.Util;

@Path("/")
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
			ResponseCtx responseCtx = pdp.evaluateRequest(Util.buildRequest(request.getSubject(), request.getAction(), request.getResource()));
			be.cetic.rbac.man.json.Response response = new be.cetic.rbac.man.json.Response();
			if(!responseCtx.getResults().isEmpty()){
				Result result = (Result)responseCtx.getResults().iterator().next();
				response.setDecision(result.getDecision());
				response.setMessage(result.getStatus().getMessage());
				response.setPermit(result.getDecision()==Result.DECISION_PERMIT);
				logger.log(Level.INFO, "Response of PDP {0} - {1} - {2} ", new Object[]{response.getDecision(), response.getMessage(), response.isPermit()});
			}
						
			return Response.ok(mapper.writeValueAsString(response)).build();
			
		}
		catch(Exception ex){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response ping(){
		return Response.ok("WELCOME TO RBAC MAN").build();
	}
}
