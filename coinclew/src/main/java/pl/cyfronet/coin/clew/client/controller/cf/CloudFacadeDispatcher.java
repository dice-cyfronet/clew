package pl.cyfronet.coin.clew.client.controller.cf;

import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Method;

import pl.cyfronet.coin.clew.client.DevelopmentProperties;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Window;

public class CloudFacadeDispatcher implements Dispatcher {
	public static final CloudFacadeDispatcher INSTANCE = new CloudFacadeDispatcher();
	
	@Override
	public Request send(Method method, RequestBuilder builder) throws RequestException {
		//trying to retrieve MI token, if it is not there falling back to private key
		MiTicketReader ticketReader = new MiTicketReader();
		String ticket = ticketReader.getTicket();
		
		if (ticket == null) {
			if(Window.Location.getParameter("private_token") != null) {
				builder.setHeader("PRIVATE-TOKEN", Window.Location.getParameter("private_token"));
			} else if (ticketReader.getCfToken().equals(DevelopmentProperties.MISSING) || ticketReader.getUserLogin().equals(DevelopmentProperties.MISSING)) {
				builder.getCallback().onError(null, new IllegalArgumentException("Authentication token is missing"));
				
				return null;
			} else {
				builder.setHeader("PRIVATE-TOKEN", ticketReader.getCfToken());
			}
		} else {
			builder.setHeader("MI-TICKET", ticket);
		}
		
		return builder.send();
	}
}