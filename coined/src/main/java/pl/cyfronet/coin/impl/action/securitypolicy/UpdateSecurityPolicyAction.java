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

import pl.cyfronet.coin.api.beans.OwnedPayload;
import pl.cyfronet.coin.impl.action.ownedpayload.UpdateOwnedPayloadAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class UpdateSecurityPolicyAction extends UpdateOwnedPayloadAction {

	public UpdateSecurityPolicyAction(AirClient air, String username,
			String policyName, OwnedPayload ownedPayload) {
		super(air, username, policyName, ownedPayload);
	}

	@Override
	protected GetSecurityPolicyAction getOwnedPayloadAction(
			String ownedPolicyName) {
		return new GetSecurityPolicyAction(getAir(), null, ownedPolicyName);
	}

	@Override
	protected void updateOwnedPayload(String username, String ownedPayloadName,
			List<String> owners, String ownedPayloadToUpdate) {
		getAir().updateSecurityPolicy(username, ownedPayloadName,
				ownedPayloadToUpdate, owners);
	}
}
