package pl.cyfronet.coin.clew.client.controller.cf.portmapping;

import org.fusesource.restygwt.client.Json;

public class PortMapping {
	private String id;
	@Json(name = "public_ip")
	private String publicIp;
	@Json(name = "source_port")
	private String sourcePort;
	@Json(name = "appliance_id")
	private String applianceId;
	@Json(name = "port_mapping_template_id")
	private String portMappingTemplateId;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPublicIp() {
		return publicIp;
	}
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}
	public String getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(String sourcePort) {
		this.sourcePort = sourcePort;
	}
	public String getApplianceId() {
		return applianceId;
	}
	public void setApplianceId(String applianceId) {
		this.applianceId = applianceId;
	}
	public String getPortMappingTemplateId() {
		return portMappingTemplateId;
	}
	public void setPortMappingTemplateId(String portMappingTemplateId) {
		this.portMappingTemplateId = portMappingTemplateId;
	}
	
	@Override
	public String toString() {
		return "PortMapping [id=" + id + ", publicIp=" + publicIp
				+ ", sourcePort=" + sourcePort + ", applianceId=" + applianceId
				+ ", portMappingTemplateId=" + portMappingTemplateId + "]";
	}
}