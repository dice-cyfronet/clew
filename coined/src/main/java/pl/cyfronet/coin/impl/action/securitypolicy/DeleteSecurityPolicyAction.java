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

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.impl.action.ownedpayload.DeleteOwnedPayloadAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class DeleteSecurityPolicyAction extends DeleteOwnedPayloadAction {

	public DeleteSecurityPolicyAction(AirClient air, String username,
			String policyName) {
		super(air, username, policyName);
	}

	@Override
	protected void deleteOwnedPayload(String username, String ownedPayloadName) {
		getAir().deleteSecurityPolicy(username, ownedPayloadName);
	}

	@Override
	protected NewSecurityPolicyAction getNewOwnedPayloadAction(String username,
			NamedOwnedPayload ownedPayload) {
		return new NewSecurityPolicyAction(getAir(), username, ownedPayload);
	}
	
	@Override
	protected GetSecurityPolicyAction getOwnedPayloadAction(String username, String ownedPolicyName) {
		return new GetSecurityPolicyAction(
				getAir(), null, ownedPolicyName);
	}
}
