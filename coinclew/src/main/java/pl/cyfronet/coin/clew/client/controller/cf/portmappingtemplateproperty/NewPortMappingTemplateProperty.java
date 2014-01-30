package pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplateproperty;

import org.fusesource.restygwt.client.Json;

public class NewPortMappingTemplateProperty {
	@Json(name = "port_mapping_template_id")
	private String portMappingTemplateId;
	private String key;
	private String value;
	
	public String getPortMappingTemplateId() {
		return portMappingTemplateId;
	}
	public void setPortMappingTemplateId(String portMappingTemplateId) {
		this.portMappingTemplateId = portMappingTemplateId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}