package pl.cyfronet.coin.clew.client.controller.cf.applianceset;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class ApplianceSetsResponse {
	@Json(name = "appliance_sets")
	private List<ApplianceSet> applianceSets;

	public List<ApplianceSet> getApplianceSets() {
		return applianceSets;
	}
	public void setApplianceSets(List<ApplianceSet> applianceSets) {
		this.applianceSets = applianceSets;
	}
}