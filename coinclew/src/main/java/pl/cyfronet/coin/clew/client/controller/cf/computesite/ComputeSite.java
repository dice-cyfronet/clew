package pl.cyfronet.coin.clew.client.controller.cf.computesite;

import org.fusesource.restygwt.client.Json;

public class ComputeSite {
	private String id;
	@Json(name = "site_id")
	private String siteId;
	private String name;
	private String location;
	@Json(name = "site_type")
	private String siteType;
	private String technology;
	private String config;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	@Override
	public String toString() {
		return "ComputeSite [id=" + id + ", siteId=" + siteId + ", name="
				+ name + ", location=" + location + ", siteType=" + siteType
				+ ", technology=" + technology + ", config=" + config + "]";
	}
}