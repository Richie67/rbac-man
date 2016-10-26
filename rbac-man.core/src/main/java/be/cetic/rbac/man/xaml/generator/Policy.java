package be.cetic.rbac.man.xaml.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.sun.xacml.Indenter;
import com.sun.xacml.PolicyMetaData;
import com.sun.xacml.Rule;
import com.sun.xacml.Target;
import com.sun.xacml.TargetMatch;
import com.sun.xacml.TargetMatchGroup;
import com.sun.xacml.TargetSection;
import com.sun.xacml.attr.AttributeDesignator;
import com.sun.xacml.attr.AttributeFactory;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.combine.PermitOverridesRuleAlg;
import com.sun.xacml.combine.RuleCombiningAlgorithm;
import com.sun.xacml.cond.Condition;
import com.sun.xacml.cond.Evaluatable;
import com.sun.xacml.cond.Function;
import com.sun.xacml.cond.FunctionFactory;
import com.sun.xacml.ctx.Result;

public class Policy extends XacmlRequestEntity{
	public static Logger logger = Logger.getLogger(Policy.class.getName());
	
	private Subject subject;
	private ArrayList<String> permitActions;
	private ArrayList<String> denyActions;
	
	public Policy(Subject subject){
		this.subject = subject;
		permitActions = new ArrayList<String>();
		denyActions = new ArrayList<String>();
		
	}
	
	public void addPermit(String action){
		permitActions.add(action);
	}
	
	public void addDeny(String action){
		denyActions.add(action);
	}
	
	public void generate(File policyFile){
		generate(policyFile, null);
	}
	
	public void generate(File policyFile, List<String> resources){
		try{
			
			Target target;
			String role = subject.getRoleName().toUpperCase();
			role = role.replaceAll(" ", "_");
			URI idPolicy = new URI("POLICIES_FOR_ROLE_" + role);
			RuleCombiningAlgorithm combiningAlg = new PermitOverridesRuleAlg();
		
						
			URI id;
			List<Rule> rules = new ArrayList<Rule>();
		
			for(int i=0; i < permitActions.size(); i++){
				String action = permitActions.get(i);
				int effect = Result.DECISION_PERMIT;

				String description = "Rule_" + action;
				 
				id = new URI(description.replaceAll(" ", "_"));
				Condition condition = null;
				target = getRuleTarget(action);
				Rule rule = new Rule(id, effect, description, target, condition);
				rules.add(rule);
			}
			
			for(int i=0; i < denyActions.size(); i++){
				String action = denyActions.get(i);
				int effect = Result.DECISION_DENY;

				String description = "Rule_" + action;
				 
				id = new URI(description.replaceAll(" ", "_"));
				Condition condition = null;
				target = getRuleTarget(action);
				Rule rule = new Rule(id, effect, description, target, condition);
				rules.add(rule);
			}
			target = getInitialTarget(resources);

			com.sun.xacml.Policy policy = new com.sun.xacml.Policy(idPolicy, combiningAlg,target, rules);
			
			FileOutputStream out = new FileOutputStream(policyFile);
			logger.info("Write policy for role " + subject.getRoleName() + " into " + policyFile.getAbsolutePath());
			policy.encode(out, new Indenter(5));
		}
		catch(Exception e){
			logger.warning(e.getMessage());
		}

	}
	
