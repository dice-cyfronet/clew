package pl.cyfronet.coin.auth;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import javax.ws.rs.WebApplicationException;

public class AuthService extends TimerTask {

	private MasterInterfaceAuthClient authClient;

	private Map<String, UserDetails> cache = new HashMap<String, UserDetails>();

	private long cacheInterval = 5000;

	public boolean isValid(String ticket) {
		return getUserDetails(ticket) != null;
	}

	public UserDetails getUserDetails(String ticket) {
		UserDetails details = null;

		if (cache.containsKey(ticket)) {
			synchronized (cache) {
				details = cache.get(ticket);
			}
		} else if (ticket != null && !"".equals(ticket)) {
			try {
				details = authClient.validate(ticket);
			} catch (WebApplicationException e) {
				// wrong user ticket or service is down in the feature
				// distinguish between these two situations
			}
			synchronized (cache) {
				cache.put(ticket, details);
			}
		}
		return details;
	}
	
	@Override
	public void run() {
		cleanCache();
	}
	
	private void cleanCache() {
		synchronized (cache) {
			long current = System.currentTimeMillis();
			for (Iterator<Map.Entry<String, UserDetails>> it = cache.entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry<String, UserDetails> entry = it.next();
				if (entry.getValue().getCreationTime() + cacheInterval < current) {
					it.remove();
				}
			}
		}
	}
	
	public void setAuthClient(MasterInterfaceAuthClient authClient) {
		this.authClient = authClient;
	}	

	public void setCacheInterval(long cacheInterval) {
		this.cacheInterval = cacheInterval;
	}
}
