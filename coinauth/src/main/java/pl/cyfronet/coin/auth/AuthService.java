package pl.cyfronet.coin.auth;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthService extends TimerTask {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthService.class);

	private MasterInterfaceAuthClient authClient;

	private Map<String, UserDetails> cache = new HashMap<String, UserDetails>();

	private long cacheInterval = 5000;

	public boolean isValid(String ticket) {
		return getUserDetails(ticket) != null;
	}

	public UserDetails getUserDetails(String ticket) {
		logger.debug("Getting user details for {}", ticket);
			UserDetails details = cache.get(ticket);
			if (cache.containsKey(ticket)) {
				return details;
			} else if (ticket != null && !"".equals(ticket)) {
				try {
					details = authClient.validate(ticket);
					logger.debug("User details for {} - {}", ticket, details);
				} catch (WebApplicationException e) {
					// wrong user ticket or service is down in the feature
					// distinguish between these two situations
					logger.debug("unable to connect to master interface", e);
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
			for (Iterator<Map.Entry<String, UserDetails>> it = cache.entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry<String, UserDetails> entry = it.next();
				UserDetails details = entry.getValue();
				if (shouldRemove(details)) {
					it.remove();
				}
			}
		}
	}

	private boolean shouldRemove(UserDetails details) {
		long current = System.currentTimeMillis();
		return details == null
				|| details.getCreationTime() + cacheInterval < current;
	}

	public void setAuthClient(MasterInterfaceAuthClient authClient) {
		this.authClient = authClient;
	}

	public void setCacheInterval(long cacheInterval) {
		this.cacheInterval = cacheInterval;
	}
}
