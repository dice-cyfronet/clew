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
import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.beans.OwnedPayload;
import pl.cyfronet.coin.api.exception.AlreadyExistsException;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.auth.annotation.Public;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class SecurityPolicyServiceImpl extends UsernameAwareService implements
		SecurityPolicyService {

	private ActionFactory actionFactory;

	@Public
	@Override
	public List<String> list() {
		Action<List<String>> action = actionFactory
				.createListSecurityPoliciesAction();
		return action.execute();
	}

	@Public
	@Override
	public NamedOwnedPayload get(String name) throws NotFoundException {
		Action<NamedOwnedPayload> action = actionFactory
				.createGetSecurityPolicyAction(name);
		return action.execute();
	}

	@Public
	@Override
	public String getPayload(String name) {
		Action<String> action = actionFactory
				.createGetSecurityPolicyPayloadAction(name);
		return action.execute();
	}

	@Override
	public void create(NamedOwnedPayload ownedPayload)
			throws AlreadyExistsException {
		Action<Class<Void>> action = actionFactory
				.createNewSecurityPolicyAction(getUsername(), ownedPayload);
		action.execute();
	}

	@Override
	public void update(String name, OwnedPayload ownedPayload)
			throws AlreadyExistsException {
		Action<Class<Void>> action = actionFactory
				.createUpdateSecurityPolicyAction(getUsername(), name,
						ownedPayload);
		action.execute();
	}

	@Override
	public void delete(String name) {
		Action<Class<Void>> action = actionFactory
				.createDeleteSecurityPolicyAction(getUsername(), name);
		action.execute();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
}
