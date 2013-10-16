package pl.cyfronet.coin.clew.client.controller.cf.applianceset;

import org.fusesource.restygwt.client.Json;

public class NewApplianceSet {
	public enum Type {
		portal,
		workflow,
		development
	}
	
	@Json(name = "appliance_set_type")
	private Type type;
	private String priority;
	private String name;
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
}