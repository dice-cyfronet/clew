package pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class DevelopmentModePropertySetsResponse {
	@Json(name = "dev_mode_property_sets")
	private List<DevelopmentModePropertySet> developmentModePropertySets;

	public List<DevelopmentModePropertySet> getDevelopmentModePropertySets() {
		return developmentModePropertySets;
	}

	public void setDevelopmentModePropertySets(
			List<DevelopmentModePropertySet> developmentModePropertySets) {
		this.developmentModePropertySets = developmentModePropertySets;
	}
}