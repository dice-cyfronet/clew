package pl.cyfronet.coin.clew.client.controller.cf.httpmapping;

import org.fusesource.restygwt.client.Json;

public class AliasRequest {
	@Json(name = "http_mapping")
	private AliasHttpMapping aliasHttpMapping;

	public AliasHttpMapping getAliasHttpMapping() {
		return aliasHttpMapping;
	}

	public void setAliasHttpMapping(AliasHttpMapping aliasHttpMapping) {
		this.aliasHttpMapping = aliasHttpMapping;
	}
}