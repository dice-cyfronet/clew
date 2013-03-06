package pl.cyfronet.coin.portlet.lobcder;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import pl.cyfronet.coin.portlet.util.HttpUtil;

public class LobcderRestClient {
	private static final Logger log = LoggerFactory.getLogger(LobcderRestClient.class);
	
	@Autowired private RestTemplate rest;
	@Autowired private HttpUtil httpUtil;
	
	private String baseUrl;
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public LobcderRestMetadata getMetadata(String path, String securityToken) throws LobcderException {
		if(path.equals("/")) {
			throw new LobcderException("Cannot fetch metadata for the root LOBCDER directory from the REST service");
		}
		
		if(path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		
		if(!path.startsWith("/")) {
			path = "/" + path;
		}
		
		String resourceName = path.substring(path.lastIndexOf("/") + 1, path.length());
		String parentPath = path.substring(0, path.lastIndexOf("/"));
		
		if(parentPath.length() == 0) {
			parentPath = "/";
		}
		
		log.debug("Fetching LOBCDER metadata for resource {} in parent {}", resourceName, parentPath);
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Authorization", httpUtil.createBasicAuthenticationHeaderValue(null, securityToken));
		requestHeaders.add("Accept", "application/xml");
		
		HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<LobcderRestMetadataList> response = rest.exchange(getMetadataUrl(parentPath), HttpMethod.GET, requestEntity, LobcderRestMetadataList.class);
		log.debug("LOBCDER REST response status and body for {} ({}) is {} and {}",
				new String[] {resourceName, getMetadataUrl(parentPath), String.valueOf(response.getStatusCode()),
				response.getBody().toString()});

		if(response.getBody() != null && response.getBody().getMetadataList() != null) {
			for(LobcderRestMetadata metadata : response.getBody().getMetadataList()) {
				if(metadata.getName().equals(resourceName)) {
					return metadata;
				}
			}
		}
		
		throw new LobcderException("Could not find LOBCDER metadata for resource " + resourceName + " and parent " + parentPath);
	}
	
	public void updateMetadata(LobcderRestMetadata lobcderRestMetadata, String securityToken) throws LobcderException {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Authorization", httpUtil.createBasicAuthenticationHeaderValue(null, securityToken));
		requestHeaders.add("Content-Type", "application/xml");
		
		HttpEntity<?> requestEntity;
		
		try {
			requestEntity = new HttpEntity(serializePermissions(lobcderRestMetadata.getPermissions()), requestHeaders);
		} catch (JAXBException e) {
			throw new LobcderException("Could not update LOBCDER permissions for resource with UID" + lobcderRestMetadata.getUid());
		}
		
		ResponseEntity<String> response = rest.exchange(getPermissionsUrl(lobcderRestMetadata.getUid()), HttpMethod.PUT, requestEntity, String.class);
		log.debug("LOBCDER REST response status and body for {} ({}) is {}",
				new String[] {lobcderRestMetadata.getUid(), getPermissionsUrl(lobcderRestMetadata.getUid()), String.valueOf(response.getStatusCode())});
	}

	private String serializePermissions(LobcderRestMetadataPermissions permissions) throws JAXBException {
		JAXBContext jaxb = JAXBContext.newInstance(LobcderRestMetadataPermissions.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxb.createMarshaller().marshal(permissions, baos);
		
		return baos.toString();
	}

	private String getPermissionsUrl(String uid) {
		return baseUrl + "/item/permissions/" + uid;
	}

	private String getMetadataUrl(String path) {
		return baseUrl + "/items/query?path=" + path;
	}
}