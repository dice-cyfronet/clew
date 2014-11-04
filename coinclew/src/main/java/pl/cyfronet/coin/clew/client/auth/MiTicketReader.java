package pl.cyfronet.coin.clew.client.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.cyfronet.coin.clew.client.CloudFacadeEndpointProperty;
import pl.cyfronet.coin.clew.client.DevelopmentProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Singleton;

@Singleton
public class MiTicketReader {
	private static final String DEVELOPER_ROLE = "developer";
	private static final String ROLE_DELIMITER = ",";
	private static final String CLOUDADMIN_ROLE = "cloudadmin";
	private DevelopmentProperties devProperties;
	private CloudFacadeEndpointProperty cfProperties;
	
	public MiTicketReader() {
		devProperties = GWT.create(DevelopmentProperties.class);
		cfProperties = new CloudFacadeEndpointProperty();
	}

	public boolean isDeveloper() {
		if (getTicket() == null) {
			if(Window.Location.getParameter("developer") != null && Window.Location.getParameter("developer").equalsIgnoreCase("true")) {
				return true;
			}
			
			//not in an MI environment, overriding development mode
			return !devProperties.developmentUserLogin().equals(DevelopmentProperties.MISSING);
		}
		
		List<String> roles = getRoles();
		
		return roles.contains(DEVELOPER_ROLE);
	}

	public String getTicket() {
		if(!devProperties.ticketOverride().equals(DevelopmentProperties.MISSING)) {
			return devProperties.ticketOverride();
		}
		
		String ticket = Cookies.getCookie("vph-tkt");
		
		if (ticket != null) {
			ticket = ticket.replaceAll("\"", "");
		}
		
		return ticket;
	}
	
	public String getUserLogin() {
		String ticket = getTicket();
		
		if (ticket != null) {
			String decoded = decodeBase64(ticket);
			RegExp regexp = RegExp.compile("^uid=(.*?);.*");
			MatchResult matchResult = regexp.exec(decoded);
			
			if (matchResult.getGroupCount() > 1) {
				String login = matchResult.getGroup(1);
				
				return login;
			}
		}
		
		String login = cfProperties.getUsername();
		if(login != null) {
			return login;
		}

		return devProperties.developmentUserLogin();
	}
	
	public String getCfToken() {
		String privateToken = cfProperties.getPrivateToken();
		if(privateToken != null) {
			return privateToken;
		}
		return devProperties.cloudFacadeKey();
	}
	
	public boolean isCloudAdmin() {
		List<String> roles = getRoles();
		
		return roles.contains(CLOUDADMIN_ROLE);
	}
	
	private List<String> getRoles() {
		List<String> result = new ArrayList<String>();
		String ticket = getTicket();
		
		if (ticket != null) {
			String decoded = decodeBase64(ticket);
			
			RegExp regexp = RegExp.compile(".*tokens=(.*?);.*");
			MatchResult matchResult = regexp.exec(decoded);
			
			if (matchResult != null && matchResult.getGroupCount() > 1) {
				String roles = matchResult.getGroup(1);
				String[] splitRoles = roles.split(ROLE_DELIMITER);
				result.addAll(Arrays.asList(splitRoles));
			}
		}
		
		return result;
	}

	private native String decodeBase64(String base64String) /*-{
		return atob(base64String);
	}-*/;
}