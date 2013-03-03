package pl.cyfronet.coin.portlet.lobcder;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpMethod;

public class LobcderInputStream {
	private HttpMethod httpMethod;
	
	public LobcderInputStream(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}
	
	public InputStream getInputStream() throws IOException {
		return httpMethod.getResponseBodyAsStream();
	}
	
	public void close() {
		httpMethod.releaseConnection();
	}
}