package pl.cyfronet.coin.clew.client.controller.cf.applianceconf;

import org.fusesource.restygwt.client.Json;

public class NewApplianceConfiguration {
	private String name;

	private String payload;

	@Json(name = "appliance_type_id")
	private String applianceTypeId;

	public String getApplianceTypeId() {
		return applianceTypeId;
	}

	public void setApplianceTypeId(String applianceTypeId) {
		this.applianceTypeId = applianceTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
}
