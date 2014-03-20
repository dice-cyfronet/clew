package pl.cyfronet.coin.clew.client.controller.cf.endpoint;

import org.fusesource.restygwt.client.Json;

public class NewEndpoint {
	private String description;
	private String descriptor;
	@Json(name = "endpoint_type")
	private String endpointType;
	@Json(name = "port_mapping_template_id")
	private String portMappingTemplateId;
	@Json(name = "invocation_path")
	private String invocationPath;
	private String name;
	private boolean secured;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
	public String getEndpointType() {
		return endpointType;
	}
	public void setEndpointType(String endpointType) {
		this.endpointType = endpointType;
	}
	public String getPortMappingTemplateId() {
		return portMappingTemplateId;
	}
	public void setPortMappingTemplateId(String portMappingTemplateId) {
		this.portMappingTemplateId = portMappingTemplateId;
	}
	public String getInvocationPath() {
		return invocationPath;
	}
	public void setInvocationPath(String invocationPath) {
		this.invocationPath = invocationPath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSecured() {
		return secured;
	}
	public void setSecured(boolean secured) {
		this.secured = secured;
	}
}