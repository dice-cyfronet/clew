package pl.cyfronet.coin.clew.client.controller.cf.applianceset;

import org.fusesource.restygwt.client.Json;

public class ApplianceSetRequestResponse {
	@Json(name = "appliance_set")
	private ApplianceSet applianceSet;

	public ApplianceSet getApplianceSet() {
		return applianceSet;
	}

	public void setApplianceSet(ApplianceSet applianceSet) {
		this.applianceSet = applianceSet;
	}
}