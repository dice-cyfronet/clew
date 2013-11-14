package pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class PortMappingTemplatesResponse {
	@Json(name = "port_mapping_templates")
	private List<PortMappingTemplate> portMappingTemplates;

	public List<PortMappingTemplate> getPortMappingTemplates() {
		return portMappingTemplates;
	}

	public void setPortMappingTemplates(List<PortMappingTemplate> portMappingTemplates) {
		this.portMappingTemplates = portMappingTemplates;
	}
}