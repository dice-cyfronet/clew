package pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplateproperty;

import org.fusesource.restygwt.client.Json;

public class PortMappingTemplatePropertyRequestResponse {
	@Json(name = "port_mapping_property")
	private PortMappingTemplateProperty portMappingTemplateProperty;

	public PortMappingTemplateProperty getPortMappingTemplateProperty() {
		return portMappingTemplateProperty;
	}

	public void setPortMappingTemplateProperty(PortMappingTemplateProperty portMappingTemplateProperty) {
		this.portMappingTemplateProperty = portMappingTemplateProperty;
	}
}