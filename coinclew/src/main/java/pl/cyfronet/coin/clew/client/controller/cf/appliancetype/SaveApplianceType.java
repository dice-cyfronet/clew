package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;

import org.fusesource.restygwt.client.Json;

public class SaveApplianceType extends NewApplianceType {
	@Json(name = "appliance_id")
	private String applianceId;

	public String getApplianceId() {
		return applianceId;
	}

	public void setApplianceId(String applianceId) {
		this.applianceId = applianceId;
	}
}