package pl.cyfronet.coin.clew.client.controller.cf.applianceconf;

import org.fusesource.restygwt.client.Json;

public class ApplianceConfigurationRequestResponse {
	@Json(name = "appliance_configuration_template")
	private ApplianceConfiguration applianceConfiguration;

	public ApplianceConfiguration getApplianceConfiguration() {
		return applianceConfiguration;
	}

	public void setApplianceConfiguration(ApplianceConfiguration applianceConfiguration) {
		this.applianceConfiguration = applianceConfiguration;
	}
}