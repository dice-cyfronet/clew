package pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliancetype;

import java.util.List;

import org.fusesource.restygwt.client.Json;

import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSite;

public class AggregateApplianceTypes {
	@Json(name = "appliance_types")
	private List<AggregateApplianceType> applianceTypes;
	@Json(name = "compute_sites")
	private List<ComputeSite> computeSites;

	public List<AggregateApplianceType> getApplianceTypes() {
		return applianceTypes;
	}

	public void setApplianceTypes(List<AggregateApplianceType> applianceTypes) {
		this.applianceTypes = applianceTypes;
	}

	public List<ComputeSite> getComputeSites() {
		return computeSites;
	}

	public void setComputeSites(List<ComputeSite> computeSites) {
		this.computeSites = computeSites;
	}

	@Override
	public String toString() {
		return "AggregateApplianceTypes [applianceTypes=" + applianceTypes + ", computeSites=" + computeSites + "]";
	}
}