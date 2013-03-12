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
package pl.cyfronet.coin.impl.action.securitypolicy;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class DeleteSecurityPolicyAction extends AirAction<Class<Void>> {

	private String policyName;
	private String username;

	private NamedOwnedPayload payload;

	public DeleteSecurityPolicyAction(AirClient air, String username,
			String policyName) {
		super(air);
		this.username = username;
		this.policyName = policyName;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		loadOldPolicyPayload();
		try {
			getAir().deleteSecurityPolicy(username, policyName);
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 404) {
				throw new NotAllowedException();
			}
			throw new CloudFacadeException(
					"Error while deleting security policy from Air, response code"
							+ e.getStatus());
		}

		return Void.TYPE;
	}

	@Override
	public void rollback() {
		try {
			if (payload != null) {
				NewSecurityPolicyAction action = new NewSecurityPolicyAction(
						getAir(), username, payload);
				action.execute();
			}
		} catch (Exception e) {
			// best effort.
		}
	}

	private void loadOldPolicyPayload() throws NotFoundException {
		GetSecurityPolicyAction getPolicyAction = new GetSecurityPolicyAction(
				getAir(), null, policyName);
		payload = getPolicyAction.execute();
	}
}
