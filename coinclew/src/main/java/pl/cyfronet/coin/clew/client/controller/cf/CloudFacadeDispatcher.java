package pl.cyfronet.coin.clew.client.controller.cf;

import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;

public class CloudFacadeDispatcher implements Dispatcher {
	private static final String CF_KEY = "t8YTdyd-yiAkmJx195VC";
	
	public static final CloudFacadeDispatcher INSTANCE = new CloudFacadeDispatcher();
	
	@Override
	public Request send(Method method, RequestBuilder builder) throws RequestException {
		builder.setHeader("PRIVATE-TOKEN", CF_KEY);
		
		return builder.send();
	}
}