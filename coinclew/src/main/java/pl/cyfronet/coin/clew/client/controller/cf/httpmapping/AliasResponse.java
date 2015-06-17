package pl.cyfronet.coin.clew.client.controller.cf.httpmapping;

import org.fusesource.restygwt.client.Json;

public class AliasResponse {
	@Json(name = "http_mapping")
	private AliasResponseHttpMapping aliasResponseHttpMapping;

	public AliasResponseHttpMapping getAliasResponseHttpMapping() {
		return aliasResponseHttpMapping;
	}

	public void setAliasResponseHttpMapping(AliasResponseHttpMapping aliasResponseHttpMapping) {
		this.aliasResponseHttpMapping = aliasResponseHttpMapping;
	}
}