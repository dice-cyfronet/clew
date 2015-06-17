package pl.cyfronet.coin.clew.client.controller.cf.httpmapping;

import org.fusesource.restygwt.client.Json;

public class AliasHttpMapping {
	@Json(name = "custom_name")
	private String customName;

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}
}