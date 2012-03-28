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

import javax.ws.rs.core.Response;

import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class BasicRsAuthenticationHandler implements RequestHandler {

	@Autowired
	private AuthenticationHandler authenticator;

	@Override
	public Response handleRequest(Message m, ClassResourceInfo resourceClass) {
		AuthorizationPolicy policy = (AuthorizationPolicy) m
				.get(AuthorizationPolicy.class);
		if (policy != null) {
			String username = policy.getUserName();
			String password = policy.getPassword();
			if (authenticator.isAuthenticated(username, password)) {
				// let request to continue
				return null;
			}
		}
		// authentication failed, request the authetication, add the
		// realm name if needed to the value of WWW-Authenticate
		return Response.status(401).header("WWW-Authenticate", "Basic").build();
	}

	/**
	 * @param authenticator the authenticator to set
	 */
	public void setAuthenticator(AuthenticationHandler authenticator) {
		this.authenticator = authenticator;
	}

}
