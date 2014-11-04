package pl.cyfronet.coin.clew.client;

import java.util.MissingResourceException;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;

public class CloudFacadeEndpointProperty {
	private ClewProperties properties;
	private Dictionary dictionary;

	public CloudFacadeEndpointProperty() {
		properties = GWT.create(ClewProperties.class);
		
		try {
			dictionary = Dictionary.getDictionary("ClewOverrides");
		} catch (MissingResourceException e) {
			//ignoring - default property value will be used
		}
	}

	public String getCloudFacadeEndpoint() {
		String endpoint = getDictionaryProperty("cloudFacadeUrl");
		if (endpoint == null) {
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
	
	private String getProperty(String propertyName) {
		String property = getDictionaryProperty(propertyName);
		if(property == null) {
			property = Window.Location.getParameter(propertyName);
		}
		return property;
	}
	
	private String getDictionaryProperty(String propertyName) {
		if (dictionary != null && dictionary.keySet().contains(propertyName)) {
			return dictionary.get(propertyName);
		}
		
		return null;
	}
}