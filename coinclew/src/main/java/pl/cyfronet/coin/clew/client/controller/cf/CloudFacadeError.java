package pl.cyfronet.coin.clew.client.controller.cf;

import java.util.List;
import java.util.Map;

public class CloudFacadeError {
	private String type;
	
	private String message;
	
	private Map<String, List<String>> details;
	
	private String requestId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, List<String>> getDetails() {
		return details;
	}

	public void setDetails(Map<String, List<String>> details) {
		this.details = details;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}