	private Target getInitialTarget(List<String> resources){
		try{
			FunctionFactory factory = FunctionFactory.getTargetInstance();
			Function  function;
			URI id;
			Evaluatable eval;
			AttributeValue attrValue;
			List<TargetMatchGroup> resourcesMatchGroup = new ArrayList<TargetMatchGroup>();
			
			if(resources != null){
				for(int i=0; i < resources.size(); i++){
					String resource = resources.get(i);
					function = factory.createFunction("urn:oasis:names:tc:xacml:1.0:function:anyURI-equal");
					eval = new AttributeDesignator(AttributeDesignator.RESOURCE_TARGET, getAnyURI(), new URI(Resource.RESOURCE_ID), true);
					id = new URI(Resource.RESOURCE_ID);
			
					attrValue = AttributeFactory.createAttribute(getAnyURI(), resource);
					TargetMatch resourceMatch = new TargetMatch(TargetMatch.RESOURCE, function, eval, attrValue);
					ArrayList<TargetMatch> resourcesMatches = new ArrayList<TargetMatch>();
					resourcesMatches.add(resourceMatch);
			
					TargetMatchGroup resourceMatchGroup = new TargetMatchGroup(resourcesMatches, TargetMatch.RESOURCE);
				
					resourcesMatchGroup.add(resourceMatchGroup);
				}
			}
			
			function = factory.createFunction("urn:oasis:names:tc:xacml:1.0:function:string-equal");
			id = new URI(Subject.SUBJECT_ROLE);
			eval = new AttributeDesignator(AttributeDesignator.SUBJECT_TARGET, getString(), id, true);
			
			attrValue = subject.getSubjectRole().getValue();
			TargetMatch subjectMatch = new TargetMatch(TargetMatch.SUBJECT, function, eval, attrValue);
			
			ArrayList<TargetMatch> subjectsMatches = new ArrayList<TargetMatch>();
			subjectsMatches.add(subjectMatch);
			TargetMatchGroup subjectMatchGroup = new TargetMatchGroup(subjectsMatches, TargetMatch.SUBJECT);
			List<TargetMatchGroup> subjectsMatchGroup = new ArrayList<TargetMatchGroup>();
			subjectsMatchGroup.add(subjectMatchGroup);
			
			//ArrayList<TargetMatch> actionsMatches = new ArrayList<TargetMatch>();
			//TargetMatchGroup actionMatchGroup = new TargetMatchGroup(actionsMatches, TargetMatch.ACTION);
			List<TargetMatchGroup> actionsMatchGroup = new ArrayList<TargetMatchGroup>();
			//actionsMatchGroup.add(actionMatchGroup);
			
			
			if(subjectsMatchGroup.size()==0)
				subjectsMatchGroup = null;
			if(resourcesMatchGroup.size()==0)
				resourcesMatchGroup = null;
			if(actionsMatchGroup.size()==0)
				actionsMatchGroup = null;
			
			
			TargetSection subjectsSection = new TargetSection(subjectsMatchGroup, TargetMatch.SUBJECT, PolicyMetaData.XACML_VERSION_2_0);
			TargetSection resourcesSection = new TargetSection(resourcesMatchGroup, TargetMatch.RESOURCE, PolicyMetaData.XACML_VERSION_2_0);;
			TargetSection actionsSection = new TargetSection(actionsMatchGroup, TargetMatch.ACTION, PolicyMetaData.XACML_VERSION_2_0);;
			
			Target target = new Target(subjectsSection, resourcesSection, actionsSection);
			return target;
			
		}
		catch(Exception e){
			logger.severe(e.getMessage());
			return null;
		}
	}
	
	
	private Target getRuleTarget(String action){
		try{

			//ArrayList<TargetMatch> resourcesMatches = new ArrayList<TargetMatch>();			
			//TargetMatchGroup resourceMatchGroup = new TargetMatchGroup(resourcesMatches, TargetMatch.RESOURCE);
			List<TargetMatchGroup> resourcesMatchGroup = new ArrayList<TargetMatchGroup>();
			//resourcesMatchGroup.add(resourceMatchGroup);
			
			
			FunctionFactory factory = FunctionFactory.getTargetInstance();
			/*Function function = factory.createFunction("urn:oasis:names:tc:xacml:1.0:function:string-equal");
			URI id = new URI(be.cetic.ponte.pdp.Subject.SUBJECT_ID);
			Evaluatable eval = new AttributeDesignator(AttributeDesignator.SUBJECT_TARGET, getString(), id, true);
			
			AttributeValue attrValue = AttributeFactory.createAttribute(getString(), "<!-- You must entry the user id -->");
			TargetMatch subjectMatch = new TargetMatch(TargetMatch.SUBJECT, function, eval, attrValue);
			*/
			//ArrayList<TargetMatch> subjectsMatches = new ArrayList<TargetMatch>();
			//subjectsMatches.add(subjectMatch);
			
			//TargetMatchGroup subjectMatchGroup = new TargetMatchGroup(subjectsMatches, TargetMatch.SUBJECT);
			List<TargetMatchGroup> subjectsMatchGroup = new ArrayList<TargetMatchGroup>();
			//subjectsMatchGroup.add(subjectMatchGroup);
			
			
			
			Function function = factory.createFunction("urn:oasis:names:tc:xacml:1.0:function:string-equal");
			URI id = new URI(Action.ACTION_NAME);
			Evaluatable eval = new AttributeDesignator(AttributeDesignator.ACTION_TARGET, getString(), id, true);
			
			AttributeValue attrValue = AttributeFactory.createAttribute(getString(), action);
			TargetMatch actionMatch = new TargetMatch(TargetMatch.ACTION, function, eval, attrValue);
					
			ArrayList<TargetMatch> actionsMatches = new ArrayList<TargetMatch>();
			actionsMatches.add(actionMatch);
			TargetMatchGroup actionMatchGroup = new TargetMatchGroup(actionsMatches, TargetMatch.ACTION);
			List<TargetMatchGroup> actionsMatchGroup = new ArrayList<TargetMatchGroup>();
			actionsMatchGroup.add(actionMatchGroup);
			
			
			if(subjectsMatchGroup.size()==0)
				subjectsMatchGroup = null;
			if(resourcesMatchGroup.size()==0)
				resourcesMatchGroup = null;
			if(actionsMatchGroup.size()==0)
				actionsMatchGroup = null;
			
			TargetSection subjectsSection = new TargetSection(subjectsMatchGroup, TargetMatch.SUBJECT, PolicyMetaData.XACML_VERSION_2_0);
			TargetSection resourcesSection = new TargetSection(resourcesMatchGroup, TargetMatch.RESOURCE, PolicyMetaData.XACML_VERSION_2_0);;
			TargetSection actionsSection = new TargetSection(actionsMatchGroup, TargetMatch.ACTION, PolicyMetaData.XACML_VERSION_2_0);;
			
			
			
			Target target = new Target(subjectsSection, resourcesSection, actionsSection);
			return target;
			
		}
		catch(Exception e){
			logger.severe(e.getMessage());
			return null;
		}
	}

}
