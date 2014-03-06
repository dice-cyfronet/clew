package pl.cyfronet.coin.clew.client;

public class UrlHelper {
	public static String joinUrl(String url, String path, String user, String token) {
		if (url == null) {
			return null;
		}
		
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		
		if (path != null && path.startsWith("/")) {
			path = path.substring(1, path.length());
		}
		
		String result = url + "/" + path;
		
		if (user != null && token != null) {
			if (result.startsWith("http://")) {
				result = "http://" + user + ":" + token + "@" + result.substring(7);
			} else if (result.startsWith("https://")) {
				result = "https://" + user + ":" + token + "@" + result.substring(8);
			}
		}
		
		return result;
	}
}