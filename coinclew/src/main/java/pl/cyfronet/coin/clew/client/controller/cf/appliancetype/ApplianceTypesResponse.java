package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class ApplianceTypesResponse {
	@Json(name = "appliance_types")
	private List<ApplianceType> applianceTypes;

	public List<ApplianceType> getApplianceTypes() {
		return applianceTypes;
	}
	public void setApplianceTypes(List<ApplianceType> applianceTypes) {
		this.applianceTypes = applianceTypes;
	}
	
	@Override
	public String toString() {
		return "ApplianceTypesResponse [applianceTypes=" + applianceTypes + "]";
	}
}