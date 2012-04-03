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

package pl.cyfronet.coin.impl.security.mi;

import org.apache.geronimo.mail.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.impl.security.AuthenticationHandler;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class MasterInterfaceAuthenticationHandler implements
		AuthenticationHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(MasterInterfaceAuthenticationHandler.class);

	private String ipAddress;

	private ModAuthTkt modAuthTkt;

	@Override
	public boolean isAuthenticated(String username, String password) {
		try {
			return modAuthTkt.validateTicket(unwrap(password), ipAddress, 0,
					System.currentTimeMillis()) == TicketAuthStatus.VALID;
		} catch (Exception e) {
			logger.warn("Exception thrown while validating ticket", e);
			return false;
		}
	}

	@Override
	public String getUsername(String username, String password) {
		try {
			String ticket = unwrap(password);
			UserInfo userInfo = new UserInfo(ticket);
			return userInfo.getUserId();
		} catch (Exception e) {
			logger.warn("Wrong ticket format", e);
			return null;
		}
	}

	private String unwrap(String ticket) {
		return new String(Base64.decode(ticket));
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @param modAuthTkt the modAuthTkt to set
	 */
	public void setModAuthTkt(ModAuthTkt modAuthTkt) {
		this.modAuthTkt = modAuthTkt;
	}
}
