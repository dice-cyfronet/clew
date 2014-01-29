package pl.cyfronet.coin.clew.client.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.cyfronet.coin.clew.client.DevelopmentProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Singleton;

@Singleton
public class MiTicketReader {
	private static final String DEVELOPER_ROLE = "developer";
	private static final String ROLE_DELIMITER = ",";
	private DevelopmentProperties devProperties;
	
	public MiTicketReader() {
		devProperties = GWT.create(DevelopmentProperties.class);
	}

	public boolean isDeveloper() {
		if (getTicket() == null) {
			//not in an MI environment, overriding development mode
			return !devProperties.developmentUserLogin().equals(DevelopmentProperties.MISSING);
		}
		
		List<String> roles = getRoles();
		
		return roles.contains(DEVELOPER_ROLE);
	}

	public String getTicket() {
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

		return devProperties.developmentUserLogin();
	}
	
	public String getCfToken() {
		return devProperties.cloudFacadeKey();
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