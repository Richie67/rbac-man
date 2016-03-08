package be.cetic.rbac.man.server;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.sun.xacml.ParsingException;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.support.finder.PolicyReader;

public class PAP {
	public static final Logger logger = Logger.getLogger(PAP.class.getName());
	
	private File policiesDirectory;
	private FilenameFilter xacmlFilter;
	
	public PAP(File policiesDirectory){
		xacmlFilter = new FilenameFilter() {
			
			public boolean accept(File dir, String name) {
				String extension = name.substring(name.lastIndexOf(".")+1).toLowerCase();
				if(extension.equals("xml") || extension.equals("xacml")){
					PolicyReader reader = new PolicyReader(new PolicyFinder(), logger);
					try{						
						reader.readPolicy(new File(dir, name));
						return true;
					}
					catch(ParsingException e){
						return false;
					}
					catch(IllegalArgumentException e){
						logger.warning(e.getMessage());
					}
				}
				return false;
			}
		};
			this.policiesDirectory = policiesDirectory;
			if(!policiesDirectory.exists()){
				logger.info("Making policies directory [" + policiesDirectory.getAbsolutePath() + "]");
				policiesDirectory.mkdirs();
			}
	}
	
	public ArrayList<String> getPolicies(){
		return getPolicies(policiesDirectory);
	}
	
	private ArrayList<String> getPolicies(File directory){
		ArrayList<String> policies = new ArrayList<String>();
		if(directory.exists()){
			logger.info("Loading policies into " + directory.getAbsolutePath());
			String[] filenames = directory.list(xacmlFilter);
		
			for(String filename: filenames){
				File file = new File(directory, filename);
				if(file.isFile()){
					logger.info("Load XACML policy " + file.getAbsolutePath());
					policies.add(file.getAbsolutePath());
				}
				else{
					policies.addAll(getPolicies(file));
				}
			}
		}
		logger.info(policies.size() + " policies loaded");
		return policies;
	}
}
