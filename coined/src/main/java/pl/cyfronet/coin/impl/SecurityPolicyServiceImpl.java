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
import pl.cyfronet.coin.auth.annotation.Public;
import pl.cyfronet.coin.auth.annotation.Role;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class SecurityPolicyServiceImpl implements SecurityPolicyService {

	private ActionFactory actionFactory;

	@Public
	@Override
	public String getSecurityPolicy(String policyName) {
		Action<String> action = actionFactory
				.createGetSecurityPolicyAction(policyName);
		return action.execute();
	}

	@Public
	@Override
	public List<String> getPoliciesNames() {
		Action<List<String>> action = actionFactory
				.createListSecurityPoliciesAction();
		return action.execute();
	}

	@Role(values = "admin")
	@Override
	public void updateSecurityPolicy(String policyName, String policyContent,
			boolean overwrite) {
		Action<Class<Void>> action = actionFactory
				.createUploadSecurityPolicyAction(policyName, policyContent,
						overwrite);
		action.execute();
	}

	@Role(values = "admin")
	@Override
	public void deleteSecurityPolicy(String policyName) {
		Action<Class<Void>> action = actionFactory
				.createDeleteSecurityPolicyAction(policyName);
		action.execute();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
}
