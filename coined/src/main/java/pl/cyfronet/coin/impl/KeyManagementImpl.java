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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.KeyManagement;
import pl.cyfronet.coin.api.beans.PublicKeyInfo;
import pl.cyfronet.coin.api.exception.KeyAlreadyExistsException;
import pl.cyfronet.coin.api.exception.KeyNotFoundException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AddPublicKeyAction;
import pl.cyfronet.coin.impl.action.DeletePublicKeyAction;
import pl.cyfronet.coin.impl.action.GetPublicKeyAction;
import pl.cyfronet.coin.impl.action.ListUserKeysAction;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class KeyManagementImpl extends UsernameAwareService implements
		KeyManagement {

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(KeyManagementImpl.class);

	private ActionFactory actionFactory;

	@Override
	public List<PublicKeyInfo> list() {
		String username = getUsername();
		logger.debug("Listing keys for {}", username);
		ListUserKeysAction action = actionFactory
				.createListUserKeysAction(username);
		return action.execute();
	}

	@Override
	public String add(String keyName, String publicKey)
			throws KeyAlreadyExistsException {
		String username = getUsername();
		logger.debug("Adding new key {} for {}", keyName, username);
		AddPublicKeyAction action = actionFactory.createAddPublicKeyAction(
				username, keyName, publicKey);
		return action.execute();
	}

	@Override
	public void delete(String keyId) throws KeyNotFoundException {
		String username = getUsername();
		logger.debug("Adding key {} for {}", keyId, username);
		DeletePublicKeyAction action = actionFactory
				.createDeletePublicKeyAction(username, keyId);
		action.execute();
	}

	@Override
	public String get(String keyId) throws KeyNotFoundException {
		String username = getUsername();
		logger.debug("Getting key {} for {}", keyId, username);
		GetPublicKeyAction action = actionFactory.createGetPublicKeyAction(
				username, keyId);
		return action.execute();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}

}
