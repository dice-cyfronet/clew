package pl.cyfronet.coin.portlet.lobcder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class LobcderRestClient {
	private static final Logger log = LoggerFactory.getLogger(LobcderRestClient.class);
	
	private String baseUrl;
	
	@Autowired private RestTemplate rest;
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public LobcderRestMetadata getMetadata(String path) {
		return rest.getForObject(getMetadataUrl(path), LobcderRestMetadata.class);
	}

	private String getMetadataUrl(String path) {
		return baseUrl + "/Items?path=" + path;
	}
}