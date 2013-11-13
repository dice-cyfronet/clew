package pl.cyfronet.coin.clew.client.controller.cf.appliancevm;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class ApplianceVmsResponse {
	@Json(name = "virtual_machines")
	private List<ApplianceVm> applianceVms;

	public List<ApplianceVm> getApplianceVms() {
		return applianceVms;
	}

	public void setApplianceVms(List<ApplianceVm> applianceVms) {
		this.applianceVms = applianceVms;
	}
}