package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;

import org.fusesource.restygwt.client.Json;

public class ApplianceTypeSaveInPlace {
	@Json(name = "appliance_id")
	private String applianceInstanceId;

	public String getApplianceInstanceId() {
		return applianceInstanceId;
	}

	public void setApplianceInstanceId(String applianceInstanceId) {
		this.applianceInstanceId = applianceInstanceId;
	}
}