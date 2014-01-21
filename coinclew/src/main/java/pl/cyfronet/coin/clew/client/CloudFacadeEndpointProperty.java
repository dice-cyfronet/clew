package pl.cyfronet.coin.clew.client;

import java.util.MissingResourceException;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Dictionary;

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
		if (dictionary != null && dictionary.get("cloudFacadeUrl") != null) {
			return dictionary.get("cloudFacadeUrl");
		}
		
		return properties.getCloudFacadeRootUrl();
	}
}