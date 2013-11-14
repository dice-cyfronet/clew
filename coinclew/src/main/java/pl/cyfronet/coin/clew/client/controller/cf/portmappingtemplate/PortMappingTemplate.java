package pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate;

import org.fusesource.restygwt.client.Json;

public class PortMappingTemplate {
	private String id;
	@Json(name = "transport_protocol")
	private String transportProtocol;
	@Json(name = "application_protocol")
	private String applicationProtocol;
	@Json(name = "service_name")
	private String serviceName;
	@Json(name = "target_port")
	private int targetPort;
	@Json(name = "appliance_type_id")
	private String applianceTypeId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTransportProtocol() {
		return transportProtocol;
	}
	public void setTransportProtocol(String transportProtocol) {
		this.transportProtocol = transportProtocol;
	}
	public String getApplicationProtocol() {
		return applicationProtocol;
	}
	public void setApplicationProtocol(String applicationProtocol) {
		this.applicationProtocol = applicationProtocol;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public int getTargetPort() {
		return targetPort;
	}
	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}
	public String getApplianceTypeId() {
		return applianceTypeId;
	}
	public void setApplianceTypeId(String applianceTypeId) {
		this.applianceTypeId = applianceTypeId;
	}
}