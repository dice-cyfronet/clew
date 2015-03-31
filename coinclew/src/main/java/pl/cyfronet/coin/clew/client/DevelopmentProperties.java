package pl.cyfronet.coin.clew.client;

import com.google.gwt.i18n.client.Constants;

/**
 * To override these properties create the following file:
 * pl/cyfronet/coin/clew/client/DevelopmentProperties.properties
 *
 */
public interface DevelopmentProperties extends Constants {
	static final String MISSING = "missing";
	
	@DefaultStringValue(MISSING)
	String cloudFacadeKey();
	
	@DefaultStringValue(MISSING)
	String developmentUserLogin();
	
	@DefaultStringValue(MISSING)
	String ticketOverride();
	
	@DefaultStringValue(MISSING)
	String proxyBase64();
}