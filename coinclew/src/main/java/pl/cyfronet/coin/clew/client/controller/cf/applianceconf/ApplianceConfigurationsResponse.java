package pl.cyfronet.coin.clew.client.controller.cf.applianceconf;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class ApplianceConfigurationsResponse {
	@Json(name = "appliance_configuration_templates")
	private List<ApplianceConfiguration> applianceConfigurations;

	public List<ApplianceConfiguration> getApplianceConfigurations() {
		return applianceConfigurations;
	}

	public void setApplianceConfigurations(
			List<ApplianceConfiguration> applianceConfigurations) {
		this.applianceConfigurations = applianceConfigurations;
	}
}