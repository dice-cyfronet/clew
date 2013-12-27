package pl.cyfronet.coin.clew.client.controller.cf;

import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Method;

import pl.cyfronet.coin.clew.client.auth.MiTicketReader;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;

public class CloudFacadeDispatcher implements Dispatcher {
	public static final CloudFacadeDispatcher INSTANCE = new CloudFacadeDispatcher();
	
	@Override
	public Request send(Method method, RequestBuilder builder) throws RequestException {
		//trying to retrieve MI token, if it is not there falling back to private key
		MiTicketReader ticketReader = new MiTicketReader();
		String ticket = ticketReader.getTicket();
		
		if (ticket == null) {
			builder.setHeader("PRIVATE-TOKEN", ticketReader.getCfToken());
		} else {
			builder.setHeader("MI-TICKET", ticket);
		}
		
		return builder.send();
	}
}