package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;

import org.fusesource.restygwt.client.Json;

public class NewApplianceTypeRequest {
	@Json(name = "appliance_type")
	private NewApplianceType applianceType;

	public NewApplianceType getApplianceType() {
		return applianceType;
	}

	public void setApplianceType(NewApplianceType applianceType) {
		this.applianceType = applianceType;
	}
}