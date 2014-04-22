package pl.cyfronet.coin.clew.client.controller.cf.flavor;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class FlavorsResponse {
	@Json(name = "virtual_machine_flavors")
	private List<Flavor> flavors;

	public List<Flavor> getFlavors() {
		return flavors;
	}

	public void setFlavors(List<Flavor> flavors) {
		this.flavors = flavors;
	}
}