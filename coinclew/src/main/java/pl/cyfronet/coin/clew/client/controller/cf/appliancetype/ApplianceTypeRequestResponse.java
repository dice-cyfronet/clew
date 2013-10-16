package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;

import org.fusesource.restygwt.client.Json;

public class ApplianceTypeRequestResponse {
	@Json(name = "appliance_type")
	private ApplianceType applianceType;

	public ApplianceType getApplianceType() {
		return applianceType;
	}

	public void setApplianceType(ApplianceType applianceType) {
		this.applianceType = applianceType;
	}
}