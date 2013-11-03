package pl.cyfronet.coin.clew.client.controller.cf.applianceinstance;

import org.fusesource.restygwt.client.Json;

public class NewApplianceInstanceRequest {
	@Json(name = "appliance")
	private NewApplianceInstance applianceInstance;

	public NewApplianceInstance getApplianceInstance() {
		return applianceInstance;
	}

	public void setApplianceInstance(NewApplianceInstance applianceInstance) {
		this.applianceInstance = applianceInstance;
	}
}