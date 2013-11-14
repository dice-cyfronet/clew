package pl.cyfronet.coin.clew.client.controller.cf.endpoint;

import java.util.List;

public class EndpointsResponse {
	private List<Endpoint> endpoints;

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}
}