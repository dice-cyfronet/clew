package pl.cyfronet.coin.clew.client.controller.cf.applianceinstance;

import org.fusesource.restygwt.client.Json;

public class ApplianceInstance extends NewApplianceInstance {
	public enum Status {
		booting,
		running
	}
	
	private String id;
	@Json(name = "appliance_type_id")
	private String applianceTypeId;
	@Json(name = "appliance_configuration_instance_id")
	private String configurationInstanceId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApplianceTypeId() {
		return applianceTypeId;
	}
	public void setApplianceTypeId(String applianceTypeId) {
		this.applianceTypeId = applianceTypeId;
	}
	public String getConfigurationInstanceId() {
		return configurationInstanceId;
	}
	public void setConfigurationInstanceId(String configurationInstanceId) {
		this.configurationInstanceId = configurationInstanceId;
	}
}