package pl.cyfronet.coin.clew.client.controller.cf.computesite;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class ComputeSitesResponse {
	@Json(name = "compute_sites")
	private List<ComputeSite> computeSites;

	public List<ComputeSite> getComputeSites() {
		return computeSites;
	}

	public void setComputeSites(List<ComputeSite> computeSites) {
		this.computeSites = computeSites;
	}
}