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
package pl.cyfronet.coin.impl;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.geronimo.mail.util.Base64;
import org.springframework.beans.factory.annotation.Required;

import pl.cyfronet.coin.auth.AuthenticationHandler;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class UsernameAwareService {

	@Context
	private HttpHeaders headers;

	private AuthenticationHandler authenticator;

	protected String getUsername() {
		String[] values = getUsernamePassword();
		String username = values[0];
		String password = values[1];

		return authenticator.getUsername(username, password);
	}

	/**
	 * @return
	 */
	private String[] getUsernamePassword() {
		String auth = headers.getRequestHeader("authorization").get(0);

		auth = auth.substring("Basic ".length());
		String[] values = new String(Base64.decode(auth)).split(":");
		return values;
	}

	protected boolean hasRole(String role) {
		String[] values = getUsernamePassword();
		String username = values[0];
		String password = values[1];

		return authenticator.hasRole(role, username, password);
	}

	/**
	 * @param authenticator the authenticator to set
	 */
	@Required
	public void setAuthenticator(AuthenticationHandler authenticator) {
		this.authenticator = authenticator;
	}
}
