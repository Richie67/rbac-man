package be.cetic.rbac.man.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.sun.xacml.ConfigurationStore;
import com.sun.xacml.PDPConfig;
import com.sun.xacml.ParsingException;
import com.sun.xacml.UnknownIdentifierException;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.finder.AttributeFinder;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.impl.CurrentEnvModule;
import com.sun.xacml.support.finder.FilePolicyModule;

public class PDP {
	private static final Logger logger = Logger.getLogger(PDP.class.getName());
	private com.sun.xacml.PDP pdp = null;
	private PAP pap = null;
	
	public PDP(File configFile){
		try{
			System.setProperty("com.sun.xacml.PDPConfigFile", configFile.getAbsolutePath());
			ConfigurationStore store = new ConfigurationStore();
			store.useDefaultFactories();
			PDPConfig config = store.getDefaultPDPConfig();			
			pdp = new com.sun.xacml.PDP(config);
		}
		catch(ParsingException e){
			logger.warning(e.getMessage());
		}
		catch(UnknownIdentifierException e){
			logger.warning(e.getMessage());
		}
	}
	
	public PDP(PAP pap){
		this.pap = pap;
		init();
	}
	
	private void init(){
		FilePolicyModule policyModule = new FilePolicyModule();
		ArrayList<String> policies = pap.getPolicies();
		for(int i=0; i < policies.size(); i++)
			policyModule.addPolicy(policies.get(i));
		
		PolicyFinder policyFinder = new PolicyFinder();
		Set policyModules = new HashSet();
		policyModules.add(policyModule);
		policyFinder.setModules(policyModules);
		
		CurrentEnvModule envModule = new CurrentEnvModule();
		AttributeFinder attributeFinder = new AttributeFinder();
		List attrModules = new ArrayList();
		attrModules.add(envModule);
		attributeFinder.setModules(attrModules);
		
		pdp = new com.sun.xacml.PDP(new PDPConfig(attributeFinder, policyFinder, null));	
	}
	
	public ResponseCtx evaluateRequest(RequestCtx request){
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		request.encode(output);
		byte[] buffer = output.toByteArray();
		logger.info(new String (buffer));
		return pdp.evaluate(request);
	}
}
