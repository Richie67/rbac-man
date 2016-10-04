package be.cetic.rbac.man.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="response")
public class Response {
	
	private int decision;
	private String message;
	private boolean isPermit;
	

	@JsonProperty(value="decision")
	public int getDecision() {
		return decision;
	}

	public void setDecision(int decision) {
		this.decision = decision;
	}

	@JsonProperty(value="message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty(value="ispermit")
	public boolean isPermit() {
		return isPermit;
	}

	public void setPermit(boolean isPermit) {
		this.isPermit = isPermit;
	}
	
	
	
	
	
}
