package pl.cyfronet.coin.portlet.lobcder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.client.methods.DavMethod;
import org.apache.jackrabbit.webdav.client.methods.DeleteMethod;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.jackrabbit.webdav.client.methods.PropPatchMethod;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.apache.jackrabbit.webdav.property.DavProperty;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.property.DefaultDavProperty;
import org.apache.jackrabbit.webdav.xml.Namespace;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import pl.cyfronet.coin.portlet.util.HttpUtil;

public class LobcderClient {
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(LobcderClient.class);
	
	private static Namespace CUSTOM_NAMESPACE = Namespace.getNamespace("c", "custom:");
	
	private static final DavPropertyName DRI_SUPERVISED = DavPropertyName.create("dri-supervised", CUSTOM_NAMESPACE);
	private static final DavPropertyName DRI_CHECKSUM = DavPropertyName.create("dri-checksum-MD5", CUSTOM_NAMESPACE);
	private static final DavPropertyName DRI_LAST_VALIDATION = DavPropertyName.create("dri-last-validation-date-ms", CUSTOM_NAMESPACE);
	
	private String baseUrl;
	private HttpClient client;
	
	@Autowired private HttpUtil httpUtil;
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void initialize() throws MalformedURLException {
		HostConfiguration hostConfig = new HostConfiguration();
		URL url = new URL(baseUrl);
        hostConfig.setHost(url.getHost(), url.getPort());
        
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        int maxHostConnections = 20;
        params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
        connectionManager.setParams(params);
        client = new HttpClient(connectionManager);
        client.setHostConfiguration(hostConfig);
	}
	
	public List<LobcderEntry> list(String path, String securityToken) throws LobcderException {
		List<LobcderEntry> entries = new ArrayList<>();
		DavMethod pFind = null;
		
		try {
			DavPropertyNameSet properties = new DavPropertyNameSet();
			properties.add(DavPropertyName.GETCONTENTLENGTH);
			pFind = new PropFindMethod(createLobcderPath(path), properties, DavConstants.DEPTH_1);
			executeMethod(pFind, securityToken);

		    MultiStatus multiStatus = pFind.getResponseBodyAsMultiStatus();
		    MultiStatusResponse[] responses = multiStatus.getResponses();

		    for (int i = 0; i < responses.length; i++) {
		    	MultiStatusResponse response = responses[i];
	        	String name = normalizeName(response.getHref());
	        	
	        	if (!name.equals(path)) {
		            LobcderEntry entry = new LobcderEntry(name);
		            entry.setDirectory(response.getHref().endsWith("/"));
		            
		            if(!entry.isDirectory()) {
		            	entry.setBytes(Long.parseLong((String) response.getProperties(200).get(DavPropertyName.GETCONTENTLENGTH).getValue()));
		            }
		            
		            entries.add(entry);
	        	}
		    } 
		} catch (IOException | DavException e) {
			String msg = "Could not list LOBCDER resources for base URL [" + baseUrl + "] and path [" + path + "]";
			log.error(msg, e);
			throw new LobcderException(msg, e);
		} finally {
			if(pFind != null) {
				pFind.releaseConnection();
			}
		}

		return entries;
	}

	public void put(String path, String fileName, InputStream inputStream, String securityToken) throws LobcderException {
		path = ensureDirectory(path);
		PutMethod putMethod = new PutMethod(createLobcderPath(path) + fileName);
		putMethod.setRequestEntity(new InputStreamRequestEntity(inputStream));
		
		try {
			executeMethod(putMethod, securityToken);
		} catch (IOException e) {
			String msg = "Could not upload file to LOBCDER for base URL [" + baseUrl + "], path [" + path + "] and file [" + fileName + "]";
			log.error(msg, e);
			throw new LobcderException(msg, e);
		} finally {
			if(putMethod != null) {
				putMethod.releaseConnection();
			}
		}
	}

