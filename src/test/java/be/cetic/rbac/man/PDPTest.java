package be.cetic.rbac.man;

import java.io.File;

import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;

import be.cetic.rbac.man.json.Action;
import be.cetic.rbac.man.json.Resource;
import be.cetic.rbac.man.json.User;
import be.cetic.rbac.man.server.PAP;
import be.cetic.rbac.man.server.PDP;
import be.cetic.rbac.man.server.Util;
import junit.framework.TestCase;

public class PDPTest extends TestCase{

	private PDP pdp;
	private PAP pap;
	private File policiesDirectory = new File("./src/test/resources/_policies");
	
	protected void setUp(){
		pap = new PAP(policiesDirectory);
		pdp = new PDP(pap);
	}
	
	public void testPolicy() throws Exception{
		
		User subject = new User();
		subject.setUsername("rbacman");
		subject.setRole("tenant");		
		
		Resource resource = new Resource();
		resource.setUrl("http://localhost:8080/rbacman/test");
		
		Action action = new Action();
		action.setName("read");
		
		RequestCtx request = Util.buildRequest(subject, action, resource);
		ResponseCtx response = pdp.evaluateRequest(request);
		response.encode(System.out);
		
	}
}
