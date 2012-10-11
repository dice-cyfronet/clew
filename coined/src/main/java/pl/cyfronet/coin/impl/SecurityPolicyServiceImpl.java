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

import java.util.List;

import pl.cyfronet.coin.api.SecurityPolicyService;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.DeleteSecurityPolicyAction;
import pl.cyfronet.coin.impl.action.GetSecurityPolicyAction;
import pl.cyfronet.coin.impl.action.ListSecurityPoliciesAction;
import pl.cyfronet.coin.impl.action.UploadSecurityPolicyAction;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class SecurityPolicyServiceImpl implements SecurityPolicyService {

	private ActionFactory actionFactory;

	@Override
	public String getSecurityPolicy(String policyName) {
		GetSecurityPolicyAction action = actionFactory
				.createGetSecurityPolicyAction(policyName);
		return action.execute();
	}

	@Override
	public List<String> getPoliciesNames() {
		ListSecurityPoliciesAction action = actionFactory
				.createListSecurityPoliciesAction();
		return action.execute();
	}

	@Override
	public void updateSecurityPolicy(String policyName, String policyContent,
			boolean overwrite) {
		UploadSecurityPolicyAction action = actionFactory
				.createUploadSecurityPolicyAction(policyName, policyContent,
						overwrite);
		action.execute();
	}

	@Override
	public void deleteSecurityPolicy(String policyName) {
		DeleteSecurityPolicyAction action = actionFactory
				.createDeleteSecurityPolicyAction(policyName);
		action.execute();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
}
