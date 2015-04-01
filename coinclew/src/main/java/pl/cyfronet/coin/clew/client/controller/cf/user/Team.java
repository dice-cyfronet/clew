package pl.cyfronet.coin.clew.client.controller.cf.user;

import org.fusesource.restygwt.client.Json;

public class Team {
	private String id;
	@Json(name = "shortname")
	private String shortName;
	private String description;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}