package pl.cyfronet.coin.clew.client.controller.cf.applianceconf;

import org.fusesource.restygwt.client.Json;

public class NewApplianceConfigurationRequest {
	@Json(name = "appliance_configuration_template")
	private NewApplianceConfiguration applianceConfiguration;

	public NewApplianceConfiguration getApplianceConfiguration() {
		return applianceConfiguration;
	}

	public void setApplianceConfiguration(NewApplianceConfiguration applianceConfiguration) {
		this.applianceConfiguration = applianceConfiguration;
	}
}