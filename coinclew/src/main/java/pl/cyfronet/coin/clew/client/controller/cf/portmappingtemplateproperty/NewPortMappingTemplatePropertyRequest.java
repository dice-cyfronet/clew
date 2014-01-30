package pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplateproperty;

import org.fusesource.restygwt.client.Json;

public class NewPortMappingTemplatePropertyRequest {
	@Json(name = "port_mapping_property")
	private NewPortMappingTemplateProperty portMappingTemplateProperty;

	public NewPortMappingTemplateProperty getPortMappingTemplateProperty() {
		return portMappingTemplateProperty;
	}

	public void setPortMappingTemplateProperty(
			NewPortMappingTemplateProperty portMappingTemplateProperty) {
		this.portMappingTemplateProperty = portMappingTemplateProperty;
	}
}