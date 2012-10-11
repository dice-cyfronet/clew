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
package pl.cyfronet.coin.impl.action;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.SecurityPolicyNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetSecurityPolicyAction extends AirAction<String> {

	private String policyName;

	GetSecurityPolicyAction(AirClient air, String policyName) {
		super(air);
		this.policyName = policyName;
	}

	@Override
	public String execute() throws CloudFacadeException {
		try {
			return getAir().getSecurityPolicy(policyName);
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 400) {
				throw new SecurityPolicyNotFoundException();
			}
			throw new CloudFacadeException(
					"Error while geting security policy from Air, response code"
							+ e.getStatus());
		}
	}

	@Override
	public void rollback() {
		// read only action - no rollback needed.
	}

}
