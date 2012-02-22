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

package pl.cyfronet.coin.impl.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
public class AuthenticationHandler {

	/**
	 * Map of allowed users to this system with their corresponding passwords.
	 */
	private Map<String, String> users;
	
	public boolean isAuthenticated(String username, String password) {
		String realPassword = users.get(username);
		return realPassword != null && realPassword.equals(password);
	}
	
	/**
	 * Set user names and passwords.
	 * @param users Information about authorized users.
	 */
	@Required
	public void setUsers(Map<String, String> users) {
		this.users = users;
	}
}
