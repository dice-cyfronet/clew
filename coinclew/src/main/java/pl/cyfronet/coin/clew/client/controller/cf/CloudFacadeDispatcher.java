package pl.cyfronet.coin.clew.client.controller.cf;

import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Method;

import pl.cyfronet.coin.clew.client.DevelopmentProperties;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;
import pl.cyfronet.coin.clew.client.widgets.su.SuPresenter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;

public class CloudFacadeDispatcher implements Dispatcher {
	public static final CloudFacadeDispatcher INSTANCE = new CloudFacadeDispatcher();
	
	@Override
	public Request send(Method method, RequestBuilder builder) throws RequestException {
		MiTicketReader ticketReader = new MiTicketReader();
		
		//needed for CORS requests
		builder.setIncludeCredentials(true);
		
		//if present let's pass the project indicator down to cloud facade
		if(ticketReader.getProject() != null && !ticketReader.getProject().isEmpty()) {
			builder.setHeader("PROJECT", ticketReader.getProject());
		}

		//if a csrf token was passed through the overrides let's use it
		String csrfHeaderName = ticketReader.getCsrfHeaderName();
		String csrfToken = ticketReader.getCsrfToken();
		
		if(csrfHeaderName != null && csrfToken != null) {
			if(!builder.getHTTPMethod().equalsIgnoreCase("GET")) {
				builder.setHeader(csrfHeaderName, csrfToken);
			}
		} else {
			//trying to retrieve MI token, if it is not there falling back to private key
			String ticket = ticketReader.getTicket();
			
			if(ticket == null) {
				String cfToken = ticketReader.getCfToken();
				
				if(cfToken.equals(DevelopmentProperties.MISSING) || ticketReader.getUserLogin().equals(DevelopmentProperties.MISSING)) {
					//no authentication token found, sending nonetheless
					return builder.send();
				} else {
					builder.setHeader("PRIVATE-TOKEN", cfToken);
				}
			} else {
				builder.setHeader("MI-TICKET", ticket);
			}
		}
		
		if(SuPresenter.getSuUser() != null) {
			builder.setHeader("HTTP-SUDO", SuPresenter.getSuUser());
		}
		
		return builder.send();
	}
}
