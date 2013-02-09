/*
 * Copyright 2013 ACC CYFRONET AGH
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

import pl.cyfronet.coin.api.GrantService;
import pl.cyfronet.coin.api.beans.Grant;
import pl.cyfronet.coin.api.exception.GrantAlreadyExistException;
import pl.cyfronet.coin.api.exception.GrantNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GrantServiceImpl implements GrantService {

	private ActionFactory actionFactory;

	@Override
	public List<String> listGrants() {
		Action<List<String>> action = actionFactory.createListGrantsAction();
		return action.execute();
	}

	@Override
	public Grant getGrant(String grantName) {
		Action<Grant> action = actionFactory.createGetGrantAction(grantName);
		return action.execute();
	}

	@Override
	public void updateGrant(String grantName, Grant grant, boolean overwrite)
			throws GrantAlreadyExistException {
		Action<Class<Void>> action = actionFactory.createUpdateGrantAction(
				grantName, grant, overwrite);
		action.execute();
	}

	@Override
	public void deleteGrant(String grantName) throws GrantNotFoundException {
		Action<Class<Void>> action = actionFactory
				.createDeleteGrantAction(grantName);
		action.execute();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
}
