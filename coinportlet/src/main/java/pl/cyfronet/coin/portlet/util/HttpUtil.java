package pl.cyfronet.coin.portlet.util;

import org.apache.commons.codec.binary.Base64;

public class HttpUtil {
	public String createBasicAuthenticationHeaderValue (String user, String password) {
		String auth = (user != null ? user : "") + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);
		
		return authHeader;
	}
}