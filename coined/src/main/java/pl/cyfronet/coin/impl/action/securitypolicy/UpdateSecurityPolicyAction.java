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

import java.util.List;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.beans.OwnedPayload;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.impl.action.AirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class UpdateSecurityPolicyAction extends AirAction<Class<Void>> {

	private String username;
	private String policyName;
	private OwnedPayload ownedPayload;
	private OwnedPayload oldPayload;

	public UpdateSecurityPolicyAction(AirClient air, String username,
			String policyName, OwnedPayload ownedPayload) {
		super(air);
		this.policyName = policyName;
		this.ownedPayload = ownedPayload;
		this.username = username;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		loadOldPolicyPayload();
		try {
			update(ownedPayload);
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 404) {
				throw new NotAllowedException();
			}
			throw new CloudFacadeException(
					"Error while deleting security policy from Air, response code "
							+ e.getStatus());
		}

		return Void.TYPE;
	}

	private void update(OwnedPayload payload) {
		List<String> owners = payload.getOwners() == null ? oldPayload
				.getOwners() : payload.getOwners();
		String payloadToUpdate = payload.getPayload() == null ? oldPayload
				.getPayload() : payload.getPayload();
		getAir().updateSecurityPolicy(username, policyName, payloadToUpdate,
				owners);
	}

	private void loadOldPolicyPayload() {
		GetSecurityPolicyAction getPolicyAction = new GetSecurityPolicyAction(
				getAir(), policyName);
		oldPayload = getPolicyAction.execute();
	}

	@Override
	public void rollback() {
		try {
			if (oldPayload != null) {
				update(oldPayload);
			}
		} catch (Exception e) {
			// best effort.
		}
	}
}
