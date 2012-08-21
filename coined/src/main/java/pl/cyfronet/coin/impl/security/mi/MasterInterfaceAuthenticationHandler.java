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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.auth.AuthService;
import pl.cyfronet.coin.auth.UserDetails;
import pl.cyfronet.coin.impl.security.AuthenticationHandler;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class MasterInterfaceAuthenticationHandler implements
		AuthenticationHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(MasterInterfaceAuthenticationHandler.class);

	private AuthService authService;

	@Override
	public boolean isAuthenticated(String username, String password) {
		logger.trace("Checking if user is authenticated");
		return authService.isValid(password);
	}

	@Override
	public String getUsername(String username, String password) {
		logger.trace("Getting user username");
		UserDetails userDetails = authService.getUserDetails(password);
		if (userDetails != null) {
			return userDetails.getUsername();
		}
		return null;
	}

	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}
}
