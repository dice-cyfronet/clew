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

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.SecurityPolicy;

public class ListSecurityPoliciesAction extends AirAction<List<String>> {

	ListSecurityPoliciesAction(AirClient air) {
		super(air);
	}

	@Override
	public List<String> execute() throws CloudFacadeException {
		List<SecurityPolicy> policies = getAir().getSecurityPolicies();
		List<String> policyNames = new ArrayList<String>();
		for (SecurityPolicy policy : policies) {
			policyNames.add(policy.getPolicy_name());
		}
		return policyNames;
	}

	@Override
	public void rollback() {
		// readonly action - no rollback needed.
	}

}
