package pl.cyfronet.coin.clew.client.controller.cf.applianceinstance;

import org.fusesource.restygwt.client.Json;

public class ApplianceInstanceRequestResponse {
	@Json(name = "appliance")
	private ApplianceInstance applianceInstance;

	public ApplianceInstance getApplianceInstance() {
		return applianceInstance;
	}

	public void setApplianceInstance(ApplianceInstance applianceInstance) {
		this.applianceInstance = applianceInstance;
	}
}