	public InputStream get(String filePath, String securityToken) throws LobcderException {
		GetMethod getMethod = new GetMethod(createLobcderPath(filePath));
		
		try {
			executeMethod(getMethod, securityToken);
			
			return getMethod.getResponseBodyAsStream();
		} catch (IOException e) {
			String msg = "Could not download file from LOBCDER for base URL [" + baseUrl + "] and file path [" + filePath + "]";
			log.error(msg, e);
			throw new LobcderException(msg, e);
		} finally {
			if(getMethod != null) {
				getMethod.releaseConnection();
			}
		}
	}
	
	public void createDirectory(String parentPath, String directoryName, String securityToken) throws LobcderException {
		MkColMethod mkdirMethod = new MkColMethod(createLobcderPath(parentPath) + directoryName);
		
		try {
			executeMethod(mkdirMethod, securityToken);
		} catch (IOException e) {
			String msg = "Could not create new directory in LOBCDER for base URL [" + baseUrl + "], parent path [" + parentPath + "]" +
					" and directory name [" + directoryName + "]";
			log.error(msg, e);
			throw new LobcderException(msg, e);
		} finally {
			if(mkdirMethod != null) {
				mkdirMethod.releaseConnection();
			}
		}
	}
	
	public void delete(String path, String securityToken) throws LobcderException {
		DeleteMethod deleteMethod = new DeleteMethod(createLobcderPath(path));
		
		try {
			executeMethod(deleteMethod, securityToken);
		} catch (Exception e) {
			String msg = "Could not delete LOBCDER resource for base URL [" + baseUrl + "] and path [" + path + "]";
			log.error(msg, e);
			throw new LobcderException(msg, e);
		} finally {
			if(deleteMethod != null) {
				deleteMethod.releaseConnection();
			}
		}
	}
	
	public LobcderWebDavMetadata getMetadata(String path, String securityToken) throws LobcderException {
		LobcderWebDavMetadata lobcderWebDavMetadata = new LobcderWebDavMetadata();
		DavPropertyNameSet properties = new DavPropertyNameSet();
		properties.add(DRI_SUPERVISED);
		properties.add(DRI_CHECKSUM);
		properties.add(DRI_LAST_VALIDATION);
		properties.add(DavPropertyName.CREATIONDATE);
		properties.add(DavPropertyName.GETLASTMODIFIED);
		properties.add(DavPropertyName.GETCONTENTTYPE);
		DavMethod propFind = null;

		try {
			propFind = new PropFindMethod(createLobcderPath(path), properties, DavConstants.DEPTH_0);
			executeMethod(propFind, securityToken);
			
			MultiStatus multiStatus = propFind.getResponseBodyAsMultiStatus();
		    MultiStatusResponse[] responses = multiStatus.getResponses();

		    if(responses.length > 0) {
		    	MultiStatusResponse response = responses[0];
		    	lobcderWebDavMetadata.setDriSupervised(Boolean.parseBoolean(getValue(response, DRI_SUPERVISED)));
		    	lobcderWebDavMetadata.setDriChecksum(Long.parseLong(getValue(response, DRI_CHECKSUM)));
		    	lobcderWebDavMetadata.setDriLastValidationDateMs(Long.parseLong(getValue(response, DRI_LAST_VALIDATION)));
		    	lobcderWebDavMetadata.setCreationDate(getValue(response, DavPropertyName.CREATIONDATE));
		    	lobcderWebDavMetadata.setModificationDate(getValue(response, DavPropertyName.GETLASTMODIFIED));
		    	lobcderWebDavMetadata.setFormat(getValue(response, DavPropertyName.GETCONTENTTYPE));
		    }
		} catch (Exception e) {
			String msg = "Could not fetch LOBCDER metadata for base URL [" + baseUrl + "] and path [" + path + "]";
			log.error(msg, e);
			throw new LobcderException(msg, e);
		} finally {
			if(propFind != null) {
				propFind.releaseConnection();
			}
		}

		return lobcderWebDavMetadata;
	}

