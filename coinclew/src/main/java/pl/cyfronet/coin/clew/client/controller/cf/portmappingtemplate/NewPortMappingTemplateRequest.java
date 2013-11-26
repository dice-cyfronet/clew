package pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate;

import org.fusesource.restygwt.client.Json;

public class NewPortMappingTemplateRequest {
	@Json(name = "port_mapping_template")
	private NewPortMappingTemplate portMapping;

	public NewPortMappingTemplate getPortMapping() {
		return portMapping;
	}

	public void setPortMapping(NewPortMappingTemplate portMapping) {
		this.portMapping = portMapping;
	}
}