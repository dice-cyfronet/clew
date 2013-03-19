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

package pl.cyfronet.coin.impl.action.key;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.PublicKeyInfo;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AtmosphereAndAirAction;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class DeletePublicKeyAction extends AtmosphereAndAirAction<Class<Void>> {

	private static final Logger logger = LoggerFactory
			.getLogger(DeletePublicKeyAction.class);

	private String keyId;

	private String publicKeyContent;

	private String keyName;

	public DeletePublicKeyAction(ActionFactory actionFactory, String username,
			String keyId) {
		super(actionFactory, username);
		this.keyId = keyId;
	}

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.impl.action.Action#execute()
	 */
	@Override
	public Class<Void> execute() throws CloudFacadeException {
		publicKeyContent = getActionFactory().createGetPublicKeyAction(
				getUsername(), keyId).execute();
		keyName = getKeyName();
		try {
			getAtmosphere().removeKeyPair(keyId);
			getAir().deletePublicKey(getUsername(), keyId);
		} catch (Exception e) {
			logger.warn("Error while contacting AiR", e);
			throw new CloudFacadeException("Error while contacting AiR");
		}

		return Void.TYPE;
	}

	private String getKeyName() {
		List<PublicKeyInfo> keys = getActionFactory().createListUserKeysAction(
				getUsername()).execute();
		for (PublicKeyInfo userKeyInfo : keys) {
			if (keyId.equals(userKeyInfo.getId())) {
				return userKeyInfo.getKeyName();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.impl.action.Action#rollback()
	 */
	@Override
	public void rollback() {
		if (keyName != null) {
			try {
				Action<String> action = getActionFactory()
						.createAddPublicKeyAction(getUsername(), keyName,
								publicKeyContent);
				action.execute();
			} catch (Exception e) {
				logger.warn("Unable to rollback delete key operation", e);
				// best effort.
			}
		}
	}

}
