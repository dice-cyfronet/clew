package pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliance;

import org.fusesource.restygwt.client.Json;

public class AggregateAppliancesResponse {
	@Json(name = "clew_appliance_instances")
	private AggregateAppliances appliances;

	public AggregateAppliances getAppliances() {
		return appliances;
	}

	public void setAppliances(AggregateAppliances appliances) {
		this.appliances = appliances;
	}
}