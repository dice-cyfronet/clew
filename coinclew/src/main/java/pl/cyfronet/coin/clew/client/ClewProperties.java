package pl.cyfronet.coin.clew.client;

import com.google.gwt.i18n.client.Constants;

public interface ClewProperties extends Constants {
	String getDashboardContainerId();
	/* Do not use this directly, instead use pl/cyfronet/coin/clew/client/CloudFacadeEndpointProperty.java */
	String getCloudFacadeRootUrl();
	String userKeyUploadPath();
}