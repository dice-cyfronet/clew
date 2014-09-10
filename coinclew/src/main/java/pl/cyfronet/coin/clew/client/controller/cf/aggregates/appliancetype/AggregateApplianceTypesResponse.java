package pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliancetype;

import org.fusesource.restygwt.client.Json;

public class AggregateApplianceTypesResponse {
	@Json(name = "clew_appliance_types")
	private AggregateApplianceTypes applianceTypes;

	public AggregateApplianceTypes getApplianceTypes() {
		return applianceTypes;
	}

	public void setApplianceTypes(AggregateApplianceTypes applianceTypes) {
		this.applianceTypes = applianceTypes;
	}
}