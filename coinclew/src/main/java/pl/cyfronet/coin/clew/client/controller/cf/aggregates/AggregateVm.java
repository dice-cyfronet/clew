package pl.cyfronet.coin.clew.client.controller.cf.aggregates;

import java.util.List;

import org.fusesource.restygwt.client.Json;

import pl.cyfronet.coin.clew.client.controller.cf.computesite.ComputeSite;
import pl.cyfronet.coin.clew.client.controller.cf.flavor.Flavor;
import pl.cyfronet.coin.clew.client.controller.cf.portmapping.PortMapping;

public class AggregateVm {
	private String id;
	
	@Json(name = "compute_site")
	private ComputeSite computeSite;
	
	@Json(name = "virtual_machine_flavor")
	private Flavor flavor;
	
	@Json(name = "port_mappings")
	private List<PortMapping> portMappings;
	
	private String ip;
	private String state;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ComputeSite getComputeSite() {
		return computeSite;
	}

	public void setComputeSite(ComputeSite computeSite) {
		this.computeSite = computeSite;
	}

	public Flavor getFlavor() {
		return flavor;
	}

	public void setFlavor(Flavor flavor) {
		this.flavor = flavor;
	}

	@Override
	public String toString() {
		return "AggregateVm [id=" + id + ", computeSite=" + computeSite
				+ ", flavor=" + flavor + ", portMappings=" + portMappings
				+ ", ip=" + ip + ", state=" + state + "]";
	}

	public List<PortMapping> getPortMappings() {
		return portMappings;
	}

	public void setPortMappings(List<PortMapping> portMappings) {
		this.portMappings = portMappings;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}