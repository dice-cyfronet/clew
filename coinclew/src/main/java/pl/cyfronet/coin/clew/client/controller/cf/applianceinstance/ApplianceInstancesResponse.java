package pl.cyfronet.coin.clew.client.controller.cf.applianceinstance;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class ApplianceInstancesResponse {
	@Json(name = "appliances")
	private List<ApplianceInstance> applianceInstances;

	public List<ApplianceInstance> getApplianceInstances() {
		return applianceInstances;
	}

	public void setApplianceInstances(List<ApplianceInstance> applianceInstances) {
		this.applianceInstances = applianceInstances;
	}
}