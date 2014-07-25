package pl.cyfronet.coin.clew.client.controller.cf.aggregates;

import java.util.List;

import org.fusesource.restygwt.client.Json;

import pl.cyfronet.coin.clew.client.controller.cf.endpoint.Endpoint;
import pl.cyfronet.coin.clew.client.controller.cf.httpmapping.HttpMapping;

public class AggregatePortMappingTemplate {
	private String id;
	
	@Json(name = "http_mappings")
	private List<HttpMapping> httpMappings;
	
	private List<Endpoint> endpoints;
	
	@Json(name = "transport_protocol")
	private String transportProtocol;
	
	@Json(name = "application_protocol")
	private String applicationProtocol;
	
	@Json(name = "service_name")
	private String serviceName;
	
	@Json(name = "target_port")
	private int targetPort;
	
	public int getTargetPort() {
		return targetPort;
	}
	
	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<HttpMapping> getHttpMappings() {
		return httpMappings;
	}

	public void setHttpMappings(List<HttpMapping> httpMappings) {
		this.httpMappings = httpMappings;
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	@Override
	public String toString() {
		return "AggregatePortMappingTemplate [id=" + id + ", httpMappings="
				+ httpMappings + ", endpoints=" + endpoints + ", serviceName="
				+ serviceName + ", targetPort=" + targetPort
				+ ", transportProtocol=" + transportProtocol
				+ ", applicationProtocol=" + applicationProtocol + "]";
	}
}