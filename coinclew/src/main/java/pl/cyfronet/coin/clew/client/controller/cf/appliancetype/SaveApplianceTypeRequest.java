package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;

import org.fusesource.restygwt.client.Json;

public class SaveApplianceTypeRequest {
	@Json(name = "appliance_type")
	private SaveApplianceType applianceType;

	public SaveApplianceType getApplianceType() {
		return applianceType;
	}

	public void setApplianceType(SaveApplianceType applianceType) {
		this.applianceType = applianceType;
	}
}