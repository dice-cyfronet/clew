package pl.cyfronet.coin.portlet.lobcder;

import java.io.IOException;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class LobcderRestClient {
	private static final Logger log = LoggerFactory.getLogger(LobcderRestClient.class);
	
	@Autowired private RestTemplate rest;
	
	private String baseUrl;
	private ObjectMapper mapper;
	
	public LobcderRestClient() {
		mapper = new ObjectMapper();
		
		AnnotationIntrospector ai = new JaxbAnnotationIntrospector();
		mapper.getDeserializationConfig().setAnnotationIntrospector(ai);
		mapper.getSerializationConfig().setAnnotationIntrospector(ai);
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public LobcderRestMetadata getMetadata(String path) throws LobcderException {
		if(path.equals("/")) {
			throw new LobcderException("Cannot fetch metadata for the root LOBCDER directory from the REST service");
		}
		
		if(path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		
		String resourceName = path.substring(path.lastIndexOf("/") + 1, path.length());
		String parentPath = path.substring(0, path.lastIndexOf("/"));
		
		if(parentPath.length() == 0) {
			parentPath = "/";
		}
		
		log.debug("Fetching LOBCDER metadata for resource {} in parent {}", resourceName, parentPath);
		
		String response = rest.getForObject(getMetadataUrl(parentPath), String.class);
		log.debug("LOBCDER REST response for {} is {}", resourceName, response);
		
		LobcderRestMetadataList metadataList = null;
		
		try {
			metadataList = mapper.readValue(response, LobcderRestMetadataList.class);
		} catch (IOException e) {
			throw new LobcderException("Could not find LOBCDER metadata for resource " + resourceName + " and parent " + parentPath, e);
		}
		
		for(LobcderRestMetadata metadata : metadataList.getMetadataList()) {
			if(metadata.getName().equals(resourceName)) {
				return metadata;
			}
		}
		
		throw new LobcderException("Could not find LOBCDER metadata for resource " + resourceName + " and parent " + parentPath);
	}

	private String getMetadataUrl(String path) {
		return baseUrl + "/Items?path=" + path;
	}
}