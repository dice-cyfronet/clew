package pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate;

import org.fusesource.restygwt.client.Json;

public class PortMappingTemplateRequestResponse {
	@Json(name = "port_mapping_template")
	private PortMappingTemplate portMappingTemplate;

	public PortMappingTemplate getPortMappingTemplate() {
		return portMappingTemplate;
	}

	public void setPortMappingTemplate(PortMappingTemplate portMappingTemplate) {
		this.portMappingTemplate = portMappingTemplate;
	}
}