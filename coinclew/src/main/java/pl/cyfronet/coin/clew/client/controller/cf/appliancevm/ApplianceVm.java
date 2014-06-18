package pl.cyfronet.coin.clew.client.controller.cf.appliancevm;

import org.fusesource.restygwt.client.Json;

public class ApplianceVm {
	private String id;
	@Json(name = "id_at_site")
	private String siteId;
	private String name;
	private String state;
	private String ip;
	@Json(name = "compute_site_id")
	private String computeSiteId;
	@Json(name = "flavor_id")
	private String flavorId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getComputeSiteId() {
		return computeSiteId;
	}
	public void setComputeSiteId(String computeSiteId) {
		this.computeSiteId = computeSiteId;
	}
	public String getFlavorId() {
		return flavorId;
	}
	public void setFlavorId(String flavorId) {
		this.flavorId = flavorId;
	}
}