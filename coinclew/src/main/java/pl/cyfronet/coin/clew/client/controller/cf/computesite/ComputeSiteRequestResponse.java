package pl.cyfronet.coin.clew.client.controller.cf.computesite;

import org.fusesource.restygwt.client.Json;

public class ComputeSiteRequestResponse {
	@Json(name = "compute_site")
	private ComputeSite computeSite;

	public ComputeSite getComputeSite() {
		return computeSite;
	}

	public void setComputeSite(ComputeSite computeSite) {
		this.computeSite = computeSite;
	}
}