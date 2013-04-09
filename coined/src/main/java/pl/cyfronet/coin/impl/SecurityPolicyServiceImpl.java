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

import javax.ws.rs.core.Response;

import pl.cyfronet.coin.api.OwnedPayloadService;
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
		OwnedPayloadService {

	private ActionFactory actionFactory;

	@Public
	@Override
	public List<String> list() {
		Action<List<String>> action = actionFactory.getPoliciesActionFactory()
				.createListAction();
		return action.execute();
	}

	@Public
	@Override
	public NamedOwnedPayload get(String name) throws NotFoundException {
		Action<NamedOwnedPayload> action = actionFactory
				.getPoliciesActionFactory().createGetAction(name);
		return action.execute();
	}

	@Public
	@Override
	public String getPayload(String name) {
		Action<String> action = actionFactory.getPoliciesActionFactory()
				.createGetPayloadAction(name);
		return action.execute();
	}

	@Override
	public Response create(NamedOwnedPayload ownedPayload)
			throws AlreadyExistsException {
		Action<Class<Void>> action = actionFactory.getPoliciesActionFactory()
				.createNewAction(getUsername(), ownedPayload);
		action.execute();

		return Response.status(Response.Status.CREATED).build();
	}

	@Override
	public Response update(String name, OwnedPayload ownedPayload)
			throws AlreadyExistsException {
		Action<Class<Void>> action = actionFactory.getPoliciesActionFactory()
				.createUpdateAction(getUsername(), name, ownedPayload);
		action.execute();

		return Response.ok().build();
	}

	@Override
	public Response delete(String name) {
		Action<Class<Void>> action = actionFactory.getPoliciesActionFactory()
				.createDeleteAction(getUsername(), name);
		action.execute();

		return Response.ok().build();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
}
