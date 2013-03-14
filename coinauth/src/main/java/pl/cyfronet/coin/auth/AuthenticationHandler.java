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

/**
 * Authentication handler specific for concrete deployment. For example for
 * VPH-Share password is used to pass user ticket.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public interface AuthenticationHandler {

	/**
	 * Check if user is authenticated.
	 * @param username User name from basic authentication field.
	 * @param password Password from basic authentication field.
	 * @return True if user is authenticated.
	 */
	boolean isAuthenticated(String username, String password);

	/**
	 * Get user name. We need password because in some deployment (e.g.
	 * VPH-Share) all information about user is stored in ticket which is passed
	 * inside password field.
	 * @param username User name from basic authentication field.
	 * @param password Password from basic authentication field.
	 * @return User name.
	 */
	String getUsername(String username, String password);

	/**
	 * Check if user has role.
	 * @param role Role name
	 * @param username User name from basic authentication field.
	 * @param password Password from basic authentication field.
	 * @return True if user contains given role, false otherwise.
	 */
	boolean hasRole(String role, String username, String password);
}
