package pl.cyfronet.coin.clew.client.controller.cf.applianceset;

import org.fusesource.restygwt.client.Json;

public class NewApplianceSetRequest {
	@Json(name = "appliance_set")
	private NewApplianceSet applianceSet;

	public NewApplianceSet getApplianceSet() {
		return applianceSet;
	}
	public void setApplianceSet(NewApplianceSet applianceSet) {
		this.applianceSet = applianceSet;
	}
}