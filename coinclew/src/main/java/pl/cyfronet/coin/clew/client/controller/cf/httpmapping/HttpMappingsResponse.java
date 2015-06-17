package pl.cyfronet.coin.clew.client.controller.cf.httpmapping;

import java.util.List;

import org.fusesource.restygwt.client.Json;

public class HttpMappingsResponse {
	@Json(name = "http_mappings")
	private List<HttpMapping> httpMappings;

	public List<HttpMapping> getHttpMappings() {
		return httpMappings;
	}

	public void setHttpMappings(List<HttpMapping> httpMappings) {
		this.httpMappings = httpMappings;
	}
}