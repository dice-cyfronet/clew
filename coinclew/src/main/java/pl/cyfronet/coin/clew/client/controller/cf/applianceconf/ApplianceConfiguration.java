package pl.cyfronet.coin.clew.client.controller.cf.applianceconf;

import java.util.List;


public class ApplianceConfiguration extends NewApplianceConfiguration {
	private String id;
	private List<String> parameters;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getParameters() {
		return parameters;
	}
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
}