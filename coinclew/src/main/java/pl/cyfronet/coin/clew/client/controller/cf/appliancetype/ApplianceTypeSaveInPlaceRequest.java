package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;

import org.fusesource.restygwt.client.Json;

public class ApplianceTypeSaveInPlaceRequest {
	@Json(name = "appliance_type")
	private ApplianceTypeSaveInPlace applianceType;

	public ApplianceTypeSaveInPlace getApplianceType() {
		return applianceType;
	}

	public void setApplianceType(ApplianceTypeSaveInPlace applianceType) {
		this.applianceType = applianceType;
	}
}