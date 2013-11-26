package pl.cyfronet.coin.clew.client.controller.cf.portmapping;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class PortMappingResponse {
	@Json(name = "port_mappings")
	private List<PortMapping> portMappings;

	public List<PortMapping> getPortMappings() {
		return portMappings;
	}

	public void setPortMappings(List<PortMapping> portMappings) {
		this.portMappings = portMappings;
	}
}