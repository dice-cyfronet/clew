package pl.cyfronet.coin.clew.client.controller.cf.endpoint;

import org.fusesource.restygwt.client.Json;

public class Endpoint {
	private String id;
	private String description;
	private String descriptor;
	@Json(name = "endpoint_type")
	private String endpointType;
	private String portMappingTemplateId;
	@Json(name = "invocation_path")
	private String invocationPath;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
}