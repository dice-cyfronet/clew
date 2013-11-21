package pl.cyfronet.coin.clew.client.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Singleton;
import com.googlecode.gwt.crypto.bouncycastle.util.encoders.Base64;

@Singleton
public class MiTicketReader {
	private static final String DEVELOPER_ROLE = "developer";
	private static final String ROLE_DELIMITER = ",";

	public boolean isDeveloper() {
		if (getTicket() == null) {
			//not in an MI environment, overriding development mode
			return true;
		}
		
		List<String> roles = getRoles();
		
		return roles.contains(DEVELOPER_ROLE);
	}

	public String getTicket() {
		return Cookies.getCookie("vph-tkt");
	}
	
	private List<String> getRoles() {
		List<String> result = new ArrayList<String>();
		String ticket = getTicket();
		String decoded = new String(Base64.decode(ticket));
		
		RegExp regexp = RegExp.compile(".*tokens=(.*?);.*");
		MatchResult matchResult = regexp.exec(decoded);
		
		if (matchResult.getGroupCount() > 1) {
			String roles = matchResult.getGroup(1);
			String[] splitRoles = roles.split(ROLE_DELIMITER);
			result.addAll(Arrays.asList(splitRoles));
		}
		
		return result;
	}
}