package pl.cyfronet.coin.clew.client.controller.cf.httpmapping;

import org.fusesource.restygwt.client.Json;

public class HttpMapping {
	private String id;
	@Json(name = "application_protocol")
	
	private String applicationProtocol;
	
	private String url;
	
	@Json(name = "appliance_id")
	private String applianceId;
	
	@Json(name = "port_mapping_template_id")
	private String portMappingTemplateId;
	
	@Json(name = "monitoring_status")
	private String status;
	
	@Json(name = "custom_name")
	private String custonName;
	
	@Json(name = "custom_url")
	private String custromUrl;
	
	public String getCustonName() {
		return custonName;
	}
	public void setCustonName(String custonName) {
		this.custonName = custonName;
	}
	public String getCustromUrl() {
		return custromUrl;
	}
	public void setCustromUrl(String custromUrl) {
		this.custromUrl = custromUrl;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApplicationProtocol() {
		return applicationProtocol;
	}
	public void setApplicationProtocol(String applicationProtocol) {
		this.applicationProtocol = applicationProtocol;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
		return "HttpMapping [id=" + id + ", applicationProtocol="
				+ applicationProtocol + ", url=" + url + ", applianceId="
				+ applianceId + ", portMappingTemplateId="
				+ portMappingTemplateId + ", status=" + status + "]";
	}
}