	public void updateMetadata(String path, LobcderWebDavMetadata lobcderWebDavMetadata, String securityToken) throws LobcderException {
		DavPropertySet setProperties = null;
		DavMethod propPatch = null;
		
		//each property has to be updated separately :(
		try {
			setProperties = new DavPropertySet();
			setProperties.add(new DefaultDavProperty<Boolean>(DRI_SUPERVISED, lobcderWebDavMetadata.isDriSupervised()));
			propPatch = new PropPatchMethod(createLobcderPath(path), setProperties, new DavPropertyNameSet());
			executeMethod(propPatch, securityToken);
		
			setProperties = new DavPropertySet();
			setProperties.add(new DefaultDavProperty<Long>(DRI_CHECKSUM, lobcderWebDavMetadata.getDriChecksum()));
			propPatch = new PropPatchMethod(createLobcderPath(path), setProperties, new DavPropertyNameSet());
			executeMethod(propPatch, securityToken);
			
			setProperties = new DavPropertySet();
			setProperties.add(new DefaultDavProperty<Long>(DRI_LAST_VALIDATION, lobcderWebDavMetadata.getDriLastValidationDateMs()));
			propPatch = new PropPatchMethod(createLobcderPath(path), setProperties, new DavPropertyNameSet());
			executeMethod(propPatch, securityToken);
			
			setProperties = new DavPropertySet();
			setProperties.add(new DefaultDavProperty<String>(DavPropertyName.CREATIONDATE, lobcderWebDavMetadata.getCreationDate()));
			propPatch = new PropPatchMethod(createLobcderPath(path), setProperties, new DavPropertyNameSet());
			executeMethod(propPatch, securityToken);
			
			setProperties = new DavPropertySet();
			setProperties.add(new DefaultDavProperty<String>(DavPropertyName.GETLASTMODIFIED, lobcderWebDavMetadata.getModificationDate()));
			propPatch = new PropPatchMethod(createLobcderPath(path), setProperties, new DavPropertyNameSet());
			executeMethod(propPatch, securityToken);
			
			setProperties = new DavPropertySet();
			setProperties.add(new DefaultDavProperty<String>(DavPropertyName.GETCONTENTTYPE, lobcderWebDavMetadata.getFormat()));
			propPatch = new PropPatchMethod(createLobcderPath(path), setProperties, new DavPropertyNameSet());
			executeMethod(propPatch, securityToken);
		} catch (Exception e) {
			String msg = "Could not set LOBCDER metadata for base URL [" + baseUrl + "] and path [" + path + "]";
			log.error(msg, e);
			throw new LobcderException(msg, e);
		} finally {
			if(propPatch != null) {
				propPatch.releaseConnection();
			}
		}
	}

	private String getValue(MultiStatusResponse response, DavPropertyName davPropertyName) {
		DavProperty<?> property = response.getProperties(200).get(davPropertyName);
		
		if(property != null) {
			return (String) property.getValue();
		}
		
		return null;
	}
	
	private String normalizeName(String nativeName) throws MalformedURLException {
		URL url = new URL(baseUrl);
		String baseWithoutHost = baseUrl.substring(baseUrl.indexOf("/", baseUrl.indexOf(url.getHost())));
		String normalizedName = nativeName.substring(baseWithoutHost.length() + 1);
		
		//we want the root to be returned as "/"
		if(normalizedName.isEmpty()) {
			normalizedName = "/";
		}
		
		return normalizedName;
	}

	private String createLobcderPath(String path) {
		StringBuilder result = new StringBuilder();
		result.append(baseUrl);
		
		if(!baseUrl.endsWith("/") && !path.startsWith("/")) {
			result.append("/");
		}
		
		if(baseUrl.endsWith("/") && path.startsWith("/")) {
			result.deleteCharAt(result.length() - 1);
		}
		
		result.append(path);
		
		return result.toString();
	}
	
	private String ensureDirectory(String path) {
		String result = path;
		
		if(path != null) {
			if(!path.endsWith("/")) {
				result = path + "/";
			}
		}
		
		return result;
	}
	
	private void executeMethod(HttpMethod method, String securityToken) throws HttpException, IOException {
		method.addRequestHeader("Authorization", httpUtil.createBasicAuthenticationHeaderValue(null, securityToken));
		client.executeMethod(method);
	}
}