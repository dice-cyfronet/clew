package pl.cyfronet.coin.clew.client.controller.overlay;

import java.util.List;

import pl.cyfronet.coin.clew.client.controller.cf.endpoint.Endpoint;
import pl.cyfronet.coin.clew.client.controller.cf.portmapping.PortMapping;

public class Redirection {
	private String id;
	private boolean isHttp;
	private String protocol;
	private String name;
	private int targetPort;
	private String httpUrl;
	private String httpsUrl;
	private List<Endpoint> endpoints;
	private List<PortMapping> portMappings;
	private String httpUrlStatus;
	private String httpsUrlStatus;
	private String customHttpAlias;
	private String customHttpsAlias;
	private String customHttpUrl;
	private String customHttpsUrl;
	private String httpMappingId;
	private String httpsMappingId;
	
	public String getHttpMappingId() {
		return httpMappingId;
	}
	public void setHttpMappingId(String httpMappingId) {
		this.httpMappingId = httpMappingId;
	}
	public String getHttpsMappingId() {
		return httpsMappingId;
	}
	public void setHttpsMappingId(String httpsMappingId) {
		this.httpsMappingId = httpsMappingId;
	}
	public String getCustomHttpAlias() {
		return customHttpAlias;
	}
	public void setCustomHttpAlias(String customHttpAlias) {
		this.customHttpAlias = customHttpAlias;
	}
	public String getCustomHttpsAlias() {
		return customHttpsAlias;
	}
	public void setCustomHttpsAlias(String customHttpsAlias) {
		this.customHttpsAlias = customHttpsAlias;
	}
	public String getCustomHttpUrl() {
		return customHttpUrl;
	}
	public void setCustomHttpUrl(String customHttpUrl) {
		this.customHttpUrl = customHttpUrl;
	}
	public String getCustomHttpsUrl() {
		return customHttpsUrl;
	}
	public void setCustomHttpsUrl(String customHttpsUrl) {
		this.customHttpsUrl = customHttpsUrl;
	}
	public String getHttpUrlStatus() {
		return httpUrlStatus;
	}
	public void setHttpUrlStatus(String httpUrlStatus) {
		this.httpUrlStatus = httpUrlStatus;
	}
	public String getHttpsUrlStatus() {
		return httpsUrlStatus;
	}
	public void setHttpsUrlStatus(String httpsUrlStatus) {
		this.httpsUrlStatus = httpsUrlStatus;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isHttp() {
		return isHttp;
	}
	public void setHttp(boolean isHttp) {
		this.isHttp = isHttp;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHttpUrl() {
		return httpUrl;
	}
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	public String getHttpsUrl() {
		return httpsUrl;
	}
	public void setHttpsUrl(String httpsUrl) {
		this.httpsUrl = httpsUrl;
	}
	public List<Endpoint> getEndpoints() {
		return endpoints;
	}
	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}
	public int getTargetPort() {
		return targetPort;
	}
	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}
	public List<PortMapping> getPortMappings() {
		return portMappings;
	}
	public void setPortMappings(List<PortMapping> portMappings) {
		this.portMappings = portMappings;
	}
}