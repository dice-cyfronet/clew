package pl.cyfronet.coin.portlet.lobcder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
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
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.apache.jackrabbit.webdav.property.DavProperty;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
import org.slf4j.Logger;

public class LobcderClient {
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(LobcderClient.class);
	
	private String baseUrl;
	private String username;
	private String password;
	
	private HttpClient client;
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
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
        
        Credentials creds = new UsernamePasswordCredentials(username, password);
        client = new HttpClient(connectionManager);
        client.getState().setCredentials(AuthScope.ANY, creds);
        client.setHostConfiguration(hostConfig);
	}
	
	public List<LobcderEntry> list(String path) throws LobcderException {
		List<LobcderEntry> entries = new ArrayList<>();
		
		try {
			DavPropertyNameSet properties = new DavPropertyNameSet();
			properties.add(DavPropertyName.GETCONTENTLENGTH);
			DavMethod pFind = new PropFindMethod(createLobcderPath(path), properties, DavConstants.DEPTH_1);
			client.executeMethod(pFind);

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
		}

		return entries;
	}
	
	public void put(String path, String fileName, InputStream inputStream) throws LobcderException {
		PutMethod putMethod = new PutMethod(createLobcderPath(path) + fileName);
		putMethod.setRequestEntity(new InputStreamRequestEntity(inputStream));
		
		try {
			client.executeMethod(putMethod);
		} catch (IOException e) {
			String msg = "Could not upload file to LOBCDER for base URL [" + baseUrl + "], path [" + path + "] and file [" + fileName + "]";
			log.error(msg, e);
			throw new LobcderException(msg, e);
		}
	}
	
	public InputStream get(String filePath) throws LobcderException {
		GetMethod getMethod = new GetMethod(createLobcderPath(filePath));
		
		try {
			client.executeMethod(getMethod);
			
			return getMethod.getResponseBodyAsStream();
		} catch (IOException e) {
			String msg = "Could not download file from LOBCDER for base URL [" + baseUrl + "] and file path [" + filePath + "]";
			log.error(msg, e);
			throw new LobcderException(msg, e);
		}
	}
	
	public void createDirectory(String parentPath, String directoryName) throws LobcderException {
		MkColMethod mkdirMethod = new MkColMethod(createLobcderPath(parentPath) + directoryName);
		
		try {
			client.executeMethod(mkdirMethod);
		} catch (IOException e) {
			String msg = "Could not create new directory in LOBCDER for base URL [" + baseUrl + "], parent path [" + parentPath + "]" +
					" and directory name [" + directoryName + "]";
			log.error(msg, e);
			throw new LobcderException(msg, e);
		}
	}
	
	public void delete(String path) throws LobcderException {
		DeleteMethod deleteMethod = new DeleteMethod(createLobcderPath(path));
		
		try {
			client.executeMethod(deleteMethod);
		} catch (IOException e) {
			String msg = "Could not delete LOBCDER resource for base URL [" + baseUrl + "] and path [" + path + "]";
			log.error(msg, e);
			throw new LobcderException(msg, e);
		}
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
}