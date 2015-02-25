package pl.cyfronet.coin.clew.client;

import java.util.MissingResourceException;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;

public class CloudFacadeOverrideProperties {
	private ClewProperties properties;
	private Dictionary dictionary;

	public CloudFacadeOverrideProperties() {
		properties = GWT.create(ClewProperties.class);
		
		try {
			dictionary = Dictionary.getDictionary("ClewOverrides");
		} catch (MissingResourceException e) {
			//ignoring - default property value will be used
		}
	}

	public String getCloudFacadeEndpoint() {
		String endpoint = getDictionaryProperty("cloudFacadeUrl");
		
		if(endpoint == null) {
			endpoint = properties.getCloudFacadeRootUrl();
		}
		
		return endpoint;
	}
	
	public String getPrivateToken() {		
		return getProperty("private_token");
	}
	
	public String getUsername() {
		return getProperty("username");
	}
	
	public String getBuildInfo() {
		return properties.buildInfo();
	}
	
	public String getTicket() {
		return getDictionaryProperty("vphTicket");
	}
	
	public String getCsrfHeaderName() {
		return getDictionaryProperty("csrf_header_name");
	}

	public String getCsrfToken() {
		return getDictionaryProperty("csrf_token");
	}
	
	private String getProperty(String propertyName) {
		String property = getDictionaryProperty(propertyName);
		
		if(property == null) {
			property = Window.Location.getParameter(propertyName);
		}
		
		return property;
	}
	
	private String getDictionaryProperty(String propertyName) {
		if(dictionary != null && dictionary.keySet().contains(propertyName)) {
			return dictionary.get(propertyName);
		}
		
		return null;
	}
}