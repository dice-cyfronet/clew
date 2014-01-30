package pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplateproperty;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class PortMappingTemplatePropertiesResponse {
	@Json(name = "port_mapping_properties")
	private List<PortMappingTemplateProperty> properties;

	public List<PortMappingTemplateProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<PortMappingTemplateProperty> properties) {
		this.properties = properties;
	}
}