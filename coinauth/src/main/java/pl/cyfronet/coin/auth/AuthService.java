/*
 * Copyright 2012 ACC CYFRONET AGH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package pl.cyfronet.coin.auth;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.auth.annotation.Public;
import pl.cyfronet.coin.auth.annotation.Role;
import pl.cyfronet.coin.auth.mi.MasterInterfaceAuthClient;
import pl.cyfronet.coin.auth.mi.UserDetails;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AuthService extends TimerTask {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthService.class);

	private MasterInterfaceAuthClient authClient;

	private Map<String, UserDetails> cache = new HashMap<String, UserDetails>();

	private long cacheInterval = 5000;

	public AuthService() {
		logger.debug("!!!IMPORTANT!!! application is started in debug mode. Tickets will be available in log file !!!IMPORTANT!!!");
	}

	public boolean authenticate(String ticket) {
		return getUserDetails(ticket) != null;
	}

	private boolean isPublic(Method method) {
		return method.getAnnotation(Public.class) != null;
	}

	public boolean authorize(String ticket, Method method) {
		if (!isPublic(method)) {
			Role role = method.getAnnotation(Role.class);

			if (role != null) {
				String[] requiredRoles = role.values();
				List<String> userRoles = getUserDetails(ticket).getRole();
				return userRoles.containsAll(Arrays.asList(requiredRoles));
			}
		}

		return true;
	}

	public UserDetails getUserDetails(String ticket) {
		logger.debug("Getting user details for {}", ticket);
		UserDetails details = cache.get(ticket);
		if (cache.containsKey(ticket)) {
			logger.debug("Getting cached user details: {}", details);
			return details;
		} else if (ticket != null && !"".equals(ticket)) {
			try {
				logger.debug("Getting user data for {}", ticket);
				details = authClient.validate(ticket);
				logger.debug("User details {}", details);
			} catch (WebApplicationException e) {
				// wrong user ticket or service is down in the feature
				// distinguish between these two situations
				logger.debug("unable to connect to master interface", e);
			}
			synchronized (cache) {
				cache.put(ticket, details);
			}
		}
		logger.debug("Returning user details {}", details);
		return details;
	}

	@Override
	public void run() {
		cleanCache();
	}

	private void cleanCache() {
		synchronized (cache) {
			logger.debug("Clearing AuthService cache");
			for (Iterator<Map.Entry<String, UserDetails>> it = cache.entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry<String, UserDetails> entry = it.next();
				UserDetails details = entry.getValue();
				if (shouldRemove(details)) {
					it.remove();
				}
			}
			logger.debug("{} entries in cache after cache cleared", cache.size());
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
